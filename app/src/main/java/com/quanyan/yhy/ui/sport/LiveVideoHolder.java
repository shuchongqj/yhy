package com.quanyan.yhy.ui.sport;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newyhy.views.RoundImageView;
import com.quanyan.yhy.R;

/**
 * Created by shenwenjie on 2018/1/29.
 */

public class LiveVideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView tvVideoType = null;
    TextView tvVideoName = null;
    TextView tvAudienceNum = null;
    ImageView ivPlayVideo = null;
    RoundImageView ivCover = null;
    LiveVideoListener listener = null;
    RelativeLayout rl_video = null;

    public LiveVideoHolder(View itemView) {
        super(itemView);
        rl_video = (RelativeLayout) itemView.findViewById(R.id.rl_video);
        ivCover = (RoundImageView) itemView.findViewById(R.id.live_list_item_ivCover);
        tvVideoType = (TextView) itemView.findViewById(R.id.live_list_item_tvVideoType);
        tvVideoName = (TextView) itemView.findViewById(R.id.live_list_item_tvVideoName);
        tvAudienceNum = (TextView) itemView.findViewById(R.id.live_list_item_tvAudienceNum);
        ivPlayVideo = (ImageView) itemView.findViewById(R.id.live_list_item_ivPlayVideo);
        rl_video.setOnClickListener(this);
        ivPlayVideo.setOnClickListener(this);
    }

    public void setListener(LiveVideoListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.live_list_item_ivPlayVideo:
                if (listener != null) {
                    int position = (int)v.getTag();
                    listener.onPlayVideoItemClick(position);
                }
                break;
            case R.id.rl_video:
                if (listener != null) {
                    int position = (int)v.getTag();
                    listener.onPlayVideoItemClick(position);
                }
                break;
            default:
                break;
        }
    }
}
