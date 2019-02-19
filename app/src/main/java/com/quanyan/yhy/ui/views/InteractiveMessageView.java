package com.quanyan.yhy.ui.views;

import android.app.Activity;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.mogujie.tt.imservice.manager.IMNotificationManager;
import com.newyhy.config.CircleItemType;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.videolibrary.utils.IntentUtil;
import com.yhy.common.beans.net.model.discover.UgcInfoResult;
import com.yhy.common.beans.net.model.notification.NotificationInteractiveMessage;
import com.yhy.common.constants.NotificationConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.router.YhyRouter;

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
public class InteractiveMessageView extends LinearLayout {
    @ViewInject(R.id.iv_icon)
    ImageView icon;
    @ViewInject(R.id.tv_name)
    TextView userName;
    @ViewInject(R.id.tv_time)
    TextView time;
    @ViewInject(R.id.tv_comment)
    TextView commnet;
    @ViewInject(R.id.iv_pic)
    ImageView pic;
    @ViewInject(R.id.tv_content)
    TextView content;
    private Context context;
    NotificationInteractiveMessage notificationInteractiveMessage;

    public InteractiveMessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InteractiveMessageView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.item_noti_interative_message, this);
        ViewUtils.inject(this);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notificationInteractiveMessage == null) return;
                switch (notificationInteractiveMessage.getBizSubType()) {
                    // 动态详情
                    case NotificationConstants.RECEIVE_PRAISE:
                    case NotificationConstants.RECEIVE_COMMENT:
                        doGetLiveDetail(context, notificationInteractiveMessage.getOutId(), notificationInteractiveMessage.getBizSubType());

//                        YhyRouter.getInstance().startCircleDetailActivity(context, notificationInteractiveMessage.getOutId(), false);
                        break;
                    // 文章详情
                    case NotificationConstants.ARTICLE_PRAISE:
                    case NotificationConstants.ARTICLE_COMMENT:
                        String url = SPUtils.getURL_QUANZI_ARTICLE(context);
                        if (TextUtils.isEmpty(url))
                            return;
                        NavUtils.startWebview((Activity) context, "", url + notificationInteractiveMessage.getOutId(), -1);
                        break;
                    // 精彩视频
                    case NotificationConstants.WANDERFULL_VIDEO_PRAISE:
                    case NotificationConstants.WANDERFULL_VIDEO_COMMENT:
                        // 赛事视频
                    case NotificationConstants.MATCH_VIDEO_PRAISE:
                    case NotificationConstants.MATCH_VIDEO_COMMENT:
                        doGetLiveDetail(context, notificationInteractiveMessage.getOutId(), notificationInteractiveMessage.getBizSubType());
                        break;
                }
//               NavUtils.gotoLiveDetailActivity((Activity) InteractiveMessageView.this.context, notificationInteractiveMessage.getOutId(), null, ValueConstants.TYPE_COMMENT_LIVESUP, ValueConstants.TYPE_PRAISE_LIVESUP, false);
                IMNotificationManager.instance().cancelSessionNotifications(String.valueOf(notificationInteractiveMessage.getMessageId()));
            }
        });
    }

    public void loadData(NotificationInteractiveMessage item) {
        notificationInteractiveMessage = item;
        time.setText(item.getDisplayTime());

        if (!StringUtil.isEmpty(item.getUserPhoto())) {
            ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(item.getUserPhoto()), R.mipmap.icon_default_avatar, (int) context.getResources().getDimension(R.dimen.dd_dimen_88px), (int) context.getResources().getDimension(R.dimen.dd_dimen_88px), icon);
        } else {
            icon.setImageResource(R.mipmap.icon_default_avatar);
        }

        userName.setText(item.getNickName());

        if (!StringUtil.isEmpty(item.getVideoPicUrl())) {
            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(item.getVideoPicUrl()), R.mipmap.icon_default_150_150, (int) context.getResources().getDimension(R.dimen.dd_dimen_88px), (int) context.getResources().getDimension(R.dimen.dd_dimen_88px), pic);
            pic.setVisibility(View.VISIBLE);
            content.setVisibility(View.GONE);
        } else if (!StringUtil.isEmpty(item.getSubjectImage())) {
            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(item.getSubjectImage()), R.mipmap.icon_default_150_150, (int) context.getResources().getDimension(R.dimen.dd_dimen_88px), (int) context.getResources().getDimension(R.dimen.dd_dimen_88px), pic);

            pic.setVisibility(View.VISIBLE);
            content.setVisibility(View.GONE);
        } else {
            content.setText(item.getSubjectContent());
            content.setVisibility(View.VISIBLE);
            pic.setVisibility(View.INVISIBLE);
        }
        if (item.getBizSubType() == NotificationConstants.RECEIVE_PRAISE ||
                item.getBizSubType() == NotificationConstants.ARTICLE_PRAISE ||
                item.getBizSubType() == NotificationConstants.WANDERFULL_VIDEO_PRAISE ||
                item.getBizSubType() == NotificationConstants.MATCH_VIDEO_PRAISE) {               //  各种点赞
            SpannableStringBuilder spannableString = new SpannableStringBuilder("  点赞");
            spannableString.setSpan(new ImageSpan(context, R.mipmap.heart_red), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            commnet.setText(spannableString);
        } else if (TextUtils.isEmpty(item.getReplyName())) {                                      //  各种评论
            commnet.setText(item.getMessage());
        } else {                                                                                  //  各种回复
            String replyName = item.getReplyName();
            String replyString = context.getString(R.string.reply, replyName);
            int start = replyString.indexOf(replyName);
            int lenth = replyName.length();
            SpannableStringBuilder builder = new SpannableStringBuilder(replyString);
            builder.append(item.getMessage());
            builder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.main)), start, start + lenth, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            commnet.setText(builder);
        }

    }

    public void doGetLiveDetail(Context context, long id, int bizSubType) {
        NetManager.getInstance(context).doGetUGCDetail(id, new OnResponseListener<UgcInfoResult>() {
            @Override
            public void onComplete(boolean isOK, UgcInfoResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null){
                        return;
                    }
                    switch (bizSubType) {
                        // 动态详情
                        case NotificationConstants.RECEIVE_PRAISE:
                        case NotificationConstants.RECEIVE_COMMENT:
                        // 精彩视频
                        case NotificationConstants.WANDERFULL_VIDEO_PRAISE:
                        case NotificationConstants.WANDERFULL_VIDEO_COMMENT:
                            // 赛事视频
                        case NotificationConstants.MATCH_VIDEO_PRAISE:
                        case NotificationConstants.MATCH_VIDEO_COMMENT:

                            int type = 0;
                            if (result.contentType == 2) type =  CircleItemType.UGC_PIC;//无图带图Ugc
                            if (result.contentType == 3) type = CircleItemType.LIVE;//直播
                            if (result.shortVideoType == 4 || result.shortVideoType == 5) type =  CircleItemType.COFFEE_VIDEO;//小视频
                            int liveStatus = 0;
                            if ("START_LIVE".equals(result.liveStatus)) {
                                liveStatus = 1;
                            } else if ("REPLAY_LIVE".equals(result.liveStatus)) {
                                liveStatus = 2;
                            }
                            if(type == CircleItemType.UGC_PIC){
                                YhyRouter.getInstance().startCircleDetailActivity(context, notificationInteractiveMessage.getOutId(), false);
                            }else {
                                if (type == CircleItemType.LIVE) {
                                    IntentUtil.startVideoClientActivity(result.liveId, 0,liveStatus == 1,result.liveScreenType);
                                }else if (type == CircleItemType.COFFEE_VIDEO) {
                                    IntentUtil.startVideoClientActivity(result.liveId, 0,false,1,result.videoUrl,result.id);
                                }
                            }
                            break;
                    }
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
            }
        });
    }


}
