// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class DestCityPageContent implements Serializable{

    private static final long serialVersionUID = 6177804618910943640L;
    /**
     * 市名称
     */
    public String name;
      
    /**
     * 目的地描述，备用
     */
    public String description;
      
    /**
     * 目的地id
     */
    public String destId;
      
    /**
     * 必去景点
     */
    public GreatScenicColumn greatScenicColumn;
      
    /**
     * 精选酒店
     */
    public GreatHotelColumn greatHotelColumn;
      
    /**
     * 我在·XX
     */
    public BaseColumn touristShowColumn;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static DestCityPageContent deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static DestCityPageContent deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            DestCityPageContent result = new DestCityPageContent();
            
            // 市名称
            
              if(!json.isNull("name")){
                  result.name = json.optString("name", null);
              }
            // 目的地描述，备用
            
              if(!json.isNull("description")){
                  result.description = json.optString("description", null);
              }
            // 目的地id
            
              if(!json.isNull("destId")){
                  result.destId = json.optString("destId", null);
              }
            // 必去景点
            result.greatScenicColumn = GreatScenicColumn.deserialize(json.optJSONObject("greatScenicColumn"));
            // 精选酒店
            result.greatHotelColumn = GreatHotelColumn.deserialize(json.optJSONObject("greatHotelColumn"));
            // 我在·XX
            result.touristShowColumn = BaseColumn.deserialize(json.optJSONObject("touristShowColumn"));
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 市名称
        if(this.name != null) { json.put("name", this.name); }
          
        // 目的地描述，备用
        if(this.description != null) { json.put("description", this.description); }
          
        // 目的地id
        if(this.destId != null) { json.put("destId", this.destId); }
          
        // 必去景点
        if (this.greatScenicColumn != null) { json.put("greatScenicColumn", this.greatScenicColumn.serialize()); }
          
        // 精选酒店
        if (this.greatHotelColumn != null) { json.put("greatHotelColumn", this.greatHotelColumn.serialize()); }
          
        // 我在·XX
        if (this.touristShowColumn != null) { json.put("touristShowColumn", this.touristShowColumn.serialize()); }
          
        return json;
    }
}
  