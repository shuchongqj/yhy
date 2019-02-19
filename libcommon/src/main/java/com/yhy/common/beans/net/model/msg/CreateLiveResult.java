package com.yhy.common.beans.net.model.msg;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:CreateLiveResult
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-31
 * Time:17:53
 * Version 1.1.0
 */

public class CreateLiveResult implements Serializable {
    private static final long serialVersionUID = -8057766194426869827L;

    /**
     * 直播ID
     */
    public long liveId;

    /**
     * 用户ID
     */
    public long userId;

    /**
     * 直播间ID
     */
    public long roomId;

    /**
     * 直播标题
     */
    public String liveTitle;

    /**
     * 直播类别Code
     */
    public long liveCategoryCode;

    /**
     * 直播类别Name
     */
    public String liveCategoryName;

    /**
     * 直播状态 直播/回放
     */
    public String liveStatus;

    /**
     * 城市code
     */
    public String locationCityCode;

    /**
     * 城市名称
     */
    public String locationCityName;

    /**
     * 直播地址流信息
     */
    public String pushStreamUrl;

    /**
     * 在线人数
     */
    public int onlineCount;

    /**
     * 直播配置
     */
    public LiveConfig liveConfig;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static CreateLiveResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static CreateLiveResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            CreateLiveResult result = new CreateLiveResult();

            // 直播ID
            result.liveId = json.optLong("liveId");
            // 用户ID
            result.userId = json.optLong("userId");
            // 直播间ID
            result.roomId = json.optLong("roomId");
            // 直播标题

            if(!json.isNull("liveTitle")){
                result.liveTitle = json.optString("liveTitle", null);
            }
            // 直播类别Code
            result.liveCategoryCode = json.optLong("liveCategoryCode");
            // 直播类别Name

            if(!json.isNull("liveCategoryName")){
                result.liveCategoryName = json.optString("liveCategoryName", null);
            }
            // 直播状态 直播/回放

            if(!json.isNull("liveStatus")){
                result.liveStatus = json.optString("liveStatus", null);
            }
            // 城市code

            if(!json.isNull("locationCityCode")){
                result.locationCityCode = json.optString("locationCityCode", null);
            }
            // 城市名称

            if(!json.isNull("locationCityName")){
                result.locationCityName = json.optString("locationCityName", null);
            }
            // 直播地址流信息

            if(!json.isNull("pushStreamUrl")){
                result.pushStreamUrl = json.optString("pushStreamUrl", null);
            }
            // 在线人数
            result.onlineCount = json.optInt("onlineCount");
            // 直播配置
            result.liveConfig = LiveConfig.deserialize(json.optJSONObject("liveConfig"));
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 直播ID
        json.put("liveId", this.liveId);

        // 用户ID
        json.put("userId", this.userId);

        // 直播间ID
        json.put("roomId", this.roomId);

        // 直播标题
        if(this.liveTitle != null) { json.put("liveTitle", this.liveTitle); }

        // 直播类别Code
        json.put("liveCategoryCode", this.liveCategoryCode);

        // 直播类别Name
        if(this.liveCategoryName != null) { json.put("liveCategoryName", this.liveCategoryName); }

        // 直播状态 直播/回放
        if(this.liveStatus != null) { json.put("liveStatus", this.liveStatus); }

        // 城市code
        if(this.locationCityCode != null) { json.put("locationCityCode", this.locationCityCode); }

        // 城市名称
        if(this.locationCityName != null) { json.put("locationCityName", this.locationCityName); }

        // 直播地址流信息
        if(this.pushStreamUrl != null) { json.put("pushStreamUrl", this.pushStreamUrl); }

        // 在线人数
        json.put("onlineCount", this.onlineCount);

        // 直播配置
        if (this.liveConfig != null) { json.put("liveConfig", this.liveConfig.serialize()); }

        return json;
    }
}
