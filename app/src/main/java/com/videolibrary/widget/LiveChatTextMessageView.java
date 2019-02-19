package com.videolibrary.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.quanyan.yhy.R;
import com.videolibrary.chat.entity.LiveChatMessageEntity;
import com.videolibrary.chat.entity.LiveChatTextMessage;
import com.yhy.common.utils.SPUtils;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

/**
 * Created with Android Studio.
 * Title:LiveChatTextMessage
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/8/16
 * Time:17:17
 * Version 1.0
 */
public class LiveChatTextMessageView extends LinearLayout implements LiveChatMessageViewListener {
    Context context;
    TextView textView;

    @Autowired
    IUserService userService;

    public LiveChatTextMessageView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.item_live_chat_text, this);
        textView = (TextView) this.findViewById(R.id.textview);
        YhyRouter.getInstance().inject(this);
    }

    OnNickNameOnclickListener onNickNameOnclickListener;

    public void setOnNickNameOnclickListener(OnNickNameOnclickListener onNickNameOnclickListener) {
        this.onNickNameOnclickListener = onNickNameOnclickListener;
    }

    @Override
    public void loadEntity(Object object, LiveChatListView.LiveType liveType,int fromUi) {
        LiveChatTextMessage message = null;
        if (object instanceof LiveChatTextMessage) {
            message = (LiveChatTextMessage) object;
        }
        if (message == null) return;
        /*if (message.getContentColorResId() != -1) {
            textView.setTextColor(getResources().getColor(message.getContentColorResId()));
        } else if (liveType == LiveChatListView.LiveType.VERTICAL) {
//            textView.setBackgroundResource(R.drawable.shape_live_chat_text_bg_user);
            textView.setTextColor(getResources().getColor(R.color.neu_666666));
        } else if (liveType == LiveChatListView.LiveType.HORIZONTAL) {
//            textView.setBackgroundResource(R.drawable.shape_live_chat_text_bg_master);
            textView.setTextColor(getResources().getColor(R.color.white));
        }*/

        textView.setText("");
        switch (fromUi) {
            case 1://主播端

                if (message.getFromId() == userService.getLoginUserId()) {//昵称颜色,根据用户身份区分
                    SpannableString name = new SpannableString(message.getFromName()+":");
                    ForegroundColorSpan spanName = new ForegroundColorSpan(context.getResources().getColor(R.color.user_self));
                    name.setSpan(spanName, 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textView.append(name);
                } else {
                    SpannableString name = new SpannableString(message.getFromName()+":");
                    ForegroundColorSpan spanName = new ForegroundColorSpan(context.getResources().getColor(R.color.other_user));
                    name.setSpan(spanName, 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textView.append(name);
                }

                SpannableString content = new SpannableString(message.getMessageContent());
                ForegroundColorSpan spanC = new ForegroundColorSpan(Color.WHITE);
                content.setSpan(spanC, 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.append(content);
                break;

            case 2://全屏观看端

                if (message.getFromId() == userService.getLoginUserId()) {//昵称颜色,根据用户身份区分
                    SpannableString name = new SpannableString(message.getFromName()+":");
                    ForegroundColorSpan spanName = new ForegroundColorSpan(context.getResources().getColor(R.color.user_self));
                    name.setSpan(spanName, 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textView.append(name);
                } else {
                    SpannableString name = new SpannableString(message.getFromName()+":");
                    ForegroundColorSpan spanName = new ForegroundColorSpan(context.getResources().getColor(R.color.other_user));
                    name.setSpan(spanName, 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textView.append(name);
                }

                SpannableString content2 = new SpannableString(message.getMessageContent());
                ForegroundColorSpan spanC2 = new ForegroundColorSpan(Color.BLACK);
                content2.setSpan(spanC2, 0, content2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.append(content2);
                break;

            case 3://半屏观看端

                if (message.getFromId() == userService.getLoginUserId()) {//昵称颜色,根据用户身份区分
                    SpannableString name = new SpannableString(message.getFromName()+":");
                    ForegroundColorSpan spanName = new ForegroundColorSpan(context.getResources().getColor(R.color.user_self));
                    name.setSpan(spanName, 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textView.append(name);
                } else {
                    SpannableString name = new SpannableString(message.getFromName()+":");
                    ForegroundColorSpan spanName = new ForegroundColorSpan(context.getResources().getColor(R.color.other_user_half));
                    name.setSpan(spanName, 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textView.append(name);
                }

                SpannableString content3 = new SpannableString(message.getMessageContent());
                ForegroundColorSpan spanC3 = new ForegroundColorSpan(Color.BLACK);
                content3.setSpan(spanC3, 0, content3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.append(content3);
                break;

        }

     /*   if (message.getFromId() == SPUtils.getUid(context)){//自己
            if (chatType == LiveChatAdapter.ANCHOR_TYPE) {//直播端
                SpannableString name = new SpannableString(message.getFromName()+":");
                ForegroundColorSpan span1 = new ForegroundColorSpan(context.getResources().getColor(R.color.user_self));
                name.setSpan(span1, 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                SpannableString content = new SpannableString(message.getMessageContent());
                ForegroundColorSpan span2 = new ForegroundColorSpan(Color.WHITE);
                content.setSpan(span2, 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                textView.append(name);
                textView.append(content);
            }else {//观看端（评论是白底观看端）
                SpannableString name = new SpannableString(message.getFromName()+":");
                ForegroundColorSpan span1 = new ForegroundColorSpan(context.getResources().getColor(R.color.user_self));
                name.setSpan(span1, 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                SpannableString content = new SpannableString(message.getMessageContent());
                ForegroundColorSpan span2 = new ForegroundColorSpan(Color.BLACK);
                content.setSpan(span2, 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                textView.append(name);
                textView.append(content);
            }

        }else {//他人

            if (chatType == LiveChatAdapter.ANCHOR_TYPE) {//直播端
                SpannableString name = new SpannableString(message.getFromName()+":");
                ForegroundColorSpan span1 = new ForegroundColorSpan(context.getResources().getColor(R.color.other_user));
                name.setSpan(span1, 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                SpannableString content = new SpannableString(message.getMessageContent());
                ForegroundColorSpan span2 = new ForegroundColorSpan(Color.WHITE);
                content.setSpan(span2, 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                textView.append(name);
                textView.append(content);
            }else {//观看端（评论是白底观看端）
                SpannableString name = new SpannableString(message.getFromName()+":");
                ForegroundColorSpan span1 = new ForegroundColorSpan(context.getResources().getColor(R.color.other_user));
                name.setSpan(span1, 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                SpannableString content = new SpannableString(message.getMessageContent());
                ForegroundColorSpan span2 = new ForegroundColorSpan(Color.BLACK);
                content.setSpan(span2, 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                textView.append(name);
                textView.append(content);
            }
        }*/

        /*Log.e("EZEZ",message.getMessageContent());
        textView.setText(message.getDisplayContent(context, onNickNameOnclickListener));
        Log.e("EZEZ",message.getDisplayContent(context, onNickNameOnclickListener)+"");*/
        /*textView.setMovementMethod(LinkMovementMethod.getInstance());*/
    }

    public interface OnNickNameOnclickListener {
        void onClick(LiveChatMessageEntity entity);
    }
}
