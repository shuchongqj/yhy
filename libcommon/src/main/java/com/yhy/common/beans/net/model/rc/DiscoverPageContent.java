// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class DiscoverPageContent implements Serializable {

    private static final long serialVersionUID = 9109986595988357147L;
    /**
     * 伴手礼
     */
    public GiftInfoColumn giftInfoColumn;

    /**
     * 大咖游记
     */
    public TravelNoteColumn travelNoteColumn;

    /**
     * 直播信息
     */
    public SubjectInfoColumn subjectInfoColumn;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static DiscoverPageContent deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static DiscoverPageContent deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            DiscoverPageContent result = new DiscoverPageContent();

            // 伴手礼
            result.giftInfoColumn = GiftInfoColumn.deserialize(json.optJSONObject("giftInfoColumn"));
            // 大咖游记
            result.travelNoteColumn = TravelNoteColumn.deserialize(json.optJSONObject("travelNoteColumn"));
            // 直播信息
            result.subjectInfoColumn = SubjectInfoColumn.deserialize(json.optJSONObject("subjectInfoColumn"));
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 伴手礼
        if (this.giftInfoColumn != null) {
            json.put("giftInfoColumn", this.giftInfoColumn.serialize());
        }

        // 大咖游记
        if (this.travelNoteColumn != null) {
            json.put("travelNoteColumn", this.travelNoteColumn.serialize());
        }

        // 直播信息
        if (this.subjectInfoColumn != null) {
            json.put("subjectInfoColumn", this.subjectInfoColumn.serialize());
        }

        return json;
    }
}
  