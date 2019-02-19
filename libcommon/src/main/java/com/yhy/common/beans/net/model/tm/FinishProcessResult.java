package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:FinishProcessResult
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-20
 * Time:10:37
 * Version 1.0
 * Description:
 */
public class FinishProcessResult implements Serializable{
    private static final long serialVersionUID = -3044032048003418476L;

    /**
     * 是否成功
     */
    public boolean success;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static FinishProcessResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static FinishProcessResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            FinishProcessResult result = new FinishProcessResult();

            // 是否成功
            result.success = json.optBoolean("success");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 是否成功
        json.put("success", this.success);

        return json;
    }
}
