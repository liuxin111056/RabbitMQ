package com.zefun.api.listener;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;

import com.rabbitmq.client.Channel;
import com.zefun.api.service.CashierOrderService;
import com.zefun.api.utils.App;

import net.sf.json.JSONObject;

public class CashierOrderCommission implements ChannelAwareMessageListener {
	
	private static final Logger logger = Logger.getLogger(CashierOrderCommission.class);

	@Autowired
    private MessageConverter msgConverter;
	
	@Autowired
	private CashierOrderService cashierOrderService;
	
	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
        Object obj = null;
        try {
            obj = msgConverter.fromMessage(message);
            
        } catch (MessageConversionException e) {
            logger.error("CashierOrderCommission convert MQ message error.", e);
        } finally {
            long deliveryTag = message.getMessageProperties().getDeliveryTag();
            if (deliveryTag != App.DELIVERIED_TAG) {
                channel.basicAck(deliveryTag, false);
                message.getMessageProperties().setDeliveryTag(App.DELIVERIED_TAG);
                logger.info("CashierOrderCommission revice and ack msg: " + (obj == null ? message : new String((byte[]) obj)));
            }
        }
        if (obj == null) {
            return;
        }
        JSONObject json = (JSONObject) obj;
        Integer orderId = json.getInt("orderId");
        logger.info("cashier order submit, orderId: " + orderId);
        cashierOrderService.insertCashierCommission(orderId);
        
	}

}
