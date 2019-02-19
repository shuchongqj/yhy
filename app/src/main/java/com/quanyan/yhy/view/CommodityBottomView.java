package com.quanyan.yhy.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.yhy.common.beans.net.model.ProductCardModel;
import com.yhy.common.beans.net.model.item.CityActivityDetail;
import com.yhy.common.beans.net.model.shop.MerchantItem;
import com.yhy.common.beans.net.model.trip.LineItemDetail;
import com.yhy.common.beans.net.model.user.UserInfo;
import com.yhy.common.utils.SPUtils;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:商品详情底部导航条
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/2/27
 * Time:09:33
 * Version 1.0
 */
public class CommodityBottomView extends LinearLayout implements View.OnClickListener {

    private ExchangeData mExchangeData;

    private LinearLayout mCustomServerLayout;
    //        private LinearLayout mStoreLayout;
    private LinearLayout mCollectLayout;
    private LinearLayout mAddShopCartLayout;
    private LinearLayout mPurchaseLayout;

    private AddSPCartListener mAddSpcartListener;

    @Autowired
    IUserService userService;

    public CommodityBottomView(Context context) {
        super(context);
        initView(context, null);
    }

    public CommodityBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public CommodityBottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CommodityBottomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        YhyRouter.getInstance().inject(this);
        View.inflate(context, R.layout.commodity_common_bottom, this);
        mCustomServerLayout = (LinearLayout) findViewById(R.id.commodity_bottom_customserver_layout);
//        mStoreLayout = (LinearLayout) findViewById(R.id.commodity_bottom_store_layout);
        mCollectLayout = (LinearLayout) findViewById(R.id.commodity_bottom_collect_layout);
        mAddShopCartLayout = (LinearLayout) findViewById(R.id.commodity_bottom_shopcart_layout);
        mPurchaseLayout = (LinearLayout) findViewById(R.id.commodity_bottom_purchase_layout);

        mCustomServerLayout.setOnClickListener(this);
//        mStoreLayout.setOnClickListener(this);
        mCollectLayout.setOnClickListener(this);
        mAddShopCartLayout.setOnClickListener(this);
        mPurchaseLayout.setOnClickListener(this);
        findViewById(R.id.commodity_bottom_other_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/3/29  电话咨询
            }
        });
        findViewById(R.id.commodity_bottom_chat_private_layout).setOnClickListener(this);
        findViewById(R.id.commodity_bottom_service_phone_layout).setOnClickListener(this);
        findViewById(R.id.commodity_bottom_shopcart_layout).setOnClickListener(this);
        findViewById(R.id.commodity_bottom_collect_layout).setOnClickListener(this);
    }

    public void setExchangeData(ExchangeData exchangeData) {
        mExchangeData = exchangeData;
    }

    /**
     * 隐藏店铺，收藏，客服的布局
     */
    public void hideOtherLayout() {
        findViewById(R.id.commodity_bottom_other_layout).setVisibility(View.GONE);
    }

    /**
     * 显示电话咨询的布局
     */
    public void showConsultLayout() {
        findViewById(R.id.commodity_bottom_consult_layout).setVisibility(View.VISIBLE);
    }

    /**
     * 显示私聊的布局
     */
    public void showChatPrivateLayout() {
        findViewById(R.id.commodity_bottom_chat_private_layout).setVisibility(View.VISIBLE);
    }

    //显示商品详情的电话咨询
    public void showServicePhoneLayout() {
        findViewById(R.id.commodity_bottom_service_phone_layout).setVisibility(View.VISIBLE);
    }

    //隐藏商品详情的电话咨询
    public void hideServicePhoneLayout() {
        findViewById(R.id.commodity_bottom_service_phone_layout).setVisibility(View.GONE);
    }

    //显示购物车按钮
    public void showSpcartBtn() {
        findViewById(R.id.commodity_bottom_collect_layout).setVisibility(View.VISIBLE);
    }

    //隐藏购物车按钮
    public void hideSpcartBtn() {
        findViewById(R.id.commodity_bottom_collect_layout).setVisibility(View.GONE);
    }

    //显示添加购物车按钮
    public void showAddSpcartBtn() {
        findViewById(R.id.commodity_bottom_shopcart_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.commodity_bottom_shopcart_layout).setBackgroundResource(R.color.white);
        findViewById(R.id.commodity_bottom_purchase_layout).setBackgroundResource(R.color.red_ying);
        TextView tv = (TextView) findViewById(R.id.commodity_bottom_purchase_text);
        tv.setTextColor(getResources().getColor(R.color.white));
        TextView tv1 = (TextView) findViewById(R.id.tv_add_cart);
        tv1.setTextColor(getResources().getColor(R.color.red_ying));
    }

    //隐藏添加购物车按钮
    public void hideAddSpcartBtn() {
        findViewById(R.id.commodity_bottom_shopcart_layout).setVisibility(View.GONE);
    }

    public void showSpCartNum(long count) {
        TextView tv = (TextView) findViewById(R.id.tv_spcart_num);
        if (count > 0) {
            tv.setVisibility(View.VISIBLE);
            if(count > 99){
                tv.setText("99+");
            }else{
                tv.setText(count + "");
            }
        } else {
            tv.setVisibility(View.GONE);
        }
    }

    /**
     * 设置立即购买是否可以点击
     *
     * @param clickable
     */
    public void setPurchaseClickable(boolean clickable) {
        mPurchaseLayout.setEnabled(clickable);
        if (clickable) {
            if (ItemType.MASTER_CONSULT_PRODUCTS.equals(mExchangeData.getCommodityType())) {
                ((TextView) findViewById(R.id.commodity_bottom_purchase_text)).setText(R.string.consult_now);
            }
            mPurchaseLayout.setBackgroundColor(getResources().getColor(R.color.ac_title_bg_color));
            ((TextView) findViewById(R.id.commodity_bottom_purchase_text)).setTextColor(getResources().getColor(R.color.neu_333333));
        } else {
            if (ItemType.MASTER_CONSULT_PRODUCTS.equals(mExchangeData.getCommodityType())) {
                ((TextView) findViewById(R.id.commodity_bottom_purchase_text)).setText(R.string.label_btn_offline);
            }
            mPurchaseLayout.setBackgroundColor(getResources().getColor(R.color.tc_e1e1e1));
            ((TextView) findViewById(R.id.commodity_bottom_purchase_text)).setTextColor(getResources().getColor(R.color.neu_666666));
        }
        mPurchaseLayout.invalidate();
    }

    /**
     * 设置立即购买是否可以点击
     *
     * @param clickable
     */
    public void setCustomServerClickable(boolean clickable) {
        mCustomServerLayout.setEnabled(clickable);
        if (clickable) {
            mCustomServerLayout.setBackgroundColor(Color.WHITE);
        } else {
            mCustomServerLayout.setBackgroundColor(getResources().getColor(R.color.tc_e1e1e1));
        }
        mCustomServerLayout.invalidate();
    }

    /**
     * 达人咨询详情页的底部布局
     */
    public void showMasterConsultView() {
        hideServicePhoneLayout();
        findViewById(R.id.commodity_bottom_line2).setVisibility(View.GONE);
        mCustomServerLayout.setVisibility(View.GONE);
        setPurchaseText(getContext().getString(R.string.consult_now));
    }

    /**
     * 设置立即购买的文本，（活动已结束显示已结束）
     *
     * @param text
     */
    public void setPurchaseText(String text) {
        if (!TextUtils.isEmpty(text)) {
            ((TextView) findViewById(R.id.commodity_bottom_purchase_text)).setText(text);
        }
    }

    @Override
    public void onClick(View v) {
        if (mExchangeData == null) {
            throw new NullPointerException("please use #setExchangeData(ExchangeData)# to initialize" +
                    " the params and implement the interface #ExchangeData#");
        }
        switch (v.getId()) {
            case R.id.commodity_bottom_customserver_layout: {
                // TODO: 16/2/27 客服
                ProductCardModel productCardModel = null;
                long uid = -1;
                Object o = mExchangeData.getData();
                if (o != null) {
                    if (o instanceof LineItemDetail) {
                        uid = ((LineItemDetail) o).userInfo.id;
                        productCardModel = new ProductCardModel(1,
                                mExchangeData.getCommodityType(),
                                ((LineItemDetail) o).itemVO.title,
                                ((LineItemDetail) o).itemVO.id,
                                ((LineItemDetail) o).itemVO.picUrls == null ? "" : ((LineItemDetail) o).itemVO.picUrls.get(0),
                                ((LineItemDetail) o).itemVO.description,
                                ((LineItemDetail) o).itemVO.marketPrice);
                        shopIm(mExchangeData.getCommodityType(),
                                ((LineItemDetail) o).itemVO.title,
                                ((LineItemDetail) o).itemVO.id + "");
                    } else if (o instanceof MerchantItem) {
                        uid = SPUtils.getServiceUID(getContext());
//                        uid = ((MerchantItem) o).itemVO.sellerId;
                        productCardModel = new ProductCardModel(1,
                                mExchangeData.getCommodityType(),
                                ((MerchantItem) o).itemVO.title,
                                ((MerchantItem) o).itemVO.id,
                                ((MerchantItem) o).itemVO.picUrls == null ? "" : ((MerchantItem) o).itemVO.picUrls.get(0),
                                ((MerchantItem) o).itemVO.description,
                                ((MerchantItem) o).itemVO.marketPrice);
                        shopIm(mExchangeData.getCommodityType(),
                                ((MerchantItem) o).itemVO.title,
                                ((MerchantItem) o).itemVO.id + "");
                    } else if (o instanceof CityActivityDetail) {
                        uid = ((CityActivityDetail) o).userInfo.id;
                        productCardModel = new ProductCardModel(1,
                                mExchangeData.getCommodityType(),
                                ((CityActivityDetail) o).itemVO.title,
                                ((CityActivityDetail) o).itemVO.id,
                                ((CityActivityDetail) o).itemVO.picUrls == null ? "" : ((CityActivityDetail) o).itemVO.picUrls.get(0),
                                ((CityActivityDetail) o).itemVO.description,
                                ((CityActivityDetail) o).itemVO.marketPrice);
                        shopIm(mExchangeData.getCommodityType(),
                                ((CityActivityDetail) o).itemVO.title,
                                ((CityActivityDetail) o).itemVO.id + "");
                    }
                }
                if (!userService.isLogin()) {
                    NavUtils.gotoLoginActivity(getContext());
                } else {
                    NavUtils.gotoMessageActivity(getContext(), uid, productCardModel);
                }
                break;
            }
            case R.id.commodity_bottom_service_phone_layout:
//                // TODO: 16/2/27电话咨询
                Object data = mExchangeData.getData();
                if (data != null) {
                    if (data instanceof LineItemDetail) {
                        if (!StringUtil.isEmpty(((LineItemDetail) data).userInfo.serviceCall)) {
                            LocalUtils.call(getContext(), ((LineItemDetail) data).userInfo.serviceCall);
                        } else {
                            ToastUtil.showToast(getContext(), getContext().getString(R.string.label_hint_no_service_phone));
                        }
                        shopCall(mExchangeData.getCommodityType(),
                                ((LineItemDetail) data).itemVO.title,
                                ((LineItemDetail) data).itemVO.id + "");
                    } else if (data instanceof MerchantItem) {
                        if (!StringUtil.isEmpty(((MerchantItem) data).userInfo.serviceCall)) {
                            LocalUtils.call(getContext(), ((MerchantItem) data).userInfo.serviceCall);
                        } else {
                            ToastUtil.showToast(getContext(), getContext().getString(R.string.label_hint_no_service_phone));
                        }
                        shopCall(mExchangeData.getCommodityType(),
                                ((MerchantItem) data).itemVO.title,
                                ((MerchantItem) data).itemVO.id + "");
                    } else if (data instanceof CityActivityDetail) {
                        if (!StringUtil.isEmpty(((CityActivityDetail) data).userInfo.serviceCall)) {
                            LocalUtils.call(getContext(), ((CityActivityDetail) data).userInfo.serviceCall);
                        } else {
                            ToastUtil.showToast(getContext(), getContext().getString(R.string.label_hint_no_service_phone));
                        }
                        shopCall(mExchangeData.getCommodityType(),
                                ((CityActivityDetail) data).itemVO.title,
                                ((CityActivityDetail) data).itemVO.id + "");
                    }
                }
                break;
//            case R.id.commodity_bottom_store_layout:
////                // TODO: 16/2/27 店铺
//                NavUtils.gotoShopHomePageActivity(getContext(),"quanyan",-1);
//                break;
            case R.id.commodity_bottom_collect_layout:
                // TODO: 16/2/27 购物车
//                mExchangeData.getData();
//                HarwkinLogUtil.info("您想收藏该商品");
//                NavUtils.gotoShopHomePageActivity(getContext(),"quanyan",-1);
                NavUtils.gotoSPCartActivity(getContext());
                break;
            case R.id.commodity_bottom_shopcart_layout:
//                // TODO: 16/2/27 加入购物车
                if (mAddSpcartListener != null) {
                    mAddSpcartListener.add();
                }
                break;
            case R.id.commodity_bottom_purchase_layout: {
                // TODO: 16/2/27 立即购买, 立即咨询（达人咨询详情）
                if (!userService.isLogin()) {
                    NavUtils.gotoLoginActivity(getContext());
                    return;
                }
                Bundle bundle = new Bundle();
//                bundle.putSerializable(SPUtils.EXTRA_DATA, (Serializable) mExchangeData.getData());
                Object o = mExchangeData.getData();
                if (o != null) {
                    if (o instanceof LineItemDetail) {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(AnalyDataValue.KEY_PTYPE, AnalyDataValue.getType(mExchangeData.getCommodityType()));
                        params.put(AnalyDataValue.KEY_PNAME, ((LineItemDetail) o).itemVO.title);
                        params.put(AnalyDataValue.KEY_PID, ((LineItemDetail) o).itemVO.id + "");
                        TCEventHelper.onEvent(getContext(), AnalyDataValue.TC_ID_BUY, params);

                        NavUtils.gotoLineOrderActivity(getContext(), ((LineItemDetail) o).itemVO.id, mExchangeData.getCommodityType());
                    } else if (o instanceof MerchantItem) {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(AnalyDataValue.KEY_PTYPE, AnalyDataValue.getType(mExchangeData.getCommodityType()));
                        params.put(AnalyDataValue.KEY_PNAME, ((MerchantItem) o).itemVO.title);
                        params.put(AnalyDataValue.KEY_PID, ((MerchantItem) o).itemVO.id + "");
                        TCEventHelper.onEvent(getContext(), AnalyDataValue.TC_ID_BUY, params);
                        if (((MerchantItem) o).itemVO != null) {
                            if (ItemType.MASTER_CONSULT_PRODUCTS.equals(mExchangeData.getCommodityType())) {
                                if (mOnClickListener != null) {
                                    // TODO: 16/8/2 为达人咨询服务单独处理 详情见#CommodityDetailActivity#中的mOnClickListener
                                    mOnClickListener.onClick(v);
                                    return;
                                }
                                NavUtils.gotoMasterConsultActivity(getContext(), ((MerchantItem) o).itemVO.id, ((MerchantItem) o).itemVO.marketPrice, ((MerchantItem) o).itemVO.consultTime);
                            } else {
                                if (ItemType.POINT_MALL.equals(mExchangeData.getCommodityType()) || ItemType.NORMAL.equals(mExchangeData.getCommodityType())) {
                                    NavUtils.gotoPointOrderActivity(getContext(), ((MerchantItem) o).itemVO.id, mExchangeData.getCommodityType());

                                } else {
                                    NavUtils.gotoLineOrderActivity(getContext(), ((MerchantItem) o).itemVO.id, mExchangeData.getCommodityType());
                                }
                            }
                        }
                    } else if (o instanceof CityActivityDetail) {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(AnalyDataValue.KEY_PTYPE, AnalyDataValue.getType(mExchangeData.getCommodityType()));
                        params.put(AnalyDataValue.KEY_PNAME, ((CityActivityDetail) o).itemVO.title);
                        params.put(AnalyDataValue.KEY_PID, ((CityActivityDetail) o).itemVO.id + "");
                        TCEventHelper.onEvent(getContext(), AnalyDataValue.TC_ID_BUY, params);

                        NavUtils.gotoLineOrderActivity(getContext(), ((CityActivityDetail) o).itemVO.id, mExchangeData.getCommodityType());
                    }
                }
                break;
            }
            case R.id.commodity_bottom_chat_private_layout: {
                // TODO: 16/3/29 私聊

                break;
            }
        }
    }

    /**
     * 店铺电话
     *
     * @param ptype
     * @param pname
     */
    private void shopCall(String ptype, String pname, String pid) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(AnalyDataValue.KEY_PTYPE, AnalyDataValue.getType(ptype));
        params.put(AnalyDataValue.KEY_PNAME, pname);
        params.put(AnalyDataValue.KEY_PID, pid);
        TCEventHelper.onEvent(getContext(), AnalyDataValue.TC_ID_SHOPP_CALL, params);
    }

    /**
     * 店铺在线咨询
     *
     * @param ptype
     * @param pname
     * @param pid
     */
    private void shopIm(String ptype, String pname, String pid) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(AnalyDataValue.KEY_PTYPE, AnalyDataValue.getType(ptype));
        params.put(AnalyDataValue.KEY_PNAME, pname);
        params.put(AnalyDataValue.KEY_PID, pid);
        TCEventHelper.onEvent(getContext(), AnalyDataValue.TC_ID_SHOPP_IM, params);
    }

    public interface ExchangeData<T> {
        T getData();

        UserInfo getUserInfo();

        String getCommodityType();
    }

    private View.OnClickListener mOnClickListener;

    /**
     * 达人咨询服务详情的点击咨询需要调接口，重新设置，覆盖这里的点击事件
     *
     * @param purchaseLayoutClick
     */
    public void setPurchaseLayoutClick(View.OnClickListener purchaseLayoutClick) {
        mOnClickListener = purchaseLayoutClick;
    }

    public void setAddSPCartListener(AddSPCartListener mAddSPCartListener) {
        this.mAddSpcartListener = mAddSPCartListener;
    }

    public interface AddSPCartListener {
        void add();
    }
}
