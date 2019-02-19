package com.yhy.common.beans.net.model.paycore;

//import com.smart.sdk.api.resp.Api_PAYCORE_Settlement;
//import com.smart.sdk.api.resp.Api_PAYCORE_SettlementList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:SettlementList
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-13
 * Time:12:53
 * Version 1.1.0
 */

public class SettlementList implements Serializable {
    private static final long serialVersionUID = 1880658455991665485L;

    /**
     * 结算列表
     */
    public List<Settlement> list;
    /**
     * 结果数目
     */
    public int totalCount;

    /**
     * 已结总额
     */
    public long settlementTotalAmount;

    /**
     * 订单总额
     */
    public long tradeTotalAmount;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SettlementList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SettlementList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SettlementList result = new SettlementList();

            // 结算列表
            JSONArray listArray = json.optJSONArray("list");
            if (listArray != null) {
                int len = listArray.length();
                result.list = new ArrayList<Settlement>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = listArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.list.add(Settlement.deserialize(jo));
                    }
                }
            }

            // 结果数目
            result.totalCount = json.optInt("totalCount");
            // 已结总额
            result.settlementTotalAmount = json.optLong("settlementTotalAmount");
            // 订单总额
            result.tradeTotalAmount = json.optLong("tradeTotalAmount");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 结算列表
        if (this.list != null) {
            JSONArray listArray = new JSONArray();
            for (Settlement value : this.list)
            {
                if (value != null) {
                    listArray.put(value.serialize());
                }
            }
            json.put("list", listArray);
        }

        // 结果数目
        json.put("totalCount", this.totalCount);

        // 已结总额
        json.put("settlementTotalAmount", this.settlementTotalAmount);

        // 订单总额
        json.put("tradeTotalAmount", this.tradeTotalAmount);

        return json;
    }
}
