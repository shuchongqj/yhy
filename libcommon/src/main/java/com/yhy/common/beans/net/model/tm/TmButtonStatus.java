// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TmButtonStatus implements Serializable {

    private static final long serialVersionUID = 8493731779267057002L;
    /**
     * 付款按钮
     */
    public boolean payButton;

    /**
     * 确认收货
     */
    public boolean buyerConfirmOrder;

    /**
     * 查看物流-暂时不用
     */
    public boolean buyerLgOrder;

    /**
     * 查看订单详情
     */
    public boolean viewDetailButton;

    /**
     * 取消订单
     */
    public boolean cancelButton;

    /**
     * 取消订单
     */
    public boolean refundButton;

    /**
     * 待评价
     */
    public boolean rateButton;


    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmButtonStatus deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmButtonStatus deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmButtonStatus result = new TmButtonStatus();

            // 付款按钮
            result.payButton = json.optBoolean("payButton");
            // 确认收货
            result.buyerConfirmOrder = json.optBoolean("buyerConfirmOrder");
            // 查看物流-暂时不用
            result.buyerLgOrder = json.optBoolean("buyerLgOrder");
            // 查看订单详情
            result.viewDetailButton = json.optBoolean("viewDetailButton");
            // 取消订单
            result.cancelButton = json.optBoolean("cancelButton");
            // 取消订单
            result.refundButton = json.optBoolean("refundButton");

            // 待评价
            result.rateButton = json.optBoolean("rateButton");

            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 付款按钮
        json.put("payButton", this.payButton);

        // 确认收货
        json.put("buyerConfirmOrder", this.buyerConfirmOrder);

        // 查看物流-暂时不用
        json.put("buyerLgOrder", this.buyerLgOrder);

        // 查看订单详情
        json.put("viewDetailButton", this.viewDetailButton);

        // 取消订单
        json.put("cancelButton", this.cancelButton);

        // 取消订单
        json.put("refundButton", this.refundButton);
        // 待评价
        json.put("rateButton", this.rateButton);

        return json;
    }
}
  