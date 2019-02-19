package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:SellerAndConsultStateResult
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-19
 * Time:17:40
 * Version 1.0
 * Description:
 */
public class SellerAndConsultStateResult implements Serializable{
    private static final long serialVersionUID = -5513843846921315929L;

    /**
     * 是否可以下单
     */
    public boolean canCreateOrder;

    /**
     * 不可下单原因 取值范围 ORDER_NOT_FINISH, TALENT_NOT_ONLINE
     */
    public String reason;

    /**
     * 当前流程单信息
     */
    public ProcessOrder processOrder;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SellerAndConsultStateResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SellerAndConsultStateResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SellerAndConsultStateResult result = new SellerAndConsultStateResult();

            // 是否可以下单
            result.canCreateOrder = json.optBoolean("canCreateOrder");
            // 不可下单原因 取值范围 ORDER_NOT_FINISH, TALENT_NOT_ONLINE

            if(!json.isNull("reason")){
                result.reason = json.optString("reason", null);
            }
            // 当前流程单信息
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

        // 是否可以下单
        json.put("canCreateOrder", this.canCreateOrder);

        // 不可下单原因 取值范围 ORDER_NOT_FINISH, TALENT_NOT_ONLINE
        if(this.reason != null) { json.put("reason", this.reason); }

        // 当前流程单信息
        if (this.processOrder != null) { json.put("processOrder", this.processOrder.serialize()); }

        return json;
    }

}
