// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.comment;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ComTagInfo implements Serializable{

    private static final long serialVersionUID = 4509868050058385114L;
    /**
     * 标签id
     */
    public long id;
      
    /**
     * 标签名
     */
    public String name;
      
    /**
     * 创建时间
     */
    public long gmtCreated;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ComTagInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ComTagInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ComTagInfo result = new ComTagInfo();
            
            // 标签id
            result.id = json.optLong("id");
            // 标签名
            
              if(!json.isNull("name")){
                  result.name = json.optString("name", null);
              }
            // 创建时间
            result.gmtCreated = json.optLong("gmtCreated");
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 标签id
        json.put("id", this.id);
          
        // 标签名
        if(this.name != null) { json.put("name", this.name); }
          
        // 创建时间
        json.put("gmtCreated", this.gmtCreated);
          
        return json;
    }
}
  