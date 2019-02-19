package com.quanyan.yhy.ui.line.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.customview.NoScrollGridView;
import com.quanyan.base.view.customview.imgpager.ImgPagerView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.master.helper.MasterViewHelper;
import com.yhy.common.beans.net.model.DefaultCityBean;
import com.yhy.common.beans.net.model.RCShowcase;
import com.yhy.common.beans.net.model.common.BoothList;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/3/24
 * Time:11:28
 * Version 1.0
 */
public class LineHeaderView extends LinearLayout {

    private Context mContext;
    private LinearLayout mFreePackHotCityLayout;
    private NoScrollGridView mNoScrollGridView;
    private RecommendView mRecommandDestView;
    private NoScrollGridView mFunRecommandView;
    private QuickAdapter<RCShowcase> mDestAdapter;
    private String mPageTitle;
    private String mPageType;
    private ImgPagerView mImgPagerView;
    private QuickAdapter<RCShowcase> mFreePackTopAdapter;
    private QuickAdapter<RCShowcase> mTopicsAdapter;

    public LineHeaderView(Context context) {
        super(context);
        init(context);
    }

    public LineHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LineHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LineHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(final Context context) {
        mContext = context;
        setOrientation(VERTICAL);
        setBackgroundColor(getResources().getColor(R.color.neu_f4f4f4));
        setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        View.inflate(context, R.layout.header_line_activity, this);
        mFreePackHotCityLayout = (LinearLayout) findViewById(R.id.header_line_activity_hot_city);
        mNoScrollGridView = (NoScrollGridView) findViewById(R.id.noscroll_gridview);
        mRecommandDestView = (RecommendView) findViewById(R.id.view_fun_recommand);

        mFunRecommandView = (NoScrollGridView) findViewById(R.id.nsg_des_list);
        mRecommandDestView.setRecommendTitle(context.getString(R.string.label_fun_line_recommand));

        //运营预留位置
        mImgPagerView = (ImgPagerView) findViewById(R.id.ac_line_home_banner);

        initAdapter();

        mFunRecommandView.setAdapter(mDestAdapter);

        setListener();
    }

    private void initAdapter() {
        mFreePackTopAdapter = new QuickAdapter<RCShowcase>(mContext, R.layout.item_grid_free_pack_topic, new ArrayList<RCShowcase>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, RCShowcase item) {
                helper.setImageUrl(R.id.item_grid_topic_free_pack_img, item.imgUrl, 0, 0, R.mipmap.icon_default_215_150)
                        .setText(R.id.item_grid_topic_free_pack_btn, item.title);
            }
        };

        mTopicsAdapter = new QuickAdapter<RCShowcase>(mContext, R.layout.item_grid_topic, new ArrayList<RCShowcase>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, RCShowcase item) {
                int imageW = ScreenSize.convertDIP2PX(getContext().getApplicationContext(), 40);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) helper.getView(R.id.item_grid_topic_img).getLayoutParams();
                lp.width = imageW;
                lp.height = imageW;
                helper.getView(R.id.item_grid_topic_img).setLayoutParams(lp);
                helper.setImageUrlRound(R.id.item_grid_topic_img, item.imgUrl, 100, 100, R.mipmap.icon_default_128_128)
                        .setText(R.id.item_grid_topic_title, item.title);
            }
        };

        mDestAdapter = new QuickAdapter<RCShowcase>(mContext, R.layout.item_base_hot_search_history, new ArrayList<RCShowcase>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, RCShowcase item) {
                if (context instanceof Activity) {
                    MasterViewHelper.handleMasterDestItem((Activity) context, helper, item);
                }
            }
        };
    }

    private void setListener() {
        mFunRecommandView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: 16/4/13 当地好去处
                if (StringUtil.isEmpty(((RCShowcase) mFunRecommandView.getAdapter().getItem(position)).operationContent)) {
                    return;
                }
                NavUtils.gotoLineSearchResultActivity(mContext,
                        mPageTitle,
                        mPageType,
                        null,
                        ((RCShowcase) mFunRecommandView.getAdapter().getItem(position)).operationContent,
                        ((RCShowcase) mFunRecommandView.getAdapter().getItem(position)).title,
                        null,
                        -1);
                if (ItemType.ARROUND_FUN.equals(mPageType)) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(AnalyDataValue.KEY_CITY_CODE, ((RCShowcase) mFunRecommandView.getAdapter().getItem(position)).operationContent);
                    map.put(AnalyDataValue.KEY_CITY_NAME, ((RCShowcase) mFunRecommandView.getAdapter().getItem(position)).title);
                    TCEventHelper.onEvent(getContext(), AnalyDataValue.TC_ID_PERIPHERALPLAY_PERIPHERY, map);
                }
            }
        });

        mNoScrollGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: 16/4/13 主题， 热门城市
                if (StringUtil.isEmpty(((RCShowcase) mNoScrollGridView.getAdapter().getItem(position)).operationContent)) {
                    return;
                }
                if (ItemType.TOUR_LINE.equals(mPageType) || ItemType.FREE_LINE.equals(mPageType) ||
                        ItemType.TOUR_LINE_ABOARD.equals(mPageType) || ItemType.FREE_LINE_ABOARD.equals(mPageType)) {
                    NavUtils.gotoLineSearchResultActivity(mContext,
                            mPageTitle,
                            mPageType,
                            null,
                            ((RCShowcase) mNoScrollGridView.getAdapter().getItem(position)).operationContent,
                            ((RCShowcase) mNoScrollGridView.getAdapter().getItem(position)).title,
                            null,
                            -1);
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(AnalyDataValue.KEY_CITY_CODE, ((RCShowcase) mNoScrollGridView.getAdapter().getItem(position)).operationContent);
                    map.put(AnalyDataValue.KEY_CITY_NAME, ((RCShowcase) mNoScrollGridView.getAdapter().getItem(position)).title);
                    TCEventHelper.onEvent(getContext(), AnalyDataValue.TC_ID_GROUP_HOT_CITY, map);
                } else if (ItemType.ARROUND_FUN.equals(mPageType)) {
                    String cityName = SPUtils.getArroundCityName(getContext().getApplicationContext());
                    NavUtils.gotoLineSearchResultActivity(mContext,
                            mPageTitle,
                            mPageType,
                            null,
                            TextUtils.isEmpty(cityName) ? DefaultCityBean.cityCode :
                                    SPUtils.getArroundCityCode(getContext().getApplicationContext()),
                            TextUtils.isEmpty(cityName) ? DefaultCityBean.cityName : cityName,
                            ((RCShowcase) mNoScrollGridView.getAdapter().getItem(position)).title,
                            Long.parseLong(((RCShowcase) mNoScrollGridView.getAdapter().getItem(position)).operationContent)
                    );
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(AnalyDataValue.KEY_TAGID, ((RCShowcase) mNoScrollGridView.getAdapter().getItem(position)).operationContent);
                    map.put(AnalyDataValue.KEY_TAGNAME, ((RCShowcase) mNoScrollGridView.getAdapter().getItem(position)).title);
                    TCEventHelper.onEvent(getContext(), AnalyDataValue.TC_ID_PERIPHERALPLAY_THEME, map);

                } else if (ItemType.CITY_ACTIVITY.equals(mPageType)) {
                    String cityName = SPUtils.getLocalCityName(getContext().getApplicationContext());
                    NavUtils.gotoLineSearchResultActivity(mContext,
                            mPageTitle,
                            mPageType,
                            null,
                            TextUtils.isEmpty(cityName) ? DefaultCityBean.cityCode :
                                    SPUtils.getLocalCityCode(getContext().getApplicationContext()),
                            TextUtils.isEmpty(cityName) ? DefaultCityBean.cityName : cityName,
                            ((RCShowcase) mNoScrollGridView.getAdapter().getItem(position)).title,
                            Long.parseLong(((RCShowcase) mNoScrollGridView.getAdapter().getItem(position)).operationContent)
                    );
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(AnalyDataValue.KEY_TAGID, ((RCShowcase) mNoScrollGridView.getAdapter().getItem(position)).operationContent);
                    map.put(AnalyDataValue.KEY_TAGNAME, ((RCShowcase) mNoScrollGridView.getAdapter().getItem(position)).title);
                    TCEventHelper.onEvent(getContext(), AnalyDataValue.TC_ID_PERIPHERALPLAY_THEME, map);
                }
            }
        });
    }

    /**
     * 设置轮播图是否自动播放
     *
     * @param scroll
     */
    public void startBannerHandle(boolean scroll) {
        if (scroll) {
            mImgPagerView.startAutoScroll();
        } else {
            mImgPagerView.stopAutoScroll();
        }
    }

    public void setPageTitle(String pageTitle) {
        mPageTitle = pageTitle;
    }

    public void setPageType(String pageType) {
        mPageType = pageType;
        RecommendView recommendView = (RecommendView) findViewById(R.id.recommend_view_layout);
        if (ItemType.TOUR_LINE.equals(mPageType) || ItemType.FREE_LINE.equals(mPageType) ||
                ItemType.TOUR_LINE_ABOARD.equals(mPageType) || ItemType.FREE_LINE_ABOARD.equals(mPageType)) {
            recommendView.setRecommendTitle(R.string.label_freewalk_packagetour_recommedn);
            int padd_lr = ScreenSize.convertDIP2PX(mContext.getApplicationContext(), 10);
            int width = (ScreenSize.getScreenWidth(getContext().getApplicationContext()) - 2 * padd_lr - 10) / 2;
            mNoScrollGridView.setNumColumns(2);
            mNoScrollGridView.setColumnWidth(width);
            mNoScrollGridView.setPadding(padd_lr, 0, padd_lr, 0);
            mNoScrollGridView.setAdapter(mFreePackTopAdapter);
            mFreePackHotCityLayout.setVisibility(View.VISIBLE);
        } else if (ItemType.ARROUND_FUN.equals(mPageType)) {
            recommendView.setRecommendTitle(R.string.label_arround_recommend);
            int padd_tb = ScreenSize.convertDIP2PX(mContext.getApplicationContext(), 10);
            int width = (ScreenSize.getScreenWidth(getContext().getApplicationContext()) - 2 * padd_tb - 30) / 4;
            mNoScrollGridView.setNumColumns(4);
            mNoScrollGridView.setColumnWidth(width);
            mNoScrollGridView.setPadding(padd_tb, 0, padd_tb, 0);
            mNoScrollGridView.setAdapter(mTopicsAdapter);
            mFreePackHotCityLayout.setVisibility(View.GONE);
        } else if (ItemType.CITY_ACTIVITY.equals(mPageType)) {
            recommendView.setRecommendTitle(R.string.label_citylocal_recommend);
            int padd_tb = ScreenSize.convertDIP2PX(mContext.getApplicationContext(), 10);
            int width = (ScreenSize.getScreenWidth(getContext().getApplicationContext()) - 2 * padd_tb - 30) / 4;
            mNoScrollGridView.setNumColumns(4);
            mNoScrollGridView.setColumnWidth(width);
            mNoScrollGridView.setPadding(padd_tb, 0, padd_tb, 0);
            mNoScrollGridView.setAdapter(mTopicsAdapter);
            mFreePackHotCityLayout.setVisibility(View.GONE);
        } else {
            recommendView.setRecommendTitle(R.string.label_recommend);
        }
    }

    /**
     * 跟团游资源位
     *
     * @param list
     */
    public void handlePackageTourResouceList(BoothList list) {
        if (list == null || list.value == null || list.value.size() == 0) {
            return;
        }
        if (list.value.size() > 0 && list.value.get(0).showcases != null) {
            mImgPagerView.setBannerList(list.value.get(0).showcases);
            mImgPagerView.startAutoScroll();
        }

        if (list.value.size() > 1 && list.value.get(1).showcases != null) {
//            if(!TextUtils.isEmpty(list.value.get(1).name)) {
//            ((TextView)findViewById(R.id.line_header_view_hot_city_text)).setText(list.value.get(1).name);
//            }
            mFreePackTopAdapter.replaceAll(list.value.get(1).showcases);
        }

    }

    /**
     * 自由行资源位
     *
     * @param list
     */
    public void handleFreeWalkResouceList(BoothList list) {
        if (list == null || list.value == null || list.value.size() == 0) {
            return;
        }
        if (list.value.size() > 0 && list.value.get(0).showcases != null) {
            mImgPagerView.setBannerList(list.value.get(0).showcases);
            mImgPagerView.startAutoScroll();
        }

        if (list.value.size() > 1 && list.value.get(1).showcases != null) {
//            if(!TextUtils.isEmpty(list.value.get(1).name)) {
//            ((TextView)findViewById(R.id.line_header_view_hot_city_text)).setText(list.value.get(1).name);
//            }
            mFreePackTopAdapter.replaceAll(list.value.get(1).showcases);
        }
    }

    /**
     * 同城活动资源位
     *
     * @param list
     */
    public void handleCityActivityResouceList(BoothList list) {
        if (list == null || list.value == null || list.value.size() == 0) {
            return;
        }
        if (list.value.size() > 0 && list.value.get(0).showcases != null) {
            mImgPagerView.setBannerList(list.value.get(0).showcases);
            mImgPagerView.startAutoScroll();
        }

        if (list.value.size() > 1 && list.value.get(1).showcases != null) {
            mTopicsAdapter.replaceAll(list.value.get(1).showcases);
        }
    }

    /**
     * 周边玩乐资源位
     *
     * @param list
     */
    public void handleArroundFunResouceList(BoothList list) {
        if (list == null || list.value == null || list.value.size() == 0) {
            return;
        }
        if (list.value.size() > 0 && list.value.get(0).showcases != null) {
            mImgPagerView.setBannerList(list.value.get(0).showcases);
            mImgPagerView.startAutoScroll();
        }

        if (list.value.size() > 1 && list.value.get(1).showcases != null) {
            mTopicsAdapter.replaceAll(list.value.get(1).showcases);
        }

        if (list.value.size() > 2 && list.value.get(2).showcases != null) {
//            if(!TextUtils.isEmpty(list.value.get(2).name)) {
//                mRecommandDestView.setRecommendTitle(list.value.get(2).name);
//            }
            mRecommandDestView.setVisibility(View.VISIBLE);
            mFunRecommandView.setVisibility(View.VISIBLE);
            mDestAdapter.replaceAll(list.value.get(2).showcases);
        }
    }
}
