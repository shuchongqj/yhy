// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.common;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ComIconInfo implements Serializable{

    private static final long serialVersionUID = 839348430258743486L;
    /**
     * 图标地址
     */
    public String icon;
      
    /**
     * type
     */
    public int type;
      
    /**
     * code
     */
    public int code;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ComIconInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ComIconInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ComIconInfo result = new ComIconInfo();
            
            // 图标地址
            
              if(!json.isNull("icon")){
                  result.icon = json.optString("icon", null);
              }
            // type
            result.type = json.optInt("type");
            // code
            result.code = json.optInt("code");
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 图标地址
        if(this.icon != null) { json.put("icon", this.icon); }
          
        // type
        json.put("type", this.type);
          
        // code
        json.put("code", this.code);
          
        return json;
    }
}
  