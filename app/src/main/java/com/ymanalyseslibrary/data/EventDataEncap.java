package com.ymanalyseslibrary.data;


import com.yhy.common.utils.JSONUtils;

import java.util.Map;

/**
 * Created with Android Studio.
 * Title:EventDataEncap
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-4-29
 * Time:11:18
 * Version 1.0
 */

public class EventDataEncap {
    private Map<String, Object> mEventValues;

    public EventDataEncap() {
    }

    public EventDataEncap(Map<String, Object> mEventValues) {
        this.mEventValues = mEventValues;
    }

    public Map<String, Object> getmEventValues() {

        return mEventValues;
    }

    public void setmEventValues(Map<String, Object> mEventValues) {
        this.mEventValues = mEventValues;
    }

    @Override
    public String toString() {
        String jsonString = "";
        if(mEventValues != null){
            jsonString = JSONUtils.toJsonWithPretty(mEventValues);
        }

        return jsonString;
    }

}
