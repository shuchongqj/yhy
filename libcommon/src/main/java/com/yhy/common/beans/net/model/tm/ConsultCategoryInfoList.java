package com.yhy.common.beans.net.model.tm;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:ConsultCategoryInfoList
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-22
 * Time:19:58
 * Version 1.1.0
 */

public class ConsultCategoryInfoList implements Serializable {
    private static final long serialVersionUID = 4851874802281405345L;

    /**
     * 咨询商品属性集合
     */
    public List<ItemProperty> itemProperties;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ConsultCategoryInfoList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ConsultCategoryInfoList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ConsultCategoryInfoList result = new ConsultCategoryInfoList();

            // 咨询商品属性集合
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

            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 咨询商品属性集合
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

        return json;
    }
}
