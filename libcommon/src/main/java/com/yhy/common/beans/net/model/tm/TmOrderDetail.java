// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TmOrderDetail implements Serializable {
    private static final long serialVersionUID = 1986704169239002499L;
    /**
     * 订单信息
     */
    public TmMainOrder mainOrder;

    /**
     * 支付流水号
     */
    public String payNum;

    /**
     * 普通商品的收货信息
     */
    public TmLogisticsOrder logisticsOrder;
    /**
     * 新增ItemType仅供商品使用
     */
    public String itemType;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmOrderDetail deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmOrderDetail deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmOrderDetail result = new TmOrderDetail();

            // 订单信息
            result.mainOrder = TmMainOrder.deserialize(json.optJSONObject("mainOrder"));
            // 支付流水号

            if (!json.isNull("payNum")) {
                result.payNum = json.optString("payNum", null);
            }
            // 普通商品的收货信息
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

        // 订单信息
        if (this.mainOrder != null) {
            json.put("mainOrder", this.mainOrder.serialize());
        }

        // 支付流水号
        if (this.payNum != null) {
            json.put("payNum", this.payNum);
        }

        // 普通商品的收货信息
        if (this.logisticsOrder != null) {
            json.put("logisticsOrder", this.logisticsOrder.serialize());
        }

        return json;
    }
}
  