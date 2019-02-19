package com.quanyan.pedometer.heartbeat;

import android.content.Context;

public interface HeartBeatSetter {
  void cancelAlarm(Context context);

  void checkWakeupOnTime();

  void setAlarm(Context context);
}