package com.yhy.common.beans.net.model.master;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:MerchantDesc
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-23
 * Time:17:10
 * Version 1.1.0
 */

public class MerchantDesc implements Serializable {
    private static final long serialVersionUID = 2638455407925755144L;

    /**
     * 店铺简介
     */
    public String shopDesc;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static MerchantDesc deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static MerchantDesc deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            MerchantDesc result = new MerchantDesc();

            // 店铺简介

            if(!json.isNull("shopDesc")){
                result.shopDesc = json.optString("shopDesc", null);
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

        // 店铺简介
        if(this.shopDesc != null) { json.put("shopDesc", this.shopDesc); }

        return json;
    }
}
