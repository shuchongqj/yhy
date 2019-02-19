// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BoothList implements Serializable{

    private static final long serialVersionUID = 8277983927048601657L;
    /**
     * 展位
     */
    public List<Booth> value;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static BoothList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static BoothList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            BoothList result = new BoothList();
            
            // 展位
            JSONArray valueArray = json.optJSONArray("value");
            if (valueArray != null) {
                int len = valueArray.length();
                result.value = new ArrayList<Booth>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = valueArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.value.add(Booth.deserialize(jo));
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
        
        // 展位 
        if (this.value != null) {
            JSONArray valueArray = new JSONArray();
            for (Booth value : this.value)
            {
                if (value != null) {
                    valueArray.put(value.serialize());
                }
            }
            json.put("value", valueArray);
        }
      
        return json;
    }
}
  