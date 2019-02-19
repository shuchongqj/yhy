package com.yhy.common.beans.net.model.tm;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:ItemInfoResult
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-10-25
 * Time:9:47
 * Version 1.1.0
 */


public class ItemInfoResult implements Serializable {
    private static final long serialVersionUID = -3602254003286807981L;

    /**
     * id
     */
    public long id;

    /**
     * 商品id
     */
    public long itemId;

    /**
     * 商家id
     */
    public long sellerId;

    /**
     * skuId
     */
    public long itemSkuId;

    /**
     * 图片
     */
    public String picUrls;

    /**
     * 商品标题
     */
    public String title;

    /**
     * sku name
     */
    public String skuName;

    /**
     * 商品库存
     */
    public int stockNum;

    /**
     * 商品价格
     */
    public long price;

    /**
     * 购买数量
     */
    public long buyAmount;

    /**
     * 积分可抵金额
     */
    public long pointTotalFee;

    /**
     * 商品勾选状态
     */
    public boolean itemCheck;

    /**
     * 商品状态 1:已抢光   2：上架    3：下架   4：商家删除
     */
    public int itemStatus;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ItemInfoResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ItemInfoResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ItemInfoResult result = new ItemInfoResult();

            // id
            result.id = json.optLong("id");
            // 商品id
            result.itemId = json.optLong("itemId");
            // 商家id
            result.sellerId = json.optLong("sellerId");
            // skuId
            result.itemSkuId = json.optLong("itemSkuId");
            // 图片

            if(!json.isNull("picUrls")){
                result.picUrls = json.optString("picUrls", null);
            }
            // 商品标题

            if(!json.isNull("title")){
                result.title = json.optString("title", null);
            }
            // sku name

            if(!json.isNull("skuName")){
                result.skuName = json.optString("skuName", null);
            }
            // 商品库存
            result.stockNum = json.optInt("stockNum");
            // 商品价格
            result.price = json.optLong("price");
            // 购买数量
            result.buyAmount = json.optLong("buyAmount");
            // 积分可抵金额
            result.pointTotalFee = json.optLong("pointTotalFee");
            // 商品勾选状态
            result.itemCheck = json.optBoolean("itemCheck");
            // 商品状态 1:已抢光   2：上架    3：下架   4：商家删除
            result.itemStatus = json.optInt("itemStatus");
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

        // 商品id
        json.put("itemId", this.itemId);

        // 商家id
        json.put("sellerId", this.sellerId);

        // skuId
        json.put("itemSkuId", this.itemSkuId);

        // 图片
        if(this.picUrls != null) { json.put("picUrls", this.picUrls); }

        // 商品标题
        if(this.title != null) { json.put("title", this.title); }

        // sku name
        if(this.skuName != null) { json.put("skuName", this.skuName); }

        // 商品库存
        json.put("stockNum", this.stockNum);

        // 商品价格
        json.put("price", this.price);

        // 购买数量
        json.put("buyAmount", this.buyAmount);

        // 积分可抵金额
        json.put("pointTotalFee", this.pointTotalFee);

        // 商品勾选状态
        json.put("itemCheck", this.itemCheck);

        // 商品状态 1:已抢光   2：上架    3：下架   4：商家删除
        json.put("itemStatus", this.itemStatus);

        return json;
    }

}
