package com.quanyan.yhy.ui.common.city.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quanyan.yhy.R;
import com.quanyan.yhy.view.HomeMenu_GridView;
import com.yhy.common.beans.city.bean.AddressBean;
import com.yhy.common.beans.city.bean.CityResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:DestSelectAdapter
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-3-9
 * Time:15:21
 * Version 1.0
 */

public class DestSelectAdapter extends BaseAdapter {

    private Context mContext;
    private List<AddressBean> mAddressbeans;
    private List<CityResultBean> mCityResultBeans;

    public DestSelectAdapter(Context context) {
        this.mContext = context;
        mCityResultBeans = new ArrayList<>();
    }

    public void addAll(List<CityResultBean> elem) {
        mCityResultBeans.addAll(elem);
        notifyDataSetChanged();
    }

    public void replaceAll(List<CityResultBean> elem) {
        mCityResultBeans.clear();
        mCityResultBeans.addAll(elem);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCityResultBeans == null ? 0 : mCityResultBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = View.inflate(parent.getContext(), R.layout.item_destselect_bottom, null);
        } else {
            view = convertView;
        }

        DestHolder holder = DestHolder.getHolder(view);
        CityResultBean cityResultBean = mCityResultBeans.get(position);
        holder.tv_bottom_index.setText(cityResultBean.getIndex());
        final List<AddressBean> lists = cityResultBean.getLists();
        SubGridViewAdapter subAdapter = new SubGridViewAdapter(mContext);
        holder.hg_bottom_cityname.setAdapter(subAdapter);
        subAdapter.replaceAll(lists);

        holder.hg_bottom_cityname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mOnSubscribeClickListener != null){
                    mOnSubscribeClickListener.onSubscribeClick(lists.get(position));
                }
            }
        });


        return view;
    }

    static class DestHolder {
        TextView tv_bottom_index;
        HomeMenu_GridView hg_bottom_cityname;

        public DestHolder(View convertView) {
            tv_bottom_index = (TextView) convertView.findViewById(R.id.tv_bottom_index);
            hg_bottom_cityname = (HomeMenu_GridView) convertView.findViewById(R.id.hg_bottom_cityname);

        }

        public static DestHolder getHolder(View convertView) {
            DestHolder holder = (DestHolder) convertView.getTag();
            if (holder == null) {
                holder = new DestHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }

    private OnSubscribeClickListener mOnSubscribeClickListener;

    public void setOnSubscribeClickListener(OnSubscribeClickListener listener){
        this.mOnSubscribeClickListener = listener;
    }


    public interface OnSubscribeClickListener {
        public void onSubscribeClick(AddressBean addressBean);
    }

}
