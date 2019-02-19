// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.comment;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class RateCountInfo implements Serializable{

    private static final long serialVersionUID = 1178200134899754389L;
    /**
     * 评价数量
     */
    public int count;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static RateCountInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static RateCountInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            RateCountInfo result = new RateCountInfo();
            
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
        
        // 评价数量
        json.put("count", this.count);
          
        return json;
    }
}
  