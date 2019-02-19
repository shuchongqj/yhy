// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DimensionList implements Serializable{

    private static final long serialVersionUID = 5591050510723665699L;
    /**
     * 维度信息列表
     */
    public List<DimensionInfo> dimensionList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static DimensionList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static DimensionList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            DimensionList result = new DimensionList();
            
            // 维度信息列表
            JSONArray dimensionListArray = json.optJSONArray("dimensionList");
            if (dimensionListArray != null) {
                int len = dimensionListArray.length();
                result.dimensionList = new ArrayList<DimensionInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = dimensionListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.dimensionList.add(DimensionInfo.deserialize(jo));
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
        
        // 维度信息列表 
        if (this.dimensionList != null) {
            JSONArray dimensionListArray = new JSONArray();
            for (DimensionInfo value : this.dimensionList)
            {
                if (value != null) {
                    dimensionListArray.put(value.serialize());
                }
            }
            json.put("dimensionList", dimensionListArray);
        }
      
        return json;
    }
}
  