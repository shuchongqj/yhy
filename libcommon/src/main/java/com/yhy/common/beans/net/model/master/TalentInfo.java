// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.master;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TalentInfo implements Serializable{

    private static final long serialVersionUID = -7572750245585622864L;

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
     * 签名
     */
    public String signature;
    /**
     * 封面
     */
    public String frontCover;


    /**
     * 达人手机号
     */
    public String telNum;

    /**
     *  true-大V认证  false-非大V认证
     */
    public boolean type;

    /**
     * 达人首页轮播图
     */
    public List<String> pictures;
    /**
     * 达人技能类型
     */
    public int certificateType;

    /**
     * 达人认证
     */
    public List<MasterCertificates> certificates;
    /**
     * 关注数量
     */
    public long followCount;

    /**
     * 粉丝数量
     */
    public long fansCount;

    /**
     * ugc数量
     */
    public long ugcCount;

    /**
     * NONE-未关注/FOLLOW-单向关注/BIFOLLOW-双向关注
     */
    public String attentionType;

    /**
     * 是否有主页
     */
    public boolean isHasMainPage;

    public long orgId;

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

            // 用户ID
            result.userId = json.optLong("userId");
            // 用户头像

            if(!json.isNull("avatar")){
                result.avatar = json.optString("avatar", null);
            }
            // 昵称

            if(!json.isNull("nickName")){
                result.nickName = json.optString("nickName", null);
            }
            // 性别 1-未确认 2-男  3-女

            if(!json.isNull("gender")){
                result.gender = json.optString("gender", null);
            }
            // 服务描述

            if(!json.isNull("serveDesc")){
                result.serveDesc = json.optString("serveDesc", null);
            }
            // 服务次数
            result.serveCount = json.optLong("serveCount");
            // 所在城市

            if(!json.isNull("city")){
                result.city = json.optString("city", null);
            }
            if(!json.isNull("signature")){
                result.signature = json.optString("signature", null);
            }
            if(!json.isNull("frontCover")){
                result.frontCover = json.optString("frontCover", null);
            }


            // 城市code

            if(!json.isNull("cityCode")){
                result.cityCode = json.optString("cityCode", null);
            }
            // 达人手机号

            if(!json.isNull("telNum")){
                result.telNum = json.optString("telNum", null);
            }


            // 是否有主页
            result.isHasMainPage = json.optBoolean("isHasMainPage");
            //  true-大V认证  false-非大V认证
            result.type = json.optBoolean("type");
            // 达人首页轮播图
            JSONArray picturesArray = json.optJSONArray("pictures");
            if (picturesArray != null) {
                int len = picturesArray.length();
                result.pictures = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if(!picturesArray.isNull(i)){
                        result.pictures.add(picturesArray.optString(i, null));
                    }else{
                        result.pictures.add(i, null);
                    }

                }
            }

            // 达人技能类型
            result.certificateType = json.optInt("certificateType");
            // 达人认证
            JSONArray certificatesArray = json.optJSONArray("certificates");
            if (certificatesArray != null) {
                int len = certificatesArray.length();
                result.certificates = new ArrayList<MasterCertificates>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = certificatesArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.certificates.add(MasterCertificates.deserialize(jo));
                    }
                }
            }

            // 关注数量
            result.followCount = json.optLong("followCount");
            // 粉丝数量
            result.fansCount = json.optLong("fansCount");
            // ugc数量
            result.ugcCount = json.optLong("ugcCount");
            // NONE-未关注/FOLLOW-单向关注/BIFOLLOW-双向关注

            if(!json.isNull("attentionType")){
                result.attentionType = json.optString("attentionType", null);
            }

            // 是否有主页
            result.isHasMainPage = json.optBoolean("isHasMainPage");

            result.orgId = json.optLong("orgId");

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
        if(this.avatar != null) { json.put("avatar", this.avatar); }

        // 昵称
        if(this.nickName != null) { json.put("nickName", this.nickName); }

        // 性别 1-未确认 2-男  3-女
        if(this.gender != null) { json.put("gender", this.gender); }

        // 服务描述
        if(this.serveDesc != null) { json.put("serveDesc", this.serveDesc); }

        // 服务次数
        json.put("serveCount", this.serveCount);

        // 所在城市
        if(this.city != null) { json.put("city", this.city); }

        // 城市code
        if(this.cityCode != null) { json.put("cityCode", this.cityCode); }

        // 达人手机号
        if(this.telNum != null) { json.put("telNum", this.telNum); }
        // 是否有主页
        json.put("isHasMainPage", this.isHasMainPage);
        //  true-大V认证  false-非大V认证
        json.put("type", this.type);

        // 达人首页轮播图
        if (this.pictures != null) {
            JSONArray picturesArray = new JSONArray();
            for (String value : this.pictures)
            {
                picturesArray.put(value);
            }
            json.put("pictures", picturesArray);
        }

        // 达人技能类型
        json.put("certificateType", this.certificateType);

        // 达人认证
        if (this.certificates != null) {
            JSONArray certificatesArray = new JSONArray();
            for (MasterCertificates value : this.certificates)
            {
                if (value != null) {
                    certificatesArray.put(value.serialize());
                }
            }
            json.put("certificates", certificatesArray);
        }

        // 关注数量
        json.put("followCount", this.followCount);

        // 粉丝数量
        json.put("fansCount", this.fansCount);

        // ugc数量
        json.put("ugcCount", this.ugcCount);

        // NONE-未关注/FOLLOW-单向关注/BIFOLLOW-双向关注
        if(this.attentionType != null) { json.put("attentionType", this.attentionType); }
        // 签名
        if(this.signature != null) { json.put("signature", this.signature); }

        // 封面
        if(this.frontCover != null) { json.put("frontCover", this.frontCover); }

        // 是否有主页
        json.put("isHasMainPage", this.isHasMainPage);

        return json;
    }

}
  