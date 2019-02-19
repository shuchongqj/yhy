// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.club;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class POIInfo implements Serializable{

    private static final long serialVersionUID = 147841874634913068L;
    /**
     * 详细
     */
    public String detail;
      
    /**
     * 经度
     */
    public double longitude;
      
    /**
     * 纬度
     */
    public double latitude;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static POIInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static POIInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            POIInfo result = new POIInfo();
            
            // 详细
            
              if(!json.isNull("detail")){
                  result.detail = json.optString("detail", null);
              }
            // 经度
            result.longitude = json.optDouble("longitude");
            // 纬度
            result.latitude = json.optDouble("latitude");
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 详细
        if(this.detail != null) { json.put("detail", this.detail); }
          
        // 经度
        json.put("longitude", this.longitude);
          
        // 纬度
        json.put("latitude", this.latitude);
          
        return json;
    }
}
  