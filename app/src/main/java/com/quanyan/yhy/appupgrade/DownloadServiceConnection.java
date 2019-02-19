package com.quanyan.yhy.appupgrade;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created with Android Studio.
 * Title:DownloadServiceConnection
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2015/12/30
 * Time:17:31
 * Version 1.0
 */
public class DownloadServiceConnection implements ServiceConnection {

    private DownloadService.DownloadServiceBinder binder;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        this.binder = (DownloadService.DownloadServiceBinder) service;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    public DownloadService getService() {
        return binder.getService();
    }
}
