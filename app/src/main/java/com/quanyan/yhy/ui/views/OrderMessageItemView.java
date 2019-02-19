package com.quanyan.yhy.ui.views;

import android.content.Context;
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
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.yhy.common.beans.net.model.notification.NotificationOrderMessage;
import com.yhy.common.constants.NotificationConstants;
import com.yhy.common.constants.ValueConstants;

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
public class OrderMessageItemView extends LinearLayout {
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
    NotificationOrderMessage notificationOrderMessage;

    public OrderMessageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public OrderMessageItemView(Context context) {
        super(context);
        init(context);
    }

    private void init(final Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.item_notification_order_message, this);
        ViewUtils.inject(this);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notificationOrderMessage == null || notificationOrderMessage.getBusType() != NotificationConstants.BUS_TYPE_USER)
                    return;
                if (ValueConstants.ORDER_TYPE_EXPERT_ACTIVITY.equals(notificationOrderMessage.getOrderType())) {
                    NavUtils.gotoExpertOrderDetailActivity(context, 0, notificationOrderMessage.getOrderId());
                } else {
                    NavUtils.gotoOrderDetailsActivity(context, notificationOrderMessage.getOrderType(), notificationOrderMessage.getOrderId());
                }
                IMNotificationManager.instance().cancelSessionNotifications(String.valueOf(notificationOrderMessage.getMessageId()));
            }
        });
    }

    public void loadData(NotificationOrderMessage message, boolean isLastItem) {
        notificationOrderMessage = message;
        icon.setImageResource(message.getIconResId());
        title.setText(message.getTitle());
        time.setText(message.getDisplayTime());
        this.message.setText(message.getMessage());
        divider.setVisibility(isLastItem ? View.GONE : View.VISIBLE);
    }
}
