package com.yhy.common.beans.net.model.paycore;

//import com.smart.sdk.api.resp.Api_PAYCORE_Settlement;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:Settlement
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-13
 * Time:12:54
 * Version 1.1.0
 */

public class Settlement implements Serializable {
    private static final long serialVersionUID = -4839732252687854966L;

    /**
     * 交易完成时间
     */
    public String tradeFinishedTime;

    /**
     * 结算单号
     */
    public long settlementId;

    /**
     * 向银联发起结算请求的日期
     */
    public String reqDate;

    /**
     * 结算金额
     */
    public long settlementAmount;

    /**
     * 服务费（交易金额*1%）
     */
    public long serviceFee;

    /**
     * 订单金额
     */
    public long tradeAmount;

    /**
     * 订单名称
     */
    public String bizOrderName;

    /**
     * 订单号
     */
    public long bizOrderId;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Settlement deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Settlement deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            Settlement result = new Settlement();

            // 交易完成时间

            if(!json.isNull("tradeFinishedTime")){
                result.tradeFinishedTime = json.optString("tradeFinishedTime", null);
            }
            // 结算单号
            result.settlementId = json.optLong("settlementId");
            // 向银联发起结算请求的日期

            if(!json.isNull("reqDate")){
                result.reqDate = json.optString("reqDate", null);
            }
            // 结算金额
            result.settlementAmount = json.optLong("settlementAmount");
            // 服务费（交易金额*1%）
            result.serviceFee = json.optLong("serviceFee");
            // 订单金额
            result.tradeAmount = json.optLong("tradeAmount");
            // 订单名称

            if(!json.isNull("bizOrderName")){
                result.bizOrderName = json.optString("bizOrderName", null);
            }
            // 订单号
            result.bizOrderId = json.optLong("bizOrderId");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 交易完成时间
        if(this.tradeFinishedTime != null) { json.put("tradeFinishedTime", this.tradeFinishedTime); }

        // 结算单号
        json.put("settlementId", this.settlementId);

        // 向银联发起结算请求的日期
        if(this.reqDate != null) { json.put("reqDate", this.reqDate); }

        // 结算金额
        json.put("settlementAmount", this.settlementAmount);

        // 服务费（交易金额*1%）
        json.put("serviceFee", this.serviceFee);

        // 订单金额
        json.put("tradeAmount", this.tradeAmount);

        // 订单名称
        if(this.bizOrderName != null) { json.put("bizOrderName", this.bizOrderName); }

        // 订单号
        json.put("bizOrderId", this.bizOrderId);

        return json;
    }
}
