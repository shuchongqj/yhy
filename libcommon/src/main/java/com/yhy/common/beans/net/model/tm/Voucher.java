package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:Voucher
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-12-27
 * Time:15:22
 * Version 1.1.0
 */


public class Voucher implements Serializable {
    private static final long serialVersionUID = 9190627663530322615L;

    /**
     * 卖家id
     */
    public long sellerId;

    /**
     * 优惠券id
     */
    public long voucherId;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Voucher deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Voucher deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            Voucher result = new Voucher();

            // 卖家id
            result.sellerId = json.optLong("sellerId");
            // 优惠券id
            result.voucherId = json.optLong("voucherId");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 卖家id
        json.put("sellerId", this.sellerId);

        // 优惠券id
        json.put("voucherId", this.voucherId);

        return json;
    }
}
