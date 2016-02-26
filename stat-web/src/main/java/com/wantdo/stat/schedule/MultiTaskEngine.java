package com.wantdo.stat.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.TimerTask;

/**
 * @ Date : 15/10/29
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public class MultiTaskEngine extends TimerTask {

    private ThreadPoolTaskExecutor threadPool;

    @Override
    public void run() {
        System.out.println("hi");
        for(int i=0; i<200; i++){
            threadPool.execute(new MultiTaskThread(i));
        }
    }

    public ThreadPoolTaskExecutor getThreadPool() {
        return threadPool;
    }

    @Autowired
    public void setThreadPool(ThreadPoolTaskExecutor threadPool) {
        this.threadPool = threadPool;
    }
}
