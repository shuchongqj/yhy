package com.newyhy.activity;

import android.os.Bundle;

import com.newyhy.adapter.circle.StandardVideoAdapter;
import com.newyhy.utils.DateUtil;
import com.newyhy.views.YhyListVideoView;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXVodPlayer;
import com.yhy.common.base.YHYBaseApplication;

import static com.newyhy.activity.VideoPlayer.YhyState.YHY_END;
import static com.newyhy.activity.VideoPlayer.YhyState.YHY_EXCEPTION;
import static com.newyhy.activity.VideoPlayer.YhyState.YHY_LOAD;
import static com.newyhy.activity.VideoPlayer.YhyState.YHY_PAUSE;
import static com.newyhy.activity.VideoPlayer.YhyState.YHY_PLAYING;
import static com.newyhy.activity.VideoPlayer.YhyState.YHY_READY;
import static com.tencent.rtmp.TXLiveConstants.EVT_PLAY_DURATION;
import static com.tencent.rtmp.TXLiveConstants.EVT_PLAY_PROGRESS;
import static com.tencent.rtmp.TXLiveConstants.PLAY_ERR_NET_DISCONNECT;
import static com.tencent.rtmp.TXLiveConstants.PLAY_EVT_PLAY_BEGIN;
import static com.tencent.rtmp.TXLiveConstants.PLAY_EVT_PLAY_END;
import static com.tencent.rtmp.TXLiveConstants.PLAY_EVT_PLAY_LOADING;
import static com.tencent.rtmp.TXLiveConstants.PLAY_EVT_PLAY_PROGRESS;
import static com.tencent.rtmp.TXLiveConstants.PLAY_EVT_VOD_LOADING_END;

public class VideoPlayer {
    private static final VideoPlayer ourInstance = new VideoPlayer();

    public static VideoPlayer getInstance() {
        return ourInstance;
    }

    private VideoPlayer() {
    }


    private TXVodPlayer txVodPlayer;

    private int currentState = YHY_READY;

    private int position = -1;

    private int progress = -1;
    private int duration = -1;

    private YhyListVideoView yhyListVideoView;
    private StandardVideoAdapter standardVideoAdapter;

    public void init(StandardVideoAdapter standardVideoAdapter){
        this.standardVideoAdapter = standardVideoAdapter;
        txVodPlayer = new TXVodPlayer(YHYBaseApplication.getInstance());
        txVodPlayer.setVodListener(new ITXVodPlayListener() {
            @Override
            public void onPlayEvent(TXVodPlayer txVodPlayer, int i, Bundle bundle) {
                handleVodListener(txVodPlayer, i, bundle);
            }

            @Override
            public void onNetStatus(TXVodPlayer txVodPlayer, Bundle bundle) {

            }
        });
    }

    public void release(){
        position = -1;
        currentState = YHY_READY;
        progress = -1;
        duration = -1;
        standardVideoAdapter = null;
        final TXVodPlayer finalTxVodPlayer = txVodPlayer;
        if (finalTxVodPlayer != null){
            stop();
            deattach(null);
            new Thread(() -> finalTxVodPlayer.stopPlay(true)).start();
        }

    }

    public int getCurrentState() {
        return currentState;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void reAttach(){
        if (standardVideoAdapter != null && position != -1){
            standardVideoAdapter.notifyItemChanged(position);
        }
    }

    public void attach(YhyListVideoView yhyListVideoView){
        if (this.yhyListVideoView == yhyListVideoView){
            return;
        }
        if (this.yhyListVideoView != null){
            this.yhyListVideoView.onDeattach();
        }
        this.yhyListVideoView = yhyListVideoView;
        yhyListVideoView.setStateListener(new YhyListVideoView.PlayStateListener() {
            @Override
            public void onClickPlay(String url) {
                if (currentState == YHY_READY || currentState == YHY_END) {
                    currentState = YHY_LOAD;
                    if (txVodPlayer != null ) {
                        txVodPlayer.startPlay(url);
                    }
                }else if (currentState == YHY_PAUSE) {
                    currentState = YHY_LOAD;
                    if (txVodPlayer != null ) {
                        txVodPlayer.resume();
                    }
                }else if (currentState == YHY_PLAYING) {
                    currentState = YHY_PAUSE;
                    if (txVodPlayer != null ) {
                        txVodPlayer.pause();
                    }
                }
                if (yhyListVideoView != null){
                    yhyListVideoView.onStateChanged(currentState);
                }

            }

            @Override
            public void onSeek(int progress) {
                currentState = YHY_LOAD;
                if (txVodPlayer != null) {
                    txVodPlayer.seek(progress);
                }
            }

            @Override
            public void onTraceSeek(int offset) {

            }

            @Override
            public void onPlay() {

            }

            @Override
            public void onPause() {

            }

            @Override
            public void onEnd() {

            }
        });
        txVodPlayer.setPlayerView(yhyListVideoView.video_view);
        this.yhyListVideoView.onAttach();
        if (currentState == YHY_READY) {
            currentState = YHY_LOAD;
            if (txVodPlayer != null) {
                txVodPlayer.startPlay(yhyListVideoView.sourceUrl);
            }
        }

        this.yhyListVideoView.onStateChanged(currentState);
        this.yhyListVideoView.onProgress(progress, duration);

        if (currentState == YHY_PLAYING){
            this.yhyListVideoView.setActionContent(true);
            this.yhyListVideoView.postHideController(3);
        }
    }

    public void deattach(YhyListVideoView yhyListVideoView){
        if (this.yhyListVideoView == null){
            return;
        }
        if (yhyListVideoView == null || this.yhyListVideoView == yhyListVideoView){
            this.yhyListVideoView.onDeattach();
            this.yhyListVideoView = null;
        }
    }

    public void seek(int progress){
        if (txVodPlayer != null) {
            txVodPlayer.seek(progress);
        }
    }

    public void stop(){
        if (txVodPlayer != null) {
            txVodPlayer.pause();
            currentState = YHY_READY;
            if (this.yhyListVideoView != null){
                this.yhyListVideoView.onStateChanged(currentState);
            }
        }
    }

    public void pause(){
        if (txVodPlayer != null) {
            txVodPlayer.pause();
            currentState = YHY_PAUSE;
            if (this.yhyListVideoView != null){
                this.yhyListVideoView.onStateChanged(currentState);
            }
        }
    }


    public interface PlayerListener {

        void onAttach();

        void onDeattach();

        void onStateChanged(int state);

        void onProgress(int progress, int duration);
    }

    public class YhyState{
        public static final int YHY_READY = 801;
        public static final int YHY_PLAYING = 802;
        public static final int YHY_PAUSE = 803;
        public static final int YHY_LOAD = 804;
        public static final int YHY_END = 805;
        public static final int YHY_EXCEPTION = 800;
    }

    /**********************************************************************       logic method    *********************************************************************/
    public void handleVodListener(TXVodPlayer txVodPlayer, int event, Bundle bundle) {
        switch (event) {
            //1.播放事件
            case PLAY_EVT_PLAY_BEGIN://开始播放
                currentState = YHY_PLAYING;
                break;

            case PLAY_EVT_PLAY_PROGRESS://播放进度
                // 视频总长, 单位是秒
                duration = bundle.getInt(EVT_PLAY_DURATION);
                // 播放进度, 单位是秒
                progress = bundle.getInt(EVT_PLAY_PROGRESS);
                if (yhyListVideoView != null){
                    yhyListVideoView.onProgress(progress, duration);
                }
                break;

            case PLAY_EVT_PLAY_LOADING://播放加载
                currentState = YHY_LOAD;
                break;

            //2.结束事件
            case PLAY_EVT_PLAY_END://播放结束
                currentState = YHY_END;
                break;

            case PLAY_ERR_NET_DISCONNECT://播放重连
                currentState = YHY_LOAD;
                break;

            case PLAY_EVT_VOD_LOADING_END://加载完毕
                currentState = YHY_PLAYING;
                break;

            default://播放器的Event定制了很多事件，各种Event对应的页面逻辑不一致，不适合用default统一处理
                currentState = YHY_EXCEPTION;
                break;
        }
        if (yhyListVideoView != null && event != PLAY_EVT_PLAY_PROGRESS){
            yhyListVideoView.onStateChanged(currentState);
        }

    }

    public YhyListVideoView getYhyListVideoView() {
        return yhyListVideoView;
    }
}
