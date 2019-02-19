package com.quanyan.yhy.common;


import com.yhy.common.types.BannerType;

/**
 * Created with Android Studio.
 * Title:ItemType
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:和平必胜、正义必胜、人民必胜
 * Author:炎黄子孙
 * Date:15/11/30
 * Time:下午4:54
 * Version 1.0
 */
public class ItemType {
    //咨询服务
    public static final String CONSULT = "CONSULT";
//    //积分商城商品
//    public static final String INTEGRALMALL = "INTEGRALMALL";
    //普通商品交易
    public static final String NORMAL = "NORMAL";
    //积分商品评价
    public static final String POINT = "POINT";

    //积分商品
    public static final String POINT_MALL = "POINT_MALL";
    //线路商品
    public static final String LINE = "LINE";
    //咨询服务目的地
    public static final String SERVICE = "SERVICE";
    //发布服务目的地
    public static final String PUBLISH_SERVICE = "PUBLISH_SERVICE";
    //无国外的目的地类型
    public static final String URBAN_LINE = "URBAN_LINE";
    //酒店商品
    public static final String HOTEL = "HOTEL";
    //景区商品
    public static final String SCENIC = "SCENIC";
    //景区门票
    public static final String SPOTS = "SPOTS";
    //境内跟团游
    public static final String TOUR_LINE = "TOUR_LINE";
    //导览（自定义）
    public static final String TOUR_DUIDE= "TOUR_DUIDE";
    //境外跟团游
    public static final String TOUR_LINE_ABOARD = "TOUR_LINE_ABOARD";
    //境内自由行
    public static final String FREE_LINE = "FREE_LINE";
    //境外自由行
    public static final String FREE_LINE_ABOARD = "FREE_LINE_ABOARD";
    //同城活动
    public static final String CITY_ACTIVITY = "CITY_ACTIVITY";
    //店铺类型
    public static final String SHOP_HOMEPAGE = "SHOP_HOMEPAGE";

    //周边游 TODO ONLY USER IN SEARCH PAGE
    public static final String ARROUND_FUN = "ARROUND_FUN";
    //达人 TODO ONLY USER IN SEARCH PAGE
    public static final String MASTER = "MASTER";
    //达人商品 TODO ONLY USER IN SEARCH PAGE
    public static final String MASTER_PRODUCTS = "MASTER_PRODUCTS";
    //达人咨询商品
    public static final String MASTER_CONSULT_PRODUCTS = "CONSULT";
    //景区搜索列表
    public static final String KEY_SCENIC_TYPE_LIST = "scenic_list";
    //景区主题 TODO ONLY USER IN SEARCH PAGE
    public static final String KEY_SCENIC_TYPE_THEME = "scenic_theme";
    //景区周边 TODO ONLY USER IN SEARCH PAGE
    public static final String KEY_SCENIC_TYPE_AROUND = "scenic_around";
    //景区本地列表 TODO ONLY USER IN SEARCH PAGE
    public static final String KEY_SCENIC_TYPE_LOCAL = "scenic_local";
    //美食类型
    public static final String KEY_FOOD = "DELICIOUS_FOOD";
    /**
     * 根据跳转类型获取商品类型
     *
     * @param type
     * @return
     */
    public static String getItemTypeByBannerType(String type) {
        if (BannerType.STR_PACKAGE_TOUR_HOME_PAGE.equals(type)) {
            return TOUR_LINE;
        } else if (BannerType.STR_FREE_TOUR_HOME_PAGE.equals(type)) {
            return FREE_LINE;
        } else if (BannerType.STR_CITY_ACTIVITY_HOME_PAGE.equals(type)) {
            return CITY_ACTIVITY;
        } else if (BannerType.STR_ARROUND_FUN_HOME_PAGE.equals(type)) {
            return ARROUND_FUN;
        }
        return null;
    }

    /**
     * 根据商品类型获取过滤类型
     *
     * @param type
     * @return
     */
    public static String getFilterTypeByItemType(String type){
        if(TOUR_LINE.equals(type) || TOUR_LINE_ABOARD.equals(type)){
            return FilterType.QUANYAN_GROUP_TRAVELL_QUERY_TERN;
        }else if(FREE_LINE.equals(type)|| FREE_LINE_ABOARD.equals(type)){
            return FilterType.QUANYAN_FREE_TRAVELL_QUERY_TERN;
        }else if(CITY_ACTIVITY.equals(type)){
            return FilterType.QUANYAN_ACTIVITY_QUERY_TERN;
        }else if(ARROUND_FUN.equals(type)){
            return FilterType.QUANYAN_NEARBY_ENJOY_QUERY_TERN;
        }else if(MASTER.equals(type)){
            return FilterType.QUANYAN_MASTER_QUERY_TERN;
        }else if(MASTER_PRODUCTS.equals(type)){
            return FilterType.QUANYAN_MASTER_ITEM_QUERY_TERN;
        }
        return null;
    }

    /**
     * 判断是否为线路
     * @param type
     * @return
     */
    public boolean isLine(String type){
        return TOUR_LINE.equals(type) ||
                TOUR_LINE_ABOARD.equals(type) ||
                FREE_LINE.equals(type)||
                FREE_LINE_ABOARD.equals(type) ||
                ARROUND_FUN.equals(type);
    }

    /**
     * 判断是否为境内境外跟团游
     * @param type
     * @return
     */
    public boolean isTourLine(String type){
        return TOUR_LINE.equals(type) ||
                TOUR_LINE_ABOARD.equals(type);
    }
    /**
     * 判断是否为境内自由行
     * @param type
     * @return
     */
    public boolean isFreeLine(String type){
        return FREE_LINE.equals(type)||
                FREE_LINE_ABOARD.equals(type) ;
    }
}
