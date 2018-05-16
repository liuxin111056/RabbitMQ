package com.rabbitmq.fanout.receive;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import com.alibaba.fastjson.JSON;

public class NfsQueueConsumer implements MessageListener {
	@Override
	public void onMessage(Message message) {
		String jsonString = new String(message.getBody());
		System.out.println("反欺诈系统，接收非金融交易: "+ jsonString);
		Map<String,String> map = JSON.parseObject(jsonString, HashMap.class);
		NoticeRequest request = JSON.parseObject(map.get("head"), NoticeRequest.class);
		BodyDemo bodyDemo = JSON.parseObject(map.get("body"), BodyDemo.class);
		System.out.println("接收消息：request"+request.getChannelId()+","+bodyDemo.getCardNo());
		
		
//		Map<String, Object> respMap = JSON.parseObject(redbagCommonService.cancelRedbag(gitJsonInfo), HashMap.class);
//        MsgResp msgResp =  JSON.parseObject(respMap.get("msgResp").toString(),MsgResp.class);
        
//		Map<String, Object> jsonObject = JSON.parseObject(jsonString);
//		NoticeRequest request = jsonObject.getObject("head", NoticeRequest.class);
		
//		Teacher teacher = JSON.parseObject(COMPLEX_JSON_STR, new TypeReference<Teacher>() {});
		
	}
}
