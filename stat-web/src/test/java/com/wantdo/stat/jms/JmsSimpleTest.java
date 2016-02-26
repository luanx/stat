package com.wantdo.stat.jms;

import com.wantdo.stat.entity.account.User;
import com.wantdo.stat.jms.simple.NotifyMessageListener;
import com.wantdo.stat.jms.simple.NotifyMessageProducer;
import com.wantdo.stat.log.LogbackListAppender;
import com.wantdo.stat.test.spring.SpringContextTestCase;
import com.wantdo.stat.utils.Threads;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;


@DirtiesContext
@ContextConfiguration(locations = { "/applicationContext.xml", "/jms/applicationContext-jms-simple.xml" })
public class JmsSimpleTest extends SpringContextTestCase {

	@Autowired
	private NotifyMessageProducer notifyMessageProducer;


	@Test
	public void queueMessage() {
		Threads.sleep(1000);
		LogbackListAppender appender = new LogbackListAppender();
		appender.addToLogger(NotifyMessageListener.class);

		User user = new User();
		user.setName("luanx");
		user.setEmail("luanx@wantdo.com");

		notifyMessageProducer.sendQueue(user);
		logger.info("sended message");

		Threads.sleep(1000);
		assertThat(appender.getFirstMessage()).isEqualTo("UserName:luanx, Email:luanx@wantdo.com");
	}

	@Test
	public void topicMessage() {
		Threads.sleep(1000);
		LogbackListAppender appender = new LogbackListAppender();
		appender.addToLogger(NotifyMessageListener.class);

		User user = new User();
		user.setName("luanx");
		user.setEmail("luanx@wantdo.com");

		notifyMessageProducer.sendTopic(user);
		logger.info("sended message");

		Threads.sleep(1000);
		assertThat(appender.getFirstMessage()).isEqualTo("UserName:luanx, Email:luanx@wantdo.com");
	}
}
