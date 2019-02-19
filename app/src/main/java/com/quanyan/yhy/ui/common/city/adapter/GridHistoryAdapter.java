package com.quanyan.yhy.ui.common.city.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.quanyan.yhy.R;
import com.yhy.common.beans.city.bean.AddressBean;

import java.util.ArrayList;

public class GridHistoryAdapter extends BaseAdapter {

    private ArrayList<AddressBean> historyCitys;
    public GridHistoryAdapter(ArrayList<AddressBean> historyCitys){
        this.historyCitys = historyCitys;
    }

    @Override
    public int getCount() {
        return historyCitys == null ? 0 : historyCitys.size();
    }

    @Override
    public Object getItem(int position) {
        return historyCitys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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
        indexAddressHolder.tv_grid_child.setText(historyCitys.get(position).getName());

        return view;
    }
}
