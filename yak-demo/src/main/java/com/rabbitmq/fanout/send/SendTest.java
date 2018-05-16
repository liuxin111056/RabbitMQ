package com.rabbitmq.fanout.send;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;

public class SendTest {

	public static void main(String[] args) {

		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("rabbitmq-fanout-send.xml");
		RabbitTemplate rabbitTemplate = context.getBean("rabbitTemplate", RabbitTemplate.class);
		//往名字为leo.pay.fanout.exchange的路由里面发送数据，客户端中只要是与该路由绑定在一起的队列都会收到相关消息，这类似全频广播，发送端不管队列是谁，都由客户端自己去绑定，谁需要数据谁去绑定自己的处理队列。
		for(int i = 1; i <= 1; i++) {
			
			String str = "通联非金融交易调,例如：卡片激活" + i;
			System.out.println("生产者发送："+ str);
			//已“nfs.meituan.notice” 为routing key，发送到交换上。
			NoticeRequest head = new NoticeRequest();
			head.setOrg("00000000001");
			head.setServiceSn("111100001");
			head.setServiceId("003");
			head.setChannelId("93");
			head.setRequestTime(new Date());
			head.setServiceCode("S10010");
			BodyDemo demo = new BodyDemo();
			demo.setCardNo("625000000000");
			String requestInfoString = getJsonStringByObject(head, demo);
			
			rabbitTemplate.send("fanoutExchange", "", new Message(requestInfoString.getBytes(), new MessageProperties()));
		}
		
//		for(int i = 1; i <= 1; i++) {
//			
//			String str = "通联金融交易调,例如：取现交易" + i;
//			System.out.println("生产者发送："+ str);
//			//已“nfs.meituan.notice” 为routing key，发送到交换上。
//			rabbitTemplate.send("fanoutExchange", "", new Message(str.getBytes(), new MessageProperties()));
//		}
	
	}
//	@Test
//	public void testRabbitMq() throws Exception {
//		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
//		RabbitTemplate rabbitTemplate = context.getBean("rabbitTemplate", RabbitTemplate.class);
//		//往名字为leo.pay.fanout.exchange的路由里面发送数据，客户端中只要是与该路由绑定在一起的队列都会收到相关消息，这类似全频广播，发送端不管队列是谁，都由客户端自己去绑定，谁需要数据谁去绑定自己的处理队列。
//		for(int i = 1; i <= 10; i++) {
//			String str = "hello" + i;
//			rabbitTemplate.send("noticeExchange", "", new Message(str.getBytes(), new MessageProperties()));
//		}
//	}

	private static String getJsonStringByObject(NoticeRequest head, BodyDemo demo) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(NoticeRequest.HEAD, JSON.toJSONString(head));
		map.put(NoticeRequest.BODY, JSON.toJSONString(demo));
		String requestInfoString = JSON.toJSONString(map);
		return requestInfoString;
	}
}
