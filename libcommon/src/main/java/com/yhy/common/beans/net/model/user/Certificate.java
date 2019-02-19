// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.user;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Certificate implements Serializable {

    private static final long serialVersionUID = -4173762808892953760L;
    /**
     * 证件类型 1-身份证 2-护照 3-军人证 4-港澳通行证
     */
    public String type;
      
    /**
     * 证件号码
     */
    public String cardNO;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Certificate deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Certificate deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            Certificate result = new Certificate();
            
            // 证件类型 1-身份证 2-护照 3-军人证 4-港澳通行证
            
              if(!json.isNull("type")){
                  result.type = json.optString("type", null);
              }
            // 证件号码
            
              if(!json.isNull("cardNO")){
                  result.cardNO = json.optString("cardNO", null);
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
        
        // 证件类型 1-身份证 2-护照 3-军人证 4-港澳通行证
        if(this.type != null) { json.put("type", this.type); }
          
        // 证件号码
        if(this.cardNO != null) { json.put("cardNO", this.cardNO); }
          
        return json;
    }
}
  