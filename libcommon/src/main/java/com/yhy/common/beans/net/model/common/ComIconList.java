// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ComIconList implements Serializable{

    private static final long serialVersionUID = -2269423090773367194L;
    /**
     * 图标信息
     */
    public List<ComIconInfo> comIconInfoList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ComIconList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ComIconList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ComIconList result = new ComIconList();
            
            // 图标信息
            JSONArray comIconInfoListArray = json.optJSONArray("comIconInfoList");
            if (comIconInfoListArray != null) {
                int len = comIconInfoListArray.length();
                result.comIconInfoList = new ArrayList<ComIconInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = comIconInfoListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.comIconInfoList.add(ComIconInfo.deserialize(jo));
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
        
        // 图标信息 
        if (this.comIconInfoList != null) {
            JSONArray comIconInfoListArray = new JSONArray();
            for (ComIconInfo value : this.comIconInfoList)
            {
                if (value != null) {
                    comIconInfoListArray.put(value.serialize());
                }
            }
            json.put("comIconInfoList", comIconInfoListArray);
        }
      
        return json;
    }
}
  