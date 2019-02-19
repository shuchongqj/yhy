// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.pedometer;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SyncParam implements Serializable{

    private static final long serialVersionUID = 8558557227598540428L;
    /**
     * 最后好记步时间戳,用于同步，客户端记录
     */
    public long lastRecordTime;
      
    /**
     * 记步日期
     */
    public long stepDate;
      
    /**
     * 当日总步数
     */
    public long stepCounts;
      
    /**
     * 当日目标步数
     */
    public long targetSteps;
      
    /**
     * 燃烧脂肪
     */
    public float fats;
      
    /**
     * 燃烧卡路里
     */
    public float calorie;
      
    /**
     * 记步距离，单位km
     */
    public float distance;
      
    /**
     * 记步详细记录
     */
    public List<StepRecord> stepRecordList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SyncParam deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SyncParam deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SyncParam result = new SyncParam();
            
            // 最后好记步时间戳,用于同步，客户端记录
            result.lastRecordTime = json.optLong("lastRecordTime");
            // 记步日期
            result.stepDate = json.optLong("stepDate");
            // 当日总步数
            result.stepCounts = json.optLong("stepCounts");
            // 当日目标步数
            result.targetSteps = json.optLong("targetSteps");
            // 燃烧脂肪
            result.fats = (float)json.optDouble("fats");
            // 燃烧卡路里
            result.calorie = (float)json.optDouble("calorie");
            // 记步距离，单位km
            result.distance = (float)json.optDouble("distance");
            // 记步详细记录
            JSONArray stepRecordListArray = json.optJSONArray("stepRecordList");
            if (stepRecordListArray != null) {
                int len = stepRecordListArray.length();
                result.stepRecordList = new ArrayList<StepRecord>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = stepRecordListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.stepRecordList.add(StepRecord.deserialize(jo));
                    }
                }
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
        
        // 最后好记步时间戳,用于同步，客户端记录
        json.put("lastRecordTime", this.lastRecordTime);
          
        // 记步日期
        json.put("stepDate", this.stepDate);
          
        // 当日总步数
        json.put("stepCounts", this.stepCounts);
          
        // 当日目标步数
        json.put("targetSteps", this.targetSteps);
          
        // 燃烧脂肪
        json.put("fats", this.fats);
          
        // 燃烧卡路里
        json.put("calorie", this.calorie);
          
        // 记步距离，单位km
        json.put("distance", this.distance);
          
        // 记步详细记录 
        if (this.stepRecordList != null) {
            JSONArray stepRecordListArray = new JSONArray();
            for (StepRecord value : this.stepRecordList)
            {
                if (value != null) {
                    stepRecordListArray.put(value.serialize());
                }
            }
            json.put("stepRecordList", stepRecordListArray);
        }
      
        return json;
    }
}
  