package com.quanyan.yhy.net.lsn;


public interface OnResponseListener<T> extends OnAbstractListener {
    void onComplete(boolean isOK, T result, int errorCode, String errorMsg);
}

