// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class PostRateParam implements Serializable{

    private static final long serialVersionUID = -4903203725016837808L;
    /**
     * 主订单评价对象
     */
    public OrderRate mainOrderRate;
      
    /**
     * 用户id 必填
     */
    public long userId;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static PostRateParam deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static PostRateParam deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            PostRateParam result = new PostRateParam();
            
            // 主订单评价对象
            result.mainOrderRate = OrderRate.deserialize(json.optJSONObject("mainOrderRate"));
            // 用户id 必填
            result.userId = json.optLong("userId");
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 主订单评价对象
        if (this.mainOrderRate != null) { json.put("mainOrderRate", this.mainOrderRate.serialize()); }
          
        // 用户id 必填
        json.put("userId", this.userId);
          
        return json;
    }
}
  