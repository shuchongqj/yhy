// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TagInfo implements Serializable{

    private static final long serialVersionUID = -4026092551344398195L;
    /**
     * 标签ID
     */
    public long id;
      
    /**
     * 标签名称
     */
    public String name;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TagInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TagInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TagInfo result = new TagInfo();
            
            // 标签ID
            result.id = json.optLong("id");
            // 标签名称
            
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
        
        // 标签ID
        json.put("id", this.id);
          
        // 标签名称
        if(this.name != null) { json.put("name", this.name); }
          
        return json;
    }
}
  