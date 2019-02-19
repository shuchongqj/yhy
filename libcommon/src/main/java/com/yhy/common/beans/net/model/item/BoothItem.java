package com.yhy.common.beans.net.model.item;


import com.yhy.common.beans.net.model.trip.ShortItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:BoothItem
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-29
 * Time:10:57
 * Version 1.0
 * Description:
 */
public class BoothItem implements Serializable{
    private static final long serialVersionUID = -7034233966828532433L;

    /**
     * 主题code
     */
    public String boothCode;

    /**
     * 主题标题
     */
    public String themeTitle;

    /**
     * 主题咨询总数
     */
    public long consultCount;

    /**
     * 商品列表
     */
    public List<ShortItem> shortItemList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static BoothItem deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static BoothItem deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            BoothItem result = new BoothItem();

            // 主题code

            if(!json.isNull("boothCode")){
                result.boothCode = json.optString("boothCode", null);
            }
            // 主题标题

            if(!json.isNull("themeTitle")){
                result.themeTitle = json.optString("themeTitle", null);
            }
            // 主题咨询总数
            result.consultCount = json.optLong("consultCount");
            // 商品列表
            JSONArray shortItemListArray = json.optJSONArray("shortItemList");
            if (shortItemListArray != null) {
                int len = shortItemListArray.length();
                result.shortItemList = new ArrayList<ShortItem>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = shortItemListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.shortItemList.add(ShortItem.deserialize(jo));
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

        // 主题code
        if(this.boothCode != null) { json.put("boothCode", this.boothCode); }

        // 主题标题
        if(this.themeTitle != null) { json.put("themeTitle", this.themeTitle); }

        // 主题咨询总数
        json.put("consultCount", this.consultCount);

        // 商品列表
        if (this.shortItemList != null) {
            JSONArray shortItemListArray = new JSONArray();
            for (ShortItem value : this.shortItemList)
            {
                if (value != null) {
                    shortItemListArray.put(value.serialize());
                }
            }
            json.put("shortItemList", shortItemListArray);
        }

        return json;
    }
}
