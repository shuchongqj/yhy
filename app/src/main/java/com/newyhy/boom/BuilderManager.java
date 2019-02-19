package com.newyhy.boom;


import android.graphics.Color;

import com.quanyan.yhy.R;
import com.yhy.boombutton.BoomButtons.SimpleCircleButton;
import com.yhy.boombutton.BoomButtons.TextOutsideCircleButton;

public class BuilderManager {

    private static int[] imageResources = new int[]{
            R.mipmap.fawenzi,
            R.mipmap.camera,
            R.mipmap.picture,
            R.mipmap.record,
            R.mipmap.live,
            R.mipmap.topic,
            R.mipmap.club,
            R.mipmap.activity,
    };

    private static String[] textStrings = new String[]{
            "文字",
            "拍摄",
            "相册",
            "小视频",
            "直播",
            "话题",
            "俱乐部",
            "活动",
    };

    static SimpleCircleButton.Builder getSimpleCircleButtonBuilder() {
        return new SimpleCircleButton.Builder()
                .normalImageRes(getImageResource());
    }

    public static TextOutsideCircleButton.Builder getTextOutsideCircleButtonBuilder() {
        return new TextOutsideCircleButton.Builder()
                .normalImageRes(getImageResource())
                .normalText(getStringResource())
                .textSize(12)
                .normalTextColor(Color.BLACK);
    }

    private static int imageResourceIndex = 0;

    static int getImageResource() {
        if (imageResourceIndex >= imageResources.length) imageResourceIndex = 0;
        return imageResources[imageResourceIndex];
    }

    static String getStringResource() {
        if (imageResourceIndex >= textStrings.length) imageResourceIndex = 0;
        return textStrings[imageResourceIndex++];
    }
}
