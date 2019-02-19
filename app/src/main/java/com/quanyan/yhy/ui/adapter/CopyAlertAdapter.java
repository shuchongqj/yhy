package com.quanyan.yhy.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quanyan.yhy.R;

/**
 * Created with Android Studio.
 * Title:CopyAlertAdapter
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-1-14
 * Time:10:18
 * Version 1.0
 */

public class CopyAlertAdapter extends BaseAdapter{

    private Context mContext;

    public String[] text_count = {"复制"};

    public CopyAlertAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return text_count.length;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_list_alert, parent, false);
        }
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title_name);

        tvTitle.setText(text_count[position]);
        return convertView;
    }
}
