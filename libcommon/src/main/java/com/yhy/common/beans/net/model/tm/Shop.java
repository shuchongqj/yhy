package com.yhy.common.beans.net.model.tm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:Shop
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-12-21
 * Time:15:14
 * Version 1.1.0
 */


public class Shop implements Serializable {
    private static final long serialVersionUID = 5859497368073538816L;

    /**
     * 子订单详情
     */
    public List<OrderItem> orderItemList;
    /**
     * 卖家id
     */
    public long sellerId;

    /**
     * 卖家名称
     */
    public String sellerName;

    /**
     * 卖家图片
     */
    public String sellerLogo;

    /**
     * 订单类型
     */
    public int orderBizType;

    /**
     * 店铺订单实际支付金额
     */
    public long actualTotalFee;

    /**
     * 店铺订单原价总金额
     */
    public long originalTotalFee;

    /**
     * 店铺订单参与优惠的金额，单位分，如优惠共减了10元则为1000
     */
    public long promotionDiscountFee;

    /**
     * 可选店铺优惠集合
     */
    public List<Promotion> promotionDTOList;
    /**
     * 所选店铺优惠的id
     */
    public long selectedPromotionId;

    /**
     * 选择的店铺优惠减免金额
     */
    public long selectedPromotionDiscountFee;

    public VoucherResult voucherResult;

    public String mLeaveMessage;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Shop deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Shop deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            Shop result = new Shop();

            // 子订单详情
            JSONArray orderItemListArray = json.optJSONArray("orderItemList");
            if (orderItemListArray != null) {
                int len = orderItemListArray.length();
                result.orderItemList = new ArrayList<OrderItem>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = orderItemListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.orderItemList.add(OrderItem.deserialize(jo));
                    }
                }
            }

            // 卖家id
            result.sellerId = json.optLong("sellerId");
            // 卖家名称

            if (!json.isNull("sellerName")) {
                result.sellerName = json.optString("sellerName", null);
            }
            // 卖家图片

            if (!json.isNull("sellerLogo")) {
                result.sellerLogo = json.optString("sellerLogo", null);
            }
            // 订单类型
            result.orderBizType = json.optInt("orderBizType");
            // 店铺订单实际支付金额
            result.actualTotalFee = json.optLong("actualTotalFee");
            // 店铺订单原价总金额
            result.originalTotalFee = json.optLong("originalTotalFee");
            // 店铺订单参与优惠的金额，单位分，如优惠共减了10元则为1000
            result.promotionDiscountFee = json.optLong("promotionDiscountFee");
            // 可选店铺优惠集合
            JSONArray promotionDTOListArray = json.optJSONArray("promotionDTOList");
            if (promotionDTOListArray != null) {
                int len = promotionDTOListArray.length();
                result.promotionDTOList = new ArrayList<Promotion>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = promotionDTOListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.promotionDTOList.add(Promotion.deserialize(jo));
                    }
                }
            }

            // 所选店铺优惠的id
            result.selectedPromotionId = json.optLong("selectedPromotionId");
            // 选择的店铺优惠减免金额
            result.selectedPromotionDiscountFee = json.optLong("selectedPromotionDiscountFee");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 子订单详情
        if (this.orderItemList != null) {
            JSONArray orderItemListArray = new JSONArray();
            for (OrderItem value : this.orderItemList) {
                if (value != null) {
                    orderItemListArray.put(value.serialize());
                }
            }
            json.put("orderItemList", orderItemListArray);
        }

        // 卖家id
        json.put("sellerId", this.sellerId);

        // 卖家名称
        if (this.sellerName != null) {
            json.put("sellerName", this.sellerName);
        }

        // 卖家图片
        if (this.sellerLogo != null) {
            json.put("sellerLogo", this.sellerLogo);
        }

        // 订单类型
        json.put("orderBizType", this.orderBizType);

        // 店铺订单实际支付金额
        json.put("actualTotalFee", this.actualTotalFee);

        // 店铺订单原价总金额
        json.put("originalTotalFee", this.originalTotalFee);

        // 店铺订单参与优惠的金额，单位分，如优惠共减了10元则为1000
        json.put("promotionDiscountFee", this.promotionDiscountFee);

        // 可选店铺优惠集合
        if (this.promotionDTOList != null) {
            JSONArray promotionDTOListArray = new JSONArray();
            for (Promotion value : this.promotionDTOList) {
                if (value != null) {
                    promotionDTOListArray.put(value.serialize());
                }
            }
            json.put("promotionDTOList", promotionDTOListArray);
        }

        // 所选店铺优惠的id
        json.put("selectedPromotionId", this.selectedPromotionId);

        // 选择的店铺优惠减免金额
        json.put("selectedPromotionDiscountFee", this.selectedPromotionDiscountFee);

        return json;
    }
}
