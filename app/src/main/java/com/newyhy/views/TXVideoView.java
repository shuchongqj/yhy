package com.newyhy.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.newyhy.utils.DateUtil;
import com.newyhy.utils.live.AlphaAnimationUtils;
import com.quanyan.yhy.R;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.yhy.common.base.NoLeakHandler;

import static com.tencent.rtmp.TXLiveConstants.EVT_PLAY_DURATION;
import static com.tencent.rtmp.TXLiveConstants.EVT_PLAY_PROGRESS;
import static com.tencent.rtmp.TXLiveConstants.PLAY_ERR_NET_DISCONNECT;
import static com.tencent.rtmp.TXLiveConstants.PLAY_EVT_PLAY_BEGIN;
import static com.tencent.rtmp.TXLiveConstants.PLAY_EVT_PLAY_END;
import static com.tencent.rtmp.TXLiveConstants.PLAY_EVT_PLAY_LOADING;
import static com.tencent.rtmp.TXLiveConstants.PLAY_EVT_PLAY_PROGRESS;
import static com.tencent.rtmp.TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION;
import static com.tencent.rtmp.TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN;

public class TXVideoView extends RelativeLayout implements View.OnClickListener {

    public static final int TX_LIVE_PLAYER = 0x0011;
    public static final int TX_VOD_PLAYER = 0x0012;
    private TXCloudVideoView mVideoSurface;
    private ProgressBar mProgressBar;
    private TextView tv_load_failed;
    private Runnable runnable;

    private TXLivePlayer mLivePlayer;
    private TXVodPlayer mVodPlayer;
    private RelativeLayout player_function_bar;
    private LinearLayout player_progress_bar;
    private boolean playedFinish = false;
    private boolean autoRestart = false;

    public static final int HORIZONTAL_LIVE = 0x0013;
    public static final int HORIZONTAL_VOD = 0x0014;
    public static final int VERTICAL_LIVE = 0x0015;
    public static final int VERTICAL_VOD = 0x0016;

    /**
     * 当前播放模式
     */
    private int currentMode;

    private int currentStatus;
    public static final int Ready = 0x300;
    public static final int Playing = 0x301;
    public static final int Loading = 0x302;
    public static final int Pause = 0x303;
    public static final int End = 0x304;

    private TextView playing_time;
    private TextView end_time;
    private SeekBar seek_bar;
    private ImageView player_state_center,iv_full_screen;
    private RelativeLayout rl_controller;
    private String url;
    private boolean isShow = false;

    private boolean controllerShow = false;

    private TXPlayerStatusListener listener;
    private int uiStyle;
    private int startSeekTime = 0;

    public TXVideoView(Context context) {
        super(context);
        init();
    }

    public TXVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TXVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        runnable = new Runnable() {
            @Override
            public void run() {
                doVisibilityAnimation(false);
                isShow = !isShow;
                if (listener != null) {
                    listener.showController(false);
                }
            }
        };
        mVideoSurface = findViewById(R.id.tx_video_view);
        mProgressBar = findViewById(R.id.pb_video_view);
        tv_load_failed = findViewById(R.id.tv_load_failed);
        playing_time = findViewById(R.id.playing_time);
        end_time = findViewById(R.id.end_time);
        seek_bar = findViewById(R.id.seek_bar);
        player_state_center = findViewById(R.id.player_state_center);
        player_function_bar = findViewById(R.id.player_function_bar);
        iv_full_screen = findViewById(R.id.iv_full_screen);
        player_progress_bar = findViewById(R.id.player_progress_bar);

        player_state_center.setOnClickListener(this);
        rl_controller = findViewById(R.id.rl_controller);
        iv_full_screen.setOnClickListener(this);
        rl_controller.setOnClickListener(this);
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (TX_VOD_PLAYER == currentMode) {
                    mVodPlayer.pause();
                    startSeekTime = seekBar.getProgress();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (TX_VOD_PLAYER == currentMode) {
                    if (initResource) mVodPlayer.seek(seekBar.getProgress());
                    mVodPlayer.resume();
                    if (listener != null){
                        listener.onSeekBarTracking(seekBar.getProgress() - startSeekTime);
                    }
                }
            }
        });
    }

    /**
     * 设置底部进度条margin更多
     */
    public void setBottomMarginMore(){
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) player_function_bar.getLayoutParams();
        layoutParams.bottomMargin = (int) getResources().getDimension(R.dimen.yhy_size_40px);
        player_function_bar.setLayoutParams(layoutParams);
    }

    /**
     * 播放结束是否自动重播
     */
    public void setAutoRestart(boolean autoRestart){
        this.autoRestart = autoRestart;
    }

    /**
     * true 变大,false 变小
     * @param bigger
     */
    public void setPlayerButtonBigger(boolean bigger){
        RelativeLayout.LayoutParams params = (LayoutParams) player_state_center.getLayoutParams();
        params.width = getResources().getDimensionPixelSize(bigger?R.dimen.yhy_size_65px:R.dimen.yhy_size_45px);
        params.height = getResources().getDimensionPixelSize(bigger?R.dimen.yhy_size_65px:R.dimen.yhy_size_45px);
        player_state_center.setLayoutParams(params);
    }

    /**
     * 填充videoview
     */
    private void init(){
        inflate(getContext(), R.layout.layout_txvideoview,this);
    }

    /**
     * 设置解码器是直播还是点播
     * @param playerType
     */
    public void initPlayer(int playerType,int style,int fitMode) {
        switch (playerType){
            case TX_LIVE_PLAYER:
                //创建 player 对象
                mLivePlayer = new TXLivePlayer(getContext());
                //关键 player 对象与界面 view
                mLivePlayer.setPlayerView(mVideoSurface);
                //自动模式
                TXLivePlayConfig mPlayConfig = new TXLivePlayConfig();
                mPlayConfig.setAutoAdjustCacheTime(true);
                mPlayConfig.setMinAutoAdjustCacheTime(1);
                mPlayConfig.setMaxAutoAdjustCacheTime(5);
                mLivePlayer.setConfig(mPlayConfig);
                mLivePlayer.setRenderMode(fitMode);
                mLivePlayer.setPlayListener(new ITXLivePlayListener() {
                    @Override
                    public void onPlayEvent(int i, Bundle bundle) {
                        handleVodListener(i,bundle);
                    }

                    @Override
                    public void onNetStatus(Bundle bundle) {

                    }
                });
                currentMode = TX_LIVE_PLAYER;
                break;
            case TX_VOD_PLAYER:
                //创建player对象
                mVodPlayer = new TXVodPlayer(getContext());
                //关键player对象与界面view
                mVodPlayer.setPlayerView(mVideoSurface);
                mVodPlayer.setRenderMode(fitMode);
                rl_controller.setEnabled(false);
                mVodPlayer.setVodListener(new ITXVodPlayListener() {
                    @Override
                    public void onPlayEvent(TXVodPlayer txVodPlayer, int i, Bundle bundle) {
                        handleVodListener(i,bundle);
                    }

                    @Override
                    public void onNetStatus(TXVodPlayer txVodPlayer, Bundle bundle) {

                    }
                });
                currentMode = TX_VOD_PLAYER;
                break;
        }

        uiStyle = style;
        switch (uiStyle) {
            case HORIZONTAL_LIVE:
                break;
            case HORIZONTAL_VOD:
                break;
            case VERTICAL_LIVE:
                break;
            case VERTICAL_VOD:
                findViewById(R.id.bottom_shadow).setVisibility(VISIBLE);
                setPlayerButtonBigger(true);
                break;
        }
    }

    private boolean initResource = false;
    public void handleVodListener(int event, Bundle bundle){
        switch (event) {
            //1.播放事件
            case PLAY_EVT_PLAY_BEGIN://开始播放
                rl_controller.setEnabled(true);
                currentStatus = Playing;
                rl_controller.setEnabled(true);
                adjustControllerUi();
                showProgress(false);
                if (listener != null && currentMode == TX_VOD_PLAYER){
                    listener.showController(false);
                }
                if (currentMode != TX_LIVE_PLAYER) {
                    doVisibilityAnimation(false);
                }
                break;

            case PLAY_EVT_PLAY_PROGRESS://播放进度
                if (currentMode == TX_VOD_PLAYER) {
                    // 视频总长, 单位是秒
                    int duration = bundle.getInt(EVT_PLAY_DURATION);
                    // 播放进度, 单位是秒
                    int progress = bundle.getInt(EVT_PLAY_PROGRESS);
                    int playAbleDuration = bundle.getInt(TXLiveConstants.EVT_PLAYABLE_DURATION_MS);
                    if (!initResource) {
                        initResource = true;
                        seek_bar.setMax(duration);
                        end_time.setText(DateUtil.getTimeStrBySecond_2(duration));
                    }
                    playing_time.setText(DateUtil.getTimeStrBySecond_2(progress));
                    seek_bar.setSecondaryProgress(playAbleDuration / 1000);
                    seek_bar.setProgress(progress);
                }
                break;

            case PLAY_EVT_PLAY_LOADING://播放加载
                showProgress(true);
                currentStatus = Loading;
                break;

            //2.结束事件
            case PLAY_EVT_PLAY_END://播放结束
                if (autoRestart){
                    currentStatus = End;
                    starPlay(url);
                }else {
                    currentStatus = End;
                    playedFinish = true;
                    showProgress(false);
                    adjustControllerUi();
                }
                break;

            case PLAY_ERR_NET_DISCONNECT://播放重连
                currentStatus = Loading;
                showProgress(true);
                break;
            default:
                showProgress(false);
                break;

        }
        if (listener != null) {
            listener.onStatusChange(currentStatus);
        }
    }

    //According to type,adjust controller
    public void adjustControllerUi() {
        if (currentMode == TX_VOD_PLAYER) {
            switch (currentStatus) {
                case Ready:
                    player_state_center.setVisibility(View.VISIBLE);
                    break;

                case Playing:
                    player_state_center.setImageResource(R.mipmap.ic_video_pause);
                    player_state_center.setVisibility(View.INVISIBLE);
                    break;

                case Pause:
                    player_state_center.setImageResource(R.mipmap.ic_video_play);
                    player_state_center.setVisibility(View.VISIBLE);
                    break;

                case End:
                    player_state_center.setImageResource(R.mipmap.ic_video_replay);
                    player_state_center.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    public void changeUI(){
        if (currentMode == TX_VOD_PLAYER) {
            switch (currentStatus) {
                case Ready:
                    break;

                case Playing:
                    player_state_center.setImageResource(R.mipmap.ic_video_pause);
                    break;

                case Pause:
                    player_state_center.setImageResource(R.mipmap.ic_video_play);
                    break;

                case End:
                    player_state_center.setImageResource(R.mipmap.ic_video_replay);
                    break;
            }
        }
    }

    /**
     * live只支持hlv拉流地址
     * @param pullStreamUrl
     */
    public void starPlay(String pullStreamUrl){
        url = pullStreamUrl;
        showProgress(true);
        switch (currentMode){
            case TX_LIVE_PLAYER:
                if (mLivePlayer != null){
                    mLivePlayer.startPlay(pullStreamUrl,TXLivePlayer.PLAY_TYPE_LIVE_FLV);
                }
                break;
            case TX_VOD_PLAYER:
                if (mVodPlayer != null){
                    mVodPlayer.startPlay(pullStreamUrl);
                }
                break;
        }
    }

    /**
     * 暂停播放
     */
    public void pausePlay(){
        if (mVodPlayer != null){
            mVodPlayer.pause();
        }

        if (mLivePlayer != null){
            mLivePlayer.pause();
        }
    }

    /**
     * 继续播放
     */
    public void resumePlay(){
        if (mVodPlayer != null){
            mVodPlayer.resume();
        }

        if (mLivePlayer != null){
            mLivePlayer.resume();
        }
    }

    /**
     * 销毁view
     */
    public void destroyVideoVideo(){
        if (mLivePlayer != null){
            mLivePlayer.stopPlay(false); //true为清除最后一帧，false为～
        }

        if (mVodPlayer != null){
            mVodPlayer.stopPlay(false);
        }

        if (mVideoSurface != null){
            mVideoSurface.onDestroy();
        }
    }

    public void setTXPlayerStatusListener(TXPlayerStatusListener statusListener){
        this.listener = statusListener;
    }

    /**
     * 是否需要显示全屏的按钮
     * @param need
     */
    public void needShowFullScreenButton(boolean need){
        iv_full_screen.setVisibility(need?VISIBLE:GONE);
    }

    /**
     * 设置全屏button的src
     * @param fullScreenStatus true 显示缩小的样子 ,false 显示放大的样子
     */
    public void setFullScreenButtonStatus(boolean fullScreenStatus){
        iv_full_screen.setImageResource(fullScreenStatus?R.mipmap.ic_live_fullscreen_quit:R.mipmap.ic_live_full_screen);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_controller:
                    isShow = !isShow;
                    if (listener != null) {
                        listener.showController(isShow);
                    }
                    doVisibilityAnimation(isShow);
                    if (listener != null) {
                        listener.onStatusChange(currentStatus);
                    }
                break;
            case R.id.player_state_center:
                isShow = !isShow;
                switch (currentStatus) {
                    case Playing:
                        mVodPlayer.pause();
                        currentStatus = Pause;
                        break;

                    case Pause:
                        mVodPlayer.resume();
                        currentStatus = Playing;
                        break;

                    case End:
                        starPlay(url);
                        currentStatus = Playing;
                        break;
                }
                if (listener != null) {
                    listener.showController(isShow);
                    listener.onCenterButtonClick(currentStatus == Pause);
                }
                changeUI();
                doVisibilityAnimation(isShow);
                if (listener != null) {
                    listener.onStatusChange(currentStatus);
                }
                break;
            case R.id.iv_full_screen:
                if (listener != null){
                    listener.fullScreenButtonClick();
                }
                break;
        }
    }

    public interface TXPlayerStatusListener{
        void onStatusChange(int currentState);

        void showController(boolean show);

        void fullScreenButtonClick();

        void onSeekBarTracking(int trackProgress);

        /**
         * true 点击播放,false 点击暂停
         * @param isPlay
         */
        void onCenterButtonClick(boolean isPlay);
    }

    public boolean isPlayedFinish() {
        return playedFinish;
    }

    /**
     * 控制进度圈显示隐藏
     * @param show
     */
    private void showProgress(boolean show){
        mProgressBar.setVisibility(show?VISIBLE:GONE);
    }

    public void doFullScreenButtonAnimation(boolean show){
        AlphaAnimationUtils.alphaAnimation(iv_full_screen,show?1.0f:0.0f,show?0.0f:1.0f,300, !show);
    }

    /**
     * 控制显示或者隐藏播放器控制按钮
     * @param show
     */
    private void doVisibilityAnimation(boolean show){
        if (show){
            switch (uiStyle) {
                case HORIZONTAL_LIVE:
                    break;
                case HORIZONTAL_VOD:
                    AlphaAnimationUtils.alphaAnimation(player_state_center,0.0f,1.0f,300, show);
                    AlphaAnimationUtils.alphaAnimation(player_function_bar,0.0f,1.0f,300, show);
                    AlphaAnimationUtils.alphaAnimation(player_progress_bar,0.0f,1.0f,300, show);
                    AlphaAnimationUtils.alphaAnimation(iv_full_screen,0.0f,1.0f,300, show);
                    break;
                case VERTICAL_LIVE:

                    break;
                case VERTICAL_VOD:
                    AlphaAnimationUtils.alphaAnimation(player_state_center,0.0f,1.0f,300, show);
                    AlphaAnimationUtils.alphaAnimation(player_function_bar,0.0f,1.0f,300, show);
                    AlphaAnimationUtils.alphaAnimation(player_progress_bar,0.0f,1.0f,300, show);
                    break;
            }
            mVideoSurface.removeCallbacks(runnable);
            mVideoSurface.postDelayed(runnable,3300);
        }else {
            mVideoSurface.removeCallbacks(runnable);
            switch (uiStyle) {
                case HORIZONTAL_LIVE:
                    break;
                case HORIZONTAL_VOD:
                    AlphaAnimationUtils.alphaAnimation(player_state_center,1.0f,0.0f,300, show);
                    AlphaAnimationUtils.alphaAnimation(player_function_bar,1.0f,0.0f,300, show);
                    AlphaAnimationUtils.alphaAnimation(iv_full_screen,1.0f,0.0f,300, show);
                    AlphaAnimationUtils.alphaAnimation(player_progress_bar,1.0f,0.0f,300, show);
                    break;
                case VERTICAL_LIVE:

                    break;
                case VERTICAL_VOD:
                    AlphaAnimationUtils.alphaAnimation(player_progress_bar,1.0f,0.0f,300, show);
                    AlphaAnimationUtils.alphaAnimation(player_function_bar,1.0f,0.0f,300, show);
                    AlphaAnimationUtils.alphaAnimation(player_state_center,1.0f,0.0f,300, show);
                    break;
            }
        }
    }
}
