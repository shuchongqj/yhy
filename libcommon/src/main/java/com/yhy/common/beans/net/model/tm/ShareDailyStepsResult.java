package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:ShareDailyStepsResult
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-6
 * Time:16:07
 * Version 1.0
 * Description:
 */
public class ShareDailyStepsResult implements Serializable {

    private static final long serialVersionUID = -4849828771100371754L;
    /**
     * 积分数
     */
    public long amount;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ShareDailyStepsResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ShareDailyStepsResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ShareDailyStepsResult result = new ShareDailyStepsResult();

            // 积分数
            result.amount = json.optLong("amount");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 积分数
        json.put("amount", this.amount);

        return json;
    }
}
