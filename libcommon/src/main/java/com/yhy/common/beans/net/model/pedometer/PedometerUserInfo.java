// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.pedometer;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class PedometerUserInfo implements Serializable{

    private static final long serialVersionUID = 3212014973818140729L;
    public int age;
    public int weight;
    public int height;
    public String userCode;
    public long syncDate;
    public PointConfig pointConfig;
    public TrackConfig trackConfig;

    public PedometerUserInfo() {
    }

    public static PedometerUserInfo deserialize(String json) throws JSONException {
        return json != null && !json.isEmpty()?deserialize(new JSONObject(json)):null;
    }

    public static PedometerUserInfo deserialize(JSONObject json) throws JSONException {
        if(json != null && json != JSONObject.NULL && json.length() > 0) {
            PedometerUserInfo result = new PedometerUserInfo();
            result.age = json.optInt("age");
            result.weight = json.optInt("weight");
            result.height = json.optInt("height");
            if(!json.isNull("userCode")) {
                result.userCode = json.optString("userCode", (String)null);
            }

            result.syncDate = json.optLong("syncDate");
            result.pointConfig = PointConfig.deserialize(json.optJSONObject("pointConfig"));
            result.trackConfig = TrackConfig.deserialize(json.optJSONObject("trackConfig"));
            return result;
        } else {
            return null;
        }
    }

    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("age", this.age);
        json.put("weight", this.weight);
        json.put("height", this.height);
        if(this.userCode != null) {
            json.put("userCode", this.userCode);
        }

        json.put("syncDate", this.syncDate);
        if(this.pointConfig != null) {
            json.put("pointConfig", this.pointConfig.serialize());
        }

        if(this.trackConfig != null) {
            json.put("trackConfig", this.trackConfig.serialize());
        }

        return json;
    }
}
  