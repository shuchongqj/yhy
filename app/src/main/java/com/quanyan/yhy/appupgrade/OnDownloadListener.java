package com.quanyan.yhy.appupgrade;

/**
 * Created by sky on 14-3-27.
 */
/** 下载监听 */
public interface OnDownloadListener {
    public void onDownloadStart(int maxLenth,String downloadUrl);
    public void onProgress(int percent);
    public void onDownloadFinish(DownloadTask task);
    public void onDownloadFailed(int errCode);
    void onDownloadStop(DownloadTask task);
}
