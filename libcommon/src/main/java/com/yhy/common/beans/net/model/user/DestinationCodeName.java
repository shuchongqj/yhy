// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.user;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class DestinationCodeName implements Serializable{

    private static final long serialVersionUID = 5156763520749896382L;
    /**
     * 编码
     */
    public int code;
      
    /**
     * 名称
     */
    public String name;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static DestinationCodeName deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static DestinationCodeName deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            DestinationCodeName result = new DestinationCodeName();
            
            // 编码
            result.code = json.optInt("code");
            // 名称
            
              if(!json.isNull("name")){
                  result.name = json.optString("name", null);
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
        
        // 编码
        json.put("code", this.code);
          
        // 名称
        if(this.name != null) { json.put("name", this.name); }
          
        return json;
    }
}
  