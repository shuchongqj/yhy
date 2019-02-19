package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:CartInfo
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-10-25
 * Time:9:58
 * Version 1.1.0
 */


public class CartInfo implements Serializable {
    private static final long serialVersionUID = 3440596875874890076L;

    /**
     * 总金额
     */
    public long actualTotalFee;

    /**
     * 积分可抵金额
     */
    public long pointTotalFee;

    /**
     * 勾选商品总数量
     */
    public int count;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static CartInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static CartInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            CartInfo result = new CartInfo();

            // 总金额
            result.actualTotalFee = json.optLong("actualTotalFee");
            // 积分可抵金额
            result.pointTotalFee = json.optLong("pointTotalFee");
            // 勾选商品总数量
            result.count = json.optInt("count");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 总金额
        json.put("actualTotalFee", this.actualTotalFee);

        // 积分可抵金额
        json.put("pointTotalFee", this.pointTotalFee);

        // 勾选商品总数量
        json.put("count", this.count);

        return json;
    }

}
