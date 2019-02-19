// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.comment;

import com.yhy.common.beans.net.model.club.SnsUserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductRateInfo implements Serializable{

    private static final long serialVersionUID = -1439779726220687223L;
    /**
     * 用户信息
     */
    public SnsUserInfo userInfo;
      
    /**
     * 评价id
     */
    public long id;
      
    /**
     * 评分
     */
    public long score;
      
    /**
     * 评价时间
     */
    public long gmtCreated;
      
    /**
     * 评价内容
     */
    public String content;
      
    /**
     * 评价图片
     */
    public List<String> picList;
    /**
     * 匿名评价
     */
    public String annoy;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ProductRateInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ProductRateInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ProductRateInfo result = new ProductRateInfo();
            
            // 用户信息
            result.userInfo = SnsUserInfo.deserialize(json.optJSONObject("userInfo"));
            // 评价id
            result.id = json.optLong("id");
            // 评分
            result.score = json.optLong("score");
            // 评价时间
            result.gmtCreated = json.optLong("gmtCreated");
            // 评价内容
            
              if(!json.isNull("content")){
                  result.content = json.optString("content", null);
              }
            // 评价图片
            JSONArray picListArray = json.optJSONArray("picList");
            if (picListArray != null) {
                int len = picListArray.length();
                result.picList = new
                      ArrayList<String>(len);
                for (int i = 0; i < len; i++) {
                        
          if(!picListArray.isNull(i)){
              result.picList.add(picListArray.optString(i, null));
          }else{
              result.picList.add(i, null);
          }
          
                }
            }
      
            // 匿名评价
            
              if(!json.isNull("annoy")){
                  result.annoy = json.optString("annoy", null);
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
        
        // 用户信息
        if (this.userInfo != null) { json.put("userInfo", this.userInfo.serialize()); }
          
        // 评价id
        json.put("id", this.id);
          
        // 评分
        json.put("score", this.score);
          
        // 评价时间
        json.put("gmtCreated", this.gmtCreated);
          
        // 评价内容
        if(this.content != null) { json.put("content", this.content); }
          
        // 评价图片 
        if (this.picList != null) {
            JSONArray picListArray = new JSONArray();
            for (String value : this.picList)
            {
                picListArray.put(value);
            }
            json.put("picList", picListArray);
        }
      
        // 匿名评价
        if(this.annoy != null) { json.put("annoy", this.annoy); }
          
        return json;
    }
}
  