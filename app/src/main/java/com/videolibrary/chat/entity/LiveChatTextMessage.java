package com.videolibrary.chat.entity;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.quanyan.yhy.R;
import com.videolibrary.chat.Constants;
import com.videolibrary.chat.protobuf.IMLive;
import com.videolibrary.widget.LiveChatTextMessageView;
import com.yhy.common.utils.SPUtils;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import java.io.UnsupportedEncodingException;

/**
 * Created with Android Studio.
 * Title:TextMessage
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/8/16
 * Time:15:39
 * Version 1.0
 */
public class LiveChatTextMessage extends LiveChatMessageEntity {

    private int contentColorResId = -1;


    public LiveChatTextMessage(long fromId, long toId, String fromName, String fromPic, String messageContent) {
        super(fromId, toId, fromName, fromPic, Constants.MSG_TYPE_TEXT, messageContent);
    }

    public LiveChatTextMessage(long fromId, long toId, String fromName, String fromPic, String messageContent, int contentColorResId) {
        super(fromId, toId, fromName, fromPic, Constants.MSG_TYPE_TEXT, messageContent);
        this.contentColorResId = contentColorResId;
    }

    @Override
    public byte[] getSendContent() {
        try {
            return getMessageContent().getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {

            return null;
        }
    }

    public static LiveChatMessageEntity parseFrom(IMLive.IMGroupMsgData msgData) {
        return new LiveChatTextMessage(msgData.getFromUserId(), msgData.getToGroupId(), msgData.getUserName(), msgData.getUserImage(), msgData.getMsgData().toStringUtf8());
    }

}
