package com.videolibrary.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.tencent.ijk.media.player.IMediaPlayer;
import com.videolibrary.client.activity.VerticalVideoClientActivity;
import com.videolibrary.core.VideoClientView2;
import com.videolibrary.core.VideoTypeInfo;
import com.videolibrary.core.VideoTypeInfo.VideoType;
import com.videolibrary.utils.VideoUtils;
import com.yhy.common.utils.CommonUtil;

import java.lang.reflect.Field;
import java.util.Formatter;
import java.util.Locale;


/**
 * Created with Android Studio.
 * Title:VideoView
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/8/3
 * Time:17:12
 * Version 1.1.0
 */
public class VerticalVideoRootView extends RelativeLayout {

    public static final int SHOW_PROGRESS = 0x801;
    private static final int FADE_OUT = 0x802;
    private static final int LIVE_SCREEN_HORIZONTAL = 1;
    private static final int LIVE_SCREEN_VERTICAL = 2;

    private RelativeLayout mViewRoot;
    private FrameLayout mTitleBar;
    private FrameLayout mBottomBar;
    private ProgressBar mProgressBar;

    private BarrageViewParent mBarrageViewParent;

    private VideoTypeInfo mVideoTypeInfo;
    private TextView mImPickUpTextView;
    private ImageView mImPickUpImageView;

    private ImageView mPlayerStatePause;

    //adjust the video size parameters
    private float mVideoWidth = 0;
    private float mVideoHeight = 0;

    private int liveScreenStyle;

    StringBuilder mFormatBuilder;
    Formatter mFormatter;
    /**
     * 点击输入法，禁止自动隐藏(或者视频已删除)
     */
    private boolean isPreviewHidden = false;

    /**
     * 是否是全屏状态
     */
    private boolean isFullScreen = true;

    public VerticalVideoRootView(Context context) {
        super(context);
        initView(context);
    }

    public VerticalVideoRootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        liveScreenStyle = getLiveScreenStyle(context,attrs);
        initView(context);
    }

    public VerticalVideoRootView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VerticalVideoRootView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.live_stream_video_root_view, this);
        setBackgroundColor(Color.DKGRAY);
        initView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && ((Activity) getContext()).getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mTitleBar.setPadding(0, VideoUtils.getStatusBarHeight(getContext().getApplicationContext()), 0, 0);
        }

        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

        mViewRoot.setOnTouchListener(new OnTouchListener() {
            float x1, x2, y1, y2, dx, dy;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        y1 = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mClientEditText != null && mClientEditText.isFocused()) {
                            CommonUtil.hideInput((Activity) getContext());
                            mClientEditText.clearFocus();

                        }
                        if (mVideoClientView2 != null && mPlayStateImg != null && mPlayerStatePause != null) {
                            if (mVideoClientView2.isPlaying()) {
//                                mVideoClientView2.pause();
                                if(getContext().getResources().getConfiguration().orientation
                                        == Configuration.ORIENTATION_PORTRAIT) {
                                    //pausePlayCallback();
                                    showButton();
                                    return false;
                                }
                            } else {
//                                mVideoClientView2.start();
                                showButton();
                                //resumePlayCallback();
                            }
                        }
                        if (isClientLive){
                            showButton();
                        }
                        mHandler.sendEmptyMessageDelayed(FADE_OUT,5000);
                        //mHandler.removeMessages(FADE_OUT);
                        int deltaY = (int) (event.getY() - y1);
                        if (Math.abs(deltaY) > 0.5) {
                            return false;
                        } else {
                            if (!isPreviewHidden && isClient) {
                                toggleTitleBarVisibility();
                            }
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 显示弹幕信息
     *
     * @param msgContent
     * @param isSelf
     */
    public void show(String msgContent, boolean isSelf) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mBarrageViewParent.show(msgContent, isSelf);
        }
    }

    /**
     * 自动隐藏titlebar， bottombar
     */
    public void showDelayTitle() {
        mHandler.removeMessages(FADE_OUT);
        mHandler.sendEmptyMessageDelayed(FADE_OUT, 0);
    }

    private void toggleTitleBarVisibility() {
        if (isClientLive){
            return;
        }
        if (mTitleBar.getVisibility() == View.INVISIBLE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (((Activity) getContext()).getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    systemUIFullScreen();
                } else {
                    clearWindowFlag();
                }
            }
            mTitleBar.setVisibility(View.VISIBLE);
            ObjectAnimator alpha = ObjectAnimator.ofFloat(mTitleBar, "alpha", 0f, 1.0f);
            AnimatorSet animationSet = new AnimatorSet();
            animationSet.setDuration(0);
            animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
            animationSet.play(alpha);
            animationSet.start();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window window = ((Activity) getContext()).getWindow();
                if (((Activity) getContext()).getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    window.getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                } else {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                }
            }
            mTitleBar.setVisibility(View.INVISIBLE);
            ObjectAnimator alpha = ObjectAnimator.ofFloat(mTitleBar, "alpha", 1.0f, 0f);
            AnimatorSet animationSet = new AnimatorSet();
            animationSet.setDuration(100);
            animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
            animationSet.play(alpha);
            animationSet.start();
        }

        if (mBottomBar.getVisibility() == View.INVISIBLE) {
            mBottomBar.setVisibility(View.VISIBLE);
            ObjectAnimator alpha = ObjectAnimator.ofFloat(mBottomBar, "alpha", 0f, 1.0f);
            AnimatorSet animationSet = new AnimatorSet();
            animationSet.setDuration(100);
            animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
            animationSet.play(alpha);
            animationSet.start();
            showDelayTitle();
        } else {
            mBottomBar.setVisibility(View.INVISIBLE);
            ObjectAnimator alpha = ObjectAnimator.ofFloat(mBottomBar, "alpha", 1.0f, 0f);
            AnimatorSet animationSet = new AnimatorSet();
            animationSet.setDuration(100);
            animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
            animationSet.play(alpha);
            animationSet.start();
        }
    }

    private void initView() {
        //TODO 判断是否是竖屏,如果是,吧左上角布局margin一个actionbar高度
        if (liveScreenStyle == LIVE_SCREEN_VERTICAL){
            LinearLayout top_left_linearLayout = (android.widget.LinearLayout) findViewById(R.id.live_stream_push_top_left_layout);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) top_left_linearLayout.getLayoutParams();
            params.setMargins(0,getStatusBarHeight(),0,0);
            top_left_linearLayout.setLayoutParams(params);
        }

        mViewRoot = (RelativeLayout) findViewById(R.id.live_stream_view_root);
        mTitleBar = (FrameLayout) findViewById(R.id.live_stream_title_bar);
        mBottomBar = (FrameLayout) findViewById(R.id.live_stream_bottom_layout);

        mProgressBar = (ProgressBar) findViewById(R.id.live_stream_progress_bar);

        mBarrageViewParent = (BarrageViewParent) findViewById(R.id.live_stream_barrage_view_parent);

        mPlayerStatePause = (ImageView) findViewById(R.id.live_root_view_player_state_img);
        mPlayerStatePause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016/11/4 暂停，播放
                resumePlayCallback();
            }
        });


    }

    private LinearLayout mNetUnAvailableView;
    /**
     * 设置网络无连接时的界面
     *
     * @param networkDisconnect
     */
    public void setNetworkDisconnect(final View.OnClickListener networkDisconnect) {
        mNetUnAvailableView = new LinearLayout(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mNetUnAvailableView.setLayoutParams(layoutParams);
        mNetUnAvailableView.setOrientation(LinearLayout.VERTICAL);
        mNetUnAvailableView.setGravity(Gravity.CENTER);
        mNetUnAvailableView.setBackgroundColor(Color.BLACK);
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText("网络连接失败");
        textView.setTextSize(15);
        textView.setTextColor(Color.WHITE);
        mNetUnAvailableView.addView(textView);

        int paddingleft = (int) getResources().getDimension(R.dimen.live_root_view_error_padding_left);
        int paddingtop = (int) getResources().getDimension(R.dimen.live_root_view_error_padding_top);
        TextView textView1 = new TextView(getContext());
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams1.topMargin = paddingleft;
        textView1.setLayoutParams(layoutParams1);
        textView1.setTextColor(getResources().getColor(R.color.neu_ffaf00));
        textView1.setBackgroundResource(R.drawable.shape_btn_live_net_connect);
        textView1.setTextSize(13);
        textView1.setPadding(paddingleft, paddingtop, paddingleft, paddingtop);
        textView1.setText("重新连接");
        textView1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                networkDisconnect.onClick(v);
            }
        });

        mNetUnAvailableView.addView(textView1);

        mViewRoot.addView(mNetUnAvailableView, 1);
    }

    public void removeNetUnAvailableView(){
        mViewRoot.removeView(mNetUnAvailableView);
    }

    /**
     * 视频被删的错误页面
     */
    public void setVideoDelete() {
//        isPreviewHidden = true;
        showProgress(false);
        final LinearLayout linearLayout = new LinearLayout(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setBackgroundColor(Color.BLACK);

        TextView textView = new TextView(getContext());
        textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText("视频已删除");
        textView.setTextSize(15);
        textView.setTextColor(Color.WHITE);
        linearLayout.addView(textView);

        mViewRoot.addView(linearLayout, 1);

        mBottomBar.removeAllViews();
        //findViewById(R.id.live_client_portrait_share_view).setVisibility(View.INVISIBLE);
        findViewById(R.id.live_client_landscape_share_view).setVisibility(View.INVISIBLE);
        findViewById(R.id.live_client_landscape_divide2).setVisibility(View.INVISIBLE);
        findViewById(R.id.live_client_landscape_follow_view).setVisibility(View.INVISIBLE);
    }

    public void setChatView(ViewGroup chatView) {
        ((RelativeLayout) findViewById(R.id.live_stream_im_list_layout)).addView(chatView,
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setMediaPath(VideoTypeInfo videoTypeInfo,int orientation) {
        mVideoTypeInfo = videoTypeInfo;
        if (VideoType.MP4.ordinal() == videoTypeInfo.mMediaType.ordinal()) {
//            setClientPlayVisible();
//            initPlayer(mVideoTypeInfo.mMediaURL);
            initPlayer2(mVideoTypeInfo.mMediaURL);
        } else if (VideoType.RTMP.ordinal() == videoTypeInfo.mMediaType.ordinal()) {
            if (videoTypeInfo.isClient) {
//                setClientLiveVisible();
                initPlayer2(mVideoTypeInfo.mMediaURL);
            }
        }
    }

    /**
     * 直播客户端在线人数
     */
    private TextView mClientOnLineNumTv;
    /**
     * 直播客户端房间号
     */
    private TextView mClientRoomNum;

    private EditText mClientEditText;
    private RelativeLayout mLivePortraitTitleLayout;
    private RelativeLayout mLiveLandscapeTitleLayout;
    private RelativeLayout mLivePortraitBottomLayout;
    private RelativeLayout mLiveLandscapeBottomLayout;

    private boolean isClient = false;
    private boolean isClientLive = false;


    /**
     * 直播，客户端布局
     */
    public void setClientLiveVisible() {
        isClient = true;
        isClientLive = true;
        //findViewById(R.id.live_client_portrait_title_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.live_client_portrait_bottom_layout).setVisibility(View.VISIBLE);
        //findViewById(R.id.live_client_portrait_bottom_fullscreen_view_layout).setOnClickListener(new FullSreenViewClick(true));
        //findViewById(R.id.live_client_landscape_bottom_right_full_screen_layout).setOnClickListener(new FullSreenViewClick(true));

        //findViewById(R.id.live_client_portrait_back_view).setOnClickListener(mClientClick);//返回（竖屏）
        //findViewById(R.id.live_client_portrait_share_view).setOnClickListener(mClientClick);//分享（竖屏）
        findViewById(R.id.live_client_landscape_follow_view).setOnClickListener(mClientClick);//关注
        findViewById(R.id.live_client_landscape_back_view).setOnClickListener(mClientClick);//返回（横屏）
        findViewById(R.id.live_client_landscape_share_view).setOnClickListener(mClientClick);//分享（横屏）
        findViewById(R.id.live_client_hide_im).setOnClickListener(mClientClick);//隐藏弹幕消息（横屏）
//        findViewById(R.id.live_client_landscape_bottom_post_tv).setOnClickListener(mClientClick);//发送消息

        //mLivePortraitTitleLayout = (RelativeLayout) findViewById(R.id.live_client_portrait_title_layout);
        mLiveLandscapeTitleLayout = (RelativeLayout) findViewById(R.id.live_client_landscape_title_layout);
        mLivePortraitBottomLayout = (RelativeLayout) findViewById(R.id.live_client_portrait_bottom_layout);
        mLiveLandscapeBottomLayout = (RelativeLayout) findViewById(R.id.live_client_landscape_bottom_layout);
        mClientOnLineNumTv = (TextView) findViewById(R.id.live_client_portrait_online_num_tv);
        mClientRoomNum = (TextView) findViewById(R.id.live_client_portrait_room_num_tv);
        mClientEditText = (EditText) findViewById(R.id.live_client_landscape_bottom_edittext);
        mClientEditText.setOnEditorActionListener(mOnEditorActionListener);//编辑输入，发送消息
        mClientEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    clearWindowFlag();
                } else {
                    //在onSizeChange中替换
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        ((Activity)getContext()).getWindow().getDecorView().setSystemUiVisibility(
//                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//                    }
                }
                isPreviewHidden = hasFocus;
            }
        });
        showButton();
    }

    /**
     * 更改关注的状态
     *
     * @param isAttentioned
     */
    public void setFolowState(boolean isAttentioned) {
        ((LinearLayout) findViewById(R.id.live_client_landscape_follow_view)).getChildAt(0).setSelected(isAttentioned);
    }

    /**
     * 设置直播房间号（客户端）
     *
     * @param clientRoomNum
     */
    public void setClientRoomNum(String clientRoomNum) {
        if (mClientRoomNum != null) {
            mClientRoomNum.setText(TextUtils.isEmpty(clientRoomNum) ? "" : clientRoomNum);
        }
    }

    /**
     * 设置在线人数（直播客户端）
     *
     * @param clientOnLineNumTv
     */
    public void setClientOnLineNumTv(String clientOnLineNumTv) {
        if (mClientOnLineNumTv != null) {
            mClientOnLineNumTv.setText(TextUtils.isEmpty(clientOnLineNumTv) ? "" : clientOnLineNumTv);
        }
    }

    /**
     * 回放当前时长
     */
    private TextView mVideoTimeCurrentTv;
    /**
     * 回放视频总时长
     */
    private TextView mVideoTimeTotalTv;
    /**
     * 回放进度条是否拖拽状态
     */
    private boolean mDragging = false;
    /**
     * 回放进度条
     */
    private SeekBar mSeekBar;

    private RelativeLayout mPlaybackPortraitClientTitleLayout;
    private ImageView mPlayStateImg;

    private RelativeLayout live_stream_play_view_control_layout;

    /**
     * 显示客户端回放的布局
     */
    public void setClientPlayVisible() {
        isClient = true;

        findViewById(R.id.live_client_portrait_bottom_layout).setVisibility(View.INVISIBLE);
        //findViewById(R.id.live_client_portrait_title_layout).setVisibility(View.VISIBLE);
        live_stream_play_view_control_layout = findViewById(R.id.live_stream_play_view_control_layout);
        live_stream_play_view_control_layout.setVisibility(View.VISIBLE);
        findViewById(R.id.live_root_view_replay_label_text).setVisibility(View.VISIBLE);

        //mPlaybackPortraitClientTitleLayout = (RelativeLayout) findViewById(R.id.live_client_portrait_title_layout);
        mLiveLandscapeTitleLayout = (RelativeLayout) findViewById(R.id.live_client_landscape_title_layout);
        mSeekBar = (SeekBar) findViewById(R.id.live_stream_play_seek_bar);
        mVideoTimeCurrentTv = (TextView) findViewById(R.id.live_stream_play_current_time);
        mVideoTimeTotalTv = (TextView) findViewById(R.id.live_stream_play_total_time);
        mPlayStateImg = (ImageView) findViewById(R.id.live_client_play_state_img);

        findViewById(R.id.live_client_landscape_follow_view).setOnClickListener(mClientClick);//关注
        //findViewById(R.id.live_client_portrait_back_view).setOnClickListener(mClientClick);
        findViewById(R.id.live_client_landscape_back_view).setOnClickListener(mClientClick);
        //findViewById(R.id.live_client_portrait_share_view).setOnClickListener(mClientClick);
        findViewById(R.id.live_client_landscape_share_view).setOnClickListener(mClientClick);
        findViewById(R.id.live_client_play_state_img_layout).setOnClickListener(mClientClick);

        //显示上面的和下面的button
        showButton();
    }

    public void calculateReplayBackLabelTextPosition() {
        final TextView textView = (TextView) findViewById(R.id.live_root_view_replay_label_text);
        mTitleBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mTitleBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                if(getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    int height = mTitleBar.getMeasuredHeight();
                    LayoutParams layoutParams = (LayoutParams) textView.getLayoutParams();
                    layoutParams.topMargin = height + 10 + ScreenSize.getStatusBarHeight(getContext().getApplicationContext());
                    textView.setLayoutParams(layoutParams);
                }else{
                    int height = mTitleBar.getMeasuredHeight();
                    LayoutParams layoutParams = (LayoutParams) textView.getLayoutParams();
                    layoutParams.topMargin = height + 10;
                    textView.setLayoutParams(layoutParams);
                }
            }
        });
    }

    /**
     * 设置标题内容
     *
     * @param landscapeTitleTv
     */
    public void setLandscapeTitleTv(String landscapeTitleTv) {
        ((TextView) findViewById(R.id.live_client_landscape_title_text))
                .setText(TextUtils.isEmpty(landscapeTitleTv) ? "" : landscapeTitleTv);
    }

    public String getLiveTitle() {
        return ((TextView) findViewById(R.id.live_client_landscape_title_text)).getText().toString();
    }

    //private class FullSreenViewClick implements View.OnClickListener {

       /* private boolean isLive = true;

        public FullSreenViewClick(boolean isLive) {
            this.isLive = isLive;
        }

        *//**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         *//*
        @Override
        public void onClick(final View v) {
            //切换成全屏
            isFullScreen = !isFullScreen;
            TCEventHelper.onEvent(v.getContext(), AnalyDataValue.LIVE_DETAIL_FULL_SCREAN_CLICK);
            if(isFullScreen){
                halfButton.setImageResource(R.mipmap.ic_live_fullscreen_quit);
            }else {
                halfButton.setImageResource(R.mipmap.ic_live_full_screen);
            }
            adjustVideoSize(isFullScreen);
        }*/
    //}

    /**
     * 发送消息
     */
    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId) {
                case EditorInfo.IME_ACTION_SEND:
                    // TODO: 8/30/16 点击键盘发送按钮的时触发
                    if (mImeActionSend != null) {
                        mImeActionSend.onSendMessage(v.getText().toString());
                        v.setText("");
                    }
                    break;

            }
            return true;
        }
    };

    /**
     * 点击事件（所有View，可能没有初始化需要判断）
     */
    private View.OnClickListener mClientClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //case R.id.live_client_portrait_back_view:
                case R.id.live_client_landscape_back_view:
                    ((Activity) v.getContext()).finish();
                    break;
                //case R.id.live_client_portrait_share_view:
                case R.id.live_client_landscape_share_view:
                    // TODO: 16/8/25 分享
                    if (mViewClick != null) {
                        mViewClick.share(v);
                    }
                    break;
                case R.id.live_client_play_state_img_layout:
                    // TODO: 16/8/25 暂停播放 / 开始播放
//                    if (mFetchCore != null) {
//                        if (mFetchCore.isPlaying()) {
//                            mFetchCore.pause();
//                        } else {
//                            mFetchCore.start();
//                        }
//                    }
                    if (mVideoClientView2 != null) {
                        if (mVideoClientView2.isPlaying()) {
//                            mVideoClientView2.pause();
                            pausePlayCallback();
                        } else {
//                            mVideoClientView2.start();
                            resumePlayCallback();
                        }
                    }
                    break;
                case R.id.live_client_landscape_follow_view:
                    // TODO: 16/8/25 点赞
                    if (mViewClick != null) {
                        mViewClick.follow(v);
                    }
                    break;
//                case R.id.live_client_landscape_bottom_post_tv:
//                    // TODO: 16/8/25 发送消息
//                    if (mClientEditText != null && mViewClick != null) {
//                        mViewClick.sendMessage(mClientEditText.getText().toString());
//                    }
//                    break;
                case R.id.live_client_hide_im:
                    // TODO: 16/8/25 隐藏弹幕（客户端横屏时）
                    mBarrageViewParent.setVisibility(
                            mBarrageViewParent.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
                    if (mBarrageViewParent.getVisibility() == View.INVISIBLE) {
                        TCEventHelper.onEvent(getContext(), AnalyDataValue.LIVE_DETAIL_STOP_BARRAGE);
                        mBarrageViewParent.interruptMessage();
                    } else {
                        mBarrageViewParent.resetShowMessage();
                        TCEventHelper.onEvent(getContext(), AnalyDataValue.LIVE_DETAIL_OPEN_BARRAGE);
                    }

                    findViewById(R.id.live_client_hide_im).setSelected(
                            mBarrageViewParent.getVisibility() == View.INVISIBLE
                    );
                    break;
            }
        }
    };

    /**
     * 聊天界面
     */
    private RelativeLayout mPushChatLayout;

    private TextView mPushOnlineNumTv;

    /**
     * 显示直播已结束
     */
    public void setVideoLiveOver() {
//        isPreviewHidden = true;
        showProgress(false);
        LinearLayout linearLayout = new LinearLayout(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setBackgroundColor(Color.BLACK);
        TextView textView = new TextView(getContext());
        textView.setText("直播已结束");
        textView.setTextSize(15);
        textView.setTextColor(Color.WHITE);
        linearLayout.addView(textView);

        mViewRoot.addView(linearLayout, 1);

        mTitleBar.setVisibility(View.VISIBLE);
        mBottomBar.removeAllViews();
        //findViewById(R.id.live_client_portrait_share_view).setVisibility(View.INVISIBLE);
        findViewById(R.id.live_client_landscape_share_view).setVisibility(View.INVISIBLE);
        findViewById(R.id.live_client_landscape_divide2).setVisibility(View.INVISIBLE);
        findViewById(R.id.live_client_landscape_follow_view).setVisibility(View.INVISIBLE);

        ((VerticalVideoClientActivity) getContext()).hiddenChatListView();
    }



    boolean isPickUp = false;
    OnClickListener pickUpOnclickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int resId;
            int imgResId;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mPushChatLayout.getLayoutParams();
            if (isPickUp) {
                resId = R.string.pick_up;
                imgResId = R.mipmap.ic_live_im_pick_up;
                params.height = (int) getResources().getDimension(R.dimen.live_stream_chat_layout_height_normal);
                params.width = (int) getResources().getDimension(R.dimen.live_stream_chat_layout_width);
                isPickUp = false;
            } else {
                resId = R.string.develop;
                imgResId = R.mipmap.ic_live_im_develop;
                params.height = (int) getResources().getDimension(R.dimen.live_stream_chat_layout_height_pickup);
                params.width = (int) getResources().getDimension(R.dimen.live_stream_chat_layout_width);
                isPickUp = true;
            }
            mImPickUpImageView.setImageResource(imgResId);
            mPushChatLayout.setLayoutParams(params);
            mImPickUpTextView.setText(resId);
        }
    };


    /**
     * 直播端，布局的点击事件
     */
    private View.OnClickListener mPushViewClick = new View.OnClickListener() {

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.live_stream_push_top_back_layout:
                    // TODO: 16/8/25 关闭
                    if (getContext() instanceof Activity) {
                        ((Activity) getContext()).finish();
                    }
                    break;
                case R.id.live_stream_push_top_hide_im_layout:
                    // TODO: 16/8/25 主播端  隐藏IM栏
                    mPushChatLayout.setVisibility(mPushChatLayout.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
                    if (mPushChatLayout.getVisibility() == View.INVISIBLE) {
                        ((ImageView) findViewById(R.id.live_stream_push_top_im_img)).setImageResource(R.mipmap.ic_live_push_im_close);
                    } else {
                        ((ImageView) findViewById(R.id.live_stream_push_top_im_img)).setImageResource(R.mipmap.ic_live_push_im_open);
                    }
                    break;

                case R.id.live_stream_push_top_share_layout:
                    // TODO: 16/8/25 分享
                    if (mIPushView != null) {
                        mIPushView.share(v);
                    }
                    break;
                case R.id.live_stream_close_view:
                    // TODO: 16/8/25 关闭直播
                    if (mIPushView != null) {
                        mIPushView.closePush(v);
                    }
                    break;
            }
        }
    };


//    private VideoClientView mFetchCore;

    public void showProgress(boolean isShown) {
        mProgressBar.setVisibility(isShown ? View.VISIBLE : View.INVISIBLE);
    }

    private VideoClientView2 mVideoClientView2;

    private void initPlayer2(String url) {
        mVideoClientView2 = new VideoClientView2(getContext(), url);
        mViewRoot.addView(mVideoClientView2, 0, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        mVideoClientView2.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                // 当视频加载完毕以后，隐藏加载进度条
                mProgressBar.setVisibility(View.INVISIBLE);
                if (!isClientLive) {
                    if(mPlayStateImg != null) {
                        mPlayStateImg.setImageResource(R.mipmap.ic_live_state_pause);
                    }
                    if(mPlayerStatePause != null) {
                        mPlayerStatePause.setVisibility(View.INVISIBLE);
                    }

                    // 设置控制条,放在加载完成以后设置，防止获取getDuration()错误
                    mSeekBar.setMax(1000);
                    mSeekBar.setProgress(0);
                    // 设置播放时间
                    mVideoTimeCurrentTv.setText("00:00");
                    mVideoTimeTotalTv.setText(stringForTime((int) mp.getDuration()));
                    // 设置拖动监听事件
                    mSeekBar.setOnSeekBarChangeListener(new SeekBarChangeListener());

                    mHandler.sendEmptyMessage(SHOW_PROGRESS);

                    mVideoClientView2.start();
                }
            }
        });

        mVideoClientView2.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {
                if (!isClientLive) {
                    mHandler.removeMessages(SHOW_PROGRESS);
                    pausePlayCallback();
                }
            }
        });

        mVideoClientView2.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                if (!isClientLive) {
                    mHandler.removeMessages(SHOW_PROGRESS);
                }
//                stopPlayback();
                if (mIMediaError != null && mIMediaError.onError()) {
                    return true;
                }
                setVideoErrorView();
                return true;
            }
        });

        mVideoClientView2.setOnVideoSizeChangedListener(new IMediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
                adjustVideoSize(isFullScreen);
            }
        });

        mVideoClientView2.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                LogUtils.v("what -->> " + what + "   extra -->> " + extra);
                switch (what) {
                    case IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                        LogUtils.d("MEDIA_INFO_VIDEO_TRACK_LAGGING:");
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        LogUtils.d("MEDIA_INFO_BUFFERING_START:");
                        showProgress(true);
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        LogUtils.d("MEDIA_INFO_BUFFERING_END:");
                        showProgress(false);
                        break;
                    case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
                        LogUtils.d("MEDIA_INFO_NETWORK_BANDWIDTH: " + extra);
                        break;
                    case IMediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                        LogUtils.d("MEDIA_INFO_BAD_INTERLEAVING:");
                        break;
                    case IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                        LogUtils.d("MEDIA_INFO_NOT_SEEKABLE:");
                        break;
                    case IMediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                        LogUtils.d("MEDIA_INFO_METADATA_UPDATE:");
                        break;
                    case IMediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE:
                        LogUtils.d("MEDIA_INFO_UNSUPPORTED_SUBTITLE:");
                        break;
                    case IMediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT:
                        LogUtils.d("MEDIA_INFO_SUBTITLE_TIMED_OUT:");
                        break;
                    case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                        LogUtils.d("MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + extra);
                        break;
                    case IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                        LogUtils.d("MEDIA_INFO_AUDIO_RENDERING_START:");
                        break;
                    case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                        LogUtils.d("MEDIA_INFO_VIDEO_RENDERING_START:");
                        showProgress(false);
                        break;
                }
                return false;
            }
        });

        mVideoClientView2.setIPlayerControl(new VideoClientView2.IPlayerControl() {
            @Override
            public void onSurfaceDestroy() {
                if (!isClientLive) {
                    mHandler.removeMessages(SHOW_PROGRESS);
                }
            }
        });
    }

    /**
     * 根据视频宽高比适配屏幕比例
     *
     * @param isFullScreen
     */
    private void adjustVideoSize(boolean isFullScreen) {
        if (mVideoClientView2 != null) {
            int screenWidth;
            int screenHeight;
            RelativeLayout.LayoutParams layoutParams;
            ViewGroup.LayoutParams layoutParams1 ;
            if (!isFullScreen) {

                screenHeight =(int) getResources().getDimension(R.dimen.live_stream_surface_height);
                screenWidth = (int) (screenHeight / 1.76);
                layoutParams = new RelativeLayout.LayoutParams(screenWidth,screenHeight);

                layoutParams1 = getLayoutParams();
                layoutParams1.width = ScreenSize.getScreenWidth(getContext().getApplicationContext());
                layoutParams1.height = screenHeight;

                //设置回放的时候,让底部margin一个导航栏的高度
                FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) live_stream_play_view_control_layout.getLayoutParams();
                layoutParams2.setMargins(0,0,0, 0);
                live_stream_play_view_control_layout.setLayoutParams(layoutParams2);

                Log.e("EZEZ","视频宽高= "+(int) (getResources().getDimension
                        (R.dimen.yhy_size_230px) * (mVideoWidth / mVideoHeight))+" = "+ (int) (screenWidth * (mVideoHeight / mVideoWidth)));
            } else {
                screenWidth = LayoutParams.MATCH_PARENT;
                screenHeight = LayoutParams.MATCH_PARENT;
                layoutParams = new RelativeLayout.LayoutParams(screenWidth,screenHeight);

                layoutParams1 = getLayoutParams();
                layoutParams1.width = LayoutParams.MATCH_PARENT;
                layoutParams1.height = LayoutParams.MATCH_PARENT;
                Log.e("EZEZ","kuan gao = ");

            }
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            setLayoutParams(layoutParams1);
            mVideoClientView2.setLayoutParams(layoutParams);
        }
    }

    /**
     * 显示回放视频出错信息
     */
    public void setVideoErrorView() {
        LinearLayout linearLayout = new LinearLayout(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setBackgroundColor(Color.BLACK);
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText("视频出错");
        textView.setTextSize(15);
        textView.setTextColor(Color.WHITE);
        linearLayout.addView(textView);

        mViewRoot.addView(linearLayout, 1);
    }

    /**
     * 开始播放
     *
     * @param pushStreamUrl
     */
    public void startPlay(String pushStreamUrl) {
        if (mVideoClientView2 != null) {
            showProgress(true);
            mVideoClientView2.setVideoUrl(pushStreamUrl);
            mVideoClientView2.openVideo();
            mVideoClientView2.start();
        }
    }

    /**
     * 停止播放
     */
    public void stopPlayback() {
        if (mVideoClientView2 != null) {
            mVideoClientView2.stopPlayback();
        }
    }

    public void resetPlay(){
        if(mVideoClientView2 != null){
            mVideoClientView2.resetVideo();
        }
    }

    /**
     * 暂停播放
     */
    public void pausePlayCallback() {
        mHandler.removeMessages(FADE_OUT);
        if (!isClientLive) {
            mTitleBar.setVisibility(View.VISIBLE);
            mBottomBar.setVisibility(View.VISIBLE);
        }
        mTitleBar.setAlpha(1);
        mBottomBar.setAlpha(1);
        isPreviewHidden = true;
        Log.e("EZEZ", "pausePlayCallback: ");
        if(!isClientLive) {
            if(mPlayStateImg != null) {
                mPlayStateImg.setImageResource(R.mipmap.ic_live_state_play);
            }
            if(mPlayerStatePause != null) {
                mPlayerStatePause.setVisibility(View.VISIBLE);
            }
            mHandler.removeMessages(SHOW_PROGRESS);
        }

        if (mVideoClientView2 != null && mVideoClientView2.canPause()) {
            mVideoClientView2.pause();
        }
    }

    public void showButton(){

        mHandler.removeMessages(FADE_OUT);
        if (!isClientLive) {
            mTitleBar.setVisibility(View.VISIBLE);
            mBottomBar.setVisibility(View.VISIBLE);
            mTitleBar.setAlpha(1);
            mBottomBar.setAlpha(1);
        }
        isPreviewHidden = true;
        if(!isClientLive) {
            if(mPlayStateImg != null) {
                mPlayStateImg.setImageResource(R.mipmap.ic_live_state_pause);
            }
        }
        mHandler.sendEmptyMessageDelayed(FADE_OUT,5000);

        //使activity也显示layout
        ((VerticalVideoClientActivity) getContext()).showTopLayout(true);
    }

    public void hiddenButton(){
        if (mTitleBar.getVisibility() == View.VISIBLE) {
            isPreviewHidden = false;
        }
        if(!isClientLive) {
            if(mPlayStateImg != null) {
                mPlayStateImg.setImageResource(R.mipmap.ic_live_state_pause);
            }
            if(mPlayerStatePause != null) {
                mPlayerStatePause.setVisibility(View.INVISIBLE);
            }
            mHandler.sendEmptyMessage(SHOW_PROGRESS);
        }
        mHandler.sendEmptyMessageDelayed(FADE_OUT,5000);
    }

    /**
     * 恢复继续播放
     */
    public void resumePlayCallback() {

        if (mTitleBar.getVisibility() == View.VISIBLE) {
            isPreviewHidden = false;
        }
        if(!isClientLive) {
            if(mPlayStateImg != null) {
                mPlayStateImg.setImageResource(R.mipmap.ic_live_state_pause);
            }
            if(mPlayerStatePause != null) {
                mPlayerStatePause.setVisibility(View.INVISIBLE);
            }
            mHandler.sendEmptyMessage(SHOW_PROGRESS);
        }
        if (mVideoClientView2 != null) {
            mVideoClientView2.start();
        }
        mHandler.sendEmptyMessageDelayed(FADE_OUT,5000);
    }

//    private void initPlayer(String url) {
//        mFetchCore = new VideoClientView(getContext(), url);
//        mFetchCore.setIMediaPlay(new VideoClientView.IMediaPlay() {
//            @Override
//            public void prepared(int videoWidth, int videoHeight, int duration) {
//                // 当视频加载完毕以后，隐藏加载进度条
//                mProgressBar.setVisibility(View.INVISIBLE);
//                setResolutionInfo(videoWidth, videoHeight);
//                // 设置控制条,放在加载完成以后设置，防止获取getDuration()错误
//                mSeekBar.setMax(1000);
//                mSeekBar.setProgress(0);
//                // 设置播放时间
//                mVideoTimeCurrentTv.setText("00:00");
//                mVideoTimeTotalTv.setText(stringForTime(duration));
//                // 设置拖动监听事件
//                mSeekBar.setOnSeekBarChangeListener(new SeekBarChangeListener());
//
//                mHandler.sendEmptyMessage(SHOW_PROGRESS);
//            }
//
//            @Override
//            public void onCompletion() {
//                mHandler.removeMessages(SHOW_PROGRESS);
//            }
//
//            @Override
//            public void onError(MediaPlayer mp, int what, int extra) {
//                mHandler.removeMessages(SHOW_PROGRESS);
//            }
//
//            @Override
//            public void onDestroy() {
//                mHandler.removeMessages(SHOW_PROGRESS);
//            }
//        });
//        mViewRoot.addView(mFetchCore, 0);
//    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int pos;
            switch (msg.what) {
                case FADE_OUT:
                        toggleTitleBarVisibility();
                    //使activity也显示layout
                    ((VerticalVideoClientActivity) getContext()).showTopLayout(false);
                    break;
                case SHOW_PROGRESS:
                    pos = setProgress();
//                    if (!mDragging && mFetchCore.isPlaying()) {
//                        msg = obtainMessage(SHOW_PROGRESS);
//                        sendMessageDelayed(msg, 1000 - (pos % 1000));
//                    }
                    if (!mDragging && mVideoClientView2.isPlaying()) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
            }
        }
    };

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        LogUtils.d("visible -->> " + (View.VISIBLE == visibility) + "  invisible -->> " + (View.INVISIBLE == visibility)
                + "  INVISIBLE -->> " + (View.INVISIBLE == visibility));
        if (isClient) {
            if (View.VISIBLE == visibility) {
                if (((Activity) getContext()).getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        systemUIFullScreen();
                    }
                } else {
                    if (isClient) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            clearWindowFlag();
                        }
                    }
                }
            } else {
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        LogUtils.v("***********************" + w + ", " + h + ", " + oldw + ", " + oldh);
        if (isClient) {
            if (isClientLive && getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_LANDSCAPE) {
                if (oldh < h) {
                    if (mClientEditText != null) {
                        mClientEditText.clearFocus();
                    }
                    this.requestFocus();

//                    systemUIFullScreen();

                    mHandler.removeCallbacks(mRunnable);
                    mHandler.postDelayed(mRunnable, 500);
                }
            }
        }
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            systemUIFullScreen();
        }
    };

    /**
     * <li>https://developer.android.com/training/system-ui/immersive.html</li>
     *
     * @param newConfig
     */
    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtils.d("onConfigurationChanged : " + newConfig.orientation);
        calculateReplayBackLabelTextPosition();
        if (isClient) {
            CommonUtil.hideInput((Activity) getContext());
            if (Configuration.ORIENTATION_LANDSCAPE == newConfig.orientation) {
                if (isClient) {
                    if (!isClientLive) {
                        mTitleBar.setBackgroundColor(getResources().getColor(R.color.transparent_black_60));
                        mBottomBar.setBackgroundColor(getResources().getColor(R.color.transparent_black_60));
                    } else {
                        mTitleBar.setBackgroundColor(getResources().getColor(R.color.transparent_black_60));
                        mBottomBar.setBackgroundColor(getResources().getColor(R.color.transparent_black_60));
                    }
//                mTitleBar.setBackgroundResource(R.drawable.bg_gradient_white_black);
                    if (mLivePortraitTitleLayout != null) {
                        mLivePortraitTitleLayout.setVisibility(View.INVISIBLE);
                    }
                    if (mLiveLandscapeTitleLayout != null) {
                        mLiveLandscapeTitleLayout.setVisibility(View.VISIBLE);
                    }
                    if (mLivePortraitBottomLayout != null) {
                        mLivePortraitBottomLayout.setVisibility(View.INVISIBLE);
                    }
                    if (mLiveLandscapeBottomLayout != null) {
                        mLiveLandscapeBottomLayout.setVisibility(View.VISIBLE);
                    }
                    if (mPlaybackPortraitClientTitleLayout != null) {
                        mPlaybackPortraitClientTitleLayout.setVisibility(View.INVISIBLE);
                    }

                } else {

                }
                if (mVideoWidth > 0 && mVideoHeight > 0) {
                    adjustVideoSize(true);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    systemUIFullScreen();
                }
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ViewGroup.LayoutParams layoutParams = getLayoutParams();
                        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        setLayoutParams(layoutParams);
                    }
                }, 300);
            } else if (Configuration.ORIENTATION_PORTRAIT == newConfig.orientation) {
                if (isClient) {
//                    if (isClientLive) {
                    mTitleBar.setBackgroundColor(Color.TRANSPARENT);
                    mBottomBar.setBackgroundColor(Color.TRANSPARENT);
//                    } else {
//                        mTitleBar.setBackgroundColor(getResources().getColor(R.color.transparent_black_60));
//                        mBottomBar.setBackgroundColor(getResources().getColor(R.color.transparent_black_60));
//                    }

                    if (mLivePortraitTitleLayout != null) {
                        mLivePortraitTitleLayout.setVisibility(View.VISIBLE);
                    }
                    if (mLiveLandscapeTitleLayout != null) {
                        mLiveLandscapeTitleLayout.setVisibility(View.INVISIBLE);
                    }
                    if (mLivePortraitBottomLayout != null) {
                        mLivePortraitBottomLayout.setVisibility(View.VISIBLE);
                    }
                    if (mLiveLandscapeBottomLayout != null) {
                        mLiveLandscapeBottomLayout.setVisibility(View.INVISIBLE);
                    }
                    if (mPlaybackPortraitClientTitleLayout != null) {
                        mPlaybackPortraitClientTitleLayout.setVisibility(View.VISIBLE);
                    }

                } else {
                }
                if (mVideoWidth > 0 && mVideoHeight > 0) {
                    adjustVideoSize(false);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    clearWindowFlag();
                }
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isFullScreen) {
                            ViewGroup.LayoutParams layoutParams = getLayoutParams();
                            layoutParams.height = (int) getResources().getDimension(R.dimen.live_stream_surface_height);
                            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                            setLayoutParams(layoutParams);
                        }
                    }
                }, 300);
            }
        }
    }

    /**
     * 如果同时设置，对于软件盘谈起，重置的时候就会赞成界面resize，底部有白条
     * 故，使用延迟设置immersive的模式
     */
    private Runnable mImmersiveRunnable = new Runnable() {
        @Override
        public void run() {
            // Set the content to appear under the system bars so that the content
            // doesn't resize when the system bars hide and show.
            Window window = ((Activity) getContext()).getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE);
            mTitleBar.setPadding(0,
                    VideoUtils.getStatusBarHeight(getContext().getApplicationContext()),
                    0,
                    0);
        }
    };

    public void systemUIFullScreen() {
        Window window = ((Activity) getContext()).getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        mHandler.removeCallbacks(mImmersiveRunnable);
        mHandler.postDelayed(mImmersiveRunnable, 500);
    }

    private void clearWindowFlag() {
        mHandler.removeCallbacks(mRunnable);
        mHandler.removeCallbacks(mImmersiveRunnable);
        Window window = ((Activity) getContext()).getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_VISIBLE);
        mTitleBar.setPadding(0,
                0,
                0,
                0);
    }

    private void setResolutionInfo(int videoWidth, int videoHeight) {
        LogUtils.d("video resolution info : " + videoWidth + " x " + videoHeight);
//        mSurfaceHolder.setFixedSize(videoWidth, videoHeight);
        requestLayout();
    }

    private class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        /**
         * Notification that the progress level has changed. Clients can use the fromUser parameter
         * to distinguish user-initiated changes from those that occurred programmatically.
         *
         * @param seekBar  The SeekBar whose progress has changed
         * @param progress The current progress level. This will be in the range 0..max where max
         *                 was set by {@link ProgressBar#setMax(int)}. (The default value for max is 100.)
         * @param fromUser True if the progress change was initiated by the user.
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!fromUser) {
                // We're not interested in programmatically generated changes to
                // the progress bar's position.
                return;
            }

//            long duration = mFetchCore.getDuration();
//            long newposition = (duration * progress) / 1000L;
//            mFetchCore.setPlayPosition((int) newposition);
//            mFetchCore.seekTo((int) newposition);
//            if (mVideoTimeCurrentTv != null) {
//                mVideoTimeCurrentTv.setText(stringForTime((int) newposition));
//            }
            long duration = mVideoClientView2.getDuration();
            long newposition = (duration * progress) / 1000L;
//            mVideoClientView2.setPlayPosition((int) newposition);
            mVideoClientView2.seekTo((int) newposition);
            if (mVideoTimeCurrentTv != null) {
                mVideoTimeCurrentTv.setText(stringForTime((int) newposition));
            }
        }

        /**
         * Notification that the user has started a touch gesture. Clients may want to use this
         * to disable advancing the seekbar.
         *
         * @param seekBar The SeekBar in which the touch gesture began
         */
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mDragging = true;
            mHandler.removeMessages(SHOW_PROGRESS);
        }

        /**
         * Notification that the user has finished a touch gesture. Clients may want to use this
         * to re-enable advancing the seekbar.
         *
         * @param seekBar The SeekBar in which the touch gesture began
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mDragging = false;
            setProgress();

            mHandler.sendEmptyMessage(SHOW_PROGRESS);
        }
    }

    private int setProgress() {
//        if (mFetchCore == null || mDragging) {
//            return 0;
//        }
        if (mVideoClientView2 == null || mDragging) {
            return 0;
        }

//        int position = mFetchCore.getCurrentPosition();
//        int duration = (int) mFetchCore.getDuration();
//        if (mSeekBar != null) {
//            if (duration > 0) {
//                // use long to avoid overflow
//                long pos = 1000L * position / duration;
//                mSeekBar.setProgress((int) pos);
//            }
//            int percent = mFetchCore.getBufferPercentage();
//            mSeekBar.setSecondaryProgress(percent * 10);
//        }
//
//        if (mVideoTimeCurrentTv != null)
//            mVideoTimeCurrentTv.setText(stringForTime(position));
        int position = mVideoClientView2.getCurrentPosition();
        int duration = (int) mVideoClientView2.getDuration();
        if (mSeekBar != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                mSeekBar.setProgress((int) pos);
            }
            int percent = mVideoClientView2.getBufferPercentage();
            mSeekBar.setSecondaryProgress(percent * 10);
        }

        if (mVideoTimeCurrentTv != null)
            mVideoTimeCurrentTv.setText(stringForTime(position));

        return position;
    }

    /**
     * 转换播放时间
     *
     * @param timeMs 传入毫秒值
     */
    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private ViewClick mViewClick;

    public void setViewClick(ViewClick viewClick) {
        mViewClick = viewClick;
    }

    public interface ViewClick {
        /**
         * 退出
         */
        void back(View v);

        /**
         * 分享
         */
        void share(View v);

        /**
         * 关注
         */
        void follow(View v);

//        /**
//         * 发送消息
//         *
//         * @param message
//         */
//        void sendMessage(String message);
    }

    private IPushView mIPushView;

    public void setIPushView(IPushView iPushView) {
        mIPushView = iPushView;
    }

    public interface IPushView {
        /**
         * 主播关闭直播流
         */
        void closePush(View v);

        /**
         * 主播切换前后摄像头
         */
        void changeCamra(View v);

        /**
         * 闪光灯
         *
         * @param v
         */
        void flashLight(View v);

        /**
         * 分享
         *
         * @param v
         */
        void share(View v);
    }

    private ImeActionSend mImeActionSend;

    public void setImeActionSend(ImeActionSend imeActionSend) {
        mImeActionSend = imeActionSend;
    }

    public interface ImeActionSend {
        /**
         * 客户端聊天消息的发送
         *
         * @param message 消息内容
         */
        void onSendMessage(String message);
    }

    private IMediaError mIMediaError;

    public void setIMediaError(IMediaError IMediaError) {
        mIMediaError = IMediaError;
    }

    public interface IMediaError {
        boolean onError();
    }

    private int getLiveScreenStyle(Context context,AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HorizontalVideoRootView);
        int screenStyle = typedArray.getInt(R.styleable.HorizontalVideoRootView_liveScreenStyle, LIVE_SCREEN_VERTICAL);
        typedArray.recycle();
        return screenStyle;
    }

    /**
     * 得到状态栏的高度
     * @return
     */
    private int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
            return 75;
        }
    }

}
