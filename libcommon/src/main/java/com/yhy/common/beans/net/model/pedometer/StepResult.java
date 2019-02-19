// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.pedometer;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class StepResult implements Serializable{

    private static final long serialVersionUID = -2020880554361033457L;
    /**
     * 步数对应积分
     */
    public long point;
      
    /**
     * 调用接口是否成功
     */
    public boolean success;
      
    /**
     * 是否翻倍
     */
    public boolean doublePoint;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static StepResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static StepResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            StepResult result = new StepResult();
            
            // 步数对应积分
            result.point = json.optLong("point");
            // 调用接口是否成功
            result.success = json.optBoolean("success");
            // 是否翻倍
            result.doublePoint = json.optBoolean("doublePoint");
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 步数对应积分
        json.put("point", this.point);
          
        // 调用接口是否成功
        json.put("success", this.success);
          
        // 是否翻倍
        json.put("doublePoint", this.doublePoint);
          
        return json;
    }
}
  