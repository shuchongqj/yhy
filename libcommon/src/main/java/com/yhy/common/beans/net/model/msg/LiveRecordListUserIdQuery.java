package com.yhy.common.beans.net.model.msg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:LiveRecordListUserIdQuery
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-10-24
 * Time:16:59
 * Version 1.1.0
 */


public class LiveRecordListUserIdQuery implements Serializable {
    private static final long serialVersionUID = 3891260675296133462L;

    /**
     * 达人ID（根据达人ID查找）
     */
    public long userId;

    /**
     * 直播状态:直播 (START_LIVE)/回放 (REPLAY_LIVE)
     */
    public List<String> liveStatus;
    /**
     * 直播记录状态 正常(NORMAL_LIVE) 下架 (OFF_SHELVE_LIVE)
     */
    public List<String> liveRecordStatus;
    /**
     * 页码
     */
    public int pageNo;

    /**
     * 每页大小
     */
    public int pageSize;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static LiveRecordListUserIdQuery deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static LiveRecordListUserIdQuery deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            LiveRecordListUserIdQuery result = new LiveRecordListUserIdQuery();

            // 达人ID（根据达人ID查找）
            result.userId = json.optLong("userId");
            // 直播状态:直播 (START_LIVE)/回放 (REPLAY_LIVE)
            JSONArray liveStatusArray = json.optJSONArray("liveStatus");
            if (liveStatusArray != null) {
                int len = liveStatusArray.length();
                result.liveStatus = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if(!liveStatusArray.isNull(i)){
                        result.liveStatus.add(liveStatusArray.optString(i, null));
                    }else{
                        result.liveStatus.add(i, null);
                    }

                }
            }

            // 直播记录状态 正常(NORMAL_LIVE) 下架 (OFF_SHELVE_LIVE)
            JSONArray liveRecordStatusArray = json.optJSONArray("liveRecordStatus");
            if (liveRecordStatusArray != null) {
                int len = liveRecordStatusArray.length();
                result.liveRecordStatus = new
                        ArrayList<String>(len);
                for (int i = 0; i < len; i++) {

                    if(!liveRecordStatusArray.isNull(i)){
                        result.liveRecordStatus.add(liveRecordStatusArray.optString(i, null));
                    }else{
                        result.liveRecordStatus.add(i, null);
                    }

                }
            }

            // 页码
            result.pageNo = json.optInt("pageNo");
            // 每页大小
            result.pageSize = json.optInt("pageSize");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 达人ID（根据达人ID查找）
        json.put("userId", this.userId);

        // 直播状态:直播 (START_LIVE)/回放 (REPLAY_LIVE)
        if (this.liveStatus != null) {
            JSONArray liveStatusArray = new JSONArray();
            for (String value : this.liveStatus)
            {
                liveStatusArray.put(value);
            }
            json.put("liveStatus", liveStatusArray);
        }

        // 直播记录状态 正常(NORMAL_LIVE) 下架 (OFF_SHELVE_LIVE)
        if (this.liveRecordStatus != null) {
            JSONArray liveRecordStatusArray = new JSONArray();
            for (String value : this.liveRecordStatus)
            {
                liveRecordStatusArray.put(value);
            }
            json.put("liveRecordStatus", liveRecordStatusArray);
        }

        // 页码
        json.put("pageNo", this.pageNo);

        // 每页大小
        json.put("pageSize", this.pageSize);

        return json;
    }
}
