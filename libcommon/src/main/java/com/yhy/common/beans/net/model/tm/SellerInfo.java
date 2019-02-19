package com.yhy.common.beans.net.model.tm;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:SellerInfo
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-12-14
 * Time:10:22
 * Version 1.1.0
 */


public class SellerInfo implements Serializable {
    private static final long serialVersionUID = -4180138724320572493L;

    /**
     * 商家id
     */
    public long sellerId;

    /**
     * 商家名称
     */
    public String sellerName;

    /**
     * 商家头像
     */
    public String sellerPic;

    /**
     * 商家是否有优惠券
     */
    public boolean hasPromotion;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SellerInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SellerInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SellerInfo result = new SellerInfo();

            // 商家id
            result.sellerId = json.optLong("sellerId");
            // 商家名称

            if(!json.isNull("sellerName")){
                result.sellerName = json.optString("sellerName", null);
            }
            // 商家头像

            if(!json.isNull("sellerPic")){
                result.sellerPic = json.optString("sellerPic", null);
            }
            // 商家是否有优惠券
            result.hasPromotion = json.optBoolean("hasPromotion");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 商家id
        json.put("sellerId", this.sellerId);

        // 商家名称
        if(this.sellerName != null) { json.put("sellerName", this.sellerName); }

        // 商家头像
        if(this.sellerPic != null) { json.put("sellerPic", this.sellerPic); }

        // 商家是否有优惠券
        json.put("hasPromotion", this.hasPromotion);

        return json;
    }
}
