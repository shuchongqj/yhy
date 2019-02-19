package com.yhy.common.beans.net.model.trip;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:SearchResultVO
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2015-11-24
 * Time:18:05
 * Version 1.0
 * Description:
 */
public class SearchResultVO implements Serializable{

    private static final long serialVersionUID = 1875563953734649451L;
    /**
     * 资源类型 HOTEL：酒店/SCENIC：景点/GIFT：伴手礼
     */
    public String type;

    /**
     * 资源id
     */
    public long id;

    /**
     * 资源名
     */
    public String name;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SearchResultVO deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SearchResultVO deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SearchResultVO result = new SearchResultVO();

            // 资源类型 HOTEL：酒店/SCENIC：景点/GIFT：伴手礼

            if(!json.isNull("type")){
                result.type = json.optString("type", null);
            }
            // 资源id
            result.id = json.optLong("id");
            // 资源名

            if(!json.isNull("name")){
                result.name = json.optString("name", null);
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

        // 资源类型 HOTEL：酒店/SCENIC：景点/GIFT：伴手礼
        if(this.type != null) { json.put("type", this.type); }

        // 资源id
        json.put("id", this.id);

        // 资源名
        if(this.name != null) { json.put("name", this.name); }

        return json;
    }

}
