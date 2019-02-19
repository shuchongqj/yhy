package com.quanyan.yhy.ui.spcart.adapter;

import android.app.Activity;
import android.content.Context;
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

import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.spcart.view.CartNumberChoose;
import com.yhy.common.beans.net.model.tm.ItemInfoResult;
import com.yhy.common.beans.net.model.tm.ShopInfoResult;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:SPCartAdapter
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-10-31
 * Time:10:37
 * Version 1.0
 * Description:
 */
public class SPCartAdapter extends BaseExpandableListAdapter {

    private LayoutInflater mLayoutInflater;
    protected Context mContext;
    private ExpandableListView expandableListView;
    private List<ShopInfoResult> orderList = new ArrayList<>();

    private SpcartAdapterClick mSpcartAdapterClick;

    public SPCartAdapter(Activity activity, ExpandableListView mex) {
        this.mContext = activity;
        this.expandableListView = mex;
        mLayoutInflater = LayoutInflater.from(activity);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        int groupCount = this.getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            expandableListView.expandGroup(i);
        }
    }


    public void replaceAll(List<ShopInfoResult> elem) {
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

    public void addAll(List<ShopInfoResult> list) {
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
        return orderList.get(groupPosition).itemInfoResultList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return orderList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return orderList.get(groupPosition).itemInfoResultList.get(childPosition);
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
            convertView = mLayoutInflater.inflate(R.layout.cell_spcart, null);
            groupViewHolder.mGroupsCheckIv = (ImageView) convertView.findViewById(R.id.iv_group_check);
            groupViewHolder.mGroupsHead = (ImageView) convertView.findViewById(R.id.cell_group_head);
            groupViewHolder.mGroupTitle = (TextView) convertView.findViewById(R.id.tv_group_title);
            groupViewHolder.mGroupCouponTV = (TextView) convertView.findViewById(R.id.tv_coupon);
            groupViewHolder.mGroupLayout = (LinearLayout) convertView.findViewById(R.id.ll_group);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        ShopInfoResult shopInfoResult = orderList.get(groupPosition);
        setDataToGroupView(shopInfoResult, groupViewHolder);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildGoodsHolder childGoodsHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.cell_spcart_item, null);
            childGoodsHolder = new ChildGoodsHolder();
            childGoodsHolder.mCheckStateIv = (ImageView) convertView.findViewById(R.id.iv_check_state);
            childGoodsHolder.mGoodsIv = (ImageView) convertView.findViewById(R.id.sa_iv_good);
            childGoodsHolder.mGoodsStoreCount = (TextView) convertView.findViewById(R.id.tv_store_count);
            childGoodsHolder.mGoodsTitle = (TextView) convertView.findViewById(R.id.tv_goods_title);
            childGoodsHolder.mIntegral = (TextView) convertView.findViewById(R.id.tv_integral);
            childGoodsHolder.mGoodsPrice = (TextView) convertView.findViewById(R.id.tv_goods_price);
            childGoodsHolder.mGoodsPriceSymbol = (TextView)convertView.findViewById(R.id.tv_goods_price_symbol);
            childGoodsHolder.mNumberChooseView = (CartNumberChoose) convertView.findViewById(R.id.nc_num_select);
            childGoodsHolder.mItemLayout = (LinearLayout) convertView.findViewById(R.id.ll_spcart_item);
            childGoodsHolder.mStoreCountLayout = (RelativeLayout) convertView.findViewById(R.id.rl_store_count);
            childGoodsHolder.mNoStoreLayout = (RelativeLayout) convertView.findViewById(R.id.rl_no_stock);
            childGoodsHolder.mNoStoreImage = (ImageView) convertView.findViewById(R.id.iv_no_stock);
            convertView.setTag(childGoodsHolder);
        } else {
            childGoodsHolder = (ChildGoodsHolder) convertView.getTag();
        }

        ItemInfoResult itemInfoResult = orderList.get(groupPosition).itemInfoResultList.get(childPosition);

        setDataToChildView(itemInfoResult, childGoodsHolder, orderList.get(groupPosition));

        return convertView;
    }


    public class GroupViewHolder {
        ImageView mGroupsCheckIv;
        ImageView mGroupsHead;
        TextView mGroupTitle;
        TextView mGroupCouponTV;
        LinearLayout mGroupLayout;
    }

    public class ChildGoodsHolder {
        ImageView mCheckStateIv;
        ImageView mGoodsIv;
        TextView mGoodsStoreCount;
        TextView mGoodsTitle;
        TextView mIntegral;
        TextView mGoodsPrice;
        TextView mGoodsPriceSymbol;
        CartNumberChoose mNumberChooseView;
        LinearLayout mItemLayout;
        RelativeLayout mStoreCountLayout;
        RelativeLayout mNoStoreLayout;
        ImageView mNoStoreImage;
    }


    private void setDataToGroupView(final ShopInfoResult shopInfoResult, GroupViewHolder groupViewHolder) {
        if (shopInfoResult == null || groupViewHolder == null) {
            return;
        }

        if (shopInfoResult.shopCheck) {
//            groupViewHolder.mGroupsCheckIv.setImageResource(R.mipmap.spcart_checked);
            groupViewHolder.mGroupsCheckIv.setImageResource(R.mipmap.ic_share_cb_checked);
        } else {
            groupViewHolder.mGroupsCheckIv.setImageResource(R.mipmap.spcart_unchecked);
        }

        if (!TextUtils.isEmpty(shopInfoResult.sellerInfo.sellerName)) {
            groupViewHolder.mGroupTitle.setText(shopInfoResult.sellerInfo.sellerName);
        } else {
            groupViewHolder.mGroupTitle.setText("");
        }

        if (shopInfoResult.sellerInfo != null) {
            groupViewHolder.mGroupCouponTV.setVisibility(shopInfoResult.sellerInfo.hasPromotion ? View.VISIBLE : View.GONE);
//            groupViewHolder.mGroupCouponTV.setVisibility(View.VISIBLE);
        } else {
            groupViewHolder.mGroupCouponTV.setVisibility(View.GONE);
        }

//        BaseImgView.loadimg(groupViewHolder.mGroupsHead,
//                shopInfoResult.sellerInfo.sellerPic,
//                R.mipmap.ic_shop_default_logo,
//                R.mipmap.ic_shop_default_logo,
//                R.mipmap.ic_shop_default_logo,
//                ImageScaleType.EXACTLY,
//                75,
//                75,
//                180);
        ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(shopInfoResult.sellerInfo.sellerPic), R.mipmap.ic_shop_default_logo, 75, 75, groupViewHolder.mGroupsHead);

        groupViewHolder.mGroupsCheckIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSpcartAdapterClick != null) {
                    mSpcartAdapterClick.groupChecked(shopInfoResult);
                }
            }
        });

        groupViewHolder.mGroupCouponTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSpcartAdapterClick != null) {
                    mSpcartAdapterClick.showCouponDialog(shopInfoResult);
                }
            }
        });

        // TODO: 2018/3/7    不跳转商家
//        groupViewHolder.mGroupLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NavUtils.gotoMustBuyShopHomePageActivity(mContext, shopInfoResult.sellerInfo.sellerName, shopInfoResult.sellerInfo.sellerId);
//            }
//        });

    }

    private void setDataToChildView(final ItemInfoResult itemInfoResult, final ChildGoodsHolder childGoodsHolder, final ShopInfoResult shopInfoResult) {
        if (itemInfoResult == null || childGoodsHolder == null) {
            return;
        }

        // 商品状态 1:已抢光 2：上架 3：下架 4：商家删除
        if (itemInfoResult.itemStatus == 1) {
            childGoodsHolder.mStoreCountLayout.setVisibility(View.GONE);
            childGoodsHolder.mNoStoreLayout.setVisibility(View.VISIBLE);
            childGoodsHolder.mNoStoreImage.setImageResource(R.mipmap.spcart_nostockeet_image);
            childGoodsHolder.mGoodsTitle.setTextColor(mContext.getResources().getColor(R.color.neu_999999));
            childGoodsHolder.mIntegral.setVisibility(View.GONE);
            childGoodsHolder.mGoodsPrice.setTextColor(mContext.getResources().getColor(R.color.neu_999999));
            childGoodsHolder.mGoodsPriceSymbol.setTextColor(mContext.getResources().getColor(R.color.neu_999999));
            childGoodsHolder.mNumberChooseView.setVisibility(View.GONE);
        } else if (itemInfoResult.itemStatus == 3 || itemInfoResult.itemStatus == 4) {
            childGoodsHolder.mStoreCountLayout.setVisibility(View.GONE);
            childGoodsHolder.mNoStoreLayout.setVisibility(View.VISIBLE);
            childGoodsHolder.mNoStoreImage.setImageResource(R.mipmap.spcart_soldout_image);
            childGoodsHolder.mGoodsTitle.setTextColor(mContext.getResources().getColor(R.color.neu_999999));
            childGoodsHolder.mGoodsPrice.setTextColor(mContext.getResources().getColor(R.color.neu_999999));
            childGoodsHolder.mGoodsPriceSymbol.setTextColor(mContext.getResources().getColor(R.color.neu_999999));
            childGoodsHolder.mIntegral.setVisibility(View.GONE);
            childGoodsHolder.mNumberChooseView.setVisibility(View.GONE);
        } else {
            childGoodsHolder.mNoStoreLayout.setVisibility(View.GONE);
            childGoodsHolder.mGoodsTitle.setTextColor(mContext.getResources().getColor(R.color.neu_333333));
            childGoodsHolder.mGoodsPrice.setTextColor(mContext.getResources().getColor(R.color.main));
            childGoodsHolder.mGoodsPriceSymbol.setTextColor(mContext.getResources().getColor(R.color.main));
            childGoodsHolder.mIntegral.setVisibility(View.VISIBLE);
            childGoodsHolder.mNumberChooseView.setVisibility(View.VISIBLE);
            if (itemInfoResult.stockNum > 0) {
                childGoodsHolder.mGoodsStoreCount.setText("仅剩" + itemInfoResult.stockNum + "件");
                if (itemInfoResult.stockNum < 6) {
                    childGoodsHolder.mStoreCountLayout.setVisibility(View.VISIBLE);
                } else {
                    childGoodsHolder.mStoreCountLayout.setVisibility(View.GONE);
                }
            } else {
                childGoodsHolder.mStoreCountLayout.setVisibility(View.GONE);
            }
        }

        if (itemInfoResult.itemCheck) {
//            childGoodsHolder.mCheckStateIv.setImageResource(R.mipmap.spcart_checked);
            childGoodsHolder.mCheckStateIv.setImageResource(R.mipmap.ic_share_cb_checked);
        } else {
            childGoodsHolder.mCheckStateIv.setImageResource(R.mipmap.spcart_unchecked);
        }

        if (!TextUtils.isEmpty(itemInfoResult.title)) {
            childGoodsHolder.mGoodsTitle.setText(itemInfoResult.title);
        } else {
            childGoodsHolder.mGoodsTitle.setText("");
        }

//        BaseImgView.loadimg(childGoodsHolder.mGoodsIv,
//                itemInfoResult.picUrls,
//                R.mipmap.icon_default_150_150,
//                R.mipmap.icon_default_150_150,
//                R.mipmap.icon_default_150_150,
//                ImageScaleType.EXACTLY,
//                300,
//                300,
//                0);
        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(itemInfoResult.picUrls), R.mipmap.icon_default_150_150, 300, 300, childGoodsHolder.mGoodsIv);

        if (itemInfoResult.pointTotalFee > 0) {
//            childGoodsHolder.mIntegral.setText("积分可抵" + StringUtil.converRMb2YunWithFlag(mContext, itemInfoResult.pointTotalFee));
            childGoodsHolder.mIntegral.setText("积分可抵" + StringUtil.pointToYuanOne(itemInfoResult.pointTotalFee*10));
        } else {

        }

//        childGoodsHolder.mGoodsPrice.setText(StringUtil.converRMb2YunNoFlag(itemInfoResult.price));
        childGoodsHolder.mGoodsPrice.setText(StringUtil.pointToYuan(itemInfoResult.price));

//        if (itemInfoResult.buyAmount >= 0) {
//            int a = ((int) itemInfoResult.buyAmount) >
//                    itemInfoResult.stockNum ? itemInfoResult.stockNum : (int) itemInfoResult.buyAmount;
//            int mixCount = a > 99 ? 99 : a;
//            childGoodsHolder.mNumberChooseView.initCheckValue(itemInfoResult.stockNum, 1, mixCount);
//        }

        if (itemInfoResult.buyAmount >= 0) {
            int max = itemInfoResult.stockNum > 99 ? 99 : itemInfoResult.stockNum;
            childGoodsHolder.mNumberChooseView.initCheckValue(max, 1, (int) itemInfoResult.buyAmount);
        }


        //选中状态监听
        childGoodsHolder.mCheckStateIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSpcartAdapterClick != null) {
                    mSpcartAdapterClick.childChecked(itemInfoResult, shopInfoResult);
                }
            }
        });

        childGoodsHolder.mNumberChooseView.setNumberChooseListener(new CartNumberChoose.NumberClickListener() {
            @Override
            public void onReduce(int num) {
                if (mSpcartAdapterClick != null) {
                    mSpcartAdapterClick.childReduce(itemInfoResult, num);
                }
            }

            @Override
            public void onIncrease(int num) {
                if (mSpcartAdapterClick != null) {
                    mSpcartAdapterClick.childIncrease(itemInfoResult, num);
                }
            }

            @Override
            public void textClick(int num) {
                if (itemInfoResult.stockNum > 0) {
                    if (mSpcartAdapterClick != null) {
                        mSpcartAdapterClick.textClick(itemInfoResult.stockNum, num, itemInfoResult.id);
                    }
                } else {

                }
            }
        });

        childGoodsHolder.mItemLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mSpcartAdapterClick != null) {
                    mSpcartAdapterClick.childDelete(itemInfoResult);
                }
                return false;
            }
        });

        childGoodsHolder.mItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.gotoProductDetail(mContext,
                        ItemType.POINT_MALL,
                        itemInfoResult.itemId,
                        itemInfoResult.title);
            }
        });
    }

    public void setAdapterClickListener(SpcartAdapterClick mSpcartAdapterClick) {
        this.mSpcartAdapterClick = mSpcartAdapterClick;
    }

    public interface SpcartAdapterClick {
        void groupChecked(ShopInfoResult shopInfoResult);

        void childChecked(ItemInfoResult itemInfoResult, ShopInfoResult shopInfoResult);

        void childReduce(ItemInfoResult itemInfoResult, int num);

        void childIncrease(ItemInfoResult itemInfoResult, int num);

        void childDelete(ItemInfoResult itemInfoResult);

        void showCouponDialog(ShopInfoResult shopInfoResult);

        void textClick(int maxNum, int currentNum, long id);
    }

}
