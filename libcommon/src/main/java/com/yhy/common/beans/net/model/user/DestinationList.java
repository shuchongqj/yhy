// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.user;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DestinationList implements Serializable{

    private static final long serialVersionUID = 5303037857200719323L;
    /**
     * 目的地
     */
    public List<Destination> value;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static DestinationList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static DestinationList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            DestinationList result = new DestinationList();
            
            // 目的地
            JSONArray valueArray = json.optJSONArray("value");
            if (valueArray != null) {
                int len = valueArray.length();
                result.value = new ArrayList<Destination>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = valueArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.value.add(Destination.deserialize(jo));
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
        
        // 目的地 
        if (this.value != null) {
            JSONArray valueArray = new JSONArray();
            for (Destination value : this.value)
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
  