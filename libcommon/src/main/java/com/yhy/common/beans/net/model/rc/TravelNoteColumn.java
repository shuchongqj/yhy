// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;

import com.yhy.common.beans.net.model.discover.TravelSpecialInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TravelNoteColumn implements Serializable{

    private static final long serialVersionUID = -5815528363936000688L;
    /**
     * 标题
     */
    public String title;
      
    /**
     * 描述
     */
    public String description;
      
    /**
     * 品质游记列表
     */
    public List<TravelSpecialInfo> travelNoteInfoList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TravelNoteColumn deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TravelNoteColumn deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TravelNoteColumn result = new TravelNoteColumn();
            
            // 标题
            
              if(!json.isNull("title")){
                  result.title = json.optString("title", null);
              }
            // 描述
            
              if(!json.isNull("description")){
                  result.description = json.optString("description", null);
              }
            // 品质游记列表
            JSONArray TravelSpecialInfoListArray = json.optJSONArray("travelNoteInfoList");
            if (TravelSpecialInfoListArray != null) {
                int len = TravelSpecialInfoListArray.length();
                result.travelNoteInfoList = new ArrayList<TravelSpecialInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = TravelSpecialInfoListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.travelNoteInfoList.add(TravelSpecialInfo.deserialize(jo));
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
          
        // 品质游记列表 
        if (this.travelNoteInfoList != null) {
            JSONArray TravelSpecialInfoListArray = new JSONArray();
            for (TravelSpecialInfo value : this.travelNoteInfoList)
            {
                if (value != null) {
                    TravelSpecialInfoListArray.put(value.serialize());
                }
            }
            json.put("travelNoteInfoList", TravelSpecialInfoListArray);
        }
      
        return json;
    }
}
  