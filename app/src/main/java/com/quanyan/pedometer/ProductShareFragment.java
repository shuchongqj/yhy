package com.quanyan.pedometer;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.base.BaseFragment;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.DirConstants;
import com.quanyan.yhy.ui.ShareTableActivity;
import com.quanyan.yhy.ui.base.utils.NativeUtils;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.util.QRCodeUtil;
import com.yhy.common.beans.net.model.ShareBean;
import com.yhy.common.beans.net.model.shop.MerchantItem;
import com.yhy.common.beans.net.model.user.NativeDataBean;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.LogUtils;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created with Android Studio.
 * Title:ProductShareFragment
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/8/29
 * Time:09:46
 * Version 1.1.0
 */
public class ProductShareFragment extends BaseFragment {

    private int mShareType = -1;

    private LinearLayout mContentLayout;
    private LinearLayout mShareTypeLayout;


    private View mSharePointView;

    //截屏分享出去的view
    private View mPointShareView;

    private MerchantItem mMerchantItem;

    /**
     * 初始化布局对象
     *
     * @param view               跟布局对象
     * @param savedInstanceState
     */
    @Override
    protected void initView(final View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mShareType = bundle.getInt(SPUtils.EXTRA_TYPE, -1);
            mMerchantItem = (MerchantItem) bundle.getSerializable(SPUtils.EXTRA_DATA);
        }
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        view.findViewById(R.id.fg_share_pro_parent_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
//        mContentLayout = (LinearLayout) view.findViewById(R.id.fg_share_point_content_view);
//        mShareTypeLayout = (LinearLayout) view.findViewById(R.id.fg_share_pro_share_layout);

//        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mContentLayout.getLayoutParams();
//        layoutParams.bottomMargin = (ScreenSize.getScreenHeightExcludeStatusBar(getActivity().getApplicationContext()) * 2) / 6;
//        mContentLayout.setLayoutParams(layoutParams);
//        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) mShareTypeLayout.getLayoutParams();
//        layoutParams1.bottomMargin = (ScreenSize.getScreenHeightExcludeStatusBar(getActivity().getApplicationContext()) * 1) / 6;
//        mShareTypeLayout.setLayoutParams(layoutParams1);

        mSharePointView = view.findViewById(R.id.fg_share_point_content_view);
//        ((ImageView) view.findViewById(R.id.fg_share_pro_bg)).setImageResource(R.mipmap.ic_point_share_bg);
        mPointShareView = ((FrameLayout) view.findViewById(R.id.view_point_share_content)).getChildAt(0);
        setPointData(mSharePointView, mPointShareView, mMerchantItem);
//        genQr();

        view.findViewById(R.id.fg_share_pro_cicles_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/8/29 达人圈
                getScreenShot(ShareTableActivity.QUANYAN);
            }
        });
        view.findViewById(R.id.fg_share_pro_wechat_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/8/29 微信
                getScreenShot(ShareTableActivity.WEIXIN);
            }
        });
        view.findViewById(R.id.fg_share_pro_wechat_circles_layut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/8/29 朋友圈
                getScreenShot(ShareTableActivity.WEIXIN_CIRCLE);
            }
        });
        view.findViewById(R.id.fg_share_pro_qq_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/8/29 qq
                getScreenShot(ShareTableActivity.QQ);
            }
        });
        view.findViewById(R.id.fg_share_pro_weibo_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/8/29 新浪
                getScreenShot(ShareTableActivity.WEIBO);
            }
        });
    }

    // 初始化二维码
    private void genQr() {
        NativeDataBean nativeDataBean = new NativeDataBean();
        nativeDataBean.setId(String.valueOf(mMerchantItem.itemVO.id));
        nativeDataBean.setName(mMerchantItem.itemVO.title);
        String integralUrl = QRCodeUtil.getProductPageUrl(getContext(), nativeDataBean);
        LogUtils.d("Harwkin", "url======" + integralUrl);
        if (!TextUtils.isEmpty(integralUrl)){
            createQRCode(integralUrl);
        }
    }

    private void setPointData(View sharePointView, View pointShareView, MerchantItem merchantItem) {
        if (merchantItem != null) {
            ImageView mProImg = (ImageView) sharePointView.findViewById(R.id.share_layout_point_mall_image);
            ImageView mProImg1 = (ImageView) pointShareView.findViewById(R.id.view_point_share_pro_img);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mProImg1.getLayoutParams();
            int width = ScreenSize.getScreenWidth(getActivity().getApplicationContext()) - 200;
            int height = width * 380 / 680;
            layoutParams.width = height;
            layoutParams.height = height;
            mProImg1.setLayoutParams(layoutParams);
            if (merchantItem.itemVO != null) {
                if(merchantItem.itemVO.picUrls != null && merchantItem.itemVO.picUrls.size() >0) {
                    ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(merchantItem.itemVO.picUrls.get(0)), R.mipmap.icon_default_215_150, 680, 380, mProImg);
                    ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(merchantItem.itemVO.picUrls.get(0)), R.mipmap.icon_default_215_150, 680, 380, mProImg1);
                }
                ((TextView) sharePointView.findViewById(R.id.share_layout_point_mall_title)).setText(
                        TextUtils.isEmpty(merchantItem.itemVO.title) ? "" : merchantItem.itemVO.title
                );
                ((TextView) pointShareView.findViewById(R.id.view_point_share_title)).setText(
                        TextUtils.isEmpty(merchantItem.itemVO.title) ? "" : merchantItem.itemVO.title
                );
                if (merchantItem.itemVO.payInfo != null) {
                    ((TextView) sharePointView.findViewById(R.id.share_layout_point_mall_discount_price)).setText(
                            getString(R.string.label_integralmall_deductible) + " " + getString(R.string.money_symbol)
                                    + (merchantItem.itemVO.payInfo.maxPoint > merchantItem.itemVO.payInfo.minPoint ?
                                    StringUtil.pointToYuan(merchantItem.itemVO.payInfo.minPoint*10) + "-" + StringUtil.pointToYuan(merchantItem.itemVO.payInfo.maxPoint*10): StringUtil.pointToYuan(merchantItem.itemVO.payInfo.maxPoint*10)));
//                                    + StringUtil.pointToYuan(merchantItem.itemVO.payInfo.maxPoint*10));
//                                    + StringUtil.convertScoreToDiscount(getActivity(), merchantItem.itemVO.payInfo.maxPoint));
                    ((TextView) pointShareView.findViewById(R.id.view_point_share_discount_price)).setText(
                            getString(R.string.label_integralmall_deductible) + " " + getString(R.string.money_symbol)
                                    + (merchantItem.itemVO.payInfo.maxPoint > merchantItem.itemVO.payInfo.minPoint ?
                                    StringUtil.pointToYuan(merchantItem.itemVO.payInfo.minPoint*10) + "-" + StringUtil.pointToYuan(merchantItem.itemVO.payInfo.maxPoint*10): StringUtil.pointToYuan(merchantItem.itemVO.payInfo.maxPoint*10)));
//                                    + StringUtil.convertScoreToDiscount(getActivity(), merchantItem.itemVO.payInfo.maxPoint));
                }

                long discountPrice = 0;
                if(merchantItem.itemVO.payInfo != null){
                    discountPrice = merchantItem.itemVO.payInfo.maxPoint;
                }

                ((TextView) sharePointView.findViewById(R.id.share_layout_point_mall_price)).setText(
                        getString(R.string.money_symbol) +
                                (merchantItem.itemVO.maxPrice > merchantItem.itemVO.marketPrice ?
                                        StringUtil.convertPriceNoSymbolExceptLastZero(merchantItem.itemVO.marketPrice) + "起": StringUtil.convertPriceNoSymbolExceptLastZero(merchantItem.itemVO.marketPrice)));
//                StringUtil.convertPriceNoSymbolExceptLastZero(item.originalPrice)
//                                StringUtil.pointToYuan(merchantItem.itemVO.marketPrice)
//                                StringUtil.convertPriceNoSymbolExceptLastZero(merchantItem.itemVO.marketPrice - discountPrice)
//                );
                ((TextView) pointShareView.findViewById(R.id.view_point_share_price)).setText(
                        getString(R.string.money_symbol) +
                                (merchantItem.itemVO.maxPrice > merchantItem.itemVO.marketPrice ?
                                        StringUtil.convertPriceNoSymbolExceptLastZero(merchantItem.itemVO.marketPrice) + "起": StringUtil.convertPriceNoSymbolExceptLastZero(merchantItem.itemVO.marketPrice)));

//                                StringUtil.convertPriceNoSymbolExceptLastZero(merchantItem.itemVO.marketPrice - discountPrice)
//                );
            }
        }
    }

    private void getScreenShot(int shareWay) {
        String qrCodePath = Environment.getExternalStorageDirectory() + File.separator + "Jiu_xiu_share_qrcode_" + System.currentTimeMillis() + ".jpg";
//        boolean flag = QRCodeUtil.createQRImage(getActivity(),
//                "http://www.yingheying.com/d/jssc_item?appUri=quanyan://app?content={\"TYPE\":\"QUANYAN\",\"OPERATION\":\"VIEW_INTEGRAL_MALL_DETAIL\",\"DATA\":{\"id\":\"" + mMerchantItem.itemVO.id + "\"}}", qrCodePath);
        /*String url = SPUtils.getIntegralHomeUrl(getActivity().getApplicationContext());
        if(url.contains("?")){
            url = url.concat("&ch=jssc_item");
        }else{
            url = url.concat("?ch=jssc_item");
        }
        LogUtils.v("url -->> " + url);
        boolean flag = QRCodeUtil.createQRImage(getActivity(), url, qrCodePath);*/

        //积分商城的二维码链接获取
        String integralUrl = genorIntergralUrl();

//        NativeDataBean nativeDataBean = new NativeDataBean();
//        nativeDataBean.setId(String.valueOf(mMerchantItem.itemVO.id));
//        nativeDataBean.setName(mMerchantItem.itemVO.title);
//        String integralUrl = QRCodeUtil.getProductPageUrl(getContext(), nativeDataBean);
//        LogUtil.d("Harwkin", "url======" + integralUrl);
//        if (!TextUtils.isEmpty(integralUrl)){
//            createQRCode(integralUrl);
//        }

        boolean flag = QRCodeUtil.createQRImage(getActivity(), integralUrl, qrCodePath);
        if (flag) {
            ((ImageView) mPointShareView.findViewById(R.id.view_point_share_qr_code)).setImageURI(Uri.fromFile(new File(qrCodePath)));
        }
        Message msg = new Message();
        msg.what = ShareFragment.MSG_CROP_VIEW;
        msg.arg1 = shareWay;
        mHandler.sendMessageDelayed(msg, 5);
    }

    private void createQRCode(String url){
        Bitmap bitmap = QRCodeUtil.GenorateImage(url);
//        Bitmap bitmap = QRCodeUtil.GenorateImage(getContext(), url);
        if(bitmap != null) {
            ((ImageView) mPointShareView.findViewById(R.id.view_point_share_qr_code)).setImageBitmap(bitmap);
        }
    }

    private String genorIntergralUrl() {
        NativeDataBean nativeDataBean = new NativeDataBean();
        if(mMerchantItem != null && mMerchantItem.itemVO != null){
            nativeDataBean.setId(String.valueOf(mMerchantItem.itemVO.id));
        }
        //String userPageUrl = QRCodeUtil.getUserPageUrl(getActivity(), NativeUtils.VIEW_INTEGRAL_MALL_DETAIL, nativeDataBean);
        return QRCodeUtil.getUserPageUrl(getActivity(), NativeUtils.VIEW_INTEGRAL_MALL_DETAIL, nativeDataBean);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case ShareFragment.MSG_CROP_VIEW:
                handleShare(msg.arg1);
                break;
        }
    }

    private void handleShare(int index) {
//        SPUtils.setShareOK(getActivity(), true);
        String bitmap = "";
        bitmap = cropView(mPointShareView);

        if (StringUtil.isEmpty(bitmap)) {
            ToastUtil.showToast(getActivity(), getString(R.string.label_toast_share_steps_error));
            return;
        }
        String shareContent = "#积分商城# 消费越多越省钱，分享越多越赚钱，质优价优，兑到手软！";
        String weiboTopicName = "";
        String topicName = "";
//        if(mMerchantItem != null && mMerchantItem.itemVO != null){
//            shareContent = mMerchantItem.itemVO.title;
//        }
        switch (index) {
            case ShareTableActivity.WEIBO: {
                ShareBean shareBean = new ShareBean();
                shareBean.shareImageLocal = bitmap;
                shareBean.shareWay = index;
                shareBean.isNeedSyncToDynamic = false;
                if (!StringUtil.isEmpty(shareContent)) {
                    shareBean.shareContent = (StringUtil.isEmpty(weiboTopicName) ? "" : weiboTopicName) + shareContent;
                }
                NavUtils.gotoShareTableActivity(getActivity(), shareBean, "");
            }
            break;
            case ShareTableActivity.QQ:
                AndroidShareUtil.shareQQFriend(getActivity(), "", StringUtil.isEmpty(shareContent) ? "" : (shareContent), AndroidShareUtil.IMAGE, bitmap);
                break;
            case ShareTableActivity.WEIXIN:
                AndroidShareUtil.shareWeChatFriend(getActivity(), "", StringUtil.isEmpty(shareContent) ? "" : (shareContent), AndroidShareUtil.IMAGE, bitmap);
                break;
            case ShareTableActivity.WEIXIN_CIRCLE:
                AndroidShareUtil.shareWeChatFriendCircle(getActivity(), "", StringUtil.isEmpty(shareContent) ? "" : (shareContent), AndroidShareUtil.IMAGE, bitmap);
                break;
            case ShareTableActivity.QUANYAN: {
                ShareBean shareBean = new ShareBean();
                shareBean.shareImageLocal = bitmap;
                shareBean.shareWay = index;
                if (!StringUtil.isEmpty(shareContent)) {
                    shareBean.shareContent = (StringUtil.isEmpty(topicName) ? "" : topicName) + shareContent;
                }
                NavUtils.gotoShareTableActivity(getActivity(), shareBean, "");
            }
            break;
        }
        getActivity().finish();
    }

    private String cropView(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        if (bitmap == null) {
            return null;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(1f, 1f);
        Bitmap temp = Bitmap.createBitmap(bitmap, 0 ,0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        view.destroyDrawingCache();
        File file = new File(DirConstants.DIR_CACHE + "cache_QuanYan_invite_share_" + System.currentTimeMillis() + ".jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            Log.i("file", file.getAbsolutePath());
            temp.setHasAlpha(true);
            temp.prepareToDraw();

//            int width = temp.getWidth();
//            int height = temp.getHeight();
//            // 设置想要的大小
//            int newWidth = 480;
//            int newHeight = 800;
//            // 计算缩放比例
//            float scaleWidth = ((float) newWidth) / width;
//            float scaleHeight = ((float) newHeight) / height;
//            // 取得想要缩放的matrix参数
//            Matrix matrix = new Matrix();
//            matrix.postScale(scaleWidth, scaleHeight);
//            // 得到新的图片
//            Bitmap result = Bitmap.createBitmap(temp, 0, 0, width, height, matrix, true);
////            Bitmap result = Bitmap.createBitmap(temp, 0, 0, width, height);
//            result.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            temp.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if(temp != null && temp.isRecycled()){
            temp.recycle();
            temp = null;
        }
        return file.getAbsolutePath();
    }

    /**
     * 加载主布局文件
     *
     * @return 布局文件 {@link View}
     */
    @Override
    public View onLoadContentView() {
        return View.inflate(getActivity(), R.layout.fg_share_product_view, null);
    }
}
