package com.yhy.sport.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.yhy.sport.filter.GPSState;
import com.yhy.sport.filter.KalmanFilter;
import com.yhy.sport.filter.LocationWrapper;
import com.yhy.sport.filter.NoiseFilter;
import com.yhy.sport.filter.SportType;
import com.yhy.sport.util.Const;
import com.yhy.sport.util.MapUtil;

import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

import de.greenrobot.event.EventBus;

/**
 * @Description: 位置服务，集成噪声，GPS信号策略,Kalman滤波等
 * @Created by zhaolei.yang 2018-07-10 13:53
 */
public class LocationService extends Service implements AMapLocationListener {
    private GPSState mGPSState;
    private SportType mSportType;
    private KalmanFilter mKalmanFilter;
    private NoiseFilter mNoisePointerFilter;

    private AMapLocationClient mLocationClient;
    private DataProcessTask mDataProcessTask;

    private Queue<LocationWrapper> mProcessQueue = new PriorityBlockingQueue<>();

    @Override
    public void onCreate() {
        super.onCreate();

        initParameter();
        initLocation();
    }

    @Override
    public void onDestroy() {
        releaseLocation();

        super.onDestroy();
    }

    private void releaseLocation() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
            mLocationClient = null;
        }

        if (mDataProcessTask != null) {
            mDataProcessTask.terminate();
            mDataProcessTask = null;
        }

        if (mProcessQueue != null) {
            mProcessQueue.clear();
            mProcessQueue = null;
        }
    }

    private void initParameter() {
        mGPSState = new GPSState();
        mSportType = new SportType();
        mKalmanFilter = new KalmanFilter();
        mNoisePointerFilter = new NoiseFilter(mGPSState, mSportType);
    }

    private void initLocation() {
        mLocationClient = new AMapLocationClient(getApplicationContext());

        //设置位置回调监听，注意在合适时机释放回调
        mLocationClient.setLocationListener(this);
        mLocationClient.setLocationOption(getLocationOption());
        mLocationClient.stopLocation();
        mLocationClient.startLocation();

        mDataProcessTask = new DataProcessTask(Const.DATA_TASK_DELTA, mProcessQueue);
        mDataProcessTask.needTerminate = false;
        mDataProcessTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private AMapLocationClientOption getLocationOption() {
        AMapLocationClientOption option = new AMapLocationClientOption();

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //设置是否返回地址信息（默认返回地址信息）
        option.setNeedAddress(true);

        //关闭缓存机制
        option.setLocationCacheEnable(false);

        //获取一次定位结果：
        //该方法默认为false。
        option.setOnceLocation(false);

        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        option.setOnceLocationLatest(false);

        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        option.setInterval(Const.AMAP_INTERVAL);

        // 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
//        option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        return option;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    public class LocalBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation == null)
            return;

        String origin = String.format("Origin Latitude: %s , Longitude: %s", aMapLocation.getLatitude(), aMapLocation.getLongitude());
        Log.i(Const.TAG, origin);

        long temp = 0;
        long timeStamp;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            temp = MapUtil.nano2milli(aMapLocation.getElapsedRealtimeNanos());
        }

        timeStamp = temp == 0 ? aMapLocation.getTime() : temp;

        if (timeStamp == 0) {
            timeStamp = System.currentTimeMillis();
        }

        LocationWrapper wrapper = new LocationWrapper();
        wrapper.setTimeStamp(timeStamp).setLocation(aMapLocation);

        if (mProcessQueue != null) {
            mProcessQueue.add(wrapper);
        }
    }

    private final class DataProcessTask extends AsyncTask {
        private long delta;
        private boolean needTerminate = false;
        private Queue<LocationWrapper> mProcessQueue;

        DataProcessTask(long delta, Queue<LocationWrapper> mProcessQueue) {
            this.delta = delta;
            this.mProcessQueue = mProcessQueue;
        }

        public void terminate() {
            needTerminate = true;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            while (!needTerminate) {
                try {
                    Thread.sleep(delta);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    continue;
                }

                LocationWrapper wrapper;
                double lastTimeStamp = 0.0;
                while ((wrapper = mProcessQueue.poll()) != null) {
                    if (wrapper.getTimeStamp() < lastTimeStamp) {
                        continue;
                    }

                    lastTimeStamp = wrapper.getTimeStamp();
                    Location l = wrapper.getLocation();

                    String v = String.format("doInBackground lan %s, lon %s, wrapper time %s, location time %s", l.getLatitude(), l.getLongitude(), wrapper.getTimeStamp(), l.getTime());
                    Log.i(Const.TAG, v);

                    if (mGPSState == null) {
                        mGPSState = new GPSState();
                        mGPSState.registerListener(new GPSState.StateChangeListener() {
                            @Override
                            public void onStateChanged(GPSState.State state) {
                                Log.i(Const.TAG, "state is: " + state.getMsg());
                            }
                        });
                    }

                    mGPSState.check(l);

                    if (mNoisePointerFilter == null)
                        mNoisePointerFilter = new NoiseFilter(mGPSState, mSportType);

                    if (!mNoisePointerFilter.isValidate(wrapper))
                        continue;

                    if (mKalmanFilter == null)
                        mKalmanFilter = new KalmanFilter();

                    Log.i(Const.TAG, "8### Noise is Passed");
                    AMapLocation location = mKalmanFilter.process(wrapper);
                    if (location != null)
                        Log.i(Const.TAG, "9### Filter location is: " + location.getLatitude() + " " + location.getLongitude());

                    publishProgress(location);
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            if (values[0] != null) {
                processImp((AMapLocation) values[0]);
            } else {
                Log.i(Const.TAG, "Filter location is null, please check");
            }
        }

        private void processImp(AMapLocation location) {
            if (location == null || location.getLatitude() == 0 ||
                    location.getLongitude() == 0 ||
                    !location.getProvider().equals(Const.KLMAN_FILTER_PROVIDER)) {
                String filter = String.format("Filter Latitude: %s , Longitude: %s", location.getLatitude(), location.getLongitude());
                Log.i(Const.TAG, filter);
                return;
            }

            String filter = String.format("Filter Latitude: %s , Longitude: %s", location.getLatitude(), location.getLongitude());
            Log.i(Const.TAG, filter);

            //TODO listener
            EventBus.getDefault().post(location);
        }
    }
}
