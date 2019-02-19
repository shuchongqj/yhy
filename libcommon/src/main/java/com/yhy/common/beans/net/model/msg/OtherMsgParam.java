package com.yhy.common.beans.net.model.msg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:OtherMsgParam
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-30
 * Time:11:11
 * Version 1.1.0
 */

public class OtherMsgParam implements Serializable {
    private static final long serialVersionUID = -8653429661257970509L;

    /**
     * 直播ID/群组ID
     */
    public long liveId;

    /**
     * 通知消息内容.消息类型为NORMAL_MSG时生效,其他类型不处理,消息内容为想要通知客户端的任意内容.
     */
    public String msgContent;

    /**
     * 需要通知的用户,如需通知所有客户端此参数传空
     */
    public long[] userIdList;
    /**
     * 通知消息类型.FOLLOW_MSG-关注/DISABLE_SEND_MSG-禁言/SHARE_MSG-分享直播/TAKE_A_BREAK-主播离开/COME_BACK-主播回来/ANCHOR_LEAVE-主播离开直播结束
     */
    public String msgNotifyType;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static OtherMsgParam deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static OtherMsgParam deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            OtherMsgParam result = new OtherMsgParam();

            // 直播ID/群组ID
            result.liveId = json.optLong("liveId");
            // 用户昵称

            // 需要通知的用户,如需通知所有客户端此参数传空
            JSONArray userIdListArray = json.optJSONArray("userIdList");
            if (userIdListArray != null) {
                int len = userIdListArray.length();
                result.userIdList = new long[len];
                for (int i = 0; i < len; i++) {
                    result.userIdList[i] = userIdListArray.optLong(i);
                }
            }

            // 通知消息类型.FOLLOW_MSG-关注/DISABLE_SEND_MSG-禁言/SHARE_MSG-分享直播/TAKE_A_BREAK-主播离开/COME_BACK-主播回来/ANCHOR_LEAVE-主播离开直播结束

            if (!json.isNull("msgNotifyType")) {
                result.msgNotifyType = json.optString("msgNotifyType", null);
            }

            // 通知消息内容.消息类型为NORMAL_MSG时生效,其他类型不处理,消息内容为想要通知客户端的任意内容.

            if (!json.isNull("msgContent")) {
                result.msgContent = json.optString("msgContent", null);
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

        // 直播ID/群组ID
        json.put("liveId", this.liveId);


        // 需要通知的用户,如需通知所有客户端此参数传空
        if (this.userIdList != null) {
            JSONArray userIdListArray = new JSONArray();
            for (long value : this.userIdList) {
                userIdListArray.put(value);
            }
            json.put("userIdList", userIdListArray);
        }

        // 通知消息类型.FOLLOW_MSG-关注/DISABLE_SEND_MSG-禁言/SHARE_MSG-分享直播/TAKE_A_BREAK-主播离开/COME_BACK-主播回来/ANCHOR_LEAVE-主播离开直播结束
        if (this.msgNotifyType != null) {
            json.put("msgNotifyType", this.msgNotifyType);
        }
        // 通知消息内容.消息类型为NORMAL_MSG时生效,其他类型不处理,消息内容为想要通知客户端的任意内容.
        if (this.msgContent != null) {
            json.put("msgContent", this.msgContent);
        }

        return json;
    }
}
