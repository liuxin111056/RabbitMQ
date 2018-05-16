package com.zefun.api;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class AppMain {

    private static final Logger logger = Logger.getLogger(AppMain.class);

    public void start() {
        ClassPathXmlApplicationContext context = null;
        try {
        	context = new ClassPathXmlApplicationContext("classpath:app.xml");
        } catch (Exception e) {
        	logger.error("An error occurred, applicationContext will close.", e);
        	if (context != null) {
        		context.close();
        	}
        	context = null;
        	logger.error("#################closed####################");
        }
        logger.info("api_consumer is started...");
    }

    public static void main(String[] args) {
    	new AppMain().start();
    }

}
