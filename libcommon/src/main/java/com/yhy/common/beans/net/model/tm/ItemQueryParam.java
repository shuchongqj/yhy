package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:ItemQueryParam
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-28
 * Time:13:49
 * Version 1.0
 * Description:
 */
public class ItemQueryParam implements Serializable{
    private static final long serialVersionUID = -5515786864379307542L;

        /**
     * 商品id
     */
    public long id;

    /**
     * 类目id
     */
    public long categoryId;

    /**
     * 页码
     */
    public int pageNo;

    /**
     * 每页显示条数
     */
    public int pageSize;

    /**
     * 商品发布状态2:已发布3:待发布
     */
    public int serviceState;

    /**
     * 商品状态
     */
    public int state;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ItemQueryParam deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ItemQueryParam deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ItemQueryParam result = new ItemQueryParam();

            // 商品id
            result.id = json.optLong("id");
            // 类目id
            result.categoryId = json.optLong("categoryId");
            // 页码
            result.pageNo = json.optInt("pageNo");
            // 每页显示条数
            result.pageSize = json.optInt("pageSize");
            // 商品发布状态2:已发布3:待发布
            result.serviceState = json.optInt("serviceState");
            // 商品状态
            result.state = json.optInt("state");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 商品id
        json.put("id", this.id);

        // 类目id
        json.put("categoryId", this.categoryId);

        // 页码
        json.put("pageNo", this.pageNo);

        // 每页显示条数
        json.put("pageSize", this.pageSize);

        // 商品发布状态2:已发布3:待发布
        json.put("serviceState", this.serviceState);

        // 商品状态
        json.put("state", this.state);

        return json;
    }
}
