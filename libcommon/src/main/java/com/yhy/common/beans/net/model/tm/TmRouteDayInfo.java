// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TmRouteDayInfo implements Serializable {

    private static final long serialVersionUID = -6795265420635384446L;
    public boolean isShrink = true;//非网络数据
    /**
     * 第几天的行程
     */
    public int day;

    /**
     * 当天交通信息
     */
    public TmRouteTrafficInfo routeTrafficInfo;

    /**
     * 文字简介列表
     */
    public List<TmRouteItemDesc> descList;
    /**
     * 图文描述列表
     */
    public List<TmRouteItemDetail> detailList;
    /**
     * 当天文字描述
     */
    public String description;

    /**
     * 行程所有图片url列表
     */
    public List<String> picUrls;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmRouteDayInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmRouteDayInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmRouteDayInfo result = new TmRouteDayInfo();

            // 第几天的行程
            result.day = json.optInt("day");
            // 当天交通信息
            result.routeTrafficInfo = TmRouteTrafficInfo.deserialize(json.optJSONObject("routeTrafficInfo"));
            // 文字简介列表
            JSONArray descListArray = json.optJSONArray("descList");
            if (descListArray != null) {
                int len = descListArray.length();
                result.descList = new ArrayList<TmRouteItemDesc>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = descListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.descList.add(TmRouteItemDesc.deserialize(jo));
                    }
                }
            }

            // 图文描述列表
            JSONArray detailListArray = json.optJSONArray("detailList");
            if (detailListArray != null) {
                int len = detailListArray.length();
                result.detailList = new ArrayList<TmRouteItemDetail>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = detailListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.detailList.add(TmRouteItemDetail.deserialize(jo));
                    }
                }
            }

            // 当天文字描述

            if(!json.isNull("description")){
                result.description = json.optString("description", null);
            }
            // 行程所有图片url列表
            JSONArray picUrlsArray = json.optJSONArray("picUrls");
            if (picUrlsArray != null) {
                int len = picUrlsArray.length();
                result.picUrls = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if(!picUrlsArray.isNull(i)){
                        result.picUrls.add(picUrlsArray.optString(i, null));
                    }else{
                        result.picUrls.add(i, null);
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

        // 第几天的行程
        json.put("day", this.day);

        // 当天交通信息
        if (this.routeTrafficInfo != null) { json.put("routeTrafficInfo", this.routeTrafficInfo.serialize()); }

        // 文字简介列表
        if (this.descList != null) {
            JSONArray descListArray = new JSONArray();
            for (TmRouteItemDesc value : this.descList)
            {
                if (value != null) {
                    descListArray.put(value.serialize());
                }
            }
            json.put("descList", descListArray);
        }

        // 图文描述列表
        if (this.detailList != null) {
            JSONArray detailListArray = new JSONArray();
            for (TmRouteItemDetail value : this.detailList)
            {
                if (value != null) {
                    detailListArray.put(value.serialize());
                }
            }
            json.put("detailList", detailListArray);
        }

        // 当天文字描述
        if(this.description != null) { json.put("description", this.description); }

        // 行程所有图片url列表
        if (this.picUrls != null) {
            JSONArray picUrlsArray = new JSONArray();
            for (String value : this.picUrls)
            {
                picUrlsArray.put(value);
            }
            json.put("picUrls", picUrlsArray);
        }


        return json;
    }
}
  