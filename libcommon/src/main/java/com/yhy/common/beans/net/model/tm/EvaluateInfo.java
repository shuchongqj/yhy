package com.yhy.common.beans.net.model.tm;

import com.yhy.common.beans.net.model.common.PictureTextItemInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:EvaluateInfo
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-19
 * Time:14:38
 * Version 1.0
 * Description:
 */
public class EvaluateInfo implements Serializable{

    private static final long serialVersionUID = -5480698187507198221L;

    /**
     * 头像
     */
    public String avater;

    /**
     * 昵称
     */
    public String nickName;

    /**
     * 星级
     */
    public String level;

    /**
     * 评价时间
     */
    public String dateTime;

    /**
     * 评价信息
     */
    public List<PictureTextItemInfo> PictureTextItems;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static EvaluateInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static EvaluateInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            EvaluateInfo result = new EvaluateInfo();

            // 头像

            if(!json.isNull("avater")){
                result.avater = json.optString("avater", null);
            }
            // 昵称

            if(!json.isNull("nickName")){
                result.nickName = json.optString("nickName", null);
            }
            // 星级

            if(!json.isNull("level")){
                result.level = json.optString("level", null);
            }
            // 评价时间

            if(!json.isNull("dateTime")){
                result.dateTime = json.optString("dateTime", null);
            }
            // 评价信息
            JSONArray PictureTextItemsArray = json.optJSONArray("PictureTextItems");
            if (PictureTextItemsArray != null) {
                int len = PictureTextItemsArray.length();
                result.PictureTextItems = new ArrayList<PictureTextItemInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = PictureTextItemsArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.PictureTextItems.add(PictureTextItemInfo.deserialize(jo));
                    }
                }
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

        // 头像
        if(this.avater != null) { json.put("avater", this.avater); }

        // 昵称
        if(this.nickName != null) { json.put("nickName", this.nickName); }

        // 星级
        if(this.level != null) { json.put("level", this.level); }

        // 评价时间
        if(this.dateTime != null) { json.put("dateTime", this.dateTime); }

        // 评价信息
        if (this.PictureTextItems != null) {
            JSONArray PictureTextItemsArray = new JSONArray();
            for (PictureTextItemInfo value : this.PictureTextItems)
            {
                if (value != null) {
                    PictureTextItemsArray.put(value.serialize());
                }
            }
            json.put("PictureTextItems", PictureTextItemsArray);
        }

        return json;
    }
}
