package com.wantdo.stat.jms;


import com.wantdo.stat.entity.account.User;
import com.wantdo.stat.jms.advanced.AdvancedNotifyMessageListener;
import com.wantdo.stat.jms.advanced.AdvancedNotifyMessageProducer;
import com.wantdo.stat.log.LogbackListAppender;
import com.wantdo.stat.test.spring.SpringContextTestCase;
import com.wantdo.stat.utils.Threads;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;
import javax.jms.*;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext
@ContextConfiguration(locations = { "/applicationContext.xml", "/jms/applicationContext-jms-advanced.xml" })
public class JmsAdvancedTest extends SpringContextTestCase {

	@Autowired
	private AdvancedNotifyMessageProducer notifyMessageProducer;

	@Resource
	private JmsTemplate advancedJmsTemplate;

	@Resource
	private Destination advancedNotifyTopic;

	@Test
	public void queueMessage() {
		Threads.sleep(1000);
		LogbackListAppender appender = new LogbackListAppender();
		appender.addToLogger(AdvancedNotifyMessageListener.class);

		User user = new User();
		user.setName("luanx");
		user.setEmail("luanx@wantdo.com");

		notifyMessageProducer.sendQueue(user);
		Threads.sleep(1000);
		assertThat(appender.getFirstMessage()).isEqualTo(
				"UserName:luanx, Email:luanx@wantdo.com, ObjectType:user");
	}

	@Test
	public void topicMessage() {
		Threads.sleep(1000);
		LogbackListAppender appender = new LogbackListAppender();
		appender.addToLogger(AdvancedNotifyMessageListener.class);

		User user = new User();
		user.setName("luanx");
		user.setEmail("luanx@wantdo.com");

		notifyMessageProducer.sendTopic(user);
		Threads.sleep(1000);
		assertThat(appender.getFirstMessage()).isEqualTo(
				"UserName:luanx, Email:luanx@wantdo.com, ObjectType:user");
	}

	@Test
	public void topicMessageWithWrongType() {
		Threads.sleep(1000);
		LogbackListAppender appender = new LogbackListAppender();
		appender.addToLogger(AdvancedNotifyMessageListener.class);

		advancedJmsTemplate.send(advancedNotifyTopic, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {

				MapMessage message = session.createMapMessage();
				message.setStringProperty("objectType", "role");
				return message;
			}
		});

		Threads.sleep(1000);
		assertThat(appender.isEmpty()).isTrue();
	}
}
