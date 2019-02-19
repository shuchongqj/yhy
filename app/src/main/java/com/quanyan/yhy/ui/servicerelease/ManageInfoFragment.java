package com.quanyan.yhy.ui.servicerelease;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;

import com.quanyan.base.BaseListViewFragment;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.eventbus.EvBusManageService;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.servicerelease.controller.ManageInfoController;
import com.quanyan.yhy.ui.servicerelease.helper.OrderListHelper;
import com.yhy.common.base.NoLeakHandler;
import com.yhy.common.beans.net.model.tm.ItemApiResult;
import com.yhy.common.beans.net.model.tm.ItemManagement;
import com.yhy.common.beans.net.model.tm.ItemQueryParam;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created with Android Studio.
 * Title:***Acitivity
 * Description:  我发布的服务fragment
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:王东旭
 * Date:2016/6/1
 * Time:16:46
 * Version 2.0
 */
public class ManageInfoFragment extends BaseListViewFragment<ItemManagement> implements NoLeakHandler.HandlerCallback {
    /**
     * 请求码，编辑状态下立即发布，刷新列表
     */
    public static final int REQUEST_CODE_PUBLISH = 0x10;
    /**
     * 删除操作
     */
    public static final int STATE_DELETE = 4;
    /**
     * 下架操作
     */
    public static final int STATE_UNDO = 3;
    /**
     * 上架操作
     */
    public static final int STATE_PUBLISH = 2;
    private ManageInfoController mManageInfoController;
    private int mPageType;

    public static ManageInfoFragment newInstance(int type) {
        ManageInfoFragment manageInfoFragment = new ManageInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SPUtils.EXTRA_TYPE, type);
        manageInfoFragment.setArguments(bundle);
        return manageInfoFragment;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ManageInfoFragment.REQUEST_CODE_PUBLISH:
                if (Activity.RESULT_OK == resultCode) {
                    manualRefresh();
                }
                break;
        }
    }

    @Override
    public void fetchData(int pageIndex) {
        ItemQueryParam itemQueryParam = new ItemQueryParam();

        itemQueryParam.pageNo = pageIndex;
        itemQueryParam.pageSize = getPageSize();
        itemQueryParam.serviceState = mPageType;
        itemQueryParam.categoryId = 241;
        if (mManageInfoController != null) {
            mManageInfoController.doServiceManageListRequest(itemQueryParam);
        }
    }

    /**
     * /商品上下架上下架
     *
     * @param mItemManagement
     * @param type
     */
    public void doUpdateState(ItemManagement mItemManagement, int type) {
        if (mManageInfoController == null) {
            mManageInfoController = new ManageInfoController(getActivity(), mHandler);
        }
        if (mItemManagement == null) {
            return;
        }
        if (mItemManagement.publishServiceDO != null) {
            ItemQueryParam params = new ItemQueryParam();
            params.state = type;
            params.id = mItemManagement.publishServiceDO.id;
            params.categoryId = mItemManagement.publishServiceDO.categoryType;
            mManageInfoController.doSendONorOFFLineRequest(params);
        } else {
            ToastUtil.showToast(getActivity(), "请求ManageInfoController参数错误");
        }

    }

    @Override
    public void dispatchMessage(Message msg) {
        if (!isAdded() || getActivity() == null) {
            return;
        }
        getPullListView().onRefreshComplete();
        hideErrorView(null);

        switch (msg.what) {
            case ValueConstants.MSG_SERVICE_MANAGE_SUCCESS:
                ItemApiResult result = (ItemApiResult) msg.obj;
                setData(result);
                break;
            case ValueConstants.MSG_SERVICE_MANAGE_FAIL:
                showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                        IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
                    @Override
                    public void onClick(View view) {
                        manualRefresh();
                    }
                });
                break;
            case ValueConstants.MSG_SERVICE_UPDATE_SUCCESS:
                boolean res = (boolean) msg.obj;
                if (res) {
                    EventBus.getDefault().post(new EvBusManageService(true));
                    if(STATE_DELETE != mCurrentState) {
                        if (ManageServiceInfoAcitvity.STATE_MASTER_SERVICEMANAGE_PUBLISH_OVER == mPageType) {
                            ToastUtil.showToast(getActivity(), "下架成功");
                        } else {
                            ToastUtil.showToast(getActivity(), "您的服务已成功发布");
                        }
                    }
                }
                break;
            case ValueConstants.MSG_SERVICE_UPDATE_FAIL:
                if(STATE_DELETE != mCurrentState) {
                    if (ManageServiceInfoAcitvity.STATE_MASTER_SERVICEMANAGE_PUBLISH_OVER == mPageType) {
                        ToastUtil.showToast(getActivity(), "下架失败");
                    } else {
                        ToastUtil.showToast(getActivity(), "发布失败");
                    }
                }
                break;
            case ValueConstants.MSG_HAS_NO_DATA:
                ToastUtil.showToast(getActivity(), getResources().getString(R.string.scenic_hasnodata));
                break;
        }

    }

    public void setData(ItemApiResult result) {
        if (isRefresh()) {
            setHaxNext(true);
            if (result != null && result.itemManagements != null) {
                getAdapter().replaceAll(result.itemManagements);
                if (result.itemManagements.size() < getPageSize()) {
                    getBaseDropListView().getPullRefreshView().setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                } else {
                    getBaseDropListView().getPullRefreshView().setMode(PullToRefreshBase.Mode.BOTH);
                }
            } else {
                getAdapter().clear();
                if (getAdapter().getData().size() == 0) {
                    showNoDataPage();
                }
            }
        } else {
            if (result != null && result.itemManagements != null) {
                getAdapter().addAll(result.itemManagements);
                if (result.itemManagements.size() < getPageSize()) {
                    setHaxNext(false);
                }
            } else {
                setHaxNext(false);
            }
        }
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mManageInfoController = new ManageInfoController(getActivity(), mHandler);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mPageType = bundle.getInt(SPUtils.EXTRA_TYPE);
        }
        manualRefresh();
    }

    OrderListHelper.OnItemEditListener mOnItemEditListener = new OrderListHelper.OnItemEditListener() {
        @Override
        public void delete(ItemManagement mItemManagement) {
            alertDialog(mItemManagement, STATE_DELETE);
        }

        @Override
        public void edit(ItemManagement mItemManagement) {
            if (mItemManagement.publishServiceDO != null && mItemManagement.publishServiceDO.id != 0 && mItemManagement.publishServiceDO.categoryType != 0) {
                NavUtils.gotoReleaseServiceActivity(getActivity(), mItemManagement.publishServiceDO.id, mItemManagement.publishServiceDO.categoryType, REQUEST_CODE_PUBLISH);
            }
        }

        @Override
        public void onLine(ItemManagement mItemManagement) {
            doUpdateState(mItemManagement, STATE_PUBLISH);
        }

        @Override
        public void offLine(ItemManagement mItemManagement) {
            alertDialog(mItemManagement, STATE_UNDO);
        }
    };

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        int headerCount = getBaseDropListView().getListView().getHeaderViewsCount();
        if (position >= headerCount) {
            ItemManagement itemManagement = getAdapter().getItem(position - headerCount);
            NavUtils.gotoProductDetail(getActivity(), ItemType.MASTER_CONSULT_PRODUCTS, itemManagement.itemId, "服务管理详情", true);
        }
    }

    /**
     * EventBus消息通知(上架，下架后刷新列表)
     *
     * @param flag
     */
    public void onEvent(EvBusManageService flag) {
        manualRefresh();
    }

    /**
     * 处理列表布局的item方法
     *
     * @param helper 提供处理布局内容的工具类
     * @param item   item数据对象
     */
    @Override
    public void convertItem(BaseAdapterHelper helper, ItemManagement item) {
        OrderListHelper.handleManageItem(getActivity(), helper, item, mOnItemEditListener, mPageType);
    }

    @Override
    public int setItemLayout() {
        return R.layout.item_manage_service_info;
    }


    @Override
    public List<String> setTabStrings() {
        return null;
    }


    @Override
    public List<View> setPopViews() {
        return null;
    }

    private Dialog mDialog;
    private int mCurrentState;
    public void alertDialog(final ItemManagement mItemManagement, final int type) {
        mCurrentState = type;
        String content = "";
        if (type == STATE_PUBLISH) {
            content = getString(R.string.alert_on_line_tip);
        } else if (type == STATE_UNDO) {
            content = getString(R.string.alert_off_line_tip);
        } else if (type == STATE_DELETE) {
            content = getString(R.string.alert_delete_tip);
        }
        mDialog = DialogUtil.showMessageDialog(getActivity(),
                content,
                "",
                getString(R.string.label_btn_ok),
                getString(R.string.label_btn_cancel),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                        doUpdateState(mItemManagement, type);
                    }
                }, null);
        if (mDialog != null) {
            mDialog.show();
        }
    }

    public void showNoDataPage() {
        showErrorView(null, IActionTitleBar.ErrorType.EMPTYVIEW, getString(R.string.error_text_signed_noservice),
                " ", "", null);
    }
}
