package com.mogujie.tt.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;

import com.mogujie.tt.imservice.service.IMService;
import com.mogujie.tt.imservice.support.IMServiceConnector;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshListView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.database.DBManager;
import com.quanyan.yhy.service.controller.NotiMessageController;
import com.quanyan.yhy.ui.adapter.NotificationMessageAdapter;
import com.quanyan.yhy.view.NetWorkErrorView;
import com.yhy.common.beans.im.NotificationMessageEntity;
import com.yhy.common.beans.net.model.notification.NotificationInteractiveMessage;
import com.yhy.common.beans.net.model.notification.NotificationOrderMessage;
import com.yhy.common.constants.DBConstant;
import com.yhy.common.constants.IntentConstants;
import com.yhy.common.constants.NotificationConstants;
import com.yhy.common.constants.SysConstant;
import com.yhy.common.eventbus.event.NotificationEvent;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 个人中心-消息中心 on 2015/11/2.
 */
public class NotificationListActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2 {

    PullToRefreshListView mListView;
    NotificationMessageAdapter mAdapter;
    NotiMessageController mController;
    private int mBizType;
    private IMService mImService;
    private IMServiceConnector mConnector = new IMServiceConnector() {
        @Override
        public void onIMServiceConnected() {
            if (!EventBus.getDefault().isRegistered(NotificationListActivity.this)) {
                EventBus.getDefault().register(NotificationListActivity.this, SysConstant.MESSAGE_EVENTBUS_PRIORITY);
            }
            mImService = mConnector.getIMService();
            if (mImService == null) {
                return;
            }
            initData();
//            clearNotificationBar();
        }

        @Override
        public void onServiceDisconnected() {
            if (EventBus.getDefault().isRegistered(NotificationListActivity.this)) {
                EventBus.getDefault().unregister(NotificationListActivity.this);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mConnector.disconnect(this);
    }

//    private void clearNotificationBar() {
//        if (mImService != null && mBizType == NotificationConstants.BIZ_TYPE_TRANSACTION) {
//            mImService.getNotificationManager().cancelSessionNotifications(String.valueOf(DBConstant.SESSION_TYPE_NOTIFICATION));
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
//        clearNotificationBar();
    }

    public static void gotoNotificationListActivity(Context context, int bizType) {
        Intent intent = new Intent(context, NotificationListActivity.class);
        intent.putExtra(IntentConstants.EXTRA_BIZ_TYPE, bizType);
        context.startActivity(intent);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.base_pull_refresh_layout_listview, null);
    }

    private BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mBizType = getIntent().getIntExtra(IntentConstants.EXTRA_BIZ_TYPE, NotificationConstants.BIZ_TYPE_TRANSACTION);
        initView();
        mConnector.connect(this);
    }

    private void initData() {
        mController = new NotiMessageController(this, mHandler);
        mController.loadMessageFromDb(NotificationListActivity.this, mBizType, mAdapter.getMixId());
        DBManager.getInstance(this).clearNotiMsgUnRead(mBizType);
        mAdapter.setImService(mImService);
    }

    private void initView() {
        mBaseNavView.setTitleText(mBizType == NotificationConstants.BIZ_TYPE_TRANSACTION ? R.string.label_notification : R.string.label_interaction);
        mListView = findViewById(R.id.pull_to_refresh_listview);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mAdapter = new NotificationMessageAdapter(this, null);
        mListView.setAdapter(mAdapter);
        mListView.setOnRefreshListener(this);
        NetWorkErrorView errorView = new NetWorkErrorView(this);
        errorView.show(R.mipmap.error_empty_icon, getString(R.string.message_empty_list_message), null, null, null);
        mListView.setEmptyView(errorView);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        mController.loadMessageFromDb(NotificationListActivity.this, mBizType, mAdapter.getMixId());
    }

    public void onEvent(NotificationEvent event) {
        switch (event.event) {
            case RECEIVE:
                if (mBizType == event.entity.getPeerId()) {
                    NotificationMessageEntity entity = event.entity;
                    DBManager.getInstance(this).saveOrUpdate(entity);
                    mImService.getSessionManager().updateSession(event.entity);
                    if (event.entity.isNewVersion()) {
                        //如果是新版本 直接使用entity
                    } else if (event.entity.getPeerId() == NotificationConstants.BIZ_TYPE_TRANSACTION) {
                        entity = NotificationOrderMessage.parseFromDB(event.entity);
                    } else if (event.entity.getPeerId() == NotificationConstants.BIZ_TYPE_INTERACTION) {
                        entity = NotificationInteractiveMessage.parseFromDB(event.entity);
                    }
                    final NotificationMessageEntity finalEntity = entity;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.add(finalEntity);
                        }
                    });
                    EventBus.getDefault().cancelEventDelivery(event);
                }
                break;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        int what = msg.what;
        mListView.onRefreshComplete();
        if (what == NotiMessageController.LOAD_MESSAGE_SUCCESS) {
            mAdapter.addAll((List<NotificationMessageEntity>) msg.obj);
            if (((List<NotificationMessageEntity>) msg.obj).size() < DBConstant.LIMIT) {
                mListView.setMode(PullToRefreshBase.Mode.DISABLED);
            }
        } else if (what == NotiMessageController.LOAD_MESSAGE_FAIL) {
            mListView.setMode(PullToRefreshBase.Mode.DISABLED);
        }
    }
}
