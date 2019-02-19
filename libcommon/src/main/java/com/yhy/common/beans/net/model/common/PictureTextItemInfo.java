package com.yhy.common.beans.net.model.common;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:PictureTextItemInfo
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-28
 * Time:11:33
 * Version 1.0
 * Description:
 */
public class PictureTextItemInfo implements Serializable{
    private static final long serialVersionUID = 6090241681352872608L;

    /**
     * 图文类型  COMENT:内容,IMAGE:图片
     */
    public String type;

    /**
     * 图文的值
     */
    public String value;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static PictureTextItemInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static PictureTextItemInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            PictureTextItemInfo result = new PictureTextItemInfo();

            // 图文类型  COMENT:内容,IMAGE:图片

            if(!json.isNull("type")){
                result.type = json.optString("type", null);
            }
            // 图文的值

            if(!json.isNull("value")){
                result.value = json.optString("value", null);
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

        // 图文类型  COMENT:内容,IMAGE:图片
        if(this.type != null) { json.put("type", this.type); }

        // 图文的值
        if(this.value != null) { json.put("value", this.value); }

        return json;
    }
}
