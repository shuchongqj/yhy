// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TmCreateOrderResultTO implements Serializable {

    private static final long serialVersionUID = 1047595481533404493L;
    /**
     * 是否成功
     */
    public boolean success;

    /**
     * 主订单信息
     */
    public TmMainOrder mainOrder;

    /**
     * 普通商品发货信息 收货人及收货地址
     */
    public TmLogisticsOrder logisticsOrder;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmCreateOrderResultTO deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmCreateOrderResultTO deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmCreateOrderResultTO result = new TmCreateOrderResultTO();

            // 是否成功
            result.success = json.optBoolean("success");
            // 主订单信息
            result.mainOrder = TmMainOrder.deserialize(json.optJSONObject("mainOrder"));
            // 普通商品发货信息 收货人及收货地址
            result.logisticsOrder = TmLogisticsOrder.deserialize(json.optJSONObject("logisticsOrder"));
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

        // 主订单信息
        if (this.mainOrder != null) {
            json.put("mainOrder", this.mainOrder.serialize());
        }

        // 普通商品发货信息 收货人及收货地址
        if (this.logisticsOrder != null) {
            json.put("logisticsOrder", this.logisticsOrder.serialize());
        }

        return json;
    }
}
  