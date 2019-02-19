// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class HotelItem implements Serializable {

    private static final long serialVersionUID = -3958723723992864161L;
    /**
     * 商品id
     */
    public long itemId;

    /**
     * 商品名称
     */
    public String name;

    /**
     * 商品SKU id列表
     */
    public long[] itemSkuIds;
    /**
     * 销售方名称
     */
    public String sellerName;

    /**
     * 销售方id
     */
    public long sellerId;

    /**
     * 价格
     */
    public long price;

    /**
     * 库存
     */
    public int stock;

    /**
     * 支付方式
     */
    public String payMode;

    /**
     * 退订规则
     */
    public String cancelRules;

    /**
     * 退订约束
     */
    public String cancelLimit;

    /**
     * 早餐
     */
    public String breakfast;


    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static HotelItem deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static HotelItem deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            HotelItem result = new HotelItem();

            // 商品id
            result.itemId = json.optLong("itemId");
            // 商品名称

            if (!json.isNull("name")) {
                result.name = json.optString("name", null);
            }
            // 商品SKU id列表
            JSONArray itemSkuIdsArray = json.optJSONArray("itemSkuIds");
            if (itemSkuIdsArray != null) {
                int len = itemSkuIdsArray.length();
                result.itemSkuIds = new long[len];
                for (int i = 0; i < len; i++) {
                    result.itemSkuIds[i] = itemSkuIdsArray.optLong(i);
                }
            }

            // 销售方名称

            if (!json.isNull("sellerName")) {
                result.sellerName = json.optString("sellerName", null);
            }
            // 销售方id
            result.sellerId = json.optLong("sellerId");
            // 价格
            result.price = json.optLong("price");
            // 库存
            result.stock = json.optInt("stock");
            // 支付方式

            if (!json.isNull("payMode")) {
                result.payMode = json.optString("payMode", null);
            }
            // 退订规则

            if (!json.isNull("cancelRules")) {
                result.cancelRules = json.optString("cancelRules", null);
            }
            // 退订约束

            if (!json.isNull("cancelLimit")) {
                result.cancelLimit = json.optString("cancelLimit", null);
            }

            // 早餐

            if (!json.isNull("breakfast")) {
                result.breakfast = json.optString("breakfast", null);
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

        // 商品id
        json.put("itemId", this.itemId);

        // 商品名称
        if (this.name != null) {
            json.put("name", this.name);
        }

        // 商品SKU id列表 
        if (this.itemSkuIds != null) {
            JSONArray itemSkuIdsArray = new JSONArray();
            for (long value : this.itemSkuIds) {
                itemSkuIdsArray.put(value);
            }
            json.put("itemSkuIds", itemSkuIdsArray);
        }

        // 销售方名称
        if (this.sellerName != null) {
            json.put("sellerName", this.sellerName);
        }

        // 销售方id
        json.put("sellerId", this.sellerId);

        // 价格
        json.put("price", this.price);

        // 库存
        json.put("stock", this.stock);

        // 支付方式
        if (this.payMode != null) {
            json.put("payMode", this.payMode);
        }

        // 退订规则
        if (this.cancelRules != null) {
            json.put("cancelRules", this.cancelRules);
        }

        // 退订约束
        if (this.cancelLimit != null) {
            json.put("cancelLimit", this.cancelLimit);
        }
        // 早餐
        if (this.breakfast != null) {
            json.put("breakfast", this.breakfast);
        }

        return json;
    }

}
  