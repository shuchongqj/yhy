package com.yhy.common.beans.net.model.tm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:CreateOrderContextParamForPointMall
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-12-21
 * Time:15:06
 * Version 1.1.0
 */


public class CreateOrderContextParamForPointMall implements Serializable {
    private static final long serialVersionUID = -7425866107792058806L;

    /**
     * 购买商品信息
     */
    public List<ItemParamForOrderContext> itemParamList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static CreateOrderContextParamForPointMall deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static CreateOrderContextParamForPointMall deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            CreateOrderContextParamForPointMall result = new CreateOrderContextParamForPointMall();

            // 购买商品信息
            JSONArray itemParamListArray = json.optJSONArray("itemParamList");
            if (itemParamListArray != null) {
                int len = itemParamListArray.length();
                result.itemParamList = new ArrayList<ItemParamForOrderContext>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = itemParamListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.itemParamList.add(ItemParamForOrderContext.deserialize(jo));
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

        // 购买商品信息
        if (this.itemParamList != null) {
            JSONArray itemParamListArray = new JSONArray();
            for (ItemParamForOrderContext value : this.itemParamList)
            {
                if (value != null) {
                    itemParamListArray.put(value.serialize());
                }
            }
            json.put("itemParamList", itemParamListArray);
        }

        return json;
    }
}
