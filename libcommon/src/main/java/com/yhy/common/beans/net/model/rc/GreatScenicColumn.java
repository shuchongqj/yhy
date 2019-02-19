// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;

import com.yhy.common.beans.net.model.trip.ScenicInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GreatScenicColumn implements Serializable{

    private static final long serialVersionUID = -4104217205266476189L;
    /**
     * 标题
     */
    public String title;
      
    /**
     * 描述
     */
    public String description;
      
    /**
     * 必去景点列表
     */
    public List<ScenicInfo> greatScenicInfoList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static GreatScenicColumn deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static GreatScenicColumn deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            GreatScenicColumn result = new GreatScenicColumn();
            
            // 标题
            
              if(!json.isNull("title")){
                  result.title = json.optString("title", null);
              }
            // 描述
            
              if(!json.isNull("description")){
                  result.description = json.optString("description", null);
              }
            // 必去景点列表
            JSONArray greatScenicInfoListArray = json.optJSONArray("greatScenicInfoList");
            if (greatScenicInfoListArray != null) {
                int len = greatScenicInfoListArray.length();
                result.greatScenicInfoList = new ArrayList<ScenicInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = greatScenicInfoListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.greatScenicInfoList.add(ScenicInfo.deserialize(jo));
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
          
        // 必去景点列表 
        if (this.greatScenicInfoList != null) {
            JSONArray greatScenicInfoListArray = new JSONArray();
            for (ScenicInfo value : this.greatScenicInfoList)
            {
                if (value != null) {
                    greatScenicInfoListArray.put(value.serialize());
                }
            }
            json.put("greatScenicInfoList", greatScenicInfoListArray);
        }
      
        return json;
    }
}
  