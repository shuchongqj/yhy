package com.mogujie.tt.imservice.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.harwkin.nb.camera.FileUtil;
import com.mogujie.tt.DB.DBInterface;
import com.mogujie.tt.DB.sp.ConfigurationSp;
import com.mogujie.tt.imservice.event.LoginEvent;
import com.mogujie.tt.imservice.event.PriorityEvent;
import com.mogujie.tt.imservice.event.UserInfoEvent;
import com.mogujie.tt.imservice.manager.IMConsultManager;
import com.mogujie.tt.imservice.manager.IMContactManager;
import com.mogujie.tt.imservice.manager.IMHeartBeatManager;
import com.mogujie.tt.imservice.manager.IMLoginManager;
import com.mogujie.tt.imservice.manager.IMMessageManager;
import com.mogujie.tt.imservice.manager.IMNotificationManager;
import com.mogujie.tt.imservice.manager.IMOtherManager;
import com.mogujie.tt.imservice.manager.IMReconnectManager;
import com.mogujie.tt.imservice.manager.IMSessionManager;
import com.mogujie.tt.imservice.manager.IMSocketManager;
import com.mogujie.tt.imservice.manager.IMSwitchServiceManager;
import com.mogujie.tt.imservice.manager.IMUnreadMsgManager;
import com.mogujie.tt.protobuf.helper.EntityChangeEngine;
import com.mogujie.tt.utils.Logger;
import com.quanyan.base.util.HarwkinLogUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.common.DirConstants;
import com.quanyan.yhy.database.DBManager;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.yhy.common.beans.im.NotificationMessageEntity;
import com.yhy.common.beans.im.entity.MessageEntity;
import com.yhy.common.beans.net.model.common.CrashInfo;
import com.yhy.common.beans.net.model.common.CrashInfoList;
import com.yhy.common.constants.DBConstant;
import com.yhy.common.constants.SysConstant;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.eventbus.event.NotificationEvent;
import com.yhy.common.utils.JSONUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * IMService 负责所有IMManager的初始化与reset
 * 并且Manager的状态的改变 也会影响到IMService的操作
 * 备注: 有些服务应该在LOGIN_OK 之后进行
 * todo IMManager reflect or just like  ctx.getSystemService()
 */
public class IMService extends Service {
    private Logger logger = Logger.getLogger(IMService.class);

    /**
     * binder
     */
    private IMServiceBinder binder = new IMServiceBinder();

    public IMConsultManager getConsultManager() {
        return consultManager;
    }

    public IMSwitchServiceManager getSwitchServiceManager() {
        return switchServiceManager;
    }

    public class IMServiceBinder extends Binder {
        public IMService getService() {
            return IMService.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        logger.i("IMService onBind");
        return binder;
    }

    //所有的管理类
    private IMSocketManager socketMgr = IMSocketManager.instance();
    private IMLoginManager loginMgr = IMLoginManager.instance();
    private IMContactManager contactMgr = IMContactManager.instance();
    private IMMessageManager messageMgr = IMMessageManager.instance();
    private IMSessionManager sessionMgr = IMSessionManager.instance();
    private IMReconnectManager reconnectMgr = IMReconnectManager.instance();
    private IMUnreadMsgManager unReadMsgMgr = IMUnreadMsgManager.instance();
    private IMNotificationManager notificationMgr = IMNotificationManager.instance();
    private IMHeartBeatManager heartBeatManager = IMHeartBeatManager.instance();
    private IMConsultManager consultManager = IMConsultManager.instance();
    private IMSwitchServiceManager switchServiceManager = IMSwitchServiceManager.instance();
    private IMOtherManager otherManager = IMOtherManager.instance();
    private ConfigurationSp configSp;
    private DBInterface dbInterface = DBInterface.instance();

    private static NotificationEvent lastNotificationEvent;

    @Override
    public void onCreate() {
        logger.i("IMService onCreate");
        super.onCreate();
        EventBus.getDefault().register(this, SysConstant.SERVICE_EVENTBUS_PRIORITY);
        // make the service foreground, so stop "360 yi jian qingli"(a clean
        // tool) to stop our app
        // todo eric study wechat's mechanism, use a better solution
//        startForeground((int) System.currentTimeMillis(), new Notification());

        Context ctx = getApplicationContext();
        // 放在这里还有些问题 todo
        socketMgr.onStartIMManager(ctx);
        contactMgr.onStartIMManager(ctx);
        messageMgr.onStartIMManager(ctx);
        sessionMgr.onStartIMManager(ctx);
        unReadMsgMgr.onStartIMManager(ctx);
        notificationMgr.onStartIMManager(ctx);
        reconnectMgr.onStartIMManager(ctx);
        heartBeatManager.onStartIMManager(ctx);
        consultManager.onStartIMManager(ctx);
        loginMgr.onStartIMManager(ctx);

        otherManager.onStartIMManager(ctx);
//        ImageLoaderUtil.initImageLoaderConfig(ctx);
        //发送错误报告
        sendBugReport();
    }

    private void sendBugReport() {
        final String readFile = FileUtil.readContent(new File(DirConstants.DIR_LOGS, ValueConstants.YM_CRASH_BUG_REPORT));
        HarwkinLogUtil.info("bugReport text= " + readFile);
        if(StringUtil.isEmpty(readFile)){
            return;
        }

        CrashInfoList crashInfoList = new CrashInfoList();
        List<CrashInfo> crashInfos = new ArrayList<>();
        String[] datas = readFile.split(";");
        for(String data : datas){
            if(!StringUtil.isEmpty(data)){
                try {
                    CrashInfo crashInfo = JSONUtils.convertToObject(data, CrashInfo.class);
                    crashInfos.add(crashInfo);
                }catch (Exception e){
                    HarwkinLogUtil.error("crashInfo parseJson error");
                }

            }

        }
        crashInfoList.crashList = crashInfos;
        if(crashInfos != null && crashInfos.size() > 0){
            NetManager.getInstance(this).doAddCrash(crashInfoList, new OnResponseListener<Boolean>() {
                @Override
                public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                    if(result){
                        FileUtil.deleteFile(new File(DirConstants.DIR_LOGS, ValueConstants.YM_CRASH_BUG_REPORT));
                    }
                    if(readFile.length() > 1024 * 4){
                        FileUtil.deleteFile(new File(DirConstants.DIR_LOGS, ValueConstants.YM_CRASH_BUG_REPORT));
                    }
                }

                @Override
                public void onInternError(int errorCode, String errorMessage) {
                    if(readFile.length() > 1024 * 4){
                        FileUtil.deleteFile(new File(DirConstants.DIR_LOGS, ValueConstants.YM_CRASH_BUG_REPORT));
                    }
                }
            });
        }

    }

    @Override
    public void onDestroy() {
        logger.i("IMService onDestroy");
        // todo 在onCreate中使用startForeground
        // 在这个地方是否执行 stopForeground呐
        EventBus.getDefault().unregister(this);
        handleLoginout();
        // DB的资源的释放
        dbInterface.close();
        super.onDestroy();
    }

    /**
     * 收到消息需要上层的activity判断 {MessageActicity onEvent(PriorityEvent event)}，这个地方是特殊分支
     */
    public void onEvent(PriorityEvent event) {
        switch (event.event) {
            case MSG_RECEIVED_MESSAGE: {
                MessageEntity entity = (MessageEntity) event.object;
                /**非当前的会话*/
                logger.d("messageactivity#not this session msg -> id:%s", entity.getFromId());
                messageMgr.ackReceiveMsg(entity);
                unReadMsgMgr.add(entity);
            }
            break;
        }
    }

    public void onEvent(NotificationEvent event) {
        switch (event.event) {
            case RECEIVE:
                if (loginMgr.isEverLogined()) {
                    if (lastNotificationEvent != null){

                        NotificationMessageEntity notificationMessageEntity = lastNotificationEvent.entity;
                        if (notificationMessageEntity == null){
                            return;
                        }
                        if (notificationMessageEntity.getMessageId() == event.entity.getMessageId()){
                            return;
                        }

                    }
                    lastNotificationEvent = event;
                    if (event.overrideUnRead){
                        event.entity.setStatus(DBConstant.READED);
                    }else {
                        event.entity.setStatus(DBConstant.UNREAD);
                    }
//                    event.entity.setStatus(DBConstants.UNREAD);
                    DBManager.getInstance(this).saveOrUpdate(event.entity);
                    sessionMgr.updateSession(event.entity);
                    unReadMsgMgr.add(event.entity);
                    EventBus.getDefault().cancelEventDelivery(event);
                }
                break;
            case UNREAD_CLEAR:
                unReadMsgMgr.readUnreadSession(EntityChangeEngine.getSessionKey(event.bizType, DBConstant.SESSION_TYPE_NOTIFICATION,0), false);
                break;
        }
    }

    // EventBus 事件驱动
    public void onEvent(LoginEvent event) {
        switch (event) {
            case LOGIN_OK:
                onNormalLoginOk();
                break;
            case LOCAL_LOGIN_SUCCESS:
                onLocalLoginOk();
                break;
            case LOCAL_LOGIN_MSG_SERVICE:
                onLocalNetOk();
                break;
            case LOGIN_OUT:
                handleLoginout();
                break;
        }
    }

    public void onEvent(UserInfoEvent event) {
        switch (event) {
            case USER_LOGIN_INFO_UPDATE:
                contactMgr.onLocalLoginOk();
                break;
        }
    }

    // 负责初始化 每个manager
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        logger.i("IMService onStartCommand");
        //应用开启初始化 下面这几个怎么释放 todo
        return START_STICKY;
    }


    /**
     * 用户输入登陆流程
     * userName/pwd -> reqMessage ->connect -> loginMessage ->loginSuccess
     */
    private void onNormalLoginOk() {
        logger.d("imservice#onLogin Successful");
        //初始化其他manager todo 这个地方注意上下文的清除
        Context ctx = getApplicationContext();
        long loginId = loginMgr.getLoginId();
        configSp = ConfigurationSp.instance(ctx, loginId);
        dbInterface.initDbHelp(ctx, loginId);

        contactMgr.onNormalLoginOk();
        sessionMgr.onNormalLoginOk();
        unReadMsgMgr.onNormalLoginOk();
        reconnectMgr.onNormalLoginOk();
        consultManager.onNormalLoginOk();
        otherManager.onNormalLoginOk();

        //依赖的状态比较特殊
        messageMgr.onLoginSuccess();
        notificationMgr.onLoginSuccess();
        heartBeatManager.onloginNetSuccess();
        // 这个时候loginManage中的localLogin 被置为true
    }


    /**
     * 自动登陆/离线登陆成功
     * autoLogin -> DB(loginInfo,loginId...) -> loginSucsess
     */
    private void onLocalLoginOk() {
        Context ctx = getApplicationContext();
        long loginId = loginMgr.getLoginId();
        configSp = ConfigurationSp.instance(ctx, loginId);
        dbInterface.initDbHelp(ctx, loginId);

        contactMgr.onLocalLoginOk();
        sessionMgr.onLocalLoginOk();
        reconnectMgr.onLocalLoginOk();
        notificationMgr.onLoginSuccess();
        messageMgr.onLoginSuccess();
    }

    /**
     * 1.从本机加载成功之后，请求MessageService建立链接成功(loginMessageSuccess)
     * 2. 重练成功之后
     */
    private void onLocalNetOk() {
        /**为了防止逗比直接把loginId与userName的对应直接改了,重刷一遍*/
        Context ctx = getApplicationContext();
        long loginId = loginMgr.getLoginId();
        configSp = ConfigurationSp.instance(ctx, loginId);
        dbInterface.initDbHelp(ctx, loginId);

//        contactMgr.onLocalNetOk();
        consultManager.onLocalNetOk();
        sessionMgr.onLocalNetOk();
        unReadMsgMgr.onLocalNetOk();
        reconnectMgr.onLocalNetOk();
        heartBeatManager.onloginNetSuccess();
        otherManager.onLocalNetOk();
    }

    private void handleLoginout() {
        logger.d("imservice#handleLoginout");

        // login需要监听socket的变化,在这个地方不能释放，设计上的不合理?
        reconnectMgr.reset();
        socketMgr.reset();
        loginMgr.reset();
        contactMgr.reset();
        messageMgr.reset();
        sessionMgr.reset();
        unReadMsgMgr.reset();
        notificationMgr.reset();
        heartBeatManager.reset();
        otherManager.reset();
        configSp = null;
        EventBus.getDefault().removeAllStickyEvents();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        logger.d("imservice#onTaskRemoved");
        // super.onTaskRemoved(rootIntent);
        this.stopSelf();
    }

    /**
     * -----------------get/set 的实体定义---------------------
     */
    public IMLoginManager getLoginManager() {
        return loginMgr;
    }

    public IMContactManager getContactManager() {
        return contactMgr;
    }

    public IMMessageManager getMessageManager() {
        return messageMgr;
    }

    public IMSessionManager getSessionManager() {
        return sessionMgr;
    }

    public IMReconnectManager getReconnectManager() {
        return reconnectMgr;
    }


    public IMUnreadMsgManager getUnReadMsgManager() {
        return unReadMsgMgr;
    }

    public IMNotificationManager getNotificationManager() {
        return notificationMgr;
    }

    public DBInterface getDbInterface() {
        return dbInterface;
    }

    public ConfigurationSp getConfigSp() {
        return configSp;
    }

}
