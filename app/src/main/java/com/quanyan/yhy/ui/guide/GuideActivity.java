package com.quanyan.yhy.ui.guide;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.base.SystemBarTintManager;

import com.quanyan.yhy.R;
import com.yhy.common.base.YHYBaseApplication;
import com.yhy.router.YhyRouter;
import com.yhy.router.types.MainActivityFromType;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends Activity implements OnPageChangeListener {
    private static final String TAG = GuideActivity.class.getSimpleName();

    private GuideViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private List<View> views;

    private boolean isHide = false;
    private boolean isWaitingInTwo = false;

    private ImageView indicate_1;
    private ImageView indicate_2;
    private ImageView indicate_3;
    private ImageView indicate_4;
    private ImageView indicate_5;

    private LinearLayout ll_indicate;

    private ImageView goBtn;
    private TextView tvSkip;

    private SystemBarTintManager mSystemBarTintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏的设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            mSystemBarTintManager = new SystemBarTintManager(this);
            mSystemBarTintManager.setStatusBarTintEnabled(true);
            mSystemBarTintManager.setNavigationBarTintEnabled(true);

            mSystemBarTintManager.setTintColor(getResources().getColor(R.color.transparent_black_85));
        }
        hideBottomUIMenu();
        setContentView(R.layout.guide_page);

        try {
            // 初始化页面
            initViews();
        } catch (Exception e) {
            Log.w(TAG, e.toString(), e);
        }

        if (isHide) {
            hideLoginAndRegisterFunc();
        }
    }

    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @SuppressLint("NewApi")
    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(this);

        views = new ArrayList<View>();
        // 初始化引导图片列表
        View view1 = inflater.inflate(R.layout.what_new_one, null);
        views.add(view1);
        ImageView iv_guide_1 = (ImageView) view1.findViewById(R.id.iv_guide_1);
        iv_guide_1.setBackgroundResource(R.mipmap.ic_guide_1);

        View view2 = inflater.inflate(R.layout.what_new_two, null);
        views.add(view2);
        ImageView iv_guide_2 = (ImageView) view2.findViewById(R.id.iv_guide_2);
        iv_guide_2.setBackgroundResource(R.mipmap.ic_guide_2);

        View view3 = inflater.inflate(R.layout.what_new_three, null);
        views.add(view3);
        ImageView iv_guide_3 = (ImageView) view3.findViewById(R.id.iv_guide_3);
        iv_guide_3.setBackgroundResource(R.mipmap.ic_guide_3);

        View view4 = inflater.inflate(R.layout.what_new_three, null);
        views.add(view4);
        ImageView iv_guide_4 = (ImageView) view4.findViewById(R.id.iv_guide_3);
        iv_guide_4.setBackgroundResource(R.mipmap.ic_guide_4);

        View view5 = inflater.inflate(R.layout.what_new_five, null);
        views.add(view5);
        ImageView iv_guide_5 = (ImageView) view5.findViewById(R.id.iv_guide_5);
        iv_guide_5.setBackgroundResource(R.mipmap.ic_guide_5);

        // 初始化Adapter
        vp = (GuideViewPager) findViewById(R.id.viewpager);
        vpAdapter = new ViewPagerAdapter(views, vp);

        vp.setAdapter(vpAdapter);
        // 绑定回调
        vp.setOnPageChangeListener(this);

//        goBtn = (TextView) view5.findViewById(R.id.go_btn);
        goBtn = (ImageView) view5.findViewById(R.id.go_btn);
        goBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //TCEventHelper.onEvent(GuideActivity.this, AnalyDataValue.GUIDE_BUTTON_CLICK);
                gotoMain();
            }
        });

        tvSkip = (TextView) findViewById(R.id.tv_skip);
        tvSkip.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //TCEventHelper.onEvent(GuideActivity.this, AnalyDataValue.GUIDE_BUTTON_CLICK);
                gotoMain();
            }
        });

        ll_indicate = (LinearLayout) findViewById(R.id.ll_indicate);
        indicate_1 = (ImageView) findViewById(R.id.indicate_1);
        indicate_2 = (ImageView) findViewById(R.id.indicate_2);
        indicate_3 = (ImageView) findViewById(R.id.indicate_3);
        indicate_4 = (ImageView) findViewById(R.id.indicate_4);
        indicate_5 = (ImageView) findViewById(R.id.indicate_5);
    }

    @Override
    protected void onDestroy() {
//        releaseResources();
        super.onDestroy();
    }

    private void selectIndicate(int index) {
        switch (index) {
            case 0:
                indicate_1.setImageResource(R.mipmap.guide_page_point_selected);
                indicate_2.setImageResource(R.mipmap.guide_page_point_normal);
                indicate_3.setImageResource(R.mipmap.guide_page_point_normal);
                indicate_4.setImageResource(R.mipmap.guide_page_point_normal);
                indicate_5.setImageResource(R.mipmap.guide_page_point_normal);
                break;
            case 1:
                indicate_2.setImageResource(R.mipmap.guide_page_point_selected);
                indicate_1.setImageResource(R.mipmap.guide_page_point_normal);
                indicate_3.setImageResource(R.mipmap.guide_page_point_normal);
                indicate_4.setImageResource(R.mipmap.guide_page_point_normal);
                indicate_5.setImageResource(R.mipmap.guide_page_point_normal);
                break;
            case 2:
                indicate_5.setImageResource(R.mipmap.guide_page_point_normal);
                indicate_4.setImageResource(R.mipmap.guide_page_point_normal);
                indicate_3.setImageResource(R.mipmap.guide_page_point_selected);
                indicate_2.setImageResource(R.mipmap.guide_page_point_normal);
                indicate_1.setImageResource(R.mipmap.guide_page_point_normal);
                break;
            case 3:
                indicate_4.setImageResource(R.mipmap.guide_page_point_selected);
                indicate_3.setImageResource(R.mipmap.guide_page_point_normal);
                indicate_2.setImageResource(R.mipmap.guide_page_point_normal);
                indicate_1.setImageResource(R.mipmap.guide_page_point_normal);
                indicate_5.setImageResource(R.mipmap.guide_page_point_normal);
                break;
            case 4:
                indicate_4.setImageResource(R.mipmap.guide_page_point_normal);
                indicate_3.setImageResource(R.mipmap.guide_page_point_normal);
                indicate_2.setImageResource(R.mipmap.guide_page_point_normal);
                indicate_1.setImageResource(R.mipmap.guide_page_point_normal);
                indicate_5.setImageResource(R.mipmap.guide_page_point_selected);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        YHYBaseApplication.getInstance().exitAllActivity();
    }

    private void hideLoginAndRegisterFunc() {
        goBtn.setVisibility(View.GONE);
    }

    private void gotoMain() {
        YhyRouter.getInstance().startMainActivity(this, MainActivityFromType.START_FROM_GUIDE);
        finish();
        overridePendingTransition(R.anim.anim_pop_in, R.anim.anim_not_change);
    }

    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    // 当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        if (isHide) {
            if (arg0 == 2 && arg1 == 0.0f && arg2 == 0) {
//                gotoMain();
                if (isWaitingInTwo) {
                    isWaitingInTwo = true;
                } else {
                    finish();
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            }
        }
    }

    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int pageIndex) {
        if (pageIndex == (views.size() - 1)) {
            ll_indicate.setVisibility(View.GONE);
            tvSkip.setVisibility(View.GONE);
        } else {
            ll_indicate.setVisibility(View.VISIBLE);
            tvSkip.setVisibility(View.GONE);

        }
        selectIndicate(pageIndex);
    }
}
