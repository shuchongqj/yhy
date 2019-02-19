package com.quanyan.yhy.ui.tab.view.hometab;

import android.view.ViewGroup;

/**
 * Created with Android Studio.
 * Title:HomeViewInterface
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/7/6
 * Time:14:06
 * Version 1.1.0
 */
public interface HomeViewInterface<T> {

    /**
     * 向父布局中添加View
     *
     * @param parentView 父布局（HomeFragment中的ScrollView的第一个子View）
     * @param index 要添加的子view的位置
     */
    void setView(ViewGroup parentView, int index);

    /**
     * 分发处理数据，将网络获取的数据逐一下发给各个view，在对应对应的View中处理自己需要的数据
     * @param data
     */
    void handleData(T data);
}
