package com.quanyan.pedometer;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.quanyan.base.BaseFragment;
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
import com.yhy.service.IUserService;

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
public class ProductShareMasterFragment extends BaseFragment {

    @Autowired
    IUserService userService;
    private int mShareType = -1;

    private View mShareMasterConsultView;

    //截屏分享出去的view
    private View mMasterConsultShareView;

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
        view.findViewById(R.id.fg_share_pro_master_parent_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mShareMasterConsultView = view.findViewById(R.id.fg_share_master_consult_content_parent);
        mMasterConsultShareView = ((ScrollView)((FrameLayout) view.findViewById(R.id.view_point_share_content)).getChildAt(0)).getChildAt(0);
        setMasterConsultData(mShareMasterConsultView, mMasterConsultShareView, mMerchantItem);

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

    private void setMasterConsultData(View shareMasterConsultView, View masterConsultShareView, MerchantItem merchantItem) {
        if (merchantItem != null) {
            ImageView userHead = (ImageView) shareMasterConsultView.findViewById(R.id.share_layout_master_consult_user_head);
            ImageView userHead1 = (ImageView) masterConsultShareView.findViewById(R.id.view_share_master_consult_userhead);
            if (merchantItem.userInfo != null) {
//                BaseImgView.loadimg(userHead, ImageUtils.getImageFullUrl(merchantItem.userInfo.avatar), R.mipmap.icon_default_avatar,
//                        R.mipmap.icon_default_avatar,
//                        R.mipmap.icon_default_avatar,
//                        ImageScaleType.EXACTLY,
//                        180, 180, 180);
//                BaseImgView.loadimg(userHead1, ImageUtils.getImageFullUrl(merchantItem.userInfo.avatar), R.mipmap.icon_default_avatar,
//                        R.mipmap.icon_default_avatar,
//                        R.mipmap.icon_default_avatar,
//                        ImageScaleType.EXACTLY,
//                        280, 280, 180);
                ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(merchantItem.userInfo.avatar), R.mipmap.icon_default_avatar, 180, 180, userHead);
                ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(merchantItem.userInfo.avatar), R.mipmap.icon_default_avatar, 280, 280, userHead1);

            } else {
                userHead.setImageResource(R.mipmap.icon_default_avatar);
                userHead1.setImageResource(R.mipmap.icon_default_avatar);
            }
            TextView title = (TextView) shareMasterConsultView.findViewById(R.id.share_layout_master_consult_title);
            TextView title1 = (TextView) masterConsultShareView.findViewById(R.id.view_share_master_consult_title);
            TextView serveArea = (TextView) shareMasterConsultView.findViewById(R.id.share_layout_master_consult_serve_area);
            TextView serveArea1 = (TextView) masterConsultShareView.findViewById(R.id.view_share_master_consult_serve_area);

            TextView consultTimePoint = (TextView) shareMasterConsultView.findViewById(R.id.share_layout_master_consult_price);
            TextView consultTimePoint1 = (TextView) masterConsultShareView.findViewById(R.id.view_share_master_consult_price);
            if (merchantItem.itemVO != null) {
                title.setText(
                        TextUtils.isEmpty(merchantItem.itemVO.title) ? "" : merchantItem.itemVO.title);
                title1.setText(
                        TextUtils.isEmpty(merchantItem.itemVO.title) ? "" : merchantItem.itemVO.title);

                serveArea.setText(String.format(getActivity().getString(R.string.label_consulting_service_area),
                        TextUtils.isEmpty(merchantItem.itemVO.destinations) ? "无" : merchantItem.itemVO.destinations));
                serveArea1.setText(String.format(getActivity().getString(R.string.label_consulting_service_area),
                        TextUtils.isEmpty(merchantItem.itemVO.destinations) ? "无" : merchantItem.itemVO.destinations));

                if (merchantItem.itemVO.marketPrice == 0) {
                    consultTimePoint.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    consultTimePoint1.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    consultTimePoint.setText(String.format(getActivity().getString(R.string.label_points_format), merchantItem.itemVO.originalPrice/10)
                            + "/" + String.format(getActivity().getString(R.string.label_time_minutes_format), merchantItem.itemVO.consultTime / 60));
                    consultTimePoint1.setText(String.format(getActivity().getString(R.string.label_points_format), merchantItem.itemVO.originalPrice/10)
                            + "/" + String.format(getActivity().getString(R.string.label_time_minutes_format), merchantItem.itemVO.consultTime / 60));
                } else {
                    TextView textView = (TextView) shareMasterConsultView.findViewById(R.id.share_layout_master_consult_original_price);
                    TextView textView1 = (TextView) masterConsultShareView.findViewById(R.id.view_share_master_consult_original_price);
                    textView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    textView1.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    textView.setText(String.format(getActivity().getString(R.string.label_consult_original_price_format), merchantItem.itemVO.originalPrice/10));
                    textView1.setText(String.format(getActivity().getString(R.string.label_consult_original_price_format), merchantItem.itemVO.originalPrice/10));
                    ((TextView) shareMasterConsultView.findViewById(R.id.share_layout_master_consult_free_label)).setText(
                            String.format(getActivity().getString(R.string.label_points_format), merchantItem.itemVO.marketPrice/10));
                    ((TextView) masterConsultShareView.findViewById(R.id.view_share_master_consult_free_label)).setText(
                            String.format(getActivity().getString(R.string.label_points_format), merchantItem.itemVO.marketPrice/10));
                    consultTimePoint.setText("/" + String.format(getActivity().getString(R.string.label_time_minutes_format), merchantItem.itemVO.consultTime / 60));
                    consultTimePoint1.setText("/" + String.format(getActivity().getString(R.string.label_time_minutes_format), merchantItem.itemVO.consultTime / 60));
                }

                String sales = "";
                if (merchantItem.userInfo != null && userService.getLoginUserId() == (merchantItem.userInfo.userId == 0?merchantItem.userInfo.id : merchantItem.userInfo.userId)) {
                    if (merchantItem.itemVO.sales > 999) {
                        sales = "999+";
                    } else {
                        sales = merchantItem.itemVO.sales + "";
                    }
                } else {
                    if (merchantItem.itemVO.showSales > 999) {
                        sales = "999+";
                    } else {
                        sales = merchantItem.itemVO.showSales + "";
                    }
                    }


                ((TextView) shareMasterConsultView.findViewById(R.id.share_layout_master_consult_consult_num)).setText(sales+"人咨询");
                ((TextView) masterConsultShareView.findViewById(R.id.view_share_master_consult_num)).setText(sales+"人咨询");
            }
        }
    }

    private void getScreenShot(int shareWay) {
        String qrCodePath = Environment.getExternalStorageDirectory() + File.separator + "Jiu_xiu_share_qrcode_" + System.currentTimeMillis() + ".jpg";
        /*boolean flag = QRCodeUtil.createQRImage(getActivity(),
                "http://www.yingheying.com/d/daren_zx?appUri=quanyan://app?content={\"TYPE\":\"QUANYAN\",\"OPERATION\":\"VIEW_CONSULTING_SERVICE_DETAIL\",\"DATA\":{\"id\":\"" + mMerchantItem.itemVO.id+"\"}}", qrCodePath);*/

        String consultUrl = genorConsultUrl();
        LogUtils.d("Harwkin", "url======" + consultUrl);
        boolean flag = QRCodeUtil.createQRImage(getActivity(), consultUrl, qrCodePath);
        if(flag) {
            ((ImageView) mMasterConsultShareView.findViewById(R.id.view_point_share_qr_code)).setImageURI(Uri.fromFile(new File(qrCodePath)));
        }

        Message msg = new Message();
        msg.what = ShareFragment.MSG_CROP_VIEW;
        msg.arg1 = shareWay;
        mHandler.sendMessageDelayed(msg, 5);
    }

    private String genorConsultUrl() {
        NativeDataBean nativeDataBean = new NativeDataBean();
        if(mMerchantItem != null && mMerchantItem.itemVO != null){
            nativeDataBean.setId(String.valueOf(mMerchantItem.itemVO.id));
        }
        //String userPageUrl = QRCodeUtil.getUserPageUrl(getActivity(), NativeUtils.MASTER_CONSULT_DETAIL, nativeDataBean);
        return QRCodeUtil.getUserPageUrl(getActivity(), NativeUtils.VIEW_CONSULTING_SERVICE_DETAIL, nativeDataBean);
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
        SPUtils.setShareOK(getActivity(), true);
        String bitmap = "";
        bitmap = cropView(mMasterConsultShareView);

        if (StringUtil.isEmpty(bitmap)) {
            ToastUtil.showToast(getActivity(), getString(R.string.label_toast_share_steps_error));
            return;
        }
        String shareContent = "#鹰和鹰咨询师#运动咨询，有问必答，一对一服务，为您的运动保驾护航！";
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

        if(bitmap == null){
            return "";
        }

        Matrix matrix = new Matrix();
        matrix.postScale(0.5f, 0.5f);
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
        return View.inflate(getActivity(), R.layout.fg_share_product_master_view, null);
    }
}
