package com.yhy.common.beans.net.model.msg;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with Android Studio.
 * Title:LiveRoomLivingRecordResult
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:2016/10/20
 * Time:15:57
 * Version 1.1.0
 */
public class LiveRoomLivingRecordResult {
    public long roomId;
    public long liveId;

    public LiveRoomLivingRecordResult() {
    }

    public static LiveRoomLivingRecordResult deserialize(String json) throws JSONException {
        return json != null && !json.isEmpty()?deserialize(new JSONObject(json)):null;
    }

    public static LiveRoomLivingRecordResult deserialize(JSONObject json) throws JSONException {
        if(json != null && json != JSONObject.NULL && json.length() > 0) {
            LiveRoomLivingRecordResult result = new LiveRoomLivingRecordResult();
            result.roomId = json.optLong("roomId");
            result.liveId = json.optLong("liveId");
            return result;
        } else {
            return null;
        }
    }

    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("roomId", this.roomId);
        json.put("liveId", this.liveId);
        return json;
    }
}
