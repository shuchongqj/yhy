package com.quanyan.yhy.ui.common.address.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.yhy.common.beans.net.model.common.address.MyAddressContentInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:Adapter
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:和平必胜、正义必胜、人民必胜
 * Author:炎黄子孙
 * Date:15/10/30
 * Time:下午8:27
 * Version 1.0
 */
public class AddressInfoAdapter extends BaseAdapter{
    private Context mContext;
    private int type;
    private List<MyAddressContentInfo> mAddresses;
    private String province = "";
    private String city = "";
    private String area = "";
    private String default_address = "1";

    public AddressInfoAdapter(Context context, int type){
        mContext = context;
        mAddresses = new ArrayList<>();
        this.type = type;
    }

    /**
     * 更新数据
     * @param list
     */
    public void setData(List<MyAddressContentInfo> list){
        if(list != null){
            mAddresses.clear();
            mAddresses.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void clearData(){
        mAddresses.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mAddresses.size();
    }

    @Override
    public Object getItem(int position) {
        return mAddresses.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_address_list, null);
        }

        AddressHolder holder = AddressHolder.getHolder(convertView);
        final MyAddressContentInfo myAddress = (MyAddressContentInfo) getItem(position);

        if(!StringUtil.isEmpty(myAddress.isDefault)){
            if(myAddress.isDefault.equals(default_address)){
                holder.tv_default.setVisibility(View.VISIBLE);
            }else {
                holder.tv_default.setVisibility(View.INVISIBLE);
            }
        }

        if(!StringUtil.isEmpty(myAddress.recipientsName)) {
            holder.tv_name.setText(myAddress.recipientsName);
        }

        if(!StringUtil.isEmpty(myAddress.recipientsPhone)) {
            holder.tv_phone.setText(myAddress.recipientsPhone);
        }

        //详细地址
        if(!StringUtil.isEmpty(myAddress.detailAddress)){
            if(!StringUtil.isEmpty(myAddress.province)){
                province = myAddress.province;
            }
            if(!StringUtil.isEmpty(myAddress.city) && !myAddress.city.equals(myAddress.province)){
                city = myAddress.city;
            }else {
                city = "";
            }
            if(!StringUtil.isEmpty(myAddress.area)){
                area = myAddress.area;
            }

            String newAddress = province + city + area + myAddress.detailAddress;
            holder.tv_address.setText(newAddress);
        }


        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnContactItemClickLsn != null) {
                    mOnContactItemClickLsn.onEditClick(myAddress);
                }
            }
        });


        holder.cell_visitor_list_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnContactItemClickLsn != null) {
                    mOnContactItemClickLsn.onDeleteClick(myAddress);
                }
            }
        });


        return convertView;
    }

    static class AddressHolder {

        TextView tv_default, tv_name, tv_phone, tv_address, iv_edit;
        RelativeLayout rl_visitor;
        LinearLayout cell_visitor_list_delete;
        ImageView iv_check;

        public AddressHolder(View convertView) {
            tv_default = (TextView) convertView.findViewById(R.id.tv_default);
            tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            cell_visitor_list_delete = (LinearLayout) convertView.findViewById(R.id.cell_visitor_list_delete);
            rl_visitor = (RelativeLayout) convertView.findViewById(R.id.rl_visitor);
            iv_check = (ImageView) convertView.findViewById(R.id.iv_check);
            iv_edit = (TextView) convertView.findViewById(R.id.iv_edit);
        }

        public static AddressHolder getHolder(View convertView) {
            AddressHolder holder = (AddressHolder) convertView.getTag();
            if (holder == null) {
                holder = new AddressHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }


    AddressInfoAdapter.OnAddressItemClickLsn mOnContactItemClickLsn;
    public void setOnContactItemClickLsn(AddressInfoAdapter.OnAddressItemClickLsn lsn){
        mOnContactItemClickLsn = lsn;
    }

    public interface OnAddressItemClickLsn{
        void onEditClick(MyAddressContentInfo info);

        void onDeleteClick(MyAddressContentInfo info);

        //void onSelectClick(MyAddressContentInfo info);
    }

}
