package com.quanyan.yhy.ui.line.lineinterface;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/3/30
 * Time:19:04
 * Version 1.0
 */
public interface DropMenuInterface<T> {
    void onFirstItemSelect(T data);
    void onSecondItemSelect(T data);
}
