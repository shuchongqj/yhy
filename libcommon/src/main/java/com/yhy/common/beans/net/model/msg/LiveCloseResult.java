package com.yhy.common.beans.net.model.msg;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with Android Studio.
 * Title:LiveCloseResult
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/10/10
 * Time:10:23
 * Version 1.0
 */
public class LiveCloseResult {

    /**
     * 关闭状态
     */
    public boolean status;

    /**
     * 观看人数
     */
    public int viewCount;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static LiveCloseResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static LiveCloseResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            LiveCloseResult result = new LiveCloseResult();

            // 关闭状态
            result.status = json.optBoolean("status");
            // 观看人数
            result.viewCount = json.optInt("viewCount");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 关闭状态
        json.put("status", this.status);

        // 观看人数
        json.put("viewCount", this.viewCount);

        return json;
    }
}



