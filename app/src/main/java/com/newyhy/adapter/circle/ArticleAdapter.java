package com.newyhy.adapter.circle;

import android.app.Activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.newyhy.beans.TestBean;
import com.newyhy.config.CircleItemType;
import com.quanyan.yhy.R;

import java.util.ArrayList;

/**
 * Created by yangboxue on 2018/6/25.    ArticleAdapter
 */

public class ArticleAdapter extends BaseQuickAdapter<TestBean, BaseViewHolder> {

    private Activity mActivity;

    public ArticleAdapter(Activity activity, ArrayList<TestBean> list) {
        super(list);
        mActivity = activity;

        setMultiTypeDelegate(new MultiTypeDelegate<TestBean>() {
            @Override
            protected int getItemType(TestBean data) {
                int viewType = 0;
                switch (data.type) {
                    case 0:
                        viewType = CircleItemType.NEWS_NOPIC;
                        break;
                    case 1:
                        viewType = CircleItemType.NEWS_1PIC;
                        break;
                    case 2:
                        viewType = CircleItemType.NEWS_3PIC;
                        break;
                    case 3:
                        viewType = CircleItemType.LIVE;
                        break;
                    case 4:
                        viewType = CircleItemType.COFFEE_VIDEO;
                        break;
                    case 5:
                        viewType = CircleItemType.STANDARD_VIDEO;
                        break;
                    case 6:
                        viewType = CircleItemType.UGC_PIC;
                        break;
                }
                return viewType;
            }
        });

        getMultiTypeDelegate()
                .registerItemType(CircleItemType.NEWS_NOPIC, R.layout.circle_news_nopic_recycler_item)
                .registerItemType(CircleItemType.NEWS_1PIC, R.layout.circle_news_1pic_recycler_item)
                .registerItemType(CircleItemType.NEWS_3PIC, R.layout.circle_news_3pic_recycler_item)
                .registerItemType(CircleItemType.LIVE, R.layout.circle_ugc_live_recycler_item)
                .registerItemType(CircleItemType.COFFEE_VIDEO, R.layout.circle_ugc_coffee_video_recycler_item)
                .registerItemType(CircleItemType.STANDARD_VIDEO, R.layout.circle_standard_video_recycler_item)
                .registerItemType(CircleItemType.UGC_PIC, R.layout.circle_ugc_pic_recycler_item);
    }

    @Override
    protected void convert(BaseViewHolder holder, TestBean bean) {
        switch (holder.getItemViewType()) {
            case CircleItemType.NEWS_NOPIC:
//                ((CircleNewsNoPicLayout) holder.getView(R.id.circle_item_news_nopic)).setData(bean);
//                setAaFightView(holder, fightBean);
                break;
            case CircleItemType.NEWS_1PIC:
//                setHalfFightView(holder, fightBean);
                break;
            case CircleItemType.NEWS_3PIC:
//                setAaBallView(holder, fightBean);
                break;
            case CircleItemType.LIVE:
//                setAaBallView(holder, fightBean);
                break;
            case CircleItemType.COFFEE_VIDEO:
//                setAaBallView(holder, fightBean);
                break;

            case CircleItemType.STANDARD_VIDEO:
//                setAaBallView(holder, fightBean);
                break;
            case CircleItemType.UGC_PIC:
//                setAaBallView(holder, fightBean);
                break;
        }
    }
}
