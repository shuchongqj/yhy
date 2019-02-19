package com.yhy.common.beans.net.model.trip;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:CityInfo
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:和平必胜、正义必胜、人民必胜
 * Author:炎黄子孙
 * Date:15/11/7
 * Time:下午5:16
 * Version 1.0
 */
public class CityInfo implements Serializable{
    private static final long serialVersionUID = 7075780382974885892L;

    public long id;
    public int cityCode;
    public String name;
    public int level;
    public String description;
    public String url;
    public String bgUrl;
    public boolean hot;
    public String pinyin;
    public String shortPinyin;


    public static CityInfo deserialize(String json) throws JSONException {
        return json != null && !json.isEmpty()?deserialize(new JSONObject(json)):null;
    }

    public static CityInfo deserialize(JSONObject json) throws JSONException {
        if(json != null && json != JSONObject.NULL && json.length() > 0) {
            CityInfo result = new CityInfo();
            result.id = json.optLong("id");
            result.cityCode = json.optInt("cityCode");
            if(!json.isNull("name")) {
                result.name = json.optString("name", (String)null);
            }

            result.level = json.optInt("level");
            if(!json.isNull("description")) {
                result.description = json.optString("description", (String)null);
            }

            if(!json.isNull("url")) {
                result.url = json.optString("url", (String)null);
            }

            if(!json.isNull("bgUrl")) {
                result.bgUrl = json.optString("bgUrl", (String)null);
            }

            result.hot = json.optBoolean("hot");
            if(!json.isNull("pinyin")) {
                result.pinyin = json.optString("pinyin", (String)null);
            }

            if(!json.isNull("shortPinyin")) {
                result.shortPinyin = json.optString("shortPinyin", (String)null);
            }

            return result;
        } else {
            return null;
        }
    }

    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", this.id);
        json.put("cityCode", this.cityCode);
        if(this.name != null) {
            json.put("name", this.name);
        }

        json.put("level", this.level);
        if(this.description != null) {
            json.put("description", this.description);
        }

        if(this.url != null) {
            json.put("url", this.url);
        }

        if(this.bgUrl != null) {
            json.put("bgUrl", this.bgUrl);
        }

        json.put("hot", this.hot);
        if(this.pinyin != null) {
            json.put("pinyin", this.pinyin);
        }

        if(this.shortPinyin != null) {
            json.put("shortPinyin", this.shortPinyin);
        }

        return json;
    }
}
