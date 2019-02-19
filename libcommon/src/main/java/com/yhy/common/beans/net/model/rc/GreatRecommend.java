// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;


import com.yhy.common.beans.net.model.comment.ComTagInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GreatRecommend implements Serializable{

    private static final long serialVersionUID = -809796686691916309L;
    /**
     * 记录id
     */
    public long id;
      
    /**
     * 用户或俱乐部基本信息
     */
    public OwnerInfo ownerInfo;
      
    /**
     * 原价
     */
    public long price;
      
    /**
     * 会员价
     */
    public long memberPrice;
      
    /**
     * 线路或活动标题
     */
    public String title;
      
    /**
     * 线路或活动图片
     */
    public String imgUrl;
    /**
     * 销量
     */
    public int sales = 10;
      
    /**
     * 线路或活动标签
     */
    public List<ComTagInfo> comTagList;
    /**
     * ACTIVITY 活动,REGULAR_LINE 普通线路,FLIGHT_HOTEL 机酒套餐,SCENIC_HOTEL 景酒套餐
     */
    public String type;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static GreatRecommend deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static GreatRecommend deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            GreatRecommend result = new GreatRecommend();
            
            // 记录id
            result.id = json.optLong("id");
            // 用户或俱乐部基本信息
            result.ownerInfo = OwnerInfo.deserialize(json.optJSONObject("ownerInfo"));
            // 原价
            result.price = json.optLong("price");
            // 会员价
            result.memberPrice = json.optLong("memberPrice");
            // 线路或活动标题
            
              if(!json.isNull("title")){
                  result.title = json.optString("title", null);
              }
            // 线路或活动图片
            
              if(!json.isNull("imgUrl")){
                  result.imgUrl = json.optString("imgUrl", null);
              }
            // 线路或活动标签
            JSONArray comTagListArray = json.optJSONArray("comTagList");
            if (comTagListArray != null) {
                int len = comTagListArray.length();
                result.comTagList = new ArrayList<ComTagInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = comTagListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.comTagList.add(ComTagInfo.deserialize(jo));
                    }
                }
            }
      
            // ACTIVITY 活动,REGULAR_LINE 普通线路,FLIGHT_HOTEL 机酒套餐,SCENIC_HOTEL 景酒套餐
            
              if(!json.isNull("type")){
                  result.type = json.optString("type", null);
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
        
        // 记录id
        json.put("id", this.id);
          
        // 用户或俱乐部基本信息
        if (this.ownerInfo != null) { json.put("ownerInfo", this.ownerInfo.serialize()); }
          
        // 原价
        json.put("price", this.price);
          
        // 会员价
        json.put("memberPrice", this.memberPrice);
          
        // 线路或活动标题
        if(this.title != null) { json.put("title", this.title); }
          
        // 线路或活动图片
        if(this.imgUrl != null) { json.put("imgUrl", this.imgUrl); }
          
        // 线路或活动标签 
        if (this.comTagList != null) {
            JSONArray comTagListArray = new JSONArray();
            for (ComTagInfo value : this.comTagList)
            {
                if (value != null) {
                    comTagListArray.put(value.serialize());
                }
            }
            json.put("comTagList", comTagListArray);
        }
      
        // ACTIVITY 活动,REGULAR_LINE 普通线路,FLIGHT_HOTEL 机酒套餐,SCENIC_HOTEL 景酒套餐
        if(this.type != null) { json.put("type", this.type); }
          
        return json;
    }
}
  