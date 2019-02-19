package com.yhy.common.beans.net.model.paycore;

//import com.smart.sdk.api.resp.Api_PAYCORE_Bill;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:Bill
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-13
 * Time:12:27
 * Version 1.1.0
 */

public class Bill implements Serializable {
    private static final long serialVersionUID = -4294386709893643142L;

    /**
     * 交易类型,充值:TOP_UP,提现:WITHDRAW,结算:SETTLEMENT,余额支付:BALANCE_PAY,资金转入:TRANSFER_IN,资金转出:TRANSFER_OUT
     */
    public String transType;

    /**
     * 交易金额
     */
    public long transAmount;

    /**
     * 交易时间
     */
    public long transTime;

    /**
     * 交易单号
     */
    public String transOrderId;

    /**
     * 钱包余额
     */
    public long balance;

    /**
     * 备注
     */
    public String remark;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Bill deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Bill deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            Bill result = new Bill();

            // 交易类型,充值:TOP_UP,提现:WITHDRAW,结算:SETTLEMENT,余额支付:BALANCE_PAY,资金转入:TRANSFER_IN,资金转出:TRANSFER_OUT

            if(!json.isNull("transType")){
                result.transType = json.optString("transType", null);
            }
            // 交易金额
            result.transAmount = json.optLong("transAmount");
            // 交易时间
            result.transTime = json.optLong("transTime");
            // 交易单号

            if(!json.isNull("transOrderId")){
                result.transOrderId = json.optString("transOrderId", null);
            }
            // 钱包余额
            result.balance = json.optLong("balance");
            // 备注

            if(!json.isNull("remark")){
                result.remark = json.optString("remark", null);
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

        // 交易类型,充值:TOP_UP,提现:WITHDRAW,结算:SETTLEMENT,余额支付:BALANCE_PAY,资金转入:TRANSFER_IN,资金转出:TRANSFER_OUT
        if(this.transType != null) { json.put("transType", this.transType); }

        // 交易金额
        json.put("transAmount", this.transAmount);

        // 交易时间
        json.put("transTime", this.transTime);

        // 交易单号
        if(this.transOrderId != null) { json.put("transOrderId", this.transOrderId); }

        // 钱包余额
        json.put("balance", this.balance);

        // 备注
        if(this.remark != null) { json.put("remark", this.remark); }

        return json;
    }
}
