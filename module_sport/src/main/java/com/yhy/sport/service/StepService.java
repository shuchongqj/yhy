package com.yhy.sport.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.yhy.sport.util.WakeLockUtils;

/**
 * @Description:计步后台会单独开启一个进程，同时会保持屏幕熄灭后cpu正常运转
 * @Created by zhaolei.yang 2018-07-10 19:34
 */
public class StepService extends Service {

    private SensorManager mSensorManager;

    private StepCounter mStepCounter;
    private StepAccelerometer mStepAccelerometer;

    private int mSensorType = Sensor.TYPE_ACCELEROMETER;

    //传感器的采样周期，这里使用SensorManager.SENSOR_DELAY_FASTEST，如果使用SENSOR_DELAY_UI会导致部分手机后台清理内存之后传感器不记步
    private static final int SAMPLING_PERIOD_US = SensorManager.SENSOR_DELAY_FASTEST;

    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerSensor();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterSensor();
    }

    /**
     * 实现Binder接口方法
     */
    private final IStepInterface.Stub mBinder = new IStepInterface.Stub() {
        @Override
        public void walkStart() {
            if (mStepCounter != null) {
                mStepCounter.start();
            } else if (mStepAccelerometer != null) {
                mStepAccelerometer.start();
            }
        }

        @Override
        public void walkResume() {
            if (mStepCounter != null) {
                mStepCounter.resume();
            } else if (mStepAccelerometer != null) {
                mStepAccelerometer.resume();
            }

        }

        @Override
        public void walkPause() {
            if (mStepCounter != null) {
                mStepCounter.pause();
            } else if (mStepAccelerometer != null) {
                mStepAccelerometer.pause();
            }

        }

        @Override
        public void walkStop() {
            if (mStepCounter != null) {
                mStepCounter.stop();
            } else if (mStepAccelerometer != null) {
                mStepAccelerometer.stop();
            }
        }

        @Override
        public int getCurrentWalkSteps() {
            int step = 0;
            if (mStepCounter != null) {
                step = mStepCounter.getStep();
            } else if (mStepAccelerometer != null) {
                step = mStepAccelerometer.getStep();
            }

            return step;
        }
    };

    /**
     * Android4.4以后如果有step counter,优先使用计步传感器
     */
    private void registerSensor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && getStepCounterEventListener()) {
            mSensorType = Sensor.TYPE_STEP_COUNTER;
            registerStepCounter();
        } else {
            mSensorType = Sensor.TYPE_ACCELEROMETER;
            registerStepAccelerometer();
        }
    }

    private void unregisterSensor() {
        if (mStepCounter != null) {
            mSensorManager.unregisterListener(mStepCounter);
        }

        if (mStepAccelerometer != null) {
            mSensorManager.unregisterListener(mStepAccelerometer);
        }
    }

    private void registerStepCounter() {
        Sensor countSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor == null) {
            return;
        }
        mStepCounter = new StepCounter(getApplicationContext());
        mSensorManager.registerListener(mStepCounter, countSensor, SAMPLING_PERIOD_US);
    }

    private void registerStepAccelerometer() {

        if (mStepAccelerometer != null) {
            WakeLockUtils.getLock(this, StepService.class.getName());
            return;
        }
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor == null) {
            return;
        }
        mStepAccelerometer = new StepAccelerometer(getApplicationContext());
        // 获得传感器的类型，这里获得的类型是加速度传感器
        // 此方法用来注册，只有注册过才会生效，参数：SensorEventListener的实例，Sensor的实例，更新速率
        mSensorManager.registerListener(mStepAccelerometer, sensor, SAMPLING_PERIOD_US);
    }

    private boolean getStepCounterEventListener() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder.asBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
