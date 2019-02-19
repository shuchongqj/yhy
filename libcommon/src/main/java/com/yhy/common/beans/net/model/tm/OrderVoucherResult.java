package com.yhy.common.beans.net.model.tm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:OrderVoucherResult
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-29
 * Time:10:36
 * Version 1.0
 * Description:
 */
public class OrderVoucherResult implements Serializable{

    private static final long serialVersionUID = 1452070843615316882L;
    /**
     * 店铺优惠券集合
     */
    public List<SellerVoucher> sellerVouchers;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static OrderVoucherResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static OrderVoucherResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            OrderVoucherResult result = new OrderVoucherResult();

            // 店铺优惠券集合
            JSONArray sellerVouchersArray = json.optJSONArray("sellerVouchers");
            if (sellerVouchersArray != null) {
                int len = sellerVouchersArray.length();
                result.sellerVouchers = new ArrayList<SellerVoucher>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = sellerVouchersArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.sellerVouchers.add(SellerVoucher.deserialize(jo));
                    }
                }
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

        // 店铺优惠券集合
        if (this.sellerVouchers != null) {
            JSONArray sellerVouchersArray = new JSONArray();
            for (SellerVoucher value : this.sellerVouchers)
            {
                if (value != null) {
                    sellerVouchersArray.put(value.serialize());
                }
            }
            json.put("sellerVouchers", sellerVouchersArray);
        }

        return json;
    }

}
