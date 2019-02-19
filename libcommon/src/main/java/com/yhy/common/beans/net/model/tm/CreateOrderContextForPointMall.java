package com.yhy.common.beans.net.model.tm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:CreateOrderContextForPointMall
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-12-21
 * Time:15:10
 * Version 1.1.0
 */


public class CreateOrderContextForPointMall implements Serializable {
    private static final long serialVersionUID = -8453809083847847461L;

    /**
     * 默认收货地址，如果没有返回null
     */
    public Address defaultAddress;

    /**
     * domain
     */
    public int domain;

    /**
     * 店铺订单
     */
    public List<Shop> shopList;
    /**
     * 子订单
     */
    public List<OrderItem> invalidOrderItemList;
    /**
     * 订单商品总价-优惠后
     */
    public long actualTotalFee;

    /**
     * 订单商品总价-优惠前
     */
    public long originalTotalFee;

    /**
     * 订单中全部商品可用积分总和
     */
    public long allPoint;

    /**
     * 积分可抵扣金额总和
     */
    public long pointDeductionFee;

    /**
     * 店铺订单
     */
    public List<ShopItems> shopItemsList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static CreateOrderContextForPointMall deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static CreateOrderContextForPointMall deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            CreateOrderContextForPointMall result = new CreateOrderContextForPointMall();

            // 默认收货地址，如果没有返回null
            result.defaultAddress = Address.deserialize(json.optJSONObject("defaultAddress"));
            // domain
            result.domain = json.optInt("domain");
            // 店铺订单
            JSONArray shopListArray = json.optJSONArray("shopList");
            if (shopListArray != null) {
                int len = shopListArray.length();
                result.shopList = new ArrayList<Shop>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = shopListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.shopList.add(Shop.deserialize(jo));
                    }
                }
            }

            // 子订单
            JSONArray invalidOrderItemListArray = json.optJSONArray("invalidOrderItemList");
            if (invalidOrderItemListArray != null) {
                int len = invalidOrderItemListArray.length();
                result.invalidOrderItemList = new ArrayList<OrderItem>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = invalidOrderItemListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.invalidOrderItemList.add(OrderItem.deserialize(jo));
                    }
                }
            }

            // 订单商品总价-优惠后
            result.actualTotalFee = json.optLong("actualTotalFee");
            // 订单商品总价-优惠前
            result.originalTotalFee = json.optLong("originalTotalFee");
            // 订单中全部商品可用积分总和
            result.allPoint = json.optLong("allPoint");
            // 积分可抵扣金额总和
            result.pointDeductionFee = json.optLong("pointDeductionFee");
            // 店铺订单
            JSONArray shopItemsListArray = json.optJSONArray("shopItemsList");
            if (shopItemsListArray != null) {
                int len = shopItemsListArray.length();
                result.shopItemsList = new ArrayList<ShopItems>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = shopItemsListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.shopItemsList.add(ShopItems.deserialize(jo));
                    }
                }
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

        // 默认收货地址，如果没有返回null
        if (this.defaultAddress != null) { json.put("defaultAddress", this.defaultAddress.serialize()); }

        // domain
        json.put("domain", this.domain);

        // 店铺订单
        if (this.shopList != null) {
            JSONArray shopListArray = new JSONArray();
            for (Shop value : this.shopList)
            {
                if (value != null) {
                    shopListArray.put(value.serialize());
                }
            }
            json.put("shopList", shopListArray);
        }

        // 子订单
        if (this.invalidOrderItemList != null) {
            JSONArray invalidOrderItemListArray = new JSONArray();
            for (OrderItem value : this.invalidOrderItemList)
            {
                if (value != null) {
                    invalidOrderItemListArray.put(value.serialize());
                }
            }
            json.put("invalidOrderItemList", invalidOrderItemListArray);
        }

        // 订单商品总价-优惠后
        json.put("actualTotalFee", this.actualTotalFee);

        // 订单商品总价-优惠前
        json.put("originalTotalFee", this.originalTotalFee);

        // 订单中全部商品可用积分总和
        json.put("allPoint", this.allPoint);

        // 积分可抵扣金额总和
        json.put("pointDeductionFee", this.pointDeductionFee);

        // 店铺订单
        if (this.shopItemsList != null) {
            JSONArray shopItemsListArray = new JSONArray();
            for (ShopItems value : this.shopItemsList)
            {
                if (value != null) {
                    shopItemsListArray.put(value.serialize());
                }
            }
            json.put("shopItemsList", shopItemsListArray);
        }

        return json;
    }
}
