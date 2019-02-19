package com.videolibrary.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.yhy.R;
import com.videolibrary.chat.entity.LiveChatNotifyMessage;

/**
 * Created with Android Studio.
 * Title:LiveChatNotifyMessageView
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/8/17
 * Time:15:36
 * Version 1.0
 */
public class LiveChatNotifyMessageView extends LinearLayout implements LiveChatMessageViewListener {
    Context context;
    TextView textView;

    public LiveChatNotifyMessageView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.item_live_chat_notify, this);
        textView = (TextView) this.findViewById(R.id.textview);
    }

    @Override
    public void loadEntity(Object object, LiveChatListView.LiveType liveType ,int chatType) {
        LiveChatNotifyMessage message = null;
        if (object instanceof LiveChatNotifyMessage) {
            message = (LiveChatNotifyMessage) object;
        }
        if (message == null) return;
        if (liveType == LiveChatListView.LiveType.VERTICAL) {
//            textView.setBackgroundResource(R.drawable.shape_live_chat_text_bg_user);
        } else if (liveType == LiveChatListView.LiveType.HORIZONTAL) {
//            textView.setBackgroundResource(R.drawable.shape_live_chat_text_bg_master);
        }

        textView.setText(message.getMessageContent());
        textView.setTextColor(getResources().getColor(R.color.orange_ying));
    }
}
