package com.quanyan.yhy.ui.line;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.quanyan.base.BaseFragment;
import com.quanyan.base.BaseStickyActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseTransparentNavView;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.stickyview.StickyNavLayout;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.pedometer.ShareActivity;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.consult.controller.ConsultController;
import com.quanyan.yhy.ui.line.lineinterface.IUpdateTab;
import com.quanyan.yhy.ui.line.view.CommodityDetailTopView;
import com.quanyan.yhy.ui.line.view.RightPanelView;
import com.quanyan.yhy.ui.spcart.controller.SPCartController;
import com.quanyan.yhy.ui.spcart.dialog.SPCartDialog;
import com.quanyan.yhy.ui.travel.controller.TravelController;
import com.quanyan.yhy.view.CommodityBottomView;
import com.yhy.common.beans.net.model.item.CityActivityDetail;
import com.yhy.common.beans.net.model.shop.MerchantItem;
import com.yhy.common.beans.net.model.tm.CartAmountResult;
import com.yhy.common.beans.net.model.tm.CreateCartInfo;
import com.yhy.common.beans.net.model.tm.SellerAndConsultStateResult;
import com.yhy.common.beans.net.model.tm.VoucherTemplateResultList;
import com.yhy.common.beans.net.model.trip.LineItemDetail;
import com.yhy.common.beans.net.model.user.UserInfo;
import com.yhy.common.constants.ConsultContants;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;
import com.yhy.service.IUserService;

import java.util.ArrayList;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:商品详情页
 * <p/>
 * <li> 只接受以下类型{@link ItemType#TOUR_LINE},{@link ItemType#FREE_LINE},
 * {@link ItemType#CITY_ACTIVITY},{@link ItemType#NORMAL}, {@link ItemType#POINT_MALL},
 * {@link ItemType#MASTER_CONSULT_PRODUCTS}
 * </li>
 * </P>
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/2/27
 * Time:09:14
 * Version 1.0
 */
public class CommodityDetailActivity extends BaseStickyActivity implements
        CommodityBottomView.ExchangeData<Object>, View.OnClickListener, StickyNavLayout.StickyLayoutScrollY,
        BaseTransparentNavView.TransNavInterface {

    private CommodityBottomView mCommodityBottomView;

    private ViewGroup mTopView;

    private TravelController mTravelController;
    private ConsultController mConsultController;

    private SPCartController mSPCartController;

    private long mLineId = -1;
    private MerchantItem mMerchantItem = null;
    private LineItemDetail mLineDetail = null;
    private CityActivityDetail mCityActivityDetail = null;

    private RightPanelView mRightPanelView;
    private String mPageType;
    private boolean isPublish;//咨询达人商品服务，是否发布状态

    private Dialog mSaveCartDialog;
    private static final int mSaveCartDialogShowTime = 1000;
    private static final int mSaveCartDialogShowWhat = 0x011;

    private boolean isFirstLoad = true;
    private int addAmount = 0;
    @Autowired
    IUserService userService;

    public IUserService getUserService() {
        return userService;
    }
    /**
     * 营养师咨询
     *
     * @param context
     * @param lineId
     * @param pageType {@link ItemType#TOUR_LINE},{@link ItemType#FREE_LINE},
     *                 {@link ItemType#CITY_ACTIVITY},{@link ItemType#NORMAL},
     *                 {@link ItemType#POINT_MALL}, {@link ItemType#MASTER_CONSULT_PRODUCTS}
     */
    public static void gotoFreeWalkerAndPackageTourActivity(Context context, String pageType, long lineId) {
        Intent intent = new Intent(context, CommodityDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(SPUtils.EXTRA_ID, lineId);
        bundle.putString(SPUtils.EXTRA_TYPE, pageType);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param pageType  {@link ItemType#TOUR_LINE},{@link ItemType#FREE_LINE},
     *                  {@link ItemType#CITY_ACTIVITY},{@link ItemType#NORMAL},
     *                  {@link ItemType#POINT_MALL}, {@link ItemType#MASTER_CONSULT_PRODUCTS}
     * @param lineId
     * @param isPublish true: 未发布，false：已发布
     */
    public static void gotoProductionDetail(Context context, String pageType, long lineId, boolean isPublish) {
        Intent intent = new Intent(context, CommodityDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(SPUtils.EXTRA_ID, lineId);
        bundle.putString(SPUtils.EXTRA_TYPE, pageType);
        bundle.putBoolean(SPUtils.EXTRA_CONSULT_SERVICE_STATE, isPublish);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle arg0) {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mLineId = bundle.getLong(SPUtils.EXTRA_ID);
            mPageType = bundle.getString(SPUtils.EXTRA_TYPE, "");
            isPublish = bundle.getBoolean(SPUtils.EXTRA_CONSULT_SERVICE_STATE, false);
        }

        if (TextUtils.isEmpty(mPageType)) {
//            throw new IllegalArgumentException("商品类型不能为空!!!");
        }
        super.onCreate(arg0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLineDetail != null && mCommodityBottomView != null) {
            if (mLineDetail.userInfo != null
                    && mLineDetail.userInfo.id == userService.getLoginUserId()) {
                mCommodityBottomView.setVisibility(View.GONE);
                if (mTopView instanceof CommodityDetailTopView) {
                    ((CommodityDetailTopView) mTopView).hideChoosePackLayout(false);
                }
            } else {
                mCommodityBottomView.setVisibility(View.VISIBLE);
            }
            resetViewHeight();
        } else if (mMerchantItem != null && mCommodityBottomView != null) {
            if (mMerchantItem.userInfo != null
                    && mMerchantItem.userInfo.id == userService.getLoginUserId()) {
                mCommodityBottomView.setVisibility(View.GONE);
                if (mTopView instanceof CommodityDetailTopView) {
                    ((CommodityDetailTopView) mTopView).hideChoosePackLayout(false);
                } else if (mTopView instanceof MasterConsultDetailTopView) {

                }
            } else if (mPageType == ItemType.MASTER_CONSULT_PRODUCTS && mMerchantItem.userInfo != null && mMerchantItem.userInfo.serviceUserId == userService.getLoginUserId()) {
                // TODO: 16/8/2 咨询师自己不显示
                mCommodityBottomView.setVisibility(View.GONE);
            } else {
                mCommodityBottomView.setVisibility(View.VISIBLE);
            }
            resetViewHeight();
        } else if (mCityActivityDetail != null && mCommodityBottomView != null) {
            if (mCityActivityDetail.userInfo != null
                    && mCityActivityDetail.userInfo.id == userService.getLoginUserId()) {
                mCommodityBottomView.setVisibility(View.GONE);
                if (mTopView instanceof CommodityDetailTopView) {
                    ((CommodityDetailTopView) mTopView).hideChoosePackLayout(false);
                }
            } else {
                mCommodityBottomView.setVisibility(View.VISIBLE);
            }
            resetViewHeight();
        }

        //购物车
        if (ItemType.POINT_MALL.equals(mPageType) && !isFirstLoad) {
            mSPCartController.doSelectCartAmount(this);
        }

    }

    @Override
    protected void initOthers() {
        setTitleBarBackground(Color.TRANSPARENT);
        mPullToRefreshSticyView.setMode(PullToRefreshBase.Mode.DISABLED);
        mBaseTransparentNavView.getShareView().setVisibility(View.VISIBLE);
        if (ItemType.FREE_LINE.equals(mPageType) || ItemType.FREE_LINE_ABOARD.equals(mPageType)) {
            mBaseTransparentNavView.setTitleText(R.string.label_title_free_walk);
        } else if (ItemType.TOUR_LINE.equals(mPageType) || ItemType.TOUR_LINE_ABOARD.equals(mPageType)) {
            mBaseTransparentNavView.setTitleText(R.string.label_title_tour_group);
        } else if (ItemType.NORMAL.equals(mPageType)) {
            mBaseTransparentNavView.setTitleText(R.string.label_title_must_buy);
        } else if (ItemType.CITY_ACTIVITY.equals(mPageType)) {
            mBaseTransparentNavView.setTitleText(R.string.label_title_city_activity);
        } else if (ItemType.POINT_MALL.equals(mPageType)) {
            mBaseTransparentNavView.setTitleText(R.string.label_integralmall_details_title);
        } else if (ItemType.MASTER_CONSULT_PRODUCTS.equals(mPageType)) {
            mBaseTransparentNavView.setTitleText(R.string.label_title_master_consult);
        }
        mBaseTransparentNavView.showBottomDivid(false);

        mStickyNavLayout.setScrollListener(this);
        mTravelController = new TravelController(this, mHandler);
        mConsultController = new ConsultController(this, mHandler);
        mSPCartController = new SPCartController(this, mHandler);

        mCommodityBottomView = new CommodityBottomView(this);
//        setBottomView();

        if (!(ItemType.NORMAL.equals(mPageType) || ItemType.CITY_ACTIVITY.equals(mPageType)
                || ItemType.POINT_MALL.equals(mPageType) || ItemType.MASTER_CONSULT_PRODUCTS.equals(mPageType)
                || ItemType.MASTER_PRODUCTS.equals(mPageType))) {
            mRightPanelView = new RightPanelView(this);
            addRightPanel(mRightPanelView);
        }

        manualRefresh();
        mContentViewPager.setOffscreenPageLimit(3);

        getNavParentView().measure(
                View.MeasureSpec.makeMeasureSpec(
                        ((ViewGroup) getNavParentView().getParent()).getMeasuredWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.base_sticky_right_panel);
        linearLayout.setPadding(0,
                getNavParentView().getMeasuredHeight(),
                0,
                0);

        mContentViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (getPullToRefreshSticyView() != null
                        && getPullToRefreshSticyView().getRefreshableView() != null
                        && !getPullToRefreshSticyView().getRefreshableView().isTopHidden) {
                    if (mFragments != null && mFragments.get(position) != null && mFragments.get(position).getView() != null) {
                        ViewGroup view = (ViewGroup) mFragments.get(position).getView().findViewById(R.id.id_stickynavlayout_innerscrollview);
                        getPullToRefreshSticyView().getRefreshableView().resetScroll(view);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setBottomView() {
        addBottomView(mCommodityBottomView);
        mCommodityBottomView.setExchangeData(this);
        if (ItemType.MASTER_PRODUCTS.equals(mPageType)) {
            mCommodityBottomView.hideOtherLayout();
            mCommodityBottomView.showChatPrivateLayout();
            mCommodityBottomView.showConsultLayout();
        } else if (ItemType.MASTER_CONSULT_PRODUCTS.equals(mPageType)) {
            mCommodityBottomView.showMasterConsultView();
            mCommodityBottomView.setPurchaseLayoutClick(mOnClickListener);
            mCommodityBottomView.setVisibility(View.GONE);
        } else if (ItemType.POINT_MALL.equals(mPageType)) {
//            mCommodityBottomView.setPurchaseText(getString(R.string.label_integralmall_exchange) + "");
            //显示购物车按钮
            /****************购物车入口**********************/
            mCommodityBottomView.setPurchaseText(getString(R.string.label_tv_buy_now) + "");
            mCommodityBottomView.showAddSpcartBtn();
            mCommodityBottomView.showSpcartBtn();
            /*********************************************/

            mCommodityBottomView.setAddSPCartListener(new CommodityBottomView.AddSPCartListener() {
                @Override
                public void add() {
                    if (mMerchantItem != null) {
                        if (addAmount < mMerchantItem.itemVO.stockNum) {
                            addAmount++;
                            CreateCartInfo createCartInfo = new CreateCartInfo();
                            createCartInfo.amount = 1;
                            createCartInfo.title = mMerchantItem.itemVO.title;
                            createCartInfo.picList = mMerchantItem.itemVO.picUrls;
                            createCartInfo.itemId = mMerchantItem.itemVO.id;
                            createCartInfo.itemType = mMerchantItem.itemVO.itemType;
                            createCartInfo.sellerId = mMerchantItem.itemVO.sellerId;
                            mSPCartController.doSaveToSpcart(createCartInfo, CommodityDetailActivity.this);
                        } else {
                            ToastUtil.showToast(CommodityDetailActivity.this, "库存不足");
                        }

                    } else {
//                        ToastUtil.showToast(CommodityDetailActivity.this, getString(R.string.pay_dataerror));
                    }
                }
            });
        }
    }

    @Override
    public View addTopView() {
        mTopView = CommodityDetailUtil.setTopView(this, mPageType);
        return mTopView;
    }

    private BaseTransparentNavView mBaseTransparentNavView;

    @Override
    public View onLoadNavView() {
        mBaseTransparentNavView = new BaseTransparentNavView(this);
        return mBaseTransparentNavView;
    }

    @Override
    public boolean isTopCover() {
        return true;
    }

    @Override
    public ArrayList<Fragment> setFragments() {
        return CommodityDetailUtil.getFragment(mLineId, mPageType);
    }

    @Override
    protected ArrayList<String> setPagerTitles() {
        // TODO: 16/7/29 设置tab的标题
        String[] titles;
        if (ItemType.MASTER_PRODUCTS.equals(mPageType)) {
            titles = getResources().getStringArray(R.array.master_detail_tabs);
        } else if (ItemType.MASTER_CONSULT_PRODUCTS.equals(mPageType)) {
            titles = getResources().getStringArray(R.array.master_consult_tabs);
        } else {
            titles = getResources().getStringArray(R.array.commodity_tabs);
        }
        ArrayList<String> strings = new ArrayList<>();
        strings.add(titles[0]);
        strings.add(titles[1]);
        strings.add(String.format(titles[2], 0).substring(0, 2));
        return strings;
    }

    @Override
    public void fetchData(int pageIndex) {
        // TODO: 16/2/29 从网络中获取数据
        CommodityDetailUtil.fetchData(this, mTravelController, mLineId, mPageType);
        if (1 == pageIndex) {
            //刷新当前页的Fragment
            int tabIndex = mContentViewPager.getCurrentItem();
            if (mFragments.get(tabIndex) instanceof IUpdateTab) {
                ((IUpdateTab) mFragments.get(tabIndex)).updateTabContent();
            } else {
                ToastUtil.showToast(this, "未实现IUpdateTab接口");
//                throw new ClassCastException("未实现IUpdateTab接口");
            }
        }
    }

    @Override
    public void updateTabData() {
        super.updateTabData();
        // TODO: 16/2/29 调用onPullRefresh刷新列表时，更新TAB的数据
    }

    @Override
    public void onTabClick(View view, int position) {
        // TODO: 16/2/29 点击TAB标签页的时候调用
        if (!mStickyNavLayout.isTopHidden) {
            switch (position) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    if (mFragments.get(position) instanceof IUpdateTab) {
                        ((IUpdateTab) mFragments.get(position)).updateTabContent();
                    }
                    break;
            }
            mFragments.get(position).getView().findViewById(R.id.id_stickynavlayout_innerscrollview).scrollTo(0, 0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.coupon_choose_layout:
                dealDetailData();
                break;
        }
    }

    public void dealDetailData() {
        long itemId = -1;
        if (mLineDetail != null) {
            itemId = mLineDetail.itemVO.id;
        } else if (mMerchantItem != null) {
            itemId = mMerchantItem.itemVO.id;
        } else if (mCityActivityDetail != null) {
            itemId = mCityActivityDetail.itemVO.id;
        }
        NavUtils.gotoSellCouponAcitvity(this, itemId, 2);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideLoadingView();
        mPullToRefreshSticyView.onRefreshComplete();
        switch (msg.what) {
            case ValueConstants.MSG_LINE_DETAIL_OK:
                hideErrorView(null);
                if (ItemType.FREE_LINE.equals(mPageType) || ItemType.TOUR_LINE.equals(mPageType) ||
                        ItemType.FREE_LINE_ABOARD.equals(mPageType) || ItemType.TOUR_LINE_ABOARD.equals(mPageType)) {
                    LineItemDetail lineDetail = (LineItemDetail) msg.obj;
                    mLineDetail = lineDetail;
                    if (lineDetail != null) {
                        if (ValueConstants.ACTIVITY_STATE_INVALID.equals(lineDetail.itemVO.status)) {
                            ToastUtil.showToast(this, getString(R.string.label_product_outdate));
                            finish();
                            return;
                        }

                        setBottomView();
                        if (mCommodityBottomView != null) {
                            if (lineDetail.userInfo != null
                                    && lineDetail.userInfo.id == userService.getLoginUserId()) {
                                mCommodityBottomView.setVisibility(View.GONE);
                                if (mTopView instanceof CommodityDetailTopView) {
                                    ((CommodityDetailTopView) mTopView).hideChoosePackLayout(false);
                                }
                            } else {
                                mCommodityBottomView.setVisibility(View.VISIBLE);
                                if (lineDetail.userInfo != null && !StringUtil.isEmpty(lineDetail.userInfo.serviceCall)) {
                                    mCommodityBottomView.showServicePhoneLayout();
                                } else {
                                    mCommodityBottomView.hideServicePhoneLayout();
                                }
                            }
                            resetViewHeight();
                        }
                        if (mTopView instanceof CommodityDetailTopView) {
                            ((CommodityDetailTopView) mTopView).bindViewData(lineDetail, mPageType);
                        }
                    }
                } else if (ItemType.NORMAL.equals(mPageType) || ItemType.POINT_MALL.equals(mPageType)
                        || ItemType.MASTER_CONSULT_PRODUCTS.equals(mPageType)) {
                    mMerchantItem = (MerchantItem) msg.obj;
                    if (mMerchantItem.itemVO != null) {
                        mPageType = mMerchantItem.itemVO.itemType;
                    }

                    if (mMerchantItem != null) {
                        if (!isPublish && ValueConstants.ACTIVITY_STATE_INVALID.equals(mMerchantItem.itemVO.status)) {
                            ToastUtil.showToast(this, getString(R.string.label_product_outdate));
                            finish();
                            return;
                        }

                        if (ItemType.POINT_MALL.equals(mPageType) || ItemType.NORMAL.equals(mPageType)) {
                            mTravelController.doGetCouponSellerList(this, 10, 1, mLineId);
                            resetTopView();
                        }
                        setBottomView();

                        //购物车
                        if (ItemType.POINT_MALL.equals(mPageType) && isFirstLoad) {
                            isFirstLoad = false;
                            mSPCartController.doSelectCartAmount(this);
                        }

                        if (mCommodityBottomView != null) {
                            long userid = mMerchantItem.userInfo.id;
                            long serviceid = mMerchantItem.userInfo.serviceUserId;
                            long uid = userService.getLoginUserId();
                            boolean is = mPageType.equals(ItemType.MASTER_CONSULT_PRODUCTS);

                            if (mMerchantItem.userInfo != null && mMerchantItem.userInfo.id == userService.getLoginUserId()) {
                                // TODO: 16/8/2 商家自己不显示
                                mCommodityBottomView.setVisibility(View.GONE);
                                if (mTopView instanceof CommodityDetailTopView) {
                                    ((CommodityDetailTopView) mTopView).hideChoosePackLayout(false);
                                }
                            } else if ( is && serviceid == uid) {
                                // TODO: 16/8/2 咨询师自己不显示
                                mCommodityBottomView.setVisibility(View.GONE);
                            } else {
                                mCommodityBottomView.setVisibility(View.VISIBLE);
                                if (!ItemType.MASTER_CONSULT_PRODUCTS.equals(mPageType)) {
                                    if (mMerchantItem.userInfo != null && !StringUtil.isEmpty(mMerchantItem.userInfo.serviceCall)) {
                                        mCommodityBottomView.showServicePhoneLayout();
                                    } else {
                                        mCommodityBottomView.hideServicePhoneLayout();
                                    }
                                }
                                if (ItemType.MASTER_CONSULT_PRODUCTS.equals(mPageType)) {
                                    if (mMerchantItem.itemVO.stockNum == 0
                                            || (mMerchantItem.userInfo != null && "NOTONLINE".equals(mMerchantItem.userInfo.onlineStatus))) {
                                        mCommodityBottomView.setPurchaseClickable(false);
                                    }
                                } else {
                                    if (mMerchantItem.itemVO.stockNum == 0) {
                                        mCommodityBottomView.setPurchaseClickable(false);
                                        if (ItemType.POINT_MALL.equals(mPageType)) {
                                            mCommodityBottomView.setPurchaseText(getString(R.string.label_integralmall_cashing));
                                            mCommodityBottomView.hideAddSpcartBtn();
                                        } else {
                                            mCommodityBottomView.setPurchaseText(getString(R.string.label_integralmall_soldout));
                                        }
                                    }

                                }
                            }
                            resetViewHeight();
                        }
                        if (mTopView != null) {
                            if (mTopView instanceof MasterConsultDetailTopView) {
                                //达人咨询服务详情
                                ((MasterConsultDetailTopView) mTopView).bindViewData(mMerchantItem);
                            } else {
                                //必买，积分详情
                                if (mTopView instanceof CommodityDetailTopView) {
                                    ((CommodityDetailTopView) mTopView).bindViewData(mMerchantItem, mPageType);
                                }
                            }
                        }
                    }
                } else if (ItemType.CITY_ACTIVITY.equals(mPageType)) {
                    final CityActivityDetail cityActivityDetail = (CityActivityDetail) msg.obj;
                    mCityActivityDetail = cityActivityDetail;
                    setBottomView();
                    if (cityActivityDetail != null) {
                        if (mCommodityBottomView != null) {
                            if (cityActivityDetail.userInfo != null
                                    && cityActivityDetail.userInfo.id == userService.getLoginUserId()) {
                                mCommodityBottomView.setVisibility(View.GONE);
                                if (mTopView instanceof CommodityDetailTopView) {
                                    ((CommodityDetailTopView) mTopView).hideChoosePackLayout(false);
                                }
                            } else {
                                mCommodityBottomView.setVisibility(View.VISIBLE);
                                if (!StringUtil.isEmpty(cityActivityDetail.userInfo.serviceCall)) {
                                    mCommodityBottomView.showServicePhoneLayout();
                                } else {
                                    mCommodityBottomView.hideServicePhoneLayout();
                                }
                            }
                            resetViewHeight();
                        }
                        if (mTopView instanceof CommodityDetailTopView) {
                            ((CommodityDetailTopView) mTopView).bindViewData(cityActivityDetail);
                            //地理位置
                            ((CommodityDetailTopView) mTopView).setLocationClick(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    NavUtils.gotoViewBigMapView(CommodityDetailActivity.this,
                                            cityActivityDetail.itemVO.title,
                                            cityActivityDetail.latitude,
                                            cityActivityDetail.longitude,
                                            false,
                                            cityActivityDetail.locationText);
                                }
                            });

                        }
                        if (cityActivityDetail.itemVO != null) {
                            if (ValueConstants.ACTIVITY_STATE_EXPIRED.equals(cityActivityDetail.itemVO.status)) {
                                if (mCommodityBottomView != null) {
                                    mCommodityBottomView.setPurchaseClickable(false);
                                    mCommodityBottomView.setPurchaseText(getString(R.string.active_state_end));
                                }
                            } else if (ValueConstants.ACTIVITY_STATE_INVALID.equals(cityActivityDetail.itemVO.status)) {
                                if (mCommodityBottomView != null) {
                                    mCommodityBottomView.setPurchaseClickable(false);
                                    mCommodityBottomView.setPurchaseText(getString(R.string.label_product_outdate));
                                    ToastUtil.showToast(this, getString(R.string.label_product_outdate));
                                    finish();
                                    return;
                                }
                            }
                        }
                    }
                } else if (ItemType.MASTER_PRODUCTS.equals(mPageType)) {

                }
                break;
            case ValueConstants.MSG_LINE_DETAIL_ERROR:
                showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                        IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
                    @Override
                    public void onClick(View view) {
                        manualRefresh();
                        for (int i = 1; i < mFragments.size(); i++) {
                            if (mFragments.get(i) instanceof IUpdateTab) {
                                ((IUpdateTab) mFragments.get(i)).updateTabContent();
                            } else {
                                ToastUtil.showToast(view.getContext(), "未实现IUpdateTab接口");
                            }
                        }
                    }
                });
//                ToastUtil.showToast(CommodityDetailActivity.this,
//                        StringUtil.handlerErrorCode(getApplicationContext(), msg.arg1));
                break;
            //请求优惠券列表接口返回
            case ValueConstants.COUPON_INFO_SELLER_SUCCESS:
                VoucherTemplateResultList value = (VoucherTemplateResultList) msg.obj;
                if (mTopView instanceof CommodityDetailTopView) {
                    if (value != null && value.value != null && value.value.size() > 0) {
                        //TODO 有优惠券
//                        if (ItemType.POINT_MALL.equals(mPageType)) {
//                            ((CommodityDetailTopView) mTopView).setChooseCouponLayoutVisible(false);//暂时打开优惠卷
//                        } else {
//                            ((CommodityDetailTopView) mTopView).setChooseCouponLayoutVisible(true);
//                        }
                        ((CommodityDetailTopView) mTopView).setChooseCouponLayoutVisible(true);
                    } else {
                        ((CommodityDetailTopView) mTopView).setChooseCouponLayoutVisible(false);
                    }
                }
                break;
            case ValueConstants.COUPON_INFO_SELLER_FAIL:
                if (mTopView instanceof CommodityDetailTopView) {
                    ((CommodityDetailTopView) mTopView).setChooseCouponLayoutVisible(false);
                }
                break;
            case ValueConstants.SELLER_AND_CONSULT_STATE_OK:
                // TODO: 16/8/2 达人咨询服务的状态
                final SellerAndConsultStateResult result = (SellerAndConsultStateResult) msg.obj;
                if (result != null) {
                    if (result.canCreateOrder) {
                        if (isPurchaseClick && mMerchantItem != null) {
                            NavUtils.gotoMasterConsultActivity(this, mLineId, mMerchantItem.itemVO.marketPrice, mMerchantItem.itemVO.consultTime);
                        }
                    } else {
                        if (ConsultContants.ORDER_NOT_FINISH.equals(result.reason)) {
                            if (isPurchaseClick) {
                                notFinishDialog = DialogUtil.showMessageDialog(this, null, getString(R.string.label_finish_current_cousult), getString(R.string.cancel), getString(R.string.label_btn_ok), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        notFinishDialog.dismiss();
                                    }
                                }, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (result.processOrder != null && result.processOrder.buyerInfo != null) {
                                            NavUtils.gotoMessageActivity(v.getContext(), result.processOrder.consultUserInfo.userId, null, result.processOrder.itemId);
                                        }
                                        notFinishDialog.dismiss();
                                    }
                                });
                                notFinishDialog.setCanceledOnTouchOutside(true);
                                notFinishDialog.show();
                            } else {
                                mCommodityBottomView.setPurchaseClickable(false);
                            }
                        } else if (ConsultContants.TALENT_NOT_ONLINE.equals(result.reason)) {
                            if (isPurchaseClick) {
                                DialogUtil.showMessageDialog(this, null, getString(R.string.dialog_advice_no_fromsale), getString(R.string.dialog_advice_bt), null, null, null, null, null).show();
                            }
                            mCommodityBottomView.setPurchaseClickable(false);
                        } else if (ConsultContants.ITEM_NOT_AVAILABLE.equals(result.reason)) {
                            if (isPurchaseClick) {
                                DialogUtil.showMessageDialog(this, null, getString(R.string.dialog_item_not_avaliable), getString(R.string.dialog_advice_bt), null, null, null, null, null).show();
                            }
                            mCommodityBottomView.setPurchaseClickable(false);
                        }
                    }
                }
                isPurchaseClick = false;
                break;
            case ValueConstants.SELLER_AND_CONSULT_STATE_KO:
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;
            case ValueConstants.SPCART_SaveToCart_OK:
                //TODO 加入购物车成功
                Boolean isSuccess = (Boolean) msg.obj;
                if (isSuccess) {
                    mSaveCartDialog = SPCartDialog.showSaveToSpcartDialog(CommodityDetailActivity.this, "加入购物车成功!", R.mipmap.save_to_cart_success_img);
                    mSPCartController.doSelectCartAmount(this);
                    mHandler.sendEmptyMessageDelayed(mSaveCartDialogShowWhat, mSaveCartDialogShowTime);
                } else {
                    ToastUtil.showToast(CommodityDetailActivity.this, "加入购物车失败");
                }
                break;
            case ValueConstants.SPCART_SaveToCart_ERROR:
                //TODO 加入购物车失败
//                if (msg.arg1 == ApiCode.CART_NUM_29000005) {
//                    ToastUtil.showToast(CommodityDetailActivity.this, "购物车已塞满，先把选好的商品下单吧");
//                } else {
//                    ToastUtil.showToast(CommodityDetailActivity.this, StringUtil.handlerErrorCode(this, msg.arg1));
//                }
                break;
            case ValueConstants.SPCART_SelectCartAmount_OK:
                //TODO 获取购物车数量成功
                CartAmountResult cartAmountResult = (CartAmountResult) msg.obj;
                if (cartAmountResult != null) {
                    mCommodityBottomView.showSpCartNum(cartAmountResult.amount);
                }
                break;
            case ValueConstants.SPCART_SelectCartAmount_ERROR:
                //TODO 获取购物车数量失败

                break;
            case mSaveCartDialogShowWhat:
                if (mSaveCartDialog != null && mSaveCartDialog.isShowing()) {
                    mSaveCartDialog.dismiss();
                }
                break;
        }
        for (int fragmentIndex = 0; fragmentIndex < mFragments.size(); fragmentIndex++) {
            if (mFragments.get(fragmentIndex) instanceof BaseFragment) {
                ((BaseFragment) mFragments.get(fragmentIndex)).handleMessage(msg);
            } else {
                //ToastUtil.showToast(this, "未继承BaseFragment");
//                throw new ClassCastException("未继承BaseFragment");
            }
        }
    }

    private boolean isPurchaseClick = false;//达人咨询服务详情的“立即购买”点击事件（其他地方不用// ）
    private Dialog notFinishDialog;//达人咨询服务详情的（其他地方不用// ）

    @Override
    public Object getData() {
        // TODO: 16/3/29 传递给CommonBottomView中的数据（点击客服,立即购买）
        if (ItemType.FREE_LINE.equals(mPageType) || ItemType.TOUR_LINE.equals(mPageType) ||
                ItemType.FREE_LINE_ABOARD.equals(mPageType) || ItemType.TOUR_LINE_ABOARD.equals(mPageType)) {
            return mLineDetail;
        } else if (ItemType.NORMAL.equals(mPageType) || ItemType.POINT_MALL.equals(mPageType)
                || ItemType.MASTER_CONSULT_PRODUCTS.equals(mPageType)) {
            return mMerchantItem;
        } else if (ItemType.CITY_ACTIVITY.equals(mPageType)) {
            return mCityActivityDetail;
        } else if (ItemType.MASTER_PRODUCTS.equals(mPageType)) {

        }
        return null;
    }

    @Override
    public UserInfo getUserInfo() {
        // TODO: 16/3/29  传递给CommonBottomView中的数据（点击客服）
        return new UserInfo();
    }

    @Override
    public String getCommodityType() {
        return mPageType;
    }

    public RightPanelView getRightPanelView() {
        return mRightPanelView;
    }

    public StickyNavLayout getStickyNavLayout() {
        return mStickyNavLayout;
    }

    @Override
    public void scrollY(boolean isHidden, int scrollY) {
        mBaseTransparentNavView.showTitleText();
        if (scrollY >= 360) {
            setTitleBarBackground(Color.WHITE);
            mBaseTransparentNavView.showBottomDivid(true);
            mBaseTransparentNavView.getLeftImg().setImageResource(R.mipmap.arrow_back_gray);
            mBaseTransparentNavView.getShareView().setImageResource(R.mipmap.icon_top_share_nobg);
        } else {
            mBaseTransparentNavView.showBottomDivid(false);

            mBaseTransparentNavView.getLeftImg().setImageResource(R.mipmap.scenic_arrow_back_white);
            mBaseTransparentNavView.getShareView().setImageResource(R.mipmap.icon_top_share_gray_bg);
            int alpha = (int) (255 * (scrollY / 360.0));
            setTitleBarBackground(Color.argb(alpha, 0xff, 0xff, 0xff));
            mBaseTransparentNavView.setTitleTextColor2(Color.argb(alpha, 0x00, 0x00, 0x00));
        }
    }

    @Override
    public void shareClick() {
        // TODO: 16/8/1 导航栏的分享
        if (ItemType.MASTER_CONSULT_PRODUCTS.equals(mPageType)) {
            if (mMerchantItem != null) {
                NavUtils.gotoShareActivity(this, ShareActivity.SHARE_TYPE_MASTER_CONSULT, -1, mMerchantItem);
            }
        } else if (ItemType.POINT_MALL.equals(mPageType)) {
            if (mMerchantItem != null) {
                NavUtils.gotoShareActivity(this, ShareActivity.SHARE_TYPE_POINT_PRODUCT, -1, mMerchantItem);
            }
        } else {
            if (mLineDetail != null || mCityActivityDetail != null || mMerchantItem != null) {
                CommodityDetailUtil.share(this, mLineId, mLineDetail, mMerchantItem, mCityActivityDetail, mPageType);
            }
        }
    }


    @Override
    public void collectClick() {
        // TODO: 16/8/1 导航栏的收藏
    }

    @Override
    public void praiseClick(ImageView imageView) {
        // TODO: 16/8/1 导航栏的点赞
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO: 16/8/2 达人咨询服务详情的“立即咨询”的点击事件 （独立事件，不建议使用）
            isPurchaseClick = true;
            showLoadingView("");
            getConsultState(v.getContext(), mLineId);
        }
    };

    /**
     * 获取达人的状态
     */
    private void getConsultState(final Context context, final long lineId) {
        if (isPurchaseClick && !getUserService().isLogin()) {
            NavUtils.gotoLoginActivity(this);
            return;
        }
        mConsultController.querySellerAndConsultState(context, lineId);
    }

}
