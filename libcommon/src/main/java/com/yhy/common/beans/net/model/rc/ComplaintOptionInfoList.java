// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ComplaintOptionInfoList implements Serializable{

    private static final long serialVersionUID = 5192495887651033785L;
    /**
     * 选项
     */
    public List<ComplaintOptionInfo> complaintOptionInfos;
    /**
     * 一页多少
     */
    public int pageSize;
      
    /**
     * 第几页
     */
    public int pageNo;
      
    /**
     * 第几页
     */
    public int totalCount;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ComplaintOptionInfoList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ComplaintOptionInfoList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ComplaintOptionInfoList result = new ComplaintOptionInfoList();
            
            // 选项
            JSONArray complaintOptionInfosArray = json.optJSONArray("complaintOptionInfos");
            if (complaintOptionInfosArray != null) {
                int len = complaintOptionInfosArray.length();
                result.complaintOptionInfos = new ArrayList<ComplaintOptionInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = complaintOptionInfosArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.complaintOptionInfos.add(ComplaintOptionInfo.deserialize(jo));
                    }
                }
            }
      
            // 一页多少
            result.pageSize = json.optInt("pageSize");
            // 第几页
            result.pageNo = json.optInt("pageNo");
            // 第几页
            result.totalCount = json.optInt("totalCount");
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 选项 
        if (this.complaintOptionInfos != null) {
            JSONArray complaintOptionInfosArray = new JSONArray();
            for (ComplaintOptionInfo value : this.complaintOptionInfos)
            {
                if (value != null) {
                    complaintOptionInfosArray.put(value.serialize());
                }
            }
            json.put("complaintOptionInfos", complaintOptionInfosArray);
        }
      
        // 一页多少
        json.put("pageSize", this.pageSize);
          
        // 第几页
        json.put("pageNo", this.pageNo);
          
        // 第几页
        json.put("totalCount", this.totalCount);
          
        return json;
    }
}
  