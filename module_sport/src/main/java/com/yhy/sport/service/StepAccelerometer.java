package com.yhy.sport.service;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * @Description: 在Android4.4以下系统使用加速传感器计步（波峰波谷周期原理）
 * @Created by zhaolei.yang 2018-07-10 18:59
 */
public class StepAccelerometer implements SensorEventListener {
    private final Context mContext;

    //存放三轴数据
    private float[] mOriValues = new float[3];
    private final int mValueNum = 4;
    //用于存放计算阈值的波峰波谷差值
    private float[] mTempValue = new float[mValueNum];
    private int mTempCount = 0;
    //是否上升的标志位
    private boolean mIsDirectionUp = false;
    //持续上升次数
    private int mContinueUpCount = 0;
    //上一点的持续上升的次数，为了记录波峰的上升次数
    private int mContinueUpFormerCount = 0;
    //上一点的状态，上升还是下降
    private boolean mLastStatus = false;
    //波峰值
    private float mPeakOfWave = 0;
    //波谷值
    private float mValleyOfWave = 0;
    //此次波峰的时间
    private long mTimeOfThisPeak = 0;
    //上次波峰的时间
    private long mTimeOfLastPeak = 0;
    //当前的时间
    private long mTimeOfNow = 0;
    //当前传感器的值
    private float mGravityNew = 0;
    //上次传感器的值
    private float mGravityOld = 0;
    //动态阈值需要动态的数据，这个值用于这些动态数据的阈值
    private final float mInitialValue = (float) 1.3;
    //初始阈值
    private float mThreadValue = (float) 2.0;
    //波峰波谷时间差
    private int mTimeInterval = 400;

    private long mTimeOfLastPeak1 = 0;
    private long mTimeOfThisPeak1 = 0;

    private int mCurCount = 0;
    private boolean mPaused;

    private long mReloadTime = 0;


    public StepAccelerometer(Context context) {
        mContext = context;
    }

    public void start() {
        mCurCount = 0;
        mReloadTime = 0;
        mPaused = false;
    }

    public void pause() {
        mPaused = true;
    }

    public void resume() {
        mReloadTime = System.currentTimeMillis();
        mPaused = false;
    }

    public void stop() {
        mPaused = true;
    }

    public int getStep() {
        return mCurCount;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mPaused) {
            return;
        }

        for (int i = 0; i < 3; i++) {
            mOriValues[i] = event.values[i];
        }
        mGravityNew = (float) Math.sqrt(mOriValues[0] * mOriValues[0]
                + mOriValues[1] * mOriValues[1] + mOriValues[2] * mOriValues[2]);
        detectorNewStep(mGravityNew);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /*
     * 检测步子，并开始计步
     * 1.传入sersor中的数据
     * 2.如果检测到了波峰，并且符合时间差以及阈值的条件，则判定为1步
     * 3.符合时间差条件，波峰波谷差值大于initialValue，则将该差值纳入阈值的计算中
     * */
    private void detectorNewStep(float values) {
        if (mGravityOld == 0) {
            mGravityOld = values;
        } else {
            if (detectorPeak(values, mGravityOld)) {
                mTimeOfLastPeak = mTimeOfThisPeak;
                mTimeOfNow = System.currentTimeMillis();
                if (mTimeOfNow - mTimeOfLastPeak >= mTimeInterval
                        && (mPeakOfWave - mValleyOfWave >= mThreadValue)) {
                    mTimeOfThisPeak = mTimeOfNow;
                    /*
                     * 更新界面的处理，不涉及到算法
                     * 一般在通知更新界面之前，增加下面处理，为了处理无效运动：
                     * 1.连续记录10才开始计步
                     * 2.例如记录的9步用户停住超过3秒，则前面的记录失效，下次从头开始
                     * 3.连续记录了9步用户还在运动，之前的数据才有效
                     * */
                    countStep();
                }
                if (mTimeOfNow - mTimeOfLastPeak >= mTimeInterval
                        && (mPeakOfWave - mValleyOfWave >= mInitialValue)) {
                    mTimeOfThisPeak = mTimeOfNow;
                    mThreadValue = peakValleyThread(mPeakOfWave - mValleyOfWave);
                }
            }
        }
        mGravityOld = values;
    }

    /*
     * 检测波峰
     * 以下四个条件判断为波峰：
     * 1.目前点为下降的趋势：isDirectionUp为false
     * 2.之前的点为上升的趋势：lastStatus为true
     * 3.到波峰为止，持续上升大于等于2次
     * 4.波峰值大于20
     * 记录波谷值
     * 1.观察波形图，可以发现在出现步子的地方，波谷的下一个就是波峰，有比较明显的特征以及差值
     * 2.所以要记录每次的波谷值，为了和下次的波峰做对比
     * */
    private boolean detectorPeak(float newValue, float oldValue) {
        mLastStatus = mIsDirectionUp;
        if (newValue >= oldValue) {
            mIsDirectionUp = true;
            mContinueUpCount++;
        } else {
            mContinueUpFormerCount = mContinueUpCount;
            mContinueUpCount = 0;
            mIsDirectionUp = false;
        }

        if (!mIsDirectionUp && mLastStatus
                && (mContinueUpFormerCount >= 2 || oldValue >= 20)) {
            mPeakOfWave = oldValue;
            return true;
        } else if (!mLastStatus && mIsDirectionUp) {
            mValleyOfWave = oldValue;
            return false;
        } else {
            return false;
        }
    }

    /*
     * 阈值的计算
     * 1.通过波峰波谷的差值计算阈值
     * 2.记录4个值，存入tempValue[]数组中
     * 3.在将数组传入函数averageValue中计算阈值
     * */
    private float peakValleyThread(float value) {
        float tempThread = mThreadValue;
        if (mTempCount < mValueNum) {
            mTempValue[mTempCount] = value;
            mTempCount++;
        } else {
            tempThread = averageValue(mTempValue, mValueNum);
            for (int i = 1; i < mValueNum; i++) {
                mTempValue[i - 1] = mTempValue[i];
            }
            mTempValue[mValueNum - 1] = value;
        }
        return tempThread;

    }

    /*
     * 梯度化阈值
     * 1.计算数组的均值
     * 2.通过均值将阈值梯度化在一个范围里
     * */
    private float averageValue(float value[], int n) {
        float ave = 0;
        for (int i = 0; i < n; i++) {
            ave += value[i];
        }
        ave = ave / mValueNum;
        if (ave >= 8)
            ave = (float) 4.3;
        else if (ave >= 7 && ave < 8)
            ave = (float) 3.3;
        else if (ave >= 4 && ave < 7)
            ave = (float) 2.3;
        else if (ave >= 3 && ave < 4)
            ave = (float) 2.0;
        else {
            ave = (float) 1.7;
        }
        return ave;
    }


    /*
     * 连续走十步才会开始计步
     * 连续走了9步以下,停留超过3秒,则计数清空
     * */
    private void countStep() {
        mTimeOfLastPeak1 = mReloadTime != 0 ? mReloadTime : mTimeOfThisPeak1;
        mReloadTime = 0;
        mTimeOfThisPeak1 = System.currentTimeMillis();
        if (mTimeOfThisPeak1 - mTimeOfLastPeak1 <= 3000L) {
            if (mCurCount < 9) {
                mCurCount++;
            } else if (mCurCount == 9) {
                mCurCount += 10;
            } else {
                mCurCount++;
            }
        } else {//超时
            mCurCount = 1;//为1,不是0
        }
    }
}
