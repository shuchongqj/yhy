package com.quanyan.yhy.libanalysis;

import com.yhy.common.base.ModuleApplication;
import com.yhy.common.base.YHYBaseApplication;

import java.util.HashMap;


public class AnalysisModuleApplication implements ModuleApplication {
    @Override
    public void onCreate(YHYBaseApplication application) {

        // 友盟KEY
        String UMENG_APP_KEY;
        // TalkingData App_Id
        String TD_APP_ID;
        if (application.getYhyEnvironment().isOnline()) {
            // 正式环境
            UMENG_APP_KEY = "5a7b0018a40fa318a10000fa";
            TD_APP_ID = "0878429DF04D47CD897149BB419BE348";
        } else { // 测试环境
            UMENG_APP_KEY = "5ae18844a40fa36e0e000078";
            TD_APP_ID = "E157F2D3141C444585B504769619C409";
        }

        //事件统计
        HashMap<String,String> map = AnArgs.Instance().build(Analysis.UMENG_KEY, UMENG_APP_KEY)
                .build(Analysis.TC_APP_ID, TD_APP_ID)
                .getMap();
        Analysis.initAnalysis(application, application.getYhyEnvironment().getChannel(), map);

    }
}
