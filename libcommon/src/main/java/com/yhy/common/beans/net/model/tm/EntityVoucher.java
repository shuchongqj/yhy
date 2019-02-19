package com.yhy.common.beans.net.model.tm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:EntityVoucher
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-29
 * Time:10:38
 * Version 1.0
 * Description:
 */
public class EntityVoucher implements Serializable{

    /**
     * 拥有券的对象
     */
    public OrderItemDTO orderItemDTO;

    /**
     * 券列表
     */
    public List<VoucherResult> voucherResultList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static EntityVoucher deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static EntityVoucher deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            EntityVoucher result = new EntityVoucher();

            // 拥有券的对象
            result.orderItemDTO = OrderItemDTO.deserialize(json.optJSONObject("orderItemDTO"));
            // 券列表
            JSONArray voucherResultListArray = json.optJSONArray("voucherResultList");
            if (voucherResultListArray != null) {
                int len = voucherResultListArray.length();
                result.voucherResultList = new ArrayList<VoucherResult>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = voucherResultListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.voucherResultList.add(VoucherResult.deserialize(jo));
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

        // 拥有券的对象
        if (this.orderItemDTO != null) { json.put("orderItemDTO", this.orderItemDTO.serialize()); }

        // 券列表
        if (this.voucherResultList != null) {
            JSONArray voucherResultListArray = new JSONArray();
            for (VoucherResult value : this.voucherResultList)
            {
                if (value != null) {
                    voucherResultListArray.put(value.serialize());
                }
            }
            json.put("voucherResultList", voucherResultListArray);
        }

        return json;
    }
}
