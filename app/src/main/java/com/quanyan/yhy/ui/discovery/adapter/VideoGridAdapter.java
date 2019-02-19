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
import android.widget.TextView;

import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.common.calendar.ScreenUtil;
import com.yhy.common.beans.net.model.VideoInfo;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 发帖9个图片的gridadapter
 */
public class VideoGridAdapter extends BaseAdapter {
    private LayoutInflater inflater; // 视图容器
    private boolean shape;
    private Context mContext;
    private List<VideoInfo> mData = new ArrayList<VideoInfo>();
    private List<VideoInfo> mDeleteData = new ArrayList<VideoInfo>();
    private int mIamgeWidth = 0, mIamgeHeight = 0;
    private PicNumChanged mPicNumChanged;
    private RelativeLayout.LayoutParams mLayoutParams;
    private AbsListView.LayoutParams mParentParams;

    private HashMap <Long,Boolean> mSelectMap = new HashMap<>();
    public boolean isShape() {
        return shape;
    }

    public void setShape(boolean shape) {
        this.shape = shape;
    }

    public VideoGridAdapter(Context context,List<VideoInfo> list) {
        mData = list;
        init(context);
    }

    public void setPicNumChanged(PicNumChanged picNumChanged){
        this.mPicNumChanged = picNumChanged;
    }

    /**
     * 清空选择项
     */
    private void clearSelected(){
        for(VideoInfo videoInfo:mData){
            mSelectMap.put(videoInfo.id,false);
        }
    }

    private void init(Context context) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        mIamgeWidth = (ScreenUtil.getScreenWidth(mContext) - 30 * 2 - 40 * 2) / 3;
        mIamgeHeight = mIamgeWidth + 5;

        mLayoutParams = new RelativeLayout.LayoutParams(mIamgeWidth, mIamgeWidth);
        mParentParams = new AbsListView.LayoutParams(mIamgeHeight, mIamgeHeight);

        clearSelected();
    }

    /**
     * 改变选择状态
     * @param videoInfo
     */
    public void changeSelectedStatus(VideoInfo videoInfo){
        clearSelected();
        mSelectMap.put(videoInfo.id,!mSelectMap.get(videoInfo.id));
        notifyDataSetChanged();
    }

    public int getCount() {
        return mIsEdit?mData.size():(mData.size() + 1);
    }

    public Object getItem(int arg0) {
        return mData.size() <= 0 ? null : mData.get(arg0);
    }

    public long getItemId(int arg0) {
        return arg0;
    }

    /**
     * ListView Item设置
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        convertView = inflater.inflate(R.layout.item_video_thumb, parent, false);
        holder = new ViewHolder();
        holder.mRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.select_img_parent);
        holder.image = (ImageView) convertView.findViewById(R.id.ivpush);
        holder.mDeleteImg = (ImageView) convertView.findViewById(R.id.select_img_delete);
        holder.mParentView = (RelativeLayout) convertView.findViewById(R.id.select_img_parent);
        holder.mPlayClickTv = (TextView) convertView.findViewById(R.id.tv_click_play);
        holder.mPlayImg = (ImageView) convertView.findViewById(R.id.iv_play);

        holder.image.setLayoutParams(mLayoutParams);
        holder.mRelativeLayout.setLayoutParams(mParentParams);
        convertView.setTag(holder);
        holder.mParentView.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
        if(!mIsEdit){
            if (position == mData.size()) {
                holder.mDeleteImg.setVisibility(View.GONE);
                holder.image.setImageResource(R.mipmap.add_photo);
            } else {
                if(mSelectMap.get(mData.get(position).id)){
                    holder.mPlayClickTv.setVisibility(View.VISIBLE);
                    holder.mPlayImg.setVisibility(View.VISIBLE);
                    holder.mParentView.setBackgroundColor(mContext.getResources().getColor(R.color.ac_title_bg_color));
                }else{
                    holder.mPlayClickTv.setVisibility(View.GONE);
                    holder.mPlayImg.setVisibility(View.GONE);
                    holder.mParentView.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
                }
                holder.mDeleteImg.setVisibility(View.GONE);
                holder.mDeleteImg.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mDeleteData.add(mData.get(position));
                        mData.remove(position);
                        notifyDataSetChanged();
                        mPicNumChanged.onPicNumChange(mData);
                    }
                });
//                ImageLoaderUtil.loadLocalImage(holder.image, !TextUtils.isEmpty(mData.get(position).videoThumbLocalPath) ?
//                        mData.get(position).videoThumbLocalPath : mData.get(position).videoLocalPath);
                ImageLoadManager.loadImage(!TextUtils.isEmpty(mData.get(position).videoThumbLocalPath) ?
                        CommonUtil.getImageFullUrl(mData.get(position).videoThumbLocalPath) : CommonUtil.getImageFullUrl(mData.get(position).videoLocalPath), holder.image);

            }
        }else{
            holder.mDeleteImg.setVisibility(View.VISIBLE);
            holder.mDeleteImg.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mDeleteData.add(mData.get(position));
                    mData.remove(position);
                    notifyDataSetChanged();
                    mPicNumChanged.onPicNumChange(mData);
                }
            });
//            ImageLoaderUtil.loadLocalImage(holder.image, !TextUtils.isEmpty(mData.get(position).videoThumbLocalPath) ?
//                    mData.get(position).videoThumbLocalPath : mData.get(position).videoLocalPath);
            ImageLoadManager.loadImage(!TextUtils.isEmpty(mData.get(position).videoThumbLocalPath) ?
                    CommonUtil.getImageFullUrl(mData.get(position).videoThumbLocalPath) : CommonUtil.getImageFullUrl(mData.get(position).videoLocalPath), holder.image);

        }

        return convertView;
    }

    public void setData(List<VideoInfo> data) {
        mData = data;
        mDeleteData.clear();
        clearSelected();
        notifyDataSetChanged();
        mPicNumChanged.onPicNumChange(mData);
    }

    /**
     * 获取删除列表
     * @return
     */
    public List<VideoInfo> getDeletList(){
        return mDeleteData;
    }

    /**
     * 更新编辑状态
     */
    private boolean mIsEdit = false;
    public void swichEditStatus(boolean isEdit){
        mIsEdit = isEdit;
        notifyDataSetChanged();
    }

    public int getDataSize(){
        return mData.size();
    }

    public void replaceAll(List<VideoInfo> data){
        this.mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
        mPicNumChanged.onPicNumChange(mData);
    }

    public void addAll(List<VideoInfo> data){
        this.mData.addAll(data);
        notifyDataSetChanged();
        mPicNumChanged.onPicNumChange(mData);
    }

    /**
     * 重置选择状态
     */
    public void reset(){
        clearSelected();
        notifyDataSetChanged();
    }

    public List<VideoInfo> getData(){
        return this.mData;
    }

    public class ViewHolder {
        RelativeLayout mRelativeLayout;
        ImageView image;
        ImageView mDeleteImg;
        ImageView mPlayImg;
        RelativeLayout mParentView;
        TextView mPlayClickTv;
    }

    public interface PicNumChanged{
        void onPicNumChange(List<VideoInfo> data);
    }

    /**
     * 获取当前选择的视频ID
     * @return
     */
    public long getSelectedVideo(){
        Iterator iter = mSelectMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Long key = (Long)entry.getKey();
            Boolean val = (Boolean)entry.getValue();
            if(val){
                return key;
            }
        }
        return -1;
    }
}