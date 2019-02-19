// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;


import com.yhy.common.beans.net.model.trip.PropertyVO;
import com.yhy.common.beans.net.model.trip.SalesPropertyVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TmItemInfo implements Serializable {

    private static final long serialVersionUID = 4336192864347321910L;
    /**
     * 商品id
     */
    public long id;

    /**
     * 商品类目id
     */
    public long categoryId;

    /**
     * 卖家id
     */
    public long sellerId;

    /**
     * 标题
     */
    public String title;

    /**
     * 短标题
     */
    public String subTitle;

    /**
     * 一句话描述
     */
    public String oneWord;

    /**
     * 商品简介/退订规则
     */
    public String description;

    /**
     * 销售库存
     */
    public int stockNum;

    /**
     * 商品描述url
     */
    public String detailUrl;

    /**
     * 合同条款url
     */
    public String agreementUrl;

    /**
     * 商品列表图
     */
    public String itemPic;

    /**
     * 详情图地址
     */
    public List<String> picUrls;
    /**
     * 市场价格
     */
    public long marketPrice;

    /**
     * 会员价格
     */
    public long memberPrice;

    /**
     * 优惠价格 此项大于等于0有效
     */
    public long discountPrice;

    /**
     * 外部ID
     */
    public long outerId;

    /**
     * 外部类型 酒店:HOTEL/景区:SCENIC/线路:Line
     */
    public String outerType;

    /**
     * 点赞数
     */
    public int likes;

    /**
     * 点赞状态
     */
    public String likeStatus;

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
     * 单个商品最大可用积分
     */
    public long maxUsePoint;

    /**
     * 商品类型 NORMAL:普通商品交易/LINE：线路商品/HOTEL：酒店商品/SPOTS：景区门票/FLIGHT_HOTEL：机酒套餐/SPOTS_HOTEL:景酒套餐/ACTIVITY:活动商品/MEMBER_RECHARGE：会员充值
     */
    public String itemType;

    /**
     * 是否有sku
     */
    public boolean hasSku;

    /**
     * 退款要求 FREE_REFUND:免费退 CAN_REFUND:可退 CANNOT_REFUND:不可退
     */
    public String refundRequirement;

    /**
     * 早餐情况 NONE:不含早 SINGLE:含早 DOUBLE:含双早 MULTI:含多早
     */
    public String hasBreakfast;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmItemInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmItemInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmItemInfo result = new TmItemInfo();

            // 商品id
            result.id = json.optLong("id");
            // 商品类目id
            result.categoryId = json.optLong("categoryId");
            // 卖家id
            result.sellerId = json.optLong("sellerId");
            // 标题

            if(!json.isNull("title")){
                result.title = json.optString("title", null);
            }
            // 短标题

            if(!json.isNull("subTitle")){
                result.subTitle = json.optString("subTitle", null);
            }
            // 一句话描述

            if(!json.isNull("oneWord")){
                result.oneWord = json.optString("oneWord", null);
            }
            // 商品简介/退订规则

            if(!json.isNull("description")){
                result.description = json.optString("description", null);
            }
            // 销售库存
            result.stockNum = json.optInt("stockNum");
            // 商品描述url

            if(!json.isNull("detailUrl")){
                result.detailUrl = json.optString("detailUrl", null);
            }
            // 合同条款url

            if(!json.isNull("agreementUrl")){
                result.agreementUrl = json.optString("agreementUrl", null);
            }
            // 商品列表图

            if(!json.isNull("itemPic")){
                result.itemPic = json.optString("itemPic", null);
            }
            // 详情图地址
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

            // 市场价格
            result.marketPrice = json.optLong("marketPrice");
            // 会员价格
            result.memberPrice = json.optLong("memberPrice");
            // 优惠价格 此项大于等于0有效
            result.discountPrice = json.optLong("discountPrice");
            // 外部ID
            result.outerId = json.optLong("outerId");
            // 外部类型 酒店:HOTEL/景区:SCENIC/线路:Line

            if(!json.isNull("outerType")){
                result.outerType = json.optString("outerType", null);
            }
            // 点赞数
            result.likes = json.optInt("likes");
            // 点赞状态

            if(!json.isNull("likeStatus")){
                result.likeStatus = json.optString("likeStatus", null);
            }
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

            // 单个商品最大可用积分
            result.maxUsePoint = json.optLong("maxUsePoint");

            // 商品类型 NORMAL:普通商品交易/LINE：线路商品/HOTEL：酒店商品/SPOTS：景区门票/FLIGHT_HOTEL：机酒套餐/SPOTS_HOTEL:景酒套餐/ACTIVITY:活动商品/MEMBER_RECHARGE：会员充值

            if(!json.isNull("itemType")){
                result.itemType = json.optString("itemType", null);
            }
            // 是否有sku
            result.hasSku = json.optBoolean("hasSku");
            // 退款要求 FREE_REFUND:免费退 CAN_REFUND:可退 CANNOT_REFUND:不可退

            if(!json.isNull("refundRequirement")){
                result.refundRequirement = json.optString("refundRequirement", null);
            }
            // 早餐情况 NONE:不含早 SINGLE:含早 DOUBLE:含双早 MULTI:含多早

            if(!json.isNull("hasBreakfast")){
                result.hasBreakfast = json.optString("hasBreakfast", null);
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

        // 商品id
        json.put("id", this.id);

        // 商品类目id
        json.put("categoryId", this.categoryId);

        // 卖家id
        json.put("sellerId", this.sellerId);

        // 标题
        if(this.title != null) { json.put("title", this.title); }

        // 短标题
        if(this.subTitle != null) { json.put("subTitle", this.subTitle); }

        // 一句话描述
        if(this.oneWord != null) { json.put("oneWord", this.oneWord); }

        // 商品简介/退订规则
        if(this.description != null) { json.put("description", this.description); }

        // 销售库存
        json.put("stockNum", this.stockNum);

        // 商品描述url
        if(this.detailUrl != null) { json.put("detailUrl", this.detailUrl); }

        // 合同条款url
        if(this.agreementUrl != null) { json.put("agreementUrl", this.agreementUrl); }

        // 商品列表图
        if(this.itemPic != null) { json.put("itemPic", this.itemPic); }

        // 详情图地址
        if (this.picUrls != null) {
            JSONArray picUrlsArray = new JSONArray();
            for (String value : this.picUrls)
            {
                picUrlsArray.put(value);
            }
            json.put("picUrls", picUrlsArray);
        }

        // 市场价格
        json.put("marketPrice", this.marketPrice);

        // 会员价格
        json.put("memberPrice", this.memberPrice);

        // 优惠价格 此项大于等于0有效
        json.put("discountPrice", this.discountPrice);

        // 外部ID
        json.put("outerId", this.outerId);

        // 外部类型 酒店:HOTEL/景区:SCENIC/线路:Line
        if(this.outerType != null) { json.put("outerType", this.outerType); }

        // 点赞数
        json.put("likes", this.likes);

        // 点赞状态
        if(this.likeStatus != null) { json.put("likeStatus", this.likeStatus); }

        // SKU列表
        if (this.skuList != null) {
            JSONArray skuListArray = new JSONArray();
            for (ItemSkuVO value : this.skuList)
            {
                if (value != null) {
                    skuListArray.put(value.serialize());
                }
            }
            json.put("skuList", skuListArray);
        }

        // 非销售属性列表
        if (this.propertyList != null) {
            JSONArray propertyListArray = new JSONArray();
            for (PropertyVO value : this.propertyList)
            {
                if (value != null) {
                    propertyListArray.put(value.serialize());
                }
            }
            json.put("propertyList", propertyListArray);
        }

        // 销售属性列表
        if (this.salesPropertyList != null) {
            JSONArray salesPropertyListArray = new JSONArray();
            for (SalesPropertyVO value : this.salesPropertyList)
            {
                if (value != null) {
                    salesPropertyListArray.put(value.serialize());
                }
            }
            json.put("salesPropertyList", salesPropertyListArray);
        }

        // 单个商品最大可用积分
        json.put("maxUsePoint", this.maxUsePoint);

        // 商品类型 NORMAL:普通商品交易/LINE：线路商品/HOTEL：酒店商品/SPOTS：景区门票/FLIGHT_HOTEL：机酒套餐/SPOTS_HOTEL:景酒套餐/ACTIVITY:活动商品/MEMBER_RECHARGE：会员充值
        if(this.itemType != null) { json.put("itemType", this.itemType); }

        // 是否有sku
        json.put("hasSku", this.hasSku);

        // 退款要求 FREE_REFUND:免费退 CAN_REFUND:可退 CANNOT_REFUND:不可退
        if(this.refundRequirement != null) { json.put("refundRequirement", this.refundRequirement); }

        // 早餐情况 NONE:不含早 SINGLE:含早 DOUBLE:含双早 MULTI:含多早
        if(this.hasBreakfast != null) { json.put("hasBreakfast", this.hasBreakfast); }

        return json;
    }
}
  