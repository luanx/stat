package com.wantdo.stat.utils;

import org.joda.time.DateTime;

/**
 * @Date : 2015-9-21
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public class JodaTimeTest {

    public static void main(String[] args) throws Exception{

        DateTime jodaYesStart = JodaTime.getYesterdayStartDateTime();
        System.out.println(jodaYesStart);

        DateTime joda = JodaTime.stringToDateTime("2015-06-24T07:17:15-07:00");
        System.out.println(JodaTime.convertToTimeStamp(joda));

    }

}
