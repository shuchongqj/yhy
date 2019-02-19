// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;

import com.yhy.common.beans.net.model.club.POIInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ScenicDetail implements Serializable {

    private static final long serialVersionUID = -8936192559945607050L;
    /**
     * 景区id
     */
    public long id;

    /**
     * 景区名
     */
    public String name;

    /**
     * 景区封面图片url
     */
    public String cover_pic;

    /**
     * 景区所有图片url列表
     */
    public List<String> picUrls;
    /**
     * 得分
     */
    public float grade;

    /**
     * 点赞数
     */
    public float likes;

    /**
     * 营业时间
     */
    public String openTime;

    /**
     * 类型 LEVEL_A-1A级 LEVEL_AA-2A级 LEVEL_AAA-3A级 LEVEL_AAAA-4A级 LEVEL_AAAAA-5A级
     */
    public String level;

    /**
     * 景区所在省名称
     */
    public String provinceName;

    /**
     * 景区所在市名称
     */
    public String cityName;

    /**
     * 景区所在县名称
     */
    public String townName;

    /**
     * 景区准确坐标
     */
    public POIInfo locationPOI;

    /**
     * 景区简介
     */
    public String description;

    /**
     * 点赞状态 AVAILABLE:已赞/DELETED未赞
     */
    public String likeStatus;

    /**
     * 景区电话，最多三个
     */
    public List<String> phoneNumbers;
    /**
     * 景区标签
     */
    public List<String> tags;
    /**
     * 门票信息
     */
    public List<ItemVO> tickets;
    /**
     * 门票价格
     */
    public long price;

    /**
     * 会员门票价格
     */
    public long memberPrice;

    /**
     * 退改规定
     */
    public String refundRules;

    /**
     * 入园须知
     */
    public NeedKnow needKnow;

    /**
     * 旅游咖推荐
     */
    public MasterRecommend masterRecommend;

    /**
     * 票型和商品列表的对应关系
     */
    public List<TicketItemVO> ticketItemVOList;

    /**
     * 是否有导览
     */
    public boolean isGuide;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ScenicDetail deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ScenicDetail deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ScenicDetail result = new ScenicDetail();

            // 景区id
            result.id = json.optLong("id");
            // 景区名

            if (!json.isNull("name")) {
                result.name = json.optString("name", null);
            }
            // 景区封面图片url

            if (!json.isNull("cover_pic")) {
                result.cover_pic = json.optString("cover_pic", null);
            }
            // 景区所有图片url列表
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

            // 得分
            result.grade = (float) json.optDouble("grade");
            // 点赞数
            result.likes = (float) json.optDouble("likes");
            // 营业时间

            if (!json.isNull("openTime")) {
                result.openTime = json.optString("openTime", null);
            }
            // 类型 FIVE_STAR-五星级 FOUR_STAR-四星级 THREE_STAR-三星级 TWO_STAR-其他

            if (!json.isNull("level")) {
                result.level = json.optString("level", null);
            }
            // 景区所在省名称

            if (!json.isNull("provinceName")) {
                result.provinceName = json.optString("provinceName", null);
            }
            // 景区所在市名称

            if (!json.isNull("cityName")) {
                result.cityName = json.optString("cityName", null);
            }
            // 景区所在县名称

            if (!json.isNull("townName")) {
                result.townName = json.optString("townName", null);
            }
            // 景区准确坐标
            result.locationPOI = POIInfo.deserialize(json.optJSONObject("locationPOI"));
            // 景区简介

            if (!json.isNull("description")) {
                result.description = json.optString("description", null);
            }
            // 点赞状态 AVAILABLE:已赞/DELETED未赞

            if (!json.isNull("likeStatus")) {
                result.likeStatus = json.optString("likeStatus", null);
            }
            // 景区电话，最多三个
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

            // 景区标签
            JSONArray tagsArray = json.optJSONArray("tags");
            if (tagsArray != null) {
                int len = tagsArray.length();
                result.tags = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if (!tagsArray.isNull(i)) {
                        result.tags.add(tagsArray.optString(i, null));
                    } else {
                        result.tags.add(i, null);
                    }

                }
            }

            // 门票信息
            JSONArray ticketsArray = json.optJSONArray("tickets");
            if (ticketsArray != null) {
                int len = ticketsArray.length();
                result.tickets = new ArrayList<ItemVO>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = ticketsArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.tickets.add(ItemVO.deserialize(jo));
                    }
                }
            }

            // 门票价格
            result.price = json.optLong("price");
            // 会员门票价格
            result.memberPrice = json.optLong("memberPrice");
            // 退改规定

            if (!json.isNull("refundRules")) {
                result.refundRules = json.optString("refundRules", null);
            }
            // 票型和商品列表的对应关系
            JSONArray ticketItemVOListArray = json.optJSONArray("ticketItemVOList");
            if (ticketItemVOListArray != null) {
                int len = ticketItemVOListArray.length();
                result.ticketItemVOList = new ArrayList<TicketItemVO>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = ticketItemVOListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.ticketItemVOList.add(TicketItemVO.deserialize(jo));
                    }
                }
            }

            // 入园须知
            result.needKnow = NeedKnow.deserialize(json.optJSONObject("needKnow"));
            // 旅游咖推荐
            result.masterRecommend = MasterRecommend.deserialize(json.optJSONObject("masterRecommend"));
            // 是否有导览
            result.isGuide = json.optBoolean("isGuide");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 景区id
        json.put("id", this.id);

        // 景区名
        if (this.name != null) {
            json.put("name", this.name);
        }

        // 景区封面图片url
        if (this.cover_pic != null) {
            json.put("cover_pic", this.cover_pic);
        }

        // 景区所有图片url列表
        if (this.picUrls != null) {
            JSONArray picUrlsArray = new JSONArray();
            for (String value : this.picUrls) {
                picUrlsArray.put(value);
            }
            json.put("picUrls", picUrlsArray);
        }

        // 得分
        json.put("grade", this.grade);

        // 点赞数
        json.put("likes", this.likes);

        // 营业时间
        if (this.openTime != null) {
            json.put("openTime", this.openTime);
        }

        // 类型 FIVE_STAR-五星级 FOUR_STAR-四星级 THREE_STAR-三星级 TWO_STAR-其他
        if (this.level != null) {
            json.put("level", this.level);
        }

        // 景区所在省名称
        if (this.provinceName != null) {
            json.put("provinceName", this.provinceName);
        }

        // 景区所在市名称
        if (this.cityName != null) {
            json.put("cityName", this.cityName);
        }

        // 景区所在县名称
        if (this.townName != null) {
            json.put("townName", this.townName);
        }

        // 景区准确坐标
        if (this.locationPOI != null) {
            json.put("locationPOI", this.locationPOI.serialize());
        }

        // 景区简介
        if (this.description != null) {
            json.put("description", this.description);
        }

        // 点赞状态 AVAILABLE:已赞/DELETED未赞
        if (this.likeStatus != null) {
            json.put("likeStatus", this.likeStatus);
        }

        // 景区电话，最多三个
        if (this.phoneNumbers != null) {
            JSONArray phoneNumbersArray = new JSONArray();
            for (String value : this.phoneNumbers) {
                phoneNumbersArray.put(value);
            }
            json.put("phoneNumbers", phoneNumbersArray);
        }

        // 景区标签
        if (this.tags != null) {
            JSONArray tagsArray = new JSONArray();
            for (String value : this.tags) {
                tagsArray.put(value);
            }
            json.put("tags", tagsArray);
        }

        // 门票信息
        if (this.tickets != null) {
            JSONArray ticketsArray = new JSONArray();
            for (ItemVO value : this.tickets) {
                if (value != null) {
                    ticketsArray.put(value.serialize());
                }
            }
            json.put("tickets", ticketsArray);
        }

        // 门票价格
        json.put("price", this.price);

        // 会员门票价格
        json.put("memberPrice", this.memberPrice);

        // 退改规定
        if (this.refundRules != null) {
            json.put("refundRules", this.refundRules);
        }

        // 票型和商品列表的对应关系
        if (this.ticketItemVOList != null) {
            JSONArray ticketItemVOListArray = new JSONArray();
            for (TicketItemVO value : this.ticketItemVOList) {
                if (value != null) {
                    ticketItemVOListArray.put(value.serialize());
                }
            }
            json.put("ticketItemVOList", ticketItemVOListArray);
        }

        // 入园须知
        if (this.needKnow != null) {
            json.put("needKnow", this.needKnow.serialize());
        }

        // 旅游咖推荐
        if (this.masterRecommend != null) {
            json.put("masterRecommend", this.masterRecommend.serialize());
        }

        // 是否有导览
        json.put("isGuide", this.isGuide);

        return json;
    }
}
  