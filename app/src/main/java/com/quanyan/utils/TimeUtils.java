package com.quanyan.utils;

/**
 * Created by Administrator on 2018/1/25.
 */

public class TimeUtils {

public static String[] generateTimes(int start ,int end) {
    int num = end-start;
    String[]times = new String[num];
    for (int i = 0; i < num; i++) {
        times[i]=formatHour(start+i)+formatMinute(i);
    }
    return times;
}
    public static String formatHour(int number) {
        //return number < 10 ? "0" + number : "" + number;
        return ""+number;
    }
    public static String formatMinute(int number) {
        //return number%2==0 ? ":00"  : ":30" ;
        return ":00";
    }
}
