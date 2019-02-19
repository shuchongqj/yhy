package com.yhy.webview;

import android.net.Uri;
import android.webkit.ValueCallback;

/**
 * Created by Administrator on 2018-02-09.
 */

public interface OpenFileChooserCallBack {
    void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType);
    void showFileChooserCallBack(ValueCallback<Uri[]> filePathCallback);
}
