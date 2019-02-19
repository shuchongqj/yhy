package com.yhy.common.beans.net.model.tm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:ProcessOrderContent
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-19
 * Time:17:28
 * Version 1.0
 * Description:
 */
public class ProcessOrderContent implements Serializable{
    private static final long serialVersionUID = -3578092704069228601L;

    /**
     * 内容标签
     */
    public List<String> demandDescription;
    /**
     * 文本内容
     */
    public String demandDetail;

    /**
     * 城市
     */
    public String destination;

    /**
     * 旅游人数
     */
    public int personNum;

    /**
     * 旅游天数
     */
    public int travelDays;

    public String personNumStr;

    public String travelDaysStr;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ProcessOrderContent deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ProcessOrderContent deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ProcessOrderContent result = new ProcessOrderContent();

            // 内容标签
            JSONArray demandDescriptionArray = json.optJSONArray("demandDescription");
            if (demandDescriptionArray != null) {
                int len = demandDescriptionArray.length();
                result.demandDescription = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if(!demandDescriptionArray.isNull(i)){
                        result.demandDescription.add(demandDescriptionArray.optString(i, null));
                    }else{
                        result.demandDescription.add(i, null);
                    }

                }
            }

            // 文本内容

            if(!json.isNull("demandDetail")){
                result.demandDetail = json.optString("demandDetail", null);
            }
            // 城市

            if(!json.isNull("destination")){
                result.destination = json.optString("destination", null);
            }
            // 旅游人数
            result.personNum = json.optInt("personNum");
            // 旅游天数
            result.travelDays = json.optInt("travelDays");

            if(!json.isNull("personNumStr")) {
                result.personNumStr = json.optString("personNumStr", (String)null);
            }

            if(!json.isNull("travelDaysStr")) {
                result.travelDaysStr = json.optString("travelDaysStr", (String)null);
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

        // 内容标签
        if (this.demandDescription != null) {
            JSONArray demandDescriptionArray = new JSONArray();
            for (String value : this.demandDescription)
            {
                demandDescriptionArray.put(value);
            }
            json.put("demandDescription", demandDescriptionArray);
        }

        // 文本内容
        if(this.demandDetail != null) { json.put("demandDetail", this.demandDetail); }

        // 城市
        if(this.destination != null) { json.put("destination", this.destination); }

        // 旅游人数
        json.put("personNum", this.personNum);

        // 旅游天数
        json.put("travelDays", this.travelDays);

        if(this.personNumStr != null) {
            json.put("personNumStr", this.personNumStr);
        }
        
        if(this.travelDaysStr != null) {
            json.put("travelDaysStr", this.travelDaysStr);
        }
        return json;
    }
}
