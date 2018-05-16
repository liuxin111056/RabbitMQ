package com.xuz.work;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class Worker01 {
	private static final String TASK_QUEUE_NAME = "task_queue";
	public static void main(String[] args) throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("127.0.0.1");
		Connection conn = factory.newConnection();
		Channel channel = conn.createChannel();
		/**
		 * true:
		 * 这样设置之后，服务器收到消息后就会立刻将消息写入到硬盘，就可以防止突然服务器挂掉，而引起数据丢失了，但是服务器如果刚收到消息，还没有来得写入硬盘，就挂掉了，这样
		 * 无法避免消息得丢失。
		 */
		channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
		System.out.println("waiting for message.To exit press CTRL+C");
		channel.basicQos(1);
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(TASK_QUEUE_NAME, false,consumer);
		while(true){
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			System.out.println("Received:["+message+"] from Task");
			doWork(message);
			System.out.println("Done!");
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
		}
	}
	private static void doWork(String message) throws InterruptedException {
		for (char ch : message.toCharArray()) {
			if(ch == '.')Thread.sleep(1000);
		} 
	}
}
