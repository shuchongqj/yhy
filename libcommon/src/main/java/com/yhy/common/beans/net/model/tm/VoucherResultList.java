package com.yhy.common.beans.net.model.tm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:VoucherResult_ArrayResp
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-29
 * Time:10:29
 * Version 1.0
 * Description:
 */
public class VoucherResultList implements Serializable{

    private static final long serialVersionUID = 6652386952520918295L;
    /**
     * 用户优惠券
     */
    public List<VoucherResult> value;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static VoucherResultList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static VoucherResultList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            VoucherResultList result = new VoucherResultList();

            // 用户优惠券
            JSONArray valueArray = json.optJSONArray("value");
            if (valueArray != null) {
                int len = valueArray.length();
                result.value = new ArrayList<VoucherResult>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = valueArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.value.add(VoucherResult.deserialize(jo));
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

        // 用户优惠券
        if (this.value != null) {
            JSONArray valueArray = new JSONArray();
            for (VoucherResult value : this.value)
            {
                if (value != null) {
                    valueArray.put(value.serialize());
                }
            }
            json.put("value", valueArray);
        }

        return json;
    }
}
