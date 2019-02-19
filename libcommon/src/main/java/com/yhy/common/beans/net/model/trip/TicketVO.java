// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TicketVO implements Serializable{

    private static final long serialVersionUID = 2496508273806500617L;
    /**
     * 票型id
     */
    public long id;
      
    /**
     * 票型名称
     */
    public String title;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TicketVO deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TicketVO deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TicketVO result = new TicketVO();
            
            // 票型id
            result.id = json.optLong("id");
            // 票型名称
            
              if(!json.isNull("title")){
                  result.title = json.optString("title", null);
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
        
        // 票型id
        json.put("id", this.id);
          
        // 票型名称
        if(this.title != null) { json.put("title", this.title); }
          
        return json;
    }
}
  