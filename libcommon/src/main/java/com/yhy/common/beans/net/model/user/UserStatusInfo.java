package com.yhy.common.beans.net.model.user;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:UserStatusInfo
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-27
 * Time:15:50
 * Version 1.0
 * Description:
 */
public class UserStatusInfo implements Serializable{
    private static final long serialVersionUID = -5779954416469648042L;
    /**
     * 用户ID
     */
    public long userId;

    /**
     * 用户状态
     */
    public int status;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static UserStatusInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static UserStatusInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            UserStatusInfo result = new UserStatusInfo();

            // 用户ID
            result.userId = json.optLong("userId");
            // 用户状态
            result.status = json.optInt("status");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 用户ID
        json.put("userId", this.userId);

        // 用户状态
        json.put("status", this.status);

        return json;
    }
}
