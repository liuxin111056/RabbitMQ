package com.xuz.send;
import java.io.IOException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
public class Sender01 {
	public static void main(String[] args) throws IOException {
		ConnectionFactory factory = new ConnectionFactory();
		//RabbitMQ-Server安装在本机，所以直接用127.0.0.1
		factory.setHost("127.0.0.1");
		//创建一个连接
		Connection conn = factory.newConnection();
		//创建一个通信通道
		Channel channel = conn.createChannel();
		//定义Queue名称
		String queueName = "queue01";
		//为Channel定义queue的属性，queueName为queue名称
		channel.queueDeclare(queueName, false, false,false,null);
		String msg = "Hello World!xuzheng test!";
		//发送消息
		channel.basicPublish("", queueName, null, msg.getBytes());
		System.out.println("send message["+msg+"] to "+queueName+"success!");
		//关闭通道
		channel.close();
		//关闭连接
		conn.close();
	}
}
