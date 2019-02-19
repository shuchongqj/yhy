// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ScenicInfo implements Serializable {

    private static final long serialVersionUID = -713298915077495404L;
    /**
     * 景区id
     */
    public long id;

    /**
     * 景区名
     */
    public String name;

    /**
     * 景区首页图片url
     */
    public String logo_pic;

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
     * 景区地址
     */
    public String address;

    /**
     * 景区点赞数
     */
    public int likes;

    /**
     * 门票价格
     */
    public long price;

    /**
     * 会员门票价格
     */
    public long memberPrice;

    /**
     * 评价数量
     */
    public int rateCount;

    /**
     * 距离
     */
    public long distance;

    /**
     * 是否今日可订
     */
    public boolean isCanOrderToday;

    /**
     * 是否导览
     */
    public boolean isGuide;


    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ScenicInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ScenicInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ScenicInfo result = new ScenicInfo();

            // 景区id
            result.id = json.optLong("id");
            // 景区名

            if (!json.isNull("name")) {
                result.name = json.optString("name", null);
            }
            // 景区首页图片url

            if (!json.isNull("logo_pic")) {
                result.logo_pic = json.optString("logo_pic", null);
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
            // 景区地址

            if (!json.isNull("address")) {
                result.address = json.optString("address", null);
            }
            // 景区点赞数
            result.likes = json.optInt("likes");
            // 门票价格
            result.price = json.optLong("price");
            // 会员门票价格
            result.memberPrice = json.optLong("memberPrice");

            // 评价数量
            result.rateCount = json.optInt("rateCount");
            // 距离
            result.distance = json.optLong("distance");
            // 是否今日可订
            result.isCanOrderToday = json.optBoolean("isCanOrderToday");
            // 是否导览
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

        // 景区首页图片url
        if (this.logo_pic != null) {
            json.put("logo_pic", this.logo_pic);
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

        // 景区地址
        if (this.address != null) {
            json.put("address", this.address);
        }

        // 景区点赞数
        json.put("likes", this.likes);

        // 门票价格
        json.put("price", this.price);

        // 会员门票价格
        json.put("memberPrice", this.memberPrice);

        // 评价数量
        json.put("rateCount", this.rateCount);

        // 距离
        json.put("distance", this.distance);

        // 是否今日可订
        json.put("isCanOrderToday", this.isCanOrderToday);

        // 是否导览
        json.put("isGuide", this.isGuide);

        return json;
    }
}
  