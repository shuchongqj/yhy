package com.videolibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.quanyan.yhy.R;
import com.videolibrary.chat.entity.LiveChatNotifyMessage;

/**
 * Created with Android Studio.
 * Title:LiveChatListView
 * Description:直播聊天listview
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/8/16
 * Time:16:22
 * Version 1.0
 */
public class LiveChatListView extends LinearLayout {
    private ListView mListView;
    private LiveChatAdapter mAdatper;
    private LiveChatTextMessageView.OnNickNameOnclickListener onNickNameOnclickListener;

    private int fromUi = 3;
    public void setFromUi(int fromUi) {
        this.fromUi = fromUi;
    }

    public void add(Object object) {
        mAdatper.add(object);
    }

    public void scrollToBottomListItem() {
        mListView.setSelection(mAdatper.getCount() + 1);
    }

    public void setOnNickNameOnclickListener(LiveChatTextMessageView.OnNickNameOnclickListener onNickNameOnclickListener) {
        this.onNickNameOnclickListener = onNickNameOnclickListener;
        if (mAdatper != null) mAdatper.setOnNickNameOnclickListener(onNickNameOnclickListener);
    }

    public void addImConfig(String configContent) {
        if (configContent == null || configContent.length() == 0) {
            return;
        }

        LiveChatNotifyMessage message = LiveChatNotifyMessage.createMessageByLocal("鹰和鹰直播: " + configContent);
        mAdatper.add(message);

    }

    public enum LiveType {
        HORIZONTAL, VERTICAL
    }

    public LiveChatListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LiveChatListView(Context context,int formUi) {
        super(context);
        fromUi = formUi;
        init(context);
    }

    public LiveChatListView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.base_listview, this);
        mListView = (ListView) findViewById(R.id.base_listview);
        mListView.setDivider(null);
        mListView.setDividerHeight(6);
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setAdapter(mAdatper = new LiveChatAdapter(context,fromUi));
    }

    public void setLiveType(LiveType liveType) {
        if (liveType == LiveType.HORIZONTAL) {
//            mListView.setBackgroundColor(getResources().getColor(R.color.transparent_50));
            mListView.setDividerHeight(31);
        } else if (liveType == LiveType.VERTICAL) {
            mListView.setDividerHeight(30);
//            mListView.setBackgroundColor(getResources().getColor(R.color.white));
        }
        mAdatper.setLiveType(liveType);

        mAdatper.notifyDataSetChanged();
    }

}
