package com.yhy.common.beans.net.model.msg;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:DisableParam
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-8-17
 * Time:15:54
 * Version 1.0
 * Description:
 */
public class DisableParam implements Serializable {
    private static final long serialVersionUID = -7673455823019428141L;

    /**
     * 直播ID
     */
    public long liveId;

    /**
     * 观看直播用户ID
     */
    public long viewId;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static DisableParam deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static DisableParam deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            DisableParam result = new DisableParam();

            // 直播ID
            result.liveId = json.optLong("liveId");
            // 观看直播用户ID
            result.viewId = json.optLong("viewId");

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

        // 观看直播用户ID
        json.put("viewId", this.viewId);

        return json;
    }
}
