package com.yhy.common.beans.net.model.tm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:DeleteCartInfo
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-10-25
 * Time:9:35
 * Version 1.1.0
 */


public class DeleteCartInfo implements Serializable {
    private static final long serialVersionUID = -3505690446906007219L;

    /**
     * 购物车信息
     */
    public List<CartIdsInfo> cartIdsInfoList;
    /**
     * 用户id
     */
    public long buyUserId;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static DeleteCartInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static DeleteCartInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            DeleteCartInfo result = new DeleteCartInfo();

            // 购物车信息
            JSONArray cartIdsInfoListArray = json.optJSONArray("cartIdsInfoList");
            if (cartIdsInfoListArray != null) {
                int len = cartIdsInfoListArray.length();
                result.cartIdsInfoList = new ArrayList<CartIdsInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = cartIdsInfoListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.cartIdsInfoList.add(CartIdsInfo.deserialize(jo));
                    }
                }
            }

            // 用户id
            result.buyUserId = json.optLong("buyUserId");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 购物车信息
        if (this.cartIdsInfoList != null) {
            JSONArray cartIdsInfoListArray = new JSONArray();
            for (CartIdsInfo value : this.cartIdsInfoList)
            {
                if (value != null) {
                    cartIdsInfoListArray.put(value.serialize());
                }
            }
            json.put("cartIdsInfoList", cartIdsInfoListArray);
        }

        // 用户id
        json.put("buyUserId", this.buyUserId);

        return json;
    }

}
