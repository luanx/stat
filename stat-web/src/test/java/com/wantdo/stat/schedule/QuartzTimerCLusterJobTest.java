package com.wantdo.stat.schedule;

import com.wantdo.stat.log.LogbackListAppender;
import com.wantdo.stat.test.spring.SpringTransactionalTestCase;
import com.wantdo.stat.utils.Threads;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;


import static org.assertj.core.api.Assertions.assertThat;

/**
 * @ Date : 15/10/7
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@DirtiesContext
@ContextConfiguration(locations = {"/applicationContext.xml", "/schedule/applicationContext-quartz-timer-cluster.xml"})
public class QuartzTimerCLusterJobTest extends SpringTransactionalTestCase{

    private static LogbackListAppender appender;

    @BeforeClass
    public static void initLogger(){
        //加载测试用logger appender
        appender = new LogbackListAppender();
        appender.addToLogger(QuartzClusterableJob.class.getName() + ".quartz cluster job");
    }

    @AfterClass
    public static void removeLogger(){
        appender.removeFromLogger(QuartzClusterableJob.class);
    }

    @Test
    public void scheduleJob() throws Exception{
        //等待任务延时2秒并执行完毕
        Threads.sleep(2000);

        //验证任务已执行
        assertThat(appender.getLogsCount()).isEqualTo(1);
        assertThat(appender.getFirstMessage()).isEqualTo("There are 4 user in database, on node default.");
    }

}
