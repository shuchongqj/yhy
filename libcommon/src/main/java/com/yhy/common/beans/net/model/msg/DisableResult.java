package com.yhy.common.beans.net.model.msg;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:DisableResult
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-8-17
 * Time:16:00
 * Version 1.0
 * Description:
 */
public class DisableResult implements Serializable {
    private static final long serialVersionUID = 5804765074657794286L;

    /**
     * 禁言状态.值：DISABLE_SEND_MSG:禁言/ENABLE_SEND_MSG:未禁言/HAS_DISABLE_SEND_MSG:禁言中
     */
    public String disableStatus;

    /**
     * 返回结果描述
     */
    public String resultDesc;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static DisableResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static DisableResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            DisableResult result = new DisableResult();

            // 禁言状态.值：DISABLE_SEND_MSG:禁言/ENABLE_SEND_MSG:未禁言/HAS_DISABLE_SEND_MSG:禁言中

            if (!json.isNull("disableStatus")) {
                result.disableStatus = json.optString("disableStatus", null);
            }
            // 返回结果描述

            if (!json.isNull("resultDesc")) {
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

        // 禁言状态.值：DISABLE_SEND_MSG:禁言/ENABLE_SEND_MSG:未禁言/HAS_DISABLE_SEND_MSG:禁言中
        if (this.disableStatus != null) {
            json.put("disableStatus", this.disableStatus);
        }

        // 返回结果描述
        if (this.resultDesc != null) {
            json.put("resultDesc", this.resultDesc);
        }

        return json;
    }
}
