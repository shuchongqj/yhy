package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:AcceptProcessOrderResult
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-19
 * Time:17:35
 * Version 1.0
 * Description:
 */
public class AcceptProcessOrderResult implements Serializable{
    private static final long serialVersionUID = 2208741471742299357L;
    /**
     * 是否成功
     */
    public boolean success;
    public String message;
    /**
     * 服务流程订单信息
     */
    public ProcessOrder processOrder;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static AcceptProcessOrderResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static AcceptProcessOrderResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            AcceptProcessOrderResult result = new AcceptProcessOrderResult();

            // 是否成功
            result.success = json.optBoolean("success");
            result.message = json.optString("message");
            // 服务流程订单信息
            result.processOrder = ProcessOrder.deserialize(json.optJSONObject("processOrder"));
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 是否成功
        json.put("success", this.success);
        json.put("message",this.message);
        // 服务流程订单信息
        if (this.processOrder != null) { json.put("processOrder", this.processOrder.serialize()); }

        return json;
    }
}
