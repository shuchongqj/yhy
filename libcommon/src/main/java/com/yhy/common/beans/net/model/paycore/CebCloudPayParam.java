package com.yhy.common.beans.net.model.paycore;

//import com.smart.sdk.api.resp.Api_PAYCORE_CebCloudPayParam;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:CebCloudPayParam
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-12
 * Time:14:55
 * Version 1.1.0
 */

public class CebCloudPayParam implements Serializable {
    private static final long serialVersionUID = 2052346242254495411L;

    /**
     * 订单ID
     */
    public long bizOrderId;

    /**
     * 同步返回的地址
     */
    public String returnUrl;

    /**
     * 来源,手机应用:APP,手机网页:WAP,PC网页:WEB,其他:OTHER
     */
    public String sourceType;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static CebCloudPayParam deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static CebCloudPayParam deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            CebCloudPayParam result = new CebCloudPayParam();

            // 订单ID
            result.bizOrderId = json.optLong("bizOrderId");
            // 同步返回的地址

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

        // 订单ID
        json.put("bizOrderId", this.bizOrderId);

        // 同步返回的地址
        if(this.returnUrl != null) { json.put("returnUrl", this.returnUrl); }

        // 来源,手机应用:APP,手机网页:WAP,PC网页:WEB,其他:OTHER
        if(this.sourceType != null) { json.put("sourceType", this.sourceType); }

        return json;
    }
}
