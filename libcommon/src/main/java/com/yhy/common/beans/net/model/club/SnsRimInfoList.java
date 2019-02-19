// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.club;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SnsRimInfoList implements Serializable {

    private static final long serialVersionUID = 7602596696310406122L;
    /**
     * 活动列表
     */
    public List<SnsActivePageInfo> snsActivePageInfoList;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SnsRimInfoList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SnsRimInfoList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SnsRimInfoList result = new SnsRimInfoList();

            // 活动列表
            JSONArray snsActivePageInfoListArray = json.optJSONArray("snsActivePageInfoList");
            if (snsActivePageInfoListArray != null) {
                int len = snsActivePageInfoListArray.length();
                result.snsActivePageInfoList = new ArrayList<SnsActivePageInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = snsActivePageInfoListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.snsActivePageInfoList.add(SnsActivePageInfo.deserialize(jo));
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

        // 活动列表 
        if (this.snsActivePageInfoList != null) {
            JSONArray snsActivePageInfoListArray = new JSONArray();
            for (SnsActivePageInfo value : this.snsActivePageInfoList) {
                if (value != null) {
                    snsActivePageInfoListArray.put(value.serialize());
                }
            }
            json.put("snsActivePageInfoList", snsActivePageInfoListArray);
        }

        return json;
    }
}
  