package com.yhy.common.beans.net.model.user;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:FollowRetInfo
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-21
 * Time:13:54
 * Version 1.0
 * Description:
 */
public class FollowRetInfo implements Serializable{
    private static final long serialVersionUID = -5100381006114661865L;

    /**
     * 关注结果  FOLLOW_SUCCESS:关注成功  FOLLOWED:已关注  FOLLOW_FAILE:关注失败
     */
    public String type;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static FollowRetInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static FollowRetInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            FollowRetInfo result = new FollowRetInfo();

            // 关注结果  FOLLOW_SUCCESS:关注成功  FOLLOWED:已关注  FOLLOW_FAILE:关注失败

            if(!json.isNull("type")){
                result.type = json.optString("type", null);
            }
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 关注结果  FOLLOW_SUCCESS:关注成功  FOLLOWED:已关注  FOLLOW_FAILE:关注失败
        if(this.type != null) { json.put("type", this.type); }

        return json;
    }
}
