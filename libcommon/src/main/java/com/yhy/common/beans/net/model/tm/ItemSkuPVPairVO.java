package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:ItemSkuPVPairVO
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2015-11-27
 * Time:15:28
 * Version 1.0
 * Description:
 */
public class ItemSkuPVPairVO implements Serializable{

    private static final long serialVersionUID = -2470036448327483139L;
    /**
     * SKU属性id
     */
    public long pId;

    /**
     * SKU属性名称
     */
    public String pTxt;

    /**
     * SKU属性类型 TEXT:普通文字/DATE:普通日期/START_DATE:出发日期/PERSON_TYPE:人员类型/LINE_PACKAGE:套餐/ACTIVITY_TIME:活动时间/ACTIVITY_CONTENT:活动内容/
     */
    public String pType;

    /**
     * SKU属性值id
     */
    public long vId;

    /**
     * SKU属性值内容
     */
    public String vTxt;

    /**
     * SKU属性值类型  ADULT:成人/SINGLE_ROOM:单房差
     */
    public String vType;


    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ItemSkuPVPairVO deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ItemSkuPVPairVO deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ItemSkuPVPairVO result = new ItemSkuPVPairVO();

            // SKU属性id
            result.pId = json.optLong("pId");
            // SKU属性名称

            if(!json.isNull("pTxt")){
                result.pTxt = json.optString("pTxt", null);
            }
            // SKU属性类型 TEXT:普通文字/DATE:普通日期/START_DATE:出发日期/PERSON_TYPE:人员类型/LINE_PACKAGE:套餐/ACTIVITY_TIME:活动时间/ACTIVITY_CONTENT:活动内容/

            if(!json.isNull("pType")){
                result.pType = json.optString("pType", null);
            }
            // SKU属性值id
            result.vId = json.optLong("vId");
            // SKU属性值内容

            if(!json.isNull("vTxt")){
                result.vTxt = json.optString("vTxt", null);
            }

            // SKU属性值类型  ADULT:成人/SINGLE_ROOM:单房差

            if(!json.isNull("vType")){
                result.vType = json.optString("vType", null);
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

        // SKU属性id
        json.put("pId", this.pId);

        // SKU属性名称
        if(this.pTxt != null) { json.put("pTxt", this.pTxt); }

        // SKU属性类型 TEXT:普通文字/DATE:普通日期/START_DATE:出发日期/PERSON_TYPE:人员类型/LINE_PACKAGE:套餐/ACTIVITY_TIME:活动时间/ACTIVITY_CONTENT:活动内容/
        if(this.pType != null) { json.put("pType", this.pType); }

        // SKU属性值id
        json.put("vId", this.vId);

        // SKU属性值内容
        if(this.vTxt != null) { json.put("vTxt", this.vTxt); }

        // SKU属性值类型  ADULT:成人/SINGLE_ROOM:单房差
        if(this.vType != null) { json.put("vType", this.vType); }

        return json;
    }

}
