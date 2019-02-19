package com.yhy.common.beans.net.model.notification;

import com.yhy.common.beans.im.NotificationMessageEntity;
import com.yhy.common.constants.DBConstant;
import com.yhy.common.constants.NotificationConstants;
import com.yhy.common.utils.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created with Android Studio.
 * Title:NotificationInteractiveMessage
 * Description:通知互动消息
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2015/12/16
 * Time:19:34
 * Version 1.0
 */
public class NotificationInteractiveMessage extends NotificationMessageEntity {
    protected String displayTime;
    protected String nickName;
    protected String subjectImage;
    protected String videoPicUrl;
    protected String userPhoto;
    protected String replyName;
    protected String subjectContent;


    public static NotificationInteractiveMessage parseFromDB(NotificationMessageEntity entity) {
        if (entity.getBizType() != NotificationConstants.BIZ_TYPE_INTERACTION)
            return null;
        NotificationInteractiveMessage message = new NotificationInteractiveMessage();
        message.id = entity.getId();
        message.message = entity.getMessage();
        message.bizSubType = entity.getBizSubType();
        message.bizType = entity.getBizType();
        message.outId = entity.getOutId();
        message.messageId = entity.getMessageId();
        message.data = entity.getData();
        message.sessionType = DBConstant.SESSION_TYPE_NOTIFICATION;
        message.msgType = DBConstant.MSG_TYPE_INTERACTION;
        try {
            JSONObject jsonObject = new JSONObject(message.data);
            message.createTime = jsonObject.optLong(NotificationConstants.KEY_CREATER_TIME);
            message.displayTime = DateUtils.getyyyymmddhhmm(message.createTime);
            message.nickName = jsonObject.optString(NotificationConstants.KEY_NICK_NAME);
            message.subjectImage = jsonObject.optString(NotificationConstants.KEY_SUBJECT_IMAGE);
            message.videoPicUrl = jsonObject.optString(NotificationConstants.KEY_VIDEO_PIC_URL);
            message.userPhoto = jsonObject.optString(NotificationConstants.KEY_USER_PHOTO);
            message.outId = jsonObject.optLong(NotificationConstants.KEY_O_ID);
            message.replyName = jsonObject.optString(NotificationConstants.KEY_REPLOY_NAME);
            message.subjectContent = jsonObject.optString(NotificationConstants.KEY_SUBJECT_CONTENT);
        } catch (JSONException e) {
            return message;
        }
        return message;
    }

    public String getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(String displayTime) {
        this.displayTime = displayTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSubjectImage() {
        return subjectImage;
    }

    public void setSubjectImage(String subjectImage) {
        this.subjectImage = subjectImage;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getReplyName() {
        return replyName;
    }

    public void setReplyName(String reployName) {
        this.replyName = reployName;
    }

    public String getSubjectContent() {
        return subjectContent;
    }

    public String getVideoPicUrl() {
        return videoPicUrl;
    }
}

