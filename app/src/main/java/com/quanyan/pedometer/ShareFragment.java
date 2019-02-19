package com.quanyan.pedometer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newyhy.utils.ShareUtils;
import com.quanyan.base.BaseFragment;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.util.StringUtil;
import com.quanyan.pedometer.newpedometer.StepService;
import com.quanyan.pedometer.utils.PreferencesUtils;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.ui.ShareTableActivity;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.calendar.ScreenUtil;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.util.QRCodeUtil;
import com.yhy.common.beans.net.model.ShareBean;
import com.yhy.common.types.BannerType;
import com.yhy.common.utils.LogUtils;
import com.yhy.common.utils.SPUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with Android Studio.
 * Title:ShareFragment
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/7/7
 * Time:下午6:48
 * Version 1.1.0
 */
public class ShareFragment extends BaseFragment {
    public final static int MSG_CROP_VIEW = 0x2222;
    private ImageView mImageView;
    private TextView mStepsView;
    private TextView mDistanceView;
    private TextView mCalView;

    private LinearLayout mQuanYanCircleView;
    private LinearLayout mWeiXinView;
    private LinearLayout mWeiXinCircleView;
    private LinearLayout mQQCiew;
    private LinearLayout mWeiBoView;

    private View mParentView;
    private LinearLayout mSubParentView;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        setTitleBarBackground(Color.TRANSPARENT);
        mParentView = view;

        mImageView = ((ImageView) view.findViewById(R.id.imageview));
        mStepsView = ((TextView) view.findViewById(R.id.view_share_pedometer_steps));
        mDistanceView = ((TextView) view.findViewById(R.id.view_share_pedometer_distance));
        mCalView = ((TextView) view.findViewById(R.id.view_share_pedometer_calories));

        mQuanYanCircleView = (LinearLayout) view.findViewById(R.id.view_share_pedometer_cicles_layout);
        mWeiXinView = (LinearLayout) view.findViewById(R.id.view_share_pedometer_wechat_layout);
        mWeiXinCircleView = (LinearLayout) view.findViewById(R.id.view_share_pedometer_wechat_circles_layut);
        mQQCiew = (LinearLayout) view.findViewById(R.id.view_share_pedometer_qq_layout);
        mWeiBoView = (LinearLayout) view.findViewById(R.id.view_share_pedometer_weibo_layout);

        mSubParentView = (LinearLayout) view.findViewById(R.id.view_pedometer_share_container);

        int width = ScreenSize.getScreenWidth(getActivity().getApplicationContext()) - ScreenUtil.dip2px(getActivity(), 60);
        int height = width * 732 / 1500;
        mImageView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
//        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams)mSubParentView.getLayoutParams();
//        rlp.leftMargin = ScreenUtil.getScreenWidth(getActivity())/10;
//        rlp.rightMargin = ScreenUtil.getScreenWidth(getActivity())/10;
//        rlp.topMargin = ScreenUtil.getScreenHeight(getActivity())/8;
//        rlp.bottomMargin = ScreenUtil.getScreenHeight(getActivity())/8;
//        mSubParentView.setLayoutParams(rlp);

        initOthers();

        view.findViewById(R.id.view_pedometer_share_parent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(getActivity(), R.layout.view_pedometer_share, null);
    }

    /**
     * 初始化
     */
    private void initOthers() {
        //二维码生成
        genorateQr();

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        mStepsView.setText(StepService.getInstance().getSteps() + "");
        mDistanceView.setText(decimalFormat.format(StepService.getInstance().getDistance()));
        mCalView.setText(decimalFormat.format(StepService.getInstance().getCalories()));

        mQuanYanCircleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/6/29 分享健康圈
                TCEventHelper.onEvent(getActivity(), AnalyDataValue.STEP_SHARE, "QUANYAN");
                //事件统计
                Analysis.pushEvent(getActivity(), AnEvent.ZXSJ_SHARE_QUANZI);
                cropView(ShareTableActivity.QUANYAN);

            }
        });
        mWeiXinView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/6/29 分享微信
                TCEventHelper.onEvent(getActivity(), AnalyDataValue.STEP_SHARE, "WEIXIN");
                //事件统计
                Analysis.pushEvent(getActivity(), AnEvent.ZXSJ_SHARE_WECHAT);
                cropView(ShareTableActivity.WEIXIN);
            }
        });
        mWeiXinCircleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/6/29 分享微信朋友圈
                TCEventHelper.onEvent(getActivity(), AnalyDataValue.STEP_SHARE, "WEIXIN_CICRLE");
                //事件统计
                Analysis.pushEvent(getActivity(), AnEvent.ZXSJ_SHARE_WECHATQUAN);
                cropView(ShareTableActivity.WEIXIN_CIRCLE);
            }
        });
        mQQCiew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/6/29 分享QQ
                TCEventHelper.onEvent(getActivity(), AnalyDataValue.STEP_SHARE, "QQ");
                //事件统计
                Analysis.pushEvent(getActivity(), AnEvent.ZXSJ_SHARE_QQ);
                cropView(ShareTableActivity.QQ);
            }
        });
        mWeiBoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/6/29 分享微博
                TCEventHelper.onEvent(getActivity(), AnalyDataValue.STEP_SHARE, "WEIBO");

                // 友盟埋点
//                MobclickAgent.onEvent(getActivity(), UmengEvent.ZXSJ_SHARE_w);

                cropView(ShareTableActivity.WEIBO);
            }
        });
    }

    private void genorateQr() {
        //String shareUrl = PreferencesUtils.getString(getActivity().getApplicationContext(), StepService.PEDOMETER_SAHRE_URL_QR_CODE, "http://t.cn/R5rCLyp");
        String shareUrl = QRCodeUtil.getUserPageUrl(getActivity(), BannerType.STR_HOME_PEDMOTER, null);

        LogUtils.d("Harwkin", "url======" + shareUrl);
        if (!TextUtils.isEmpty(shareUrl)) {
            createQRCode(shareUrl);
        }
    }

    /**
     * 开始分享
     *
     * @param index
     */
    private void handleShare(int index) {
        SPUtils.setShareOK(getActivity(), true);

        String bitmap = getScreenShot();
//        Bitmap bitmap1 =BitmapFactory.decodeFile(bitmap);

        if (StringUtil.isEmpty(bitmap)) {
            ToastUtil.showToast(getActivity(), getString(R.string.label_toast_share_steps_error));
            return;
        }
        String shareContent = "";
        String weiboTopicName = PreferencesUtils.getString(getActivity(), StepService.PEDOMETER_SHARE_WEIBO_TOPIC_NAME, "");
        String topicName = PreferencesUtils.getString(getActivity(), StepService.PEDOMETER_SHARE_MASTER_CIRCLE_TOPIC_NAME, "");
        String shareText = PreferencesUtils.getString(getActivity().getApplicationContext(), StepService.PEDOMETER_SAHRE_TEXT, "分享内容:");
        if (!StringUtil.isEmpty(shareText)) {
            if (shareText.contains("%s")) {
                shareContent = String.format(shareText, StepService.getInstance().getSteps());
            } else {
                shareContent = shareText;
            }
        }
        String qrCodeUrl = PreferencesUtils.getString(getActivity(), StepService.PEDOMETER_SAHRE_URL_QR_CODE);
        switch (index) {
            case ShareTableActivity.WEIBO:
                {
                ShareBean shareBean = new ShareBean();
                shareBean.shareImageLocal = bitmap;
                shareBean.shareWay = ShareUtils.WEIBO;
                shareBean.isNeedSyncToDynamic = false;
                if (!StringUtil.isEmpty(shareContent)) {
                    shareBean.shareContent = (StringUtil.isEmpty(weiboTopicName) ? "" : weiboTopicName) + shareContent + qrCodeUrl;
                }
                NavUtils.gotoShareTableActivity(getActivity(), shareBean, true, true);
//            ShareUtils.doImageShare( getActivity(), shareBean, bitmap1,  true);
            }
            break;
            case ShareTableActivity.QQ: {
//                {
//                ShareBean shareBean = new ShareBean();
//                shareBean.shareImageLocal = bitmap;
//                shareBean.shareWay = index;
//                shareBean.isNeedSyncToDynamic = false;
//                if (!StringUtil.isEmpty(shareContent)) {
//                    shareBean.shareContent = (StringUtil.isEmpty(weiboTopicName) ? "" : weiboTopicName) + shareContent + qrCodeUrl;
//                }
//                NavUtils.gotoShareTableActivity(getActivity(), shareBean, true);
//            }
//                AndroidShareUtil.shareQQFriend(getActivity(), "", StringUtil.isEmpty(shareContent) ? "" : (shareContent + qrCodeUrl), AndroidShareUtil.IMAGE, bitmap);
                ShareBean shareBean = new ShareBean();
                shareBean.shareImageLocal = bitmap;
                shareBean.shareWay = ShareUtils.QQ;
                shareBean.isNeedSyncToDynamic = false;
//                if (!StringUtil.isEmpty(msgText)) {
//            shareBean.shareContent = (StringUtil.isEmpty(weiboTopicName) ? "" : weiboTopicName) + shareContent + qrCodeUrl;
//                }
                NavUtils.gotoShareTableActivity(getActivity(), shareBean, true, true);
//                ShareUtils.doImageShare( getActivity(), shareBean,  bitmap1,true);

            }
                break;
            case ShareTableActivity.WEIXIN:
            {
                ShareBean shareBean = new ShareBean();
                shareBean.shareImageLocal = bitmap;
                shareBean.shareWay = ShareUtils.WEIXIN;
                shareBean.isNeedSyncToDynamic = false;
//                if (!StringUtil.isEmpty(msgText)) {
//            shareBean.shareContent = (StringUtil.isEmpty(weiboTopicName) ? "" : weiboTopicName) + shareContent + qrCodeUrl;
//                }
                NavUtils.gotoShareTableActivity(getActivity(), shareBean, true, true);
//                ShareUtils.doImageShare( getActivity(), shareBean,  bitmap1,true);

            }
//                AndroidShareUtil.shareWeChatFriend(getActivity(), "", StringUtil.isEmpty(shareContent) ? "" : (shareContent + qrCodeUrl), AndroidShareUtil.IMAGE, bitmap);
                break;
            case ShareTableActivity.WEIXIN_CIRCLE:
//                AndroidShareUtil.shareWeChatFriendCircle(getActivity(), "", StringUtil.isEmpty(shareContent) ? "" : (shareContent + qrCodeUrl), AndroidShareUtil.IMAGE, bitmap);
            {
                ShareBean shareBean = new ShareBean();
                shareBean.shareImageLocal = bitmap;
                shareBean.shareWay = ShareUtils.WEIXIN_CIRCLE;
                shareBean.isNeedSyncToDynamic = false;
//                if (!StringUtil.isEmpty(msgText)) {
//            shareBean.shareContent = (StringUtil.isEmpty(weiboTopicName) ? "" : weiboTopicName) + shareContent + qrCodeUrl;
//                }
                NavUtils.gotoShareTableActivity(getActivity(), shareBean, true, true);
//                ShareUtils.doImageShare( getActivity(), shareBean,  bitmap1,true);

            }
                break;
            case ShareTableActivity.QUANYAN: {
                ShareBean shareBean = new ShareBean();
                shareBean.shareImageLocal = bitmap;
                shareBean.shareWay = ShareUtils.QUANYAN;
                if (!StringUtil.isEmpty(shareContent)) {
                    shareBean.shareContent = (StringUtil.isEmpty(topicName) ? "" : topicName) + shareContent;
                }
                NavUtils.gotoShareTableActivity(getActivity(), shareBean, true, true);
//                ShareUtils.doImageShare( getActivity(), shareBean,  bitmap1,true);

            }
            break;
        }
        getActivity().finish();
    }

    /**
     * 开始截图
     *
     * @return
     */
    private void cropView(int index) {
        ((TextView) mParentView.findViewById(R.id.view_share_pedometer_share_text)).setText("我的邀请二维码");
        mParentView.findViewById(R.id.view_share_pedometer_share_layout).setVisibility(View.GONE);
        mParentView.findViewById(R.id.view_share_pedometer_qr_code_layout).setVisibility(View.VISIBLE);
        mParentView.setVisibility(View.INVISIBLE);

        Message msg = new Message();
        msg.what = MSG_CROP_VIEW;
        msg.arg1 = index;
        mHandler.sendMessageDelayed(msg, 10);
    }

    /**
     * 获取截图
     *
     * @return
     */
    private String getScreenShot() {

//        mSubParentView.setDrawingCacheEnabled(true);
//        mSubParentView.buildDrawingCache();
        Bitmap bitmap = getBitmap(mSubParentView);
        if (bitmap == null) {
            return null;
        }

//        Matrix matrix = new Matrix();
//        matrix.postScale(0.5f, 0.5f);
//        Bitmap temp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//        mSubParentView.destroyDrawingCache();
////        File file = new File(DirConstants.DIR_CACHE + "cache_QuanYan_steps_share_qr" + System.currentTimeMillis() + ".jpg");
//        File file = new File(DirConstants.DIR_CACHE + "cache_QuanYan_steps_share_qr" + ".jpg");
//        try {
//            FileOutputStream fileOutputStream = new FileOutputStream(file);
//            Log.i("file", file.getAbsolutePath());
//            temp.setHasAlpha(true);
//            temp.prepareToDraw();
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
//            Bitmap result = Bitmap.createBitmap(temp, 0, 0, width, height, matrix,true);
//            result.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
//            temp.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

//            Bitmap bitmap = QRCodeUtil.GenorateImage(url);
//            String fileName = System.currentTimeMillis() + ".png";
//            saveFile(temp,fileName,DirConstants.DIR_PIC_ORIGIN);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (temp != null && temp.isRecycled()) {
//            temp.recycle();
//            temp = null;
//        }
        return saveAsBitmap(getContext(), bitmap, "", "");
    }

    public static Bitmap getBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public static String saveAsBitmap(Context context, Bitmap bitmap, String folderName, String fileName) {
        String parentpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/yhy/";
        if (TextUtils.isEmpty(fileName)) {
            Date date   = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
            fileName = format.format(date) + ".jpg";
        }

        File pic = new File(parentpath, fileName);
        if (!pic.getParentFile().exists())
            pic.getParentFile().mkdirs();

        if (pic.exists())
            pic.delete();

        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(pic));
            MediaScannerConnection.scanFile(context, new String[]{pic.toString()}, null, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmap.recycle();
        return pic.getAbsolutePath();
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        hideErrorView(null);
        switch (msg.what) {
            case MSG_CROP_VIEW:
                handleShare(msg.arg1);
                break;
        }
    }

    public void saveFile(Bitmap bm, String fileName, String saveFile) throws IOException {
        File file = new File(saveFile);
        if (!file.exists()) {
            file.mkdir();
        }
        File save = new File(saveFile + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(save));
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(getContext().getContentResolver(),
                    save.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(save)));
    }

    private void createQRCode(String url) {
        Bitmap bitmap = QRCodeUtil.GenorateImage(url);
        if (bitmap != null) {
            ((ImageView) mParentView.findViewById(R.id.view_share_pedometer_qr_code)).setImageBitmap(bitmap);
        }
    }
}
