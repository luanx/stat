package com.wantdo.stat.service.account;

/**
 *
 * Service层公用的Exception.
 * 继承自RuntimeException, 从由Spring管理事务的函数中抛出时会触发事务回滚.
 *
 * @Date : 2015-8-25
 * @From : stat
 * @Author : luanx@wantdo.com
 */

public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1401593546385403720L;

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
