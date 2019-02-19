package com.quanyan.yhy.ui.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.shop.controller.ShopController;
import com.quanyan.yhy.ui.tab.homepage.logistics.LogisticsFragment;
import com.quanyan.yhy.view.tabscrollview.PluginScrollView;
import com.yhy.common.beans.net.model.tm.PackageDetail;
import com.yhy.common.beans.net.model.tm.PackageDetailResult;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

public class LogisticsActivity extends BaseActivity {

    @ViewInject(R.id.viewpagerLayout)
    private ViewPager viewPager;
    @ViewInject(R.id.view_line)
    private View viewLine;
    @ViewInject(R.id.horizontalScrollView)
    PluginScrollView mPluginScrollView;
    BaseNavView mBaseNavView;
    List<String> scrollTabList = new ArrayList<>();
    LogisticsFragment[] mLogisticsFragment;
    private ShopController mController;
    private long mOrderId;

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        initView();
    }


    public static void gotoLogisticsActivity(Context context, long orderId) {
        Intent intent = new Intent(context, LogisticsActivity.class);
        intent.putExtra(SPUtils.EXTRA_ID, orderId);
        context.startActivity(intent);
    }

    public void initView() {
        mController = new ShopController(this, mHandler);
        mOrderId = getIntent().getLongExtra(SPUtils.EXTRA_ID, -1);
         GetGoodLogisticPacket(mOrderId);
       // testUi();
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        hideErrorView(null);
        switch (msg.what) {
            case ValueConstants.MSG_GET_ORDER_PACKET_LIST_OK:
                if (msg.obj != null) {
                    PackageDetailResult mOrderDetailResult = (PackageDetailResult) msg.obj;
                    updateUi(mOrderDetailResult);
                }
                break;
            case ValueConstants.MSG_GET_ORDER_PACKET_LIST_KO:
                ToastUtil.showToast(LogisticsActivity.this, (String) msg.obj);
                showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                        IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
                    @Override
                    public void onClick(View view) {
                        GetGoodLogisticPacket(mOrderId);
                    }
                });

                break;
        }
    }

    private void showNoDataPage() {
        showErrorView(viewPager, IActionTitleBar.ErrorType.EMPTYVIEW, getString(R.string.label_nodata_wonderful_play_list), getString(R.string.label_nodata_wonderfulplay_list_message), "", null);
    }


    public void testUi() {
        mLogisticsFragment = new LogisticsFragment[10];
        for (int i = 10; i >0; i--) {
            scrollTabList.add("包裹" + i);
            mLogisticsFragment[i-1] = LogisticsFragment.newInstance(null);
        }
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mLogisticsFragment[position];
            }

            @Override
            public int getCount() {
                return mLogisticsFragment.length;
            }
        });
        viewPager.setOnPageChangeListener(MOnPageChangeListener);

        mPluginScrollView.setTestList(scrollTabList);
        mPluginScrollView.setViewPager(viewPager);
    }

    public void updateUi(PackageDetailResult mPackageDetailResult) {
        List<PackageDetail> packageDetailList = mPackageDetailResult.packageDetail;
        if (packageDetailList != null) {
            if (packageDetailList.size() == 1) {
                mPluginScrollView.setVisibility(View.INVISIBLE);
                viewLine.setVisibility(View.INVISIBLE);
                mLogisticsFragment = new LogisticsFragment[1];
                mLogisticsFragment[0] = LogisticsFragment.newInstance(packageDetailList.get(0));
            } else {
                mLogisticsFragment = new LogisticsFragment[packageDetailList.size()];
                for (int i = packageDetailList.size(); i >0; i--) {
                    scrollTabList.add("包裹" + i);
                    mLogisticsFragment[i-1] = LogisticsFragment.newInstance(packageDetailList.get(i-1));
                }
            }
            viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    return mLogisticsFragment[position];
                }

                @Override
                public int getCount() {
                    return mLogisticsFragment.length;
                }
            });
            viewPager.setOnPageChangeListener(MOnPageChangeListener);

            mPluginScrollView.setTestList(scrollTabList);
            mPluginScrollView.setViewPager(viewPager);
        } else {
            showNoDataPage();
        }


    }

    ViewPager.OnPageChangeListener MOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int arg0) {
            mPluginScrollView.buttonSelected(arg0);
            viewPager.setCurrentItem(arg0);


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    };


    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_logistics_activity, null);
    }

    @Override
    public View onLoadNavView() {
        if (mBaseNavView == null) {
            mBaseNavView = new BaseNavView(this);
        }
        mBaseNavView.setTitleText(getString(R.string.label_title_logistics_detail));
        return mBaseNavView;

    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    public void GetGoodLogisticPacket(long orderId) {
        showLoadingView(getString(R.string.loading_text));
        mController.doQueryPackageBuyOrder(LogisticsActivity.this, orderId);
    }
}
