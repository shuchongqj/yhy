// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.user;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class MilitaryCertificate implements Serializable{

    private static final long serialVersionUID = -5658696502012887940L;
    /**
     * 证件基本信息
     */
    public Certificate cert;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static MilitaryCertificate deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static MilitaryCertificate deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            MilitaryCertificate result = new MilitaryCertificate();
            
            // 证件基本信息
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
        
        // 证件基本信息
        if (this.cert != null) { json.put("cert", this.cert.serialize()); }
          
        return json;
    }
}
  