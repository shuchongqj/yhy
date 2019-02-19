// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.comment;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class RateCaseInfo implements Serializable{

    private static final long serialVersionUID = -5236197629365430955L;
    /**
     * 评价条件code
     */
    public String code;
      
    /**
     * 评价条件名称
     */
    public String name;
      
    /**
     * 评价数量
     */
    public int count;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static RateCaseInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static RateCaseInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            RateCaseInfo result = new RateCaseInfo();
            
            // 评价条件code
            
              if(!json.isNull("code")){
                  result.code = json.optString("code", null);
              }
            // 评价条件名称
            
              if(!json.isNull("name")){
                  result.name = json.optString("name", null);
              }
            // 评价数量
            result.count = json.optInt("count");
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 评价条件code
        if(this.code != null) { json.put("code", this.code); }
          
        // 评价条件名称
        if(this.name != null) { json.put("name", this.name); }
          
        // 评价数量
        json.put("count", this.count);
          
        return json;
    }
}
  