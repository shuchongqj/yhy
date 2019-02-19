// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;


import com.yhy.common.beans.net.model.club.POIInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HotelDetail implements Serializable {

    private static final long serialVersionUID = -8602468629503567619L;
    /**
     * 酒店id
     */
    public long id;

    /**
     * 酒店名
     */
    public String name;

    /**
     * 酒店封面图片url
     */
    public String cover_pic;

    /**
     * 酒店所有图片url列表
     */
    public List<String> picUrls;
    /**
     * 酒店级别FIVE_STAR豪华型/五星级 FOUR_STAR高档型/四星级 THREE_STAR舒适型/三星级 TWO_STAR经济型/两星及以下
     */
    public String level;

    /**
     * 酒店得分，4分对应4颗星
     */
    public float grade;

    /**
     * 点赞状态 NON_LIKE未赞/LIKE:已赞
     */
    public String likeStatus;

    /**
     * 酒店所在省名称
     */
    public String provinceName;

    /**
     * 酒店所在市名称
     */
    public String cityName;

    /**
     * 酒店所在县名称
     */
    public String townName;

    /**
     * 酒店准确坐标
     */
    public POIInfo locationPOI;

    /**
     * 酒店简介
     */
    public String description;

    /**
     * 酒店电话，最多三个
     */
    public List<String> phoneNumbers;
    /**
     * 入住时间描述
     */
    public String openTime;

    /**
     * 点赞数
     */
    public int likes;

    /**
     * 酒店起价
     */
    public long price;

    /**
     * 酒店会员起价
     */
    public long memeberPrice;

    /**
     * 酒店设施
     */
    public List<String> hotelFacilities;
    /**
     * 房间设施
     */
    public List<String> roomFacilities;
    /**
     * 特殊服务
     */
    public List<String> specialServices;
    /**
     * 旅游咖推荐
     */
    public MasterRecommend masterRecommend;

    /**
     * 入住须知
     */
    public NeedKnow needKnow;

    /**
     * 图片数量
     */
    public int pictureCount;
    /**
     * 酒店类型
     */
    public String type;

    /**
     * 酒店头图
     */
    public String logo_pic;
    /**
     * 点评数
     */
    public int comRateCount;


    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static HotelDetail deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static HotelDetail deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            HotelDetail result = new HotelDetail();

            // 酒店id
            result.id = json.optLong("id");
            // 酒店名

            if (!json.isNull("name")) {
                result.name = json.optString("name", null);
            }
            // 酒店类型

            if (!json.isNull("type")) {
                result.type = json.optString("type", null);
            }
            // 酒店封面图片url

            if (!json.isNull("cover_pic")) {
                result.cover_pic = json.optString("cover_pic", null);
            }
            // 酒店所有图片url列表
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

            // 酒店级别FIVE_STAR豪华型/五星级 FOUR_STAR高档型/四星级 THREE_STAR舒适型/三星级 TWO_STAR经济型/两星及以下

            if (!json.isNull("level")) {
                result.level = json.optString("level", null);
            }
            // 酒店得分，4分对应4颗星
            result.grade = (float) json.optDouble("grade");
            // 点赞状态 DELETED未赞/AVAILABLE:已赞

            if (!json.isNull("likeStatus")) {
                result.likeStatus = json.optString("likeStatus", null);
            }
            // 酒店所在省名称

            if (!json.isNull("provinceName")) {
                result.provinceName = json.optString("provinceName", null);
            }
            // 酒店所在市名称

            if (!json.isNull("cityName")) {
                result.cityName = json.optString("cityName", null);
            }
            // 酒店所在县名称

            if (!json.isNull("townName")) {
                result.townName = json.optString("townName", null);
            }
            // 酒店准确坐标
            result.locationPOI = POIInfo.deserialize(json.optJSONObject("locationPOI"));
            // 酒店简介

            if (!json.isNull("description")) {
                result.description = json.optString("description", null);
            }
            // 酒店电话，最多三个
            JSONArray phoneNumbersArray = json.optJSONArray("phoneNumbers");
            if (phoneNumbersArray != null) {
                int len = phoneNumbersArray.length();
                result.phoneNumbers = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if (!phoneNumbersArray.isNull(i)) {
                        result.phoneNumbers.add(phoneNumbersArray.optString(i, null));
                    } else {
                        result.phoneNumbers.add(i, null);
                    }

                }
            }

            // 入住时间描述

            if (!json.isNull("openTime")) {
                result.openTime = json.optString("openTime", null);
            }
            // 点赞数
            result.likes = json.optInt("likes");
            // 酒店起价
            result.price = json.optLong("price");
            // 酒店会员起价
            result.memeberPrice = json.optLong("memeberPrice");
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
            // 入住须知
            result.needKnow = NeedKnow.deserialize(json.optJSONObject("needKnow"));
            // 图片数量
            result.pictureCount = json.optInt("pictureCount");
            // 酒店头图

            if (!json.isNull("logo_pic")) {
                result.logo_pic = json.optString("logo_pic", null);
            }
            // 点评数
            result.comRateCount = json.optInt("comRateCount");

            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 酒店id
        json.put("id", this.id);

        // 酒店名
        if (this.name != null) {
            json.put("name", this.name);
        }

        // 酒店类型
        if (this.type != null) {
            json.put("type", this.type);
        }

        // 酒店封面图片url
        if (this.cover_pic != null) {
            json.put("cover_pic", this.cover_pic);
        }

        // 酒店所有图片url列表
        if (this.picUrls != null) {
            JSONArray picUrlsArray = new JSONArray();
            for (String value : this.picUrls) {
                picUrlsArray.put(value);
            }
            json.put("picUrls", picUrlsArray);
        }

        // 酒店级别FIVE_STAR豪华型/五星级 FOUR_STAR高档型/四星级 THREE_STAR舒适型/三星级 TWO_STAR经济型/两星及以下
        if (this.level != null) {
            json.put("level", this.level);
        }

        // 酒店得分，4分对应4颗星
        json.put("grade", this.grade);

        // 点赞状态 DELETED未赞/AVAILABLE:已赞
        if (this.likeStatus != null) {
            json.put("likeStatus", this.likeStatus);
        }

        // 酒店所在省名称
        if (this.provinceName != null) {
            json.put("provinceName", this.provinceName);
        }

        // 酒店所在市名称
        if (this.cityName != null) {
            json.put("cityName", this.cityName);
        }

        // 酒店所在县名称
        if (this.townName != null) {
            json.put("townName", this.townName);
        }

        // 酒店准确坐标
        if (this.locationPOI != null) {
            json.put("locationPOI", this.locationPOI.serialize());
        }

        // 酒店简介
        if (this.description != null) {
            json.put("description", this.description);
        }

        // 酒店电话，最多三个
        if (this.phoneNumbers != null) {
            JSONArray phoneNumbersArray = new JSONArray();
            for (String value : this.phoneNumbers) {
                phoneNumbersArray.put(value);
            }
            json.put("phoneNumbers", phoneNumbersArray);
        }

        // 入住时间描述
        if (this.openTime != null) {
            json.put("openTime", this.openTime);
        }

        // 点赞数
        json.put("likes", this.likes);

        // 酒店起价
        json.put("price", this.price);

        // 酒店会员起价
        json.put("memeberPrice", this.memeberPrice);

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

        // 入住须知
        if (this.needKnow != null) {
            json.put("needKnow", this.needKnow.serialize());
        }

        // 图片数量
        json.put("pictureCount", this.pictureCount);

        // 酒店头图
        if (this.logo_pic != null) {
            json.put("logo_pic", this.logo_pic);
        }
        // 点评数
        json.put("comRateCount", this.comRateCount);

        return json;
    }

}
  