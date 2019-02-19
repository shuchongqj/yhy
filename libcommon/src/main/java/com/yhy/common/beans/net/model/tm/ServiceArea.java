package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:ServiceArea
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-19
 * Time:14:22
 * Version 1.0
 * Description:
 */
public class ServiceArea implements Serializable{

    private static final long serialVersionUID = 2987986474534208350L;

    /**
     * 用户id
     */
    public long outId;

    /**
     * 地区code
     */
    public long areaCode;

    /**
     * 地区名称
     */
    public String areaName;

    /**
     * 标签类型
     */
    public int outType;

    /**
     * 系统code
     */
    public int domain;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ServiceArea deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ServiceArea deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ServiceArea result = new ServiceArea();

            // 用户id
            result.outId = json.optLong("outId");
            // 地区code
            result.areaCode = json.optLong("areaCode");
            // 地区名称

            if(!json.isNull("areaName")){
                result.areaName = json.optString("areaName", null);
            }
            // 标签类型
            result.outType = json.optInt("outType");
            // 系统code
            result.domain = json.optInt("domain");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 用户id
        json.put("outId", this.outId);

        // 地区code
        json.put("areaCode", this.areaCode);

        // 地区名称
        if(this.areaName != null) { json.put("areaName", this.areaName); }

        // 标签类型
        json.put("outType", this.outType);

        // 系统code
        json.put("domain", this.domain);

        return json;
    }
}
