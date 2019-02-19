package com.quanyan.yhy.ui.discovery.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.common.calendar.ScreenUtil;
import com.yhy.common.beans.net.model.PhotoData;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 发帖9个图片的gridadapter
 * Created by loza on 2015/6/6.
 */
public class SendSubjectImgGridAdapter extends BaseAdapter {
    private LayoutInflater inflater; // 视图容器
    private int selectedPosition = -1;// 选中的位置
    private boolean shape;
    private Context mContext;
    private List<PhotoData> mData = new ArrayList<PhotoData>();
    private int mIamgeWidth = 0, mIamgeHeight = 0;
    private PicNumChanged mPicNumChanged;
    private RelativeLayout.LayoutParams mLayoutParams;
    private AbsListView.LayoutParams mParentParams;
    private int mMaxPic;

    public boolean isShape() {
        return shape;
    }

    public void setShape(boolean shape) {
        this.shape = shape;
    }

    public void setPicNumChanged(PicNumChanged picNumChanged){
        this.mPicNumChanged = picNumChanged;
    }

    public SendSubjectImgGridAdapter(Context context) {
        mMaxPic = ValueConstants.MAX_SELECT_PICTURE;
        init(context);
    }

    public SendSubjectImgGridAdapter(Context context, int maxPic) {
        this.mMaxPic = maxPic;
        init(context);
    }

    private void init(Context context) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        mIamgeWidth = (ScreenUtil.getScreenWidth(mContext) - 30 * 2
                - 40 * 2) / 3;
        mIamgeHeight = mIamgeWidth + 5;

        mLayoutParams = new RelativeLayout.LayoutParams(mIamgeWidth, mIamgeWidth);
        mParentParams = new AbsListView.LayoutParams(mIamgeHeight, mIamgeHeight);
    }


    public int getCount() {
        if(mData.size() == mMaxPic){
            return mData.size();
        }else {
            return (mData.size() + 1);
        }
    }

    public Object getItem(int arg0) {

        return mData.size() <= 0 ? null : mData.get(arg0);
    }

    public long getItemId(int arg0) {

        return arg0;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    /**
     * ListView Item设置
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
//        if(convertView == null) {
            convertView = inflater.inflate(R.layout.item_send_grid_image, parent, false);
            holder = new ViewHolder();
            holder.mRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.select_img_parent);
            holder.image = (ImageView) convertView.findViewById(R.id.ivpush);
            holder.mDeleteImg = (ImageView) convertView.findViewById(R.id.select_img_delete);

            holder.image.setLayoutParams(mLayoutParams);
            holder.mRelativeLayout.setLayoutParams(mParentParams);
            convertView.setTag(holder);
//        }else{
//            holder = (ViewHolder) convertView.getTag();
//        }
        if (position == mData.size()) {
//            holder.image.setVisibility(View.VISIBLE);
            holder.mDeleteImg.setVisibility(View.GONE);
            holder.image.setImageResource(R.mipmap.add_photo_new);
        } else {
//            holder.image.setVisibility(View.VISIBLE);
            holder.mDeleteImg.setVisibility(View.VISIBLE);
            holder.mDeleteImg.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mData.remove(position);
                    notifyDataSetChanged();
                    mPicNumChanged.onPicNumChange(mData);
                }
            });
//            ImageLoaderUtil.loadLocalImage(holder.image, !TextUtils.isEmpty(mData.get(position).mThumbnail) ? mData.get(position).mThumbnail : mData.get(position).mLocalUrl);
            ImageLoadManager.loadImage(!TextUtils.isEmpty(mData.get(position).mThumbnail) ? CommonUtil.getImageFullUrl(mData.get(position).mThumbnail) : CommonUtil.getImageFullUrl(mData.get(position).mLocalUrl), holder.image);

        }
        return convertView;
    }

    public void setData(List<PhotoData> data) {
        mData = data;
        notifyDataSetChanged();
        mPicNumChanged.onPicNumChange(mData);
    }

    public int getDataSize(){
        return mData.size();
    }

    public void replaceAll(List<PhotoData> data){
        this.mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
        mPicNumChanged.onPicNumChange(mData);
    }

    public void addAll(List<PhotoData> data){
        this.mData.addAll(data);
        notifyDataSetChanged();
        mPicNumChanged.onPicNumChange(mData);
    }

    public List<PhotoData> getData(){
        return this.mData;
    }

    public void clear() {
        this.mData.clear();
        notifyDataSetChanged();
        mPicNumChanged.onPicNumChange(mData);
    }

    public class ViewHolder {
        RelativeLayout mRelativeLayout;
        ImageView image;
        ImageView mDeleteImg;
    }

    public interface PicNumChanged{
        void onPicNumChange(List<PhotoData> data);
    }

}