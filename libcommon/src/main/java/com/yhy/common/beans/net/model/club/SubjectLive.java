// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.club;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SubjectLive implements Serializable {

    private static final long serialVersionUID = -1430061128990219687L;
    /**
     * 标签Id
     */
    public long objId;

    /**
     * 标签名字
     */
    public String tagName;

    /**
     * 用户id
     */
    public long createId;

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

    public int duration;

    /**
     * 视频URL
     */
    public String videoUrl;

    /**
     * 视频缩略图URL
     */
    public String videoPicUrl;


    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SubjectLive deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SubjectLive deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SubjectLive result = new SubjectLive();

            // 标签Id
            result.objId = json.optLong("objId");
            // 标签名字

            if (!json.isNull("tagName")) {
                result.tagName = json.optString("tagName", null);
            }
            // 用户id
            result.createId = json.optLong("createId");
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

            // 视频URL

            if (!json.isNull("videoUrl")) {
                result.videoUrl = json.optString("videoUrl", null);
            }
            // 视频缩略图URL

            if (!json.isNull("videoPicUrl")) {
                result.videoPicUrl = json.optString("videoPicUrl", null);
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

        // 标签Id
        json.put("objId", this.objId);

        // 标签名字
        if (this.tagName != null) {
            json.put("tagName", this.tagName);
        }

        // 用户id
        json.put("createId", this.createId);

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

        // 视频URL
        if (this.videoUrl != null) {
            json.put("videoUrl", this.videoUrl);
        }

        // 视频缩略图URL
        if (this.videoPicUrl != null) {
            json.put("videoPicUrl", this.videoPicUrl);
        }

        return json;
    }
}
  