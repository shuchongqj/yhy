// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoomsResult implements Serializable{

    private static final long serialVersionUID = -3463846368753431578L;
    /**
     * 房型列表
     */
    public List<RoomInfo> roomList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static RoomsResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static RoomsResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            RoomsResult result = new RoomsResult();
            
            // 房型列表
            JSONArray roomListArray = json.optJSONArray("roomList");
            if (roomListArray != null) {
                int len = roomListArray.length();
                result.roomList = new ArrayList<RoomInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = roomListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.roomList.add(RoomInfo.deserialize(jo));
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
        
        // 房型列表 
        if (this.roomList != null) {
            JSONArray roomListArray = new JSONArray();
            for (RoomInfo value : this.roomList)
            {
                if (value != null) {
                    roomListArray.put(value.serialize());
                }
            }
            json.put("roomList", roomListArray);
        }
      
        return json;
    }
}
  