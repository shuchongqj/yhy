// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.master;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class MasterCertificates implements Serializable{

    private static final long serialVersionUID = -5828129312546818473L;
    /**
     * 认证ID
     */
    public int id;
      
    /**
     * 认证名称
     */
    public String name;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static MasterCertificates deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static MasterCertificates deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            MasterCertificates result = new MasterCertificates();
            
            // 认证ID
            result.id = json.optInt("id");
            // 认证名称
            
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
        
        // 认证ID
        json.put("id", this.id);
          
        // 认证名称
        if(this.name != null) { json.put("name", this.name); }
          
        return json;
    }
}
  