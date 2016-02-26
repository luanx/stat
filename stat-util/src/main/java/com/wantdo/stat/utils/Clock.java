package com.wantdo.stat.utils;

import java.util.Date;

/**
 * @Date : 2015-8-24
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public interface Clock {

    static final Clock DEFAULT = new DefaultClock();

    Date getCurrentDate();

    long getCurrentTimeInMillis();

    /**
     * 默认时间提供者，返回当前的时间，线程安全。
     */
    public static class DefaultClock implements Clock {

        @Override
        public Date getCurrentDate() {
            return new Date();
        }

        @Override
        public long getCurrentTimeInMillis() {
            return System.currentTimeMillis();
        }
    }

    /**
     * 可配置的时间提供者，用于测试.
     */
    public static class MockClock implements Clock {

        private long time;

        public MockClock(Date date) {
            this.time = date.getTime();
        }

        public MockClock(long time) {
            this.time = time;
        }

        @Override
        public Date getCurrentDate() {
            return new Date(time);
        }

        @Override
        public long getCurrentTimeInMillis() {
            return time;
        }
    }

}
