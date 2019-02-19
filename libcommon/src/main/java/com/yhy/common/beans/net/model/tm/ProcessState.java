package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:ProcessState
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-20
 * Time:17:43
 * Version 1.0
 * Description:
 */
public class ProcessState implements Serializable{
    private static final long serialVersionUID = -2819954675037801173L;

    /**
     * 流程订单信息
     */
    public ProcessOrder processOrder;

    /**
     * 咨询服务信息
     */
    public ConsultInfo consultInfo;

    /**
     * 最近流程项信息
     */
    public ProcessOrderItem processOrderItem;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ProcessState deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ProcessState deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ProcessState result = new ProcessState();

            // 流程订单信息
            result.processOrder = ProcessOrder.deserialize(json.optJSONObject("processOrder"));
            // 咨询服务信息
            result.consultInfo = ConsultInfo.deserialize(json.optJSONObject("consultInfo"));
            // 最近流程项信息
            result.processOrderItem = ProcessOrderItem.deserialize(json.optJSONObject("processOrderItem"));
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 流程订单信息
        if (this.processOrder != null) { json.put("processOrder", this.processOrder.serialize()); }

        // 咨询服务信息
        if (this.consultInfo != null) { json.put("consultInfo", this.consultInfo.serialize()); }

        // 最近流程项信息
        if (this.processOrderItem != null) { json.put("processOrderItem", this.processOrderItem.serialize()); }

        return json;
    }
}
