package com.yhy.common.beans.net.model.trip;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:KeywordSearchDTO
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2015-11-24
 * Time:17:59
 * Version 1.0
 * Description:
 */
public class KeywordSearchDTO implements Serializable{

    private static final long serialVersionUID = -5323229375032833263L;

    /**
     * 关键字
     */
    public String keyword;

    /**
     * 搜索类型 HOTEL:酒店 SCENIC:景区
     */
    public String type;

    /**
     * 页号
     */
    public int pageNo;

    /**
     * 页面大小
     */
    public int pageSize;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static KeywordSearchDTO deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static KeywordSearchDTO deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            KeywordSearchDTO result = new KeywordSearchDTO();

            // 关键字

            if(!json.isNull("keyword")){
                result.keyword = json.optString("keyword", null);
            }
            // 搜索类型 HOTEL:酒店 SCENIC:景区

            if(!json.isNull("type")){
                result.type = json.optString("type", null);
            }
            // 页号
            result.pageNo = json.optInt("pageNo");
            // 页面大小
            result.pageSize = json.optInt("pageSize");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 关键字
        if(this.keyword != null) { json.put("keyword", this.keyword); }

        // 搜索类型 HOTEL:酒店 SCENIC:景区
        if(this.type != null) { json.put("type", this.type); }

        // 页号
        json.put("pageNo", this.pageNo);

        // 页面大小
        json.put("pageSize", this.pageSize);

        return json;
    }

}
