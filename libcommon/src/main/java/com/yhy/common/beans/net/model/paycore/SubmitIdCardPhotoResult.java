package com.yhy.common.beans.net.model.paycore;

//import com.smart.sdk.api.resp.Api_PAYCORE_SubmitIdCardPhotoResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:SubmitIdCardPhotoResult
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-10-31
 * Time:14:31
 * Version 1.1.0
 */


public class SubmitIdCardPhotoResult implements Serializable {
    private static final long serialVersionUID = 7341972817021130242L;
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
     * 客户真实姓名
     */
    public String userName;

    /**
     * 身份证号
     */
    public String idNo;

    /**
     * 身份证有效期
     */
    public String idCardValidDate;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SubmitIdCardPhotoResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SubmitIdCardPhotoResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SubmitIdCardPhotoResult result = new SubmitIdCardPhotoResult();

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
            // 客户真实姓名

            if(!json.isNull("userName")){
                result.userName = json.optString("userName", null);
            }
            // 身份证号

            if(!json.isNull("idNo")){
                result.idNo = json.optString("idNo", null);
            }
            // 身份证有效期

            if(!json.isNull("idCardValidDate")){
                result.idCardValidDate = json.optString("idCardValidDate", null);
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

        // 客户真实姓名
        if(this.userName != null) { json.put("userName", this.userName); }

        // 身份证号
        if(this.idNo != null) { json.put("idNo", this.idNo); }

        // 身份证有效期
        if(this.idCardValidDate != null) { json.put("idCardValidDate", this.idCardValidDate); }

        return json;
    }
}
