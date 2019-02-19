package com.quanyan.yhy.ui.shop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harwkin.nb.camera.ImageUtils;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.CommonUrl;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.common.SysConfigType;
import com.quanyan.yhy.ui.base.utils.NativeUtils;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.util.QRCodeUtil;
import com.yhy.common.beans.net.model.ShareBean;
import com.yhy.common.beans.net.model.master.Merchant;
import com.yhy.common.beans.net.model.user.NativeDataBean;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.LogUtils;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;


/**
 * Created with Android Studio.
 * Title:ShopSelfCardActivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-23
 * Time:16:04
 * Version 1.1.0
 */

public class ShopSelfCardActivity extends BaseActivity {

    private BaseNavView mBaseNavView;
    private ImageView mIVShopBg;//背景图
    private ImageView mIVShopHeadIcon;//头像
    private TextView mTVShopName;//店铺名称
    private TextView mTVLinkValue;//链接地址
    private ImageView mIVTwoCode;//二维码
    private Merchant mSellerInfo;
    private String mShareUrlSuffix;
    private TextView mTVDesc;


    public static void gotoShopSelfCardActivity(Context context, Merchant serllerInfo) {
        Intent intent = new Intent(context, ShopSelfCardActivity.class);
        intent.putExtra(SPUtils.EXTRA_DATA, serllerInfo);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mSellerInfo = (Merchant) getIntent().getSerializableExtra(SPUtils.EXTRA_DATA);
        finId();
        initData();
        initClick();
    }

    private void initClick() {
        //分享点击
        mBaseNavView.setRightImgClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分享点击
                shareDataEncape();
            }
        });
    }

    private void shareDataEncape() {
        ShareBean shareBean = new ShareBean();
        if (!StringUtil.isEmpty(mShareUrlSuffix)) {
            shareBean.shareWebPage = mShareUrlSuffix + mSellerInfo.sellerId;

        }

        if (!StringUtil.isEmpty(mSellerInfo.name)) {
            shareBean.shareTitle = "-" + mSellerInfo.name;
        }

        if (!StringUtil.isEmpty(mSellerInfo.backPic)) {
            shareBean.shareImageURL = ImageUtils.getImageFullUrl(mSellerInfo.backPic);
        }

        shareBean.shareContent = getString(R.string.shop_share_content);
        shareBean.pid = mSellerInfo.sellerId + "";
        shareBean.pname = mSellerInfo.name;
        shareBean.ptype = ItemType.SHOP_HOMEPAGE;

        shareBean.isNeedSyncToDynamic = false;
        NavUtils.gotoShareTableActivity(this, shareBean, "");
    }

    private void initData() {
        if (mSellerInfo != null) {
            if (!StringUtil.isEmpty(mSellerInfo.name)) {
                mTVShopName.setText(mSellerInfo.name);
            }
            if (!StringUtil.isEmpty(mSellerInfo.backPic)) {
//                BaseImgView.loadimg(mIVShopBg,
//                        mSellerInfo.backPic,
//                        R.mipmap.icon_default_750_360,
//                        R.mipmap.icon_default_750_360,
//                        R.mipmap.icon_default_750_360,
//                        ImageScaleType.EXACTLY,
//                        -1,
//                        -1,
//                        -1);

                ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(mSellerInfo.backPic), R.mipmap.icon_default_750_360, mIVShopBg);

            } else {
                mIVShopBg.setImageResource(R.mipmap.icon_default_750_360);
            }
            if (!StringUtil.isEmpty(mSellerInfo.icon)) {
//                BaseImgView.loadimg(mIVShopHeadIcon,
//                        mSellerInfo.icon,
//                        R.mipmap.ic_shop_default_logo,
//                        R.mipmap.ic_shop_default_logo,
//                        R.mipmap.ic_shop_default_logo,
//                        ImageScaleType.EXACTLY,
//                        -1,
//                        -1,
//                        180);
                ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(mSellerInfo.icon), R.mipmap.ic_shop_default_logo, mIVShopHeadIcon);

            } else {
                mIVShopHeadIcon.setImageResource(R.mipmap.ic_shop_default_logo);
            }
        }

        mShareUrlSuffix = CommonUrl.getShareUrlSuffix(this, SysConfigType.URL_SHOP_HOMPAGE_AUDIO_SUFFIX);

        setQRCode();
    }

    //生成二维码
    private void setQRCode() {
        /*if(!StringUtil.isEmpty(mShareUrlSuffix)){
            mTVDesc.setVisibility(View.VISIBLE);
            String path = mShareUrlSuffix + mSellerInfo.sellerId;
            Bitmap bitmap = QRCodeUtil.GenorateImage(path);
            mIVTwoCode.setImageBitmap(bitmap);
        }else {
            mTVDesc.setVisibility(View.GONE);
        }*/

        NativeDataBean nativeDataBean = new NativeDataBean();
        nativeDataBean.setId(String.valueOf(mSellerInfo.sellerId));
        nativeDataBean.setName(mSellerInfo.name);
        String shopPageUrl = QRCodeUtil.getUserPageUrl(this, NativeUtils.SHOP_HOME_PAGE, nativeDataBean);
        LogUtils.d("Harwkin", "url======" + shopPageUrl);
        Bitmap bitmap = QRCodeUtil.createQrAddImg(this, shopPageUrl);
        if (bitmap != null) {
            mIVTwoCode.setImageBitmap(bitmap);
        }else {
            mTVDesc.setVisibility(View.GONE);
        }

    }

    private void finId() {
        mIVShopBg = (ImageView) findViewById(R.id.iv_shop_bg);
        mIVShopHeadIcon = (ImageView) findViewById(R.id.iv_shop_head_icon);
        mTVShopName = (TextView) findViewById(R.id.tv_shop_name);
        mTVLinkValue = (TextView) findViewById(R.id.tv_link_value);
        mIVTwoCode = (ImageView) findViewById(R.id.iv_two_code);
        mTVDesc = (TextView) findViewById(R.id.tv_two_desc);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_shop_self_card, null);
    }

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setRightImg(R.mipmap.icon_top_share_nobg);
        mBaseNavView.setTitleText(R.string.shop_identifity);
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }
}
