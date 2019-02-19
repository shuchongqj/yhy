package com.quanyan.pedometer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.quanyan.pedometer.utils.Tools;


public class GardientRingView extends LinearLayout {

    private SweepGradient mGradientSweep;
    private Paint mArcPaint;
    private int mCenterX;
    private int mCenterY;
    private int mRadius;
    private int mStartAngle = 270;
    private int mSweepAngleBegin = 1;
    private int mSweepAngleEnd = 300;
    private int mDrawSpeed = 20;
    private int mStrokeWidth = 50;
    private int mOutterCircleStrokeWidth = 10;
    private int[] mColors = { Color.RED, Color.GREEN, Color.BLUE, Color.RED };
    private Paint mDividerPaint;
    private Paint mOutterCirclePaint;
    private Paint mArcBehindCirclePaint;
    private Paint mInnerCirclePaint;

    private final String DEFAULT_NAMESPACE = "http://schemas.android.com/apk/res/android";

    public GardientRingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        setBackgroundColor(0x000000);
        String wStr = attrs
                .getAttributeValue(DEFAULT_NAMESPACE, "layout_width");
        String hStr = attrs.getAttributeValue(DEFAULT_NAMESPACE,
                "layout_height");
        int w = Tools.dip2px(context,
                Integer.valueOf(wStr.substring(0, wStr.indexOf("."))));
        int h = Tools.dip2px(context,
                Integer.valueOf(hStr.substring(0, hStr.indexOf("."))));
        mCenterX = w / 2;
        mCenterY = h / 2;
        mRadius = w / 2 - mStrokeWidth / 2 - mOutterCircleStrokeWidth - 1;

        mGradientSweep = new SweepGradient(mCenterX, mCenterY, mColors, null);
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true); // 消除锯齿
        mArcPaint.setStyle(Paint.Style.STROKE); // 绘制空心圆
        mArcPaint.setStrokeWidth(mStrokeWidth);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);
        mArcPaint.setStrokeJoin(Paint.Join.MITER);
        mArcPaint.setShader(mGradientSweep);

        mOutterCirclePaint = new Paint();
        mOutterCirclePaint.setColor(0xff999999);
        mOutterCirclePaint.setAntiAlias(true);
        mOutterCirclePaint.setStyle(Paint.Style.STROKE);
        mOutterCirclePaint.setStrokeWidth(mOutterCircleStrokeWidth);

        mArcBehindCirclePaint = new Paint();
        mArcBehindCirclePaint.setColor(0xffaaaaaa);
        mArcBehindCirclePaint.setAntiAlias(true);
        mArcBehindCirclePaint.setStyle(Paint.Style.STROKE);
        mArcBehindCirclePaint.setStrokeWidth(mStrokeWidth);

        mInnerCirclePaint = new Paint();
        mInnerCirclePaint.setColor(0xffcccccc);
        mInnerCirclePaint.setAntiAlias(true);

        mDividerPaint = new Paint();
        mDividerPaint.setColor(0xff111111);
        mDividerPaint.setAntiAlias(true);
        mDividerPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        canvas.drawCircle(mCenterX, mCenterY, mRadius + mStrokeWidth / 2
                + mOutterCircleStrokeWidth / 2, mOutterCirclePaint);
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mArcBehindCirclePaint);
        canvas.drawCircle(mCenterX, mCenterY, mRadius - mStrokeWidth / 2,
                mInnerCirclePaint);

        canvas.drawCircle(mCenterX, mCenterY, mRadius + mStrokeWidth / 2
                + mOutterCircleStrokeWidth, mDividerPaint);
        canvas.drawCircle(mCenterX, mCenterY, mRadius + mStrokeWidth / 2,
                mDividerPaint);
        canvas.drawCircle(mCenterX, mCenterY, mRadius - mStrokeWidth / 2,
                mDividerPaint);

        canvas.drawArc(new RectF(mCenterX - mRadius, mCenterY - mRadius,
                mCenterX + mRadius, mCenterY + mRadius), mStartAngle,
                mSweepAngleBegin, false, mArcPaint);
        if (mSweepAngleBegin < mSweepAngleEnd) {
            mSweepAngleBegin += 2;
            postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    postInvalidate();
                }
            }, mDrawSpeed);
        }
    }
}