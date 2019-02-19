package com.quanyan.yhy.pay;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alipay.sdk.app.PayTask;
import com.harwkin.nb.camera.TimeUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.MD5Utils;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.PayStatus;
import com.quanyan.yhy.common.WeiXinPayResult;
import com.quanyan.yhy.eventbus.GuangDaPaySuccessEvBus;
import com.quanyan.yhy.pay.alipay.PayResult;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.spcart.controller.SPCartController;
import com.quanyan.yhy.ui.wallet.WalletUtils;
import com.quanyan.yhy.ui.wallet.view.WalletDialog;
import com.quanyan.yhy.ui.wallet.view.gridpasswordview.GridPasswordView;
import com.quanyan.yhy.wxapi.WeichatPayTask;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yhy.common.beans.net.model.param.WebParams;
import com.yhy.common.beans.net.model.paycore.AliBatchPayParam;
import com.yhy.common.beans.net.model.paycore.AliPayInfo;
import com.yhy.common.beans.net.model.paycore.CebCloudBatchPayParam;
import com.yhy.common.beans.net.model.paycore.CebCloudPayInfo;
import com.yhy.common.beans.net.model.paycore.EleAccountInfo;
import com.yhy.common.beans.net.model.paycore.ElePurseBatchPayParam;
import com.yhy.common.beans.net.model.paycore.PayCoreBaseResult;
import com.yhy.common.beans.net.model.paycore.PcPayStatusInfo;
import com.yhy.common.beans.net.model.paycore.WxPayInfo;
import com.yhy.common.beans.net.model.tm.TmPayStatusInfo;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.types.AppDebug;
import com.yhy.common.utils.SPUtils;
import com.yhy.service.IUserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015-11-10.
 * 支付界面
 */
public class PayActivity extends BaseActivity {

//    // 商户PID
//    public static String PARTNER = "";
//    // 商户收款账号
//    public static String SELLER = "";
//    // 商户私钥，pkcs8格式
//    public static String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_WEIXIN_PAY_FLAG = 2;
    private ListView payway_listview;
    private OrderPayWayAdapter mAdapter;
    private List<PayModle> payList;
    private LinearLayout topLayout;
    private long[] bizOrderId;
    private PayController payController;
    @ViewInject(R.id.order_config_btn)
    private Button order_config_btn;
    @ViewInject(R.id.payprice_tv)
    private TextView payprice_tv;
    //    private TmPayInfo mPayInfo = null;
    private String stringPrice;
    private String payStatus = PayStatus.PAYERROR;
    @ViewInject(R.id.order_bottom_config_layout)
    private RelativeLayout order_bottom_config_layout;

    private int clickPostion = 0;
    private IWXAPI msgApi;
    private PayReq wxPayReq;
    private int serverCount = 0;
    private static final int MAX_COUNT = 3;
    private static final int SEND_TIME = 1000;
    WeichatPayTask mWeichatPayTask;
    private boolean isAuth = false;
    private String type;
    private long accountBalance = 0;//个人账户余额
    public String idCardPhotoStatus;
    private Dialog mWalletDialog;
    long subPrice = 0;//实际支付金额
    private Dialog mPayDialog;

    private boolean isPerson = false;
    EleAccountInfo mEleAccountInfo;

    @Autowired
    IUserService userService;

    public IUserService getUserService() {
        return userService;
    }

    private SPCartController mSPCartController;

    public static Intent getIntent(Context context, long[] bizOrderId, String price) {
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra(ValueConstants.PAY_BIZORDERID, bizOrderId);
        intent.putExtra(ValueConstants.PAY_PRICE, price);
        return intent;
    }

    private void finishActivity() {
        Intent intent = getIntent();
        intent.putExtra(SPUtils.EXTRA_DATA, payStatus);
        setResult(Activity.RESULT_OK, intent);
        finish();
        PayActivity.this.overridePendingTransition(R.anim.push_up_in2, R.anim.push_up_out2);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.scenicorderconfig_paywaypop_layout, null);
    }

    @Override
    public View onLoadNavView() {
        return null;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        EventBus.getDefault().register(this);
        msgApi = WXAPIFactory.createWXAPI(this, null);
        initView();
        //Just for monkey test disable pay
        if (AppDebug.IS_MONKEY_TEST) {
            finish();
            return;
        }
    }

    private void initView() {
        if (mWeichatPayTask == null) {
            mWeichatPayTask = new WeichatPayTask(PayActivity.this);
        }
        bizOrderId = getIntent().getLongArrayExtra(ValueConstants.PAY_BIZORDERID);
        stringPrice = getIntent().getStringExtra(ValueConstants.PAY_PRICE);
        if (!TextUtils.isEmpty(stringPrice)) {
            subPrice = Long.parseLong(stringPrice);
        } else {

        }
        payController = new PayController(this, mHandler);
        mSPCartController = new SPCartController(this, mHandler);
        setList();
        payway_listview = (ListView) this.findViewById(R.id.payway_listview);
        mAdapter = new OrderPayWayAdapter(this, payList);
        payway_listview.setAdapter(mAdapter);
        topLayout = (LinearLayout) this.findViewById(R.id.toplayout);
        payprice_tv.setText(StringUtil.converRMb2YunNoFlag(subPrice));

        initClick();
    }

    public boolean isUploadIdcard(EleAccountInfo info) {
        if ("NOT_UPLOAD".equals(info.idCardPhotoStatus)) {
            if ("YES".equals(info.isForcedUploadPhoto)) {
                showDialog(info, true);
                return false;
            } /*else {
                if (PrefUtil.getIsFirstStart(PayActivity.this)) {
                    showDialog(info, false);
                    PrefUtil.putIsFirstStart(PayActivity.this, false);
                }
            }*/

        }
        return true;
    }

    public void showDialog(final EleAccountInfo info, final boolean isFocus) {
        WalletDialog.showUptateIdentityDialog(this, info.uploadMsg, isFocus, new WalletDialog.IdentifyInterface() {
            @Override
            public void OnIdentifyConfirm() {
                NavUtils.gotoIdCardUploadActivity(PayActivity.this, info.userName);
            }

            @Override
            public void OnIdentifyCancel() {

            }
        });

    }

    private void initClick() {
        topLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });

        //支付
        order_config_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TimeUtil.isFastDoubleClick()) {
                    return;
                }
                if (getUserService().isLogin()) {
                    if (clickPostion == 1) {//支付宝支付
//                        payController.getPayInfo(PayActivity.this, bizOrderId, PayStatus.PAYTYPE_ZFB);

                        AliBatchPayParam aliBatchPayParam = new AliBatchPayParam();
                        aliBatchPayParam.bizOrderIdList = bizOrderId;
                        aliBatchPayParam.payChannel = PayStatus.PAYTYPE_ZFB;
                        aliBatchPayParam.sourceType = "APP";
                        mSPCartController.doGetAliBatchPayInfo(PayActivity.this, aliBatchPayParam);

                        Map<String, String> stringMap = new HashMap<String, String>();
                        stringMap.put(AnalyDataValue.KEY_PAY_WAY, getString(R.string.pay_eventzfb));
                        stringMap.put(AnalyDataValue.KEY_ORDER_ID, String.valueOf(bizOrderId));
                        TCEventHelper.onEvent(PayActivity.this, AnalyDataValue.CONFIRM_PAYMENT, stringMap);
                    }/* else if (clickPostion == 1) {//微信支付
                        payController.doGetWxBatchPayInfo(PayActivity.this, new long[]{bizOrderId});
                    }*/ else if (clickPostion == 0) {//支付
                        if (isPerson) {
                            if (!isAuth) {

                                NavUtils.gotoRealNameAuthActivity(PayActivity.this, type, "");
                                //  mWalletDialog = WalletDialog.showIdentificationDialog(PayActivity.this, type);
                            } else {
                                if (mEleAccountInfo != null) {
                                    if (isUploadIdcard(mEleAccountInfo)) {
                                        if (accountBalance < subPrice) {
                                            showNoBalanceDialog("钱包余额不足，请充值后再支付");
                                        } else {
                                            mPayDialog = showOrderPayDialog(PayActivity.this, subPrice, bizOrderId);
                                        }
                                    }
                                }

                            }
                        } else {
                            ToastUtil.showToast(PayActivity.this, "您的帐号暂不支持该功能");
                        }
                    } else if (clickPostion == 2) {//快捷支付
                        if (isPerson) {
                            //请求光大银行的链接
//                            payController.doGetCebCloudPayInfo(bizOrderId, null, WalletUtils.APP, PayActivity.this);

                            //请求光大银行的链接
                            CebCloudBatchPayParam cebCloudBatchPayParam = new CebCloudBatchPayParam();
                            cebCloudBatchPayParam.bizOrderIdList = bizOrderId;
                            cebCloudBatchPayParam.sourceType = "APP";
                            mSPCartController.doGetCebCloudBatchPayInfo(PayActivity.this, cebCloudBatchPayParam);

                            showLoadingView("");
                        } else {
                            ToastUtil.showToast(PayActivity.this, "您的帐号暂不支持该功能");
                        }
                    }
                } else {
                    NavUtils.gotoLoginActivity(PayActivity.this);
                }
            }
        });

        order_bottom_config_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishActivity();
        }
        return true;
    }

    private void setList() {
        payList = new ArrayList<PayModle>();
        PayModle modle1 = new PayModle();
        modle1.setTitle(getString(R.string.pay_quanyan));
        modle1.setIsSelect(true);

        PayModle modle2 = new PayModle();
        modle2.setTitle(getString(R.string.pay_byzfb));
        modle2.setIsSelect(false);

        PayModle modle3 = new PayModle();
        modle3.setTitle(getString(R.string.pay_kuaijie));
        modle3.setIsSelect(false);

        PayModle modle4 = new PayModle();
        modle4.setTitle(getString(R.string.pay_bywx));
        modle4.setIsSelect(false);

        payList.add(modle1);
        // payList.add(modle4);
        payList.add(modle2);
        payList.add(modle3);
//        if (PayConfig.WEIPAYCONFIG) {
//            if (MobileUtil.isWeixinAvilible(PayActivity.this)) {
//                payList.add(modle2);
//            }
//        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case ValueConstants.SPCART_GetAliBatchPayInfo_OK://成功
                AliPayInfo payInfo = (AliPayInfo) msg.obj;
                handleData(payInfo);
                break;
            case ValueConstants.SPCART_GetAliBatchPayInfo_ERROR:
                Toast.makeText(PayActivity.this, getString(R.string.pay_nonetwork),
                        Toast.LENGTH_SHORT).show();
                break;

            case ValueConstants.MSG_GET_PAY_WEIXINSTATUS_OK://服务器返回状态成功
                TmPayStatusInfo wxPayStatuInfo = (TmPayStatusInfo) msg.obj;
                handleWxPayStatu(wxPayStatuInfo);
                break;
            case ValueConstants.MSG_GET_PAY_WEIXINSTATUS_KO://无网状态处理
                Toast.makeText(PayActivity.this, getString(R.string.pay_nonetwork),
                        Toast.LENGTH_SHORT).show();
                payStatus = PayStatus.PAYING;
                finishActivity();
                break;
            //支付宝处理结果
            case SDK_PAY_FLAG:
                PayResult payResult = new PayResult((String) msg.obj);
                // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                if (TextUtils.equals(resultStatus, PayStatus.PAYOK)) {
                    Toast.makeText(PayActivity.this, getString(R.string.pay_success),
                            Toast.LENGTH_SHORT).show();
                    payStatus = PayStatus.PAYOK;
                    finishActivity();
                } else {
                    // 判断resultStatus 为非“9000”则代表可能支付失败
                    // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    if (TextUtils.equals(resultStatus, PayStatus.PAYING)) {
                        Toast.makeText(PayActivity.this, getString(R.string.pay_ing),
                                Toast.LENGTH_SHORT).show();
                        payStatus = PayStatus.PAYING;
                        finishActivity();
                    } else {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                        Toast.makeText(PayActivity.this, getString(R.string.pay_failed),
                                Toast.LENGTH_SHORT).show();
                        payStatus = PayStatus.PAYERROR;
                    }
                }
//                    finishActivity();
                break;


            case ValueConstants.MSG_GET_PAY_WEIXIN_OK://微信
                WxPayInfo mTmWxPayInfo = (WxPayInfo) msg.obj;
                if (mTmWxPayInfo.success) {
                    mWeichatPayTask.payByWeiChat(mTmWxPayInfo);
                } else {
                    ToastUtil.showToast(PayActivity.this, mTmWxPayInfo.errorMsg);
                }

                break;
            case ValueConstants.MSG_GET_PAY_WEIXIN_KO:
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;
            //获取个人电子钱包账户信息
            case ValueConstants.PAY_GETELEACCOUNTINFO_SUCCESS:
                EleAccountInfo info = (EleAccountInfo) msg.obj;
                handMsg(info);
                break;
            case ValueConstants.PAY_GETELEACCOUNTINFO_ERROR:
//                ToastUtil.showToast(WalletActivity.this, StringUtil.handlerErrorCode(WalletActivity.this, msg.arg1));
                break;

            case ValueConstants.PAY_ElePursePay_SUCCESS://钱包支付成功
                hideLoadingView();
                PayCoreBaseResult payCoreBaseResult = (PayCoreBaseResult) msg.obj;
                if (payCoreBaseResult != null) {
                    if (payCoreBaseResult.success) {
//                        ToastUtil.showToast(PayActivity.this, "支付成功");
                        payStatus = PayStatus.PAYOK;
                        finishActivity();
                    } else {
                        if (payCoreBaseResult.errorCode.equals(WalletUtils.PAY_PWD_ERROR)) {
                            showPassErrorDialog("支付密码错误，请重试");
                        } else if (payCoreBaseResult.errorCode.equals(WalletUtils.PAY_PWD_MORE_THAN_MAXIMUM_RETRIES)) {
                            showSurPassErrorDialog("支付密码输入错误过多账户已被锁定，请点击忘记密码进行找回或10分钟后重试");
                        } else {
                            ToastUtil.showToast(PayActivity.this, "支付失败");
                        }
                    }
                } else {
                    ToastUtil.showToast(PayActivity.this, "支付失败");
                }
                break;
            case ValueConstants.PAY_ElePursePay_ERROR://钱包支付失败
                hideLoadingView();
                ToastUtil.showToast(PayActivity.this, StringUtil.handlerErrorCode(PayActivity.this, msg.arg1));
                break;
            case ValueConstants.SPCART_GetCebCloudBatchPayInfo_OK://获取光大银行快捷支付URL
                hideLoadingView();
                CebCloudPayInfo cebCloudPayInfo = (CebCloudPayInfo) msg.obj;
                if (cebCloudPayInfo != null) {
                    if (!TextUtils.isEmpty(cebCloudPayInfo.payInfo)) {
                        WebParams params = new WebParams();
                        params.setUrl(cebCloudPayInfo.payInfo);
                        params.setTitle("支付");
                        NavUtils.openBrowser(PayActivity.this, params);
                    } else {
                        ToastUtil.showToast(PayActivity.this, "支付失败");
                    }
                } else {
                    ToastUtil.showToast(PayActivity.this, "支付失败");
                }
                break;
            case ValueConstants.SPCART_GetCebCloudBatchPayInfo_ERROR:
                hideLoadingView();
                ToastUtil.showToast(PayActivity.this, StringUtil.handlerErrorCode(PayActivity.this, msg.arg1));
                break;
            case ValueConstants.PAY_GetPayStatusInfo_SUCCESS:
                hideLoadingView();
                PcPayStatusInfo payStatusInfo = (PcPayStatusInfo) msg.obj;
                if (payStatusInfo != null) {
                    if (payStatusInfo.payStatus == 10000 && payStatusInfo.success) {
//                        ToastUtil.showToast(PayActivity.this, "支付成功");
                        payStatus = PayStatus.PAYOK;
                        finishActivity();
                    } else {
                        ToastUtil.showToast(PayActivity.this, "支付失败");
                    }
                } else {
                    ToastUtil.showToast(PayActivity.this, "支付失败");
                }
                break;
            case ValueConstants.PAY_GetPayStatusInfo_ERROR:
                hideLoadingView();
                ToastUtil.showToast(PayActivity.this, StringUtil.handlerErrorCode(PayActivity.this, msg.arg1));
                break;
            case 0x110:
                ElePurseBatchPayParam params = (ElePurseBatchPayParam) msg.obj;

//                payController.doElePursePay(params, PayActivity.this);
                mSPCartController.doElePurseBatchPay(PayActivity.this, params);

                if (mPayDialog != null && mPayDialog.isShowing()) {
                    mPayDialog.dismiss();
                }
                showLoadingView("正在支付，请稍候...");
                break;

        }
    }

    //处理服务器返回的微信支付状态
    private void handleWxPayStatu(TmPayStatusInfo wxPayStatuInfo) {
        if (wxPayStatuInfo != null && wxPayStatuInfo.payStatus == PayStatus.PAYSERVEROK) {
            hideLoadingView();
            Toast.makeText(PayActivity.this, getString(R.string.pay_success), Toast.LENGTH_SHORT).show();
            payStatus = PayStatus.PAYOK;
            finishActivity();
        } else {
            //三次向服务器轮询获取结果
            if (serverCount < MAX_COUNT) {
                try {
                    Thread.sleep(SEND_TIME);
                    payController.getPayStatusInfo(PayActivity.this, bizOrderId[0]);
                    serverCount++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                payStatus = PayStatus.PAYING;
                hideLoadingView();
                finishActivity();
            }
        }

    }


    public void onEventMainThread(WeiXinPayResult result) {

        BaseResp resp = result.getResp();

        //微信支付处理code
        switch (resp.errCode) {

            case 0://微信支付成功
                showLoadingView("");
                payController.getPayStatusInfo(PayActivity.this, bizOrderId[0]);//请求服务器判断是否成功
                serverCount++;
                /*Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                payStatus = PayStatus.PAYOK;
                finishActivity();*/
                break;
            case -1://支付失败
                Toast.makeText(PayActivity.this, getString(R.string.pay_failed), Toast.LENGTH_SHORT).show();
                payStatus = PayStatus.PAYERROR;
                break;
            case -2://用户取消
                Toast.makeText(PayActivity.this, getString(R.string.pay_failed), Toast.LENGTH_SHORT).show();
                payStatus = PayStatus.PAYERROR;
                break;

            default:
                break;
        }

        /*Message msg = new Message();
        msg.what = SDK_WEIXIN_PAY_FLAG;
        msg.obj = resp;
        mHandler.sendMessage(msg);*/

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        msgApi.unregisterApp();
        EventBus.getDefault().unregister(this);
    }


    private void handleData(AliPayInfo payInfo) {
        if (payInfo == null) {
            ToastUtil.showToast(this, getString(R.string.pay_backnull));
            return;
        }
        if (StringUtil.isEmpty(payInfo.payInfo)) {
            ToastUtil.showToast(PayActivity.this, getString(R.string.pay_dataerror));
            return;
        }
        alipay(payInfo.payInfo);
    }

    //start======================支付宝支付相关代码============//
     /*
     * 支付宝支付
     *
     * @param payInfo
     */
    private void alipay(final String payInfo) {
        if (payInfo == null) {
            return;
        }
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PayActivity.this);
                // 调用支付接口，获取支付结果
//                String result = alipay.pay(payInfo);
//
//                Message msg = new Message();
//                msg.what = SDK_PAY_FLAG;
//                msg.obj = result;
//                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }

    //end======================上面是支付宝支付相关代码============//
    class OrderPayWayAdapter extends BaseAdapter {
        private Context mContext;
        private List<PayModle> mList;

        public OrderPayWayAdapter(Context mContext, List<PayModle> mList) {
            this.mContext = mContext;
            this.mList = mList;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.scenicorderconfig_paywaypop_item_layout, null);
            TextView payway_texttitle = (TextView) convertView.findViewById(R.id.payway_texttitle);
            ImageView payway_image = (ImageView) convertView.findViewById(R.id.payway_image);
            ImageView pay_select_im = (ImageView) convertView.findViewById(R.id.pay_select_im);
            RelativeLayout pay_item_layout = (RelativeLayout) convertView.findViewById(R.id.pay_item_layout);
            payway_texttitle.setText(mList.get(position).getTitle().toString());
            if (mList.get(position).getTitle().toString().equals(getString(R.string.pay_byzfb))) {
                payway_image.setImageResource(R.mipmap.pay_zhifubao_image);
            } else if (mList.get(position).getTitle().toString().equals(getString(R.string.pay_quanyan))) {
                payway_image.setImageResource(R.mipmap.jiuxiu_pay);
            } else if (mList.get(position).getTitle().toString().equals(getString(R.string.pay_kuaijie))) {
                payway_image.setImageResource(R.mipmap.kuaijie_pay);
            } else if (mList.get(position).getTitle().toString().equals(getString(R.string.pay_weichat))) {
                payway_image.setImageResource(R.mipmap.pay_wexin_image);
            }

            if (mList.get(position).isSelect()) {
                pay_select_im.setImageResource(R.mipmap.ic_checked);
            } else {
                pay_select_im.setImageResource(R.mipmap.ic_uncheck);
            }

            pay_item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetPayList(position);
                    clickPostion = position;
                }
            });

            return convertView;
        }
    }

    /**
     * 重置数据刷新界面
     *
     * @param postion
     */
    private void resetPayList(int postion) {
        for (int i = 0; i < payList.size(); i++) {
            if (postion == i) {
                payList.get(i).setIsSelect(true);
            } else {
                payList.get(i).setIsSelect(false);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    class PayModle {
        private String title;
        private boolean isSelect;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setIsSelect(boolean isSelect) {
            this.isSelect = isSelect;
        }
    }

    //请求接口获取个人钱包信息
    private void getUserPayMsg() {
        payController.doGetEleAccountInfo(this);
    }

    //获取订单支付状态
    private void doGetPayStatusInfo(long bizID) {
        payController.doGetPayStatusInfo(bizID, this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mWalletDialog != null && mWalletDialog.isShowing()) {
            mWalletDialog.dismiss();
        }
        getUserPayMsg();
    }


    public void onEvent(GuangDaPaySuccessEvBus bus) {
        //TODO 光大银行支付完成后处理
        doGetPayStatusInfo(bizOrderId[0]);
        showLoadingView("正在确认支付状态，请稍候");
    }

    private void handMsg(EleAccountInfo info) {
        this.mEleAccountInfo = info;
        if (info == null) {
            isPerson = true;
            isAuth = false;
            type = WalletUtils.OPEN_ELE_ACCOUNT;
        } else {
            if (!TextUtils.isEmpty(info.accountType)) {
                if (info.accountType.equals(WalletUtils.PERSON)) {
                    isPerson = true;
                    idCardPhotoStatus = info.idCardPhotoStatus;
                    if (!TextUtils.isEmpty(info.status)) {
                        if (info.status.equals(WalletUtils.TACK_EFFECT)) {
                            if (info.existPayPwd) {
                                isAuth = true;
                            } else {
                                type = WalletUtils.SETUP_PAY_PWD;
                                isAuth = false;
                            }
                        } else {
                            type = WalletUtils.OPEN_ELE_ACCOUNT;
                            isAuth = false;
                        }
                        accountBalance = info.accountBalance;
                    } else {
                        type = WalletUtils.OPEN_ELE_ACCOUNT;
                        isAuth = false;
                    }
                } else {
//                    ToastUtil.showToast(this, "企业账户");
                }
            } else {
//                ToastUtil.showToast(this, "企业账户");
            }
        }
    }

    /**
     * 支付弹框
     *
     * @param context
     * @param sub
     * @param bizOrderId
     * @return
     */
    private Dialog showOrderPayDialog(final Context context, final long sub, final long[] bizOrderId) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        View view = mLayoutInflater.inflate(R.layout.pay_pass_dialog, null);
        TextView tv_subtitle = (TextView) view.findViewById(R.id.tv_subtitle);
        tv_subtitle.setText("");
        final Dialog dialog = new Dialog(context, R.style.kangzai_dialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        final GridPasswordView mCustomUi = (GridPasswordView) view.findViewById(R.id.gpv_customUi);
        TextView mMoney = (TextView) view.findViewById(R.id.tv_money);
        mMoney.setText(StringUtil.formatWalletMoneyNoFlg(sub));
        mCustomUi.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
                if (!TextUtils.isEmpty(psw) && psw.length() == 6) {
                    WalletUtils.hideSoft(context, mCustomUi.getWindowToken());
//                    ElePursePayParam payParam = new ElePursePayParam();
                    ElePurseBatchPayParam payParam = new ElePurseBatchPayParam();
                    payParam.bizOrderIdList = bizOrderId;
                    payParam.payPwd = MD5Utils.toMD5(psw);
                    payParam.sourceType = WalletUtils.APP;

                    Message msg = Message.obtain();
                    msg.obj = payParam;
                    msg.what = 0x110;
                    mHandler.sendMessageDelayed(msg, 500);
                }
            }

            @Override
            public void onInputFinish(String psw) {

            }
        });
        dialog.show();
        return dialog;
    }

    /**
     * 显示输入密码错误弹框
     */

    private Dialog mErrorDilog;

    private void showPassErrorDialog(String msg) {
        if (mErrorDilog == null) {
            mErrorDilog = DialogUtil.showMessageDialog(this,
                    null,
                    msg,
                    "重试",
                    "忘记密码",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mErrorDilog != null) {
                                mErrorDilog.dismiss();
                            }
                            mPayDialog = showOrderPayDialog(PayActivity.this, subPrice, bizOrderId);
                        }
                    },
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mErrorDilog != null) {
                                mErrorDilog.dismiss();
                            }
                            NavUtils.gotoForgetPasSelectCardActivity(PayActivity.this);
                        }
                    });
        }
        mErrorDilog.show();
    }

    private Dialog mSurErrorDialog;

    private void showSurPassErrorDialog(String msg) {
        if (mSurErrorDialog == null) {
            mSurErrorDialog = DialogUtil.showMessageDialog(this,
                    null,
                    msg,
                    "取消",
                    "忘记密码",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mSurErrorDialog != null) {
                                mSurErrorDialog.dismiss();
                            }
                        }
                    },
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mSurErrorDialog != null) {
                                mSurErrorDialog.dismiss();
                            }
                            NavUtils.gotoForgetPasSelectCardActivity(PayActivity.this);
                        }
                    });
        }
        mSurErrorDialog.show();
    }

    private Dialog mNoBalanceDialog;

    private void showNoBalanceDialog(String msg) {
        if (mNoBalanceDialog == null) {
            mNoBalanceDialog = DialogUtil.showMessageDialog(this,
                    null,
                    msg,
                    "充值",
                    "取消",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mNoBalanceDialog != null) {
                                mNoBalanceDialog.dismiss();
                            }
                            NavUtils.gotoWalletActivity(PayActivity.this);
                        }
                    },
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mNoBalanceDialog != null) {
                                mNoBalanceDialog.dismiss();
                            }
                        }
                    });
        }
        mNoBalanceDialog.show();
    }

}
