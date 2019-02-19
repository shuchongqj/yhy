package com.videolibrary.chat.manager;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.GeneratedMessageLite;
import com.loopj.android.http.AsyncHttpClient;
import com.mogujie.tt.imservice.callback.ListenerQueue;
import com.mogujie.tt.imservice.callback.Packetlistener;
import com.mogujie.tt.imservice.network.SocketThread;
import com.mogujie.tt.imservice.network.SslContextFactory;
import com.mogujie.tt.protobuf.base.DataBuffer;
import com.mogujie.tt.protobuf.base.DefaultHeader;
import com.mogujie.tt.utils.Logger;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.videolibrary.chat.callback.LiveChatSocketHandler;
import com.videolibrary.chat.event.LiveChatLoginEvent;
import com.videolibrary.chat.event.LiveChatSocketEvent;
import com.videolibrary.chat.protobuf.IMBaseDefine;
import com.yhy.common.beans.net.model.msg.LiveMsgServerAddrParam;
import com.yhy.common.beans.net.model.msg.LiveMsgServerAddrResult;
import com.yhy.common.constants.SysConstant;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;

import de.greenrobot.event.EventBus;

/**
 * @author : yingmu on 14-12-30.
 * @email : yingmu@mogujie.com.
 * <p/>
 * 业务层面:
 * 长连接建立成功之后，就要发送登陆信息，否则15s之内就会断开
 * 所以connMsg 与 login是强耦合的关系
 */
public class LiveChatSocketManager extends LiveChatManager {

    private Logger logger = Logger.getLogger(LiveChatSocketManager.class);
    private static LiveChatSocketManager inst = new LiveChatSocketManager();

    public static LiveChatSocketManager instance() {
        return inst;
    }

    public LiveChatSocketManager() {
        logger.d("LiveChatSocketManager create");
    }

    private ListenerQueue listenerQueue = new ListenerQueue();

    // 请求消息服务器地址
    private AsyncHttpClient client = new AsyncHttpClient();

    /**
     * 底层socket
     */
    private SocketThread msgServerThread;

    /**
     * 自身状态
     */
    private LiveChatSocketEvent socketStatus = LiveChatSocketEvent.NONE;

    /**
     * 获取Msg地址，等待链接
     */
    @Override
    public void doOnStart() {
        socketStatus = LiveChatSocketEvent.NONE;
        SslContextFactory.getInstance().init(getContext());
    }

    @Override
    public void doOnStop() {
        disconnectMsgServer();
        socketStatus = LiveChatSocketEvent.NONE;
//        currentMsgAddress = null;
    }


    /**
     * 实现自身的事件驱动
     *
     * @param event
     */
    public void triggerEvent(LiveChatSocketEvent event) {
        setSocketStatus(event);
        EventBus.getDefault().postSticky(event);
    }

    /**
     * -------------------------------功能方法--------------------------------------
     */

    public void sendRequest(GeneratedMessageLite requset, int sid, int cid) {
        sendRequest(requset, sid, cid, null);
    }

    public void sendRequest(GeneratedMessageLite requset, int sid, int cid, Packetlistener packetlistener) {
        int seqNo = 0;
        try {
            //组装包头 header
            com.mogujie.tt.protobuf.base.Header header = new DefaultHeader(sid, cid);
            int bodySize = requset.getSerializedSize();
            header.setLength(SysConstant.PROTOCOL_HEADER_LENGTH + bodySize);
            seqNo = header.getSeqnum();
            listenerQueue.push(seqNo, packetlistener);
            boolean sendRes = msgServerThread.sendRequest(requset, header);
        } catch (Exception e) {
            if (packetlistener != null) {
                packetlistener.onFaild();
            }
            listenerQueue.pop(seqNo);
        }
    }

    public void sendRequest(GeneratedMessage requset, int sid, int cid, Packetlistener packetlistener) {
        int seqNo = 0;
        try {
            //组装包头 header
            com.mogujie.tt.protobuf.base.Header header = new DefaultHeader(sid, cid);
            int bodySize = requset.getSerializedSize();
            header.setLength(SysConstant.PROTOCOL_HEADER_LENGTH + bodySize);
            seqNo = header.getSeqnum();
            listenerQueue.push(seqNo, packetlistener);
            boolean sendRes = msgServerThread.sendRequest(requset, header);
        } catch (Exception e) {
            if (packetlistener != null) {
                packetlistener.onFaild();
            }
            listenerQueue.pop(seqNo);
        }
    }

    public void packetDispatch(ChannelBuffer channelBuffer) {
        DataBuffer buffer = new DataBuffer(channelBuffer);
        com.mogujie.tt.protobuf.base.Header header = new com.mogujie.tt.protobuf.base.Header();
        header.decode(buffer);
        /**buffer 的指针位于body的地方*/
        int commandId = header.getCommandId();
        int serviceId = header.getServiceId();
        int seqNo = header.getSeqnum();
        logger.d("dispatch packet, serviceId:%d, commandId:%d", serviceId,
                commandId);
        CodedInputStream codedInputStream = CodedInputStream.newInstance(new ChannelBufferInputStream(buffer.getOrignalBuffer()));

        Packetlistener listener = listenerQueue.pop(seqNo);
        if (listener != null) {
            listener.onSuccess(codedInputStream);
            return;
        }
        // todo eric make it a table
        // 抽象 父类执行
        switch (serviceId) {
            case IMBaseDefine.ServiceID.SID_GROUP_MSG_VALUE:
                LiveChatMessageManager.instance().packetDisPatch(commandId, codedInputStream);
                break;
            case IMBaseDefine.ServiceID.SID_USER_NOTIFY_VALUE:
                LiveChatMessageManager.instance().packetDisPatchNotify(commandId, codedInputStream);

            default:
                logger.e("packet#unhandled serviceId:%d, commandId:%d", serviceId,
                        commandId);
                break;
        }
    }

//    public void reqImMsgServerAddrs() {
//        boolean sucess = true;
//        if (sucess) {
//            connectMsgServer(new Api_USER_ImLoginResp());
//        } else {
//            triggerEvent(LiveChatSocketEvent.FAIL);
//        }
//    }

    private void connectMsgServer(LiveMsgServerAddrResult currentMsgAddress) {
        String priorIP = currentMsgAddress.priorIp;
        int port = currentMsgAddress.port;
        logger.i("LiveChatSocket connectMsgServer -> (%s:%d)", priorIP, port);

        //check again,may be unimportance
        if (msgServerThread != null) {
            msgServerThread.close();
            msgServerThread = null;
        }

        msgServerThread = new SocketThread(priorIP, port, new LiveChatSocketHandler());
        msgServerThread.start();
    }

    /**
     * 断开与msg的链接
     */
    public void disconnectMsgServer() {
        listenerQueue.onDestory();
        logger.i("login#disconnectMsgServer");
        if (msgServerThread != null) {
            msgServerThread.close();
            msgServerThread = null;
            logger.i("login#do real disconnectMsgServer ok");
        }
        LiveChatLoginManager.instance().setStatus(LiveChatLoginEvent.NONE);
        triggerEvent(LiveChatSocketEvent.FAIL);
    }

    /**
     * 之前没有连接成功
     */
    public void onConnectMsgServerFail() {
        disconnectMsgServer();
    }

    public void onMsgServerConnected() {
        logger.i("login#onMsgServerConnected");
        listenerQueue.onStart();
        triggerEvent(LiveChatSocketEvent.SUCESS);
        LiveChatLoginManager.instance().loginGroup();
    }

    /**
     * 判断链接是否处于断开状态
     */
    public boolean isSocketConnect() {
        return !(msgServerThread == null || msgServerThread.isClose());
    }


    public void onMsgServerDisconn() {
        logger.w("login#onMsgServerDisconn");
        disconnectMsgServer();
    }


    /**
     * ------------get/set----------------------------
     */
    public LiveChatSocketEvent getSocketStatus() {
        return socketStatus;
    }

    public void setSocketStatus(LiveChatSocketEvent socketStatus) {
        this.socketStatus = socketStatus;
    }

    public void reqLiveChatService(long liveId, boolean isTourist) {
        triggerEvent(LiveChatSocketEvent.CONNECTING);
        LiveMsgServerAddrParam param = new LiveMsgServerAddrParam();
        param.liveId = liveId;
        if (!isTourist) {
            NetManager.getInstance(getContext()).doGetMsgServerAddressByUser(param, lsn);
        } else {
            NetManager.getInstance(getContext()).doGetMsgServerAddressByVisitor(param, lsn);
        }
    }

    OnResponseListener<LiveMsgServerAddrResult> lsn = new OnResponseListener<LiveMsgServerAddrResult>() {
        @Override
        public void onComplete(boolean isOK, LiveMsgServerAddrResult result, int errorCode, String errorMsg) {
            if (isOK && result != null) {
                LiveChatLoginManager.instance().setCurrentUserId(result.userId);
                connectMsgServer(result);
                return;
            }
            triggerEvent(LiveChatSocketEvent.FAIL);
        }

        @Override
        public void onInternError(int errorCode, String errorMessage) {
            triggerEvent(LiveChatSocketEvent.FAIL);
        }
    };
}
