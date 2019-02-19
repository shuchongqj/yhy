package com.yhy.common.beans.net.model;

import com.yhy.common.beans.net.model.common.Booth;
import com.yhy.common.beans.net.model.rc.ArticleRecommendInfo;
import com.yhy.common.beans.net.model.trip.ShortItemsResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:AppHomeData
 * Description:App首页数据
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/7/28
 * Time:下午8:11
 * Version 1.1.0
 */
public class AppHomeData implements Serializable {
    private static final long serialVersionUID = 1488017503053013271L;
    //Banner
    public Booth mBanner;
    //球
    public Booth mBall;
    //头条
    public Booth mQuanYanHeader;
    //五个方块
    public Booth mFiveBlocks;
    //3个方块
    public Booth mThreeBlocks;
    //咨询达人
    public ShortItemsResult mConsultingGoods;
    //抢先体验
    public ShortItemsResult mExperienceGoods;
    //旅游资讯
    public ArticleRecommendInfo mArticles;
    //本周推荐
    public Booth mWeekRecommand;
    //推荐
    public Booth mQuanYanRecommand;
    //直播
    public Booth mQuanYanLive;
    //直播查看更多
    public Booth mQuanYanLiveMoreClick;
    //app首页咨询达人查看更多
    public Booth mMasterConsultMoreClick;
    //球下面的6个方块
    public Booth mSixBlocks;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static AppHomeData deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static AppHomeData deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            AppHomeData result = new AppHomeData();

            result.mBanner = Booth.deserialize(json.optJSONObject("mBanner"));

            result.mBall = Booth.deserialize(json.optJSONObject("mBall"));

            result.mQuanYanHeader = Booth.deserialize(json.optJSONObject("mQuanYanHeader"));

            result.mFiveBlocks = Booth.deserialize(json.optJSONObject("mFiveBlocks"));

            result.mThreeBlocks = Booth.deserialize(json.optJSONObject("mThreeBlocks"));

            result.mConsultingGoods = ShortItemsResult.deserialize(json.optJSONObject("mConsultingGoods"));

            result.mExperienceGoods = ShortItemsResult.deserialize(json.optJSONObject("mExperienceGoods"));

            result.mArticles = ArticleRecommendInfo.deserialize(json.optJSONObject("mArticles"));

            result.mWeekRecommand = Booth.deserialize(json.optJSONObject("mWeekRecommand"));

            result.mQuanYanRecommand = Booth.deserialize(json.optJSONObject("mQuanYanRecommand"));

            result.mQuanYanLive = Booth.deserialize(json.optJSONObject("mQuanYanLive"));

            result.mQuanYanLiveMoreClick = Booth.deserialize(json.optJSONObject("mQuanYanLiveMoreClick"));

            result.mMasterConsultMoreClick = Booth.deserialize(json.optJSONObject("mMasterConsultMoreClick"));

            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        if (this.mBanner != null) {
            json.put("mBanner", this.mBanner.serialize());
        }

        if (this.mBall != null) {
            json.put("mBall", this.mBall.serialize());
        }

        if (this.mQuanYanHeader != null) {
            json.put("mQuanYanHeader", this.mQuanYanHeader.serialize());
        }

        if (this.mFiveBlocks != null) {
            json.put("mFiveBlocks", this.mFiveBlocks.serialize());
        }

        if (this.mThreeBlocks != null) {
            json.put("mThreeBlocks", this.mThreeBlocks.serialize());
        }

        if (this.mConsultingGoods != null) {
            json.put("mConsultingGoods", this.mConsultingGoods.serialize());
        }

        if (this.mExperienceGoods != null) {
            json.put("mExperienceGoods", this.mExperienceGoods.serialize());
        }

        if (this.mArticles != null) {
            json.put("mArticles", this.mArticles.serialize());
        }

        if (this.mWeekRecommand != null) {
            json.put("mWeekRecommand", this.mWeekRecommand.serialize());
        }

        if (this.mQuanYanRecommand != null) {
            json.put("mQuanYanRecommand", this.mQuanYanRecommand.serialize());
        }

        if (this.mQuanYanLive != null) {
            json.put("mQuanYanLive", this.mQuanYanLive.serialize());
        }

        if (this.mQuanYanLiveMoreClick != null) {
            json.put("mQuanYanLiveMoreClick", this.mQuanYanLiveMoreClick.serialize());
        }
        if (this.mMasterConsultMoreClick != null) {
            json.put("mMasterConsultMoreClick", this.mMasterConsultMoreClick.serialize());
        }

        return json;
    }
}
