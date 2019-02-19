package com.yhy.common.beans.net.model.msg;

import com.yhy.common.beans.net.model.user.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:LiveRecordResult
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-8-16
 * Time:11:48
 * Version 1.0
 * Description:
 */
public class LiveRecordResult implements Serializable {
    private static final long serialVersionUID = -3592569410953611575L;

    public boolean isChoosed = false;
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
     * 直播类别code
     */
    public long liveCategoryCode;

    /**
     * 直播类别name
     */
    public String liveCategoryName;

    /**
     * 直播标题
     */
    public String liveTitle;

    /**
     * 直播封面
     */
    public String liveCover;

    /**
     * 直播状态
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
     * 开始时间
     */
    public long startDate;

    /**
     * 结束时间
     */
    public long endDate;

    /**
     * 在线人数
     */
    public int onlineCount;

    /**
     * 观看次数
     */
    public int viewCount;

    /**
     * 回放地址
     */
    public List<String> replayUrls;
    /**
     * 拉流地址
     */
    public String pushStreamUrl;

    /**
     * 推流地址
     */
    public String pullStreamUrl;

    /**
     * 用户信息
     */
    public UserInfo userInfo;

    /**
     * 直播配置
     */
    public String liveConfig;

    /**
     * 直播记录状态:正常(NORMAL_LIVE) 删除（DELETE_LIVE）下架 (OFF_SHELVE_LIVE)
     */
    public String status;

    /**
     * 视频或者直播的横竖屏
     */
    public int liveScreenType;


    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static LiveRecordResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static LiveRecordResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            LiveRecordResult result = new LiveRecordResult();

            // 直播ID
            result.liveId = json.optLong("liveId");
            // 用户ID
            result.userId = json.optLong("userId");
            // 直播间ID
            result.roomId = json.optLong("roomId");
            //直播横竖屏
            result.liveScreenType = json.optInt("liveScreenType");
            // 直播类别code
            result.liveCategoryCode = json.optLong("liveCategoryCode");
            // 直播类别name

            if(!json.isNull("liveCategoryName")){
                result.liveCategoryName = json.optString("liveCategoryName", null);
            }
            // 直播标题

            if(!json.isNull("liveTitle")){
                result.liveTitle = json.optString("liveTitle", null);
            }
            // 直播封面


            //直播公告
            if(!json.isNull("liveConfig")){
                result.liveConfig = json.optString("liveConfig", null);
            }

            if(!json.isNull("liveCover")){
                result.liveCover = json.optString("liveCover", null);
            }
            // 直播状态

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
            // 开始时间
            result.startDate = json.optLong("startDate");
            // 结束时间
            result.endDate = json.optLong("endDate");
            // 在线人数
            result.onlineCount = json.optInt("onlineCount");
            // 观看次数
            result.viewCount = json.optInt("viewCount");
            // 回放地址
            JSONArray replayUrlsArray = json.optJSONArray("replayUrls");
            if (replayUrlsArray != null) {
                int len = replayUrlsArray.length();
                result.replayUrls = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if(!replayUrlsArray.isNull(i)){
                        result.replayUrls.add(replayUrlsArray.optString(i, null));
                    }else{
                        result.replayUrls.add(i, null);
                    }

                }
            }

            // 拉流地址

            if(!json.isNull("pushStreamUrl")){
                result.pushStreamUrl = json.optString("pushStreamUrl", null);
            }
            // 推流地址

            if(!json.isNull("pullStreamUrl")){
                result.pullStreamUrl = json.optString("pullStreamUrl", null);
            }
            // 用户信息
            result.userInfo = UserInfo.deserialize(json.optJSONObject("userInfo"));

            // 直播记录状态:正常(NORMAL_LIVE) 删除（DELETE_LIVE）下架 (OFF_SHELVE_LIVE)
            if(!json.isNull("status")){
                result.status = json.optString("status", null);
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

        // 直播ID
        json.put("liveId", this.liveId);

        //直播横竖屏
        json.put("liveScreenType",this.liveScreenType);

        // 用户ID
        json.put("userId", this.userId);

        // 直播间ID
        json.put("roomId", this.roomId);

        // 直播类别code
        json.put("liveCategoryCode", this.liveCategoryCode);

        // 直播类别name
        if(this.liveCategoryName != null) { json.put("liveCategoryName", this.liveCategoryName); }

        // 直播标题
        if(this.liveTitle != null) { json.put("liveTitle", this.liveTitle); }

        // 直播封面
        if(this.liveCover != null) { json.put("liveCover", this.liveCover); }

        // 直播状态
        if(this.liveStatus != null) { json.put("liveStatus", this.liveStatus); }

        // 城市code
        if(this.locationCityCode != null) { json.put("locationCityCode", this.locationCityCode); }

        // 城市名称
        if(this.locationCityName != null) { json.put("locationCityName", this.locationCityName); }

        // 开始时间
        json.put("startDate", this.startDate);

        // 结束时间
        json.put("endDate", this.endDate);

        // 在线人数
        json.put("onlineCount", this.onlineCount);

        // 观看次数
        json.put("viewCount", this.viewCount);

        // 回放地址
        if (this.replayUrls != null) {
            JSONArray replayUrlsArray = new JSONArray();
            for (String value : this.replayUrls)
            {
                replayUrlsArray.put(value);
            }
            json.put("replayUrls", replayUrlsArray);
        }

        // 拉流地址
        if(this.pushStreamUrl != null) { json.put("pushStreamUrl", this.pushStreamUrl); }

        // 推流地址
        if(this.pullStreamUrl != null) { json.put("pullStreamUrl", this.pullStreamUrl); }

        // 用户信息
        if (this.userInfo != null) { json.put("userInfo", this.userInfo.serialize()); }

        // 直播配置
        if (this.liveConfig != null) { json.put("liveConfig", this.liveConfig); }

        // 直播记录状态:正常(NORMAL_LIVE) 删除（DELETE_LIVE）下架 (OFF_SHELVE_LIVE)
        if(this.status != null) { json.put("status", this.status); }

        return json;
    }
}
