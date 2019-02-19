// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TmRouteItemDesc implements Serializable{

    private static final long serialVersionUID = 6027639575753960099L;
    /**
     * 行程项类型 BREAKFAST早餐/LUNCH午餐/DINNER晚餐/HOTEL酒店/SCENIC景点
     */
    public String type;

    /**
     * 行程项名字列表 如怡心园、豪生酒店
     */
    public List<TmRouteTextItem> textItems;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmRouteItemDesc deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmRouteItemDesc deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmRouteItemDesc result = new TmRouteItemDesc();

            // 行程项类型 BREAKFAST早餐/LUNCH午餐/DINNER晚餐/HOTEL酒店/SCENIC景点

            if(!json.isNull("type")){
                result.type = json.optString("type", null);
            }
            // 行程项名字列表 如怡心园、豪生酒店
            JSONArray textItemsArray = json.optJSONArray("textItems");
            if (textItemsArray != null) {
                int len = textItemsArray.length();
                result.textItems = new ArrayList<TmRouteTextItem>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = textItemsArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.textItems.add(TmRouteTextItem.deserialize(jo));
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

        // 行程项类型 BREAKFAST早餐/LUNCH午餐/DINNER晚餐/HOTEL酒店/SCENIC景点
        if(this.type != null) { json.put("type", this.type); }

        // 行程项名字列表 如怡心园、豪生酒店 
        if (this.textItems != null) {
            JSONArray textItemsArray = new JSONArray();
            for (TmRouteTextItem value : this.textItems)
            {
                if (value != null) {
                    textItemsArray.put(value.serialize());
                }
            }
            json.put("textItems", textItemsArray);
        }

        return json;
    }

}
  