package com.harwkin.nb.camera.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.harwkin.nb.camera.album.MediaBucket;
import com.quanyan.yhy.R;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

import java.util.List;


public class BucketAdapter extends BaseAdapter {

    private Context mContext;
    private List<MediaBucket> dataList;
    private int selectOptions;
    public BucketAdapter(Context mContext, List<MediaBucket> dataList) {
        super();
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return null != dataList ? dataList.size() : 0;
    }

    @Override
    public MediaBucket getItem(int position) {
        return null != dataList ? dataList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        ImageView mImg;
        ImageView mSelectBucket;
        TextView mName;
        TextView mCount;
    }

    public void setSelectOptions(int selectOptions){
        this.selectOptions = selectOptions;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_image_bucket,
                    null);
            holder.mImg = (ImageView) convertView.findViewById(R.id.image);
            holder.mName = (TextView) convertView.findViewById(R.id.name);
            holder.mCount = (TextView) convertView.findViewById(R.id.count);
            holder.mSelectBucket = (ImageView) convertView.findViewById(R.id.iv_select_bucket);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        if (!TextUtils.isEmpty(dataList.get(position).bucketName)) {
            holder.mName.setText(dataList.get(position).bucketName);
        }
        if (null != dataList.get(position).mediaList && dataList.get(position).mediaList.size() > 0 ) {
            if ( !TextUtils
                    .isEmpty(dataList.get(position).mediaList.get(0).thumbnailPath)) {
//                ImageLoaderUtil.loadLocalImage(holder.mImg,
//                        dataList.get(position).mediaList.get(0).thumbnailPath);
                ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(dataList.get(position).mediaList.get(0).thumbnailPath), holder.mImg);

            } else {
//                ImageLoaderUtil.loadLocalImage(holder.mImg,
//                        dataList.get(position).mediaList.get(0).mediaPath);
                ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(dataList.get(position).mediaList.get(0).thumbnailPath), holder.mImg);

            }
        }
        if (position == selectOptions){
            holder.mSelectBucket.setVisibility(View.VISIBLE);
        }else{
            holder.mSelectBucket.setVisibility(View.GONE);
        }

        holder.mCount.setText(String.valueOf(dataList.get(position).count)+"张");

        return convertView;
    }
}
