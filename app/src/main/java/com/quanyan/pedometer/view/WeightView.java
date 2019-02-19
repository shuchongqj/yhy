package com.quanyan.pedometer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.quanyan.pedometer.utils.Tools;
import com.quanyan.yhy.R;

public class WeightView extends View {
    private int MARGIN_TOP = 10;

    private Paint linePaint;
    private Paint sectorPaint;
    private Paint pointerPaint;
    private Paint anglePaint;
    private Paint OutPaint;
    private int r = 0;
    private Context mContext;
    private int sWidth = 0;
    private int sHeight = 0;
    private float down_x, down_y, down_degree, move_x, move_y, current_degree;
    private float weight = 0;
    private float offest = 0; // 旋转偏移
    public static final int MARGIN_TOP_OF_ARC = 20;
    public static final int MARGIN_TOP_OF_SMALL_ARC = 120;
    public int GRADUATION_LONG = 30;
    public int GRADUATION_SHORT = 20;
    public static final int GRADUATION_TEXT_MARGIN_TOP_Y = 30;
    public static final int GRADUATION_TEXT_MARGIN_TOP_X = 0;
    private int touch_offset = 240;
    private OnWeightPickListener mOnWeightPickListener;

    private int defaultWeight = 65;
    private boolean isFirst = true;

    public interface OnWeightPickListener {
        void onPick(int weight);
    }

    public WeightView(Context context) {
        super(context);
        init(context);
    }

    public WeightView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        sWidth = context.getResources().getDisplayMetrics().widthPixels;
        sHeight = context.getResources().getDisplayMetrics().heightPixels;
        r = sWidth - Tools.dip2px(context,40);
//        if (r > sHeight) {
//            r = sHeight;
//        }
        initPaint();
    }

    Bitmap mBm;

    private void initPaint() {
        linePaint = new Paint();
        linePaint.setStrokeWidth(3);
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Style.FILL);

        anglePaint = new Paint();
        anglePaint.setColor(mContext.getResources().getColor(R.color.line_color));
        anglePaint.setAntiAlias(true);
        anglePaint.setTextSize(Tools.dip2px(mContext, 13));
        // anglePaint.setColor(Color.parseColor("#D1D1D1"));
        anglePaint.setAntiAlias(true);
        anglePaint.setStyle(Style.FILL);

        MARGIN_TOP = Tools.dip2px(mContext, MARGIN_TOP);
        GRADUATION_LONG = Tools.dip2px(mContext, GRADUATION_LONG);
        GRADUATION_SHORT = Tools.dip2px(mContext, GRADUATION_SHORT);

        OutPaint = new Paint();
        OutPaint.setStrokeWidth(20);
        OutPaint.setAntiAlias(true);
        OutPaint.setStyle(Style.STROKE);

        pointerPaint = new Paint();
        pointerPaint.setColor(Color.RED);
        pointerPaint.setStrokeWidth(3);
        pointerPaint.setAntiAlias(true);
        pointerPaint.setStyle(Style.FILL);

        sectorPaint = new Paint();
        // /第一个参数是阴影扩散半径，紧接着的2个参数是阴影在X和Y方向的偏移量，最后一个参数是颜色
        sectorPaint.setShadowLayer(10, 1, 1, Color.GRAY);
        sectorPaint.setColor(Color.parseColor("#D1D1D1"));
        sectorPaint.setStrokeWidth(2);
        sectorPaint.setAntiAlias(true);
        sectorPaint.setStyle(Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        drawTranspSegment(canvas);
//        drawSmallTranspSegment(canvas);
//        drawLineMethod(canvas);
        drawPointers(canvas);
        drawRedPointer(canvas);
        // drawWeightText(canvas);
    }

    private void drawLineMethod(Canvas canvas) {
        float startX, startX1 = 0, startY, startY1 = 0, stopX, stopX1 = 0, stopY, stopY1 = 0;
        startX = (float) (r / 2 - (r + MARGIN_TOP_OF_ARC)
                * Math.cos(65 * Math.PI / 180));
        startY = (float) (r + MARGIN_TOP - (r + MARGIN_TOP_OF_ARC)
                * Math.sin(65 * Math.PI / 180));

        stopX = (float) (r / 2 - (r - MARGIN_TOP_OF_SMALL_ARC)
                * Math.cos(65 * Math.PI / 180));
        stopY = (float) (r + MARGIN_TOP - ((r - MARGIN_TOP_OF_SMALL_ARC) * Math
                .sin(65 * Math.PI / 180)));
        canvas.drawLine(startX, startY, stopX, stopY, sectorPaint);

        startX1 = (float) (r / 2 + (r + MARGIN_TOP_OF_ARC)
                * Math.cos(65 * Math.PI / 180));
        startY1 = startY;

        stopX1 = (float) (r / 2 + (r - MARGIN_TOP_OF_SMALL_ARC)
                * Math.cos(65 * Math.PI / 180));
        stopY1 = stopY;
        canvas.drawLine(startX1, startY1, stopX1, stopY1, sectorPaint);
    }

    // private void drawWeightText(Canvas canvas) {
    // canvas.drawText(weight+"KG", r/2, MARGIN_TOP/2, anglePaint);
    // }

    private void drawTranspSegment(Canvas canvas) {
        sectorPaint.setStyle(Style.STROKE);
        canvas.drawArc(new RectF(-MARGIN_TOP_OF_ARC - r / 2, MARGIN_TOP
                - MARGIN_TOP_OF_ARC, r + MARGIN_TOP_OF_ARC + r / 2, r
                + MARGIN_TOP + MARGIN_TOP_OF_ARC + r), 245, 50, false,
                sectorPaint);
    }

    private void drawSmallTranspSegment(Canvas canvas) {
        sectorPaint.setStyle(Style.STROKE);
        canvas.drawArc(new RectF(MARGIN_TOP_OF_SMALL_ARC - r / 2, MARGIN_TOP
                + MARGIN_TOP_OF_SMALL_ARC,
                -MARGIN_TOP_OF_SMALL_ARC + r + r / 2, -MARGIN_TOP_OF_SMALL_ARC
                        + r + MARGIN_TOP + r), 245, 50, false, sectorPaint);
    }


    public void setDefaultWeight(int defaultWeight) {
        if (defaultWeight < 0){
            defaultWeight = 0;
        }
        if (defaultWeight > 270){
            defaultWeight = 270;
        }
        this.defaultWeight = defaultWeight;
    }

    private void drawPointers(Canvas canvas) {
        FontMetrics fm = anglePaint.getFontMetrics();
        linePaint.setColor(Color.BLACK);
        linePaint.setStyle(Style.FILL);

        if (isFirst){
            touch_offset = 270 - defaultWeight;
            isFirst = false;
        }

        float currentRadian = 0;
        for (float i = 0; i <= 360; i++) {

            currentRadian = touch_offset + i + offest;
            if (currentRadian + 2 >= 270 && currentRadian + 2 <= 272) {
                // 计算当前所指刻度
                int f = (int) ((270 - currentRadian) / 2 * 10);
                if (i + ((float) f) / 10 < 0) {
                    weight = 0;
                } else if (i + ((float) f) / 10 > 360) {
                    weight = 360;
                } else
                    weight = i + ((float) f) / 10;
                if (mOnWeightPickListener != null) {
                    mOnWeightPickListener.onPick((int) weight);
                }
            }

            float startX = 0, startY = 0, stopX = 0, stopY = 0;
            float textOffestY = 20;
            // float textW = anglePaint.measureText((int)i+"");
            float textH = (float) (Math.ceil(fm.descent - fm.ascent) + 2);
            anglePaint.setTextAlign(Align.CENTER);
            textOffestY = 0 + textH;
            textOffestY += 10;
            if (currentRadian >= 242 && currentRadian <= 278) {
                currentRadian = currentRadian - 180;
                if (i % 5 != 0) {
                    startX = (float) (r / 2 - r
                            * Math.cos(currentRadian * Math.PI / 180));
                    startY = (float) (r + MARGIN_TOP - r
                            * Math.sin(currentRadian * Math.PI / 180));
                    stopX = (float) (r / 2 - (r - GRADUATION_SHORT)
                            * Math.cos(currentRadian * Math.PI / 180));
                    stopY = (float) (r + MARGIN_TOP - ((r - GRADUATION_SHORT) * Math
                            .sin(currentRadian * Math.PI / 180)));
                    canvas.drawLine(startX, startY, stopX, stopY, linePaint);
                } else {
                    startX = (float) (r / 2 - r
                            * Math.cos(currentRadian * Math.PI / 180));
                    startY = (float) (r + MARGIN_TOP - r
                            * Math.sin(currentRadian * Math.PI / 180));
                    stopX = (float) (r / 2 - (r - GRADUATION_LONG)
                            * Math.cos(currentRadian * Math.PI / 180));
                    stopY = (float) (r + MARGIN_TOP - ((r - GRADUATION_LONG) * Math
                            .sin(currentRadian * Math.PI / 180)));
                    canvas.drawLine(startX, startY, stopX, stopY, linePaint);
                    if (i % 10 == 0) {
                        canvas.drawText((int) i + "", stopX
                                + GRADUATION_TEXT_MARGIN_TOP_X, stopY
                                + textOffestY, anglePaint);
                    }
                }
            } else if (currentRadian >= 262 && currentRadian <= 298) {
                currentRadian = 360 - currentRadian;
                if (i % 5 != 0) {
                    startX = (float) (r / 2 + r
                            * Math.cos(currentRadian * Math.PI / 180));
                    startY = (float) (r + MARGIN_TOP - r
                            * Math.sin(currentRadian * Math.PI / 180));
                    stopX = (float) (r / 2 + (r - GRADUATION_SHORT)
                            * Math.cos(currentRadian * Math.PI / 180));
                    stopY = (float) (r + MARGIN_TOP - ((r - GRADUATION_SHORT) * Math
                            .sin(currentRadian * Math.PI / 180)));
                    canvas.drawLine(startX, startY, stopX, stopY, linePaint);
                } else {
                    startX = (float) (r / 2 + r
                            * Math.cos(currentRadian * Math.PI / 180));
                    startY = (float) (r + MARGIN_TOP - r
                            * Math.sin(currentRadian * Math.PI / 180));
                    stopX = (float) (r / 2 + (r - GRADUATION_LONG)
                            * Math.cos(currentRadian * Math.PI / 180));
                    stopY = (float) (r + MARGIN_TOP - ((r - GRADUATION_LONG) * Math
                            .sin(currentRadian * Math.PI / 180)));
                    canvas.drawLine(startX, startY, stopX, stopY, linePaint);
                    if (i % 10 == 0) {
                        canvas.drawText((int) i + "", stopX
                                + GRADUATION_TEXT_MARGIN_TOP_X, stopY
                                + +textOffestY, anglePaint);
                    }
                }
            }
        }
    }

    private void drawRedPointer(Canvas canvas) {
        canvas.drawLine(r / 2, MARGIN_TOP + r, r / 2, MARGIN_TOP, pointerPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN: {
            down_x = event.getX();
            down_y = event.getY();
            down_degree = detaDegree(r / 2, MARGIN_TOP + r / 2, down_x, down_y);
            break;

        }
        case MotionEvent.ACTION_MOVE: {
            move_x = event.getX();
            move_y = event.getY();
            current_degree = detaDegree(r / 2, MARGIN_TOP + r / 2, move_x,
                    move_y);
            // 滑过的弧度增量
            offest = current_degree - down_degree;
            if (touch_offset + offest > 270) {
                touch_offset = 270;
                offest = 0;
            } else if (touch_offset + offest < -90) {
                touch_offset = -90;
                offest = 0;
            }
            invalidate();

            break;
        }
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP: {
            // up_x = event.getX();
            // up_y = event.getY();
            touch_offset += offest;
            offest = 0;
            invalidate();
            break;
        }
        }
        return true;
    }

    /**
     * 计算以(src_x,src_y)为坐标圆点，建立直角体系，求出(target_x,target_y)坐标与x轴的夹角
     */
    float detaDegree(float src_x, float src_y, float target_x, float target_y) {

        float detaX = target_x - src_x;
        float detaY = target_y - src_y;
        double d;
        if (detaX != 0) {
            float tan = Math.abs(detaY / detaX);

            if (detaX > 0) {

                if (detaY >= 0) {
                    d = Math.atan(tan);

                } else {
                    d = 2 * Math.PI - Math.atan(tan);
                }

            } else {
                if (detaY >= 0) {

                    d = Math.PI - Math.atan(tan);
                } else {
                    d = Math.PI + Math.atan(tan);
                }
            }

        } else {
            if (detaY > 0) {
                d = Math.PI / 2;
            } else {

                d = -Math.PI / 2;
            }
        }

        return (float) ((d * 180) / Math.PI);
    }

    public void setOnWeightPickListener(OnWeightPickListener lsn) {
        mOnWeightPickListener = lsn;
    }
}