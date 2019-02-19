// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.comment;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class CommetDetailInfo implements Serializable{

    private static final long serialVersionUID = 9147065952851871428L;
    /**
     * 外部id
     */
    public long outId;
      
    /**
     * 评论人id A
     */
    public long userId;
      
    /**
     * 回复某用户id A的值
     */
    public long replyToUserId;
      
    /**
     * 回复的文字
     */
    public String textContent;
      
    /**
     * 类型
     */
    public String outType;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static CommetDetailInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static CommetDetailInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            CommetDetailInfo result = new CommetDetailInfo();
            
            // 外部id
            result.outId = json.optLong("outId");
            // 评论人id A
            result.userId = json.optLong("userId");
            // 回复某用户id A的值
            result.replyToUserId = json.optLong("replyToUserId");
            // 回复的文字
            
              if(!json.isNull("textContent")){
                  result.textContent = json.optString("textContent", null);
              }
            // 类型
            
              if(!json.isNull("outType")){
                  result.outType = json.optString("outType", null);
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
        
        // 外部id
        json.put("outId", this.outId);
          
        // 评论人id A
        json.put("userId", this.userId);
          
        // 回复某用户id A的值
        json.put("replyToUserId", this.replyToUserId);
          
        // 回复的文字
        if(this.textContent != null) { json.put("textContent", this.textContent); }
          
        // 类型
        if(this.outType != null) { json.put("outType", this.outType); }
          
        return json;
    }
}
  