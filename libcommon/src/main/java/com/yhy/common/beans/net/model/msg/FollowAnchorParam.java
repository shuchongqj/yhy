package com.yhy.common.beans.net.model.msg;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:FollowAnchorParam
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-8-17
 * Time:16:02
 * Version 1.0
 * Description:
 */
public class FollowAnchorParam implements Serializable {

    private static final long serialVersionUID = 4532928498966604081L;

    /**
     * 直播ID
     */
    public long liveId;

    /**
     * 关注的主播id
     */
    public long anchorId;


    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static FollowAnchorParam deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static FollowAnchorParam deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            FollowAnchorParam result = new FollowAnchorParam();

            // 直播ID
            result.liveId = json.optLong("liveId");
            // 关注的主播id
            result.anchorId = json.optLong("anchorId");
            // 用户昵称，发送关注消息用

            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 直播ID
        json.put("liveId", this.liveId);

        // 关注的主播id
        json.put("anchorId", this.anchorId);


        return json;
    }
}
