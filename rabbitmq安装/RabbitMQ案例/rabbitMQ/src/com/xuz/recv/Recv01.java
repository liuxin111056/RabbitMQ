package com.xuz.recv;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;

public class Recv01 {
	public static void main(String[] args) throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("127.0.0.1");
		Connection conn = factory.newConnection();
		Channel channel = conn.createChannel();
		String queueName = "queue01";
		channel.queueDeclare(queueName, false, false, false, null);
		//以上部分和sender一样
		//配置好获取消息得方式
		QueueingConsumer consumer =  new QueueingConsumer(channel);
		channel.basicConsume(queueName, true,consumer);
		//循环获取消息
		while(true){
			//获取消息，如果没有消息，这一步将会一直阻塞
			Delivery delivery = consumer.nextDelivery();
			String msg = new String(delivery.getBody());
			System.out.println("received message["+msg+"] from "+queueName);
		}
	}
}	
