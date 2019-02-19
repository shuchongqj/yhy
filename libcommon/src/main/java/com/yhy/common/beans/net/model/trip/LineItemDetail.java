// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;



import com.yhy.common.beans.net.model.tm.TmRouteDayInfo;
import com.yhy.common.beans.net.model.user.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LineItemDetail implements Serializable{

    private static final long serialVersionUID = -687423535545638796L;
    /**
     * 商品信息
     */
    public ItemVO itemVO;
      
    /**
     * 线路id
     */
    public long id;
      
    /**
     * 游咖信息/商家信息
     */
    public UserInfo userInfo;
      
    /**
     * 线路主题
     */
    public List<TagInfo> tags;
    /**
     * 线路出发地
     */
    public List<CityInfo> departs;
    /**
     * 线路目的地
     */
    public List<CityInfo> dests;
    /**
     * 行程计划
     */
    public RoutePlanVO routePlan;
      
    /**
     * 每日行程列表
     */
    public List<TmRouteDayInfo> route;
    /**
     * 购买须知
     */
    public NeedKnow needKnow;
      
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static LineItemDetail deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }
    
    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static LineItemDetail deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            LineItemDetail result = new LineItemDetail();
            
            // 商品信息
            result.itemVO = ItemVO.deserialize(json.optJSONObject("itemVO"));
            // 线路id
            result.id = json.optLong("id");
            // 游咖信息/商家信息
            result.userInfo = UserInfo.deserialize(json.optJSONObject("userInfo"));
            // 线路主题
            JSONArray tagsArray = json.optJSONArray("tags");
            if (tagsArray != null) {
                int len = tagsArray.length();
                result.tags = new ArrayList<TagInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = tagsArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.tags.add(TagInfo.deserialize(jo));
                    }
                }
            }
      
            // 线路出发地
            JSONArray departsArray = json.optJSONArray("departs");
            if (departsArray != null) {
                int len = departsArray.length();
                result.departs = new ArrayList<CityInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = departsArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.departs.add(CityInfo.deserialize(jo));
                    }
                }
            }
      
            // 线路目的地
            JSONArray destsArray = json.optJSONArray("dests");
            if (destsArray != null) {
                int len = destsArray.length();
                result.dests = new ArrayList<CityInfo>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = destsArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.dests.add(CityInfo.deserialize(jo));
                    }
                }
            }
      
            // 行程计划
            result.routePlan = RoutePlanVO.deserialize(json.optJSONObject("routePlan"));
            // 每日行程列表
            JSONArray routeArray = json.optJSONArray("route");
            if (routeArray != null) {
                int len = routeArray.length();
                result.route = new ArrayList<>(len);
                for (int i = 0; i < len; i++) {
                        JSONObject jo = routeArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.route.add(TmRouteDayInfo.deserialize(jo));
                    }
                }
            }
      
            // 购买须知
            result.needKnow = NeedKnow.deserialize(json.optJSONObject("needKnow"));
            return result;
        }
        return null;
    }
    
    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        
        // 商品信息
        if (this.itemVO != null) { json.put("itemVO", this.itemVO.serialize()); }
          
        // 线路id
        json.put("id", this.id);
          
        // 游咖信息/商家信息
        if (this.userInfo != null) { json.put("userInfo", this.userInfo.serialize()); }
          
        // 线路主题 
        if (this.tags != null) {
            JSONArray tagsArray = new JSONArray();
            for (TagInfo value : this.tags)
            {
                if (value != null) {
                    tagsArray.put(value.serialize());
                }
            }
            json.put("tags", tagsArray);
        }
      
        // 线路出发地 
        if (this.departs != null) {
            JSONArray departsArray = new JSONArray();
            for (CityInfo value : this.departs)
            {
                if (value != null) {
                    departsArray.put(value.serialize());
                }
            }
            json.put("departs", departsArray);
        }
      
        // 线路目的地 
        if (this.dests != null) {
            JSONArray destsArray = new JSONArray();
            for (CityInfo value : this.dests)
            {
                if (value != null) {
                    destsArray.put(value.serialize());
                }
            }
            json.put("dests", destsArray);
        }
      
        // 行程计划
        if (this.routePlan != null) { json.put("routePlan", this.routePlan.serialize()); }
          
        // 每日行程列表 
        if (this.route != null) {
            JSONArray routeArray = new JSONArray();
            for (TmRouteDayInfo value : this.route)
            {
                if (value != null) {
                    routeArray.put(value.serialize());
                }
            }
            json.put("route", routeArray);
        }
      
        // 购买须知
        if (this.needKnow != null) { json.put("needKnow", this.needKnow.serialize()); }
          
        return json;
    }
}
  