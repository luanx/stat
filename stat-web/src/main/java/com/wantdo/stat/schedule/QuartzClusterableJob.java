package com.wantdo.stat.schedule;

import com.wantdo.stat.service.account.UserService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Map;

/**
 * 被Spring的Quartz JobDetailBean定时执行的Job类，支持持久化到数据库实现Quartz集群
 *
 * 因为需要被持久化，不能有用XXService等不能被持久化的成员变量
 * 只能在每次调度时从QuartzJobBean注入的applicationContext中动态取出
 * @ Date : 15/10/6
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public class QuartzClusterableJob  extends QuartzJobBean{

    private static Logger logger = LoggerFactory.getLogger(QuartzClusterableJob.class.getName() + ".quartz cluster job");

    private ApplicationContext applicationContext;

    /**
     * 定时查询快递状态
     */
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        UserService userService = applicationContext.getBean(UserService.class);
        Map config = (Map) applicationContext.getBean("timerJobConfig");

        long userCount = userService.getUserCount();
        String nodeName = (String) config.get("nodeName");

        logger.info("There are {} user in database, on node {}.", userCount, nodeName);
    }

    /**
     * 从SchedulerFactoryBean注入的applicationContext.
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
