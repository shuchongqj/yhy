// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TmUserRoute implements Serializable {

    private static final long serialVersionUID = -4428617337906167942L;
    /**
     * 行程ID
     */
    public long id;

    /**
     * 订单ID
     */
    public long order_id;

    /**
     * 行程标题
     */
    public String title;

    /**
     * 行程背景图
     */
    public String logo_pic;

    /**
     * 行程详情页
     */
    public String detail_pic;

    /**
     * 客服电话
     */
    public String servicePhone;

    /**
     * 每天行程列表
     */
    public List<TmRouteDayInfo> routeDayInfoList;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmUserRoute deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmUserRoute deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmUserRoute result = new TmUserRoute();

            // 行程ID
            result.id = json.optLong("id");
            // 订单ID
            result.order_id = json.optLong("order_id");
            // 行程标题

            if(!json.isNull("title")){
                result.title = json.optString("title", null);
            }
            // 行程背景图

            if(!json.isNull("logo_pic")){
                result.logo_pic = json.optString("logo_pic", null);
            }
            // 行程详情页

            if(!json.isNull("detail_pic")){
                result.detail_pic = json.optString("detail_pic", null);
            }
            // 客服电话

            if(!json.isNull("servicePhone")){
                result.servicePhone = json.optString("servicePhone", null);
            }
            // 每天行程列表
            JSONArray routeDayInfoListArray = json.optJSONArray("routeDayInfoList");
            if (routeDayInfoListArray != null) {
                int len = routeDayInfoListArray.length();
                result.routeDayInfoList = new ArrayList<TmRouteDayInfo>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = routeDayInfoListArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.routeDayInfoList.add(TmRouteDayInfo.deserialize(jo));
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

        // 行程ID
        json.put("id", this.id);

        // 订单ID
        json.put("order_id", this.order_id);

        // 行程标题
        if(this.title != null) { json.put("title", this.title); }

        // 行程背景图
        if(this.logo_pic != null) { json.put("logo_pic", this.logo_pic); }

        // 行程详情页
        if(this.detail_pic != null) { json.put("detail_pic", this.detail_pic); }

        // 客服电话
        if(this.servicePhone != null) { json.put("servicePhone", this.servicePhone); }

        // 每天行程列表
        if (this.routeDayInfoList != null) {
            JSONArray routeDayInfoListArray = new JSONArray();
            for (TmRouteDayInfo value : this.routeDayInfoList)
            {
                if (value != null) {
                    routeDayInfoListArray.put(value.serialize());
                }
            }
            json.put("routeDayInfoList", routeDayInfoListArray);
        }

        return json;
    }
}
  