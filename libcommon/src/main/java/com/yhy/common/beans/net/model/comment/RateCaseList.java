// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RateCaseList implements Serializable{

    private static final long serialVersionUID = 9058674688427372919L;
    /**
     * 评价条件列表
     */
    public List<RateCaseInfo> rateCaseList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static RateCaseList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static RateCaseList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            RateCaseList result = new RateCaseList();
            
            // 评价条件列表
            JSONArray rateCaseListArray = json.optJSONArray("rateCaseList");
            if (rateCaseListArray != null) {
                int len = rateCaseListArray.length();
                result.rateCaseList = new ArrayList<RateCaseInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = rateCaseListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.rateCaseList.add(RateCaseInfo.deserialize(jo));
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
        
        // 评价条件列表 
        if (this.rateCaseList != null) {
            JSONArray rateCaseListArray = new JSONArray();
            for (RateCaseInfo value : this.rateCaseList)
            {
                if (value != null) {
                    rateCaseListArray.put(value.serialize());
                }
            }
            json.put("rateCaseList", rateCaseListArray);
        }
      
        return json;
    }
}
  