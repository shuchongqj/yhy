package com.yhy.common.beans.net.model.tm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:ShopInfoResult
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-10-25
 * Time:9:46
 * Version 1.1.0
 */


public class ShopInfoResult implements Serializable {
    private static final long serialVersionUID = -1567762788748464703L;

    /**
     * 商家信息
     */
    public SellerInfo sellerInfo;

    /**
     * 商品信息
     */
    public List<ItemInfoResult> itemInfoResultList;
    /**
     * 店铺勾选状态
     */
    public boolean shopCheck;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ShopInfoResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ShopInfoResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ShopInfoResult result = new ShopInfoResult();

            // 商家信息
            result.sellerInfo = SellerInfo.deserialize(json.optJSONObject("sellerInfo"));
            // 商品信息
            JSONArray itemInfoResultListArray = json.optJSONArray("itemInfoResultList");
            if (itemInfoResultListArray != null) {
                int len = itemInfoResultListArray.length();
                result.itemInfoResultList = new ArrayList<ItemInfoResult>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = itemInfoResultListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.itemInfoResultList.add(ItemInfoResult.deserialize(jo));
                    }
                }
            }

            // 店铺勾选状态
            result.shopCheck = json.optBoolean("shopCheck");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 商家信息
        if (this.sellerInfo != null) { json.put("sellerInfo", this.sellerInfo.serialize()); }

        // 商品信息
        if (this.itemInfoResultList != null) {
            JSONArray itemInfoResultListArray = new JSONArray();
            for (ItemInfoResult value : this.itemInfoResultList)
            {
                if (value != null) {
                    itemInfoResultListArray.put(value.serialize());
                }
            }
            json.put("itemInfoResultList", itemInfoResultListArray);
        }

        // 店铺勾选状态
        json.put("shopCheck", this.shopCheck);

        return json;
    }

}
