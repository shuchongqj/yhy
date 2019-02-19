package com.yhy.common.beans.net.model.tm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:ItemParamForCreateOrder
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-12-21
 * Time:19:18
 * Version 1.1.0
 */


public class ItemParamForCreateOrder implements Serializable {
    private static final long serialVersionUID = 7086444658683373369L;

    /**
     * 商品id 必填
     */
    public long itemId;

    /**
     * 商品购买数量 必填
     */
    public long buyAmount;

    /**
     * 卖家id
     */
    public long sellerId;

    /**
     * sku列表 有sku时必填
     */
    public List<ItemSku> skuList;
    /**
     * 使用积分数量
     */
    public long usePoint;

    /**
     * 是否是实体商品
     */
    public boolean isEntity;

    /**
     * 商品类型 NORMAL:普通商品交易/LINE：线路商品/HOTEL：酒店商品/SPOTS：景区门票/FLIGHT_HOTEL：机酒套餐/SPOTS_HOTEL:景酒套餐/ACTIVITY:活动商品/MEMBER_RECHARGE：会员充值/TOUR_LINE:跟团游/FREE_LINE:自由行/CITY_ACTIVITY:同城活动
     */
    public String itemType;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ItemParamForCreateOrder deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ItemParamForCreateOrder deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ItemParamForCreateOrder result = new ItemParamForCreateOrder();

            // 商品id 必填
            result.itemId = json.optLong("itemId");
            // 商品购买数量 必填
            result.buyAmount = json.optLong("buyAmount");
            // 卖家id
            result.sellerId = json.optLong("sellerId");
            // sku列表 有sku时必填
            JSONArray skuListArray = json.optJSONArray("skuList");
            if (skuListArray != null) {
                int len = skuListArray.length();
                result.skuList = new ArrayList<ItemSku>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = skuListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.skuList.add(ItemSku.deserialize(jo));
                    }
                }
            }

            // 使用积分数量
            result.usePoint = json.optLong("usePoint");
            // 是否是实体商品
            result.isEntity = json.optBoolean("isEntity");
            // 商品类型 NORMAL:普通商品交易/LINE：线路商品/HOTEL：酒店商品/SPOTS：景区门票/FLIGHT_HOTEL：机酒套餐/SPOTS_HOTEL:景酒套餐/ACTIVITY:活动商品/MEMBER_RECHARGE：会员充值/TOUR_LINE:跟团游/FREE_LINE:自由行/CITY_ACTIVITY:同城活动

            if(!json.isNull("itemType")){
                result.itemType = json.optString("itemType", null);
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

        // 商品id 必填
        json.put("itemId", this.itemId);

        // 商品购买数量 必填
        json.put("buyAmount", this.buyAmount);

        // 卖家id
        json.put("sellerId", this.sellerId);

        // sku列表 有sku时必填
        if (this.skuList != null) {
            JSONArray skuListArray = new JSONArray();
            for (ItemSku value : this.skuList)
            {
                if (value != null) {
                    skuListArray.put(value.serialize());
                }
            }
            json.put("skuList", skuListArray);
        }

        // 使用积分数量
        json.put("usePoint", this.usePoint);

        // 是否是实体商品
        json.put("isEntity", this.isEntity);

        // 商品类型 NORMAL:普通商品交易/LINE：线路商品/HOTEL：酒店商品/SPOTS：景区门票/FLIGHT_HOTEL：机酒套餐/SPOTS_HOTEL:景酒套餐/ACTIVITY:活动商品/MEMBER_RECHARGE：会员充值/TOUR_LINE:跟团游/FREE_LINE:自由行/CITY_ACTIVITY:同城活动
        if(this.itemType != null) { json.put("itemType", this.itemType); }

        return json;
    }
}
