package com.yhy.sport.service;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import com.yhy.sport.util.Const;
import com.yhy.sport.util.WakeLockUtils;

/**
 * @Description:在Android4.4以上系统直接使用计步器传感器
 * @Created by zhaolei.yang 2018-07-10 19:00
 */
public class StepCounter implements SensorEventListener {

    private Context mContext;

    private int mPreStep = 0;
    private int mCurStep = 0;

    private boolean mPaused;

    public StepCounter(Context context) {
        mContext = context;

        WakeLockUtils.getLock(context, StepService.class.getName());
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //如果手机没有重启，那么values[0]中的步行数据会一直累加，所以单词运动的时候需要做累加处理

        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {

            int counterStep = (int) event.values[0];
            if (counterStep <= 0) {
                return;
            }

            if (mPreStep == 0) {
                mCurStep = 1;
            } else if (!mPaused) {
                int delta = counterStep - mPreStep;
                mCurStep += delta;
            }

            mPreStep = counterStep;

            Log.i(Const.STEP_COUNTER_TAG, "@@@@@@@@计步器：" + mCurStep);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void start() {
        mPaused = false;

        mPreStep = 0;
        mCurStep = 0;

        Log.i(Const.STEP_COUNTER_TAG, "@@@@@@@@计步器：start");
    }

    public void pause() {
        mPaused = true;

        Log.i(Const.STEP_COUNTER_TAG, "@@@@@@@@计步器：pause");

    }

    public void resume() {
        mPaused = false;

        Log.i(Const.STEP_COUNTER_TAG, "@@@@@@@@计步器：reload");
    }

    public void stop() {
        mPaused = true;

        Log.i(Const.STEP_COUNTER_TAG, "@@@@@@@@计步器：stop");
    }


    public int getStep() {
        return mCurStep;
    }
}
