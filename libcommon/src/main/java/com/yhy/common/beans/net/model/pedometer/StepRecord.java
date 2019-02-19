// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.pedometer;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class StepRecord implements Serializable{

    private static final long serialVersionUID = 7416711070437901989L;
    /**
     * 记步详细记录时间
     */
    public long recordTime;
      
    /**
     * 记录步数
     */
    public long stepCounts;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static StepRecord deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static StepRecord deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            StepRecord result = new StepRecord();
            
            // 记步详细记录时间
            result.recordTime = json.optLong("recordTime");
            // 记录步数
            result.stepCounts = json.optLong("stepCounts");
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 记步详细记录时间
        json.put("recordTime", this.recordTime);
          
        // 记录步数
        json.put("stepCounts", this.stepCounts);
          
        return json;
    }
}
  