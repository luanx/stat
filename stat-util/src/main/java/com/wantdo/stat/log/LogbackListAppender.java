package com.wantdo.stat.log;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 在List中保存日志的Appender，用于测试Logback的日志输出
 *
 * @ Date : 15/10/7
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public class LogbackListAppender extends UnsynchronizedAppenderBase<ILoggingEvent>{

    private final List<ILoggingEvent> logs = Lists.newArrayList();

    public LogbackListAppender() {
        start();
    }

    @Override
    protected void append(ILoggingEvent e) {
        logs.add(e);
    }

    /**
     * 将此appender添加到logger中
     */
    public void addToLogger(String loggerName){
        Logger logger = (Logger) LoggerFactory.getLogger(loggerName);
        logger.addAppender(this);
    }

    /**
     * 将此appender添加到logger中
     */
    public void addToLogger(Class<?> loggerClass){
        Logger logger = (Logger) LoggerFactory.getLogger(loggerClass);
        logger.addAppender(this);
    }

    /**
     * 将此appender从logger中移除
     */
    public void removeFromLogger(Class<?> loggerClass){
        Logger logger = (Logger) LoggerFactory.getLogger(loggerClass);
        logger.detachAppender(this);
    }

    /**
     * 将此appender从root logger中移除
     */
    public void removefromlogger(){
        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.detachAppender(this);
    }

    /**
     * 返回Log的数量
     */
    public int getLogsCount(){
        return logs.size();
    }

    /**
     * 判断是否有log
     */
    public boolean isEmpty(){
        return logs.isEmpty();
    }

    /**
     * 返回之前append的第一个log
     */
    public ILoggingEvent getFirstLog(){
        if (logs.isEmpty()){
            return null;
        }
        return logs.get(0);
    }

    /**
     * 返回之前append的第一个log的内容
     */
    public String getFirstMessage(){
        if (logs.isEmpty()){
            return null;
        }
        return getFirstLog().getFormattedMessage();
    }

    /**
     * 返回之前append的最后一个log
     */
    public ILoggingEvent getLastLog(){
        if (logs.isEmpty()){
            return null;
        }
        return Iterables.getLast(logs);
    }

    /**
     * 返回之前append的最后一个log的内容
     */
    public String getLastMessage(){
        if (logs.isEmpty()){
            return null;
        }
        return getLastLog().getFormattedMessage();
    }

}
