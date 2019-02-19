package com.quanyan.pedometer.newpedometer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.lidroid.xutils.util.LogUtils;
import com.quanyan.pedometer.utils.PreferencesUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:PedometerUtils
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/7/2
 * Time:09:46
 * Version 1.1.0
 */
public class PedometerUtil {
    private double mHeight = 175;//cm
    private double mWeight = 60;//kg

    private static PedometerUtil ourInstance = new PedometerUtil();

    private List<OnStepListener> mStepListeners = new ArrayList<>();
    private DecimalFormat mDecimalFormat = new DecimalFormat("#.###");

    public static PedometerUtil getInstance() {
        return ourInstance;
    }

    private PedometerUtil() {
    }

    public void setHeight(double height) {
        mHeight = height;
    }

    public void setWeight(double weight) {
        mWeight = weight;
    }

    public static long getTargetStep(Context context) {
        int target = PreferencesUtils.getInt(context.getApplicationContext(), StepService.PEDOMETER_VIEW_MAX_STEPS, 20000);
        return target;
    }

    public long getStepCounts() {
        long steps = 0;
        if (mStepService != null) {
            steps = mStepService.getSteps();
        }
        return steps;
    }

    public double getDistance() {
        double distance = 0;
        if (mStepService != null) {
            distance = mStepService.getDistance();
        }
        return distance;
    }

    public double getCalories() {
        double calories = 0;
        if (mStepService != null) {
            calories = mStepService.getCalories();
        }
        return calories;
    }

    public interface IStepCallback {
        void onStep(long step);
    }

    public interface OnStepListener {
        void onStep(long step, double speed, double calories, long time, double distance);
    }

    private StepService mStepService;
    private boolean isBinded = false;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.i("new service connection");
            try {
                mStepService = ((StepService.StepBinder) service).getService();
                mStepService.registerCallback(mStepCallback);
                updateSteps(mStepService.getSteps(), mStepService.getSpeed(), mStepService.getCalories(),
                        mStepService.getTime(), mStepService.getDistance());
            } catch (RemoteException e) {
                LogUtils.e("服务启动失败");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            try {
                if (mStepService != null) {
                    mStepService.unregisterCallback(mStepCallback);
                    mStepService = null;
                }
            } catch (RemoteException e) {
                LogUtils.e("服务取消失败");
            }
        }
    };

    public void bindService(Context context) {
        Intent intent = new Intent(context, StepService.class);
        context.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        isBinded = true;
    }

    public void unBindService(Context context) {
        if (isBinded) {
            context.unbindService(mServiceConnection);
            isBinded = false;
        }
    }

    public void addListener(OnStepListener listener) {
        synchronized (this) {
            if (!mStepListeners.contains(listener)) {
                mStepListeners.add(listener);
            }
        }
    }

    public void removeListener(OnStepListener listener) {
        synchronized (this) {
            if (mStepListeners.contains(listener)) {
                mStepListeners.remove(listener);
            }
        }
    }

    private IStepCallback mStepCallback = new IStepCallback() {
        @Override
        public void onStep(long step) {
            updateSteps(mStepService.getSteps(), mStepService.getSpeed(),
                    mStepService.getCalories(), mStepService.getTime(), mStepService.getDistance());

        }
    };

    public void updateSteps(long steps, double speed, double calories, long time, double distance) {
        for (int i = 0; i < mStepListeners.size(); i++) {
            mStepListeners.get(i).onStep(steps, speed, calories, time, distance);
            LogUtils.i("steps:" + steps + " speed" + speed + " cal: " + calories + " time: " + time + " dis:" + distance);
        }
    }

    public double getCalories(double speed) {
        double calories = (double) (4.5 * mWeight * speed / 3600);
        if(Double.compare(calories, 0.06) > 0 ){
            return 0.045;
        }
        return Double.parseDouble(mDecimalFormat.format(calories));
    }

    public double getSpeed(double pace) {
        double speed = pace * getStepLength(pace) //meter / seconds
//    			/ 1000f // kilometer/seconds
//    			* 60 * 60// km/hour
                ;
        return Double.parseDouble(mDecimalFormat.format(speed));
    }

    public double getStepLength(double pace) {
        double stepLength = mHeight / 4 + 10; // Default as average
        if (pace >= 0 && pace < 2) {
            stepLength = mHeight / 5;
        } else if (pace >= 2 && pace < 3) {
            stepLength = mHeight / 4;
        } else if (pace >= 3 && pace < 4) {
            stepLength = mHeight / 3;
        } else if (pace >= 4 && pace < 5) {
            stepLength = mHeight / 2;
        } else if (pace >= 5 && pace < 6) {
            stepLength = mHeight / 1.2;
        } else if (pace >= 6 && pace < 8) {
            stepLength = mHeight;
        } else {
            stepLength = mHeight * 1.2;
        }
        return Double.parseDouble(mDecimalFormat.format(((stepLength + (mHeight / 6.0)) / 100))); //meter
    }
}
