// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class BaseInfo implements Serializable{

    private static final long serialVersionUID = 3262624084180344579L;
    /**
     * ID
     */
    public long id;

    /**
     * 标题
     */
    public String title;

    /**
     * 业务代码
     */
    public String bizCode;

    /**
     * 简介
     */
    public String description;

    /**
     * 图片url
     */
    public String img_url;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static BaseInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static BaseInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            BaseInfo result = new BaseInfo();

            // ID
            result.id = json.optLong("id");
            // 标题

            if(!json.isNull("title")){
                result.title = json.optString("title", null);
            }
            // 业务代码

            if(!json.isNull("bizCode")){
                result.bizCode = json.optString("bizCode", null);
            }
            // 简介

            if(!json.isNull("description")){
                result.description = json.optString("description", null);
            }
            // 图片url

            if(!json.isNull("img_url")){
                result.img_url = json.optString("img_url", null);
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

        // ID
        json.put("id", this.id);

        // 标题
        if(this.title != null) { json.put("title", this.title); }

        // 业务代码
        if(this.bizCode != null) { json.put("bizCode", this.bizCode); }

        // 简介
        if(this.description != null) { json.put("description", this.description); }

        // 图片url
        if(this.img_url != null) { json.put("img_url", this.img_url); }

        return json;
    }
}
  