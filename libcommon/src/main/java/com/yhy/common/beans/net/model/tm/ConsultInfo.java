package com.yhy.common.beans.net.model.tm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:ConsultInfo
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-19
 * Time:17:53
 * Version 1.0
 * Description:
 */
public class ConsultInfo implements Serializable{
    private static final long serialVersionUID = -1845357675035090413L;

    /**
     * 开始排队时间
     */
    public long joinTime;

    /**
     * 开始咨询时间
     */
    public long consultTime;

    /**
     * 服务总时间，例如：900秒
     */
    public long serviceTotalTime;

    /**
     * 已服务时间，当前到了多少时间，例如：100秒
     */
    public long serviceCurrentTime;

    /**
     * 达人队列总长度
     */
    public long sellerQueueLength;

    /**
     * 用户前面的排队人数
     */
    public long userQueuePosition;

    /**
     * 当前订单达人是否在线
     */
    public boolean talentOnlineStatus;

    /**
     * 达人能否接单
     */
    public boolean ableToAcceptOrder;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static ConsultInfo deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static ConsultInfo deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            ConsultInfo result = new ConsultInfo();

            // 开始排队时间
            result.joinTime = json.optLong("joinTime");
            // 开始咨询时间
            result.consultTime = json.optLong("consultTime");
            // 服务总时间，例如：900秒
            result.serviceTotalTime = json.optLong("serviceTotalTime");
            // 已服务时间，当前到了多少时间，例如：100秒
            result.serviceCurrentTime = json.optLong("serviceCurrentTime");
            // 达人队列总长度
            result.sellerQueueLength = json.optLong("sellerQueueLength");
            // 用户前面的排队人数
            result.userQueuePosition = json.optLong("userQueuePosition");
            // 当前订单达人是否在线
            result.talentOnlineStatus = json.optBoolean("talentOnlineStatus");
            // 达人能否接单
            result.ableToAcceptOrder = json.optBoolean("ableToAcceptOrder");
            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        // 开始排队时间
        json.put("joinTime", this.joinTime);

        // 开始咨询时间
        json.put("consultTime", this.consultTime);

        // 服务总时间，例如：900秒
        json.put("serviceTotalTime", this.serviceTotalTime);

        // 已服务时间，当前到了多少时间，例如：100秒
        json.put("serviceCurrentTime", this.serviceCurrentTime);

        // 达人队列总长度
        json.put("sellerQueueLength", this.sellerQueueLength);

        // 用户前面的排队人数
        json.put("userQueuePosition", this.userQueuePosition);

        // 当前订单达人是否在线
        json.put("talentOnlineStatus", this.talentOnlineStatus);

        // 达人能否接单
        json.put("ableToAcceptOrder", this.ableToAcceptOrder);

        return json;
    }
}
