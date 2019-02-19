// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.common.person;


import com.yhy.common.beans.net.model.user.HkMacaoCertificate;
import com.yhy.common.beans.net.model.user.IDCertificate;
import com.yhy.common.beans.net.model.user.MilitaryCertificate;
import com.yhy.common.beans.net.model.user.PassportCertificate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class UserContact implements Serializable {

    private static final long serialVersionUID = -429470543896613459L;
    //是否选中
    public boolean isChoosed = false;

    //是否可选
    public boolean isCanChoose = true;

    /**
     * id
     */
    public long id;

    /**
     * 联系人姓名
     */
    public String contactName;

    /**
     * 联系人手机号码
     */
    public String contactPhone;

    /**
     * 联系人邮箱
     */
    public String contactEmail;

    /**
     * 证件类型 1-身份证 2-护照 3-军人证 4-港澳通行证
     */
    public String certificatesType;

    /**
     * 证件号
     */
    public String certificatesNum;

    /**
     * 用户id
     */
    public long userId;

    /**
     * 是否删除
     */
    public String isDel;

    /**
     * 修改时间
     */
    public long gmtModified;

    /**
     * 创建时间
     */
    public long gmtCreated;

    /**
     * 英文姓
     */
    public String lastName;

    /**
     * 英文名字
     */
    public String firstName;

    /**
     * 身份证
     */
    public IDCertificate idCert;

    /**
     * 军人证
     */
    public MilitaryCertificate militaryCert;

    /**
     * 护照
     */
    public PassportCertificate passportCert;

    /**
     * 港澳通行证
     */
    public HkMacaoCertificate hkMacaoCert;


    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static UserContact deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static UserContact deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            UserContact result = new UserContact();

            // id
            result.id = json.optLong("id");
            // 联系人姓名

            if (!json.isNull("contactName")) {
                result.contactName = json.optString("contactName", null);
            }
            // 联系人手机号码

            if (!json.isNull("contactPhone")) {
                result.contactPhone = json.optString("contactPhone", null);
            }
            // 联系人邮箱

            if (!json.isNull("contactEmail")) {
                result.contactEmail = json.optString("contactEmail", null);
            }
            // 证件类型 1-身份证 2-护照 3-军人证 4-港澳通行证

            if (!json.isNull("certificatesType")) {
                result.certificatesType = json.optString("certificatesType", null);
            }
            // 证件号

            if (!json.isNull("certificatesNum")) {
                result.certificatesNum = json.optString("certificatesNum", null);
            }
            // 用户id
            result.userId = json.optLong("userId");
            // 是否删除

            if (!json.isNull("isDel")) {
                result.isDel = json.optString("isDel", null);
            }
            // 修改时间
            result.gmtModified = json.optLong("gmtModified");
            // 创建时间
            result.gmtCreated = json.optLong("gmtCreated");
            // 英文姓

            if(!json.isNull("lastName")){
                result.lastName = json.optString("lastName", null);
            }
            // 英文名字

            if(!json.isNull("firstName")){
                result.firstName = json.optString("firstName", null);
            }
            // 身份证
            result.idCert = IDCertificate.deserialize(json.optJSONObject("idCert"));
            // 军人证
            result.militaryCert = MilitaryCertificate.deserialize(json.optJSONObject("militaryCert"));
            // 护照
            result.passportCert = PassportCertificate.deserialize(json.optJSONObject("passportCert"));
            // 港澳通行证
            result.hkMacaoCert = HkMacaoCertificate.deserialize(json.optJSONObject("hkMacaoCert"));

            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // id
        json.put("id", this.id);

        // 联系人姓名
        if (this.contactName != null) {
            json.put("contactName", this.contactName);
        }

        // 联系人手机号码
        if (this.contactPhone != null) {
            json.put("contactPhone", this.contactPhone);
        }

        // 联系人邮箱
        if (this.contactEmail != null) {
            json.put("contactEmail", this.contactEmail);
        }

        // 证件类型 1-身份证 2-护照 3-军人证 4-港澳通行证
        if (this.certificatesType != null) {
            json.put("certificatesType", this.certificatesType);
        }

        // 证件号
        if (this.certificatesNum != null) {
            json.put("certificatesNum", this.certificatesNum);
        }

        // 用户id
        json.put("userId", this.userId);

        // 是否删除
        if (this.isDel != null) {
            json.put("isDel", this.isDel);
        }

        // 修改时间
        json.put("gmtModified", this.gmtModified);

        // 创建时间
        json.put("gmtCreated", this.gmtCreated);

        // 英文姓
        if(this.lastName != null) { json.put("lastName", this.lastName); }

        // 英文名字
        if(this.firstName != null) { json.put("firstName", this.firstName); }

        // 身份证
        if (this.idCert != null) { json.put("idCert", this.idCert.serialize()); }

        // 军人证
        if (this.militaryCert != null) { json.put("militaryCert", this.militaryCert.serialize()); }

        // 护照
        if (this.passportCert != null) { json.put("passportCert", this.passportCert.serialize()); }

        // 港澳通行证
        if (this.hkMacaoCert != null) { json.put("hkMacaoCert", this.hkMacaoCert.serialize()); }

        return json;
    }
}
  