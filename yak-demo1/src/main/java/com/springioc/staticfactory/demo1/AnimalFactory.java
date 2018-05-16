package com.springioc.staticfactory.demo1;

public class AnimalFactory {
	public static Animal getAnimal(String type){
		if ("cat".equalsIgnoreCase(type)){
			return new Cat();
		} else {
			return new Dog();
		}
	}
}
