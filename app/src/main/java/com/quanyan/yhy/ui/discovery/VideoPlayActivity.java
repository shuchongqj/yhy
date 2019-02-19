package com.quanyan.yhy.ui.discovery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.harwkin.nb.camera.FileUtil;
import com.harwkin.nb.camera.ImageUtils;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.HarwkinLogUtil;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.DirConstants;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.umeng.socialize.utils.Log;
import com.yhy.common.beans.net.model.VideoInfo;
import com.yhy.common.utils.SPUtils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created with Android Studio.
 * Title:VideoGuideActivity
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:和平必胜、正义必胜、人民必胜
 * Author:炎黄子孙
 * Date:15/11/25
 * Time:下午6:51
 * Version 1.0
 */
public class VideoPlayActivity extends BaseActivity {
    private VideoView mVideoView;
    private ImageView mPlayIv;

    private boolean isPaused = false;
    private VideoInfo mVideoInfo;
    //进度条
    private static final int MSG_SHOW_CONTROL_BAR = 0x1001;
    //下载成功
    private static final int MSG_DOWNLOAD_OK = 0x1002;
    //下载失败
    private static final int MSG_DOWNLOAD_FAILED = 0x1003;
    //下载进度
    private static final int MSG_DOWNLOADING_PROGRESS = 0x1004;
    //时间延迟
    private static final int TIME_DELAY = 2000;
    //是否正在播放
    private boolean isPlaying = true;

    /**
     * 跳转视频播放页
     *
     * @param context
     */
    public static void gotoVideoPlayerActivity(Context context, VideoInfo videoInfo) {
        Intent intent = new Intent(context, VideoPlayActivity.class);
        if (videoInfo != null) {
            intent.putExtra(SPUtils.EXTRA_DATA, videoInfo);
        }
        context.startActivity(intent);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_SHOW_CONTROL_BAR:
                if (mPlayIv != null) {
                    mPlayIv.setVisibility(View.GONE);
                }
                break;
            case MSG_DOWNLOAD_OK:
                prepareVideoPlay(DirConstants.DIR_VIDEOS_CACHE + FileUtil.getFileName(ImageUtils.getImageFullUrl(mVideoInfo.videoUrl)));
                break;
            case MSG_DOWNLOAD_FAILED:
                ToastUtil.showToast(VideoPlayActivity.this, getString(R.string.label_toast_video_download_failed));
                finish();
                break;
            case MSG_DOWNLOADING_PROGRESS:
                if ((int) msg.obj == 100) {
                    hideLoadingView();
                } else {
                    showLoadingView((int) msg.obj + "%", R.color.white);
                }
                break;
        }
    }

    @Override
    public void onCreate(Bundle arg0) {
        mVideoInfo = (VideoInfo) getIntent().getSerializableExtra(SPUtils.EXTRA_DATA);

        if (mVideoInfo == null) {
            ToastUtil.showToast(this, getString(R.string.label_toast_video_uri_invalid));
            finish();
            return;
        }
        super.onCreate(arg0);
    }

    @Override
    protected void onDestroy() {
        if (mVideoView != null) {
            mVideoView.stopPlayback();
        }
        if (call != null) {
            call.cancel();
        }
        super.onDestroy();
    }

    @Override
    public void finish() {
        hideLoadingView();
        super.finish();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitleBarBackground(Color.BLACK);

        mVideoView = (VideoView) findViewById(R.id.vv);
        mPlayIv = (ImageView) findViewById(R.id.iv_play);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
        mVideoView.setLayoutParams(layoutParams);
        String path = mVideoInfo.videoLocalPath;


        if (StringUtil.isEmpty(path)) {
            path = ImageUtils.getImageFullUrl(mVideoInfo.videoUrl);
            prepareDownloadVideo(path);
            showLoadingView("");
            Log.i("tag", path);
        } else {
            Log.i("tag", path);
            prepareVideoPlay(path);
            showLoadingView("");
        }

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });

        mVideoView.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        hideLoadingView();
                    }
                });

        mVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mVideoView == null) {
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mHandler.removeMessages(MSG_SHOW_CONTROL_BAR);
                    if (isPlaying) {
                        // TODO: 2018/3/8    现在修改成了直接关闭 
//                        isPlaying = false;
//                        mPlayIv.setVisibility(View.VISIBLE);
//                        mPlayIv.setImageResource(R.mipmap.ic_video_play);
//                        mVideoView.pause();
//                        mHandler.sendEmptyMessageDelayed(MSG_SHOW_CONTROL_BAR, TIME_DELAY);
                        finish();
                    } else {
                        isPlaying = true;
                        mPlayIv.setVisibility(View.GONE);
                        mVideoView.start();
                    }
                }
                return true;
            }
        });
    }

    /**
     * 开始播放
     *
     * @param path
     */
    private void prepareVideoPlay(String path) {
        if (!StringUtil.isEmpty(path)) {
            mVideoView.setVideoPath("file://" + path);
        } else {
            ToastUtil.showToast(this, getString(R.string.label_toast_video_uri_invalid));
            finish();
            return;
        }

        mVideoView.requestFocus();
        mVideoView.start();
    }

    private Call call;

    /**
     * 准备播放网络视频
     *
     * @param path
     */
    private void prepareDownloadVideo(String path) {
        final String videoCachePath = DirConstants.DIR_VIDEOS_CACHE + FileUtil.getFileName(path);
        if (new File(videoCachePath).exists()) {
            prepareVideoPlay(videoCachePath);
        } else {
            if (LocalUtils.isAlertMaxStorage()) {
                ToastUtil.showToast(this, getString(R.string.label_toast_sdcard_unavailable));
                finish();
                return;
            }
            OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build();
            Request request = null;
            try {
                request = new Request.Builder().url(new URL(path)).build();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(MSG_DOWNLOAD_FAILED);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    long fileSize = response.body().contentLength();
                    HarwkinLogUtil.info(response.message() + "\n" + ",fileSize = " + fileSize);
                    InputStream ins = response.body().byteStream();
                    DataInputStream dis = new DataInputStream(ins);
                    try {
                        byte[] buffer = new byte[32768];
                        long hasRead = 0;
                        int length;
                        FileUtil.mkdirs(DirConstants.DIR_VIDEOS);
                        FileUtil.mkdirs(DirConstants.DIR_VIDEOS_CACHE);
                        File file = new File(videoCachePath);
                        if (!file.exists()) {
                            file.createNewFile();
                            FileOutputStream fos = new FileOutputStream(file);
                            while ((length = dis.read(buffer)) > 0) {
                                fos.write(buffer, 0, length);
                                hasRead = hasRead + length;
                                if (fileSize > 0) {
                                    int percent = (int) (hasRead * 100.0f / fileSize);
                                    Message.obtain(mHandler, MSG_DOWNLOADING_PROGRESS, percent).sendToTarget();
                                }
                            }
                            ins.close();
                            dis.close();
                            fos.close();
                            mHandler.sendEmptyMessage(MSG_DOWNLOAD_OK);
                        }
                    } catch (Exception e) {
                        if (ins != null) {
                            ins.close();
                        }
                        if (dis != null) {
                            dis.close();
                        }
                        FileUtil.deleteFile(new File(videoCachePath));
                        mHandler.sendEmptyMessage(MSG_DOWNLOAD_FAILED);
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    protected void onPause() {
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
            isPaused = true;
        }
        mHandler.removeMessages(MSG_SHOW_CONTROL_BAR);
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (isPaused) {
            isPaused = false;
            mVideoView.start();
        }
        super.onResume();
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_video_player, null);
    }

    @Override
    public View onLoadNavView() {
        return null;
    }

    @Override
    public boolean isTopCover() {
        return true;
    }
}
