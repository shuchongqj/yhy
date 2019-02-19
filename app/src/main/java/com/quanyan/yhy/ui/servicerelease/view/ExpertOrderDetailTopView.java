package com.quanyan.yhy.ui.servicerelease.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.lidroid.xutils.util.LogUtils;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ConsultingState;

/**
 * Created with Android Studio.
 * Title:***Acitivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:王东旭
 * Date:2016/6/1
 * Time:16:46
 * Version 2.0
 */
public class ExpertOrderDetailTopView extends View {
    //进度数字,文字的颜色
    Paint mDotStatePaint;
    //当前状态的颜色
    Paint mCurrentStatusPoint;
    //
    Point mPoint;
    int scale = 50;
    int radius = 50;
    int mCurrentStateTextSize = 40;
    int strokeWith = 6;
    int circleStrokeWith = 8;
    int length = 150;//间距
    int textDist = 18;//线间距
    int textLeft = 62;
    int describeBottom = 120;
    int textStateYScale = 80;
    Context context;

    public ExpertOrderDetailTopView(Context context) {
        super(context);
        init(context, null);
    }

    float[] pts = {170, 150, 270, 150,
            370, 150, 470, 150,
            570, 150, 670, 150,
            770, 150, 870, 150};
    String[] stateNameNo = new String[]{"待下单", "待付款", "待确认", "待服务", "待评价"};
    String[] stateNameAlready = new String[]{"已下单", "已付款", "已确认", "已完成", "已评价"};
    String stateName;
    //状态描述
    String mStatusDescribeInfo;
    private boolean onlyDrawText = false;


    /**
     * 布局的宽
     */
    private int mMeasureWidth = 0;
    /**
     * 布局的高
     */
    private int mMeasureHeight = 0;

    /**
     * onDraw过程中消除锯齿
     */
    private PaintFlagsDrawFilter mDrawFilter;
    /**
     * 绘制状态文本的paint
     */
    private Paint mTextPaint;
    /**
     * 绘制圆环的paint
     */
    private Paint mCirclePaint;
    /**
     * 绘制圆环之间连线的paint
     */
    private Paint mCircleLinePaint;
    /**
     * 绘制圆环内部文本的paint
     */
    private Paint mCircleTextPaint;
    /**
     * 状态文本的大小
     */
    private float mStateTextSize = sp2px(getContext(), 12);
    /**
     * 圆环内部文本的大小
     */
    private float mCircleTextSize = sp2px(getContext(), 15);
    /**
     * 底部提示文本的大小
     */
    private float mStateNumTextSize = sp2px(getContext(), 18);

    /**
     * 圆环的半径
     */
    private int mCircleRadius = dip2px(getContext(), 18);
    /**
     * 圆环的宽度
     */
    private int mCircleStrokeWidth = dip2px(getContext(), 5);

    /**
     * 数组大小
     */
    private int mLength = 5;
    /**
     * 圆心的位置数组
     */
    private Point[] mCenter = new Point[5];

    /**
     * 当前状态
     */
    private int mCurrentConsultState = ConsultingState.CONSULT_IN_QUEUE;

    public ExpertOrderDetailTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, null);
    }

    public ExpertOrderDetailTopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, null);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExpertOrderDetailTopView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasureWidth = getMeasuredWidth();
        mMeasureHeight = getMeasuredHeight();

        caculateThePoint();
        LogUtils.v("measure width --> " + mMeasureWidth + "  measure height --> " + mMeasureHeight);
    }

    /**
     * Returns whether or not this View draws on its own.
     *
     * @return true if this view has nothing to draw, false otherwise
     */
    @Override
    public boolean willNotDraw() {
        return false;
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        mCurrentStateTextSize = getResources().getDimensionPixelSize(R.dimen.dd_dimen_24px);
        initPoint();

        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(mStateTextSize);

        mCirclePaint = new Paint();
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mCircleStrokeWidth);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(getResources().getColor(R.color.red_ying));

        mCircleTextPaint = new Paint();
        mCircleTextPaint.setAntiAlias(true);
        mCircleTextPaint.setColor(getResources().getColor(R.color.red_ying));
        mCircleTextPaint.setTextSize(mCircleTextSize);

        mCircleLinePaint = new Paint();
        mCircleLinePaint.setAntiAlias(true);
        mCircleLinePaint.setColor(getResources().getColor(R.color.red_ying));
        mCircleLinePaint.setStrokeWidth(mCircleStrokeWidth);
        mCircleLinePaint.setStrokeCap(Paint.Cap.SQUARE);


        mCurrentStatusPoint = new Paint();
//        mCurrentStatusPoint.setColor(getResources().getColor(R.color.white));
        mCurrentStatusPoint.setColor(getResources().getColor(R.color.black));
        mCurrentStatusPoint.setTextSize(mStateNumTextSize);
        mCurrentStatusPoint.setStyle(Paint.Style.STROKE);
        mCurrentStatusPoint.setAntiAlias(true);
    }

    /**
     * 在onMeasure完成之后计算圆心的位置
     */
    private void caculateThePoint() {
        int dividWidth = (mMeasureWidth - getPaddingLeft() - getPaddingRight() - mCircleRadius * 2) / 4;
        int centerY = mMeasureHeight / 2;
        int firstX = getPaddingLeft() + mCircleRadius;
        Point point1 = new Point(firstX, centerY);
        int secondX = firstX + dividWidth;
        Point point2 = new Point(secondX, centerY);
        int thirdX = secondX + dividWidth;
        Point point3 = new Point(thirdX, centerY);
        int fourthX = thirdX + dividWidth;
        Point point4 = new Point(fourthX, centerY);
        int fiveX = fourthX + dividWidth;
        Point point5 = new Point(fiveX, centerY);

        mCenter[0] = point1;
        mCenter[1] = point2;
        mCenter[2] = point3;
        mCenter[3] = point4;
        mCenter[4] = point5;
    }

    /**
     * 设置服务的当前状态
     * @param state
     */
    public void setCurrentConsultState(int state) {
        mCurrentConsultState = state;
        postInvalidate();
    }

    //咨询订单状态
    private int mConsultState;//state 0-4

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //抗锯齿
        canvas.setDrawFilter(mDrawFilter);

//        if (!onlyDrawText) {
//            initPaint();
//            initWhitePaint();
//            drawLineSelect(canvas);
//            drawLineUnSelect(canvas);
//            drawCircleAndTextSelect(canvas);
//        }
        if (mConsultState == ConsultingState.CANCEL) {
            return;
        }
        switch (mCurrentConsultState) {
            case ConsultingState.WAITING_PAY:
            case ConsultingState.CONSULT_IN_QUEUE: {
                drawContent(canvas, 2);
                break;
            }
            case ConsultingState.CONSULT_IN_CHAT: {
                drawContent(canvas, 3);
                break;
            }
            case ConsultingState.CANCEL:
                break;
            case ConsultingState.FINISH:
                drawContent(canvas, 4);
                break;
            case ConsultingState.RATED:
                drawContent(canvas, 5);
                break;
        }
        drawDescribeInfo(canvas);
    }

    /**
     * 绘制要显示的内容
     * @param canvas 画布
     * @param state 当前点亮的位置
     */
    private void drawContent(Canvas canvas, int state){
        mCirclePaint.setColor(getResources().getColor(R.color.red_ying));
        mCircleTextPaint.setColor(getResources().getColor(R.color.red_ying));
        mCircleLinePaint.setColor(getResources().getColor(R.color.red_ying));
        for (int i = 0; i < mLength; i++) {
            if (i < state) {
                Rect textBound = getTextBound(mTextPaint, stateNameAlready[i]);
                canvas.drawText(stateNameAlready[i],
                        mCenter[i].x - textBound.width() / 2,
                        mCenter[i].y - mCircleRadius - 10 - textBound.height() / 2,
                        mTextPaint);
            } else {
                Rect textBound = getTextBound(mTextPaint, stateNameNo[i]);
                canvas.drawText(stateNameNo[i],
                        mCenter[i].x - textBound.width() / 2,
                        mCenter[i].y - mCircleRadius - 10 - textBound.height() / 2,
                        mTextPaint);
            }

            if (i >= state) {
                mCirclePaint.setColor(getResources().getColor(R.color.neu_999999));
                mCircleTextPaint.setColor(getResources().getColor(R.color.neu_999999));
                mCircleLinePaint.setColor(getResources().getColor(R.color.neu_999999));
            }

            RectF rectF = new RectF(mCenter[i].x - mCircleRadius,
                    mCenter[i].y - mCircleRadius,
                    mCenter[i].x + mCircleRadius,
                    mCenter[i].y + mCircleRadius);
            canvas.drawArc(rectF, 0, 360, false, mCirclePaint);

            Rect rect = getTextBound(mCircleTextPaint, (i + 1) + "");
            canvas.drawText((i + 1) + "",
                    mCenter[i].x - rect.width() / 2,
                    mCenter[i].y + rect.height() / 2,
                    mCircleTextPaint);

            if(i > 0) {
                canvas.drawLine(mCenter[i - 1].x + mCircleRadius + mCircleStrokeWidth, mCenter[i - 1].y,
                        mCenter[i].x - mCircleRadius - mCircleStrokeWidth, mCenter[i].y, mCircleLinePaint);
            }
        }
    }

    /**
     * 初始化状态描述的Paint
     */
    private void initWhitePaint() {
        //初始化交易描述的画笔
        if (mCurrentStatusPoint == null) {
            mCurrentStatusPoint = new Paint();
        }
        mCurrentStatusPoint.setColor(getResources().getColor(R.color.black));
//        mCurrentStatusPoint.setColor(getResources().getColor(R.color.white));
        mCurrentStatusPoint.setTextSize(mStateNumTextSize);
        mCurrentStatusPoint.setStyle(Paint.Style.STROKE);
        mCurrentStatusPoint.setAntiAlias(true);

        //初始化状态的画笔
        if (mDotStatePaint == null) {
            mDotStatePaint = new Paint();
        }
//        mDotStatePaint.setColor(getResources().getColor(R.color.white));
        mDotStatePaint.setColor(getResources().getColor(R.color.black));
        mDotStatePaint.setTextSize(mCurrentStateTextSize);
        mDotStatePaint.setStyle(Paint.Style.STROKE);
        mDotStatePaint.setAntiAlias(true);
    }

    /***
     * 刷新状态描述的文字
     *
     * @param canvas
     */
    private void drawDescribeInfo(Canvas canvas) {
        if (!TextUtils.isEmpty(mStatusDescribeInfo)) {
            Rect textBound = getTextBound(mTextPaint, mStatusDescribeInfo);

            canvas.drawText(mStatusDescribeInfo,
                    mCenter[0].x - mCircleRadius,
                    mCenter[0].y + mCircleRadius + mCircleStrokeWidth + textBound.height() + 30,
                    mCurrentStatusPoint);
        }
    }

    private void initPoint() {
        if (mPoint == null) {
            mPoint = new Point();
        }
        mPoint.x = 120;
        mPoint.y = 150;
    }

    /**
     * 初始化圆圈的
     */
    private void initPaint() {
        if (mCirclePaint == null) {
            mCirclePaint = new Paint();
        }
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(getResources().getColor(R.color.neu_999999));
        mCirclePaint.setStrokeWidth(circleStrokeWith);
    }

    boolean isCancle;

    public void setOrderState(int state, Activity act, boolean isCancle) {
        this.mConsultState = state;
        this.isCancle = isCancle;
        int width = act.getWindowManager().getDefaultDisplay().getWidth();

        if (width < 800) {
            scale = 25;
            radius = 35;
            mStateNumTextSize = getResources().getDimensionPixelSize(R.dimen.dd_dimen_32px);//数字尺寸
            mCurrentStateTextSize = getResources().getDimensionPixelSize(R.dimen.dd_dimen_24px);
            length = 100;
            strokeWith = 3;
            circleStrokeWith = 4;
            textDist = 12;
            mPoint.x = 120;
            mPoint.y = 120;
            textLeft = 42;
            textStateYScale = 65;
            describeBottom = 85;
            pts = new float[]{
                    155, 120, 210, 120,
                    280, 120, 335, 120,
                    405, 120, 460, 120,
                    530, 120, 585, 120};
        }
        invalidate();
    }

    public void setDescribeInfo(String describeInfo) {
        this.mStatusDescribeInfo = describeInfo;
    }

    public void resetCancelDraw(Activity act) {
        onlyDrawText = true;
        this.mStatusDescribeInfo = "买家取消了这笔交易";
        int width = act.getWindowManager().getDefaultDisplay().getWidth();
        if (width < 800) {
            describeBottom = 70;
            textLeft = 42;
        }
        invalidate();
    }

    /**
     * 画圆形状态和文本
     *
     * @param canvas
     */
    public void drawCircleAndTextSelect(Canvas canvas) {
        if (mConsultState == ConsultingState.CANCEL) {
            return;
        }
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setAntiAlias(true);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.neu_999999));
        paint.setTextSize(mStateNumTextSize);
        paint.setStrokeWidth(strokeWith);
        for (int i = 0; i < 5; i++) {
            int tempState = mConsultState + 1;
            if (i <= tempState) {
                mCirclePaint.setColor(getResources().getColor(R.color.red_ying));
                paint.setColor(getResources().getColor(R.color.red_ying));
                mDotStatePaint.setColor(getResources().getColor(R.color.red_ying));
            } else {
                mCirclePaint.setColor(getResources().getColor(R.color.neu_999999));
                paint.setColor(getResources().getColor(R.color.neu_999999));
                mDotStatePaint.setColor(getResources().getColor(R.color.neu_999999));
            }
            if (i <= mConsultState) {
                if (i == mConsultState) {
                    if (i == ConsultingState.RATED) {
                        if (mConsultState == ConsultingState.FINISH) {
                            stateName = stateNameNo[i];
                        } else {
                            stateName = stateNameAlready[i];
                        }
                    } else {
                        stateName = stateNameAlready[i];
                    }
                } else {
                    stateName = stateNameAlready[i];
                }
            } else {
                stateName = stateNameNo[i];
            }
            canvas.drawCircle(mPoint.x + i * (scale + length), mPoint.y, radius, mCirclePaint);
            canvas.drawText("" + (i + 1), mPoint.x + i * (scale + length) - textDist, mPoint.y + textDist, paint);
            canvas.drawText("" + stateName, mPoint.x + i * (scale + length) - textLeft, mPoint.y - textStateYScale, mDotStatePaint);
        }
    }

    //绘制线 选择状态
    public void drawLineSelect(Canvas canvas) {
        if (mConsultState == ConsultingState.CANCEL) {
            return;
        }
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.red_ying));
        paint.setStrokeWidth(strokeWith);
        paint.setAntiAlias(true);
        canvas.drawLines(pts, getStartPositionSelect(), getEndPositionSelect(), paint);
    }

    public int getStartPositionSelect() {
        return 0;
    }

    public int getEndPositionSelect() {
        if (mConsultState == ConsultingState.WAITING_PAY) {
            return 4;
        } else if (mConsultState == ConsultingState.CONSULT_IN_QUEUE) {
            return 8;
        } else if (mConsultState == ConsultingState.CONSULT_IN_CHAT) {
            return 12;
        } else if (mConsultState == ConsultingState.FINISH) {
            return 16;
        } else if (mConsultState == ConsultingState.RATED || mConsultState == ConsultingState.CANCEL) {
            return 16;
        }
        return 0;
    }

    public void drawLineUnSelect(Canvas canvas) {
        if (mConsultState == ConsultingState.CANCEL) {
            return;
        }
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.neu_999999));
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWith);
        canvas.drawLines(pts, getStartPositionUnSelect(), getEndPositionUnSelect(), paint);
    }

    public int getStartPositionUnSelect() {
        if (mConsultState == ConsultingState.WAITING_PAY) {
            return 0;
        } else if (mConsultState == ConsultingState.CONSULT_IN_QUEUE) {
            return 4;
        } else if (mConsultState == ConsultingState.CONSULT_IN_CHAT) {
            return 8;
        } /*else if (mConsultState == ConsultingState.FINISH) {
            return 12;
        }*/ else if (mConsultState == ConsultingState.FINISH || mConsultState == ConsultingState.RATED || mConsultState == ConsultingState.CANCEL) {
            return 16;
        }
        return 0;
    }

    public int getEndPositionUnSelect() {
        if (mConsultState == ConsultingState.WAITING_PAY) {
            return 16;
        } else if (mConsultState == ConsultingState.CONSULT_IN_QUEUE) {
            return 12;
        } else if (mConsultState == ConsultingState.CONSULT_IN_CHAT) {
            return 8;
        } /*else if (mConsultState == ConsultingState.FINISH ) {
            return 4;
        } */ else if (mConsultState == ConsultingState.RATED || mConsultState == ConsultingState.FINISH) {
            return 0;
        }
        return 0;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 计算文字的宽度
     *
     * @param paint
     * @param str
     * @return
     */
    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    public static Rect getTextBound(Paint paint, String str) {
        //2. 计算文字所在矩形，可以得到宽高
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        int w = rect.width();
        int h = rect.height();
        return rect;
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
