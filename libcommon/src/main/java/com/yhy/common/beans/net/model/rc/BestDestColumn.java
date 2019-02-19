// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;


import com.yhy.common.beans.net.model.trip.CityInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BestDestColumn implements Serializable{

    private static final long serialVersionUID = 5195406692183307017L;
    /**
     * 标题
     */
    public String title;
      
    /**
     * 描述
     */
    public String description;
      
    /**
     * 目的地列表
     */
    public List<CityInfo> bestDestInfoList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static BestDestColumn deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static BestDestColumn deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            BestDestColumn result = new BestDestColumn();
            
            // 标题
            
              if(!json.isNull("title")){
                  result.title = json.optString("title", null);
              }
            // 描述
            
              if(!json.isNull("description")){
                  result.description = json.optString("description", null);
              }
            // 目的地列表
            JSONArray CityInfoListArray = json.optJSONArray("bestDestInfoList");
            if (CityInfoListArray != null) {
                int len = CityInfoListArray.length();
                result.bestDestInfoList = new ArrayList<CityInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = CityInfoListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.bestDestInfoList.add(CityInfo.deserialize(jo));
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
          
        // 目的地列表 
        if (this.bestDestInfoList != null) {
            JSONArray CityInfoListArray = new JSONArray();
            for (CityInfo value : this.bestDestInfoList)
            {
                if (value != null) {
                    CityInfoListArray.put(value.serialize());
                }
            }
            json.put("bestDestInfoList", CityInfoListArray);
        }
      
        return json;
    }
}
  