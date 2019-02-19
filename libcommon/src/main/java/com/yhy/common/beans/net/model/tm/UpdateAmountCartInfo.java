package com.yhy.common.beans.net.model.tm;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:UpdateAmountCartInfo
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-10-25
 * Time:10:05
 * Version 1.1.0
 */


public class UpdateAmountCartInfo implements Serializable {
    private static final long serialVersionUID = 6497175189031672984L;

    /**
     * 购买数量
     */
    public long amount;

    /**
     * 购物车id
     */
    public long id;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static UpdateAmountCartInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static UpdateAmountCartInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            UpdateAmountCartInfo result = new UpdateAmountCartInfo();

            // 购买数量
            result.amount = json.optLong("amount");
            // 购物车id
            result.id = json.optLong("id");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 购买数量
        json.put("amount", this.amount);

        // 购物车id
        json.put("id", this.id);

        return json;
    }

}
