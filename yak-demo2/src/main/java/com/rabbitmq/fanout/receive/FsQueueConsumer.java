package com.rabbitmq.fanout.receive;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class FsQueueConsumer implements MessageListener {
	@Override
	public void onMessage(Message message) {
		System.out.println("反欺诈系统，接收金融交易: " + new String(message.getBody()));
	}
}
