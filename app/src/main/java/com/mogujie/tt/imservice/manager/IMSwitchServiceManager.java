package com.mogujie.tt.imservice.manager;

import android.text.TextUtils;

import com.mogujie.tt.imservice.callback.Packetlistener;
import com.mogujie.tt.imservice.event.SwitchP2PEvent;
import com.mogujie.tt.protobuf.IMBaseDefine;
import com.mogujie.tt.protobuf.IMSwitchService;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created with Android Studio.
 * Title:IMSwitchServiceManager
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/9/21
 * Time:10:56
 * Version 1.0
 */
public class IMSwitchServiceManager extends IMManager {
    int KEY_P2PCMD_WRITING = 2;
    int KEY_P2PCMD_WRITING_NOTIFY = KEY_P2PCMD_WRITING << 16 | 1;
    int KEY_P2PCMD_STOP_WRITING_NOTIFY = KEY_P2PCMD_WRITING << 16 | 2;
    private static final String WRITING = "writing";
    private static final String STOP_WRITING = "stop writing";
    private static IMSwitchServiceManager inst = new IMSwitchServiceManager();

    public static IMSwitchServiceManager instance() {
        return inst;
    }

    @Override
    public void doOnStart() {

    }

    @Override
    public void reset() {

    }

    public void onRecP2PMsg(IMSwitchService.IMP2PCmdMsg msg) {
        long fromId = msg.getFromUserId();
        long toId = msg.getToUserId();
        try {
            JSONObject jsonObject = new JSONObject(msg.getCmdMsgData());
            int serviceId = jsonObject.optInt("service_id");
            long itemId = jsonObject.optLong("item_id");
            int sessionType = jsonObject.optInt("session_type");
            if (serviceId == KEY_P2PCMD_WRITING) {
                int cmd = jsonObject.optInt("cmd_id");

                if (cmd == KEY_P2PCMD_WRITING_NOTIFY) {
                    EventBus.getDefault().post(new SwitchP2PEvent(fromId, toId, sessionType, itemId, SwitchP2PEvent.Event.WRITING));
                } else if (cmd == KEY_P2PCMD_STOP_WRITING_NOTIFY) {
                    EventBus.getDefault().post(new SwitchP2PEvent(fromId, toId, sessionType, itemId, SwitchP2PEvent.Event.STOP_WRITING));
                }
            }
        } catch (JSONException e) {
        }

    }

    public void sendP2PWritingMsg(long fromId, long toId, boolean isWriting, int sessionType, long itemId) {
        int jsonCID = isWriting ? KEY_P2PCMD_WRITING_NOTIFY : KEY_P2PCMD_STOP_WRITING_NOTIFY;
        String jsonContent = isWriting ? WRITING : STOP_WRITING;
        String msgData = buildMsgData(KEY_P2PCMD_WRITING, jsonCID, jsonContent, sessionType, itemId);
        if (TextUtils.isEmpty(msgData)) {
            return;
        }
        IMSwitchService.IMP2PCmdMsg msg = IMSwitchService.IMP2PCmdMsg.newBuilder().setFromUserId(fromId).setToUserId(toId).setCmdMsgData(msgData).build();
        int cid = IMBaseDefine.SwitchServiceCmdID.CID_SWITCH_P2P_CMD_VALUE;
        int sid = IMBaseDefine.ServiceID.SID_SWITCH_SERVICE_VALUE;
        IMSocketManager.instance().sendRequest(msg, sid, cid, new Packetlistener() {
            @Override
            public void onSuccess(Object response) {

            }

            @Override
            public void onFaild() {

            }

            @Override
            public void onTimeout() {

            }
        });
    }

    private String buildMsgData(int service_id, int cmd_id, String content, int sessionType, long itemId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("service_id", service_id);
            jsonObject.put("cmd_id", cmd_id);
            jsonObject.put("content", content);
            jsonObject.put("session_type", sessionType);
            jsonObject.put("item_id", itemId);
            return jsonObject.toString();
        } catch (JSONException e) {
            return null;
        }
    }
}
