package com.yhy.sport;


import com.yhy.common.base.ModuleApplication;
import com.yhy.common.base.YHYBaseApplication;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class SportModuleApplication implements ModuleApplication {

    @Override
    public void onCreate(YHYBaseApplication application) {
        Realm.init(application);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("StepData.realm")
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(config);
    }
}
