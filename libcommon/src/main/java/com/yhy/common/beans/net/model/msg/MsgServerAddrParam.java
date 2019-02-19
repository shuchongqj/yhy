package com.yhy.common.beans.net.model.msg;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:MsgServerAddrParam
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-8-17
 * Time:16:06
 * Version 1.0
 * Description:
 */
public class MsgServerAddrParam implements Serializable{
    private static final long serialVersionUID = 6919744415556899879L;

    /**
     * 观看直播用户ID
     */
    public long userId;

    /**
     * 直播ID
     */
    public long liveId;

    /**
     * 用户类型.值：0:未知,1:已注册用户,2:游客
     */
    public int userType;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static MsgServerAddrParam deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static MsgServerAddrParam deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            MsgServerAddrParam result = new MsgServerAddrParam();

            // 观看直播用户ID
            result.userId = json.optLong("userId");
            // 直播ID
            result.liveId = json.optLong("liveId");
            // 用户类型.值：0:未知,1:已注册用户,2:游客
            result.userType = json.optInt("userType");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 观看直播用户ID
        json.put("userId", this.userId);

        // 直播ID
        json.put("liveId", this.liveId);

        // 用户类型.值：0:未知,1:已注册用户,2:游客
        json.put("userType", this.userType);

        return json;
    }
}
