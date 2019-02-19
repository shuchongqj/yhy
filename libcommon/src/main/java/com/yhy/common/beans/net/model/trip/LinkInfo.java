// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class LinkInfo implements Serializable {

    private static final long serialVersionUID = 4392100500113062397L;
    /**
     * 跳转类型
     */
    public String link_type;
      
    /**
     * 目的地url
     */
    public String link_url;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static LinkInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static LinkInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            LinkInfo result = new LinkInfo();
            
            // 跳转类型
            
              if(!json.isNull("link_type")){
                  result.link_type = json.optString("link_type", null);
              }
            // 目的地url
            
              if(!json.isNull("link_url")){
                  result.link_url = json.optString("link_url", null);
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
        
        // 跳转类型
        if(this.link_type != null) { json.put("link_type", this.link_type); }
          
        // 目的地url
        if(this.link_url != null) { json.put("link_url", this.link_url); }
          
        return json;
    }
}
  