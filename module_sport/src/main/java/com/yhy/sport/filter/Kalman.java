package com.yhy.sport.filter;

import com.amap.api.maps.model.LatLng;

/**
 * @Description: Kalman 滤波核心类
 * @Created by zhaolei.yang 2018-07-07 16:49
 */
public class Kalman {

    private double lastLocation_x; //上次位置
    private double currentLocation_x;//这次位置
    private double lastLocation_y; //上次位置
    private double currentLocation_y;//这次位置
    private double estimate_x; //修正后数据
    private double estimate_y; //修正后数据
    private double pdelt_x; //自预估偏差
    private double pdelt_y; //自预估偏差
    private double mdelt_x; //上次模型偏差
    private double mdelt_y; //上次模型偏差
    private double gauss_x; //高斯噪音偏差
    private double gauss_y; //高斯噪音偏差
    private double kalmanGain_x; //卡尔曼增益
    private double kalmanGain_y; //卡尔曼增益

    public double getLastLocation_x() {
        return lastLocation_x;
    }

    public void setLastLocation_x(double lastLocation_x) {
        this.lastLocation_x = lastLocation_x;
    }

    public double getCurrentLocation_x() {
        return currentLocation_x;
    }

    public void setCurrentLocation_x(double currentLocation_x) {
        this.currentLocation_x = currentLocation_x;
    }

    public double getLastLocation_y() {
        return lastLocation_y;
    }

    public void setLastLocation_y(double lastLocation_y) {
        this.lastLocation_y = lastLocation_y;
    }

    public double getCurrentLocation_y() {
        return currentLocation_y;
    }

    public void setCurrentLocation_y(double currentLocation_y) {
        this.currentLocation_y = currentLocation_y;
    }

    public double getEstimate_x() {
        return estimate_x;
    }

    public void setEstimate_x(double estimate_x) {
        this.estimate_x = estimate_x;
    }

    public double getEstimate_y() {
        return estimate_y;
    }

    public void setEstimate_y(double estimate_y) {
        this.estimate_y = estimate_y;
    }

    public double getPdelt_x() {
        return pdelt_x;
    }

    public void setPdelt_x(double pdelt_x) {
        this.pdelt_x = pdelt_x;
    }

    public double getPdelt_y() {
        return pdelt_y;
    }

    public void setPdelt_y(double pdelt_y) {
        this.pdelt_y = pdelt_y;
    }

    public double getMdelt_x() {
        return mdelt_x;
    }

    public void setMdelt_x(double mdelt_x) {
        this.mdelt_x = mdelt_x;
    }

    public double getMdelt_y() {
        return mdelt_y;
    }

    public void setMdelt_y(double mdelt_y) {
        this.mdelt_y = mdelt_y;
    }

    public double getGauss_x() {
        return gauss_x;
    }

    public void setGauss_x(double gauss_x) {
        this.gauss_x = gauss_x;
    }

    public double getGauss_y() {
        return gauss_y;
    }

    public void setGauss_y(double gauss_y) {
        this.gauss_y = gauss_y;
    }

    public double getKalmanGain_x() {
        return kalmanGain_x;
    }

    public void setKalmanGain_x(double kalmanGain_x) {
        this.kalmanGain_x = kalmanGain_x;
    }

    public double getKalmanGain_y() {
        return kalmanGain_y;
    }

    public void setKalmanGain_y(double kalmanGain_y) {
        this.kalmanGain_y = kalmanGain_y;
    }

    public double getM_R() {
        return m_R;
    }

    public void setM_R(double m_R) {
        this.m_R = m_R;
    }

    public double getM_Q() {
        return m_Q;
    }

    public void setM_Q(double m_Q) {
        this.m_Q = m_Q;
    }

    private double m_R = 0;
    private double m_Q = 0;

    //初始模型
    public void initial() {
        pdelt_x = 0.001;
        pdelt_y = 0.001;
//        mdelt_x = 0;
//        mdelt_y = 0;
        mdelt_x = 5.698402909980532E-4;
        mdelt_y = 5.698402909980532E-4;
    }

    public LatLng kalmanFilter(double oldValue_x, double value_x, double oldValue_y, double value_y) {
        lastLocation_x = oldValue_x;
        currentLocation_x = value_x;
        gauss_x = Math.sqrt(pdelt_x * pdelt_x + mdelt_x * mdelt_x) + m_Q;     //计算高斯噪音偏差
        kalmanGain_x = Math.sqrt((gauss_x * gauss_x) / (gauss_x * gauss_x + pdelt_x * pdelt_x)) + m_R; //计算卡尔曼增益
        estimate_x = kalmanGain_x * (currentLocation_x - lastLocation_x) + lastLocation_x;    //修正定位点
        mdelt_x = Math.sqrt((1 - kalmanGain_x) * gauss_x * gauss_x);      //修正模型偏差

        lastLocation_y = oldValue_y;
        currentLocation_y = value_y;
        gauss_y = Math.sqrt(pdelt_y * pdelt_y + mdelt_y * mdelt_y) + m_Q;     //计算高斯噪音偏差
        kalmanGain_y = Math.sqrt((gauss_y * gauss_y) / (gauss_y * gauss_y + pdelt_y * pdelt_y)) + m_R; //计算卡尔曼增益
        estimate_y = kalmanGain_y * (currentLocation_y - lastLocation_y) + lastLocation_y;    //修正定位点
        mdelt_y = Math.sqrt((1 - kalmanGain_y) * gauss_y * gauss_y);      //修正模型偏差

        LatLng latlng = new LatLng(estimate_y, estimate_x);


        return latlng;
    }
}
