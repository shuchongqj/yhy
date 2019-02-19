package com.quanyan.yhy.ui.coupon.fragment;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.quanyan.base.BaseFragment;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshListView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.CouponStatus;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.coupon.adapter.CouponInfoListAdapter;
import com.quanyan.yhy.ui.coupon.controller.CouponController;
import com.quanyan.yhy.ui.shop.ShopHomePageActivity;
import com.quanyan.yhy.view.NetWorkErrorView;
import com.smart.sdk.api.request.ApiCode;
import com.yhy.common.beans.net.model.tm.VoucherResult;
import com.yhy.common.beans.net.model.tm.VoucherResultList;
import com.yhy.common.beans.net.model.tm.VoucherTemplateResult;
import com.yhy.common.beans.net.model.tm.VoucherTemplateResultList;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;
import com.yhy.service.IUserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:***Acitivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:王东旭
 * Date:2016/6/28
 * Time:20:23
 * Version 2.0
 */
public class CouponInfoListFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2 {
    private int mUserId;
    private PullToRefreshListView mListView;
    private NetWorkErrorView errorView;
    private CouponInfoListAdapter mCouponInfoListAdapter;
    public List<VoucherResult> mList;
    private CouponController mController;
    private long itemId;//商品id 商品代金券使用
    private String mStatus;   // 状态 //NO_USED 未使用，USED 已使用 ，DATE_DUE 已过期
    private int pageSize;//每条页数
    private int pageNo = -1;  // 页码
    private boolean isPullRefresh;
    private boolean canPullDown = true;
    private VoucherResultList mVoucherResultList;

    @Autowired
    IUserService userService;

    private VoucherTemplateResultList mVoucherTemplateResultList;

    public CouponInfoListFragment() {
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(getActivity(), R.layout.fragment_coupon_list_view, null);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mListView = (PullToRefreshListView) view.findViewById(R.id.pull_to_refresh_listview);
        mListView.setOnRefreshListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBundleParams();
        initController();
        mListView.setMode(PullToRefreshBase.Mode.BOTH);

 /*       errorView = new NetWorkErrorView(mContext);

        mListView.setEmptyView(errorView);*/

        mCouponInfoListAdapter = new CouponInfoListAdapter(getActivity(), mList, 0);
        mListView.setAdapter(mCouponInfoListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!userService.isLogin()){
                    NavUtils.gotoLoginActivity(getActivity());
                    return;
                }
                int headerCount = mListView.getRefreshableView().getHeaderViewsCount();

                if (position >= headerCount) {
                    //打点
                    tcEvent((VoucherResult) mCouponInfoListAdapter.getItem(position - headerCount));
                }


                if (mStatus != null && mStatus.equals("SELL") && mCouponInfoListAdapter.getItem(position - headerCount) != null) {
                    mController.doGetGenerateVoucher(getActivity(), ((VoucherResult) mCouponInfoListAdapter.getItem(position - headerCount)).id);
                } else {
                    if (mList != null && mList.size() > 0) {
                        if (CouponStatus.ACTIVE.equals(((VoucherResult) mCouponInfoListAdapter.getItem(position - headerCount)).status)) {
                            NavUtils.gotoShopHomePageActivity(getActivity(), ((VoucherResult) mCouponInfoListAdapter.getItem(position - headerCount)).sellerResult.merchantName, ((VoucherResult) mCouponInfoListAdapter.getItem(position - headerCount)).sellerResult.sellerId);
                        }
                    }

                }

            }
        });
    }

    private void tcEvent(VoucherResult voucherResult) {
        if (voucherResult != null) {
            Map<String, String> map = new HashMap();
            map.put(AnalyDataValue.KEY_CID, voucherResult.id + "");
            map.put(AnalyDataValue.KEY_CNAME, voucherResult.title);
            if (voucherResult.sellerResult != null) {
                map.put(AnalyDataValue.KEY_SELLER_NAME, voucherResult.sellerResult.merchantName);
            }
            map.put(AnalyDataValue.KEY_FULL_PRICE, voucherResult.requirement + "");
            map.put(AnalyDataValue.KEY_REDUCE_PRICE, voucherResult.value + "");
            TCEventHelper.onEvent(getActivity(), AnalyDataValue.MINE_COUPON_LIST_ITEM, map);
        }
    }

    public void getBundleParams() {
        Bundle mBundle = getArguments();
        if (mBundle != null) {
            mStatus = mBundle.getString("status");
            itemId = mBundle.getLong("itemVOid");
            pageSize = mBundle.getInt("pageSize");
            pageNo = mBundle.getInt("pageNo");
        }
        if (pageSize == 0) {
            pageSize = 10;
        }
        if (pageNo == -1 || pageNo == 0) {
            pageNo = 1;
        }

    }

    public void initController() {
        if (mController == null) {
            mController = new CouponController(getActivity(), mHandler);
        }
        exeController();
    }

    public void exeController() {

        if (CouponStatus.SELL.equals(mStatus)) {
            mController.doGetCouponSeller(getActivity(), itemId, pageSize, pageNo);
            pageNo++;
            return;
        }
        if (!StringUtil.isEmpty(mStatus) && pageSize != 0 && pageNo != -1) {
            mController.doGetCouponList(getActivity(), mStatus, pageSize, pageNo);
            pageNo++;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }
        mListView.onRefreshComplete();
        switch (msg.what) {
            case ValueConstants.COUPON_INFO_LIST_SUCCESS:
                mVoucherResultList = (VoucherResultList) msg.obj;
                if (mVoucherResultList != null) {
                    mList = mVoucherResultList.value;
                    mCouponInfoListAdapter.setData(mList, isPullRefresh);
                    isPullRefresh = false;
                    if (mList.size() == 10) {
                        canPullDown = true;
                    } else {
                        // ToastUtil.showToast(getActivity(),""+getResources().getString(R.string.scenic_hasnodata);
                        // mListView.setMode(PullToRefreshBase.Mode.DISABLED);
                    }
                } else {
                    if (mCouponInfoListAdapter.getCount() == 0) {
                        showNoDataPage();
                    } else {
                        ToastUtil.showToast(getActivity(), "" + getResources().getString(R.string.scenic_hasnodata));
                        // mListView.setMode(PullToRefreshBase.Mode.DISABLED);
                    }
                }

                break;
            case ValueConstants.COUPON_INFO_LIST_FAIL:
                showNetErrorView(null, msg.arg1);
                break;
            case ValueConstants.COUPON_INFO_SELLER_SUCCESS:
                mVoucherTemplateResultList = (VoucherTemplateResultList) msg.obj;
                //
                if (mVoucherTemplateResultList != null && mVoucherTemplateResultList.value != null) {
                    mList = new ArrayList<VoucherResult>();
                    VoucherResult mVoucherResult;
                    for (VoucherTemplateResult voucher : mVoucherTemplateResultList.value) {
                        mVoucherResult = new VoucherResult();
                        mVoucherResult.id = voucher.id;
                        mVoucherResult.title = voucher.title;
                        mVoucherResult.voucherType = voucher.voucherType;
                        mVoucherResult.requirement = voucher.requirement;
                        mVoucherResult.value = voucher.value;
                        mVoucherResult.status = voucher.status;
                        mVoucherResult.voucherCode = voucher.voucherCode;
                        mVoucherResult.startTime = voucher.startTime;
                        mVoucherResult.endTime = voucher.endTime;
                        mVoucherResult.sellerResult = voucher.sellerResult;
                        mList.add(mVoucherResult);
                    }

                    mCouponInfoListAdapter.setData(mList, isPullRefresh);
                    isPullRefresh = false;
                    if (mList.size() == 10) {
                        canPullDown = true;
                    } else {
                        //mListView.setMode(PullToRefreshBase.Mode.DISABLED);
                    }
                } else {
                    if (mCouponInfoListAdapter.getCount() == 0) {
                        showNoDataPage();
                    } else {
                        ToastUtil.showToast(getActivity(), "" + getResources().getString(R.string.scenic_hasnodata));
                        //  mListView.setMode(PullToRefreshBase.Mode.DISABLED);
                    }

                }
                break;
            case ValueConstants.COUPON_INFO_SELLER_FAIL:
                break;
            case ValueConstants.COUPON_INFO_GET_SUCCESS:
                DialogUtil.showCouponDialog(getActivity(), R.mipmap.coupon_get_success, ShopHomePageActivity.TIME_INTERVAL_COUPON_DISPLAY);
                //initController();
                break;
            case ValueConstants.COUPON_INFO_GET_FAIL:
                if (ApiCode.HAS_BIND_VOUCHER_11000003 == msg.arg2) {
                    DialogUtil.showCouponDialog(getActivity(), R.mipmap.coupon_get_already, ShopHomePageActivity.TIME_INTERVAL_COUPON_DISPLAY);
                } else {
                    DialogUtil.showCouponDialog(getActivity(), R.mipmap.coupon_get_faile, ShopHomePageActivity.TIME_INTERVAL_COUPON_DISPLAY);
                }

                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        pageNo = 1;
        isPullRefresh = true;
        initController();

    }

    public void showNoDataPage() {
        showErrorView(null, IActionTitleBar.ErrorType.EMPTYVIEW, getString(R.string.empty_coupon), " ", "", null);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (canPullDown) {
            initController();
        } else {
            ToastUtil.showToast(getActivity(), "" + getResources().getString(R.string.scenic_hasnodata));
        }
    }

    protected void showNetErrorView(ViewGroup parent, int arg1) {

    }
}
