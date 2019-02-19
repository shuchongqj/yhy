package com.newyhy.adapter.live;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.newyhy.activity.HorizontalLiveActivity;
import com.quanyan.yhy.R;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.videolibrary.utils.IntentUtil;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.location.LocationManager;
import com.yhy.network.resp.snscenter.GetUserSuperbListResp;


import java.util.List;

public class UserAllVideosAdapter extends BaseQuickAdapter<GetUserSuperbListResp.LiveRecordResult,BaseViewHolder> {

    public UserAllVideosAdapter(int layoutResId, @Nullable List<GetUserSuperbListResp.LiveRecordResult> data) {
        super(layoutResId, data);
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 埋点
                Analysis.pushEvent(mContext, AnEvent.PageVideoRecommend,
                        new Analysis.Builder().
                                setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                setId(String.valueOf(data.get(position).liveId)));

                IntentUtil.startVideoClientActivity(data.get(position).liveId,
                        -1, !"REPLAY_LIVE".equals(data.get(position).liveStatus),
                        "HORIZONTAL".equals(data.get(position).liveScreenType) ? 0 : 1);
            }
        });
    }


    @Override
    protected void convert(BaseViewHolder holder, GetUserSuperbListResp.LiveRecordResult item) {
        ImageView video_cover = holder.getView(R.id.video_cover);
        TextView tv_anchor_name = holder.getView(R.id.tv_anchor_name);
        TextView tv_video_view_num = holder.getView(R.id.tv_video_view_num);
        TextView tv_video_name = holder.getView(R.id.tv_video_name);
        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(item.liveCover),R.mipmap.icon_default_750_360,
                video_cover,3,true);
        tv_anchor_name.setText(item.nickname);
        tv_video_view_num.setText(""+item.viewCount);
        tv_video_name.setText(item.liveTitle);
    }
}
