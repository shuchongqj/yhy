package com.ymanalyseslibrary.alinterface;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:1/28/16
 * Time:09:30
 * Version 1.0
 */
public interface PostInterface {

    void onSuccess(int position, String postBody);

    void onFailed(int position, String postBody);
}
