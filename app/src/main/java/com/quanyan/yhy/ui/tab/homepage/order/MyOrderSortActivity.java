package com.quanyan.yhy.ui.tab.homepage.order;

import android.os.Bundle;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.views.MineMenuItem;

/**
 * 个人中心 --我的订单列表
 */
public class MyOrderSortActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.order_travel)
    private MineMenuItem order_travel;


    @ViewInject(R.id.order_hotel)
    private MineMenuItem order_hotel;

    @ViewInject(R.id.order_scenic)
    private MineMenuItem order_scenic;


    @ViewInject(R.id.order_souvenir)
    private MineMenuItem order_souvenir;

    @ViewInject(R.id.order_activity)
    private MineMenuItem order_activity;

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.myorderlist_activity, null);
    }

    private BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText("我的订单");
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        initialize();
    }

    private void initialize() {
        order_travel.initItem(R.mipmap.ic_head_travel,R.string.myorder_travel,-1);
        order_hotel.initItem(R.mipmap.ic_head_hotel, R.string.myorder_hotel, -1);
        order_scenic.initItem(R.mipmap.ic_head_scenic, R.string.myorder_scenic, -1);
        order_souvenir.initItem(R.mipmap.ic_head_handceremony, R.string.myorder_souvenir, -1);
        order_activity.initItem(R.mipmap.ic_head_activity, R.string.myorder_activity, -1);

        order_travel.setOnClickListener(this);
        order_hotel.setOnClickListener(this);
        order_scenic.setOnClickListener(this);
        order_souvenir.setOnClickListener(this);
        order_activity.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_travel://旅游订单
//                TCEventHelper.onEvent(this, AnalyDataValue.MY_ORDER_TOUR_CLICK);
//                NavUtils.gotoOrderListActivity(this, OrderController.PARENT_TYPE_TRAVEL);
                break;
            case R.id.order_hotel://酒店订单
//                TCEventHelper.onEvent(this, AnalyDataValue.MY_ORDER_HOTEL_CLICK);
//                NavUtils.gotoOrderListActivity(this, OrderController.PARENT_TYPE_HOTEL);
                break;
            case R.id.order_scenic://景区订单
//                TCEventHelper.onEvent(this, AnalyDataValue.MY_ORDER_SCENIC_CLICK);
//                NavUtils.gotoOrderListActivity(this, OrderController.PARENT_TYPE_SPOT);
                break;
            case R.id.order_souvenir://伴手礼订单
//                TCEventHelper.onEvent(this, AnalyDataValue.MY_ORDER_HANDCERE_CLICK);
//                NavUtils.gotoOrderListActivity(this, OrderController.PARENT_TYPE_HANDCEREMONY);
                break;
            case R.id.order_activity://活动订单
//                TCEventHelper.onEvent(this, AnalyDataValue.MY_ORDER_ACTIVE);
//                NavUtils.gotoOrderListActivity(this, OrderController.PARENT_TYPE_ACTIVITY);
                break;
            default:
                break;
        }
    }


}
