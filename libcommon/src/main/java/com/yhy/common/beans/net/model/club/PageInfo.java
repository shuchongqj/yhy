// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.club;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class PageInfo implements Serializable{

    private static final long serialVersionUID = 7560424622808093765L;
    /**
     * 页码
     */
    public int pageNo;
      
    /**
     * 每页大小
     */
    public int pageSize;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static PageInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static PageInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            PageInfo result = new PageInfo();
            
            // 页码
            result.pageNo = json.optInt("pageNo");
            // 每页大小
            result.pageSize = json.optInt("pageSize");
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 页码
        json.put("pageNo", this.pageNo);
          
        // 每页大小
        json.put("pageSize", this.pageSize);
          
        return json;
    }
}
  