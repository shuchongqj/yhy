package com.newyhy.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.newyhy.utils.DisplayUtils;
import com.newyhy.utils.ShareUtils;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.util.StringUtil;
import com.quanyan.pedometer.newpedometer.StepService;
import com.quanyan.pedometer.utils.PreferencesUtils;
import com.quanyan.yhy.R;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.ui.ShareTableActivity;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.calendar.ScreenUtil;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.util.QRCodeUtil;
import com.yhy.common.beans.net.model.ShareBean;
import com.yhy.common.types.BannerType;

import java.text.DecimalFormat;

/**
 * 步步吸金分享弹窗
 * Created by yangboxue on 2018/5/30.
 */

public class StepSharePopupWindow extends PopupWindow implements View.OnClickListener {

    private Activity mActivity;
    private LayoutInflater mInflater;

    private View mContentView;
    private LinearLayout centerView;

    private ImageView mImageView;
    private TextView mStepsView;
    private TextView mDistanceView;
    private TextView mCalView;

    private View mParentView;

    public StepSharePopupWindow(Activity activity) {

        mActivity = activity;

        // 初始化UI
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = mInflater.inflate(R.layout.step_share_popu_window, null);

        mImageView = mContentView.findViewById(R.id.imageview);
        mStepsView = mContentView.findViewById(R.id.view_share_pedometer_steps);
        mDistanceView = mContentView.findViewById(R.id.view_share_pedometer_distance);
        mCalView = mContentView.findViewById(R.id.view_share_pedometer_calories);

        mContentView.findViewById(R.id.view_share_pedometer_cicles_layout).setOnClickListener(this);
        mContentView.findViewById(R.id.view_share_pedometer_wechat_layout).setOnClickListener(this);
        mContentView.findViewById(R.id.view_share_pedometer_wechat_circles_layut).setOnClickListener(this);
        mContentView.findViewById(R.id.view_share_pedometer_qq_layout).setOnClickListener(this);
        mContentView.findViewById(R.id.view_share_pedometer_weibo_layout).setOnClickListener(this);

        centerView = mContentView.findViewById(R.id.view_pedometer_share_container);

        int width = ScreenSize.getScreenWidth(activity.getApplicationContext()) - ScreenUtil.dip2px(activity, 60);
        int height = width * 732 / 1500;
        mImageView.setLayoutParams(new LinearLayout.LayoutParams(width, height));

        mContentView.findViewById(R.id.view_pedometer_share_parent).setOnClickListener(this);

        this.setContentView(mContentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();

        // 渲染UI
        renderUI();
    }

    private void renderUI() {
        //二维码生成
        genorateQr();

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        mStepsView.setText(StepService.getInstance().getSteps() + "");
        mDistanceView.setText(decimalFormat.format(StepService.getInstance().getDistance()));
        mCalView.setText(decimalFormat.format(StepService.getInstance().getCalories()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_share_pedometer_cicles_layout:
                //事件统计
                Analysis.pushEvent(mActivity, AnEvent.ZXSJ_SHARE_QUANZI);
                cropView(ShareTableActivity.QUANYAN);
                break;
            case R.id.view_share_pedometer_wechat_layout:
                //事件统计
                Analysis.pushEvent(mActivity, AnEvent.ZXSJ_SHARE_WECHAT);
                cropView(ShareTableActivity.WEIXIN);
                break;
            case R.id.view_share_pedometer_wechat_circles_layut:
                //事件统计
                Analysis.pushEvent(mActivity, AnEvent.ZXSJ_SHARE_WECHATQUAN);
                cropView(ShareTableActivity.WEIXIN_CIRCLE);
                break;
            case R.id.view_share_pedometer_qq_layout:
                //事件统计
                Analysis.pushEvent(mActivity, AnEvent.ZXSJ_SHARE_QQ);
                cropView(ShareTableActivity.QQ);
                break;
            case R.id.view_share_pedometer_weibo_layout:
                cropView(ShareTableActivity.WEIBO);
                break;
            case R.id.view_pedometer_share_parent:
                showOrDismiss(mParentView);
                break;
        }
    }

    private void genorateQr() {
        String shareUrl = QRCodeUtil.getUserPageUrl(mActivity, BannerType.STR_HOME_PEDMOTER, null);
        if (!TextUtils.isEmpty(shareUrl)) {
            createQRCode(shareUrl);
        }
    }

    private void createQRCode(String url) {
        Bitmap bitmap = QRCodeUtil.GenorateImage(url);
        if (bitmap != null) {
            ((ImageView) mContentView.findViewById(R.id.view_share_pedometer_qr_code)).setImageBitmap(bitmap);
        }
    }

    /**
     * 开始截图
     *
     * @return
     */
    private void cropView(int index) {
        ((TextView) mContentView.findViewById(R.id.view_share_pedometer_share_text)).setText("我的邀请二维码");
        mContentView.findViewById(R.id.view_share_pedometer_share_layout).setVisibility(View.GONE);
        mContentView.findViewById(R.id.view_share_pedometer_qr_code_layout).setVisibility(View.VISIBLE);
        mContentView.setVisibility(View.INVISIBLE);

        Message msg = new Message();
        msg.what = 0;
        msg.arg1 = index;
        mHander.sendMessageDelayed(msg, 10);
    }

    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    handleShare(msg.arg1);
                    break;
            }

        }

    };

    /**
     * 开始分享
     *
     * @param index
     */
    private void handleShare(int index) {

        String bitmap = DisplayUtils.getScreenShot(mActivity, centerView);

        if (StringUtil.isEmpty(bitmap)) {
            ToastUtil.showToast(mActivity, mActivity.getString(R.string.label_toast_share_steps_error));
            return;
        }
        String shareContent = "";
        String weiboTopicName = PreferencesUtils.getString(mActivity, StepService.PEDOMETER_SHARE_WEIBO_TOPIC_NAME, "");
        String topicName = PreferencesUtils.getString(mActivity, StepService.PEDOMETER_SHARE_MASTER_CIRCLE_TOPIC_NAME, "");
        String shareText = PreferencesUtils.getString(mActivity.getApplicationContext(), StepService.PEDOMETER_SAHRE_TEXT, "分享内容:");
        if (!StringUtil.isEmpty(shareText)) {
            if (shareText.contains("%s")) {
                shareContent = String.format(shareText, StepService.getInstance().getSteps());
            } else {
                shareContent = shareText;
            }
        }
        String qrCodeUrl = PreferencesUtils.getString(mActivity, StepService.PEDOMETER_SAHRE_URL_QR_CODE);
        ShareBean shareBean = new ShareBean();
        shareBean.shareImageLocal = bitmap;
        shareBean.isNeedSyncToDynamic = false;
        if (!StringUtil.isEmpty(shareContent)) {
            shareBean.shareContent = (StringUtil.isEmpty(weiboTopicName) ? "" : weiboTopicName) + shareContent + qrCodeUrl;
        }
        switch (index) {
            case ShareTableActivity.WEIBO:
                shareBean.shareWay = ShareUtils.WEIBO;
                break;
            case ShareTableActivity.QQ:
                shareBean.shareWay = ShareUtils.QQ;
                break;
            case ShareTableActivity.WEIXIN:
                shareBean.shareWay = ShareUtils.WEIXIN;
                break;
            case ShareTableActivity.WEIXIN_CIRCLE:
                shareBean.shareWay = ShareUtils.WEIXIN_CIRCLE;
                break;
            case ShareTableActivity.QUANYAN:
                shareBean.shareWay = ShareUtils.QUANYAN;
                break;
        }
        //            ShareUtils.doImageShare( getActivity(), shareBean, bitmap1,  true);
        NavUtils.gotoShareTableActivity(mActivity, shareBean, true, true);

        mParentView.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();

            }
        }, 300);
    }

    public void showOrDismiss(View parent) {

        if (parent == null) return;

        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }

        mParentView = parent;

        long duration = 300;

        if (!this.isShowing()) {

            // Appear
            this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);

            // Animation
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setRepeatCount(0);
            alphaAnimation.setDuration(duration);
            mContentView.startAnimation(alphaAnimation);

            ScaleAnimation scaleAnimation = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setRepeatCount(0);
            scaleAnimation.setDuration(duration);
            centerView.startAnimation(scaleAnimation);

        } else {

            // Animation
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimation.setRepeatCount(0);
            alphaAnimation.setDuration(duration);
            mContentView.startAnimation(alphaAnimation);

            ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.1f, 1.0f, 0.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setRepeatCount(0);
            scaleAnimation.setDuration(duration);
            centerView.startAnimation(scaleAnimation);

            // Dismiss
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    StepSharePopupWindow.this.dismiss();
                }
            }, duration);
        }
    }


}
