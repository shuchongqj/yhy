package com.yhy.common.beans.net.model.msg;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:LiveMsgServerAddrParam
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-26
 * Time:10:47
 * Version 1.1.0
 */

public class LiveMsgServerAddrParam implements Serializable {
    private static final long serialVersionUID = -5354023401042580311L;
    /**
     * 群组ID/直播ID
     */
    public long liveId;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static LiveMsgServerAddrParam deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static LiveMsgServerAddrParam deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            LiveMsgServerAddrParam result = new LiveMsgServerAddrParam();

            // 群组ID/直播ID
            result.liveId = json.optLong("liveId");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 群组ID/直播ID
        json.put("liveId", this.liveId);
        return json;
    }
}
