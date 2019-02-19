// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;


import com.yhy.common.beans.net.model.tm.PayInfo;
import com.yhy.common.beans.net.model.user.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShortItem implements Serializable {

    private static final long serialVersionUID = 5094484065235911349L;
    /**
     * 商品id
     */
    public long id;

    /**
     * 标题
     */
    public String title;

    /**
     * 主图片地址
     */
    public String mainPicUrl;

    /**
     * 价格
     */
    public long price;

    /**
     * 卖家信息
     */
    public UserInfo userInfo;

    /**
     * 外部ID
     */
    public long outId;

    /**
     * 外部类型 酒店:HOTEL/景区:SCENIC/线路:LINE/同城活动:CITY_ACTIVITY
     */
    public String outType;

    /**
     * 点赞数，保留字段
     */
    public int likes;

    /**
     * 点赞状态 DELETED未赞/AVAILABLE已赞，保留字段
     */
    public String likeStatus;

    /**
     * 销售库存，保留字段
     */
    public int stockNum;

    /**
     * 评分 0-5，保留字段
     */
    public float grade;

    /**
     * 销量
     */
    public int sales;

    /**
     * 标签列表
     */
    public List<TagInfo> tagList;
    /**
     * 标签列表
     */
    public List<CityInfo> startCityList;

    //日期
    public long date;

    /**
     * 商品类型 NORMAL:普通商品交易 / LINE:线路商品 / HOTEL:酒店商品 / SPOTS:景区门票 / FLIGHT_HOTEL:机票+酒店 / SPOTS_HOTEL:景点+酒店 / ACTIVITY:活动商品 / MEMBER_RECHARGE:会员充值 / GF:婕珞芙 / TEMP_PRODUCT_CARD:临时产品卡 / TOUR_LINE:跟团游 / FREE_LINE:自由行 / CITY_ACTIVITY:同城活动
     */
    public String itemType;

    /**
     * 距离
     */
    public int distance;

    /**
     * 状态 有效:VALID / 已结束:EXPIRED
     */
    public String status;

    /**
     * 支付相关信息，如是否可用积分
     */
    public PayInfo payInfo;

    /**
     * 用户在线状态标识 在线：ONLINE / 不在线 NOTONLINE
     */
    public String onlineStatus;

    /**
     * 商品对应服务地区，多个服务地区用英文逗号分隔
     */
    public String destinations;

    public long originalPrice;

    public long consultTime;

    /**
     * 新销量
     */
    public int showSales;

    /**
     * 最大价格
     */
    public long maxPrice;

    /**
     * 跳转类型 本地还是h5 native
     */
    public String skipType;

    /**
     * 是否是热销
     */
    public boolean hotSales;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ShortItem deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ShortItem deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ShortItem result = new ShortItem();
            result.originalPrice = json.optLong("originalPrice");
            result.consultTime = json.optLong("consultTime");

            // 标题

            // 商品id
            result.id = json.optLong("id");
            // 标题

            if (!json.isNull("title")) {
                result.title = json.optString("title", null);
            }
            // 主图片地址

            if (!json.isNull("mainPicUrl")) {
                result.mainPicUrl = json.optString("mainPicUrl", null);
            }
            // 价格
            result.price = json.optLong("price");
            // 最大价格
            result.maxPrice = json.optLong("maxPrice");
            // 卖家信息
            result.userInfo = UserInfo.deserialize(json.optJSONObject("userInfo"));
            // 外部ID
            result.outId = json.optLong("outId");
            // 外部类型 酒店:HOTEL/景区:SCENIC/线路:LINE/同城活动:CITY_ACTIVITY

            if (!json.isNull("outType")) {
                result.outType = json.optString("outType", null);
            }
            // 点赞数，保留字段
            result.likes = json.optInt("likes");
            // 点赞状态 DELETED未赞/AVAILABLE已赞，保留字段

            if (!json.isNull("likeStatus")) {
                result.likeStatus = json.optString("likeStatus", null);
            }
            // 销售库存，保留字段
            result.stockNum = json.optInt("stockNum");
            // 评分 0-5，保留字段
            result.grade = (float) json.optDouble("grade");
            // 销量
            result.sales = json.optInt("sales");
            // 标签列表
            JSONArray tagListArray = json.optJSONArray("tagList");
            if (tagListArray != null) {
                int len = tagListArray.length();
                result.tagList = new ArrayList<TagInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = tagListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.tagList.add(TagInfo.deserialize(jo));
                    }
                }
            }

            // 标签列表
            JSONArray startCityListArray = json.optJSONArray("startCityList");
            if (startCityListArray != null) {
                int len = startCityListArray.length();
                result.startCityList = new ArrayList<CityInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = startCityListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.startCityList.add(CityInfo.deserialize(jo));
                    }
                }
            }

            // 商品类型 NORMAL:普通商品交易 / LINE:线路商品 / HOTEL:酒店商品 / SPOTS:景区门票 / FLIGHT_HOTEL:机票+酒店 / SPOTS_HOTEL:景点+酒店 / ACTIVITY:活动商品 / MEMBER_RECHARGE:会员充值 / GF:婕珞芙 / TEMP_PRODUCT_CARD:临时产品卡 / TOUR_LINE:跟团游 / FREE_LINE:自由行 / CITY_ACTIVITY:同城活动

            if (!json.isNull("itemType")) {
                result.itemType = json.optString("itemType", null);
            }

            // 距离
            result.distance = json.optInt("distance");
            // 状态 有效:VALID / 已结束:EXPIRED

            if (!json.isNull("status")) {
                result.status = json.optString("status", null);
            }

            // 支付相关信息，如是否可用积分
            result.payInfo = PayInfo.deserialize(json.optJSONObject("payInfo"));

            if (!json.isNull("onlineStatus")) {
                result.onlineStatus = json.optString("onlineStatus", null);
            }
            // 商品对应服务地区，多个服务地区用英文逗号分隔

            if (!json.isNull("destinations")) {
                result.destinations = json.optString("destinations", null);
            }
            // 新销量
            result.showSales = json.optInt("showSales");
            // 跳转类型
            result.skipType = json.optString("skipType", null);
            // 是否热销
            result.hotSales = json.optBoolean("hotSales", false);
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 商品id
        json.put("id", this.id);

        // 标题
        if (this.title != null) {
            json.put("title", this.title);
        }

        // 主图片地址
        if (this.mainPicUrl != null) {
            json.put("mainPicUrl", this.mainPicUrl);
        }
        json.put("consultTime", this.consultTime);
        //
        json.put("originalPrice", this.originalPrice);
        // 价格
        json.put("price", this.price);

        // 卖家信息
        if (this.userInfo != null) {
            json.put("userInfo", this.userInfo.serialize());
        }

        // 外部ID
        json.put("outId", this.outId);

        // 外部类型 酒店:HOTEL/景区:SCENIC/线路:LINE/同城活动:CITY_ACTIVITY
        if (this.outType != null) {
            json.put("outType", this.outType);
        }

        // 点赞数，保留字段
        json.put("likes", this.likes);

        // 点赞状态 DELETED未赞/AVAILABLE已赞，保留字段
        if (this.likeStatus != null) {
            json.put("likeStatus", this.likeStatus);
        }

        // 销售库存，保留字段
        json.put("stockNum", this.stockNum);

        // 评分 0-5，保留字段
        json.put("grade", this.grade);

        // 销量
        json.put("sales", this.sales);

        // 标签列表
        if (this.tagList != null) {
            JSONArray tagListArray = new JSONArray();
            for (TagInfo value : this.tagList) {
                if (value != null) {
                    tagListArray.put(value.serialize());
                }
            }
            json.put("tagList", tagListArray);
        }

        // 标签列表
        if (this.startCityList != null) {
            JSONArray startCityListArray = new JSONArray();
            for (CityInfo value : this.startCityList) {
                if (value != null) {
                    startCityListArray.put(value.serialize());
                }
            }
            json.put("startCityList", startCityListArray);
        }

        // 商品类型 NORMAL:普通商品交易 / LINE:线路商品 / HOTEL:酒店商品 / SPOTS:景区门票 / FLIGHT_HOTEL:机票+酒店 / SPOTS_HOTEL:景点+酒店 / ACTIVITY:活动商品 / MEMBER_RECHARGE:会员充值 / GF:婕珞芙 / TEMP_PRODUCT_CARD:临时产品卡 / TOUR_LINE:跟团游 / FREE_LINE:自由行 / CITY_ACTIVITY:同城活动
        if (this.itemType != null) {
            json.put("itemType", this.itemType);
        }

        // 距离
        json.put("distance", this.distance);

        // 状态 有效:VALID / 已结束:EXPIRED
        if (this.status != null) {
            json.put("status", this.status);
        }

        // 支付相关信息，如是否可用积分
        if (this.payInfo != null) {
            json.put("payInfo", this.payInfo.serialize());
        }

        // 用户在线状态标识 在线：ONLINE / 不在线 NOTONLINE
        if (this.onlineStatus != null) {
            json.put("onlineStatus", this.onlineStatus);
        }

        // 商品对应服务地区，多个服务地区用英文逗号分隔
        if (this.destinations != null) {
            json.put("destinations", this.destinations);
        }
        // 新销量
        json.put("showSales", this.showSales);

        return json;
    }

}
  