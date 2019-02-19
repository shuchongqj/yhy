package com.yhy.common.beans.net.model.msg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:LiveRecordAPIPageQuery
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-8-16
 * Time:11:53
 * Version 1.0
 * Description:
 */
public class LiveRecordAPIPageQuery implements Serializable{
    private static final long serialVersionUID = 4978697385469293847L;

    /**
     * 用户ID
     */
    public long userId;

    /**
     * 直播类别:旅游度假/特色表演/吃喝玩乐/户外探险
     */
    public List<String> liveCategory;
    /**
     * 直播状态:达人创建直播/开始直播/结束直播/达人结束直播/直播在回放状态
     */
    public List<String> liveStatus;
    /**
     * 直播城市code
     */
    public String locationCityCode;

    /**
     * 直播开始时间
     */
    public long startDate;

    /**
     * 直播结束时间
     */
    public long endDate;

    /**
     * 页码
     */
    public int pageNo;

    /**
     * 每页大小
     */
    public int pageSize;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static LiveRecordAPIPageQuery deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static LiveRecordAPIPageQuery deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            LiveRecordAPIPageQuery result = new LiveRecordAPIPageQuery();

            // 用户ID
            result.userId = json.optLong("userId");
            // 直播类别:旅游度假/特色表演/吃喝玩乐/户外探险
            JSONArray liveCategoryArray = json.optJSONArray("liveCategory");
            if (liveCategoryArray != null) {
                int len = liveCategoryArray.length();
                result.liveCategory = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if(!liveCategoryArray.isNull(i)){
                        result.liveCategory.add(liveCategoryArray.optString(i, null));
                    }else{
                        result.liveCategory.add(i, null);
                    }

                }
            }

            // 直播状态:达人创建直播/开始直播/结束直播/达人结束直播/直播在回放状态
            JSONArray liveStatusArray = json.optJSONArray("liveStatus");
            if (liveStatusArray != null) {
                int len = liveStatusArray.length();
                result.liveStatus = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if(!liveStatusArray.isNull(i)){
                        result.liveStatus.add(liveStatusArray.optString(i, null));
                    }else{
                        result.liveStatus.add(i, null);
                    }

                }
            }

            // 直播城市code

            if(!json.isNull("locationCityCode")){
                result.locationCityCode = json.optString("locationCityCode", null);
            }
            // 直播开始时间
            result.startDate = json.optLong("startDate");
            // 直播结束时间
            result.endDate = json.optLong("endDate");
            // 页码
            result.pageNo = json.optInt("pageNo");
            // 每页大小
            result.pageSize = json.optInt("pageSize");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 用户ID
        json.put("userId", this.userId);

        // 直播类别:旅游度假/特色表演/吃喝玩乐/户外探险
        if (this.liveCategory != null) {
            JSONArray liveCategoryArray = new JSONArray();
            for (String value : this.liveCategory)
            {
                liveCategoryArray.put(value);
            }
            json.put("liveCategory", liveCategoryArray);
        }

        // 直播状态:达人创建直播/开始直播/结束直播/达人结束直播/直播在回放状态
        if (this.liveStatus != null) {
            JSONArray liveStatusArray = new JSONArray();
            for (String value : this.liveStatus)
            {
                liveStatusArray.put(value);
            }
            json.put("liveStatus", liveStatusArray);
        }

        // 直播城市code
        if(this.locationCityCode != null) { json.put("locationCityCode", this.locationCityCode); }

        // 直播开始时间
        json.put("startDate", this.startDate);

        // 直播结束时间
        json.put("endDate", this.endDate);

        // 页码
        json.put("pageNo", this.pageNo);

        // 每页大小
        json.put("pageSize", this.pageSize);

        return json;
    }
}
