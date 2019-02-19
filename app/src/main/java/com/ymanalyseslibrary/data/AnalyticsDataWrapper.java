package com.ymanalyseslibrary.data;

import com.ymanalyseslibrary.AnalysesConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:2/23/16
 * Time:10:35
 * Version 1.0
 */
public class AnalyticsDataWrapper {
    private String softwareName;//软件名称
    private String softwareId;//appid
    private String softwareVersion;//版本号
    private String mac;//mac地址
    private String IP;//ip地址
    private String networkingMde;//联网方式
    private String cityName;//定位城市名称
    private String termType;//终端类型（移动端、PC端、PAD端）
    private String os;//终端操作系统（安卓、IOS）
    private String osVersion;//终端操作系统版本号
    private String channelNumber;//渠道编号（推广渠道唯一性标示）
    private String channelName;//渠道名称（推广渠道名称）
    private String resolution;//终端分辨率
    private String terminalBrand;//终端品牌
    private String terminalType;//终端型号
    private String userId;//用户ID，用户登录的手机号。
    private String deviceId;//MEI
    private String data;//动作相关属性
    private String jsonData;//数据的jsonges
    private JSONArray jsonArray;
    private String domainId;//domin id

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getSoftwareName() {
        return softwareName;
    }

    public String getSoftwareId() {
        return softwareId;
    }

    public void setSoftwareId(String softwareId) {
        this.softwareId = softwareId;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public String getMac() {
        return mac;
    }

    public String getIP() {
        return IP;
    }

    public String getNetworkingMde() {
        return networkingMde;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTermType() {
        return termType;
    }

    public String getOs() {
        return os;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public String getChannelNumber() {
        return channelNumber;
    }

    public void setChannelNumber(String channelNumber) {
        this.channelNumber = channelNumber;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getResolution() {
        return resolution;
    }

    public String getTerminalBrand() {
        return terminalBrand;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public String getUserId() {
        return userId;
    }

    public String getData() {
        return data;
    }

    public AnalyticsDataWrapper(){
    }

    public void setSoftwareName(String softwareName) {
        this.softwareName = softwareName;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public void setNetworkingMde(String networkingMde) {
        this.networkingMde = networkingMde;
    }

    public void setTerminalBrand(String terminalBrand) {
        this.terminalBrand = terminalBrand;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public void setTermType(String termType) {
        this.termType = termType;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setData(String data) {
        this.data = data;
    }

    public JSONObject getJsonData(){
        JSONObject jsonObject = new JSONObject();
        jointJson(jsonObject);
        return jsonObject;
    }

    public String getStringJsonData(){
        JSONObject jsonObject = new JSONObject();
        jointJson(jsonObject);
        return jsonObject.toString();
    }

    //json拼装
    private void jointJson(JSONObject jsonObject) {
        try {
            jsonObject.putOpt(AnalysesConstants.BODY_KEY_SOFTWARENAME, softwareName);//软件名称
            jsonObject.putOpt(AnalysesConstants.BODY_KEY_SOFTWAREID, softwareId);//appid
            jsonObject.putOpt(AnalysesConstants.BODY_KEY_SOFTWAREVERSION, softwareVersion);//软件版本号
            jsonObject.putOpt(AnalysesConstants.BODY_KEY_MAC_ADDRESS, mac);//MAC地址
            jsonObject.putOpt(AnalysesConstants.BODY_KEY_IP_ADDRESS, IP);//IP地址
            jsonObject.putOpt(AnalysesConstants.BODY_KEY_NETWORKINGMDE, networkingMde);//网络类型
            jsonObject.putOpt(AnalysesConstants.BODY_KEY_LOCATION_NAME, cityName);//定位城市名称
            jsonObject.putOpt(AnalysesConstants.BODY_KEY_TERMTYPE, termType);//终端类型
            jsonObject.putOpt(AnalysesConstants.BODY_KEY_OS, os);//终端操作系统
            jsonObject.putOpt(AnalysesConstants.BODY_KEY_OSVERSION, osVersion);//终端操作系统版本号
            jsonObject.putOpt(AnalysesConstants.BODY_KEY_CHANNEL_NUMBER, channelNumber);//渠道编号
            jsonObject.putOpt(AnalysesConstants.BODY_KEY_CHANNEL_NAME, channelName);//渠道名称
            jsonObject.putOpt(AnalysesConstants.BODY_KEY_RESOLUTION, resolution);//终端使用率
            jsonObject.putOpt(AnalysesConstants.BODY_KEY_TERMINALBRAND, terminalBrand);//终端品牌
            jsonObject.putOpt(AnalysesConstants.BODY_KEY_TERMINALTYPE, terminalType);//终端型号
            //jsonObject.putOpt(AnalysesConstants.BODY_KEY_USER_ID, userId);
            jsonObject.putOpt(AnalysesConstants.BODY_KEY_DEVICEID, deviceId);//设备的imei
            jsonObject.putOpt(AnalysesConstants.BODY_KEY_DOMAIN_ID, domainId);//domain id
            //直接传入json类型的数据
            if(jsonData != null && jsonData.length() > 0){
                jsonArray = new JSONArray(jsonData);
            }else {
                jsonArray = new JSONArray();
                String[] strings = data.split(";");
                for(String string : strings){
                    jsonArray.put(new JSONObject(string));
                }
            }
            jsonObject.putOpt(AnalysesConstants.BODY_KEY_DATA, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
