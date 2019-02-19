/*
 *  Pedometer - Android App
 *  Copyright (C) 2009 Levente Bagi
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

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.lidroid.xutils.util.LogUtils;
import com.quanyan.pedometer.heartbeat.EnvironmentDetector;
import com.quanyan.pedometer.heartbeat.HeartBeatSetting;
import com.quanyan.pedometer.utils.StepDBManger;
import com.quanyan.pedometer.utils.TimeUtil;
import com.yhy.common.beans.net.model.pedometer.WalkDataInfo;
import com.yhy.common.utils.SPUtils;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * <p>Notice the use of the {@link NotificationManager} when interesting things
 * happen in the service.  This is generally how background services should
 * interact with the user, rather than doing something more disruptive such as
 * calling startActivity().
 */
public class StepService extends Service {
    private static final String TAG = "StepService";
    private static StepService mInstance;
    private IBinder binder = new StepService.StepBinder();
    private SharedPreferences mSettings;
    private PedometerSettings mPedometerSettings;
    private SharedPreferences mState;
    private SharedPreferences.Editor mStateEditor;
    private SensorManager mSensorManager;
    private Sensor mSensor;

    private StepNotifier mStepNotifier;
    private PaceNotifier mPaceNotifier;
    private int mPace;
    private int mSteps;

    private float mDistance;
    private float mSpeed;
    private float mCalories;

    private long mLastStepTime = 0;
    private long mLastStepDayTime = 0;
    private long mStepTime = 0;

    @Autowired
    IUserService userService;

    /**
     * @Fields mSynTime: new save hour time
     */
    private long mSynTime = 0;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    private ArrayList<Pedometer.IStepCallback> mCallbacks;
//	private PedometerDepositary mPedoDepo;

    private HeartBeatSetting mHeartBeatSetting;

    public static StepService getInstance() {
        return mInstance;
    }

    public class StepBinder extends Binder {
        StepService getService() {
            return StepService.this;
        }
    }

//		public void saveToDatebase(PedometerInfo info) {
//			if(info == null) {
//				info = new PedometerInfo(mPersonId);
//				info.setDate(mLastStepDayTime);
//			} 
//	        info.setCalories(mCalories);
//	        info.setSteps(mSteps);
//	        info.setWalkDuration(mStepTime);
//	        info.setDistance(mDistance);
//	        
//	        mPedoDepo.saveDayInfo(info);
//	        
//		}

    @Override
    public void onCreate() {
        LogUtils.i("[SERVICE] onCreate");
        super.onCreate();

        YhyRouter.getInstance().inject(this);
//        readEnvSettings();
        StepDBManger.getDefaultDbUtils(this);

        mCallbacks = new ArrayList<Pedometer.IStepCallback>();

        // Load settings
//        mPedoDepo = new PedometerDepositary(this);
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        mPedometerSettings = new PedometerSettings(mSettings);
        mState = getSharedPreferences("state", 0);

        mSteps = mState.getInt("steps", 0);
        mDistance = mState.getFloat("distance", 0);
        mPace = mState.getInt("pace", 0);
        mSpeed = mState.getFloat("speed", 0);
        mCalories = mState.getFloat("calories", 0);
        mLastStepDayTime = mState.getLong("daytime", 0);
        mStepTime = mState.getLong("time", 0);
        LogUtils.i("Step time read as: " + mStepTime + "mLastDayTime as: " + mLastStepDayTime);

//        acquireWakeLock();

        // Start detecting
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Register our receiver for the ACTION_SCREEN_OFF action. This will make our receiver
        // code be called whenever the phone enters standby mode.
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Constants.ACTION_UPLOAD_STEP);
        filter.addAction(Constants.ACTION_UPLOAD_STEP_DATA_OK);
        filter.addAction(Constants.ACTION_UPLOAD_STEP_DATA_KO);
        filter.addAction(Constants.ACTION_SAVE_STEP_PER_HOUR);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        registerReceiver(mReceiver, filter);

        mStepNotifier = new StepNotifier(mPedometerSettings);

        mStepNotifier.addListener(mStepListener);
        mStepNotifier.setSteps(mSteps);

        mPaceNotifier = new PaceNotifier(mPedometerSettings);
        mPaceNotifier.setPace(mPace);
        mPaceNotifier.addListener(mPaceListener);

        mStepNotifier.addListener(mPaceNotifier);

        //开启
//        WriteDataToDBPerHour();

        mSynTime = System.currentTimeMillis();
//		setRepeatTask(Constants.ACTION_SAVE_STEP_PER_HOUR,
//				AlarmManager.INTERVAL_HOUR);
        setRepeatTask(Constants.ACTION_UPLOAD_STEP, AlarmManager.INTERVAL_DAY / 4);
//		setNewDayValue();
        if (mLastStepDayTime != 0) {
            long current = System.currentTimeMillis();
            if (current - mLastStepDayTime > Constants.MIL_SECONDS_DAY) {
                clearData();
            } else if (IsNeedSaveData()) {
                saveDataPerHour(false);
            }
        } else {
            initDayTimeValue();
        }
        setNewHourValue();
        delayUpdata();
        if (Pedometer.getInstance().isDetectorRegistered(this)) {
            registerDetector();
//            PartialWakeLock.getInstance().acquireWakeLock(this);
        }
        mInstance = this;
        mHeartBeatSetting = HeartBeatSetting.getInstance(this);
        EnvironmentDetector.getInstance(this);
        LogUtils.d("xiaomi check result is : " + EnvironmentDetector.isXiaoMi());
    }

    private void callback(int step) {
        int N = mCallbacks.size();
        for (int i = 0; i < N; i++) {
            mCallbacks.get(i).onStep(step);
        }
    }

    private void saveData() {
        mStateEditor = mState.edit();
        mStateEditor.putInt("steps", mSteps);
        mStateEditor.putInt("pace", mPace);
        mStateEditor.putFloat("distance", mDistance);
        mStateEditor.putFloat("speed", mSpeed);
        mStateEditor.putFloat("calories", mCalories);
        mStateEditor.putLong("time", mStepTime);
        mStateEditor.apply();
    }

    @Override
    public void onDestroy() {
        LogUtils.i("[SERVICE] onDestroy");

        // Unregister our receiver.
        unregisterReceiver(mReceiver);
        unregisterDetector();

        saveData();
        super.onDestroy();

        // Stop detecting
        unregisterDetector();
        PartialWakeLock.getInstance().releaseWakeLock();
        mInstance = null;

        Intent localIntent = new Intent();
        localIntent.setClass(this, StepService.class); //销毁时重新启动Service
        this.startService(localIntent);
    }

    public void setActive() {
        if (!EnvironmentDetector.isScreenOn(this)) {
            PartialWakeLock.getInstance().acquireWakeLock(this);
        }
        LogUtils.i("setActive");
        this.mHeartBeatSetting.cancelAlarm(this);
        resumeMotionTracker();
    }

    public void setSleepy() {
        LogUtils.i("setSleepy");
        pauseMotionTracker();
        this.mHeartBeatSetting.cancelAlarm(this);
        this.mHeartBeatSetting.setAlarm(this);
        PartialWakeLock.getInstance().releaseWakeLock();
    }

    public void pauseMotionTracker() {
        LogUtils.i("unregister step tracker");
        this.mSensorManager.unregisterListener(mStepNotifier);
    }

    public void resumeMotionTracker() {
        if (!Pedometer.getInstance().isDetectorRegistered(this)) {
            PartialWakeLock.getInstance().releaseWakeLock();
            return;
        }
        registerDetector();
    }

    public void registerDetector() {
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        LogUtils.d("registerDetector----------");
        mSensorManager.registerListener(mStepNotifier, mSensor, 50000);

    }

    public void unregisterDetector() {
        mSensorManager.unregisterListener(mStepNotifier);
        LogUtils.d("unregisterDetector----------");
    }


    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.i("[SERVICE] onBind");
        if (Pedometer.getInstance().isDetectorRegistered(this)) {
            registerDetector();
        }
        return binder;
    }

    public void reloadSettings() {

//        if (mStepNotifier    != null) mStepNotifier.reloadSettings();
//        if (mPaceNotifier     != null) mPaceNotifier.reloadSettings();
    }

    public void resetValues() {
//        mStepDisplayer.setSteps(0);
//        mPaceNotifier.setPace(0);
//        mSpeedNotifier.setSpeed(0);
//        mCaloriesNotifier.setCalories(0);

        mSteps = 0;
        mDistance = 0;
        mStepTime = 0;
        mSpeed = 0;
        mCalories = 0;
        mPace = 0;
        saveData();
    }

    private int mStepBuf = 0;


    /**
     * Forwards step values from StepDisplayer to the activity.
     */
    private IStepListener mStepListener = new IStepListener() {

        @Override
        public void onStep(int value) {
            long current = System.currentTimeMillis();
            if (current - mLastStepDayTime > Constants.MIL_SECONDS_DAY) {
//    			saveToDatebase(null);
//    			initDayTimeValue();
////    			resetValues();
                clearData();
                setNewHourValue();
            } else if (IsNeedSaveData()) {
                saveDataPerHour(false);
                setNewHourValue();
            }
            long time = getStepTime(current);
            LogUtils.i("Accumalated time is: " + String.valueOf(time));

            mSteps += value;
            callback(mSteps);
            if (mSteps % 10 == 0) {
                saveData();
            }
        }

        @Override
        public void onStateChanged(int value) {
            mStepBuf = value;
        }


    };
    /**
     * Forwards pace values from PaceNotifier to the activity.
     */
    private PaceNotifier.Listener mPaceListener = new PaceNotifier.Listener() {

        @Override
        public void paceChanged(double distance, double cal) {
            mDistance += distance;
            mCalories += cal;
            LogUtils.d("Step forward:" + String.valueOf(mSteps) + " mDistance is: "
                    + mDistance + " mCalories is: " + mCalories);
        }

    };

    private boolean isContinuousStep() {
        return mStepBuf >= Constants.STEP_THRESHOLD ? true : false;
//    	return true;
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Check action just to be on the safe side.
            String action = intent.getAction();

            Log.i(TAG, "onReceive action:" + action);
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                if (Pedometer.getInstance().isDetectorRegistered(context)) {
                    StepService.this.unregisterDetector();
                    StepService.this.registerDetector();
                }
                if (EnvironmentDetector.isXiaoMi()) {
                    PartialWakeLock.getInstance().releaseWakeLock();
                }
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                // Unregisters the listener and registers it again.

                if (EnvironmentDetector.isXiaoMi() && !EnvironmentDetector.isLedongliRunning(StepService.this)) {
                    PartialWakeLock.getInstance().acquireWakeLock(StepService.this);
                }
                if (Pedometer.getInstance().isDetectorRegistered(context)) {
                    StepService.this.unregisterDetector();
                    StepService.this.registerDetector();
                }
                LogUtils.i("Wake lock is: " + PartialWakeLock.getInstance().isHeld());

            } else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

                ConnectivityManager connectMgr = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo wifiNetInfo = connectMgr
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (wifiNetInfo.isConnected()) {
                    long current = System.currentTimeMillis();
                    if (current - mLastStepDayTime > Constants.MIL_SECONDS_DAY) {
                        clearData();
                    } else {
                        if (IsNeedSaveData()) {
                            saveDataPerHour(false);
                        }
                    }
                    LogUtils.i("ConnectivityManager~~ connected");
                    if (userService.isLogin()) {
                        // uploadData();
                        delayUpdata();
                    }
                    setRepeatTask(Constants.ACTION_UPLOAD_STEP,
                            AlarmManager.INTERVAL_DAY / 4);

                } else {
                }
            } else if (Constants.ACTION_UPLOAD_STEP.equals(action)) {
                if (userService.isLogin()) {
                    uploadData();
                }
            } else if (Constants.ACTION_SAVE_STEP_PER_HOUR.equals(action)) {
                LogUtils.i("Action received:" + Constants.ACTION_SAVE_STEP_PER_HOUR);
                long current = System.currentTimeMillis();
                if (current - mLastStepDayTime > Constants.MIL_SECONDS_DAY) {
                    clearData();
                } else {
                    if (IsNeedSaveData()) {
                        saveDataPerHour(false);
                    }
                }
                setNewHourValue();
            } else if (Constants.ACTION_CLEAR_DATA.equals(action)) {
                LogUtils.i("Action received:" + Constants.ACTION_CLEAR_DATA);
                saveDataPerHour(true);
                resetValues();
            } else if (Intent.ACTION_DATE_CHANGED.equals(action) || Intent.ACTION_TIME_CHANGED.equals(action)) {
                setNewHourValue();
            }
        }
    };

    private void clearData() {
        if (mLastStepDayTime != 0) {
            LogUtils.i("clearData");
            saveDataPerHour(true);
            resetValues();
        }
        initDayTimeValue();
    }

    private void setRepeatTask(String action, long time) {
        Log.i(TAG, "setRepeatTask");
        Intent intent = new Intent(action);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(alarmIntent);
        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),
                time, alarmIntent);
    }

    private void setNewDayValue() {
        initDayTimeValue();
        Intent intent = new Intent(Constants.ACTION_CLEAR_DATA);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(alarmIntent);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME, mLastStepDayTime, AlarmManager.INTERVAL_DAY, alarmIntent);
        LogUtils.i("setNewValue when mLastStepDayTime=" + mLastStepDayTime);
    }

    private void setNewHourValue() {
        Log.i(TAG, "setNewHourValue ");
        Intent intent = new Intent(Constants.ACTION_SAVE_STEP_PER_HOUR);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(alarmIntent);
        Log.i(TAG, "TimeUtil.getNowHourTime(): " + TimeUtil.getNowHourTime());
//    	am.setRepeating(AlarmManager.ELAPSED_REALTIME, TimeUtil.getNowHourTime(), AlarmManager.INTERVAL_HOUR, alarmIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, TimeUtil.getNowHourTime() + AlarmManager.INTERVAL_HOUR, alarmIntent);
        } else
            am.set(AlarmManager.RTC_WAKEUP, TimeUtil.getNowHourTime() + AlarmManager.INTERVAL_HOUR, alarmIntent);

    }

    /**
     * @Description 获得本地需要更新的数据，并进行更新
     * @author xiezhidong@pajk.cn
     */

    public void uploadData() {
        //上一次更新时间
        long lastSynTime = mState.getLong("lastSynTime", -1);
        if (mSynTime > lastSynTime) {
            Log.i(TAG, "Need sync ");
            Log.d(TAG, "lastSynTime:" + lastSynTime + "mSynTime" + mSynTime);
//			List<WalkDataInfo> list = StepDBManger.loadWalkDataInfoList(mSynTime, lastSynTime);
            StepDBManger.LoadWalkDataInfoPerHourTask loadtask = new StepDBManger.LoadWalkDataInfoPerHourTask(mSynTime, lastSynTime, new StepDBManger.LoadWalkDataInfoListTaskListener() {

                @Override
                public void onPost(List<WalkDataInfo> result) {
                    Log.d(TAG, "doSaveOrUpdateBatchPersonalWalkData todo");
                    if (result == null || result.size() <= 0) {
                        Intent intent = new Intent(Constants.ACTION_UPLOAD_STEP_DATA_KO);
                        sendBroadcast(intent);
                        return;
                    }
                    Log.d(TAG, "doSaveOrUpdateBatchPersonalWalkData goto, result.size:" + result.size());
                    try {
//						NetManager.getInstance(StepService.this).doSaveOrUpdateBatchPersonalWalkData(result, new OnSaveOrUpdateUserInfoListener() {
//
//							@Override
//							public void onInernError(int errorCode, String errorMessage) {
//								Intent intent = new Intent(Constants.ACTION_UPLOAD_STEP_DATA_KO);
//								sendBroadcast(intent);
//							}
//
//							@Override
//							public void onComplete(boolean isOK, Boolean value, int errorCode,
//									String errorMsg) {
//								Log.d(TAG, "result:" + value + "errorcode:" + errorCode + "msg:" + errorMsg);
//								if(value) {
//							         mStateEditor = mState.edit();
//									 mStateEditor.putLong("lastSynTime", mSynTime);
//									 mStateEditor.apply();
//									 StepDBManger.cleanWalkDataInfoPerHourList();
//								} else  {
//
//								}
//								Intent intent = new Intent(Constants.ACTION_UPLOAD_STEP_DATA_OK);
//								sendBroadcast(intent);
//							}
//
//						});
                    } catch (Exception e) {
                        e.printStackTrace();
                        Intent intent = new Intent(Constants.ACTION_UPLOAD_STEP_DATA_KO);
                        sendBroadcast(intent);
                    }

                }

            });
            loadtask.execute();
        } else {
            Intent intent = new Intent(Constants.ACTION_UPLOAD_STEP_DATA_KO);
            sendBroadcast(intent);
        }
    }

    private Handler mHandler = new Handler();
    /**
     * @Description 每小时直接广播通知执行保存数据 ---延迟5秒执行
     * @author xiezhidong@pajk.cn
     */
    private void delayUpdata() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Constants.ACTION_UPLOAD_STEP);
                sendBroadcast(intent);
            }
        }, 5000);
    }

    /**
     * @param isdaily 是否进行当日数据保存---0点以后记做前一日数据，0点之前记做今日数据
     * @Description 保存上一次记录小时的数据
     * @author xiezhidong@pajk.cn
     */
    private void saveDataPerHour(boolean isdaily) {
        long lastHourTime = mState.getLong("lastHourTime", -1);
        mSynTime = System.currentTimeMillis();
//		SaveWalkDataInfoPerHourTask savetask = new SaveWalkDataInfoPerHourTask(
//				mSteps, mDistance, lastHourTime + 2, mCalories);
        StepDBManger.SaveWalkDataInfoPerHourTask savetask = new StepDBManger.SaveWalkDataInfoPerHourTask(
                mSteps, mDistance, mSynTime, mCalories);
        LogUtils.d("loza--save() mSteps:" + mSteps + ",mDistance:" + mDistance
                + ",mSynTime:" + mSynTime + ",mCalories:" + mCalories);
        if (isdaily) {
            savetask.setDailySave();
        }
        savetask.execute();

        mStateEditor = mState.edit();
        mStateEditor.putLong("lastHourTime", mSynTime);
        mStateEditor.apply();

//		mSynTime = lastHourTime + 2;
    }


    /**
     * @return
     * @Description 是否超过1个小时需要进行记录
     * @author xiezhidong@pajk.cn
     */

    private boolean IsNeedSaveData() {
        long lastHourTime = mState.getLong("lastHourTime", -1);
        if (lastHourTime == -1) {
            mStateEditor = mState.edit();
            mStateEditor.putLong("lastHourTime", TimeUtil.getNowHourTime());
            mStateEditor.apply();
            return false;
        }

        // 由于闹钟提醒时间浮动，故缩短2分钟内的误差容错
        if (System.currentTimeMillis() >= lastHourTime
                + AlarmManager.INTERVAL_HOUR - 60 * 1000 * 2) {
            return true;
        }
        return false;
    }

    //    private void downloadData(long personId) {
//    	long time = System.currentTimeMillis();
//    	NetManager.getInstance(getApplicationContext()).doLoadWalkData(personId,
//    			time - 30 * Constants.MIL_SECONDS_DAY, time, "DAY",
//    			new OnLoadWalkDataListener() {
//			@Override
//			public void onComplete(boolean result, List<PedometerInfo> list,
//					int errorCode, String errorMsg) {
//				Log.d(TAG, "result:" + result + "errorcode:" + errorCode + "msg:" + errorMsg);
//				if(result) {
//					PedometerDepositary PedoDepo = new PedometerDepositary(getApplicationContext());
//					PedoDepo.save(list);
//				}
//			}
//			
//		});
//    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private void initDayTimeValue() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        mLastStepDayTime = (long) (cal.getTimeInMillis() / 1000);
        mLastStepDayTime *= 1000;

        mStateEditor = mState.edit();
        mStateEditor.putLong("daytime", mLastStepDayTime);
        mStateEditor.apply();
        LogUtils.i("Last day time inited as: " + String.valueOf(mLastStepDayTime));
    }

    private long getStepTime(long current) {
        long noWalkingTime;
        if (current - mLastStepTime > Constants.THRESHOLD) {
            noWalkingTime = current - mLastStepTime;
//			mStepBuf = 0;
        } else {
            noWalkingTime = 0;
        }
        mStepTime += current - mLastStepTime - noWalkingTime;
        mLastStepTime = current;
        return mStepTime;
    }

    public void registerCallback(Pedometer.IStepCallback cb) throws RemoteException {
        if (cb != null) {
            mCallbacks.add(cb);
        }
    }

    public void unregisterCallback(Pedometer.IStepCallback cb) throws RemoteException {
        if (cb != null) {
            mCallbacks.remove(cb);
        }
    }

    public void setValues(int mSteps, float distance, float cal) {
        this.mSteps = mSteps;
        this.mDistance = distance;
        this.mCalories = cal;
    }

    public float getDistance() {
        return mDistance;
    }

    public int getSteps() {
        return mSteps;
    }

    public void reset() throws RemoteException {
        mSteps = 0;
        SharedPreferences.Editor editor = mState.edit();
        editor.putInt("steps", 0);
        editor.apply();
    }

    public float getSpeed() {
        return mSpeed;
    }

    public float getCalories() {
        return mCalories;
    }

    public long getTime() {
        return mStepTime;
    }
}


