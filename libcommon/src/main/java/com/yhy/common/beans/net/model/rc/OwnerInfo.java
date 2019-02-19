// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class OwnerInfo implements Serializable{

    private static final long serialVersionUID = -5687636918808374935L;
    /**
     * 用户或俱乐部id
     */
    public long ownerId;
      
    /**
     * 用户或俱乐部头像
     */
    public String ownerLogo;
      
    /**
     * 用户或俱乐部名称
     */
    public String ownerName;
      
    /**
     * 用户或俱乐部简介
     */
    public String ownerDesc;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static OwnerInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static OwnerInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            OwnerInfo result = new OwnerInfo();
            
            // 用户或俱乐部id
            result.ownerId = json.optLong("ownerId");
            // 用户或俱乐部头像
            
              if(!json.isNull("ownerLogo")){
                  result.ownerLogo = json.optString("ownerLogo", null);
              }
            // 用户或俱乐部名称
            
              if(!json.isNull("ownerName")){
                  result.ownerName = json.optString("ownerName", null);
              }
            // 用户或俱乐部简介
            
              if(!json.isNull("ownerDesc")){
                  result.ownerDesc = json.optString("ownerDesc", null);
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
        
        // 用户或俱乐部id
        json.put("ownerId", this.ownerId);
          
        // 用户或俱乐部头像
        if(this.ownerLogo != null) { json.put("ownerLogo", this.ownerLogo); }
          
        // 用户或俱乐部名称
        if(this.ownerName != null) { json.put("ownerName", this.ownerName); }
          
        // 用户或俱乐部简介
        if(this.ownerDesc != null) { json.put("ownerDesc", this.ownerDesc); }
          
        return json;
    }
}
  