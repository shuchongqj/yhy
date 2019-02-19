package com.quanyan.yhy.ui.adapter.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.order.entity.FilterCondition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:***Acitivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:田海波
 * Date: 2016/9/22
 * Time: 18:27
 * Version 2.0
 */

public class GridViewFilterAdapter extends BaseAdapter {
    private Context context;
    List<FilterCondition> dataList;
    ViewHolder holder;
    public Map<Integer, Boolean> isSelected;


    public GridViewFilterAdapter(Context context, List<FilterCondition> dataList) {
        this.context = context;
        this.dataList = dataList;
        init();
    }

    public void init() {
        isSelected = new HashMap<Integer, Boolean>();
        for (int i = 0; i < dataList.size(); i++) {
            if (i == 0) {
                isSelected.put(i, true);
            } else {
                isSelected.put(i, false);
            }
        }


    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context,
                    R.layout.item_gridview_filter, null);
            holder.textView = (TextView) convertView.findViewById(R.id.tv_gridview_filter);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(dataList.get(position).getConName());
        holder.textView.setSelected(isSelected.get(position));
        return convertView;
    }

    public class ViewHolder {
        public TextView textView;

    }


}
