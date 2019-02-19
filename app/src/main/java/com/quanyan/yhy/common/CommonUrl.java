package com.quanyan.yhy.common;

import android.content.Context;

import com.yhy.common.utils.SPUtils;


/**
 * Created with Android Studio.
 * Title:CommonUrl
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/4/6
 * Time:下午1:13
 * Version 1.0
 */
public class CommonUrl {
    /*********************图文详情***************************/
    //对应商品中的图文详情信息
    public static final String KEY_ITEM = "ITEM";
    //对应达人中的图文详情信息
    public static final String KEY_EXPERT = "EXPERT";
    //酒店图文介绍
    public static final String KEY_HOTEL = "HOTEL";
    public static final String KEY_HOTEL_PICTURE = "HOTEL_PICTURE";
    //景区图文介绍
    public static final String KEY_SCENIC = "SCENIC";
    public static final String KEY_SCENIC_PICTURE = "SCENIC_PICTURE";
    //达人咨询服务商品详情图文介绍
    public static final String KEY_CONSULTING_SERVICE = "CONSULTING_SERVICE";
    /*********************分享链接***************************/
    //对应自由行中的分享链接前缀
    public static final String KEY_SHARE_FREETRAVEL = "FREETRAVEL_SHARE";
    //对应跟团游中的分享链接前缀
    public static final String KEY_SHARE_TEAMTRAVEL = "TEAMTRAVEL_SHARE";
    //对应同城活动中的分享链接前缀
    public static final String KEY_SHARE_LOCALPLOY = "LOCALPLOY_SHARE";
    //对应必买中的分享链接前缀
    public static final String KEY_SHARE_MUSTBUY = "MUSTBUY_SHARE";
    //对应咨询服务商品详情中的分享链接前缀
    public static final String KEY_SHARE_CONSULTING_SERVICE = "CONSULTING_SERVICE_SHARE";
    //对应店铺首页分享链接前缀
    public static final String KEY_SHOP_HOMEPAGE = "URL_SHOP_HOMPAGE_AUDIO_SUFFIX";

    /**
     * 根据商品类型获取分享链接的前缀
     * @param context
     * @param itemType
     * @return
     */
    public static String getShareUrlSuffix(Context context,String itemType){
        if(ItemType.TOUR_LINE.equals(itemType) || ItemType.TOUR_LINE_ABOARD.equals(itemType)){
            return SPUtils.getShareDefaultUrl(context,KEY_SHARE_TEAMTRAVEL);
        }else if(ItemType.FREE_LINE.equals(itemType) || ItemType.FREE_LINE_ABOARD.equals(itemType)){
            return SPUtils.getShareDefaultUrl(context,KEY_SHARE_FREETRAVEL);
        }else if(ItemType.CITY_ACTIVITY.equals(itemType)){
            return SPUtils.getShareDefaultUrl(context,KEY_SHARE_LOCALPLOY);
        }else if(ItemType.NORMAL.equals(itemType)||ItemType.POINT_MALL.equals(itemType)){
            return SPUtils.getShareDefaultUrl(context,KEY_SHARE_MUSTBUY);
        }else if(ItemType.CONSULT.equals(itemType)){
            return SPUtils.getShareDefaultUrl(context,KEY_SHARE_CONSULTING_SERVICE);
        }else if(SysConfigType.URL_SHOP_HOMPAGE_AUDIO_SUFFIX.equals(itemType)){
            return SPUtils.getShareDefaultUrl(context, KEY_SHOP_HOMEPAGE);
        }else if(SysConfigType.URL_ADD_QR_HEAD.equals(itemType)){
            return SPUtils.getShareDefaultUrl(context, SysConfigType.URL_ADD_QR_HEAD);
        }else if(SysConfigType.URL_LIVE_SHARE_LINK.equals(itemType)){
            return SPUtils.getShareDefaultUrl(context, SysConfigType.URL_LIVE_SHARE_LINK);
        }
        return null;
    }

    /**
     * 根据商品类型获取图文详情的前缀
     * @param context
     * @param itemType
     * @return
     */
    public static String getItemDetailUrlSuffix(Context context, String itemType){
        if(ItemType.TOUR_LINE.equals(itemType) ||
                ItemType.TOUR_LINE_ABOARD.equals(itemType) ||
                ItemType.FREE_LINE.equals(itemType) ||
                ItemType.FREE_LINE_ABOARD.equals(itemType) ||
                ItemType.ARROUND_FUN.equals(itemType) ||
                ItemType.CITY_ACTIVITY.equals(itemType) ||
                ItemType.NORMAL.equals(itemType)){
            return SPUtils.getShareDefaultUrl(context,KEY_ITEM);
        } else if(ItemType.MASTER.equals(itemType)){
            return SPUtils.getShareDefaultUrl(context,KEY_EXPERT);
        } else if(ItemType.HOTEL.equals(itemType)){
            return SPUtils.getShareDefaultUrl(context,KEY_HOTEL_PICTURE);
        } else if(ItemType.SCENIC.equals(itemType)){
            return SPUtils.getShareDefaultUrl(context,KEY_SCENIC_PICTURE);
        } else if(ItemType.KEY_FOOD.equals(itemType)){
            return SPUtils.getShareDefaultUrl(context,ItemType.KEY_FOOD);
        }else if(ItemType.MASTER_CONSULT_PRODUCTS.equals(itemType)){
            return SPUtils.getShareDefaultUrl(context, CommonUrl.KEY_CONSULTING_SERVICE);
        }else if(SysConfigType.URL_GUIDE_SCENIC_INTRODUCE.equals(itemType)){
            return SPUtils.getShareDefaultUrl(context, SysConfigType.URL_GUIDE_SCENIC_INTRODUCE);
        }
        return null;
    }
}
