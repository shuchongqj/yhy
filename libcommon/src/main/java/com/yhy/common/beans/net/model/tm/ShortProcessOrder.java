package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:ShortProcessOrder
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-8-9
 * Time:14:36
 * Version 1.0
 * Description:
 */
public class ShortProcessOrder implements Serializable{
    private static final long serialVersionUID = 4570817850154614662L;

    /**
     * 流程单id
     */
    public long processOrderId;

    /**
     * 商品id
     */
    public long itemId;

    /**
     * 买家ID
     */
    public long buyerId;

    /**
     * 卖家ID
     */
    public long sellerId;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ShortProcessOrder deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ShortProcessOrder deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ShortProcessOrder result = new ShortProcessOrder();

            // 流程单id
            result.processOrderId = json.optLong("processOrderId");
            // 商品id
            result.itemId = json.optLong("itemId");
            // 买家ID
            result.buyerId = json.optLong("buyerId");
            // 卖家ID
            result.sellerId = json.optLong("sellerId");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 流程单id
        json.put("processOrderId", this.processOrderId);

        // 商品id
        json.put("itemId", this.itemId);

        // 买家ID
        json.put("buyerId", this.buyerId);

        // 卖家ID
        json.put("sellerId", this.sellerId);

        return json;
    }
}
