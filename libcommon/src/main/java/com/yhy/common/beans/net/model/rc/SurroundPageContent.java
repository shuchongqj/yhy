// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class SurroundPageContent implements Serializable{

    private static final long serialVersionUID = -5874667903665807444L;
    /**
     * 俱乐部类型
     */
    public BaseColumn clubCategoryColumn;
      
    /**
     * 热门周边
     */
    public BestDestColumn hotSurroundColumn;
      
    /**
     * 精彩活动
     */
    public ActivityInfoColum activityInfoColumn;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SurroundPageContent deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SurroundPageContent deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SurroundPageContent result = new SurroundPageContent();
            
            // 周边活动类型
            result.clubCategoryColumn = BaseColumn.deserialize(json.optJSONObject("clubCategoryColumn"));
            // 热门周边
            result.hotSurroundColumn = BestDestColumn.deserialize(json.optJSONObject("hotSurroundColumn"));
            // 精彩活动
            result.activityInfoColumn = ActivityInfoColum.deserialize(json.optJSONObject("activityInfoColumn"));
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 周边活动类型
        if (this.clubCategoryColumn != null) { json.put("clubCategoryColumn", this.clubCategoryColumn.serialize()); }
          
        // 热门周边
        if (this.hotSurroundColumn != null) { json.put("hotSurroundColumn", this.hotSurroundColumn.serialize()); }
          
        // 精彩活动
        if (this.activityInfoColumn != null) { json.put("activityInfoColumn", this.activityInfoColumn.serialize()); }
          
        return json;
    }
}
  