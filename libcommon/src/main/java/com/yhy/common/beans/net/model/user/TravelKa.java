// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.user;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TravelKa implements Serializable {

    private static final long serialVersionUID = 4304221084863648731L;
    /**
     * id
     */
    public long id;

    /**
     * userId
     */
    public long userId;

    /**
     * 用户对象实体
     */
    public UserInfo userInfo;

    /**
     * 旅游咖能力
     */
    public List<Ability> abilities;
    /**
     * 旅游咖俱乐部信息
     */
    public TravelKaClub travelKaClub;

    /**
     * 服务内容
     */
    public String serviceContent;

    /**
     * 职业id
     */
    public long occupationId;

    /**
     * 背景图片
     */
    public String backgroundImg;

    /**
     * 是否删除 0否 1是
     */
    public String isDel;

    /**
     * 是否是大咖 0否 1是
     */
    public String isTravelKa;

    /**
     * 改修时间
     */
    public long gmtModified;

    /**
     * 创建时间
     */
    public long gmtCreated;

    /**
     * 真实身份验证 0-否 1-是
     */
    public String identityValidated;

    /**
     * 电话号码验证  0-否 1-是
     */
    public String mobileValidated;

    /**
     * 职业验证 0-否 1-是
     */
    public String occupationValidated;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TravelKa deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TravelKa deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TravelKa result = new TravelKa();

            // id
            result.id = json.optLong("id");
            // userId
            result.userId = json.optLong("userId");
            // 用户对象实体
            result.userInfo = UserInfo.deserialize(json.optJSONObject("userInfo"));
            // 旅游咖能力
            JSONArray abilitiesArray = json.optJSONArray("abilities");
            if (abilitiesArray != null) {
                int len = abilitiesArray.length();
                result.abilities = new ArrayList<Ability>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = abilitiesArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.abilities.add(Ability.deserialize(jo));
                    }
                }
            }

            // 旅游咖俱乐部信息
            result.travelKaClub = TravelKaClub.deserialize(json.optJSONObject("travelKaClub"));
            // 服务内容

            if (!json.isNull("serviceContent")) {
                result.serviceContent = json.optString("serviceContent", null);
            }
            // 职业id
            result.occupationId = json.optLong("occupationId");
            // 背景图片

            if (!json.isNull("backgroundImg")) {
                result.backgroundImg = json.optString("backgroundImg", null);
            }
            // 是否删除 0否 1是

            if (!json.isNull("isDel")) {
                result.isDel = json.optString("isDel", null);
            }
            // 是否是大咖 0否 1是

            if (!json.isNull("isTravelKa")) {
                result.isTravelKa = json.optString("isTravelKa", null);
            }
            // 改修时间
            result.gmtModified = json.optLong("gmtModified");
            // 创建时间
            result.gmtCreated = json.optLong("gmtCreated");
            // 真实身份验证 0-否 1-是

            if (!json.isNull("identityValidated")) {
                result.identityValidated = json.optString("identityValidated", null);
            }
            // 电话号码验证  0-否 1-是

            if (!json.isNull("mobileValidated")) {
                result.mobileValidated = json.optString("mobileValidated", null);
            }
            // 职业验证 0-否 1-是

            if (!json.isNull("occupationValidated")) {
                result.occupationValidated = json.optString("occupationValidated", null);
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

        // id
        json.put("id", this.id);

        // userId
        json.put("userId", this.userId);

        // 用户对象实体
        if (this.userInfo != null) {
            json.put("userInfo", this.userInfo.serialize());
        }

        // 旅游咖能力 
        if (this.abilities != null) {
            JSONArray abilitiesArray = new JSONArray();
            for (Ability value : this.abilities) {
                if (value != null) {
                    abilitiesArray.put(value.serialize());
                }
            }
            json.put("abilities", abilitiesArray);
        }

        // 旅游咖俱乐部信息
        if (this.travelKaClub != null) {
            json.put("travelKaClub", this.travelKaClub.serialize());
        }

        // 服务内容
        if (this.serviceContent != null) {
            json.put("serviceContent", this.serviceContent);
        }

        // 职业id
        json.put("occupationId", this.occupationId);

        // 背景图片
        if (this.backgroundImg != null) {
            json.put("backgroundImg", this.backgroundImg);
        }

        // 是否删除 0否 1是
        if (this.isDel != null) {
            json.put("isDel", this.isDel);
        }

        // 是否是大咖 0否 1是
        if (this.isTravelKa != null) {
            json.put("isTravelKa", this.isTravelKa);
        }

        // 改修时间
        json.put("gmtModified", this.gmtModified);

        // 创建时间
        json.put("gmtCreated", this.gmtCreated);

        // 真实身份验证 0-否 1-是
        if (this.identityValidated != null) {
            json.put("identityValidated", this.identityValidated);
        }

        // 电话号码验证  0-否 1-是
        if (this.mobileValidated != null) {
            json.put("mobileValidated", this.mobileValidated);
        }

        // 职业验证 0-否 1-是
        if (this.occupationValidated != null) {
            json.put("occupationValidated", this.occupationValidated);
        }

        return json;
    }


}
  