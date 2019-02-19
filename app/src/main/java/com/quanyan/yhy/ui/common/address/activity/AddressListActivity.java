package com.quanyan.yhy.ui.common.address.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshScrollDeleteListView;
import com.quanyan.base.view.customview.scrolldeletelistview.ScrollDeleteListView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.address.AddressController;
import com.quanyan.yhy.ui.common.address.adapter.AddressInfoAdapter;
import com.quanyan.yhy.ui.common.address.model.DeleteMyAdress;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.order.OrderTopView;
import com.quanyan.yhy.ui.order.PointOrderActivity;
import com.quanyan.yhy.ui.spcart.SPCartOrderActivity;
import com.yhy.common.beans.net.model.common.address.MyAddressContentInfo;
import com.yhy.common.beans.net.model.common.address.MyAddress_ArrayResp;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.AndroidUtils;
import com.yhy.common.utils.SPUtils;
import com.yhy.service.IUserService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:PersonListActivity
 * Description:收货地址
 * Copyright:Copyright (c) 2015
 * Company:和平必胜、正义必胜、人民必胜
 * Author:wjm
 * Date:16/7/1
 * Time:
 * Version 1.3.0
 */
public class AddressListActivity extends BaseActivity {

    private static final String SOURCE = "SOURCE";

    private AddressInfoAdapter mAdapter;

    public RelativeLayout rlBottomAddhint;
    private PullToRefreshScrollDeleteListView mPullToRefreshScrollDeleteListView;
    private int mPageIndex = 1;
    private int mPageSize = 10;
    private AddressController mController;
    private int REQCODE_ADD = 101;
    private int type;
    private long uid;
    private RelativeLayout ll_address;
    private ListView mListView;
    private String source;
    private List<MyAddressContentInfo> deleMyAdress;
    private DeleteMyAdress deleteMyAdress;
    private MyAddressContentInfo mMyAdress;

    @Autowired
    IUserService userService;

    /**
     * 跳转到地址列表
     */
    public static void gotoAddressListActivity(Activity context, int reqCode, String source) {
        Intent intent = new Intent(context, AddressListActivity.class);
        intent.putExtra(SOURCE, source);
        context.startActivityForResult(intent, reqCode);
    }


    /**
     * 删除地址
     */
    private void deleteBack() {
        Intent intent = new Intent();
        if (deleteMyAdress == null) {
            deleteMyAdress = new DeleteMyAdress();
        }
        deleteMyAdress.deleMyAdress = deleMyAdress;
        intent.putExtra(SPUtils.EXTRA_DATA, deleteMyAdress);
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void handleMessage(Message msg) {
        hideLoadingView();
        hideErrorView(null);
        mPullToRefreshScrollDeleteListView.onRefreshComplete();
        switch (msg.what) {
            case ValueConstants.MSG_GET_ADDRESS_LIST_OK:
                handlerAddressList((MyAddress_ArrayResp) msg.obj);
                break;
            case ValueConstants.MSG_GET_ADDRESS_LIST_KO:
                if (mAdapter.getCount() == 0) {
                    mAdapter.clearData();
                    showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                            IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
                        @Override
                        public void onClick(View view) {
                            showLoadingView(getString(R.string.loading_text));
                            mController.doGetAddressList(AddressListActivity.this, uid);
                        }
                    });
                } else {
                    AndroidUtils.showToastCenter(this, StringUtil.handlerErrorCode(this, msg.arg1));
                }

                break;
            case ValueConstants.MSG_DELETE_ADDRESS_OK:
                deleMyAdress.add(mMyAdress);
                mMyAdress = null;
                mController.doGetAddressList(AddressListActivity.this, uid);
                break;
            case ValueConstants.MSG_DELETE_ADDRESS_KO:
                ToastUtil.showToast(AddressListActivity.this, getString(R.string.toast_delete_visitor_failed));
                break;
        }
    }

    private void handlerAddressList(MyAddress_ArrayResp arrayResp) {
        List<MyAddressContentInfo> value = arrayResp.value;
        if (value != null && value.size() > 0) {
//			rlBottomAddhint.setVisibility(View.GONE);
            mAdapter.setData(value);
        } else {
//			rlBottomAddhint.setVisibility(View.VISIBLE);
            mAdapter.clearData();

        }
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_address_list, null);
    }

    private OrderTopView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new OrderTopView(this);
        mBaseNavView.setOrderTopTitle(getString(R.string.title_addresses_list));
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        type = getIntent().getIntExtra(SPUtils.EXTRA_TYPE, -1);
        source = getIntent().getStringExtra(SOURCE);
        uid = userService.getLoginUserId();
        deleMyAdress = new ArrayList<MyAddressContentInfo>();
        mController = new AddressController(this, mHandler);
        mPullToRefreshScrollDeleteListView = (PullToRefreshScrollDeleteListView) findViewById(R.id.lv_content);
        rlBottomAddhint = (RelativeLayout) findViewById(R.id.rl_bottom_view);
        mPullToRefreshScrollDeleteListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mPullToRefreshScrollDeleteListView.setScrollingWhileRefreshingEnabled(!mPullToRefreshScrollDeleteListView.isScrollingWhileRefreshingEnabled());
        mListView = mPullToRefreshScrollDeleteListView.getRefreshableView();
        ll_address = (RelativeLayout) findViewById(R.id.ll_address);
        rlBottomAddhint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewAddress();
            }
        });
        initData();

        showLoadingView(getString(R.string.loading_text));
        mController.doGetAddressList(AddressListActivity.this, uid);
    }

    //新增地址
    private void addNewAddress() {
        NavUtils.gotoAddOrUpdateAddressActivity(this, REQCODE_ADD, null);
    }


    /**
     * 保存选择
     */
    private void saveAddressSelect(MyAddressContentInfo myAddress) {
        Intent intent = new Intent();
        intent.putExtra(SPUtils.EXTRA_DATA, myAddress);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initData() {
        mAdapter = new AddressInfoAdapter(this, type);
        mListView.setDividerHeight(ScreenSize.convertPX2DIP(getApplicationContext(), 1));
        mPullToRefreshScrollDeleteListView.setAdapter(mAdapter);

//		if (type == TYPE_FROM_ORDER) {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (PointOrderActivity.class.getSimpleName().equals(source) || SPCartOrderActivity.class.getSimpleName().equals(source)) {
                    int headerCount = mListView.getHeaderViewsCount();
                    int newPosition = position - headerCount;
                    if (newPosition >= 0) {
                        MyAddressContentInfo myAddress = (MyAddressContentInfo) mAdapter.getItem(newPosition);
                        saveAddressSelect(myAddress);
                    }
                }
            }
        });
//		}

        mAdapter.setOnContactItemClickLsn(new AddressInfoAdapter.OnAddressItemClickLsn() {
            @Override
            public void onEditClick(MyAddressContentInfo myAddress) {
                doAddOrUpdateAddressInfo(myAddress);
            }

            @Override
            public void onDeleteClick(MyAddressContentInfo myAddress) {
                mMyAdress = myAddress;
                doDeleteAddressInfo(myAddress);
            }
        });


        mPullToRefreshScrollDeleteListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollDeleteListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollDeleteListView> refreshView) {
                mController.doGetAddressList(AddressListActivity.this, uid);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollDeleteListView> refreshView) {

            }
        });

        mBaseNavView.setBackClickListener(new OrderTopView.BackClickListener() {
            @Override
            public void clickBack() {
                deleteBack();
            }
        });

    }

    //新增或者更新旅客信息
    private void doAddOrUpdateAddressInfo(MyAddressContentInfo myAddress) {
        NavUtils.gotoAddOrUpdateAddressActivity(this, REQCODE_ADD, myAddress);
    }

    //删除旅客信息
    private void doDeleteAddressInfo(final MyAddressContentInfo myAddress) {
        mController.doDeleteAddress(AddressListActivity.this, myAddress.id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            showLoadingView(getString(R.string.loading_text));
            mController.doGetAddressList(AddressListActivity.this, uid);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            deleteBack();
        }
        return true;
    }
}
