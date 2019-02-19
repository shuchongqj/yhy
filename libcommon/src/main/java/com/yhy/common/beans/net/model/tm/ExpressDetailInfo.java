package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:ExpressDetailInfo
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-12-22
 * Time:15:33
 * Version 1.1.0
 */


public class ExpressDetailInfo implements Serializable {
    private static final long serialVersionUID = 1931426780219888449L;

    /**
     * 运送信息
     */
    public String context;

    /**
     * 运送时间
     */
    public String time;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ExpressDetailInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ExpressDetailInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ExpressDetailInfo result = new ExpressDetailInfo();

            // 运送信息

            if(!json.isNull("context")){
                result.context = json.optString("context", null);
            }
            // 运送时间

            if(!json.isNull("time")){
                result.time = json.optString("time", null);
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

        // 运送信息
        if(this.context != null) { json.put("context", this.context); }

        // 运送时间
        if(this.time != null) { json.put("time", this.time); }

        return json;
    }
}
