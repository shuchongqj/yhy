package com.quanyan.yhy.service.controller;

import android.content.Context;
import android.os.Handler;

import com.quanyan.base.BaseController;
import com.quanyan.yhy.database.DBManager;
import com.quanyan.yhy.net.NetThreadManager;
import com.yhy.common.beans.im.NotificationMessageEntity;
import com.yhy.common.beans.net.model.notification.NotificationInteractiveMessage;
import com.yhy.common.beans.net.model.notification.NotificationOrderMessage;
import com.yhy.common.constants.NotificationConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:NotiMessageController
 * Description:通知消息
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2015/12/17
 * Time:13:37
 * Version 1.0
 */
public class NotiMessageController extends BaseController {
    public static final int LOAD_MESSAGE_SUCCESS = 1;
    public static final int LOAD_MESSAGE_FAIL = 2;

    public NotiMessageController(Context context, Handler handler) {
        super(context, handler);
    }

    public void loadMessageFromDb(final Context context,final int bizType, final long mixId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                List<NotificationMessageEntity> list = DBManager.getInstance(context).getNotificationMessageEntity(bizType, mixId);
                if (list != null && list.size() > 0) {
                    List<NotificationMessageEntity> replaylist = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isNewVersion()) {
                            replaylist.add(list.get(i));
                        } else if (list.get(i).getBizType() == NotificationConstants.BIZ_TYPE_TRANSACTION) {
                            replaylist.add(NotificationOrderMessage.parseFromDB(list.get(i)));
                        } else if (list.get(i).getBizType() == NotificationConstants.BIZ_TYPE_INTERACTION) {
                            replaylist.add(NotificationInteractiveMessage.parseFromDB(list.get(i)));
                        }
                    }
                    sendMessage(LOAD_MESSAGE_SUCCESS, bizType, 0, replaylist);
                } else {
                    sendMessage(LOAD_MESSAGE_FAIL, bizType, 0, null);
                }
            }
        };
        NetThreadManager.getInstance().execute(runnable);
    }
}
