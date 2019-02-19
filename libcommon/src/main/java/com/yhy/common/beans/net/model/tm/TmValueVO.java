// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TmValueVO implements Serializable {

    private static final long serialVersionUID = 8708234505737842681L;
    /**
     * id
     */
    public long id;

    /**
     * 属性值内容
     */
    public String text;

    /**
     * 属性值类型 ADULT:成人/SINGLE_ROOM:单房差
     */
    public String type;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmValueVO deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmValueVO deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmValueVO result = new TmValueVO();

            // id
            result.id = json.optLong("id");
            // 属性值内容

            if (!json.isNull("text")) {
                result.text = json.optString("text", null);
            }
            // 属性值类型 ADULT:成人/SINGLE_ROOM:单房差

            if (!json.isNull("type")) {
                result.type = json.optString("type", null);
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

        // 属性值内容
        if (this.text != null) {
            json.put("text", this.text);
        }

        // 属性值类型 ADULT:成人/SINGLE_ROOM:单房差
        if (this.type != null) {
            json.put("type", this.type);
        }

        return json;
    }
}
  