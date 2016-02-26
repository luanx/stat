package com.wantdo.stat.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @Date : 2015-9-18
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public class Path {

    /**
     * 从文件路径中获取文件的后缀
     */
    public static String getSuffixFromPath(String path){
        int index = StringUtils.lastIndexOf(path, ".");
        return StringUtils.substring(path, index + 1);
    }

}
