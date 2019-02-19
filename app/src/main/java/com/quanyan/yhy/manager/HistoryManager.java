package com.quanyan.yhy.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.quanyan.yhy.net.cache.LocalJsonCache;
import com.yhy.common.beans.net.model.search.MasterSearchHistoryList;
import com.yhy.common.constants.ValueConstants;

/**
 * Created with Android Studio.
 * Title:HistoryManager
 * Description:
 * Copyright:Copyright (c) 2016
 * Author:徐学东
 * Date:16/3/8
 * Time:下午7:35
 * Version 1.0
 */
public class HistoryManager {
    //线路
    public static final String LINES = "LINES";
    //达人搜索
    public static final String MASTER_SEARCH = "MASTER_SEARCH";
    //达人商品搜索
    public static final String MASTER_SEARCH_LINES = "MASTER_SEARCH_LINES";
    //必买推荐
    public static final String MASTER_MUST_BUY = "MASTER_MUST_BUY";
    //酒店
    public static final String HOTEL = "HOTEL";
    //景区
    public static final String SPOTS = "SPOTS";
    //话题
    public static final String TOPIC = "topics";
    public static final String SERVICE = "SERVICE";
    LocalJsonCache<MasterSearchHistoryList> mMasterSearchCache;
    private Context mContext;
    private Handler mUIHandler;
    private String mTag ;
    public HistoryManager(Context context,String tag ,Handler handler) {
        mContext = context;
        mUIHandler = handler;
        mTag = tag;
        mMasterSearchCache = new LocalJsonCache<>(mContext);
        mMasterSearchCache.setCallback(new LocalJsonCache.Callback<MasterSearchHistoryList>() {
            @Override
            public void onSave(String path) {
            }

            @Override
            public void onParse(String path, MasterSearchHistoryList t) {
                if (t != null) {
                    Message.obtain(mUIHandler, ValueConstants.MSG_MASTER_SEARCH_HISTORY_OK, 0, ValueConstants.MSG_DATA_FROM_CACHE, t).sendToTarget();
                }else{
                    Message.obtain(mUIHandler, ValueConstants.MSG_MASTER_SEARCH_HISTORY_OK, 0, ValueConstants.MSG_DATA_FROM_CACHE, new MasterSearchHistoryList()).sendToTarget();
                }
            }
        });
    }

    /**
     * 保存搜索历史
     * @param data
     */
    public void saveMasterSearchHistory(MasterSearchHistoryList data) {
        if(mTag == null){
            return ;
        }
        mMasterSearchCache.save(mTag, data);
    }

    /**
     * 加载搜索历史
     */
    public void loadMasterSearchHistory(){
        if(mTag == null){
            return ;
        }
        mMasterSearchCache.load(mTag, MasterSearchHistoryList.class);
    }
}
