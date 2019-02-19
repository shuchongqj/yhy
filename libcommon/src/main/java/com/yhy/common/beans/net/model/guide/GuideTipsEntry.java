package com.yhy.common.beans.net.model.guide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:GuideTipsEntry
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-22
 * Time:11:34
 * Version 1.1.0
 */

public class GuideTipsEntry implements Serializable {
    private static final long serialVersionUID = 179026843715598919L;

    /**
     * 标题
     */
    public String title;

    /**
     * 内容
     */
    public String content;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static GuideTipsEntry deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static GuideTipsEntry deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            GuideTipsEntry result = new GuideTipsEntry();

            // 标题

            if(!json.isNull("title")){
                result.title = json.optString("title", null);
            }
            // 内容

            if(!json.isNull("content")){
                result.content = json.optString("content", null);
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

        // 标题
        if(this.title != null) { json.put("title", this.title); }

        // 内容
        if(this.content != null) { json.put("content", this.content); }

        return json;
    }
}
