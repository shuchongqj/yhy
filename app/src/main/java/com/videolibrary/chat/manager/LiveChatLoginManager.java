package com.videolibrary.chat.manager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.google.protobuf.CodedInputStream;
import com.mogujie.tt.imservice.callback.Packetlistener;
import com.mogujie.tt.utils.Logger;
import com.videolibrary.chat.event.LiveChatLoginEvent;
import com.videolibrary.chat.protobuf.IMBaseDefine;
import com.videolibrary.chat.protobuf.IMLive;
import com.yhy.common.utils.SPUtils;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import java.io.IOException;

import de.greenrobot.event.EventBus;

/**
 * Created with Android Studio.
 * Title:LiveChatGroupManager
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/8/12
 * Time:15:52
 * Version 1.0
 */
public class LiveChatLoginManager extends LiveChatManager {
    private Logger logger = Logger.getLogger(LiveChatLoginManager.class);
    private static LiveChatLoginManager inst = new LiveChatLoginManager();
    LiveChatLoginEvent loginStatus;
    private long currentUserId;
    private long currentLiveId;
    private boolean currentUserType;
    private String currentUserName;
    private String currentUserPic;

    @Autowired
    IUserService userService;

    public LiveChatLoginManager() {
        YhyRouter.getInstance().inject(this);
    }

    public static LiveChatLoginManager instance() {
        return inst;
    }

    @Override
    public void doOnStart() {
        loginStatus = LiveChatLoginEvent.NONE;
//        EventBus.getDefault().register(this);

    }

    @Override
    public void doOnStop() {
        loginStatus = LiveChatLoginEvent.NONE;
//        EventBus.getDefault().unregister(this);
    }

    public void reLogin() {
        synchronized (LiveChatLoginManager.class) {
            LiveChatSocketManager.instance().disconnectMsgServer();
            LiveChatSocketManager.instance().reqLiveChatService(currentLiveId, currentUserType);
        }
    }

    public void login(long liveId) {
        currentLiveId = liveId;
        currentUserId = 0;
        currentUserType = true;
        if (userService.isLogin()) {
            currentUserId = userService.getLoginUserId();
            currentUserType = false;
            currentUserName = SPUtils.getNickName(getContext());
            currentUserPic = SPUtils.getUserIcon(getContext());
        }
        LiveChatSocketManager.instance().reqLiveChatService(liveId, currentUserType);
    }


    public void loginGroup() {
        triggerEvent(LiveChatLoginEvent.LOGINING);
        IMBaseDefine.IMUserInfo imUserInfo = IMBaseDefine.IMUserInfo.newBuilder().setUserId(currentUserId).setGroupId(currentLiveId).setUserName(currentUserName == null ? "" : currentUserName).setUserImage(currentUserPic == null ? "" : currentUserPic).build();
        final IMLive.IMUserInfoLoginReq req = IMLive.IMUserInfoLoginReq.newBuilder().setUserInfo(imUserInfo).setUserAction(IMBaseDefine.UserAction.USER_ACTION_LOGIN).build();
        int sid = IMBaseDefine.ServiceID.SID_USER_INFO_VALUE;
        int cid = IMBaseDefine.UserInfoCmdID.CID_USER_INFO_LOGIN_REQ_VALUE;

        LiveChatSocketManager.instance().sendRequest(req, sid, cid, new Packetlistener() {
            @Override
            public void onSuccess(Object response) {
                try {
                    IMLive.IMUserInfoLoginRes res = IMLive.IMUserInfoLoginRes.parseFrom((CodedInputStream) response);
                    IMBaseDefine.IMGroupResultInfo resultInfo = res.getResultInfo();
                    IMBaseDefine.ResultType resultType = resultInfo.getResultCode();
                    switch (resultType) {
                        case REFUSE_REASON_NONE:
                            triggerEvent(LiveChatLoginEvent.SUCESS);
                            break;
                        default:
                            triggerEvent(LiveChatLoginEvent.FAIL);
                    }
                } catch (IOException e) {

                }
            }

            @Override
            public void onFaild() {
                triggerEvent(LiveChatLoginEvent.FAIL);
            }

            @Override
            public void onTimeout() {
                triggerEvent(LiveChatLoginEvent.FAIL);
            }
        });
    }

    public void triggerEvent(LiveChatLoginEvent event) {
        setStatus(event);
        EventBus.getDefault().postSticky(event);
    }

    public void setStatus(LiveChatLoginEvent status) {
        this.loginStatus = status;
    }

    public LiveChatLoginEvent getStatus() {
        return loginStatus;
    }

    public void setCurrentUserId(long currentUserId) {
        this.currentUserId = currentUserId;
    }

    public long getCurrentUserId() {
        return currentUserId;
    }
}
