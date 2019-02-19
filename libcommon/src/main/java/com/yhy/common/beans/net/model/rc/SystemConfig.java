// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SystemConfig implements Serializable{

    private static final long serialVersionUID = 5388740347509949254L;
    /**
     * 系统配置信息
     */
    public List<SysConfig> systemConfigList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SystemConfig deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SystemConfig deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SystemConfig result = new SystemConfig();
            
            // 系统配置信息
            JSONArray systemConfigListArray = json.optJSONArray("systemConfigList");
            if (systemConfigListArray != null) {
                int len = systemConfigListArray.length();
                result.systemConfigList = new ArrayList<SysConfig>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = systemConfigListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.systemConfigList.add(SysConfig.deserialize(jo));
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
        
        // 系统配置信息 
        if (this.systemConfigList != null) {
            JSONArray systemConfigListArray = new JSONArray();
            for (SysConfig value : this.systemConfigList)
            {
                if (value != null) {
                    systemConfigListArray.put(value.serialize());
                }
            }
            json.put("systemConfigList", systemConfigListArray);
        }
      
        return json;
    }
}
  