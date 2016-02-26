package com.wantdo.stat.jms.simple;

import com.wantdo.stat.entity.account.User;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Destination;
import java.util.HashMap;
import java.util.Map;

/**
 * JMS用户变更消息生产者
 *
 * 使用jmsTemplate将用户变更消息分别发送到queue与topic.
 *
 * @Date : 2015-9-23
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public class NotifyMessageProducer {

    private JmsTemplate jmsTemplate;
    private Destination notifyQueue;
    private Destination notifyTopic;

    public void sendQueue(User user){
        sendMessage(user, notifyQueue);
    }

    public void sendTopic(User user){
        sendMessage(user, notifyTopic);
    }

    /**
     * 使用jmsTemplate最简便的封装convertAndSend( )发送Map类型的消息.
     */
    private void sendMessage(User user, Destination destination){
        Map map = new HashMap();
        map.put("name", user.getName());
        map.put("email", user.getEmail());

        jmsTemplate.convertAndSend(destination, map);
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
