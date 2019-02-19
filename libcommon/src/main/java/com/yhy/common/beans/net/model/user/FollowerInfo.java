package com.yhy.common.beans.net.model.user;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:FollowerInfo
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-30
 * Time:13:36
 * Version 1.0
 * Description:
 */
public class FollowerInfo implements Serializable{

    /**
     * 用户id
     */
    public long userId;

    /**
     * 头像
     */
    public String avatar;

    /**
     * 签名
     */
    public String signature;

    /**
     * 性别FEMALE女 MALE男 INVALID_GENDER未知
     */
    public String gender;

    /**
     * NONE-未关注/FOLLOW-单向关注/BIFOLLOW-双向关注
     */
    public String type;

    /**
     * 昵称
     */
    public String nickname;

    /**
     * 用户身份
     */
    public long option;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static FollowerInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static FollowerInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            FollowerInfo result = new FollowerInfo();

            // 用户id
            result.userId = json.optLong("userId");
            // 头像

            if(!json.isNull("avatar")){
                result.avatar = json.optString("avatar", null);
            }
            // 签名

            if(!json.isNull("signature")){
                result.signature = json.optString("signature", null);
            }
            // 性别FEMALE女 MALE男 INVALID_GENDER未知

            if(!json.isNull("gender")){
                result.gender = json.optString("gender", null);
            }
            // NONE-未关注/FOLLOW-单向关注/BIFOLLOW-双向关注

            if(!json.isNull("type")){
                result.type = json.optString("type", null);
            }
            // 昵称

            if(!json.isNull("nickname")){
                result.nickname = json.optString("nickname", null);
            }

            // 用户身份
            result.option = json.optLong("option");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 用户id
        json.put("userId", this.userId);

        // 头像
        if(this.avatar != null) { json.put("avatar", this.avatar); }

        // 签名
        if(this.signature != null) { json.put("signature", this.signature); }

        // 性别FEMALE女 MALE男 INVALID_GENDER未知
        if(this.gender != null) { json.put("gender", this.gender); }

        // NONE-未关注/FOLLOW-单向关注/BIFOLLOW-双向关注
        if(this.type != null) { json.put("type", this.type); }

        // 昵称
        if(this.nickname != null) { json.put("nickname", this.nickname); }

        // 用户身份
        json.put("option", this.option);

        return json;
    }
}
