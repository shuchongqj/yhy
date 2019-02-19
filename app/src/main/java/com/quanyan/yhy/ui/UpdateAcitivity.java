package com.quanyan.yhy.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;

import com.quanyan.yhy.R;
import com.quanyan.yhy.appupgrade.DownloadService;
import com.quanyan.yhy.appupgrade.DownloadServiceConnection;
import com.quanyan.yhy.appupgrade.DownloadTask;
import com.quanyan.yhy.appupgrade.OnDownloadListener;
import com.quanyan.yhy.common.DirConstants;
import com.quanyan.yhy.ui.views.numberprogressbar.RocketProgressBar;
import com.yhy.common.base.YHYBaseApplication;
import com.yhy.common.beans.net.model.rc.OnlineUpgrade;
import com.yhy.common.constants.IntentConstant;

import java.io.File;
import java.util.ArrayList;

/**
 * Created with Android Studio.
 * Title:KickoutAcitivity
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/5/3
 * Time:16:46
 * Version 1.0
 */
public class UpdateAcitivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.ll_custom_dialog)
    View mDialogView;
//    @ViewInject(R.id.msg_dlg_title)
//    TextView mDialogTitle;
    @ViewInject(R.id.msg_dlg_content)
    TextView mDialogContent;
    @ViewInject(R.id.msg_dialog_btn_cancel)
    ImageView mDialogCancel;
    @ViewInject(R.id.msg_dialog_btn_ok)
    TextView mDialogUpdate;

    @ViewInject(R.id.rl_download_view)
    View mDownloadView;
//    @ViewInject(R.id.title)
//    TextView mDownloadTitle;
//    @ViewInject(R.id.content)
//    TextView mDownloadCotent;
    @ViewInject(R.id.cancel)
    TextView mDownloadCancel;
    @ViewInject(R.id.number_progress_bar)
    RocketProgressBar mDownloadProgress;

    OnlineUpgrade onlineUpgrade;
    private String mApkName = "quanyanhd.apk";
    private DownloadService downloadService;
    private DownloadServiceConnection connection = new DownloadServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            super.onServiceConnected(name, service);
            downloadService = getService();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        /*WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(layoutParams);*/
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_update, null);
    }

    @Override
    public View onLoadNavView() {
        return null;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        setTitleBarBackground(Color.TRANSPARENT);
        initView();
        bindService(new Intent(this, DownloadService.class), connection, Context.BIND_AUTO_CREATE);
    }

    private void initView() {
        onlineUpgrade = (OnlineUpgrade) getIntent().getSerializableExtra(IntentConstant.EXTRA_ONLINE_UPGRADE);
        mDownloadView.setVisibility(View.GONE);
//        mDialogTitle.setText(R.string.dlg_title_upgrade);
//        mDownloadTitle.setText(R.string.dlg_title_upgrade);
        if (onlineUpgrade.forceUpgrade) {
            mDialogContent.setText(onlineUpgrade.forceDesc);
            mDialogUpdate.setText(R.string.force_update);
//            mDownloadCotent.setText(onlineUpgrade.forceDesc);
            mDialogCancel.setVisibility(View.GONE);
        } else {
            mDialogContent.setText(onlineUpgrade.desc);
//            mDownloadCotent.setText(onlineUpgrade.desc);
            mDialogUpdate.setText(R.string.update_now);
//            mDialogCancel.setText(R.string.update_next_time);
        }

        mDialogCancel.setOnClickListener(this);
        mDialogUpdate.setOnClickListener(this);
        mDownloadCancel.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.msg_dialog_btn_cancel) {
            this.finish();
        } else if (id == R.id.msg_dialog_btn_ok) {
            if (downloadService == null) return;

            DownloadTask task = new DownloadTask();
            task.url = onlineUpgrade.downloadUrl;
            task.savePath = DirConstants.DIR_UPDATE_APP + mApkName;
            task.listener = mDownloadListener;
            connection.getService().download(task);

        } else if (id == R.id.cancel) {
            downloadService.stopDownload(onlineUpgrade.downloadUrl);
            if (onlineUpgrade.forceUpgrade) {
                exitAllActivity();
            } else {
                finish();
            }
        }
    }

    OnDownloadListener mDownloadListener = new OnDownloadListener() {
        @Override
        public void onDownloadStart(int maxLenth, String downloadUrl) {
            mDialogView.setVisibility(View.GONE);
            mDownloadView.setVisibility(View.VISIBLE);
            mDownloadProgress.setMax(maxLenth);
        }

        @Override
        public void onProgress(int percent) {
            mDownloadProgress.setProgress(percent);
        }

        @Override
        public void onDownloadFinish(DownloadTask task) {
            install(UpdateAcitivity.this, task.savePath);
            if (onlineUpgrade.forceUpgrade) {
                exitAllActivity();
            } else {
                finish();
            }
        }

        @Override
        public void onDownloadFailed(int errCode) {
            if (onlineUpgrade.forceUpgrade) {
                exitAllActivity();
            } else {
                finish();
            }
        }

        @Override
        public void onDownloadStop(DownloadTask task) {
        }
    };

    private void install(Context context, String savePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(savePath)), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public void exitAllActivity() {
        ArrayList<Activity> activityList = YHYBaseApplication.getActivityList();
        if (activityList == null || activityList.isEmpty()) {
            android.os.Process.killProcess(android.os.Process.myPid());
        } else {
            for (int i = activityList.size() - 1; i >= 0; i--) {
                activityList.get(i).finish();
            }
            YHYBaseApplication.getActivityList().clear();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

}
