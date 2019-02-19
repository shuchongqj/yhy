package com.yhy.common.beans.im;

import android.text.TextUtils;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;
import com.yhy.common.constants.Constants;
import com.yhy.common.constants.DBConstant;
import com.yhy.common.constants.NotificationConstants;
import com.yhy.common.utils.DateUtils;
import com.yhy.common.utils.EntityChangeEngine;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with Android Studio.
 * Title:MessageEntity
 * Description: 通知消息数据库存储实体
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2015/12/14
 * Time:17:44
 * Version 1.0
 */
@Table(name = "noti_message")
public class NotificationMessageEntity {
    @Id
    @Column(column = "id")
    protected long id;
    @Column(column = "message")
    protected String message;
    @Column(column = "biz_sub_type")
    protected int bizSubType;
    @Column(column = "biz_type")
    protected int bizType;
    @Column(column = "out_id")
    protected long outId;
    @Column(column = "message_id")
    protected long messageId;
    @Column(column = "create_time")
    protected long createTime;
    @Column(column = "data")
    protected String data;
    @Column(column = "status")
    protected int status = DBConstant.READED;
    @Transient
    private String sessionKey;
    @Transient
    public int sessionType = DBConstant.SESSION_TYPE_NOTIFICATION;
    @Transient
    public int msgType = 0;
    @Transient
    public boolean needNotification = true;

    @Column(column = "version")
    protected String version = "1.0";
    //通知栏跳转
    @Column(column = "ntfOperationCode")
    protected String ntfOperationCode;
    @Column(column = "ntfOperationVaule")
    protected String ntfOperationVaule;
    //消息栏跳转
    @Column(column = "msgOperationCode")
    protected String messageOperationCode;
    @Column(column = "msgOperationVaule")
    protected String messageOperationVaule;
    @Column(column = "title")
    protected String title;
    @Transient
    protected String notiMessage;
    @Transient
    protected String notiTitle;
    @Transient
    protected String disPlayTime;

    public NotificationMessageEntity(String message, String extras) {
        try {
            JSONObject jsonObject = new JSONObject(extras);
            this.message = message;
            bizType = jsonObject.optInt(NotificationConstants.KEY_BIZ_TYPE);
            bizSubType = jsonObject.optInt(NotificationConstants.KEY_BIZ_SUB_TYPE);
            outId = jsonObject.optLong(NotificationConstants.KEY_OUT_ID);
            messageId = jsonObject.optLong(NotificationConstants.KEY_MESSAGE_ID);
            createTime = jsonObject.optLong(NotificationConstants.KEY_CREATE_TIME);
            data = jsonObject.optString(NotificationConstants.KEY_DATA);
            sessionType = DBConstant.SESSION_TYPE_NOTIFICATION;
            if (bizType == NotificationConstants.BIZ_TYPE_TRANSACTION) {
                msgType = DBConstant.MSG_TYPE_NOTIFICATION;
            } else if (bizType == NotificationConstants.BIZ_TYPE_INTERACTION) {
                msgType = DBConstant.MSG_TYPE_INTERACTION;
            } else {
//                throw new JSONException(null);
                throw new JSONException("");
            }
        } catch (JSONException e) {
        }
    }

    public NotificationMessageEntity() {

    }

    public NotificationMessageEntity(String extras, String message, String title) throws JSONException {
        notiMessage = message;
        notiTitle = title;
        JSONObject jsonObject = new JSONObject(extras);
        bizType = jsonObject.optInt(NotificationConstants.KEY_BIZ_TYPE);
        bizSubType = jsonObject.optInt(NotificationConstants.KEY_BIZ_SUB_TYPE);
        outId = jsonObject.optLong(NotificationConstants.KEY_OUT_ID);
        messageId = jsonObject.optLong(NotificationConstants.KEY_MESSAGE_ID);
        createTime = jsonObject.optLong(NotificationConstants.KEY_CREATE_TIME);
        data = jsonObject.optString(NotificationConstants.KEY_DATA);
        version = jsonObject.optString(NotificationConstants.VERSION);
        ntfOperationCode = jsonObject.optString(NotificationConstants.KEY_NTF_CODE);
        ntfOperationVaule = jsonObject.optString(NotificationConstants.KEY_NTF_VAULE);
        messageOperationCode = jsonObject.optString(NotificationConstants.KEY_MSG_CODE);
        messageOperationVaule = jsonObject.optString(NotificationConstants.KEY_MSG_VAULE);
        this.title = jsonObject.optString(NotificationConstants.KEY_MSG_TITLE);
        this.message = jsonObject.optString(NotificationConstants.KEY_MSG_CONTENT);

        sessionType = DBConstant.SESSION_TYPE_NOTIFICATION;
        msgType = DBConstant.MSG_TYPE_NOTIFICATION;
    }

    public boolean isNewVersion() {
        return NotificationConstants.VERSION_2.equals(version);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getBizSubType() {
        return bizSubType;
    }

    public void setBizSubType(int bizSubType) {
        this.bizSubType = bizSubType;
    }

    public int getBizType() {
        return bizType;
    }

    public int getPeerId() {
        return bizType == 2 ? 2 : 1;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }

    public long getOutId() {
        return outId;
    }

    public void setOutId(int outId) {
        this.outId = outId;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSessionKey() {
        if (TextUtils.isEmpty(sessionKey)) {
            sessionKey = EntityChangeEngine.getSessionKey(getPeerId(), DBConstant.SESSION_TYPE_NOTIFICATION, 0);
        }
        return sessionKey;
    }

    public int getSessionType() {
        return sessionType;
    }

    public int getMsgType() {
        if (msgType == 0) {
            msgType = bizType == 2 ? DBConstant.MSG_TYPE_INTERACTION : DBConstant.MSG_TYPE_NOTIFICATION;
        }
        return msgType;
    }

    public void setOutId(long outId) {
        this.outId = outId;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public void setSessionType(int sessionType) {
        this.sessionType = sessionType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getNtfOperationCode() {
        return ntfOperationCode;
    }

    public void setNtfOperationCode(String ntfOperationCode) {
        this.ntfOperationCode = ntfOperationCode;
    }

    public String getNtfOperationVaule() {
        return ntfOperationVaule;
    }

    public void setNtfOperationVaule(String ntfOperationVaule) {
        this.ntfOperationVaule = ntfOperationVaule;
    }

    public String getMessageOperationCode() {
        return messageOperationCode;
    }

    public void setMessageOperationCode(String messageOperationCode) {
        this.messageOperationCode = messageOperationCode;
    }

    public String getMessageOperationVaule() {
        return messageOperationVaule;
    }

    public void setMessageOperationVaule(String messageOperationVaule) {
        this.messageOperationVaule = messageOperationVaule;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotiMessage() {
        return notiMessage;
    }

    public void setNotiMessage(String notiMessage) {
        this.notiMessage = notiMessage;
    }

    public String getNotiTitle() {
        return notiTitle;
    }

    public void setNotiTitle(String notiTitle) {
        this.notiTitle = notiTitle;
    }

    public String getDisPlayTime() {
        if (TextUtils.isEmpty(disPlayTime)) {
            disPlayTime = DateUtils.getyyyymmddhhmm(createTime);
        }
        return disPlayTime;
    }

    public boolean isNeedNotification() {
        return needNotification;
    }

    public void setNeedNotification(boolean needNotification) {
        this.needNotification = needNotification;
    }

    @Override
    public String toString() {
        return "NotificationMessageEntity{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", bizSubType=" + bizSubType +
                ", bizType=" + bizType +
                ", outId=" + outId +
                ", messageId=" + messageId +
                ", createTime=" + createTime +
                ", data='" + data + '\'' +
                ", status=" + status +
                ", sessionKey='" + sessionKey + '\'' +
                ", sessionType=" + sessionType +
                ", msgType=" + msgType +
                ", version='" + version + '\'' +
                ", ntfOperationCode='" + ntfOperationCode + '\'' +
                ", ntfOperationVaule='" + ntfOperationVaule + '\'' +
                ", messageOperationCode='" + messageOperationCode + '\'' +
                ", messageOperationVaule='" + messageOperationVaule + '\'' +
                ", title='" + title + '\'' +
                ", notiMessage='" + notiMessage + '\'' +
                ", notiTitle='" + notiTitle + '\'' +
                ", disPlayTime='" + disPlayTime + '\'' +
                '}';
    }
}
