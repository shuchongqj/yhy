package com.yhy.common.beans.net.model;

import com.yhy.common.beans.net.model.common.Booth;
import com.yhy.common.beans.net.model.msg.LiveRecordAPIPageResult;
import com.yhy.common.beans.net.model.trip.ShortItemsResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:TalentHomeData
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/7/28
 * Time:下午8:21
 * Version 1.1.0
 */
public class TalentHomeData implements Serializable{
    private static final long serialVersionUID = 4323200980239812883L;
    //达人故事
    public Booth mTalenStory;
    //咨询达人
    public ShortItemsResult mConsultingGoods;
    //线路达人
    public ShortItemsResult mMasterLineGoods;
    //包装页1
    public Booth mRecommandBooth1 ;
    //包装页2
    public Booth mRecommandBooth2 ;
    //咨询人数
    public int mConsultedCount;
    //线路感兴趣人数
    public int mLineReadCount;
    //直播列表
    public LiveRecordAPIPageResult mLiveRecordAPIPageResult;

    /**
     * 反序列化函数，用于从json字符串反序列化本类型实例
     */
    public static TalentHomeData deserialize(String json) throws JSONException {
        if (json != null && !json.isEmpty()) {
            return deserialize(new JSONObject(json));
        }
        return null;
    }

    /**
     * 反序列化函数，用于从json节点对象反序列化本类型实例
     */
    public static TalentHomeData deserialize(JSONObject json) throws JSONException {
        if (json != null && json != JSONObject.NULL && json.length() > 0) {
            TalentHomeData result = new TalentHomeData();

            result.mTalenStory = Booth.deserialize(json.optJSONObject("mTalenStory"));

            result.mRecommandBooth1 = Booth.deserialize(json.optJSONObject("mRecommandBooth1"));

            result.mRecommandBooth2 = Booth.deserialize(json.optJSONObject("mRecommandBooth2"));

            result.mConsultingGoods = ShortItemsResult.deserialize(json.optJSONObject("mConsultingGoods"));

            result.mMasterLineGoods = ShortItemsResult.deserialize(json.optJSONObject("mMasterLineGoods"));

            result.mConsultedCount = json.optInt("mConsultedCount");

            result.mLineReadCount = json.optInt("mLineReadCount");

            result.mLiveRecordAPIPageResult = LiveRecordAPIPageResult.deserialize(json.optJSONObject("mLiveRecordAPIPageResult"));

            return result;
        }
        return null;
    }

    /*
     * 序列化函数，用于从对象生成数据字典
     */
    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();

        if (this.mTalenStory != null) { json.put("mTalenStory", this.mTalenStory.serialize()); }

        if (this.mRecommandBooth1 != null) { json.put("mRecommandBooth1", this.mRecommandBooth1.serialize()); }

        if (this.mRecommandBooth2 != null) { json.put("mRecommandBooth2", this.mRecommandBooth2.serialize()); }

        if (this.mConsultingGoods != null) { json.put("mConsultingGoods", this.mConsultingGoods.serialize()); }

        if (this.mMasterLineGoods != null) { json.put("mExperienceGoods", this.mMasterLineGoods.serialize()); }

        json.put("mConsultedCount", this.mConsultedCount);

        json.put("mLineReadCount", this.mLineReadCount);

        if (this.mLiveRecordAPIPageResult != null) { json.put("mLiveRecordAPIPageResult", this.mLiveRecordAPIPageResult.serialize()); }

        return json;
    }
}
