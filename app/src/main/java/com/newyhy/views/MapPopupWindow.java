package com.newyhy.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.newyhy.utils.app_utils.AppInfo;
import com.quanyan.yhy.R;
import com.yhy.common.base.YHYBaseApplication;
import com.yhy.common.utils.AndroidUtils;

import java.util.HashMap;
import java.util.List;

import static com.newyhy.utils.app_utils.AppInfoUtils.mapPaks;

/**
 * Created by yangboxue on 2018/7/6.
 */

public class MapPopupWindow extends PopupWindow {

    private LinearLayout centerView;

    private Activity mActivity;
    private LayoutInflater mInflater;
    private View mContentView;
    private View mParentView;

    public MapPopupWindow(Activity activity, List<AppInfo> apps, HashMap<String, String> map) {

        mActivity = activity;

        // 初始化UI
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = mInflater.inflate(R.layout.dialog_app_list, null);
        centerView = mContentView.findViewById(R.id.centerView);

        this.setContentView(mContentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();

        // 设置动画
        // this.setAnimationStyle(R.style.AnimationSpecPopuWindow);

        // 渲染UI
        renderUI(apps, map);
    }

    private void renderUI(List<AppInfo> apps, HashMap<String, String> map) {
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        for (int i = 0; i < apps.size(); i++) {
            final AppInfo app = apps.get(i);

            // 写死在本地，有些手机拿不到appinfo里的图标和名字   Tom  add
            //"com.baidu.BaiduMap" 百度
            if (app.getPackageName().equals(mapPaks[0])) {
                mContentView.findViewById(R.id.llyt_baidu).setVisibility(View.VISIBLE);
                mContentView.findViewById(R.id.llyt_baidu).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent i = new Intent();
                            i.setAction(Intent.ACTION_VIEW);
                            i.addCategory(Intent.CATEGORY_DEFAULT);
                            i.setData(Uri.parse("baidumap://map/direction?destination=" + map.get("lat") + "," + map.get("lng") + "&mode=driving&coord_type=gcj02"));
                            //启动该页面即可
                            mActivity.startActivity(i);
                        } catch (Exception e) {
                            AndroidUtils.showToast(YHYBaseApplication.getInstance(), "未发现百度地图");
                        }

                        dismiss();
                    }
                });

            }

            //"com.autonavi.minimap" 高德
            if (app.getPackageName().equals(mapPaks[1])) {
                mContentView.findViewById(R.id.llyt_gaode).setVisibility(View.VISIBLE);
                mContentView.findViewById(R.id.llyt_gaode).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent i = new Intent();
                            i.setAction(Intent.ACTION_VIEW);
                            i.addCategory(Intent.CATEGORY_DEFAULT);
                            //将功能Scheme以URI的方式传入data
                            Uri uri = Uri.parse("amapuri://route/plan/?dlat=" + map.get("lat") + "&dlon=" + map.get("lng") + "&dname=" + map.get("address") + "&dev=0&t=0");
                            i.setData(uri);
                            //启动该页面即可
                            mActivity.startActivity(i);
                        } catch (Exception e) {
                            AndroidUtils.showToast(YHYBaseApplication.getInstance(), "未发现高德地图");
                        }

                        dismiss();
                    }
                });

            }

            //"com.tencent.map"  腾讯
            if (app.getPackageName().equals(mapPaks[2])) {
                mContentView.findViewById(R.id.llyt_tencent).setVisibility(View.VISIBLE);
                mContentView.findViewById(R.id.llyt_tencent).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent i = new Intent();
                            i.setAction(Intent.ACTION_VIEW);
                            i.addCategory(Intent.CATEGORY_DEFAULT);
                            //将功能Scheme以URI的方式传入data
                            Uri uri = Uri.parse("qqmap://map/routeplan?type=drive&tocoord=" + map.get("lat") + "," + map.get("lng") + "&policy=1");
                            i.setData(uri);
                            //启动该页面即可
                            mActivity.startActivity(i);
                        } catch (Exception e) {
                            AndroidUtils.showToast(YHYBaseApplication.getInstance(), "未发现腾讯地图");
                        }

                        dismiss();
                    }
                });

            }
        }
    }

    public void showOrDismiss(View parent) {

        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
        if (parent == null) {
            if (mParentView != null) {
                parent = mParentView;
            } else {
                parent = mActivity.getWindow().getDecorView();
            }
        }

        if (parent == null) return;

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
                    dismiss();
                }
            }, duration);
        }
    }
}
