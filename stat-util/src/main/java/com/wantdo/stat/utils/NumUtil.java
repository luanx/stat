package com.wantdo.stat.utils;

import java.text.DecimalFormat;

/**
 * @ Date : 16/4/7
 * @ From : stat
 * @ Author : luanx@wantdo.com
 */
public class NumUtil {

    public static final String STR_FORMAT = "0000000000";

    public static String addOne(String serialNum) {
        Integer intSerial = Integer.parseInt(serialNum);
        intSerial++;
        DecimalFormat df = new DecimalFormat(STR_FORMAT);
        return df.format(intSerial);
    }

}
