// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class RCExtendFieldInfo implements Serializable{

    private static final long serialVersionUID = -528044450040388662L;
    /**
     * key值
     */
    public String key;
      
    /**
     * value值
     */
    public String value;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static RCExtendFieldInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static RCExtendFieldInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            RCExtendFieldInfo result = new RCExtendFieldInfo();
            
            // key值
            
              if(!json.isNull("key")){
                  result.key = json.optString("key", null);
              }
            // value值
            
              if(!json.isNull("value")){
                  result.value = json.optString("value", null);
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
        
        // key值
        if(this.key != null) { json.put("key", this.key); }
          
        // value值
        if(this.value != null) { json.put("value", this.value); }
          
        return json;
    }
}
  