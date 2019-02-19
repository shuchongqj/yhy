// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class QueryRoomDTO implements Serializable{

    private static final long serialVersionUID = 6501210701058861529L;
    /**
     * 酒店id
     */
    public long hotelId;
      
    /**
     * 开始时间
     */
    public long startTime;
      
    /**
     * 结束时间
     */
    public long endTime;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static QueryRoomDTO deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static QueryRoomDTO deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            QueryRoomDTO result = new QueryRoomDTO();
            
            // 酒店id
            result.hotelId = json.optLong("hotelId");
            // 开始时间
            result.startTime = json.optLong("startTime");
            // 结束时间
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
        
        // 酒店id
        json.put("hotelId", this.hotelId);
          
        // 开始时间
        json.put("startTime", this.startTime);
          
        // 结束时间
        json.put("endTime", this.endTime);
          
        return json;
    }
}
  