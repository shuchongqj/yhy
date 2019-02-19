package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:TalentInfo
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-19
 * Time:14:37
 * Version 1.0
 * Description:
 */
public class TalentInfo implements Serializable{
    private static final long serialVersionUID = 2515743701607500636L;

    /**
     * 头像
     */
    public String avater;

    /**
     * 昵称
     */
    public String nickName;

    /**
     * 身份认证状态
     */
    public int certificateSate;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TalentInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TalentInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TalentInfo result = new TalentInfo();

            // 头像

            if(!json.isNull("avater")){
                result.avater = json.optString("avater", null);
            }
            // 昵称

            if(!json.isNull("nickName")){
                result.nickName = json.optString("nickName", null);
            }
            // 身份认证状态
            result.certificateSate = json.optInt("certificateSate");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 头像
        if(this.avater != null) { json.put("avater", this.avater); }

        // 昵称
        if(this.nickName != null) { json.put("nickName", this.nickName); }

        // 身份认证状态
        json.put("certificateSate", this.certificateSate);

        return json;
    }
}
