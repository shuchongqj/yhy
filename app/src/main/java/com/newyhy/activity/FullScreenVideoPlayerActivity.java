package com.newyhy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.newyhy.network.CircleNetController;
import com.newyhy.network.NetHandler;
import com.newyhy.utils.DateUtil;
import com.quanyan.yhy.R;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.yhy.common.base.BaseNewActivity;

import static com.tencent.rtmp.TXLiveConstants.EVT_PLAY_DURATION;
import static com.tencent.rtmp.TXLiveConstants.EVT_PLAY_PROGRESS;
import static com.tencent.rtmp.TXLiveConstants.PLAY_ERR_NET_DISCONNECT;
import static com.tencent.rtmp.TXLiveConstants.PLAY_EVT_PLAY_BEGIN;
import static com.tencent.rtmp.TXLiveConstants.PLAY_EVT_PLAY_END;
import static com.tencent.rtmp.TXLiveConstants.PLAY_EVT_PLAY_LOADING;
import static com.tencent.rtmp.TXLiveConstants.PLAY_EVT_PLAY_PROGRESS;
import static com.tencent.rtmp.TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION;

/**
 * 暂时用于圈子模块中观看小视频
 * Created by Jiervs on 2018/6/5.
 */

public class FullScreenVideoPlayerActivity extends BaseNewActivity implements NetHandler.NetHandlerCallback,View.OnClickListener{

    private String uri;
    private long ugcId;
    private CircleNetController controller;
    private NetHandler netHandler;
    private TXCloudVideoView video_view;
    private TXVodPlayer mVodPlayer;
    private RelativeLayout rl_controller;
    private RelativeLayout back;
    private RelativeLayout control_btn;
    private ImageView player_state;
    private ImageView player_state_center;
    private TextView playing_time;
    private TextView end_time;
    private SeekBar seek_bar;
    private int currentState = Ready;


    public static void gotoFullScreenVideoPlayerActivity(Context context,String uri,long ugcId){
        Intent intent = new Intent(context,FullScreenVideoPlayerActivity.class);
        intent.putExtra("uri",uri);
        intent.putExtra("ugcId",ugcId);
        context.startActivity(intent);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_fullscreen_video_player;
    }

    @Override
    protected void initVars() {
        super.initVars();
        uri = getIntent().getStringExtra("uri");
        ugcId = getIntent().getLongExtra("ugcId",0);
        netHandler = new NetHandler(this);
        controller = new CircleNetController(this,netHandler);
    }

    @Override
    protected void initView() {
        super.initView();
        //mPlayerView 即 step1 中添加的界面 view
        video_view = findViewById(R.id.video_view);
        //创建 player 对象
        mVodPlayer = new TXVodPlayer(this);
        mVodPlayer.setRenderMode(RENDER_MODE_ADJUST_RESOLUTION);
        //关键 player 对象与界面 view
        mVodPlayer.setPlayerView(video_view);
        mVodPlayer.startPlay(uri);
        //function
        rl_controller = findViewById(R.id.rl_controller);
        back = findViewById(R.id.back);
        control_btn = findViewById(R.id.control_btn);
        player_state = findViewById(R.id.player_state);
        playing_time = findViewById(R.id.playing_time);
        end_time = findViewById(R.id.end_time);
        seek_bar = findViewById(R.id.seek_bar);
        player_state_center = findViewById(R.id.player_state_center);
    }

    private boolean initResource = false;
    @Override
    protected void setListener() {
        super.setListener();
        back.setOnClickListener(this);
        rl_controller.setOnClickListener(this);
        control_btn.setOnClickListener(this);
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mVodPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (initResource) mVodPlayer.seek(seekBar.getProgress());
                mVodPlayer.resume();
            }
        });

        mVodPlayer.setVodListener(new ITXVodPlayListener() {
            @Override
            public void onPlayEvent(TXVodPlayer txVodPlayer, int event, Bundle bundle) {
                handleVodListener(txVodPlayer,event,bundle);
            }

            @Override
            public void onNetStatus(TXVodPlayer txVodPlayer, Bundle bundle) {

            }
        });

    }

    public void handleVodListener(TXVodPlayer txVodPlayer, int event, Bundle bundle){
        switch (event) {
            //1.播放事件
            case PLAY_EVT_PLAY_BEGIN://开始播放
                currentState = Playing;
                adjustControllerUi();
                break;

            case PLAY_EVT_PLAY_PROGRESS://播放进度
                // 视频总长, 单位是秒
                int duration = bundle.getInt(EVT_PLAY_DURATION);
                // 播放进度, 单位是秒
                int progress = bundle.getInt(EVT_PLAY_PROGRESS);
                if (!initResource) {
                    initResource = true;
                    seek_bar.setMax(duration);
                    end_time.setText(DateUtil.getTimeStrBySecond_2(duration));
                }
                playing_time.setText(DateUtil.getTimeStrBySecond_2(progress));
                seek_bar.setProgress(progress);
                break;

            case PLAY_EVT_PLAY_LOADING://播放加载
                break;

            //2.结束事件
            case PLAY_EVT_PLAY_END://播放结束
                currentState = End;
                adjustControllerUi();
                break;

            case PLAY_ERR_NET_DISCONNECT://播放重连
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(ugcId > 0) controller.doShortVideoPlayAddOne(this,ugcId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVodPlayer.stopPlay(true); // true代表清除最后一帧画面
        video_view.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.control_btn://点击播放暂停
                switch (currentState) {
                    case Ready:
                        mVodPlayer.startPlay(uri);
                        currentState = Playing;
                        break;

                    case Playing:
                        mVodPlayer.pause();
                        currentState = Pause;
                        break;

                    case Pause:
                        mVodPlayer.resume();
                        currentState = Playing;
                        break;

                    case End:
                        mVodPlayer.startPlay(uri);
                        currentState = Playing;
                        break;
                }
                adjustControllerUi();
                break;

            case R.id.rl_controller:
                switch (currentState) {
                    case Ready:
                        mVodPlayer.startPlay(uri);
                        currentState = Playing;
                        break;

                    case Playing:
                        mVodPlayer.pause();
                        currentState = Pause;
                        break;

                    case Pause:
                        mVodPlayer.resume();
                        currentState = Playing;
                        break;

                    case End:
                        mVodPlayer.startPlay(uri);
                        currentState = Playing;
                        break;
                }
                adjustControllerUi();
                break;
        }
    }

    //According to type,adjust controller
    public void adjustControllerUi() {
        switch (currentState) {
            case Ready:
                player_state.setImageResource(R.mipmap.ic_video_play);
                player_state_center.setVisibility(View.VISIBLE);
                break;

            case Playing:
                player_state.setImageResource(R.mipmap.ic_video_pause);
                player_state_center.setVisibility(View.GONE);
                break;

            case Pause:
                player_state.setImageResource(R.mipmap.ic_video_play);
                player_state_center.setVisibility(View.VISIBLE);
                break;

            case End:
                player_state.setImageResource(R.mipmap.ic_video_play);
                player_state_center.setVisibility(View.VISIBLE);
                break;
        }
    }

    /************************************************************      Constant Type      *******************************************************/
    public static final int Ready = 0x300;
    public static final int Playing = 0x301;
    public static final int Loading = 0x302;
    public static final int Pause = 0x303;
    public static final int End = 0x304;
}
