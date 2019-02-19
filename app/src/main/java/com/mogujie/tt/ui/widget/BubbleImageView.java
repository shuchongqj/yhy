package com.mogujie.tt.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.NinePatch;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.quanyan.yhy.R;

/**
 * Created by zhujian on 15/2/13.
 */
public class BubbleImageView extends ImageView {
    /**
     * 图片设置相关
     */
    protected String imageUrl = null;
    protected boolean isAttachedOnWindow = false;

    protected ImageLoaddingCallback imageLoaddingCallback;

    private NinePatch foreNinePatch = null;

    public BubbleImageView(Context context) {
        super(context);
    }

    public BubbleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BubbleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (foreNinePatch == null) return;
        foreNinePatch.draw(canvas, new RectF(getLeft(), getTop(), getRight(), getBottom()));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttachedOnWindow = true;
//        setImageUrl(this.imageUrl);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.isAttachedOnWindow = false;
//        ImageLoaderUtil.getImageLoaderInstance().cancelDisplayTask(this);
    }

    public void initForeBitmap(boolean isMine) {
        int resId = isMine ? R.drawable.tt_mine_image_bg : R.drawable.tt_other_image_bg;
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resId);
        foreNinePatch = new NinePatch(bmp, bmp.getNinePatchChunk(), null);
    }

    public interface ImageLoaddingCallback {
        void onLoadingComplete(String imageUri, View view, Bitmap loadedImage);

        void onLoadingStarted(String imageUri, View view);

        void onLoadingCanceled(String imageUri, View view);

        void onLoadingFailed(String imageUri, View view);
    }


}
