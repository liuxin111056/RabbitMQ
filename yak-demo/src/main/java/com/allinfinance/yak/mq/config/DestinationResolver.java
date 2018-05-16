package com.allinfinance.yak.mq.config;

public interface DestinationResolver {
	String getActualQueueName(String queueName);
}
