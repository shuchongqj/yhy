package com.yhy.common.beans.net.model.paycore;

//import com.smart.sdk.api.resp.Api_PAYCORE_CebCloudBatchPayParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:CebCloudBatchPayParam
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-12-28
 * Time:11:37
 * Version 1.1.0
 */


public class CebCloudBatchPayParam implements Serializable {
    private static final long serialVersionUID = 576739431528388979L;

    /**
     * 批量订单号
     */
    public long[] bizOrderIdList;
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
    public static CebCloudBatchPayParam deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static CebCloudBatchPayParam deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            CebCloudBatchPayParam result = new CebCloudBatchPayParam();

            // 批量订单号
            JSONArray bizOrderIdListArray = json.optJSONArray("bizOrderIdList");
            if (bizOrderIdListArray != null) {
                int len = bizOrderIdListArray.length();
                result.bizOrderIdList = new long[len];
                for (int i = 0; i < len; i++) {
                    result.bizOrderIdList[i] = bizOrderIdListArray.optLong(i);
                }
            }

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

        // 批量订单号
        if (this.bizOrderIdList != null) {
            JSONArray bizOrderIdListArray = new JSONArray();
            for (long value : this.bizOrderIdList)
            {
                bizOrderIdListArray.put(value);
            }
            json.put("bizOrderIdList", bizOrderIdListArray);
        }

        // 同步返回的地址
        if(this.returnUrl != null) { json.put("returnUrl", this.returnUrl); }

        // 来源,手机应用:APP,手机网页:WAP,PC网页:WEB,其他:OTHER
        if(this.sourceType != null) { json.put("sourceType", this.sourceType); }

        return json;
    }
}
