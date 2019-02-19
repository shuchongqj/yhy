package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:PayInfo
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-4
 * Time:14:37
 * Version 1.0
 * Description:
 */
public class PayInfo implements Serializable{

    /**
     * 支付的类型,ALL_MONEY 全现金,MONEY_OR_POINT 全现金或者用积分抵扣部分
     */
    public String payType;

    /**
     * 最多可以使用的积分
     */
    public long maxPoint;

    /**
     * 最少可以使用的积分
     */
    public long minPoint;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static PayInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static PayInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            PayInfo result = new PayInfo();

            // 支付的类型,ALL_MONEY 全现金,MONEY_OR_POINT 全现金或者用积分抵扣部分

            if(!json.isNull("payType")){
                result.payType = json.optString("payType", null);
            }
            // 最多可以使用的积分
            result.maxPoint = json.optLong("maxPoint");
            // 最少可以使用的积分
            result.minPoint = json.optLong("minPoint");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 支付的类型,ALL_MONEY 全现金,MONEY_OR_POINT 全现金或者用积分抵扣部分
        if(this.payType != null) { json.put("payType", this.payType); }

        // 最多可以使用的积分
        json.put("maxPoint", this.maxPoint);

        return json;
    }

}
