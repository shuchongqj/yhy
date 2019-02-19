package com.yhy.router;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.yhy.common.base.ModuleApplication;
import com.yhy.common.base.YHYBaseApplication;

public class RouterApplication implements ModuleApplication {
    @Override
    public void onCreate(YHYBaseApplication application) {
        ARouter.openLog();     // 打印日志
        ARouter.openDebug();
        ARouter.init(application);
    }
}
