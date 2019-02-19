package com.yhy.common.beans.net.model.guide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:GuideFocusInfo
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-22
 * Time:11:17
 * Version 1.1.0
 */

public class GuideFocusInfo implements Serializable {
    private static final long serialVersionUID = 8799198469075266979L;
    /**
     * 看点id
     */
    public long id;

    /**
     * 语音时长
     */
    public int audioTime;

    /**
     * 景点id
     */
    public long attractionId;

    /**
     * 看点名称
     */
    public String name;

    /**
     * 语音名称
     */
    public String audioName;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static GuideFocusInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static GuideFocusInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            GuideFocusInfo result = new GuideFocusInfo();

            // 看点id
            result.id = json.optLong("id");
            // 语音时长
            result.audioTime = json.optInt("audioTime");
            // 景点id
            result.attractionId = json.optLong("attractionId");
            // 看点名称

            if(!json.isNull("name")){
                result.name = json.optString("name", null);
            }
            // 语音名称

            if(!json.isNull("audioName")){
                result.audioName = json.optString("audioName", null);
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

        // 看点id
        json.put("id", this.id);

        // 语音时长
        json.put("audioTime", this.audioTime);

        // 景点id
        json.put("attractionId", this.attractionId);

        // 看点名称
        if(this.name != null) { json.put("name", this.name); }

        // 语音名称
        if(this.audioName != null) { json.put("audioName", this.audioName); }

        return json;
    }
}