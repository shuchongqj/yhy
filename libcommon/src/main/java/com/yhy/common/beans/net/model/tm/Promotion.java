package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:Promotion
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-12-21
 * Time:15:18
 * Version 1.1.0
 */


public class Promotion implements Serializable {
    private static final long serialVersionUID = -2424143337360983519L;

    /**
     * id
     */
    public long id;

    /**
     * 满X减Y的X，单位分
     */
    public long requirement;

    /**
     * 满X减Y的Y，直降Y，单位分
     */
    public long value;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Promotion deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Promotion deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            Promotion result = new Promotion();

            // id
            result.id = json.optLong("id");
            // 满X减Y的X，单位分
            result.requirement = json.optLong("requirement");
            // 满X减Y的Y，直降Y，单位分
            result.value = json.optLong("value");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // id
        json.put("id", this.id);

        // 满X减Y的X，单位分
        json.put("requirement", this.requirement);

        // 满X减Y的Y，直降Y，单位分
        json.put("value", this.value);

        return json;
    }
}
