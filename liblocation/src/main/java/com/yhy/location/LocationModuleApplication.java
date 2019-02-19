package com.yhy.location;

import android.app.Application;

import com.yhy.common.base.ModuleApplication;
import com.yhy.common.base.YHYBaseApplication;

public class LocationModuleApplication implements ModuleApplication{

    @Override
    public void onCreate(YHYBaseApplication application) {
        LocationManager.getInstance().init(application);
        LocationManager.getInstance().startLocation(application);
    }
}
