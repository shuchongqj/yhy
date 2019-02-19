// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TmRouteItemDetail implements Serializable{

    private static final long serialVersionUID = -2653511222171997629L;
    /**
     * 行程项id
     */
    public long id;
      
    /**
     * 行程项类型 BREAKFAST早餐/LUNCH午餐/DINNER晚餐/HOTEL酒店/SCENIC景点
     */
    public String type;
      
    /**
     * 名字 如怡心园
     */
    public String name;
      
    /**
     * 简介文字
     */
    public String shortDesc;
      
    /**
     * 图片集
     */
    public List<String> pics;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmRouteItemDetail deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmRouteItemDetail deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmRouteItemDetail result = new TmRouteItemDetail();
            
            // 行程项id
            result.id = json.optLong("id");
            // 行程项类型 BREAKFAST早餐/LUNCH午餐/DINNER晚餐/HOTEL酒店/SCENIC景点
            
              if(!json.isNull("type")){
                  result.type = json.optString("type", null);
              }
            // 名字 如怡心园
            
              if(!json.isNull("name")){
                  result.name = json.optString("name", null);
              }
            // 简介文字
            
              if(!json.isNull("shortDesc")){
                  result.shortDesc = json.optString("shortDesc", null);
              }
            // 图片集
            JSONArray picsArray = json.optJSONArray("pics");
            if (picsArray != null) {
                int len = picsArray.length();
                result.pics = new
                      ArrayList<String>(len);
                for (int i = 0; i < len; i++) {
                        
          if(!picsArray.isNull(i)){
              result.pics.add(picsArray.optString(i, null));
          }else{
              result.pics.add(i, null);
          }
          
                }
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
        
        // 行程项id
        json.put("id", this.id);
          
        // 行程项类型 BREAKFAST早餐/LUNCH午餐/DINNER晚餐/HOTEL酒店/SCENIC景点
        if(this.type != null) { json.put("type", this.type); }
          
        // 名字 如怡心园
        if(this.name != null) { json.put("name", this.name); }
          
        // 简介文字
        if(this.shortDesc != null) { json.put("shortDesc", this.shortDesc); }
          
        // 图片集 
        if (this.pics != null) {
            JSONArray picsArray = new JSONArray();
            for (String value : this.pics)
            {
                picsArray.put(value);
            }
            json.put("pics", picsArray);
        }
      
        return json;
    }
}
  