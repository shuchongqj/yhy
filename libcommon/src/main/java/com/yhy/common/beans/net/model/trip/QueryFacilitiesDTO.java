// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class QueryFacilitiesDTO implements Serializable{

    private static final long serialVersionUID = 5218205217224824421L;
    /**
     * 酒店ID
     */
    public long hotelId;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static QueryFacilitiesDTO deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static QueryFacilitiesDTO deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            QueryFacilitiesDTO result = new QueryFacilitiesDTO();
            
            // 酒店ID
            result.hotelId = json.optLong("hotelId");
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 酒店ID
        json.put("hotelId", this.hotelId);
          
        return json;
    }
}
  