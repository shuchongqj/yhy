package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:PackageDetail
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-12-22
 * Time:15:28
 * Version 1.1.0
 */


public class PackageDetail implements Serializable {
    private static final long serialVersionUID = 1713904463827086541L;
    /**
     * 包裹信息
     */
    public PackageResult packageRet;

    /**
     * 商品图片
     */
    public String itemPic;

    /**
     * 商品数量
     */
    public long itemNum;

    /**
     * 物流信息
     */
    public ExpressInfo expressInfo;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static PackageDetail deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static PackageDetail deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            PackageDetail result = new PackageDetail();

            // 包裹信息
            result.packageRet = PackageResult.deserialize(json.optJSONObject("packageRet"));
            // 商品图片

            if(!json.isNull("itemPic")){
                result.itemPic = json.optString("itemPic", null);
            }
            // 商品数量
            result.itemNum = json.optLong("itemNum");
            // 物流信息
            result.expressInfo = ExpressInfo.deserialize(json.optJSONObject("expressInfo"));
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 包裹信息
        if (this.packageRet != null) { json.put("packageRet", this.packageRet.serialize()); }

        // 商品图片
        if(this.itemPic != null) { json.put("itemPic", this.itemPic); }

        // 商品数量
        json.put("itemNum", this.itemNum);

        // 物流信息
        if (this.expressInfo != null) { json.put("expressInfo", this.expressInfo.serialize()); }

        return json;
    }
}
