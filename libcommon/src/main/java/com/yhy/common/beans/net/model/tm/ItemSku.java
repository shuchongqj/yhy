package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:ItemSku
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-12-21
 * Time:19:19
 * Version 1.1.0
 */


public class ItemSku implements Serializable {
    private static final long serialVersionUID = -8807899621988662869L;

    /**
     * skuID
     */
    public long skuId;

    /**
     * 购买数量
     */
    public int num;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ItemSku deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ItemSku deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ItemSku result = new ItemSku();

            // skuID
            result.skuId = json.optLong("skuId");
            // 购买数量
            result.num = json.optInt("num");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // skuID
        json.put("skuId", this.skuId);

        // 购买数量
        json.put("num", this.num);

        return json;
    }
}
