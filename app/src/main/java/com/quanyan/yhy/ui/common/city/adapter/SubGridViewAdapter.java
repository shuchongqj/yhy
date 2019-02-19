package com.quanyan.yhy.ui.common.city.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quanyan.yhy.R;
import com.yhy.common.beans.city.bean.AddressBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:SubGridViewAdapter
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-3-9
 * Time:19:21
 * Version 1.0
 */

public class SubGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<AddressBean> datas;

    public SubGridViewAdapter(Context context) {
        this.mContext = context;
        datas = new ArrayList<>();
    }

    public void addAll(List<AddressBean> elem) {
        datas.addAll(elem);
        notifyDataSetChanged();
    }

    public void replaceAll(List<AddressBean> elem) {
        datas.clear();
        datas.addAll(elem);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if(convertView == null){
            view = View.inflate(parent.getContext(), R.layout.item_subject_address, null);
        }else{
            view = convertView;
        }

        SubHolder holder = SubHolder.getHolder(view);
        holder.tv_name.setText(datas.get(position).getName());

        return view;
    }

    static class SubHolder{
        TextView tv_name;
        public SubHolder(View convertView){
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);

        }
        public static SubHolder getHolder(View convertView){
            SubHolder holder= (SubHolder) convertView.getTag();
            if(holder==null){
                holder=new SubHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }
}
