package com.yhy.location;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.yhy.common.utils.PermissionUtils;

import de.greenrobot.event.EventBus;

public class LocationManager {
    private static final LocationManager ourInstance = new LocationManager();

    private DestroyMapRunnable destroyMapRunnable;
    private LocationListener locationListener = new LocationListener();
    private Handler handler;

    private Context context;

    public static LocationManager getInstance() {
        return ourInstance;
    }

    private LocationManager() {
        handler = new Handler(Looper.getMainLooper());
    }

    public void init(Context context){
        this.context = context.getApplicationContext();
        LocationStorage.getInstance().init(this.context);
    }

    public void startLocation(Context context){
        handler.post(new StartLocationRunnable(context, new AMapLocationClient(context.getApplicationContext())));
    }

    public void stopLocation(){
        if (destroyMapRunnable != null){
            handler.removeCallbacks(destroyMapRunnable);
            handler.post(destroyMapRunnable);
        }
    }

    private class StartLocationRunnable implements Runnable{
        Context context;
        AMapLocationClient aMapLocManager;

        StartLocationRunnable(Context context, AMapLocationClient aMapLocManager) {
            this.context = context;
            this.aMapLocManager = aMapLocManager;
        }

        @Override
        public void run() {
            stopLocation();
            AMapLocationClientOption aMapLocationClientOption = new AMapLocationClientOption();
            aMapLocationClientOption.setHttpTimeOut(2000);
            aMapLocationClientOption.setInterval(2000);
            aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
            aMapLocManager.setLocationOption(aMapLocationClientOption);
            aMapLocManager.setLocationListener(locationListener);
            aMapLocManager.startLocation();

            destroyMapRunnable = new DestroyMapRunnable(aMapLocManager);
            handler.postDelayed(destroyMapRunnable, 10000);
        }
    }

    private class DestroyMapRunnable implements Runnable{
        AMapLocationClient aMapLocManager;

        DestroyMapRunnable(AMapLocationClient aMapLocManager) {
            this.aMapLocManager = aMapLocManager;
        }

        @Override
        public void run() {
            aMapLocManager.unRegisterLocationListener(locationListener);
            try {
                aMapLocManager.onDestroy();
            }catch (Exception e){
                e.printStackTrace();
            }
            aMapLocManager = null;
            destroyMapRunnable = null;
        }
    }


    private class LocationListener implements AMapLocationListener{

        @Override
        public void onLocationChanged(AMapLocation location) {
            stopLocation();// 销毁掉定位+
            if (location != null && location.getErrorCode() == 0 && PermissionUtils.checkLocationPermission(context)) {
                //位置管理类来做地理位置的处理
                String adCode = location.getAdCode();
                if (!TextUtils.isEmpty(adCode)){
                    if (adCode.length() > 4){
                        adCode = adCode.substring(0, adCode.length() - 2);
                        adCode = adCode + "00";
                    }
                }
                LocationStorage.getInstance().setGPRSLocation(location.getCity().replace("市", ""),adCode,
                        location.getDistrict(),"-1",
                        location.getLatitude(),location.getLongitude(), 1);
                EventBus.getDefault().post(new EvBusLocation(1001));
            } else {
                //蔡辉煌add,定位失败，取上一次的，上一次也没有默认选择深圳
                //位置管理类来做地理位置的处理
                LocationStorage.getInstance().setGPRSLocation(LocationStorage.getInstance().getLast_cityName(),LocationStorage.getInstance().getLast_cityCode(),
                        LocationStorage.getInstance().getLast_discName(),LocationStorage.getInstance().getLast_discCode(),
                        Double.valueOf(LocationStorage.getInstance().getLast_lat()),Double.valueOf(LocationStorage.getInstance().getLast_lng()), LocationStorage.getInstance().getLast_isPublic());
                EventBus.getDefault().post(new EvBusLocation(4001));
            }
        }
    }

    public LocationStorage getStorage(){
        return LocationStorage.getInstance();
    }
}
