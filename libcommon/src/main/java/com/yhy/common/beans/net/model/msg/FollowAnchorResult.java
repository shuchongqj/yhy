package com.yhy.common.beans.net.model.msg;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:FollowAnchorResult
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-8-17
 * Time:16:03
 * Version 1.0
 * Description:
 */
public class FollowAnchorResult implements Serializable{
    private static final long serialVersionUID = 8135297985051100847L;

    /**
     * 关注状态.值：FOLLOW_SUCCESS:关注成功/FOLLOW_FAILED:关注失败/HAS_FOLLOW:已关注
     */
    public String followStatus;

    /**
     * 返回结果描述
     */
    public String resultDesc;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static FollowAnchorResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static FollowAnchorResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            FollowAnchorResult result = new FollowAnchorResult();

            // 关注状态.值：FOLLOW_SUCCESS:关注成功/FOLLOW_FAILED:关注失败/HAS_FOLLOW:已关注

            if(!json.isNull("followStatus")){
                result.followStatus = json.optString("followStatus", null);
            }
            // 返回结果描述

            if(!json.isNull("resultDesc")){
                result.resultDesc = json.optString("resultDesc", null);
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

        // 关注状态.值：FOLLOW_SUCCESS:关注成功/FOLLOW_FAILED:关注失败/HAS_FOLLOW:已关注
        if(this.followStatus != null) { json.put("followStatus", this.followStatus); }

        // 返回结果描述
        if(this.resultDesc != null) { json.put("resultDesc", this.resultDesc); }

        return json;
    }

}
