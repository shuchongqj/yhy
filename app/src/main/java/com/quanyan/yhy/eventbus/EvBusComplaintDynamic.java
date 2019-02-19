package com.quanyan.yhy.eventbus;


import com.yhy.common.beans.net.model.discover.UgcInfoResult;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:直播消息对象
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:2/14/16
 * Time:10:47
 * Version 1.0
 */
public class EvBusComplaintDynamic {
    private UgcInfoResult mSubjectInfo;
    public EvBusComplaintDynamic(UgcInfoResult subjectInfo){
        mSubjectInfo = subjectInfo;
    }

    public UgcInfoResult getSubjectInfo() {
        return mSubjectInfo;
    }
}
