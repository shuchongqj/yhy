// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LineFilter implements Serializable{

    private static final long serialVersionUID = -8852428825015560996L;
    /**
     * 主题列表
     */
    public List<ItemSubjectInfo> subjectInfoList;
    /**
     * 出发地列表 保留字段
     */
    public List<CityInfo> startCityList;
    /**
     * 目的地列表
     */
    public List<CityInfo> destCityList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static LineFilter deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static LineFilter deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            LineFilter result = new LineFilter();
            
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
      
            // 出发地列表 保留字段
            JSONArray startCityListArray = json.optJSONArray("startCityList");
            if (startCityListArray != null) {
                int len = startCityListArray.length();
                result.startCityList = new ArrayList<CityInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = startCityListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.startCityList.add(CityInfo.deserialize(jo));
                    }
                }
            }
      
            // 目的地列表
            JSONArray destCityListArray = json.optJSONArray("destCityList");
            if (destCityListArray != null) {
                int len = destCityListArray.length();
                result.destCityList = new ArrayList<CityInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = destCityListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.destCityList.add(CityInfo.deserialize(jo));
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
            for (ItemSubjectInfo value : this.subjectInfoList)
            {
                if (value != null) {
                    subjectInfoListArray.put(value.serialize());
                }
            }
            json.put("subjectInfoList", subjectInfoListArray);
        }
      
        // 出发地列表 保留字段 
        if (this.startCityList != null) {
            JSONArray startCityListArray = new JSONArray();
            for (CityInfo value : this.startCityList)
            {
                if (value != null) {
                    startCityListArray.put(value.serialize());
                }
            }
            json.put("startCityList", startCityListArray);
        }
      
        // 目的地列表 
        if (this.destCityList != null) {
            JSONArray destCityListArray = new JSONArray();
            for (CityInfo value : this.destCityList)
            {
                if (value != null) {
                    destCityListArray.put(value.serialize());
                }
            }
            json.put("destCityList", destCityListArray);
        }
      
        return json;
    }
}
  