package com.yhy.common.beans.net.model.user;

import com.yhy.common.beans.net.model.club.PageInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:FollowerPageQueryInfo
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-28
 * Time:18:20
 * Version 1.0
 * Description:
 */
public class FollowerPageQueryInfo implements Serializable{
    /**
     * 当前用户
     */
    public long userId;

    /**
     * 分页信息
     */
    public PageInfo pageInfo;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static FollowerPageQueryInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static FollowerPageQueryInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            FollowerPageQueryInfo result = new FollowerPageQueryInfo();

            // 当前用户
            result.userId = json.optLong("userId");
            // 分页信息
            result.pageInfo = PageInfo.deserialize(json.optJSONObject("pageInfo"));
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 当前用户
        json.put("userId", this.userId);

        // 分页信息
        if (this.pageInfo != null) { json.put("pageInfo", this.pageInfo.serialize()); }

        return json;
    }
}
