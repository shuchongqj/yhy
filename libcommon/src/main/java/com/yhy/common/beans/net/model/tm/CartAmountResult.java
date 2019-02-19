package com.yhy.common.beans.net.model.tm;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:CartAmountResult
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-10-25
 * Time:10:03
 * Version 1.1.0
 */


public class CartAmountResult implements Serializable {
    private static final long serialVersionUID = -6511282189618112221L;

    /**
     * 购物车数量
     */
    public long amount;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static CartAmountResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static CartAmountResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            CartAmountResult result = new CartAmountResult();

            // 购物车数量
            result.amount = json.optLong("amount");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 购物车数量
        json.put("amount", this.amount);

        return json;
    }

}
