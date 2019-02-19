// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;


import com.yhy.common.beans.net.model.club.SnsActivePageInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ActivityInfoColum implements Serializable{

    private static final long serialVersionUID = 6712030446302000392L;
    /**
     * 标题
     */
    public String title;

    /**
     * 描述
     */
    public String description;

    /**
     * 活动列表
     */
    public List<SnsActivePageInfo> activityInfoList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ActivityInfoColum deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ActivityInfoColum deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ActivityInfoColum result = new ActivityInfoColum();

            // 标题

            if(!json.isNull("title")){
                result.title = json.optString("title", null);
            }
            // 描述

            if(!json.isNull("description")){
                result.description = json.optString("description", null);
            }
            // 活动列表
            JSONArray activityInfoListArray = json.optJSONArray("activityInfoList");
            if (activityInfoListArray != null) {
                int len = activityInfoListArray.length();
                result.activityInfoList = new ArrayList<SnsActivePageInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = activityInfoListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.activityInfoList.add(SnsActivePageInfo.deserialize(jo));
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
        if(this.title != null) { json.put("title", this.title); }

        // 描述
        if(this.description != null) { json.put("description", this.description); }

        // 活动列表 
        if (this.activityInfoList != null) {
            JSONArray activityInfoListArray = new JSONArray();
            for (SnsActivePageInfo value : this.activityInfoList)
            {
                if (value != null) {
                    activityInfoListArray.put(value.serialize());
                }
            }
            json.put("activityInfoList", activityInfoListArray);
        }

        return json;
    }
}
  