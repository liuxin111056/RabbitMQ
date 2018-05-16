package com.springconstructorarg.demo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Run {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

		Student student = (Student) context.getBean("student");
		System.out.println(student);

		Teacher teacher = (Teacher) context.getBean("teacher");
		System.out.println(teacher);
	}
}
