package com.quanyan.yhy.ui.spcart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.CouponStatus;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.shop.controller.ShopController;
import com.quanyan.yhy.ui.spcart.helper.SpCartHelper;
import com.yhy.common.beans.net.model.tm.VoucherTemplateResult;
import com.yhy.common.beans.net.model.tm.VoucherTemplateResultList;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;
import com.yhy.service.IUserService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:SPCartCouponActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-12-20
 * Time:11:38
 * Version 1.0
 * Description:
 */
public class SPCartCouponActivity extends BaseActivity {

    private static final String ID = "ID";
    private RelativeLayout mCouponClose;
    private ListView mCouponListView;
    private QuickAdapter<VoucherTemplateResult> mCouponInfoListAdapter;
    private ShopController mController;
    private long sellerId;

    @ViewInject(R.id.top_layout)
    private LinearLayout mTopLayout;

    @Autowired
    IUserService userService;

    public IUserService getUserService() {
        return userService;
    }

    public static Intent getIntent(Context context, long sellerId) {
        Intent intent = new Intent(context, SPCartCouponActivity.class);
        intent.putExtra(ID, sellerId);
        return intent;
    }

    private void finishActivity() {
        finish();
        SPCartCouponActivity.this.overridePendingTransition(R.anim.push_up_in2, R.anim.push_up_out2);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        sellerId = getIntent().getLongExtra(ID, 0);
        mController = new ShopController(this, mHandler);
        mCouponClose = (RelativeLayout) this.findViewById(R.id.rl_coupon_close);
        mCouponListView = (ListView) this.findViewById(R.id.coupon_listview);

        mCouponInfoListAdapter = new QuickAdapter<VoucherTemplateResult>(this, R.layout.item_coupon_list, new ArrayList<VoucherTemplateResult>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, VoucherTemplateResult item) {
                SpCartHelper.handleSpCartCouponView(helper, item);
            }
        };

        mCouponListView.setAdapter(mCouponInfoListAdapter);

        if (sellerId != 0) {
            loadCouponList(sellerId);
        }

        initClick();
    }

    private void initClick() {
        mCouponClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });

        mTopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });

        mCouponListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (getUserService().isLogin()) {
                    VoucherTemplateResult a = (VoucherTemplateResult) mCouponInfoListAdapter.getItem(position);
                    if (a != null && a.status.equals(CouponStatus.ACTIVE)) {
                        doGenerateVoucher(a.id);
                    } else {
                        ToastUtil.showToast(SPCartCouponActivity.this, "优惠券已抢光");
                    }
                } else {
                    NavUtils.gotoLoginActivity(SPCartCouponActivity.this);
                }
            }
        });
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.dialog_show_coupon, null);
    }

    @Override
    public View onLoadNavView() {
        return null;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    /**
     * 加载优惠券列表
     *
     * @param sellerId
     */
    private void loadCouponList(long sellerId) {
        mController.doQuerySellerVoucherList(getApplicationContext(), sellerId);
    }


    private void doGenerateVoucher(long voucherTemplateId) {
        mController.doGenerateVoucher(this, voucherTemplateId);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            //获取店铺优惠券
            case ValueConstants.MSG_GET_SHOP_COUPON_LIST_OK:
                VoucherTemplateResultList mVoucherTemplateResultList = (VoucherTemplateResultList) msg.obj;
                if (mVoucherTemplateResultList != null) {
                    if (mVoucherTemplateResultList.value != null && mVoucherTemplateResultList.value.size() != 0) {
                        List<VoucherTemplateResult> vList = new ArrayList<>();
                        for (int i = 0; i < mVoucherTemplateResultList.value.size(); i++) {
                            if (mVoucherTemplateResultList.value.get(i).status.equals(CouponStatus.ACTIVE)) {
                                vList.add(mVoucherTemplateResultList.value.get(i));
                            }
                        }
                        mCouponInfoListAdapter.replaceAll(vList);
                    }
                }
                break;
            case ValueConstants.MSG_GET_SHOP_COUPON_LIST_KO:
                ToastUtil.showToast(SPCartCouponActivity.this, StringUtil.handlerErrorCode(SPCartCouponActivity.this, msg.arg1));
                break;
            case ValueConstants.MSG_REC_SHOP_COUPON_OK:
                ToastUtil.showToast(SPCartCouponActivity.this, "恭喜您，领券成功!");
                loadCouponList(sellerId);
                break;
            case ValueConstants.MSG_REC_SHOP_COUPON_KO:
                ToastUtil.showToast(SPCartCouponActivity.this, StringUtil.handlerErrorCode(SPCartCouponActivity.this, msg.arg1));
                break;
        }
    }
}
