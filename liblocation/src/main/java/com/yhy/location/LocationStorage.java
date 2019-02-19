package com.yhy.location;

import android.content.Context;

import com.yhy.common.utils.SPUtils;

/**
 * 用于管理位置新信息的管理类
 * Created by Jiervs on 2018/5/3.
 */

public class LocationStorage {

    private Context mContext;

    private LocationStorage() {
    }

    private volatile static LocationStorage locationManager;

    public static LocationStorage getInstance() {
        if (locationManager == null) {
            synchronized (LocationStorage.class) {
                if (locationManager == null) {
                    locationManager = new LocationStorage();
                }
            }
        }
        return locationManager;
    }

    public void init(Context mContext) {
        this.mContext = mContext;
    }

    //记录上一次定位(只有上一次成功GPRS定位并匹配城市 或者 手动选择城市 才记录保存)
    //sp的key
    private String last_cityName = "last_cityName";
    private String last_cityCode = "last_cityCode";
    private String last_discName = "last_discName";
    private String last_discCode = "last_discCode";
    private String last_isPublic = "last_isPublic";
    private String last_lat = "last_lat";
    private String last_lng = "last_lng";

    //gprs(sdk) 定位
    private String gprs_cityName = "深圳";
    private String gprs_cityCode = "440300";
    private String gprs_discName = "全部";
    private String gprs_discCode = "-1";
    private int gprs_isPublic = 1;
    private double gprs_lat = 22.3721;
    private double gprs_lng = 114.0412;

    //手动 定位
    private String manual_cityName = "深圳";
    private String manual_cityCode = "440300";
    private String manual_discName = "全部";
    private String manual_discCode = "-1";
    private int manual_isPublic = 1;
    private double manual_lat = 22.3721;
    private double manual_lng = 114.0412;

    /***************************************************            LogicMethod           ************************************************************/
    /**
     * 设置GPRS的定位
     */
    public void setGPRSLocation(String gprs_cityName, String gprs_cityCode,
                                String gprs_discName, String gprs_discCode,
                                double gprs_lat, double gprs_lng, int gprs_isPublic) {//获取GPRS定位
        if (null != gprs_cityName) setGprs_cityName(gprs_cityName);
        if (null != gprs_cityCode) setGprs_cityCode(gprs_cityCode);
        if (null != gprs_discName) setGprs_discName(gprs_discName);
        if (null != gprs_discCode) setGprs_discCode(gprs_discCode);
        setGprs_lat(gprs_lat);
        setGprs_lng(gprs_lng);
        setGprs_isPublic(gprs_isPublic);      //  gps定位拿到的数据是没有ispublick的，默认设置为0，不是自营，  当拿到城市列表匹配到城市的时候更新这个数据，如果没有匹配到（说明这个城市不在列表内肯定不是自营）就不更新
        //gprs定位成功
        isGPRSok = true;

        //同步 手动 定位
        setManualLocation(gprs_cityName, gprs_cityCode, gprs_discName, gprs_discCode, gprs_lat, gprs_lng, gprs_isPublic);
    }

    /**
     * 设置手动的定位
     */
    public void setManualLocation(String manual_cityName, String manual_cityCode,
                                  String manual_discName, String manual_discCode,
                                  double manual_lat, double manual_lng, int manual_isPublic) {
        if (null != manual_cityName) setManual_cityName(manual_cityName);
        if (null != manual_cityCode) setManual_cityCode(manual_cityCode);
        if (null != manual_discName) setManual_discName(manual_discName);
        if (null != manual_discCode) setManual_discCode(manual_discCode);
        setManual_lat(manual_lat);
        setManual_lng(manual_lng);
        setManual_isPublic(manual_isPublic);

        setLastLocation(manual_cityName, manual_cityCode, manual_discName, manual_discCode, manual_lat, manual_lng, manual_isPublic);
    }

    /**
     * 设置最后一次定位存sp
     */
    public void setLastLocation(String last_cityName, String last_cityCode,
                                String last_discName, String last_discCode,
                                double last_lat, double last_lng, int last_isPublic) {
        SPUtils.save(mContext, this.last_cityName, last_cityName);
        SPUtils.save(mContext, this.last_cityCode, last_cityCode);
        SPUtils.save(mContext, this.last_discName, last_discName);
        SPUtils.save(mContext, this.last_discCode, last_discCode);
        SPUtils.save(mContext, this.last_isPublic, last_isPublic);
        SPUtils.save(mContext, this.last_lat, String.valueOf(last_lat));
        SPUtils.save(mContext, this.last_lng, String.valueOf(last_lng));
    }

    /**
     * 初始化到深圳定位
     */
    public void gprslocationBackToSZ() {
        //gprs(sdk) 定位
        gprs_cityName = "深圳";
        gprs_cityCode = "440300";
        gprs_discName = "全部";
        gprs_discCode = "-1";
        gprs_lat = 22.3721;
        gprs_lng = 114.0412;
        gprs_isPublic = 1;
    }

    public void manuallocationBackToSZ() {
        //手动 定位
        manual_cityName = "深圳";
        manual_cityCode = "440300";
        manual_discName = "全部";
        manual_discCode = "-1";
        manual_lat = 22.3721;
        manual_lng = 114.0412;
        manual_isPublic = 1;
    }

    /**
     * 判断是否是GPRS和手动选择是否在同一个城市(是否异地)
     */
    private boolean isSameWithGPRS = true;

    public boolean isSameWithGPRS() {
        if (gprs_cityName.equals(manual_cityName)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否和上一次定位得到位置一样
     */
    public boolean isGPRSChange;

    public boolean isGPRSChange() {
        if (gprs_cityName.equals(getLast_cityName())) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断是否是GPRS定位成功
     */
    private boolean isGPRSok = false;

    public boolean isGPRSok() {
        return isGPRSok;
    }

    public void setGPRSok(boolean GPRSok) {
        isGPRSok = GPRSok;
    }

    /************************************************            Setter && Getter            ************************************************************/
    public String getGprs_cityName() {
        return gprs_cityName;
    }

    public void setGprs_cityName(String gprs_cityName) {
        this.gprs_cityName = gprs_cityName;
    }

    public String getGprs_cityCode() {
        return gprs_cityCode;
    }

    public void setGprs_cityCode(String gprs_cityCode) {
        this.gprs_cityCode = gprs_cityCode;
    }

    public String getGprs_discName() {
        return gprs_discName;
    }

    public void setGprs_discName(String gprs_discName) {
        this.gprs_discName = gprs_discName;
    }

    public String getGprs_discCode() {
        return gprs_discCode;
    }

    public void setGprs_discCode(String gprs_discCode) {
        this.gprs_discCode = gprs_discCode;
    }

    public double getGprs_lat() {
        return gprs_lat;
    }

    public void setGprs_lat(double gprs_lat) {
        this.gprs_lat = gprs_lat;
    }

    public double getGprs_lng() {
        return gprs_lng;
    }

    public void setGprs_lng(double gprs_lng) {
        this.gprs_lng = gprs_lng;
    }

    public void setGprs_isPublic(int gprs_isPublic) {
        this.gprs_isPublic = gprs_isPublic;
    }

    public int getGprs_isPublic() {
        return gprs_isPublic;
    }

    public String getManual_cityName() {
        return manual_cityName;
    }

    public void setManual_cityName(String manual_cityName) {
        this.manual_cityName = manual_cityName;
    }

    public String getManual_cityCode() {
        return manual_cityCode;
    }

    public void setManual_cityCode(String manual_cityCode) {
        this.manual_cityCode = manual_cityCode;
    }

    public String getManual_discName() {
        return manual_discName;
    }

    public void setManual_discName(String manual_discName) {
        this.manual_discName = manual_discName;
    }

    public String getManual_discCode() {
        return manual_discCode;
    }

    public void setManual_discCode(String manual_discCode) {
        this.manual_discCode = manual_discCode;
    }

    public double getManual_lat() {
        return manual_lat;
    }

    public void setManual_lat(double manual_lat) {
        this.manual_lat = manual_lat;
    }

    public double getManual_lng() {
        return manual_lng;
    }

    public void setManual_lng(double manual_lng) {
        this.manual_lng = manual_lng;
    }

    public void setManual_isPublic(int manual_isPublic) {
        this.manual_isPublic = manual_isPublic;
    }

    public String getLast_cityName() {
        return SPUtils.getString(mContext, this.last_cityName, "深圳");
    }

    public String getLast_cityCode() {
        return SPUtils.getString(mContext, this.last_cityCode, "440300");
    }

    public String getLast_discName() {
        return SPUtils.getString(mContext, this.last_discName, "全部");
    }

    public String getLast_discCode() {
        return SPUtils.getString(mContext, this.last_discCode, "-1");
    }

    public String getLast_lat() {
        return SPUtils.getString(mContext, this.last_lat, "22.3721");
    }

    public String getLast_lng() {
        return SPUtils.getString(mContext, this.last_lng, "114.0412");
    }

    public int getLast_isPublic() {
        return SPUtils.getInt(mContext, this.last_isPublic, 1);
    }


}
