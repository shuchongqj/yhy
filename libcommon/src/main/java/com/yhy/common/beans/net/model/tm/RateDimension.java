// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class RateDimension implements Serializable {

    private static final long serialVersionUID = -4128022216761399411L;
    /**
     * 维度code
     */
    public String code;

    /**
     * 分值
     */
    public int value;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static RateDimension deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static RateDimension deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            RateDimension result = new RateDimension();

            // 维度code

            if (!json.isNull("code")) {
                result.code = json.optString("code", null);
            }
            // 分值
            result.value = json.optInt("value");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 维度code
        if (this.code != null) {
            json.put("code", this.code);
        }

        // 分值
        json.put("value", this.value);

        return json;
    }
}
  