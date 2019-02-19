package com.quanyan.yhy.appupgrade;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

import com.harwkin.nb.camera.FileUtil;
import com.quanyan.yhy.common.DirConstants;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class DownloadService extends Service {

    public static final String ACTION_DOWNLOAD = "action_download";

    public static final String ACTION_STOP_DOWNLOAD = "action_stop_download";

    public static final String ACTION_PAUSE_DOWNLOAD = "action_pause_download";

    public static final String ACTION_DOWNLOAD_FINISH = "com.pajk.hm.action_download_finish";

    public static final String ACTION_DOWNLOAD_FAILED = "com.pajk.hm.action_download_failed";
    //下载开始
    public static final String ACTION_DOWNLOAD_START = "com.pajk.hm.action_download_start";
    public static final String ACTION_DOWNLOAD_PROGRESS = "com.pajk.hm.action_download_progress";

    public static final String ACTION_DOWNLOAD_PUBLISH_PROGRESS = "com.pajk.hm.action_download_progress";

    public static final String DOWNLOAD_PROGRESS = "download_progress";

    public static final String DOWNLOAD_URL = "download_url";

    public static final String SAVE_PATH = "save_path";

    public static final String SAVE_IN_EXTERNAL = "save_in_external";

    public static final String SHOW_NOTIFICATION = "show_notification";

    public static final String DOWNLOAD_PROGRESS_NOTIFICATION = "download_progress_notification";

    public static final String DOWNLOAD_FINISH_NOTIFICATION = "download_finish_notification";

    public static final String DOWNLOAD_FAIL_NOTIFICATION = "download_fail_notification";

    public static final String PROGRESSBAR_ID = "progress_bar";

    public static final String TEXTVIEW_ID = "textview_id";

    private static final int SUCCESS = 1;

    private static final int ERROR_NETWORK = -2;

    private static final int ERROR_IO = -3;


    private DownloadServiceBinder binder = new DownloadServiceBinder();

    private ArrayList<DownloadWorker> mWorkerQueue;

//    private NotificationManager mNotificationManager;

    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mWorkerQueue = new ArrayList<DownloadWorker>();
//        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (intent == null) {
//            return -1;
//        }
//        String action = intent.getAction();
//        String downloadUrl = intent.getStringExtra(DOWNLOAD_URL);
//
//        if (action != null && action.equals(ACTION_DOWNLOAD)) {
//            String savePath = intent.getStringExtra(SAVE_PATH);
//            boolean saveInExternal = intent.getBooleanExtra(SAVE_IN_EXTERNAL, true);
//            boolean showNotification = intent.getBooleanExtra(SHOW_NOTIFICATION, false);
//
//            DownloadTask task = new DownloadTask();
//            task.url = downloadUrl;
//            task.savePath = savePath;
//            task.saveInExternal = saveInExternal;
//            if (showNotification) {
//                Notification progressNotification = (Notification) intent.getParcelableExtra(DOWNLOAD_PROGRESS_NOTIFICATION);
//                Notification finishNotification = (Notification) intent.getParcelableExtra(DOWNLOAD_FINISH_NOTIFICATION);
//                Notification failNotification = (Notification) intent.getParcelableExtra(DOWNLOAD_FAIL_NOTIFICATION);
//                task.progressNotification = progressNotification;
//                task.finishNotification = finishNotification;
//                task.failNotification = failNotification;
//                task.showNotification = showNotification;
//
//                int progressBarId = intent.getIntExtra(PROGRESSBAR_ID, 0);
//                int textViewId = intent.getIntExtra(TEXTVIEW_ID, 0);
//                if (progressBarId != 0 && textViewId != 0) {
//                    task.progressBarId = progressBarId;
//                    task.textViewId = textViewId;
//                } else {
//                    task.showNotification = false;
//                }
//            }
//            download(task);
//        } else if (action != null && action.equals(ACTION_STOP_DOWNLOAD)) {
//            stopDownload(downloadUrl);
//        } else if (action != null && action.equals(ACTION_STOP_DOWNLOAD)) {
//            pauseDownload(downloadUrl);
//        }
        return super.onStartCommand(intent, flags, startId);
    }

    public class DownloadServiceBinder extends Binder {
        public DownloadService getService() {
            return DownloadService.this;
        }
    }

    public void download(DownloadTask task) {
        DownloadWorker worker = new DownloadWorker(task);
        worker.execute();
        mWorkerQueue.add(worker);
    }

    public void stopDownload(String url) {
        for (DownloadWorker worker : mWorkerQueue) {
            if (worker.task.url.equals(url)) {
                worker.stop();
                mWorkerQueue.remove(worker);
                return;
            }
        }
    }

    public void pauseDownload(String url) {
        for (DownloadWorker worker : mWorkerQueue) {
            if (worker.task.url.equals(url)) {
                worker.stop();
                return;
            }
        }
    }

    private void sendBroadcast(String action) {
//        Intent intent = new Intent(action);
//        sendBroadcast(intent);
        Intent startIntent = new Intent();
        startIntent.putExtra("pakname", getPackageName());
        startIntent.setAction(action);
        startIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(startIntent);
    }

    private class DownloadWorker extends AsyncTask<String, Integer, Integer> {

        public boolean stop = false;

        public boolean downloading = false;

        public DownloadTask task;

        private int lastPercent = 0;

        public DownloadWorker(DownloadTask task) {
            this.task = task;
        }

        public void stop() {
            this.stop = true;
            this.downloading = false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (task.listener != null) {
                task.listener.onDownloadStart(100,task.url);
            }
        }

        @Override
        protected Integer doInBackground(String... params) {
            downloading = true;
            URL url;
            BufferedInputStream inputStream = null;
            FileOutputStream outputStream = null;
            try {
                url = new URL(task.url);
                URLConnection conn = url.openConnection();
                conn.setConnectTimeout(10000);
                inputStream = new BufferedInputStream(conn.getInputStream());
                if (task.saveInExternal) {
                    FileUtil.mkdirs(DirConstants.DIR_UPDATE_APP);
                    FileUtil.createFile(task.savePath);
                    outputStream = new FileOutputStream(task.savePath);
                } else {
                    outputStream = DownloadService.this.openFileOutput(task.savePath,
                            Context.MODE_WORLD_READABLE);// 设为其他模式将没有权限安装
                }

                int fileSize = conn.getContentLength();
                byte[] buffer = new byte[32768];
                int readCount = 0;
                int current = 0;
                while ((current = inputStream.read(buffer)) > 0) {
                    if (stop) {
                        break;
                    }

                    outputStream.write(buffer, 0, current);
                    readCount += current;

                    int percent = (int) (readCount * 100.0f / fileSize);
                    this.publishProgress(percent);
                }
                outputStream.flush();
                inputStream.close();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                downloading = false;
                return ERROR_NETWORK;
            }
            return SUCCESS;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == SUCCESS) {
                if (task.listener != null) {
                    if(stop){
                        task.listener.onDownloadStop(task);
                    }else{
                        task.listener.onDownloadFinish(task);
                    }

                }
//                if (task.showNotification) {
//                    mNotificationManager.notify(0, task.finishNotification);
//                }
                //TODO Install Apk
//                if (task.saveInExternal) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.setDataAndType(Uri.fromFile(new File(task.savePath)), "application/vnd.android.package-archive");
//                    startActivity(intent);
//
//                    mNotificationManager.cancel(0);
//                }
            } else {
                if (task.listener != null) {
                    task.listener.onDownloadFailed(ERROR_IO);
                }
//                if (task.showNotification) {
//                    mNotificationManager.notify(0, task.failNotification);
//                }
            }
            downloading = false;
            mWorkerQueue.remove(this);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int percent = values[0];
            if (task.listener != null) {
                task.listener.onProgress(percent);
            } else {
//            	Intent intent = new Intent(ACTION_DOWNLOAD_PUBLISH_PROGRESS);
//                intent.putExtra(DOWNLOAD_PROGRESS, percent);
//                sendBroadcast(intent);
            }
//            if (task.showNotification && (percent - lastPercent > 5)) {
//                lastPercent = percent;
//                RemoteViews view = task.progressNotification.contentView;
//                view.setProgressBar(task.progressBarId, 100, percent, false);
//                view.setTextViewText(task.textViewId, percent + "%");
//                mNotificationManager.notify(0, task.progressNotification);
//            }
        }
    }
}
