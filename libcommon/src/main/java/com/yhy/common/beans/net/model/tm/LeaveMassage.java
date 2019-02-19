package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:LeaveMassage
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-12-21
 * Time:19:21
 * Version 1.1.0
 */


public class LeaveMassage implements Serializable {
    private static final long serialVersionUID = 358277011720956479L;

    /**
     * 卖家id
     */
    public long sellerId;

    /**
     * 买家留言
     */
    public String leaveMassage;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static LeaveMassage deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static LeaveMassage deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            LeaveMassage result = new LeaveMassage();

            // 卖家id
            result.sellerId = json.optLong("sellerId");
            // 买家留言

            if(!json.isNull("leaveMassage")){
                result.leaveMassage = json.optString("leaveMassage", null);
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

        // 卖家id
        json.put("sellerId", this.sellerId);

        // 买家留言
        if(this.leaveMassage != null) { json.put("leaveMassage", this.leaveMassage); }

        return json;
    }
}
