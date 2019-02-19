package com.videolibrary.core;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.MediaController;

import com.lidroid.xutils.util.LogUtils;
import com.quanyan.yhy.R;
import com.tencent.ijk.media.player.IMediaPlayer;
import com.tencent.ijk.media.player.IjkMediaPlayer;
import com.yhy.common.types.AppDebug;

import java.io.IOException;


/**
 * Created with Android Studio.
 * Title:VideoClientView2
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:9/12/16
 * Time:13:52
 * Version 1.1.0
 */
public class VideoClientView2 extends SurfaceView implements SurfaceHolder.Callback2, MediaController.MediaPlayerControl {

    private String mVideoUrl;
    // all possible internal states
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PREPARED = 2;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;

    // mCurrentState is a VideoView object's current state.
    // mTargetState is the state that a method caller intends to reach.
    // For instance, regardless the VideoView object's current state,
    // calling pause() intends to bring the object to a target state
    // of STATE_PAUSED.
    private int mCurrentState = STATE_IDLE;
    private int mTargetState = STATE_IDLE;

    private IMediaPlayer.OnCompletionListener mOnCompletionListener;
    private IMediaPlayer.OnPreparedListener mOnPreparedListener;
    private int mCurrentBufferPercentage;
    private IMediaPlayer.OnErrorListener mOnErrorListener;
    private IMediaPlayer.OnInfoListener mOnInfoListener;
    private IMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener;
    private int mSeekWhenPrepared;  // recording the seek position while preparing
    private boolean mCanPause = true;
    private boolean mCanSeekBack = true;
    private boolean mCanSeekForward = true;

    private SurfaceHolder mSurfaceHolder;

    private IjkMediaPlayer mIjkMediaPlayer;

    private long mPrepareStartTime = 0;
    private long mPrepareEndTime = 0;

    private long mSeekStartTime = 0;
    private long mSeekEndTime = 0;

    public VideoClientView2(Context context, String videoTypeInfo) {
        super(context);
        init(context, videoTypeInfo);
    }

    private void init(Context context, String videoTypeInfo) {
        this.mVideoUrl = videoTypeInfo;
        // 设置mSurfaceHolder
        mSurfaceHolder = getHolder();
        // 设置Holder类型,该类型表示mSurfaceView自己不管理缓存区,虽然提示过时，但最好还是要设置
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 设置surface回调
        mSurfaceHolder.addCallback(this);
    }

    public void setVideoUrl(String videoUrl) {
        try {
            if (mIjkMediaPlayer != null) {
                if (mTargetState != STATE_IDLE) {
                    mIjkMediaPlayer.reset();
                }
                mIjkMediaPlayer.setDataSource(videoUrl);
            }
        } catch (IOException e) {
            LogUtils.e("the url is unavailable.");
        } catch (IllegalStateException e) {
            mIjkMediaPlayer.reset();
        }
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

        if (mIjkMediaPlayer != null) {
            mIjkMediaPlayer.setDisplay(mSurfaceHolder);
        } else {
            // surfaceView被创建
            // 设置播放资源
            initPlayer();
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
        if (mIjkMediaPlayer != null) {
            mIjkMediaPlayer.setDisplay(null);
        }

        if (mIPlayerControl != null) {
            mIPlayerControl.onSurfaceDestroy();
        }
    }

    private void initPlayer() {
        // we shouldn't clear the target state, because somebody might have
        // called start() previously
        release(false);

        AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        try {
            mIjkMediaPlayer = new IjkMediaPlayer();
            if(AppDebug.DEBUG_LOG) {
                mIjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);
            }else {
                mIjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_ERROR);
            }

            /**
             * static const AVOption ffp_context_options[] = {
             // original options in ffplay.c
             // FFP_MERGE: x, y, s, fs
             { "an",                             "disable audio",
             OPTION_OFFSET(audio_disable),   OPTION_INT(0, 0, 1) },
             { "vn",                             "disable video",
             OPTION_OFFSET(video_disable),   OPTION_INT(0, 0, 1) },
             // FFP_MERGE: sn, ast, vst, sst
             // TODO: ss
             { "nodisp",                         "disable graphical display",
             OPTION_OFFSET(display_disable), OPTION_INT(0, 0, 1) },
             // FFP_MERGE: f, pix_fmt, stats
             { "fast",                           "non spec compliant optimizations",
             OPTION_OFFSET(fast),            OPTION_INT(0, 0, 1) },
             // FFP_MERGE: genpts, drp, lowres, sync, autoexit, exitonkeydown, exitonmousedown
             { "loop",                           "set number of times the playback shall be looped",
             OPTION_OFFSET(loop),            OPTION_INT(1, INT_MIN, INT_MAX) },
             { "infbuf",                         "don't limit the input buffer size (useful with realtime streams)",
             OPTION_OFFSET(infinite_buffer), OPTION_INT(0, 0, 1) },
             { "framedrop",                      "drop frames when cpu is too slow",
             OPTION_OFFSET(framedrop),       OPTION_INT(0, -1, 120) },
             { "seek-at-start",                  "set offset of player should be seeked",
             OPTION_OFFSET(seek_at_start),       OPTION_INT64(0, 0, INT_MAX) },
             // FFP_MERGE: window_title
             #if CONFIG_AVFILTER
             { "af",                             "audio filters",
             OPTION_OFFSET(afilters),        OPTION_STR(NULL) },
             { "vf0",                            "video filters 0",
             OPTION_OFFSET(vfilter0),        OPTION_STR(NULL) },
             #endif
             { "rdftspeed",                      "rdft speed, in msecs",
             OPTION_OFFSET(rdftspeed),       OPTION_INT(0, 0, INT_MAX) },
             // FFP_MERGE: showmode, default, i, codec, acodec, scodec, vcodec
             // TODO: autorotate

             // extended options in ff_ffplay.c
             { "max-fps",                        "drop frames in video whose fps is greater than max-fps",
             OPTION_OFFSET(max_fps),         OPTION_INT(31, -1, 121) },

             { "overlay-format",                 "fourcc of overlay format",
             OPTION_OFFSET(overlay_format),  OPTION_INT(SDL_FCC_RV32, INT_MIN, INT_MAX),
             .unit = "overlay-format" },
             { "fcc-_es2",                       "", 0, OPTION_CONST(SDL_FCC__GLES2), .unit = "overlay-format" },
             { "fcc-i420",                       "", 0, OPTION_CONST(SDL_FCC_I420), .unit = "overlay-format" },
             { "fcc-yv12",                       "", 0, OPTION_CONST(SDL_FCC_YV12), .unit = "overlay-format" },
             { "fcc-rv16",                       "", 0, OPTION_CONST(SDL_FCC_RV16), .unit = "overlay-format" },
             { "fcc-rv24",                       "", 0, OPTION_CONST(SDL_FCC_RV24), .unit = "overlay-format" },
             { "fcc-rv32",                       "", 0, OPTION_CONST(SDL_FCC_RV32), .unit = "overlay-format" },

             { "start-on-prepared",                  "automatically start playing on prepared",
             OPTION_OFFSET(start_on_prepared),   OPTION_INT(1, 0, 1) },

             { "video-pictq-size",                   "max picture queue frame count",
             OPTION_OFFSET(pictq_size),          OPTION_INT(VIDEO_PICTURE_QUEUE_SIZE_DEFAULT,
             VIDEO_PICTURE_QUEUE_SIZE_MIN,
             VIDEO_PICTURE_QUEUE_SIZE_MAX) },

             { "max-buffer-size",                    "max buffer size should be pre-read",
             OPTION_OFFSET(dcc.max_buffer_size), OPTION_INT(MAX_QUEUE_SIZE, 0, MAX_QUEUE_SIZE) },
             { "min-frames",                         "minimal frames to stop pre-reading",
             OPTION_OFFSET(dcc.min_frames),      OPTION_INT(DEFAULT_MIN_FRAMES, MIN_MIN_FRAMES, MAX_MIN_FRAMES) },
             { "first-high-water-mark-ms",           "first chance to wakeup read_thread",
             OPTION_OFFSET(dcc.first_high_water_mark_in_ms),
             OPTION_INT(DEFAULT_FIRST_HIGH_WATER_MARK_IN_MS,
             DEFAULT_FIRST_HIGH_WATER_MARK_IN_MS,
             DEFAULT_LAST_HIGH_WATER_MARK_IN_MS) },
             { "next-high-water-mark-ms",            "second chance to wakeup read_thread",
             OPTION_OFFSET(dcc.next_high_water_mark_in_ms),
             OPTION_INT(DEFAULT_NEXT_HIGH_WATER_MARK_IN_MS,
             DEFAULT_FIRST_HIGH_WATER_MARK_IN_MS,
             DEFAULT_LAST_HIGH_WATER_MARK_IN_MS) },
             { "last-high-water-mark-ms",            "last chance to wakeup read_thread",
             OPTION_OFFSET(dcc.last_high_water_mark_in_ms),
             OPTION_INT(DEFAULT_LAST_HIGH_WATER_MARK_IN_MS,
             DEFAULT_FIRST_HIGH_WATER_MARK_IN_MS,
             DEFAULT_LAST_HIGH_WATER_MARK_IN_MS) },

             { "packet-buffering",                   "pause output until enough packets have been read after stalling",
             OPTION_OFFSET(packet_buffering),    OPTION_INT(1, 0, 1) },
             { "sync-av-start",                      "synchronise a/v start time",
             OPTION_OFFSET(sync_av_start),       OPTION_INT(1, 0, 1) },
             { "iformat",                            "force format",
             OPTION_OFFSET(iformat_name),        OPTION_STR(NULL) },
             { "no-time-adjust",                     "return player's real time from the media stream instead of the adjusted time",
             OPTION_OFFSET(no_time_adjust),      OPTION_INT(0, 0, 1) },
             { "preset-5-1-center-mix-level",        "preset center-mix-level for 5.1 channel",
             OPTION_OFFSET(preset_5_1_center_mix_level), OPTION_DOUBLE(M_SQRT1_2, -32, 32) },

             // Android only options
             { "mediacodec",                             "MediaCodec: enable H264 (deprecated by 'mediacodec-avc')",
             OPTION_OFFSET(mediacodec_avc),          OPTION_INT(0, 0, 1) },
             { "mediacodec-auto-rotate",                 "MediaCodec: auto rotate frame depending on meta",
             OPTION_OFFSET(mediacodec_auto_rotate),  OPTION_INT(0, 0, 1) },
             { "mediacodec-all-videos",                  "MediaCodec: enable all videos",
             OPTION_OFFSET(mediacodec_all_videos),   OPTION_INT(0, 0, 1) },
             { "mediacodec-avc",                         "MediaCodec: enable H264",
             OPTION_OFFSET(mediacodec_avc),          OPTION_INT(0, 0, 1) },
             { "mediacodec-hevc",                        "MediaCodec: enable HEVC",
             OPTION_OFFSET(mediacodec_hevc),         OPTION_INT(0, 0, 1) },
             { "mediacodec-mpeg2",                       "MediaCodec: enable MPEG2VIDEO",
             OPTION_OFFSET(mediacodec_mpeg2),        OPTION_INT(0, 0, 1) },
             { "mediacodec-mpeg4",                       "MediaCodec: enable MPEG4",
             OPTION_OFFSET(mediacodec_mpeg4),        OPTION_INT(0, 0, 1) },
             { "mediacodec-handle-resolution-change",                    "MediaCodec: handle resolution change automatically",
             OPTION_OFFSET(mediacodec_handle_resolution_change),     OPTION_INT(0, 0, 1) },
             { "opensles",                           "OpenSL ES: enable",
             OPTION_OFFSET(opensles),            OPTION_INT(0, 0, 1) },

             { NULL }
             };
             */
//            mIjkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "infbuf", 1);
//            mIjkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
            mIjkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-avc", 1);
            //below options is used while "mediacodec" is 1;
//            mIjkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 0);//using media codec auto rotate
//            mIjkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 0);//media codec handle resolution change

            mIjkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0);
            mIjkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
            mIjkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
            mIjkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
            mIjkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "analyzeduration", "2000000");
            mIjkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "fflags", "nobuffer");
            mIjkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "probsize", "4096");

            mIjkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);
            mIjkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 0);

            // REMOVED: mAudioSession
            mIjkMediaPlayer.setOnPreparedListener(mPreparedListener);
            mIjkMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
            mIjkMediaPlayer.setOnCompletionListener(mCompletionListener);
            mIjkMediaPlayer.setOnErrorListener(mErrorListener);
            mIjkMediaPlayer.setOnInfoListener(mInfoListener);
            mIjkMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            mIjkMediaPlayer.setOnSeekCompleteListener(mSeekCompleteListener);
            mCurrentBufferPercentage = 0;

            if (!TextUtils.isEmpty(mVideoUrl)) {
                LogUtils.v("the rtmp url is -->> " + mVideoUrl);
                mIjkMediaPlayer.setDataSource(mVideoUrl);
            }
            mIjkMediaPlayer.setDisplay(mSurfaceHolder);
            mIjkMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mIjkMediaPlayer.setScreenOnWhilePlaying(true);
            mPrepareStartTime = System.currentTimeMillis();

            // REMOVED: mPendingSubtitleTracks

            // we don't set the target state here either, but preserve the
            // target state that was there before.
            mCurrentState = STATE_PREPARING;
        } catch (IOException ex) {
            LogUtils.w("Unable to open content: " + mVideoUrl, ex);
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;
            mErrorListener.onError(mIjkMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
        } catch (IllegalArgumentException ex) {
            LogUtils.w("Unable to open content: " + mVideoUrl, ex);
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;
            mErrorListener.onError(mIjkMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
        } finally {
            // REMOVED: mPendingSubtitleTracks.clear();
        }
    }

    public void openVideo() {
        if (mIjkMediaPlayer != null) {
            mIjkMediaPlayer.prepareAsync();
        }
    }

    public void resetVideo(){
        release(true);
        surfaceDestroyed(mSurfaceHolder);
        surfaceCreated(mSurfaceHolder);
    }

    public void stopPlayback() {
        if (mIjkMediaPlayer != null) {
            mIjkMediaPlayer.stop();
            mIjkMediaPlayer.release();
            mIjkMediaPlayer = null;
            mCurrentState = STATE_IDLE;
            mTargetState = STATE_IDLE;
            AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            am.abandonAudioFocus(null);
        }
    }

    /*
     * release the media player in any state
     */
    public void release(boolean cleartargetstate) {
        if (mIjkMediaPlayer != null) {
            mIjkMediaPlayer.reset();
            mIjkMediaPlayer.release();
            mIjkMediaPlayer = null;
            // REMOVED: mPendingSubtitleTracks.clear();
            mCurrentState = STATE_IDLE;
            if (cleartargetstate) {
                mTargetState = STATE_IDLE;
            }
            AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            am.abandonAudioFocus(null);
        }
    }

    IMediaPlayer.OnPreparedListener mPreparedListener = new IMediaPlayer.OnPreparedListener() {
        public void onPrepared(IMediaPlayer mp) {
            mPrepareEndTime = System.currentTimeMillis();
            mCurrentState = STATE_PREPARED;

            // Get the capabilities of the player for this stream
            // REMOVED: Metadata

            if (mOnPreparedListener != null) {
                mOnPreparedListener.onPrepared(mIjkMediaPlayer);
            }

            int seekToPosition = mSeekWhenPrepared;  // mSeekWhenPrepared may be changed after seekTo() call
            if (seekToPosition != 0) {
                seekTo(seekToPosition);
            }

            // We didn't actually change the size (it was already at the size
            // we need), so we won't get a "surface changed" callback, so
            // start the video here instead of in the callback.
            if (mTargetState == STATE_PLAYING) {
                start();
            } else if (!isPlaying() &&
                    (seekToPosition != 0 || getCurrentPosition() > 0)) {

            }
        }
    };

    private IMediaPlayer.OnCompletionListener mCompletionListener =
            new IMediaPlayer.OnCompletionListener() {
                public void onCompletion(IMediaPlayer mp) {
                    mCurrentState = STATE_PLAYBACK_COMPLETED;
                    mTargetState = STATE_PLAYBACK_COMPLETED;
                    if (mOnCompletionListener != null) {
                        mOnCompletionListener.onCompletion(mIjkMediaPlayer);
                    }
                }
            };

    private IMediaPlayer.OnErrorListener mErrorListener =
            new IMediaPlayer.OnErrorListener() {
                public boolean onError(IMediaPlayer mp, int framework_err, int impl_err) {
                    LogUtils.d("Error: " + framework_err + "," + impl_err);
                    mCurrentState = STATE_ERROR;
                    mTargetState = STATE_ERROR;

                    /* If an error handler has been supplied, use it and finish. */
                    if (mOnErrorListener != null) {
                        if (mOnErrorListener.onError(mIjkMediaPlayer, framework_err, impl_err)) {
                            return true;
                        }
                    }

                    /* Otherwise, pop up an error InterestMultiPageDialog so the user knows that
                     * something bad has happened. Only try and pop up the InterestMultiPageDialog
                     * if we're attached to a window. When we're going away and no
                     * longer have a window, don't bother showing the user an error.
                     */
                    if (getWindowToken() != null) {
                        Resources r = getContext().getResources();
                        int messageId;

                        if (framework_err == MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK) {
                            messageId = R.string.VideoView_error_text_invalid_progressive_playback;
                        } else {
                            messageId = R.string.VideoView_error_text_unknown;
                        }

                        new AlertDialog.Builder(getContext())
                                .setMessage(messageId)
                                .setPositiveButton(R.string.VideoView_error_button,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                            /* If we get here, there is no onError listener, so
                                             * at least inform them that the video is over.
                                             */
                                                if (mOnCompletionListener != null) {
                                                    mOnCompletionListener.onCompletion(mIjkMediaPlayer);
                                                }
                                            }
                                        })
                                .setCancelable(false)
                                .show();
                    }
                    return true;
                }
            };

    private IMediaPlayer.OnInfoListener mInfoListener =
            new IMediaPlayer.OnInfoListener() {
                public boolean onInfo(IMediaPlayer mp, int arg1, int arg2) {
                    if (mOnInfoListener != null) {
                        mOnInfoListener.onInfo(mp, arg1, arg2);
                    }
                    return true;
                }
            };

    private IMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener =
            new IMediaPlayer.OnBufferingUpdateListener() {
                public void onBufferingUpdate(IMediaPlayer mp, int percent) {
                    mCurrentBufferPercentage = percent;
                }
            };

    private IMediaPlayer.OnSeekCompleteListener mSeekCompleteListener = new IMediaPlayer.OnSeekCompleteListener() {

        @Override
        public void onSeekComplete(IMediaPlayer mp) {
            mSeekEndTime = System.currentTimeMillis();
        }
    };

    private IMediaPlayer.OnVideoSizeChangedListener mSizeChangedListener = new IMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
            if (mOnVideoSizeChangedListener != null) {
                mOnVideoSizeChangedListener.onVideoSizeChanged(mp, width, height, sar_num, sar_den);
            }
        }
    };

    /**
     * Register a callback to be invoked when the media file
     * is loaded and ready to go.
     *
     * @param l The callback that will be run
     */
    public void setOnPreparedListener(IMediaPlayer.OnPreparedListener l) {
        mOnPreparedListener = l;
    }

    /**
     * Register a callback to be invoked when the end of a media file
     * has been reached during playback.
     *
     * @param l The callback that will be run
     */
    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener l) {
        mOnCompletionListener = l;
    }

    /**
     * Register a callback to be invoked when an error occurs
     * during playback or setup.  If no listener is specified,
     * or if the listener returned false, VideoView will inform
     * the user of any errors.
     *
     * @param l The callback that will be run
     */
    public void setOnErrorListener(IMediaPlayer.OnErrorListener l) {
        mOnErrorListener = l;
    }

    /**
     * Register a callback to be invoked when an informational event
     * occurs during playback or setup.
     *
     * @param l The callback that will be run
     */
    public void setOnInfoListener(IMediaPlayer.OnInfoListener l) {
        mOnInfoListener = l;
    }

    /**
     * Register a callback to be invoked when the video size changed
     *
     * @param l
     */
    public void setOnVideoSizeChangedListener(IMediaPlayer.OnVideoSizeChangedListener l) {
        mOnVideoSizeChangedListener = l;
    }

    @Override
    public void start() {
        if (isInPlaybackState()) {
            mIjkMediaPlayer.start();
            mCurrentState = STATE_PLAYING;
        }
        mTargetState = STATE_PLAYING;
    }

    @Override
    public void pause() {
        if (isInPlaybackState()) {
            if (mIjkMediaPlayer.isPlaying()) {
                mIjkMediaPlayer.pause();
                mCurrentState = STATE_PAUSED;
            }
        }
        mTargetState = STATE_PAUSED;
    }

    @Override
    public int getDuration() {
        if (isInPlaybackState()) {
            return (int) mIjkMediaPlayer.getDuration();
        }

        return -1;
    }

    @Override
    public int getCurrentPosition() {
        if (isInPlaybackState()) {
            return (int) mIjkMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void seekTo(int pos) {
        if (isInPlaybackState()) {
            mSeekStartTime = System.currentTimeMillis();
            mIjkMediaPlayer.seekTo(pos);
            mSeekWhenPrepared = 0;
        } else {
            mSeekWhenPrepared = pos;
        }
    }

    private boolean isInPlaybackState() {
        return (mIjkMediaPlayer != null &&
                mCurrentState != STATE_ERROR &&
                mCurrentState != STATE_IDLE &&
                mCurrentState != STATE_PREPARING);
    }

    @Override
    public boolean isPlaying() {
        return isInPlaybackState() && mIjkMediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        if (mIjkMediaPlayer != null) {
            return mCurrentBufferPercentage;
        }
        return 0;
    }

    @Override
    public boolean canPause() {
        return mCanPause;
    }

    @Override
    public boolean canSeekBackward() {
        return mCanSeekBack;
    }

    @Override
    public boolean canSeekForward() {
        return mCanSeekForward;
    }

    /**
     * Get the audio session id for the player used by this VideoView. This can be used to
     * apply audio effects to the audio track of a video.
     *
     * @return The audio session, or 0 if there was an error.
     */
    @Override
    public int getAudioSessionId() {
        return 0;
    }

    private IPlayerControl mIPlayerControl;

    public void setIPlayerControl(IPlayerControl IPlayerControl) {
        mIPlayerControl = IPlayerControl;
    }

    public interface IPlayerControl {
        void onSurfaceDestroy();
    }
}
