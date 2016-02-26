package com.wantdo.stat.schedule;

import com.wantdo.stat.service.shop.market.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ Date : 15/10/6
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */

@Component
public class AreaCountScanner {

    @Autowired
    private ReportService reportService;

    public void executeByJdk(){
        execute("jdk timer job");
    }

    public void executeBySpringCronByJava(){
        execute("spring cron job by java");
    }

    //被Spring的Quartz MethodInvokingJobDetailFactoryBean反射执行
    public void executeByQuartzLocalJob(){
        execute("quartz local job");
    }

    //被Spring的Scheduler namespace反射构造成ScheduledMethodRunnable
    public void executeBySpringCronByXml(){
        execute("spring cron job by xml");
    }

    //被Spring的Scheduler namespace反射构造成ScheduledMethodRunnable
    public void executeBySpringTimerByXml(){
        execute("spring timer job by xml");
    }

    /**
     * 定时打印当前用户数到日志
     */
    private void execute(String by){
        Logger logger = LoggerFactory.getLogger(AreaCountScanner.class.getName() + "." +by);
        reportService.areaSchedule();
    }



}
