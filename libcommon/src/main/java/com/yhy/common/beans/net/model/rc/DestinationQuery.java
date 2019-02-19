// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class DestinationQuery implements Serializable{

    private static final long serialVersionUID = 3492118365257585148L;
    /**
     * 名称
     */
    public String name;
      
    /**
     * 编码
     */
    public int code;
      
    /**
     * 父级编码
     */
    public int parentCode;
      
    /**
     * 类型:FOREIGN_REGION-国际区域  PROVINCE-省份  CITY-城市 AREA-行政区域 COMMERCIAL_AREA-商业圈 SCENIC-景区  OVERSEAS-国际
     */
    public String type;
      
    /**
     * 类型:SCENIC-景区  LINE-线路  HOTEL-酒店
     */
    public String outType;
      
    /**
     * 简拼
     */
    public String simpleCode;
      
    /**
     * 名字模糊查询
     */
    public String nameLike;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static DestinationQuery deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static DestinationQuery deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            DestinationQuery result = new DestinationQuery();
            
            // 名称
            
              if(!json.isNull("name")){
                  result.name = json.optString("name", null);
              }
            // 编码
            result.code = json.optInt("code");
            // 父级编码
            result.parentCode = json.optInt("parentCode");
            // 类型:FOREIGN_REGION-国际区域  PROVINCE-省份  CITY-城市 AREA-行政区域 COMMERCIAL_AREA-商业圈 SCENIC-景区  OVERSEAS-国际
            
              if(!json.isNull("type")){
                  result.type = json.optString("type", null);
              }
            // 类型:SCENIC-景区  LINE-线路  HOTEL-酒店
            
              if(!json.isNull("outType")){
                  result.outType = json.optString("outType", null);
              }
            // 简拼
            
              if(!json.isNull("simpleCode")){
                  result.simpleCode = json.optString("simpleCode", null);
              }
            // 名字模糊查询
            
              if(!json.isNull("nameLike")){
                  result.nameLike = json.optString("nameLike", null);
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
        
        // 名称
        if(this.name != null) { json.put("name", this.name); }
          
        // 编码
        json.put("code", this.code);
          
        // 父级编码
        json.put("parentCode", this.parentCode);
          
        // 类型:FOREIGN_REGION-国际区域  PROVINCE-省份  CITY-城市 AREA-行政区域 COMMERCIAL_AREA-商业圈 SCENIC-景区  OVERSEAS-国际
        if(this.type != null) { json.put("type", this.type); }
          
        // 类型:SCENIC-景区  LINE-线路  HOTEL-酒店
        if(this.outType != null) { json.put("outType", this.outType); }
          
        // 简拼
        if(this.simpleCode != null) { json.put("simpleCode", this.simpleCode); }
          
        // 名字模糊查询
        if(this.nameLike != null) { json.put("nameLike", this.nameLike); }
          
        return json;
    }
}
  