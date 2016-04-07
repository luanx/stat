package com.wantdo.stat.utils;

import com.google.common.collect.Lists;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
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

        String dateStr = "Thu Apr 07 00:00:00 CST 2016";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date(dateStr));
        String aaa = date + " 08:30:00";
        String bbb = date + " 18:00:00";
        System.out.println(aaa +"\n" + bbb);

    }

}
