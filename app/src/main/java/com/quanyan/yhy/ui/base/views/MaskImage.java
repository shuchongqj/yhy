package com.quanyan.yhy.ui.base.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import org.akita.util.AndroidUtil;

/**
 * IM 图片显示
 * 
 * @author zhaocheng
 * 
 */
public class MaskImage extends ImageView {

    public static final float GROWSMALLSCALE = 0.15f; // 规定长图片 比例最低值
    public static final float WIDESMALLSCALE = 10.5f; // 规定宽图片 比例最低值
    public static final float HALL_SCALE = 1.5f;

    private Bitmap zommImage = null; // 处理后的图片
    private TypedArray a;
    private boolean sendMessage = false;
    private int maskSoure = 0;
    private int maskClickSource = 0;
    private boolean clickImage = false; // 是否点击图片

    public MaskImage(Context context) {
        super(context);
    }

    public MaskImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        a = context.getTheme().obtainStyledAttributes(
                attrs,
                AndroidUtil.getResourceDeclareStyleableIntArray(context,
                        "ImImageView"), 0, 0);
        sendMessage = a.getBoolean(AndroidUtil.getStyleableResourceInt(context,
                "ImImageView_sendMessage"), false);
        if (a != null) {
            a.recycle();
        }

    }

    public MaskImage(Context context, AttributeSet attrs) {
        super(context, attrs);

        a = context.getTheme().obtainStyledAttributes(
                attrs,
                AndroidUtil.getResourceDeclareStyleableIntArray(context,
                        "ImImageView"), 0, 0);
        sendMessage = a.getBoolean(AndroidUtil.getStyleableResourceInt(context,
                "ImImageView_sendMessage"), false);
        if (a != null) {
            a.recycle();
        }

    }

    public void sendOrFrom() {
//        if (sendMessage) {
//            maskSoure = R.drawable.chat_img_to_bg_mask_press;
//            maskClickSource = R.drawable.chat_send_img_to_bg_mask_press;
//        } else {
//            maskSoure = R.drawable.chat_img_from_bg_mask;
//            maskClickSource = R.drawable.chat_from_img_to_bg_mask_press;
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            clickImage = true;
            invalidate();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            clickImage = false;
            invalidate();
        }
        if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            clickImage = false;
            invalidate();
        }

        return super.onTouchEvent(event);
    }

    //
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        // 判断图片是否为空
        if (drawable != null && drawable instanceof BitmapDrawable) {
            Bitmap original = ((BitmapDrawable) drawable).getBitmap();
            // 转换为Bitmap的是否为Null
            if (original != null) {
                showMask(canvas, original);
            }
        }

    }

    private void showMask(Canvas canvas, Bitmap original) {
        float width = original.getWidth();
        float height = original.getHeight();
        conductImg(original, width, height);
        int x = 0;
        int y = 0;
        int sc = canvas.saveLayer(x, y, x + this.getWidth(),
                y + this.getHeight(), null, Canvas.MATRIX_SAVE_FLAG
                        | Canvas.CLIP_SAVE_FLAG
                        | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
                        | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
                        | Canvas.CLIP_TO_LAYER_SAVE_FLAG);
        Rect rf = new Rect(0, 0, this.getWidth(), this.getHeight());
        // 显示模型样式 发送遮罩 接受遮罩
        sendOrFrom();
        // .9图片改变大小
        NinePatchDrawable npd = (NinePatchDrawable) getResources().getDrawable(
                maskSoure);
        npd.setBounds(rf);
        // 将遮罩图片绘制到画板
        npd.draw(canvas);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // 图片交接处显示之前的样式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(zommImage, 0, 0, paint);
        // 图片点击 添加一层图片 实现点击图片切换样式
        if (clickImage) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
            NinePatchDrawable click = (NinePatchDrawable) getResources()
                    .getDrawable(maskClickSource);
            click.setBounds(rf);
            click.draw(canvas);
        }

        canvas.restoreToCount(sc);
    }

    private void conductImg(Bitmap original, float width, float height) {
        if ((width / height) < GROWSMALLSCALE
                && original.getHeight() > this.getHeight()) {
            zommImage = cutBitmap(original);
        } else if ((width / height) > WIDESMALLSCALE
                && original.getWidth() > this.getWidth()) {
            zommImage = cutBitmapWidth(original);
        } else {
            zommImage = zoomImg(original, this.getWidth(), this.getHeight(),
                    false);
        }
    }

    public Bitmap cutBitmapWidth(Bitmap bm) {
        int width = bm.getWidth() / 2;
        Bitmap createBitmap = null;

        if (width - this.getWidth() / 2 > 0) {
            createBitmap = Bitmap.createBitmap(bm,
                    bm.getWidth() / 2 - this.getWidth() / 2, 0,
                    this.getWidth(), bm.getHeight());
        } else {
            createBitmap = Bitmap.createBitmap(bm, bm.getWidth() / 2, 0,
                    bm.getWidth(), this.getHeight());
        }
        if (createBitmap.getHeight() < this.getHeight()) {
            Bitmap zoomImg = zoomImg(createBitmap, this.getWidth(),
                    this.getHeight(), false);
            createBitmap.recycle();
            createBitmap = null;
            return zoomImg;
        }
        return createBitmap;
    }

    public Bitmap cutBitmap(Bitmap bm) {
        int height = bm.getHeight() / 2;
        Bitmap createBitmap = null;

        if (height - this.getHeight() / 2 > 0) {
            createBitmap = Bitmap.createBitmap(bm, 0,
                    bm.getHeight() / 2 - this.getHeight() / 2, bm.getWidth(),
                    this.getHeight());
        } else {
            createBitmap = Bitmap.createBitmap(bm, 0, bm.getHeight() / 2,
                    bm.getWidth(), this.getHeight());
        }
        if (createBitmap.getWidth() < this.getWidth()) {
            Bitmap zoomImg = zoomImg(createBitmap, this.getWidth(),
                    this.getHeight(), false);
            createBitmap.recycle();
            createBitmap = null;
            return zoomImg;
        }
        return createBitmap;
    }

    // 缩放图片

    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight,
            boolean recycle) {
        if (bm.getWidth() == newWidth && bm.getHeight() == newHeight) {
            return bm;
        } else {
            // 获得图片的宽高
            int width = bm.getWidth();

            int height = bm.getHeight();

            // 计算缩放比例

            float scaleWidth = ((float) newWidth) / width;

            float scaleHeight = ((float) newHeight) / height;

            // 取得想要缩放的matrix参数

            Matrix matrix = new Matrix();

            matrix.postScale(scaleWidth, scaleHeight);

            Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                    true);
            if (recycle) {
                if (newbm != bm) {
                    bm.recycle(); // 回收掉原图
                    bm = null;
                }
            }

            return newbm;
        }

    }

}
