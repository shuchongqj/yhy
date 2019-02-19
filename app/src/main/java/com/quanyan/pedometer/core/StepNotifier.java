/*
 *  Pedometer - Android App
 *  Copyright (C) 2014 Gao Yu
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.quanyan.pedometer.core;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import com.lidroid.xutils.util.LogUtils;
import com.quanyan.pedometer.heartbeat.EnvironmentDetector;

import java.util.ArrayList;


/**
 * Counts steps provided by StepDetector and passes the current
 * step count to the activity.
 */
public class StepNotifier implements SensorEventListener {
	private static final String TAG = "P_StepNotifier";
    private int mCount = 0;
    private IStepAlgorithm mAlgorithm;
    public StepNotifier(PedometerSettings settings) {
        notifyListener(true, 0);
        mAlgorithm = new StepCounter(); 
    }

    public void reloadSettings(double[] paras) {
    	try {
	    	mAlgorithm.setParameter(paras);
	        notifyListener(true, 0);
    	} catch(Exception e) {
    		LogUtils.e(e.toString(), e);
    	}
    }

    public void setSteps(int steps) {
        mCount = steps;
        notifyListener(true, 0);
    }

    public void setAlgorithm(IStepAlgorithm algorithm) {
    	mAlgorithm = algorithm;
    }
 
    private ArrayList<IStepListener> mListeners = new ArrayList<IStepListener>();

    public void addListener(IStepListener l) {
        mListeners.add(l);
    }
 
    public void notifyListener(boolean isWalking, int value) {
    	if(isWalking) {
	        for (IStepListener listener : mListeners) {
	            listener.onStep(value);
	        }
    	} else {
	        for (IStepListener listener : mListeners) {
	            listener.onStateChanged(value);
	        }
    	}
    }

    private int mCount2 = 0;
	private long firstNoMovementTime_ = 0;
	private long lastNoMovementTime_ = 0;
	private int frameCountForNoMovement_ = 0;
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		try{
			int result = mAlgorithm.detectStep(event.timestamp / 1000000, event.values);
			if(result > 0){
				Log.i(TAG, "result = " + result);
			}
			if(result == Constants.STEP_THRESHOLD + 1) {
				notifyListener(true, 1);
			} else if(result == Constants.STEP_THRESHOLD) {
				notifyListener(true, Constants.STEP_THRESHOLD);
			} else if(result < Constants.STEP_THRESHOLD && result > 0) {
				notifyListener(false, result);
			} 
			if (EnvironmentDetector.isNoMovement(event.values)) {
				long l = System.currentTimeMillis();
			    if (this.frameCountForNoMovement_  == 0) {
			          this.firstNoMovementTime_ = l;
			          lastNoMovementTime_ = l;
			    }
			    if(l - this.lastNoMovementTime_ < 500) {//sampling rate is 50 ms
			    	long d = l - this.lastNoMovementTime_ ;
			    	this.frameCountForNoMovement_ = (1 + this.frameCountForNoMovement_);
			    	lastNoMovementTime_ = l;
			    } else {
			    	this.frameCountForNoMovement_ = 0;
	//		    	LogUtils.d("restart counting frameCountForNoMovement_");
			    }
	//		    LogUtils.i("this.frameCountForNoMovement_ is " + this.frameCountForNoMovement_);
				if (l - this.firstNoMovementTime_ > 60000L && StepService.getInstance() != null
						&& frameCountForNoMovement_ > 600) {//200*50ms = 10s
					if(!EnvironmentDetector.isScreenOn(StepService.getInstance())) {
						this.frameCountForNoMovement_ = 0;
						if(EnvironmentDetector.isXiaoMi()) {
							LogUtils.d("Screen not on, set sleepy");
	//						StepService.getInstance().setSleepy();
						}
					} else {
	//					LogUtils.d("Screen on not set sleepy");
					}
			      }
			} 
		} catch(Exception e) {
			e.toString();
			LogUtils.e(e.toString(), e);
		}
	}


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
    
}
