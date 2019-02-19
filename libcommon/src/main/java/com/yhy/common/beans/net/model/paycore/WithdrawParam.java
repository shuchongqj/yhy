package com.yhy.common.beans.net.model.paycore;

//import com.smart.sdk.api.resp.Api_PAYCORE_WithdrawParam;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:WithdrawParam
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-13
 * Time:13:32
 * Version 1.1.0
 */

public class WithdrawParam implements Serializable {
    private static final long serialVersionUID = 6222138002653587819L;

    /**
     * 支付密码
     */
    public String payPwd;

    /**
     * 提现金额
     */
    public long withdrawAmount;

    /**
     * 绑定银行卡ID
     */
    public long bindCardId;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static WithdrawParam deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static WithdrawParam deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            WithdrawParam result = new WithdrawParam();

            // 支付密码

            if(!json.isNull("payPwd")){
                result.payPwd = json.optString("payPwd", null);
            }
            // 提现金额
            result.withdrawAmount = json.optLong("withdrawAmount");
            // 绑定银行卡ID
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

        // 支付密码
        if(this.payPwd != null) { json.put("payPwd", this.payPwd); }

        // 提现金额
        json.put("withdrawAmount", this.withdrawAmount);

        // 绑定银行卡ID
        json.put("bindCardId", this.bindCardId);

        return json;
    }
}
