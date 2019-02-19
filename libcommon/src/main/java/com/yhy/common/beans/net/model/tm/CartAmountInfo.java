package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:CartAmountInfo
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-10-25
 * Time:10:01
 * Version 1.1.0
 */


public class CartAmountInfo implements Serializable {
    private static final long serialVersionUID = 5868968540652410881L;

    /**
     * 用户id
     */
    public long buyUserId;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static CartAmountInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static CartAmountInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            CartAmountInfo result = new CartAmountInfo();

            // 用户id
            result.buyUserId = json.optLong("buyUserId");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 用户id
        json.put("buyUserId", this.buyUserId);

        return json;
    }

}
