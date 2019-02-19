package com.yhy.common.beans.net.model.msg;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:LiveCategoryResult
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-31
 * Time:18:32
 * Version 1.1.0
 */

public class LiveCategoryResult implements Serializable {
    private static final long serialVersionUID = 1555768841961030991L;

    /**
     * 直播类别
     */
    public String name;

    /**
     * 直播code
     */
    public long code;

    /**
     * 状态
     */
    public int status;

    /**
     * 排序
     */
    public int score;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static LiveCategoryResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static LiveCategoryResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            LiveCategoryResult result = new LiveCategoryResult();

            // 直播类别

            if(!json.isNull("name")){
                result.name = json.optString("name", null);
            }
            // 直播code
            result.code = json.optLong("code");
            // 状态
            result.status = json.optInt("status");
            // 排序
            result.score = json.optInt("score");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 直播类别
        if(this.name != null) { json.put("name", this.name); }

        // 直播code
        json.put("code", this.code);

        // 状态
        json.put("status", this.status);

        // 排序
        json.put("score", this.score);

        return json;
    }
}
