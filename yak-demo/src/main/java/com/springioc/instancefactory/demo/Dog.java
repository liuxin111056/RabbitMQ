package com.springioc.instancefactory.demo;

public class Dog implements Animal {
	private String msg;
	//����ע��ʱ�����setter����
	public void setMsg(String msg){
		this.msg = msg;
	}
	@Override
	public void sayHello(){
		System.out.println(msg + "����~��~");
	}
}