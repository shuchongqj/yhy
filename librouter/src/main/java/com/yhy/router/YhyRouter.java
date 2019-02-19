package com.yhy.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;
import com.yhy.common.beans.net.model.NativeBean;
import com.yhy.common.utils.SPUtils;
import com.yhy.router.types.MainActivityFromType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class YhyRouter {
    private static final YhyRouter ourInstance = new YhyRouter();

    public static YhyRouter getInstance() {
        return ourInstance;
    }

    private YhyRouter() {
    }

    public void inject(Object o){
        ARouter.getInstance().inject(o);
    }

    /**
     * 启动APP主界面
     *
     * @param context
     * @param from    从哪里启动的主activity
     */
    public void startMainActivity(Context context, MainActivityFromType from) {
        Postcard postcard = ARouter.getInstance()
                .build(RouterPath.ACTIVITY_MAIN)
                .withSerializable(SPUtils.EXTRA_TYPE, from);
        if (context instanceof Activity) {
            postcard.navigation(context);
        } else {
            postcard.withFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation(context);
        }

    }

    /**
     * 启动APP广告
     *
     * @param context
     */
    public void startAdActivity(Context context) {
        ARouter.getInstance()
                .build(RouterPath.ACTIVITY_AD)
                .navigation(context);
    }


    /**
     * 启动登陆界面
     *
     * @param context
     * @param nativeBean 导航的信息
     * @param flags      activity的flags
     * @param resultCode startActivityForResult的resultCode 当context为activity时可以设置resultCode，反则无效
     */
    public void startLoginActivity(Context context, NativeBean nativeBean, int flags, int resultCode) {
        Postcard postcard = ARouter.getInstance()
                .build(RouterPath.ACTIVITY_LOGIN)
                .withFlags(flags);
        if (nativeBean != null) {
            postcard.withSerializable(SPUtils.EXTRA_DATA, nativeBean);
        }
        if (context instanceof Activity) {
            postcard.navigation((Activity) context, resultCode);
        } else {
            postcard.navigation(context);
        }
    }

    /**
     * 启动个人主页
     *
     * @param context
     * @param mTalentId
     * @param fromCousult
     */
    public void startHomePageActivity(Context context, long mTalentId, boolean fromCousult) {
        ARouter.getInstance()
                .build(RouterPath.ACTIVITY_HOMEPAGE)
                .withLong(SPUtils.EXTRA_ID, mTalentId)
                .withBoolean("fromCousult", fromCousult)
                .navigation(context);
    }

    /**
     * 圈子动态详情页
     *
     * @param context
     * @param subjectId
     * @param isCommentClick
     */
    public void startCircleDetailActivity(Context context, long subjectId, boolean isCommentClick) {
        ARouter.getInstance()
                .build(RouterPath.ACTIVITY_CIRCLE_DETAIL)
                .withLong(SPUtils.EXTRA_ID, subjectId)
                .withBoolean("isCommentClick", isCommentClick)
                .navigation(context);
    }

    /**
     * 选择城市页
     *
     * @param context
     */
    public void startCitySelectActivity(Context context, boolean isFromWeb , HashMap<String, String> extraMap) {
        ARouter.getInstance()
                .build(RouterPath.ACTIVITY_CITY_SELECT)
                .withBoolean("isFromWeb", isFromWeb)
                .withObject("extraMap", extraMap)
                .navigation(context);
    }

    /**
     * 创建圈子的主fragment
     *
     * @return
     */
    public Fragment makeCircleFragment() {
        return (Fragment) ARouter.getInstance()
                .build(RouterPath.FRAGMENT_CIRCLE)
                .navigation();
    }


    /**
     * 圈子的话题列表
     *
     * @return
     */
    public void startHotTopicListActivity(Context context) {
        ARouter.getInstance()
                .build(RouterPath.ACTIVITY_TOPIC_LIST)
                .navigation(context);
    }

    /**
     * 圈子 搜索界面
     *
     * @return
     */
    public void startCircleSearchActivity(Context context) {
        ARouter.getInstance()
                .build(RouterPath.ACTIVITY_CIRCLE_SEARCH)
                .navigation(context);
    }

    /**
     * 圈子 推荐(动态标签页) fragment
     *
     * @return
     */
    public Fragment makeCircleRecommendFragment(Context context, long tagId, HashMap<String, String> extraMap) {
        return (Fragment) ARouter.getInstance()
                .build(RouterPath.FRAGMENT_CIRCLE_RECOMMEND)
                .withLong("tagId", tagId)
                .withObject("extraMap", extraMap)
                .navigation(context);
    }

    /**
     * 圈子 小视频 fragment
     *
     * @return
     */
    public Fragment makeCircleCoffeeVideoFragment(Context context, HashMap<String, String> extraMap) {
        return (Fragment) ARouter.getInstance()
                .build(RouterPath.FRAGMENT_CIRCLE_COFFEEVIDEO)
                .withObject("extraMap", extraMap)
                .navigation(context);
    }

    /**
     * 圈子 动态 fragment
     *
     * @return
     */
    public Fragment makeCircleDynamicFragment(Context context, HashMap<String, String> extraMap) {
        return (Fragment) ARouter.getInstance()
                .build(RouterPath.FRAGMENT_CIRCLE_DYNAMIC)
                .withObject("extraMap", extraMap)
                .navigation(context);
    }

    /**
     * 圈子 关注 fragment
     *
     * @return
     */
    public Fragment makeCircleFollowFragment(Context context, HashMap<String, String> extraMap) {
        return (Fragment) ARouter.getInstance()
                .build(RouterPath.FRAGMENT_CIRCLE_FOLLOW)
                .withObject("extraMap", extraMap)
                .navigation(context);
    }

    /**
     * 圈子 直播 fragment
     *
     * @return
     */
    public Fragment makeCircleLiveFragment(Context context, HashMap<String, String> extraMap) {
        return (Fragment) ARouter.getInstance()
                .build(RouterPath.FRAGMENT_CIRCLE_LIVE)
                .withObject("extraMap", extraMap)
                .navigation(context);
    }

    /**
     * 圈子 视频 fragment
     *
     * @return
     */
    public Fragment makeCircleStandardVideoFragment(Context context, HashMap<String, String> extraMap) {
        return (Fragment) ARouter.getInstance()
                .build(RouterPath.FRAGMENT_CIRCLE_STANDARDVIDEO)
                .withObject("extraMap", extraMap)
                .navigation(context);
    }

    /**
     * 精彩视频列表
     *
     * @return
     */
    public void startWonderfulVideoListActivity(Context context) {
        ARouter.getInstance()
                .build(RouterPath.ACTIVITY_VIDEO_LIST)
                .navigation(context);
    }

    /**
     * 直播列表
     *
     * @return
     */
    public void startLiveListActivity(Context context) {
        ARouter.getInstance()
                .build(RouterPath.ACTIVITY_LIVE_LIST)
                .navigation(context);
    }
}
