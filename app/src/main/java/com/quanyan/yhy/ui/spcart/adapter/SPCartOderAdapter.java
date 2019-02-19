package com.quanyan.yhy.ui.spcart.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.yhy.common.beans.net.model.tm.OrderItem;
import com.yhy.common.beans.net.model.tm.Shop;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

import java.util.List;

/**
 * Created with Android Studio.
 * Title:SPCartOderAdapter
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-11-1
 * Time:13:44
 * Version 1.0
 * Description:
 */
public class SPCartOderAdapter extends BaseExpandableListAdapter {

    private LayoutInflater mLayoutInflater;
    protected Context mContext;
    private ExpandableListView expandableListView;
    private List<Shop> orderList;

    private SPCartOrderAdapterClick mSPCartOrderAdapterClick;
    private int[] editPosition;

    public SPCartOderAdapter(Activity activity, ExpandableListView mex, List<Shop> orderList) {
        this.mContext = activity;
        this.expandableListView = mex;
        this.orderList = orderList;
        mLayoutInflater = LayoutInflater.from(activity);
        editPosition = new int[]{-1, -1};
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        int groupCount = this.getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            expandableListView.expandGroup(i);
        }
    }


    public void replaceAll(List<Shop> elem) {
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

    public void addAll(List<Shop> list) {
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
    public int getGroupCount() {
        return orderList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return orderList.get(groupPosition).orderItemList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return orderList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return orderList.get(groupPosition).orderItemList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder = null;
        if (convertView == null) {
            groupViewHolder = new GroupViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.spcart_order_group_item, null);
            groupViewHolder.mGroupsCheckIv = (ImageView) convertView.findViewById(R.id.iv_group_check);
            groupViewHolder.mGroupsHead = (ImageView) convertView.findViewById(R.id.cell_group_head);
            groupViewHolder.mGroupTitle = (TextView) convertView.findViewById(R.id.tv_group_title);
            groupViewHolder.mCouponTv = (TextView) convertView.findViewById(R.id.tv_coupon);
            groupViewHolder.mArrowIv = (ImageView) convertView.findViewById(R.id.iv_arrow);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        Shop shopInfoResult = orderList.get(groupPosition);
        setDataToGroupView(shopInfoResult, groupViewHolder);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildGoodsHolder childGoodsHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.cell_sporder_item, null);
            childGoodsHolder = new ChildGoodsHolder();
            childGoodsHolder.mItemLayout = (LinearLayout) convertView.findViewById(R.id.ll_cell_item);
            childGoodsHolder.mGoodsIv = (ImageView) convertView.findViewById(R.id.sa_iv_good);
            childGoodsHolder.mGoodsStoreCount = (TextView) convertView.findViewById(R.id.tv_store_count);
            childGoodsHolder.mGoodsTitle = (TextView) convertView.findViewById(R.id.tv_goods_title);
            childGoodsHolder.mIntegral = (TextView) convertView.findViewById(R.id.tv_integral);
            childGoodsHolder.mGoodsPrice = (TextView) convertView.findViewById(R.id.tv_goods_price);
            childGoodsHolder.mBottomView = (LinearLayout) convertView.findViewById(R.id.ll_bottom_view);
            childGoodsHolder.mGoodsAllCountTv = (TextView) convertView.findViewById(R.id.tv_all_count_goods);
            childGoodsHolder.mGoodsAllPriceTv = (TextView) convertView.findViewById(R.id.tv_all_price_goods);
            childGoodsHolder.mGoodsAllNum = (TextView) convertView.findViewById(R.id.tv_goods_number);
            childGoodsHolder.mCouponLayout = (RelativeLayout) convertView.findViewById(R.id.rl_coupon_layout);
            childGoodsHolder.mCouponTextView = (TextView) convertView.findViewById(R.id.tv_coupon);
            childGoodsHolder.mLeaveMessage = (EditText) convertView.findViewById(R.id.scenic_othersrq_et);
            convertView.setTag(childGoodsHolder);
        } else {
            childGoodsHolder = (ChildGoodsHolder) convertView.getTag();
        }

        OrderItem itemInfoResult = orderList.get(groupPosition).orderItemList.get(childPosition);
        Shop shopInfoResult = orderList.get(groupPosition);

        if (childPosition == orderList.get(groupPosition).orderItemList.size() - 1) {
            childGoodsHolder.mBottomView.setVisibility(View.VISIBLE);
        } else {
            childGoodsHolder.mBottomView.setVisibility(View.GONE);
        }

        setDataToChildView(itemInfoResult, childGoodsHolder, shopInfoResult, groupPosition, childPosition);

        return convertView;
    }


    public class GroupViewHolder {
        ImageView mGroupsCheckIv;
        ImageView mGroupsHead;
        TextView mGroupTitle;
        TextView mCouponTv;
        ImageView mArrowIv;
    }

    public class ChildGoodsHolder {
        LinearLayout mItemLayout;
        ImageView mGoodsIv;
        TextView mGoodsStoreCount;
        TextView mGoodsTitle;
        TextView mIntegral;
        TextView mGoodsPrice;
        LinearLayout mBottomView;
        TextView mGoodsAllCountTv;
        TextView mGoodsAllPriceTv;
        TextView mGoodsAllNum;
        RelativeLayout mCouponLayout;
        TextView mCouponTextView;
        EditText mLeaveMessage;

    }


    private void setDataToGroupView(final Shop shopInfoResult, GroupViewHolder groupViewHolder) {
        if (shopInfoResult == null || groupViewHolder == null) {
            return;
        }

        groupViewHolder.mGroupsCheckIv.setVisibility(View.GONE);
        groupViewHolder.mCouponTv.setVisibility(View.GONE);
        groupViewHolder.mArrowIv.setVisibility(View.GONE);
        groupViewHolder.mGroupTitle.setText(TextUtils.isEmpty(shopInfoResult.sellerName) ? "" : shopInfoResult.sellerName);

//        BaseImgView.loadimg(groupViewHolder.mGroupsHead,
//                shopInfoResult.sellerLogo,
//                R.mipmap.ic_shop_default_logo,
//                R.mipmap.ic_shop_default_logo,
//                R.mipmap.ic_shop_default_logo,
//                ImageScaleType.EXACTLY,
//                75,
//                75,
//                180);

        ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(shopInfoResult.sellerLogo), R.mipmap.ic_shop_default_logo, 75, 75, groupViewHolder.mGroupsHead);


        groupViewHolder.mGroupsCheckIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    private void setDataToChildView(final OrderItem itemInfoResult, final ChildGoodsHolder childGoodsHolder, final Shop shopInfoResult, final int groupPostion, final int childPosition) {
        if (itemInfoResult == null || childGoodsHolder == null || shopInfoResult == null) {
            return;
        }
        childGoodsHolder.mGoodsAllCountTv.setText("共" + getAllCount(shopInfoResult) + "件商品");
        childGoodsHolder.mGoodsAllPriceTv.setText(StringUtil.converRMb2YunWithFlag(mContext, shopInfoResult.originalTotalFee));

        childGoodsHolder.mGoodsTitle.setText(TextUtils.isEmpty(itemInfoResult.itemTitle) ? "" : itemInfoResult.itemTitle);

//        BaseImgView.loadimg(childGoodsHolder.mGoodsIv,
//                itemInfoResult.picUrl,
//                R.mipmap.icon_default_150_150,
//                R.mipmap.icon_default_150_150,
//                R.mipmap.icon_default_150_150,
//                ImageScaleType.EXACTLY,
//                300,
//                300,
//                0);

        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(itemInfoResult.picUrl), R.mipmap.icon_default_150_150, 300, 300, childGoodsHolder.mGoodsIv);

        childGoodsHolder.mGoodsAllNum.setText("X" + itemInfoResult.buyAmount);
        childGoodsHolder.mGoodsPrice.setText(StringUtil.converRMb2YunNoFlag(itemInfoResult.price));

        childGoodsHolder.mCouponLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSPCartOrderAdapterClick != null) {
                    mSPCartOrderAdapterClick.gotoChooseCoupon(shopInfoResult.sellerId, shopInfoResult.originalTotalFee,
                            groupPostion, childGoodsHolder.mCouponTextView);
                }
            }
        });

        if (shopInfoResult.voucherResult != null) {
            if (shopInfoResult.voucherResult.value <= 0) {
                childGoodsHolder.mCouponTextView.setText("");
            } else {
                childGoodsHolder.mCouponTextView.setText("-" + StringUtil.converRMb2YunWithFlag(mContext, shopInfoResult.voucherResult.value));
            }
        } else {
            childGoodsHolder.mCouponTextView.setText("");
        }

        childGoodsHolder.mLeaveMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    index = position;
                    editPosition[0] = groupPostion;
                    editPosition[1] = childPosition;
                }
                return false;
            }
        });

        childGoodsHolder.mLeaveMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    shopInfoResult.mLeaveMessage = s.toString();
                }
            }
        });

        childGoodsHolder.mLeaveMessage.clearFocus();
        if (editPosition[0] != -1 && editPosition[1] != -1 && editPosition[0] == groupPostion && editPosition[1] == childPosition) {
            //强制加上焦点
            childGoodsHolder.mLeaveMessage.requestFocus();
            //设置光标显示到编辑框尾部
//            childGoodsHolder.mLeaveMessage.setSelection(childGoodsHolder.mLeaveMessage.getText().length());
            //重置
            editPosition[0] = -1;
            editPosition[1] = -1;
        }
    }

    private long getAllCount(Shop shopInfoResult) {
        if (shopInfoResult == null) {
            return 0;
        }

        if (shopInfoResult.orderItemList == null || shopInfoResult.orderItemList.size() == 0) {
            return 0;
        }

        long allBuyAmount = 0;
        for (int i = 0; i < shopInfoResult.orderItemList.size(); i++) {
            allBuyAmount += shopInfoResult.orderItemList.get(i).buyAmount;
        }
        return allBuyAmount;
    }


    public void setSPCartOrderAdapterClickListener(SPCartOrderAdapterClick mSPCartOrderAdapterClick) {
        this.mSPCartOrderAdapterClick = mSPCartOrderAdapterClick;
    }

    public interface SPCartOrderAdapterClick {
        void gotoChooseCoupon(long sellerId, long totalPrice, int groupPostion, TextView mCouponTextView);
    }

}
