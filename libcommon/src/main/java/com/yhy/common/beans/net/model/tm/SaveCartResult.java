package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:SaveCartResult
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-12-30
 * Time:11:35
 * Version 1.1.0
 */


public class SaveCartResult implements Serializable {
    private static final long serialVersionUID = -4467163206335285853L;

    /**
     * 游客id
     */
    public long visitorId;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SaveCartResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SaveCartResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SaveCartResult result = new SaveCartResult();

            // 游客id
            result.visitorId = json.optLong("visitorId");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 游客id
        json.put("visitorId", this.visitorId);

        return json;
    }
}
