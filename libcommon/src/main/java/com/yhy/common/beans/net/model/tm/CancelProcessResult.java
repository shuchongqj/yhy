package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:CancelProcessResult
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-20
 * Time:10:42
 * Version 1.0
 * Description:
 */
public class CancelProcessResult implements Serializable{
    private static final long serialVersionUID = -5749192864123098652L;

    /**
     * 是否成功
     */
    public boolean success;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static CancelProcessResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static CancelProcessResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            CancelProcessResult result = new CancelProcessResult();

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
