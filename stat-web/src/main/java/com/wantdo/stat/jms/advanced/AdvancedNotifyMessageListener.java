package com.wantdo.stat.jms.advanced;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * 消息的异步被动接收者
 *
 * @Date : 2015-9-23
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public class AdvancedNotifyMessageListener implements MessageListener{

    private static Logger logger = LoggerFactory.getLogger(AdvancedNotifyMessageListener.class);

    @Override
    public void onMessage(Message message) {
        try{
            MapMessage mapMessage = (MapMessage) message;

            logger.info("UserName:{}, Email:{}, ObjectType:{}", mapMessage.getString("name"),
                    mapMessage.getString("email"), mapMessage.getStringProperty("objectType"));
        } catch (Exception e){
            logger.error("处理消息时发生异常", e);
        }
    }
}
