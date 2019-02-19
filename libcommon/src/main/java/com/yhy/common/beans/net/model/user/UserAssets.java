package com.yhy.common.beans.net.model.user;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:UserAssets
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-30
 * Time:14:11
 * Version 1.0
 * Description:
 */
public class UserAssets implements Serializable{

    private static final long serialVersionUID = 1310563199544513065L;

    public boolean isClick;
    /**
     * 积分数
     */
    public long point;

    /**
     * 优惠券数
     */
    public int voucherCount;

    /**
     * 粉丝数
     */
    public int fansNumber;

    /**
     * 关注数
     */
    public int followNumber;

    /**
     * 商城文字
     */
    public String pointMallTitle;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static UserAssets deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static UserAssets deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            UserAssets result = new UserAssets();

            // 积分数
            result.point = json.optLong("point");
            // 优惠券数
            result.voucherCount = json.optInt("voucherCount");
            // 粉丝数
            result.fansNumber = json.optInt("fansNumber");
            // 关注数
            result.followNumber = json.optInt("followNumber");
            // 商城文字

            if(!json.isNull("pointMallTitle")){
                result.pointMallTitle = json.optString("pointMallTitle", null);
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

        // 积分数
        json.put("point", this.point);

        // 优惠券数
        json.put("voucherCount", this.voucherCount);

        // 粉丝数
        json.put("fansNumber", this.fansNumber);

        // 关注数
        json.put("followNumber", this.followNumber);

        // 商城文字
        if(this.pointMallTitle != null) { json.put("pointMallTitle", this.pointMallTitle); }

        return json;
    }

}
