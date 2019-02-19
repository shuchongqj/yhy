package com.videolibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;
import com.newyhy.activity.NewPushStreamHorizontalActivity;
import com.newyhy.activity.NewPushStreamVerticalActivity;
import com.videolibrary.client.activity.LiveListActivity;
import com.videolibrary.puhser.activity.LiveCategoryActivity;
import com.videolibrary.puhser.activity.LiveNoticeActivity;
import com.videolibrary.puhser.activity.LiveRecordActivity;
import com.videolibrary.puhser.activity.LiveSettingsActivity;
import com.videolibrary.puhser.activity.PublishLiveActivity;
import com.yhy.common.beans.net.model.msg.LiveCategoryResult;
import com.yhy.router.RouterPath;

import java.util.ArrayList;

/**
 * Created with Android Studio.
 * Title:IntentUtil
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/8/3
 * Time:18:08
 * Version 1.1.0
 */
public class IntentUtil {

    public static final String BUNDLE_LIVEID = "bundle_live_id";
    public static final String BUNDLE_ANCHORID = "bundle_anchor_id";
    public static final String BUNDLE_CITYCODE = "live_item_cityCode";
    public static final String BUNDLE_TITLE = "title";
    public static final String BUNDLE_CITYNAME = "bundle_cityname";
    public static final String BUNDLE_RTMP_URL = "bundle_rtmp_url";
    public static final String BUNDLE_ROOM_ID = "BUNDLE_ROOM_ID";
    public static final String BUNDLE_CATEGORY_CODE = "BUNDLE_CATEGORY_CODE";
    public static final String BUNDLE_CATEGORY_NAME = "BUNDLE_CATEGORY_NAME";
    public static final String BUNDLE_NOTICE = "BUNDLE_NOTICE";
    public static final String BUNDLE_CATEGORY_RESULT = "BUNDLE_CATEGORY_RESULT";
    public static final String BUNDLE_USERID = "bundle_userid";
    public static final String BUNDLE_IS_LIVE = "bundle_is_live";
    public static final String BUNDLE_REPLAY_URLS = "bundle_replay_urls";
    public static final String BUNDLE_ONLINE_COUNT = "BUNDLE_ONLINE_COUNT";
    public static final String BUNDLE_CONFIG = "BUNDLE_CONFIG";
    public static final String BUNDLE_IS_NEED_BANNER = "bundle_is_need_banner";
    public static final String BUNDLE_LIVE_SCREEN_TYPE = "bundle_live_screen_type";
    public static final String BUNDLE_SCREEN_TYPE = "bundle_screen_type";
    public static final String BUNDLE_BATCH_ID = "bundle_batch_id";

    public static void startActivity(Context context, Class componentName, Bundle bundle) {
        Intent intent = new Intent(context, componentName);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    /**
     * 跳转我的直播记录页面
     *
     * @param context
     * @param userId
     */
    public static void startLiveRecordActivity(Context context, final long userId) {
        Bundle bundle = new Bundle();
        bundle.putLong(BUNDLE_USERID, userId);
        startActivity(context, LiveRecordActivity.class, bundle);
    }

    /**
     * 跳转发布直播页面
     *
     * @param context
     */
    public static void startPublishActivity(Context context) {
        startActivity(context, PublishLiveActivity.class, null);
    }

    /**
     * 发布直播成功后，跳转推流页面
     *
     * @param context
     * @param title
     * @param cityName
     * @param cityCode
     * @param batchId
     */
    public static void startPushStreamActivity(Context context, String title, String cityName, String cityCode, int sort, long roomId, int liveScreenType, String notice, long batchId) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        bundle.putString(BUNDLE_CITYNAME, cityName);
        bundle.putString(BUNDLE_CITYCODE, cityCode);
        bundle.putLong(BUNDLE_ROOM_ID, roomId);
        bundle.putInt(BUNDLE_CATEGORY_CODE, sort);
        bundle.putString(BUNDLE_NOTICE, notice);
        bundle.putInt(BUNDLE_LIVE_SCREEN_TYPE, liveScreenType);
        bundle.putLong(BUNDLE_BATCH_ID, batchId);
        startActivity(context, NewPushStreamVerticalActivity.class, bundle);
    }

    public static void startPushStreamHorizontalActivity(Context context, String title, String cityName, String cityCode, int sort, long roomId, int liveScreenType, String notice, long batchId) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        bundle.putString(BUNDLE_CITYNAME, cityName);
        bundle.putString(BUNDLE_CITYCODE, cityCode);
        bundle.putLong(BUNDLE_ROOM_ID, roomId);
        bundle.putInt(BUNDLE_CATEGORY_CODE, sort);
        bundle.putInt(BUNDLE_LIVE_SCREEN_TYPE, liveScreenType);
        bundle.putString(BUNDLE_NOTICE, notice);
        bundle.putLong(BUNDLE_BATCH_ID, batchId);
        startActivity(context, NewPushStreamHorizontalActivity.class, bundle);
    }

    /**
     * 直播客户端
     * @param context
     * @param roomId
     *
     */

    /**
     * 回放客户端
     */
    public static void startVideoClientActivity(final long liveId, final long anchorId,
                                                final boolean isLive, int videoScreenType) {
        String routerPath = "";
        switch (videoScreenType) {
            case 0://视频为横屏的
                if (isLive) {
                    routerPath = RouterPath.ACTIVITY_HORIZONTAL_LIVE;
                } else {
                    routerPath = RouterPath.ACTIVITY_HORIZONTAL_REPLAY;
                }
                break;
            case 1://视频为竖屏的
                if (isLive) {
                    routerPath = RouterPath.ACTIVITY_VERTICAL_LIVE;
                } else {
                    routerPath = RouterPath.ACTIVITY_VERTICAL_REPLAY;
                }
                break;
        }
        ARouter.getInstance().build(routerPath).withLong("liveId", liveId).withLong("anchorId", anchorId).
                navigation();
    }

    /**
     * 回放客户端
     */
    public static void startVideoClientActivity(final long liveId, final long anchorId,
                                                final boolean isLive, int videoScreenType,
                                                String videoUrl, long ugcId) {
        String routerPath = "";
        switch (videoScreenType) {
            case 0://视频为横屏的
                if (isLive) {
                    routerPath = RouterPath.ACTIVITY_HORIZONTAL_LIVE;
                } else {
                    routerPath = RouterPath.ACTIVITY_VERTICAL_REPLAY;
                }
                break;
            case 1://视频为竖屏的
                if (isLive) {
                    routerPath = RouterPath.ACTIVITY_VERTICAL_LIVE;
                } else {
                    routerPath = RouterPath.ACTIVITY_VERTICAL_REPLAY;
                }
                break;
        }
        ARouter.getInstance().build(routerPath).withLong("liveId", liveId).withLong("anchorId", anchorId).
                withString("videoUrl", videoUrl).withLong("ugcId", ugcId).navigation();
    }

    public static void startLiveSettingsActivity(Activity ac, String notice, String categoryName, long categoryCode, long roomId, ArrayList<LiveCategoryResult> list, int request_setting_code) {
        Intent intent = new Intent(ac, LiveSettingsActivity.class);
        intent.putExtra(BUNDLE_ROOM_ID, roomId);
        intent.putExtra(BUNDLE_NOTICE, notice);
        intent.putExtra(BUNDLE_CATEGORY_NAME, categoryName);
        intent.putExtra(BUNDLE_CATEGORY_CODE, categoryCode);
        intent.putExtra(BUNDLE_CATEGORY_RESULT, list);
        ac.startActivityForResult(intent, request_setting_code);
    }

    public static void startLiveNoticeActivity(Activity ac, String notice, int request_notice_code) {
        Intent intent = new Intent(ac, LiveNoticeActivity.class);
        intent.putExtra("notice", notice);
        ac.startActivityForResult(intent, request_notice_code);
    }

    /**
     * 直播列表页
     *
     * @param context
     * @param cityCode (0 -->> 火星,  -1 -->> 全部)
     */
    public static void startLiveListActivity(Context context, String title, long cityCode, boolean isNeedBanner) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        bundle.putLong(BUNDLE_CITYCODE, cityCode);
        bundle.putBoolean(BUNDLE_IS_NEED_BANNER, isNeedBanner);
        startActivity(context, LiveListActivity.class, bundle);
    }

    public static void startLiveCategoryActivity(Activity ac, long categoryCode, ArrayList<LiveCategoryResult> categoryResults, int reqeustCode) {
        Intent intent = new Intent(ac, LiveCategoryActivity.class);
        intent.putExtra(BUNDLE_CATEGORY_CODE, categoryCode);
        intent.putExtra(BUNDLE_CATEGORY_RESULT, categoryResults);
        ac.startActivityForResult(intent, reqeustCode);
    }


}
