package com.quanyan.yhy.ui.sport;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.harwkin.nb.camera.ImageUtils;
import com.quanyan.yhy.R;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.ui.sport.model.LiveVideoInfo;
import com.videolibrary.metadata.LiveTypeConstants;
import com.videolibrary.utils.IntentUtil;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import java.util.ArrayList;

/**
 * Created by shenwenjie on 2018/1/29.
 */

public class LiveVideoAdapter extends RecyclerView.Adapter implements LiveVideoListener {

    private Context context = null;
    private ArrayList<LiveVideoInfo> list = null;
    private int source;        // 1是主场2是运动
    @Autowired
    IUserService userService;

    public LiveVideoAdapter(Context context, int source) {
        this.context = context;
        list = new ArrayList<>();
        this.source = source;
        YhyRouter.getInstance().inject(this);
    }

    public void setList(ArrayList<LiveVideoInfo> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_live_list_item, parent, false);
        LiveVideoHolder mLiveVideoHolder = new LiveVideoHolder(view);
        return mLiveVideoHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LiveVideoHolder mVideoViewHolder = (LiveVideoHolder) holder;
        if (list != null && !list.isEmpty()) {
            LiveVideoInfo i = list.get(position);

            String liveTitle = i.getLiveTitle();
            if (liveTitle != null && !liveTitle.isEmpty()) {
                mVideoViewHolder.tvVideoName.setText(liveTitle);
            }
            int viewCount = i.getViewCount();
            int onlineCount = i.getOnlineCount();
            mVideoViewHolder.tvAudienceNum.setText(String.valueOf(viewCount));

            String liveCover = i.getLiveCover();
            if (liveCover != null && !liveCover.isEmpty()) {
                liveCover = ImageUtils.getImageFullUrl(liveCover);
//                Glide.with(context).load(liveCover).into(mVideoViewHolder.ivCover);
//                Glide.with(context).load(liveCover).into(mVideoViewHolder.ivCover);
//                mVideoViewHolder.ivCover.setImageURI(liveCover);
                ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(liveCover), mVideoViewHolder.ivCover);
            }
            String liveStatus = i.getLiveStatus();
            if (liveStatus != null && liveStatus.equals(LiveTypeConstants.LIVE_ING)) {
                mVideoViewHolder.tvVideoType.setText("直播");
//                mVideoViewHolder.tvVideoType.setBackgroundResource(R.mipmap.commant_tag_video_yellow);
                mVideoViewHolder.tvVideoType.setBackgroundResource(R.drawable.commant_tag_video_red);
            } else if (liveStatus != null && liveStatus.equals(LiveTypeConstants.LIVE_REPLAY)) {
                mVideoViewHolder.tvVideoType.setText("视频");
//                mVideoViewHolder.tvVideoType.setBackgroundResource(R.mipmap.commant_tag_video_green);
                mVideoViewHolder.tvVideoType.setBackgroundResource(R.drawable.commant_tag_video_blue);
            }

            mVideoViewHolder.ivPlayVideo.setTag(position);
            mVideoViewHolder.rl_video.setTag(position);
            mVideoViewHolder.setListener(this);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public void onPlayVideoItemClick(int position) {
        if (list != null && !list.isEmpty()) {
            LiveVideoInfo i = list.get(position);
            String liveStatus = i.getLiveStatus();
            //事件统计
            if (source == 2){// 运动
                Analysis.pushEvent(context, AnEvent.WONDERFUL_VIDEO,String.valueOf(i.getLiveTitle()));
            }else {// 主场
                Analysis.pushEvent(context, AnEvent.LIVE_VIDEO,String.valueOf(i.getLiveTitle()));
            }

            if (LiveTypeConstants.LIVE_ING.equals(liveStatus)) {
                IntentUtil.startVideoClientActivity(i.getLiveId(), userService.getLoginUserId(), true, list.get(position).getLiveScreenType());
            } else if (LiveTypeConstants.LIVE_REPLAY.equals(liveStatus)) {
                IntentUtil.startVideoClientActivity(i.getLiveId(), userService.getLoginUserId(), false, list.get(position).getLiveScreenType());
            }
        }
    }

}
