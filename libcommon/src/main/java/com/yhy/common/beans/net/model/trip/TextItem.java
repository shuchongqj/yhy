// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TextItem implements Serializable{

    private static final long serialVersionUID = 8470803866087576269L;
    /**
     * 标题
     */
    public String title;
      
    /**
     * 图片url 可选
     */
    public String pic_url;
      
    /**
     * 内容
     */
    public String content;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TextItem deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TextItem deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TextItem result = new TextItem();
            
            // 标题
            
              if(!json.isNull("title")){
                  result.title = json.optString("title", null);
              }
            // 图片url 可选
            
              if(!json.isNull("pic_url")){
                  result.pic_url = json.optString("pic_url", null);
              }
            // 内容
            
              if(!json.isNull("content")){
                  result.content = json.optString("content", null);
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
        
        // 标题
        if(this.title != null) { json.put("title", this.title); }
          
        // 图片url 可选
        if(this.pic_url != null) { json.put("pic_url", this.pic_url); }
          
        // 内容
        if(this.content != null) { json.put("content", this.content); }
          
        return json;
    }
}
  