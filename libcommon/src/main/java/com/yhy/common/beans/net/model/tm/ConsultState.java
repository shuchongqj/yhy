package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:ConsultState
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-19
 * Time:17:51
 * Version 1.0
 * Description:
 */
public class ConsultState implements Serializable{
    private static final long serialVersionUID = -4847692255858216778L;

    /**
     * 符合条件的最近流程订单信息，若为空则表示没有
     */
    public ProcessOrder processOrder;

    /**
     * 咨询相关信息
     */
    public ConsultInfo consultInfo;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ConsultState deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ConsultState deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ConsultState result = new ConsultState();

            // 符合条件的最近流程订单信息，若为空则表示没有
            result.processOrder = ProcessOrder.deserialize(json.optJSONObject("processOrder"));
            // 咨询相关信息
            result.consultInfo = ConsultInfo.deserialize(json.optJSONObject("consultInfo"));
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 符合条件的最近流程订单信息，若为空则表示没有
        if (this.processOrder != null) { json.put("processOrder", this.processOrder.serialize()); }

        // 咨询相关信息
        if (this.consultInfo != null) { json.put("consultInfo", this.consultInfo.serialize()); }

        return json;
    }

}
