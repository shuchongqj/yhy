package com.harwkin.nb.camera.pop;


import com.harwkin.nb.camera.options.CameraOptions;

/**
 * @author zhaocheng
 * 
 */
public interface CameraPopListener {

    /**
     * click openCamrea
     */
    void onCamreaClick(CameraOptions options);

    /**
     * click openPick
     */
    void onPickClick(CameraOptions options);

    /**
     * del image click
     */
    void onDelClick();
    //小视屏录制
    void onVideoClick();
    //小视频草稿
    //void onVideoDraftClick();

}
