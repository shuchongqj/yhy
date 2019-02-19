package com.yhy.common.beans.net.model.paycore;

//import com.smart.sdk.api.resp.Api_PAYCORE_SetupPayPwdParam;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:SetupPayPwdParam
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-13
 * Time:13:15
 * Version 1.1.0
 */

public class SetupPayPwdParam implements Serializable {
    private static final long serialVersionUID = -7194309379822351088L;

    /**
     * 验证码
     */
    public String verifyCode;

    /**
     * 验证码类型,单独设置支付密码：SETUP_PAY_PWD,忘记密码:FORGET_PAY_PWD,开通电子账户时设置支付密码：OPEN_ELE_ACCOUNT
     */
    public String verifyCodeType;

    /**
     * 身份验证码
     */
    public String verifyIdentityCode;

    /**
     * 新密码
     */
    public String payPwd;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SetupPayPwdParam deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SetupPayPwdParam deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SetupPayPwdParam result = new SetupPayPwdParam();

            // 验证码

            if(!json.isNull("verifyCode")){
                result.verifyCode = json.optString("verifyCode", null);
            }
            // 验证码类型,单独设置支付密码：SETUP_PAY_PWD,忘记密码:FORGET_PAY_PWD,开通电子账户时设置支付密码：OPEN_ELE_ACCOUNT

            if(!json.isNull("verifyCodeType")){
                result.verifyCodeType = json.optString("verifyCodeType", null);
            }
            // 身份验证码

            if(!json.isNull("verifyIdentityCode")){
                result.verifyIdentityCode = json.optString("verifyIdentityCode", null);
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

        // 验证码
        if(this.verifyCode != null) { json.put("verifyCode", this.verifyCode); }

        // 验证码类型,单独设置支付密码：SETUP_PAY_PWD,忘记密码:FORGET_PAY_PWD,开通电子账户时设置支付密码：OPEN_ELE_ACCOUNT
        if(this.verifyCodeType != null) { json.put("verifyCodeType", this.verifyCodeType); }

        // 身份验证码
        if(this.verifyIdentityCode != null) { json.put("verifyIdentityCode", this.verifyIdentityCode); }

        // 新密码
        if(this.payPwd != null) { json.put("payPwd", this.payPwd); }

        return json;
    }
}
