// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.club;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ActivityJsonInfo implements Serializable{

    private static final long serialVersionUID = -4650417530440149084L;
    /**
     * 活动介绍key
     */
    public String activityTitle;
      
    /**
     * 活动介绍value
     */
    public String avtivityDec;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ActivityJsonInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ActivityJsonInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ActivityJsonInfo result = new ActivityJsonInfo();
            
            // 活动介绍key
            
              if(!json.isNull("activityTitle")){
                  result.activityTitle = json.optString("activityTitle", null);
              }
            // 活动介绍value
            
              if(!json.isNull("avtivityDec")){
                  result.avtivityDec = json.optString("avtivityDec", null);
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
        
        // 活动介绍key
        if(this.activityTitle != null) { json.put("activityTitle", this.activityTitle); }
          
        // 活动介绍value
        if(this.avtivityDec != null) { json.put("avtivityDec", this.avtivityDec); }
          
        return json;
    }
}
  