// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.pedometer;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TrackInvitationResult implements Serializable{

    private static final long serialVersionUID = -2856009139857821063L;
    /**
     * 推广url
     */
    public String invUrl;
      
    /**
     * 手机号是否已被邀请过
     */
    public boolean invFlag;
      
    /**
     * 是否邀请
     */
    public boolean invite;
      
    /**
     * 邀请好友人数
     */
    public int count;
      
    /**
     * 每日邀请好友人数
     */
    public int countDay;
      
    /**
     * 调用接口是否成功
     */
    public boolean success;
      
    /**
     * 调用接口失败原因
     */
    public String errorMessage;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TrackInvitationResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TrackInvitationResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TrackInvitationResult result = new TrackInvitationResult();
            
            // 推广url
            
              if(!json.isNull("invUrl")){
                  result.invUrl = json.optString("invUrl", null);
              }
            // 手机号是否已被邀请过
            result.invFlag = json.optBoolean("invFlag");
            // 是否邀请
            result.invite = json.optBoolean("invite");
            // 邀请好友人数
            result.count = json.optInt("count");
            // 每日邀请好友人数
            result.countDay = json.optInt("countDay");
            // 调用接口是否成功
            result.success = json.optBoolean("success");
            // 调用接口失败原因
            
              if(!json.isNull("errorMessage")){
                  result.errorMessage = json.optString("errorMessage", null);
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
        
        // 推广url
        if(this.invUrl != null) { json.put("invUrl", this.invUrl); }
          
        // 手机号是否已被邀请过
        json.put("invFlag", this.invFlag);
          
        // 是否邀请
        json.put("invite", this.invite);
          
        // 邀请好友人数
        json.put("count", this.count);
          
        // 每日邀请好友人数
        json.put("countDay", this.countDay);
          
        // 调用接口是否成功
        json.put("success", this.success);
          
        // 调用接口失败原因
        if(this.errorMessage != null) { json.put("errorMessage", this.errorMessage); }
          
        return json;
    }
}
  