package com.yhy.common.beans.net.model.paycore;

//import com.smart.sdk.api.resp.Api_PAYCORE_BankCard;
//import com.smart.sdk.api.resp.Api_PAYCORE_BankCardList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:BankCardList
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-13
 * Time:12:33
 * Version 1.1.0
 */

public class BankCardList implements Serializable {
    private static final long serialVersionUID = 3086564915212891271L;

    /**
     * 银行卡列表
     */
    public List<BankCard> bankCardList;
    /**
     * 总数
     */
    public int totalCount;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static BankCardList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static BankCardList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            BankCardList result = new BankCardList();

            // 银行卡列表
            JSONArray bankCardListArray = json.optJSONArray("bankCardList");
            if (bankCardListArray != null) {
                int len = bankCardListArray.length();
                result.bankCardList = new ArrayList<BankCard>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = bankCardListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.bankCardList.add(BankCard.deserialize(jo));
                    }
                }
            }

            // 总数
            result.totalCount = json.optInt("totalCount");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 银行卡列表
        if (this.bankCardList != null) {
            JSONArray bankCardListArray = new JSONArray();
            for (BankCard value : this.bankCardList)
            {
                if (value != null) {
                    bankCardListArray.put(value.serialize());
                }
            }
            json.put("bankCardList", bankCardListArray);
        }

        // 总数
        json.put("totalCount", this.totalCount);

        return json;
    }
}
