// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.comment;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class RateCountQuery implements Serializable{

    private static final long serialVersionUID = -6727523664235830295L;
    /**
     * id-景区酒店等
     */
    public long outId;
      
    /**
     * 外部类型： HOTEL: 酒店，SCENIC：景区，LINE：线路，LOCAL：同城活动，PRODUCT：实物商品
     */
    public String outType;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static RateCountQuery deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static RateCountQuery deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            RateCountQuery result = new RateCountQuery();
            
            // id-景区酒店等
            result.outId = json.optLong("outId");
            // 外部类型： HOTEL: 酒店，VIEW：景区，LINE：线路，LOCAL：同城活动，PRODUCT：实物商品
            
              if(!json.isNull("outType")){
                  result.outType = json.optString("outType", null);
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
        
        // id-景区酒店等
        json.put("outId", this.outId);
          
        // 外部类型： HOTEL: 酒店，VIEW：景区，LINE：线路，LOCAL：同城活动，PRODUCT：实物商品
        if(this.outType != null) { json.put("outType", this.outType); }
          
        return json;
    }
}
  