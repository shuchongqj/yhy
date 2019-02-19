package com.yhy.push;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.icu.lang.UScript;
import android.text.TextUtils;
import android.util.Log;

import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.push.handler.GetTokenHandler;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.yhy.common.base.ModuleApplication;
import com.yhy.common.base.YHYBaseApplication;
import com.yhy.common.eventbus.event.EvBusPushRegister;
import com.yhy.common.eventbus.event.NotificationEvent;
import com.yhy.common.utils.SPUtils;
import com.yhy.network.api.user.UserApi;
import com.yhy.network.req.msgcenter.SaveMsgRelevanceReq;
import com.yhy.router.YhyRouter;

import de.greenrobot.event.EventBus;

public class PushModuleApplication implements ModuleApplication{

    public static String XG_MI_APP_ID = "";
    public static String XG_MI_APP_KEY = "";
    public static String XG_MZ_APP_ID = "";
    public static String XG_MZ_APP_KEY = "";

    @Override
    public void onCreate(YHYBaseApplication application) {

        try {
            ApplicationInfo appInfo = application.getPackageManager().getApplicationInfo(application.getPackageName(), PackageManager.GET_META_DATA);
            XG_MI_APP_ID = appInfo.metaData.get("XG_MI_APP_ID").toString().replace("\0", "");
            XG_MI_APP_KEY = appInfo.metaData.get("XG_MI_APP_KEY").toString().replace("\0", "");
            XG_MZ_APP_ID = appInfo.metaData.get("XG_MZ_APP_ID").toString().replace("\0", "");
            XG_MZ_APP_KEY = appInfo.metaData.get("XG_MZ_APP_KEY").toString().replace("\0", "");
        }catch (Exception e){
            e.printStackTrace();
        }
        if(Rom.isEmui()){
            HMSAgent.init(application);
            HMSAgent.Push.getToken(rtnCode -> {
                Log.d("TPush", "华为推送注册结果：" + rtnCode);

            });
            try {
                XGPushManager.unregisterPush(application);
            }catch (Exception e){
                e.printStackTrace();
            }

        }else {
            XGPushConfig.setMiPushAppId(application, XG_MI_APP_ID);
            XGPushConfig.setMiPushAppKey(application, XG_MI_APP_KEY);

            XGPushConfig.setMzPushAppId(application, XG_MZ_APP_ID);
            XGPushConfig.setMzPushAppKey(application, XG_MZ_APP_KEY);

            XGPushManager.registerPush(application, new XGIOperateCallback() {
                @Override
                public void onSuccess(Object data, int flag) {
                    //token在设备卸载重装的时候有可能会变
                    //TODO 保存token解耦
                    registerToken(data.toString());
                }
                @Override
                public void onFail(Object data, int errCode, String msg) {
                    Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
                }
            });
        }
    }

    public static void registerToken(String token){
        SPUtils.setRegisterApnsToken(YHYBaseApplication.getInstance(), token);
        Log.d("TPush", "注册成功，设备token为：" + token);
        UserApi userApi = new UserApi();
        if (userApi.isLogin() && !TextUtils.isEmpty(token)){
            userApi.bindUserAndPush(new SaveMsgRelevanceReq(token, userApi.getPhone(), Rom.isEmui() ? 1 : 0), null).execAsync();
        }
    }
}
