package com.quanyan.yhy.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanyan.yhy.R;

/**
 * Created with Android Studio.
 * Title:ShareAdapter
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-1-21
 * Time:15:44
 * Version 1.0
 */

public class ShareAdapter extends BaseAdapter {

    private Context mContext;

    public String[] img_text = { "微信", "朋友圈", "微博", "QQ"/*,"达人圈"*/};
    public int[] imgs = {R.mipmap.share_new_weixin, R.mipmap.share_new_wxcircle,
            R.mipmap.share_new_weibo, R.mipmap.share_new_qq/*,R.mipmap.ic_app_launcher*/};

    public ShareAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return img_text.length;
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
                    R.layout.item_grid_share, parent, false);
        }
        TextView tv = BaseViewHolder.get(convertView, R.id.tv_item);
        ImageView iv = BaseViewHolder.get(convertView, R.id.iv_item);
        iv.setImageResource(imgs[position]);

        tv.setText(img_text[position]);
        return convertView;
    }
}
