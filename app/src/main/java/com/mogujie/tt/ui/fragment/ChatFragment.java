package com.mogujie.tt.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.mogujie.tt.imservice.event.ConsultEvent;
import com.mogujie.tt.imservice.event.SessionEvent;
import com.mogujie.tt.imservice.event.UnreadEvent;
import com.mogujie.tt.imservice.event.UserInfoEvent;
import com.mogujie.tt.imservice.service.IMService;
import com.mogujie.tt.imservice.support.IMServiceConnector;
import com.mogujie.tt.ui.adapter.ChatAdapter;
import com.mogujie.tt.utils.IMUIHelper;
import com.mogujie.tt.utils.Logger;
import com.newyhy.config.UmengEvent;
import com.quanyan.base.BaseFragment;
import com.yhy.common.utils.NetworkUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshScrollDeleteListView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.consult.controller.ConsultController;
import com.quanyan.yhy.view.NetWorkErrorView;
import com.umeng.analytics.MobclickAgent;
import com.yhy.common.beans.im.entity.RecentInfo;
import com.yhy.common.beans.net.model.tm.AcceptProcessOrderResult;
import com.yhy.common.beans.net.model.tm.ConsultInfo;
import com.yhy.common.beans.net.model.tm.ConsultState;
import com.yhy.common.constants.ConsultContants;
import com.yhy.common.constants.DBConstant;
import com.yhy.common.constants.NotificationConstants;
import com.yhy.common.constants.ValueConstants;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created with Android Studio.
 * Title:ChatFragment
 * Description:最近消息列表
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/3/3
 * Time:10:10
 * Version 1.0
 */
public class ChatFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    Logger logger = Logger.getLogger(ChatFragment.class);
    private ChatAdapter mContactAdapter;
    private PullToRefreshScrollDeleteListView mListView;
    private IMService imService;
    private TextView mQueue;
    private View mTop;
    private TextView mAccpect;
    private View mLLNetWork;

    @Override
    public View onLoadContentView() {
        return View.inflate(getActivity(), R.layout.tt_activity_chat, null);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        initView(view);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mImServiceConnector.connect(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mImServiceConnector.disconnect(getActivity());
        imService = null;
    }

    ConsultController consultController;

    private void initView(View v) {
        consultController = new ConsultController(getActivity(), mHandler);
        mTop = v.findViewById(R.id.rl_top);
        mLLNetWork = v.findViewById(R.id.ll_network);
        mAccpect = v.findViewById(R.id.tv_accpect);
        mQueue = v.findViewById(R.id.tv_queue);
        mAccpect.setOnClickListener(accpectListener);
        mListView = v.findViewById(R.id.pull_to_refresh_listview);
        mListView.setOnItemClickListener(this);
        mListView.setMode(PullToRefreshBase.Mode.DISABLED);
        mContactAdapter = new ChatAdapter(getActivity());
        mListView.setAdapter(mContactAdapter);
        NetWorkErrorView errorView = new NetWorkErrorView(getActivity());
        errorView.show(R.mipmap.error_empty_icon, getString(R.string.message_empty_list_message), null, null, null);
        mListView.setEmptyView(errorView);
    }

    BroadcastReceiver mNetBroadCast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                if (NetworkUtil.isNetworkAvailable(getActivity())) {
                    mLLNetWork.setVisibility(View.GONE);
                    if (imService != null && imService.getConsultManager() != null)
                        imService.getConsultManager().requestConsultInfo(false);
                } else {
                    mLLNetWork.setVisibility(View.VISIBLE);
                    mTop.setVisibility(View.GONE);
                }
            }
        }
    };

    private View.OnClickListener accpectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showLoadingView(null);
            TCEventHelper.onEvent(v.getContext(), AnalyDataValue.IM_CONSULTING_ACCEPT);
            consultController.acceptProcessOrder(getActivity());
        }
    };

    private View.OnClickListener gotoMessageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (consultState != null && consultState.processOrder != null)
                NavUtils.gotoMessageActivity(getActivity(), consultState.processOrder.buyerInfo.userId, null, consultState.processOrder.itemId);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (imService != null) {
            imService.getNotificationManager().cancelSessionNotifications(String.valueOf(DBConstant.SESSION_TYPE_SINGLE));
        }
        if (!isNetworkRegisted) {
            IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            getActivity().registerReceiver(mNetBroadCast, intentFilter);
            isNetworkRegisted = true;
        }
    }

    boolean isNetworkRegisted = false;
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        String sessionKey = getIntent().getStringExtra(IntentConstant.KEY_SESSION_KEY);
//        if (sessionKey != null) {
//            IMUIHelper.openChatActivity(this, sessionKey);
//        }
//    }

    private IMServiceConnector mImServiceConnector = new IMServiceConnector() {

        @Override
        public void onServiceDisconnected() {
            if (EventBus.getDefault().isRegistered(ChatFragment.this)) {
                EventBus.getDefault().unregister(ChatFragment.this);
            }
        }

        @Override
        public void onIMServiceConnected() {
            // TODO Auto-generated method stub
            logger.d("chatfragment#recent#onIMServiceConnected");
            imService = mImServiceConnector.getIMService();
            if (imService == null) {
                // why ,some reason
                return;
            }
            imService.getNotificationManager().cancelSessionNotifications(String.valueOf(DBConstant.SESSION_TYPE_SINGLE));
            // 依赖联系人回话、未读消息、用户的信息三者的状态
            onRecentContactDataReady();
            EventBus.getDefault().registerSticky(ChatFragment.this);
            imService.getConsultManager().requestConsultInfo(false);
        }
    };

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        try {
            if (isNetworkRegisted) {
                getActivity().unregisterReceiver(mNetBroadCast);
                isNetworkRegisted = false;
            }
        } catch (IllegalArgumentException e) {

        }
    }

    private void onRecentContactDataReady() {
        boolean isUserData = imService.getContactManager().isUserDataReady();
        boolean isSessionData = imService.getSessionManager().isSessionListReady();
//        boolean isConsultData = imService.getConsultManager().isConsultDataReady();
        if (!(isUserData && isSessionData)) {
            return;
        }
        List<RecentInfo> recentSessionList = imService.getSessionManager().getRecentListInfo();
        imService.getConsultManager().checkAllConsultInfoUpdate();
        mContactAdapter.setData(recentSessionList, imService);
    }

    public void onEventMainThread(SessionEvent sessionEvent) {
        logger.d("chatfragment#SessionEvent# -> %s", sessionEvent);
        switch (sessionEvent) {
            case RECENT_SESSION_LIST_UPDATE:
            case RECENT_SESSION_LIST_SUCCESS:
            case SET_SESSION_TOP:
                onRecentContactDataReady();
                break;
        }
    }

    public void onEventMainThread(UnreadEvent event) {
        switch (event.event) {
            case UNREAD_MSG_RECEIVED:
            case UNREAD_MSG_LIST_OK:
            case SESSION_READED_UNREAD_MSG:
                onRecentContactDataReady();
                break;
        }
    }

    public void onEventMainThread(UserInfoEvent event) {
        switch (event) {
            case USER_INFO_UPDATE:
            case USER_INFO_OK:
                onRecentContactDataReady();
                break;
        }
    }

    ConsultState consultState;

    public void onEventMainThread(ConsultEvent event) {
        switch (event.event) {
            case SESSION_CONSULT_OK:
                onRecentContactDataReady();
                break;
            case CONSULT_QUEUE_UPDATE:
                consultState = (ConsultState) event.object;
                refreshQueue(consultState);
                break;
        }
    }

    private void refreshQueue(ConsultState state) {
        ConsultInfo consultInfo = state.consultInfo;
        long lenth = consultInfo.sellerQueueLength;
        if (lenth > 0) {
            mTop.setVisibility(View.VISIBLE);
            if (consultInfo.ableToAcceptOrder) {
                mQueue.setText(getString(R.string.tip_chat_top_consult_queue_accept, lenth));
                mAccpect.setText(getString(R.string.accpect_order));
                mAccpect.setOnClickListener(accpectListener);
                mAccpect.setVisibility(View.VISIBLE);
            } else {
                mQueue.setText(getString(R.string.tip_chat_top_consulting_queue, lenth));
                mAccpect.setText(getString(R.string.back_chat));
                mAccpect.setOnClickListener(gotoMessageListener);
                mAccpect.setVisibility(View.VISIBLE);
            }
        } else {
            if (state.processOrder != null && state.processOrder.processOrderStatus.equals(ConsultContants.STATUS_CONSULT_IN_CHAT)) {
                mTop.setVisibility(View.VISIBLE);
                mQueue.setText(getString(R.string.tip_chat_top_consulting));
                mAccpect.setText(getString(R.string.back_chat));
                mAccpect.setOnClickListener(gotoMessageListener);
                mAccpect.setVisibility(View.VISIBLE);
            } else {
                mTop.setVisibility(View.GONE);
            }
        }
    }


    // 这个地方跳转一定要快
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < 10) {
            switch (position) {
                case 9:
                    MobclickAgent.onEvent(getActivity(), UmengEvent.MYMESSAGE10);
                    break;
                case 8:
                    MobclickAgent.onEvent(getActivity(), UmengEvent.MYMESSAGE9);
                    break;
                case 7:
                    MobclickAgent.onEvent(getActivity(), UmengEvent.MYMESSAGE8);
                    break;
                case 6:
                    MobclickAgent.onEvent(getActivity(), UmengEvent.MYMESSAGE7);
                    break;
                case 5:
                    MobclickAgent.onEvent(getActivity(), UmengEvent.MYMESSAGE6);
                    break;
                case 4:
                    MobclickAgent.onEvent(getActivity(), UmengEvent.MYMESSAGE5);
                    break;
                case 3:
                    MobclickAgent.onEvent(getActivity(), UmengEvent.MYMESSAGE4);
                    break;
                case 2:
                    MobclickAgent.onEvent(getActivity(), UmengEvent.MYMESSAGE3);
                    break;
                case 1:
                    MobclickAgent.onEvent(getActivity(), UmengEvent.MYMESSAGE2);
                    break;
                case 0:
                    MobclickAgent.onEvent(getActivity(), UmengEvent.MYMESSAGE1);
                    break;
            }
        }
        RecentInfo recentInfo = mContactAdapter.getItem(position - 1);
        if (recentInfo == null) {
            logger.e("recent#null recentInfo -> position:%d", position - 1);
            return;
        }
        if (recentInfo.getSessionType() == DBConstant.SESSION_TYPE_NOTIFICATION) {
            int bizType = NotificationConstants.BIZ_TYPE_TRANSACTION;
            if (recentInfo.getLatestMsgType() == DBConstant.MSG_TYPE_INTERACTION) {
                bizType = NotificationConstants.BIZ_TYPE_INTERACTION;
            }
            NavUtils.gotoNotificationListActivity(getActivity(), bizType);
        } else {
            IMUIHelper.openChatActivity(getActivity(), recentInfo.getSessionKey());
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        int what = msg.what;
        if (what == ValueConstants.ACCEPT_PROCESS_ORDER_OK) {
            AcceptProcessOrderResult result = (AcceptProcessOrderResult) msg.obj;
            if (result.success) {
                NavUtils.gotoMessageActivity(getActivity(), result.processOrder.buyerInfo.userId, null, result.processOrder.itemId);
            } else {
                if (!StringUtil.isEmpty(result.message)) {
                    ToastUtil.showToast(getActivity(), result.message);
                }
            }

        } else if (what == ValueConstants.ACCEPT_PROCESS_ORDER_KO) {
            ToastUtil.showToast(getActivity(), StringUtil.handlerErrorCode(getActivity(), msg.arg1));
        }
    }
}
