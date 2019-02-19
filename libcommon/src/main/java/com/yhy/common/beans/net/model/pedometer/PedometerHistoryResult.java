// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.pedometer;
    
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Table(name = "pedometer_history")
public class PedometerHistoryResult implements Serializable{

    private static final long serialVersionUID = -1362680635682836503L;
    /**
     * 记步日期
     */
    @Id
    @NoAutoIncrement
    @Column(column = "stepDate")
    public long stepDate;
      
    /**
     * 当日总步数
     */
    @Column(column = "stepCounts")
    public long stepCounts;
      
    /**
     * 当日目标步数
     */
    @Column(column = "targetSteps")
    public long targetSteps;
      
    /**
     * 脂肪
     */
    @Column(column = "fats")
    public float fats;
      
    /**
     * 卡路里
     */
    @Column(column = "calorie")
    public float calorie;
      
    /**
     * 距离
     */
    @Column(column = "distance")
    public float distance;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static PedometerHistoryResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static PedometerHistoryResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            PedometerHistoryResult result = new PedometerHistoryResult();
            
            // 记步日期
            result.stepDate = json.optLong("stepDate");
            // 当日总步数
            result.stepCounts = json.optLong("stepCounts");
            // 当日目标步数
            result.targetSteps = json.optLong("targetSteps");
            // 脂肪
            result.fats = (float)json.optDouble("fats");
            // 卡路里
            result.calorie = (float)json.optDouble("calorie");
            // 距离
            result.distance = (float)json.optDouble("distance");
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 记步日期
        json.put("stepDate", this.stepDate);
          
        // 当日总步数
        json.put("stepCounts", this.stepCounts);
          
        // 当日目标步数
        json.put("targetSteps", this.targetSteps);
          
        // 脂肪
        json.put("fats", this.fats);
          
        // 卡路里
        json.put("calorie", this.calorie);
          
        // 距离
        json.put("distance", this.distance);
          
        return json;
    }
}
  