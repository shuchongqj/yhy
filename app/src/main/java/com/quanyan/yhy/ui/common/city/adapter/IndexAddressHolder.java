package com.quanyan.yhy.ui.common.city.adapter;

import android.view.View;
import android.widget.TextView;

import com.quanyan.yhy.R;

public class IndexAddressHolder {
    public TextView tv_grid_child;
    public IndexAddressHolder(View convertView){
        tv_grid_child= (TextView) convertView.findViewById(R.id.tv_grid_child);

    }
    public static IndexAddressHolder getHolder(View convertView){
        IndexAddressHolder holder= (IndexAddressHolder) convertView.getTag();
        if(holder==null){
            holder=new IndexAddressHolder(convertView);
            convertView.setTag(holder);
        }
        return holder;
    }
}
