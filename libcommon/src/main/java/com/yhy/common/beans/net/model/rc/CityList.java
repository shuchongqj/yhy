// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.rc;

import com.yhy.common.beans.net.model.trip.CityInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CityList implements Serializable {

    private static final long serialVersionUID = -6373013139187413122L;
    /**
     * 省列表
     */
    public List<CityInfo> provinceList;
    /**
     * 市列表
     */
    public List<CityInfo> cityInfoList;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static CityList deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static CityList deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            CityList result = new CityList();

            // 省列表
            JSONArray provinceListArray = json.optJSONArray("provinceList");
            if (provinceListArray != null) {
                int len = provinceListArray.length();
                result.provinceList = new ArrayList<CityInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = provinceListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.provinceList.add(CityInfo.deserialize(jo));
                    }
                }
            }

            // 市列表
            JSONArray cityInfoListArray = json.optJSONArray("cityInfoList");
            if (cityInfoListArray != null) {
                int len = cityInfoListArray.length();
                result.cityInfoList = new ArrayList<CityInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = cityInfoListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.cityInfoList.add(CityInfo.deserialize(jo));
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

        // 省列表 
        if (this.provinceList != null) {
            JSONArray provinceListArray = new JSONArray();
            for (CityInfo value : this.provinceList) {
                if (value != null) {
                    provinceListArray.put(value.serialize());
                }
            }
            json.put("provinceList", provinceListArray);
        }

        // 市列表 
        if (this.cityInfoList != null) {
            JSONArray cityInfoListArray = new JSONArray();
            for (CityInfo value : this.cityInfoList) {
                if (value != null) {
                    cityInfoListArray.put(value.serialize());
                }
            }
            json.put("cityInfoList", cityInfoListArray);
        }

        return json;
    }
}
  