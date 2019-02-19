// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TmItemSku implements Serializable {

    private static final long serialVersionUID = -2883929154317796742L;
    /**
     * skuID
     */
    public long skuId;

    /**
     * 购买数量
     */
    public int num;

    public long price;

    public long time;

    public String title;

    public TmItemSku(long skuId, int num, long price, long time) {
        this.skuId = skuId;
        this.num = num;
        this.price = price;
        this.time = time;
    }

    public TmItemSku() {
    }

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmItemSku deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * ba
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmItemSku deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmItemSku result = new TmItemSku();

            // skuID
            result.skuId = json.optLong("skuId");
            // 购买数量
            result.num = json.optInt("num");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // skuID
        json.put("skuId", this.skuId);

        // 购买数量
        json.put("num", this.num);

        return json;
    }
}
  