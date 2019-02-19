package com.yhy.common.beans.net.model.paycore;

//import com.smart.sdk.api.resp.Api_PAYCORE_VerifyIdentityResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:VerifyIdentityResult
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-13
 * Time:13:25
 * Version 1.1.0
 */

public class VerifyIdentityResult implements Serializable {
    private static final long serialVersionUID = -7757520679941257979L;

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
     * 身份认证码,有效期3分钟
     */
    public String verifyIdentityCode;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static VerifyIdentityResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static VerifyIdentityResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            VerifyIdentityResult result = new VerifyIdentityResult();

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
            // 身份认证码,有效期3分钟

            if(!json.isNull("verifyIdentityCode")){
                result.verifyIdentityCode = json.optString("verifyIdentityCode", null);
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

        // 是否成功
        json.put("success", this.success);

        // 错误信息
        if(this.errorMsg != null) { json.put("errorMsg", this.errorMsg); }

        // 错误码
        if(this.errorCode != null) { json.put("errorCode", this.errorCode); }

        // 身份认证码,有效期3分钟
        if(this.verifyIdentityCode != null) { json.put("verifyIdentityCode", this.verifyIdentityCode); }

        return json;
    }
}
