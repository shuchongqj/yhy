package com.yhy.common.beans.net.model.paycore;

//import com.smart.sdk.api.resp.Api_PAYCORE_UpdatePayPwdParam;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:UpdatePayPwdParam
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-13
 * Time:13:20
 * Version 1.1.0
 */

public class UpdatePayPwdParam implements Serializable {
    private static final long serialVersionUID = -2780145313611187437L;

    /**
     * 旧密码
     */
    public String oldPayPwd;

    /**
     * 新密码
     */
    public String payPwd;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static UpdatePayPwdParam deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static UpdatePayPwdParam deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            UpdatePayPwdParam result = new UpdatePayPwdParam();

            // 旧密码

            if(!json.isNull("oldPayPwd")){
                result.oldPayPwd = json.optString("oldPayPwd", null);
            }
            // 新密码

            if(!json.isNull("payPwd")){
                result.payPwd = json.optString("payPwd", null);
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

        // 旧密码
        if(this.oldPayPwd != null) { json.put("oldPayPwd", this.oldPayPwd); }

        // 新密码
        if(this.payPwd != null) { json.put("payPwd", this.payPwd); }

        return json;
    }
}
