// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.comment;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ComSupportInfo implements Serializable{

    private static final long serialVersionUID = 5854453594034037698L;
    /**
     * id
     */
    public long id;
      
    /**
     * 外部id
     */
    public long outId;
      
    /**
     * 类型
     */
    public String outType;
      
    /**
     * 点赞状态
     */
    public String isSupport;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ComSupportInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ComSupportInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ComSupportInfo result = new ComSupportInfo();
            
            // id
            result.id = json.optLong("id");
            // 外部id
            result.outId = json.optLong("outId");
            // 类型
            
              if(!json.isNull("outType")){
                  result.outType = json.optString("outType", null);
              }
            // 点赞状态
            
              if(!json.isNull("isSupport")){
                  result.isSupport = json.optString("isSupport", null);
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
        
        // id
        json.put("id", this.id);
          
        // 外部id
        json.put("outId", this.outId);
          
        // 类型
        if(this.outType != null) { json.put("outType", this.outType); }
          
        // 点赞状态
        if(this.isSupport != null) { json.put("isSupport", this.isSupport); }
          
        return json;
    }
}
  