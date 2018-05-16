package com.zefun.api.utils;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;

public class MQRejectAndDontRequeueRecoverer extends
        RejectAndDontRequeueRecoverer {

    @Override
    public void recover(Message message, Throwable cause) {
        throw new MQListenerExecutionFailedException("Retry Policy Exhausted", message,
                new AmqpRejectAndDontRequeueException(cause));
    }

}
