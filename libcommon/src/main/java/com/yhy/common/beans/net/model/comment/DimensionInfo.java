// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.comment;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class DimensionInfo implements Serializable{

    private static final long serialVersionUID = 4960378376087519928L;
    /**
     * 维度code
     */
    public String dimensionCode;
      
    /**
     * 维度名称
     */
    public String dimensionName;
      
    /**
     * 评价分数
     */
    public long dimensionScore;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static DimensionInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static DimensionInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            DimensionInfo result = new DimensionInfo();
            
            // 维度code
            
              if(!json.isNull("dimensionCode")){
                  result.dimensionCode = json.optString("dimensionCode", null);
              }
            // 维度名称
            
              if(!json.isNull("dimensionName")){
                  result.dimensionName = json.optString("dimensionName", null);
              }
            // 评价分数
            result.dimensionScore = json.optLong("dimensionScore");
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 维度code
        if(this.dimensionCode != null) { json.put("dimensionCode", this.dimensionCode); }
          
        // 维度名称
        if(this.dimensionName != null) { json.put("dimensionName", this.dimensionName); }
          
        // 评价分数
        json.put("dimensionScore", this.dimensionScore);
          
        return json;
    }
}
  