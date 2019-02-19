// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NeedKnow implements Serializable {

    private static final long serialVersionUID = -5873190383479996781L;
    /**
     * 首页购买须知
     */
    public List<TextItem> frontNeedKnow;
    /**
     * 更多购买须知文件路径
     */
    public String extraInfoUrl;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static NeedKnow deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static NeedKnow deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            NeedKnow result = new NeedKnow();
            
            // 首页购买须知
            JSONArray frontNeedKnowArray = json.optJSONArray("frontNeedKnow");
            if (frontNeedKnowArray != null) {
                int len = frontNeedKnowArray.length();
                result.frontNeedKnow = new ArrayList<TextItem>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = frontNeedKnowArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.frontNeedKnow.add(TextItem.deserialize(jo));
                    }
                }
            }
      
            // 更多购买须知文件路径
            
              if(!json.isNull("extraInfoUrl")){
                  result.extraInfoUrl = json.optString("extraInfoUrl", null);
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
        
        // 首页购买须知 
        if (this.frontNeedKnow != null) {
            JSONArray frontNeedKnowArray = new JSONArray();
            for (TextItem value : this.frontNeedKnow)
            {
                if (value != null) {
                    frontNeedKnowArray.put(value.serialize());
                }
            }
            json.put("frontNeedKnow", frontNeedKnowArray);
        }
      
        // 更多购买须知文件路径
        if(this.extraInfoUrl != null) { json.put("extraInfoUrl", this.extraInfoUrl); }
          
        return json;
    }
}
  