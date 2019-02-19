package com.quanyan.yhy.ui.line.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.quanyan.base.BaseFragment;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.HarwkinLogUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.CommonUrl;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.WebViewActivity;
import com.quanyan.yhy.ui.line.lineinterface.IUpdateTab;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/3/1
 * Time:09:17
 * Version 1.0
 */
public class CommodityWebInfoFragment extends BaseFragment implements IUpdateTab {

    private WebView mWebView;
    private long mLineId;
    private String mItemType;
    public static CommodityWebInfoFragment getInstance(long lineId, String type){
        CommodityWebInfoFragment fragment = new CommodityWebInfoFragment();
        Bundle bundle = new Bundle();
        if(lineId  > 0) {
            bundle.putLong(SPUtils.EXTRA_ID, lineId);
        }
        if(!StringUtil.isEmpty(type)) {
            bundle.putString(SPUtils.EXTRA_TYPE, type);
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(getActivity(), R.layout.layout_commodity_webview, null);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            if(bundle.containsKey(SPUtils.EXTRA_ID)) {
                mLineId = bundle.getLong(SPUtils.EXTRA_ID);
            }
            if(bundle.containsKey(SPUtils.EXTRA_TYPE)) {
                mItemType = bundle.getString(SPUtils.EXTRA_TYPE);
            }
        }
        initView(getView());
    }

    private void initView(View view){
//        if(ItemType.SCENIC.equals(mItemType)){
//            view.findViewById(R.id.commodity_bottomview).setVisibility(View.GONE);
//        }else if(ItemType.MASTER_CONSULT_PRODUCTS.equals(mItemType)){
//            ((TextView) view.findViewById(R.id.dash_textview)).setText(R.string.tv_master_consult_detail_preview);
//        }
//        mWebView = (WebView) view.findViewById(R.id.commodity_detail_webview);
        mWebView = (WebView) view.findViewById(R.id.id_stickynavlayout_innerscrollview);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setAllowFileAccess(false);
        settings.setDomStorageEnabled(false);
        settings.setUseWideViewPort(true);
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(false);
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.HONEYCOMB) {
            settings.setDisplayZoomControls(false);
        }
        // 不使用缓存，只从网络获取数据
//        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 添加js交互接口类，并起别名 imagelistner
        mWebView.addJavascriptInterface(new JavascriptInterface(getActivity()), "imagelistner");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                mHandler.sendEmptyMessageDelayed(MSG_HOOK_IMAGE_CLICK,100);
                hideLoadingView();
                hideErrorView(null);
//                if(getView() != null) {
//                    View view1 = getView().findViewById(R.id.commodity_bottomview);
//                    if (view1 != null) {
//                        if(!ItemType.SCENIC.equals(mItemType)){
//                            view1.setVisibility(View.VISIBLE);
//                        }
//                    }
//                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                view.getSettings().setJavaScriptEnabled(true);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                super.onReceivedError(view, errorCode, description, failingUrl);
                mHandler.sendEmptyMessageDelayed(WebViewActivity.MSG_SHOW_ERROR_PAGE,100);
            }
        });
        HarwkinLogUtil.info("Harwkin","Url = " + CommonUrl.getItemDetailUrlSuffix(getActivity(), mItemType) + mLineId);
//        if(AppDebug.DEBUG_LOG && ItemType.MASTER_CONSULT_PRODUCTS.equals(mItemType)){
//            mWebView.loadUrl("http://m2.test.yingheying.com/view/H5pack/content.html?outId=109141");
//        }
        if(mLineId > 0 && CommonUrl.getItemDetailUrlSuffix(getActivity(), mItemType) != null){
            mWebView.loadUrl(CommonUrl.getItemDetailUrlSuffix(getActivity(), mItemType) + mLineId);
        }
    }
    /**
     * 处理网络返回消息
     * @param msg
     */
    @Override
    public void handleMessage(Message msg) {
        if(getActivity() == null){
            return;
        }
        hideLoadingView();
        hideErrorView(null);
        switch (msg.what) {
            case WebViewActivity.MSG_SHOW_ERROR_PAGE:
                showErrorView(null, IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
                    @Override
                    public void onClick(View view) {
                        mWebView.loadUrl(CommonUrl.getItemDetailUrlSuffix(getActivity(), mItemType) + mLineId);
                        showLoadingView(getResources().getString(R.string.scenic_loading_notice));
                    }
                });
                break;
//            case MSG_HOOK_IMAGE_CLICK:
//                // html加载完成之后，添加监听图片的点击js函数
////                addImageClickListner();
//                HarwkinLogUtil.info("addImageClickListner");
//                break;
        }
    }

    @Override
    public void updateTabContent() {
        if(mLineId > 0 && CommonUrl.getItemDetailUrlSuffix(getActivity(), mItemType) != null && mWebView != null){
            mWebView.loadUrl(CommonUrl.getItemDetailUrlSuffix(getActivity(), mItemType) + mLineId);
        }
    }

//    private final static int MSG_HOOK_IMAGE_CLICK = 0x2200;
    // 注入js函数监听
//    private void addImageClickListner() {
//        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
//        mWebView.loadUrl("javascript:(function(){" +
//                "var objs = document.getElementsByTagName(\"img\"); " +
//                "for(var i=0;i<objs.length;i++)  " +
//                "{"
//                + "    objs[i].onclick=function()  " +
//                "    {  "
//                + "        window.imagelistner.openImage(this.src);  " +
//                "    }  " +
//                "}" +
//                "})()");
//    }

    // js通信接口
    public class JavascriptInterface {

        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

//        public void openImage(String img) {
//            if(StringUtil.isEmpty(img)){
//                return ;
//            }
//            HarwkinLogUtil.info(img);
//            ArrayList<String> pics = new ArrayList<String>();
//            pics.add(img);
//            NavUtils.gotoLookBigImage(context, pics, 1);
//        }

        public void openImage(String[] img,int pos) {
            if(img == null){
                return ;
            }
            ArrayList<String> pics = new ArrayList<String>();
            for(String str:img){
                pics.add(str);
            }
            HarwkinLogUtil.info(pics.toArray().toString());
            NavUtils.gotoLookBigImage(context, pics, pos);
        }
    }
}
