package com.proxy.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonProxy implements IPerson{

	@Override
	public void sleep() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eating() {
		// TODO Auto-generated method stub
		
	}/*

	private IPerson person;

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public PersonProxy(IPerson person) {

		this.person = person;

	}

	public void eating() {

		logger.info("开始执行时间:" + new Date()");

		person.eating();

		logger.info(""执行结束时间:” + new Date()");

	}

	public void sleep() {

		logger.info("开始执行时间:" + new Date()");

		person.sleep();

		logger.info(""执行结束时间:” + new Date()");

	}
*/}
