// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.member;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class PrivilegeInfo implements Serializable{

    private static final long serialVersionUID = -4000043732930187136L;
    /**
     * 特权名称
     */
    public String title;
      
    /**
     * 特权小logo
     */
    public String imageUrl;
      
    /**
     * 特权展示图片
     */
    public String imageShowUrl;
      
    /**
     * 特权描述信息
     */
    public String description;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static PrivilegeInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static PrivilegeInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            PrivilegeInfo result = new PrivilegeInfo();
            
            // 特权名称
            
              if(!json.isNull("title")){
                  result.title = json.optString("title", null);
              }
            // 特权小logo
            
              if(!json.isNull("imageUrl")){
                  result.imageUrl = json.optString("imageUrl", null);
              }
            // 特权展示图片
            
              if(!json.isNull("imageShowUrl")){
                  result.imageShowUrl = json.optString("imageShowUrl", null);
              }
            // 特权描述信息
            
              if(!json.isNull("description")){
                  result.description = json.optString("description", null);
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
        
        // 特权名称
        if(this.title != null) { json.put("title", this.title); }
          
        // 特权小logo
        if(this.imageUrl != null) { json.put("imageUrl", this.imageUrl); }
          
        // 特权展示图片
        if(this.imageShowUrl != null) { json.put("imageShowUrl", this.imageShowUrl); }
          
        // 特权描述信息
        if(this.description != null) { json.put("description", this.description); }
          
        return json;
    }
}
  