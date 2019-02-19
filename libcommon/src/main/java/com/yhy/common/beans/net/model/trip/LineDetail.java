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

public class LineDetail implements Serializable {

    private static final long serialVersionUID = -5915951666116511558L;
    /**
     * 线路id
     */
    public long id;

    /**
     * 线路商品id
     */
    public long item_id;

    /**
     * 线路名
     */
    public String name;

    /**
     * 游咖信息/商家信息
     */
    public UserInfo userInfo;

    /**
     * 线路类型 REGULAR_LINE-线路 FLIGHT_HOTEL-机酒套餐 SCENIC_HOTEL-景酒套餐 TOUR_LINE-跟团游 FREE_LINE-自由行
     */
    public String type;

    /**
     * 线路首页图片url
     */
    public String logo_pic;

    /**
     * 线路所有图片url列表
     */
    public List<String> picUrls;
    /**
     * 线路所有标签列表
     */
    public List<TagInfo> tags;
    /**
     * 点赞状态 AVAILABLE:已赞/DELETED未赞
     */
    public String likeStatus;

    /**
     * 点赞数
     */
    public int likes;

    /**
     * 会员价
     */
    public long memberPrice;

    /**
     * 现价
     */
    public long price;

    /**
     * 原价
     */
    public long oldPrice;

    /**
     * 客服电话
     */
    public List<String> servicePhone;
    /**
     * 线路简介
     */
    public String description;

    /**
     * 线路出发地列表 暂时只有一个
     */
    public List<String> startCityList;
    /**
     * 参考机票简介
     */
    public FlightInfo flight;

    /**
     * 参考机票
     */
    public List<FlightDetail> flightsDetail;
    /**
     * 参考酒店
     */
    public List<HotelInfo> hotels;
    /**
     * 参考景点
     */
    public List<ScenicInfo> scenics;
    /**
     * 每日行程列表
     */
    public List<TmRouteDayInfo> route;
    /**
     * 购买须知
     */
    public NeedKnow needKnow;

    /**
     * 旅游咖推荐
     */
    public MasterRecommend masterRecommend;

    /**
     * 行程描述类型 1-行程项对象 2-H5行程详情
     */
    public int routeType;

    /**
     * H5行程详情链接
     */
    public String routeDetailUrl;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static LineDetail deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static LineDetail deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            LineDetail result = new LineDetail();

            // 线路id
            result.id = json.optLong("id");
            // 线路商品id
            result.item_id = json.optLong("item_id");
            // 线路名

            if(!json.isNull("name")){
                result.name = json.optString("name", null);
            }
            // 游咖信息/商家信息
            result.userInfo = UserInfo.deserialize(json.optJSONObject("userInfo"));
            // 线路类型 REGULAR_LINE-线路 FLIGHT_HOTEL-机酒套餐 SCENIC_HOTEL-景酒套餐 TOUR_LINE-跟团游 FREE_LINE-自由行

            if(!json.isNull("type")){
                result.type = json.optString("type", null);
            }
            // 线路首页图片url

            if(!json.isNull("logo_pic")){
                result.logo_pic = json.optString("logo_pic", null);
            }
            // 线路所有图片url列表
            JSONArray picUrlsArray = json.optJSONArray("picUrls");
            if (picUrlsArray != null) {
                int len = picUrlsArray.length();
                result.picUrls = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if(!picUrlsArray.isNull(i)){
                        result.picUrls.add(picUrlsArray.optString(i, null));
                    }else{
                        result.picUrls.add(i, null);
                    }

                }
            }

            // 线路所有标签列表
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

            // 点赞状态 AVAILABLE:已赞/DELETED未赞

            if(!json.isNull("likeStatus")){
                result.likeStatus = json.optString("likeStatus", null);
            }
            // 点赞数
            result.likes = json.optInt("likes");
            // 会员价
            result.memberPrice = json.optLong("memberPrice");
            // 现价
            result.price = json.optLong("price");
            // 原价
            result.oldPrice = json.optLong("oldPrice");
            // 客服电话
            JSONArray servicePhoneArray = json.optJSONArray("servicePhone");
            if (servicePhoneArray != null) {
                int len = servicePhoneArray.length();
                result.servicePhone = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if(!servicePhoneArray.isNull(i)){
                        result.servicePhone.add(servicePhoneArray.optString(i, null));
                    }else{
                        result.servicePhone.add(i, null);
                    }

                }
            }

            // 线路简介

            if(!json.isNull("description")){
                result.description = json.optString("description", null);
            }
            // 线路出发地列表 暂时只有一个
            JSONArray startCityListArray = json.optJSONArray("startCityList");
            if (startCityListArray != null) {
                int len = startCityListArray.length();
                result.startCityList = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if(!startCityListArray.isNull(i)){
                        result.startCityList.add(startCityListArray.optString(i, null));
                    }else{
                        result.startCityList.add(i, null);
                    }

                }
            }

            // 参考机票简介
            result.flight = FlightInfo.deserialize(json.optJSONObject("flight"));
            // 参考机票
            JSONArray flightsDetailArray = json.optJSONArray("flightsDetail");
            if (flightsDetailArray != null) {
                int len = flightsDetailArray.length();
                result.flightsDetail = new ArrayList<FlightDetail>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = flightsDetailArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.flightsDetail.add(FlightDetail.deserialize(jo));
                    }
                }
            }

            // 参考酒店
            JSONArray hotelsArray = json.optJSONArray("hotels");
            if (hotelsArray != null) {
                int len = hotelsArray.length();
                result.hotels = new ArrayList<HotelInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = hotelsArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.hotels.add(HotelInfo.deserialize(jo));
                    }
                }
            }

            // 参考景点
            JSONArray scenicsArray = json.optJSONArray("scenics");
            if (scenicsArray != null) {
                int len = scenicsArray.length();
                result.scenics = new ArrayList<ScenicInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = scenicsArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.scenics.add(ScenicInfo.deserialize(jo));
                    }
                }
            }

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
            // 旅游咖推荐
            result.masterRecommend = MasterRecommend.deserialize(json.optJSONObject("masterRecommend"));
            // 行程描述类型 1-行程项对象 2-H5行程详情
            result.routeType = json.optInt("routeType");
            // H5行程详情链接

            if(!json.isNull("routeDetailUrl")){
                result.routeDetailUrl = json.optString("routeDetailUrl", null);
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

        // 线路id
        json.put("id", this.id);

        // 线路商品id
        json.put("item_id", this.item_id);

        // 线路名
        if(this.name != null) { json.put("name", this.name); }

        // 游咖信息/商家信息
        if (this.userInfo != null) { json.put("userInfo", this.userInfo.serialize()); }

        // 线路类型 REGULAR_LINE-线路 FLIGHT_HOTEL-机酒套餐 SCENIC_HOTEL-景酒套餐 TOUR_LINE-跟团游 FREE_LINE-自由行
        if(this.type != null) { json.put("type", this.type); }

        // 线路首页图片url
        if(this.logo_pic != null) { json.put("logo_pic", this.logo_pic); }

        // 线路所有图片url列表
        if (this.picUrls != null) {
            JSONArray picUrlsArray = new JSONArray();
            for (String value : this.picUrls)
            {
                picUrlsArray.put(value);
            }
            json.put("picUrls", picUrlsArray);
        }

        // 线路所有标签列表
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

        // 点赞状态 AVAILABLE:已赞/DELETED未赞
        if(this.likeStatus != null) { json.put("likeStatus", this.likeStatus); }

        // 点赞数
        json.put("likes", this.likes);

        // 会员价
        json.put("memberPrice", this.memberPrice);

        // 现价
        json.put("price", this.price);

        // 原价
        json.put("oldPrice", this.oldPrice);

        // 客服电话
        if (this.servicePhone != null) {
            JSONArray servicePhoneArray = new JSONArray();
            for (String value : this.servicePhone)
            {
                servicePhoneArray.put(value);
            }
            json.put("servicePhone", servicePhoneArray);
        }

        // 线路简介
        if(this.description != null) { json.put("description", this.description); }

        // 线路出发地列表 暂时只有一个
        if (this.startCityList != null) {
            JSONArray startCityListArray = new JSONArray();
            for (String value : this.startCityList)
            {
                startCityListArray.put(value);
            }
            json.put("startCityList", startCityListArray);
        }

        // 参考机票简介
        if (this.flight != null) { json.put("flight", this.flight.serialize()); }

        // 参考机票
        if (this.flightsDetail != null) {
            JSONArray flightsDetailArray = new JSONArray();
            for (FlightDetail value : this.flightsDetail)
            {
                if (value != null) {
                    flightsDetailArray.put(value.serialize());
                }
            }
            json.put("flightsDetail", flightsDetailArray);
        }

        // 参考酒店
        if (this.hotels != null) {
            JSONArray hotelsArray = new JSONArray();
            for (HotelInfo value : this.hotels)
            {
                if (value != null) {
                    hotelsArray.put(value.serialize());
                }
            }
            json.put("hotels", hotelsArray);
        }

        // 参考景点
        if (this.scenics != null) {
            JSONArray scenicsArray = new JSONArray();
            for (ScenicInfo value : this.scenics)
            {
                if (value != null) {
                    scenicsArray.put(value.serialize());
                }
            }
            json.put("scenics", scenicsArray);
        }

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

        // 旅游咖推荐
        if (this.masterRecommend != null) { json.put("masterRecommend", this.masterRecommend.serialize()); }

        // 行程描述类型 1-行程项对象 2-H5行程详情
        json.put("routeType", this.routeType);

        // H5行程详情链接
        if(this.routeDetailUrl != null) { json.put("routeDetailUrl", this.routeDetailUrl); }
        return json;
    }

}
  