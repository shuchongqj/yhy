package com.quanyan.yhy.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间处理
 *
 * Created by zhaoxp on 2015-11-6.
 */
public class TimeUtil {

	public static final long MINUTE_SECOND = 60;
	public static final long HOUR_SECOND = 60 * 60;
	public static final long DAY_SECOND = 60 * 60 * 24;

	public static String convertLong2String(long time, String pattern){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.SIMPLIFIED_CHINESE);
		Date date = new Date(time);
		return simpleDateFormat.format(date);
	}

	public static String getStartTime(long time, String pattern){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm",Locale.SIMPLIFIED_CHINESE);
		Date date = new Date(time);
		String format = simpleDateFormat.format(date);
		String arrays[] = format.split(" ");
		return arrays[1];
	}

	/**
	 *
	 * @param second long型时间
	 * @return String
	 */
	public static String getDisplayTime(long second) {
		long current = System.currentTimeMillis() / 1000;
		long offset = current - second;
		if (offset <= 0) {
			return "刚刚";
		}
		if (offset > DAY_SECOND) {
			return offset / DAY_SECOND + "天前";
		}
		if (offset > HOUR_SECOND) {
			return offset / HOUR_SECOND + "小时前";
		}
		if (offset > MINUTE_SECOND) {
			return offset / MINUTE_SECOND + "分钟前";
		}
		return offset + "秒前";
	}

	/**
	 *
	 * @param second long型时间
	 * @return String（“昨天  HH:mm:ss”，“今天 HH:mm”，“YYYY-MM-dd”）
	 */
	public static String getCommentDisplayTime(long second) {
		Calendar calendar = Calendar.getInstance();
		calendar.get(Calendar.DAY_OF_MONTH);

		Calendar commentCalendar = Calendar.getInstance();
		commentCalendar.setTimeInMillis(second * 1000);
		int dayDf = calendar.get(Calendar.DAY_OF_MONTH) - commentCalendar.get(Calendar.DAY_OF_MONTH);
		if (dayDf == 0) {
			return String.format("今天 %02d:%02d", commentCalendar.get(Calendar.HOUR_OF_DAY),
					commentCalendar.get(Calendar.MINUTE));
		} else if (dayDf == 1) {
			return String.format("昨天 %02d:%02d", commentCalendar.get(Calendar.HOUR_OF_DAY),
					commentCalendar.get(Calendar.MINUTE));
		} else {
			return String.format("%04d-%02d-%02d", commentCalendar.get(Calendar.YEAR),
					commentCalendar.get(Calendar.MONTH), commentCalendar.get(Calendar.DAY_OF_MONTH));
		}
	}
}
