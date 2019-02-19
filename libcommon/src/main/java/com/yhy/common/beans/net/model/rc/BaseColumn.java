// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BaseColumn {

    /**
     * 标题
     */
    public String title;
      
    /**
     * 描述
     */
    public String description;
      
    /**
     * 基础信息列表
     */
    public List<BaseInfo> clubCategoryList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static BaseColumn deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static BaseColumn deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            BaseColumn result = new BaseColumn();
            
            // 标题
            
              if(!json.isNull("title")){
                  result.title = json.optString("title", null);
              }
            // 描述
            
              if(!json.isNull("description")){
                  result.description = json.optString("description", null);
              }
            // 基础信息列表
            JSONArray clubCategoryListArray = json.optJSONArray("clubCategoryList");
            if (clubCategoryListArray != null) {
                int len = clubCategoryListArray.length();
                result.clubCategoryList = new ArrayList<BaseInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = clubCategoryListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.clubCategoryList.add(BaseInfo.deserialize(jo));
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
          
        // 基础信息列表 
        if (this.clubCategoryList != null) {
            JSONArray clubCategoryListArray = new JSONArray();
            for (BaseInfo value : this.clubCategoryList)
            {
                if (value != null) {
                    clubCategoryListArray.put(value.serialize());
                }
            }
            json.put("clubCategoryList", clubCategoryListArray);
        }
      
        return json;
    }
}
  