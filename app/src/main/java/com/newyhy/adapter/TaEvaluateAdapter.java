package com.newyhy.adapter;

import android.app.Activity;
import android.graphics.Paint;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.quanyan.base.util.HarwkinLogUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ItemType;
import com.yhy.common.beans.net.model.trip.ShortItem;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;

import java.util.List;

import static com.quanyan.yhy.ui.master.helper.MasterViewHelper.geteLablesString;

/**
 * Created by yangboxue on 2018/6/22.
 */

public class TaEvaluateAdapter extends BaseQuickAdapter<ShortItem, BaseViewHolder> {

    private Activity mActivity;

    public TaEvaluateAdapter(Activity activity, List<ShortItem> data) {
        super(R.layout.item_shop_product_ver, data);
        mActivity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, ShortItem item) {
        String bgUrl = item.mainPicUrl;
        int width = (int) com.quanyan.yhy.ui.common.calendar.ScreenUtil.getScreenWidth(mActivity);
        int height = (int) (width / ValueConstants.SCALE_PRODUCT_LIST_IMG);
        ImageLoadManager.loadImage(bgUrl, R.mipmap.ic_default_list_big, (int) (width * 1.5f), (int) (height * 1.5f), helper.getView(R.id.item_home_recommend_img));
//        helper.setFrescoImageUrl(R.id.item_home_recommend_img, bgUrl, (int) (width * 1.5f), (int) (height * 1.5f), R.mipmap.ic_default_list_big);
        helper.getView(R.id.item_home_recommend_img).setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        helper.setText(R.id.item_home_recommend_title, item.title);
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
                lat = Double.parseDouble(SPUtils.getExtraCurrentLat(mActivity));
            } catch (NumberFormatException e) {
                lat = 0;
            } catch (NullPointerException ex) {
                lat = 0;
            }
            if (lat > 0 && item.distance > 0 && !StringUtil.isEmpty(SPUtils.getExtraCurrentLat(mActivity))) {
                sb.append(StringUtil.formatDistance(mActivity, item.distance));
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
                helper.setText(R.id.tv_master_integral, item.originalPrice / 10 + "");
                helper.setText(R.id.tv_master_consult_time, "积分/" + item.consultTime / 60 + "分钟");
                ((TextView) helper.getView(R.id.tv_master_integral)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                ((TextView) helper.getView(R.id.tv_master_consult_time)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                helper.setVisible(R.id.ll_item_home_isfree, true);
                helper.setVisible(R.id.ll_item_service_pice, false);
            } else {
                helper.setVisible(R.id.ll_item_home_isfree, false);
                helper.setVisible(R.id.ll_item_service_pice, true);
                helper.setText(R.id.item_home_service_price, item.price / 10 + "积分/" + item.consultTime / 60 + "分钟");

            }
        }

        helper.setGone(R.id.view_line, helper.getAdapterPosition() == 0 ? true : false);
    }
}
