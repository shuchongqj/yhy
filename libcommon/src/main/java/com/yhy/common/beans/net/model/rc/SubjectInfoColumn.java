// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;

import com.yhy.common.beans.net.model.club.SubjectInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SubjectInfoColumn implements Serializable{

    private static final long serialVersionUID = -9032155590607585474L;
    /**
     * 标题
     */
    public String title;

    /**
     * 描述
     */
    public String description;

    /**
     * 直播信息
     */
    public List<SubjectInfo> subjectInfoList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SubjectInfoColumn deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SubjectInfoColumn deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SubjectInfoColumn result = new SubjectInfoColumn();

            // 标题

            if(!json.isNull("title")){
                result.title = json.optString("title", null);
            }
            // 描述

            if(!json.isNull("description")){
                result.description = json.optString("description", null);
            }
            // 直播信息
            JSONArray subjectInfoListArray = json.optJSONArray("subjectInfoList");
            if (subjectInfoListArray != null) {
                int len = subjectInfoListArray.length();
                result.subjectInfoList = new ArrayList<SubjectInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = subjectInfoListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.subjectInfoList.add(SubjectInfo.deserialize(jo));
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

        // 直播信息 
        if (this.subjectInfoList != null) {
            JSONArray subjectInfoListArray = new JSONArray();
            for (SubjectInfo value : this.subjectInfoList)
            {
                if (value != null) {
                    subjectInfoListArray.put(value.serialize());
                }
            }
            json.put("subjectInfoList", subjectInfoListArray);
        }

        return json;
    }
}
  