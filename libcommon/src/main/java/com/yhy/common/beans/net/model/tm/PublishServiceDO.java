package com.yhy.common.beans.net.model.tm;


import com.yhy.common.beans.net.model.common.PictureTextItemInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:PublishServiceDO
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-19
 * Time:14:21
 * Version 1.0
 * Description:
 */
public class PublishServiceDO implements Serializable{

    private static final long serialVersionUID = 7917423681080454L;

    /**
     * 头图
     */
    public String avater;

    /**
     * 标题
     */
    public String title;

    /**
     * 类目
     */
    public int categoryType;

    /**
     * 服务区域
     */
    public List<ServiceArea> serviceAreas;
    /**
     * 图文详情
     */
    public List<PictureTextItemInfo> pictureTextItems;
    /**
     * 费用说明
     */
    public String feeDesc;

    /**
     * 预定说明
     */
    public String bookingTip;

    /**
     * 退改规定
     */
    public String refundRule;

    /**
     * 咨询商品属性
     */
    public List<ItemProperty> itemProperties;

    /**
     * 原价
     */
    public long oldPrice;

    /**
     * 原价时间
     */
    public long oldTime;

    /**
     * 优惠价
     */
    public long discountPrice;

    /**
     * 优惠时间
     */
    public long discountTime;
    /**
     * 主键
     */
    public long id;

    /**
     * 商品发布状态1:已发布2:待发布
     */
    public int serviceState;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static PublishServiceDO deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static PublishServiceDO deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            PublishServiceDO result = new PublishServiceDO();

            // 头图

            if(!json.isNull("avater")){
                result.avater = json.optString("avater", null);
            }
            // 标题

            if(!json.isNull("title")){
                result.title = json.optString("title", null);
            }
            // 类目
            result.categoryType = json.optInt("categoryType");
            // 服务区域
            JSONArray TagRelationInfoVOsArray = json.optJSONArray("serviceAreas");
            if (TagRelationInfoVOsArray != null) {
                int len = TagRelationInfoVOsArray.length();
                result.serviceAreas = new ArrayList<ServiceArea>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = TagRelationInfoVOsArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.serviceAreas.add(ServiceArea.deserialize(jo));
                    }
                }
            }

            // 图文详情
            JSONArray pictureTextItemsArray = json.optJSONArray("pictureTextItems");
            if (pictureTextItemsArray != null) {
                int len = pictureTextItemsArray.length();
                result.pictureTextItems = new ArrayList<PictureTextItemInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = pictureTextItemsArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.pictureTextItems.add(PictureTextItemInfo.deserialize(jo));
                    }
                }
            }

            // 费用说明

            if(!json.isNull("feeDesc")){
                result.feeDesc = json.optString("feeDesc", null);
            }
            // 预定说明

            if(!json.isNull("bookingTip")){
                result.bookingTip = json.optString("bookingTip", null);
            }
            // 退改规定

            if(!json.isNull("refundRule")){
                result.refundRule = json.optString("refundRule", null);
            }

            // 咨询商品属性
            JSONArray itemPropertiesArray = json.optJSONArray("itemProperties");
            if (itemPropertiesArray != null) {
                int len = itemPropertiesArray.length();
                result.itemProperties = new ArrayList<ItemProperty>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = itemPropertiesArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.itemProperties.add(ItemProperty.deserialize(jo));
                    }
                }
            }

            // 原价
            result.oldPrice = json.optLong("oldPrice");
            // 原价时间
            result.oldTime = json.optLong("oldTime");
            // 优惠价
            result.discountPrice = json.optLong("discountPrice");
            // 优惠时间
            result.discountTime = json.optLong("discountTime");

            // 主键
            result.id = json.optLong("id");
            // 商品发布状态1:已发布2:待发布
            result.serviceState = json.optInt("serviceState");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 头图
        if(this.avater != null) { json.put("avater", this.avater); }

        // 标题
        if(this.title != null) { json.put("title", this.title); }

        // 类目
        json.put("categoryType", this.categoryType);

        // 服务区域
        if (this.serviceAreas != null) {
            JSONArray TagRelationInfoVOsArray = new JSONArray();
            for (ServiceArea value : this.serviceAreas)
            {
                if (value != null) {
                    TagRelationInfoVOsArray.put(value.serialize());
                }
            }
            json.put("serviceAreas", TagRelationInfoVOsArray);
        }

        // 图文详情
        if (this.pictureTextItems != null) {
            JSONArray pictureTextItemsArray = new JSONArray();
            for (PictureTextItemInfo value : this.pictureTextItems)
            {
                if (value != null) {
                    pictureTextItemsArray.put(value.serialize());
                }
            }
            json.put("pictureTextItems", pictureTextItemsArray);
        }

        // 费用说明
        if(this.feeDesc != null) { json.put("feeDesc", this.feeDesc); }

        // 预定说明
        if(this.bookingTip != null) { json.put("bookingTip", this.bookingTip); }

        // 退改规定
        if(this.refundRule != null) { json.put("refundRule", this.refundRule); }

        // 咨询商品属性
        if (this.itemProperties != null) {
            JSONArray itemPropertiesArray = new JSONArray();
            for (ItemProperty value : this.itemProperties)
            {
                if (value != null) {
                    itemPropertiesArray.put(value.serialize());
                }
            }
            json.put("itemProperties", itemPropertiesArray);
        }

        // 原价
        json.put("oldPrice", this.oldPrice);

        // 原价时间
        json.put("oldTime", this.oldTime);

        // 优惠价
        json.put("discountPrice", this.discountPrice);

        // 优惠时间
        json.put("discountTime", this.discountTime);

        // 主键
        json.put("id", this.id);

        // 商品发布状态1:已发布2:待发布
        json.put("serviceState", this.serviceState);

        return json;
    }

}
