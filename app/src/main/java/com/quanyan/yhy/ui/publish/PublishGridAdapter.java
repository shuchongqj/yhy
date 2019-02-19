package com.quanyan.yhy.ui.publish;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanyan.yhy.R;

import java.util.ArrayList;

/**
 * Created by shenwenjie on 2018/1/23.
 * 适配器
 */

public class PublishGridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<PublishInfo> list;

    public static class ViewHolder {
        public TextView textView;
        public ImageView imageView;
    }

    public PublishGridAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<PublishInfo> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return list != null ? list.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(this.context).inflate(R.layout.layout_personal_grid_item, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.personal_grid_item_imageView);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.personal_grid_item_textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (list != null && !list.isEmpty()) {
            PublishInfo info = list.get(position);
            viewHolder.imageView.setImageResource(info.getResId());
            viewHolder.textView.setText(info.getTitle());
        }
        return convertView;
    }
}
