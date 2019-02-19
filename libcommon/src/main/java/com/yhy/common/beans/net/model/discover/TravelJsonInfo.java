// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.discover;

import com.yhy.common.beans.net.model.club.POIInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TravelJsonInfo implements Serializable {

    private static final long serialVersionUID = -4666334611478824864L;
    /**
     * 游记图文信息
     */
    public String travelDes;

    /**
     * 游记图文信息
     */
    public List<String> travelImg;
    /**
     * 地理位置信息
     */
    public POIInfo poiInfo;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TravelJsonInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TravelJsonInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TravelJsonInfo result = new TravelJsonInfo();

            // 游记图文信息

            if (!json.isNull("travelDes")) {
                result.travelDes = json.optString("travelDes", null);
            }
            // 游记图文信息
            JSONArray travelImgArray = json.optJSONArray("travelImg");
            if (travelImgArray != null) {
                int len = travelImgArray.length();
                result.travelImg = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if (!travelImgArray.isNull(i)) {
                        result.travelImg.add(travelImgArray.optString(i, null));
                    } else {
                        result.travelImg.add(i, null);
                    }

                }
            }

            // 地理位置信息
            result.poiInfo = POIInfo.deserialize(json.optJSONObject("poiInfo"));
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 游记图文信息
        if (this.travelDes != null) {
            json.put("travelDes", this.travelDes);
        }

        // 游记图文信息 
        if (this.travelImg != null) {
            JSONArray travelImgArray = new JSONArray();
            for (String value : this.travelImg) {
                travelImgArray.put(value);
            }
            json.put("travelImg", travelImgArray);
        }

        // 地理位置信息
        if (this.poiInfo != null) {
            json.put("poiInfo", this.poiInfo.serialize());
        }

        return json;
    }
}
  