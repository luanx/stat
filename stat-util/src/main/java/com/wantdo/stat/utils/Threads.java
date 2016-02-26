package com.wantdo.stat.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *  线程相关工具类
 * @ Date : 15/10/6
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public class Threads {

    /**
     *  sleep等待，单位为毫秒
     */
    public static void sleep(long durationMillis){
        try {
            Thread.sleep(durationMillis);
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    /**
     *  sleep等待
     */
    public static void sleep(long duration, TimeUnit unit){
        try {
            Thread.sleep(unit.toMillis(duration));
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 直接调用shutdownNow的方法，有timeout控制. 取消在workQueue中pending的任务，并中断所有阻塞函数
     */
    public static void normalShutdown(ExecutorService pool, int timeout, TimeUnit timeUnit){
        try {
            pool.shutdownNow();
            if (pool.awaitTermination(timeout, timeUnit)){
                System.err.println("Pool did not terminated");
            }
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

}
