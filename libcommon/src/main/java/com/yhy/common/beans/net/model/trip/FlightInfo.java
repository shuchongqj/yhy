// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class FlightInfo implements Serializable{

    private static final long serialVersionUID = -3102181144934860617L;
    /**
     * 去程出发地
     */
    public String forwardDepartCity;

    /**
     * 去程目的地
     */
    public String forwardArriveCity;

    /**
     * 回程出发地
     */
    public String returnDepartCity;

    /**
     * 回程目的地
     */
    public String returnArriveCity;

    /**
     * 去程出发时间 保留
     */
    public long forwardDepartTime;

    /**
     * 去程到达时间 保留
     */
    public long forwardArriveTime;

    /**
     * 回程出发时间 保留
     */
    public long returnDepartTime;

    /**
     * 回程到达时间 保留
     */
    public long returnArriveTime;

    /**
     * 去程出发日期描述
     */
    public String forwardDepartDate;

    /**
     * 去程到达日期描述
     */
    public String forwardArriveDate;

    /**
     * 回程出发日期描述
     */
    public String returnDepartDate;

    /**
     * 回程到达日期描述
     */
    public String returnArriveDate;

    /**
     * 备注描述 无详情时使用
     */
    public String memo;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static FlightInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static FlightInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            FlightInfo result = new FlightInfo();

            // 去程出发地

            if(!json.isNull("forwardDepartCity")){
                result.forwardDepartCity = json.optString("forwardDepartCity", null);
            }
            // 去程目的地

            if(!json.isNull("forwardArriveCity")){
                result.forwardArriveCity = json.optString("forwardArriveCity", null);
            }
            // 回程出发地

            if(!json.isNull("returnDepartCity")){
                result.returnDepartCity = json.optString("returnDepartCity", null);
            }
            // 回程目的地

            if(!json.isNull("returnArriveCity")){
                result.returnArriveCity = json.optString("returnArriveCity", null);
            }
            // 去程出发时间 保留
            result.forwardDepartTime = json.optLong("forwardDepartTime");
            // 去程到达时间 保留
            result.forwardArriveTime = json.optLong("forwardArriveTime");
            // 回程出发时间 保留
            result.returnDepartTime = json.optLong("returnDepartTime");
            // 回程到达时间 保留
            result.returnArriveTime = json.optLong("returnArriveTime");
            // 去程出发日期描述

            if(!json.isNull("forwardDepartDate")){
                result.forwardDepartDate = json.optString("forwardDepartDate", null);
            }
            // 去程到达日期描述

            if(!json.isNull("forwardArriveDate")){
                result.forwardArriveDate = json.optString("forwardArriveDate", null);
            }
            // 回程出发日期描述

            if(!json.isNull("returnDepartDate")){
                result.returnDepartDate = json.optString("returnDepartDate", null);
            }
            // 回程到达日期描述

            if(!json.isNull("returnArriveDate")){
                result.returnArriveDate = json.optString("returnArriveDate", null);
            }
            // 备注描述 无详情时使用

            if(!json.isNull("memo")){
                result.memo = json.optString("memo", null);
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

        // 去程出发地
        if(this.forwardDepartCity != null) { json.put("forwardDepartCity", this.forwardDepartCity); }

        // 去程目的地
        if(this.forwardArriveCity != null) { json.put("forwardArriveCity", this.forwardArriveCity); }

        // 回程出发地
        if(this.returnDepartCity != null) { json.put("returnDepartCity", this.returnDepartCity); }

        // 回程目的地
        if(this.returnArriveCity != null) { json.put("returnArriveCity", this.returnArriveCity); }

        // 去程出发时间 保留
        json.put("forwardDepartTime", this.forwardDepartTime);

        // 去程到达时间 保留
        json.put("forwardArriveTime", this.forwardArriveTime);

        // 回程出发时间 保留
        json.put("returnDepartTime", this.returnDepartTime);

        // 回程到达时间 保留
        json.put("returnArriveTime", this.returnArriveTime);

        // 去程出发日期描述
        if(this.forwardDepartDate != null) { json.put("forwardDepartDate", this.forwardDepartDate); }

        // 去程到达日期描述
        if(this.forwardArriveDate != null) { json.put("forwardArriveDate", this.forwardArriveDate); }

        // 回程出发日期描述
        if(this.returnDepartDate != null) { json.put("returnDepartDate", this.returnDepartDate); }

        // 回程到达日期描述
        if(this.returnArriveDate != null) { json.put("returnArriveDate", this.returnArriveDate); }

        // 备注描述 无详情时使用
        if(this.memo != null) { json.put("memo", this.memo); }

        return json;
    }

}
  