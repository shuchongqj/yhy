package com.yhy.common.beans.net.model.guide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:GuideScenicInfo
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-22
 * Time:11:14
 * Version 1.1.0
 */

public class GuideScenicInfo implements Serializable {
    private static final long serialVersionUID = -2311263948203001693L;

    /**
     * 导览id
     */
    public long guideId;

    /**
     * 景区id
     */
    public long scenicId;

    /**
     * 导览图片
     */
    public String listImg;

    /**
     * 景区名称
     */
    public String scenicName;

    /**
     * 导览景区收听人数
     */
    public String listenCount;

    /**
     * 景区归属城市
     */
    public String cityName;

    /**
     * 距离
     */
    public String distance;

    /**
     * 景区级别
     */
    public String level;

    /**
     * 景区地址
     */
    public String address;

    /**
     * 是否有门票售卖
     */
    public boolean isOnSale;

    /**
     * 导览电子地图
     */
    public String guideImg;

    /**
     * 导览开场语音
     */
    public String guideAudio;

    /**
     * 导览开场语音时长-秒
     */
    public int guideAudioTime;

    /**
     * 开场白名称
     */
    public String guideAudioTitle;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static GuideScenicInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static GuideScenicInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            GuideScenicInfo result = new GuideScenicInfo();

            // 导览id
            result.guideId = json.optLong("guideId");
            // 景区id
            result.scenicId = json.optLong("scenicId");
            // 导览图片

            if(!json.isNull("listImg")){
                result.listImg = json.optString("listImg", null);
            }
            // 景区名称

            if(!json.isNull("scenicName")){
                result.scenicName = json.optString("scenicName", null);
            }
            // 导览景区收听人数

            if(!json.isNull("listenCount")){
                result.listenCount = json.optString("listenCount", null);
            }
            // 景区归属城市

            if(!json.isNull("cityName")){
                result.cityName = json.optString("cityName", null);
            }
            // 距离
            result.distance = json.optString("distance");
            // 景区级别

            if(!json.isNull("level")){
                result.level = json.optString("level", null);
            }
            // 景区地址

            if(!json.isNull("address")){
                result.address = json.optString("address", null);
            }
            // 是否有门票售卖
            result.isOnSale = json.optBoolean("isOnSale");
            // 导览电子地图

            if(!json.isNull("guideImg")){
                result.guideImg = json.optString("guideImg", null);
            }
            // 导览开场语音

            if(!json.isNull("guideAudio")){
                result.guideAudio = json.optString("guideAudio", null);
            }
            // 导览开场语音时长-秒
            result.guideAudioTime = json.optInt("guideAudioTime");
            // 开场白名称

            if(!json.isNull("guideAudioTitle")){
                result.guideAudioTitle = json.optString("guideAudioTitle", null);
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

        // 导览id
        json.put("guideId", this.guideId);

        // 景区id
        json.put("scenicId", this.scenicId);

        // 导览图片
        if(this.listImg != null) { json.put("listImg", this.listImg); }

        // 景区名称
        if(this.scenicName != null) { json.put("scenicName", this.scenicName); }

        // 导览景区收听人数
        if(this.listenCount != null) { json.put("listenCount", this.listenCount); }

        // 景区归属城市
        if(this.cityName != null) { json.put("cityName", this.cityName); }

        // 距离
        json.put("distance", this.distance);

        // 景区级别
        if(this.level != null) { json.put("level", this.level); }

        // 景区地址
        if(this.address != null) { json.put("address", this.address); }

        // 是否有门票售卖
        json.put("isOnSale", this.isOnSale);

        // 导览电子地图
        if(this.guideImg != null) { json.put("guideImg", this.guideImg); }

        // 导览开场语音
        if(this.guideAudio != null) { json.put("guideAudio", this.guideAudio); }

        // 导览开场语音时长-秒
        json.put("guideAudioTime", this.guideAudioTime);

        // 开场白名称
        if(this.guideAudioTitle != null) { json.put("guideAudioTitle", this.guideAudioTitle); }

        return json;
    }
}
