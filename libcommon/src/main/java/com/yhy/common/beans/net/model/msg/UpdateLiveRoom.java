package com.yhy.common.beans.net.model.msg;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:UpdateLiveRoom
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-31
 * Time:18:50
 * Version 1.1.0
 */

public class UpdateLiveRoom implements Serializable {
    private static final long serialVersionUID = -453230173644748433L;

    /**
     * 直播间ID
     */
    public long roomId;

    /**
     * 直播间公告
     */
    public String liveNotice;

    /**
     * 直播间类别code
     */
    public long liveCategory;

    /**
     * 直播间标题
     */
    public String liveTitle;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static UpdateLiveRoom deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static UpdateLiveRoom deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            UpdateLiveRoom result = new UpdateLiveRoom();

            // 直播间ID
            result.roomId = json.optLong("roomId");
            // 直播间公告

            if(!json.isNull("liveNotice")){
                result.liveNotice = json.optString("liveNotice", null);
            }
            // 直播间类别code
            result.liveCategory = json.optLong("liveCategory");
            // 直播间标题

            if(!json.isNull("liveTitle")){
                result.liveTitle = json.optString("liveTitle", null);
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

        // 直播间ID
        json.put("roomId", this.roomId);

        // 直播间公告
        if(this.liveNotice != null) { json.put("liveNotice", this.liveNotice); }

        // 直播间类别code
        json.put("liveCategory", this.liveCategory);

        // 直播间标题
        if(this.liveTitle != null) { json.put("liveTitle", this.liveTitle); }

        return json;
    }
}
