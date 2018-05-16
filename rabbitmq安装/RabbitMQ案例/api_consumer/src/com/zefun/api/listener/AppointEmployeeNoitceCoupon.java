package com.zefun.api.listener;

import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;

import com.rabbitmq.client.Channel;
import com.zefun.api.service.RedisService;
import com.zefun.api.utils.App;
import com.zefun.api.utils.HttpClientUtil;


public class AppointEmployeeNoitceCoupon implements ChannelAwareMessageListener{
    
    @Autowired
    private MessageConverter msgConverter;
    @Autowired
    private RedisService redisService;

    private static final Logger logger = Logger.getLogger(AppointEmployeeNoitceCoupon.class);
    
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

        Object obj = null;
        try {
            obj = msgConverter.fromMessage(message);
        } catch (MessageConversionException e) {
            logger.error("convert MQ message error.", e);
        } finally {
            long deliveryTag = message.getMessageProperties().getDeliveryTag();
            if (deliveryTag != App.DELIVERIED_TAG) {
                channel.basicAck(deliveryTag, false);
                message.getMessageProperties().setDeliveryTag(App.DELIVERIED_TAG);
                logger.info("revice and ack msg: " + (obj == null ? message : new String((byte[]) obj)));
            }
        }
        if (obj == null) {
            return;
        }
        Map<?, ?> map = (Map<?, ?>) obj;
        JSONObject msg = HttpClientUtil.httpRequest(getTemplSendUrl(map.get("storeId").toString()), "POST", JSONObject.fromObject(map).toString());
        if(!msg.get("errmsg").equals("ok")){
            throw new RuntimeException("hanler message " + obj + " failed.");
        }
        
    }

    private String getTemplSendUrl(String storeId) {
        String accessToken = redisService.hget(App.Redis.STORE_WECHAT_ACCESS_TOKEN_KEY_HASH, storeId);
        return "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;
    }
}
