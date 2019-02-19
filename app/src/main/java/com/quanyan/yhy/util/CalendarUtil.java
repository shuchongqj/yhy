package com.quanyan.yhy.util;

import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;

public class CalendarUtil {

    private int weeks = 0;// 用来全局控制 上一周，本周，下一周的周数变化
    private int MaxDate; // 一月最大天数
    private int MaxYear; // 一年最大天数


    /**
     * 获取本月第一天
     */
    public static Calendar getFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, 1);// 设为当前月的1号
        return calendar;
    }

    /**
     * 获取本月最后一天
     */
    public static Calendar getLastDayOfMonth(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, 1);// 设为当前月的1号
        calendar.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        calendar.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
        return calendar;
    }

    /**
     * 设置为凌晨 0 点整
     */
    public static Calendar setMidNight(Calendar cal) {
        cal.set(HOUR_OF_DAY, 0);
        cal.set(MINUTE, 0);
        cal.set(SECOND, 0);
        cal.set(MILLISECOND, 0);
        return cal;
    }


    public static Calendar setMidNight(Date date) {
        if (date == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return setMidNight(calendar);
    }

    /**
     * 获取当天凌晨的Calendar
     *
     * @param timeInMillis
     * @return
     */
    public static Calendar getCalendarWithMidNight(long timeInMillis) {
        if (timeInMillis == 0) return getCalendarWithMidNight();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        return setMidNight(calendar);
    }

    /**
     * 获取当天凌晨的Calendar
     *
     * @return
     */
    public static Calendar getCalendarWithMidNight() {
        Calendar calendar = Calendar.getInstance();
        return setMidNight(calendar);
    }

}