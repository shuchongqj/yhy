// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.user;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TravelKaItem implements Serializable {

    private static final long serialVersionUID = -7287370886153963474L;
    /**
     * userId
     */
    public long userId;

    /**
     * 性别
     */
    public int gender;

    /**
     * 用户头像
     */
    public String avatar;

    /**
     * 昵称
     */
    public String nickname;

    /**
     * 省code
     */
    public int provinceCode;

    /**
     * 市code
     */
    public int cityCode;

    /**
     * 省名称
     */
    public String province;

    /**
     * 市名称
     */
    public String city;

    /**
     * 年龄
     */
    public int age;

    /**
     * 签名
     */
    public String signature;

    /**
     * 生日
     */
    public long birthday;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TravelKaItem deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TravelKaItem deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TravelKaItem result = new TravelKaItem();

            // userId
            result.userId = json.optLong("userId");
            //生日
            result.birthday = json.optLong("birthday");
            // 性别
            result.gender = json.optInt("gender");
            // 用户头像

            if (!json.isNull("avatar")) {
                result.avatar = json.optString("avatar", null);
            }
            // 昵称

            if (!json.isNull("nickname")) {
                result.nickname = json.optString("nickname", null);
            }
            // 省code
            result.provinceCode = json.optInt("provinceCode");
            // 市code
            result.cityCode = json.optInt("cityCode");
            // 省名称

            if (!json.isNull("province")) {
                result.province = json.optString("province", null);
            }
            // 市名称

            if (!json.isNull("city")) {
                result.city = json.optString("city", null);
            }
            // 年龄
            result.age = json.optInt("age");
            // 签名

            if (!json.isNull("signature")) {
                result.signature = json.optString("signature", null);
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

        // userId
        json.put("userId", this.userId);

        // 性别
        json.put("gender", this.gender);

        // 用户头像
        if (this.avatar != null) {
            json.put("avatar", this.avatar);
        }

        // 昵称
        if (this.nickname != null) {
            json.put("nickname", this.nickname);
        }

        // 省code
        json.put("provinceCode", this.provinceCode);

        // 市code
        json.put("cityCode", this.cityCode);

        // 省名称
        if (this.province != null) {
            json.put("province", this.province);
        }

        // 市名称
        if (this.city != null) {
            json.put("city", this.city);
        }

        // 年龄
        json.put("age", this.age);
        // 生日
        json.put("birthday", this.birthday);

        // 签名
        if (this.signature != null) {
            json.put("signature", this.signature);
        }

        return json;
    }
}
  