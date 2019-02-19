// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.member;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PrivilegeInfoPageList implements Serializable{

    private static final long serialVersionUID = -8484995311372302302L;
    /**
     * 特权列表
     */
    public List<PrivilegeInfo> privilegeInfoPageList;
    /**
     * 当前页码
     */
    public int pageNo;
      
    /**
     * 是否有下一页
     */
    public boolean hasNext;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static PrivilegeInfoPageList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static PrivilegeInfoPageList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            PrivilegeInfoPageList result = new PrivilegeInfoPageList();
            
            // 特权列表
            JSONArray privilegeInfoPageListArray = json.optJSONArray("privilegeInfoPageList");
            if (privilegeInfoPageListArray != null) {
                int len = privilegeInfoPageListArray.length();
                result.privilegeInfoPageList = new ArrayList<PrivilegeInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = privilegeInfoPageListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.privilegeInfoPageList.add(PrivilegeInfo.deserialize(jo));
                    }
                }
            }
      
            // 当前页码
            result.pageNo = json.optInt("pageNo");
            // 是否有下一页
            result.hasNext = json.optBoolean("hasNext");
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 特权列表 
        if (this.privilegeInfoPageList != null) {
            JSONArray privilegeInfoPageListArray = new JSONArray();
            for (PrivilegeInfo value : this.privilegeInfoPageList)
            {
                if (value != null) {
                    privilegeInfoPageListArray.put(value.serialize());
                }
            }
            json.put("privilegeInfoPageList", privilegeInfoPageListArray);
        }
      
        // 当前页码
        json.put("pageNo", this.pageNo);
          
        // 是否有下一页
        json.put("hasNext", this.hasNext);
          
        return json;
    }
}
  