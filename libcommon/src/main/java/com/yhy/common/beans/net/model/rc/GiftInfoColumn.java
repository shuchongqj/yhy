// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;

import com.yhy.common.beans.net.model.trip.ItemVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GiftInfoColumn implements Serializable {

    private static final long serialVersionUID = -6205425232312411866L;
    /**
     * 标题
     */
    public String title;

    /**
     * 描述
     */
    public String description;

    /**
     * 伴手礼列表
     */
    public List<ItemVO> giftInfoList;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static GiftInfoColumn deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static GiftInfoColumn deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            GiftInfoColumn result = new GiftInfoColumn();

            // 标题

            if (!json.isNull("title")) {
                result.title = json.optString("title", null);
            }
            // 描述

            if (!json.isNull("description")) {
                result.description = json.optString("description", null);
            }
            // 伴手礼列表
            JSONArray giftInfoListArray = json.optJSONArray("giftInfoList");
            if (giftInfoListArray != null) {
                int len = giftInfoListArray.length();
                result.giftInfoList = new ArrayList<ItemVO>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = giftInfoListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.giftInfoList.add(ItemVO.deserialize(jo));
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

        // 标题
        if (this.title != null) {
            json.put("title", this.title);
        }

        // 描述
        if (this.description != null) {
            json.put("description", this.description);
        }

        // 伴手礼列表 
        if (this.giftInfoList != null) {
            JSONArray giftInfoListArray = new JSONArray();
            for (ItemVO value : this.giftInfoList) {
                if (value != null) {
                    giftInfoListArray.put(value.serialize());
                }
            }
            json.put("giftInfoList", giftInfoListArray);
        }

        return json;
    }
}
  