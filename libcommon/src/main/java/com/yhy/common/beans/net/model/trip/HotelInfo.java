// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class HotelInfo implements Serializable {

    private static final long serialVersionUID = -5768274399035591943L;
    /**
     * 酒店id
     */
    public long id;

    /**
     * 酒店名
     */
    public String name;

    /**
     * 酒店首页图片url
     */
    public String logo_pic;

    /**
     * 酒店级别FIVE_STAR豪华型/五星级 FOUR_STAR高档型/四星级 THREE_STAR舒适型/三星级 TWO_STAR经济型/两星及以下
     */
    public String level;

    /**
     * 酒店得分，4分对应4颗星
     */
    public int grade;

    /**
     * 酒店销量
     */
    public int sales;

    /**
     * 酒店简介文字
     */
    public String shortDesc;

    /**
     * 酒店点赞数
     */
    public int likes;

    /**
     * 酒店地址
     */
    public String address;

    /**
     * 酒店起价
     */
    public long price;

    /**
     * 酒店会员起价
     */
    public long memeberPrice;

    /**
     * 评价数量
     */
    public int rateCount;

    /**
     * 距离
     */
    public long distance;

    /**
     * 商圈
     */
    public String commercialArea;

    /**
     * 库存
     */
    public int stockNum;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static HotelInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static HotelInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            HotelInfo result = new HotelInfo();

            // 酒店id
            result.id = json.optLong("id");
            // 酒店名

            if (!json.isNull("name")) {
                result.name = json.optString("name", null);
            }
            // 酒店首页图片url

            if (!json.isNull("logo_pic")) {
                result.logo_pic = json.optString("logo_pic", null);
            }
            // 酒店级别FIVE_STAR豪华型/五星级 FOUR_STAR高档型/四星级 THREE_STAR舒适型/三星级 TWO_STAR经济型/两星及以下

            if (!json.isNull("level")) {
                result.level = json.optString("level", null);
            }
            // 酒店得分，4分对应4颗星
            result.grade = (int) json.optDouble("grade");
            // 酒店销量
            result.sales = json.optInt("sales");
            // 酒店简介文字

            if (!json.isNull("shortDesc")) {
                result.shortDesc = json.optString("shortDesc", null);
            }
            // 酒店点赞数
            result.likes = json.optInt("likes");
            // 酒店地址

            if (!json.isNull("address")) {
                result.address = json.optString("address", null);
            }
            // 酒店起价
            result.price = json.optLong("price");
            // 酒店会员起价
            result.memeberPrice = json.optLong("memeberPrice");
            // 评价数量
            result.rateCount = json.optInt("rateCount");
            // 距离
            result.distance = json.optLong("distance");
            // 商圈

            if (!json.isNull("commercialArea")) {
                result.commercialArea = json.optString("commercialArea", null);
            }

            // 库存
            result.stockNum = json.optInt("stockNum");

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

        // 酒店首页图片url
        if (this.logo_pic != null) {
            json.put("logo_pic", this.logo_pic);
        }

        // 酒店级别FIVE_STAR豪华型/五星级 FOUR_STAR高档型/四星级 THREE_STAR舒适型/三星级 TWO_STAR经济型/两星及以下
        if (this.level != null) {
            json.put("level", this.level);
        }

        // 酒店得分，4分对应4颗星
        json.put("grade", this.grade);

        // 酒店销量
        json.put("sales", this.sales);

        // 酒店简介文字
        if (this.shortDesc != null) {
            json.put("shortDesc", this.shortDesc);
        }

        // 酒店点赞数
        json.put("likes", this.likes);

        // 酒店地址
        if (this.address != null) {
            json.put("address", this.address);
        }

        // 酒店起价
        json.put("price", this.price);

        // 酒店会员起价
        json.put("memeberPrice", this.memeberPrice);
        // 评价数量
        json.put("rateCount", this.rateCount);

        // 距离
        json.put("distance", this.distance);

        // 商圈
        if (this.commercialArea != null) {
            json.put("commercialArea", this.commercialArea);
        }

        // 库存
        json.put("stockNum", this.stockNum);

        return json;
    }

}
  