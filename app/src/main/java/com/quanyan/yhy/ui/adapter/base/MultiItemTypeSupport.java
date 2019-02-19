package com.quanyan.yhy.ui.adapter.base;

/**
 * Created with Android Studio.
 * Title:MultiItemTypeSupport
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:2016/10/11
 * Time:11:22
 * Version 1.1.0
 */
public interface MultiItemTypeSupport<T> {
    int getLayoutId(int position , T t);

    int getViewTypeCount();

    int getItemViewType(int postion,T t );
}
