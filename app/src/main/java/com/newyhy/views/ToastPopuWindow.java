package com.newyhy.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.newyhy.utils.DisplayUtils;
import com.quanyan.yhy.R;

/**
 * Created by yangboxue on 2018/5/15.
 */

public class ToastPopuWindow extends PopupWindow {

    private ImageView toastImage;
    private TextView toastTitle;
    private TextView toastSubTitle;
    private LinearLayout centerView;

    private Activity mActivity;
    private LayoutInflater mInflater;
    private View mContentView;
    private View mParentView;

    public static ToastPopuWindow makeText(Activity activity, String title, String subTitle) {
        return new ToastPopuWindow(activity, title, subTitle);
    }

    // 普通Toast
    public ToastPopuWindow(Activity activity, String title, String subTitle) {
        if (activity.isFinishing())
            return;
        init(activity);
        renderUI_Common(title, subTitle);
    }

    /**
     * 初始化
     *
     * @param activity Activity
     */
    private void init(Activity activity) {
        mActivity = activity;

        // 初始化UI
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = mInflater.inflate(R.layout.alert_toast_view, null);
        centerView = mContentView.findViewById(R.id.centerView);
        toastImage = mContentView.findViewById(R.id.toastImage);
        toastTitle = mContentView.findViewById(R.id.toast_title);
        toastSubTitle = mContentView.findViewById(R.id.toast_subtitle);

        setContentView(mContentView);
        setWidth(DisplayUtils.getScreenWidth(mActivity));
        setHeight(DisplayUtils.getScreenHeight(mActivity));
        setFocusable(true);
        setOutsideTouchable(true);
        update();
    }

    public ToastPopuWindow parentView(View parentView) {
        mParentView = parentView;
        return this;
    }

    public void show() {
        show(null);
    }

    private void renderUI_Common(String title, String subTitle) {
//        int resId = -1;
//        switch (type) {
//            case AlertViewType_OK: {
//                resId = R.mipmap.icon_toast_ok;
//                break;
//            }
//            case AlertViewType_Warning: {
//                resId = R.mipmap.icon_toast_warning;
//                break;
//            }
//            case AlertViewType_Error: {
//                resId = R.mipmap.icon_toast_error;
//                break;
//            }
//            case AlterViewType_Pray: {
//                resId = R.mipmap.icon_toast_like;
//                break;
//            }
//            case AlterViewType_Add_Cart: {
//                resId = R.mipmap.ic_add_cart_error;
//                break;
//            }
//        }
//
//        if (resId == -1) return;

        toastImage.setImageResource(R.mipmap.ic_share_cb_unchecked);
        toastTitle.setText(title);
        toastSubTitle.setText(subTitle);
    }

    private void show(View parent) {

        if (parent == null) {
            if (mParentView != null) {
                parent = mParentView;
            } else {
                parent = mActivity.getWindow().getDecorView();
            }
        }

        if (!this.isShowing()) {
            // Appear
            this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            dismissMySelf(1000);
        }
    }

    private void dismissMySelf(long delayMillis) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(centerView, "scaleX", 1, 0.8f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(centerView, "scaleY", 1, 0.8f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(centerView, "alpha", 1, 0);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setStartDelay(delayMillis);
        animatorSet.setDuration(300);
        animatorSet.play(scaleX).with(scaleY).before(alpha);
        animatorSet.start();

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (isShowing()) {
                    try {
                        dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

}
