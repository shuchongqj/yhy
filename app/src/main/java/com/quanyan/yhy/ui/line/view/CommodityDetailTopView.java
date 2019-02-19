package com.quanyan.yhy.ui.line.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.customview.imgpager.ImgPagerView;
import com.quanyan.base.view.customview.viewpagerindicator.CirclePageIndicator;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.common.MerchantType;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.line.CommodityDetailActivity;
import com.quanyan.yhy.ui.master.activity.MasterHomepageActivity;
import com.quanyan.yhy.ui.shop.ShopHomePageActivity;
import com.quanyan.yhy.view.CommodityBottomView;
import com.quanyan.yhy.view.LabelLayout;
import com.quanyan.yhy.view.NumStarView;
import com.yhy.common.beans.net.model.item.CityActivityDetail;
import com.yhy.common.beans.net.model.shop.MerchantItem;
import com.yhy.common.beans.net.model.trip.LineItemDetail;
import com.yhy.common.beans.net.model.trip.TagInfo;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/3/4
 * Time:14:23
 * Version 1.0
 */
public class CommodityDetailTopView extends LinearLayout {

    private CommodityBottomView.ExchangeData mExchangeData;
    private Context mContext;
    private ImgPagerView mImgPagerView;
    private TextView mCommodityTitle;
    private TextView mCommodityPrice;
    private LabelLayout mCommodityLabels;
    private TextView mCommoditySaleNums;

    private LinearLayout mMerchantLayout;
    private ImageView mMerchatnImg;
    private TextView mMerchantName;
    private ImageView mMerchantCertificate;
    private RelativeLayout mRLLocationContain;
    private TextView mTvDetailLocation;

    @Autowired
    IUserService userService;

    public CommodityDetailTopView(Context context) {
        super(context);
        init(context, null);
    }

    public CommodityDetailTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CommodityDetailTopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CommodityDetailTopView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    TextView tvIntegralmallMonkey;

    public void setExchangeData(CommodityBottomView.ExchangeData exchangeData) {
        mExchangeData = exchangeData;
    }

    private void init(Context context, AttributeSet attributeSet) {
        YhyRouter.getInstance().inject(this);
        mContext = context;
        View.inflate(context, R.layout.free_walker_and_package_tour_layout, this);
        mImgPagerView = (ImgPagerView) findViewById(R.id.commodity_pager_view);
        mCommodityTitle = (TextView) findViewById(R.id.commodity_title);
        mCommodityPrice = (TextView) findViewById(R.id.commodity_money);
        mCommodityLabels = (LabelLayout) findViewById(R.id.commodity_label_string);
        mCommoditySaleNums = (TextView) findViewById(R.id.commodity_sale_num);
        tvIntegralmallMonkey = (TextView) findViewById(R.id.tv_integralmall_deductible_monkey);
        mMerchantLayout = (LinearLayout) findViewById(R.id.commodity_merchant_layout);
        mMerchatnImg = (ImageView) findViewById(R.id.commodity_merchant_img);
        mMerchantName = (TextView) findViewById(R.id.commodity_merchant_name);
        mMerchantCertificate = (ImageView) findViewById(R.id.commodity_merchant_certificate_img);
        mRLLocationContain = (RelativeLayout) findViewById(R.id.rl_location_contain);
        mTvDetailLocation = (TextView) findViewById(R.id.tv_detail_location);


        ((CirclePageIndicator) findViewById(R.id.pager_indicator)).setPadding(0,
                ScreenSize.convertDIP2PX(getContext().getApplicationContext(), 10),
                0,
                ScreenSize.convertDIP2PX(getContext().getApplicationContext(), 30));

        mImgPagerView.setScale(context, (float) ValueConstants.SCALE_PRODUCT_HEADER_IMG);
        /**
         * 套餐的点击事件
         */
        findViewById(R.id.commodity_choose_layout).setOnClickListener(mChoosePackLayoutClick);
        findViewById(R.id.package_choose_layout).setOnClickListener(mChoosePackLayoutClick);

        /**
         * 商铺头像的点击事件
         */
        mMerchantLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mExchangeData == null) {
                    throw new NullPointerException("please use #setExchangeData(ExchangeData)# to initialize" +
                            " the params and implement the interface #ExchangeData#");
                }
                Object object = mExchangeData.getData();
                if (object != null) {
                    if (object instanceof MerchantItem) {
                        MerchantItem merchantItem = (MerchantItem) object;
                        long merchantId = ((MerchantItem) object).userInfo.userId == 0 ? ((MerchantItem) object).userInfo.id : ((MerchantItem) object).userInfo.userId;
                        if (merchantItem.userInfo != null) {
                            if (MerchantType.MERCHANT.equals(merchantItem.userInfo.sellerType)) {
                                ShopHomePageActivity.gotoShopHomePageActivity(getContext(), merchantItem.userInfo.nickname, merchantId);
                            } else {
                                MasterHomepageActivity.gotoMasterHomepage(getContext(), merchantId);
                            }
//                            NavUtils.gotoPersonalPage(getContext(), merchantId, merchantItem.userInfo.nickname, merchantItem.userInfo.options);
                        }
                    } else if (object instanceof LineItemDetail) {
                        //线路为用户信息
                        LineItemDetail merchantItem = (LineItemDetail) object;
                        long merchantId = ((LineItemDetail) object).userInfo.userId == 0 ? ((LineItemDetail) object).userInfo.id : ((LineItemDetail) object).userInfo.userId;
                        if (merchantItem.userInfo != null) {
                            if (MerchantType.MERCHANT.equals(merchantItem.userInfo.sellerType)) {
                                ShopHomePageActivity.gotoShopHomePageActivity(getContext(), merchantItem.userInfo.nickname, merchantId);
                            } else {
                                MasterHomepageActivity.gotoMasterHomepage(getContext(), merchantId);
                            }
//                            NavUtils.gotoPersonalPage(getContext(), merchantId, merchantItem.userInfo.nickname, merchantItem.userInfo.options);
                        }
                    } else if (object instanceof CityActivityDetail) {
                        //同城活动
                        CityActivityDetail merchantItem = (CityActivityDetail) object;
                        long merchantId = ((CityActivityDetail) object).userInfo.userId == 0 ? ((CityActivityDetail) object).userInfo.id : ((CityActivityDetail) object).userInfo.userId;
                        if (merchantItem.userInfo != null) {
                            if (MerchantType.MERCHANT.equals(merchantItem.userInfo.sellerType)) {
                                ShopHomePageActivity.gotoShopHomePageActivity(getContext(), merchantItem.userInfo.nickname, merchantId);
                            } else {
                                MasterHomepageActivity.gotoMasterHomepage(getContext(), merchantId);
                            }
//                            NavUtils.gotoPersonalPage(getContext(), merchantId, merchantItem.userInfo.nickname, merchantItem.userInfo.options);
                        }
                    }
                }
            }
        });
    }

    /**
     * 商铺点击事件
     *
     * @param onMerchatLayoutClick
     */
    public void setOnMerchatLayoutClick(View.OnClickListener onMerchatLayoutClick) {
        mMerchantLayout.setOnClickListener(onMerchatLayoutClick);
    }

    /**
     * 设置选择套餐的点击事件
     *
     * @param onChoosePackLayoutClick
     */
    public void setOnChoosePackLayoutClick(View.OnClickListener onChoosePackLayoutClick) {
        findViewById(R.id.commodity_choose_layout).setOnClickListener(onChoosePackLayoutClick);
        findViewById(R.id.package_choose_layout).setOnClickListener(onChoosePackLayoutClick);
    }

    /**
     * 设置选择券点击事件
     *
     * @param mOnclickListener
     */
    public void setOnCouponSelectClick(View.OnClickListener mOnclickListener) {
        findViewById(R.id.coupon_choose_layout).setOnClickListener(mOnclickListener);
    }

    public void setChooseContent(int stringResId) {
        ((TextView) findViewById(R.id.commodity_choose_purchase_type)).setText(getResources().getString(stringResId));
    }

    /**
     * 地点点击事件
     *
     * @param mOnClickListener
     */
    public void setLocationClick(View.OnClickListener mOnClickListener) {
        findViewById(R.id.rl_location_contain).setOnClickListener(mOnClickListener);
    }


    /**
     * 绑定View的数据(自由行，跟团游)
     *
     * @param lineDetail
     * @param pageType
     */
    public void bindViewData(LineItemDetail lineDetail, String pageType) {
        if (lineDetail.itemVO != null) {
            if (ItemType.FREE_LINE.equals(pageType) || ItemType.FREE_LINE_ABOARD.equals(pageType)) {
                ((ImageView) findViewById(R.id.commodity_type_img_label)).setImageResource(R.mipmap.freewalk);
                ((TextView) findViewById(R.id.commodity_type_name)).setText(getContext().getString(R.string.label_title_free_walk));
            } else if (ItemType.TOUR_LINE.equals(pageType) || ItemType.TOUR_LINE_ABOARD.equals(pageType)) {
                ((ImageView) findViewById(R.id.commodity_type_img_label)).setImageResource(R.mipmap.package_tour);
                ((TextView) findViewById(R.id.commodity_type_name)).setText(getContext().getString(R.string.label_title_tour_group));
            } else if (ItemType.NORMAL.equals(pageType)) {
                ((TextView) findViewById(R.id.commodity_type_name)).setText(getContext().getString(R.string.title_guide_buy));
            }
            mImgPagerView.setImgs(lineDetail.itemVO.picUrls);
            setAutoScroll(true);
            mCommodityTitle.setText(TextUtils.isEmpty(lineDetail.itemVO.title) ? "" : lineDetail.itemVO.title);
            mCommodityPrice.setText(StringUtil.convertPriceNoSymbolExceptLastZero(lineDetail.itemVO.marketPrice));
            if (lineDetail.tags == null || lineDetail.tags.size() == 0) {
                findViewById(R.id.commodity_labels_layout).setVisibility(View.GONE);//没有标签列表
            } else {
                addLabels(mContext, mCommodityLabels, lineDetail.tags);
            }
//            if(ItemType.FREE_LINE.equals(pageType) && lineDetail.itemVO.sales == 0){
//                mCommoditySaleNums.setVisibility(View.GONE);
//            }else {
            mCommoditySaleNums.setText(StringUtil.formatSales(getContext(), lineDetail.itemVO.sales));
//            }
        }
        if (lineDetail.userInfo != null) {
            mMerchantName.setText(TextUtils.isEmpty(lineDetail.userInfo.nickname) ? "" : lineDetail.userInfo.nickname);
            int defaultPic = MerchantType.TALENT.equals(lineDetail.userInfo.sellerType) ?
                    R.mipmap.icon_default_avatar : R.mipmap.ic_shop_default_logo;

            if (TextUtils.isEmpty(lineDetail.userInfo.avatar)) {
                mMerchatnImg.setImageResource(defaultPic);
            } else {
//                BaseImgView.loadimg(mMerchatnImg, lineDetail.userInfo.avatar, defaultPic,
//                        defaultPic, defaultPic, ImageScaleType.EXACTLY,
//                        250, 250, 180);

                ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(lineDetail.userInfo.avatar), defaultPic, 250, 250, mMerchatnImg);
            }
        }
    }

    /**
     * 绑定View的数据(必买推荐)
     *
     * @param merchantItem
     * @param pageType
     */
    public void bindViewData(MerchantItem merchantItem, String pageType) {
//        ((ImageView)findViewById(R.id.commodity_type_img_label)).setImageResource(R.mipmap.package_tour);
        findViewById(R.id.commodity_labels_layout).setVisibility(View.GONE);//没有标签列表
        if (ItemType.POINT_MALL.equals(pageType)) {
            if (merchantItem.itemVO.payInfo != null) {
//                if (merchantItem.itemVO.payInfo.payType.equals("ALL_MONEY")) {
//                    findViewById(R.id.layout_integralmall_deductible).setVisibility(View.GONE);
//                } else if (merchantItem.itemVO.payInfo.payType.equals("MONEY_OR_POINT")) {
//                    findViewById(R.id.layout_integralmall_deductible).setVisibility(View.VISIBLE);
//                    tvIntegralmallMonkey.setText(merchantItem.itemVO.payInfo.maxPoint + "");
//                }
                // 积分可抵
                findViewById(R.id.layout_integralmall_deductible).setVisibility(View.VISIBLE);
//                tvIntegralmallMonkey.setText(StringUtil.convertScoreToDiscount(getContext(), merchantItem.itemVO.payInfo.maxPoint) + "");
//                tvIntegralmallMonkey.setText(StringUtil.pointToYuanOne(merchantItem.itemVO.payInfo.maxPoint*10) + "");
                tvIntegralmallMonkey.setText(merchantItem.itemVO.payInfo.maxPoint > merchantItem.itemVO.payInfo.minPoint ? StringUtil.pointToYuanOne(merchantItem.itemVO.payInfo.minPoint*10) + "-" + StringUtil.pointToYuanOne(merchantItem.itemVO.payInfo.maxPoint*10) :
                        StringUtil.pointToYuanOne(merchantItem.itemVO.payInfo.minPoint*10));

            }
//            if(lineDetail.tags == null || lineDetail.tags.size() == 0){
//                findViewById(R.id.commodity_labels_layout).setVisibility(View.GONE);//没有标签列表
//            }else {
//                addLabels(mContext, mCommodityLabels, lineDetail.tags);
//            }
//            findViewById(R.id.commodity_integralmal_market_pirce_layout).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.layout_integralmall_deductible).setVisibility(View.GONE);//没有积分抵扣
        }

        if (merchantItem.itemVO != null) {
            // tom add
            mCommoditySaleNums.setVisibility(GONE);
            mCommoditySaleNums.setText(StringUtil.formatSales(getContext(), merchantItem.itemVO.sales));
            if (ItemType.FREE_LINE.equals(pageType) || ItemType.FREE_LINE_ABOARD.equals(pageType)) {
                ((TextView) findViewById(R.id.commodity_type_name)).setText(getContext().getString(R.string.label_title_free_walk));
            } else if (ItemType.TOUR_LINE.equals(pageType) || ItemType.TOUR_LINE_ABOARD.equals(pageType)) {
                ((TextView) findViewById(R.id.commodity_type_name)).setText(getContext().getString(R.string.label_title_tour_group));
            } else if (ItemType.NORMAL.equals(pageType)) {
                ((TextView) findViewById(R.id.commodity_type_name)).setText(getContext().getString(R.string.title_guide_buy));
            }
            mImgPagerView.setImgs(merchantItem.itemVO.picUrls);
            setAutoScroll(true);
            //标题
            mCommodityTitle.setText(TextUtils.isEmpty(merchantItem.itemVO.title) ? "" : merchantItem.itemVO.title);
            //价格
            long discountPrice = 0;
            if (merchantItem.itemVO.payInfo != null) {
                discountPrice = merchantItem.itemVO.payInfo.maxPoint;
            }
//            mCommodityPrice.setText(StringUtil.pointToYuan(merchantItem.itemVO.marketPrice / 10 - (SPUtils.getScore(getContext()) > discountPrice ? discountPrice : SPUtils.getScore(getContext())))
//            mCommodityPrice.setText(StringUtil.pointToYuan(merchantItem.itemVO.marketPrice));
            mCommodityPrice.setText(StringUtil.convertPriceNoSymbolExceptLastZero(merchantItem.itemVO.marketPrice));
//            mCommodityPrice.setText(StringUtil.convertPriceNoSymbolExceptLastZero(
//                    merchantItem.itemVO.marketPrice - discountPrice));
            //评分
            ((NumStarView) findViewById(R.id.detail_rating_view)).setRating(merchantItem.itemVO.grade);

            if (ItemType.POINT_MALL.equals(pageType) && merchantItem.itemVO.originalPrice > 0) {
                findViewById(R.id.commodity_integralmal_market_pirce_layout).setVisibility(View.VISIBLE);
                //积分商城的原价
                TextView integralmalPrice = ((TextView) findViewById(R.id.commodity_integralmal_market_pirce));
                integralmalPrice.setText(getContext().getString(R.string.money_symbol)
                        + StringUtil.convertPriceNoSymbolExceptLastZero(merchantItem.itemVO.originalPrice));
                integralmalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }
        if (merchantItem.userInfo != null) {
            //商家信息
            int defaultPic = MerchantType.TALENT.equals(merchantItem.userInfo.sellerType) ?
                    R.mipmap.icon_default_avatar : R.mipmap.ic_shop_default_logo;

            if (TextUtils.isEmpty(merchantItem.userInfo.avatar)) {
                mMerchatnImg.setImageResource(defaultPic);
            } else {
//                BaseImgView.loadimg(mMerchatnImg, merchantItem.userInfo.avatar, defaultPic,
//                        defaultPic, defaultPic, ImageScaleType.EXACTLY, 150, 150, 180);

                ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(merchantItem.userInfo.avatar), defaultPic, 150, 150, mMerchatnImg);

            }
            mMerchantName.setText(TextUtils.isEmpty(merchantItem.userInfo.nickname) ? "" :
                    merchantItem.userInfo.nickname);
        }
    }

    /**
     * 活动详情数据
     *
     * @param cityActivityDetail
     */
    public void bindViewData(CityActivityDetail cityActivityDetail) {
        ((ImageView) findViewById(R.id.commodity_type_img_label)).setImageResource(R.mipmap.local_active);
        ((TextView) findViewById(R.id.commodity_type_name)).setText(getContext().getString(R.string.label_title_city_activity));
        if (!StringUtil.isEmpty(cityActivityDetail.locationText)) {
            if (mRLLocationContain.getVisibility() == GONE) {
                mRLLocationContain.setVisibility(VISIBLE);
                mTvDetailLocation.setText(cityActivityDetail.locationText);
            }
        }

        if (cityActivityDetail.itemVO != null) {
            mImgPagerView.setImgs(cityActivityDetail.itemVO.picUrls);
            setAutoScroll(true);
            mCommodityTitle.setText(TextUtils.isEmpty(cityActivityDetail.itemVO.title) ?
                    "" : cityActivityDetail.itemVO.title);
            mCommodityPrice.setText(StringUtil.convertPriceNoSymbolExceptLastZero(cityActivityDetail.itemVO.marketPrice));
            mCommoditySaleNums.setText(String.format(mContext.getString(R.string.label_sign_up_people),
                    cityActivityDetail.itemVO.sales));
        }
        if (cityActivityDetail.userInfo != null) {
            //商家信息
            int defaultPic = MerchantType.TALENT.equals(cityActivityDetail.userInfo.sellerType) ?
                    R.mipmap.icon_default_avatar : R.mipmap.ic_shop_default_logo;
            if (TextUtils.isEmpty(cityActivityDetail.userInfo.avatar)) {
                mMerchatnImg.setImageResource(defaultPic);
            } else {
//                BaseImgView.loadimg(mMerchatnImg, cityActivityDetail.userInfo.avatar, defaultPic,
//                        defaultPic, defaultPic, ImageScaleType.EXACTLY, 150, 150, 180);
                ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(cityActivityDetail.userInfo.avatar), defaultPic,150, 150, mMerchatnImg);

            }
            mMerchantName.setText(TextUtils.isEmpty(cityActivityDetail.userInfo.nickname) ? "" :
                    cityActivityDetail.userInfo.nickname);
        }
        if (cityActivityDetail.tags == null || cityActivityDetail.tags.size() == 0) {
            findViewById(R.id.commodity_labels_layout).setVisibility(View.GONE);//没有标签列表
        } else {
            addLabels(mContext, mCommodityLabels, cityActivityDetail.tags);
        }
    }

    /**
     * 设置是否显示选择套餐的选项
     *
     * @param visible
     */
    public void setChooseLayoutVisible(boolean visible) {
        findViewById(R.id.commodity_choose_layout).setVisibility(visible ? View.VISIBLE : View.GONE);
        if (!visible) {
            ((CirclePageIndicator) findViewById(R.id.pager_indicator)).setPadding(0,
                    ScreenSize.convertDIP2PX(getContext().getApplicationContext(), 10),
                    0,
                    ScreenSize.convertDIP2PX(getContext().getApplicationContext(), 10));
        }
    }

    /**
     * 设置是否显示选择优惠卷
     *
     * @param visible
     */
    public void setChooseCouponLayoutVisible(boolean visible) {
        findViewById(R.id.coupon_choose_layout).setVisibility(visible ? View.VISIBLE : View.GONE);

    }

    /**
     * 设置是否显示选择套餐的选项(积分商城)
     *
     * @param visible
     */
    public void setChoosePackageLayoutVisible(boolean visible) {
        findViewById(R.id.package_choose_layout).setVisibility(visible ? View.VISIBLE : View.GONE);
    }


    /**
     * 隐藏评星栏
     */
    public void hideMarkLayout() {
        findViewById(R.id.commodity_mark_layout).setVisibility(View.INVISIBLE);
    }

    /**
     * 隐藏商铺
     *
     * @param
     */
    public void hideMerchatCommodityLayoutClick() {
        findViewById(R.id.commodity_merchant_layout).setVisibility(View.GONE);
    }

    /**
     * 显示积分商品详情
     *
     * @param
     */
    public void displayMerchatOrderDetailsLayoutClick() {
        findViewById(R.id.commodity_order_details_layout).setVisibility(View.VISIBLE);
    }


    /**
     * 设置选择套餐的可见性
     *
     * @param visible
     */
    public void hideChoosePackLayout(boolean visible) {
        findViewById(R.id.commodity_choose_layout).setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setChoosePackLayoutClickable(boolean clickable) {
        LinearLayout view = (LinearLayout) findViewById(R.id.commodity_choose_layout);
        if (clickable) {
            view.setBackgroundColor(Color.WHITE);
        } else {
            view.setBackgroundColor(getResources().getColor(R.color.tc_e1e1e1));
        }
        view.setEnabled(clickable);
        view.invalidate();
    }


    private void addLabels(Context context, LabelLayout labelsLayout, final List<TagInfo> item) {
        int labelViewPaddingLR = ScreenSize.convertDIP2PX(context.getApplicationContext(), 8);
        int labelViewPaddingTB = ScreenSize.convertDIP2PX(context.getApplicationContext(), 2);
        LabelLayout.LayoutParams layoutParams = new LabelLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.rightMargin = labelViewPaddingLR;
        labelsLayout.removeAllViews();
        if (item != null) {
            int tagSize = item.size();
            List<TagInfo> comTagInfos = item;
            for (int i = 0; i < tagSize; i++) {
                TextView textView = new TextView(context.getApplicationContext());
                textView.setTextSize(10);
                textView.setBackgroundResource(R.drawable.bg_live_label_selector);
                textView.setPadding(labelViewPaddingLR, labelViewPaddingTB, labelViewPaddingLR, labelViewPaddingTB);
                textView.setTextColor(context.getResources().getColor(R.color.neu_fa4619));
                textView.setText(comTagInfos.get(i).name);
                labelsLayout.addView(textView, layoutParams);
            }
        }
    }

    /**
     * 设置自动滚动
     *
     * @param autoScroll
     */
    public void setAutoScroll(boolean autoScroll) {
        if (autoScroll) {
            mImgPagerView.startAutoScroll();
        } else {
            mImgPagerView.stopAutoScroll();
        }
    }

    /**
     * 设置选择套餐的点击事件
     */
    private View.OnClickListener mChoosePackLayoutClick = new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            if (!userService.isLogin()) {
                if (getContext() instanceof CommodityDetailActivity) {
                    NavUtils.gotoLoginActivity(((CommodityDetailActivity) getContext()));
                }
                return;
            }
            if (mExchangeData == null) {
                throw new NullPointerException("please use #setExchangeData(ExchangeData)# to initialize" +
                        " the params and implement the interface #ExchangeData#");
            }
            Object object = mExchangeData.getData();
            if (object != null) {
                if (object instanceof LineItemDetail) {
                    if (((LineItemDetail) object).userInfo == null
                            || ((LineItemDetail) object).userInfo.id != userService.getLoginUserId()) {
                        if (((LineItemDetail) object).itemVO != null) {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put(AnalyDataValue.KEY_PTYPE, AnalyDataValue.getType(mExchangeData.getCommodityType()));
                            params.put(AnalyDataValue.KEY_PNAME, ((LineItemDetail) object).itemVO.title);
                            params.put(AnalyDataValue.KEY_PID, ((LineItemDetail) object).itemVO.id + "");
                            TCEventHelper.onEvent(getContext(), AnalyDataValue.TC_ID_BUY, params);

                            if (getContext() instanceof CommodityDetailActivity) {
                                NavUtils.gotoLineOrderActivity(((CommodityDetailActivity) getContext()), ((LineItemDetail) object).itemVO.id, mExchangeData.getCommodityType());
                            }
                        }
                    }
                } else if (object instanceof MerchantItem) {
                    if (((MerchantItem) object).userInfo == null
                            || ((MerchantItem) object).userInfo.id != userService.getLoginUserId()) {
                        if (((MerchantItem) object).itemVO != null) {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.getType(mExchangeData.getCommodityType()));
                            params.put(AnalyDataValue.KEY_NAME, ((MerchantItem) object).itemVO.title);
                            params.put(AnalyDataValue.KEY_ID, ((MerchantItem) object).itemVO.id + "");
                            TCEventHelper.onEvent(getContext(), AnalyDataValue.TC_ID_BUY, params);

                            if (getContext() instanceof CommodityDetailActivity) {
                                NavUtils.gotoLineOrderActivity(((CommodityDetailActivity) getContext()), ((MerchantItem) object).itemVO.id, mExchangeData.getCommodityType());
                            }
                        }
                    }
                } else if (object instanceof CityActivityDetail) {
                    if (((CityActivityDetail) object).userInfo == null
                            || ((CityActivityDetail) object).userInfo.id != userService.getLoginUserId()) {
                        if (((CityActivityDetail) object).itemVO != null) {
                            if (!ValueConstants.ACTIVITY_STATE_EXPIRED.equals(((CityActivityDetail) object).itemVO.status)) {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.getType(mExchangeData.getCommodityType()));
                                params.put(AnalyDataValue.KEY_NAME, ((CityActivityDetail) object).itemVO.title);
                                params.put(AnalyDataValue.KEY_ID, ((CityActivityDetail) object).itemVO.id + "");
                                TCEventHelper.onEvent(getContext(), AnalyDataValue.TC_ID_BUY, params);
                                if (getContext() instanceof CommodityDetailActivity) {
                                    NavUtils.gotoLineOrderActivity(((CommodityDetailActivity) getContext()), ((CityActivityDetail) object).itemVO.id, mExchangeData.getCommodityType());
                                }
                            }
                        }
                    }
                }
            }
        }
    };
}
