// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.master;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Merchant implements Serializable {

    private static final long serialVersionUID = 94083891766082636L;
    /**
     * 卖家Id
     */
    public long sellerId;

    /**
     * 店铺名称
     */
    public String name;

    /**
     * 城市code
     */
    public String cityCode;

    /**
     * 城市名称
     */
    public String city;

    /**
     * 营业时间
     */
    public String serviceTime;

    /**
     * 人均消费
     */
    public long avgprice;

    /**
     * 客服电话
     */
    public String serviceTel;

    /**
     * 店铺图标
     */
    public String icon;

    /**
     * 店铺背景图
     */
    public String backPic;

    /**
     * 经度
     */
    public double longitude;

    /**
     * 纬度
     */
    public double latitude;

    /**
     * 商铺地址
     */
    public String address;

    /**
     * 店铺技能类型
     */
    public int certificateType;

    /**
     * 店铺提供服务
     */
    public List<MasterCertificates> certificates;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Merchant deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Merchant deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            Merchant result = new Merchant();

            // 卖家Id
            result.sellerId = json.optLong("sellerId");
            // 店铺名称

            if(!json.isNull("name")){
                result.name = json.optString("name", null);
            }
            // 城市code

            if(!json.isNull("cityCode")){
                result.cityCode = json.optString("cityCode", null);
            }
            // 城市名称

            if(!json.isNull("city")){
                result.city = json.optString("city", null);
            }
            // 营业时间

            if(!json.isNull("serviceTime")){
                result.serviceTime = json.optString("serviceTime", null);
            }
            // 人均消费
            result.avgprice = json.optLong("avgprice");
            // 客服电话

            if(!json.isNull("serviceTel")){
                result.serviceTel = json.optString("serviceTel", null);
            }
            // 店铺图标

            if(!json.isNull("icon")){
                result.icon = json.optString("icon", null);
            }
            // 店铺背景图

            if(!json.isNull("backPic")){
                result.backPic = json.optString("backPic", null);
            }
            // 经度
            result.longitude = json.optDouble("longitude");
            // 纬度
            result.latitude = json.optDouble("latitude");
            // 商铺地址

            if(!json.isNull("address")){
                result.address = json.optString("address", null);
            }
            // 店铺技能类型
            result.certificateType = json.optInt("certificateType");
            // 店铺提供服务
            JSONArray certificatesArray = json.optJSONArray("certificates");
            if (certificatesArray != null) {
                int len = certificatesArray.length();
                result.certificates = new ArrayList<MasterCertificates>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = certificatesArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.certificates.add(MasterCertificates.deserialize(jo));
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

        // 卖家Id
        json.put("sellerId", this.sellerId);

        // 店铺名称
        if(this.name != null) { json.put("name", this.name); }

        // 城市code
        if(this.cityCode != null) { json.put("cityCode", this.cityCode); }

        // 城市名称
        if(this.city != null) { json.put("city", this.city); }

        // 营业时间
        if(this.serviceTime != null) { json.put("serviceTime", this.serviceTime); }

        // 人均消费
        json.put("avgprice", this.avgprice);

        // 客服电话
        if(this.serviceTel != null) { json.put("serviceTel", this.serviceTel); }

        // 店铺图标
        if(this.icon != null) { json.put("icon", this.icon); }

        // 店铺背景图
        if(this.backPic != null) { json.put("backPic", this.backPic); }

        // 经度
        json.put("longitude", this.longitude);

        // 纬度
        json.put("latitude", this.latitude);

        // 商铺地址
        if(this.address != null) { json.put("address", this.address); }

        // 店铺技能类型
        json.put("certificateType", this.certificateType);

        // 店铺提供服务 
        if (this.certificates != null) {
            JSONArray certificatesArray = new JSONArray();
            for (MasterCertificates value : this.certificates)
            {
                if (value != null) {
                    certificatesArray.put(value.serialize());
                }
            }
            json.put("certificates", certificatesArray);
        }

        return json;
    }


}
  