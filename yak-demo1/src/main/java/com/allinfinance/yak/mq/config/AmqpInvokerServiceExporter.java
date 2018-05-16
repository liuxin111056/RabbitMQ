package com.allinfinance.yak.mq.config;

import java.lang.reflect.Method;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.aop.TargetSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationBasedExporter;
import org.springframework.remoting.support.RemoteInvocationResult;

import com.allinfinance.yak.mq.exception.RPCShouldRequeueException;
import com.allinfinance.yak.support.meta.RPCVersion;
import com.allinfinance.yak.support.utils.AmqpContextHolder;
import com.allinfinance.yak.support.utils.RpcLogHandler;
import com.allinfinance.yak.support.utils.TradeIdWorker;

/**
 * 按最简单的方式实现的基于AMQP的RPC实现
 * @author licj
 *
 */
public class AmqpInvokerServiceExporter extends RemoteInvocationBasedExporter implements MessageListener, InitializingBean {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private Object proxy;
	
	private MessageConverter messageConverter = new SimpleMessageConverter();
	
	private AmqpTemplate amqpTemplate;
	
	private String rpcVersion;
	
	private RpcLogHandler rpcLogHandler=new RpcLogHandler();
	
	@Value("#{env['forceMon'] ?:false}")
	private boolean forceMon;
	
	private String serName;
	
	@Autowired
	private  Properties env;
	
	@Override
	public void onMessage(Message message) 
	{
		Object content = messageConverter.fromMessage(message);
		
		//验证接口版本
		String clientVersion = (String)message.getMessageProperties().getHeaders().get(AmqpInvokerClientInterceptor.RPC_VERSION_HEADER);
		
		//TODO 需要按版本号规则来校验，而不是简单的比较
		if (clientVersion != null && rpcVersion != null && clientVersion.compareTo(rpcVersion) != 0)
			logger.warn("接口[{}]的服务端版本({})与客户端版本({})不一致", getServiceInterface().getCanonicalName(), rpcVersion, clientVersion);
		
		if (content instanceof RemoteInvocation)
		{
			RemoteInvocation request = (RemoteInvocation)content;
			
			
			String curr="";
			
			String org=AmqpContextHolder.getCurrentOrg();
			
			//打印服务方法调用前后日志
			String msg = null;		
			
		    String AIC_SRC_TRADE_ID=AmqpContextHolder.getAicSrcTradeId();
		    
			if(forceMon){
				curr=TradeIdWorker.getInstance().getAicTraderId();
				
				// 打印请求报文的日志
				msg = String.format("TRADE_TYPE：[%s]，AIC_TRADE_ID：[%s]，AIC_SRC_TRADE_ID：[%s]", 
						serName+" "+request.getMethodName(), curr,AIC_SRC_TRADE_ID);
				rpcLogHandler.simpleOtherTradeLog(org, msg, true, true);
			}

			//调用实际服务
			RemoteInvocationResult result = invokeAndCreateResult(request, proxy);
			
			if(forceMon){
				msg = String.format("TRADE_TYPE：[%s]，AIC_TRADE_ID：[%s]，AIC_SRC_TRADE_ID：[%s]", 
						serName+" "+request.getMethodName(), curr, AIC_SRC_TRADE_ID);
				rpcLogHandler.simpleOtherTradeLog(org, msg, false, false);
			}
			
			if (result.hasException() && result.getException() instanceof RPCShouldRequeueException)
			{
				//应用指定需要requeue，则抛出该异常。如果container上没有指定requeue-rejected="false"，则容器会reject消息并且requeue
				logger.warn("RPC服务要求requeue操作", result.getException());
				
				AmqpContextHolder.setAicSrcTradeId(null);
				throw (RPCShouldRequeueException)result.getException();
			}
			
			if (message.getMessageProperties().getHeaders().containsKey(AmqpInvokerClientInterceptor.RPC_ASYNC_HEADER))
			{
				//异步调用，不发送响应，但是要检查响应是不是有异常
				if (result.getException() != null)
					logger.warn("异步调用发生异常", result.getException());
			}
			else
			{
				AmqpContextHolder.setAicSrcTradeId(AIC_SRC_TRADE_ID);
				//发送同步调用的响应
				String replyTo = message.getMessageProperties().getReplyTo();
	
				MessageProperties mp = new MessageProperties();
				//响应消息的header以请求为基础，以支持如 spring_reply_correlation之类的header
				mp.getHeaders().putAll(message.getMessageProperties().getHeaders());
				
				Message response = messageConverter.toMessage(result, mp);
	
				amqpTemplate.send(replyTo, response);
			}
			
			AmqpContextHolder.setAicSrcTradeId(null);
		}
		else
		{
			throw new IllegalArgumentException("无效请求对象" + content);
		}
	}

	public MessageConverter getMessageConverter() {
		return messageConverter;
	}

	public void setMessageConverter(MessageConverter messageConverter) {
		this.messageConverter = messageConverter;
	}

	public AmqpTemplate getAmqpTemplate() {
		return amqpTemplate;
	}

	public void setAmqpTemplate(AmqpTemplate amqpTemplate) {
		this.amqpTemplate = amqpTemplate;
	}
	public boolean isForceMon() {
		return forceMon;
	}

	public void setForceMon(boolean forceMon) {
		this.forceMon = forceMon;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		proxy = getProxyForService();
		Class<?> serviceInterface = getServiceInterface();
		RPCVersion rpcVersionAnno = serviceInterface.getAnnotation(RPCVersion.class);
		if (rpcVersionAnno != null)
			rpcVersion = rpcVersionAnno.value();
		else
			logger.warn("服务接口[{}]没有指定@RPCVersion。", serviceInterface.getCanonicalName());
		
		rpcLogHandler.afterPropertiesSet();
		
		initMonService(env);
	}
	
	public  void initMonService(Properties env){
		if(env == null) return;
		
		String serName=getSerName();
		
		this.serName=serName;
		Set<Object>keys=env.keySet();
		
		for(Object obj : keys){
			if(obj !=null){
				String key=obj.toString();
				
				if(key.indexOf("monService")!=-1 && env.get(key).equals(serName)){
					this.forceMon=true;
					return;
				}
			}
		}
	}
	
	public String getSerName(){
		String serName=this.getService().getClass().getName();
		
		Class cls=this.getService().getClass();
		
		Method [] mths=cls.getMethods();
		
		for(int i=0;i<mths.length;i++){
			if(mths[i].getName().equals("getTargetSource")){
				try {
					TargetSource target=(TargetSource)mths[i].invoke(this.getService());
					String tmp=target.getTargetClass().getName();
					if(tmp!=null && !"".equals(tmp)){
						serName=tmp;
					}
					break;
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
		return serName;
	}
}
