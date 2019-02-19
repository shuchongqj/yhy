package com.quanyan.pedometer.newpedometer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.lidroid.xutils.util.LogUtils;
import com.quanyan.pedometer.core.Constants;
import com.quanyan.pedometer.core.IStepListener;
import com.quanyan.pedometer.core.PartialWakeLock;
import com.quanyan.pedometer.core.Pedometer;
import com.quanyan.pedometer.utils.PreferencesUtils;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.yhy.common.beans.net.model.pedometer.SyncParamList;
import com.yhy.common.beans.net.model.pedometer.SyncResult;
import com.yhy.common.types.AppDebug;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created with Android Studio.
 * Title:StepService
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/6/20
 * Time:10:02
 * Version 1.1.0
 */
public class StepService extends Service {

    private static final String TAG = "StepService_new";

    public static final String PEDOMETER_POINT_COPY = "pedometer_point_copy";
    public static final String PEDOMETER_SHARE_URL = "pedometer_share_url";
    public static final String PEDOMETER_INVITE_SHARE_TEXT = "pedometer_invite_share_text";
    public static final String PEDOMETER_SAHRE_URL_QR_CODE = "pedometer_sahre_url_qr_code";
    public static final String PEDOMETER_SHARE_WEIBO_TOPIC_NAME = "pedometer_share_weibo_topic_name";
    public static final String PEDOMETER_SHARE_MASTER_CIRCLE_TOPIC_NAME = "pedometer_share_master_circle_topic_name";


    public static final String PEDOMETER_USER_INFO_AGE = "pedometer_user_info_age";
    public static final String PEDOMETER_USER_INFO_HEIGHT = "pedometer_user_info_height";
    public static final String PEDOMETER_USER_INFO_WEIGHT = "pedometer_user_info_weight";
    public static final String PEDOMETER_USER_INFO_SHARE_CODE = "pedometer_user_info_share_code";
    public static final String PEDOMETER_VIEW_MAX_STEPS = "pedometer_view_max_steps";
    //微博分享文本
    public static final String PEDOMETER_SAHRE_TEXT = "pedometer_sahre_text";

    //    public static final String THRESHOLD_VALUE = "threshold_value";
    public static final String TEMP_PACE = "temp_pace";
    public static final String TEMP_STEPS = "temp_steps";
    public static final String TEMP_SPEED = "temp_speed";
    public static final String TEMP_STEP_TIME = "temp_step_time";
    public static final String TEMP_CALORIES = "temp_calories";
    public static final String TEMP_DISTANCES = "temp_distances";
    //    public static final String LAST_STEP_NUM = "last_step_num";
//    public static final String LAST_SAVED_CALORIES = "last_saved_calories";
//    public static final String LAST_SAVED_DISTANCE = "last_saved_distance";
    public static final String LAST_STEP_DAY_TIME = "last_step_day_time";
    public static final String LAST_SERVER_SYN_TIME = "last_server_syn_time";
    public static final String PEDOMETER_INITIAL = "pedometer_initial";
    public static final String LASTSYNTIME = "lastSynTime";

    private static StepService mInstance = null;
    private IBinder mIBinder = new StepService.StepBinder();

    private SensorManager mSensorManager;
    private Sensor mAccelerationSensor;
    private Sensor mStepSensor;

    private StepDetect mStepDetect;
    private PaceNotifier mPaceNotifier;

    private long mLastStepDayTime = 0;
    /**
     * 记录步长
     */
    private double mTempPace = 0;
    /**
     * 当日总步数
     */
    private long mTempSteps = 0;
    /**
     * 当日消耗的总卡路里
     */
    private double mTempCalories = 0;
    /**
     * 当日走的总距离
     */
    private double mTempDistance = 0;

    /**
     * 行走速度
     */
    private double mTempSpeed = 0;
    /**
     * 行走时间
     */
    private long mTempStepTime = 0;

//    /**
//     * 步数同步时间
//     */
//    private long mSynTime = 0;

//    /**
//     * 步数上传的阈值
//     */
//    private int threshold_value = 0;
//    /**
//     * 上次同步时的步数
//     */
//    private long mLastSavedSteps = 0;
//    /**
//     * 上次同步时的卡路里
//     */
//    private double mLastSavedCalories = 0;
//    /**
//     * 上次同步时的距离
//     */
//    private double mlastSavedDistance = 0;
    /**
     * PedometerUtil中注册的监听器，在具体Activity中通知步数变化
     */
    private ArrayList<PedometerUtil.IStepCallback> mCallbacks;

    private DecimalFormat mDecimalFormat = new DecimalFormat("#.######");

    public static StepService getInstance() {
        return mInstance;
    }

    private boolean isUploading = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mCallbacks = new ArrayList<>();

        StepDBManager.getDefaultDbUtils(this);

//        threshold_value = PreferencesUtils.getInt(getApplicationContext(), THRESHOLD_VALUE, 300);
        PedometerUtil.getInstance().setHeight(PreferencesUtils.getInt(getApplicationContext(), PEDOMETER_USER_INFO_HEIGHT, 175));
        PedometerUtil.getInstance().setWeight(PreferencesUtils.getInt(getApplicationContext(), PEDOMETER_USER_INFO_WEIGHT, 60));

        mTempPace = PreferencesUtils.getFloat(getApplicationContext(), TEMP_PACE, 0);
        mTempSteps = PreferencesUtils.getLong(getApplicationContext(), TEMP_STEPS, 0);
        mTempSpeed = PreferencesUtils.getFloat(getApplicationContext(), TEMP_SPEED, 0);
        mTempStepTime = PreferencesUtils.getLong(getApplicationContext(), TEMP_STEP_TIME, 0);
        mTempCalories = PreferencesUtils.getFloat(getApplicationContext(), TEMP_CALORIES, 0);
        mTempDistance = PreferencesUtils.getFloat(getApplicationContext(), TEMP_DISTANCES, 0);

//        mLastSavedSteps = PreferencesUtils.getLong(getApplicationContext(), LAST_STEP_NUM, 0);
//        mLastSavedCalories = PreferencesUtils.getFloat(getApplicationContext(), LAST_SAVED_CALORIES, 0);
//        mlastSavedDistance = PreferencesUtils.getFloat(getApplicationContext(), LAST_SAVED_DISTANCE, 0);

        mLastStepDayTime = PreferencesUtils.getLong(getApplicationContext(), LAST_STEP_DAY_TIME, 0);

//        mSynTime = System.currentTimeMillis();

        //格式化double型数值
        mTempPace = Double.parseDouble(mDecimalFormat.format(mTempPace));
        mTempSpeed = Double.parseDouble(mDecimalFormat.format(mTempSpeed));
        mTempCalories = Double.parseDouble(mDecimalFormat.format(mTempCalories));
        mTempDistance = Double.parseDouble(mDecimalFormat.format(mTempDistance));
//        mLastSavedCalories = Double.parseDouble(mDecimalFormat.format(mLastSavedCalories));
//        mlastSavedDistance = Double.parseDouble(mDecimalFormat.format(mlastSavedDistance));

        mSensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        mAccelerationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mStepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        //检测步数的变化
        mStepDetect = StepDetect.newInstance();
        //监听不行距离的变化
        mPaceNotifier = new PaceNotifier();
//        mPaceNotifier.setPace(mTempPace);

        registerDetector();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Constants.ACTION_UPLOAD_STEP);
        filter.addAction(Constants.ACTION_UPLOAD_STEP_DATA_OK);
        filter.addAction(Constants.ACTION_UPLOAD_STEP_DATA_KO);
        filter.addAction(Constants.ACTION_SAVE_STEP_PER_HOUR);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        registerReceiver(mReceiver, filter);

        /**
         * 重复闹钟，一天的时间
         */
        setRepeatTask(Constants.ACTION_UPLOAD_STEP, AlarmManager.INTERVAL_DAY);

        if (mLastStepDayTime != 0) {
            long current = System.currentTimeMillis();
            if (current - mLastStepDayTime >= Constants.MIL_SECONDS_DAY) {
                LogUtils.d("upload steps : 超过一天");
//                uploadData(mTempSteps - mLastSavedSteps, mTempDistance - mlastSavedDistance,
//                        mTempCalories - mLastSavedCalories, mTempSpeed, mTempPace,
//                        System.currentTimeMillis());
                uploadDataDay(mTempSteps, mTempDistance, mTempCalories, System.currentTimeMillis(), mLastStepDayTime, "程序启动    跨天");
                resetDayValue();
            } else {
                final long lastSynTime = PreferencesUtils.getLong(getApplicationContext(), LASTSYNTIME, -1);
                if (lastSynTime != -1 && mLastStepDayTime > lastSynTime + Constants.MIL_SECONDS_DAY) {
                    uploadOldData(lastSynTime);
                }
            }
        } else {
//            uploadData(mTempSteps - mLastSavedSteps, mTempDistance - mlastSavedDistance,
//                    mTempCalories - mLastSavedCalories, mTempSpeed, mTempPace,
//                    System.currentTimeMillis());
//            mLastSavedSteps = mTempSteps;
//            mlastSavedDistance = Double.parseDouble(mDecimalFormat.format(mTempDistance));
//            mLastSavedCalories = Double.parseDouble(mDecimalFormat.format(mTempCalories));
            initNewDayTimeValue();
        }

//        final long lastSynTime = PreferencesUtils.getLong(getApplicationContext(), LASTSYNTIME, -1);
//        if(mLastStepDayTime <= lastSynTime - Constants.MIL_SECONDS_DAY) {
//            /**进程杀死之后，重启需要从服务器获取新的历史数据**/
//            NetManager.getInstance(this).doGetHistoryPedometerInfo(new OnResponseListener<PedometerHistoryResultList>() {
//                @Override
//                public void onComplete(boolean isOK, PedometerHistoryResultList result, int errorCode, String errorMsg) {
//                    if (isOK && result != null) {
//                        Message message = Message.obtain();
//                        message.what = StepFragment2.MSG_GET_SERVER_DATA_OK;
//                        message.obj = result;
//                        mHandler.sendMessage(message);
//                        return;
//                    }
//                }
//
//                @Override
//                public void onInternError(int errorCode, String errorMessage) {
//                }
//            });
//        }
    }

//    private Handler mHandler = new Handler(Looper.getMainLooper()) {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case StepFragment2.MSG_GET_SERVER_DATA_OK:
//                    PedometerHistoryResultList data = (PedometerHistoryResultList) msg.obj;
//                    List<PedometerHistoryResult> dataList = null;
//                    if (data != null && data.pedometerHistoryResultList != null && data.pedometerHistoryResultList.size() > 0) {
//                        dataList = data.pedometerHistoryResultList;
//                        StepDBManager.getDefaultDbUtils(getApplicationContext());
//                        new StepDBManager.SaveAndLoadWalkDataInfoListTask(StepDBManager.DataChange2(dataList), null).execute();
//                    }
//                    break;
//            }
//        }
//    };
//    public void setThreshold_value(int threshold_value) {
//        this.threshold_value = threshold_value;
//    }

//    public void resetValue(long stepCount, double distance, double calories) {
//        long a = mTempSteps - mLastSavedSteps;
//        double b = Double.parseDouble(mDecimalFormat.format(mTempDistance - mlastSavedDistance));
//        double c = Double.parseDouble(mDecimalFormat.format(mTempCalories - mLastSavedCalories));
//
//        this.mTempSteps = stepCount + a;
//        this.mTempDistance = Double.parseDouble(mDecimalFormat.format(distance + b));
//        this.mTempCalories = Double.parseDouble(mDecimalFormat.format(calories + c));
//
//        this.mLastSavedSteps = stepCount;
//        this.mlastSavedDistance = Double.parseDouble(mDecimalFormat.format(distance));
//        this.mLastSavedCalories = Double.parseDouble(mDecimalFormat.format(calories));
//        callback(mTempSteps);
//    }

    public double getDistance() {
        return mTempDistance;
    }

    public long getSteps() {
        return mTempSteps;
    }

    public double getCalories() {
        return mTempCalories;
    }

    public double getSpeed() {
        return mTempSpeed;
    }

    public long getTime() {
        return mTempStepTime;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("on destroy");
        saveData();

        unregisterReceiver(mReceiver);
        // Stop detecting
        unregisterDetector();

        cancelRepeatTask(Constants.ACTION_UPLOAD_STEP);
        cancelSleepAlarm();

        mInstance = null;
        Intent localIntent = new Intent();
        localIntent.setClass(this, StepService.class); //销毁时重新启动Service
        startService(localIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (!PreferencesUtils.getBoolean(getApplicationContext(), PEDOMETER_INITIAL)) {
            registerDetector();
        }
        return mIBinder;
    }

    public class StepBinder extends Binder {
        public StepService getService() {
            return StepService.this;
        }
    }

    private void cancelRepeatTask(String action) {
        Intent intent = new Intent(action);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(alarmIntent);
    }

    private void setRepeatTask(String action, long internerTime) {
        LogUtils.i("setRepeatTask");
        Intent intent = new Intent(action);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(alarmIntent);
        alarmIntent.cancel();

//        long firstTime = SystemClock.elapsedRealtime(); // 开机之后到现在的运行时间(包括睡眠时间)
        long systemTime = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(systemTime);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 58);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MILLISECOND, 0);

        long selectTime = calendar.getTimeInMillis();
        // 计算现在时间到设定时间的时间差
        long time = selectTime - systemTime;
//        firstTime += time;
        systemTime += time;

        am.setRepeating(AlarmManager.RTC_WAKEUP, systemTime, internerTime, alarmIntent);
    }

    public void registerDetector() {
        LogUtils.d("加速度传感器     注册监听器");
        mPaceNotifier.addListener(mPaceListener);
        mStepDetect.addListener(mStepListener);
        mStepDetect.addListener(mPaceNotifier);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            mSensorManager.registerListener(mStepDetect, mStepSensor, SensorManager.SENSOR_DELAY_NORMAL);
//        }else{
        mSensorManager.registerListener(mStepDetect, mAccelerationSensor, SensorManager.SENSOR_DELAY_UI);
//        }
        PreferencesUtils.putBoolean(getApplicationContext(), PEDOMETER_INITIAL, true);
    }

    public void unregisterDetector() {
        LogUtils.d("加速度传感器     取消监听器");
        mPaceNotifier.removeListener(mPaceListener);
        mStepDetect.removeListener(mStepListener);
        mStepDetect.removeListener(mPaceNotifier);
        mSensorManager.unregisterListener(mStepDetect);
        PreferencesUtils.putBoolean(getApplicationContext(), PEDOMETER_INITIAL, false);
    }

    /**
     * 通过Pedometer注册的监听器，通知到具体页面的数据变化
     *
     * @param iStepCallback
     * @throws RemoteException
     */
    public void registerCallback(PedometerUtil.IStepCallback iStepCallback) throws RemoteException {
        if (iStepCallback != null) {
            if (!mCallbacks.contains(iStepCallback)) {
                mCallbacks.add(iStepCallback);
            }
        }
    }

    /**
     * 注销Activity页面中通过pedometer注册的监听器
     *
     * @param iStepCallback
     * @throws RemoteException
     */
    public void unregisterCallback(PedometerUtil.IStepCallback iStepCallback) throws RemoteException {
        if (iStepCallback != null) {
            if (mCallbacks.contains(iStepCallback)) {
                mCallbacks.remove(iStepCallback);
            }
        }
    }

    /**
     * 每当步数变化时通知Activity
     *
     * @param step 当日步数的总值
     */
    private void callback(long step) {
        int N = mCallbacks.size();
        for (int i = 0; i < N; i++) {
            mCallbacks.get(i).onStep(step);
        }
    }

    /**
     * 初始化当日的数据
     */
    private void initNewDayTimeValue() {
        if (mLastStepDayTime > 0) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
            long timeFormat = (cal.getTimeInMillis() / 1000) * 1000;
            if ((timeFormat - mLastStepDayTime) >= (2 * Constants.MIL_SECONDS_DAY)) {
                mLastStepDayTime = timeFormat;
            } else {
                if (mLastStepDayTime > timeFormat) {
                    mLastStepDayTime = timeFormat;
                } else {
                    cal.setTime(new Date(mLastStepDayTime));
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                    long timeFormat1 = (cal.getTimeInMillis() / 1000) * 1000;
                    mLastStepDayTime = timeFormat1;
                }
            }
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
            long timeFormat2 = (cal.getTimeInMillis() / 1000) * 1000;
            mLastStepDayTime = timeFormat2;
        }
        LogUtils.i("初始化当日时间 ： " + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(mLastStepDayTime));

        PreferencesUtils.putLong(getApplicationContext(), LAST_STEP_DAY_TIME, mLastStepDayTime);
    }

    /**
     * 当时间超过一天，重置数据
     */
    public void resetDayValue() {
        initNewDayTimeValue();
//        mPaceNotifier.setPace(0);
        mTempSteps = 0;
        mTempDistance = 0;
        mTempCalories = 0;
        mTempPace = 0;
        mTempSpeed = 0;
        mTempStepTime = 0;

//        mLastSavedSteps = 0;
//        mLastSavedCalories = 0;
//        mlastSavedDistance = 0;
        saveData();
    }

    /**
     * 当步数超过一定值保存数据，减少数据丢失
     */
    public void saveData() {
        PreferencesUtils.putLong(getApplicationContext(), TEMP_STEPS, mTempSteps);
        PreferencesUtils.putFloat(getApplicationContext(), TEMP_DISTANCES, (float) mTempDistance);
        PreferencesUtils.putFloat(getApplicationContext(), TEMP_CALORIES, (float) mTempCalories);
        PreferencesUtils.putFloat(getApplicationContext(), TEMP_PACE, (float) mTempPace);
        PreferencesUtils.putFloat(getApplicationContext(), TEMP_SPEED, (float) mTempSpeed);
        PreferencesUtils.putLong(getApplicationContext(), TEMP_STEP_TIME, mTempStepTime);

//        PreferencesUtils.putLong(getApplicationContext(), LAST_STEP_NUM, mLastSavedSteps);
//        PreferencesUtils.putFloat(getApplicationContext(), LAST_SAVED_CALORIES, (float) mLastSavedCalories);
//        PreferencesUtils.putFloat(getApplicationContext(), LAST_SAVED_DISTANCE, (float) mlastSavedDistance);
    }

    /**
     * Forwards step values from StepDisplayer to the activity.
     */
    private IStepListener mStepListener = new IStepListener() {

        @Override
        public void onStep(int value) {
            LogUtils.i("step : " + value);
            if (System.currentTimeMillis() - mLastStepDayTime >= Constants.MIL_SECONDS_DAY) {
                LogUtils.d("upload steps : 超过一天");
//                uploadData(mTempSteps - mLastSavedSteps, mTempDistance - mlastSavedDistance,
//                        mTempCalories - mLastSavedCalories, mTempSpeed, mTempPace,
//                        System.currentTimeMillis());
                uploadDataDay(mTempSteps, mTempDistance, mTempCalories, System.currentTimeMillis(), mLastStepDayTime, "onStep    跨天");
                resetDayValue();
            }

            mTempSteps += value;
            callback(mTempSteps);//通知具体Activity的步数变化
//            if (mTempSteps % 20 == 0) {
//            }
            saveData();

//            if (mTempSteps - mLastSavedSteps >= threshold_value) {
//                LogUtils.d("upload steps : 达到阈值");
//                uploadData(mTempSteps - mLastSavedSteps, mTempDistance - mlastSavedDistance,
//                        mTempCalories - mLastSavedCalories, mTempSpeed, mTempPace,
//                        System.currentTimeMillis());
//                mLastSavedSteps = mTempSteps;
//                mlastSavedDistance = Double.parseDouble(mDecimalFormat.format(mTempDistance));
//                mLastSavedCalories = Double.parseDouble(mDecimalFormat.format(mTempCalories));
//            }

//            DecimalFormat decimalFormat = new DecimalFormat("#.##");
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(StepService.this);
//            builder.setSmallIcon(R.mipmap.ic_app_launcher)
//                    .setContentTitle("今日历程")
//                    .setContentText("步数：" + mTempSteps + "   距离：" + (decimalFormat.format(mTempDistance / 1000)) + "   卡路里：" + mTempCalories)
//                    .setSubText("")
//                    .setAutoCancel(false);
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
//            notificationManager.notify(1,builder.build());
        }

        @Override
        public void onStateChanged(int value) {

        }
    };

    /**
     * 每走一步计算得到的距离和卡路里值
     */
    private PaceNotifier.PaceListener mPaceListener = new PaceNotifier.PaceListener() {
        @Override
        public void paceChanged(double distance, double cal, double pace, double speed) {
            mTempDistance = Double.parseDouble(mDecimalFormat.format(mTempDistance + (distance / 1000)));
            mTempCalories = Double.parseDouble(mDecimalFormat.format(mTempCalories + cal));
            mTempSpeed = Double.parseDouble(mDecimalFormat.format(speed));
            mTempPace = Double.parseDouble(mDecimalFormat.format(pace));
            LogUtils.i("Step forward:" + String.valueOf(mTempSteps) + " mDistance is: "
                    + mTempDistance + " mCalories is: " + mTempCalories + "   pace : " + pace + "  speed : " + speed);
        }
    };

    private synchronized void uploadOldData(final long lastSynTime) {
        if (isUploading) {
            return;
        }
        isUploading = true;
        StepDBManager.UploadWalkDataDaily uploadWalkDataDaily = new StepDBManager.UploadWalkDataDaily(lastSynTime,
                new StepDBManager.LoadSuccessDailyData() {
                    @Override
                    public void success(List<WalkDataDaily> result) {
                        LogUtils.d("saved walk data threshold ok");
                        if (result == null || result.size() <= 0) {
                            Intent intent = new Intent(Constants.ACTION_UPLOAD_STEP_DATA_KO);
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                            isUploading = false;
                            return;
                        }
                        LogUtils.d("saved walk data threshold ok, result.size:" + result.size());
                        SyncParamList syncParamList = new SyncParamList();
                        syncParamList.syncParamList = StepDBManager.dataChange(lastSynTime, result);
                        try {
                            NetManager.getInstance((StepService.this)).doSyncHistoryData(syncParamList, new OnResponseListener<SyncResult>() {
                                @Override
                                public void onComplete(boolean isOK, SyncResult result, int errorCode, String errorMsg) {
                                    if (result != null) {
                                        PreferencesUtils.putLong(getApplicationContext(), LASTSYNTIME, result.syncDate);
                                    }
                                    isUploading = false;
                                }

                                @Override
                                public void onInternError(int errorCode, String errorMessage) {
                                    LogUtils.e("同步步数时网络请求失败");
                                    isUploading = false;
                                }
                            });
                        } catch (JSONException e) {
                            isUploading = false;
                            LogUtils.e("同步步数时网络请求失败");
                        }
                    }
                });
        uploadWalkDataDaily.execute();
    }

    private synchronized void uploadDataDay(long stepCount, double distance, double calories, final long synTime, final long lastStepDayTime, final String tag) {
        if (isUploading) {
            return;
        }

        isUploading = true;
        if (AppDebug.DEBUG_LOG) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS", Locale.SIMPLIFIED_CHINESE);
                    String text = "synchronize time : " + simpleDateFormat.format(new Date(synTime)) + tag + "\n";
                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + "step_debug_data.txt");
                    FileOutputStream fileOutputStream = null;
                    try {
                        fileOutputStream = new FileOutputStream(file, true);
                        fileOutputStream.write(text.getBytes());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
        //上一次更新时间
        final long lastSynTime = PreferencesUtils.getLong(getApplicationContext(), LASTSYNTIME, -1);
        LogUtils.i("Need sync ");
        StepDBManager.SaveWalkDataDaily saveWalkDataDaily = new StepDBManager.SaveWalkDataDaily(this, stepCount,
                distance, calories, lastStepDayTime, lastSynTime,
                new StepDBManager.LoadSuccessDailyData() {
                    @Override
                    public void success(List<WalkDataDaily> result) {
//                        sendBroadcast(new Intent(Constants.ACTION_OVER_DAY));
                        LogUtils.d("saved walk data threshold ok");
                        if (result == null || result.size() <= 0) {
                            Intent intent = new Intent(Constants.ACTION_UPLOAD_STEP_DATA_KO);
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                            isUploading = false;
                            return;
                        }
                        LogUtils.d("saved walk data threshold ok, result.size:" + result.size());
                        SyncParamList syncParamList = new SyncParamList();
                        syncParamList.syncParamList = StepDBManager.dataChange(lastSynTime, result);
                        try {
                            NetManager.getInstance((StepService.this)).doSyncHistoryData(syncParamList, new OnResponseListener<SyncResult>() {
                                @Override
                                public void onComplete(boolean isOK, SyncResult result, int errorCode, String errorMsg) {
                                    if (result != null) {
                                        PreferencesUtils.putLong(getApplicationContext(), LASTSYNTIME, result.syncDate);
                                    }
                                    isUploading = false;
                                }

                                @Override
                                public void onInternError(int errorCode, String errorMessage) {
                                    LogUtils.e("同步步数时网络请求失败");
                                    isUploading = false;
                                }
                            });
                        } catch (JSONException e) {
                            isUploading = false;
                            LogUtils.e("同步步数时网络请求失败");
                        }

                        Intent intent = new Intent(Constants.ACTION_OVER_DAY);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    }
                });
        saveWalkDataDaily.execute();
    }

    /**
     * 同步数据
     */
//    public synchronized void uploadData(long stepCount, double distance, double calories, double tempSpeed,
//                                        double tempPace, final long synTime) {
//        if(isUploading){
//            return;
//        }
//        isUploading = true;
//        //上一次更新时间
//        final long lastSynTime = PreferencesUtils.getLong(getApplicationContext(), LASTSYNTIME, -1);
//        LogUtils.i("Need sync ");
//        if (AppDebug.DEBUG_LOG) {
//            new StepDBManager.DebugData(stepCount, distance,
//                    calories, tempSpeed, tempPace, synTime).execute();
//        }
//        if (synTime > lastSynTime) {
//            LogUtils.d("mSynTime:" + synTime + "   mLastSynTime : " + lastSynTime + "   tempsteps : " + mTempSteps + "  lasttempsteps : " + mLastSavedSteps);
//                StepDBManager.SaveWalkDataPerThreshold saveWalkDataPerThreshold = new StepDBManager.SaveWalkDataPerThreshold(this,
//                        stepCount, distance, calories, synTime, lastSynTime, new StepDBManager.SavedSuccessTreshold() {
//                    @Override
//                    public void success(List<WalkDataThresholdGag> result) {
//                        LogUtils.d("saved walk data threshold ok");
//                        isUploading = false;
//                        if (result == null || result.size() <= 0) {
//                            Intent intent = new Intent(Constants.ACTION_UPLOAD_STEP_DATA_KO);
//                            sendBroadcast(intent);
//                            return;
//                        }
//                        final List<WalkDataThresholdGag> walkDataThresholdGags = result;
//                        LogUtils.d("saved walk data threshold ok, result.size:" + result.size());
//                        SyncParamList syncParamList = new SyncParamList();
//                        syncParamList.syncParamList = StepDBManager.dataChange(lastSynTime, result);
//                        try {
//                            NetManager.getInstance((StepService.this)).doSyncHistoryData(syncParamList, new OnResponseListener<SyncResult>() {
//                                @Override
//                                public void onComplete(boolean isOK, SyncResult result, int errorCode, String errorMsg) {
//                                    if (result != null) {
//                                        PreferencesUtils.putLong(getApplicationContext(), LASTSYNTIME, result.syncDate);
//                                    }
//                                    //删除数据
//                                    new StepDBManager.DeleteUploadData(walkDataThresholdGags).execute();
//                                    Intent intent = new Intent(Constants.ACTION_UPLOAD_STEP_DATA_OK);
//                                    sendBroadcast(intent);
//                                }
//
//                                @Override
//                                public void onInternError(int errorCode, String errorMessage) {
//                                    Intent intent = new Intent(Constants.ACTION_UPLOAD_STEP_DATA_KO);
//                                    sendBroadcast(intent);
//                                }
//                            });
//                        } catch (JSONException e) {
//                            Intent intent = new Intent(Constants.ACTION_UPLOAD_STEP_DATA_KO);
//                            sendBroadcast(intent);
//                        }
//                    }
//                });
//                saveWalkDataPerThreshold.execute();
//        } else {
//            isUploading = false;
//            Intent intent = new Intent(Constants.ACTION_UPLOAD_STEP_DATA_KO);
//            sendBroadcast(intent);
//        }
//    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                long current = System.currentTimeMillis();
                if (current - mLastStepDayTime >= Constants.MIL_SECONDS_DAY) {
                    LogUtils.d("通知超过了一天需要重置 : 超过一天");
//                        uploadData(mTempSteps - mLastSavedSteps, mTempDistance - mlastSavedDistance,
//                                mTempCalories - mLastSavedCalories, mTempSpeed, mTempPace,
//                                System.currentTimeMillis());
                    uploadDataDay(mTempSteps, mTempDistance, mTempCalories, System.currentTimeMillis(), mLastStepDayTime, "屏幕亮起    跨天");
                    resetDayValue();
                } else {
                    final long lastSynTime = PreferencesUtils.getLong(getApplicationContext(), LASTSYNTIME, -1);
                    if (lastSynTime != -1 && mLastStepDayTime > lastSynTime + Constants.MIL_SECONDS_DAY) {
                        uploadOldData(lastSynTime);
                    }
                }
                PartialWakeLock.getInstance().releaseWakeLock();
//                if (EnvironmentDetector.isXiaoMi()) {
                cancelSleepAlarm();
//                }
                if (PreferencesUtils.getBoolean(getApplicationContext(), PEDOMETER_INITIAL)) {
                    unregisterDetector();
                    registerDetector();
                }
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                PartialWakeLock.getInstance().acquireWakeLock(StepService.this);
//                if (EnvironmentDetector.isXiaoMi()) {
                setSleepAlarm(30000);
//                }
                // Unregisters the listener and registers it again.
                if (PreferencesUtils.getBoolean(getApplicationContext(), PEDOMETER_INITIAL)) {
                    unregisterDetector();
                    registerDetector();
                }
                LogUtils.i("Wake lock is: " + PartialWakeLock.getInstance().isHeld());

            } else if (Constants.ACTION_UPLOAD_STEP.equals(action)) {
                LogUtils.d("upload steps : 闹钟提醒");
//                uploadData(mTempSteps - mLastSavedSteps, mTempDistance - mlastSavedDistance,
//                        mTempCalories - mLastSavedCalories, mTempSpeed, mTempPace,
//                        System.currentTimeMillis());
                uploadDataDay(mTempSteps, mTempDistance, mTempCalories, System.currentTimeMillis(), mLastStepDayTime, "闹钟提醒");
                LogUtils.d("upload steps : 超过一天");
                resetDayValue();
            } else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                ConnectivityManager connectMgr = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (wifiNetInfo.isConnected()) {
                    long current = System.currentTimeMillis();
                    if (current - mLastStepDayTime >= Constants.MIL_SECONDS_DAY) {
                        LogUtils.d("upload steps : 超过一天");
//                        uploadData(mTempSteps - mLastSavedSteps, mTempDistance - mlastSavedDistance,
//                                mTempCalories - mLastSavedCalories, mTempSpeed, mTempPace,
//                                System.currentTimeMillis());
                        uploadDataDay(mTempSteps, mTempDistance, mTempCalories, System.currentTimeMillis(), mLastStepDayTime, "网络连接   跨天");
                        resetDayValue();
                    } else {
                        final long lastSynTime = PreferencesUtils.getLong(getApplicationContext(), LASTSYNTIME, -1);
                        if (lastSynTime != -1 && mLastStepDayTime > lastSynTime + Constants.MIL_SECONDS_DAY) {
                            uploadOldData(lastSynTime);
                        }
                    }
                    LogUtils.d("upload steps : 网络连接");
//                    uploadData(mTempSteps - mLastSavedSteps, mTempDistance - mlastSavedDistance,
//                            mTempCalories - mLastSavedCalories, mTempSpeed, mTempPace,
//                            System.currentTimeMillis());
//                    mLastSavedSteps = mTempSteps;
//                    mlastSavedDistance = Double.parseDouble(mDecimalFormat.format(mTempDistance));
//                    mLastSavedCalories = Double.parseDouble(mDecimalFormat.format(mTempCalories));
                    LogUtils.i("ConnectivityManager~~ connected");
                } else {
                }
            }
        }
    };

    public void setActive() {
//        if (!EnvironmentDetector.isScreenOn(this)) {
//            PartialWakeLock.getInstance().acquireWakeLock(this);
//        }
//        LogUtils.i("setActive");
//        this.mHeartBeatSetting.cancelAlarm(this);
//        resumeMotionTracker();
    }

    public void setSleepy() {
//        LogUtils.i("setSleepy");
//        pauseMotionTracker();
//        this.mHeartBeatSetting.cancelAlarm(this);
//        this.mHeartBeatSetting.setAlarm(this);
//        PartialWakeLock.getInstance().releaseWakeLock();
    }

    public void pauseMotionTracker() {
        LogUtils.i("unregister step tracker");
        this.mSensorManager.unregisterListener(mStepDetect);
    }

    public void resumeMotionTracker() {
        if (!Pedometer.getInstance().isDetectorRegistered(this)) {
            PartialWakeLock.getInstance().releaseWakeLock();
            return;
        }
        registerDetector();
    }

    private void cancelSleepAlarm() {
        Intent filter = new Intent("step_service_sleep");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, filter, 0);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.cancel(pendingIntent);
    }

    private void setSleepAlarm(long seconds) {
        LogUtils.d("device is screen off");
        IntentFilter intentFilter = new IntentFilter("step_service_sleep");
        registerReceiver(alarmREceiver, intentFilter);

        Intent filter = new Intent("step_service_sleep");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, filter, 0);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();

        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + seconds, seconds, pendingIntent);
    }

    private BroadcastReceiver alarmREceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.d("device is screen off : 申请不休眠");
            unregisterDetector();
            registerDetector();
        }
    };
}

