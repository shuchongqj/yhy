package com.yhy.common.beans.net.model.tm;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:CartIdsInfo
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-10-25
 * Time:9:36
 * Version 1.1.0
 */


public class CartIdsInfo implements Serializable {
    private static final long serialVersionUID = 708164179433888395L;

    /**
     * 购物车id
     */
    public long id;

    /**
     * 购物车勾选状态 0:勾选  1:取消勾选
     */
    public long checkState;


    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static CartIdsInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static CartIdsInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            CartIdsInfo result = new CartIdsInfo();

            // 购物车id
            result.id = json.optLong("id");
            // 购物车勾选状态 0:勾选  1:取消勾选
            result.checkState = json.optLong("checkState");

            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 购物车id
        json.put("id", this.id);
        // 购物车勾选状态 0:勾选  1:取消勾选
        json.put("checkState", this.checkState);


        return json;
    }
}
