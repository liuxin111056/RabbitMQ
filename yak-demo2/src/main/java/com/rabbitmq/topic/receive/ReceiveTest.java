package com.rabbitmq.topic.receive;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ReceiveTest {

	public static void main(String[] args) {  
		@SuppressWarnings("resource")
		BeanFactory beanFactory = new ClassPathXmlApplicationContext("beans.xml");  
	} 
}
