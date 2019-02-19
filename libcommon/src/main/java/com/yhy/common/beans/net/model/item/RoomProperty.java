// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.item;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class RoomProperty implements Serializable{

    private static final long serialVersionUID = 4669042671337685108L;
    /**
     * 属性键
     */
    public String key;
      
    /**
     * 属性标题
     */
    public String title;
      
    /**
     * 属性内容
     */
    public String content;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static RoomProperty deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static RoomProperty deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            RoomProperty result = new RoomProperty();
            
            // 属性键
            
              if(!json.isNull("key")){
                  result.key = json.optString("key", null);
              }
            // 属性标题
            
              if(!json.isNull("title")){
                  result.title = json.optString("title", null);
              }
            // 属性内容
            
              if(!json.isNull("content")){
                  result.content = json.optString("content", null);
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
        
        // 属性键
        if(this.key != null) { json.put("key", this.key); }
          
        // 属性标题
        if(this.title != null) { json.put("title", this.title); }
          
        // 属性内容
        if(this.content != null) { json.put("content", this.content); }
          
        return json;
    }
}
  