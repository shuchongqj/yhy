package com.quanyan.yhy.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newyhy.views.RoundImageView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_FieldClubDto;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;

import java.util.List;

/**
 * Created by Administrator on 2018/1/23.
 */

public class LocalClubAdapter extends RecyclerView.Adapter<LocalClubAdapter.VideoViewHolder> {
    private List<Api_RESOURCECENTER_FieldClubDto> clubs;
    private Context mContext;

    public LocalClubAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<Api_RESOURCECENTER_FieldClubDto> clubList) {
        clubs = clubList;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.local_club_recommend_item, parent, false);
        return new LocalClubAdapter.VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, final int position) {
        final Api_RESOURCECENTER_FieldClubDto clubInfo = clubs.get(position);
//        holder.local_club_img.setImageURI(ContextHelper.getImageUrl()+clubInfo.cover);
        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(clubInfo.cover), R.mipmap.app_launch_icon, holder.local_club_img);
        holder.club_name.setText(clubInfo.name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //事件统计
                Analysis.pushEvent(mContext, AnEvent.PERMANENT_CLUB,String.valueOf(clubInfo.clubId));
                NavUtils.startWebview((Activity) mContext, "",
                        SPUtils.getClubDetail(mContext).replace(":id", String.valueOf(clubs.get(position).clubId)), 10119);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (clubs == null) {
            return 0;
        }
        return clubs.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        private RoundImageView local_club_img;
        private TextView club_name;

        public VideoViewHolder(View itemView) {
            super(itemView);
            local_club_img = itemView.findViewById(R.id.local_club_img);
            club_name = itemView.findViewById(R.id.club_name);
        }
    }
}

