package com.quanyan.yhy.ui.servicerelease.helper;

import android.app.Activity;
import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;

import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.CommentType;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.servicerelease.ManageServiceInfoAcitvity;
import com.yhy.common.beans.net.model.tm.ItemManagement;
import com.yhy.common.beans.net.model.tm.ProcessOrder;
import com.yhy.common.constants.ValueConstants;

/**
 * Created with Android Studio.
 * Title:***Acitivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:王东旭
 * Date:2016/6/1
 * Time:16:46
 * Version 2.0
 */
public class OrderListHelper {
    public interface OnItemEditListener {
        public void delete(ItemManagement mItemManagement);

        public void edit(ItemManagement mItemManagement);

        public void onLine(ItemManagement mItemManagement);

        public void offLine(ItemManagement mItemManagement);
    }

    /**
     * 绑定出售订单列表的视图
     * @param context
     * @param helper
     * @param item
     * @param mOrderType
     * @param type
     */
    public static void handleOrderListSellerItem(final Activity context, BaseAdapterHelper helper, final ProcessOrder item, final String mOrderType, int type) {
        if (item.buyerInfo != null) {
            helper.setImageUrlRound(R.id.iv_expert_pic, item.buyerInfo.imgUrl, 128, 128, R.mipmap.icon_default_avatar);
            helper.setText(R.id.tv_goods_name, item.buyerInfo.nick);
            helper.setTag(R.id.lin_shop_name, item.buyerInfo);
            helper.setOnClickListener(R.id.lin_shop_name, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavUtils.gotoMasterHomepage(context, item.buyerInfo.userId);
                }
            });
        }

        helper.setImageUrl(R.id.iv_detail_goods_img, item.itemPic, 300, 300, R.mipmap.icon_default_150_150);
        helper.setText(R.id.tv_goods_describe, item.itemTitle);

        helper.setText(R.id.tv_detail_service_time,""+ item.serveTime/60+"分钟");
        if(item.usePointNum == 0){
            helper.setText(R.id.tv_service_free_price,context.getString(R.string.label_free_now));
        }else{
            helper.setText(R.id.tv_service_free_price,item.usePointNum/10 + "积分");
        }
        helper.setText(R.id.tv_detail_goods_price,item.totalFee/10 + "积分");
        ((TextView)helper.getView(R.id.tv_detail_goods_price)).setTextColor(context.getResources().getColor(R.color.light_gray));
        ((TextView)helper.getView(R.id.tv_detail_goods_price)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

//        helper.setText(R.id.tv_detail_service_area, String.format(context.getString(R.string.label_consulting_service_area), StringUtil.getServiceArea(item.itemDestination)));
        if (item.processOrderStatus.equals(ValueConstants.ORDER_STATUS_RATED)) {
            helper.getView(R.id.item_config_button).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.item_config_button).setVisibility(View.GONE);
        }
        helper.setText(R.id.tv_item_order_state_name, getOrderStateName(item.processOrderStatus));
        helper.setOnClickListener(R.id.item_config_button, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.gotoCommentFragmentActivity(context, item.itemId, CommentType.CONSULT);
            }
        });
    }

    /**
     * 获取订单状态
     * @param state
     * @return
     */
    public static String getOrderStateName(String state) {
        switch (state) {
            case ValueConstants.ORDER_STATUS_WAITING_PAY:
                return "待付款";
            case ValueConstants.ORDER_STATUS_CONSULT_IN_QUEUE:
                return "待确认";
            case ValueConstants.ORDER_STATUS_CONSULT_IN_CHAT:
                return "已确认";
            case ValueConstants.ORDER_STATUS_FINISH:
                return "已完成";
            case ValueConstants.ORDER_STATUS_RATED:
                return "已评价";
            default:
                return "已取消";

        }
    }

    /**
     * 服务管理列表
     *
     * @param context
     * @param helper
     * @param item
     * @param mOnItemEditListener
     * @param type
     */
    public static void handleManageItem(final Activity context, BaseAdapterHelper helper, final ItemManagement item, final OnItemEditListener mOnItemEditListener, int type) {
        if(ManageServiceInfoAcitvity.STATE_MASTER_SERVICEMANAGE_PUBLISH_OVER == type){
            helper.setVisible(R.id.item_manager_service_state_layout, false);
            helper.setVisible(R.id.tv_off_line, true);
        }else{
            helper.setVisible(R.id.item_manager_service_state_layout, true);
            helper.setVisible(R.id.tv_off_line, false);
        }

        helper.setImageUrl(R.id.iv_service, item.publishServiceDO.avater, 300, 300, R.mipmap.icon_default_215_215);
        helper.setText(R.id.tv_service_content, "" + item.publishServiceDO.title);

        if (item.publishServiceDO.discountPrice == 0) {
            helper.setText(R.id.tv_service_time, "限免");
        } else {
            helper.setText(R.id.tv_service_time, "" + item.publishServiceDO.discountPrice/10 + "积分/" + item.publishServiceDO.discountTime + "分钟");
        }

        if (item.publishServiceDO != null && item.publishServiceDO.serviceAreas != null) {
            helper.setText(R.id.tv_service_area, String.format(context.getString(R.string.label_consulting_service_area),StringUtil.getConServiceArea(item.publishServiceDO.serviceAreas)));
        }else{
            helper.setText(R.id.tv_service_area, context.getString(R.string.label_default_consulting_service_area));
        }
        helper.setText(R.id.tv_num, "" + item.saleVolume);

        helper.setOnClickListener(R.id.tv_off_line, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemEditListener.offLine(item);
            }
        });
        helper.setOnClickListener(R.id.btn_delete_layout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemEditListener.delete(item);
            }
        });
        helper.setOnClickListener(R.id.tv_edit_layout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemEditListener.edit(item);
            }
        });
        helper.setOnClickListener(R.id.tv_on_line_layout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemEditListener.onLine(item);
            }
        });
    }

}
