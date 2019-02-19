package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:TmConsultInfo
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-27
 * Time:17:00
 * Version 1.0
 * Description:
 */
public class TmConsultInfo implements Serializable{
    private static final long serialVersionUID = 5767758488277407628L;
    /**
     * 快速咨询商品ID
     */
    public long itemId;

    /**
     * 卖家ID 未匹配到商品则为0
     */
    public long sellerId;

    /**
     * 商品价格
     */
    public long totalFee;

    /**
     * 服务时间
     */
    public long serviceTime;

    /**
     * 流程单 有正在咨询订单返回 没有则返回空
     */
    public ShortProcessOrder shortProcessOrder;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmConsultInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmConsultInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmConsultInfo result = new TmConsultInfo();

            // 快速咨询商品ID
            result.itemId = json.optLong("itemId");
            // 卖家ID 未匹配到商品则为0
            result.sellerId = json.optLong("sellerId");
            // 商品价格
            result.totalFee = json.optLong("totalFee");
            // 服务时间
            result.serviceTime = json.optLong("serviceTime");
            // 流程单 有正在咨询订单返回 没有则返回空
            result.shortProcessOrder = ShortProcessOrder.deserialize(json.optJSONObject("shortProcessOrder"));
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 快速咨询商品ID
        json.put("itemId", this.itemId);

        // 卖家ID 未匹配到商品则为0
        json.put("sellerId", this.sellerId);

        // 商品价格
        json.put("totalFee", this.totalFee);

        // 服务时间
        json.put("serviceTime", this.serviceTime);

        // 流程单 有正在咨询订单返回 没有则返回空
        if (this.shortProcessOrder != null) { json.put("shortProcessOrder", this.shortProcessOrder.serialize()); }

        return json;
    }
}
