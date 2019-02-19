// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HotelFacilityResult implements Serializable{

    private static final long serialVersionUID = -5815014294304115630L;
    /**
     * 设施组list
     */
    public List<FacilityGroup> facilityGroupList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static HotelFacilityResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static HotelFacilityResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            HotelFacilityResult result = new HotelFacilityResult();
            
            // 设施组list
            JSONArray facilityGroupListArray = json.optJSONArray("facilityGroupList");
            if (facilityGroupListArray != null) {
                int len = facilityGroupListArray.length();
                result.facilityGroupList = new ArrayList<FacilityGroup>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = facilityGroupListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.facilityGroupList.add(FacilityGroup.deserialize(jo));
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
        
        // 设施组list 
        if (this.facilityGroupList != null) {
            JSONArray facilityGroupListArray = new JSONArray();
            for (FacilityGroup value : this.facilityGroupList)
            {
                if (value != null) {
                    facilityGroupListArray.put(value.serialize());
                }
            }
            json.put("facilityGroupList", facilityGroupListArray);
        }
      
        return json;
    }
}
  