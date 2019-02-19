package com.yhy.common.beans.net.model.tm;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:CreateCartInfo
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-10-25
 * Time:9:52
 * Version 1.1.0
 */


public class CreateCartInfo implements Serializable {
    private static final long serialVersionUID = 6466585805390557603L;


    /**
     * 商品名称
     */
    public String title;

    /**
     * 图片集
     */
    public List<String> picList;
    /**
     * 商品id
     */
    public long itemId;

    /**
     * 商品类型
     */
    public String itemType;

    /**
     * skuId
     */
    public long skuId;

    /**
     * 卖家id
     */
    public long sellerId;

    /**
     * 购买数量
     */
    public long amount;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static CreateCartInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static CreateCartInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            CreateCartInfo result = new CreateCartInfo();
            // 商品名称

            if (!json.isNull("title")) {
                result.title = json.optString("title", null);
            }
            // 图片集
            JSONArray picListArray = json.optJSONArray("picList");
            if (picListArray != null) {
                int len = picListArray.length();
                result.picList = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if (!picListArray.isNull(i)) {
                        result.picList.add(picListArray.optString(i, null));
                    } else {
                        result.picList.add(i, null);
                    }

                }
            }

            // 商品id
            result.itemId = json.optLong("itemId");
            // 商品类型
            if (!json.isNull("itemType")) {
                result.itemType = json.optString("itemType", null);
            }
            // skuId
            result.skuId = json.optLong("skuId");
            // 卖家id
            result.sellerId = json.optLong("sellerId");
            // 购买数量
            result.amount = json.optLong("amount");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 商品名称
        if (this.title != null) {
            json.put("title", this.title);
        }

        // 图片集
        if (this.picList != null) {
            JSONArray picListArray = new JSONArray();
            for (String value : this.picList) {
                picListArray.put(value);
            }
            json.put("picList", picListArray);
        }

        // 商品id
        json.put("itemId", this.itemId);

        // 商品名称
        if (this.itemType != null) {
            json.put("itemType", this.itemType);
        }

        // skuId
        json.put("skuId", this.skuId);

        // 卖家id
        json.put("sellerId", this.sellerId);

        // 购买数量
        json.put("amount", this.amount);

        return json;
    }

}
