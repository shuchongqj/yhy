// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class RoutePlanVO implements Serializable{

    private static final long serialVersionUID = -4518248500044060691L;
    /**
     * 去程交通信息
     */
    public RouteTrafficInfo departTrafficInfo;
      
    /**
     * 回程交通信息
     */
    public RouteTrafficInfo backTrafficInfo;
      
    /**
     * 酒店信息
     */
    public String hotelInfo;
      
    /**
     * 景区信息
     */
    public String scenicInfo;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static RoutePlanVO deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static RoutePlanVO deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            RoutePlanVO result = new RoutePlanVO();
            
            // 去程交通信息
            result.departTrafficInfo = RouteTrafficInfo.deserialize(json.optJSONObject("departTrafficInfo"));
            // 回程交通信息
            result.backTrafficInfo = RouteTrafficInfo.deserialize(json.optJSONObject("backTrafficInfo"));
            // 酒店信息
            
              if(!json.isNull("hotelInfo")){
                  result.hotelInfo = json.optString("hotelInfo", null);
              }
            // 景区信息
            
              if(!json.isNull("scenicInfo")){
                  result.scenicInfo = json.optString("scenicInfo", null);
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
        
        // 去程交通信息
        if (this.departTrafficInfo != null) { json.put("departTrafficInfo", this.departTrafficInfo.serialize()); }
          
        // 回程交通信息
        if (this.backTrafficInfo != null) { json.put("backTrafficInfo", this.backTrafficInfo.serialize()); }
          
        // 酒店信息
        if(this.hotelInfo != null) { json.put("hotelInfo", this.hotelInfo); }
          
        // 景区信息
        if(this.scenicInfo != null) { json.put("scenicInfo", this.scenicInfo); }
          
        return json;
    }
}
  