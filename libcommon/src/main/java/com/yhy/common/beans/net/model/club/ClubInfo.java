// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.club;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ClubInfo implements Serializable{

    private static final long serialVersionUID = -4295064937696633305L;
    /**
     * id
     */
    public long id;
      
    /**
     * 俱乐部名称
     */
    public String clubName;
      
    /**
     * 俱乐部logo
     */
    public String logoUrl;
      
    /**
     * 俱乐部总人数
     */
    public int memberCount;
      
    /**
     * 俱乐部创建人id
     */
    public long createId;
      
    /**
     * 俱乐部创建时间
     */
    public long createTime;
      
    /**
     * 俱乐部修改时间
     */
    public long modifyTime;
      
    /**
     * 俱乐部背景图片
     */
    public String backImg;

    /**
     * 活动数量
     */
    public int activeNum;
    /**
     * 成员数量
     */
    public int clubNum;
    /**
     * 动态数量
     */
    public int clubSujectNum;
    /**
     * 是否加入俱乐部 AVAILABLE：是 DELETED:否
     */
    public String isJoin;

    /**
     * 俱乐部宣言
     */
    public String clubDes;

    /**
     * 俱乐部排名
     */
    public int rank;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ClubInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ClubInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ClubInfo result = new ClubInfo();

            // id
            result.id = json.optLong("id");
            // 俱乐部名称

            if(!json.isNull("clubName")){
                result.clubName = json.optString("clubName", null);
            }
            // 俱乐部logo

            if(!json.isNull("logoUrl")){
                result.logoUrl = json.optString("logoUrl", null);
            }
            // 俱乐部简介

            if(!json.isNull("clubDes")){
                result.clubDes = json.optString("clubDes", null);
            }
            // 俱乐部总人数
            result.memberCount = json.optInt("memberCount");
            // 俱乐部创建人id
            result.createId = json.optLong("createId");
            // 俱乐部创建时间
            result.createTime = json.optLong("createTime");
            // 俱乐部修改时间
            result.modifyTime = json.optLong("modifyTime");
            // 俱乐部背景图片

            if(!json.isNull("backImg")){
                result.backImg = json.optString("backImg", null);
            }
            // 活动数量
            result.activeNum = json.optInt("activeNum");
            // 成员数量
            result.clubNum = json.optInt("clubNum");
            // 动态数量
            result.clubSujectNum = json.optInt("clubSujectNum");
            // 是否加入俱乐部  AVAILABLE：是   DELETED:否

            if(!json.isNull("isJoin")){
                result.isJoin = json.optString("isJoin", null);
            }
            // 俱乐部排名
            result.rank = json.optInt("rank");
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

        // 俱乐部名称
        if(this.clubName != null) { json.put("clubName", this.clubName); }

        // 俱乐部logo
        if(this.logoUrl != null) { json.put("logoUrl", this.logoUrl); }

        // 俱乐部简介
        if(this.clubDes != null) { json.put("clubDes", this.clubDes); }

        // 俱乐部总人数
        json.put("memberCount", this.memberCount);

        // 俱乐部创建人id
        json.put("createId", this.createId);

        // 俱乐部创建时间
        json.put("createTime", this.createTime);

        // 俱乐部修改时间
        json.put("modifyTime", this.modifyTime);

        // 俱乐部背景图片
        if(this.backImg != null) { json.put("backImg", this.backImg); }

        // 活动数量
        json.put("activeNum", this.activeNum);

        // 成员数量
        json.put("clubNum", this.clubNum);

        // 动态数量
        json.put("clubSujectNum", this.clubSujectNum);

        // 是否加入俱乐部  AVAILABLE：是   DELETED:否
        if(this.isJoin != null) { json.put("isJoin", this.isJoin); }

        // 俱乐部排名
        json.put("rank", this.rank);

        return json;
    }
}
  