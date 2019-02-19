package com.yhy.common.beans.net.model.notification;


import com.yhy.common.R;
import com.yhy.common.beans.im.NotificationMessageEntity;
import com.yhy.common.constants.DBConstant;
import com.yhy.common.constants.NotificationConstants;
import com.yhy.common.utils.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with Android Studio.
 * Title:NotificationMessage
 * Description: 通知消息
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2015/12/15
 * Time:17:28
 * Version 1.0
 */
public class NotificationOrderMessage extends NotificationMessageEntity {
    protected String displayTime;
    protected String title;
    protected int iconResId;
    protected long orderId;
    protected String orderType;
    protected int busType = NotificationConstants.BUS_TYPE_USER;

    public static NotificationOrderMessage parseFromDB(NotificationMessageEntity entity) {
        if (entity.getBizType() != NotificationConstants.BIZ_TYPE_TRANSACTION)
            return null;
        NotificationOrderMessage message = new NotificationOrderMessage();
        message.id = entity.getId();
        message.message = entity.getMessage();
        message.bizSubType = entity.getBizSubType();
        message.bizType = entity.getBizType();
        message.outId = entity.getOutId();
        message.messageId = entity.getMessageId();
        message.createTime = entity.getCreateTime();
        message.data = entity.getData();
        message.sessionType = DBConstant.SESSION_TYPE_NOTIFICATION;
        message.msgType = DBConstant.MSG_TYPE_NOTIFICATION;
        //订单通知
        if (String.valueOf(message.bizSubType).startsWith("1")) {
            message.title = NotificationConstants.TITLE_TRANSACTION;
            message.iconResId = R.mipmap.ic_message_order;
        } else {
            message.title = NotificationConstants.TITLE_SYSTEM;
            message.iconResId = R.mipmap.ic_system;
        }


        message.displayTime = DateUtils.getyyyymmddhhmm(message.createTime);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(message.data);
        } catch (JSONException e) {
            return message;
        }
        try {
            message.orderId = jsonObject.optLong(NotificationConstants.BIZ_ORDER_ID);
            message.orderType = jsonObject.optString(NotificationConstants.ORDER_TYPE);
            if (jsonObject.has(NotificationConstants.KEY_BUS_TYPE)) {
                message.busType = jsonObject.getInt(NotificationConstants.KEY_BUS_TYPE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    public String getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(String displayTime) {
        this.displayTime = displayTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public int getBusType(){
        return busType;
    }
}
