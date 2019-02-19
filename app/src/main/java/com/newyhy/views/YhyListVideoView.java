package com.newyhy.views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.newyhy.activity.VideoSupportActivity;
import com.newyhy.activity.VideoPlayer;
import com.newyhy.utils.DateUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.location.LocationManager;

import org.quanqi.circularprogress.CircularProgressView;
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
import static com.tencent.rtmp.TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN;

/**
 * 列表播放器
 * Created by Jiervs on 2018/7/2.
 */

public class YhyListVideoView extends FrameLayout implements View.OnClickListener, VideoPlayer.PlayerListener {
    //Views
    private View mRootView;
    public TXCloudVideoView video_view;
    private ImageView iv_cover;
    private RelativeLayout action_content;
    private RelativeLayout header_action;
    private TextView tv_title;
    private TextView playing_time;
    private TextView end_time;
    private LinearLayout footer_action;
    private SeekBar seek_bar;
    private ImageView full_screen;
    private ImageView center_button;
    private CircularProgressView load_progress;
    private ImageView iv_back;

    //player
//    public TXVodPlayer vodPlayer;
    //data
    public String sourceUrl;
    private String coverUrl;
    private String title;
    //var
    private boolean uiVisible;
    public  boolean isFullScreen;
    private Handler handler = new Handler();
    private Runnable runnable;
    private Activity activity;

    //recycler的位置
    public int position;
    public long id;

    public YhyListVideoView(Context context) {
        super(context);
        initView(context);
    }

    public YhyListVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public YhyListVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void initView(Context context){
        mRootView = LayoutInflater.from(context).inflate(R.layout.yhy_video_view,this);
        video_view = findViewById(R.id.video_view);
        iv_cover = findViewById(R.id.iv_cover);
        action_content = findViewById(R.id.action_content);
        header_action = findViewById(R.id.header_action);
        tv_title = findViewById(R.id.tv_title);
        playing_time = findViewById(R.id.playing_time);
        end_time = findViewById(R.id.end_time);
        footer_action = findViewById(R.id.footer_action);
        seek_bar = findViewById(R.id.seek_bar);
        full_screen = findViewById(R.id.full_screen);
        center_button = findViewById(R.id.center_button);
        load_progress = findViewById(R.id.load_progress);
        iv_back = findViewById(R.id.iv_back);

        center_button.setOnClickListener(this);
        action_content.setOnClickListener(this);
        full_screen.setOnClickListener(this);
        action_content.setOnClickListener(this);
        full_screen.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        uiVisible = true;

        runnable = () -> {
            setActionContent(false);
        };


        //seekBar
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            private int startTracking = -1;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                startTracking = seekBar.getProgress();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (stateListener != null) {
                    stateListener.onSeek(seekBar.getProgress());
                }
                if (startTracking >= 0) {//这里用 playStateListener2 监听是用跟 视图view 绑定的 listener 监听拖动
                    playStateListener2.onTraceSeek(seekBar.getProgress() - startTracking);
                }
            }
        });
    }

    //设置全屏模式
    public void setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
        if (fullScreen) {
            iv_back.setVisibility(VISIBLE);
        }
    }

    public void adjustUi(int currentState){
        switch (currentState) {
            case YHY_READY:
                iv_cover.setVisibility(VISIBLE);
                load_progress.setVisibility(GONE);
                center_button.setImageResource(R.mipmap.ic_video_play);
                cancelHideController();
                setActionContent(true);
                seek_bar.setProgress(0);
                footer_action.setVisibility(INVISIBLE);
                break;

            case YHY_PLAYING:
                iv_cover.setVisibility(GONE);
                load_progress.setVisibility(GONE);
                center_button.setImageResource(R.mipmap.ic_video_pause);
                if (stateListener != null) stateListener.onPlay();
                break;

            case YHY_PAUSE:
                load_progress.setVisibility(GONE);
                center_button.setImageResource(R.mipmap.ic_video_play);
                if (stateListener != null) stateListener.onPause();
                setActionContent(true);
                postHideController(3);
                break;

            case YHY_LOAD:
                iv_cover.setVisibility(GONE);
                load_progress.setVisibility(VISIBLE);
                center_button.setImageResource(R.drawable.transparent);
                load_progress.bringToFront();
                load_progress.postInvalidate();
                setActionContent(false);
                cancelHideController();
                break;

            case YHY_END:
                iv_cover.setVisibility(VISIBLE);
                load_progress.setVisibility(GONE);
                center_button.setImageResource(R.mipmap.ic_video_replay);
                if (stateListener != null) stateListener.onEnd();
                setActionContent(true);
                cancelHideController();
                break;

            case YHY_EXCEPTION:
                load_progress.setVisibility(GONE);
                break;

        }
    }

    public void setUrlAndPlayer(String sourceUrl, @Nullable String coverUrl, @Nullable String title,@Nullable long id) {
        this.sourceUrl = sourceUrl;
        this.coverUrl = coverUrl;
        this.title = title;
        this.id = id;
        tv_title.setText(title);
        ImageLoadManager.loadImage(coverUrl,R.mipmap.icon_default_750_360,iv_cover);
    }

    public void setFullScreenActivity(Activity activity){
        this.activity = activity;
    }

    public void postHideController(long second){
        handler.postDelayed(runnable, second * 1000);
    }

    public void cancelHideController(){
        handler.removeCallbacks(runnable);
    }

    /**********************************************************************       User Controller Method      *********************************************************************/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.center_button://中间按钮
                if (playStateListener2 != null){ playStateListener2.onClickPlay(sourceUrl); }
                if (stateListener != null) stateListener.onClickPlay(sourceUrl);
            break;

            case R.id.action_content://控制层
                handler.removeCallbacks(runnable);
                if (VideoPlayer.getInstance().getYhyListVideoView() != this){
                    return;
                }
                if (VideoPlayer.getInstance().getCurrentState() == YHY_LOAD){
                    return;
                }
                if (VideoPlayer.getInstance().getCurrentState() == YHY_READY || VideoPlayer.getInstance().getCurrentState() == YHY_END) {
                    setActionContent(true);
                    handler.removeCallbacks(runnable);
                }else {
                    setActionContent(!uiVisible);
                    postHideController(3);
                }
                break;

            case R.id.full_screen:
                if (!isFullScreen) {
                    // 埋点
                    Analysis.pushEvent(getContext(), AnEvent.PageVideoFull,
                            new Analysis.Builder().
                                    setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                    setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                    setId(String.valueOf(id)).setList(true).setFullResize("恢复"));

                    if (activity != null) {
                        if (activity instanceof VideoSupportActivity) {
                            ((VideoSupportActivity) activity).isFullScreen = true;
                            ((VideoSupportActivity) activity).fullScreen(sourceUrl,coverUrl,title,id);
                        }
                    }
                } else {
                    // 埋点
                    Analysis.pushEvent(getContext(), AnEvent.PageVideoFull,
                            new Analysis.Builder().
                                    setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                    setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                    setId(String.valueOf(id)).setList(true).setFullResize("全屏"));

                    if (activity != null) {
                        if (activity instanceof VideoSupportActivity) {
                            ((VideoSupportActivity) activity).exitFullScreen();
                        }
                    }
                }
                break;

            case R.id.iv_back:
                if (activity != null) {
                    if (activity instanceof VideoSupportActivity) {
                        ((VideoSupportActivity) activity).exitFullScreen();
                    }
                }
                break;
        }
    }

    public void setActionContent(boolean isVisible) {
        uiVisible = isVisible;
        if (isVisible) {
            header_action.setVisibility(VISIBLE);
            center_button.setVisibility(VISIBLE);
            footer_action.setVisibility(VISIBLE);
        } else {
            header_action.setVisibility(INVISIBLE);
            center_button.setVisibility(INVISIBLE);
            footer_action.setVisibility(INVISIBLE);
        }
    }

    /**********************************************************************       Reset Method      ******************************************************************/

    public void synPlayState(int currentState){
        adjustUi(currentState);
    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDeattach() {
        synPlayState(VideoPlayer.YhyState.YHY_READY);
    }

    @Override
    public void onStateChanged(int state) {
        synPlayState(state);
    }

    @Override
    public void onProgress(int progress, int duration) {
        if (seek_bar != null){
            seek_bar.setMax(duration);
            end_time.setText(DateUtil.getTimeStrBySecond_2(duration));
            playing_time.setText(DateUtil.getTimeStrBySecond_2(progress));
            seek_bar.setProgress(progress);
        }
    }

    /**********************************************************************      inner class      *********************************************************************/


    private PlayStateListener stateListener;
    private PlayStateListener playStateListener2;

    public interface PlayStateListener{
        void onClickPlay(String url);
        void onSeek(int progress);
        void onTraceSeek(int offset);
        void onPlay();
        void onPause();
        void onEnd();
    }

    public void setStateListener(PlayStateListener stateListener) {
        this.stateListener = stateListener;
    }

    public void setPlayStateListener2(PlayStateListener playStateListener2) {
        this.playStateListener2 = playStateListener2;
    }
}
