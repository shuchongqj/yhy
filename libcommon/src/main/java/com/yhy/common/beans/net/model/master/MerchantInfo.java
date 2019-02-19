// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.master;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class MerchantInfo implements Serializable {

    private static final long serialVersionUID = -330481390064743415L;
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
     * 人均消费
     */
    public long avgprice;

    /**
     * 店铺图标
     */
    public String icon;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static MerchantInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static MerchantInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            MerchantInfo result = new MerchantInfo();

            // 卖家Id
            result.sellerId = json.optLong("sellerId");
            // 店铺名称

            if (!json.isNull("name")) {
                result.name = json.optString("name", null);
            }
            // 城市code

            if (!json.isNull("cityCode")) {
                result.cityCode = json.optString("cityCode", null);
            }
            // 城市名称

            if (!json.isNull("city")) {
                result.city = json.optString("city", null);
            }
            // 人均消费
            result.avgprice = json.optLong("avgprice");
            // 店铺图标

            if (!json.isNull("icon")) {
                result.icon = json.optString("icon", null);
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
        if (this.name != null) {
            json.put("name", this.name);
        }

        // 城市code
        if (this.cityCode != null) {
            json.put("cityCode", this.cityCode);
        }

        // 城市名称
        if (this.city != null) {
            json.put("city", this.city);
        }

        // 人均消费
        json.put("avgprice", this.avgprice);

        // 店铺图标
        if (this.icon != null) {
            json.put("icon", this.icon);
        }

        return json;
    }
}
  