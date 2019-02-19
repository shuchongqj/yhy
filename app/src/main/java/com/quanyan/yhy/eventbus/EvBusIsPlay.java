package com.quanyan.yhy.eventbus;
/**
 * Company:quanyan
 * Author:wjm
 * Date:3/31/16
 * 音频文件停止播放
 */
public class EvBusIsPlay {
    private boolean isPlay;




    public EvBusIsPlay(boolean isPlay)
    {
        this.isPlay=isPlay;
    }

    public boolean getIsPlay()
    {
        return isPlay;
    }
}
