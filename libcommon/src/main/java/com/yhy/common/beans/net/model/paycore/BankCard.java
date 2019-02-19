package com.yhy.common.beans.net.model.paycore;

//import com.smart.sdk.api.resp.Api_PAYCORE_BankCard;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:BankCard
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-12
 * Time:14:49
 * Version 1.1.0
 */

public class BankCard implements Serializable {
    private static final long serialVersionUID = 4659183302989416435L;

    /**
     * 银行名称
     */
    public String bankName;

    /**
     * 银行卡号
     */
    public String bankCardNo;

    /**
     * 绑定银行卡ID
     */
    public long bindCardId;

    /**
     * 银行卡类型,储蓄卡:DEPOSIT_CARD,信用卡:CREDIT_CARD
     */
    public String bankCardType;

    /**
     * 是否被绑定
     */
    public boolean isBind;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static BankCard deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static BankCard deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            BankCard result = new BankCard();

            // 银行名称

            if(!json.isNull("bankName")){
                result.bankName = json.optString("bankName", null);
            }
            // 银行卡号

            if(!json.isNull("bankCardNo")){
                result.bankCardNo = json.optString("bankCardNo", null);
            }
            // 绑定银行卡ID
            result.bindCardId = json.optLong("bindCardId");
            // 银行卡类型,储蓄卡:DEPOSIT_CARD,信用卡:CREDIT_CARD

            if(!json.isNull("bankCardType")){
                result.bankCardType = json.optString("bankCardType", null);
            }
            // 是否被绑定
            result.isBind = json.optBoolean("isBind");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 银行名称
        if(this.bankName != null) { json.put("bankName", this.bankName); }

        // 银行卡号
        if(this.bankCardNo != null) { json.put("bankCardNo", this.bankCardNo); }

        // 绑定银行卡ID
        json.put("bindCardId", this.bindCardId);

        // 银行卡类型,储蓄卡:DEPOSIT_CARD,信用卡:CREDIT_CARD
        if(this.bankCardType != null) { json.put("bankCardType", this.bankCardType); }

        // 是否被绑定
        json.put("isBind", this.isBind);

        return json;
    }
}
