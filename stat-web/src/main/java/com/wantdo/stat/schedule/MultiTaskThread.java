package com.wantdo.stat.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ Date : 15/10/29
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public class MultiTaskThread  implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(QuartzClusterableJob.class.getName());

    private int index;

    public MultiTaskThread() {
    }

    public MultiTaskThread(int index) {
        this.index = index;
    }

    @Override
    public void run() {
        logger.info(Thread.currentThread().getName());
        System.out.println(Thread.currentThread().getName());
    }
}
