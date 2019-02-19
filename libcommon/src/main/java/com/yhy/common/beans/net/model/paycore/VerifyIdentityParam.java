package com.yhy.common.beans.net.model.paycore;

//import com.smart.sdk.api.resp.Api_PAYCORE_VerifyIdentityParam;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:VerifyIdentityParam
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-13
 * Time:13:23
 * Version 1.1.0
 */

public class VerifyIdentityParam implements Serializable {
    private static final long serialVersionUID = -2584049665611641852L;

    /**
     * 用户姓名
     */
    public String userName;

    /**
     * 银行名称
     */
    public String bankName;

    /**
     * 银行卡号
     */
    public String bankCardNo;

    /**
     * 身份证号
     */
    public String idNo;

    /**
     * 手机号
     */
    public String mobilePhone;

    /**
     * 必填验证类型,钱包开户：OPEN_ELE_ACCOUNT,企业钱包开户：OPEN_CMP_ELE_ACCOUNT,绑定银行卡：BIND_BANK_CARD,设置支付密码：SETUP_PAY_PWD,忘记密码：FORGET_PAY_PWD,其他：OTHER
     */
    public String verifyIdentityType;

    /**
     * 商户类型,商户:IS_MERCHANT;非商户:NOT_MERCHANT
     */
    public String merchantType;

    public long bindCardId;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static VerifyIdentityParam deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static VerifyIdentityParam deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            VerifyIdentityParam result = new VerifyIdentityParam();

            // 用户姓名

            if(!json.isNull("userName")){
                result.userName = json.optString("userName", null);
            }
            // 银行名称

            if(!json.isNull("bankName")){
                result.bankName = json.optString("bankName", null);
            }
            // 银行卡号

            if(!json.isNull("bankCardNo")){
                result.bankCardNo = json.optString("bankCardNo", null);
            }
            // 身份证号

            if(!json.isNull("idNo")){
                result.idNo = json.optString("idNo", null);
            }
            // 手机号

            if(!json.isNull("mobilePhone")){
                result.mobilePhone = json.optString("mobilePhone", null);
            }
            // 必填验证类型,钱包开户：OPEN_ELE_ACCOUNT,企业钱包开户：OPEN_CMP_ELE_ACCOUNT,绑定银行卡：BIND_BANK_CARD,设置支付密码：SETUP_PAY_PWD,忘记密码：FORGET_PAY_PWD,其他：OTHER

            if(!json.isNull("verifyIdentityType")){
                result.verifyIdentityType = json.optString("verifyIdentityType", null);
            }
            // 商户类型,商户:IS_MERCHANT;非商户:NOT_MERCHANT

            if(!json.isNull("merchantType")){
                result.merchantType = json.optString("merchantType", null);
            }

            result.bindCardId = json.optLong("bindCardId");

            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 用户姓名
        if(this.userName != null) { json.put("userName", this.userName); }

        // 银行名称
        if(this.bankName != null) { json.put("bankName", this.bankName); }

        // 银行卡号
        if(this.bankCardNo != null) { json.put("bankCardNo", this.bankCardNo); }

        // 身份证号
        if(this.idNo != null) { json.put("idNo", this.idNo); }

        // 手机号
        if(this.mobilePhone != null) { json.put("mobilePhone", this.mobilePhone); }

        // 必填验证类型,钱包开户：OPEN_ELE_ACCOUNT,企业钱包开户：OPEN_CMP_ELE_ACCOUNT,绑定银行卡：BIND_BANK_CARD,设置支付密码：SETUP_PAY_PWD,忘记密码：FORGET_PAY_PWD,其他：OTHER
        if(this.verifyIdentityType != null) { json.put("verifyIdentityType", this.verifyIdentityType); }

        // 商户类型,商户:IS_MERCHANT;非商户:NOT_MERCHANT
        if(this.merchantType != null) { json.put("merchantType", this.merchantType); }

        // 绑定银行卡ID
        json.put("bindCardId", this.bindCardId);

        return json;
    }
}
