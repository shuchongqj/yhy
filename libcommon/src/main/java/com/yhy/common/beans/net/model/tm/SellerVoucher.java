package com.yhy.common.beans.net.model.tm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:SellerVoucher
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-29
 * Time:10:37
 * Version 1.0
 * Description:
 */
public class SellerVoucher implements Serializable{

    /**
     * 店铺id
     */
    public long sellerId;

    /**
     * 店铺优惠券
     */
    public List<VoucherResult> sellerVoucherList;
    /**
     * 店铺具体的商品的优惠券
     */
    public EntityVoucher entityVoucher;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static SellerVoucher deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static SellerVoucher deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            SellerVoucher result = new SellerVoucher();

            // 店铺id
            result.sellerId = json.optLong("sellerId");
            // 店铺优惠券
            JSONArray sellerVoucherListArray = json.optJSONArray("sellerVoucherList");
            if (sellerVoucherListArray != null) {
                int len = sellerVoucherListArray.length();
                result.sellerVoucherList = new ArrayList<VoucherResult>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = sellerVoucherListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.sellerVoucherList.add(VoucherResult.deserialize(jo));
                    }
                }
            }

            // 店铺具体的商品的优惠券
            result.entityVoucher = EntityVoucher.deserialize(json.optJSONObject("entityVoucher"));
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 店铺id
        json.put("sellerId", this.sellerId);

        // 店铺优惠券
        if (this.sellerVoucherList != null) {
            JSONArray sellerVoucherListArray = new JSONArray();
            for (VoucherResult value : this.sellerVoucherList)
            {
                if (value != null) {
                    sellerVoucherListArray.put(value.serialize());
                }
            }
            json.put("sellerVoucherList", sellerVoucherListArray);
        }

        // 店铺具体的商品的优惠券
        if (this.entityVoucher != null) { json.put("entityVoucher", this.entityVoucher.serialize()); }

        return json;
    }

}
