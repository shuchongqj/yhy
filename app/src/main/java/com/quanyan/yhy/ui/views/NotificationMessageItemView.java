package com.quanyan.yhy.ui.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.mogujie.tt.imservice.manager.IMNotificationManager;
import com.quanyan.yhy.R;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.ui.base.utils.NativeUtils;
import com.yhy.common.beans.im.NotificationMessageEntity;

/**
 * Created with Android Studio.
 * Title:OrderMessageItemView
 * Description:订单类型的推送消息
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2015/12/16
 * Time:16:28
 * Version 1.0
 */
public class NotificationMessageItemView extends LinearLayout {
    @ViewInject(R.id.iv_icon)
    ImageView icon;
    @ViewInject(R.id.tv_title)
    TextView title;
    @ViewInject(R.id.tv_time)
    TextView time;
    @ViewInject(R.id.tv_message)
    TextView message;
    @ViewInject(R.id.divider)
    View divider;
    private Context mContext;
    NotificationMessageEntity notificationMessageEntity;

    public NotificationMessageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NotificationMessageItemView(Context context) {
        super(context);
        init(context);
    }

    private void init(final Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.item_notification_order_message2, this);
        ViewUtils.inject(this);
        setOnClickListener(v -> {
            IMNotificationManager.instance().cancelSessionNotifications(String.valueOf(notificationMessageEntity.getMessageId()));
            Analysis.pushEvent(context, AnEvent.MYMESSAGE,String.valueOf(notificationMessageEntity.getMessageId()));
            Intent intent = NativeUtils.getIntent(notificationMessageEntity.getMessageOperationCode(), notificationMessageEntity.getMessageOperationVaule(), mContext, true);
            if (intent == null) return;
            mContext.startActivity(intent);
        });
    }

    public void loadData(NotificationMessageEntity entity, boolean isLastItem) {
        notificationMessageEntity = entity;
        int iconResId = R.mipmap.ic_system;
        if (entity.getBizType() == 5) {
            iconResId = R.mipmap.ic_activity;
        }
        icon.setImageResource(iconResId);
        title.setText(notificationMessageEntity.getTitle());
        time.setText(notificationMessageEntity.getDisPlayTime());
        this.message.setText(notificationMessageEntity.getMessage());
        divider.setVisibility(isLastItem ? View.GONE : View.VISIBLE);
    }
}
