// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.pedometer;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PedometerHistoryResultList implements Serializable{

    private static final long serialVersionUID = 2511381979593702724L;
    /**
     * 历史记步信息
     */
    public List<PedometerHistoryResult> pedometerHistoryResultList;
    /**
     * 当日步数积分
     */
    public StepResult stepResult;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static PedometerHistoryResultList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static PedometerHistoryResultList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            PedometerHistoryResultList result = new PedometerHistoryResultList();
            
            // 历史记步信息
            JSONArray pedometerHistoryResultListArray = json.optJSONArray("pedometerHistoryResultList");
            if (pedometerHistoryResultListArray != null) {
                int len = pedometerHistoryResultListArray.length();
                result.pedometerHistoryResultList = new ArrayList<PedometerHistoryResult>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = pedometerHistoryResultListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.pedometerHistoryResultList.add(PedometerHistoryResult.deserialize(jo));
                    }
                }
            }
      
            // 当日步数积分
            result.stepResult = StepResult.deserialize(json.optJSONObject("stepResult"));
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 历史记步信息 
        if (this.pedometerHistoryResultList != null) {
            JSONArray pedometerHistoryResultListArray = new JSONArray();
            for (PedometerHistoryResult value : this.pedometerHistoryResultList)
            {
                if (value != null) {
                    pedometerHistoryResultListArray.put(value.serialize());
                }
            }
            json.put("pedometerHistoryResultList", pedometerHistoryResultListArray);
        }
      
        // 当日步数积分
        if (this.stepResult != null) { json.put("stepResult", this.stepResult.serialize()); }
          
        return json;
    }
}
  