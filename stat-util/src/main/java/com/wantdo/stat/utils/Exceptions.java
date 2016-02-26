package com.wantdo.stat.utils;

/**
 * 关于异常的工具类
 *
 * @Date : 2015-8-24
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public class Exceptions {

    /**
     * 将CheckedException转换为UncheckedException.
     */
    public static RuntimeException unchecked(Throwable ex) {
        if (ex instanceof RuntimeException) {
            return (RuntimeException) ex;
        } else {
            return new RuntimeException(ex);
        }
    }
}