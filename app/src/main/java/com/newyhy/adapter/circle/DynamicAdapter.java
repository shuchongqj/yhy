package com.newyhy.adapter.circle;

import android.app.Activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.newyhy.config.CircleItemType;
import com.newyhy.views.itemview.CircleUgcCoffeeVideoLayout;
import com.newyhy.views.itemview.CircleUgcLiveLayout;
import com.newyhy.views.itemview.CircleUgcPicLayout;
import com.quanyan.yhy.R;
import com.yhy.common.beans.net.model.discover.UgcInfoResult;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 改版圈子中 动态模块 Adapter
 * Created by yangboxue on 2018/6/20.
 */

public class DynamicAdapter extends BaseQuickAdapter<UgcInfoResult, BaseViewHolder> {

    private Activity mActivity;
    public HashMap<String, String> extraMap;

    public DynamicAdapter(Activity activity, ArrayList<UgcInfoResult> list) {
        super(list);
        mActivity = activity;

        setMultiTypeDelegate(new MultiTypeDelegate<UgcInfoResult>() {
            @Override
            protected int getItemType(UgcInfoResult data) {
                int viewType = CircleItemType.UGC_PIC;
                if (data.contentType == 3) return CircleItemType.LIVE;//直播
                if (data.shortVideoType == 4 || data.shortVideoType == 5) return CircleItemType.COFFEE_VIDEO;//小视频
                if (data.contentType == 2) return CircleItemType.UGC_PIC;//无图带图Ugc
                return viewType;
            }
        });

        getMultiTypeDelegate()
                .registerItemType(CircleItemType.LIVE, R.layout.circle_ugc_live_recycler_item)
                .registerItemType(CircleItemType.COFFEE_VIDEO, R.layout.circle_ugc_coffee_video_recycler_item)
                .registerItemType(CircleItemType.UGC_PIC, R.layout.circle_ugc_pic_recycler_item);
    }

    @Override
    protected void convert(BaseViewHolder holder, UgcInfoResult data) {
        switch (holder.getItemViewType()) {
            case CircleItemType.LIVE:
                CircleUgcLiveLayout live = holder.getView(R.id.circle_item_live);
                live.setData(mActivity,data);
                live.position = holder.getAdapterPosition();
                live.extraMap = extraMap;
                break;
            case CircleItemType.COFFEE_VIDEO:
                CircleUgcCoffeeVideoLayout coffee_video = holder.getView(R.id.circle_item_coffee_video);
                coffee_video.setData(mActivity,data);
                coffee_video.position = holder.getAdapterPosition();
                coffee_video.extraMap = extraMap;
                break;
            case CircleItemType.UGC_PIC:
                CircleUgcPicLayout ugc_pic = holder.getView(R.id.circle_item_ugcpic);
                ugc_pic.setData(mActivity,data);
                ugc_pic.position = holder.getAdapterPosition();
                ugc_pic.extraMap = extraMap;
                break;
        }
    }
}
