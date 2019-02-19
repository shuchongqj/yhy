package com.quanyan.base.yminterface;

import android.view.View;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/4/28
 * Time:10:53
 * Version 1.0
 */
public interface IBaseView {
    /**
     * 加载主布局文件
     *
     * @return 布局文件 {@link View}
     */
    View onLoadContentView();

    /**
     * 设置导航栏的布局
     *
     * @return 导航栏的 {@link View}
     */
    View onLoadNavView();

    /**
     * 设置导航栏是否覆盖内容布局
     * @return true : 代表遮盖内容布局，false : 不遮盖内容布局
     */
    boolean isTopCover();

    /**
     * 是否使用4.4  API 19 以上的沉浸式样式
     * @return 默认为true
     */
    boolean isUseImmersiveStyle();
}
