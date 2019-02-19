// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.pedometer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StepParam implements Serializable{

    private static final long serialVersionUID = 9085499627338452103L;
    /**
     * 步数
     */
    public int stepCounts;
      
    /**
     * 记步日期
     */
    public long stepDate;
      
    /**
     * 记步详细记录
     */
    public List<StepRecord> stepRecordList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static StepParam deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static StepParam deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            StepParam result = new StepParam();
            
            // 步数
            result.stepCounts = json.optInt("stepCounts");
            // 记步日期
            result.stepDate = json.optLong("stepDate");
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
        
        // 步数
        json.put("stepCounts", this.stepCounts);
          
        // 记步日期
        json.put("stepDate", this.stepDate);
          
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
  