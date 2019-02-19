// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ComTagInfoList implements Serializable{

    private static final long serialVersionUID = -3067826877851762789L;
    /**
     * 当前页码
     */
    public int pageNo;
      
    /**
     * 是否有下一页
     */
    public boolean hasNext;
      
    /**
     * 标签信息
     */
    public List<ComTagInfo> tagInfoList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ComTagInfoList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ComTagInfoList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ComTagInfoList result = new ComTagInfoList();
            
            // 当前页码
            result.pageNo = json.optInt("pageNo");
            // 是否有下一页
            result.hasNext = json.optBoolean("hasNext");
            // 标签信息
            JSONArray tagInfoListArray = json.optJSONArray("tagInfoList");
            if (tagInfoListArray != null) {
                int len = tagInfoListArray.length();
                result.tagInfoList = new ArrayList<ComTagInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = tagInfoListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.tagInfoList.add(ComTagInfo.deserialize(jo));
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
        
        // 当前页码
        json.put("pageNo", this.pageNo);
          
        // 是否有下一页
        json.put("hasNext", this.hasNext);
          
        // 标签信息 
        if (this.tagInfoList != null) {
            JSONArray tagInfoListArray = new JSONArray();
            for (ComTagInfo value : this.tagInfoList)
            {
                if (value != null) {
                    tagInfoListArray.put(value.serialize());
                }
            }
            json.put("tagInfoList", tagInfoListArray);
        }
      
        return json;
    }
}
  