package com.mogujie.tt.imservice.manager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.mogujie.tt.imservice.callback.Packetlistener;
import com.mogujie.tt.imservice.event.SendCarInfoEvent;
import com.mogujie.tt.imservice.event.SendCarInfoResponseEvent;
import com.mogujie.tt.protobuf.IMBaseDefine;
import com.mogujie.tt.protobuf.IMOther;
import com.mogujie.tt.utils.Logger;
import com.yhy.common.utils.SPUtils;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import java.io.IOException;

import de.greenrobot.event.EventBus;

/**
 * Created with Android Studio.
 * Title:IMOtherManager
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/11/7
 * Time:14:30
 * Version 1.0
 */
public class IMOtherManager extends IMManager {
    Logger logger = Logger.getLogger(IMOtherManager.class);
    private static IMOtherManager imOtherManager = new IMOtherManager();

    @Autowired
    IUserService userService;
    public IMOtherManager() {
        YhyRouter.getInstance().inject(this);
    }

    /**
     * 登陆成功触发
     * auto自动登陆
     */
    public void onNormalLoginOk() {
        onLocalNetOk();
    }

    public void onLocalNetOk() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void doOnStart() {

    }

    public void reset() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    public static IMOtherManager instance() {
        return imOtherManager;
    }

    public void onEvent(SendCarInfoEvent event) {
        sendCarInfo(event);
    }

    /**
     * 发送位置信息im服务器
     */
    private void sendCarInfo(SendCarInfoEvent event) {
        if (!userService.isLogin()) {
            return;
        }
        ByteString byteString = event.getSendContentForSocket();
        if (byteString == null) return;
        int cid = IMBaseDefine.OtherCmdID.CID_OTHER_CAR_INFO_REQ_VALUE;
        int sid = IMBaseDefine.ServiceID.SID_OTHER_VALUE;
        IMOther.IMCarInfoReq req = IMOther.IMCarInfoReq.newBuilder().setUserId(userService.getLoginUserId()).setUserInfo(byteString).build();
        IMSocketManager.instance().sendRequest(req, sid, cid, new Packetlistener() {
            @Override
            public void onSuccess(Object response) {
                IMOther.IMCarInfoRep rep = null;
                try {
                    rep = IMOther.IMCarInfoRep.parseFrom((CodedInputStream) response);
                    IMOtherManager.instance().onRecCarInfo(rep);
                    EventBus.getDefault().post(SendCarInfoResponseEvent.OK);
                } catch (IOException e) {
                    EventBus.getDefault().post(SendCarInfoResponseEvent.IO_ERROR);
                }

            }

            @Override
            public void onFaild() {
                EventBus.getDefault().post(SendCarInfoResponseEvent.ERROR);
            }

            @Override
            public void onTimeout() {
                EventBus.getDefault().post(SendCarInfoResponseEvent.ERROR);
            }
        });
    }

    public void onRecCarInfo(IMOther.IMCarInfoRep rep) {
        logger.d("respones carinfo");
    }
}
