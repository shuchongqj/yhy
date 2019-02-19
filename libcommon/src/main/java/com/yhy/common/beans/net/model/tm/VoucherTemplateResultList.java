package com.yhy.common.beans.net.model.tm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:VoucherTemplateResult_ArrayResp
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-28
 * Time:20:06
 * Version 1.0
 * Description:
 */
public class VoucherTemplateResultList implements Serializable{

    private static final long serialVersionUID = 7934778620369808285L;
    /**
     * 订单模板
     */
    public List<VoucherTemplateResult> value;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static VoucherTemplateResultList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static VoucherTemplateResultList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
           VoucherTemplateResultList result = new VoucherTemplateResultList();

            // 订单模板
            JSONArray valueArray = json.optJSONArray("value");
            if (valueArray != null) {
                int len = valueArray.length();
                result.value = new ArrayList<VoucherTemplateResult>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = valueArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.value.add(VoucherTemplateResult.deserialize(jo));
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

        // 订单模板
        if (this.value != null) {
            JSONArray valueArray = new JSONArray();
            for (VoucherTemplateResult value : this.value)
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
