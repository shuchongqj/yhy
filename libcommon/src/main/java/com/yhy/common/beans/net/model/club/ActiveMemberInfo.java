// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.club;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ActiveMemberInfo implements Serializable{

    private static final long serialVersionUID = 7832178712008467985L;
    /**
     * 主键id
     */
    public long id;
      
    /**
     * 活动id
     */
    public long actityId;
      
    /**
     * 成员id
     */
    public long createId;
      
    /**
     * 成员姓名
     */
    public String userName;
      
    /**
     * 成员头像
     */
    public String userPhoto;

    /**
     * 性别
     * INVALID_GENDER("未确认",1), MALE("男",2), FEMALE("女",3);
     */
    public String gender;

    /**
     * 加入时间
     */
    public long gmtCreated;
      
    /**
     * 编辑时间
     */
    public long gmtModified;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ActiveMemberInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ActiveMemberInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ActiveMemberInfo result = new ActiveMemberInfo();
            
            // 主键id
            result.id = json.optLong("id");
            // 活动id
            result.actityId = json.optLong("actityId");
            // 成员id
            result.createId = json.optLong("createId");
            // 成员姓名
            
              if(!json.isNull("userName")){
                  result.userName = json.optString("userName", null);
              }
            // 成员头像
            
              if(!json.isNull("userPhoto")){
                  result.userPhoto = json.optString("userPhoto", null);
              }
            // 加入时间
            result.gmtCreated = json.optLong("gmtCreated");
            // 编辑时间
            result.gmtModified = json.optLong("gmtModified");
            //性别
            result.gender = json.optString("gender");
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 主键id
        json.put("id", this.id);
          
        // 活动id
        json.put("actityId", this.actityId);
          
        // 成员id
        json.put("createId", this.createId);
          
        // 成员姓名
        if(this.userName != null) { json.put("userName", this.userName); }
          
        // 成员头像
        if(this.userPhoto != null) { json.put("userPhoto", this.userPhoto); }
          
        // 加入时间
        json.put("gmtCreated", this.gmtCreated);
          
        // 编辑时间
        json.put("gmtModified", this.gmtModified);

        json.put("gender", this.gender);
        return json;
    }
}
  