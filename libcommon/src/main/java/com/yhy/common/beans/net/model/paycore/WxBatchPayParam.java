package com.yhy.common.beans.net.model.paycore;

//import com.smart.sdk.api.resp.Api_PAYCORE_WxBatchPayParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:WxBatchPayParam
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-10-31
 * Time:18:18
 * Version 1.1.0
 */


public class WxBatchPayParam implements Serializable {
    private static final long serialVersionUID = 704919514759042076L;

    /**
     * 批量订单号
     */
    public long[] bizOrderIdList;
    /**
     * 支付渠道:PAY_WX：微信支付
     */
    public String payChannel;

    /**
     * 公众号支付:JSAPI,原生扫码支付:NATIVE,APP支付:APP,WAP端支付：WAP
     */
    public String paySubChannel;

    /**
     * 来源,手机应用:APP,手机网页:WAP,PC网页:WEB,其他:OTHER
     */
    public String sourceType;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static WxBatchPayParam deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static WxBatchPayParam deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            WxBatchPayParam result = new WxBatchPayParam();

            // 批量订单号
            JSONArray bizOrderIdListArray = json.optJSONArray("bizOrderIdList");
            if (bizOrderIdListArray != null) {
                int len = bizOrderIdListArray.length();
                result.bizOrderIdList = new long[len];
                for (int i = 0; i < len; i++) {
                    result.bizOrderIdList[i] = bizOrderIdListArray.optLong(i);
                }
            }

            // 支付渠道:PAY_WX：微信支付

            if(!json.isNull("payChannel")){
                result.payChannel = json.optString("payChannel", null);
            }
            // 公众号支付:JSAPI,原生扫码支付:NATIVE,APP支付:APP,WAP端支付：WAP

            if(!json.isNull("paySubChannel")){
                result.paySubChannel = json.optString("paySubChannel", null);
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

        // 支付渠道:PAY_WX：微信支付
        if(this.payChannel != null) { json.put("payChannel", this.payChannel); }

        // 公众号支付:JSAPI,原生扫码支付:NATIVE,APP支付:APP,WAP端支付：WAP
        if(this.paySubChannel != null) { json.put("paySubChannel", this.paySubChannel); }

        // 来源,手机应用:APP,手机网页:WAP,PC网页:WEB,其他:OTHER
        if(this.sourceType != null) { json.put("sourceType", this.sourceType); }

        return json;
    }
}
