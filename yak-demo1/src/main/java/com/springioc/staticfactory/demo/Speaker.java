package com.springioc.staticfactory.demo;

/** 
 * 静态工厂类 
 * 
 */  
public class Speaker {  

	private String name ;  
	private String topic;  
	private int timeHour;  

	private static Speaker speaker = null;  

	private Speaker() {  
		this.name = "Jacke";  
		this.topic = "play football!";  
		this.timeHour = 2;  
	}

	/** 
	 * 工厂方法 
	 * @return 
	 */  
	public static Speaker getInstance() {  

		if(null==speaker){  
			speaker = new Speaker();  
		}  
		return speaker;  
	}  

	/** 
	 * 演讲 
	 */  
	public void speech() {  
		System.out.println(toString());  
	}  


	@Override  
	public String toString() {  
		return "Speaker [name=" + name + ", topic=" + topic + ", timeHour="  
				+ timeHour + "]";  
	}  
}  