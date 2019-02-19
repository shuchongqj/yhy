package com.quanyan.yhy.ui.nineclub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.harwkin.nb.camera.ImageUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.CommonUrl;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.WebViewActivity;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.nineclub.bean.EatGreatFacilityBean;
import com.quanyan.yhy.ui.nineclub.controller.BuyMustController;
import com.quanyan.yhy.view.HomeMenu_GridView;
import com.quanyan.yhy.view.JustifyTextView;
import com.yhy.common.beans.net.model.common.ComIconInfo;
import com.yhy.common.beans.net.model.common.ComIconList;
import com.yhy.common.beans.net.model.master.MasterCertificates;
import com.yhy.common.beans.net.model.master.Merchant;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:EatGreatDetailActivity
 * Description:美食详情界面
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-3-14
 * Time:9:37
 * Version 1.0
 */
public class EatGreatDetailActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.iv_eat_product_bg)
    private ImageView mIVProductbg;//店铺头背景图片

    @ViewInject(R.id.tv_eat_product_name)
    private JustifyTextView mTVProductName;//店铺头名称

    @ViewInject(R.id.tv_eat_product_address)
    private TextView mTVProductAddress;//店铺头地址

    @ViewInject(R.id.tv_eat_product_price)
    private TextView mTVProductPrice;//店铺头价格

    @ViewInject(R.id.tv_address)
    private JustifyTextView mTVAddress;//店铺详细地址

    @ViewInject(R.id.rl_address_content)
    private RelativeLayout mRLAddress;

    @ViewInject(R.id.rl_phone_content)
    private RelativeLayout mRLPhone;

    @ViewInject(R.id.tv_phone)
    private TextView mTVPhone;//店铺电话

    @ViewInject(R.id.hg_shop_detail)
    private HomeMenu_GridView mHGView;//店铺设施

    @ViewInject(R.id.tv_time)
    private TextView mTVTime;//营业时间

    @ViewInject(R.id.wb_picture_txt_intro)
    private WebView mWebView;//美食攻略

    private BuyMustController mController;

    private long eatId;
    private QuickAdapter<EatGreatFacilityBean> mAdapter;
    private String mServiceTel;

    private double mLatitude;//纬度信息
    private double mLongitude;//精度信息
    private int mCertificateType;
    private List<ComIconInfo> mAllIconList;

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_eatgreat_detail, null);
    }

    private BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);

        mController = new BuyMustController(this, mHandler);
        eatId = getIntent().getLongExtra(SPUtils.EXTRA_ID, -1);

        //初始化设施列表
        initDetailGridView();
        //点击事件
        initEvent();
        //访问网络
        doNetDatas();
        //初始化图文介绍
        initWebData();
    }

    private void initEvent() {
        mRLAddress.setOnClickListener(this);
        mRLPhone.setOnClickListener(this);

    }

    private void initDetailGridView() {
        mAdapter = new QuickAdapter<EatGreatFacilityBean>(this, R.layout.item_eatdetail, new ArrayList<EatGreatFacilityBean>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, EatGreatFacilityBean item) {
                helper.setImageUrl(R.id.iv_icon, ImageUtils.getImageFullUrl(item.getIconUrl()), 50, 50, R.mipmap.icon_default_128_128);
                helper.setText(R.id.tv_text, item.getName());
            }
        };
        mHGView.setAdapter(mAdapter);
    }

    public static void gotoEatGreatDetailActivity(Context context, long id) {
        Intent intent = new Intent(context, EatGreatDetailActivity.class);
        intent.putExtra(SPUtils.EXTRA_ID, id);
        context.startActivity(intent);
    }

    private void doNetDatas() {
        try {
            mAllIconList = ComIconList.deserialize(SPUtils.getComIcons(this)).comIconInfoList;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (-1 != eatId) {
            mController.doGetEatGreatDetail(EatGreatDetailActivity.this, eatId);
        } else {
            ToastUtil.showToast(this, getString(R.string.error_params));
        }

    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        //hideLoadingView();
        hideErrorView(null);
        switch (msg.what) {
            case ValueConstants.MSG_EATGREAT_DETAIL_OK:
                Merchant merchant = (Merchant) msg.obj;
                //处理数据
                if (merchant != null) {
                    handDatas(merchant);
                }
                break;
            case ValueConstants.MSG_EATGREAT_DETAIL_KO:
                showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                        IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", getString(R.string.error_view_network_loaderror_content), "", new ErrorViewClick() {
                    @Override
                    public void onClick(View view) {
                        doNetDatas();
                    }
                });

                break;
            case WebViewActivity.MSG_SHOW_ERROR_PAGE:
                showErrorView(null, IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
                    @Override
                    public void onClick(View view) {
                        mWebView.loadUrl(CommonUrl.getItemDetailUrlSuffix(EatGreatDetailActivity.this, ItemType.KEY_FOOD) + eatId);
                        showLoadingView(getResources().getString(R.string.scenic_loading_notice));
                    }
                });
        }
    }

    /**
     * 处理美食详情
     */
    private void handDatas(Merchant merchant) {
        //美食店经纬度处理
        mLatitude = merchant.latitude;
        mLongitude = merchant.longitude;
        mCertificateType = merchant.certificateType;
        //美食店铺图片
        if (!StringUtil.isEmpty(merchant.icon)) {
//            BaseImgView.frescoLoadimg(mIVProductbg, merchant.icon, R.mipmap.icon_default_150_150, R.mipmap.icon_default_150_150,
//                    R.mipmap.icon_default_150_150, ImageScaleType.EXACTLY, 300, 300, 0);

            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(merchant.icon), R.mipmap.icon_default_150_150, 300, 300, mIVProductbg);
        }

        //美食店名称
        if (!StringUtil.isEmpty(merchant.name)) {
            mTVProductName.setText(merchant.name.trim());
        }

        //美食店城市
        if (!StringUtil.isEmpty(merchant.city)) {
            mTVProductAddress.setText(merchant.city);
        }

        //美食店价格
        mTVProductPrice.setText(String.format(getString(R.string.eat_label_price), StringUtil.converRMb2YunNoFlag(merchant.avgprice)));

        //美食店地址
        if (!StringUtil.isEmpty(merchant.address)) {
            mTVAddress.setText(merchant.address);
        }

        //美食店电话
        mServiceTel = merchant.serviceTel;
        if (!StringUtil.isEmpty(mServiceTel)) {
            mTVPhone.setText(mServiceTel);
        }

        //美食店营业时间
        if (!StringUtil.isEmpty(merchant.serviceTime)) {
            mTVTime.setText(merchant.serviceTime);
        }

        //美食店设施
        handleFacility(merchant.certificates);

    }

    public String getIconUrl(int code, int type) {
        if (mAllIconList == null) {
            return null;
        }
        for (ComIconInfo bean : mAllIconList) {
            if (bean.code == code && bean.type == type) {
                return bean.icon;
            }
        }
        return null;
    }

    private void handleFacility(List<MasterCertificates> certificate) {

        List<EatGreatFacilityBean> facilityBeans = new ArrayList<>();

        if (certificate != null && certificate.size() > 0) {
            //转化成facilityBean对象集合
            for (int i = 0; i < certificate.size(); i++) {
                int certificateId = certificate.get(i).id;
                String certificateName = certificate.get(i).name;
                EatGreatFacilityBean facilityBean = new EatGreatFacilityBean();
                String iconUrl = getIconUrl(certificateId, mCertificateType);
                if (!StringUtil.isEmpty(iconUrl)) {
                    facilityBean.setIconUrl(iconUrl);
                }
                if (!StringUtil.isEmpty(certificateName)) {
                    facilityBean.setName(certificateName);
                }
                facilityBeans.add(facilityBean);
            }
            //刷新adapter
            if (facilityBeans.size() > 0) {
                mAdapter.addAll(facilityBeans);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_address_content:
                if (mLatitude != 0.0 && mLongitude != 0.0) {
                    jumpToBigMap();
                }
                break;
            case R.id.rl_phone_content:

                if (!StringUtil.isEmpty(mServiceTel)) {
                    LocalUtils.call(EatGreatDetailActivity.this, mServiceTel);
                }
                break;
        }
    }

    /**
     * 跳转到查看大地图页面
     */
    private void jumpToBigMap() {
        NavUtils.gotoViewBigMapView(EatGreatDetailActivity.this, mTVProductName.getText().toString(),mLatitude, mLongitude, false, mTVAddress.getText().toString());
    }

    /*****************************WebView Settings Start***********************************/
    /**
     * @Description 加载数据
     */
    private void initWebData() {
        mWebView = (WebView)findViewById(R.id.wb_picture_txt_intro);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setPluginState(WebSettings.PluginState.ON);
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
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideLoadingView();
                hideErrorView(null);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                mHandler.sendEmptyMessageDelayed(WebViewActivity.MSG_SHOW_ERROR_PAGE,100);
            }
        });
        if(eatId > 0 && CommonUrl.getItemDetailUrlSuffix(this, ItemType.KEY_FOOD) != null){
            mWebView.loadUrl(CommonUrl.getItemDetailUrlSuffix(this, ItemType.KEY_FOOD) + eatId);
        }
    }
}
