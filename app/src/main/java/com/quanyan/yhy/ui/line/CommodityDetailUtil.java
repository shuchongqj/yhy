package com.quanyan.yhy.ui.line;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.harwkin.nb.camera.ImageUtils;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.CommentType;
import com.quanyan.yhy.common.CommonUrl;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.comment.FullCommentFragment;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.line.fragment.CommodityPropertiesFragment;
import com.quanyan.yhy.ui.line.fragment.CommodityRouteFragment;
import com.quanyan.yhy.ui.line.fragment.CommodityWebInfoFragment;
import com.quanyan.yhy.ui.line.view.CommodityDetailTopView;
import com.quanyan.yhy.ui.line.view.MasterDetailTopView;
import com.quanyan.yhy.ui.travel.controller.TravelController;
import com.quanyan.yhy.view.CommodityBottomView;
import com.yhy.common.beans.net.model.ShareBean;
import com.yhy.common.beans.net.model.item.CityActivityDetail;
import com.yhy.common.beans.net.model.shop.MerchantItem;
import com.yhy.common.beans.net.model.trip.ItemVO;
import com.yhy.common.beans.net.model.trip.LineItemDetail;

import java.util.ArrayList;

/**
 * Created with Android Studio.
 * Title:CommodityDetailFragmentUtil
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/7/29
 * Time:19:55
 * Version 1.1.0
 */
public class CommodityDetailUtil {

    public static ArrayList<Fragment> getFragment(long mLineId, String mPageType) {
        ArrayList<Fragment> fragments = new ArrayList<>();
        if (ItemType.POINT_MALL.equals(mPageType)) {
            fragments.add(CommodityWebInfoFragment.getInstance(mLineId, ItemType.NORMAL));
        } else {
            fragments.add(CommodityWebInfoFragment.getInstance(mLineId, mPageType));
        }

        if (ItemType.NORMAL.equals(mPageType) || ItemType.CITY_ACTIVITY.equals(mPageType) || ItemType.POINT_MALL.equals(mPageType)
                || ItemType.MASTER_CONSULT_PRODUCTS.equals(mPageType)) {
            //必买 同城活动的商品详情
            if (ItemType.POINT_MALL.equals(mPageType)) {
                fragments.add(CommodityPropertiesFragment.getInstance(mLineId, ItemType.NORMAL));
            } else {
                fragments.add(CommodityPropertiesFragment.getInstance(mLineId, mPageType));
            }
        } else {
            //线路列表
            fragments.add(CommodityRouteFragment.getInstance(mLineId, mPageType));
        }

        //评价列表暂无
        if (ItemType.NORMAL.equals(mPageType) || ItemType.POINT_MALL.equals(mPageType)) {
            //必买 同城活动,积分商城，商品详情
            FullCommentFragment fullCommentFragment = FullCommentFragment.getInstance(mLineId, CommentType.POINT, mPageType, true);
            fragments.add(fullCommentFragment);
        } else if (ItemType.CITY_ACTIVITY.equals(mPageType)) {
            FullCommentFragment fullCommentFragment = FullCommentFragment.getInstance(mLineId, CommentType.LOCAL, mPageType, true);
            fragments.add(fullCommentFragment);
        } else if (ItemType.MASTER_CONSULT_PRODUCTS.equals(mPageType)) {
            //达人咨询服务商品详情
            FullCommentFragment fullCommentFragment = FullCommentFragment.getInstance(mLineId, CommentType.CONSULT, mPageType, true);
            fragments.add(fullCommentFragment);
        } else {
            //线路列表
            FullCommentFragment fullCommentFragment = FullCommentFragment.getInstance(mLineId, CommentType.LINE, mPageType, true);
            fragments.add(fullCommentFragment);
        }

        return fragments;
    }

    public static ViewGroup setTopView(Context context, String mPageType) {
        if (ItemType.FREE_LINE.equals(mPageType) || ItemType.TOUR_LINE.equals(mPageType) ||
                ItemType.FREE_LINE_ABOARD.equals(mPageType) || ItemType.TOUR_LINE_ABOARD.equals(mPageType)
                || ItemType.NORMAL.equals(mPageType) || ItemType.CITY_ACTIVITY.equals(mPageType)
                || ItemType.POINT_MALL.equals(mPageType)) {
            //// TODO: 16/3/21 跟团游 自由行 同城活动 比买商品
            CommodityDetailTopView lineItemDetailTopView = new CommodityDetailTopView(context);
            if (context instanceof CommodityBottomView.ExchangeData) {
                lineItemDetailTopView.setExchangeData((CommodityBottomView.ExchangeData) context);
            }
            if (ItemType.CITY_ACTIVITY.equals(mPageType)) {
                lineItemDetailTopView.setChooseContent(R.string.tv_choose_content_local_city);
            }

            if (ItemType.POINT_MALL.equals(mPageType)) {
                //不显示套餐类型
                lineItemDetailTopView.hideChoosePackLayout(false);
                //隐藏商铺
                lineItemDetailTopView.hideMerchatCommodityLayoutClick();
                //显示积分商品详情
                //lineItemDetailTopView.displayMerchatOrderDetailsLayoutClick();

                lineItemDetailTopView.setChooseLayoutVisible(false);
                lineItemDetailTopView.hideMarkLayout();
                //设置是否显示选择套餐的选项(积分商城)
                lineItemDetailTopView.setChoosePackageLayoutVisible(false);
            }
            if (ItemType.NORMAL.equals(mPageType)) {
                lineItemDetailTopView.setChooseLayoutVisible(false);
                lineItemDetailTopView.hideMarkLayout();
            }
            if (context instanceof View.OnClickListener) {
                lineItemDetailTopView.setOnCouponSelectClick((View.OnClickListener) context);
            }
            return lineItemDetailTopView;
        } else if (ItemType.MASTER_PRODUCTS.equals(mPageType)) {
            // TODO: 16/3/21 达人商品
            MasterDetailTopView mMasterDetailTopView = new MasterDetailTopView(context);
            if (context instanceof CommodityBottomView.ExchangeData) {
                mMasterDetailTopView.setExchangeData((CommodityBottomView.ExchangeData) context);
            }
            return mMasterDetailTopView;
        } else if (ItemType.MASTER_CONSULT_PRODUCTS.equals(mPageType)) {
            // TODO: 16/7/20 达人咨询商品
            MasterConsultDetailTopView mMasterConsultDetailTopView = new MasterConsultDetailTopView(context);
            if (context instanceof CommodityBottomView.ExchangeData) {
                mMasterConsultDetailTopView.setExchangeData((CommodityBottomView.ExchangeData) context);
            }
            return mMasterConsultDetailTopView;
        }
        return null;
    }

    public static void fetchData(Context context, TravelController travelController, long id, String pageType) {
        if (-1 != id) {
            if (ItemType.FREE_LINE.equals(pageType) || ItemType.TOUR_LINE.equals(pageType) ||
                    ItemType.FREE_LINE_ABOARD.equals(pageType) || ItemType.TOUR_LINE_ABOARD.equals(pageType)) {
                //线路详情
                travelController.doGetLineDetail(context, id);
                travelController.doGetCouponSellerList(context, 10, 1, id);
            } else if (ItemType.NORMAL.equals(pageType) || ItemType.POINT_MALL.equals(pageType)) {
                //必买详情
                travelController.doGetNormalProductDetail(context, id);
//                if (ItemType.NORMAL.equals(pageType)) {
//                travelController.doGetCouponSellerList(context, 10, 1, id);
//                }
            } else if (ItemType.CITY_ACTIVITY.equals(pageType)) {
                //活动详情
                travelController.doGetCityActivityDetail(context, id);
                travelController.doGetCouponSellerList(context, 10, 1, id);
            } else if (ItemType.MASTER_CONSULT_PRODUCTS.equals(pageType)) {
                //达人咨询服务商品详情
                travelController.doGetMasterConsultDetail(id, context);
            }
        } else {
            ToastUtil.showToast(context, context.getString(R.string.error_params));
        }
    }

    public static void share(Context context, final long mLineId, final LineItemDetail lineItemDetail,
                             final MerchantItem merchantItem, final CityActivityDetail cityActivityDetail, final String mPageType) {
        ItemVO itemVO = new ItemVO();
        ShareBean shareBean = new ShareBean();
        String shareUrlSuffix = CommonUrl.getShareUrlSuffix(context, mPageType);
        if (!StringUtil.isEmpty(shareUrlSuffix)) {
            shareBean.shareWebPage = shareUrlSuffix + mLineId;

        }
        //shareBean.shareWebPage = "http://www.yihg.hk/";

        if (ItemType.TOUR_LINE.equals(mPageType) || ItemType.FREE_LINE.equals(mPageType) ||
                ItemType.TOUR_LINE_ABOARD.equals(mPageType) || ItemType.FREE_LINE_ABOARD.equals(mPageType)) {
            //跟团游自由行
            if (lineItemDetail != null) {
                itemVO = lineItemDetail.itemVO;
                shareBean.pid = lineItemDetail.id + "";
            }
        } else if (ItemType.CITY_ACTIVITY.equals(mPageType)) {
            //同城活动
            if (cityActivityDetail != null) {
                itemVO = cityActivityDetail.itemVO;
                shareBean.pid = cityActivityDetail.id + "";
            }

        } else if (ItemType.NORMAL.equals(mPageType) || ItemType.POINT_MALL.equals(mPageType)) {
            //必买
            if (merchantItem != null) {
                itemVO = merchantItem.itemVO;
                shareBean.pid = merchantItem.itemVO.id + "";
            }
        }

        if (!StringUtil.isEmpty(itemVO.title)) {
            shareBean.shareTitle = itemVO.title;
        }

        if (itemVO.picUrls != null && !StringUtil.isEmpty(itemVO.picUrls.get(0))) {
            //System.out.println("baojie" + ImageUtils.getImageFullUrl(itemVO.picUrls.get(0)));
            shareBean.shareImageURL = ImageUtils.getImageFullUrl(itemVO.picUrls.get(0));
        }

        if (!StringUtil.isEmpty(itemVO.description)) {
            shareBean.shareContent = itemVO.description;
        }
        shareBean.pname = itemVO.title;
        shareBean.ptype = mPageType;
        shareBean.isNeedSyncToDynamic = false;
        NavUtils.gotoShareTableActivity(context, shareBean, mPageType);
    }
}
