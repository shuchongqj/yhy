package com.newyhy.utils.live;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

import static com.tencent.rtmp.TXLiveConstants.ENCODE_VIDEO_AUTO;
import static com.tencent.rtmp.TXLiveConstants.NET_STATUS_NET_SPEED;
import static com.tencent.rtmp.TXLiveConstants.NET_STATUS_VIDEO_BITRATE;
import static com.tencent.rtmp.TXLiveConstants.NET_STATUS_VIDEO_FPS;
import static com.tencent.rtmp.TXLiveConstants.NET_STATUS_VIDEO_HEIGHT;
import static com.tencent.rtmp.TXLiveConstants.NET_STATUS_VIDEO_WIDTH;


public class LiveHelper implements ITXLivePushListener {

    private static LiveHelper liveHelper = null;
    private TXLivePusher mLivePusher;
    private TXLivePushConfig liveConfig;

    public static synchronized LiveHelper getLiveHelper(){
        if (liveHelper == null){
            liveHelper = new LiveHelper();
        }
        return liveHelper;
    }

    public void initPublisher(Context context){
        if (mLivePusher == null) mLivePusher = new TXLivePusher(context);
    }

    public void startCameraPreview(TXCloudVideoView mCaptureView){
        mLivePusher.setConfig(liveConfig);
        mLivePusher.startCameraPreview(mCaptureView);
    }

    public void  initLiveConfig(){
        liveConfig = new TXLivePushConfig();
        //自动硬件加速
        liveConfig.setHardwareAcceleration(ENCODE_VIDEO_AUTO);
        liveConfig.setFrontCamera(false);
        liveConfig.setTouchFocus(false);
    }

    /**
     * 设置摄像头清晰度
     * @param liveQuality
     */
    public void setLiveQuality(int liveQuality){
        mLivePusher.setVideoQuality(liveQuality,false,false);
    }

    /**
     * 切换摄像头
     */
    public void switchCamera(){
     mLivePusher.switchCamera();
    }

    /**
     * 打开闪光灯
     * @param mFlashTurnOn
     */
    public void turnOnFlash(boolean mFlashTurnOn){
        mLivePusher.turnOnFlashLight(mFlashTurnOn);
    }

    /**
     * 设置是否开启镜像翻转
     * @param mirror
     */
    public void setMirror(boolean mirror){
        mLivePusher.setMirror(mirror);
    }

    public void pausePush(){
        mLivePusher.pausePusher();
    }

    public void resumePush(){
        mLivePusher.resumePusher();
    }

    /**
     * 设置旋转角度
     */
    public void setPushOrientation(int pushOrientation){
        liveConfig.setHomeOrientation(pushOrientation);
    }

    /**
     * 设置主播端旋转角度
     * @param anchorOrientation
     */
    public void setAnchorOrientation(int anchorOrientation){
        mLivePusher.setRenderRotation(anchorOrientation);
    }

    public void startRtmpPublish(String rtmpUrl){
        mLivePusher.setPushListener(this);
        mLivePusher.startPusher(rtmpUrl);
    }

    public void stopRtmpPublish(){
        mLivePusher.stopCameraPreview(true); //停止摄像头预览
        mLivePusher.stopPusher();            //停止推流
        mLivePusher.setPushListener(null);   //解绑 listener
    }

    public void stopRtmp(){
        mLivePusher.stopPusher();            //停止推流
        mLivePusher.setPushListener(null);   //解绑 listener
    }

    public TXLivePushConfig getConfig(){
        return liveConfig;
    }

    public TXLivePusher getmLivePusher(){
        return mLivePusher;
    }

    /**
     PUSH_ERR_OPEN_CAMERA_FAIL	-1301	打开摄像头失败
     PUSH_ERR_OPEN_MIC_FAIL	-1302	打开麦克风失败
     PUSH_ERR_VIDEO_ENCODE_FAIL	-1303	视频编码失败
     PUSH_ERR_AUDIO_ENCODE_FAIL	-1304	音频编码失败
     PUSH_ERR_UNSUPPORTED_RESOLUTION	-1305	不支持的视频分辨率
     PUSH_ERR_UNSUPPORTED_SAMPLERATE	-1306	不支持的音频采样率
     PUSH_ERR_NET_DISCONNECT	-1307	网络断连,且经三次抢救无效,可以放弃治疗,更多重试请自行重启推流
     */
    @Override
    public void onPushEvent(int i, Bundle bundle) {

    }

    /**
     PUSH_WARNING_NET_BUSY	1101	网络状况不佳：上行带宽太小，上传数据受阻
     PUSH_WARNING_RECONNECT	1102	网络断连, 已启动自动重连 (自动重连连续失败超过三次会放弃)
     PUSH_WARNING_HW_ACCELERATION_FAIL	1103	硬编码启动失败，采用软编码
     PUSH_WARNING_DNS_FAIL	3001	RTMP -DNS 解析失败（会触发重试流程）
     PUSH_WARNING_SEVER_CONN_FAIL	3002	RTMP 服务器连接失败（会触发重试流程）
     PUSH_WARNING_SHAKE_FAIL	3003	RTMP 服务器握手失败（会触发重试流程）
     PUSH_WARNING_SERVER_DISCONNECT	3004	RTMP 服务器主动断开连接（会触发重试流程）
     */
    @Override
    public void onNetStatus(Bundle bundle) {
       /* Log.e("EZEZ","width ＝ "+ bundle.getInt(NET_STATUS_VIDEO_WIDTH,0) +
                "  height = "+bundle.getInt(NET_STATUS_VIDEO_HEIGHT,0));
        Log.e("EZEZ","pushSpeed = "+ bundle.getInt(NET_STATUS_NET_SPEED,0));
        Log.e("EZEZ","bitrate = "+bundle.getInt(NET_STATUS_VIDEO_BITRATE,0));
        Log.e("EZEZ","FPS = "+bundle.getInt(NET_STATUS_VIDEO_FPS,0));*/
    }


}
