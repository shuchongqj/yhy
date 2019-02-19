package com.yhy.common.beans.net.model.msg;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:LiveRecordInfo
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-8-16
 * Time:11:45
 * Version 1.0
 * Description:
 */
public class LiveRecordInfo implements Serializable{
    private static final long serialVersionUID = -6886502822414993356L;

    /**
     * 直播标题
     */
    public String liveTitle;

    /**
     * 直播类别:旅游度假/特色表演/吃喝玩乐/户外探险
     */
    public int liveCategory;

    /**
     * 直播室
     */
    public long roomId;

    /**
     * 直播城市code
     */
    public String locationCityCode;

    /**
     * 直播城市名称
     */
    public String locationCityName;

    /**
     * 直播间公告
     */
    public String liveNotice;

    /**
     *直播横竖屏
     */
    public int liveScreenType;

    /**
     * 直播的批次ID
     */
    public long batchId;

    /**


     /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static LiveRecordInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static LiveRecordInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            LiveRecordInfo result = new LiveRecordInfo();

            // 直播标题

            if(!json.isNull("liveTitle")){
                result.liveTitle = json.optString("liveTitle", null);
            }
            // 直播类别:旅游度假/特色表演/吃喝玩乐/户外探险
            result.liveCategory = json.optInt("liveCategory");
            // 直播间公告

            if(!json.isNull("liveNotice")){
                result.liveNotice = json.optString("liveNotice", null);
            }

            //直播横竖屏
            result.liveScreenType = json.optInt("liveScreenType");

            //直播批次id
            result.batchId = json.optLong("batchId");

            // 直播室
            result.roomId = json.optLong("roomId");
            // 直播城市code

            if(!json.isNull("locationCityCode")){
                result.locationCityCode = json.optString("locationCityCode", null);
            }
            // 直播城市名称

            if(!json.isNull("locationCityName")){
                result.locationCityName = json.optString("locationCityName", null);
            }
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 直播标题
        if(this.liveTitle != null) { json.put("liveTitle", this.liveTitle); }

        // 直播类别:旅游度假/特色表演/吃喝玩乐/户外探险
        json.put("liveCategory", this.liveCategory);

        // 直播间公告
        if(this.liveNotice != null) { json.put("liveNotice", this.liveNotice); }

        //直播横竖屏
        json.put("liveScreenType",this.liveScreenType);

        json.put("batchId",this.batchId);

        // 直播室
        json.put("roomId", this.roomId);

        // 直播城市code
        if(this.locationCityCode != null) { json.put("locationCityCode", this.locationCityCode); }

        // 直播城市名称
        if(this.locationCityName != null) { json.put("locationCityName", this.locationCityName); }

        return json;
    }
}
