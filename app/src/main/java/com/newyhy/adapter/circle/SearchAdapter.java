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
import com.newyhy.views.itemview.CircleStandardVideoLayout;
import com.quanyan.yhy.R;

import java.util.List;

/**
 * 改版圈子中 推荐模块的 Adapter
 * Created by Jiervs on 2018/6/19.
 */

public class SearchAdapter extends BaseQuickAdapter<CircleCrawlInfo, BaseViewHolder> {

    private Activity mActivity;
    private List<CircleCrawlInfo> mList;

    public SearchAdapter(Activity mActivity , List<CircleCrawlInfo> list) {
        super(list);
        this.mActivity = mActivity;
        this.mList = list;

        setMultiTypeDelegate(new MultiTypeDelegate<CircleCrawlInfo>() {
            @Override
            protected int getItemType(CircleCrawlInfo data) {
                int viewType = 0;
                if (data.picList == null || data.picList.size() == 0) {
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
                .registerItemType(CircleItemType.NEWS_3PIC, R.layout.circle_news_3pic_recycler_item);
                /*.registerItemType(CircleItemType.STANDARD_VIDEO, R.layout.circle_standard_video_recycler_item)
                /*.registerItemType(CircleItemType.LIVE, R.layout.circle_ugc_live_recycler_item)
                .registerItemType(CircleItemType.COFFEE_VIDEO, R.layout.circle_ugc_coffee_video_recycler_item)
                .registerItemType(CircleItemType.UGC_PIC, R.layout.circle_ugc_pic_recycler_item);*/
    }

    @Override
    protected void convert(BaseViewHolder holder, CircleCrawlInfo data) {
        switch (holder.getItemViewType()) {
            case CircleItemType.NEWS_NOPIC:
                CircleNewsNoPicLayout news_nopic = holder.getView(R.id.circle_item_news_nopic);
                news_nopic.setData(mActivity,data);
                break;
            case CircleItemType.NEWS_1PIC:
                CircleNews1PicLayout news_1pic = holder.getView(R.id.circle_item_news_1pic);
                news_1pic.setData(mActivity,data);
                break;
            case CircleItemType.NEWS_3PIC:
                CircleNews3PicLayout news_3pic = holder.getView(R.id.circle_item_news_3pic);
                news_3pic.setData(mActivity,data);
                break;
            /*case CircleItemType.STANDARD_VIDEO:
                CircleStandardVideoLayout standard = holder.getView(R.id.circle_item_standard_video);
                standard.setData(mActivity,data);
                break;*/
        }
    }
}
