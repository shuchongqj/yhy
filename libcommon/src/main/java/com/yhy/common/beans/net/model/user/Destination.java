// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.user;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Destination implements Serializable {

    private static final long serialVersionUID = 7487707882864507678L;
    /**
     * id
     */
    public long id;

    /**
     * 名称
     */
    public String name;

    /**
     * 编码
     */
    public int code;

    /**
     * 父级编码
     */
    public int parentCode;

    /**
     * 类型:FOREIGN_REGION-国际区域  PROVINCE-省份  CITY-城市 AREA-行政区域 COMMERCIAL_AREA-商业圈 SCENIC-景区  OVERSEAS-国际
     */
    public String type;

    /**
     * 类型:SCENIC-景区  LINE-线路  HOTEL-酒店
     */
    public String outType;

    /**
     * 简拼
     */
    public String simpleCode;

    /**
     * 周边城市:code-name
     */
    public List<DestinationCodeName> circumCities;

    /**
     * 是否有子节点
     */
    public boolean hasChild;

    /**
     * 子目的地列表
     */
    public List<Destination> childList;

    /**
     * 是否国内区域
     */
    public boolean isInnerArea;


    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static Destination deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static Destination deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            Destination result = new Destination();

            // id
            result.id = json.optLong("id");
            // 名称

            if (!json.isNull("name")) {
                result.name = json.optString("name", null);
            }
            // 编码
            result.code = json.optInt("code");
            // 父级编码
            result.parentCode = json.optInt("parentCode");
            // 类型:FOREIGN_REGION-国际区域  PROVINCE-省份  CITY-城市 AREA-行政区域 COMMERCIAL_AREA-商业圈 SCENIC-景区  OVERSEAS-国际

            if (!json.isNull("type")) {
                result.type = json.optString("type", null);
            }
            // 类型:SCENIC-景区  LINE-线路  HOTEL-酒店

            if (!json.isNull("outType")) {
                result.outType = json.optString("outType", null);
            }
            // 简拼

            if (!json.isNull("simpleCode")) {
                result.simpleCode = json.optString("simpleCode", null);
            }
            // 周边城市:code-name
            JSONArray circumCitiesArray = json.optJSONArray("circumCities");
            if (circumCitiesArray != null) {
                int len = circumCitiesArray.length();
                result.circumCities = new ArrayList<DestinationCodeName>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = circumCitiesArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.circumCities.add(DestinationCodeName.deserialize(jo));
                    }
                }
            }
            // 是否有子节点
            result.hasChild = json.optBoolean("hasChild");
            // 子目的地列表
            JSONArray childListArray = json.optJSONArray("childList");
            if (childListArray != null) {
                int len = childListArray.length();
                result.childList = new ArrayList<Destination>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = childListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.childList.add(Destination.deserialize(jo));
                    }
                }
            }
            // 是否国内区域
            result.isInnerArea = json.optBoolean("isInnerArea");

            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // id
        json.put("id", this.id);

        // 名称
        if (this.name != null) {
            json.put("name", this.name);
        }

        // 编码
        json.put("code", this.code);

        // 父级编码
        json.put("parentCode", this.parentCode);

        // 类型:FOREIGN_REGION-国际区域  PROVINCE-省份  CITY-城市 AREA-行政区域 COMMERCIAL_AREA-商业圈 SCENIC-景区  OVERSEAS-国际
        if (this.type != null) {
            json.put("type", this.type);
        }

        // 类型:SCENIC-景区  LINE-线路  HOTEL-酒店
        if (this.outType != null) {
            json.put("outType", this.outType);
        }

        // 简拼
        if (this.simpleCode != null) {
            json.put("simpleCode", this.simpleCode);
        }

        // 周边城市:code-name 
        if (this.circumCities != null) {
            JSONArray circumCitiesArray = new JSONArray();
            for (DestinationCodeName value : this.circumCities) {
                if (value != null) {
                    circumCitiesArray.put(value.serialize());
                }
            }
            json.put("circumCities", circumCitiesArray);
        }

        // 是否有子节点
        json.put("hasChild", this.hasChild);

        // 子目的地列表
        if (this.childList != null) {
            JSONArray childListArray = new JSONArray();
            for (Destination value : this.childList) {
                if (value != null) {
                    childListArray.put(value.serialize());
                }
            }
            json.put("childList", childListArray);
        }
        // 是否国内区域
        json.put("isInnerArea", this.isInnerArea);

        return json;
    }

}
  