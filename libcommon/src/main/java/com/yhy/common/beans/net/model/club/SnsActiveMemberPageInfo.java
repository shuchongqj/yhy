// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.club;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SnsActiveMemberPageInfo implements Serializable{

    private static final long serialVersionUID = -3132435551786085890L;
    /**
     * 成员信息列表
     */
    public List<ActiveMemberInfo> activeMemberList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SnsActiveMemberPageInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SnsActiveMemberPageInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SnsActiveMemberPageInfo result = new SnsActiveMemberPageInfo();
            
            // 成员信息列表
            JSONArray activeMemberListArray = json.optJSONArray("activeMemberList");
            if (activeMemberListArray != null) {
                int len = activeMemberListArray.length();
                result.activeMemberList = new ArrayList<ActiveMemberInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = activeMemberListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.activeMemberList.add(ActiveMemberInfo.deserialize(jo));
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
        
        // 成员信息列表 
        if (this.activeMemberList != null) {
            JSONArray activeMemberListArray = new JSONArray();
            for (ActiveMemberInfo value : this.activeMemberList)
            {
                if (value != null) {
                    activeMemberListArray.put(value.serialize());
                }
            }
            json.put("activeMemberList", activeMemberListArray);
        }
      
        return json;
    }
}
  