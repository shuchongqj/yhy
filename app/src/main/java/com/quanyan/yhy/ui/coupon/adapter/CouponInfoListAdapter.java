package com.quanyan.yhy.ui.coupon.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.CouponStatus;
import com.quanyan.yhy.util.TimeUtil;
import com.yhy.common.beans.net.model.tm.VoucherResult;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

import java.util.ArrayList;
import java.util.List;

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
public class CouponInfoListAdapter extends BaseAdapter implements View.OnClickListener {

    Context mContext;
    List<VoucherResult> mList;
    private int type;

    public CouponInfoListAdapter(Context mContext, List<VoucherResult> mList, int type) {
        this.mContext = mContext;
        this.mList = mList;
        this.type = type;
    }

    public void setData(List<VoucherResult> mList, boolean isPullRefresh) {
        if (this.mList == null) {
            this.mList = mList;
        } else {
            if (this.mList == null) {
                this.mList = new ArrayList<>();
            }
            if (isPullRefresh) {
                this.mList.clear();
                this.mList.addAll(mList);
            } else {
                this.mList.addAll(mList);
            }

        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mList != null) {
            return mList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = createNewView(0);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        bindView(holder, position);
        return convertView;
    }

    private void bindView(ViewHolder holder, int position) {
        if (mList == null || mList.get(position) == null) {
            return;
        }
        if (mList.get(position).sellerResult != null && !TextUtils.isEmpty(mList.get(position).sellerResult.logo)) {
//            BaseImgView.frescoLoadimg(holder.iv_logo,
//                    mList.get(position).sellerResult.logo,
//                    R.mipmap.ic_shop_default_logo,
//                    R.mipmap.ic_shop_default_logo,
//                    R.mipmap.ic_shop_default_logo,
//                    null,
//                    75,
//                    75,
//                    180);
            ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(mList.get(position).sellerResult.logo), R.mipmap.ic_shop_default_logo, 75, 75, holder.iv_logo);

        } else {
            holder.iv_logo.setImageResource(R.mipmap.ic_shop_default_logo);
        }
        if (!TextUtils.isEmpty(mList.get(position).title)) {
            holder.tv_title.setText(mList.get(position).title);
        }
        if (mList.get(position).sellerResult != null && !TextUtils.isEmpty(mList.get(position).sellerResult.merchantName)) {
            holder.tv_merchant_name.setText("" + mList.get(position).sellerResult.merchantName);
        }
        //holder.tv_value.setText(""+ StringUtil.formatCouponPrice(mList.get(position).value));
        holder.tv_value.setText("" + StringUtil.converRMb2YunNoFlag(mList.get(position).value));
        holder.tv_use_requirement.setText("满" + StringUtil.converRMb2YunNoFlag(mList.get(position).requirement) + "元使用");
        if (mList.get(position).startTime != 0 && mList.get(position).endTime != 0) {
            holder.tv_validate_date.setText(TimeUtil.convertLong2String(mList.get(position).startTime, "yyyy-MM-dd") + " 至 " + TimeUtil.convertLong2String(mList.get(position).endTime, "yyyy-MM-dd"));
        }
        setCouponState(holder, position);

    }

    public void setCouponState(ViewHolder holder, int position) {
        if (CouponStatus.ACTIVE.equals(mList.get(position).status)) {//未使用
            holder.tv_value.setTextColor(mContext.getResources().getColor(R.color.tv_item_bottom_bg));
            holder.tv_rmb_unit.setTextColor(mContext.getResources().getColor(R.color.tv_item_bottom_bg));
            holder.lin_item_bg.setBackgroundResource(R.drawable.bg_coupon_item_white);
            holder.iv_split_line.setBackgroundResource(R.drawable.item_line_check_orange);
            holder.iv_coupon_status.setVisibility(View.GONE);
            if (mList.get(position).tag != null && CouponStatus.SOON_DATADUE.equals(mList.get(position).tag)) {
                //holder. tv_coupon_right_out_due.setVisibility(View.VISIBLE);
                if (type == 0) {
                    holder.iv_coupon_status.setVisibility(View.VISIBLE);
                    holder.iv_coupon_status.setImageResource(R.mipmap.coupon_right_out_time);
                }
            }
        } else if (CouponStatus.USED.equals(mList.get(position).status)) {//已使用
            holder.tv_value.setTextColor(mContext.getResources().getColor(R.color.coupon_company_name));
            holder.tv_rmb_unit.setTextColor(mContext.getResources().getColor(R.color.coupon_company_name));
            holder.lin_item_bg.setBackgroundResource(R.drawable.bg_coupon_item_grey);
            holder.iv_split_line.setBackgroundResource(R.drawable.item_line_check_grey);
            holder.iv_coupon_status.setImageResource(R.mipmap.coupon_already_use);
        } else if (CouponStatus.OVERDUE.equals(mList.get(position).status)) {//已过期
            holder.tv_value.setTextColor(mContext.getResources().getColor(R.color.coupon_company_name));
            holder.tv_rmb_unit.setTextColor(mContext.getResources().getColor(R.color.coupon_company_name));
            holder.lin_item_bg.setBackgroundResource(R.drawable.bg_coupon_item_grey);
            holder.iv_split_line.setBackgroundResource(R.drawable.item_line_check_grey);
            holder.iv_coupon_status.setImageResource(R.mipmap.coupon_out_time);
        } else if (CouponStatus.CAN_NOT_USE.equals(mList.get(position).status)) {//不可用
            holder.tv_value.setTextColor(mContext.getResources().getColor(R.color.coupon_company_name));
            holder.tv_rmb_unit.setTextColor(mContext.getResources().getColor(R.color.coupon_company_name));
            holder.lin_item_bg.setBackgroundResource(R.drawable.bg_coupon_item_grey);
            holder.iv_split_line.setBackgroundResource(R.drawable.item_line_check_grey);
            holder.iv_coupon_status.setImageResource(0);
        }
    }

    /**
     * 创建新的itemView
     */
    private View createNewView(int couponType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_coupon_list, null);
        v.setTag(ViewHolder.getHolder(v, couponType));
        return v;
    }

    static class ViewHolder {
        public ImageView iv_logo;//商家logo
        public TextView tv_merchant_name;//商家名称
        public TextView tv_title;//券名称
        public TextView tv_value;//价格
        public TextView tv_use_requirement;//使用要求
        public TextView tv_validate_date; //日期
        public TextView tv_rmb_unit;// 元  单位
        public TextView tv_coupon_right_out_due;//即将过期

        public LinearLayout lin_item_bg;
        public ImageView iv_split_line;
        public ImageView iv_coupon_status;

        public ViewHolder(View convertView) {
            iv_logo = (ImageView) convertView.findViewById(R.id.iv_logo);
            tv_merchant_name = (TextView) convertView.findViewById(R.id.tv_merchant_name);
            tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            tv_value = (TextView) convertView.findViewById(R.id.tv_value);
            tv_use_requirement = (TextView) convertView.findViewById(R.id.tv_use_requirement);
            tv_validate_date = (TextView) convertView.findViewById(R.id.tv_validate_date);
            tv_rmb_unit = (TextView) convertView.findViewById(R.id.tv_rmb_unit);

            lin_item_bg = (LinearLayout) convertView.findViewById(R.id.lin_item_bg);
            iv_split_line = (ImageView) convertView.findViewById(R.id.iv_split_line);
            iv_coupon_status = (ImageView) convertView.findViewById(R.id.iv_coupon_status);
            tv_coupon_right_out_due = (TextView) convertView.findViewById(R.id.tv_coupon_right_out_due);
        }

        public static ViewHolder getHolder(View convertView, int couponType) {
            ViewHolder mViewHolder = (ViewHolder) convertView.getTag();
            if (mViewHolder == null) {
                mViewHolder = new ViewHolder(convertView);
            }

            return mViewHolder;
        }
    }

    @Override
    public void onClick(View v) {

    }
}
