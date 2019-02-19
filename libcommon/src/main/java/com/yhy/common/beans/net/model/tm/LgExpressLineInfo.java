// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class LgExpressLineInfo implements Serializable{

    private static final long serialVersionUID = 5311155603484344108L;
    /**
     * 内容
     */
    public String context;
      
    /**
     * 时间
     */
    public String time;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static LgExpressLineInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static LgExpressLineInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            LgExpressLineInfo result = new LgExpressLineInfo();
            
            // 内容
            
              if(!json.isNull("context")){
                  result.context = json.optString("context", null);
              }
            // 时间
            
              if(!json.isNull("time")){
                  result.time = json.optString("time", null);
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
        
        // 内容
        if(this.context != null) { json.put("context", this.context); }
          
        // 时间
        if(this.time != null) { json.put("time", this.time); }
          
        return json;
    }
}
  