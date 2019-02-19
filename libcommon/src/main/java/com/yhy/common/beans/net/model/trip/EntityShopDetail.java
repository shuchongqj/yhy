// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;


import com.yhy.common.beans.net.model.club.POIInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EntityShopDetail extends HotelDetail{
    private static final long serialVersionUID = -2900295143460016478L;
//
//    /**
//     * 商家id
//     */
//    public long id;
//
//    /**
//     * 商家名
//     */
//    public String name;

    /**
     * 商家类型 HOTEL-酒店 RESTAURANT-饭店 SCENIC-景区
     */
    public String type;
//
//    /**
//     * 商家级别FIVE_STAR豪华型/五星级 FOUR_STAR高档型/四星级 THREE_STAR舒适型/三星级 TWO_STAR经济型/两星及以下
//     */
//    public String level;
//
//    /**
//     * 点赞数
//     */
//    public int likes;
//
//    /**
//     * 商家所在省名称
//     */
//    public String provinceName;
//
//    /**
//     * 商家所在市名称
//     */
//    public String cityName;
//
//    /**
//     * 商家所在县名称
//     */
//    public String townName;

    /**
     * 商家首页图片url
     */
    public String logo_pic;
//
//    /**
//     * 商家所有图片url列表
//     */
//    public List<String> picUrls;
//    /**
//     * 商家简介
//     */
//    public String description;
//
//    /**
//     * 位置信息
//     */
//    public POIInfo locationPOI;

//    /**
//     * 酒店设施
//     */
//    public List<String> hotelFacilities;
//    /**
//     * 房间设施
//     */
//
//    public List<String> roomFacilities;
//
//    /**
//     * 特殊服务
//     */
//    public List<String> specialServices;
//
//    /**
//     * 旅游咖推荐
//     */
//    public MasterRecommend masterRecommend;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static EntityShopDetail deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static EntityShopDetail deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            EntityShopDetail result = new EntityShopDetail();

            // 商家id
            result.id = json.optLong("id");
            // 商家名

            if (!json.isNull("name")) {
                result.name = json.optString("name", null);
            }
            // 商家类型 HOTEL-酒店 RESTAURANT-饭店 SCENIC-景区

            if (!json.isNull("type")) {
                result.type = json.optString("type", null);
            }
            // 商家级别FIVE_STAR豪华型/五星级 FOUR_STAR高档型/四星级 THREE_STAR舒适型/三星级 TWO_STAR经济型/两星及以下

            if (!json.isNull("level")) {
                result.level = json.optString("level", null);
            }
            // 点赞数
            result.likes = json.optInt("likes");

            if(!json.isNull("likeStatus")){
                result.likeStatus = json.optString("likeStatus", null);
            }
            // 商家所在省名称

            if (!json.isNull("provinceName")) {
                result.provinceName = json.optString("provinceName", null);
            }
            // 商家所在市名称

            if (!json.isNull("cityName")) {
                result.cityName = json.optString("cityName", null);
            }
            // 商家所在县名称

            if (!json.isNull("townName")) {
                result.townName = json.optString("townName", null);
            }
            // 商家首页图片url

            if (!json.isNull("logo_pic")) {
                result.logo_pic = json.optString("logo_pic", null);
            }
            // 商家所有图片url列表
            JSONArray picUrlsArray = json.optJSONArray("picUrls");
            if (picUrlsArray != null) {
                int len = picUrlsArray.length();
                result.picUrls = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if (!picUrlsArray.isNull(i)) {
                        result.picUrls.add(picUrlsArray.optString(i, null));
                    } else {
                        result.picUrls.add(i, null);
                    }

                }
            }

            // 商家简介

            if (!json.isNull("description")) {
                result.description = json.optString("description", null);
            }
            // 位置信息
            result.locationPOI = POIInfo.deserialize(json.optJSONObject("locationPOI"));
            // 酒店设施
            JSONArray hotelFacilitiesArray = json.optJSONArray("hotelFacilities");
            if (hotelFacilitiesArray != null) {
                int len = hotelFacilitiesArray.length();
                result.hotelFacilities = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if (!hotelFacilitiesArray.isNull(i)) {
                        result.hotelFacilities.add(hotelFacilitiesArray.optString(i, null));
                    } else {
                        result.hotelFacilities.add(i, null);
                    }

                }
            }

            // 房间设施
            JSONArray roomFacilitiesArray = json.optJSONArray("roomFacilities");
            if (roomFacilitiesArray != null) {
                int len = roomFacilitiesArray.length();
                result.roomFacilities = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if (!roomFacilitiesArray.isNull(i)) {
                        result.roomFacilities.add(roomFacilitiesArray.optString(i, null));
                    } else {
                        result.roomFacilities.add(i, null);
                    }

                }
            }

            // 特殊服务
            JSONArray specialServicesArray = json.optJSONArray("specialServices");
            if (specialServicesArray != null) {
                int len = specialServicesArray.length();
                result.specialServices = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if (!specialServicesArray.isNull(i)) {
                        result.specialServices.add(specialServicesArray.optString(i, null));
                    } else {
                        result.specialServices.add(i, null);
                    }

                }
            }
            // 旅游咖推荐
            result.masterRecommend = MasterRecommend.deserialize(json.optJSONObject("masterRecommend"));
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 商家id
        json.put("id", this.id);

        // 商家名
        if (this.name != null) {
            json.put("name", this.name);
        }

        // 商家类型 HOTEL-酒店 RESTAURANT-饭店 SCENIC-景区
        if (this.type != null) {
            json.put("type", this.type);
        }

        // 商家级别FIVE_STAR豪华型/五星级 FOUR_STAR高档型/四星级 THREE_STAR舒适型/三星级 TWO_STAR经济型/两星及以下
        if (this.level != null) {
            json.put("level", this.level);
        }

        // 点赞数
        json.put("likes", this.likes);

        // 商家所在省名称
        if (this.provinceName != null) {
            json.put("provinceName", this.provinceName);
        }

        // 点赞状态 NON_LIKE未赞/LIKE:已赞
        if(this.likeStatus != null) { json.put("likeStatus", this.likeStatus); }

        // 商家所在市名称
        if (this.cityName != null) {
            json.put("cityName", this.cityName);
        }

        // 商家所在县名称
        if (this.townName != null) {
            json.put("townName", this.townName);
        }

        // 商家首页图片url
        if (this.logo_pic != null) {
            json.put("logo_pic", this.logo_pic);
        }

        // 商家所有图片url列表
        if (this.picUrls != null) {
            JSONArray picUrlsArray = new JSONArray();
            for (String value : this.picUrls) {
                picUrlsArray.put(value);
            }
            json.put("picUrls", picUrlsArray);
        }

        // 商家简介
        if (this.description != null) {
            json.put("description", this.description);
        }

        // 位置信息
        if (this.locationPOI != null) {
            json.put("locationPOI", this.locationPOI.serialize());
        }

        // 酒店设施
        if (this.hotelFacilities != null) {
            JSONArray hotelFacilitiesArray = new JSONArray();
            for (String value : this.hotelFacilities) {
                hotelFacilitiesArray.put(value);
            }
            json.put("hotelFacilities", hotelFacilitiesArray);
        }

        // 房间设施
        if (this.roomFacilities != null) {
            JSONArray roomFacilitiesArray = new JSONArray();
            for (String value : this.roomFacilities) {
                roomFacilitiesArray.put(value);
            }
            json.put("roomFacilities", roomFacilitiesArray);
        }

        // 特殊服务
        if (this.specialServices != null) {
            JSONArray specialServicesArray = new JSONArray();
            for (String value : this.specialServices) {
                specialServicesArray.put(value);
            }
            json.put("specialServices", specialServicesArray);
        }


        // 旅游咖推荐
        if (this.masterRecommend != null) {
            json.put("masterRecommend", this.masterRecommend.serialize());
        }

        return json;
    }

}
  