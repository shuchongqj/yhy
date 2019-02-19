package com.quanyan.pedometer.heartbeat;

import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.lidroid.xutils.util.LogUtils;
import com.quanyan.pedometer.core.PartialWakeLock;
import com.quanyan.pedometer.newpedometer.StepService;

public class HeartBeatDetectorDefault implements HeartBeatDetector, SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private long startTime_ = 0L;
    private Context mContext;

    @Override
    public void detect(Context paramContext) {
        LogUtils.i("HeatbeatDetectorDefault detect");
        mSensorManager = (SensorManager) paramContext.getSystemService(Service.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        PartialWakeLock.getInstance().acquireWakeLock(paramContext);
        HeartBeatSetting.getInstance(paramContext).checkWakeupOnTime();
        this.startTime_ = System.currentTimeMillis();
        this.mSensorManager.registerListener(this, this.mSensor, 50000);
        mContext = paramContext.getApplicationContext();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        LogUtils.i("HeatbeatDetectorDefault detect onSensorChanged");
        if (StepService.getInstance() != null && (EnvironmentDetector.isBigMotion(event.values))) {
            this.mSensorManager.unregisterListener(this);
            StepService.getInstance().setActive();
        }
        if (StepService.getInstance() != null && (EnvironmentDetector.isBigMotion(event.values))) {
            this.mSensorManager.unregisterListener(this);
            StepService.getInstance().setActive();
        }
        if (System.currentTimeMillis() - this.startTime_ <= 100L) {
            return;
        }

        this.mSensorManager.unregisterListener(this);

        if (StepService.getInstance() != null) {
            LogUtils.i("Reset alarm");
            HeartBeatSetting.getInstance(mContext).setAlarm(mContext);
        }
        PartialWakeLock.getInstance().releaseWakeLock();
        if (StepService.getInstance() != null && (EnvironmentDetector.isBigMotion(event.values))) {
            StepService.getInstance().setActive();
        }
        if (StepService.getInstance() != null && (EnvironmentDetector.isBigMotion(event.values))) {
            StepService.getInstance().setActive();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

}
