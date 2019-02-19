package com.yhy.common.beans.net.model.paycore;

//import com.smart.sdk.api.resp.Api_PAYCORE_AliPayInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:AliPayInfo
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-12-28
 * Time:11:33
 * Version 1.1.0
 */


public class AliPayInfo implements Serializable {
    private static final long serialVersionUID = -1087846900875713874L;

    /**
     * 支付宝支付请求信息
     */
    public String payInfo;

    /**
     * 是否成功
     */
    public boolean success;

    /**
     * 错误信息
     */
    public String errorMsg;

    /**
     * 错误码
     */
    public String errorCode;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static AliPayInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static AliPayInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            AliPayInfo result = new AliPayInfo();

            // 支付宝支付请求信息

            if(!json.isNull("payInfo")){
                result.payInfo = json.optString("payInfo", null);
            }
            // 是否成功
            result.success = json.optBoolean("success");
            // 错误信息

            if(!json.isNull("errorMsg")){
                result.errorMsg = json.optString("errorMsg", null);
            }
            // 错误码

            if(!json.isNull("errorCode")){
                result.errorCode = json.optString("errorCode", null);
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

        // 支付宝支付请求信息
        if(this.payInfo != null) { json.put("payInfo", this.payInfo); }

        // 是否成功
        json.put("success", this.success);

        // 错误信息
        if(this.errorMsg != null) { json.put("errorMsg", this.errorMsg); }

        // 错误码
        if(this.errorCode != null) { json.put("errorCode", this.errorCode); }

        return json;
    }
}
