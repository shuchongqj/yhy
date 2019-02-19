package com.yhy.common.beans.net.model.item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:BoothItemsResult
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-29
 * Time:10:56
 * Version 1.0
 * Description:
 */
public class BoothItemsResult implements Serializable{
    private static final long serialVersionUID = -8869882131527545907L;

    /**
     * 主题对应商品信息
     */
    public List<BoothItem> boothItemList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static BoothItemsResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static BoothItemsResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            BoothItemsResult result = new BoothItemsResult();

            // 主题对应商品信息
            JSONArray boothItemListArray = json.optJSONArray("boothItemList");
            if (boothItemListArray != null) {
                int len = boothItemListArray.length();
                result.boothItemList = new ArrayList<BoothItem>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = boothItemListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.boothItemList.add(BoothItem.deserialize(jo));
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

        // 主题对应商品信息
        if (this.boothItemList != null) {
            JSONArray boothItemListArray = new JSONArray();
            for (BoothItem value : this.boothItemList)
            {
                if (value != null) {
                    boothItemListArray.put(value.serialize());
                }
            }
            json.put("boothItemList", boothItemListArray);
        }

        return json;
    }
}
