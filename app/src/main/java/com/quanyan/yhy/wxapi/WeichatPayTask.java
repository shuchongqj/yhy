package com.quanyan.yhy.wxapi;

import android.app.Activity;

import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yhy.common.beans.net.model.paycore.WxPayInfo;
import com.yhy.common.constants.ValueConstants;


/**
 * Created by tianhaibo on 2016/4/18.
 */

public class WeichatPayTask {
    private IWXAPI api;
    private Activity mContext;

    public WeichatPayTask(Activity mContext) {
        this.mContext = mContext;
        api = WXAPIFactory.createWXAPI(mContext, null);
        api.registerApp(ValueConstants.WX_APPID);
    }

    private boolean isWXAppInstalledAndSupported() {
        boolean sIsWXAppInstalledAndSupported = api.isWXAppInstalled()
                && api.isWXAppSupportAPI();
        return sIsWXAppInstalledAndSupported;
    }

    public void payByWeiChat(WxPayInfo mTmWxPayInfo) {
        if (!isWXAppInstalledAndSupported()) {
            ToastUtil.showToast(mContext, "未安装微信");
            return;
        }
        try {
            if (mTmWxPayInfo != null) {
                PayReq req = new PayReq();
                req.appId = mTmWxPayInfo.appid;
                req.partnerId = mTmWxPayInfo.partnerid;
                req.prepayId = mTmWxPayInfo.prepayid;
                req.packageValue = mTmWxPayInfo.packageStr;
                req.nonceStr = mTmWxPayInfo.noncestr;
                req.timeStamp = mTmWxPayInfo.timestamp;
                req.sign = mTmWxPayInfo.sign;
                req.extData = "app data"; //optional
                api.sendReq(req);
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    public void destroyWx() {
        api.unregisterApp();
    }
}
