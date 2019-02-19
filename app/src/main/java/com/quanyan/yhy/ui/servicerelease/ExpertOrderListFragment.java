package com.quanyan.yhy.ui.servicerelease;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import com.quanyan.base.BaseListViewFragment;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.service.controller.OrderController;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.servicerelease.controller.ManageInfoController;
import com.quanyan.yhy.ui.servicerelease.helper.OrderListHelper;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_ProcessQueryParam;
import com.yhy.common.base.NoLeakHandler;
import com.yhy.common.beans.net.model.tm.ProcessOrder;
import com.yhy.common.beans.net.model.tm.ProcessOrderList;
import com.yhy.common.constants.ValueConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:***Acitivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:王东旭
 * Date:2016/6/1
 * Time:16:46
 * Version 2.0
 */
public class ExpertOrderListFragment extends BaseListViewFragment implements NoLeakHandler.HandlerCallback {
    ManageInfoController mManageInfoController;
    int mPageNO = 1;
    int mPageSize = 10;
    public List<ProcessOrder> mProcessOrderList;
    @Override
    public void fetchData(int pageIndex) {
    }

    /**
     * 重新加载数据
     */
    public void reloadData(){
        isPullRefresh = true;
        mPageNO = 1;
        doControllerRequest(getType());
    }

    /**
     * 数据加载
     * @param type
     */
    public void doControllerRequest(int type) {
        showLoadingView(null);

        Api_TRADEMANAGER_ProcessQueryParam param = new Api_TRADEMANAGER_ProcessQueryParam();
        param.pageNo = mPageNO;
        param.pageSize = mPageSize;
        param.processStatus = new ArrayList<>();
        param.processStatus.add(getParamType());
        if (getParamType() != null && getParamType().equals(ValueConstants.ORDER_STATUS_FINISH)) {
            param.processStatus.add(ValueConstants.ORDER_STATUS_RATED);
        }
        param.processType = new ArrayList<>();
        param.processType.add("CONSULT");
        mManageInfoController.doGetExpertOrderListSell(param);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (!isAdded() || getActivity() == null) {
            return;
        }
        getPullListView().onRefreshComplete();
        hideLoadingView();
        hideErrorView(null);

        switch (msg.what) {
            case ValueConstants.GET_ORDER_LIST_SUCCESS://达人卖家
                ProcessOrderList mProcessOrderList = (ProcessOrderList) msg.obj;
                this.mProcessOrderList = mProcessOrderList.list;
                if (mProcessOrderList != null && mProcessOrderList.list != null) {
                    setData(mProcessOrderList.list);
                    isPullRefresh = false;
                    hasNoMoreData = false;
                } else {
                    hasNoMoreData = true;
                    if (getAdapter().getCount() > 0) {
                        return;
                    }
                    showNoDataPage();
                }
                break;
            case ValueConstants.GET_ORDER_LIST_FAIL:
                if (getAdapter().getCount() > 0) {
                    return;
                }
                ToastUtil.showToast(getActivity(), "" + msg.obj);
                showNetErrorView(null, msg.arg1);
                break;
        }
    }

    /**
     * 显示错误的页面
     * @param o
     * @param arg1
     */
    private void showNetErrorView(Object o, int arg1) {
        showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == arg1 ?
                IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
            @Override
            public void onClick(View view) {
                reloadData();
            }
        });
    }

    public void setData(List list) {
        if (list == null) {
            return;
        }

        if (getAdapter() != null && getAdapter().getCount() % 10 == 0 && !isPullRefresh) {
            getAdapter().addAll(list);
        } else {
            getAdapter().clear();
            getAdapter().replaceAll(list);
        }

        mPageNO = (getAdapter().getCount() / 10) + 1;
    }

    public int getType() {
        int type = getArguments().getInt(OrderController.PARAMS_ORDER_TYPE);
        return type;
    }

    @Override
    public void dispatchMessage(Message msg) {

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        getPullListView().setMode(PullToRefreshBase.Mode.BOTH);
        if (mHandler == null) {
            mHandler = new NoLeakHandler(this);
        }
        mManageInfoController = new ManageInfoController(getActivity(), mHandler);

        doControllerRequest(getType());
    }

    @Override
    public void convertItem(BaseAdapterHelper helper, Object item) {
        if (item instanceof ProcessOrder) {
            OrderListHelper.handleOrderListSellerItem(getActivity(), helper, (ProcessOrder) item, "", getType());
        }
    }

    @Override
    public int setItemLayout() {
        return R.layout.item_expert_order_list_seller;
    }

    @Nullable
    @Override
    public List<String> setTabStrings() {
        return null;
    }

    @Nullable
    @Override
    public List<View> setPopViews() {
        return null;
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        if (getAdapter() != null && getAdapter().getData() != null && getAdapter().getData().get(position - 1) != null) {
            NavUtils.gotoExpertOrderDetailActivityForResult(getActivity(), 1, ((List<ProcessOrder>) getAdapter().getData()).get(position - 1).processOrderId);
        } else {
            ToastUtil.showToast(getActivity(), "服务端数据返回错误");
        }
    }

    public void showNoDataPage() {
        showErrorView(null, IActionTitleBar.ErrorType.EMPTYVIEW, getString(R.string.order_list_empty_message),
                " ", "", null);
    }

    public String getParamType() {
        switch (getType()) {
            case 0:
                return null;
            case 1:
                return ValueConstants.ORDER_STATUS_WAITING_PAY;
            case 2:
                return ValueConstants.ORDER_STATUS_CONSULT_IN_QUEUE;
            case 3:
                return ValueConstants.ORDER_STATUS_CONSULT_IN_CHAT;
            case 4:
                return ValueConstants.ORDER_STATUS_FINISH;
        }
        return "";
    }

    private boolean hasNoMoreData;
    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (getAdapter().getCount() != 0 && getAdapter().getCount() % 10 == 0 && !hasNoMoreData) {
            mPageNO++;
            doControllerRequest(getType());
        } else {
            ToastUtil.showToast(getActivity(), "" + getResources().getString(R.string.scenic_hasnodata));
            mHandler.sendEmptyMessage(300);
        }
    }
    boolean isPullRefresh = false;
    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        isPullRefresh = true;
        mPageNO = 1;
        doControllerRequest(getType());
    }
}
