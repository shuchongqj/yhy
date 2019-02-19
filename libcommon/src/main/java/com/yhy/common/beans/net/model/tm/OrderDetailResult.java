package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:OrderDetailResult
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-12-22
 * Time:15:37
 * Version 1.1.0
 */


public class OrderDetailResult implements Serializable {
    private static final long serialVersionUID = -7228523434673592997L;

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
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static OrderDetailResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static OrderDetailResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            OrderDetailResult result = new OrderDetailResult();

            // 订单信息
            result.mainOrder = TmMainOrder.deserialize(json.optJSONObject("mainOrder"));
            // 支付流水号

            if(!json.isNull("payNum")){
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
        if (this.mainOrder != null) { json.put("mainOrder", this.mainOrder.serialize()); }

        // 支付流水号
        if(this.payNum != null) { json.put("payNum", this.payNum); }

        // 普通商品的收货信息
        if (this.logisticsOrder != null) { json.put("logisticsOrder", this.logisticsOrder.serialize()); }

        return json;
    }
}
