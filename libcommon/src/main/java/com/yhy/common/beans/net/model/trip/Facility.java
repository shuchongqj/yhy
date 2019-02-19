// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Facility implements Serializable{

    private static final long serialVersionUID = 7851663675307759756L;
    /**
     * 名称
     */
    public String name;
      
    /**
     * 图片url
     */
    public String pic_url;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Facility deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Facility deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            Facility result = new Facility();
            
            // 名称
            
              if(!json.isNull("name")){
                  result.name = json.optString("name", null);
              }
            // 图片url
            
              if(!json.isNull("pic_url")){
                  result.pic_url = json.optString("pic_url", null);
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
        
        // 名称
        if(this.name != null) { json.put("name", this.name); }
          
        // 图片url
        if(this.pic_url != null) { json.put("pic_url", this.pic_url); }
          
        return json;
    }
}
  