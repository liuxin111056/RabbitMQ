package com.springioc.instancefactory.demo;

public class AnimalFactory {

	public Animal getAnimal(String type){  //这里仅仅是去掉了static关键字
		if ("cat".equalsIgnoreCase(type)){
			return new Cat();
		} else {
			return new Dog();
		}
	}
}
