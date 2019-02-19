package com.videolibrary.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.videolibrary.metadata.NetStateBean;
import com.videolibrary.utils.NetWorkUtil;

import de.greenrobot.event.EventBus;

/**
 * Created with Android Studio.
 * Title:NetBroadCast
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:9/9/16
 * Time:16:36
 * Version 1.1.0
 */
public class NetBroadCast extends BroadcastReceiver {

    public NetBroadCast(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            EventBus.getDefault().post(new NetStateBean(NetWorkUtil.getNetworkState(context)));
            switch (NetWorkUtil.getNetworkState(context)){
                case NetWorkUtil.NETWORK_NONE:
                    break;
                case NetWorkUtil.NETWORK_2G:
                    break;
                case NetWorkUtil.NETWORK_3G:
                    break;
                case NetWorkUtil.NETWORK_4G:
                    break;
                case NetWorkUtil.NETWORK_MOBILE:
                    break;
                case NetWorkUtil.NETWORK_WIFI:
                    break;
            }
        }
    }
}
