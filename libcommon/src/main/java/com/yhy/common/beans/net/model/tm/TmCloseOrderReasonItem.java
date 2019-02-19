// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TmCloseOrderReasonItem implements Serializable {

    private static final long serialVersionUID = 5874528014662699405L;
    /**
     * 关闭订单理由id
     */
    public long reasonId;

    /**
     * 关闭订单理由描述
     */
    public String reasonText;

    /**
     * 关闭订单理由类型
     */
    public String reasonType;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmCloseOrderReasonItem deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmCloseOrderReasonItem deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmCloseOrderReasonItem result = new TmCloseOrderReasonItem();

            // 关闭订单理由id
            result.reasonId = json.optLong("reasonId");
            // 关闭订单理由描述

            if (!json.isNull("reasonText")) {
                result.reasonText = json.optString("reasonText", null);
            }
            // 关闭订单理由类型

            if (!json.isNull("reasonType")) {
                result.reasonType = json.optString("reasonType", null);
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

        // 关闭订单理由id
        json.put("reasonId", this.reasonId);

        // 关闭订单理由描述
        if (this.reasonText != null) {
            json.put("reasonText", this.reasonText);
        }

        // 关闭订单理由类型
        if (this.reasonType != null) {
            json.put("reasonType", this.reasonType);
        }

        return json;
    }
}
  