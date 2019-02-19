package com.yhy.common.beans.net.model.tm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:CreateOrderResultTOList
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-12-21
 * Time:19:27
 * Version 1.1.0
 */


public class CreateOrderResultTOList implements Serializable {
    private static final long serialVersionUID = 5631645162560561090L;

    /**
     * 创建订单结果
     */
    public List<TmCreateOrderResultTO> value;


    /**
     * 是否成功
     */
    public boolean success;

    /**
     * 主订单信息
     */
    public List<TmMainOrder> mainOrderList;


    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static CreateOrderResultTOList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static CreateOrderResultTOList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            CreateOrderResultTOList result = new CreateOrderResultTOList();

            // 创建订单结果
            // 是否成功
            result.success = json.optBoolean("success");
            // 主订单信息
//            result.mainOrderList = TmMainOrder.deserialize(json.optJSONObject("mainOrder"));

//            JSONArray valueArray = json.optJSONArray("value");
            JSONArray valueArray = json.optJSONArray("mainOrderList");
            if (valueArray != null) {
                int len = valueArray.length();
                result.mainOrderList = new ArrayList<TmMainOrder>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = valueArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.mainOrderList.add(TmMainOrder.deserialize(jo));
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

        // 创建订单结果
        if (this.value != null) {
            JSONArray valueArray = new JSONArray();
            for (TmCreateOrderResultTO value : this.value) {
                if (value != null) {
                    valueArray.put(value.serialize());
                }
            }
            json.put("value", valueArray);
        }

        return json;
    }
}
