package com.videolibrary.core;

/**
 * Created with Android Studio.
 * Title:MediaTypeInfo
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/8/10
 * Time:15:46
 * Version 1.1.0
 */
public class VideoTypeInfo {
    /**
     * <p>
     *     MediaType
     * </p>
     */
    public VideoType mMediaType;
    /**
     * 视频地址
     */
    public String mMediaURL;

    public boolean isClient;

    public enum VideoType {
        MP4,
        RTMP
    }

    public VideoTypeInfo(VideoType mediaType, String mediaURL, boolean isClient) {
        mMediaType = mediaType;
        mMediaURL = mediaURL;
        this.isClient = isClient;
    }
}
