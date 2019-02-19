package com.videolibrary.client.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.mogujie.tt.imservice.event.LoginEvent;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.videolibrary.chat.entity.LiveChatTextMessage;
import com.videolibrary.chat.event.LiveChatLoginEvent;
import com.videolibrary.chat.event.LiveChatMessageEvent;
import com.videolibrary.chat.event.LiveChatSocketEvent;
import com.videolibrary.client.activity.HorizontalVideoClientActivity;
import com.videolibrary.client.activity.VerticalVideoClientActivity;
import com.videolibrary.utils.IntentUtil;
import com.videolibrary.widget.HorizontalVideoRootView;
import com.videolibrary.widget.LiveChatAdapter;
import com.videolibrary.widget.LiveChatListView;
import com.yhy.common.base.NoLeakHandler;
import com.yhy.common.utils.SPUtils;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import org.akita.util.AndroidUtil;

import java.io.Serializable;

import de.greenrobot.event.EventBus;

/**
 * Created with Android Studio.
 * Title:VideoPlayChatFragment
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/8/8
 * Time:10:22
 * Version 1.1.0
 */
public class VideoPlayChatFragment extends Fragment implements NoLeakHandler.HandlerCallback, HorizontalVideoRootView.ImeActionSend {

    protected NoLeakHandler mHandler;

    public LiveChatListView mLiveChatListView;

    private EditText mMessageEdit;
    private TextView mSendButton;

    @Autowired
    IUserService userService;

    public VideoPlayChatFragment() {
        YhyRouter.getInstance().inject(this);
    }

    public static VideoPlayChatFragment getInstance(long liveId) {
        VideoPlayChatFragment videoPlayChatFragment = new VideoPlayChatFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(IntentUtil.BUNDLE_LIVEID, liveId);
        videoPlayChatFragment.setArguments(bundle);
        return videoPlayChatFragment;
    }

    public void onEventMainThread(LiveChatMessageEvent event) {
        switch (event.getEvent()) {
            case SEND_SUCESS:
            case REC:
                mLiveChatListView.add(event.object);
                mLiveChatListView.scrollToBottomListItem();
                if (event.object instanceof LiveChatTextMessage) {
                    LiveChatTextMessage message = (LiveChatTextMessage) event.object;
                    ((HorizontalVideoClientActivity) getActivity()).showBarrageMessage(
                            message.getMessageContent(),
                            message.getFromId() == userService.getLoginUserId());
                }

                break;
            case FORBIN_TALK:
                showForbinTalkDialog();
                break;
        }
    }

    Dialog forbinDialog;

    private void showForbinTalkDialog() {
        forbinDialog = DialogUtil.showMessageDialog(getActivity(), null, "您已经被禁止发言", "确定", null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forbinDialog.dismiss();
            }
        }, null);
        forbinDialog.show();
    }

    public void onEventMainThread(LiveChatLoginEvent event) {
//        String message = null;
//        switch (event) {
//            case LOGINING:
//                message = "登录中";
//                break;
//            case FAIL:
//                message = "登录失败";
//                break;
//            case SUCESS:
//                message = "登录成功";
//                break;
//        }
//        if (TextUtils.isEmpty(message)) return;
//        mLiveChatListView.add(LiveChatNotifyMessage.createMessageByLocal(message));
//        mLiveChatListView.scrollToBottomListItem();

    }

    public void onEventMainThread(LiveChatSocketEvent event) {
//        String message = null;
//        switch (event) {
//            case CONNECTING:
//                message = "连接服务器中...";
//                break;
//            case FAIL:
//                message = "连接服务器失败";
//                break;
//            case SUCESS:
//                message = "连接服务器成功";
//                break;
//        }
//        if (TextUtils.isEmpty(message)) return;
//        mLiveChatListView.add(LiveChatNotifyMessage.createMessageByLocal(message));
//        mLiveChatListView.scrollToBottomListItem();
    }

    private long liveId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        liveId = getArguments().getLong(IntentUtil.BUNDLE_LIVEID);
        Serializable serializable = getArguments().getSerializable(IntentUtil.BUNDLE_CONFIG);
        mHandler = new NoLeakHandler(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        if (mChatService == null) {
//            mConnector.connect(getActivity());
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (isConnector) mConnector.disconnect(getActivity());
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fg_video_play_chat_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(getView(), savedInstanceState);
    }

    private void initView(View view, Bundle savedInstanceState) {
        mMessageEdit = (EditText) view.findViewById(R.id.fg_video_play_chat_message_edit);
        mMessageEdit.setOnEditorActionListener(mOnEditorActionListener);

        mSendButton = (TextView) view.findViewById(R.id.fg_video_play_chat_send_button);
        mSendButton.setOnClickListener(mSendOnClickListener);

        mLiveChatListView = new LiveChatListView(getActivity(),LiveChatAdapter.HALFSCREEN_LAYOUT);
        //mLiveChatListView.setFromUi(LiveChatAdapter.HALFSCREEN_LAYOUT);//设置来源于那个界面
        mLiveChatListView.setLiveType(LiveChatListView.LiveType.VERTICAL);

        ((RelativeLayout) view.findViewById(R.id.fg_video_play_chat_list_layout)).addView(mLiveChatListView,
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    View.OnClickListener mSendOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sendMessage(mMessageEdit.getText().toString());
        }
    };

    @Override
    public void handleMessage(Message msg) {

    }

    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId) {
                case EditorInfo.IME_ACTION_SEND:
                    sendMessage(mMessageEdit.getText().toString());
                    break;
            }
            return true;
        }
    };

    private void sendMessage(String strings) {
        if (!userService.isLogin()) {
            NavUtils.gotoLoginActivity(getActivity());
            return;
        }
        if (TextUtils.isEmpty(strings.trim())) {
            ToastUtil.showToast(getActivity(), "内容为空");
        } else {
            boolean isLogin = userService.isLogin();
            long fromId = 0;
            String fromName = null;
            String fromPic = null;
            if (isLogin) {
                fromId = userService.getLoginUserId();
                fromName = SPUtils.getNickName(getActivity());
                fromPic = SPUtils.getUserIcon(getActivity());
            } else {
                if (getActivity() instanceof HorizontalVideoClientActivity) {
                    if (((HorizontalVideoClientActivity) getActivity()).getChatService() != null) {
                        fromId = ((HorizontalVideoClientActivity) getActivity()).getChatService().liveChatLoginManager.getCurrentUserId();
                    }
                }
                if (getActivity() instanceof VerticalVideoClientActivity) {
                    if (((VerticalVideoClientActivity) getActivity()).getChatService() != null) {
                        fromId = ((VerticalVideoClientActivity) getActivity()).getChatService().liveChatLoginManager.getCurrentUserId();
                    }
                }
            }
            if (getActivity() instanceof HorizontalVideoClientActivity) {
                if (((HorizontalVideoClientActivity) getActivity()).getChatService() != null) {
                    LiveChatTextMessage message = new LiveChatTextMessage(fromId, liveId, fromName, fromPic, strings);
                    ((HorizontalVideoClientActivity) getActivity()).getChatService().liveChatMessageManager.sendMessage(message);
                }
            }
            if (getActivity() instanceof VerticalVideoClientActivity) {
                if (((VerticalVideoClientActivity) getActivity()).getChatService() != null) {
                    LiveChatTextMessage message = new LiveChatTextMessage(fromId, liveId, fromName, fromPic, strings);
                    ((VerticalVideoClientActivity) getActivity()).getChatService().liveChatMessageManager.sendMessage(message);
                }
            }
            mMessageEdit.setText("");
            AndroidUtil.hideIME(getActivity(), true);
        }
    }

    public void onEvent(LoginEvent event) {
        switch (event) {
            case LOGIN_UT_OK: {
                if (getActivity() instanceof HorizontalVideoClientActivity) {
                    if (((HorizontalVideoClientActivity) getActivity()).getChatService() != null) {
                        ((HorizontalVideoClientActivity) getActivity()).getChatService().liveChatLoginManager.login(liveId);
                    }
                }
            }
            break;
        }
    }


    /**
     * 客户端聊天消息的发送
     *
     * @param message 消息内容
     */
    @Override
    public void onSendMessage(String message) {
        sendMessage(message);
    }
}
