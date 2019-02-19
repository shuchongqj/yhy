package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:ItemParamForOrderContext
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-12-21
 * Time:15:08
 * Version 1.1.0
 */


public class ItemParamForOrderContext implements Serializable {
    private static final long serialVersionUID = -1663364665258285310L;

    /**
     * 商品id 必填
     */
    public long itemId;

    /**
     * 商品购买数量 必填
     */
    public long buyAmount;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ItemParamForOrderContext deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ItemParamForOrderContext deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ItemParamForOrderContext result = new ItemParamForOrderContext();

            // 商品id 必填
            result.itemId = json.optLong("itemId");
            // 商品购买数量 必填
            result.buyAmount = json.optLong("buyAmount");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 商品id 必填
        json.put("itemId", this.itemId);

        // 商品购买数量 必填
        json.put("buyAmount", this.buyAmount);

        return json;
    }

}
