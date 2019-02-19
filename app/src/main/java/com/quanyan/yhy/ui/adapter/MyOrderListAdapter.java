package com.quanyan.yhy.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.harwkin.nb.camera.TimeUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.service.controller.OrderController;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.servicerelease.ExpertOrderListActivity;
import com.yhy.common.beans.net.model.tm.TmDetailOrder;
import com.yhy.common.beans.net.model.tm.TmMainOrder;
import com.yhy.common.constants.IntentConstants;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:***Acitivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:田海波
 * Date: 2016/8/19
 * Time: 17:25
 * Version 2.0
 */

public class MyOrderListAdapter extends BaseExpandableListAdapter implements View.OnClickListener {
    private LayoutInflater mLayoutInflater;
    protected Context mContext;
    private List<TmMainOrder> orderList = new ArrayList<>();
    ExpandableListView expandableListView;
    private OnOrderClickListener clickListener;

    @Autowired
    IUserService userService;

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        int groupCount = this.getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            expandableListView.expandGroup(i);
            //this.onGroupExpanded(i);
        }
    }

    public MyOrderListAdapter(Activity activity, ExpandableListView mex) {
        this.mContext = activity;
        this.expandableListView = mex;
        mLayoutInflater = LayoutInflater.from(activity);
        YhyRouter.getInstance().inject(this);

    }

    public void replaceAll(List<TmMainOrder> elem) {
        orderList.clear();
        orderList.addAll(elem);
        notifyDataSetChanged();
    }

    public void clear() {
        if (this.orderList != null) {
            this.orderList.clear();
            notifyDataSetChanged();
        }

    }

    public void addAll(List<TmMainOrder> list) {
        if (this.orderList.size() == 0) {
            this.orderList = list;
        } else {
            if (list != null)
                this.orderList.addAll(list);
        }
        notifyDataSetChanged();

    }


    public int getCount() {
        return orderList == null ? 0 : orderList.size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return orderList.get(groupPosition).detailOrders.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildTypeV2(int groupPosition, int childPosition) {

        int size = orderList.get(groupPosition).detailOrders.size();
        if (childPosition == size - 1) {
            return -1; // 最后一条
        } else {
            return 0;
        }
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        ChildGoodsHolder childGoodsHolder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_order_goods, null);
            childGoodsHolder = new ChildGoodsHolder();
            childGoodsHolder.tvOrderMessage = (TextView) convertView.findViewById(R.id.tv_order_message);
            childGoodsHolder.tvOrderCount = (TextView) convertView.findViewById(R.id.tv_order_count_content);
            childGoodsHolder.tvOrderPrice = (TextView) convertView.findViewById(R.id.tv_order_total_price_content);
            childGoodsHolder.tvLimit = (TextView) convertView.findViewById(R.id.tv_title_limit);
            childGoodsHolder.llLimit = (LinearLayout) convertView.findViewById(R.id.lin_limit);
            childGoodsHolder.llNumAndPrice = (LinearLayout) convertView.findViewById(R.id.lin_num_and_price);
            childGoodsHolder.llGoods = (LinearLayout) convertView.findViewById(R.id.ll_goods);
            childGoodsHolder.tvDeletePrice = (TextView) convertView.findViewById(R.id.tv_delete_price_double);
            childGoodsHolder.tvOrderTravel = (TextView) convertView.findViewById(R.id.tv_order_sku);
            childGoodsHolder.tvOrderNum = (TextView) convertView.findViewById(R.id.tv_order_count);
            childGoodsHolder.image = (ImageView) convertView.findViewById(R.id.image);
            childGoodsHolder.tvOrderTitle = (TextView) convertView.findViewById(R.id.tv_order_title);
            childGoodsHolder.vLine = (View) convertView.findViewById(R.id.v_line);
            childGoodsHolder.llGoodsItem = (LinearLayout) convertView.findViewById(R.id.ll_order_item);
            childGoodsHolder.mTotalInfo = (TextView) convertView.findViewById(R.id.item_order_list_total_price);
            childGoodsHolder.tvCancel = (TextView) convertView.findViewById(R.id.tv_cancle);
            childGoodsHolder.tvConfirm = (TextView) convertView.findViewById(R.id.tv_confirm);
            childGoodsHolder.tvPay = (TextView) convertView.findViewById(R.id.tv_pay);
            childGoodsHolder.tvLookLogistical = (TextView) convertView.findViewById(R.id.tv_logistical);
            childGoodsHolder.tvComments = (TextView) convertView.findViewById(R.id.tv_comments);
            childGoodsHolder.divider = convertView.findViewById(R.id.divider);
            childGoodsHolder.mTotalPriceLayout = (LinearLayout) convertView.findViewById(R.id.item_order_list_total_price_layout);
            childGoodsHolder.mTotalInfo = (TextView) convertView.findViewById(R.id.item_order_list_total_price);
            childGoodsHolder.mOperatell = (LinearLayout) convertView.findViewById(R.id.ll_operate);
            convertView.setTag(childGoodsHolder);
        } else {
            childGoodsHolder = (ChildGoodsHolder) convertView.getTag();
        }

        if (childPosition == 0) {
            childGoodsHolder.vLine.setVisibility(View.GONE);
        } else {
            childGoodsHolder.vLine.setVisibility(View.VISIBLE);
        }

        convertGoodsItem(orderList.get(groupPosition).detailOrders.get(childPosition), orderList.get(groupPosition), childGoodsHolder);
        childGoodsHolder.llGoodsItem.setTag(R.id.tag_goods, orderList.get(groupPosition));
        int i1 = getChildTypeV2(groupPosition, childPosition);
        if (i1 != -1) {
            childGoodsHolder.mOperatell.setVisibility(View.GONE);
        } else {
            // 最后一个item,评价 等操作 -1 最后一条
            childGoodsHolder.mOperatell.setVisibility(View.VISIBLE);
            convertGoodsOperator(childGoodsHolder, groupPosition);

        }

        return convertView;
    }

    private void setHolderOperateTag(ChildGoodsHolder holder, TmMainOrder bean) {
        holder.tvPay.setTag(R.id.tag_goods_operator, bean);
        holder.tvConfirm.setTag(R.id.tag_goods_operator, bean);
        holder.tvCancel.setTag(R.id.tag_goods_operator, bean);
        holder.tvComments.setTag(R.id.tag_goods_operator, bean);
        holder.tvLookLogistical.setTag(R.id.tag_goods_operator, bean);
        holder.mTotalPriceLayout.setTag(R.id.tag_goods_operator, bean);
        holder.mTotalInfo.setTag(R.id.tag_goods_operator, bean);
        initHolderListener(holder);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        TmMainOrder order = (TmMainOrder) v.getTag(R.id.tag_goods_operator);
        switch (id) {
            case R.id.tv_cancle:
                if (clickListener != null) {
                    clickListener.onOrderCancel(order.bizOrder.bizOrderId, order.bizOrder.orderType);
                }
                break;
            case R.id.tv_confirm:
                if (clickListener != null) {
                    clickListener.onOrderConfirm(order.bizOrder.bizOrderId);
                }
                break;
            case R.id.tv_pay:
                if (TimeUtil.isFastDoubleClick()) {
                    return;
                }
                if (!userService.isLogin()) {
                    NavUtils.gotoLoginActivity(mContext);
                    return;
                }
                NavUtils.gotoPayActivity(mContext,
                        new long[]{order.bizOrder.bizOrderId},
                        String.valueOf(order.bizOrder.actualTotalFee),
                        ExpertOrderListActivity.REQ_CODE_REFRESH_ORDER_LIST);
                break;
            case R.id.tv_comments:
                if (order.bizOrder.orderType.equals(ValueConstants.ORDER_TYPE_EXPERT_ACTIVITY)) {
                    NavUtils.gotoOrderCommentNewActivity((Activity) mContext, order.bizOrder.bizOrderId, userService.getLoginUserId(), order.bizOrder.orderType, IntentConstants.REQUEST_CODE_COMMENT);
                } else if (ValueConstants.ORDER_TYPE_NORMAL.equals(order.bizOrder.orderType) || order.bizOrder.orderType.equals(ValueConstants.ORDER_TYPE_POINT)) {
                    NavUtils.gotoGoodsCommentListActivity((Activity) mContext, order.bizOrder.bizOrderId, userService.getLoginUserId(), OrderController.getOrderTypeStringForComment(order.bizOrder.orderType), IntentConstants.REQUEST_CODE_COMMENT);
                } else {
                    NavUtils.gotoWriteCommentAcitivty((Activity) mContext, order.bizOrder.bizOrderId, userService.getLoginUserId(), OrderController.getOrderTypeStringForComment(order.bizOrder.orderType), IntentConstants.REQUEST_CODE_COMMENT);
                }
                break;
            case R.id.tv_logistical:
                NavUtils.gotoLogisticsActivity(mContext, order.bizOrder.bizOrderId);
                //  NavUtils.gotoLogisticsActivity( mContext, 102632403);
                break;
        }
    }

    public void initHolderListener(final ChildGoodsHolder viewHolder) {
        viewHolder.tvPay.setOnClickListener(this);
        viewHolder.tvConfirm.setOnClickListener(this);
        viewHolder.tvCancel.setOnClickListener(this);
        viewHolder.tvComments.setOnClickListener(this);
        viewHolder.tvLookLogistical.setOnClickListener(this);
    }

    // 影院场次
    public class ChildGoodsHolder {
        public ImageView image;
        public LinearLayout llLimit, llNumAndPrice, llGoods, llGoodsItem;
        public TextView tvOrderTitle, tvOrderMessage, tvOrderCount, tvOrderPrice, tvLimit, tvDeletePrice, tvOrderTravel, tvOrderNum, tvLookLogistical;
        public View vLine;
        public TextView tvCancel, tvConfirm, tvPay, tvComments;
        public View divider;
        public LinearLayout mTotalPriceLayout, mOperatell;
        public TextView mTotalInfo;
    }


    public void convertGoodsOperator(ChildGoodsHolder holder, int groupPosition) {
        setHolderOperateTag(holder, orderList.get(groupPosition));
        final TmMainOrder bean = orderList.get(groupPosition);
        String orderType = bean.bizOrder.orderType;
        holder.tvCancel.setVisibility(View.GONE);
        holder.tvConfirm.setVisibility(bean.buttonStatus.buyerConfirmOrder ? View.VISIBLE : View.GONE);
        holder.tvPay.setVisibility(bean.buttonStatus.payButton ? View.VISIBLE : View.GONE);
        holder.tvComments.setVisibility(OrderController.isGoodsRate(bean) ? View.VISIBLE : View.GONE);

        if (orderType.equals(ValueConstants.ORDER_TYPE_EXPERT_ACTIVITY)) {//达人
            holder.mTotalPriceLayout.setVisibility(View.GONE);
        } else {
            holder.mTotalPriceLayout.setVisibility(View.VISIBLE);
            StringBuilder stringBuilder = new StringBuilder();
            if (bean.bizOrder != null) {
                stringBuilder.append("共")
                        .append(bean.bizOrder.buyAmount + "")
                        .append("件商品  ");
            }
            stringBuilder.append("合计:  ").append(StringUtil.converRMb2YunWithFlag(mContext, bean.totalFee));
            holder.mTotalInfo.setText(stringBuilder.toString());
            // getFormatContent(holder.mTotalInfo, stringBuilder.toString(), StringUtil.converRMb2YunWithFlag(mContext, bean.totalFee));
        }
    }

    //填充商品数据
    private void convertGoodsItem(TmDetailOrder item, TmMainOrder mainOrder, ChildGoodsHolder holder) {
        String orderType = item.bizOrder.orderType;
        holder.llGoodsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TmMainOrder order = (TmMainOrder) view.getTag(R.id.tag_goods);
                if (ValueConstants.ORDER_TYPE_EXPERT_ACTIVITY.equals(order.bizOrder.orderType)) {
                    NavUtils.gotoExpertOrderDetailActivityForResult((Activity) mContext, 0, Long.parseLong(order.bizOrder.outerId));
                } else {
                    NavUtils.gotoOrderDetailsActivity(mContext, order.bizOrder.orderType, order.bizOrder.bizOrderId);
                }
            }
        });
//        BaseImgView.loadimg(holder.image,
//                item.itemPic,
//                R.mipmap.icon_default_150_150,
//                R.mipmap.icon_default_150_150,
//                R.mipmap.icon_default_150_150,
//                ImageScaleType.EXACTLY,
//                300,
//                300,
//                0);
        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(item.itemPic), R.mipmap.icon_default_150_150, 300, 300, holder.image);
        holder.tvOrderMessage.setVisibility(View.GONE);
        holder.tvOrderNum.setVisibility(View.GONE);
        holder.tvOrderCount.setVisibility(View.GONE);
        holder.tvOrderPrice.setVisibility(View.GONE);
        holder.tvOrderTravel.setVisibility(View.GONE);
        holder.tvLookLogistical.setVisibility(View.GONE);
        holder.llLimit.setVisibility(View.GONE);
        holder.tvDeletePrice.setVisibility(View.GONE);

        if (orderType.equals(ValueConstants.ORDER_TYPE_CITY_ACTIVITY) || orderType.equals(ValueConstants.ORDER_TYPE_ACTIVITY)) {
            holder.tvOrderTitle.setText(item.itemTitle);
            holder.tvOrderMessage.setVisibility(TextUtils.isEmpty(item.activityTime) ? View.GONE : View.VISIBLE);
            holder.tvOrderNum.setVisibility(View.VISIBLE);
            holder.tvOrderCount.setVisibility(View.VISIBLE);
            holder.tvOrderCount.setText("" + item.bizOrder.buyAmount);
            holder.tvOrderTravel.setVisibility(View.VISIBLE);
            holder.tvOrderTravel.setText(mContext.getString(R.string.package_type) + ": " + item.activityContent);
            holder.tvOrderMessage.setText(mContext.getString(R.string.depart_time) + ": " + item.activityTime);

        } else if (orderType.equals(ValueConstants.ORDER_TYPE_SPOTS)) {
            holder.tvOrderMessage.setVisibility(View.VISIBLE);
            holder.tvOrderTitle.setText(mainOrder.scenicTitle);
            holder.tvOrderMessage.setText(item.itemTitle);
        } else if (orderType.equals(ValueConstants.ORDER_TYPE_HOTEL) || orderType.equals(ValueConstants.ORDER_TYPE_HOTEL_OFFLINE)) {

            holder.tvOrderTitle.setText(mainOrder.hotelTitle);
            holder.tvOrderCount.setVisibility(View.VISIBLE);
            holder.tvOrderCount.setText(getMonthSpace(getTimeString_(mainOrder.checkInTime), getTimeString_(mainOrder.checkOutTime)) + "晚/" + mainOrder.roomAmount + "间");
            holder.tvOrderTravel.setVisibility(View.VISIBLE);
            holder.tvOrderTravel.setText(mContext.getString(R.string.house_type) + mainOrder.roomTitle);
            holder.tvOrderMessage.setVisibility(View.VISIBLE);
            holder.tvOrderMessage.setText(mContext.getString(R.string.check_in_date) + ":" + getTimeString(mainOrder.checkInTime) + "至" + getTimeString_(mainOrder.checkOutTime));
        } else if (orderType.equals(ValueConstants.ORDER_TYPE_EXPERT_ACTIVITY)) {
            holder.tvOrderMessage.setVisibility(View.VISIBLE);
            holder.tvOrderTitle.setText(item.itemTitle);
            holder.tvOrderMessage.setText(item.serveTime / 60 + " 分钟");
            holder.llLimit.setVisibility(View.VISIBLE);
            if (mainOrder.usePoint == 0) {
                holder.tvLimit.setText("限免");
            } else {
                holder.tvLimit.setText(mainOrder.usePoint + "积分");
            }
            if (mainOrder.detailOrders != null && mainOrder.detailOrders.size() > 0) {
                holder.tvDeletePrice.setVisibility(View.VISIBLE);
                holder.tvDeletePrice.setText(mainOrder.detailOrders.get(0).itemOriginalPrice + "积分");
            }
            holder.tvDeletePrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            if (mainOrder.detailOrders == null || mainOrder.detailOrders.get(0) == null) {
                return;
            }
        } else if (ValueConstants.ORDER_TYPE_NORMAL.equals(orderType) || ValueConstants.ORDER_TYPE_POINT.equals(orderType)) {
            holder.tvOrderTitle.setText(item.itemTitle);
            holder.tvOrderMessage.setVisibility(TextUtils.isEmpty(item.itemSubTitle) ? View.GONE : View.VISIBLE);

            holder.tvOrderMessage.setText(item.itemSubTitle);
            holder.tvOrderNum.setVisibility(View.VISIBLE);

            holder.tvOrderCount.setVisibility(View.VISIBLE); // 数量
            holder.tvOrderCount.setText("" + item.bizOrder.buyAmount);

            holder.tvOrderPrice.setVisibility(View.VISIBLE);
            holder.tvOrderPrice.setText(StringUtil.converRMb2YunWithFlag(mContext, item.itemPrice));

            holder.tvLookLogistical.setVisibility(OrderController.getLogistState(mainOrder) ? View.VISIBLE : View.GONE);
/*

            // 已收货， 已完成
            if (ValueConstants.ORDER_STATUS_FINISH.equals(orderStatus) || ValueConstants.ORDER_STATUS_RATED.equals(orderStatus) || ValueConstants.ORDER_STATUS_NOT_RATE.equals(orderStatus)) {
                holder.tvLookLogistical.setVisibility(View.VISIBLE);
                //已发货
            } else if (ValueConstants.ORDER_STATUS_SHIPPING.equals(orderStatus)) {
                holder.tvLookLogistical.setVisibility(View.VISIBLE);
            } else {
                holder.tvLookLogistical.setVisibility(View.GONE);
            }

*/

            // 线路
        } else if (orderType.equals(ValueConstants.ORDER_TYPE_TOUR_LINE)
                || orderType.equals(ValueConstants.ORDER_TYPE_FREE_LINE)
                || orderType.equals(ValueConstants.ORDER_TYPE_TOUR_LINE_ABOARD)
                || orderType.equals(ValueConstants.ORDER_TYPE_FREE_LINE_ABOARD)
                || orderType.equals(ValueConstants.ORDER_TYPE_TRAVEL)) {
            holder.tvOrderTravel.setVisibility(View.VISIBLE);
            holder.tvOrderTravel.setText(mContext.getString(R.string.package_type) + ": " + item.packageType);
            holder.tvOrderMessage.setVisibility(TextUtils.isEmpty(item.itemSubTitle) ? View.GONE : View.VISIBLE);
            holder.tvOrderMessage.setText(mContext.getString(R.string.depart_time) + ": " + getTimeString(mainOrder.departTime));
            holder.tvOrderTitle.setText(item.itemTitle);
            holder.tvOrderNum.setVisibility(View.VISIBLE);
            holder.tvOrderCount.setVisibility(View.VISIBLE);
            holder.tvOrderCount.setText("" + getBizOrderButAmount(mainOrder));
            // holder.tvOrderMessage.setText(item.itemSubTitle);
        } /*else {
            holder.llLimit.setVisibility(View.GONE);
            holder.tvOrderCount.setText("" + mainOrder.bizOrder.buyAmount);
            holder.tvLookLogistical.setVisibility(View.GONE);
            holder.tvOrderPrice.setText("");
            holder.tvOrderNum.setVisibility(View.VISIBLE);
            holder.tvOrderTravel.setVisibility(View.GONE);
        }*/

    }

    /**
     * 获取子订单购买的数量
     *
     * @param tmMainOrder
     * @return
     */
    private int getBizOrderButAmount(TmMainOrder tmMainOrder) {
        if (tmMainOrder == null || tmMainOrder.detailOrders == null || tmMainOrder.detailOrders.size() == 0) {
            return 0;
        }
        long mount = 0;
        for (TmDetailOrder detailOrder : tmMainOrder.detailOrders) {
            mount = mount + detailOrder.bizOrder.buyAmount;
        }
        return (int) mount;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return orderList.get(groupPosition).detailOrders.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return orderList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return orderList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            groupViewHolder = new GroupViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_order_list, null);
            groupViewHolder.tvOrderStatus = (TextView) convertView.findViewById(R.id.tv_order_status);
            groupViewHolder.tvMerchant = (TextView) convertView.findViewById(R.id.tv_merchant);
            groupViewHolder.merchant = (ImageView) convertView.findViewById(R.id.image_merchant);
            groupViewHolder.rlMerchantLayout = (RelativeLayout) convertView.findViewById(R.id.ll_merchant_info);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        final TmMainOrder bean = orderList.get(groupPosition);
        groupViewHolder.rlMerchantLayout.setTag(bean);
        String orderType = bean.bizOrder.orderType;
        List<TmDetailOrder> detailOrderList = bean.detailOrders;
      /*  if (detailOrderList != null && detailOrderList.size() > 0) { // 有子订单
            if (ValueConstants.ORDER_STATUS_WAITING_DELIVERY.equals(bean.bizOrder.orderStatus)) { //主订单已付款
                groupViewHolder.tvOrderStatus.setText(OrderController.getOrderStatus(detailOrderList, mContext));
            } else {
                groupViewHolder.tvOrderStatus.setText(OrderController.getOrderStatusString(bean.bizOrder.orderStatus, bean.bizOrder.orderType, mContext));
            }
        } else {// 无子订单
            groupViewHolder.tvOrderStatus.setText(OrderController.getOrderStatusString(bean.bizOrder.orderStatus, bean.bizOrder.orderType, mContext));
        }*/
        if (ValueConstants.ORDER_TYPE_NORMAL.equals(bean.bizOrder.orderType) || bean.bizOrder.orderType.equals(ValueConstants.ORDER_TYPE_POINT)) {
            if (ValueConstants.ORDER_STATUS_WAITING_DELIVERY.equals(bean.bizOrder.orderStatus)) { //主订单已付款
                groupViewHolder.tvOrderStatus.setText(OrderController.getOrderStatus(detailOrderList, mContext));
            } else {
                groupViewHolder.tvOrderStatus.setText(OrderController.getOrderStatusString(bean.bizOrder.orderStatus, bean.bizOrder.orderType, mContext));
            }
        } else {
            groupViewHolder.tvOrderStatus.setText(OrderController.getOrderStatusString(bean.bizOrder.orderStatus, bean.bizOrder.orderType, mContext));
        }


        if (bean.merchantInfo != null) {
            String headIconUrl = bean.merchantInfo.logo;
            if (StringUtil.isEmpty(headIconUrl)) {
                if (bean.merchantInfo.userInfo != null) {
                    headIconUrl = bean.merchantInfo.userInfo.imgUrl;
                }
            }
            if (orderType.equals(ValueConstants.ORDER_TYPE_EXPERT_ACTIVITY)) {
//                BaseImgView.loadimg(groupViewHolder.merchant,
//                        headIconUrl,
//                        R.mipmap.icon_default_avatar,
//                        R.mipmap.icon_default_avatar,
//                        R.mipmap.icon_default_avatar,
//                        ImageScaleType.EXACTLY,
//                        75,
//                        75,
//                        180);
                ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(headIconUrl), R.mipmap.icon_default_avatar, 75, 75, groupViewHolder.merchant);

                groupViewHolder.tvMerchant.setText(bean.merchantInfo.userInfo.nick);


            } else {
//                BaseImgView.loadimg(groupViewHolder.merchant,
//                        headIconUrl,
//                        R.mipmap.ic_shop_default_logo,
//                        R.mipmap.ic_shop_default_logo,
//                        R.mipmap.ic_shop_default_logo,
//                        ImageScaleType.EXACTLY,
//                        75,
//                        75,
//                        180);
                ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(headIconUrl), R.mipmap.ic_shop_default_logo, 75, 75, groupViewHolder.merchant);

                groupViewHolder.tvMerchant.setText(bean.merchantInfo.name);
            }
        } else {
            if (orderType.equals(ValueConstants.ORDER_TYPE_EXPERT_ACTIVITY)) {
                groupViewHolder.merchant.setImageResource(R.mipmap.icon_default_avatar);
            } else {
                groupViewHolder.merchant.setImageResource(R.mipmap.ic_shop_default_logo);
            }
        }

        groupViewHolder.rlMerchantLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TmMainOrder order = (TmMainOrder) view.getTag();
                NavUtils.gotoPersonalPage(mContext,
                        order.merchantInfo.userInfo.userId,
                        order.merchantInfo.userInfo.name,
                        order.merchantInfo.userInfo.options);
            }
        });
        return convertView;
    }

    class GroupViewHolder {
        TextView tvOrderStatus, tvMerchant;
        RelativeLayout rlMerchantLayout;
        ImageView merchant;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setOnOrderClickListener(OnOrderClickListener onOrderClickListener) {
        this.clickListener = onOrderClickListener;
    }

    /**
     * listview item按钮点击监听
     */
    public interface OnOrderClickListener {
        void onOrderCancel(long orderId, String orderType);

        void onOrderConfirm(long orderId);
    }

    public static int getMonthSpace(String date1, String date2) {

        int result = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        try {
            c1.setTime(sdf.parse(date1));
            c2.setTime(sdf.parse(date2));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;

        result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);

        return result == 0 ? 1 : Math.abs(result);

    }

    public String getTimeString(long timeMillons) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date(timeMillons));
    }

    public String getTimeString_(long timeMillons) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
        return dateFormat.format(new Date(timeMillons));
    }

}