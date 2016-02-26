package com.wantdo.stat.jms.advanced;

import com.wantdo.stat.entity.account.User;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

/**
 * @Date : 2015-9-23
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public class AdvancedNotifyMessageProducer {

    private JmsTemplate jmsTemplate;
    private Destination notifyQueue;
    private Destination notifyTopic;

    public void sendQueue(final User user){
        sendMessage(user, notifyQueue);
    }

    public void sendTopic(final User user){
        sendMessage(user, notifyQueue);
    }

    /**
     * 使用jmsTemplate的send/MessageCreator()发送Map类型的消息并在Message中附加属性用于消息过滤.
     */
    private void sendMessage(final User user, Destination destination){
       jmsTemplate.send(destination, new MessageCreator() {
           @Override
           public Message createMessage(Session session) throws JMSException {
               MapMessage message = session.createMapMessage();
               message.setString("name", user.getName());
               message.setString("email", user.getEmail());

               message.setStringProperty("objectType", "user");

               return message;
           }
       });
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setNotifyQueue(Destination notifyQueue) {
        this.notifyQueue = notifyQueue;
    }

    public void setNotifyTopic(Destination notifyTopic) {
        this.notifyTopic = notifyTopic;
    }
}
