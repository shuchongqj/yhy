package com.quanyan.base.util;

import android.app.AlarmManager;
import android.nfc.FormatException;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class DateUtil {
//    public static String getCreateAt(Date date) {
//        Calendar c = Calendar.getInstance();
////        if (c.get(Calendar.YEAR) - (date.getYear() + 1900) > 0) {
////            int i = c.get(Calendar.YEAR) - date.getYear();
////            return i + "年前";
////        } else if (c.get(Calendar.MONTH) - date.getMonth() > 0) {
////            int i = c.get(Calendar.MONTH) - date.getMonth();
////            return i + "月前";
////        } else
//        if (c.get(Calendar.DAY_OF_MONTH) - date.getDate() > 0) {
////            int i = c.get(Calendar.DAY_OF_MONTH) - date.getDate();
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            return simpleDateFormat.format(date);
//        } else if (c.get(Calendar.HOUR_OF_DAY) - date.getHours() > 0) {
//            int i = c.get(Calendar.HOUR_OF_DAY) - date.getHours();
//            return i + "小时前";
//        } else if (c.get(Calendar.MINUTE) - date.getMinutes() > 0) {
//            int i = c.get(Calendar.MINUTE) - date.getMinutes();
//            return i + "分钟前";
//        } else {
//            return "刚刚";
//        }
//    }

    public static long getRangeStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        if (calendar.get(Calendar.HOUR_OF_DAY) < 6) {
            calendar.add(Calendar.DATE, -1);
        }
        return calendar.getTimeInMillis();
    }

    private static final long DAY_SECONDS = 60 * 60 * 24;//超过12个小时算一天
    private static final long HOUR_SECONDS = 60 * 60;//小时
    private static final long MINUTES = 60;//分钟

    public static String getCreateAt(long second) throws FormatException {
        long dayFromData = getDayEndTimer(second);
        long dayCurrent = getDayEndTimer(System.currentTimeMillis());
        long offetDay = dayCurrent - dayFromData;
        if (offetDay > DAY_SECONDS) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return simpleDateFormat.format(new Date(second));
        } else {
            long currentSecond = System.currentTimeMillis() / 1000;
            long offset = currentSecond - (second / 1000);
            if (offset <= MINUTES) {
                return "刚刚";
            }
            if (offset >= DAY_SECONDS) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                return simpleDateFormat.format(new Date(second));
            }
            if (offset >= HOUR_SECONDS) {
                return (offset / HOUR_SECONDS) + "小时前";
            }
            if (offset >= MINUTES) {
                return offset / MINUTES + "分钟前";
            }
        }
        return "很久以前";
    }

    /**
     * 获取时间间隔
     *
     * @param timeDuration 毫秒单位
     * @return
     */
    public static String getDuration(long timeDuration) {
        long durSeconds = timeDuration / 1000;
        StringBuffer sb = new StringBuffer();
        if (durSeconds / 3600 > 0) {
            sb.append((durSeconds / 3600) + "");
            sb.append("小时");
        }
        if (((durSeconds / 60) % 60 != 0)) {
            sb.append(((durSeconds / 60) % 60) + "");
            sb.append("分钟");
        }
        return sb.toString();
    }

//    public static String getCreateAt(String strDate) {
//        if (strDate == null) {
//            return "刚刚";
//        }
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            Date startDate = simpleDateFormat.parse(strDate);
//            return getCreateAt(startDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return "刚刚";
//    }

    public static String getShorTime(String strDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat shortFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date shortDate = simpleDateFormat.parse(strDate);

            return shortFormat.format(shortDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "刚刚";
    }

    public static long getPostTimeMiilis(String strDate) {
        if (strDate == null) {
            return System.currentTimeMillis();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            Date startDate = simpleDateFormat.parse(strDate);
            return startDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static long getContentTimeMiilis(String strDate) {
        if (strDate == null) {
            return System.currentTimeMillis();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date startDate = simpleDateFormat.parse(strDate);
            return startDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getTimeDescription(Date date) {
        Calendar c = Calendar.getInstance();
        //年前年后
        int ret = c.get(Calendar.YEAR) - (date.getYear() + 1900);
        if (ret > 0) {
            int i = c.get(Calendar.YEAR) - date.getYear();
            return i + "年前";
        } else if (ret < 0) {
            int i = date.getYear() - c.get(Calendar.YEAR);
            return i + "年后";
        }

        ret = c.get(Calendar.MONTH) - date.getMonth();
        if (ret > 0) {
            return Math.abs(ret) + "个月前";
        } else if (ret < 0) {
            return Math.abs(ret) + "个月后";
        }

        ret = c.get(Calendar.DAY_OF_MONTH) - date.getDate();
        if (ret > 0) {
            return Math.abs(ret) + "天前";
        } else if (ret < 0) {
            return Math.abs(ret) + "天后";
        }

        ret = c.get(Calendar.HOUR_OF_DAY) - date.getHours();
        if (ret > 0) {
            return Math.abs(ret) + "小时前";
        } else if (ret < 0) {
            return Math.abs(ret) + "小时后";
        }

        ret = c.get(Calendar.MINUTE) - date.getMinutes();
        if (ret > 0) {
            return Math.abs(ret) + "分钟前";
        } else if (ret < 0) {
            return Math.abs(ret) + "分钟后";
        }

        return "刚刚";
    }

    public static String getDeadline(String strDate) {
        if (strDate == null) {
            return "刚刚";
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date startDate = simpleDateFormat.parse(strDate);
            return getTimeDescription(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "刚刚";
    }

    public static String getRequestDateNotTime(long millis) {
        if (millis < 0) {
            return null;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(new Date(millis));
    }

    public static String getRequestDate(long millis, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(new Date(millis));
    }

    public static String getRequestDate(long millis) {
        if (millis < 0) {
            return null;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return simpleDateFormat.format(new Date(millis));
    }

    public static Calendar longToDateTime(long time) {
        if (time == -1) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.clear();
        c.setTimeInMillis(time);
        return c;
//		return new DateTime(c.get(Calendar.YEAR),
//				c.get(Calendar.MONTH) + 1,
//				c.get(Calendar.DAY_OF_MONTH));
    }

    public static long DateTimeToLong(Calendar date) {
        if (date == null) {
            return -1;
        }
//		Calendar c = Calendar.getInstance();
//		c.clear();
//		c.set(date.getYear(), date.getMonth() - 1, date.getDayOfMonth(),0,0,0);
        return date.getTimeInMillis();
    }

    public static long getCalendar(int year, int month, int day, int hourOfDay, int minute, int second) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(year, month, day, hourOfDay, minute, second);
        return c.getTimeInMillis();
    }

    public static long getCalendar(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(year, month, day, 0, 0, 0);
        return c.getTimeInMillis();
    }

    public static int getAgeBySouth(long time) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.setTimeInMillis(System.currentTimeMillis());
        int yearNow = c.get(Calendar.YEAR);

        c.clear();
        c.setTimeInMillis(time);
        int yearBirthday = c.get(Calendar.YEAR);
        return yearNow - yearBirthday + 1;
    }

    /**
     * 计算年龄
     *
     * @param birthDay
     * @return
     * @throws Exception
     */
    public static int getAgeByNorth(long birthDay) {
        try {
            return getAge(birthDay);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
//        Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(System.currentTimeMillis());
//        int yearNow = c.get(Calendar.YEAR);
//        int monthNow = c.get(Calendar.MONTH) + 1;
//        int dayNow = c.get(Calendar.DAY_OF_MONTH);
//
//        c.setTimeInMillis(time);
//        int yearBirthday = c.get(Calendar.YEAR);
//        int monthBirthday = c.get(Calendar.MONTH) + 1;
//        int dayBirthday = c.get(Calendar.DAY_OF_MONTH);
//
//        boolean flag = true;
//        if (monthNow < dayBirthday || (monthNow == monthBirthday && dayNow <= dayBirthday)) {
//            flag = false;
//        }
//        if (flag) {
//            return yearNow - yearBirthday;
//        } else {
//            return (yearNow - yearBirthday - 1) > 0 ? (yearNow - yearBirthday - 1) : 0;
//        }
//        return yearNow - yearBirthday > 0 ? (yearNow - yearBirthday ) : 0;
    }

    /**
     * 计算年龄
     *
     * @param birthDay
     * @return
     * @throws Exception
     */
    public static int getAge(long birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTimeInMillis(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                //monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                } else {
                    //do nothing
                }
            } else {
                //monthNow>monthBirth
                age--;
            }
        } else {
            //monthNow<monthBirth
            //donothing
        }

        return age;
    }

    public static long reformTime(long time) {
        Calendar cal = Calendar.getInstance();
        long seconds = (long) (time / 1000);
        time = seconds * 1000;
        cal.setTimeInMillis(time);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return cal.getTimeInMillis();
    }

    public static String getDisplayDate(long time, String timeFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        return sdf.format(new Date(time));
    }

    public static String getWeekString(long time) {
        String weekStr = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        switch (week) {
            case 0:
                weekStr = "星期日";
                break;
            case 1:
                weekStr = "星期一";
                break;
            case 2:
                weekStr = "星期二";
                break;
            case 3:
                weekStr = "星期三";
                break;
            case 4:
                weekStr = "星期四";
                break;
            case 5:
                weekStr = "星期五";
                break;
            case 6:
                weekStr = "星期六";
                break;
        }
        return weekStr;
    }

    /**
     * 酒店格式的星期
     *
     * @param time
     * @return
     */
    public static String getHotelWeekString(long time) {
        String weekStr = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        switch (week) {
            case 0:
                weekStr = "周日";
                break;
            case 1:
                weekStr = "周一";
                break;
            case 2:
                weekStr = "周二";
                break;
            case 3:
                weekStr = "周三";
                break;
            case 4:
                weekStr = "周四";
                break;
            case 5:
                weekStr = "星期五";
                break;
            case 6:
                weekStr = "星期六";
                break;
        }
        return weekStr;
    }

    /**
     * 判断是否为今天
     *
     * @param t
     * @return
     */
    public static boolean isToday(long t) {
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(t);
        int y = c.get(Calendar.YEAR);
        int dy = c.get(Calendar.DAY_OF_YEAR);
        long time = System.currentTimeMillis();
        c.setTimeInMillis(time);
        int cy = c.get(Calendar.YEAR);
        int cdy = c.get(Calendar.DAY_OF_YEAR);
        if (y == cy && dy == cdy) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为过去
     *
     * @param t
     * @return
     */
    public static boolean isPast(long t) {
        return t < DateUtil.getDayStartTimer(System.currentTimeMillis());
    }

    /**
     * 判断是否为将来
     *
     * @param t
     * @return
     */
    public static boolean isFuture(long t) {
        return t > DateUtil.getDayEndTimer(System.currentTimeMillis());
    }

    public static String getyyyymmddhhmm(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",
                Locale.getDefault());
        return sdf.format(time);
    }


    public static String getZHyyyyMMdd(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日",
                Locale.getDefault());
        return sdf.format(time);
    }

    public static String getZHTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm",
                Locale.getDefault());
        return sdf.format(time);
    }

    /**
     * 获取月日
     *
     * @param time
     * @return
     */
    public static String getHcMMdd(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日",
                Locale.getDefault());
        return sdf.format(time);
    }

    /**
     * 获取时分
     *
     * @param time
     * @return
     */
    public static String getHchhmm(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm",
                Locale.getDefault());
        return sdf.format(time);
    }

    /**
     * 获取时分
     *
     * @param time
     * @return
     */
    public static String getHchhmmWith24(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(time);
    }

    /**
     * 获取某天的开始时间
     *
     * @param time
     * @return
     */
    public static long getDayStartTimer(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime().getTime();
    }

    /**
     * 获取某天的截止时间
     *
     * @param time
     * @return
     */
    public static long getDayEndTimer(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime().getTime();
    }

    /**
     * 计算两个时间点间隔的天数
     *
     * @param sTime
     * @param eTime
     * @return
     */
    public static int getDaysBetween(long sTime, long eTime) {
        return (int) ((sTime - eTime) / AlarmManager.INTERVAL_DAY);
    }

    public static String getyyyymmdd(long time, String spStr) {
        String p = "yyyy" + spStr + "MM" + spStr + "dd";
        SimpleDateFormat sdf = new SimpleDateFormat(p,
                Locale.getDefault());
        return sdf.format(time);
    }

    public static long getStringTimeMiilis(String strDate) {
        if (strDate == null) {
            return System.currentTimeMillis();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        try {
            Date startDate = simpleDateFormat.parse(strDate);
            return startDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取今天日期
     *
     * @param timeFormat
     * @return
     */
    public static String getTodayDate(String timeFormat) {
        if (TextUtils.isEmpty(timeFormat)) {
            return "";
        }
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(timeFormat);
        return sf.format(date);
    }

    /**
     * 得到明天日期
     *
     * @param timeFormat
     * @return
     */
    public static String getNextDayDate(String timeFormat) {
        if (TextUtils.isEmpty(timeFormat)) {
            return "";
        }
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(timeFormat);
        String nowDate = sf.format(date);
        //通过日历获取下一天日期
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sf.parse(nowDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.add(Calendar.DAY_OF_YEAR, +1);
        return sf.format(cal.getTime());
    }

    /**
     * 获取今天日期
     *
     * @param timeFormat
     * @return
     */
    public static String getTodayHotelDate(String timeFormat) {
        if (TextUtils.isEmpty(timeFormat)) {
            return "";
        }
        Date date = new Date();
        if(date.getHours() >= 6){
            date.setTime(System.currentTimeMillis());
        }else{
            date.setTime(System.currentTimeMillis() - AlarmManager.INTERVAL_DAY);
        }
        SimpleDateFormat sf = new SimpleDateFormat(timeFormat);
        return sf.format(date);
    }

    /**
     * 得到明天日期
     *
     * @param timeFormat
     * @return
     */
    public static String getNextDayHotelDate(String timeFormat) {
        if (TextUtils.isEmpty(timeFormat)) {
            return "";
        }
        Date date = new Date();
        if(date.getHours() >= 6){
            date.setTime(System.currentTimeMillis());
        }else{
            date.setTime(System.currentTimeMillis() - AlarmManager.INTERVAL_DAY);
        }
        SimpleDateFormat sf = new SimpleDateFormat(timeFormat);
        String nowDate = sf.format(date);
        //通过日历获取下一天日期
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sf.parse(nowDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.add(Calendar.DAY_OF_YEAR, +1);
        return sf.format(cal.getTime());
    }

    /**
     * 得到N天后的日期
     *
     * @return
     */
    public static String getNDayDate(int n) {
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(DateUtil.getTodayDate("yyyy-MM-dd"));

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, +n);
        return sf.format(cal.getTime());
    }

    public static long getNDayDateLong(long currentTime, int n) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(currentTime);
        cal.add(Calendar.DAY_OF_YEAR, +n);

        return cal.getTimeInMillis();
    }

    public static long getNDayDateLong(int n) {
        return getNDayDateLong(System.currentTimeMillis(), n);
    }

    public static long convert2long(String date, String format) {
        try {
            SimpleDateFormat sf = new SimpleDateFormat(format);
            return sf.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0l;
    }

    /**
     * 将长整型数字转换为日期格式的字符串
     *
     * @param time
     * @param format
     * @return
     */
    public static String convert2String(long time, String format) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        Date date = new Date(time);
        return sf.format(date);
    }

    /**
     * 计算日期相差几天
     */
    public static String differDay(long timeStart, long timeEnd) {
        String str1 = DateUtil.convert2String(timeStart, "yyyMMdd");//"yyyyMMdd"格式 如 20131022
        String str2 = DateUtil.convert2String(timeEnd, "yyyMMdd");//"yyyyMMdd"格式 如 20131022


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");//输入日期的格式
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(str1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            date2 = simpleDateFormat.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        GregorianCalendar cal1 = new GregorianCalendar();
        GregorianCalendar cal2 = new GregorianCalendar();
        cal1.setTime(date1);
        cal2.setTime(date2);
        double dayCount = (cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (1000 * 3600 * 24);//从间隔毫秒变成间隔天数
        return (int) dayCount + "";
    }

    /**
     * 获取一段区间内的所有事件
     *
     * @param startTime
     * @param endTime
     * @param format
     * @return
     */
    public static List<Date> getAllDifferDays(long startTime, long endTime, String format) {
        String str1 = DateUtil.convert2String(startTime, format);//"yyyyMMdd"格式 如 20131022
        String str2 = DateUtil.convert2String(endTime, format);//"yyyyMMdd"格式 如 20131022
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date dBegin = null;
        Date dEnd = null;
        try {
            dBegin = sdf.parse(str1);
            dEnd = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<Date> lDate = new ArrayList<Date>();
        lDate.add(dBegin);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(dEnd);
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(calBegin.getTime());
        }
        return lDate;
    }
}
