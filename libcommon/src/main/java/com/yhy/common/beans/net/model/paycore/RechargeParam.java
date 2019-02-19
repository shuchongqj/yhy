package com.yhy.common.beans.net.model.paycore;

//import com.smart.sdk.api.resp.Api_PAYCORE_RechargeParam;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:RechargeParam
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-13
 * Time:13:06
 * Version 1.1.0
 */

public class RechargeParam implements Serializable {
    private static final long serialVersionUID = 539694938488509599L;

    /**
     * 充值金额
     */
    public long rechargeAmount;

    /**
     * 返回跳转URL
     */
    public String returnUrl;

    /**
     * 来源,手机应用:APP,手机网页:WAP,PC网页:WEB,其他:OTHER
     */
    public String sourceType;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static RechargeParam deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static RechargeParam deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            RechargeParam result = new RechargeParam();

            // 充值金额
            result.rechargeAmount = json.optLong("rechargeAmount");
            // 返回跳转URL

            if(!json.isNull("returnUrl")){
                result.returnUrl = json.optString("returnUrl", null);
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

        // 充值金额
        json.put("rechargeAmount", this.rechargeAmount);

        // 返回跳转URL
        if(this.returnUrl != null) { json.put("returnUrl", this.returnUrl); }

        // 来源,手机应用:APP,手机网页:WAP,PC网页:WEB,其他:OTHER
        if(this.sourceType != null) { json.put("sourceType", this.sourceType); }

        return json;
    }
}
