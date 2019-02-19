package com.newyhy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanyan.yhy.R;
import com.smart.sdk.api.resp.Api_PLACE_City;
import com.smart.sdk.api.resp.Api_PLACE_District;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 15/10/30.
 */
public class PopNormalAdapter_OrderVenue<T> extends ArrayAdapter {

    private int resource;
    private int selection;
    private TextView tv;
    private RelativeLayout linearLayout;
    private ImageView img;
    private List<T> lists;
    private Context mContext;

    public PopNormalAdapter_OrderVenue(Context context, int resource, List<T> lists) {
        super(context, resource);
        this.lists = lists;
        mContext = context;
        initParams(resource);
    }

    private void initParams(int resource) {
        this.resource = resource;
        this.selection = -1;
    }

    @Override
    public int getCount() {
        if (lists != null) {
            return lists.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        if (lists.get(position) instanceof Api_PLACE_City) {      // 城市选择

            Api_PLACE_City cityInfo = (Api_PLACE_City) lists.get(position);

            convertView = LayoutInflater.from(mContext).inflate(resource, null);
            tv = convertView.findViewById(R.id.tv);
            linearLayout = convertView.findViewById(R.id.linear);
            tv.setText(cityInfo.name);

            if (position == selection) {
                tv.setTextColor(getContext().getResources().getColor(R.color.bg_ed4d4d));
                linearLayout.setBackgroundColor(getContext().getResources().getColor(R.color.white));
            } else {
                tv.setTextColor(getContext().getResources().getColor(R.color.txt_2d2d37));
                linearLayout.setBackgroundColor(getContext().getResources().getColor(R.color.neu_f0f0f0));
            }

        } else if (lists.get(position) instanceof Api_PLACE_District) {   // 区域选择

            Api_PLACE_District district = (Api_PLACE_District) lists.get(position);

            convertView = LayoutInflater.from(mContext).inflate(resource, null);
            tv = convertView.findViewById(R.id.tv);
            img = convertView.findViewById(R.id.img);
            linearLayout = convertView.findViewById(R.id.linear);
            tv.setText(district.name);

            if (position == selection) {
                tv.setTextColor(getContext().getResources().getColor(R.color.bg_ed4d4d));
                tv.setSelected(true);
                img.setImageResource(R.mipmap.ic_city_selected);
            } else {
                img.setImageResource(R.mipmap.ic_city_unselected);
                tv.setSelected(false);
                tv.setTextColor(getContext().getResources().getColor(R.color.txt_2d2d37));
            }

        }

        return convertView;

    }

    public void setPressPostion(int position) {
        this.selection = position;
        notifyDataSetChanged();
    }

    public int getPressPostion() {
        return selection;
    }

    public void setData(List<T> data) {
        if (data == null || data.size() == 0) {
            lists = null;
        } else {
            if (lists == null) {
                lists = new ArrayList<>();
            }
            lists.clear();
            lists.addAll(data);
        }

        notifyDataSetChanged();

    }

}
