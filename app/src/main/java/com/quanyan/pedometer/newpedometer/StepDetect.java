package com.quanyan.pedometer.newpedometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import com.lidroid.xutils.util.LogUtils;
import com.quanyan.pedometer.core.Constants;
import com.quanyan.pedometer.core.IStepListener;

import java.util.ArrayList;

/**
 * Created with Android Studio.
 * Title:StepDetect
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/6/22
 * Time:15:25
 * Version 1.1.0
 */
public class StepDetect implements SensorEventListener {

    public static StepDetect mInstance = null;
    private static final String TAG = StepDetect.class.getSimpleName();

    public class Parameter { // default values

        public int DIMENSIONS = 3;// Less than 10.
        public float ALPHA = 0.6f;
        public int SMOOTH_SIZE = 3;// Less than 10.
        public float RC = 0.2f;

        public int WIN_SIZE = 9; // window to detect peak and valley.Less than
        // 20;
        public int PEAK_TH = 500;// Threshold for estimate the peak-peak
        public int PEAK_TH_MAX = 6000;

        public float BIG_MOTION = 0.45f;
        public int MAX_ACC = 1024; // what is the maximum value of accelerometer
        // sensor?
    }

    private Sensor mAccelerationSensor;
    private Sensor mStepSensor;

    private Parameter mParameter = new Parameter();

    // components for filters
    private float[] SensorData = new float[10];
    private float _filterConstant;
    private float _filterConstant1Minus;
    private int _frameIndex = 0;
    private int _frameRateEstimate = 20;
    private long _last30FrameTime = 0L;
    private double _norm;
    private float dt;
    private double gravitynorm = 0;
    private int _smoothIndex = 0;
    private double[] smoothArray = new double[10];

    // components for steps detection
    private int winPos = 0;
    private int peak = 0;
    private int valley = 0;
    private int lastValley = 0;
    private int[] lastValues = new int[20];
    private boolean foundValley = true;
    private boolean startPeaking = false;

    // components for time window
    private final static long minStepInterval = 200; // 200ms 5 steps/s
    private final static long maxStepInterval = 2000; // 2000ms 0.5step /s
    private final int REGULATION = Constants.STEP_THRESHOLD; // the steps which we think is valid step
    private final int INVALID = 3; // steps we lose the regulation

    private int TempSteps = 0; // record current steps
    private int InvalidSteps = 0; // record invalid steps
    private int ReReg = 2; // record whether restart to find the regular.
    // 0 - have started,but didn't find regular
    // 1 - already find the regular.

    public long mPreiousTime = 0; // record time interval
    public long currentTime = 0;
    private boolean bFirstPoint = true;
    private boolean bFirstStep = true;

    private final static int STATE_QUIESCENCE = 0;
    private final static int STATE_ACTIVE = 1;
    private final static int QUIESCENCE_THRESHOLD = 5; // 5 seconds

    private int mState;
    private long mQuiescenceCounter;

    private ArrayList<IStepListener> mIStepListeners = new ArrayList<>();

//    private long firstNoMovementTime_ = 0;
//    private long lastNoMovementTime_ = 0;
//    private int frameCountForNoMovement_ = 0;

    public static StepDetect newInstance(){
        if(mInstance == null){
            mInstance = new StepDetect();
        }
        return mInstance;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
//            if(AppDebug.DEBUG_LOG) {
//                for (int i = 0; i < event.values.length; i++) {
//                    LogUtils.i("result = " + event.values[i] + " \n");
//                }
//            }
//            LogUtils.i("result = " + event.values);
//            notifyListener(true, 1);
//        }else{
            //传感器的时间是纳秒，所以除以10^6
            int result = detectStep(event.timestamp / 1000000, event.values);
            if (result > 0) {
                LogUtils.i("result = " + result);
            }
            if (result == Constants.STEP_THRESHOLD + 1) {
                notifyListener(true, 1);
            } else if (result == Constants.STEP_THRESHOLD) {
                notifyListener(true, Constants.STEP_THRESHOLD);
            } else if (result < Constants.STEP_THRESHOLD && result > 0) {
                notifyListener(false, result);
            }
//            if (EnvironmentDetector.isNoMovement(event.values)) {
//                long l = System.currentTimeMillis();
//                if (this.frameCountForNoMovement_ == 0) {
//                    this.firstNoMovementTime_ = l;
//                    lastNoMovementTime_ = l;
//                }
//                if (l - this.lastNoMovementTime_ < 500) {//sampling rate is 50 ms
//                    long d = l - this.lastNoMovementTime_;
//                    this.frameCountForNoMovement_ = (1 + this.frameCountForNoMovement_);
//                    lastNoMovementTime_ = l;
//                } else {
//                    this.frameCountForNoMovement_ = 0;
//                    //		    	LogUtils.d("restart counting frameCountForNoMovement_");
//                }
//                //		    LogUtils.i("this.frameCountForNoMovement_ is " + this.frameCountForNoMovement_);
//                if (l - this.firstNoMovementTime_ > 60000L && StepService.getInstance() != null
//                        && frameCountForNoMovement_ > 600) {//200*50ms = 10s
//                    if (!EnvironmentDetector.isScreenOn(StepService.getInstance())) {
//                        this.frameCountForNoMovement_ = 0;
//                        if (EnvironmentDetector.isXiaoMi()) {
////                            LogUtils.d(TAG, "Screen not on, set sleepy");
////                            StepService.getInstance().setSleepy();
//                        }
//                    } else {
////                        LogUtils.d("Screen on not set sleepy");
//                    }
//                }
//            }
//        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void addListener(IStepListener l) {
        mIStepListeners.add(l);
    }

    public void removeListener(IStepListener stepListener) {
        mIStepListeners.remove(stepListener);
    }

    public void notifyListener(boolean isWalking, int value) {
        if (isWalking) {
            for (IStepListener listener : mIStepListeners) {
                listener.onStep(value);
            }
        } else {
            for (IStepListener listener : mIStepListeners) {
                listener.onStateChanged(value);
            }
        }
    }

    public void Init() {
        InitFilter();
        InitStepDetect();
        InitTimeWindow();
        mState = STATE_QUIESCENCE;
        mQuiescenceCounter = 0;
    }

    /*
     * Initialize and reset the filter
	 */
    private void InitFilter() {
        _frameRateEstimate = 20;
        _frameIndex = 0;
        _last30FrameTime = 0L;
        dt = (1.0F / _frameRateEstimate);
        _filterConstant = (dt / (dt + mParameter.RC));
        _filterConstant1Minus = (1.0F - _filterConstant);

        for (int i = 0; i < mParameter.DIMENSIONS; i++) {
            SensorData[i] = 0;
        }

        for (int j = 0; j < mParameter.SMOOTH_SIZE; j++) {
            smoothArray[j] = 0;
        }

        gravitynorm = 0;
    }

    /**
     * Initialize and reset the step detect
     **/
    private void InitStepDetect() {
        LogUtils.d("Initialize StepDetector!");
        winPos = 0;
        peak = 0;
        valley = 0;
        lastValley = 0;
        foundValley = true;
        startPeaking = false;

        for (int i = 0; i < mParameter.WIN_SIZE; i++) {
            lastValues[i] = 0;
        }
    }

    private void InitTimeWindow() {
        LogUtils.d("Enter Init of StepCounter!");
        ReReg = 2;
        TempSteps = 0;
        InvalidSteps = 0;
    }

    private int TimeWindow() {
        // LogUtils.d("Enter TimeWindow of StepCounter!");
        int nRes = 0;
        if (bFirstStep) // if the first step, add it;
        {
            TempSteps++;
            mPreiousTime = currentTime;
            ReReg = 1;
            InvalidSteps = 0;
            bFirstStep = false;
        } else // if not the first step
        {
            long TimeInterval = currentTime - mPreiousTime;
            if ((TimeInterval >= minStepInterval)
                    && (TimeInterval <= maxStepInterval)) // if time interval is
            // valid
            {
                InvalidSteps = 0;
                if (ReReg == 1) // haven't find the regulation
                {
                    TempSteps++; // temp step++
                    if (TempSteps >= REGULATION) // arrived the valid steps
                    {
                        ReReg = 0; // we find the step regulation
                        nRes = TempSteps; // refresh the steps
                        TempSteps = 0;
                    }
                    mPreiousTime = currentTime;
                } else if (ReReg == 0) // have found the regulation, refresh the
                // steps
                {
                    nRes = 1;
                    TempSteps = 0;
                    mPreiousTime = currentTime;
                }
            } else if (TimeInterval < minStepInterval) // too sort interval
            {
                if (ReReg == 0) // have fond the regulation
                {
                    if (InvalidSteps < 255)
                        InvalidSteps++;
                    if (InvalidSteps >= INVALID) // haven't arrive the invalid
                    // count, refind the
                    // regulation
                    {
                        InvalidSteps = 0;
                        ReReg = 1;// 重新记8步
                        TempSteps = 1;
                        mPreiousTime = currentTime;
                    } else // else, discard this step.
                    {
                        mPreiousTime = currentTime;
                    }
                } else if (ReReg == 1) // havn't find the regulation, the
                // previous steps invalid.
                {
                    InvalidSteps = 0;
                    ReReg = 1;
                    TempSteps = 1;
                    mPreiousTime = currentTime;
                }
            } else if (TimeInterval > maxStepInterval) // too long interval
            {
                InvalidSteps = 0;
                ReReg = 1;
                TempSteps = 1;
                mPreiousTime = currentTime;
            }
        }

        return nRes;
    }

    private void Filters(long paramLong, float[] inData) {
        double sum = 0;
        updateAverageAndFs(paramLong);

        // LogUtils.d("SensorData[0] = " + SensorData[0]
        // + "SensorData[1] = " + SensorData[1] + "SensorData[2] = "
        // + SensorData[2]);
        // LogUtils.d("inData[0] = " + inData[0] + "inData[1] = "
        // + inData[1] + "inData[2] = " + inData[2]);

        // low pass here to get rid of high frequency noise
        for (int i = 0; i < 3; i++) {
            SensorData[i] = (inData[i] * _filterConstant + _filterConstant1Minus
                    * SensorData[i]);
        }
        _norm = (Math.sqrt(SensorData[0] * SensorData[0] + SensorData[1]
                * SensorData[1] + SensorData[2] * SensorData[2]));

        // LogUtils.d("_filterConstant = " + _filterConstant
        // + "_frameRateEstimate = " + _frameRateEstimate);
        // LogUtils.d("norm = " + _norm + "after filter 1");

        // high pass here, get rid of dc component
        gravitynorm = (mParameter.ALPHA * gravitynorm + (1 - mParameter.ALPHA)
                * _norm);
        _norm = (float) (_norm - gravitynorm);
//        LogUtils.d("norm = " + _norm + "        after filter 2");

        smoothArray[_smoothIndex] = _norm;
        _smoothIndex ++;
        if (_smoothIndex == mParameter.SMOOTH_SIZE) {
            _smoothIndex = 0;
        }

        for (int j = 0; j < mParameter.SMOOTH_SIZE; j++) {
            sum = sum + smoothArray[j];
        }
        _norm = sum / mParameter.SMOOTH_SIZE;
//         LogUtils.d("norm = " + _norm + "       after filter 3");

    }

    private void updateAverageAndFs(long paramLong) {
        if (_frameIndex == 0) {
            _last30FrameTime = paramLong;
            _frameIndex = 1;
        } else if (_frameIndex >= 30) {
            if (paramLong - _last30FrameTime != 0) {
                _frameRateEstimate = ((int) (29000L / (paramLong - _last30FrameTime)));
            }
            if (_frameRateEstimate == 0) {
                _frameRateEstimate = 20;
            }
            // the phone stopped for a long time
            if (_frameRateEstimate < 1) {
                InitFilter();
            } else {
                dt = (1.0F / _frameRateEstimate);
                _filterConstant = (dt / (dt + mParameter.RC));
                _filterConstant1Minus = (1.0F - _filterConstant);

                _last30FrameTime = paramLong;
                _frameIndex = 0;
            }
            return;
        }

        _frameIndex += 1;
    }

    private int Preprocess(long timeStamp, float[] inputAcce) {
        int resData = 0;
        Filters(timeStamp, inputAcce);

        resData = (int) (_norm * mParameter.MAX_ACC);
        return resData;
    }

    private boolean isBigMotion(float[] indata) {
        boolean bBigMotion = (Math.abs(Math.sqrt(indata[0] * indata[0]
                + indata[1] * indata[1] + indata[2] * indata[2]) - 9.812345) >= mParameter.BIG_MOTION);
        return bBigMotion;
    }

    private int detectStep(long timeStamp, float[] accelerationSensor) {
        int nStatus = -1;
        int nRes = 0;
        int nLen = accelerationSensor.length;
        int _adresult = 0;

        if (nLen != mParameter.DIMENSIONS) {
            Log.d(TAG, "step_counter: input dimension error!");
            return 0;
        }

        if (bFirstPoint) {
            Init();
            mPreiousTime = timeStamp;
            bFirstPoint = false;
        }
        currentTime = timeStamp;
        _adresult = Preprocess(timeStamp, accelerationSensor);

        int next = mState;
        switch (mState) {
            case STATE_QUIESCENCE:
                if (isBigMotion(accelerationSensor)) {
                    next = STATE_ACTIVE;
                    mQuiescenceCounter = 0;
                }
                break;
            case STATE_ACTIVE:
                if (isBigMotion(accelerationSensor)) {
                    mQuiescenceCounter = 0;
                } else {
                    mQuiescenceCounter++;
                    if (mQuiescenceCounter >= (QUIESCENCE_THRESHOLD * _frameRateEstimate)) {
                        next = STATE_QUIESCENCE;
                    }
                }
        }
        mState = next;

        if (mState == STATE_ACTIVE && 1 == stepDetect(_adresult, timeStamp)) {

            nRes = TimeWindow();

            if (1 == ReReg) {
                nStatus = TempSteps;
                LogUtils.e("middle=" + TempSteps);
            } else {
                if (REGULATION == nRes) {
                    nStatus = nRes;
                    LogUtils.e("first");
                } else {
                    nStatus = REGULATION + 1;
                    LogUtils.e("working");
                }

            }
        } else {
//			LogUtils.e("not detetecd");
        }

        return nStatus;
    }

    /*
     * Detect if a possible step
	 */
    private int stepDetect(int event, long time) {
        // LogUtils.d("Detect the step!");
        int numOfSteps = 0;

        if (startPeaking) {
            getPeakAndValley();
        }

        if (startPeaking && (peak >= 0)) {// Peak is detected
            int delta = lastValues[peak] - lastValley;
//            LogUtils.d("detect step peak and valley : " + delta);
            if (foundValley && (delta > mParameter.PEAK_TH/*  && delta <= mParameter.PEAK_TH_MAX*/)) {// Step
                // detected
                // with
                // maximum
                // peak-valley
                // range.
                numOfSteps = 1;
                foundValley = false;
            }
        }

        // Valley is detected
        if (startPeaking && (valley >= 0)) {
            foundValley = true;
            lastValley = lastValues[valley];
        }

        // Store latest accelerometer reading in the window.
        lastValues[winPos] = event;

        // Once the buffer is full, start peak/valley detection.
        if ((winPos == mParameter.WIN_SIZE - 1) && !startPeaking) {
            startPeaking = true;
        }

        // Increment position within the window.
        if (++winPos == mParameter.WIN_SIZE) {
            winPos = 0;
        }

        return (numOfSteps);

    }

    /*
     * Checks if the current middle of the window is the local peak or valley.
     */
    private void getPeakAndValley() {
        int mid = (winPos + mParameter.WIN_SIZE / 2) % mParameter.WIN_SIZE;

        peak = mid;
        valley = mid;

        for (int i = 0; i < mParameter.WIN_SIZE; i++) {
            if (i != mid) {
                if (lastValues[i] > lastValues[mid]) {
                    peak = -1;
                } else if (lastValues[i] < lastValues[mid]) {
                    valley = -1;
                }

            }
        }
    }
}
