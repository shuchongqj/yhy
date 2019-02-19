package com.yhy.common.beans.net.model.msg;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:OtherMsgResult
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-30
 * Time:11:12
 * Version 1.1.0
 */

public class OtherMsgResult implements Serializable {
    private static final long serialVersionUID = 3993793678169579199L;
    /**
     * 发送通知结果
     */
    public boolean isSuccess;

    /**
     * 返回结果描述
     */
    public String resultDesc;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static OtherMsgResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static OtherMsgResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            OtherMsgResult result = new OtherMsgResult();

            // 发送通知结果
            result.isSuccess = json.optBoolean("isSuccess");
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

        // 发送通知结果
        json.put("isSuccess", this.isSuccess);

        // 返回结果描述
        if(this.resultDesc != null) { json.put("resultDesc", this.resultDesc); }

        return json;
    }
}
