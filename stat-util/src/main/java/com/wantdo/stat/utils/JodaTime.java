package com.wantdo.stat.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.sql.Timestamp;

/**
 * @Date : 2015-9-19
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public class JodaTime {

    public static DateTime getCurrentDateTime(){
        return new DateTime();
    }

    public static DateTime getAppointedDateTime(int day){
        return new DateTime().minusDays(day);
    }

    public static DateTime getYesterdayStartDateTime(){
        return getAppointedDateTime(1).withTimeAtStartOfDay();
    }

    public static DateTime getYesterdayEndDateTime() {
        return getAppointedDateTime(1).millisOfDay().withMaximumValue();
    }

    /**
     * String转DateTime
     */
    public static DateTime stringToDateTime(String str){
        if (StringUtils.isEmpty(str)){
            return null;
        }
        return new DateTime(str).withZone(DateTimeZone.UTC);
    }

    /**
     * DateTime转String
     */
    public static String datetimeToString(DateTime dTime){
        return dTime.toString();
    }

    public static Long getTimeMillis(DateTime joda) {
        if (joda == null){
            return null;
        }
        return joda.getMillis();
    }

    /**
     * DateTime转TimeStamp
     */
    public static Timestamp convertToTimeStamp(DateTime joda) {
        if (joda == null){
            return null;
        }
        return new Timestamp(joda.getMillis());
    }


}
