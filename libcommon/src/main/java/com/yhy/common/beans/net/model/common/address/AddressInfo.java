package com.yhy.common.beans.net.model.common.address;// Auto Generated.  DO NOT EDIT!


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class AddressInfo implements Serializable{

    private static final long serialVersionUID = 8453423415161904532L;
    /**
     * 省级
     */
    public String province;

    /**
     * provinceCode
     */
    public String provinceCode;

    /**
     * 市级
     */
    public String city;

    /**
     * cityCode
     */
    public String cityCode;

    /**
     * 区县级
     */
    public String area;

    /**
     * areaCode
     */
    public String areaCode;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static AddressInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static AddressInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            AddressInfo result = new AddressInfo();

            // 省级

            if(!json.isNull("province")){
                result.province = json.optString("province", null);
            }
            // provinceCode

            if(!json.isNull("provinceCode")){
                result.provinceCode = json.optString("provinceCode", null);
            }
            // 市级

            if(!json.isNull("city")){
                result.city = json.optString("city", null);
            }
            // cityCode

            if(!json.isNull("cityCode")){
                result.cityCode = json.optString("cityCode", null);
            }
            // 区县级

            if(!json.isNull("area")){
                result.area = json.optString("area", null);
            }
            // areaCode

            if(!json.isNull("areaCode")){
                result.areaCode = json.optString("areaCode", null);
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

        // 省级
        if(this.province != null) { json.put("province", this.province); }

        // provinceCode
        if(this.provinceCode != null) { json.put("provinceCode", this.provinceCode); }

        // 市级
        if(this.city != null) { json.put("city", this.city); }

        // cityCode
        if(this.cityCode != null) { json.put("cityCode", this.cityCode); }

        // 区县级
        if(this.area != null) { json.put("area", this.area); }

        // areaCode
        if(this.areaCode != null) { json.put("areaCode", this.areaCode); }

        return json;
    }
}
