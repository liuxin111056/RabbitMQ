package com.zefun.api.utils;

import java.lang.reflect.Field;
import java.util.Date;

import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ErrorHandler;

import com.zefun.api.service.RedisService;

public class MQErrorHandler implements ErrorHandler {

    private static final Logger logger = Logger.getLogger(MQErrorHandler.class);

    @Autowired
    private RedisService redisService;
    @Autowired
    private MessageConverter msgConverter;

    @Override
    public void handleError(Throwable cause) {
        Field mqMsgField = FieldUtils.getField(MQListenerExecutionFailedException.class, "mqMsg", true);
        if (mqMsgField != null) {
            try {
                Message mqMsg = (Message) mqMsgField.get(cause);
                Object msgObj = msgConverter.fromMessage(mqMsg);
                logger.error("handle MQ msg: " + msgObj + " failed, record it to redis.", cause);
                redisService.zadd(App.MsgErr.MQ_MSG_ERR_RECORD_KEY, new Double(new Date().getTime()), msgObj.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            logger.error("An error occurred.", cause);
        }
    }

}
