package com.quanyan.yhy.ui.common.city.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.quanyan.yhy.R;
import com.yhy.common.beans.city.bean.AddressBean;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {
    private ArrayList<AddressBean> hotCitys;
    public GridAdapter(ArrayList<AddressBean> hotCitys){
        this.hotCitys=hotCitys;
    }
    @Override
    public int getCount() {
        return hotCitys.size();
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
        View view;
        if(convertView==null){
            view= View.inflate(parent.getContext(), R.layout.item_grid_city, null);
        }else{
            view=convertView;
        }
        IndexAddressHolder indexAddressHolder=IndexAddressHolder.getHolder(view);
        indexAddressHolder.tv_grid_child.setText(hotCitys.get(position).getName());

        return view;
    }

}
