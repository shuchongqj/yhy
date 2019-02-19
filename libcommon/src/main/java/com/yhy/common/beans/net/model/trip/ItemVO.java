// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;

import com.yhy.common.beans.net.model.base.BaseShrink;
import com.yhy.common.beans.net.model.tm.ItemSkuVO;
import com.yhy.common.beans.net.model.tm.PayInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemVO extends BaseShrink implements Serializable {

    private static final long serialVersionUID = -7460518711888279681L;
    /**
     * 商品id
     */
    public long id;

    /**
     * 商品类目id
     */
    public long categoryId;

    /**
     * 商品类目名称
     */
    public String categoryName;

    /**
     * 卖家id
     */
    public long sellerId;

    /**
     * 卖家名称
     */
    public String sellerName;

    /**
     * 标题
     */
    public String title;

    /**
     * 短标题
     */
    public String subTitle;

    /**
     * 别名
     */
    public String aliases;

    /**
     * 一句话描述
     */
    public String oneWord;

    /**
     * 商品简介
     */
    public String description;

    /**
     * 销售库存
     */
    public int stockNum;

    /**
     * 推荐指数 0-5
     */
    public int grade;

    /**
     * 商品描述url
     */
    public String detailUrl;

    /**
     * 合同条款url
     */
    public String agreementUrl;

    /**
     * 主图片地址
     */
    public String mainPicUrl;

    /**
     * 订单列表图地址
     */
    public String smallPicUrl;

    /**
     * 辅图地址
     */
    public List<String> picUrls;
    /**
     * 原价
     */
    public long originalPrice;

    /**
     * 实际价格
     */
    public long marketPrice;

    /**
     * 会员价格 - 过期字段
     */
    public long memberPrice;

    /**
     * 优惠价格 - 过期字段
     */
    public long discountPrice;

    /**
     * 外部ID
     */
    public long outerId;

    /**
     * 外部类型 酒店:HOTEL/景区:SCENIC/线路:Line/CITY_ACTIVITY:同城活动
     */
    public String outerType;

    /**
     * 点赞数
     */
    public int likes;

    /**
     * 点赞状态 DELETED未赞/AVAILABLE已赞
     */
    public String likeStatus;

    /**
     * 销量
     */
    public int sales;
    /**
     * 新销量
     */
    public int showSales;

    /**
     * SKU列表
     */
    public List<ItemSkuVO> skuList;
    /**
     * 非销售属性列表
     */
    public List<PropertyVO> propertyList;
    /**
     * 销售属性列表
     */
    public List<SalesPropertyVO> salesPropertyList;
    /**
     * 状态 有效:VALID / 已结束:EXPIRED / 未上架:CREATE / 已下架:INVALID / 已删除:DELETED
     */
    public String status;

    /**
     * 票型ID
     */
    public long ticketId;

    /**
     * 票型名称
     */
    public String ticketTitle;

    /**
     * 今日可定
     */
    public boolean isCanOrderToday;

    /**
     * 早餐情况 NONE:不含早 SINGLE:含早 DOUBLE:含双早 MULTI:含多早
     */
    public String hasBreakfast;

    /**
     * 商品类型 NORMAL:普通商品交易 / LINE:线路商品 / HOTEL:酒店商品 / SPOTS:景区门票 / FLIGHT_HOTEL:机票+酒店 / SPOTS_HOTEL:景点+酒店 / ACTIVITY:活动商品 / MEMBER_RECHARGE:会员充值 / GF:婕珞芙 / TEMP_PRODUCT_CARD:临时产品卡 / TOUR_LINE:跟团游 / FREE_LINE:自由行 / TOUR_LINE_ABOARD:境外跟团游 / FREE_LINE_ABOARD:境外自由行 / CITY_ACTIVITY:同城活动
     */
    public String itemType;

    /**
     * 支付相关信息，如是否可用积分
     */
    public PayInfo payInfo;

    /**
     * 商品对应服务地区，多个服务地区用英文逗号分隔
     */
    public String destinations;

    /**
     * 咨询时间，咨询商品需要字段
     */
    public long consultTime;

    /**
     * 最大价格
     */
    public long maxPrice;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ItemVO deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ItemVO deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ItemVO result = new ItemVO();

            // 商品id
            result.id = json.optLong("id");
            // 商品类目id
            result.categoryId = json.optLong("categoryId");
            // 商品类目名称

            if (!json.isNull("categoryName")) {
                result.categoryName = json.optString("categoryName", null);
            }
            // 卖家id
            result.sellerId = json.optLong("sellerId");
            // 卖家名称

            if (!json.isNull("sellerName")) {
                result.sellerName = json.optString("sellerName", null);
            }
            // 标题

            if (!json.isNull("title")) {
                result.title = json.optString("title", null);
            }
            // 短标题

            if (!json.isNull("subTitle")) {
                result.subTitle = json.optString("subTitle", null);
            }
            // 别名

            if (!json.isNull("aliases")) {
                result.aliases = json.optString("aliases", null);
            }
            // 一句话描述

            if (!json.isNull("oneWord")) {
                result.oneWord = json.optString("oneWord", null);
            }
            // 商品简介

            if (!json.isNull("description")) {
                result.description = json.optString("description", null);
            }
            // 销售库存
            result.stockNum = json.optInt("stockNum");
            // 推荐指数 0-5
            result.grade = json.optInt("grade");
            // 商品描述url

            if (!json.isNull("detailUrl")) {
                result.detailUrl = json.optString("detailUrl", null);
            }
            // 合同条款url

            if (!json.isNull("agreementUrl")) {
                result.agreementUrl = json.optString("agreementUrl", null);
            }
            // 主图片地址

            if (!json.isNull("mainPicUrl")) {
                result.mainPicUrl = json.optString("mainPicUrl", null);
            }
            // 订单列表图地址

            if (!json.isNull("smallPicUrl")) {
                result.smallPicUrl = json.optString("smallPicUrl", null);
            }
            // 辅图地址
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

            // 原价
            result.originalPrice = json.optLong("originalPrice");
            // 实际价格
            result.marketPrice = json.optLong("marketPrice");
            // 会员价格 - 过期字段
            result.memberPrice = json.optLong("memberPrice");
            // 优惠价格 - 过期字段
            result.discountPrice = json.optLong("discountPrice");
            // 外部ID
            result.outerId = json.optLong("outerId");
            // 外部类型 酒店:HOTEL/景区:SCENIC/线路:Line/CITY_ACTIVITY:同城活动

            if (!json.isNull("outerType")) {
                result.outerType = json.optString("outerType", null);
            }
            // 点赞数
            result.likes = json.optInt("likes");
            // 点赞状态 DELETED未赞/AVAILABLE已赞

            if (!json.isNull("likeStatus")) {
                result.likeStatus = json.optString("likeStatus", null);
            }
            // 销量
            result.sales = json.optInt("sales");
            // SKU列表
            JSONArray skuListArray = json.optJSONArray("skuList");
            if (skuListArray != null) {
                int len = skuListArray.length();
                result.skuList = new ArrayList<ItemSkuVO>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = skuListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.skuList.add(ItemSkuVO.deserialize(jo));
                    }
                }
            }

            // 非销售属性列表
            JSONArray propertyListArray = json.optJSONArray("propertyList");
            if (propertyListArray != null) {
                int len = propertyListArray.length();
                result.propertyList = new ArrayList<PropertyVO>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = propertyListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.propertyList.add(PropertyVO.deserialize(jo));
                    }
                }
            }

            // 销售属性列表
            JSONArray salesPropertyListArray = json.optJSONArray("salesPropertyList");
            if (salesPropertyListArray != null) {
                int len = salesPropertyListArray.length();
                result.salesPropertyList = new ArrayList<SalesPropertyVO>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = salesPropertyListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.salesPropertyList.add(SalesPropertyVO.deserialize(jo));
                    }
                }
            }

            // 状态 有效:VALID / 已结束:EXPIRED / 未上架:CREATE / 已下架:INVALID / 已删除:DELETED

            if (!json.isNull("status")) {
                result.status = json.optString("status", null);
            }
            // 票型ID
            result.ticketId = json.optLong("ticketId");
            // 票型名称

            if (!json.isNull("ticketTitle")) {
                result.ticketTitle = json.optString("ticketTitle", null);
            }
            // 今日可定
            result.isCanOrderToday = json.optBoolean("isCanOrderToday");
            // 早餐情况 NONE:不含早 SINGLE:含早 DOUBLE:含双早 MULTI:含多早
            if (!json.isNull("hasBreakfast")) {
                result.hasBreakfast = json.optString("hasBreakfast", null);
            }
            // 商品类型 NORMAL:普通商品交易 / LINE:线路商品 / HOTEL:酒店商品 / SPOTS:景区门票 / FLIGHT_HOTEL:机票+酒店 / SPOTS_HOTEL:景点+酒店 / ACTIVITY:活动商品 / MEMBER_RECHARGE:会员充值 / GF:婕珞芙 / TEMP_PRODUCT_CARD:临时产品卡 / TOUR_LINE:跟团游 / FREE_LINE:自由行 / TOUR_LINE_ABOARD:境外跟团游 / FREE_LINE_ABOARD:境外自由行 / CITY_ACTIVITY:同城活动

            if (!json.isNull("itemType")) {
                result.itemType = json.optString("itemType", null);
            }

            // 支付相关信息，如是否可用积分
            result.payInfo = PayInfo.deserialize(json.optJSONObject("payInfo"));

            // 商品对应服务地区，多个服务地区用英文逗号分隔

            if(!json.isNull("destinations")){
                result.destinations = json.optString("destinations", null);
            }
            // 咨询时间，咨询商品需要字段
            result.consultTime = json.optLong("consultTime");
            // 新销量
            result.showSales = json.optInt("showSales");

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

        // 商品类目id
        json.put("categoryId", this.categoryId);

        // 商品类目名称
        if (this.categoryName != null) {
            json.put("categoryName", this.categoryName);
        }

        // 卖家id
        json.put("sellerId", this.sellerId);

        // 卖家名称
        if (this.sellerName != null) {
            json.put("sellerName", this.sellerName);
        }

        // 标题
        if (this.title != null) {
            json.put("title", this.title);
        }

        // 短标题
        if (this.subTitle != null) {
            json.put("subTitle", this.subTitle);
        }

        // 别名
        if (this.aliases != null) {
            json.put("aliases", this.aliases);
        }

        // 一句话描述
        if (this.oneWord != null) {
            json.put("oneWord", this.oneWord);
        }

        // 商品简介
        if (this.description != null) {
            json.put("description", this.description);
        }

        // 销售库存
        json.put("stockNum", this.stockNum);

        // 推荐指数 0-5
        json.put("grade", this.grade);

        // 商品描述url
        if (this.detailUrl != null) {
            json.put("detailUrl", this.detailUrl);
        }

        // 合同条款url
        if (this.agreementUrl != null) {
            json.put("agreementUrl", this.agreementUrl);
        }

        // 主图片地址
        if (this.mainPicUrl != null) {
            json.put("mainPicUrl", this.mainPicUrl);
        }

        // 订单列表图地址
        if (this.smallPicUrl != null) {
            json.put("smallPicUrl", this.smallPicUrl);
        }

        // 辅图地址 
        if (this.picUrls != null) {
            JSONArray picUrlsArray = new JSONArray();
            for (String value : this.picUrls) {
                picUrlsArray.put(value);
            }
            json.put("picUrls", picUrlsArray);
        }

        // 原价
        json.put("originalPrice", this.originalPrice);

        // 实际价格
        json.put("marketPrice", this.marketPrice);

        // 会员价格 - 过期字段
        json.put("memberPrice", this.memberPrice);

        // 优惠价格 - 过期字段
        json.put("discountPrice", this.discountPrice);

        // 外部ID
        json.put("outerId", this.outerId);

        // 外部类型 酒店:HOTEL/景区:SCENIC/线路:Line/CITY_ACTIVITY:同城活动
        if (this.outerType != null) {
            json.put("outerType", this.outerType);
        }

        // 点赞数
        json.put("likes", this.likes);

        // 点赞状态 DELETED未赞/AVAILABLE已赞
        if (this.likeStatus != null) {
            json.put("likeStatus", this.likeStatus);
        }

        // 销量
        json.put("sales", this.sales);

        // SKU列表 
        if (this.skuList != null) {
            JSONArray skuListArray = new JSONArray();
            for (ItemSkuVO value : this.skuList) {
                if (value != null) {
                    skuListArray.put(value.serialize());
                }
            }
            json.put("skuList", skuListArray);
        }

        // 非销售属性列表 
        if (this.propertyList != null) {
            JSONArray propertyListArray = new JSONArray();
            for (PropertyVO value : this.propertyList) {
                if (value != null) {
                    propertyListArray.put(value.serialize());
                }
            }
            json.put("propertyList", propertyListArray);
        }

        // 销售属性列表 
        if (this.salesPropertyList != null) {
            JSONArray salesPropertyListArray = new JSONArray();
            for (SalesPropertyVO value : this.salesPropertyList) {
                if (value != null) {
                    salesPropertyListArray.put(value.serialize());
                }
            }
            json.put("salesPropertyList", salesPropertyListArray);
        }

        // 状态 有效:VALID / 已结束:EXPIRED / 未上架:CREATE / 已下架:INVALID / 已删除:DELETED
        if (this.status != null) {
            json.put("status", this.status);
        }

        // 票型ID
        json.put("ticketId", this.ticketId);

        // 票型名称
        if (this.ticketTitle != null) {
            json.put("ticketTitle", this.ticketTitle);
        }

        // 今日可定
        json.put("isCanOrderToday", this.isCanOrderToday);

        // 早餐情况 NONE:不含早 SINGLE:含早 DOUBLE:含双早 MULTI:含多早
        if (this.hasBreakfast != null) {
            json.put("hasBreakfast", this.hasBreakfast);
        }
        // 商品类型 NORMAL:普通商品交易 / LINE:线路商品 / HOTEL:酒店商品 / SPOTS:景区门票 / FLIGHT_HOTEL:机票+酒店 / SPOTS_HOTEL:景点+酒店 / ACTIVITY:活动商品 / MEMBER_RECHARGE:会员充值 / GF:婕珞芙 / TEMP_PRODUCT_CARD:临时产品卡 / TOUR_LINE:跟团游 / FREE_LINE:自由行 / TOUR_LINE_ABOARD:境外跟团游 / FREE_LINE_ABOARD:境外自由行 / CITY_ACTIVITY:同城活动
        if (this.itemType != null) {
            json.put("itemType", this.itemType);
        }

        // 支付相关信息，如是否可用积分
        if (this.payInfo != null) {
            json.put("payInfo", this.payInfo.serialize());
        }
        // 商品对应服务地区，多个服务地区用英文逗号分隔
        if(this.destinations != null) { json.put("destinations", this.destinations); }

        // 咨询时间，咨询商品需要字段
        json.put("consultTime", this.consultTime);
        // 新销量
        json.put("showSales", this.showSales);

        return json;
    }

}
  