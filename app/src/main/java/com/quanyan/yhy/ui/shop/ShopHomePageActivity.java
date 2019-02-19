package com.quanyan.yhy.ui.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshListView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.CouponStatus;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.common.MerchantType;
import com.quanyan.yhy.common.QueryType;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.shop.controller.ShopController;
import com.quanyan.yhy.ui.shop.helper.ShopViewHelper;
import com.quanyan.yhy.ui.views.MasterTitleTopLineView;
import com.smart.sdk.api.request.ApiCode;
import com.yhy.common.beans.net.model.master.Merchant;
import com.yhy.common.beans.net.model.master.QueryTerm;
import com.yhy.common.beans.net.model.master.QueryTermsDTO;
import com.yhy.common.beans.net.model.tm.VoucherTemplateResult;
import com.yhy.common.beans.net.model.tm.VoucherTemplateResultList;
import com.yhy.common.beans.net.model.trip.ShortItem;
import com.yhy.common.beans.net.model.trip.ShortItemsResult;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.service.IUserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:ShopHomePageActivity
 * Description:商铺首页
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/3/2
 * Time:下午1:18
 * Version 0.9
 */
public class ShopHomePageActivity extends BaseActivity implements AdapterView.OnItemClickListener,
        PullToRefreshBase.OnRefreshListener2<ListView> {
    @ViewInject(R.id.pull_to_refresh_listview)
    private PullToRefreshListView mPullToRefreshListView;
    @ViewInject(R.id.base_pullrefresh_listview_parent_layout)
    private LinearLayout mListViewParent;
    private ListView mListView;
    private QuickAdapter mAdapter;
    //店铺名字
    private TextView mShopNameView;
    //店铺背景
    private ImageView mShopBackgroudView;
    //店铺头像
    private ImageView mShopHeadIconView;
    //客服
    private TextView mStartImView;
    //全部商品的头
    private MasterTitleTopLineView mMtvAllProducts;
    private LinearLayout mCouponGridView;
    private ShopController mController;
    private String mShopName;
    private long mShopId = -1;
    private long mSellerId = -1;
    private String mItemType;
    //店铺商品的分页大小
    private final static int SHOP_PRODUCTS_LIST_SIZE = 10;
    public final static int TIME_INTERVAL_COUPON_DISPLAY = 3 * 1000;
    private boolean isListStyle = true;
    //商家信息
    private Merchant mSellerInfo;

    @Autowired
    IUserService userService;

    public IUserService getUserService() {
        return userService;
    }
     /**
     * 跳转到商铺详情
     * @param context
     * @param shopId
     */
    public static void gotoShopHomePageActivity(Context context, String title, long shopId) {
        Intent intent = new Intent(context, ShopHomePageActivity.class);
        intent.putExtra(SPUtils.EXTRA_ID, shopId);
        intent.putExtra(SPUtils.EXTRA_TITLE, title);
        context.startActivity(intent);
    }

    /**
     * 跳转到商铺详情
     * @param context
     * @param shopId
     */
    public static void gotoMustBuyShopHomePageActivity(Context context, String title, long shopId) {
        Intent intent = new Intent(context, ShopHomePageActivity.class);
        intent.putExtra(SPUtils.EXTRA_ID, shopId);
        intent.putExtra(SPUtils.EXTRA_TITLE, title);
        intent.putExtra(SPUtils.EXTRA_TYPE, ItemType.NORMAL);
        context.startActivity(intent);
    }

    @Override
    public View onLoadContentView() {
        return  View.inflate(this, R.layout.ac_shop_home_page, null);
    }

    private BaseNavView mBaseNavView;
    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);

        mController = new ShopController(this, mHandler);
        mPullToRefreshListView.setScrollingWhileRefreshingEnabled(!mPullToRefreshListView.isScrollingWhileRefreshingEnabled());
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mListView = mPullToRefreshListView.getRefreshableView();

        mShopName = getIntent().getStringExtra(SPUtils.EXTRA_TITLE);
        mShopId = getIntent().getLongExtra(SPUtils.EXTRA_ID, -1);
        mItemType = getIntent().getStringExtra(SPUtils.EXTRA_TYPE);
        if (isListStyle) {
            mAdapter = ShopViewHelper.doSetShopProductVerListAdapter(this, new ArrayList<ShortItem>());
        } else {
            mAdapter = ShopViewHelper.doSetShopProductHorListAdapter(this, new ArrayList<ShortItem>());
        }
        addHeadView();
        initData();
        mListView.setDividerHeight(0);
        mListView.setAdapter(mAdapter);
        mCouponGridView.setVisibility(View.GONE);
        mListView.setOnItemClickListener(this);
        mPullToRefreshListView.setOnRefreshListener(this);
    }

    private void tcEvent(VoucherTemplateResult item) {
        if(item != null){
            Map<String, String> map = new HashMap();
            map.put(AnalyDataValue.KEY_CID, item.id + "");
            map.put(AnalyDataValue.KEY_CNAME, item.title);
            if(item.sellerResult != null){
                map.put(AnalyDataValue.KEY_SELLER_NAME, item.sellerResult.merchantName);
            }
            map.put(AnalyDataValue.KEY_FULL_PRICE, item.requirement + "");
            map.put(AnalyDataValue.KEY_REDUCE_PRICE, item.value + "");
            TCEventHelper.onEvent(this, AnalyDataValue.MINE_COUPON_LIST_ITEM, map);
        }

    }

    /**
     * 处理优惠券点击
     * @param item
     */
    private void  handleCouponItemClick(VoucherTemplateResult item){
        if(item == null){
            return ;
        }
        showLoadingView("");
        if(CouponStatus.ACTIVE.equals(item.status)){
            mController.doGenerateVoucher(this,item.id);
        }
    }

    @Override
    protected void onResume() {
        loadSellerInfo();
        super.onResume();
    }

    /**
     * 刷新客服视觉
     */
    private void loadSellerInfo(){
        if(mSellerInfo == null){
            mStartImView.setVisibility(View.INVISIBLE);
            return ;
        }
        if(mSellerInfo.sellerId == userService.getLoginUserId()){
            mStartImView.setVisibility(View.INVISIBLE);
        }else{
            mStartImView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化商户信息
     */
    private void initData() {
        mController.doGetShopDetailInfo(ShopHomePageActivity.this,mShopId);
    }

    /**
     * 列表增加商铺详情的头
     */
    private void addHeadView() {
        View v = LayoutInflater.from(this).inflate(R.layout.header_shop_home_page, null);
        mShopNameView = (TextView) v.findViewById(R.id.tv_shop_name);
        mShopBackgroudView = (ImageView) v.findViewById(R.id.iv_shop_backgroud);
        mShopHeadIconView = (ImageView) v.findViewById(R.id.iv_shop_head_icon);
        mStartImView = (TextView) v.findViewById(R.id.tv_start_im);
        mMtvAllProducts = (MasterTitleTopLineView) v.findViewById(R.id.mtv_all_products);
        mCouponGridView = (LinearLayout) v.findViewById(R.id.sgv_coupon_list);
        mMtvAllProducts.setTopTextView(getString(R.string.label_all_prodcuts));
        mListView.addHeaderView(v);

        mStartImView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/3/3  跳转IM聊天
                if (!getUserService().isLogin()) {
                    NavUtils.gotoLoginActivity(ShopHomePageActivity.this);
                } else {
                    NavUtils.gotoMessageActivity(ShopHomePageActivity.this, (int)mSellerInfo.sellerId);
                }
            }
        });

        mShopHeadIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到店铺简介
                if(mSellerInfo != null){
                    NavUtils.gotoShopInformationActivity(ShopHomePageActivity.this, mSellerInfo);
                }
            }
        });

        mShopNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到店铺简介
                if(mSellerInfo != null){
                    NavUtils.gotoShopInformationActivity(ShopHomePageActivity.this, mSellerInfo);
                }
            }
        });


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int headerCount = mListView.getHeaderViewsCount();
        if (position < headerCount  ) {
            return;
        }
        ShortItem lineInfo = (ShortItem) mAdapter.getItem(position - headerCount);
        if(ItemType.TOUR_LINE.equals(lineInfo.itemType) ||
                ItemType.TOUR_LINE_ABOARD.equals(lineInfo.itemType) ||
                ItemType.FREE_LINE.equals(lineInfo.itemType) ||
                ItemType.FREE_LINE_ABOARD.equals(lineInfo.itemType) ||
                ItemType.CITY_ACTIVITY.equals(lineInfo.itemType) ||
                ItemType.NORMAL.equals(lineInfo.itemType) ||
                ItemType.POINT_MALL.equals(lineInfo.itemType)){
            NavUtils.gotoProductDetail(this,
                    lineInfo.itemType,
                    lineInfo.id,
                    lineInfo.title);
        } else{
            ToastUtil.showToast(this,R.string.label_toast_version_low);
        }
    }

    /**
     * 加载数据
     *
     * @param pageIndex 页数
     */
    private void startLoadData(int pageIndex) {
        showLoadingView(getString(R.string.loading_text));
        if (1 == pageIndex) {
            mHasNext = true;
            isRefresh = true;
        } else {
            isRefresh = false;
        }

        QueryTermsDTO params = new QueryTermsDTO();
        params.pageSize = SHOP_PRODUCTS_LIST_SIZE;
        params.pageNo = pageIndex;

        String latitude = SPUtils.getExtraCurrentLat(getApplicationContext());
        String longtitude = SPUtils.getExtraCurrentLon(getApplicationContext());
        double lat = TextUtils.isEmpty(latitude) ? 0 : Double.parseDouble(latitude);
        double lon = TextUtils.isEmpty(longtitude) ? 0 : Double.parseDouble(longtitude);
        if(lat > 0) {
            params.latitude = lat;
        }
        if(lon > 0) {
            params.longitude = lon;
        }
        List<QueryTerm> ts = new ArrayList<>();

        QueryTerm t1 = new QueryTerm();
        t1.type = QueryType.SELLER_ID;
        t1.value = String.valueOf(mSellerId);
        ts.add(t1);

        QueryTerm t2 = new QueryTerm();
        t2.type = QueryType.SELLER_TYPE;
        t2.value = MerchantType.MERCHANT;
        ts.add(t2);

//        if(ItemType.NORMAL.equals(mItemType)) {
//            QueryTerm t3 = new QueryTerm();
//            t3.type = QueryType.ITEM_TYPE;
//            t3.value = ItemType.NORMAL;
//            ts.add(t3);
//        }

        params.queryTerms = ts;
        mController.doGetShopProductsList(ShopHomePageActivity.this,params);
    }

    /**
     * 加载优惠券列表
     * @param sellerId
     */
    private void loadCouponList(long sellerId){
        mController.doQuerySellerVoucherList(getApplicationContext(),sellerId);
    }

    @Override
    public void handleMessage(Message msg) {
        hideLoadingView();
        hideErrorView(null);
        mPullToRefreshListView.onRefreshComplete();
        switch (msg.what) {
            case ValueConstants.MSG_GET_SHOP_PRODUCTS_LIST_OK:
                handleNetDataActive((ShortItemsResult) msg.obj);
                break;
            case ValueConstants.MSG_GET_SHOP_DETAIL_KO:
            case ValueConstants.MSG_GET_SHOP_PRODUCTS_LIST_KO:
                if (isRefresh) {
                    if(mAdapter.getCount() == 0) {
                        mAdapter.clear();
                        showErrorView(mListViewParent, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                                IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
                            @Override
                            public void onClick(View view) {
                                mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
                                mController.doGetShopDetailInfo(ShopHomePageActivity.this,mShopId);
                            }
                        });
                        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
                    }else{
                        ToastUtil.showToast(this,StringUtil.handlerErrorCode(this,msg.arg1));
                    }
                }else{
                    ToastUtil.showToast(this,StringUtil.handlerErrorCode(this,msg.arg1));
                }
                break;
            case ValueConstants.MSG_GET_SHOP_DETAIL_OK:
                refreshShopDetail((Merchant) msg.obj);
                break;
            case ValueConstants.MSG_HAS_NO_DATA:
                ToastUtil.showToast(this, getResources().getString(R.string.scenic_hasnodata));
                break;
            case ValueConstants.MSG_GET_SHOP_COUPON_LIST_OK:
                refreshShopCouponList((VoucherTemplateResultList) msg.obj);
                break;
            case ValueConstants.MSG_GET_SHOP_COUPON_LIST_KO:
                hideCouponList();
                break;
            case ValueConstants.MSG_REC_SHOP_COUPON_OK:
                DialogUtil.showCouponDialog(this,R.mipmap.coupon_get_success,TIME_INTERVAL_COUPON_DISPLAY);
                loadCouponList(mSellerId);
                break;
            case ValueConstants.MSG_REC_SHOP_COUPON_KO:
                if(ApiCode.HAS_BIND_VOUCHER_11000003 == msg.arg1){
                    DialogUtil.showCouponDialog(this,R.mipmap.coupon_get_already,TIME_INTERVAL_COUPON_DISPLAY);
                }else{
                    DialogUtil.showCouponDialog(this,R.mipmap.coupon_get_faile,TIME_INTERVAL_COUPON_DISPLAY);
                }
                break;
        }
    }

    /**
     * 显示优惠券列表
     * @param value
     */
    private void refreshShopCouponList(final VoucherTemplateResultList value){
        if(value == null || value.value == null || value.value.size() ==0 ){
            mCouponGridView.setVisibility(View.GONE);
            return ;
        }
        mCouponGridView.setVisibility(View.VISIBLE);
        mCouponGridView.removeAllViews();
        for(int i = 0; i < value.value.size(); i++){
            final int position = i;
            View view = View.inflate(this, R.layout.item_shop_coupon_list, null);
            ShopViewHelper.handleCounponListView(this, view, value.value.get(position));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!getUserService().isLogin()){
                        NavUtils.gotoLoginActivity(ShopHomePageActivity.this);
                        return ;
                    }
                    VoucherTemplateResult item = value.value.get(position);
                    tcEvent(item);
                    if(item != null && CouponStatus.ACTIVE.equals(item.status)){
                        handleCouponItemClick(item);
                    }
                }
            });
            mCouponGridView.addView(view);
        }
    }

    /**
     * 隐藏优惠券列表
     */
    private void hideCouponList(){
        mCouponGridView.setVisibility(View.GONE);
    }

    /**
     * 渲染店铺详情的视图
     *
     * @param result
     */
    private void refreshShopDetail(Merchant result) {
        if (result == null) {
            return;
        }
        mSellerInfo = result;
        loadSellerInfo();
        if (!StringUtil.isEmpty(result.name)) {
            mShopNameView.setText(result.name);
        }
        if (!StringUtil.isEmpty(result.backPic)) {
//            BaseImgView.frescoLoadimg(mShopBackgroudView,
//                    result.backPic,
//                    R.mipmap.icon_default_750_360,
//                    R.mipmap.icon_default_750_360,
//                    R.mipmap.icon_default_750_360,
//                    ImageScaleType.EXACTLY,
//                    -1,
//                    -1,
//                    -1);

            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(result.backPic), R.mipmap.icon_default_750_360, mShopBackgroudView);

        } else {
            mShopBackgroudView.setImageResource(R.mipmap.icon_default_750_360);
        }
        if (!StringUtil.isEmpty(result.icon)) {
//            BaseImgView.frescoLoadimg(mShopHeadIconView,
//                    result.icon,
//                    R.mipmap.ic_shop_default_logo,
//                    R.mipmap.ic_shop_default_logo,
//                    R.mipmap.ic_shop_default_logo,
//                    ImageScaleType.EXACTLY,
//                    -1,
//                    -1,
//                    180);

            ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(result.icon), R.mipmap.ic_shop_default_logo, mShopHeadIconView);

        } else {
            mShopHeadIconView.setImageResource(R.mipmap.ic_shop_default_logo);
        }
        if(result!=null&&result.name!=null){
            mBaseNavView.setTitleText(result.name);
        }else {
            mBaseNavView.setTitleText(mShopName);
        }
        mSellerId = result.sellerId;
        loadCouponList(mSellerId);
        startLoadData(1);
    }

    private void showNoDataPage(String notice) {
        showErrorView(null, IActionTitleBar.ErrorType.EMPTYVIEW, notice, " ", "", null);
    }


    /**
     * 处理店铺列表数据
     *
     * @param result
     */
    private boolean mHasNext = true;
    private boolean isRefresh = true;
    private int mPageIndex = 1;
    private void handleNetDataActive(ShortItemsResult result) {
        mHasNext = result.hasNext;
        mPageIndex = result.pageNo;
        if (isRefresh) {
            if (result.shortItemList != null) {
                if (result.shortItemList.size() == 0) {
//                    showNoDataPage();
                }
                if (isListStyle) {
                    mAdapter.replaceAll(result.shortItemList);
                } else {
                    mAdapter.replaceAll(ShopViewHelper.transferItemsInfoList(result.shortItemList));
                }
            } else {
                mAdapter.clear();
//                showNoDataPage();
            }
        } else {
            if (result.shortItemList != null) {
                if (isListStyle) {
                    mAdapter.addAll(result.shortItemList);
                } else {
                    mAdapter.addAll(ShopViewHelper.transferItemsInfoList(result.shortItemList));
                }
            }else{
                ToastUtil.showToast(this, getResources().getString(R.string.scenic_hasnodata));
            }
        }

    }

    private void showNoDataPage() {
        showNoDataPage(getString(R.string.label_handceremony_empty_notice_title));
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        initData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        if(mHasNext) {
            mPageIndex++;
            startLoadData(mPageIndex);
        }else{
            mHandler.sendEmptyMessageDelayed(ValueConstants.MSG_HAS_NO_DATA,100);
        }
    }
}
