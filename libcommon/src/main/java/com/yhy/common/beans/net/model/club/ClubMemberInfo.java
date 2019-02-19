// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.club;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ClubMemberInfo implements Serializable {

    private static final long serialVersionUID = -1049388936615091835L;
    /**
     * 俱乐部成员id
     */
    public long id;

    /**
     * 俱乐部id
     */
    public long clubId;

    /**
     * 创建者id
     */
    public long createId;

    /**
     * 创建者名字
     */
    public String userName;

    /**
     * 创建者头像
     */
    public String userPhoto;

    /**
     * 个性签名
     */
    public String signature;

    /**
     * 性别
     */
    public String gender;

    /**
     * 活跃度
     */
    public int liveness;

    /**
     * 参加时间
     */
    public long createTime;

    /**
     * 修改时间
     */
    public long modifyTime;

    /**
     * 是否加入
     */
    public String isResult;

    /**
     * 成员数量
     */
    public int memberCount;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ClubMemberInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ClubMemberInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ClubMemberInfo result = new ClubMemberInfo();

            // 俱乐部成员id
            result.id = json.optLong("id");
            // 俱乐部id
            result.clubId = json.optLong("clubId");
            // 创建者id
            result.createId = json.optLong("createId");
            // 创建者名字

            if(!json.isNull("userName")){
                result.userName = json.optString("userName", null);
            }
            // 创建者头像

            if(!json.isNull("userPhoto")){
                result.userPhoto = json.optString("userPhoto", null);
            }
            // 个性签名

            if(!json.isNull("signature")){
                result.signature = json.optString("signature", null);
            }
            // 性别

            if(!json.isNull("gender")){
                result.gender = json.optString("gender", null);
            }
            // 活跃度
            result.liveness = json.optInt("liveness");
            // 参加时间
            result.createTime = json.optLong("createTime");
            // 修改时间
            result.modifyTime = json.optLong("modifyTime");
            // 是否加入

            if(!json.isNull("isResult")){
                result.isResult = json.optString("isResult", null);
            }
            result.memberCount = json.optInt("memberCount");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 俱乐部成员id
        json.put("id", this.id);

        // 俱乐部id
        json.put("clubId", this.clubId);

        // 创建者id
        json.put("createId", this.createId);

        // 创建者名字
        if(this.userName != null) { json.put("userName", this.userName); }

        // 创建者头像
        if(this.userPhoto != null) { json.put("userPhoto", this.userPhoto); }

        // 个性签名
        if(this.signature != null) { json.put("signature", this.signature); }

        // 性别
        if(this.gender != null) { json.put("gender", this.gender); }

        // 活跃度
        json.put("liveness", this.liveness);

        // 参加时间
        json.put("createTime", this.createTime);

        // 修改时间
        json.put("modifyTime", this.modifyTime);

        // 是否加入
        if(this.isResult != null) { json.put("isResult", this.isResult); }
        json.put("memberCount", this.memberCount);

        return json;
    }
}
  