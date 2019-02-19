package com.quanyan.yhy.ui.tab.homepage.order;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

import com.quanyan.base.BaseFragment;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshExpandableListView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.eventbus.EvBusComment;
import com.quanyan.yhy.service.controller.OrderController;
import com.quanyan.yhy.ui.adapter.MyOrderListAdapter;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.integralmall.activity.MyOrderListActivity;
import com.quanyan.yhy.view.NetWorkErrorView;
import com.yhy.common.beans.net.model.tm.TmOrderList;
import com.yhy.common.constants.ValueConstants;

import de.greenrobot.event.EventBus;

/**
 * Created with Android Studio.
 * Title:MyOrderListFragment
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:田海波
 * Date:2016/9/12
 * Time:11:19
 * Version 1.0
 */
public class MyOrderListFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2 {

    public static final String KEY_ORDER_TYPE = "myorderlist_order_type";
    public static final String KEY_ORDER_STATUS = "myorderlist_status_type";

    private PullToRefreshExpandableListView mExpandListView;
    private int pageIndex = 1;
    private OrderController controller;
    /**
     * 当前页面订单类型
     */
    private String orderType;
    private int statusType = 0;
    private MyOrderListAdapter mAdapter;
    private NetWorkErrorView errorView;

    public static MyOrderListFragment newInstance(String orderType, int statusType) {
        MyOrderListFragment myOrderListFragment = new MyOrderListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ORDER_TYPE, orderType);
        bundle.putInt(KEY_ORDER_STATUS, statusType);
        myOrderListFragment.setArguments(bundle);
        return myOrderListFragment;
    }

    public static MyOrderListFragment newInstance(int statusType) {
        MyOrderListFragment myOrderListFragment = new MyOrderListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_ORDER_STATUS, statusType);
        myOrderListFragment.setArguments(bundle);
        return myOrderListFragment;
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(getActivity(), R.layout.base_pull_refresh_layout_expandlistview, null);
    }

    public int getType() {
        int type = getArguments().getInt(OrderController.PARAMS_ORDER_TYPE);
        return type;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(EvBusComment mEvBusComment) {
        sendGetOrderListTask(true);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            orderType = bundle.getString(KEY_ORDER_TYPE);
            statusType = bundle.getInt(KEY_ORDER_STATUS);
        }
        mExpandListView = (PullToRefreshExpandableListView) view.findViewById(R.id.pull_to_refresh_expand);
        mExpandListView.setOnRefreshListener(this);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        init();
    }

    Dialog confirmDialog;
    private MyOrderListAdapter.OnOrderClickListener onOrderClickListener = new MyOrderListAdapter.OnOrderClickListener() {

        @Override
        public void onOrderCancel(long orderId, String orderType) {
            controller.showCancelOrderDialog(getActivity(), orderId, orderType);
        }

        @Override
        public void onOrderConfirm(final long orderId) {
            confirmDialog = DialogUtil.showMessageDialog(getActivity(), null, getString(R.string.label_confirm_order), getString(R.string.label_btn_ok), getString(R.string.label_btn_cancel), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirmDialog.dismiss();
                    showLoadingView("确认收货");
                    controller.confirmOrder(getActivity(), orderId);

                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirmDialog.dismiss();
                }
            });

            confirmDialog.show();

        }
    };

    private void init() {
        // mExpandListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mExpandListView.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
//                        ImageLoader.getInstance().resume();
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                    case SCROLL_STATE_FLING:
//                        ImageLoader.getInstance().pause();
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        errorView = new NetWorkErrorView(getActivity());
        mExpandListView.setEmptyView(errorView);
        if (controller == null) {
            controller = new OrderController(getActivity(), mHandler);
        }
        ExpandableListView mex = mExpandListView.getRefreshableView();
        if (mAdapter == null) {
            mAdapter = new MyOrderListAdapter(getActivity(), mex);
        }
        mAdapter.setOnOrderClickListener(onOrderClickListener);
        mex.setAdapter(mAdapter);
        mex.setGroupIndicator(null);
        mex.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                return true;
            }
        });

        sendGetOrderListTask(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private final static int MSG_GET_ORDER_DETAIL = 0x1011;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            sendGetOrderListTask(true);

            // showLoadingView("");
            //  mHandler.sendEmptyMessageDelayed(MSG_GET_ORDER_DETAIL, 2000);
        }
    }

    public void sendGetOrderListTask(boolean isRefresh) {
        int page = pageIndex;
        if (isRefresh) {
            page = 1;
        } else {
            page++;
        }
        if (mAdapter.getCount() == 0) {
            errorView.show(R.drawable.ic_loading, getString(R.string.loading_text), null, null, null);
        }

        controller.doGetOrderList(getActivity(), orderType, statusType, ValueConstants.PAGESIZE, page);
    }

    public void refreshData(String orderType) {
        this.orderType = orderType;
        int page = 1;
       // if (mAdapter.getCount() == 0) {
           // errorView.show(R.drawable.ic_loading, getString(R.string.loading_text), null, null, null);
       // }
        Bundle bundle = getArguments();
        if (bundle != null) {
            statusType = bundle.getInt(KEY_ORDER_STATUS);
        }
        controller.doGetOrderList(getActivity(), orderType, statusType, ValueConstants.PAGESIZE, page);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (getActivity() == null || getActivity().isFinishing())
            return;
        int what = msg.what;
        int page = msg.arg2;
        hideLoadingView();
        hideErrorView(null);
        if (what == ValueConstants.GET_ORDER_LIST_SUCCESS) {
            mExpandListView.onRefreshComplete();
            if (msg.obj != null) {
                TmOrderList tmOrderList = (TmOrderList) msg.obj;
                if (tmOrderList.list != null && tmOrderList.list.size() > 0) {
                    if (page == 1) {
                        mAdapter.clear();
                        mAdapter.replaceAll(tmOrderList.list);
                    } else {
                        //其他页 添加数据
                        mAdapter.addAll(tmOrderList.list);
                    }
                    pageIndex = page;
                } else {
                    if (mAdapter.getCount() != 0) {
                        if (page != 1) {
                            ToastUtil.showToast(getActivity(), R.string.scenic_hasnodata);
                        } else {
                            mAdapter.clear();
                        }
                    }
                }

                //当请求的列表数据为空或者条目小于pagesize时 将listview 上滑拉去更多关闭
//                ((MyOrderListActivity) getActivity()).showOrderCount(statusType, tmOrderList.totalCount);
            }
            if (mAdapter.getCount() == 0) {
                mExpandListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                String message = getString(R.string.order_list_empty_message);
                errorView.show(R.mipmap.error_empty_icon, message, null, null, null);
            } else {
                mExpandListView.setMode(PullToRefreshBase.Mode.BOTH);
            }
        } else if (what == ValueConstants.GET_ORDER_LIST_FAIL) {
            mExpandListView.onRefreshComplete();
            if (mAdapter.getCount() == 0) {
//                if (ErrorCode.NETWORK_UNAVAILABLE == msg.arg1) {
//                    errorView.show(R.mipmap.error_network_unavailable, getString(R.string.error_view_network_unavailable), getString(R.string.error_view_network_unavailable_notice), null, null);
//                } else {
//                    errorView.show(R.mipmap.network_error, getString(R.string.error_view_network_loaderror_title), getString(R.string.error_view_network_loaderror_content), null, null);
                showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                        IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
                    @Override
                    public void onClick(View view) {
                        sendGetOrderListTask(true);
                    }
                });
//                }
            } else {
                ToastUtil.showToast(getActivity(), StringUtil.handlerErrorCode(getActivity(), msg.arg2));
            }
        } else if (what == ValueConstants.CANCLE_ORDER_SUCCESS) {
            if (orderType == null) {
                sendGetOrderListTask(true);
            } else {
                ((MyOrderListActivity) getActivity()).refreshAllOrder();
            }

        } else if (what == ValueConstants.CANCLE_ORDER_FAIL) {
            ToastUtil.showToast(getActivity(), StringUtil.handlerErrorCode(getActivity(), msg.arg1));

        } else if (what == ValueConstants.CONFIRM_RECIVER_SUCCESS) {
            if (orderType == null) {
                sendGetOrderListTask(true);
            } else {
                ((MyOrderListActivity) getActivity()).refreshAllOrder();
            }

        } else if (what == ValueConstants.CONFIRM_RECIVER_FAIL) {
            ToastUtil.showToast(getActivity(), StringUtil.handlerErrorCode(getActivity(), msg.arg1));
        } else if (what == ValueConstants.SHOW_LOADING_VIEW) {
            //显示加载view
            showLoadingView((String) msg.obj);
        } else if (what == MSG_GET_ORDER_DETAIL) {
            sendGetOrderListTask(true);
        }


    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        sendGetOrderListTask(true);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        sendGetOrderListTask(false);
    }

}
