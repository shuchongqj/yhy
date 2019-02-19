// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SupportUserInfoList implements Serializable{

    private static final long serialVersionUID = -3076982181936347022L;
    /**
     * 当前页码
     */
    public int pageNo;
      
    /**
     * 是否有下一页
     */
    public boolean hasNext;
      
    /**
     * 用户信息
     */
    public List<SupportUserInfo> supportUserInfoList;
    /**
     * 数量
     */
    public int count;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SupportUserInfoList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SupportUserInfoList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SupportUserInfoList result = new SupportUserInfoList();
            
            // 当前页码
            result.pageNo = json.optInt("pageNo");
            // 是否有下一页
            result.hasNext = json.optBoolean("hasNext");
            // 用户信息
            JSONArray supportUserInfoListArray = json.optJSONArray("supportUserInfoList");
            if (supportUserInfoListArray != null) {
                int len = supportUserInfoListArray.length();
                result.supportUserInfoList = new ArrayList<SupportUserInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = supportUserInfoListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.supportUserInfoList.add(SupportUserInfo.deserialize(jo));
                    }
                }
            }
      
            // 数量
            result.count = json.optInt("count");
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 当前页码
        json.put("pageNo", this.pageNo);
          
        // 是否有下一页
        json.put("hasNext", this.hasNext);
          
        // 用户信息 
        if (this.supportUserInfoList != null) {
            JSONArray supportUserInfoListArray = new JSONArray();
            for (SupportUserInfo value : this.supportUserInfoList)
            {
                if (value != null) {
                    supportUserInfoListArray.put(value.serialize());
                }
            }
            json.put("supportUserInfoList", supportUserInfoListArray);
        }
      
        // 数量
        json.put("count", this.count);
          
        return json;
    }
}
  