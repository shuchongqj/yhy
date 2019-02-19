package com.mogujie.tt.ui.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.quanyan.yhy.R;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

/**
 * Created by zhujian on 15/1/14.
 */
public class IMBaseImageView extends AppCompatImageView {

    protected String imageUrl = null;
    protected int imageId;
    protected boolean isAttachedOnWindow = false;
    protected String avatarAppend = null;
    protected int defaultImageRes = R.mipmap.icon_default_avatar;
    protected int corner = 0;

    public IMBaseImageView(Context context) {
        super(context);
    }

    public IMBaseImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IMBaseImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImageUrl(String url) {
        this.imageUrl = url;
        if (isAttachedOnWindow) {
            if (!TextUtils.isEmpty(this.imageUrl) && this.imageUrl.equals(CommonUtil.matchUrl(this.imageUrl))) {
//                ImageLoaderUtil.getImageLoaderInstance().displayImage(this.imageUrl, this, ImageLoaderUtil.getAvatarOptions(corner, defaultImageRes));
                ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(this.imageUrl), defaultImageRes, this);
            } else {
                String defaultUri = "drawable://" + defaultImageRes;
                if (imageId != 0) {
                    defaultUri = "drawable://" + imageId;
                }
//                ImageLoaderUtil.getImageLoaderInstance().displayImage(defaultUri, this, ImageLoaderUtil.getAvatarOptions(corner, defaultImageRes));
                ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(this.imageUrl), defaultImageRes, this);

            }
        }
    }

    public void setImageId(int id) {
        this.imageId = id;
    }

    public void setDefaultImageRes(int defaultImageRes) {
        this.defaultImageRes = defaultImageRes;

    }

    public void setCorner(int corner) {
        this.corner = corner;
    }

//    @Deprecated
//    public void loadImage(String imageUrl, ImageSize imageSize, ImageLoaddingCallback imageLoaddingCallback) {
//        ImageLoaderUtil.getImageLoaderInstance().loadImage(imageUrl, imageSize, new ImageLoaddingListener(imageLoaddingCallback));
//    }

    public int getCorner() {
        return corner;
    }

    public int getDefaultImageRes() {
        return defaultImageRes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttachedOnWindow = true;
        setImageUrl(this.imageUrl);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.isAttachedOnWindow = false;
//        ImageLoaderUtil.getImageLoaderInstance().cancelDisplayTask(this);
    }

//    private static class ImageLoaddingListener extends SimpleImageLoadingListener {
//        private ImageLoaddingCallback imageLoaddingCallback;
//
//        public ImageLoaddingListener(ImageLoaddingCallback callback) {
//            this.imageLoaddingCallback = callback;
//        }
//
//        @Override
//        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//            imageLoaddingCallback.onLoadingComplete(imageUri, view, loadedImage);
//        }
//
//    }

//    public interface ImageLoaddingCallback {
//        void onLoadingComplete(String imageUri, View view, Bitmap loadedImage);
//    }
}
