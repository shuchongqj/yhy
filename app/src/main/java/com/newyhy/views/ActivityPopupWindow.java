package com.newyhy.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
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
import android.widget.RelativeLayout;

import com.newyhy.utils.DisplayUtils;
import com.quanyan.yhy.R;
import com.quanyan.yhy.libanalysis.AnArgs;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.yhy.common.beans.net.model.RCShowcase;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * 首页活动广告弹窗
 * Created by yangboxue on 2018/5/10.
 */

public class ActivityPopupWindow extends PopupWindow {

    private ImageView activityImage;
    private ImageView activityCloseBtn;
    private LinearLayout centerView;

    private Activity mActivity;
    private LayoutInflater mInflater;
    private View mContentView;
    private View mParentView;
    private RCShowcase data;
//    private ActivityPopuWindowInterface mInterface;

    public ActivityPopupWindow(Activity activity, RCShowcase data) {

        mActivity = activity;
        this.data = data;

        // 初始化UI
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = mInflater.inflate(R.layout.activity_popu_window, null);
        activityImage = mContentView.findViewById(R.id.activityImage);
        activityCloseBtn = mContentView.findViewById(R.id.iv_close);
        centerView = mContentView.findViewById(R.id.centerView);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) centerView.getLayoutParams();
        lp.width = DisplayUtils.getScreenWidth(activity) * 4 / 5;
        centerView.setLayoutParams(lp);
        LinearLayout.LayoutParams lpImg = (LinearLayout.LayoutParams) activityImage.getLayoutParams();
        lpImg.width = DisplayUtils.getScreenWidth(activity) * 4 / 5;
        activityImage.setLayoutParams(lpImg);

        this.setContentView(mContentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();

        // 设置动画
        // this.setAnimationStyle(R.style.AnimationSpecPopuWindow);

        // 渲染UI
        renderUI(data);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) activityImage.getLayoutParams();
                    lp.width = msg.arg1;
                    lp.height = msg.arg2;
                    activityImage.setLayoutParams(lp);
                    break;
            }
        }
    };

    private void renderUI(final RCShowcase data) {
        // 获取活动图取得宽高比
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 显示网络上的图片
                Bitmap bitmap = null;
                try {

                    BitmapFactory.Options options = new BitmapFactory.Options();

                    /**
                     * 最关键在此，把options.inJustDecodeBounds = true;
                     * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
                     */
                    options.inJustDecodeBounds = true;
                    /**
                     *options.outHeight为原始图片的高
                     */
                    URL myFileUrl = new URL(data.imgUrl);
                    HttpURLConnection conn = (HttpURLConnection) myFileUrl
                            .openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is, null, options);
                    is.close();

                    int width = DisplayUtils.getScreenWidth(mActivity) - DisplayUtils.dp2px(mActivity, 60);
                    int height = width * options.outHeight / options.outWidth;
                    Message message = new Message();
                    message.arg1 = width;
                    message.arg2 = height;
                    message.what = 0;
                    handler.sendMessage(message);   //   在主线程中更新ui操作

                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 活动图
        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(data.imgUrl), activityImage);
        activityImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrDismiss(mParentView);
                //事件统计
                HashMap<String, String> map = AnArgs.Instance().build(AnArgs.OPERATION_CONTENT, data.operationContent).getMap();
                Analysis.pushEvent(mActivity, AnEvent.HOMETANCHUANG, String.valueOf(data.id), map);
                NavUtils.depatchAllJump(mActivity, data, -1);
            }
        });
        // 关闭按钮
        activityCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrDismiss(mParentView);
            }
        });
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
                    ActivityPopupWindow.this.dismiss();
                }
            }, duration);
        }
    }

//    public interface ActivityPopuWindowInterface {
//
//        void enterActivity(Re uri);
//
//    }
}
