package com.wantdo.stat.utils;

import com.google.common.collect.Lists;
import org.joda.time.DateTime;

import java.util.List;

/**
 * @Date : 2015-9-21
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public class DateUtilTest {

    public static void main(String[] args) throws Exception{

        DateTime end = new DateTime();
        DateTime start = end.minusDays(7);
        System.out.println(start.toString() +  " " + end.toString());

        List<String> dates = Lists.newArrayList("1", "2", "3", "4");
        String[] array = dates.toArray(new String[dates.size()]);
        System.out.println(array);


    }

}
