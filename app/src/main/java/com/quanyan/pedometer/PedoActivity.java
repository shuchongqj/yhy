package com.quanyan.pedometer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.newyhy.views.StepSharePopupWindow;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.eventbus.EvBusRecPointsStatus;
import com.quanyan.yhy.eventbus.EvBusShareSuccess;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.integralmall.integralmallcontroller.IntegralmallController;
import com.quanyan.yhy.ui.shop.ShopHomePageActivity;
import com.yhy.common.utils.SPUtils;
import com.yhy.service.IUserService;

import de.greenrobot.event.EventBus;

public class PedoActivity extends BaseActivity {
    private StepFragment2 mStepFragment;
    public static String EXTRA_MAIN_PAGE_INDEX = "extra_main_page_index";
    private int index = -1;
    private IntegralmallController mController;
    private StepSharePopupWindow stepSharePopupWindow;
    private boolean showShare;

    @Autowired
    IUserService userService;

    public IUserService getUserService() {
        return userService;
    }

    /**
     * 跳转计步器首页
     *
     * @param context
     */
    public static void gotoPedometerActivity(Context context) {
        gotoPedometerActivity(context, false);
    }

    /**
     * 跳转计步器首页直接弹出分享步数
     *
     * @param context
     */
    public static void gotoPedometerActivity(Context context, boolean showShare) {
        Intent intent = new Intent(context, PedoActivity.class);
        intent.putExtra("showShare", showShare);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mStepFragment != null) {
            mStepFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(SPUtils.isShareOK(this)){
//            doShareDailySteps();
//            SPUtils.setShareOK(this,false);
//        }
//        de.greenrobot.event.EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        de.greenrobot.event.EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mController = new IntegralmallController(this, mHandler);

        setTitleBarBackground(Color.WHITE);
        //初始化页面数
        index = getIntent().getIntExtra(EXTRA_MAIN_PAGE_INDEX, -1);
        showShare = getIntent().getBooleanExtra("showShare", false);

        FragmentManager fm = getSupportFragmentManager();
        mStepFragment = new StepFragment2();
        fm.beginTransaction().replace(R.id.container, mStepFragment, "pedometer").commit();

        if (showShare){
            mBaseNavView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    share();
                }
            }, 1000);
        }
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_pedo, null);
    }

    private BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(getString(R.string.title_pedometer));
        mBaseNavView.setRightImg(R.mipmap.icon_top_share_nobg);
        mBaseNavView.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBaseNavView.setRightImgClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getUserService().isLogin()) {
                    //事件统计
                    Analysis.pushEvent(PedoActivity.this, AnEvent.ZXSJ_SHARE);
                    share();
                } else {
                    NavUtils.gotoLoginActivity(PedoActivity.this);
                }
            }
        });
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    /**
     * 设置标题
     *
     * @param titleText
     */
    public void setTitleText(String titleText) {
        if (mBaseNavView != null) {
            mBaseNavView.setTitleText(titleText);
        }
    }

    @Override
    protected void onDestroy() {
        if (stepSharePopupWindow != null && stepSharePopupWindow.isShowing()) {
            stepSharePopupWindow.dismiss();
        }
        super.onDestroy();
    }

    /**
     * 分享当前步数
     */
    private void share() {
//        NavUtils.gotoShareActivity(this, ShareActivity.SHARE_STEPS, -1);
        stepSharePopupWindow = new StepSharePopupWindow(this);
        stepSharePopupWindow.showOrDismiss(mBaseNavView);
//        CreditNotification c= new CreditNotification();
//        c.credit = 5;
//        c.description = "songjifen";
//        c.notification = "songjifen";
//        if(c!=null){
//				Intent i=new Intent(context,SigendDialogActivity.class);
//				i.putExtra(SPUtils.EXTRA_DATA,c);
//				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				context.startActivity(i);
//            ScorePopupWindow scorePopupWindow = ScorePopupWindow.makeText(BaseApplication.getTopActivity(), c);
//            scorePopupWindow.show();

//        }else{
//        }
    }

    /**
     * 领取积分状态
     *
     * @param evBusRecPointsStatus
     */
    public void onEvent(EvBusRecPointsStatus evBusRecPointsStatus) {
        if (evBusRecPointsStatus != null) {
            if ("1".equals(evBusRecPointsStatus.getStatus())) {
                //领取成功
                DialogUtil.showRevPointsDialog(this, R.mipmap.ic_rev_points_ok, ShopHomePageActivity.TIME_INTERVAL_COUPON_DISPLAY, false);
            } else if ("2".equals(evBusRecPointsStatus.getStatus())) {
                //领取过
                DialogUtil.showRevPointsDialog(this, R.mipmap.ic_rev_points_ok, ShopHomePageActivity.TIME_INTERVAL_COUPON_DISPLAY, false);
            } else if ("3".equals(evBusRecPointsStatus.getStatus())) {
                //领取失败
//                DialogUtil.showRevPointsDialog(this,R.mipmap.ic_rev_points_ko, ShopHomePageActivity.TIME_INTERVAL_COUPON_DISPLAY,false);
            }
            mStepFragment.reLoadPointByStep();
        }
    }

    public void onEvent(EvBusShareSuccess evBusShareSuccess) {
        doShareDailySteps();
    }

    /**
     * 分享成功调用接口增加积分
     */
    private void doShareDailySteps() {
        mController.doShareDailySteps(this);
    }
}
