// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TmRouteTextItem implements Serializable{

    private static final long serialVersionUID = -3752241914938054549L;
    /**
     * 行程项id
     */
    public long id;
      
    /**
     * 行程项类型 BREAKFAST早餐/LUNCH午餐/DINNER晚餐/HOTEL酒店/SCENIC景点
     */
    public String type;
      
    /**
     * 行程项名字 如怡心园
     */
    public String name;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmRouteTextItem deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmRouteTextItem deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmRouteTextItem result = new TmRouteTextItem();
            
            // 行程项id
            result.id = json.optLong("id");
            // 行程项类型 BREAKFAST早餐/LUNCH午餐/DINNER晚餐/HOTEL酒店/SCENIC景点
            
              if(!json.isNull("type")){
                  result.type = json.optString("type", null);
              }
            // 行程项名字 如怡心园
            
              if(!json.isNull("name")){
                  result.name = json.optString("name", null);
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
        
        // 行程项id
        json.put("id", this.id);
          
        // 行程项类型 BREAKFAST早餐/LUNCH午餐/DINNER晚餐/HOTEL酒店/SCENIC景点
        if(this.type != null) { json.put("type", this.type); }
          
        // 行程项名字 如怡心园
        if(this.name != null) { json.put("name", this.name); }
          
        return json;
    }
}
  