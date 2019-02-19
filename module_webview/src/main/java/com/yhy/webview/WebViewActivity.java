//package com.yhy.webview;
//
///**
// * Created with Android Studio.
// * Title:WebViewActivity
// * Description:
// * Copyright:Copyright (c) 2015
// * Company:和平必胜、正义必胜、人民必胜
// * Author:徐学东
// * Date:15/11/11
// * Time:下午2:41
// * Version 1.0
// */
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.ClipData;
//import android.content.ClipboardManager;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.net.Uri;
//import android.net.http.SslError;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Message;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.content.PermissionChecker;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.InputMethodManager;
//import android.webkit.CookieManager;
//import android.webkit.CookieSyncManager;
//import android.webkit.JsResult;
//import android.webkit.SslErrorHandler;
//import android.webkit.ValueCallback;
//import android.webkit.WebChromeClient;
//import android.webkit.WebResourceError;
//import android.webkit.WebResourceRequest;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//
//import com.alibaba.android.arouter.facade.annotation.Autowired;
//import com.alipay.sdk.app.PayTask;
//import com.github.lzyzsd.jsbridge.BridgeHandler;
//import com.github.lzyzsd.jsbridge.BridgeWebView;
//import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
//import com.github.lzyzsd.jsbridge.CallBackFunction;
//import com.github.lzyzsd.jsbridge.DefaultHandler;
//import com.harwkin.nb.camera.CameraHandler;
//import com.harwkin.nb.camera.CameraManager;
//import com.harwkin.nb.camera.callback.CameraImageListener;
//import com.harwkin.nb.camera.callback.SelectMoreListener;
//import com.harwkin.nb.camera.options.CameraOptions;
//import com.harwkin.nb.camera.pop.CameraPop;
//import com.harwkin.nb.camera.pop.CameraPopListener;
//import com.harwkin.nb.camera.type.OpenType;
//
//import com.newyhy.utils.ShareUtils;
//import com.newyhy.utils.app_utils.AppInfo;
//import com.newyhy.utils.app_utils.AppInfoUtils;
//
//import com.newyhy.wsy.UpLoadCourseVideoService;
//
//import com.quanyan.base.baseenum.IActionTitleBar;
//import com.quanyan.base.util.HarwkinLogUtil;
//import com.quanyan.base.util.LocalUtils;
//import com.quanyan.base.view.BaseNavView;
//import com.quanyan.base.yminterface.ErrorViewClick;
//
//import com.quanyan.pedometer.ShareActivity;
//
//import com.quanyan.yhy.common.WeiXinPayResult;
//import com.quanyan.yhy.eventbus.EvBusCommentChange;
//import com.quanyan.yhy.eventbus.EvBusGalleryError;
//import com.quanyan.yhy.eventbus.EvBusUpCourseVideo;
//import com.quanyan.yhy.eventbus.EvBusUserLoginState;
//import com.quanyan.yhy.eventbus.EvBusWebUserLoginState;
//
//import com.quanyan.yhy.net.NetManager;
//import com.quanyan.yhy.net.lsn.OnResponseListener;
//import com.quanyan.yhy.pay.alipay.PayResult;
//
//import com.quanyan.yhy.ui.base.utils.DialogUtil;
//import com.quanyan.yhy.ui.base.utils.NativeUtils;
//import com.quanyan.yhy.ui.base.utils.NavUtils;
//
//import com.quanyan.yhy.util.MobileUtil;
//import com.quanyan.yhy.util.QRCodeUtil;
//import com.tencent.mm.opensdk.modelbase.BaseResp;
//import com.tencent.mm.opensdk.modelpay.PayReq;
//import com.tencent.mm.opensdk.openapi.IWXAPI;
//import com.tencent.mm.opensdk.openapi.WXAPIFactory;
//import com.umeng.socialize.UMAuthListener;
//import com.umeng.socialize.UMShareAPI;
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.yhy.cityselect.eventbus.EvBusLocationChange;
//
//import com.yhy.common.base.BaseNewActivity;
//import com.yhy.common.base.YHYBaseApplication;
//import com.yhy.common.beans.album.MediaItem;
//import com.yhy.common.beans.net.model.ProductCardModel;
//import com.yhy.common.beans.net.model.ShareBean;
//import com.yhy.common.beans.net.model.param.WebParams;
//import com.yhy.common.beans.net.model.shop.MerchantItem;
//import com.yhy.common.beans.net.model.tm.PayInfo;
//import com.yhy.common.beans.net.model.trip.ItemVO;
//import com.yhy.common.beans.user.User;
//import com.yhy.common.constants.ValueConstants;
//import com.yhy.common.utils.AndroidUtils;
//import com.yhy.common.utils.PermissionUtils;
//import com.yhy.common.utils.SPUtils;
//import com.yhy.location.LocationManager;
//import com.yhy.network.api.user.UserApi;
//import com.yhy.network.utils.RequestHandlerKt;
//import com.yhy.router.YhyRouter;
//import com.yhy.service.IUserService;
//import com.yhy.webview.utils.AndroidBug5497Workaround;
//import com.yhy.webview.utils.CookieUtil;
//import com.yhy.webview.utils.ImageLoaderUtils;
//import com.yhy.webview.utils.ScreenShotListenManager;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import de.greenrobot.event.EventBus;
//
//import static com.newyhy.wsy.UpLoadCourseVideoService.TYPE;
//import static com.newyhy.wsy.UpLoadCourseVideoService.VIDEO_ID;
//import static com.newyhy.wsy.UpLoadUgcVideoService.MEDIA_PATH;
//
//public class WebViewActivity extends BaseNewActivity implements OpenFileChooserCallBack, CameraPopListener, CameraImageListener {
//    public static final int MSG_SHOW_ERROR_PAGE = 0x1001;
//    public static final int ALI_PAY_RESULT = 0x2000;
//    public static final int REQUEST_CODE_PERMISSION = 0x3000;
//    private BridgeWebView mWebView;
//    private WebController mWebController;
//    //外面带来的参数
//    private WebParams mWebParams;
//    //是否显示错误页
//    private boolean isErrorPage = false;
//    private ValueCallback<Uri> mUploadMsg;
//    private ValueCallback<Uri[]> mUploadMsg5Plus;
//    private String file_path;
//
//    //截屏行为监听器
//    private ScreenShotListenManager manager;
//
//    CameraPop mCameraPop;
//    IWXAPI api;
//    String result_code;
//    CallBackFunction callBackFunction;
//    CallBackFunction goBackFunction;
//    CallBackFunction uploadFunction;
//    CallBackFunction selectCityFunction;
//    String videoId;
//    String changeCallback;     //  common.uploadChange
//    String openCallback;     //  common.openCallback
//
//    private boolean isResume;                        //  是否已经初始化activity
//    private boolean isLoadUrl;                       //  是否成功加载过网页
//    private String eventName;                        //  hookback参数
//    private Dialog mPermissionDialog;
//
//    @Autowired
//    IUserService userService;
//
//    public IUserService getUserService() {
//        return userService;
//    }
//
//    /**
//     * 打开内部浏览器
//     *
//     * @param params
//     */
//    public static void openBrowser(Context context, WebParams params) {
//        Intent intent = new Intent(context, WebViewActivity.class);
//        intent.putExtra(SPUtils.EXTRA_DATA, params);
//        context.startActivity(intent);
//
//    }
//
//    /**
//     * 打开内部浏览器
//     *
//     * @param params
//     */
//    public static void openBrowser(Activity context, WebParams params, int reqCode) {
//        Intent intent = new Intent(context, WebViewActivity.class);
//        intent.putExtra(SPUtils.EXTRA_DATA, params);
//        context.startActivityForResult(intent, reqCode);
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        YhyRouter.getInstance().inject(this);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (mWebParams.isCleanCookie()) {
//            CookieUtil.cleanCookie(this);
//        }
//        EventBus.getDefault().unregister(this);
//    }
//
//    @Override
//    protected void initView(Bundle savedInstanceState) {
//        EventBus.getDefault().register(this);
//        mWebController = new WebController(this, mHandler);
//        mWebView = findViewById(R.id.wv_content);
//
//        if (!mWebParams.isShowTitle()) {
//            setTitleBarBackground(Color.TRANSPARENT);
//            setSystemBarTintManagerColor(getResources().getColor(R.color.transparent));
//        }
//
//        initData();
//        if (mWebView == null) {
//            finish();
//            return;
//        }
//        mWebView.setDefaultHandler(new DefaultHandler());
//        loadCookieUrl(true);
//        callBackJsMethod();
//
//        AndroidBug5497Workaround.assistActivity(this);
//
//        mCameraPop = new CameraPop(WebViewActivity.this, this, this);
//        mCameraPop.setListener(new CameraPop.CancelListener() {
//            @Override
//            public void onCancel() {
//                if (mUploadMsg != null) {
//                    mUploadMsg.onReceiveValue(null);
//                    mUploadMsg = null;
//                }
//                if (mUploadMsg5Plus != null) {
//                    mUploadMsg5Plus.onReceiveValue(null);
//                    mUploadMsg5Plus = null;
//                }
//            }
//        });
//    }
//
//    @Override
//    public void handleMessage(Message msg) {
//        hideLoadingView();
//        hideErrorView(null);
//        switch (msg.what) {
//            case MSG_SHOW_ERROR_PAGE:
//                showErrorView(null, IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
//                    @Override
//                    public void onClick(View view) {
//                        isErrorPage = false;
//                        reloadUrlForErrorPage();
//                        showLoadingView(getResources().getString(R.string.scenic_loading_notice));
//                    }
//                });
//                break;
//            case NativeUtils.MSG_SCHEME_URL:
//                if (msg.obj instanceof String) {
//                    Uri uri = Uri.parse((String) msg.obj);
//                    NativeUtils.parseNativeData(uri, this);
//                }
//                break;
//            case ValueConstants.MSG_GET_WTK_OK:
//                String wtk = (String) msg.obj;
//                if (!TextUtils.isEmpty(wtk)) {
//                    //TODO 新老兼顾，暂时使用返回值
//                    new UserApi().saveWebToken(wtk);
//                    SPUtils.setWebUserToken(WebViewActivity.this, wtk, -1);
//                    SPUtils.setLastUpdateWtkTime(WebViewActivity.this, System.currentTimeMillis());
//                    loadCookieUrl(!isLoadUrl);
//                } else {
//                    reloadUrlForErrorPage();
//                }
//                break;
//            case ValueConstants.MSG_GET_WTK_KO:
//                reloadUrlForErrorPage();
//                break;
//            case ALI_PAY_RESULT:
//                PayResult result = new PayResult((String) msg.obj);
//                JSONObject jsonObject = new JSONObject();
//                try {
//                    jsonObject.put("code", 0);
//                    jsonObject.put("message", "OK");
//                    JSONObject otherObject = new JSONObject();
//                    otherObject.put("status", result.getResultStatus());
//                    jsonObject.put("data", otherObject.toString());
//                    callBackFunction.onCallBack(jsonObject.toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                break;
//        }
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//
//        mWebParams = (WebParams) intent.getSerializableExtra(SPUtils.EXTRA_DATA);
//    }
//
//
//    /**
//     * @Description 加载数据
//     */
//    @SuppressLint("JavascriptInterface")
//    private void initData() {
//        if (mWebView == null) {
//            return;
//        }
//        WebSettings settings = mWebView.getSettings();
//        settings.setJavaScriptEnabled(true);
//        settings.setJavaScriptCanOpenWindowsAutomatically(true);
//        settings.setPluginState(WebSettings.PluginState.ON);
//        settings.setUseWideViewPort(true);
//        settings.setLoadWithOverviewMode(true);
//        settings.setBuiltInZoomControls(true);
//        settings.setSupportZoom(true);
//        mWebView.getSettings().setDomStorageEnabled(true);
//        mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
//        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
//        mWebView.getSettings().setAppCachePath(appCachePath);
//        mWebView.getSettings().setAllowFileAccess(false);
//        mWebView.getSettings().setAppCacheEnabled(false);
//        // 允许https http内容混合
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
//        settings.setDisplayZoomControls(false);
//        // 调试用
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            mWebView.setWebContentsDebuggingEnabled(true);
//        }
//        // 添加js交互接口类，并起别名 imagelistner
//        mWebView.addJavascriptInterface(new JavascriptInterface(this), "imagelistner");
//        //不保存密码
//        mWebView.getSettings().setSavePassword(false);
//        //移除有风险的Webview系统隐藏接口
//        mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
//        mWebView.removeJavascriptInterface("accessibilityTraversal");
//        mWebView.removeJavascriptInterface("accessibility");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            mWebView.setWebContentsDebuggingEnabled(true);
//        }
//        //TODO 增加UA
//        String ua = mWebView.getSettings().getUserAgentString();
//        mWebView.getSettings().setUserAgentString(ua +
//                ";QuanYanYingHeYing_" + AndroidUtils.getVersionCode(this) +
//                ";Channal_" + YHYBaseApplication.getInstance().getYhyEnvironment().getChannel());
//
//        // 不使用缓存，只从网络获取数据
////        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//
//        mWebView.setWebViewClient(new JsWebClient(mWebView));
//        mWebView.setWebChromeClient(new ReWebChomeClient(this));
//
//        if ("会员码".equals(mWebParams.getTitle())) {
//            manager = ScreenShotListenManager.newInstance(this);
//            manager.setListener(imagePath -> reloadUrlForErrorPage());
//            manager.startListen();
//        }
//    }
//
//    /**
//     * 重现加载网页
//     */
//    private void reloadUrlForErrorPage() {
//        showLoadingView("");
//        hideErrorView(null);
//        if (mWebParams.getUrl().startsWith("http://") || mWebParams.getUrl().startsWith("https://")) {
//            mWebView.loadUrl(mWebParams.getUrl());
//        } else if (mWebParams.getUrl().startsWith("file://")) {
//            mWebView.loadUrl(mWebParams.getUrl());
//        } else {
//            mWebView.loadData(mWebParams.getUrl(), "text/html; charset=UTF-8", null);
//        }
//    }
//
//    /**
//     * http://192.168.30.217/confluence/pages/viewpage.action?pageId=2293767
//     */
//    private void callBackJsMethod() {
//        // 设置标题栏右边功能按钮
//        mWebView.registerHandler("setNavButton", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                Log.e("setNavButton", data);
//                try {
//                    JSONObject object = new JSONObject(data);
//                    mNavView.removeRightView();
//                    JSONArray array = object.getJSONArray("data");
//                    for (int i = array.length() - 1; i >= 0; i--) {
//                        try {
//                            JSONObject o = array.getJSONObject(i);
//                            String title = o.getString("title");
//                            String icon = o.getString("icon");
//                            String color = "";
//                            int font = 0;
//                            boolean bold = false;
//                            if (!o.isNull("color")) {
//                                color = o.getString("color");
//                            }
//                            if (!o.isNull("font")) {
//                                font = o.getInt("font");
//                            }
//                            if (!o.isNull("bold")) {
//                                bold = o.getBoolean("bold");
//                            }
//                            final String callback = o.getString("callback");
//
//                            mNavView.addRightView(title, icon, color, font, bold, new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    mWebView.callHandler(callback, "", new CallBackFunction() {
//                                        @Override
//                                        public void onCallBack(String data) {
//                                        }
//                                    });
//                                }
//                            });
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        // 设置标题栏中间标题
//        mWebView.registerHandler("setNativeTitle", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                Log.e("setNativeTitle", data);
//                try {
//                    JSONObject object = new JSONObject(data);
//                    String title = object.getString("title");
//                    String color = "";
//                    if (!object.isNull("color")) {
//                        color = object.getString("color");
//                    }
//                    mNavView.setTitleText(title);
//                    if (!TextUtils.isEmpty(color))
//                        mNavView.setTitleColor(color);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        // h5获取native的用户信息
//        mWebView.registerHandler("getUserInfo", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                JSONObject jsonObject = new JSONObject();
//                try {
//                    String phone = SPUtils.getMobilePhone(WebViewActivity.this);
//                    long user_id = userService.getLoginUserId();
//                    String token = SPUtils.getUserToken(WebViewActivity.this);
//
//                    User loginUser = getUserService().getUserInfo(getUserService().getLoginUserId());
//                    if (loginUser == null) {
//                        loginUser = new User();
//                    }
//                    int sportHobby = loginUser.getSportHobby();//DBManager.getInstance(YHYBaseApplication.getInstance()).getDefaultUserInfo().sportHobby;
//                    if (!TextUtils.isEmpty(phone) && user_id > 0 && !TextUtils.isEmpty(phone)) {
//                        jsonObject.put("code", 0);
//                        jsonObject.put("message", "OK");
//                        JSONObject otherObject = new JSONObject();
//                        otherObject.put("iphone", phone);
//                        otherObject.put("userId", user_id);
//                        otherObject.put("sportHobby", sportHobby);
//                        otherObject.put("token", token);
//                        jsonObject.put("data", otherObject.toString());
//                    } else {
//                        jsonObject.put("code", -1);
//                        jsonObject.put("message", "FAIL");
//                        jsonObject.put("data", "");
//                    }
//
//                    function.onCallBack(jsonObject.toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        // h5获取native的设备信息
//        mWebView.registerHandler("getClientInfo", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                JSONObject jsonObject = new JSONObject();
//                try {
//                    String deviceId = RequestHandlerKt.getDeviceId();
//                    String appVersion = AndroidUtils.getVersion(WebViewActivity.this);
//                    String os = "Android";
//                    String osVersion = Build.VERSION.RELEASE;
//
//                    double latg;
//                    double lngg;
//                    if (LocationManager.getInstance().getStorage().isGPRSok()) {
//                        if (PermissionUtils.checkLocationPermission(WebViewActivity.this)) {
//                            //如果定位成功则使用定位得到地理位置信息拉取这个接口
//                            latg = LocationManager.getInstance().getStorage().getGprs_lat();
//                            lngg = LocationManager.getInstance().getStorage().getGprs_lng();
//                        } else {
//                            //否则使用LastLocation
//                            latg = Double.valueOf(LocationManager.getInstance().getStorage().getLast_lat());
//                            lngg = Double.valueOf(LocationManager.getInstance().getStorage().getLast_lng());
//                        }
//                    } else {
//                        //否则使用LastLocation
//                        latg = Double.valueOf(LocationManager.getInstance().getStorage().getLast_lat());
//                        lngg = Double.valueOf(LocationManager.getInstance().getStorage().getLast_lng());
//                    }
//
//                    String lat = String.valueOf(latg);
//                    String lng = String.valueOf(lngg);
//                    String address = SPUtils.getExtraCurrentAddress(WebViewActivity.this);
//                    String cityCode = LocationManager.getInstance().getStorage().getManual_cityCode();
//                    String province = SPUtils.getExtraLocationProvinceName(WebViewActivity.this);
//                    String cityName = LocationManager.getInstance().getStorage().getManual_cityName();
//                    String adCode = LocationManager.getInstance().getStorage().getManual_discCode();
//                    String district = LocationManager.getInstance().getStorage().getManual_discName();
//                    jsonObject.put("code", 0);
//                    jsonObject.put("message", "OK");
//                    JSONObject otherObject = new JSONObject();
//                    otherObject.put("deviceId", deviceId);
//                    otherObject.put("appVersion", appVersion);
//                    otherObject.put("os", os);
//                    otherObject.put("osVersion", osVersion);
//                    otherObject.put("lat", lat);
//                    otherObject.put("lng", lng);
//                    otherObject.put("province", province);
//                    otherObject.put("city", cityName);
//                    otherObject.put("cityCode", cityCode);
//                    otherObject.put("district", district);
//                    otherObject.put("districtCode", adCode);
//                    otherObject.put("address", address);
//                    otherObject.put("session", RequestHandlerKt.getSession());
//                    jsonObject.put("data", otherObject.toString());
//
//                    function.onCallBack(jsonObject.toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        // h5的分享信息
//        mWebView.registerHandler("share", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                Log.e("share", data);
//                try {
//                    JSONObject object = new JSONObject(data);
//                    String url = "";
//                    if (object.has("url")) {
//                        url = object.getString("url");
//                    } else if (object.has("link")) {
//                        url = object.getString("link");
//
//                    }
//                    String title = object.getString("title");
//                    String desc = "";
//                    if (object.has("desc")) {
//                        desc = object.getString("desc");
//                    }
//                    String pic = object.getString("pic");
//
//                    ShareUtils.showShareBoard(WebViewActivity.this, title, desc, url, pic);
////                    ShareUtils.showShareBoard(WebViewActivity.this, title, desc, url, pic, true, true, null, null);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        // 设置工具栏状态
//        mWebView.registerHandler("setNavbarTransparent", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                Log.e("setNavbarTransparent", data);
//                if (data == null || data.equals("null")) {
//                    return;
//                }
//
//                try {
//                    JSONObject object = new JSONObject(data);
//                    String transparent = object.getString("transparent");
//                    String showback = object.getString("showback");
//
//                    if (transparent.equals("true")) {
//                        setDisallowTopCover(false);
//                        setTitleBarBackground(Color.TRANSPARENT);
//                        setSystemBarTintManagerColor(Color.TRANSPARENT);
//                        mNavView.removeLineView();
//
//                        if (showback.equals("true")) {
//                            mNavView.setLeftImage(R.mipmap.arrow_back_white);
//                        } else {
//                            mNavView.setLeftImage(Color.TRANSPARENT);
//                        }
//
//                    } else {
//                        setDisallowTopCover(true);
//                        mNavView.setLeftImage(R.mipmap.arrow_back_gray);
//                        setTitleBarBackground(Color.WHITE);
//                        setSystemBarTintManagerColor(getResources().getColor(R.color.gray));
//                        mNavView.addLineView();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                JSONObject jsonObject = new JSONObject();
//                try {
//                    jsonObject.put("code", 0);
//                    jsonObject.put("message", "OK");
//                    jsonObject.put("data", "");
//                    function.onCallBack(jsonObject.toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        // 显示加载框
//        mWebView.registerHandler("showLoading", new BridgeHandler() {
//
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                showLoadingView("");
//            }
//        });
//
//        // 隐藏加载框
//        mWebView.registerHandler("hideLoading", new BridgeHandler() {
//
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                hideLoadingView();
//            }
//        });
//
//        // 显示地图
//        mWebView.registerHandler("showmap", new BridgeHandler() {
//
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                try {
//                    JSONObject object = new JSONObject(data);
//                    String lat = object.getString("lat");
//                    String lng = object.getString("lng");
//                    String title = object.getString("title");
//                    String address = object.getString("address");
//                    HashMap<String, String> map = new HashMap<>();
//                    map.put("lat", lat);
//                    map.put("lng", lng);
//                    map.put("title", title);
//                    map.put("address", address);
//                    List<AppInfo> apps = AppInfoUtils.getMapApps(WebViewActivity.this);
//                    if (apps.size() > 0) {
//                        AppInfoUtils.showMapAppListDialog(WebViewActivity.this, apps, map);
//                    } else {
//                        //走其他MapApp
//                        try {
//                            String uri = "geo:" + lat + "," + lng + "?q=" + address;
//                            Uri mUri = Uri.parse(uri);
//                            Intent mIntent = new Intent(Intent.ACTION_VIEW, mUri);
//                            startActivity(mIntent);
//                        } catch (Exception e) {
//                            AndroidUtils.showToast(WebViewActivity.this, "未安装任何地图应用,无法导航");
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        // 复制文字
//        mWebView.registerHandler("copyText", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                if (data != null && !data.equals("null")) {
//                    try {
//                        JSONObject object = new JSONObject(data);
//                        String text = object.getString("data");
//                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                        ClipData clipData = ClipData.newPlainText(null, text);
//                        clipboard.setPrimaryClip(clipData);
//                        AndroidUtils.showToast(WebViewActivity.this, "复制成功");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//
//        // 打电话
//        mWebView.registerHandler("dial", new BridgeHandler() {
//
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                if (data != null && !data.equals("null")) {
//                    try {
//                        JSONObject object = new JSONObject(data);
//                        String dial = object.getString("data");
//                        Intent intent = new Intent();
//                        intent.setAction(Intent.ACTION_DIAL);
//                        intent.setData(Uri.parse("tel:" + dial));
//                        startActivity(intent);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//
//        // 预览图片
//        mWebView.registerHandler("photoPreview", new BridgeHandler() {
//
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                if (data != null && !data.equals("null")) {
//                    try {
//                        JSONObject object = new JSONObject(data);
//                        JSONArray array = object.getJSONArray("data");
//                        ArrayList<String> str_list = new ArrayList<>();
//                        for (int i = 0; i < array.length(); i++) {
//                            String url = array.optString(i);
//                            str_list.add(url);
//                        }
//
//                        Intent intent = new Intent();
//                        intent.setClass(WebViewActivity.this, ImageFromWebView.class);
//                        intent.putStringArrayListExtra("urls", str_list);
//                        startActivity(intent);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//
//        // 保存图片
//        mWebView.registerHandler("savePhoto", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                Log.e("savePhoto", data);
//                if (data != null && !data.equals("null")) {
//                    try {
//                        JSONObject object = new JSONObject(data);
//                        String url = object.getString("data");
//                        ImageLoaderUtils.downLoadImage(url, WebViewActivity.this);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//
//        // 保存二维码到相册
//        mWebView.registerHandler("saveQrCode", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                Log.e("saveQrCode", data);
//                if (data != null && !data.equals("null")) {
//                    try {
//                        JSONObject object = new JSONObject(data);
//                        String url = object.getString("data");
//                        Bitmap bitmap = QRCodeUtil.GenorateImage(url);
//                        String fileName = System.currentTimeMillis() + ".png";
//                        ImageLoaderUtils.saveFileToSD(WebViewActivity.this, fileName, bitmap);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//
//        // 打开新的WebView
//        mWebView.registerHandler("openWebview", new BridgeHandler() {
//
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                if (data != null && !data.equals("null")) {
//                    try {
//                        JSONObject object = new JSONObject(data);
//                        String url = object.getString("url");
//                        NavUtils.startWebview(WebViewActivity.this, "", url, 0);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        });
//
//        // 支付宝支付
//        mWebView.registerHandler("aliPay", new BridgeHandler() {
//
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                if (data != null && !data.equals("null")) {
//                    try {
//                        JSONObject object = new JSONObject(data);
//                        final String payInfo = object.getString("payInfo");
//                        if (!TextUtils.isEmpty(payInfo)) {
//                            Runnable payRunnable = new Runnable() {
//                                @Override
//                                public void run() {
//                                    PayTask payTask = new PayTask(WebViewActivity.this);
//                                    String result = payTask.pay(payInfo, true);
//
//                                    Message msg = new Message();
//                                    msg.what = ALI_PAY_RESULT;
//                                    msg.obj = result;
//                                    mHandler.sendMessage(msg);
//                                }
//                            };
//                            // 必须异步调用
//                            Thread payThread = new Thread(payRunnable);
//                            payThread.start();
//
//                            callBackFunction = function;
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//
//        // 微信支付
//        mWebView.registerHandler("wxPay", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                if (data != null && !data.equals("null")) {
//                    try {
//                        if (!TextUtils.isEmpty(data)) {
//
//                            if (!MobileUtil.isWeixinAvilible(WebViewActivity.this)) {
//                                AndroidUtils.showToast(WebViewActivity.this, getString(R.string.wx_isinstall));
//                                JSONObject jsonObject = new JSONObject();
//                                try {
//                                    jsonObject.put("code", 0);
//                                    jsonObject.put("message", "OK");
//                                    JSONObject otherObject = new JSONObject();
//                                    otherObject.put("status", "uninstall");
//                                    jsonObject.put("data", otherObject.toString());
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                                function.onCallBack(jsonObject.toString());
//                                return;
//                            }
//
//                            JSONObject object = new JSONObject(data);
//                            String appid = object.getString("appid");
//                            String prepayid = object.getString("prepayid");
//                            String sign = object.getString("sign");
//                            String timestamp = object.getString("timestamp");
//                            String noncestr = object.getString("noncestr");
//                            String partnerid = object.getString("partnerid");
//                            String packageStr = object.getString("packageStr");
//                            api = WXAPIFactory.createWXAPI(WebViewActivity.this, appid);
//                            api.registerApp(appid);
//                            PayReq payReq = new PayReq();
//                            payReq.appId = appid;
//                            payReq.prepayId = prepayid;
//                            payReq.sign = sign;
//                            payReq.timeStamp = timestamp;
//                            payReq.nonceStr = noncestr;
//                            payReq.partnerId = partnerid;
//                            payReq.packageValue = packageStr;
//                            api.sendReq(payReq);
//                            callBackFunction = function;
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//
//        // 微信登录
//        mWebView.registerHandler("wxLogin", new BridgeHandler() {
//            @Override
//            public void handler(String data, final CallBackFunction function) {
//                if (!MobileUtil.isWeixinAvilible(WebViewActivity.this)) {
//                    AndroidUtils.showToast(WebViewActivity.this, getString(R.string.wx_isinstall));
//                    JSONObject jsonObject = new JSONObject();
//                    try {
//                        jsonObject.put("code", -1);
//                        jsonObject.put("message", "ERROR");
//                        jsonObject.put("data", "");
//                        JSONObject otherObject = new JSONObject();
//                        otherObject.put("openId", "");
//                        otherObject.put("status", "uninstall");
//                        jsonObject.put("data", otherObject.toString());
//                        function.onCallBack(jsonObject.toString());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    return;
//                }
//                UMShareAPI.get(WebViewActivity.this).doOauthVerify(WebViewActivity.this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
//                    @Override
//                    public void onStart(SHARE_MEDIA share_media) {
//
//                    }
//
//                    @Override
//                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
//                        String open_id = map.get("openid");
//                        try {
//                            JSONObject jsonObject = new JSONObject();
//                            jsonObject.put("code", 0);
//                            jsonObject.put("message", "OK");
//                            JSONObject otherObject = new JSONObject();
//                            otherObject.put("openId", open_id);
//                            jsonObject.put("data", otherObject.toString());
//                            function.onCallBack(jsonObject.toString());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
//                        try {
//                            JSONObject jsonObject = new JSONObject();
//                            jsonObject.put("code", -1);
//                            jsonObject.put("message", "ERROR");
//                            jsonObject.put("data", "");
//                            function.onCallBack(jsonObject.toString());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onCancel(SHARE_MEDIA share_media, int i) {
//                        try {
//                            JSONObject jsonObject = new JSONObject();
//                            jsonObject.put("code", -1);
//                            jsonObject.put("message", "CANCEL");
//                            jsonObject.put("data", "");
//                            function.onCallBack(jsonObject.toString());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        });
//
//        // 本地订单详情返回h5订单列表
//        mWebView.registerHandler("common.nativeGoBack", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                goBackFunction = function;
//            }
//        });
//
//        // 直接关闭webview
//        mWebView.registerHandler("closeWebview", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                finish();
//            }
//        });
//
//        // 商品详情分享
//        mWebView.registerHandler("showGoodsShare", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                MerchantItem merchantItem = new MerchantItem();
//                try {
//                    JSONObject object = new JSONObject(data);
//                    String pic = object.getString("pic");
//                    String goodsTitle = object.getString("goodsTitle");
//                    long goodsId = object.optLong("goodsId");
//                    long minPrice = object.optLong("goodsMinMarketPrice");
//                    long maxPrice = object.optLong("goodsMaxMarketPrice");
//                    long minPoint = object.optLong("minPointPrice");
//                    long maxPoint = object.optLong("maxPointPrice");
//
//                    ItemVO itemVO = new ItemVO();
////                    itemVO.mainPicUrl = pic;
//                    itemVO.title = goodsTitle;
//                    itemVO.id = goodsId;
//                    itemVO.maxPrice = maxPrice;
//                    itemVO.marketPrice = minPrice;
//
//                    List<String> picUrls = new ArrayList<>();
//                    picUrls.add(pic);
//                    itemVO.picUrls = picUrls;
//
//                    PayInfo payInfo = new PayInfo();
//                    payInfo.minPoint = minPoint;
//                    payInfo.maxPoint = maxPoint;
//
//                    itemVO.payInfo = payInfo;
//                    merchantItem.itemVO = itemVO;
//
//                } catch (JSONException e) {
//
//                }
//                NavUtils.gotoShareActivity(WebViewActivity.this, ShareActivity.SHARE_TYPE_POINT_PRODUCT, -1, merchantItem);
////                handleShare();
//            }
//        });
//
//        // 商品详情客服聊天
//        mWebView.registerHandler("goodsCustomService", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                try {
//                    JSONObject object = new JSONObject(data);
////                    String orderId = object.getString("orderId");
////                    String goodsId = object.getString("goodsId");
//                    String commodityType = object.getString("commodityType");
//                    long commodityId = object.optLong("commodityId");
//                    String imgUrl = object.getString("imgUrl");
//                    long goodsMinMarketPrice = object.optLong("goodsMinMarketPrice");
//                    long goodsMaxMarketPrice = object.optLong("goodsMaxMarketPrice");
//                    String title = object.getString("title");
//                    String description = object.getString("description");
//
//                    long uid = SPUtils.getServiceUID(WebViewActivity.this);
//
//                    ProductCardModel productCardModel = new ProductCardModel(1,
//                            commodityType,
//                            title,
//                            commodityId,
//                            imgUrl,
//                            description,
//                            goodsMinMarketPrice, goodsMaxMarketPrice);
//
//                    if (!getUserService().isLogin()) {
//                        NavUtils.gotoLoginActivity(WebViewActivity.this);
//                    } else {
//                        NavUtils.gotoMessageActivity(WebViewActivity.this, uid, productCardModel);
//                    }
//
//                } catch (JSONException e) {
//
//                }
//
//            }
//        });
//
//        // 订单详情客服聊天
//        mWebView.registerHandler("orderCustomService", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                try {
//                    JSONObject object = new JSONObject(data);
//                    long orderId = object.optLong("orderId");
//
//                    long uid = SPUtils.getServiceUID(WebViewActivity.this);
//
//                    NavUtils.gotoMessageActivity(WebViewActivity.this,
//                            uid,
//                            orderId);
//
//                } catch (JSONException e) {
//
//                }
//
//            }
//        });
//
//        // 设置工具栏样式
//        mWebView.registerHandler("setNavStyle", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                try {
//                    JSONObject object = new JSONObject(data);
//                    String navColor = object.getString("navColor");
//                    String navTextColor = object.getString("navTextColor");
//
//                    mNavView.setTitleColor(navTextColor);
//                    mNavView.setBackgroundColor(Color.parseColor(navColor));
//
//                } catch (JSONException e) {
//
//                }
//
//            }
//        });
//
//        // 拦截返回按钮
//        mWebView.registerHandler("hookBack", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                Log.e("hookBack", data);
//                if (data != null && !data.equals("null")) {
//                    try {
//                        JSONObject object = new JSONObject(data);
//                        eventName = object.getString("eventName");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//
//        // 取消拦截返回按钮
//        mWebView.registerHandler("cancelHookBack", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                Log.e("cancelHookBack", data);
//                eventName = "";
//            }
//        });
//
//        // 世界杯分享
//        mWebView.registerHandler("sharePicture", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                try {
//                    JSONObject object = new JSONObject(data);
//                    String shareurl = object.getString("url");
//                    //shareurl = "https://shadow.yingheying.com/crayfish/mirafra111111.png";
//                    ShareUtils.showShareBoard(WebViewActivity.this, shareurl);
//
//                } catch (JSONException e) {
//
//                }
//
//            }
//        });
//
//        // 上传培训小视频
//        mWebView.registerHandler("uploadMedia", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                try {
//                    JSONObject object = new JSONObject(data);
//                    videoId = object.getString("mediaId");
//                    changeCallback = object.getString("changeCallback");
//                    openCallback = object.getString("openCallback");
//                    String type = object.getString("mediaType");   //media/video
//                    showSelectVideo("media".equals(type) ? true : false);
//                    uploadFunction = function;
//                } catch (JSONException e) {
//
//                }
//
//            }
//        });
//
//        // 文章详情评论完
//        mWebView.registerHandler("articelFinishedComment", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                try {
//                    JSONObject object = new JSONObject(data);
//                    long ugcid = object.getLong("ugcid");
//                    EventBus.getDefault().post(new EvBusCommentChange(ugcid, true));
//                } catch (JSONException e) {
//
//                }
//
//            }
//        });
//
//        // 收起键盘
//        mWebView.registerHandler("hiddenKeyBoard", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                try {
//                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                    if (imm != null)
//                        imm.hideSoftInputFromInputMethod(mNavView.getWindowToken(), 0);
//                } catch (Exception e) {
//
//                }
//
//            }
//        });
//
//        // 选择城市
//        mWebView.registerHandler("selectCity", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                //  callback  {'cityCode':'''cityName':}
//                try {
//                    JSONObject object = new JSONObject(data);
//                    String eventName = object.getString("eventName");
//                    selectCityFunction = function;
//                    YhyRouter.getInstance().startCitySelectActivity(WebViewActivity.this, true);
////                    EventBus.getDefault().post(new EvBusCommentChange(ugcid, true));
//                } catch (JSONException e) {
//
//                }
//            }
//        });
//
//    }
//
//    @Override
//    public void onNewBackPressed() {
//        if (!TextUtils.isEmpty(eventName)) {
//            mWebView.callHandler(eventName, "", data -> {
//            });
//            eventName = "";
//        } else {
//            if (mWebView.canGoBack()) {
//                mWebView.goBack();//返回上一页面} else {
//            } else {
//                super.onNewBackPressed();
//            }
//        }
//
//    }
//
//    @Override
//    public void finish() {
//        ViewGroup view = (ViewGroup) getWindow().getDecorView();
//        view.removeAllViews();
//
//        if (manager != null) {
//            manager.stopListen();
//        }
//
//        super.finish();
//    }
//
//    /**
//     * 加载URL
//     */
//
//    private void loadCookieUrl() {
//        loadCookieUrl(false);
//    }
//
//    private void loadCookieUrl(boolean isCreate) {
//
//        if (SPUtils.isNeedUpdateWtk(this) && getUserService().isLogin()) {
//            mWebController.doGetWapLoginToken(WebViewActivity.this);
//            return;
//        }
//        showLoadingView("");
//        mWebView.setDefaultHandler(new DefaultHandler());
//        if (!TextUtils.isEmpty(mWebParams.getUrl())) {
//            if (mWebParams.getUrl().trim().startsWith("http://") || mWebParams.getUrl().trim().startsWith("https://")) {
//                if (mWebParams.isSyncCookie()) {
//                    syncCookie();
//                }
//                if (isCreate) {
//                    isLoadUrl = true;
//                    mWebView.loadUrl(mWebParams.getUrl());
//                } else {
//                    mWebView.reload();
//                }
//            } else if (mWebParams.getUrl().startsWith("file://")) {
//                mWebView.loadUrl(mWebParams.getUrl());
//            } else {
//                mWebView.loadData(mWebParams.getUrl(), "text/html; charset=UTF-8", null);
//            }
//        }
//    }
//
//    @Override
//    public View onLoadContentView() {
//        return View.inflate(this, R.layout.ac_web, null);
//    }
//
//    BaseNavView mNavView;
//
//    @Override
//    public View onLoadNavView() {
//        mWebParams = (WebParams) getIntent().getSerializableExtra(SPUtils.EXTRA_DATA);
//
//        //如果URL为空直接关闭页面
//        if (TextUtils.isEmpty(mWebParams.getUrl())) {
//            finish();
//            return null;
//        }
//        //判断是否为全屏展示
//        mNavView = new BaseNavView(this);
//        if (!TextUtils.isEmpty(mWebParams.getTitle())) {
//            mNavView.setTitleText(mWebParams.getTitle());
//        }
//        if (mWebParams.isShowShareButton()) {
//            mNavView.setRightImg(R.mipmap.icon_top_share_nobg);
//            mNavView.setRightImgClick(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ShareBean sb = new ShareBean();
//                    sb.shareWebPage = mWebParams.getUrl();
//                    sb.shareContent = mWebParams.getTitle();
//                    NavUtils.gotoShareTableActivity(WebViewActivity.this, sb, null);
//                }
//            });
//        }
//
//        mNavView.setLeftClick(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mWebParams.isNeedSetResult()) {
//                    setResult(RESULT_OK);
//                }
//                onBackPressed();
//            }
//        });
//
//        if (mWebParams.isShowCloseButton()) {
//            mNavView.setRightText(R.string.sm_title_bar_close_btn);
//            mNavView.setRightTextClick(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mWebParams.isNeedSetResult()) {
//                        setResult(RESULT_OK);
//                    }
//                    finish();
//                }
//            });
//        }
//        return mNavView;
//    }
//
//    @Override
//    public boolean isTopCover() {
//        if (mWebParams.isShowTitle()) {
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    protected void syncCookie() {
//        String didCookie = "_did=" + RequestHandlerKt.getDeviceId();
//        String dsigCookie = "_dsig=" + SPUtils.getDSig(this);
//
//        CookieManager cookieManager = CookieManager.getInstance();
//        CookieSyncManager.createInstance(this);
//        cookieManager.setAcceptCookie(true);
//        cookieManager.removeSessionCookie();
//        cookieManager.removeAllCookie();
//        if (userService.isLogin()) {
//            String wtkCookie = "_wtk=" + SPUtils.getWebUserToken(this);
//            String uidCookie = "_uid=" + userService.getLoginUserId();
//
//            cookieManager.setCookie(Constants.DEFAULT_DOMAIN, wtkCookie);
//            cookieManager.setCookie(Constants.DEFAULT_DOMAIN, uidCookie);
//        }
//        cookieManager.setCookie(Constants.DEFAULT_DOMAIN, didCookie);
//        cookieManager.setCookie(Constants.DEFAULT_DOMAIN, dsigCookie);
//
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            CookieSyncManager.getInstance().sync();
//        } else {
//            CookieManager.getInstance().flush();
//        }
//    }
//
//
//    @Override
//    public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {
//        mUploadMsg = uploadMsg;
//        showOptions();
//    }
//
//    @Override
//    public void showFileChooserCallBack(ValueCallback<Uri[]> filePathCallback) {
//        mUploadMsg5Plus = filePathCallback;
//        showOptions();
//    }
//
//    public void showOptions() {
//        if (mCameraPop.isShowing()) return;
//        mCameraPop.showMenu(mWebView);
//
//    }
//
//    @Override
//    public void onSelectedAsy(String imgPath) {
//        file_path = imgPath;
//        UpImagefromCut(imgPath);
//
//        Uri result = null;
//        if (hasFile(file_path)) {
//            result = Uri.fromFile(new File(file_path));
//        }
//
//        if (mUploadMsg5Plus != null) {
//            mUploadMsg5Plus.onReceiveValue(new Uri[]{result});
//            mUploadMsg5Plus = null;
//        } else if (mUploadMsg != null) {
//            mUploadMsg.onReceiveValue(result);
//            mUploadMsg = null;
//        }
//    }
//
//    @Override
//    public void onCamreaClick(CameraOptions options) {
//        if (LocalUtils.isAlertMaxStorage()) {
//            AndroidUtils.showToast(this, getString(R.string.label_toast_sdcard_unavailable));
//            if (mUploadMsg5Plus != null) {
//                mUploadMsg5Plus.onReceiveValue(null);
//                mUploadMsg5Plus = null;
//            }
//            if (mUploadMsg != null) {
//                mUploadMsg.onReceiveValue(null);
//                mUploadMsg = null;
//            }
//            return;
//        }
//        if (!checkPermission()) {
//            // 没有权限
//            if (mPermissionDialog == null) {
//                mPermissionDialog = DialogUtil.showMessageDialog(this, null,
//                        "打开相机失败！请在\"设置\"中确认是否授权使用相机", "确认", null,
//                        new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                mPermissionDialog.dismiss();
//                            }
//                        }, null);
//            }
//            mPermissionDialog.show();
//            if (mUploadMsg5Plus != null) {
//                mUploadMsg5Plus.onReceiveValue(null);
//                mUploadMsg5Plus = null;
//            }
//            if (mUploadMsg != null) {
//                mUploadMsg.onReceiveValue(null);
//                mUploadMsg = null;
//            }
//            return;
//        }
//
////        options.setOpenType(OpenType.OPEN_CAMERA_CROP);
//        options.setOpenType(OpenType.OPEN_CAMERA);
//        /*.setCropBuilder(new CropBuilder(1, 1, 750, 420));*/
//        managerProcess();
//    }
//
//    @Override
//    public void onPickClick(CameraOptions options) {
//
////        options.setOpenType(OpenType.OPEN_GALLERY_CROP);
//        options.setOpenType(OpenType.OPEN_GALLERY);
//        /*.setCropBuilder(new CropBuilder(1, 1, 750, 420));*/
//
//        managerProcess();
//    }
//
//    @Override
//    public void onDelClick() {
//
//    }
//
//    @Override
//    public void onVideoClick() {
//
//    }
//
//    private void managerProcess() {
//        mCameraPop.process();
//        if (null != mCameraPop) {
//            mCameraPop.dismiss();
//        }
//    }
//
//    protected void UpImagefromCut(String data) {
//        if (data == null) {
//            return;
//        }
//        if (data != null && data.length() > 0) {
//        }
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CameraManager.OPEN_USER_ALBUM && resultCode == CameraManager.GET_VIDEO) {
//            // 上传小视频
//            MediaItem mVideo = data.getParcelableExtra(SPUtils.EXTRA_VIDEO);
//            if (mVideo == null || TextUtils.isEmpty(mVideo.getMediaPath())) {
//                upLoadChangeCallBack("", "video");
//                return;
//            }
//            upLoadChangeCallBack(mVideo.getMediaPath(), "video");
//            AndroidUtils.showToast(WebViewActivity.this, getString(R.string.start_upload_video));
//            Intent intentService = new Intent(WebViewActivity.this, UpLoadCourseVideoService.class);
//            intentService.putExtra(MEDIA_PATH, mVideo.getMediaPath());
//            intentService.putExtra(VIDEO_ID, videoId);
//            intentService.putExtra(TYPE, 0);
//            startService(intentService);
//        }
//
//        if (requestCode == CameraManager.OPEN_USER_ALBUM && resultCode == -1) {
//            // 上传照片
//            mCameraHandler.forResult(requestCode, resultCode, data);
//        }
//
//        if (mCameraPop != null) {
//            mCameraPop.forResult(requestCode, resultCode, data);
//        }
//        if (null == mUploadMsg && null == mUploadMsg5Plus) return;
//        if (resultCode != RESULT_OK) {//同上所说需要回调onReceiveValue方法防止下次无法响应js方法
//
//            if (mUploadMsg5Plus != null) {
//                mUploadMsg5Plus.onReceiveValue(null);
//                mUploadMsg5Plus = null;
//            }
//            if (mUploadMsg != null) {
//                mUploadMsg.onReceiveValue(null);
//                mUploadMsg = null;
//            }
//
//            return;
//        } else {
//            Uri result = null;
//            if (requestCode == CameraManager.OPEN_CAMERA_CODE) {
//                if (null != data && null != data.getData()) {
//                    result = data.getData();
//                }
//
//                if (data == null && hasFile(file_path)) {
//                    result = Uri.fromFile(new File(file_path));
//                }
//
//                if (data == null) {
//                    return;
//                }
//                if (mUploadMsg5Plus != null) {
//                    mUploadMsg5Plus.onReceiveValue(new Uri[]{result});
//                    mUploadMsg5Plus = null;
//                } else if (mUploadMsg != null) {
//                    mUploadMsg.onReceiveValue(result);
//                    mUploadMsg = null;
//                }
//            }
//        }
//    }
//
//    private void upLoadChangeCallBack(String name, String type) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("code", TextUtils.isEmpty(name) ? 1 : 0);
//            jsonObject.put("msg", TextUtils.isEmpty(name) ? "失败" : "成功");
//            JSONObject otherObject = new JSONObject();
//            otherObject.put("name", TextUtils.isEmpty(name) ? "地址为空" : name);
//            otherObject.put("type", type);
//            jsonObject.put("data", otherObject.toString());
//            mWebView.callHandler(changeCallback, jsonObject.toString(), new CallBackFunction() {
//                @Override
//                public void onCallBack(String data) {
//
//                }
//            });
//        } catch (JSONException e) {
//
//        }
//    }
//
//    public static boolean hasFile(String path) {
//        try {
//            File f = new File(path);
//            if (!f.exists()) {
//                return false;
//            }
//        } catch (Exception e) {
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * 上传身份证
//     *
//     * @param file
//     */
//    protected void UploadImage(final String file) {
//        List<String> files = new ArrayList<String>();
//        files.add(file);
//        showLoadingView("身份证上传中");
//        NetManager.getInstance(this).doUploadImages(files,
//                new OnResponseListener<List<String>>() {
//                    @Override
//                    public void onInternError(int errorCode, String errorMessage) {
//                        hideLoadingView();
//                        AndroidUtils.showToast(WebViewActivity.this, errorMessage);
//                    }
//
//                    @Override
//                    public void onComplete(boolean isOK, List<String> result, int errorCode, String errorMsg) {
//                        hideLoadingView();
//                        if (isOK) {
//
//                        } else {
//                            AndroidUtils.showToast(WebViewActivity.this, errorMsg);
//                        }
//                    }
//                });
//    }
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private void onActivityResultAboveL(Intent intent) {
//        Uri[] results = null;
//        if (intent != null) {
//            String dataString = intent.getDataString();
//            ClipData clipData = intent.getClipData();
//            if (clipData != null) {
//                results = new Uri[clipData.getItemCount()];
//                for (int i = 0; i < clipData.getItemCount(); i++) {
//                    ClipData.Item item = clipData.getItemAt(i);
//                    results[i] = item.getUri();
//                }
//            }
//            if (dataString != null)
//                results = new Uri[]{Uri.parse(dataString)};
//        }
//        mUploadMsg5Plus.onReceiveValue(results);
//        mUploadMsg5Plus = null;
//    }
//
//    /**
//     * 用户登录退出通知
//     *
//     * @param state
//     */
//    public void onEvent(EvBusUserLoginState state) {
//        loadCookieUrl();
//        if (mCameraPop != null && mCameraPop.isShowing())
//            mCameraPop.dismiss();
//    }
//
//    /**
//     * Web用户登录退出通知
//     *
//     * @param state
//     */
//    public void onEvent(EvBusWebUserLoginState state) {
//        loadCookieUrl();
//    }
//
//    // js通信接口
//    public class JavascriptInterface {
//
//        private Context context;
//
//        public JavascriptInterface(Context context) {
//            this.context = context;
//        }
//
//        public void openImage(String[] img, int pos) {
//            if (img == null) {
//                return;
//            }
//            ArrayList<String> pics = new ArrayList<String>();
//            for (String str : img) {
//                pics.add(str);
//            }
//            HarwkinLogUtil.info(pics.toArray().toString());
//            NavUtils.gotoLookBigImage(context, pics, pos);
//        }
//    }
//
//    class JsWebClient extends BridgeWebViewClient {
//        public JsWebClient(BridgeWebView webView) {
//            super(webView);
//        }
//
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            if (NativeUtils.parseUrl(url)) {
//                Message.obtain(mHandler, NativeUtils.MSG_SCHEME_URL, url).sendToTarget();
//                return true;
//            }
//            // 判断是否是邮箱
//            if (isMaile(url)) {
//                // 调起邮箱
//                try {
//
//                    Uri uri = Uri.parse(url);
//                    String[] email = {url.replace("mailto:", "")};
//                    Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
//                    intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
//                    intent.putExtra(Intent.EXTRA_SUBJECT, ""); // 主题
//                    intent.putExtra(Intent.EXTRA_TEXT, ""); // 正文
//                    startActivity(Intent.createChooser(intent, ""));
//                } catch (Exception e) {
//
//                }
//
//                return true;
//            }
//            return super.shouldOverrideUrlLoading(view, url);
//        }
//
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            super.onPageFinished(view, url);
//            hideLoadingView();
//        }
//
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            view.getSettings().setJavaScriptEnabled(true);
//            super.onPageStarted(view, url, favicon);
//        }
//
//        @Override
//        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//            super.onReceivedError(view, errorCode, description, failingUrl);
//            isErrorPage = true;
//            if (mNavView != null && StringUtil.isEmpty(mWebParams.getTitle())) {
//                mNavView.setTitleText("");
//            }
////            Message.obtain(mHandler, MSG_SHOW_ERROR_PAGE).sendToTarget();
//            Log.e("onReceivedError1", description);
//        }
//
//        @Override
//        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//            super.onReceivedError(view, request, error);
//            isErrorPage = true;
//            if (mNavView != null && TextUtils.isEmpty(mWebParams.getTitle())) {
//                mNavView.setTitleText("");
//            }
////            Message.obtain(mHandler, MSG_SHOW_ERROR_PAGE).sendToTarget();
//            Log.e("onReceivedError2", error.toString());
//
//        }
//
//        @Override
//        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
////                super.onReceivedSslError(view, handler, error);
//            //TODO 处理Https的请求
//            handler.proceed();
//        }
//    }
//
//    private boolean isMaile(String url) {
//        if (!TextUtils.isEmpty(url)) {
//            if (url.startsWith("mailto:")) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//
//    public class ReWebChomeClient extends WebChromeClient {
//
//
//        private OpenFileChooserCallBack mOpenFileChooserCallBack;
//
//
//        public ReWebChomeClient(OpenFileChooserCallBack openFileChooserCallBack) {
//            mOpenFileChooserCallBack = openFileChooserCallBack;
//        }
//
//
//        //For Android 3.0+
//        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
//            mOpenFileChooserCallBack.openFileChooserCallBack(uploadMsg, acceptType);
//        }
//
//
//        // For Android < 3.0
//        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//            openFileChooser(uploadMsg, "");
//        }
//
//
//        // For Android  > 4.1.1
//        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
//            openFileChooser(uploadMsg, acceptType);
//        }
//
//
//        // For Android 5.0+
//        @Override
//        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams
//                fileChooserParams) {
//            mOpenFileChooserCallBack.showFileChooserCallBack(filePathCallback);
//            return true;
//        }
//
//        @Override
//        public void onReceivedTitle(WebView view, String title) {
//            if (mWebParams.isShowTitle()) {
//                if (isErrorPage) {
//                    return;
//                }
//            }
//
//            if (null != title && !title.contains("http")) {
//                if (mNavView != null) {
//                    Log.e("onReceivedTitle", title);
//                    mNavView.setTitleText(title);
//                }
//            }
//        }
//
//        /**
//         * 覆盖默认的window.alert展示界面、避免title里显示为“：来自file:////”
//         */
//        @Override
//        public boolean onJsAlert(WebView view, String url, String message,
//                                 JsResult result) {
//            Dialog dialog = DialogUtil.showMessageDialog(view.getContext(),
//                    null,
//                    message,
//                    getString(R.string.label_btn_ok),
//                    null,
//                    null,
//                    null);
//
//            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                    return true;
//                }
//            });
//            // 禁止响应按back键的事件
//            dialog.setCancelable(false);
//            dialog.show();
//            result.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
//            return true;
//        }
//
//        /**
//         * 覆盖默认的window.confirm展示界面，避免title里显示为“：来自file:////”
//         */
//        @Override
//        public boolean onJsConfirm(WebView view, String url, String message,
//                                   final JsResult result) {
//
//            Dialog dialog = DialogUtil.showMessageDialog(view.getContext(),
//                    null,
//                    message,
//                    getString(R.string.label_btn_ok),
//                    getString(R.string.cancel),
//                    new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            result.confirm();
//                        }
//                    },
//                    new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            result.cancel();
//
//                        }
//                    });
//
//            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                    return true;
//                }
//            });
//            // 禁止响应按back键的事件
//            dialog.setCancelable(false);
//            dialog.show();
//            result.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
//
//            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                    result.cancel();
//                }
//            });
//
//            // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
//            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//                @Override
//                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                    return true;
//                }
//            });
//            return true;
//        }
//    }
//
//    public void onEventMainThread(WeiXinPayResult result) {
//        BaseResp resp = result.getResp();
//        //微信支付处理code
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("code", 0);
//            jsonObject.put("message", "OK");
//            JSONObject otherObject = new JSONObject();
//            otherObject.put("status", resp.errCode);
//            jsonObject.put("data", otherObject.toString());
//            callBackFunction.onCallBack(jsonObject.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        try {
//            if (isResume)
//                mWebView.callHandler("common.nativeGoBack", "", new CallBackFunction() {
//                    @Override
//                    public void onCallBack(String data) {
//                    }
//                });
//        } catch (Exception e) {
//
//        }
//
//        if (!isResume) {
//            isResume = true;
//        }
//
//    }
//
//    /**
//     * 上传小视频选择
//     */
//    CameraHandler mCameraHandler = new CameraHandler(this, new SelectMoreListener() {
//        @Override
//        public void onSelectedMoreListener(List<MediaItem> pathList) {
//            MediaItem mVideo = null;
//            if (pathList != null && pathList.size() > 0)
//                mVideo = pathList.get(0);
//            if (mVideo == null || TextUtils.isEmpty(mVideo.getMediaPath())) {
//                upLoadChangeCallBack("", "image");
//                return;
//            }
//            upLoadChangeCallBack(mVideo.getMediaPath(), "image");
//            AndroidUtils.showToast(WebViewActivity.this, getString(R.string.start_upload_pic));
//            Intent intentService = new Intent(WebViewActivity.this, UpLoadCourseVideoService.class);
//            intentService.putExtra(MEDIA_PATH, mVideo.getMediaPath());
//            intentService.putExtra(VIDEO_ID, videoId);
//            intentService.putExtra(TYPE, 1);
//            startService(intentService);
//        }
//    });
//
//    public void showSelectVideo(boolean showAll) {
//        try {
//            CameraOptions options = mCameraHandler.getOptions();
//            options.setOpenType(OpenType.OPENN_USER_ALBUM);
//            options.setMaxSelect(1);
//            if (showAll) {
//                mCameraHandler.processWithMedia();
//            } else {
//                mCameraHandler.processWithVideo();
//            }
//            mNavView.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mWebView.callHandler(openCallback, "", new CallBackFunction() {
//                        @Override
//                        public void onCallBack(String data) {
//
//                        }
//                    });
//                }
//            }, 1000);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 上传培训视频或图片成功通知
//     */
//    public void onEvent(EvBusUpCourseVideo upCourseVideo) {
//        if (upCourseVideo.success) {
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("code", 0);
//                jsonObject.put("msg", "成功");
//                JSONObject otherObject = new JSONObject();
//                otherObject.put("resource", upCourseVideo.url);
//                jsonObject.put("data", otherObject.toString());
//                uploadFunction.onCallBack(jsonObject.toString());
//            } catch (JSONException e) {
//
//            }
//        }
//    }
//
//    /**
//     * 城市选择完         callback  {'cityCode':'''cityName':}
//     */
//    public void onEvent(EvBusLocationChange evBusLocationChange) {
//        if (evBusLocationChange.isFromWeb) {
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("cityCode", evBusLocationChange.outPlaceCity.cityCode);
//                jsonObject.put("cityName", evBusLocationChange.outPlaceCity.name);
//                selectCityFunction.onCallBack(jsonObject.toString());
//            } catch (JSONException e) {
//
//            }
//        }
//    }
//
//    /**
//     * 选择图片异常后释放资源  否则第二次调不起来
//     *
//     * @param evBusGalleryError
//     */
//    public void onEvent(EvBusGalleryError evBusGalleryError) {
//        if (mUploadMsg != null) {
//            mUploadMsg.onReceiveValue(null);
//            mUploadMsg = null;
//        }
//        if (mUploadMsg5Plus != null) {
//            mUploadMsg5Plus.onReceiveValue(null);
//            mUploadMsg5Plus = null;
//        }
//    }
//
//    private boolean checkPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            int result = PermissionChecker.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
////            int result1 = PermissionChecker.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
////            int result2 = PermissionChecker.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//            return result == PackageManager.PERMISSION_GRANTED
////                    && result1 == PackageManager.PERMISSION_GRANTED
////                    && result2 == PackageManager.PERMISSION_GRANTED
//                    ;
//        } else {
//            int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
////            int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
////            int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//            return result == PackageManager.PERMISSION_GRANTED
////                    && result1 == PackageManager.PERMISSION_GRANTED
////                    && result2 == PackageManager.PERMISSION_GRANTED
//                    ;
//        }
//    }
//}