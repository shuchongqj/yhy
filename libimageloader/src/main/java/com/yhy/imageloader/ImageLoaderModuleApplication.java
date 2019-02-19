package com.yhy.imageloader;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.yhy.common.base.ModuleApplication;
import com.yhy.common.base.YHYBaseApplication;

/**
 * Created by yangboxue on 2018/7/3.
 */

public class ImageLoaderModuleApplication implements ModuleApplication {

    @Override
    public void onCreate(YHYBaseApplication application) {
        try {
            ApplicationInfo applicationInfo = application.getPackageManager().getApplicationInfo(application.getPackageName(), PackageManager.GET_META_DATA);
            ImageLoadManager.setImgUrl(applicationInfo.metaData.get("IMAGE_URL").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
