package com.newyhy.activity;

import android.content.pm.ActivityInfo;
import android.view.View;
import android.widget.FrameLayout;

import com.newyhy.views.YhyListVideoView;
import com.quanyan.yhy.R;
import com.tencent.rtmp.TXVodPlayer;
import com.yhy.common.base.BaseNewActivity;

/**
 * Created by Nandy on 2018/7/7
 */
public abstract class VideoSupportActivity extends BaseNewActivity {
    private YhyListVideoView videoView;
    public boolean isFullScreen;
    private FrameLayout full_screen_container;
    //player
    public static TXVodPlayer player;

    public static TXVodPlayer getPlayer() {
        return player;
    }

    @Override
    protected void initView() {
        super.initView();
        full_screen_container = findViewById(R.id.full_screen_container);
        player = new TXVodPlayer(this);
    }

    public void fullScreen(String videoUrl, String videoPicUrl, String title,long id) {
        videoView = findViewById(R.id.fullscreen_video);
        videoView.setUrlAndPlayer(videoUrl, videoPicUrl, title,id);
        videoView.setFullScreenActivity(this);
        videoView.setFullScreen(true);
        VideoPlayer.getInstance().attach(videoView);
        full_screen_container.setVisibility(View.VISIBLE);
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        mImmersionBar.fitsSystemWindows(false).statusBarColor(R.color.Black).statusBarDarkFont(true).init();
    }

    public void exitFullScreen() {
//        videoView.resetAllState();
        isFullScreen = false;
        VideoPlayer.getInstance().deattach(videoView);
        VideoPlayer.getInstance().reAttach();
        full_screen_container.setVisibility(View.GONE);
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mImmersionBar.fitsSystemWindows(true).statusBarColor(R.color.White).statusBarDarkFont(true).init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.setVodListener(null);
            player.setPlayListener(null);
            player.pause();
            player.setPlayerView(null);
            new Thread() {
                @Override
                public void run() {
                    player.stopPlay(true);
                    player = null;
                }
            }.start();
        }
    }
}
