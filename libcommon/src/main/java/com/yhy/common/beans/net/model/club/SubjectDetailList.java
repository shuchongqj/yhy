// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.club;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SubjectDetailList implements Serializable{

    private static final long serialVersionUID = -2813476883731889652L;
    /**
     * 当前页码
     */
    public int pageNo;
      
    /**
     * 是否有下一页
     */
    public boolean hasNext;
      
    /**
     * 话题列表
     */
    public List<SubjectDetail> subjectDetailList;
    /**
     * 当前页面用户信息
     */
    public SnsUserInfo nowUser;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SubjectDetailList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SubjectDetailList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SubjectDetailList result = new SubjectDetailList();
            
            // 当前页码
            result.pageNo = json.optInt("pageNo");
            // 是否有下一页
            result.hasNext = json.optBoolean("hasNext");
            // 话题列表
            JSONArray subjectDetailListArray = json.optJSONArray("subjectDetailList");
            if (subjectDetailListArray != null) {
                int len = subjectDetailListArray.length();
                result.subjectDetailList = new ArrayList<SubjectDetail>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = subjectDetailListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.subjectDetailList.add(SubjectDetail.deserialize(jo));
                    }
                }
            }
      
            // 当前页面用户信息
            result.nowUser = SnsUserInfo.deserialize(json.optJSONObject("nowUser"));
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
          
        // 话题列表 
        if (this.subjectDetailList != null) {
            JSONArray subjectDetailListArray = new JSONArray();
            for (SubjectDetail value : this.subjectDetailList)
            {
                if (value != null) {
                    subjectDetailListArray.put(value.serialize());
                }
            }
            json.put("subjectDetailList", subjectDetailListArray);
        }
      
        // 当前页面用户信息
        if (this.nowUser != null) { json.put("nowUser", this.nowUser.serialize()); }
          
        return json;
    }
}
  