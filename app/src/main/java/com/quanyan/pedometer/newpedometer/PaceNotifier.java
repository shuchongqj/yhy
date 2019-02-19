package com.quanyan.pedometer.newpedometer;

import com.lidroid.xutils.util.LogUtils;
import com.quanyan.pedometer.core.Constants;
import com.quanyan.pedometer.core.IStepListener;
import com.yhy.common.types.AppDebug;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created with Android Studio.
 * Title:PaceNotifier
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/7/2
 * Time:10:12
 * Version 1.1.0
 */
public class PaceNotifier implements IStepListener {

    /**
     * 步数变化是保存的时间
     */
    private long mLastStepTime = 0;
    /**
     * 计算步数平均值的暂存值
     */
    private long[] mLastStepDeltas = {-1, -1, -1, -1};
    /**
     * 暂存step/mill seconds的索引值
     */
    private int mLastStepDeltasIndex = 0;
    /**
     * 走路时，每秒走的步数
     */
    private double mPace = 2.0f;

    /** Desired pace, adjusted by the user */
    private int mDesiredPace;
    /**
     * 根据人的身高体重计算得到的走路时的速度
     */
    private double mSpeed;
    /**
     * 每走一步得到的距离
     */
    double mDistance = (double) 0.5;
    /**
     * 每走一步消耗的卡路里
     */
    private double mCal = (double) 0.03;

    private DecimalFormat mDecimalFormat = new DecimalFormat("#.###");

    public PaceNotifier(){
        try {
            mPace = Double.parseDouble(mDecimalFormat.format(mPace));
            mSpeed = Double.parseDouble(mDecimalFormat.format(mSpeed));
            mDistance = Double.parseDouble(mDecimalFormat.format(mDistance));
        }catch (NumberFormatException e){
            mPace = 2.0;
            mSpeed = 0.7;
            mDistance = 0.4;
        }
    }

//    public void setPace(double pace) {
//        mPace = pace;
//        int avg = (int) (60 * 1000.0 / mPace);
//        for (int i = 0; i < mLastStepDeltas.length; i++) {
//            mLastStepDeltas[i] = avg;
//        }
//        notifyPaceDataChange(mDistance, mCal, mPace, mSpeed);
//    }

    public interface PaceListener {
        void paceChanged(double distance, double cal, double pace, double speed);
    }
    private ArrayList<PaceListener> mListeners = new ArrayList<PaceListener>();


    public void addListener(PaceListener paceListener) {
        mListeners.add(paceListener);
    }

    public void removeListener(PaceListener paceListener){
        mListeners.remove(paceListener);
    }

    @Override
    public void onStep(int value) {
        long thisStepTime = System.currentTimeMillis();
        if(mLastStepTime > 0){
            long delta = thisStepTime - mLastStepTime;
            LogUtils.v("step delta time : " + delta);
            if(AppDebug.DEBUG_LOG){
                new StepDBManager.DebugSpeedDelta(thisStepTime, delta).execute();
            }
            if(delta <= 0){
                delta = 200;
            }
            mPace = Double.parseDouble(mDecimalFormat.format(1000.0 / delta));// How many steps per seconds
            if(Double.compare(mPace, 5) >= 0){
                mPace = 2.0f;
            }
        }
        mLastStepTime = thisStepTime;
        mSpeed = PedometerUtil.getInstance().getSpeed(mPace);

        if(value == Constants.STEP_THRESHOLD) {
            mDistance = PedometerUtil.getInstance().getStepLength(mPace) * Constants.STEP_THRESHOLD;
            mCal = PedometerUtil.getInstance().getCalories(mSpeed) * Constants.STEP_THRESHOLD;
        } else {
            mDistance = PedometerUtil.getInstance().getStepLength(mPace);
            mCal = PedometerUtil.getInstance().getCalories(mSpeed);
        }
        notifyPaceDataChange(mDistance, mCal, mPace, mSpeed);
    }

    @Override
    public void onStateChanged(int value) {

    }

    private void notifyPaceDataChange(double distance, double cal, double pace, double speed) {
        for (PaceListener listener : mListeners) {
            LogUtils.i("pacenotifier new : mPace is=" + pace + " mSpeed is=" + speed + " mDistance ="
                    + distance + " mCal=" + cal);
            listener.paceChanged(distance, cal, pace, speed);
//            if(AppDebug.DEBUG_LOG){
//                new StepDBManager.DebugSpeed(pace, speed, cal, distance).execute();
//            }
        }
    }
}
