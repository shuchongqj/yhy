package com.quanyan.yhy.ui.common.city.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quanyan.yhy.R;
import com.yhy.common.beans.city.bean.AddressBean;

import java.util.List;

/**
 * Created with Android Studio.
 * Title:SearchCityAdapter
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-3-2
 * Time:10:14
 * Version 1.0
 */

public class SearchCityAdapter extends BaseAdapter {

    private List<AddressBean> addressBeans;

    public SearchCityAdapter(List<AddressBean> addressBeans){
        this.addressBeans = addressBeans;
    }

    public void updateListView(List<AddressBean> addressBeans){
        this.addressBeans = addressBeans;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return addressBeans == null ? 0 : addressBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return addressBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = View.inflate(parent.getContext(), R.layout.item_list_serach_city, null);
        }

        CityHolder holder = CityHolder.getHolder(convertView);
        AddressBean addressBean = addressBeans.get(position);
        holder.tv_name.setText(addressBean.getName());

        return convertView;
    }

    static class CityHolder{
        TextView tv_name;
        public CityHolder(View convertView){
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        }
        public static CityHolder getHolder(View convertView){
            CityHolder holder = (CityHolder) convertView.getTag();
            if(holder == null){
                holder = new CityHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }
}
