// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.club;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SubjectDynamic implements Serializable {

    private static final long serialVersionUID = 2965476630666206667L;
    /**
     * 发表动态时存俱乐部Id-发表直播时是标签Id
     */
    public long objId;

    /**
     * 文字
     */
    public String textContent;

    /**
     * 图片集合
     */
    public List<String> picList;
    /**
     * 地理位置信息
     */
    public POIInfo poiInfo;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SubjectDynamic deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SubjectDynamic deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SubjectDynamic result = new SubjectDynamic();

            // 发表动态时存俱乐部Id-发表直播时是标签Id
            result.objId = json.optLong("objId");
            // 文字

            if (!json.isNull("textContent")) {
                result.textContent = json.optString("textContent", null);
            }
            // 图片集合
            JSONArray picListArray = json.optJSONArray("picList");
            if (picListArray != null) {
                int len = picListArray.length();
                result.picList = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if (!picListArray.isNull(i)) {
                        result.picList.add(picListArray.optString(i, null));
                    } else {
                        result.picList.add(i, null);
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

        // 发表动态时存俱乐部Id-发表直播时是标签Id
        json.put("objId", this.objId);

        // 文字
        if (this.textContent != null) {
            json.put("textContent", this.textContent);
        }

        // 图片集合 
        if (this.picList != null) {
            JSONArray picListArray = new JSONArray();
            for (String value : this.picList) {
                picListArray.put(value);
            }
            json.put("picList", picListArray);
        }

        // 地理位置信息
        if (this.poiInfo != null) {
            json.put("poiInfo", this.poiInfo.serialize());
        }

        return json;
    }
}
  