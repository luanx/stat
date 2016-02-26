package com.wantdo.stat.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * @ Date : 15/10/8
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public class JsonUtil {

    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static final String EMPTY_JSON = "{}";
    public static final String EMPTY_JSON_ARRAY = "[]";
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss SSS";

    public JsonUtil() {
    }

    /**
     * @param json        给定的字符串
     * @param token
     * @param datePattern 日期格式
     * @param <T>         要转换的目标类型
     * @return
     */
    public static <T> T fromJson(String json, TypeToken<T> token, String datePattern) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        if (StringUtils.isBlank(datePattern)) {
            datePattern = DEFAULT_DATE_PATTERN;
        }

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try {
            return gson.fromJson(json, token.getType());
        } catch (Exception e) {
            logger.error(json + " cannot change to " + token.getRawType().getName() + " Object.", e);
            return null;
        }
    }

    public static <T> T fromJson(String json, TypeToken<T> token) {
        return fromJson(json, token, null);
    }

    /**
     * @param json        给定的字符串
     * @param clazz       要转换的目标类
     * @param datePattern 日期格式
     * @param <T>         要转换的目标类型
     * @return
     */
    public static <T> T fromJson(String json, Class<T> clazz, String datePattern) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        if (StringUtils.isBlank(datePattern)) {
            datePattern = DEFAULT_DATE_PATTERN;
        }

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try {
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            logger.error(json + " cannot change to " + clazz.getName() + " Object.", e);
            return null;
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return fromJson(json, clazz, null);
    }

    /**
     * @param target     目标对象
     * @param targetType 目标对象的类型
     * @param builder
     * @return
     */
    public static String toJson(Object target, Type targetType, GsonBuilder builder) {
        if (target == null) {
            return EMPTY_JSON;
        }
        Gson gson = null;
        if (builder == null) {
            gson = new Gson();
        } else {
            gson = builder.create();
        }

        String result = EMPTY_JSON;
        try {
            if (targetType == null) {
                result = gson.toJson(target);
            } else {
                result = gson.toJson(target, targetType);
            }
        } catch (Exception e) {
            logger.warn("target " + target.getClass().getName() + " change to JSON fail", e);
            if (target instanceof Collection<?> || target instanceof Iterator<?> ||
                    target instanceof Enumeration<?> || target.getClass().isArray()) {
                result = EMPTY_JSON_ARRAY;
            }
        }
        return result;
    }

}