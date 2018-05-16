package com.rabbitmq.fanout.publisher;

import org.junit.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SendTest {

	@Test
	public void testRabbitMq() throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
		RabbitTemplate rabbitTemplate = context.getBean("rabbitTemplate", RabbitTemplate.class);
//		RabbitTemplate rabbitTemplate = (RabbitTemplate) LeoContext.getContext().getApplication().getBean("rabbitTemplate");
		//往名字为leo.pay.fanout.exchange的路由里面发送数据，客户端中只要是与该路由绑定在一起的队列都会收到相关消息，这类似全频广播，发送端不管队列是谁，都由客户端自己去绑定，谁需要数据谁去绑定自己的处理队列。
		for(int i = 1; i <= 10; i++) {
			String str = "hello" + i;
			rabbitTemplate.send("leo.pay.fanout.exchange", "", new Message(str.getBytes(), new MessageProperties()));
		}
	}
}
