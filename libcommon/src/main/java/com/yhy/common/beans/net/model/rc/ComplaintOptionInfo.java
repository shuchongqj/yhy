// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ComplaintOptionInfo implements Serializable{

    private static final long serialVersionUID = -6847326314581798589L;
    /**
     * 选项id
     */
    public long id;
      
    /**
     * 选项名称
     */
    public String title;

    public boolean isChecked = false;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ComplaintOptionInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ComplaintOptionInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ComplaintOptionInfo result = new ComplaintOptionInfo();
            
            // 选项id
            result.id = json.optLong("id");
            // 选项名称
            
              if(!json.isNull("title")){
                  result.title = json.optString("title", null);
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
        
        // 选项id
        json.put("id", this.id);
          
        // 选项名称
        if(this.title != null) { json.put("title", this.title); }
          
        return json;
    }
}
  