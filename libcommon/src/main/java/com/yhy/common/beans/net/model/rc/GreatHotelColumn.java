// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;


import com.yhy.common.beans.net.model.trip.HotelInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GreatHotelColumn implements Serializable{

    private static final long serialVersionUID = -5853548307155298882L;
    /**
     * 标题
     */
    public String title;
      
    /**
     * 描述
     */
    public String description;
      
    /**
     * 精选酒店列表
     */
    public List<HotelInfo> greatHotelInfoList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static GreatHotelColumn deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static GreatHotelColumn deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            GreatHotelColumn result = new GreatHotelColumn();
            
            // 标题
            
              if(!json.isNull("title")){
                  result.title = json.optString("title", null);
              }
            // 描述
            
              if(!json.isNull("description")){
                  result.description = json.optString("description", null);
              }
            // 精选酒店列表
            JSONArray greatHotelInfoListArray = json.optJSONArray("greatHotelInfoList");
            if (greatHotelInfoListArray != null) {
                int len = greatHotelInfoListArray.length();
                result.greatHotelInfoList = new ArrayList<HotelInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = greatHotelInfoListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.greatHotelInfoList.add(HotelInfo.deserialize(jo));
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
        
        // 标题
        if(this.title != null) { json.put("title", this.title); }
          
        // 描述
        if(this.description != null) { json.put("description", this.description); }
          
        // 精选酒店列表 
        if (this.greatHotelInfoList != null) {
            JSONArray greatHotelInfoListArray = new JSONArray();
            for (HotelInfo value : this.greatHotelInfoList)
            {
                if (value != null) {
                    greatHotelInfoListArray.put(value.serialize());
                }
            }
            json.put("greatHotelInfoList", greatHotelInfoListArray);
        }
      
        return json;
    }
}
  