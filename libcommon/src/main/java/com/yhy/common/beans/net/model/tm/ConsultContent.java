package com.yhy.common.beans.net.model.tm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:ConsultContent
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-27
 * Time:13:56
 * Version 1.0
 * Description:
 */
public class ConsultContent implements Serializable{
    private static final long serialVersionUID = -6679952256915981356L;

    /**
     * 咨询服务ID 达人咨询时使用
     */
    public long itemId;

    /**
     * 咨询内容标签
     */
    public List<String> consultTags;
    /**
     * 目的地 CityCode
     */
    public String place;

    /**
     * 描述内容
     */
    public String consultDes;

    /**
     * 旅行天数 1-20 1-20天 21 20天以上
     */
    public int travelDays;

    /**
     * 旅行人数 1-20 1-20天 21 20人以上
     */
    public int personNum;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ConsultContent deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ConsultContent deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ConsultContent result = new ConsultContent();

            // 咨询服务ID 达人咨询时使用
            result.itemId = json.optLong("itemId");
            // 咨询内容标签
            JSONArray consultTagsArray = json.optJSONArray("consultTags");
            if (consultTagsArray != null) {
                int len = consultTagsArray.length();
                result.consultTags = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if(!consultTagsArray.isNull(i)){
                        result.consultTags.add(consultTagsArray.optString(i, null));
                    }else{
                        result.consultTags.add(i, null);
                    }

                }
            }

            // 目的地 CityCode

            if(!json.isNull("place")){
                result.place = json.optString("place", null);
            }
            // 描述内容

            if(!json.isNull("consultDes")){
                result.consultDes = json.optString("consultDes", null);
            }
            // 旅行天数 1-20 1-20天 21 20天以上
            result.travelDays = json.optInt("travelDays");
            // 旅行人数 1-20 1-20天 21 20人以上
            result.personNum = json.optInt("personNum");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 咨询服务ID 达人咨询时使用
        json.put("itemId", this.itemId);

        // 咨询内容标签
        if (this.consultTags != null) {
            JSONArray consultTagsArray = new JSONArray();
            for (String value : this.consultTags)
            {
                consultTagsArray.put(value);
            }
            json.put("consultTags", consultTagsArray);
        }

        // 目的地 CityCode
        if(this.place != null) { json.put("place", this.place); }

        // 描述内容
        if(this.consultDes != null) { json.put("consultDes", this.consultDes); }

        // 旅行天数 1-20 1-20天 21 20天以上
        json.put("travelDays", this.travelDays);

        // 旅行人数 1-20 1-20天 21 20人以上
        json.put("personNum", this.personNum);

        return json;
    }
}
