package com.newyhy.views.ninelayout;

import android.content.Context;

import com.newyhy.utils.DisplayUtils;

/**
 * 适配圈子各种比例图片
 * Created by Jiervs on 2018/4/21.
 */

public class FixPicUtils {

    public static float UgcContentWidth;
    public static float NineItemWidth;


    public static PicSizeInfo getFixImage(Context context, float originalWidth, float originalHeight) {

        UgcContentWidth = DisplayUtils.getScreenWidth(context) - DisplayUtils.dp2px(context,30);//FrameLayout宽度;
        NineItemWidth = ( UgcContentWidth - 10 ) / 3 ;//Nine Item 边长 (正方形);
        PicSizeInfo size = new PicSizeInfo();
        if ( originalWidth > originalHeight) {//横向图
            float width = originalWidth;
            if (width > 2 * NineItemWidth - 5) {//宽度大于 2个九宫格
                width = 2 * NineItemWidth - 5;
                float height = originalHeight/originalWidth * width;
                if (height < NineItemWidth ) {
                    height = NineItemWidth;//高度修正
                }
                size.width = width;
                size.height = height;
            } else if (width > NineItemWidth) {//宽度处于1 个九宫格 - 2个九宫格 之间
                float height = originalHeight/originalWidth * width;
                if (height < NineItemWidth ) {
                    height = NineItemWidth;//高度修正
                }
                size.width = width;
                size.height = height;
            } else {
                width = NineItemWidth;
                float height = originalHeight/originalWidth * width;
                if (height < NineItemWidth ) {
                    height = NineItemWidth;//高度修正
                }
                size.width = width;
                size.height = height;
            }
        }

        if ( originalWidth <= originalHeight) {//竖向图
            if (originalHeight/originalWidth > 2) size.isSuperHeight = true;
            float height = originalHeight;
            if (height > 2 * NineItemWidth - 5) {//高度大于 2个九宫格
                height = 2 * NineItemWidth - 5;
                float width = originalWidth/originalHeight * height;
                if (width < NineItemWidth ) {
                    width = NineItemWidth;//宽度修正
                }
                size.width = width;
                size.height = height;
            } else if (height > NineItemWidth) {//高度处于1 个九宫格 - 2个九宫格 之间
                float width = originalWidth/originalHeight * height;
                if (width < NineItemWidth ) {
                    width = NineItemWidth;//宽度修正
                }
                size.width = width;
                size.height = height;
            } else {
                height = NineItemWidth;
                float width = originalWidth/originalHeight * height;
                if (width < NineItemWidth ) {
                    width = NineItemWidth;//宽度修正
                }
                size.width = width;
                size.height = height;
            }
        }
        return size;
    }

    public static class PicSizeInfo {
        public float width;
        public float height;
        public boolean isSuperHeight;
    }
}
