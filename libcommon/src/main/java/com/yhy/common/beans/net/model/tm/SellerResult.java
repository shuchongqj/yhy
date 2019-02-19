package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:SellerResult
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-28
 * Time:20:07
 * Version 1.0
 * Description:
 */
public class SellerResult implements Serializable{
    /**
     * 店铺名
     */
    public String merchantName;

    /**
     * 店铺logo
     */
    public String logo;

    /**
     * sellerId
     */
    public long sellerId;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SellerResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SellerResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SellerResult result = new SellerResult();

            // 店铺名

            if(!json.isNull("merchantName")){
                result.merchantName = json.optString("merchantName", null);
            }
            // 店铺logo

            if(!json.isNull("logo")){
                result.logo = json.optString("logo", null);
            }
            // sellerId
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

        // 店铺名
        if(this.merchantName != null) { json.put("merchantName", this.merchantName); }

        // 店铺logo
        if(this.logo != null) { json.put("logo", this.logo); }

        // sellerId
        json.put("sellerId", this.sellerId);

        return json;
    }
}
