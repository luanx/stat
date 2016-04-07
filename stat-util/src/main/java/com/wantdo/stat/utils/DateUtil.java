package com.wantdo.stat.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Date工具类
 *
 * @Date : 2015-9-28
 * @From : stat
 * @Author : luanx@wantdo.com
 */
public class DateUtil {


    /**
     * 当天的开始时间
     *
     * @return
     */
    public static long startOfToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        return date.getTime();
    }

    /**
     * 当天的结束时间
     *
     * @return
     */
    public static long endOfToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date date = calendar.getTime();
        return date.getTime();
    }


    /**
     * 当天指定的指定时间
     */
    public static long selectOfToday(int hour, int minute, int second, int millisecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, millisecond);
        Date date = calendar.getTime();
        return date.getTime();
    }

    /**
     * 昨天的开始时间
     *
     * @return
     */
    public static long startOfyesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        return date.getTime();
    }

    /**
     * 昨天的结束时间
     *
     * @return
     */
    public static long endOfyesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        calendar.add(Calendar.DATE, -1);
        Date date = calendar.getTime();
        return date.getTime();
    }

    /**
     * 功能：获取上周的开始时间
     */
    public static long startOfLastWeek() {// 当周开始时间
        return startOfThisWeek() - 7 * 24 * 60 * 60 * 1000;
    }

    /**
     * 功能：获取上周的结束时间
     */
    public static long endOfLastWeek() {// 当周开始时间
        return endOfThisWeek() - 7 * 24 * 60 * 60 * 1000;
    }

    /**
     * 功能：获取本周的开始时间 示例：2013-05-13 00:00:00
     */
    public static long startOfThisWeek() {// 当周开始时间
        Calendar currentDate = Calendar.getInstance();
        currentDate.setFirstDayOfWeek(Calendar.MONDAY);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        currentDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date date = currentDate.getTime();
        return date.getTime();
    }

    /**
     * 功能：获取本周的结束时间 示例：2013-05-19 23:59:59
     */
    public static long endOfThisWeek() {// 当周结束时间
        Calendar currentDate = Calendar.getInstance();
        currentDate.setFirstDayOfWeek(Calendar.MONDAY);
        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.MINUTE, 59);
        currentDate.set(Calendar.SECOND, 59);
        currentDate.set(Calendar.MILLISECOND, 999);
        currentDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date date = currentDate.getTime();
        return date.getTime();
    }

    /**
     * 功能：获取本月的开始时间
     */
    public static long startOfThisMonth() {// 当周开始时间
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        currentDate.set(Calendar.DAY_OF_MONTH, 1);
        Date date = currentDate.getTime();
        return date.getTime();
    }

    public static long endOfThisMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DATE, -1);
        Date date = cal.getTime();
        return date.getTime();
    }

    /**
     * 功能：获取上月的开始时间
     */
    public static long startOfLastMonth() {// 当周开始时间
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        currentDate.set(Calendar.DAY_OF_MONTH, 1);
        currentDate.add(Calendar.MONTH, -1);
        Date date = currentDate.getTime();
        return date.getTime();
    }

    /**
     * 功能：获取上月的结束时间
     */
    public static long endOfLastMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        cal.add(Calendar.DATE, -1);
        Date date = cal.getTime();
        return date.getTime();
    }

    /**
     * 功能：获取一个月前的时间
     */
    public static long beforeAMonth(){
        Calendar cal = Calendar.getInstance();
        Date currentDate = new Date();
        cal.setTime(currentDate);
        cal.add(Calendar.MONTH, -1);
        Date date = cal.getTime();
        return date.getTime();
    }

    /**
     * 根据long返回year
     *
     * @param milliseconds
     * @return
     */
    public static Object[] theYearOfTime(long milliseconds) {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        int thisYear = date.getYear() + 1900;
        cal.setTimeInMillis(milliseconds);
        date = cal.getTime();
        int regirsterYear = date.getYear() + 1900;
        if (regirsterYear < thisYear) {
            List<Integer> yearL = new ArrayList<Integer>();
            for (int i = regirsterYear; i <= thisYear; i++) {
                yearL.add(i);
            }
            return yearL.toArray();
        } else {
            return new Object[]{thisYear};
        }
    }

    /**
     * 功能：获取本年的开始时间
     */
    public static long startOfTheYear(int year) {// 当周开始时间
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(Calendar.YEAR, year);
        currentDate.set(Calendar.MONTH, 0);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        currentDate.set(Calendar.DAY_OF_MONTH, 1);
        Date date = currentDate.getTime();
        return date.getTime();
    }

    /**
     * 功能：获取本年的开始时间
     */
    public static long endOfTheYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, 11);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        Date date = cal.getTime();
        return date.getTime();
    }

    /**
     * String转Date
     */
    public static Date stringToDate(String str){
        Date date = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
            if (!StringUtils.isEmpty(str)) {
                date = simpleDateFormat.parse(str);
            }
            return date;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前日期
     */
    public static Date getDate(){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            return date;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前日期
     */
    public static String getStringDate() {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = simpleDateFormat.format(new Date());
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前日期
     */
    public static Date getDate(Date dateInput) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = simpleDateFormat.parse(simpleDateFormat.format(dateInput));
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getDateList(Date startDate, Date endDate){
        List lDate = Lists.newArrayList();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        lDate.add(simpleDateFormat.format(startDate));
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(startDate);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(endDate);
        // 测试此日期是否在指定日期之后

        while (endDate.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(simpleDateFormat.format(calBegin.getTime()));
        }
        return lDate;
    }

    /**
     * 得到几天前的日期
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateBefore(Date d, int day) throws Exception{
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(simpleDateFormat.format(now.getTime()));
        return date;
    }

    /**
     * 得到几天后的日期
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateAfter(Date d, int day) throws Exception{
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(simpleDateFormat.format(now.getTime()));
        return date;
    }

    /**
     * 获取整点时间,四舍五入
     * @param date
     * @return
     */
    public static DateTime integralPointOfDate(DateTime date){
        int minute = date.getMinuteOfHour();
        if (minute >0 && minute <= 30){
            return date;
        } else {
            return date.plusHours(1);
        }
    }

    public static Date randomDate(String beginDate, String endDate) throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = simpleDateFormat.parse(beginDate);
        Date end = simpleDateFormat.parse(endDate);
        if (start.getTime() >= end.getTime()) {
            return null;
        }
        long date = random(start.getTime(), end.getTime());
        return new Date(date);
    }

    private static long random(long begin, long end){
        long rd = begin + (long) (Math.random() * (end - begin));
        if (rd == begin || rd == end){
            return random(begin, end);
        }
        return rd;
    }

}
