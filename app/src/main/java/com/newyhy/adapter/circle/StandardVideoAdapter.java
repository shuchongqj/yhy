package com.newyhy.adapter.circle;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.newyhy.activity.VideoPlayer;
import com.newyhy.beans.CircleLiveInfoResult;
import com.newyhy.views.YhyListVideoView;
import com.newyhy.views.itemview.CircleStandardVideoLayout;
import com.quanyan.yhy.R;
import com.tencent.rtmp.TXVodPlayer;
import com.yhy.common.base.BaseNewFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 标准视频 Adapter
 * Created by Jiervs on 2018/6/19.
 */

public class StandardVideoAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Activity mActivity;
    private Fragment mFragment;
    private List<CircleLiveInfoResult> mList;
    public HashMap<String, String> extraMap;

    public StandardVideoAdapter(Activity mActivity, @Nullable Fragment mFragment,TXVodPlayer player, @Nullable List<CircleLiveInfoResult> list) {
        this.mActivity = mActivity;
        this.mFragment = mFragment;
        this.mList = list;
    }

    protected void convert(BaseViewHolder holder, CircleLiveInfoResult item, String map) {
        CircleStandardVideoLayout standard = holder.getView(R.id.circle_item_standard_video);
        standard.position = holder.getAdapterPosition();
        standard.extraMap = extraMap;
        standard.setData(mActivity,item, VideoPlayer.getInstance().getPosition() == holder.getAdapterPosition());
        standard.setOnlyPlay(() -> {

            int lastPlayingPosition = VideoPlayer.getInstance().getPosition();

            if (lastPlayingPosition == holder.getAdapterPosition()) return;
            if (lastPlayingPosition != -1) {
                VideoPlayer.getInstance().stop();
                notifyItemChanged(lastPlayingPosition);
            }

            VideoPlayer.getInstance().setPosition(holder.getAdapterPosition());
            notifyItemChanged(holder.getAdapterPosition());
        });

        standard.setVideoMatchActivity(mActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position, @NonNull List<Object> payloads) {

        if (payloads.size() == 0){
            convert(holder, mList.get(position), null);
            return;
        }

        String map = (String) payloads.get(0);
        convert(holder, mList.get(position), map);

    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.circle_standard_video_recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        convert(holder, mList.get(position), null);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
