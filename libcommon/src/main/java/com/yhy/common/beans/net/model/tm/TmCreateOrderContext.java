// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TmCreateOrderContext implements Serializable {

    private static final long serialVersionUID = 4471133540173058865L;
    /**
     * 默认收货地址，如果没有返回null
     */
    public Address defaultAddress;

    /**
     * 起始时间
     */
    public long startTime;

    /**
     * 截止时间
     */
    public long endTime;

    /**
     * 最晚到店时间 String列表
     */
    public List<String> latestArriveTime;
    /**
     * 线路成人属性值VID
     */
    public long lineAdultVid;

    /**
     * 线路单房差属性值VID
     */
    public long lineSingleRoomVid;

    /**
     * 商品相关信息
     */
    public TmItemInfo itemInfo;

    /**
     * 入离政策
     */
    public String hotelPolicy;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmCreateOrderContext deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmCreateOrderContext deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmCreateOrderContext result = new TmCreateOrderContext();

            // 默认收货地址，如果没有返回null
            result.defaultAddress = Address.deserialize(json.optJSONObject("defaultAddress"));
            // 起始时间
            result.startTime = json.optLong("startTime");
            // 截止时间
            result.endTime = json.optLong("endTime");
            // 最晚到店时间 值为1-24的整数
            JSONArray latestArriveTimeArray = json.optJSONArray("latestArriveTime");
            if (latestArriveTimeArray != null) {
                int len = latestArriveTimeArray.length();
                result.latestArriveTime = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if (!latestArriveTimeArray.isNull(i)) {
                        result.latestArriveTime.add(latestArriveTimeArray.optString(i, null));
                    } else {
                        result.latestArriveTime.add(i, null);
                    }

                }
            }

            // 线路成人属性值VID
            result.lineAdultVid = json.optLong("lineAdultVid");
            // 线路单房差属性值VID
            result.lineSingleRoomVid = json.optLong("lineSingleRoomVid");
            // 商品相关信息
            result.itemInfo = TmItemInfo.deserialize(json.optJSONObject("itemInfo"));

            // 入离政策

            if (!json.isNull("hotelPolicy")) {
                result.hotelPolicy = json.optString("hotelPolicy", null);
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

        // 默认收货地址，如果没有返回null
        if (this.defaultAddress != null) {
            json.put("defaultAddress", this.defaultAddress.serialize());
        }

        // 起始时间
        json.put("startTime", this.startTime);

        // 截止时间
        json.put("endTime", this.endTime);

        // 最晚到店时间 值为1-24的整数 
        if (this.latestArriveTime != null) {
            JSONArray latestArriveTimeArray = new JSONArray();
            for (String value : this.latestArriveTime) {
                latestArriveTimeArray.put(value);
            }
            json.put("latestArriveTime", latestArriveTimeArray);
        }

        // 线路成人属性值VID
        json.put("lineAdultVid", this.lineAdultVid);

        // 线路单房差属性值VID
        json.put("lineSingleRoomVid", this.lineSingleRoomVid);

        // 商品相关信息
        if (this.itemInfo != null) {
            json.put("itemInfo", this.itemInfo.serialize());
        }

        // 入离政策
        if (this.hotelPolicy != null) {
            json.put("hotelPolicy", this.hotelPolicy);
        }

        return json;
    }
}
  