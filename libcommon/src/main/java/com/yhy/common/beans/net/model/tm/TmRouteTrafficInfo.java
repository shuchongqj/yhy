// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.tm;
    
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TmRouteTrafficInfo implements Serializable{

    private static final long serialVersionUID = -1856505792302659813L;
    /**
     * 交通类型 TRAIN火车 FLIGHT飞机 BOAT轮船 BUS大巴
     */
    public String type;

    /**
     * 出发城市
     */
    public String startCity;

    /**
     * 到达城市
     */
    public String destCity;

    /**
     * 详细描述
     */
    public String description;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TmRouteTrafficInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TmRouteTrafficInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TmRouteTrafficInfo result = new TmRouteTrafficInfo();

            // 交通类型 TRAIN火车 FLIGHT飞机 BOAT轮船 BUS大巴

            if(!json.isNull("type")){
                result.type = json.optString("type", null);
            }
            // 出发城市

            if(!json.isNull("startCity")){
                result.startCity = json.optString("startCity", null);
            }
            // 到达城市

            if(!json.isNull("destCity")){
                result.destCity = json.optString("destCity", null);
            }
            // 详细描述

            if(!json.isNull("description")){
                result.description = json.optString("description", null);
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

        // 交通类型 TRAIN火车 FLIGHT飞机 BOAT轮船 BUS大巴
        if(this.type != null) { json.put("type", this.type); }

        // 出发城市
        if(this.startCity != null) { json.put("startCity", this.startCity); }

        // 到达城市
        if(this.destCity != null) { json.put("destCity", this.destCity); }

        // 详细描述
        if(this.description != null) { json.put("description", this.description); }

        return json;
    }

}
  