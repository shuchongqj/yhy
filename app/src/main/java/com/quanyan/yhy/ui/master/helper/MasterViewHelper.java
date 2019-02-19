package com.quanyan.yhy.ui.master.helper;

import android.app.Activity;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.newyhy.utils.DisplayUtils;
import com.quanyan.base.util.HarwkinLogUtil;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.Gendar;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.common.MerchantType;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.videolibrary.itemhandle.LiveListItemHelper;
import com.yhy.common.beans.net.model.DefaultCityBean;
import com.yhy.common.beans.net.model.RCShowcase;
import com.yhy.common.beans.net.model.master.TalentUserInfo;
import com.yhy.common.beans.net.model.search.BaseHistorySearch;
import com.yhy.common.beans.net.model.trip.CityInfo;
import com.yhy.common.beans.net.model.trip.ShortItem;
import com.yhy.common.beans.net.model.trip.TagInfo;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;
import com.yhy.location.LocationManager;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:MasterViewHelper
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/3/8
 * Time:下午2:42
 * Version 1.0
 */

public class MasterViewHelper {

    private static MasterViewHelper instance = new MasterViewHelper();
    @Autowired
    IUserService userService;

    private MasterViewHelper() {
        YhyRouter.getInstance().inject(this);
    }

    public static MasterViewHelper getInstance(){
        return instance;
    }
    /**
     * 绑定达人列表的视图
     *
     * @param context
     * @param helper
     * @param item
     */
    public static void handleMasterListItem(final Activity context, BaseAdapterHelper helper, final TalentUserInfo item) {
        helper.setImageUrlRound(R.id.iv_master_userhead, item.avatar, 120, 120, R.mipmap.icon_default_avatar).
                setText(R.id.tv_master_username, item.nickName).
                setText(R.id.tv_master_content, item.serveDesc).
                setText(R.id.tv_service_count, StringUtil.formatServiceCount(context, (int) item.serveCount));
        Gendar.setGendarIcon((ImageView) helper.getView(R.id.iv_master_sex), item.gender);

        helper.setOnClickListener(R.id.iv_master_userhead, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.gotoMasterHomepage(context, item.userId);
            }
        });
        helper.setText(R.id.tv_master_location, item.city);
//        helper.setVisible(R.id.iv_master_vip, item.type);
    }

    /**
     * 绑定热门搜索的视图
     *
     * @param context
     * @param helper
     * @param item
     */
    public static void handleMasterHotSearchItem(final Activity context, BaseAdapterHelper helper, RCShowcase item) {
        helper.setText(R.id.tv_text, item.operationContent);
    }

    /**
     * 绑定历史搜索的视图
     *
     * @param context
     * @param helper
     * @param item
     */
    public static void handleMasterHistorySearchItem(final Activity context, BaseAdapterHelper helper, BaseHistorySearch item) {
        helper.setText(R.id.tv_text, item.text);
    }

    /**
     * 绑定线路列表的视图
     *
     * @param context
     * @param helper
     * @param item
     */
    public static void handleMasterSearchLineItem(final Activity context, BaseAdapterHelper helper, final ShortItem item) {
        helper.setImageUrl(R.id.item_home_recommend_img, item.mainPicUrl, 750, 360, R.mipmap.ic_default_list_big)
                .setRelativeLayoutParams(R.id.item_home_recommend_img,
                        new RelativeLayout.LayoutParams(ScreenSize.getScreenHeightExcludeStatusBar(context.getApplicationContext()),
                                DisplayUtils.getImgHeight(context)))
                .setText(R.id.item_home_recommend_title, item.title)
                .setText(R.id.item_home_label_view, geteLablesString(item.tagList)); //标签
        helper.setText(R.id.item_home_recommend_price, StringUtil.convertPriceNoSymbolExceptLastZero(item.price));
        if (item.userInfo != null) {
            helper.setHeaderIcon(R.id.iv_home_recommend_shop_head, item.userInfo.avatar, item.userInfo.sellerType,
                    context.getResources().getInteger(R.integer.list_user_head_radius),
                    context.getResources().getInteger(R.integer.list_user_head_radius));
            helper.setText(R.id.tv_home_recommend_shop_title, item.userInfo.nickname);
        }
        helper.setVisible(R.id.item_home_recommend_start_city_tv, false);//默认不现实出发城市
        helper.setVisible(R.id.item_home_active_state_img, false);//默认不显示活动状态
        helper.setVisible(R.id.tv_home_recommend_sales, false);//默认不显示已售数量

        if (ItemType.TOUR_LINE.equals(item.itemType) || ItemType.FREE_LINE.equals(item.itemType) ||
                ItemType.TOUR_LINE_ABOARD.equals(item.itemType) || ItemType.FREE_LINE_ABOARD.equals(item.itemType)) {
            if (item.sales >= 0) {
                helper.setVisible(R.id.tv_home_recommend_sales, true);
//                helper.setText(R.id.tv_home_recommend_sales, StringUtil.formatSales(context,item.sales));
                helper.setText(R.id.tv_home_recommend_sales, "");
            }
        } else if (ItemType.CITY_ACTIVITY.equals(item.itemType)) {
            helper.setVisible(R.id.tv_home_recommend_sales, true);
            if (ValueConstants.ACTIVITY_STATE_EXPIRED.equals(item.status)) {
                helper.setVisible(R.id.item_home_active_state_img, true);
            }
            StringBuffer sb = new StringBuffer();/*sb.append(item.sales).append(context.getString(R.string.label_string_apply_people))*/
            ;
            helper.setText(R.id.tv_home_recommend_sales, sb.toString());
        }

        helper.setOnClickListener(R.id.iv_home_recommend_shop_head, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转商铺详情
                if (item.userInfo == null) {
                    return;
                }
                long uid = item.userInfo.userId == 0 ? item.userInfo.id : item.userInfo.userId;
                if (MerchantType.TALENT.equals(item.userInfo.sellerType)) {
                    NavUtils.gotoMasterHomepage(context, uid);
                } else {
                    NavUtils.gotoShopHomePageActivity(context, item.title, uid);
                }
            }
        });

        helper.setOnClickListener(R.id.tv_home_recommend_shop_title, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.userInfo == null) {
                    return;
                }
                long uid = item.userInfo.userId == 0 ? item.userInfo.id : item.userInfo.userId;
                if (MerchantType.TALENT.equals(item.userInfo.sellerType)) {
                    NavUtils.gotoMasterHomepage(context, uid);
                } else {
                    NavUtils.gotoShopHomePageActivity(context, item.title, uid);
                }
            }
        });
    }

    /**
     * 绑定线路列表的视图
     *
     * @param context
     * @param helper
     * @param item
     */
    public static void handleLineItem(final Activity context, BaseAdapterHelper helper, final ShortItem item, String pageSource) {
        String cityCode = "";
        if (ItemType.TOUR_LINE.equals(pageSource) || ItemType.TOUR_LINE_ABOARD.equals(pageSource)) {
            String packCityName = SPUtils.getPackTripCityCode(context.getApplicationContext());
            String homeCityName = SPUtils.getExtraCurrentCityName(context.getApplicationContext());
            if (SPUtils.isJumpFromHomeSearch(context.getApplicationContext())) {
                if (SPUtils.getHomeCityIsChange(context.getApplicationContext())) {
                    String cityName = SPUtils.getHomeChangeCityCode(context.getApplicationContext());
                    cityCode = StringUtil.isEmpty(cityName) ? DefaultCityBean.cityCode : cityName;
                } else {
                    cityCode = LocationManager.getInstance().getStorage().getLast_cityCode();
                }
            } else {
                cityCode = StringUtil.isEmpty(packCityName) ? DefaultCityBean.cityCode : packCityName;
            }
        } else if (ItemType.FREE_LINE.equals(pageSource) || ItemType.FREE_LINE_ABOARD.equals(pageSource)) {
            cityCode = SPUtils.getFreeTripCityCode(context.getApplicationContext());
        } else if (ItemType.MASTER_PRODUCTS.equals(pageSource)) {
            if (SPUtils.getHomeCityIsChange(context.getApplicationContext())) {
                String cityName = SPUtils.getHomeChangeCityName(context.getApplicationContext());
                cityCode = LocationManager.getInstance().getStorage().getLast_cityCode();
            } else {
                String homeCityName = SPUtils.getExtraCurrentCityName(context.getApplicationContext());
                cityCode = LocationManager.getInstance().getStorage().getLast_cityCode();
            }
        }

        helper.setImageUrl(R.id.item_home_recommend_img, item.mainPicUrl, 750, 420, R.mipmap.ic_default_list_big)
                .setRelativeLayoutParams(R.id.item_home_recommend_img,
                        new RelativeLayout.LayoutParams(ScreenSize.getScreenHeightExcludeStatusBar(context.getApplicationContext()),
                                DisplayUtils.getImgHeight(context)))
                .setText(R.id.item_home_recommend_title, item.title)
                .setText(R.id.item_home_label_view, geteLablesString(item.tagList)); //标签
        if (ItemType.FREE_LINE.equals(item.itemType) || ItemType.TOUR_LINE.equals(item.itemType) ||
                ItemType.FREE_LINE_ABOARD.equals(item.itemType) || ItemType.TOUR_LINE_ABOARD.equals(item.itemType)) {
            helper.setText(R.id.item_home_recommend_price, StringUtil.convertPriceNoSymbolExceptLastZero(item.price));
        } else {
            helper.setText(R.id.item_home_recommend_price, StringUtil.convertPriceNoSymbolExceptLastZero(item.price));
        }
        if (item.userInfo != null) {
//            helper.setImageUrlRound(R.id.iv_home_recommend_shop_head, item.userInfo.avatar, 50, 50, R.mipmap.icon_default_avatar);
            helper.setHeaderIcon(R.id.iv_home_recommend_shop_head, item.userInfo.avatar, item.userInfo.sellerType,
                    context.getResources().getInteger(R.integer.list_user_head_radius),
                    context.getResources().getInteger(R.integer.list_user_head_radius));
            helper.setText(R.id.tv_home_recommend_shop_title, item.userInfo.nickname);
        }else{
            helper.setHeaderIcon(R.id.iv_home_recommend_shop_head, "", item.userInfo.sellerType,
                    context.getResources().getInteger(R.integer.list_user_head_radius),
                    context.getResources().getInteger(R.integer.list_user_head_radius));
            helper.setText(R.id.tv_home_recommend_shop_title, "");
        }
        helper.setOnClickListener(R.id.iv_home_recommend_shop_head, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转商铺详情
                if (item.userInfo == null) {
                    return;
                }
                long uid = item.userInfo.userId == 0 ? item.userInfo.id : item.userInfo.userId;
                if (MerchantType.TALENT.equals(item.userInfo.sellerType)) {
                    NavUtils.gotoMasterHomepage(context, uid);
                } else {
                    NavUtils.gotoShopHomePageActivity(context, item.title, uid);
                }
            }
        });

        helper.setOnClickListener(R.id.tv_home_recommend_shop_title, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.userInfo == null) {
                    return;
                }
                long uid = item.userInfo.userId == 0 ? item.userInfo.id : item.userInfo.userId;
                if (MerchantType.TALENT.equals(item.userInfo.sellerType)) {
                    NavUtils.gotoMasterHomepage(context, uid);
                } else {
                    NavUtils.gotoShopHomePageActivity(context, item.title, uid);
                }
            }
        });
        helper.setVisible(R.id.item_home_recommend_start_city_tv, false);//默认不现实出发城市
        helper.setVisible(R.id.item_home_active_state_img, false);//默认不显示活动状态
        helper.setVisible(R.id.tv_home_recommend_sales, false);//默认不显示已售数量

        if (ItemType.TOUR_LINE.equals(pageSource) || ItemType.FREE_LINE.equals(pageSource) ||
                ItemType.TOUR_LINE_ABOARD.equals(pageSource) || ItemType.FREE_LINE_ABOARD.equals(pageSource) ||
                ItemType.MASTER_PRODUCTS.equals(pageSource)) {
            List<CityInfo> cityInfos = item.startCityList;
            if (cityInfos != null && cityInfos.size() > 0) {
                helper.setVisible(R.id.item_home_recommend_start_city_tv, true);
                String text = context.getString(R.string.label_all_city);
                for (CityInfo cityInfo : cityInfos) {
                    if (!TextUtils.isEmpty(cityCode) && cityCode.equals(cityInfo.cityCode + "")) {
                        text = cityInfo.name.replaceAll(context.getString(R.string.label_city), "");
                        break;
                    }
                }
                helper.setText(R.id.item_home_recommend_start_city_tv, String.format(
                        context.getString(R.string.label_where_departure), text));
            }
            if (item.sales >= 0) {
                helper.setVisible(R.id.tv_home_recommend_sales, true);
//                helper.setText(R.id.tv_home_recommend_sales, StringUtil.formatSales(context,item.sales));
                helper.setText(R.id.tv_home_recommend_sales, "");
            }
            if (ItemType.CITY_ACTIVITY.equals(item.itemType) && ItemType.MASTER_PRODUCTS.equals(pageSource)) {
                if (ValueConstants.ACTIVITY_STATE_EXPIRED.equals(item.status)) {
                    helper.setVisible(R.id.item_home_active_state_img, true);
                }

                StringBuffer sb = new StringBuffer();
//                sb.append(item.sales).append(context.getString(R.string.label_string_apply_people));
                double lat;
                try {
                    lat = Double.parseDouble(SPUtils.getExtraCurrentLat(context.getApplicationContext()));
                } catch (NumberFormatException e) {
                    lat = 0;
                } catch (NullPointerException ex) {
                    lat = 0;
                }
                if (lat > 0) {
                    sb/*.append("    |    ")*/
                            .append(StringUtil.formatDistance(context, item.distance));
                }
                helper.setText(R.id.tv_home_recommend_sales, sb.toString());
            }
        } else if (ItemType.CITY_ACTIVITY.equals(item.itemType)) {
            helper.setVisible(R.id.tv_home_recommend_sales, true);
            if (ValueConstants.ACTIVITY_STATE_EXPIRED.equals(item.status)) {
                helper.setVisible(R.id.item_home_active_state_img, true);
            }
            StringBuffer sb = new StringBuffer();/*sb.append(item.sales).append(context.getString(R.string.label_string_apply_people));*/
            double lat;
            try {
                lat = Double.parseDouble(SPUtils.getExtraCurrentLat(context.getApplicationContext()));
            } catch (NumberFormatException e) {
                lat = 0;
            } catch (NullPointerException ex) {
                lat = 0;
            }
            if (lat > 0) {
                sb/*.append("    |    ")*/
                        .append(StringUtil.formatDistance(context, item.distance));
            }
            helper.setText(R.id.tv_home_recommend_sales, sb.toString());
        } else {
            if (ItemType.NORMAL.equals(item.itemType)) {
                //必买不显示已售数量
                helper.setVisible(R.id.tv_home_recommend_sales, false);
            } else {
                if (item.sales >= 0) {
                    helper.setVisible(R.id.tv_home_recommend_sales, true);
//                    helper.setText(R.id.tv_home_recommend_sales, StringUtil.formatSales(context, item.sales));
                    helper.setText(R.id.tv_home_recommend_sales, "");
                }
            }
        }
    }

    /**
     * 绑定店铺商品列表的视图
     *
     * @param context
     * @param helper
     * @param item
     */
    public static void handleShopProductsItem(final Activity context, BaseAdapterHelper helper, final ShortItem item, String cityCode) {
        String bgUrl = item.mainPicUrl;
        int width = (int) com.quanyan.yhy.ui.common.calendar.ScreenUtil.getScreenWidth(context);
        int height = (int) (width / ValueConstants.SCALE_PRODUCT_LIST_IMG);
        helper.setFrescoImageUrl(R.id.item_home_recommend_img, bgUrl, (int) (width * 1.5f), (int) (height * 1.5f), R.mipmap.ic_default_list_big)
                .setRelativeLayoutParams(R.id.item_home_recommend_img, new RelativeLayout.LayoutParams(width, height))
                .setText(R.id.item_home_recommend_title, item.title);
        HarwkinLogUtil.info("width = " + width + ",height = " + height);

        helper.setText(R.id.item_home_recommend_price, StringUtil.convertPriceNoSymbolExceptLastZero(item.price));
        helper.setVisible(R.id.item_home_active_state_img, false);//默认不显示活动状态
        helper.setVisible(R.id.tv_home_recommend_sales, false);//默认显示已售数量

        if (ItemType.TOUR_LINE.equals(item.itemType) || ItemType.FREE_LINE.equals(item.itemType) ||
                ItemType.TOUR_LINE_ABOARD.equals(item.itemType) || ItemType.FREE_LINE_ABOARD.equals(item.itemType) ||
                ItemType.ARROUND_FUN.equals(item.itemType)) {
            helper.setVisible(R.id.tv_home_recommend_sales, (item.sales >= 0));//默认显示已售数量
            helper.setText(R.id.tv_home_recommend_sales, "");
        } else if (ItemType.CITY_ACTIVITY.equals(item.itemType)) {
            if (ValueConstants.ACTIVITY_STATE_EXPIRED.equals(item.status)) {
                helper.setVisible(R.id.item_home_active_state_img, true);
            }
            StringBuffer sb = new StringBuffer();
            double lat;
            try {
                lat = Double.parseDouble(SPUtils.getExtraCurrentLat(context.getApplicationContext()));
            } catch (NumberFormatException e) {
                lat = 0;
            } catch (NullPointerException ex) {
                lat = 0;
            }
            if (lat > 0 && item.distance > 0 && !StringUtil.isEmpty(SPUtils.getExtraCurrentLat(context.getApplicationContext()))) {
                sb.append(StringUtil.formatDistance(context, item.distance));
            }
            helper.setVisible(R.id.tv_home_recommend_sales, true);
            helper.setText(R.id.tv_home_recommend_sales, sb.toString());
        } else {
            if (ItemType.NORMAL.equals(item.itemType)) {
                //必买不显示已售数量
                helper.setVisible(R.id.tv_home_recommend_sales, false);
            } else {
                helper.setVisible(R.id.tv_home_recommend_sales, true);
                helper.setText(R.id.tv_home_recommend_sales, "");
            }
        }

        if (ItemType.MASTER_CONSULT_PRODUCTS.equals(item.itemType)) {
            helper.setVisible(R.id.ll_item_home_pice, false);
            helper.setVisible(R.id.ll_item_home_isfree, true);
            helper.setVisible(R.id.ll_item_service_pice, false);
            helper.setText(R.id.item_home_label_view, "·咨询服务");
        } else if (ItemType.LINE.equals(item.itemType)) {
            helper.setVisible(R.id.ll_item_home_pice, true);
            helper.setVisible(R.id.ll_item_home_isfree, false);
            helper.setVisible(R.id.ll_item_service_pice, false);
            if (!StringUtil.isEmpty(geteLablesString(item.tagList))) {
                helper.setText(R.id.item_home_label_view, geteLablesString(item.tagList)); //标签
            }
        } else {
            helper.setVisible(R.id.ll_item_home_pice, true);
            helper.setVisible(R.id.ll_item_home_isfree, false);
            helper.setVisible(R.id.ll_item_service_pice, false);
            if (!StringUtil.isEmpty(geteLablesString(item.tagList))) {
                helper.setText(R.id.item_home_label_view, geteLablesString(item.tagList)); //标签
            }
        }

        if (ItemType.MASTER_CONSULT_PRODUCTS.equals(item.itemType)) {
            if (item.price == 0) {
                helper.setText(R.id.tv_master_isfree, "限时免费");
                helper.setText(R.id.tv_master_integral, item.originalPrice/10 + "");
                helper.setText(R.id.tv_master_consult_time, "积分/" + item.consultTime / 60 + "分钟");
                helper.setFlags(R.id.tv_master_integral, Paint.STRIKE_THRU_TEXT_FLAG);
                helper.setFlags(R.id.tv_master_consult_time, Paint.STRIKE_THRU_TEXT_FLAG);
                helper.setVisible(R.id.ll_item_home_isfree, true);
                helper.setVisible(R.id.ll_item_service_pice, false);
            } else {
                helper.setVisible(R.id.ll_item_home_isfree, false);
                helper.setVisible(R.id.ll_item_service_pice, true);
                helper.setText(R.id.item_home_service_price, item.price/10 + "积分/" + item.consultTime / 60 + "分钟");

            }
        }
    }

    /**
     * 绑定达人目的地数据
     *
     * @param context
     * @param helper
     * @param item
     */
    public static void handleMasterDestItem(final Activity context, BaseAdapterHelper helper, final RCShowcase item) {
        helper.setText(R.id.tv_text, item.title);
    }


    public static String geteLablesString(List<TagInfo> comTagInfos) {
        StringBuilder stringBuilder = new StringBuilder();
        if (comTagInfos != null && comTagInfos.size() > 0) {
            int size = comTagInfos.size() > 3 ? 3 : comTagInfos.size();
            for (int i = 0; i < size; i++) {
                stringBuilder.append("・")
                        .append(comTagInfos.get(i).name);
            }
            return stringBuilder.toString();
        }
        return "";
    }


    public static void handleMasterLineItem(final Activity context, BaseAdapterHelper helper, final ShortItem item, boolean mHasNodata) {
        if (mHasNodata) {
            helper.setVisible(R.id.ll_masterline_hasdata, false)
                    .setVisible(R.id.item_live_nodata_layout, true)
                    .setText(R.id.item_live_nodata_text, "没有数据");
        } else {
            helper.setVisible(R.id.ll_masterline_hasdata, true)
                    .setVisible(R.id.item_live_nodata_layout, false);
            helper.setImageUrl(R.id.iv_topic_detail, item.mainPicUrl, 0, 0, R.mipmap.icon_default_310_180);
            helper.setText(R.id.tv_master_price, StringUtil.converRMb2YunWithFlag(context, item.price));
            helper.setText(R.id.tv_topic_content, item.title);
            if (item.tagList != null && item.tagList.size() != 0) {
                StringBuffer buffer = new StringBuffer();
                for (int i = 0; i < item.tagList.size(); i++) {
                    buffer.append("· " + item.tagList.get(i).name + " ");
                }
                helper.setText(R.id.tv_topic_title, buffer.toString());
            } else {
                helper.setText(R.id.tv_topic_title, "");
            }
        }
    }

    /**
     * @param context
     * @param helper
     * @param item
     */
    public static void handleMasterAdviceListItem(final Activity context, BaseAdapterHelper helper, final ShortItem item) {
        helper.setFlags(R.id.tv_master_consult_service_dis_unit, Paint.STRIKE_THRU_TEXT_FLAG);

        helper.setImageUrl(R.id.image_consult, item.mainPicUrl, 346 * 2, 264 * 2, R.mipmap.icon_default_215_150);
        helper.setText(R.id.tv_master_consult_service_title, item.title);//资深云南旅游达人咨询服务
        helper.setText(R.id.tv_master_consult_service_adress, TextUtils.isEmpty(item.destinations) ? "无" : item.destinations);
//        helper.setText(R.id.tv_master_consult_integral, StringUtil.convertPriceNoSymbolExceptLastZero(item.price) + "");//0
        helper.setText(R.id.tv_master_consult_integral, item.price + "");
        if (item.price == 0) {//是否限时免费
            helper.setVisible(R.id.image_free, true);
        } else {
            helper.setVisible(R.id.image_free, false);
        }
        helper.setText(R.id.tv_master_consult_time, "积分/" + item.consultTime / 60 + "分钟");
//        helper.setText(R.id.tv_master_consult_service_dis_unit, StringUtil.convertPriceNoSymbolExceptLastZero(item.originalPrice) + "积分");//1200积分
        helper.setText(R.id.tv_master_consult_service_dis_unit, item.originalPrice + "积分");

        if (item.userInfo != null && item.userInfo.id != 0) {
            if (instance.userService.getLoginUserId() == item.userInfo.id) {
                helper.setVisibleOrInVisible(R.id.btn_start_master_consult_service, false);
            } else {
                helper.setVisibleOrInVisible(R.id.btn_start_master_consult_service, true);
            }
        }

        //达人是否在线
        if (!TextUtils.isEmpty(item.onlineStatus)) {
            if (item.onlineStatus.equals("ONLINE")) {
                helper.setTextColor(R.id.btn_start_master_consult_service, context.getResources().getColor(R.color.white));
                helper.setBackgroundRes(R.id.btn_start_master_consult_service, R.drawable.btn_orange_selector);
                helper.setText(R.id.btn_start_master_consult_service,context.getString(R.string.consult_now));
            } else if (item.onlineStatus.equals("NOTONLINE")) {
                helper.setBackgroundRes(R.id.btn_start_master_consult_service, R.drawable.shape_tv_integral_rob_no_date);
                helper.setTextColor(R.id.btn_start_master_consult_service, context.getResources().getColor(R.color.white));
                helper.setText(R.id.btn_start_master_consult_service,context.getString(R.string.label_btn_offline));
            }
        } else {
            helper.setBackgroundRes(R.id.btn_start_master_consult_service, R.drawable.shape_tv_integral_rob_no_date);
            helper.setTextColor(R.id.btn_start_master_consult_service, context.getResources().getColor(R.color.white));
        }

        helper.setOnClickListener(R.id.btn_start_master_consult_service, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(item.onlineStatus)) {
                    if (item.onlineStatus.equals("ONLINE")) {
                        NavUtils.gotoMasterConsultActivity(context, item.id, item.price, item.consultTime);
                    } else if (item.onlineStatus.equals("NOTONLINE")) {
//                        DialogUtil.showMessageDialog(context, null, context.getString(R.string.dialog_advice_no_fromsale), context.getString(R.string.dialog_advice_bt), null, null, null, null, null).show();
                    }
                } else {
//                    DialogUtil.showMessageDialog(context, null, context.getString(R.string.dialog_advice_no_fromsale), context.getString(R.string.dialog_advice_bt), null, null, null, null, null).show();
                }
            }
        });

        helper.setOnClickListener(R.id.ll_master_consult, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tcEvent(context, item);
                NavUtils.gotoProductDetail(context,
                        ItemType.MASTER_CONSULT_PRODUCTS,
                        item.id,
                        "");
            }
        });
    }

    private static void tcEvent(Activity context, ShortItem item) {
        Map<String, String> map = new HashMap<>();
        map.put(AnalyDataValue.KEY_MID, item.userInfo.userId + "");
        map.put(AnalyDataValue.KEY_MNAME, item.userInfo.nickname);
        map.put(AnalyDataValue.KEY_PID, item.id + "");
        map.put(AnalyDataValue.KEY_PNAME, item.title);
        TCEventHelper.onEvent(context, AnalyDataValue.CONSULTING_HOME_CONSULT_ITEM_CLICK, map);
    }


    public static void handleLineMasterItem(final Activity context, BaseAdapterHelper helper, final ShortItem item) {
        helper.setImageUrl(R.id.item_home_recommend_img, item.mainPicUrl, 750, 360, R.mipmap.ic_default_list_big)
                .setRelativeLayoutParams(R.id.item_home_recommend_img,
                        new RelativeLayout.LayoutParams(ScreenSize.getScreenHeightExcludeStatusBar(context.getApplicationContext()),
                                DisplayUtils.getImgHeight(context)))
                .setText(R.id.item_home_recommend_title, item.title)
                .setText(R.id.item_home_label_view, geteLablesString(item.tagList)); //标签
        helper.setText(R.id.item_home_recommend_price, StringUtil.convertPriceNoSymbolExceptLastZero(item.price));
        if (item.userInfo != null) {
            helper.setHeaderIcon(R.id.iv_home_recommend_shop_head, item.userInfo.avatar, item.userInfo.sellerType,
                    context.getResources().getInteger(R.integer.list_user_head_radius),
                    context.getResources().getInteger(R.integer.list_user_head_radius));
            helper.setText(R.id.tv_home_recommend_shop_title, item.userInfo.nickname);
        }
        helper.setOnClickListener(R.id.iv_home_recommend_shop_head, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转商铺详情
                if (item.userInfo == null) {
                    return;
                }
                long uid = item.userInfo.userId == 0 ? item.userInfo.id : item.userInfo.userId;
                if (MerchantType.TALENT.equals(item.userInfo.sellerType)) {
                    NavUtils.gotoMasterHomepage(context, uid);
                } else {
                    NavUtils.gotoShopHomePageActivity(context, item.title, uid);
                }
            }
        });

        helper.setOnClickListener(R.id.tv_home_recommend_shop_title, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.userInfo == null) {
                    return;
                }
                long uid = item.userInfo.userId == 0 ? item.userInfo.id : item.userInfo.userId;
                if (MerchantType.TALENT.equals(item.userInfo.sellerType)) {
                    NavUtils.gotoMasterHomepage(context, uid);
                } else {
                    NavUtils.gotoShopHomePageActivity(context, item.title, uid);
                }
            }
        });
        helper.setVisible(R.id.item_home_recommend_start_city_tv, false);//默认不现实出发城市
        helper.setVisible(R.id.item_home_active_state_img, false);//默认不显示活动状态
        helper.setVisible(R.id.tv_home_recommend_sales, false);//默认不显示已售数量
    }

}
