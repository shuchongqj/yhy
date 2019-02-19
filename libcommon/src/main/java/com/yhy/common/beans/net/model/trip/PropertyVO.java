// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class PropertyVO implements Serializable {

    private static final long serialVersionUID = -1537440769477088783L;
    /**
     * id
     */
    public long id;

    /**
     * 属性名
     */
    public String text;

    /**
     * 属性类型 TEXT:普通文字/DATE:普通日期/START_DATE:出发日期/PERSON_TYPE:人员类型/LINE_PACKAGE:套餐/ACTIVITY_TIME:活动时间/ACTIVITY_CONTENT:活动内容/
     */
    public String type;

    /**
     * 属性值
     */
    public String value;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static PropertyVO deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static PropertyVO deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            PropertyVO result = new PropertyVO();

            // id
            result.id = json.optLong("id");
            // 属性名

            if (!json.isNull("text")) {
                result.text = json.optString("text", null);
            }
            // 属性类型 保留

            if (!json.isNull("type")) {
                result.type = json.optString("type", null);
            }
            //  属性类型 TEXT:普通文字/DATE:普通日期/START_DATE:出发日期/PERSON_TYPE:人员类型/LINE_PACKAGE:套餐/ACTIVITY_TIME:活动时间/ACTIVITY_CONTENT:活动内容/


            if (!json.isNull("value")) {
                result.value = json.optString("value", null);
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

        // id
        json.put("id", this.id);

        // 属性名
        if (this.text != null) {
            json.put("text", this.text);
        }

        //  属性类型 TEXT:普通文字/DATE:普通日期/START_DATE:出发日期/PERSON_TYPE:人员类型/LINE_PACKAGE:套餐/ACTIVITY_TIME:活动时间/ACTIVITY_CONTENT:活动内容/
        if (this.type != null) {
            json.put("type", this.type);
        }

        // 属性值
        if (this.value != null) {
            json.put("value", this.value);
        }

        return json;
    }
}
  