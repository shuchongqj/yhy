// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.comment;

import com.yhy.common.beans.net.model.club.SnsUserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class CommentInfo implements Serializable{

    private static final long serialVersionUID = 6028670229392919344L;
    /**
     * id
     */
    public long id;
      
    /**
     * 回复id
     */
    public long replyToUserId;
      
    /**
     * 创建者用户信息（A回复B中的A）
     */
    public SnsUserInfo createUserInfo;
      
    /**
     * 回复给的用户信息（A回复B中的B）
     */
    public SnsUserInfo replyToUserInfo;
      
    /**
     * 回复的文字
     */
    public String textContent;
      
    /**
     * 创建时间
     */
    public long gmtCreated;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static CommentInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static CommentInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            CommentInfo result = new CommentInfo();
            
            // id
            result.id = json.optLong("id");
            // 回复id
            result.replyToUserId = json.optLong("replyToUserId");
            // 创建者用户信息（A回复B中的A）
            result.createUserInfo = SnsUserInfo.deserialize(json.optJSONObject("createUserInfo"));
            // 回复给的用户信息（A回复B中的B）
            result.replyToUserInfo = SnsUserInfo.deserialize(json.optJSONObject("replyToUserInfo"));
            // 回复的文字
            
              if(!json.isNull("textContent")){
                  result.textContent = json.optString("textContent", null);
              }
            // 创建时间
            result.gmtCreated = json.optLong("gmtCreated");
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
          
        // 回复id
        json.put("replyToUserId", this.replyToUserId);
          
        // 创建者用户信息（A回复B中的A）
        if (this.createUserInfo != null) { json.put("createUserInfo", this.createUserInfo.serialize()); }
          
        // 回复给的用户信息（A回复B中的B）
        if (this.replyToUserInfo != null) { json.put("replyToUserInfo", this.replyToUserInfo.serialize()); }
          
        // 回复的文字
        if(this.textContent != null) { json.put("textContent", this.textContent); }
          
        // 创建时间
        json.put("gmtCreated", this.gmtCreated);
          
        return json;
    }
}
  