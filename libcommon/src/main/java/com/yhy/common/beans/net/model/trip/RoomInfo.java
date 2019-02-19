// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;

import com.yhy.common.beans.net.model.base.BaseShrink;
import com.yhy.common.beans.net.model.item.RoomProperty;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoomInfo extends BaseShrink implements Serializable {

    private static final long serialVersionUID = -2616057751731974365L;
    /**
     * 房型名称
     */
    public String name;

    /**
     * 房型属性
     */
    public List<RoomProperty> roomProperties;
    /**
     * 图片
     */
    public List<String> pics;
    /**
     * 最低价格
     */
    public long price;

    /**
     * 酒店商品列表
     */
    public List<HotelItem> hotelItemList;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static RoomInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static RoomInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            RoomInfo result = new RoomInfo();

            // 房型名称

            if (!json.isNull("name")) {
                result.name = json.optString("name", null);
            }
            // 房型属性
            JSONArray roomPropertiesArray = json.optJSONArray("roomProperties");
            if (roomPropertiesArray != null) {
                int len = roomPropertiesArray.length();
                result.roomProperties = new ArrayList<RoomProperty>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = roomPropertiesArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.roomProperties.add(RoomProperty.deserialize(jo));
                    }
                }
            }

            // 图片
            JSONArray picsArray = json.optJSONArray("pics");
            if (picsArray != null) {
                int len = picsArray.length();
                result.pics = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if (!picsArray.isNull(i)) {
                        result.pics.add(picsArray.optString(i, null));
                    } else {
                        result.pics.add(i, null);
                    }

                }
            }

            // 最低价格
            result.price = json.optLong("price");
            // 酒店商品列表
            JSONArray hotelItemListArray = json.optJSONArray("hotelItemList");
            if (hotelItemListArray != null) {
                int len = hotelItemListArray.length();
                result.hotelItemList = new ArrayList<HotelItem>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = hotelItemListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.hotelItemList.add(HotelItem.deserialize(jo));
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

        // 房型名称
        if (this.name != null) {
            json.put("name", this.name);
        }

        // 房型属性 
        if (this.roomProperties != null) {
            JSONArray roomPropertiesArray = new JSONArray();
            for (RoomProperty value : this.roomProperties) {
                if (value != null) {
                    roomPropertiesArray.put(value.serialize());
                }
            }
            json.put("roomProperties", roomPropertiesArray);
        }

        // 图片 
        if (this.pics != null) {
            JSONArray picsArray = new JSONArray();
            for (String value : this.pics) {
                picsArray.put(value);
            }
            json.put("pics", picsArray);
        }

        // 最低价格
        json.put("price", this.price);

        // 酒店商品列表 
        if (this.hotelItemList != null) {
            JSONArray hotelItemListArray = new JSONArray();
            for (HotelItem value : this.hotelItemList) {
                if (value != null) {
                    hotelItemListArray.put(value.serialize());
                }
            }
            json.put("hotelItemList", hotelItemListArray);
        }

        return json;
    }

}
  