package com.quanyan.yhy.ui.common.address.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quanyan.yhy.R;
import com.yhy.common.beans.net.model.common.address.AddressItem;

import java.util.List;

public class AddressAdapter extends BaseAdapter {
    List<AddressItem> data;
    LayoutInflater inflater;

    public AddressAdapter(List<AddressItem> data, Context context) {
        super();
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int arg0) {
        return data.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    class Hander {
        private TextView tv_address_item;
    }

    @Override
    public View getView(int arg0, View contentview, ViewGroup arg2) {
        Hander hander;
        if (contentview == null) {
            hander = new Hander();
            contentview = inflater.inflate(R.layout.list_item_common_address,null);
            hander.tv_address_item = (TextView) contentview .findViewById(R.id.tv_common_address_item);
            contentview.setTag(hander);
        } else {
            hander = (Hander) contentview.getTag();
        }
        hander.tv_address_item.setText(data.get(arg0).name);
        return contentview;
    }

}
