// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;


import com.yhy.common.beans.net.model.trip.LineInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyTripContent implements Serializable {

    private static final long serialVersionUID = -4462452043258979206L;
    /**
     * 标题
     */
    public String title;
      
    /**
     * 描述
     */
    public String description;
      
    /**
     * 我的行程列表
     */
    public List<LineInfo> list;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static MyTripContent deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static MyTripContent deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            MyTripContent result = new MyTripContent();
            
            // 标题
            
              if(!json.isNull("title")){
                  result.title = json.optString("title", null);
              }
            // 描述
            
              if(!json.isNull("description")){
                  result.description = json.optString("description", null);
              }
            // 我的行程列表
            JSONArray listArray = json.optJSONArray("list");
            if (listArray != null) {
                int len = listArray.length();
                result.list = new ArrayList<LineInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = listArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.list.add(LineInfo.deserialize(jo));
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
          
        // 我的行程列表 
        if (this.list != null) {
            JSONArray listArray = new JSONArray();
            for (LineInfo value : this.list)
            {
                if (value != null) {
                    listArray.put(value.serialize());
                }
            }
            json.put("list", listArray);
        }
      
        return json;
    }
}
  