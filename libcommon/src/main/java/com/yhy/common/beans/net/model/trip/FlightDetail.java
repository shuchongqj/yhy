// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.trip;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class FlightDetail implements Serializable{

    private static final long serialVersionUID = -5997391560409393529L;
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
     * 去程出发时间
     */
    public long forwardDepartTime;

    /**
     * 去程到达时间
     */
    public long forwardArriveTime;

    /**
     * 回程出发时间
     */
    public long returnDepartTime;

    /**
     * 回程到达时间
     */
    public long returnArriveTime;

    /**
     * 去程出发航站楼
     */
    public String forwardDepartStation;

    /**
     * 去程目的航站楼
     */
    public String forwardArriveStation;

    /**
     * 回程出发航站楼
     */
    public String returnDepartStation;

    /**
     * 回程目的航站楼
     */
    public String returnArriveStation;

    /**
     * 去程航班公司
     */
    public String forwardCompanyName;

    /**
     * 回程航班公司
     */
    public String returnCompanyName;

    /**
     * 去程航班号
     */
    public String forwardFlightNum;

    /**
     * 回程航班号
     */
    public String returnFlightNum;

    /**
     * 去程航班公司图片
     */
    public String forwardCompanyPic;

    /**
     * 回程航班公司图片
     */
    public String returnCompanyPic;

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
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static FlightDetail deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static FlightDetail deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            FlightDetail result = new FlightDetail();

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
            // 去程出发时间
            result.forwardDepartTime = json.optLong("forwardDepartTime");
            // 去程到达时间
            result.forwardArriveTime = json.optLong("forwardArriveTime");
            // 回程出发时间
            result.returnDepartTime = json.optLong("returnDepartTime");
            // 回程到达时间
            result.returnArriveTime = json.optLong("returnArriveTime");
            // 去程出发航站楼

            if(!json.isNull("forwardDepartStation")){
                result.forwardDepartStation = json.optString("forwardDepartStation", null);
            }
            // 去程目的航站楼

            if(!json.isNull("forwardArriveStation")){
                result.forwardArriveStation = json.optString("forwardArriveStation", null);
            }
            // 回程出发航站楼

            if(!json.isNull("returnDepartStation")){
                result.returnDepartStation = json.optString("returnDepartStation", null);
            }
            // 回程目的航站楼

            if(!json.isNull("returnArriveStation")){
                result.returnArriveStation = json.optString("returnArriveStation", null);
            }
            // 去程航班公司

            if(!json.isNull("forwardCompanyName")){
                result.forwardCompanyName = json.optString("forwardCompanyName", null);
            }
            // 回程航班公司

            if(!json.isNull("returnCompanyName")){
                result.returnCompanyName = json.optString("returnCompanyName", null);
            }
            // 去程航班号

            if(!json.isNull("forwardFlightNum")){
                result.forwardFlightNum = json.optString("forwardFlightNum", null);
            }
            // 回程航班号

            if(!json.isNull("returnFlightNum")){
                result.returnFlightNum = json.optString("returnFlightNum", null);
            }
            // 去程航班公司图片

            if(!json.isNull("forwardCompanyPic")){
                result.forwardCompanyPic = json.optString("forwardCompanyPic", null);
            }
            // 回程航班公司图片

            if(!json.isNull("returnCompanyPic")){
                result.returnCompanyPic = json.optString("returnCompanyPic", null);
            }
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

        // 去程出发时间
        json.put("forwardDepartTime", this.forwardDepartTime);

        // 去程到达时间
        json.put("forwardArriveTime", this.forwardArriveTime);

        // 回程出发时间
        json.put("returnDepartTime", this.returnDepartTime);

        // 回程到达时间
        json.put("returnArriveTime", this.returnArriveTime);

        // 去程出发航站楼
        if(this.forwardDepartStation != null) { json.put("forwardDepartStation", this.forwardDepartStation); }

        // 去程目的航站楼
        if(this.forwardArriveStation != null) { json.put("forwardArriveStation", this.forwardArriveStation); }

        // 回程出发航站楼
        if(this.returnDepartStation != null) { json.put("returnDepartStation", this.returnDepartStation); }

        // 回程目的航站楼
        if(this.returnArriveStation != null) { json.put("returnArriveStation", this.returnArriveStation); }

        // 去程航班公司
        if(this.forwardCompanyName != null) { json.put("forwardCompanyName", this.forwardCompanyName); }

        // 回程航班公司
        if(this.returnCompanyName != null) { json.put("returnCompanyName", this.returnCompanyName); }

        // 去程航班号
        if(this.forwardFlightNum != null) { json.put("forwardFlightNum", this.forwardFlightNum); }

        // 回程航班号
        if(this.returnFlightNum != null) { json.put("returnFlightNum", this.returnFlightNum); }

        // 去程航班公司图片
        if(this.forwardCompanyPic != null) { json.put("forwardCompanyPic", this.forwardCompanyPic); }

        // 回程航班公司图片
        if(this.returnCompanyPic != null) { json.put("returnCompanyPic", this.returnCompanyPic); }

        // 去程出发日期描述
        if(this.forwardDepartDate != null) { json.put("forwardDepartDate", this.forwardDepartDate); }

        // 去程到达日期描述
        if(this.forwardArriveDate != null) { json.put("forwardArriveDate", this.forwardArriveDate); }

        // 回程出发日期描述
        if(this.returnDepartDate != null) { json.put("returnDepartDate", this.returnDepartDate); }

        // 回程到达日期描述
        if(this.returnArriveDate != null) { json.put("returnArriveDate", this.returnArriveDate); }

        return json;
    }

}
  