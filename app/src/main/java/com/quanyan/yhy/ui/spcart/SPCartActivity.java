package com.quanyan.yhy.ui.spcart;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshExpandableListView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.eventbus.EvBusMessageCount;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.spcart.adapter.SPCartAdapter;
import com.quanyan.yhy.ui.spcart.controller.SPCartController;
import com.quanyan.yhy.ui.spcart.dialog.SPCartDialog;
import com.quanyan.yhy.ui.spcart.view.SpCartBottomView;
import com.quanyan.yhy.ui.spcart.view.SpCartEditBottomView;
import com.quanyan.yhy.ui.spcart.view.SpCartTopBarView;
import com.yhy.common.beans.net.model.tm.CartIdsInfo;
import com.yhy.common.beans.net.model.tm.CartInfoListResult;
import com.yhy.common.beans.net.model.tm.CreateOrderContextParamForPointMall;
import com.yhy.common.beans.net.model.tm.DeleteCartInfo;
import com.yhy.common.beans.net.model.tm.ItemInfoResult;
import com.yhy.common.beans.net.model.tm.ItemParamForOrderContext;
import com.yhy.common.beans.net.model.tm.SelectCartInfo;
import com.yhy.common.beans.net.model.tm.ShopInfoResult;
import com.yhy.common.beans.net.model.tm.UpdateAmountCartInfo;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;
import com.yhy.service.IUserService;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created with Android Studio.
 * Title:SPCartActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-10-19
 * Time:10:56
 * Version 1.0
 * Description:
 */
public class SPCartActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2 {

    private SpCartTopBarView mTopBarView;

    @ViewInject(R.id.pull_to_refresh_expand)
    private PullToRefreshExpandableListView mExpandListView;

    @ViewInject(R.id.sp_bttom_view)
    private SpCartBottomView mSpBottomView;
    @ViewInject(R.id.sp_edit_bottom_view)
    private SpCartEditBottomView mSpEditBottomView;

    private SPCartAdapter mSpcartAdapter;

    private int state = 0;

    private SPCartController mSpcartController;
    private CartInfoListResult cartInfoListResult;
    private List<CartIdsInfo> mSelectCartInfo;

    private int clickState = 0;

    private Dialog mDeleteDialog;

    private Dialog mSaveCartDialog;
    private static final int mSaveCartDialogShowTime = 1500;
    private static final int mSaveCartDialogShowWhat = 0x011;

    @ViewInject(R.id.rl_state01)
    private RelativeLayout mStateLayout01;
    @ViewInject(R.id.rl_state02)
    private LinearLayout mStateLayout02;
    @ViewInject(R.id.rl_state02_topview)
    private RelativeLayout mStateTopView;
    @ViewInject(R.id.tv_go_buy)
    TextView mGoBuy;

    @Autowired
    IUserService userService;

    public IUserService getUserService() {
        return userService;
    }

    public static void gotoSPCartActivity(Context context) {
        Intent intent = new Intent(context, SPCartActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        ViewUtils.inject(this);
        mSpBottomView.setVisibility(View.VISIBLE);
        mSpEditBottomView.setVisibility(View.GONE);
        mSpcartController = new SPCartController(this, mHandler);
        mExpandListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mExpandListView.setOnRefreshListener(this);
        mSelectCartInfo = new ArrayList<CartIdsInfo>();

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

        ExpandableListView mex = mExpandListView.getRefreshableView();
        if (mSpcartAdapter == null) {
            mSpcartAdapter = new SPCartAdapter(this, mex);
        }

        mex.setAdapter(mSpcartAdapter);
        mex.setGroupIndicator(null);
        mex.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });


//        mSpcartController.doSelectCart(SPCartActivity.this, new SelectCartInfo());

//        mSpcartController.doGetCartInfoList(this);

        initClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLoadingView("");
        mSpcartController.doGetCartInfoList(this);
    }

    private void initClick() {
        mTopBarView.setBackClickListener(new SpCartTopBarView.BackClickListener() {
            @Override
            public void back() {
                SPCartActivity.this.finish();
            }
        });

        mTopBarView.setEditClickListener(new SpCartTopBarView.EditClickListener() {
            @Override
            public void edit() {
                if (state == 0) {
                    state = 1;
                    mTopBarView.setEditTv("完成");
                    mSpBottomView.setVisibility(View.GONE);
                    mSpEditBottomView.setVisibility(View.VISIBLE);
                    //TODO
                    setCartInfoUnSelect(cartInfoListResult);
                } else {
                    state = 0;
                    mTopBarView.setEditTv("编辑");
                    mSpBottomView.setVisibility(View.VISIBLE);
                    mSpEditBottomView.setVisibility(View.GONE);
                    mSpcartController.doGetCartInfoList(SPCartActivity.this);
                }
            }
        });

        //点击消息按钮
        mTopBarView.setNoticeClickListener(new SpCartTopBarView.NoticeClickListener() {
            @Override
            public void notice() {
                NavUtils.gotoMsgCenter(SPCartActivity.this);
            }
        });

        mSpEditBottomView.setSpCartEditBottomViewClickListener(new SpCartEditBottomView.SpCartEditBottomViewClick() {
            @Override
            public void selectAll() {
                setCartInfoAllSelect(cartInfoListResult);
            }

            @Override
            public void deleteAll() {
                List<CartIdsInfo> mList = getDeleteList(cartInfoListResult);
                if (mList == null || mList.size() == 0) {
                    mSaveCartDialog = SPCartDialog.showSaveToSpcartDialog(SPCartActivity.this, "您还没有选择商品哦~", R.mipmap.save_to_cart_faile_img);
                    mHandler.sendEmptyMessageDelayed(mSaveCartDialogShowWhat, mSaveCartDialogShowTime);
                } else {
                    showDelete("是否删除选中商品？", null, 1, mList);
                }
            }
        });

        mSpcartAdapter.setAdapterClickListener(new SPCartAdapter.SpcartAdapterClick() {
            @Override
            public void groupChecked(ShopInfoResult shopInfoResult) {
                if (state == 0) {//TODO 批量勾选
                    if (shopInfoResult.shopCheck) {
                        for (int i = 0; i < mSelectCartInfo.size(); i++) {
                            for (int j = 0; j < shopInfoResult.itemInfoResultList.size(); j++) {
                                if (mSelectCartInfo.get(i).id == shopInfoResult.itemInfoResultList.get(j).id) {
                                    mSelectCartInfo.remove(i);
                                }
                            }
                        }
                    } else {
                        for (int j = 0; j < shopInfoResult.itemInfoResultList.size(); j++) {
                            boolean isHas = false;
                            for (int i = 0; i < mSelectCartInfo.size(); i++) {
                                if (mSelectCartInfo.get(i).id == shopInfoResult.itemInfoResultList.get(j).id) {
                                    isHas = true;
                                    break;
                                }
                            }
                            if (isHas == false) {
                                CartIdsInfo cartIdsInfo = new CartIdsInfo();
                                cartIdsInfo.id = shopInfoResult.itemInfoResultList.get(j).id;
                                mSelectCartInfo.add(cartIdsInfo);
                            }
                        }
                    }
                    SelectCartInfo info = new SelectCartInfo();
                    info.cartIdsInfoList = mSelectCartInfo;
                    mSpcartController.doSelectCart(SPCartActivity.this, info);
                } else { //TODO 批量选中
                    setGroupSelect(shopInfoResult, cartInfoListResult);
                }
            }

            @Override
            public void childChecked(ItemInfoResult itemInfoResult, ShopInfoResult shopInfoResult) {
                if (state == 0) {
                    if (itemInfoResult != null) {
                        if (itemInfoResult.itemCheck) {
                            for (int i = 0; i < mSelectCartInfo.size(); i++) {
                                if (itemInfoResult.id == mSelectCartInfo.get(i).id) {
                                    mSelectCartInfo.remove(i);
                                }
                            }
                        } else {
                            boolean isHas = false;
                            for (int i = 0; i < mSelectCartInfo.size(); i++) {
                                if (itemInfoResult.id == mSelectCartInfo.get(i).id) {
                                    isHas = true;
                                    break;
                                }
                            }
                            if (isHas == false) {
                                CartIdsInfo info1 = new CartIdsInfo();
                                info1.id = itemInfoResult.id;
                                mSelectCartInfo.add(info1);
                            }
                        }
                        SelectCartInfo info = new SelectCartInfo();
                        info.cartIdsInfoList = mSelectCartInfo;
                        mSpcartController.doSelectCart(SPCartActivity.this, info);
                    }
                } else {
                    setChildSelect(itemInfoResult, shopInfoResult, cartInfoListResult);
                }
            }

            @Override
            public void childReduce(ItemInfoResult itemInfoResult, int num) {
                if (itemInfoResult != null) {
                    UpdateAmountCartInfo updateAmountCartInfo = new UpdateAmountCartInfo();
                    updateAmountCartInfo.amount = num - 1;
                    updateAmountCartInfo.id = itemInfoResult.id;
                    mSpcartController.doUpdateCartAmount(SPCartActivity.this, updateAmountCartInfo);
                }
            }

            @Override
            public void childIncrease(ItemInfoResult itemInfoResult, int num) {
                if (itemInfoResult != null) {
                    UpdateAmountCartInfo updateAmountCartInfo = new UpdateAmountCartInfo();
                    updateAmountCartInfo.amount = num + 1;
                    updateAmountCartInfo.id = itemInfoResult.id;
                    mSpcartController.doUpdateCartAmount(SPCartActivity.this, updateAmountCartInfo);
                }
            }

            @Override
            public void childDelete(final ItemInfoResult itemInfoResult) {
                mDeleteDialog = SPCartDialog.showDeleteDialog(SPCartActivity.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        showDelete("是否删除选中商品？", itemInfoResult, 0, null);
                        if (itemInfoResult != null) {
                            CartIdsInfo cartIdsInfo = new CartIdsInfo();
                            cartIdsInfo.id = itemInfoResult.id;
                            if (mSelectCartInfo != null && mSelectCartInfo.size() != 0) {
                                for (int i = 0; i < mSelectCartInfo.size(); i++) {
                                    if (cartIdsInfo.id == mSelectCartInfo.get(i).id) {
                                        mSelectCartInfo.remove(i);
                                    }
                                }
                            }
                            List<CartIdsInfo> mList = new ArrayList<CartIdsInfo>();
                            mList.add(cartIdsInfo);
                            DeleteCartInfo deleteCartInfo = new DeleteCartInfo();
                            deleteCartInfo.cartIdsInfoList = mList;
                            mSpcartController.doDeleteCart(SPCartActivity.this, deleteCartInfo);
                        }
                        if (mDeleteDialog != null) {
                            mDeleteDialog.dismiss();
                        }
                    }
                });
            }

            @Override
            public void showCouponDialog(ShopInfoResult shopInfoResult) {
                //TODO 显示优惠券弹出框
                NavUtils.gotoSPCartCouponActivity(SPCartActivity.this, shopInfoResult.sellerInfo.sellerId);
            }


            @Override
            public void textClick(int maxNum, int currentNum, final long id) {
                SPCartDialog.showUpdateNumberDialog(SPCartActivity.this, "修改购买数量", "确定", "取消", new SPCartDialog.UpdateNumberDialogClick() {
                    @Override
                    public void config(int num) {
                        UpdateAmountCartInfo updateAmountCartInfo = new UpdateAmountCartInfo();
                        updateAmountCartInfo.amount = num;
                        updateAmountCartInfo.id = id;
                        mSpcartController.doUpdateCartAmount(SPCartActivity.this, updateAmountCartInfo);
                    }
                }, maxNum, 1, currentNum);
            }
        });


        mSpBottomView.setBottomClickListener(new SpCartBottomView.SpCartBottomClick() {
            @Override
            public void checked() {
                if (state == 0) {
                    if (cartInfoListResult.cartCheck) {
                        for (int i = 0; i < mSelectCartInfo.size(); i++) {
                            for (int j = 0; j < cartInfoListResult.shopInfoResultList.size(); j++) {
                                for (int t = 0; t < cartInfoListResult.shopInfoResultList.get(j).itemInfoResultList.size(); t++) {
                                    if (mSelectCartInfo.get(i).id == cartInfoListResult.shopInfoResultList.get(j).itemInfoResultList.get(t).id) {
                                        mSelectCartInfo.remove(i);
                                    }
                                }
                            }
                        }
                    } else {
                        for (int j = 0; j < cartInfoListResult.shopInfoResultList.size(); j++) {
                            for (int t = 0; t < cartInfoListResult.shopInfoResultList.get(j).itemInfoResultList.size(); t++) {
                                boolean isHas = false;
                                for (int i = 0; i < mSelectCartInfo.size(); i++) {
                                    if (mSelectCartInfo.get(i).id == cartInfoListResult.shopInfoResultList.get(j).itemInfoResultList.get(t).id) {
                                        isHas = true;
                                        break;
                                    }
                                }
                                if (isHas == false) {
                                    CartIdsInfo info1 = new CartIdsInfo();
                                    info1.id = cartInfoListResult.shopInfoResultList.get(j).itemInfoResultList.get(t).id;
                                    mSelectCartInfo.add(info1);
                                }
                            }
                        }
                    }
                    SelectCartInfo info = new SelectCartInfo();
                    info.cartIdsInfoList = mSelectCartInfo;
                    mSpcartController.doSelectCart(SPCartActivity.this, info);
                } else {

                }
            }

            @Override
            public void gotoDeal() {
                if (getUserService().isLogin()) {
                    CreateOrderContextParamForPointMall mCreateOrderContextParamForPointMall = getSelectAll(cartInfoListResult);
                    if (mCreateOrderContextParamForPointMall == null) {
                        return;
                    }
                    NavUtils.gotoSPCartOrderActivity(SPCartActivity.this, mCreateOrderContextParamForPointMall);
                } else {
                    NavUtils.gotoLoginActivity(SPCartActivity.this);
                }
            }
        });

        //去购买
        mGoBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到积分商城
                NavUtils.gotoIntegralmallHomeActivity(SPCartActivity.this);
            }
        });

        mStateTopView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.gotoLoginActivity(SPCartActivity.this);
            }
        });
    }


    private SelectCartInfo getGroupSelect(List<ItemInfoResult> itemInfoResultList) {
        if (itemInfoResultList == null || itemInfoResultList.size() == 0) {
            return null;
        }
        SelectCartInfo info = new SelectCartInfo();
        List<CartIdsInfo> mList = new ArrayList<>();
        for (int i = 0; i < itemInfoResultList.size(); i++) {
            CartIdsInfo cartIdsInfo = new CartIdsInfo();
            cartIdsInfo.id = itemInfoResultList.get(i).id;
            mList.add(cartIdsInfo);
        }
        info.cartIdsInfoList = mList;
        return info;
    }


    /**
     * 获取购物车中所有的选择的商品
     *
     * @param cartInfoListResult
     */
    private void getSelectCartInfo(CartInfoListResult cartInfoListResult) {
        if (cartInfoListResult == null || cartInfoListResult.shopInfoResultList == null || cartInfoListResult.shopInfoResultList.size() == 0) {
            return;
        }
        if (mSelectCartInfo == null) {
            mSelectCartInfo = new ArrayList<>();
        } else {
            mSelectCartInfo.clear();
        }
        for (int i = 0; i < cartInfoListResult.shopInfoResultList.size(); i++) {
            for (int j = 0; j < cartInfoListResult.shopInfoResultList.get(i).itemInfoResultList.size(); j++) {
                if (cartInfoListResult.shopInfoResultList.get(i).itemInfoResultList.get(j).itemCheck) {
                    CartIdsInfo cartIdsInfo = new CartIdsInfo();
                    cartIdsInfo.id = cartInfoListResult.shopInfoResultList.get(i).itemInfoResultList.get(j).id;
                    mSelectCartInfo.add(cartIdsInfo);
                }
            }
        }
    }


    private SelectCartInfo getAllSelectCartInfo(CartInfoListResult cartInfoListResult) {
        if (cartInfoListResult == null || cartInfoListResult.shopInfoResultList == null || cartInfoListResult.shopInfoResultList.size() == 0) {
            return null;
        }

        SelectCartInfo info = new SelectCartInfo();
        List<CartIdsInfo> mList = new ArrayList<>();
        for (int i = 0; i < cartInfoListResult.shopInfoResultList.size(); i++) {
            for (int j = 0; j < cartInfoListResult.shopInfoResultList.get(i).itemInfoResultList.size(); j++) {
                CartIdsInfo cartIdsInfo = new CartIdsInfo();
                cartIdsInfo.id = cartInfoListResult.shopInfoResultList.get(i).itemInfoResultList.get(j).id;
                mList.add(cartIdsInfo);
            }
        }

        info.cartIdsInfoList = mList;
        return info;
    }

    private CreateOrderContextParamForPointMall getSelectAll(CartInfoListResult cartInfoListResult) {
        if (cartInfoListResult == null || cartInfoListResult.shopInfoResultList == null || cartInfoListResult.shopInfoResultList.size() == 0) {
            return null;
        }

        List<ItemParamForOrderContext> mList = new ArrayList<>();
        for (int i = 0; i < cartInfoListResult.shopInfoResultList.size(); i++) {
            for (int j = 0; j < cartInfoListResult.shopInfoResultList.get(i).itemInfoResultList.size(); j++) {
                if (cartInfoListResult.shopInfoResultList.get(i).itemInfoResultList.get(j).itemCheck) {
                    ItemParamForOrderContext mItemParamForOrderContext = new ItemParamForOrderContext();
//                    int a = Integer.parseInt(cartInfoListResult.shopInfoResultList.get(i).itemInfoResultList.get(j).buyAmount + "") >
//                            (cartInfoListResult.shopInfoResultList.get(i).itemInfoResultList.get(j).stockNum) ?
//                            (cartInfoListResult.shopInfoResultList.get(i).itemInfoResultList.get(j).stockNum) :
//                            Integer.parseInt(cartInfoListResult.shopInfoResultList.get(i).itemInfoResultList.get(j).buyAmount + "");
//                    int mixBuy = a > 99 ? 99 : a;
                    mItemParamForOrderContext.buyAmount = cartInfoListResult.shopInfoResultList.get(i).itemInfoResultList.get(j).buyAmount;
                    mItemParamForOrderContext.itemId = cartInfoListResult.shopInfoResultList.get(i).itemInfoResultList.get(j).itemId;
                    mList.add(mItemParamForOrderContext);
                }
            }
        }

        CreateOrderContextParamForPointMall mCreateOrderContextParamForPointMall = new CreateOrderContextParamForPointMall();
        mCreateOrderContextParamForPointMall.itemParamList = mList;
        return mCreateOrderContextParamForPointMall;
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activtiy_spcart, null);
    }

    @Override
    public View onLoadNavView() {
        mTopBarView = new SpCartTopBarView(this);
        mTopBarView.setTitle("购物车");
        mTopBarView.setEditTv("编辑");
//        mTopBarView.setmNoticeBtnGone();
        return mTopBarView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideErrorView(null);
        switch (msg.what) {
            case ValueConstants.SPCART_GetCartInfoList_OK:
                //TODO 获取购物车列表数据成功
                mExpandListView.onRefreshComplete();
                hideLoadingView();
                cartInfoListResult = (CartInfoListResult) msg.obj;
                if (state == 0) {
                    if (cartInfoListResult != null) {
                        getSelectCartInfo(cartInfoListResult);
                        mSpBottomView.setBottomChecked(cartInfoListResult.cartCheck);
                        if (cartInfoListResult.shopInfoResultList != null && cartInfoListResult.shopInfoResultList.size() != 0) {
                            mSpcartAdapter.replaceAll(cartInfoListResult.shopInfoResultList);
                            setEmptyLayout(0);
                        } else {
                            mSpcartAdapter.clear();
                            setEmptyLayout(1);
                        }
                        mSpBottomView.setBottomView(cartInfoListResult);
                    } else {
                        setEmptyLayout(1);
                    }
                } else {
                    if (cartInfoListResult != null) {
                        if (cartInfoListResult.shopInfoResultList != null && cartInfoListResult.shopInfoResultList.size() != 0) {
                            mSpcartAdapter.replaceAll(cartInfoListResult.shopInfoResultList);
                            setEmptyLayout(0);
                            setCartInfoUnSelect(cartInfoListResult);
                        } else {
                            mSpcartAdapter.clear();
                            setEmptyLayout(1);
                        }
                    } else {
                        setEmptyLayout(1);
                    }
                }
                break;
            case ValueConstants.SPCART_GetCartInfoList_ERROR:
                //TODO 获取购物车列表数据失败
                hideLoadingView();
                mExpandListView.onRefreshComplete();
                showNetErrorView(msg.arg1);
                break;
            case ValueConstants.SPCART_SelectCart_OK:
                //TODO 购物车商家点击选中状态
                Boolean cartInfo = (Boolean) msg.obj;
                if (cartInfo) {
                    mSpcartController.doGetCartInfoList(this);
                } else {
//                    ToastUtil.showToast(SPCartActivity.this, "勾选失败");
                }
                break;
            case ValueConstants.SPCART_SelectCart_ERROR:

                break;
            case ValueConstants.SPCART_DeleteCart_OK:
                //TODO 批量删除商品成功
                Boolean isDelete = (Boolean) msg.obj;
                if (isDelete) {
                    mSpcartController.doGetCartInfoList(SPCartActivity.this);
                } else {
                    ToastUtil.showToast(SPCartActivity.this, "删除失败");
                }
                break;
            case ValueConstants.SPCART_DeleteCart_ERROR:
                //TODO 批量删除商品失败
                ToastUtil.showToast(SPCartActivity.this, StringUtil.handlerErrorCode(SPCartActivity.this, msg.arg1));
                break;
            case ValueConstants.SPCART_UpdateCartAmount_OK:
                //TODO 修改购物车数量成功
                Boolean isUpdate = (Boolean) msg.obj;
                if (isUpdate) {
                    mSpcartController.doGetCartInfoList(SPCartActivity.this);
                } else {
                    ToastUtil.showToast(SPCartActivity.this, "修改失败");
                }
                break;
            case ValueConstants.SPCART_UpdateCartAmount_ERROR:
                //TODO 修改购物车数量成功
                ToastUtil.showToast(SPCartActivity.this, StringUtil.handlerErrorCode(SPCartActivity.this, msg.arg1));
                break;
            case 0x10:
                mExpandListView.onRefreshComplete();
                break;
            case mSaveCartDialogShowWhat:
                if (mSaveCartDialog != null && mSaveCartDialog.isShowing()) {
                    mSaveCartDialog.dismiss();
                }
                break;
        }
    }

    private void setEmptyLayout(int mState) {
        if (mState == 0) {
            mStateLayout01.setVisibility(View.VISIBLE);
            mStateLayout02.setVisibility(View.GONE);
            if (mTopBarView != null) {
                mTopBarView.setEditBtnVisible();
            }
        } else {
            mStateLayout01.setVisibility(View.GONE);
            mStateLayout02.setVisibility(View.VISIBLE);
            if (getUserService().isLogin()) {
                mStateTopView.setVisibility(View.GONE);
            } else {
                mStateTopView.setVisibility(View.VISIBLE);
            }
            if (mTopBarView != null) {
                mTopBarView.setEditBtnGone();
            }
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        if (state == 0) {
            mSpcartController.doGetCartInfoList(SPCartActivity.this);
        } else {
            mHandler.sendEmptyMessageDelayed(0x10, 1000);
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {

    }


    /**
     * 设置adapter的选中状态为未选中
     *
     * @param mCartInfoListResult
     */
    private void setCartInfoUnSelect(CartInfoListResult mCartInfoListResult) {
        if (mCartInfoListResult == null) {
            return;
        }

        if (mCartInfoListResult.shopInfoResultList == null || mCartInfoListResult.shopInfoResultList.size() == 0) {
            return;
        }

        mCartInfoListResult.cartCheck = false;
        for (int i = 0; i < mCartInfoListResult.shopInfoResultList.size(); i++) {
            mCartInfoListResult.shopInfoResultList.get(i).shopCheck = false;
            for (int t = 0; t < mCartInfoListResult.shopInfoResultList.get(i).itemInfoResultList.size(); t++) {
                mCartInfoListResult.shopInfoResultList.get(i).itemInfoResultList.get(t).itemCheck = false;
            }
        }

        mSpEditBottomView.setAllSelectImage(mCartInfoListResult.cartCheck);
        mSpcartAdapter.notifyDataSetChanged();
    }


    /**
     * 全选或全部取消
     *
     * @param mCartInfoListResult
     */
    private void setCartInfoAllSelect(CartInfoListResult mCartInfoListResult) {
        if (mCartInfoListResult == null) {
            return;
        }

        if (mCartInfoListResult.shopInfoResultList == null || mCartInfoListResult.shopInfoResultList.size() == 0) {
            return;
        }

        if (mCartInfoListResult.cartCheck) {
            mCartInfoListResult.cartCheck = false;
            for (int i = 0; i < mCartInfoListResult.shopInfoResultList.size(); i++) {
                mCartInfoListResult.shopInfoResultList.get(i).shopCheck = false;
                for (int t = 0; t < mCartInfoListResult.shopInfoResultList.get(i).itemInfoResultList.size(); t++) {
                    mCartInfoListResult.shopInfoResultList.get(i).itemInfoResultList.get(t).itemCheck = false;
                }
            }
        } else {
            mCartInfoListResult.cartCheck = true;
            for (int i = 0; i < mCartInfoListResult.shopInfoResultList.size(); i++) {
                mCartInfoListResult.shopInfoResultList.get(i).shopCheck = true;
                for (int t = 0; t < mCartInfoListResult.shopInfoResultList.get(i).itemInfoResultList.size(); t++) {
                    mCartInfoListResult.shopInfoResultList.get(i).itemInfoResultList.get(t).itemCheck = true;
                }
            }
        }
        mSpEditBottomView.setAllSelectImage(mCartInfoListResult.cartCheck);
        mSpcartAdapter.notifyDataSetChanged();
    }

    /**
     * 点击商品的选择框
     *
     * @param itemInfoResult
     * @param shopInfoResult
     * @param mCartInfoListResult
     */
    private void setChildSelect(ItemInfoResult itemInfoResult, ShopInfoResult shopInfoResult, CartInfoListResult mCartInfoListResult) {
        if (itemInfoResult.itemCheck) {
            itemInfoResult.itemCheck = false;
            shopInfoResult.shopCheck = false;
            mCartInfoListResult.cartCheck = false;
        } else {
            itemInfoResult.itemCheck = true;
            boolean isUnGroup = false;
            for (int i = 0; i < shopInfoResult.itemInfoResultList.size(); i++) {
                if (!shopInfoResult.itemInfoResultList.get(i).itemCheck) {
                    isUnGroup = true;
                    break;
                }
            }
            if (isUnGroup) {
                shopInfoResult.shopCheck = false;
            } else {
                shopInfoResult.shopCheck = true;
                boolean isUnAllChoose = false;
                for (int t = 0; t < mCartInfoListResult.shopInfoResultList.size(); t++) {
                    if (!mCartInfoListResult.shopInfoResultList.get(t).shopCheck) {
                        isUnAllChoose = true;
                    }
                }

                if (isUnAllChoose) {
                    mCartInfoListResult.cartCheck = false;
                } else {
                    mCartInfoListResult.cartCheck = true;
                }
            }
        }

        mSpEditBottomView.setAllSelectImage(mCartInfoListResult.cartCheck);
        mSpcartAdapter.notifyDataSetChanged();
    }

    /**
     * 设置商家选择按钮点击
     *
     * @param shopInfoResult
     * @param mCartInfoListResult
     */
    private void setGroupSelect(ShopInfoResult shopInfoResult, CartInfoListResult mCartInfoListResult) {
        if (shopInfoResult.shopCheck) {
            shopInfoResult.shopCheck = false;
            mCartInfoListResult.cartCheck = false;
            for (int i = 0; i < shopInfoResult.itemInfoResultList.size(); i++) {
                shopInfoResult.itemInfoResultList.get(i).itemCheck = false;
            }
        } else {
            shopInfoResult.shopCheck = true;
            for (int i = 0; i < shopInfoResult.itemInfoResultList.size(); i++) {
                shopInfoResult.itemInfoResultList.get(i).itemCheck = true;
            }

            boolean isUnAllChoose = false;
            for (int t = 0; t < mCartInfoListResult.shopInfoResultList.size(); t++) {
                if (!mCartInfoListResult.shopInfoResultList.get(t).shopCheck) {
                    isUnAllChoose = true;
                }
            }

            if (isUnAllChoose) {
                mCartInfoListResult.cartCheck = false;
            } else {
                mCartInfoListResult.cartCheck = true;
            }
        }

        mSpEditBottomView.setAllSelectImage(mCartInfoListResult.cartCheck);
        mSpcartAdapter.notifyDataSetChanged();
    }

    private List<CartIdsInfo> getDeleteList(CartInfoListResult mCartInfoListResult) {
        if (mCartInfoListResult == null) {
            return null;
        }

        List<CartIdsInfo> mList = new ArrayList<CartIdsInfo>();
        for (int i = 0; i < mCartInfoListResult.shopInfoResultList.size(); i++) {
            for (int t = 0; t < mCartInfoListResult.shopInfoResultList.get(i).itemInfoResultList.size(); t++) {
                if (mCartInfoListResult.shopInfoResultList.get(i).itemInfoResultList.get(t).itemCheck) {
                    CartIdsInfo cartIdsInfo = new CartIdsInfo();
                    cartIdsInfo.id = mCartInfoListResult.shopInfoResultList.get(i).itemInfoResultList.get(t).id;
                    mList.add(cartIdsInfo);
                }
            }
        }
        return mList;
    }


    private Dialog mSurErrorDialog;

    private void showDelete(String msg, final ItemInfoResult itemInfoResult, final int type, final List<CartIdsInfo> mList) {
        mSurErrorDialog = DialogUtil.showMessageDialog(this,
                null,
                msg,
                "确定",
                "取消",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mSurErrorDialog != null) {
                            mSurErrorDialog.dismiss();
                        }
                        if (type == 0) {
                            if (itemInfoResult != null) {
                                CartIdsInfo cartIdsInfo = new CartIdsInfo();
                                cartIdsInfo.id = itemInfoResult.id;
                                if (mSelectCartInfo != null && mSelectCartInfo.size() != 0) {
                                    for (int i = 0; i < mSelectCartInfo.size(); i++) {
                                        if (cartIdsInfo.id == mSelectCartInfo.get(i).id) {
                                            mSelectCartInfo.remove(i);
                                        }
                                    }
                                }
                                List<CartIdsInfo> mList = new ArrayList<CartIdsInfo>();
                                mList.add(cartIdsInfo);
                                DeleteCartInfo deleteCartInfo = new DeleteCartInfo();
                                deleteCartInfo.cartIdsInfoList = mList;
                                mSpcartController.doDeleteCart(SPCartActivity.this, deleteCartInfo);
                            }
                        } else {
                            if (mList != null && mList.size() != 0) {
                                DeleteCartInfo deleteCartInfo = new DeleteCartInfo();
                                deleteCartInfo.cartIdsInfoList = mList;
                                mSpcartController.doDeleteCart(SPCartActivity.this, deleteCartInfo);
                            }
                        }
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mSurErrorDialog != null) {
                            mSurErrorDialog.dismiss();
                        }
                    }
                });
        mSurErrorDialog.show();
    }


    private void showNetErrorView(int arg1) {
        showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == arg1 ?
                IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
            @Override
            public void onClick(View view) {
                showLoadingView("");
                mSpcartController.doGetCartInfoList(SPCartActivity.this);
            }
        });
    }


    public void onEvent(EvBusMessageCount evBusMessageCount) {
        int count = evBusMessageCount.getCount();
        mTopBarView.updateIMMessageCount(count);
    }
}
