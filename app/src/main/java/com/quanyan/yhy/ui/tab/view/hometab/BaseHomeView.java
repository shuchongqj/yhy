package com.quanyan.yhy.ui.tab.view.hometab;

/**
 * Created with Android Studio.
 * Title:BaseHomeView
 * Description:
 * <p>主界面中和Activity或者Fragment有关的方法关联</p>
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/7/6
 * Time:14:55
 * Version 1.1.0
 */
public class BaseHomeView {

    /**
     * 主界面中view滑动过程中将滑动距离传递给子view
     * @param scrollY 滑动的距离
     */
    public void deliverViewScrollY(int scrollY){}

    /**
     * 主机面onResume状态的传递
     */
    public void onResume(){}

    /**
     * 主界面onPause状态的传递
     */
    public void onPause(){}

    public void onStop(){}

    public void onDestroy(){}

    public void stopImgPagerScroll(){}

    public void startImgPagerScroll(){}

}
