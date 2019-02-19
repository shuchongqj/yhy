package com.newyhy.adapter.circle;

import android.app.Activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.newyhy.beans.CircleCrawlInfo;
import com.newyhy.config.CircleItemType;
import com.newyhy.views.itemview.CircleNews1PicLayout;
import com.newyhy.views.itemview.CircleNews3PicLayout;
import com.newyhy.views.itemview.CircleNewsNoPicLayout;
import com.newyhy.views.itemview.CircleNewsVideoLayout;
import com.quanyan.yhy.R;
import com.yhy.common.base.BaseNewFragment;
import com.yhy.network.resp.snscenter.GetRecommendPageListResp;

import java.util.HashMap;
import java.util.List;

/**
 * 改版圈子中 推荐模块的 Adapter
 * Created by Jiervs on 2018/6/19.
 */

public class RecommendAdapter extends BaseQuickAdapter<GetRecommendPageListResp.RecommendResult, BaseViewHolder> {

    private Activity mActivity;
    private List<GetRecommendPageListResp.RecommendResult> mList;
    private String searchKey;
    public HashMap<String, String> extraMap;


    public String tab = "";     // 圈子打点用
    public String tag = "";     // 圈子打点用

    public RecommendAdapter(Activity mActivity , List<GetRecommendPageListResp.RecommendResult> list) {
        super(list);
        this.mActivity = mActivity;
        this.mList = list;

        setMultiTypeDelegate(new MultiTypeDelegate<GetRecommendPageListResp.RecommendResult>() {
            @Override
            protected int getItemType(GetRecommendPageListResp.RecommendResult data) {
                int viewType = 0;
                if (data.videoUrl != null && data.videoUrl.length() >0) {
                    viewType = CircleItemType.STANDARD_VIDEO;
                } else if (data.picList == null || data.picList.size() == 0) {
                    viewType = CircleItemType.NEWS_NOPIC;
                } else if (data.picList.size() == 1 || data.picList.size() == 2) {
                    viewType = CircleItemType.NEWS_1PIC;
                } else if (data.picList.size() >= 3) {
                    viewType = CircleItemType.NEWS_3PIC;
                }
                return viewType;
            }
        });

        getMultiTypeDelegate()
                .registerItemType(CircleItemType.NEWS_NOPIC, R.layout.circle_news_nopic_recycler_item)
                .registerItemType(CircleItemType.NEWS_1PIC, R.layout.circle_news_1pic_recycler_item)
                .registerItemType(CircleItemType.NEWS_3PIC, R.layout.circle_news_3pic_recycler_item)
                .registerItemType(CircleItemType.STANDARD_VIDEO, R.layout.circle_news_video_recycler_item);
                /*.registerItemType(CircleItemType.LIVE, R.layout.circle_ugc_live_recycler_item)
                .registerItemType(CircleItemType.COFFEE_VIDEO, R.layout.circle_ugc_coffee_video_recycler_item)
                .registerItemType(CircleItemType.UGC_PIC, R.layout.circle_ugc_pic_recycler_item);*/
    }

    @Override
    protected void convert(BaseViewHolder holder, GetRecommendPageListResp.RecommendResult data) {
        switch (holder.getItemViewType()) {
            case CircleItemType.NEWS_NOPIC:
                CircleNewsNoPicLayout news_nopic = holder.getView(R.id.circle_item_news_nopic);
                if (searchKey != null && searchKey.length() > 0) news_nopic.searchKey = searchKey;
                news_nopic.setData(mActivity,data);
                news_nopic.position = holder.getAdapterPosition();
                news_nopic.extraMap = extraMap;
                break;
            case CircleItemType.NEWS_1PIC:
                CircleNews1PicLayout news_1pic = holder.getView(R.id.circle_item_news_1pic);
                if (searchKey != null && searchKey.length() > 0) news_1pic.searchKey = searchKey;
                news_1pic.setData(mActivity,data);
                news_1pic.position = holder.getAdapterPosition();
                news_1pic.extraMap = extraMap;
                break;
            case CircleItemType.NEWS_3PIC:
                CircleNews3PicLayout news_3pic = holder.getView(R.id.circle_item_news_3pic);
                if (searchKey != null && searchKey.length() > 0) news_3pic.searchKey = searchKey;
                news_3pic.setData(mActivity,data);
                news_3pic.position = holder.getAdapterPosition();
                news_3pic.extraMap = extraMap;
                break;
            case CircleItemType.STANDARD_VIDEO:
                CircleNewsVideoLayout video = holder.getView(R.id.circle_item_news_video);
                if (searchKey != null && searchKey.length() > 0) video.searchKey = searchKey;
                video.setData(mActivity,data);
                video.position = holder.getAdapterPosition();
                video.extraMap = extraMap;
                break;
        }
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }
}
