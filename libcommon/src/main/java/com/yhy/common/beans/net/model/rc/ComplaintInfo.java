// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ComplaintInfo implements Serializable{

    private static final long serialVersionUID = 5996960044804889433L;
    /**
     * id
     */
    public long id;
      
    /**
     * 投诉类型，动态的投诉类型：SUBJECT
     */
    public String outType;
      
    /**
     * 被投诉的内容的id，比如动态，就是动态的id
     */
    public long outId;
      
    /**
     * 被投诉的内容概要，比如动态，就把动态的文字写进来
     */
    public String outContent;
      
    /**
     * 被投诉的图片地址，比如动态，就把动态的图片地址写进来
     */
    public List<String> outPicUrls;
    /**
     * 投诉原因id
     */
    public long optionId;
      
    /**
     * 投诉原因
     */
    public String reasonContent;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ComplaintInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ComplaintInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ComplaintInfo result = new ComplaintInfo();
            
            // id
            result.id = json.optLong("id");
            // 投诉类型，动态的投诉类型：SUBJECT
            
              if(!json.isNull("outType")){
                  result.outType = json.optString("outType", null);
              }
            // 被投诉的内容的id，比如动态，就是动态的id
            result.outId = json.optLong("outId");
            // 被投诉的内容概要，比如动态，就把动态的文字写进来
            
              if(!json.isNull("outContent")){
                  result.outContent = json.optString("outContent", null);
              }
            // 被投诉的图片地址，比如动态，就把动态的图片地址写进来
            JSONArray outPicUrlsArray = json.optJSONArray("outPicUrls");
            if (outPicUrlsArray != null) {
                int len = outPicUrlsArray.length();
                result.outPicUrls = new
                      ArrayList<String>(len);
                for (int i = 0; i < len; i++) {
                        
          if(!outPicUrlsArray.isNull(i)){
              result.outPicUrls.add(outPicUrlsArray.optString(i, null));
          }else{
              result.outPicUrls.add(i, null);
          }
          
                }
            }
      
            // 投诉原因id
            result.optionId = json.optLong("optionId");
            // 投诉原因
            
              if(!json.isNull("reasonContent")){
                  result.reasonContent = json.optString("reasonContent", null);
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
        
        // id
        json.put("id", this.id);
          
        // 投诉类型，动态的投诉类型：SUBJECT
        if(this.outType != null) { json.put("outType", this.outType); }
          
        // 被投诉的内容的id，比如动态，就是动态的id
        json.put("outId", this.outId);
          
        // 被投诉的内容概要，比如动态，就把动态的文字写进来
        if(this.outContent != null) { json.put("outContent", this.outContent); }
          
        // 被投诉的图片地址，比如动态，就把动态的图片地址写进来 
        if (this.outPicUrls != null) {
            JSONArray outPicUrlsArray = new JSONArray();
            for (String value : this.outPicUrls)
            {
                outPicUrlsArray.put(value);
            }
            json.put("outPicUrls", outPicUrlsArray);
        }
      
        // 投诉原因id
        json.put("optionId", this.optionId);
          
        // 投诉原因
        if(this.reasonContent != null) { json.put("reasonContent", this.reasonContent); }
          
        return json;
    }
}
  