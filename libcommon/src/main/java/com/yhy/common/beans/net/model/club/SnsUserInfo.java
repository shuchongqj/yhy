// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.club;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class SnsUserInfo implements Serializable {

    private static final long serialVersionUID = -6770497785704606359L;
    /**
     * 用户ID
     */
    public long userId;

    /**
     * 头像
     */
    public String avatar;

    /**
     * 性别FEMALE女 MALE男 INVALID_GENDER未知
     */
    public String gender;

    /**
     * 昵称
     */
    public String nickname;

    /**
     * 级别，VERIFIED：加v的,  NORMAL 普通的,DOCTOR 医生,AGENCY 机构，FAMOUS 名人
     */
    public String level;

    /**
     * 描述
     */
    public String introduction;

    /**
     * 当前用户关注类型 UNFOLLOW(不关注), FOLLOW(关注), BOTHFOLLOW(互相关注)
     */
    public String currentUserFollowStatus;

    /**
     * 粉丝数
     */
    public long followerNum;

    /**
     * 用户类型
     */
    public String userType;

    /**
     * 用户角色
     */
    public long options;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SnsUserInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SnsUserInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SnsUserInfo result = new SnsUserInfo();

            // 用户ID
            result.userId = json.optLong("userId");
            // 头像

            if (!json.isNull("avatar")) {
                result.avatar = json.optString("avatar", null);
            }
            // 性别FEMALE女 MALE男 INVALID_GENDER未知

            if (!json.isNull("gender")) {
                result.gender = json.optString("gender", null);
            }
            // 昵称

            if (!json.isNull("nickname")) {
                result.nickname = json.optString("nickname", null);
            }
            // 级别，VERIFIED：加v的,  NORMAL 普通的,DOCTOR 医生,AGENCY 机构，FAMOUS 名人

            if (!json.isNull("level")) {
                result.level = json.optString("level", null);
            }
            // 描述

            if (!json.isNull("introduction")) {
                result.introduction = json.optString("introduction", null);
            }
            // 当前用户关注类型 UNFOLLOW(不关注), FOLLOW(关注), BOTHFOLLOW(互相关注)

            if (!json.isNull("currentUserFollowStatus")) {
                result.currentUserFollowStatus = json.optString("currentUserFollowStatus", null);
            }
            // 粉丝数
            result.followerNum = json.optLong("followerNum");
            // 用户类型

            if (!json.isNull("userType")) {
                result.userType = json.optString("userType", null);
            }

            // 用户角色
            result.options = json.optLong("options");
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

        // 头像
        if (this.avatar != null) {
            json.put("avatar", this.avatar);
        }

        // 性别FEMALE女 MALE男 INVALID_GENDER未知
        if (this.gender != null) {
            json.put("gender", this.gender);
        }

        // 昵称
        if (this.nickname != null) {
            json.put("nickname", this.nickname);
        }

        // 级别，VERIFIED：加v的,  NORMAL 普通的,DOCTOR 医生,AGENCY 机构，FAMOUS 名人
        if (this.level != null) {
            json.put("level", this.level);
        }

        // 描述
        if (this.introduction != null) {
            json.put("introduction", this.introduction);
        }

        // 当前用户关注类型 UNFOLLOW(不关注), FOLLOW(关注), BOTHFOLLOW(互相关注)
        if (this.currentUserFollowStatus != null) {
            json.put("currentUserFollowStatus", this.currentUserFollowStatus);
        }

        // 粉丝数
        json.put("followerNum", this.followerNum);

        // 用户类型
        if (this.userType != null) {
            json.put("userType", this.userType);
        }

        // 用户角色
        json.put("options", this.options);
        return json;
    }
}
  