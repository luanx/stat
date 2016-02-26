package com.wantdo.stat.utils;

import java.util.Collection;
import java.util.Map;

/**
 * Collections工具集.
 * 在JDK的Collections和Guava的Collections2后, 命名为Collections3.
 *
 * @Date : 2015-8-25
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public class Collections3 {

    /**
     * 判断是否为空.
     */
    public static boolean isEmpty(Collection collection) {
        return (collection == null) || collection.isEmpty();
    }

    /**
     * 判断是否为空.
     */
    public static boolean isEmpty(Map map) {
        return (map == null) || map.isEmpty();
    }

    /**
     * 判断是否为空.
     */
    public static boolean isNotEmpty(Collection collection) {
        return (collection != null) && !(collection.isEmpty());
    }

}
