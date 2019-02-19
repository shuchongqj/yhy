// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.master;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TalentUserInfo implements Serializable {

    private static final long serialVersionUID = 1854385970130899428L;

    /**
     * 用户ID
     */
    public long userId;

    /**
     * 用户头像
     */
    public String avatar;

    /**
     * 昵称
     */
    public String nickName;

    /**
     * 性别 1-未确认 2-男  3-女
     */
    public String gender;

    /**
     * 服务描述
     */
    public String serveDesc;

    /**
     * 服务次数
     */
    public long serveCount;

    /**
     * 所在城市
     */
    public String city;

    /**
     * 城市code
     */
    public String cityCode;

    /**
     * true-大V认证  false-非大V认证
     */
    public boolean type;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TalentUserInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TalentUserInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TalentUserInfo result = new TalentUserInfo();

            // 用户ID
            result.userId = json.optLong("userId");
            // 用户头像

            if (!json.isNull("avatar")) {
                result.avatar = json.optString("avatar", null);
            }
            // 昵称

            if (!json.isNull("nickName")) {
                result.nickName = json.optString("nickName", null);
            }
            // 性别 1-未确认 2-男  3-女

            if (!json.isNull("gender")) {
                result.gender = json.optString("gender", null);
            }
            // 服务描述

            if (!json.isNull("serveDesc")) {
                result.serveDesc = json.optString("serveDesc", null);
            }
            // 服务次数
            result.serveCount = json.optLong("serveCount");
            // 所在城市

            if (!json.isNull("city")) {
                result.city = json.optString("city", null);
            }
            // 城市code

            if (!json.isNull("cityCode")) {
                result.cityCode = json.optString("cityCode", null);
            }
            //  true-大V认证  false-非大V认证
            result.type = json.optBoolean("type");
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

        // 用户头像
        if (this.avatar != null) {
            json.put("avatar", this.avatar);
        }

        // 昵称
        if (this.nickName != null) {
            json.put("nickName", this.nickName);
        }

        // 性别 1-未确认 2-男  3-女
        if (this.gender != null) {
            json.put("gender", this.gender);
        }

        // 服务描述
        if (this.serveDesc != null) {
            json.put("serveDesc", this.serveDesc);
        }

        // 服务次数
        json.put("serveCount", this.serveCount);

        // 所在城市
        if (this.city != null) {
            json.put("city", this.city);
        }

        // 城市code
        if (this.cityCode != null) {
            json.put("cityCode", this.cityCode);
        }

        //  true-大V认证  false-非大V认证
        json.put("type", this.type);

        return json;
    }

}
  