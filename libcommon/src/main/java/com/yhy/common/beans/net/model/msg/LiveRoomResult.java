package com.yhy.common.beans.net.model.msg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:LiveRoomResult
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-31
 * Time:18:31
 * Version 1.1.0
 */

public class LiveRoomResult implements Serializable {
    private static final long serialVersionUID = -5335704022285423906L;

    /**
     * 直播ID
     */
    public long roomId;

    /**
     * 直播类别Code
     */
    public long liveCategoryCode;

    /**
     * 直播类别Name
     */
    public String liveCategoryName;

    /**
     * 直播间公告
     */
    public String roomNotice;

    /**
     * 直播间标题
     */
    public String liveTitle;

    /**
     * 直播间状态 1：直播中 2：空闲 3：关闭
     */
    public int liveRoomStatus;

    /**
     * 直播类别
     */
    public List<LiveCategoryResult> list;
    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static LiveRoomResult deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static LiveRoomResult deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            LiveRoomResult result = new LiveRoomResult();

            // 直播ID
            result.roomId = json.optLong("roomId");
            // 直播类别Code
            result.liveCategoryCode = json.optLong("liveCategoryCode");
            // 直播类别Name

            if(!json.isNull("liveCategoryName")){
                result.liveCategoryName = json.optString("liveCategoryName", null);
            }
            // 直播间公告

            if(!json.isNull("roomNotice")){
                result.roomNotice = json.optString("roomNotice", null);
            }
            // 直播间标题

            if(!json.isNull("liveTitle")){
                result.liveTitle = json.optString("liveTitle", null);
            }
            // 直播间状态 1：直播中 2：空闲 3：关闭
            result.liveRoomStatus = json.optInt("liveRoomStatus");
            // 直播类别
            JSONArray listArray = json.optJSONArray("list");
            if (listArray != null) {
                int len = listArray.length();
                result.list = new ArrayList<LiveCategoryResult>(len);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = listArray.optJSONObject(i);
                    if (jo != null && jo != JSONObject.NULL && jo.length() > 0) {
                        result.list.add(LiveCategoryResult.deserialize(jo));
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

        // 直播ID
        json.put("roomId", this.roomId);

        // 直播类别Code
        json.put("liveCategoryCode", this.liveCategoryCode);

        // 直播类别Name
        if(this.liveCategoryName != null) { json.put("liveCategoryName", this.liveCategoryName); }

        // 直播间公告
        if(this.roomNotice != null) { json.put("roomNotice", this.roomNotice); }

        // 直播间标题
        if(this.liveTitle != null) { json.put("liveTitle", this.liveTitle); }

        // 直播间状态 1：直播中 2：空闲 3：关闭
        json.put("liveRoomStatus", this.liveRoomStatus);

        // 直播类别
        if (this.list != null) {
            JSONArray listArray = new JSONArray();
            for (LiveCategoryResult value : this.list)
            {
                if (value != null) {
                    listArray.put(value.serialize());
                }
            }
            json.put("list", listArray);
        }

        return json;
    }
}
