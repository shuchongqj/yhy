package com.quanyan.pedometer.heartbeat;

import java.util.Calendar;

public class AlarmStrategy {
	public static long getAlarmTime() {
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		boolean isXiaoMi = EnvironmentDetector.isXiaoMi();
		boolean isSleeping;
		long time = 0;
		if (isXiaoMi) {
			time = 10000;
		} else {
			if(hour < 6 || hour > 22) {
				time = 3600000;//1 hour
			} else {
				time = 30000;//30s
			}
		}
		return time;
	}
}
