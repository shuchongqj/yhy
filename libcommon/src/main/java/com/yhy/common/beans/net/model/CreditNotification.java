// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class CreditNotification implements Serializable{

    private static final long serialVersionUID = -3683913237258045692L;
    /**
     * 描述,为何送积分
     */
    public String description;
      
    /**
     * 积分值
     */
    public long credit;
      
    /**
     * 提示,送了多少积分
     */
    public String notification;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static CreditNotification deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static CreditNotification deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            CreditNotification result = new CreditNotification();
            
            // 描述,为何送积分
            
              if(!json.isNull("description")){
                  result.description = json.optString("description", null);
              }
            // 积分值
            result.credit = json.optLong("credit");
            // 提示,送了多少积分
            
              if(!json.isNull("notification")){
                  result.notification = json.optString("notification", null);
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
        
        // 描述,为何送积分
        if(this.description != null) { json.put("description", this.description); }
          
        // 积分值
        json.put("credit", this.credit);
          
        // 提示,送了多少积分
        if(this.notification != null) { json.put("notification", this.notification); }
          
        return json;
    }
}
  