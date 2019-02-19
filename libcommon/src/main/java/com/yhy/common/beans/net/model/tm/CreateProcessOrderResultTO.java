package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:CreateProcessOrderResultTO
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-20
 * Time:10:24
 * Version 1.0
 * Description:
 */
public class CreateProcessOrderResultTO implements Serializable{
    private static final long serialVersionUID = 8630976687415626214L;

    /**
     * 是否成功
     */
    public boolean success;

    /**
     * 服务流程订单信息
     */
    public ProcessOrder processOrder;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static CreateProcessOrderResultTO deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static CreateProcessOrderResultTO deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            CreateProcessOrderResultTO result = new CreateProcessOrderResultTO();

            // 是否成功
            result.success = json.optBoolean("success");
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

        // 服务流程订单信息
        if (this.processOrder != null) { json.put("processOrder", this.processOrder.serialize()); }

        return json;
    }
}
