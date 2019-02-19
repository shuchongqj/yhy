package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:GoodsDetail
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-19
 * Time:14:29
 * Version 1.0
 * Description:
 */
public class SDItemDetail implements Serializable{

    private static final long serialVersionUID = 1185306617410773003L;
    /**
     * 商品管理
     */
    public ItemManagement itemManagement;

    /**
     * 商品id
     */
    public long id;
    /**
     * 线路id
     */
    public long routeId;


    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SDItemDetail deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SDItemDetail deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SDItemDetail result = new SDItemDetail();

            // 商品管理
            result.itemManagement = ItemManagement.deserialize(json.optJSONObject("itemManagement"));
            // 商品id
            result.id = json.optLong("id");
            // 线路id
            result.routeId = json.optLong("routeId");

            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 商品管理
        if (this.itemManagement != null) { json.put("itemManagement", this.itemManagement.serialize()); }

        // 商品id
        json.put("id", this.id);

        // 线路id
        json.put("routeId", this.routeId);


        return json;
    }
}
