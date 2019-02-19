// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.user;
    

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class PassportCertificate implements Serializable{

    private static final long serialVersionUID = 3073325443527058627L;
    /**
     * 基本信息
     */
    public Certificate cert;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static PassportCertificate deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static PassportCertificate deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            PassportCertificate result = new PassportCertificate();
            
            // 基本信息
            result.cert = Certificate.deserialize(json.optJSONObject("cert"));
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 基本信息
        if (this.cert != null) { json.put("cert", this.cert.serialize()); }
          
        return json;
    }
}
  