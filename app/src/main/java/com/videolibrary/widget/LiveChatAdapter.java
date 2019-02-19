package com.videolibrary.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.videolibrary.chat.Constants;
import com.videolibrary.chat.entity.LiveChatNotifyMessage;
import com.videolibrary.chat.entity.LiveChatTextMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:LiveChatAdapter
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/8/16
 * Time:18:49
 * Version 1.0
 */
public class LiveChatAdapter extends BaseAdapter {
    public Context context;
    public LiveChatListView.LiveType liveType = LiveChatListView.LiveType.VERTICAL;
    private LiveChatTextMessageView.OnNickNameOnclickListener onNickNameOnclickListener;
    public static final int ANCHOR_TYPE = 1;
    public static final int VIEWER_TYPE = 2;
    private int fromUi = 3 ;//：1:主播界面，2：全屏观看见面 3：下面也有白色布局聊天列表的观看界面
    private int chatType;
    public static final int ANCHOR_LAYOUT = 1;
    public static final int FULLSCREEN_LAYOUT = 2;
    public static final int HALFSCREEN_LAYOUT = 3;

    public LiveChatAdapter(Context context) {
        this.context = context;
    }

    public LiveChatAdapter(Context context,int fromUi) {
        this.context = context;
        this.fromUi = fromUi;
    }

    List<Object> objectList = new ArrayList<>();

    @Override
    public int getCount() {
        return objectList.size();
    }

    @Override
    public Object getItem(int position) {
        return objectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (objectList.get(position) instanceof LiveChatTextMessage) {
            return Constants.MSG_TYPE_TEXT;
        } else if (objectList.get(position) instanceof LiveChatNotifyMessage) {
            return Constants.MSG_TYPE_NOTIFY;
        }
        return Constants.MSG_TYPE_UNKNOWN;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            int type = getItemViewType(position);
            switch (type) {
                case Constants.MSG_TYPE_TEXT:
                    convertView = new LiveChatTextMessageView(context);
                    ((LiveChatTextMessageView) convertView).setOnNickNameOnclickListener(onNickNameOnclickListener);
                    break;
                case Constants.MSG_TYPE_NOTIFY:
                    convertView = new LiveChatNotifyMessageView(context);
                    break;
            }
        }
        if (convertView instanceof LiveChatMessageViewListener) {
            ((LiveChatMessageViewListener) convertView).loadEntity(objectList.get(position), liveType ,fromUi);
        }
        return convertView;
    }

    public void setLiveType(LiveChatListView.LiveType liveType) {
        this.liveType = liveType;
    }

    public void add(Object object) {
        objectList.add(object);
        notifyDataSetChanged();
    }

    public void setOnNickNameOnclickListener(LiveChatTextMessageView.OnNickNameOnclickListener onNickNameOnclickListener) {
        this.onNickNameOnclickListener = onNickNameOnclickListener;
    }

    /*public void setChatType(int chatType){
        this.chatType = chatType;
    }*/

    public void setFromUi(int fromUi){
        this.fromUi = fromUi;
    }
}
