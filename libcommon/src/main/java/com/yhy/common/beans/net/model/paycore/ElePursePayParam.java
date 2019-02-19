package com.yhy.common.beans.net.model.paycore;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:ElePursePayParam
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-17
 * Time:19:14
 * Version 1.1.0
 */

public class ElePursePayParam implements Serializable {
    private static final long serialVersionUID = 1074699228368625773L;

    /**
     * 订单号
     */
    public long bizOrderId;

    /**
     * 支付密码
     */
    public String payPwd;

    /**
     * 来源,手机应用:APP,手机网页:WAP,PC网页:WEB,其他:OTHER
     */
    public String sourceType;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ElePursePayParam deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ElePursePayParam deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ElePursePayParam result = new ElePursePayParam();

            // 订单号
            result.bizOrderId = json.optLong("bizOrderId");
            // 支付密码

            if(!json.isNull("payPwd")){
                result.payPwd = json.optString("payPwd", null);
            }
            // 来源,手机应用:APP,手机网页:WAP,PC网页:WEB,其他:OTHER

            if(!json.isNull("sourceType")){
                result.sourceType = json.optString("sourceType", null);
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

        // 订单号
        json.put("bizOrderId", this.bizOrderId);

        // 支付密码
        if(this.payPwd != null) { json.put("payPwd", this.payPwd); }

        // 来源,手机应用:APP,手机网页:WAP,PC网页:WEB,其他:OTHER
        if(this.sourceType != null) { json.put("sourceType", this.sourceType); }

        return json;
    }
}
