// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TmUserContactInfo implements Serializable {

    private static final long serialVersionUID = -969494138979101180L;
    /**
     * 关联用户Id
     */
    public long userId;

    /**
     * 姓名
     */
    public String name;

    /**
     * 联系电话
     */
    public String phoneNum;

    /**
     * 联系邮箱
     */
    public String email;

    /**
     * 身份证号
     */
    public String idNumber;

    /**
     * 证件类型 1-身份证 2-护照 3-军人证 4-港澳通行证
     */
    public String CertificateType;

    /**
     * 英文姓
     */
    public String lastName;

    /**
     * 英文名
     */
    public String firstName;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmUserContactInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmUserContactInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmUserContactInfo result = new TmUserContactInfo();

            // 关联用户Id
            result.userId = json.optLong("userId");
            // 姓名

            if (!json.isNull("name")) {
                result.name = json.optString("name", null);
            }
            // 联系电话

            if (!json.isNull("phoneNum")) {
                result.phoneNum = json.optString("phoneNum", null);
            }
            // 联系邮箱

            if (!json.isNull("email")) {
                result.email = json.optString("email", null);
            }
            // 身份证号

            if (!json.isNull("idNumber")) {
                result.idNumber = json.optString("idNumber", null);
            }

            // 证件类型

            if (!json.isNull("CertificateType")) {
                result.CertificateType = json.optString("CertificateType", null);
            }

            // 英文姓

            if (!json.isNull("lastName")) {
                result.lastName = json.optString("lastName", null);
            }
            // 英文名

            if (!json.isNull("firstName")) {
                result.firstName = json.optString("firstName", null);
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

        // 关联用户Id
        json.put("userId", this.userId);

        // 姓名
        if (this.name != null) {
            json.put("name", this.name);
        }

        // 联系电话
        if (this.phoneNum != null) {
            json.put("phoneNum", this.phoneNum);
        }

        // 联系邮箱
        if (this.email != null) {
            json.put("email", this.email);
        }

        // 身份证号
        if (this.idNumber != null) {
            json.put("idNumber", this.idNumber);
        }

        // 证件类型
        if (this.CertificateType != null) {
            json.put("CertificateType", this.CertificateType);
        }

        // 英文姓
        if (this.lastName != null) {
            json.put("lastName", this.lastName);
        }

        // 英文名
        if (this.firstName != null) {
            json.put("firstName", this.firstName);
        }
        return json;
    }
}
  