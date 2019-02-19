package com.videolibrary.core;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.lidroid.xutils.util.LogUtils;

import java.io.IOException;

/**
 * Created with Android Studio.
 * Title:FetchCore
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/8/3
 * Time:17:13
 * Version 1.1.0
 */
public class VideoClientView extends SurfaceView implements SurfaceHolder.Callback2,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnInfoListener {

    private MediaPlayer mMediaPlayer;
    private SurfaceHolder mSurfaceHolder;

    private String mMediaURL;
    private int mPlayPosition = 0;

    public VideoClientView(Context context, String mediaURL) {
        super(context);
        mMediaURL = mediaURL;
        init(context);
    }

    public VideoClientView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VideoClientView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VideoClientView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        // 设置mSurfaceHolder
        mSurfaceHolder = getHolder();
        // 设置Holder类型,该类型表示mSurfaceView自己不管理缓存区,虽然提示过时，但最好还是要设置
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 设置surface回调
        mSurfaceHolder.addCallback(this);
    }

    /**
     * Called when the application needs to redraw the content of its
     * surface, after it is resized or for some other reason.  By not
     * returning from here until the redraw is complete, you can ensure that
     * the user will not see your surface in a bad state (at its new
     * size before it has been correctly drawn that way).  This will
     * typically be preceeded by a call to {@link #surfaceChanged}.
     *
     * @param holder The SurfaceHolder whose surface has changed.
     */
    @Override
    public void surfaceRedrawNeeded(SurfaceHolder holder) {

    }

    /**
     * This is called immediately after the surface is first created.
     * Implementations of this should start up whatever rendering code
     * they desire.  Note that only one thread can ever draw into
     * a {@link android.view.Surface}, so you should not draw into the Surface here
     * if your normal rendering will be in another thread.
     *
     * @param holder The SurfaceHolder whose surface is being created.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        LogUtils.v("the surface view is created!!!");

        // surfaceView被创建
        // 设置播放资源
        playVideo();
    }

    /**
     * 播放视频
     */
    public void playVideo() {
        // 初始化MediaPlayer
        mMediaPlayer = new MediaPlayer();
        // 重置mediaPlay,建议在初始化mediaPlay时立即调用。
        mMediaPlayer.reset();
        // 设置播放完成监听
        mMediaPlayer.setOnCompletionListener(this);
        // 设置媒体加载完成以后回调函数。
        mMediaPlayer.setOnPreparedListener(this);
        // 错误监听回调函数
        mMediaPlayer.setOnErrorListener(this);
        // 设置缓存变化监听
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnSeekCompleteListener(this);
        mMediaPlayer.setOnInfoListener(this);
//        Uri uri = Uri.parse(mMediaURL);
        try {
            // mMediaPlayer.reset();
//            mMediaPlayer.setDataSource(getContext(), uri);
            mMediaPlayer.setDataSource(mMediaURL);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mMediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            }
            // 设置异步加载视频，包括两种方式 prepare()同步，prepareAsync()异步
            mMediaPlayer.prepareAsync();
        } catch (IllegalStateException e) {
           LogUtils.e(e.getMessage());
        } catch (IOException e) {
            LogUtils.e(e.getMessage());
        } catch (SecurityException e) {
            LogUtils.e(e.getMessage());
        } catch (IllegalArgumentException e) {
            LogUtils.e(e.getMessage());
        }
    }

    /**
     * This is called immediately after any structural changes (format or
     * size) have been made to the surface.  You should at this point update
     * the imagery in the surface.  This method is always called at least
     * once, after {@link #surfaceCreated}.
     *
     * @param holder The SurfaceHolder whose surface has changed.
     * @param format The new PixelFormat of the surface.
     * @param width  The new width of the surface.
     * @param height The new height of the surface.
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        LogUtils.v("the surface view is changed!!!");

    }

    /**
     * This is called immediately before a surface is being destroyed. After
     * returning from this call, you should no longer try to access this
     * surface.  If you have a rendering thread that directly accesses
     * the surface, you must ensure that thread is no longer touching the
     * Surface before returning from this function.
     *
     * @param holder The SurfaceHolder whose surface is being destroyed.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtils.v("the surface view is destroyed!!!");
        // surfaceView销毁
        // 如果MediaPlayer没被销毁，则销毁mMediaPlayer
//        mHandler.removeMessages(SHOW_PROGRESS);
        if (mIMediaPlay != null) {
            mIMediaPlay.onDestroy();
        }
        if (null != mMediaPlayer) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /**
     * Called to update status in buffering a media stream received through
     * progressive HTTP download. The received buffering percentage
     * indicates how much of the content has been buffered or played.
     * For example a buffering update of 80 percent when half the content
     * has already been played indicates that the next 30 percent of the
     * content to play has been buffered.
     *
     * @param mp      the MediaPlayer the update pertains to
     * @param percent the percentage (0-100) of the content
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        LogUtils.d("percent : " + percent);
        mBufferPercent = percent;
    }

    private int mBufferPercent = 0;

    public int getBufferPercentage() {
        return mBufferPercent;
    }

    /**
     * Called when the end of a media source is reached during playback.
     *
     * @param mp the MediaPlayer that reached the end of the file
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        LogUtils.d("over playing!!!!");
        mp.reset();
//        mHandler.removeMessages(SHOW_PROGRESS);
        if (mIMediaPlay != null) {
            mIMediaPlay.onCompletion();
        }
    }

    /**
     * Called to indicate an error.
     *
     * @param mp    the MediaPlayer the error pertains to
     * @param what  the type of error that has occurred:
     *              <ul>
     *              <li>{@link MediaPlayer#MEDIA_ERROR_UNKNOWN}
     *              <li>{@link MediaPlayer#MEDIA_ERROR_SERVER_DIED}
     *              </ul>
     * @param extra an extra code, specific to the error. Typically
     *              implementation dependent.
     *              <ul>
     *              <li>{@link MediaPlayer#MEDIA_ERROR_IO}
     *              <li>{@link MediaPlayer#MEDIA_ERROR_MALFORMED}
     *              <li>{@link MediaPlayer#MEDIA_ERROR_UNSUPPORTED}
     *              <li>{@link MediaPlayer#MEDIA_ERROR_TIMED_OUT}
     *              </ul>
     * @return True if the method handled the error, false if it didn't.
     * Returning false, or not having an OnErrorListener at all, will
     * cause the OnCompletionListener to be called.
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
//        mHandler.removeMessages(SHOW_PROGRESS);
        if (mIMediaPlay != null) {
            mIMediaPlay.onError(mp, what, extra);
        }
        return false;
    }

    /**
     * Called to indicate an info or a warning.
     *
     * @param mp    the MediaPlayer the info pertains to.
     * @param what  the type of info or warning.
     *              <ul>
     *              <li>{@link MediaPlayer#MEDIA_INFO_UNKNOWN}
     *              <li>{@link MediaPlayer#MEDIA_INFO_VIDEO_TRACK_LAGGING}
     *              <li>{@link MediaPlayer#MEDIA_INFO_VIDEO_RENDERING_START}
     *              <li>{@link MediaPlayer#MEDIA_INFO_BUFFERING_START}
     *              <li>{@link MediaPlayer#MEDIA_INFO_BUFFERING_END}
     *              <li>{@link MediaPlayer#MEDIA_INFO_BAD_INTERLEAVING}
     *              <li>{@link MediaPlayer#MEDIA_INFO_NOT_SEEKABLE}
     *              <li>{@link MediaPlayer#MEDIA_INFO_METADATA_UPDATE}
     *              <li>{@link MediaPlayer#MEDIA_INFO_UNSUPPORTED_SUBTITLE}
     *              <li>{@link MediaPlayer#MEDIA_INFO_SUBTITLE_TIMED_OUT}
     *              </ul>
     * @param extra an extra code, specific to the info. Typically
     *              implementation dependent.
     * @return True if the method handled the info, false if it didn't.
     * Returning false, or not having an OnErrorListener at all, will
     * cause the info to be discarded.
     */
    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        LogUtils.i("what : " + what + "    " + "extra : " + extra);
        return false;
    }

    public void setPlayPosition(int playPosition) {
        mPlayPosition = playPosition;
    }

    /**
     * Called when the media file is ready for playback.
     *
     * @param mp the MediaPlayer that is ready for playback
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
//        // 当视频加载完毕以后，隐藏加载进度条
//        mProgressBar.setVisibility(View.GONE);
        // 设置显示到屏幕
        mMediaPlayer.setDisplay(mSurfaceHolder);
        // 设置声音效果
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        // 设置surfaceView保持在屏幕上
        mMediaPlayer.setScreenOnWhilePlaying(true);
//        setResolutionInfo(mMediaPlayer.getVideoWidth(), mMediaPlayer.getVideoHeight());
        // 判断是否有保存的播放位置,防止屏幕旋转时，界面被重新构建，播放位置丢失。
        if (mPlayPosition >= 0) {
            mMediaPlayer.seekTo((int) mPlayPosition);
            mPlayPosition = -1;
//             mSurfaceHolder.unlockCanvasAndPost(Constants.getCanvas());
        }
        // 播放视频
        mMediaPlayer.start();
        mSurfaceHolder.setKeepScreenOn(true);
        if (mIMediaPlay != null) {
            mIMediaPlay.prepared(mMediaPlayer.getVideoWidth(), mMediaPlayer.getVideoHeight(), mMediaPlayer.getDuration());
        }
//        // 设置控制条,放在加载完成以后设置，防止获取getDuration()错误
//        mSeekBar.setMax(1000);
//        mSeekBar.setProgress(0);
//        // 设置播放时间
//        mVideoTimeString = stringForTime(mMediaPlayer.getDuration());
//        mVideoTimeCurrentTv.setText("00:00");
//        mVideoTimeTotalTv.setText(mVideoTimeString);
//        // 设置拖动监听事件
//        mSeekBar.setOnSeekBarChangeListener(new SeekBarChangeListener());

//        mHandler.sendEmptyMessage(SHOW_PROGRESS);
    }

    /**
     * Called to indicate the completion of a seek operation.
     *
     * @param mp the MediaPlayer that issued the seek operation
     */
    @Override
    public void onSeekComplete(MediaPlayer mp) {
        mp.start();
    }

    private IMediaPlay mIMediaPlay;

    public void setIMediaPlay(IMediaPlay IMediaPlay) {
        mIMediaPlay = IMediaPlay;
    }

    public boolean isPlaying() {
        return mMediaPlayer == null ? false : mMediaPlayer.isPlaying();
    }

    public long getDuration() {
        return mMediaPlayer == null ? 0 : mMediaPlayer.getDuration();
    }

    public void seekTo(int newposition) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(newposition);
        }
    }

    public void pause() {
        if(mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    public void start(){
        if(mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    public int getCurrentPosition() {
        return mMediaPlayer == null ? 0 : mMediaPlayer.getCurrentPosition();
    }

    public interface IMediaPlay {
        void prepared(int videoWidth, int videoHeight, int duration);

        void onCompletion();

        void onError(MediaPlayer mp, int what, int extra);

        void onDestroy();
    }
}
