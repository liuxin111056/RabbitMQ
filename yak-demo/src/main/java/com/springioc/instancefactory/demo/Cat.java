package com.springioc.instancefactory.demo;

public class Cat implements Animal{
	private String msg;
	//依赖注入时必须的setter方法
	public void setMsg(String msg){
		this.msg = msg;
	}
	@Override
	public void sayHello(){
		System.out.println(msg + "，喵~喵~");
	}
}
