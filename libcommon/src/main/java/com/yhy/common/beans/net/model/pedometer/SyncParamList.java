// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.pedometer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SyncParamList implements Serializable{

    private static final long serialVersionUID = 6313088056953302058L;
    /**
     * 步数同步参数list
     */
    public List<SyncParam> syncParamList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SyncParamList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SyncParamList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SyncParamList result = new SyncParamList();
            
            // 步数同步参数list
            JSONArray syncParamListArray = json.optJSONArray("syncParamList");
            if (syncParamListArray != null) {
                int len = syncParamListArray.length();
                result.syncParamList = new ArrayList<SyncParam>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = syncParamListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.syncParamList.add(SyncParam.deserialize(jo));
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
        
        // 步数同步参数list 
        if (this.syncParamList != null) {
            JSONArray syncParamListArray = new JSONArray();
            for (SyncParam value : this.syncParamList)
            {
                if (value != null) {
                    syncParamListArray.put(value.serialize());
                }
            }
            json.put("syncParamList", syncParamListArray);
        }
      
        return json;
    }
}
  