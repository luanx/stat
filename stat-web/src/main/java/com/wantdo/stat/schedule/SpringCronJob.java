package com.wantdo.stat.schedule;

import com.wantdo.stat.utils.Threads;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 使用Spring的ThreadPoolTaskScheduler执行Cron式任务的类
 *
 * @ Date : 15/10/6
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public class SpringCronJob implements  Runnable{

    private String cronExpression;

    private int shutdownTimeout = Integer.MAX_VALUE;

    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Autowired
    private ExpressScanner expressScanner;

    @PostConstruct
    public void start(){
        Validate.notBlank(cronExpression);

        threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setThreadNamePrefix("SpringCronJob");
        threadPoolTaskScheduler.initialize();

        threadPoolTaskScheduler.schedule(this, new CronTrigger(cronExpression));
    }

    @PreDestroy
    public void stop(){
        ScheduledExecutorService scheduledExecutorService = threadPoolTaskScheduler.getScheduledExecutor();
        Threads.normalShutdown(scheduledExecutorService, shutdownTimeout, TimeUnit.SECONDS);
    }

    /**
     * 定时查询快递状态
     */
    @Override
    public void run(){
        expressScanner.executeBySpringCronByJava();
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    /**
     * 设置normalShutdown的等待时间，单位秒
     */
    public void setShutdownTimeout(int shutdownTimeout) {
        this.shutdownTimeout = shutdownTimeout;
    }
}
