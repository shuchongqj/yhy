package com.yhy.common.beans.net.model.tm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:ShopItems
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-12-21
 * Time:15:20
 * Version 1.1.0
 */


public class ShopItems implements Serializable {
    private static final long serialVersionUID = -7668502951728450757L;
    /**
     * 店铺ID
     */
    public long sellerId;

    /**
     * 店铺名
     */
    public String sellerName;

    /**
     * 商品总价
     */
    public long totalFee;

    /**
     * 商品合计数量
     */
    public int totalCount;

    /**
     * 商品相关信息
     */
    public List<TmItemInfo> itemInfoList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ShopItems deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ShopItems deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ShopItems result = new ShopItems();

            // 店铺ID
            result.sellerId = json.optLong("sellerId");
            // 店铺名

            if(!json.isNull("sellerName")){
                result.sellerName = json.optString("sellerName", null);
            }
            // 商品总价
            result.totalFee = json.optLong("totalFee");
            // 商品合计数量
            result.totalCount = json.optInt("totalCount");
            // 商品相关信息
            JSONArray itemInfoListArray = json.optJSONArray("itemInfoList");
            if (itemInfoListArray != null) {
                int len = itemInfoListArray.length();
                result.itemInfoList = new ArrayList<TmItemInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = itemInfoListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.itemInfoList.add(TmItemInfo.deserialize(jo));
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

        // 店铺ID
        json.put("sellerId", this.sellerId);

        // 店铺名
        if(this.sellerName != null) { json.put("sellerName", this.sellerName); }

        // 商品总价
        json.put("totalFee", this.totalFee);

        // 商品合计数量
        json.put("totalCount", this.totalCount);

        // 商品相关信息
        if (this.itemInfoList != null) {
            JSONArray itemInfoListArray = new JSONArray();
            for (TmItemInfo value : this.itemInfoList)
            {
                if (value != null) {
                    itemInfoListArray.put(value.serialize());
                }
            }
            json.put("itemInfoList", itemInfoListArray);
        }

        return json;
    }

}
