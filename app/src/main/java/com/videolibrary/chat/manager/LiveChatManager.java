package com.videolibrary.chat.manager;

import android.content.Context;

/**
 * Created with Android Studio.
 * Title:LiveChatManager
 * Description:直播base manager
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/8/12
 * Time:15:38
 * Version 1.0
 */
public abstract class LiveChatManager {
    private Context mContext;

    protected Context getContext() {
        return mContext;
    }

    public void onStartIMManager(Context ctx) {
        mContext = ctx;
        doOnStart();
    }

    public abstract void doOnStart();

    public abstract void doOnStop();



}
