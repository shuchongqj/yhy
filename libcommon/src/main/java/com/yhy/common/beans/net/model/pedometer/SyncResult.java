// Auto Generated.  DO NOT EDIT!
package com.yhy.common.beans.net.model.pedometer;
    
import com.smart.sdk.api.resp.Api_TRACK_StepResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class SyncResult implements Serializable{

    private static final long serialVersionUID = 2849218789011137640L;
    public long syncDate;
    public boolean success;
    public Api_TRACK_StepResult stepResult;

    public SyncResult() {
    }

    public static SyncResult deserialize(String json) throws JSONException {
        return json != null && !json.isEmpty()?deserialize(new JSONObject(json)):null;
    }

    public static SyncResult deserialize(JSONObject json) throws JSONException {
        if(json != null && json != JSONObject.NULL && json.length() > 0) {
            SyncResult result = new SyncResult();
            result.syncDate = json.optLong("syncDate");
            result.success = json.optBoolean("success");
            result.stepResult = Api_TRACK_StepResult.deserialize(json.optJSONObject("stepResult"));
            return result;
        } else {
            return null;
        }
    }

    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("syncDate", this.syncDate);
        json.put("success", this.success);
        if(this.stepResult != null) {
            json.put("stepResult", this.stepResult.serialize());
        }

        return json;
    }
}
  