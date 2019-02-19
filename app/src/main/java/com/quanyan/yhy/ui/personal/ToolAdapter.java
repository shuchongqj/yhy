package com.quanyan.yhy.ui.personal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.harwkin.nb.camera.ImageUtils;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.personal.model.CommonToolInfo;

import java.util.ArrayList;

/**
 * Created by shenwenjie on 2018/1/31.
 */

public class ToolAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CommonToolInfo> list ;

    public static class ViewHolder {
        public ImageView ivToolImage;
        public TextView tvToolName;
    }

    public ToolAdapter(Context context,ArrayList<CommonToolInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ToolAdapter.ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ToolAdapter.ViewHolder();
            convertView = LayoutInflater.from(this.context).inflate(R.layout.layout_common_tool, parent, false);
            viewHolder.ivToolImage = (ImageView) convertView.findViewById(R.id.personal_common_tool_imageView);
            viewHolder.tvToolName = (TextView) convertView.findViewById(R.id.personal_common_tool_textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ToolAdapter.ViewHolder) convertView.getTag();
        }
        if (list != null && !list.isEmpty()) {
            CommonToolInfo i = list.get(position);

            String cover = i.getCover();
            if (cover != null && !cover.isEmpty()) {
                cover = ImageUtils.getImageFullUrl(cover);
                Glide.with(context).load(cover).into(viewHolder.ivToolImage);
            } else {
                int resId = i.getResId();
                if (resId != 0) {
                    viewHolder.ivToolImage.setImageResource(resId);
                }
            }
            String title = i.getTitle();
            if (title != null && !title.isEmpty()) {
                viewHolder.tvToolName.setText(title);
            }
        }
        return convertView;
    }

}
