package com.quanyan.yhy.net;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.quanyan.yhy.net.cache.LocalJsonCache;
import com.quanyan.yhy.ui.circles.CirclesController;
import com.quanyan.yhy.ui.discovery.controller.DiscoverController;
import com.yhy.common.beans.net.model.AppHomeData;
import com.yhy.common.beans.net.model.TalentHomeData;
import com.yhy.common.beans.net.model.discover.TopicInfoResultList;
import com.yhy.common.beans.net.model.discover.TravelSpecialInfoList;
import com.yhy.common.beans.net.model.discover.UgcInfoResultList;
import com.yhy.common.constants.ValueConstants;

/**
 * Created with Android Studio.
 * Title:CacheManager
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:和平必胜、正义必胜、人民必胜
 * Author:炎黄子孙
 * Date:15/11/26
 * Time:下午7:48
 * Version 1.0
 */
public class CacheManager {
    //APP首页缓存
    public static final String HOME_DATA_CACHE = "HOME_DATA_CACHE";
    //达人首页
    public static final String TALENT_HOME_DATA_CACHE = "TALENT_HOME_DATA_CACHE";
    //话题列表
    public static final String TOPIC_LIST_DATA_CACHE = "TOPIC_LIST_DATA_CACHE";
    //直播列表
    public static final String LIVE_LIST_DATA_CACHE = "LIVE_LIST_DATA_CACHE";
    //游记列表
    public static final String TRAVEL_NOTES_LIST_DATA_CACHE = "TRAVEL_NOTES_LIST_DATA_CACHE";

    //APP首页
    LocalJsonCache<AppHomeData> mHomePageContentLocalJsonCache;
    //达人首页
    LocalJsonCache<TalentHomeData> mMasterHomePageContentLocalJsonCache;
    //话题列表
    LocalJsonCache<TopicInfoResultList> mTopicContentLocalJsonCache;
    //直播列表
    LocalJsonCache<UgcInfoResultList> liveListLocalJsonCache;
    //游记列表
    LocalJsonCache<TravelSpecialInfoList> travelNotesLocalJsonCache;

    private Context mContext;
    private Handler mUIHandler;
    public CacheManager(Context context,Handler handler) {
        mContext = context;
        mUIHandler = handler;

        mHomePageContentLocalJsonCache = new LocalJsonCache<AppHomeData>(mContext);
        mHomePageContentLocalJsonCache.setCallback(new LocalJsonCache.Callback<AppHomeData>() {
            @Override
            public void onSave(String path) {
            }

            @Override
            public void onParse(String path, AppHomeData t) {
                if (t != null) {
                    Message.obtain(mUIHandler,ValueConstants.MSG_GET_FIRST_PAGE_LIST_OK, 0,
                            ValueConstants.MSG_DATA_FROM_CACHE, t).sendToTarget();
                }
            }
        });

        mMasterHomePageContentLocalJsonCache = new LocalJsonCache<TalentHomeData>(mContext);
        mMasterHomePageContentLocalJsonCache.setCallback(new LocalJsonCache.Callback<TalentHomeData>() {
            @Override
            public void onSave(String path) {
            }

            @Override
            public void onParse(String path, TalentHomeData t) {
                if (t != null) {
                    Message.obtain(mUIHandler, ValueConstants.MSG_GET_SECOND_PAGE_LIST_OK, 0,
                            ValueConstants.MSG_DATA_FROM_CACHE, t).sendToTarget();
                }
            }
        });
        mTopicContentLocalJsonCache = new LocalJsonCache<TopicInfoResultList>(mContext);
        mTopicContentLocalJsonCache.setCallback(new LocalJsonCache.Callback<TopicInfoResultList>() {
            @Override
            public void onSave(String path) {
            }

            @Override
            public void onParse(String path, TopicInfoResultList t) {
                if (t != null) {
                    Message.obtain(mUIHandler, CirclesController.MSG_TOPIC_LIST_OK, 0,
                            ValueConstants.MSG_DATA_FROM_CACHE, t).sendToTarget();
                }
            }
        });

        //直播列表
        liveListLocalJsonCache = new LocalJsonCache<UgcInfoResultList>(mContext);
        liveListLocalJsonCache.setCallback(new LocalJsonCache.Callback<UgcInfoResultList>() {
            @Override
            public void onSave(String path) {
            }

            @Override
            public void onParse(String path, UgcInfoResultList t) {
                if (t != null) {
                    Message.obtain(mUIHandler, DiscoverController.MSG_LIVE_LIST_OK, 0,
                            ValueConstants.MSG_DATA_FROM_CACHE, t).sendToTarget();
                }
            }
        });
        //游记列表
        travelNotesLocalJsonCache = new LocalJsonCache<TravelSpecialInfoList>(mContext);
        travelNotesLocalJsonCache.setCallback(new LocalJsonCache.Callback<TravelSpecialInfoList>() {
            @Override
            public void onSave(String path) {
            }

            @Override
            public void onParse(String path, TravelSpecialInfoList t) {
                if (t != null) {
                    Message.obtain(mUIHandler, ValueConstants.MSG_TRAVELNOTES_LIST_OK, 0,
                            ValueConstants.MSG_DATA_FROM_CACHE, t).sendToTarget();
                }
            }
        });
    }
    //缓存首页
    public void saveMainPageData(AppHomeData data) {
        mHomePageContentLocalJsonCache.save(HOME_DATA_CACHE, data);
    }

    //保存达人首页内容
    public void saveMasterHomePageData(TalentHomeData data) {
        mMasterHomePageContentLocalJsonCache.save(TALENT_HOME_DATA_CACHE, data);
    }

    //保存话题列表
    public void saveTopicListData(TopicInfoResultList data) {
        mTopicContentLocalJsonCache.save(TOPIC_LIST_DATA_CACHE, data);
    }

    //保存直播列表
    public void saveLiveListData(UgcInfoResultList data) {
        liveListLocalJsonCache.save(LIVE_LIST_DATA_CACHE, data);
    }

    //保存游记列表
    public void saveTravelNotesData(TravelSpecialInfoList data) {
        travelNotesLocalJsonCache.save(TRAVEL_NOTES_LIST_DATA_CACHE, data);
    }

    /**
     * 首页
     */
    public void loadHomeCache(){
        mHomePageContentLocalJsonCache.load(HOME_DATA_CACHE, AppHomeData.class);
    }

    /**
     * 加载达人首页
     */
    public void loadMasterHomeCache(){
        mMasterHomePageContentLocalJsonCache.load(TALENT_HOME_DATA_CACHE, TalentHomeData.class);
    }


    //加载话题列表
    public void loadTopicListCache(){
        mTopicContentLocalJsonCache.load(TOPIC_LIST_DATA_CACHE, TopicInfoResultList.class);
    }

    //加载直播列表
    public void loadLiveListCache(){
        liveListLocalJsonCache.load(LIVE_LIST_DATA_CACHE, UgcInfoResultList.class);
    }

    //加载游记列表
    public void loadTravelNotesCache(){
        travelNotesLocalJsonCache.load(TRAVEL_NOTES_LIST_DATA_CACHE, TravelSpecialInfoList.class);
    }
}
