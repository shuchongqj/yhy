package com.yhy.common.beans.net.model.user;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:SnsCountInfo
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-28
 * Time:19:00
 * Version 1.0
 * Description:
 */
public class SnsCountInfo implements Serializable {
    private static final long serialVersionUID = 104298264945890330L;
    /**
     * 关注数量
     */
    public long followingCount;

    /**
     * 粉丝数量
     */
    public long fansCount;

    /**
     * UGC数量
     */
    public long ugcCount;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SnsCountInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SnsCountInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SnsCountInfo result = new SnsCountInfo();

            // 关注数量
            result.followingCount = json.optLong("followingCount");
            // 粉丝数量
            result.fansCount = json.optLong("fansCount");
            // UGC数量
            result.ugcCount = json.optLong("ugcCount");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 关注数量
        json.put("followingCount", this.followingCount);

        // 粉丝数量
        json.put("fansCount", this.fansCount);

        // UGC数量
        json.put("ugcCount", this.ugcCount);

        return json;
    }
}
