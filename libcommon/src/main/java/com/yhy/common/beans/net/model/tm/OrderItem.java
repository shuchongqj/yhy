package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:OrderItem
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-12-21
 * Time:15:17
 * Version 1.1.0
 */


public class OrderItem implements Serializable {
    private static final long serialVersionUID = 1407166537263405101L;

    /**
     * 卖家id
     */
    public long sellerId;

    /**
     * 卖家名称
     */
    public String sellerName;

    /**
     * 商品id
     */
    public long itemId;

    /**
     * 商品标题
     */
    public String itemTitle;

    /**
     * 商品图片
     */
    public String picUrl;

    /**
     * skuId
     */
    public long skuId;

    /**
     * 单价,原售卖价
     */
    public long price;

    /**
     * 购买数量
     */
    public long buyAmount;

    /**
     * 子订单总价
     */
    public long actualTotalFee;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static OrderItem deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static OrderItem deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            OrderItem result = new OrderItem();

            // 卖家id
            result.sellerId = json.optLong("sellerId");
            // 卖家名称

            if(!json.isNull("sellerName")){
                result.sellerName = json.optString("sellerName", null);
            }
            // 商品id
            result.itemId = json.optLong("itemId");
            // 商品标题

            if(!json.isNull("itemTitle")){
                result.itemTitle = json.optString("itemTitle", null);
            }
            // 商品图片

            if(!json.isNull("picUrl")){
                result.picUrl = json.optString("picUrl", null);
            }
            // skuId
            result.skuId = json.optLong("skuId");
            // 单价,原售卖价
            result.price = json.optLong("price");
            // 购买数量
            result.buyAmount = json.optLong("buyAmount");
            // 子订单总价
            result.actualTotalFee = json.optLong("actualTotalFee");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 卖家id
        json.put("sellerId", this.sellerId);

        // 卖家名称
        if(this.sellerName != null) { json.put("sellerName", this.sellerName); }

        // 商品id
        json.put("itemId", this.itemId);

        // 商品标题
        if(this.itemTitle != null) { json.put("itemTitle", this.itemTitle); }

        // 商品图片
        if(this.picUrl != null) { json.put("picUrl", this.picUrl); }

        // skuId
        json.put("skuId", this.skuId);

        // 单价,原售卖价
        json.put("price", this.price);

        // 购买数量
        json.put("buyAmount", this.buyAmount);

        // 子订单总价
        json.put("actualTotalFee", this.actualTotalFee);

        return json;
    }
}
