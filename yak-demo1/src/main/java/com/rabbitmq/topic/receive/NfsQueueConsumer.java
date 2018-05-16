package com.rabbitmq.topic.receive;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class NfsQueueConsumer implements MessageListener {
	@Override
	public void onMessage(Message message) {
		System.out.println("通知前置，接收非金融交易: " + new String(message.getBody()));
	}
}
