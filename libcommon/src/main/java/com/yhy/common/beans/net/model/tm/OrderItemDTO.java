package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:OrderItemDTO
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-29
 * Time:10:39
 * Version 1.0
 * Description:
 */
public class OrderItemDTO implements Serializable{

    /**
     * 商品id
     */
    public long itemId;

    /**
     * skuId
     */
    public long skuId;

    /**
     * 数量
     */
    public int num;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static OrderItemDTO deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static OrderItemDTO deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            OrderItemDTO result = new OrderItemDTO();

            // 商品id
            result.itemId = json.optLong("itemId");
            // skuId
            result.skuId = json.optLong("skuId");
            // 数量
            result.num = json.optInt("num");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 商品id
        json.put("itemId", this.itemId);

        // skuId
        json.put("skuId", this.skuId);

        // 数量
        json.put("num", this.num);

        return json;
    }

}
