package com.yhy.common.beans.net.model.paycore;

//import com.smart.sdk.api.resp.Api_PAYCORE_EleAccountInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:EleAccountInfo
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-12
 * Time:15:20
 * Version 1.1.0
 */

public class EleAccountInfo implements Serializable {
    private static final long serialVersionUID = -6215036219867135480L;

    /**
     * 用户名
     */
    public String userName;

    /**
     * 用户昵称
     */
    public String nickName;

    /**
     * 是否有支付密码
     */
    public boolean existPayPwd;

    /**
     * 账户余额
     */
    public long accountBalance;

    /**
     * 状态,生效:TACK_EFFECT,冻结:FREEZE,注销:LOGOUT,无效:INVALID,已认证:VERIFIED
     */
    public String status;

    /**
     * 是否是商户
     */
    public boolean isMerchant;

    /**
     * 账户类型,个人:PERSON,企业:COMPANY
     */
    public String accountType;

    /**
     * 已上传:UPLOADED_DONE,未上传:NOT_UPLOAD
     */
    public String idCardPhotoStatus;

    /**
     * 是否强制上传身份证,是:YES,不是:NO
     */
    public String isForcedUploadPhoto;

    /**
     * 上传提示语
     */
    public String uploadMsg;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static EleAccountInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static EleAccountInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            EleAccountInfo result = new EleAccountInfo();

            // 用户名

            if(!json.isNull("userName")){
                result.userName = json.optString("userName", null);
            }
            // 用户昵称

            if(!json.isNull("nickName")){
                result.nickName = json.optString("nickName", null);
            }
            // 是否有支付密码
            result.existPayPwd = json.optBoolean("existPayPwd");
            // 账户余额
            result.accountBalance = json.optLong("accountBalance");
            // 状态,生效:TACK_EFFECT,冻结:FREEZE,注销:LOGOUT,无效:INVALID,已认证:VERIFIED

            if(!json.isNull("status")){
                result.status = json.optString("status", null);
            }
            // 是否是商户
            result.isMerchant = json.optBoolean("isMerchant");
            // 账户类型,个人:PERSON,企业:COMPANY

            if(!json.isNull("accountType")){
                result.accountType = json.optString("accountType", null);
            }

            // 已上传:UPLOADED_DONE,未上传:NOT_UPLOAD

            if(!json.isNull("idCardPhotoStatus")){
                result.idCardPhotoStatus = json.optString("idCardPhotoStatus", null);
            }

            // 是否强制上传身份证,是:YES,不是:NO

            if(!json.isNull("isForcedUploadPhoto")){
                result.isForcedUploadPhoto = json.optString("isForcedUploadPhoto", null);
            }
            // 上传提示语

            if(!json.isNull("uploadMsg")){
                result.uploadMsg = json.optString("uploadMsg", null);
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

        // 用户名
        if(this.userName != null) { json.put("userName", this.userName); }

        // 用户昵称
        if(this.nickName != null) { json.put("nickName", this.nickName); }

        // 是否有支付密码
        json.put("existPayPwd", this.existPayPwd);

        // 账户余额
        json.put("accountBalance", this.accountBalance);

        // 状态,生效:TACK_EFFECT,冻结:FREEZE,注销:LOGOUT,无效:INVALID,已认证:VERIFIED
        if(this.status != null) { json.put("status", this.status); }

        // 是否是商户
        json.put("isMerchant", this.isMerchant);

        // 账户类型,个人:PERSON,企业:COMPANY
        if(this.accountType != null) { json.put("accountType", this.accountType); }

        // 已上传:UPLOADED_DONE,未上传:NOT_UPLOAD
        if(this.idCardPhotoStatus != null) { json.put("idCardPhotoStatus", this.idCardPhotoStatus); }

        // 是否强制上传身份证,是:YES,不是:NO
        if(this.isForcedUploadPhoto != null) { json.put("isForcedUploadPhoto", this.isForcedUploadPhoto); }

        // 上传提示语
        if(this.uploadMsg != null) { json.put("uploadMsg", this.uploadMsg); }

        return json;
    }
}
