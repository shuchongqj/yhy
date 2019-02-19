// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ScenicFilter implements Serializable {

    private static final long serialVersionUID = -7950398023544512243L;
    /**
     * 主题列表
     */
    public List<ItemSubjectInfo> subjectInfoList;
    /**
     * 区域列表
     */
    public List<CityInfo> cityInfoList;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ScenicFilter deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ScenicFilter deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ScenicFilter result = new ScenicFilter();

            // 主题列表
            JSONArray subjectInfoListArray = json.optJSONArray("subjectInfoList");
            if (subjectInfoListArray != null) {
                int len = subjectInfoListArray.length();
                result.subjectInfoList = new ArrayList<ItemSubjectInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = subjectInfoListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.subjectInfoList.add(ItemSubjectInfo.deserialize(jo));
                    }
                }
            }

            // 区域列表
            JSONArray cityInfoListArray = json.optJSONArray("cityInfoList");
            if (cityInfoListArray != null) {
                int len = cityInfoListArray.length();
                result.cityInfoList = new ArrayList<CityInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = cityInfoListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.cityInfoList.add(CityInfo.deserialize(jo));
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

        // 主题列表 
        if (this.subjectInfoList != null) {
            JSONArray subjectInfoListArray = new JSONArray();
            for (ItemSubjectInfo value : this.subjectInfoList) {
                if (value != null) {
                    subjectInfoListArray.put(value.serialize());
                }
            }
            json.put("subjectInfoList", subjectInfoListArray);
        }

        // 区域列表 
        if (this.cityInfoList != null) {
            JSONArray cityInfoListArray = new JSONArray();
            for (CityInfo value : this.cityInfoList) {
                if (value != null) {
                    cityInfoListArray.put(value.serialize());
                }
            }
            json.put("cityInfoList", cityInfoListArray);
        }

        return json;
    }
}
  