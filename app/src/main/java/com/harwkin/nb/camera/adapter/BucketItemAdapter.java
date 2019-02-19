package com.harwkin.nb.camera.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newyhy.utils.DateUtil;
import com.quanyan.yhy.R;
import com.yhy.common.beans.album.MediaItem;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

import java.util.List;

public class BucketItemAdapter extends BaseAdapter {

    private Context mContext;
    private List<MediaItem> dataList;
    public BucketItemAdapter(Context mContext, List<MediaItem> dataList) {
        super();
        this.mContext = mContext;
        this.dataList = dataList;

    }

    @Override
    public int getCount() {
        return null != dataList ? dataList.size() : 0;
    }

    @Override
    public MediaItem getItem(int position) {
        return null != dataList ? dataList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        private ImageView iv;
        private ImageView selected;
        private ImageView iv_media_type;
        private TextView tv_duration;
        private LinearLayout ll_icon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_image_bucket_item, null);
            holder.iv = convertView.findViewById(R.id.iv_image_item);
            holder.iv_media_type = convertView.findViewById(R.id.iv_media_type);
            holder.tv_duration = convertView.findViewById(R.id.tv_duration);
            holder.ll_icon = convertView.findViewById(R.id.ll_icon);
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            int height = width/4 - mContext.getResources().getDimensionPixelSize(R.dimen.image_height_padding);
            holder.iv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height));
            holder.selected = convertView.findViewById(R.id.iv_image_select);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (dataList.get(position).isSelected)
            holder.selected.setVisibility(View.VISIBLE);
        else
            holder.selected.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(dataList.get(position).thumbnailPath)) {
            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(dataList.get(position).thumbnailPath), holder.iv);
        } else {
            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(dataList.get(position).mediaPath), holder.iv);
        }

        //视频Item
        if (dataList.get(position).mediaType == 2) {
            holder.ll_icon.setVisibility(View.VISIBLE);
            holder.tv_duration.setText(DateUtil.getTimeStrBySecond(dataList.get(position).getDuration()/1000));
        }else {
            holder.ll_icon.setVisibility(View.GONE);
        }

        return convertView;
    }

}
