// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.member;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Member implements Serializable{

    private static final long serialVersionUID = 4787998296191513704L;
    /**
     * id
     */
    public long id;
      
    /**
     * user id
     */
    public long userId;
      
    /**
     * 类型
     */
    public String type;
      
    /**
     * 会员号
     */
    public String code;
      
    /**
     * 状态
     */
    public String status;
      
    /**
     * 会员有效期开始时间
     */
    public long startTime;
      
    /**
     * 会员有效期截止时间
     */
    public long endTime;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Member deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Member deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            Member result = new Member();
            
            // id
            result.id = json.optLong("id");
            // user id
            result.userId = json.optLong("userId");
            // 类型
            
              if(!json.isNull("type")){
                  result.type = json.optString("type", null);
              }
            // 会员号
            
              if(!json.isNull("code")){
                  result.code = json.optString("code", null);
              }
            // 状态
            
              if(!json.isNull("status")){
                  result.status = json.optString("status", null);
              }
            // 会员有效期开始时间
            result.startTime = json.optLong("startTime");
            // 会员有效期截止时间
            result.endTime = json.optLong("endTime");
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // id
        json.put("id", this.id);
          
        // user id
        json.put("userId", this.userId);
          
        // 类型
        if(this.type != null) { json.put("type", this.type); }
          
        // 会员号
        if(this.code != null) { json.put("code", this.code); }
          
        // 状态
        if(this.status != null) { json.put("status", this.status); }
          
        // 会员有效期开始时间
        json.put("startTime", this.startTime);
          
        // 会员有效期截止时间
        json.put("endTime", this.endTime);
          
        return json;
    }
}
  