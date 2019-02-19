package com.quanyan.yhy;

import com.mogujie.tt.ui.activity.ChatAcitivity;
import com.newyhy.activity.HomeMainTabActivity;
import com.newyhy.activity.NewAdActivity;
import com.newyhy.activity.SplashActivity;
import com.quanyan.pedometer.PedoActivity;
import com.quanyan.yhy.ui.common.WebViewActivity;
import com.quanyan.yhy.ui.discovery.AddLiveAcitivty;
import com.quanyan.yhy.ui.guide.GuideActivity;
import com.quanyan.yhy.ui.home.AboutYiMayActivity;
import com.quanyan.yhy.ui.master.activity.MasterAdviceListActivity;
import com.quanyan.yhy.ui.zxing.CaptureActivity;

public class UsedActivity {


    static {
        Class[] activities = new Class[]{

                SplashActivity.class,
                HomeMainTabActivity.class,
                WebViewActivity.class,
                ChatAcitivity.class,
                NewAdActivity.class,
                GuideActivity.class,
                CaptureActivity.class,
                ChatAcitivity.class,
                AddLiveAcitivty.class,
                MasterAdviceListActivity.class,//健康咨询
                PedoActivity.class,//计步器
                AboutYiMayActivity.class,//关于

        };
    }



}
