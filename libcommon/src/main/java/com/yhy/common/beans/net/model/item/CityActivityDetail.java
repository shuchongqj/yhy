// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.item;

import com.yhy.common.beans.net.model.tm.TmRouteDayInfo;
import com.yhy.common.beans.net.model.trip.ItemVO;
import com.yhy.common.beans.net.model.trip.NeedKnow;
import com.yhy.common.beans.net.model.trip.TagInfo;
import com.yhy.common.beans.net.model.user.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CityActivityDetail implements Serializable {

    private static final long serialVersionUID = 386571453109195366L;
    /**
     * 商品信息
     */
    public ItemVO itemVO;

    /**
     * 同城活动id
     */
    public long id;

    /**
     * 同城活动所有主题标签列表
     */
    public List<TagInfo> tags;
    /**
     * 点赞状态 AVAILABLE:已赞/DELETED未赞 保留字段
     */
    public String likeStatus;

    /**
     * 点赞数 保留字段
     */
    public int likes;

    /**
     * 每日行程列表
     */
    public List<TmRouteDayInfo> route;
    /**
     * 购买须知
     */
    public NeedKnow needKnow;

    /**
     * 卖家信息
     */
    public UserInfo userInfo;

    /**
     * 详细地址
     */
    public String locationText;

    /**
     * 目的地经度
     */
    public double longitude;

    /**
     * 目的地纬度
     */
    public double latitude;



    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static CityActivityDetail deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static CityActivityDetail deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            CityActivityDetail result = new CityActivityDetail();

            // 商品信息
            result.itemVO = ItemVO.deserialize(json.optJSONObject("itemVO"));
            // 同城活动id
            result.id = json.optLong("id");
            // 详细地址
            if(!json.isNull("locationText")){
                result.locationText = json.optString("locationText", null);
            }
            // 目的地经度
            result.longitude = json.optDouble("longitude");
            // 目的地纬度
            result.latitude = json.optDouble("latitude");
            // 同城活动所有主题标签列表
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

            // 点赞状态 AVAILABLE:已赞/DELETED未赞 保留字段

            if (!json.isNull("likeStatus")) {
                result.likeStatus = json.optString("likeStatus", null);
            }
            // 点赞数 保留字段
            result.likes = json.optInt("likes");
            // 每日行程列表
            JSONArray routeArray = json.optJSONArray("route");
            if (routeArray != null) {
                int len = routeArray.length();
                result.route = new ArrayList<TmRouteDayInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = routeArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.route.add(TmRouteDayInfo.deserialize(jo));
                    }
                }
            }

            // 购买须知
            result.needKnow = NeedKnow.deserialize(json.optJSONObject("needKnow"));
            // 卖家信息
            result.userInfo = UserInfo.deserialize(json.optJSONObject("userInfo"));
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
        if (this.itemVO != null) {
            json.put("itemVO", this.itemVO.serialize());
        }

        // 同城活动id
        json.put("id", this.id);

        // 详细地址
        if(this.locationText != null) { json.put("locationText", this.locationText); }

        // 目的地经度
        json.put("longitude", this.longitude);

        // 目的地纬度
        json.put("latitude", this.latitude);


        // 同城活动所有主题标签列表 
        if (this.tags != null) {
            JSONArray tagsArray = new JSONArray();
            for (TagInfo value : this.tags) {
                if (value != null) {
                    tagsArray.put(value.serialize());
                }
            }
            json.put("tags", tagsArray);
        }

        // 点赞状态 AVAILABLE:已赞/DELETED未赞 保留字段
        if (this.likeStatus != null) {
            json.put("likeStatus", this.likeStatus);
        }

        // 点赞数 保留字段
        json.put("likes", this.likes);

        // 每日行程列表 
        if (this.route != null) {
            JSONArray routeArray = new JSONArray();
            for (TmRouteDayInfo value : this.route) {
                if (value != null) {
                    routeArray.put(value.serialize());
                }
            }
            json.put("route", routeArray);
        }

        // 购买须知
        if (this.needKnow != null) {
            json.put("needKnow", this.needKnow.serialize());
        }

        // 卖家信息
        if (this.userInfo != null) {
            json.put("userInfo", this.userInfo.serialize());
        }
        return json;
    }
}
  