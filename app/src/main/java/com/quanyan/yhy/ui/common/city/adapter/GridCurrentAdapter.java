package com.quanyan.yhy.ui.common.city.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quanyan.base.util.LocalUtils;
import com.quanyan.yhy.R;
import com.yhy.common.beans.city.bean.AddressBean;

public class GridCurrentAdapter extends BaseAdapter {

    private Context mContext;

    public GridCurrentAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return 1;
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
        final View view = View.inflate(parent.getContext(), R.layout.item_grid_city, null);
        TextView tv_grid_child = (TextView) view.findViewById(R.id.tv_grid_child);
        AddressBean addressBean = LocalUtils.citycodeToLocal(view.getContext());
        if (addressBean != null && addressBean.getName() != null) {
            tv_grid_child.setText(addressBean.getName());
        }
        return view;
    }


}
