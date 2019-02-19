// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FacilityGroup implements Serializable{

    private static final long serialVersionUID = -7730369580956571229L;
    /**
     * 名称
     */
    public String groupName;
      
    /**
     * 组类型 HOTEL_FACILITY:酒店设施 / HOTEL_SERVICE:酒店服务 / ROOM_FACILITY:房间设施
     */
    public String type;
      
    /**
     * 设施list
     */
    public List<Facility> facilityList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static FacilityGroup deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static FacilityGroup deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            FacilityGroup result = new FacilityGroup();
            
            // 名称
            
              if(!json.isNull("groupName")){
                  result.groupName = json.optString("groupName", null);
              }
            // 组类型
            
              if(!json.isNull("type")){
                  result.type = json.optString("type", null);
              }
            // 设施list
            JSONArray facilityListArray = json.optJSONArray("facilityList");
            if (facilityListArray != null) {
                int len = facilityListArray.length();
                result.facilityList = new ArrayList<Facility>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = facilityListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.facilityList.add(Facility.deserialize(jo));
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
        
        // 名称
        if(this.groupName != null) { json.put("groupName", this.groupName); }
          
        // 组类型
        if(this.type != null) { json.put("type", this.type); }
          
        // 设施list 
        if (this.facilityList != null) {
            JSONArray facilityListArray = new JSONArray();
            for (Facility value : this.facilityList)
            {
                if (value != null) {
                    facilityListArray.put(value.serialize());
                }
            }
            json.put("facilityList", facilityListArray);
        }
      
        return json;
    }
}
  