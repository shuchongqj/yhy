package com.quanyan.yhy.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.quanyan.yhy.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 酒店首页进度条
 */
public class SlideOptionView extends View {

    //是否显示测试log
    private static final boolean isShowLog = true;

    //小球可点击区域相对于小球多大
    private static final float mClickRote = 1.5f;//点击区域是小球的1.5倍,方便触发滑动，提高用户体验

    //主要颜色
    private static final String main_color = "#28a4ff";

    //画笔
    private Paint mPaint;

    //View的宽度
    private float mWidth;

    //View的高度
    private float mHeight;

    //水平横线的相对坐标
    private int mCenterY;

    //水平横线的左右内边距
    private int mPadding;

    //水平横线切分的平均值
    private int mAverage;

    //小竖线的高度
    private int mIntervalLineHeight;

    //title列表
    private List<String> titles;

    //字体Rect
    private Rect bounds;

    //圆球距离左边距的距离
    private float mCircleTranslateX = 0;

    //Touch事件状态机
    private TouchState mTouchState;

    //当前小球的位置
    private int mCurrentPosition = 0;

    //小球的半径
    private float mCircleRadius;

    //小球滑动的停止位置
    float mCircleTargetTranslateX;

    //小球滑动方向的状态机
    private DirectState mDirectState;

    //处理小球滑动的主线程的绘制
    private Handler handler;

    //滑动的事件监听器
    private OnSelectChangeListener mOnselectChangeListener;

    public SlideOptionView(Context context) {
        super(context);

    }

    public SlideOptionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setTextSize(25);
        this.bounds = new Rect();
        this.mTouchState = new TouchState();
        this.mDirectState = new DirectState();
        this.titles = new ArrayList<String>() {
            {
                add("1KM");
                add("3KM");
                add("5KM");
                add("10KM");
                add("不限");
            }
        };
        this.handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    invalidate();
                } else if (msg.what == 2) {
                    if (mOnselectChangeListener != null) {
                        mOnselectChangeListener.change(mCurrentPosition);
                    }
                }
            }
        };
    }

    public SlideOptionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //未使用测量到的高宽
        int widthMeasureSpecModel = MeasureSpec.getMode(widthMeasureSpec);
        widthMeasureSpec = MeasureSpec.getSize(widthMeasureSpec);

        int heightMeasureSpecModel = MeasureSpec.getMode(heightMeasureSpec);
        heightMeasureSpec = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed == true) {
            //TODO 设置子View的位置
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
        this.initData();
    }

    private void initData() {
        this.mCenterY = (int) (this.mHeight / 1.6);
        this.mPadding = (int) (this.mWidth / 13);//这里我设置的左右padding是屏幕的十分之一
        this.mAverage = (int) (this.mWidth - 2 * this.mPadding) / (titles.size() - 1);
        this.mIntervalLineHeight = this.mAverage / 10;

        this.mCircleRadius = this.mIntervalLineHeight * 2;

        //设置默认字体大小
        this.mPaint.setTextSize(this.mIntervalLineHeight * 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //水平横线的绘制
        mPaint.setColor(this.getResources().getColor(R.color.ac_title_bg_color));
        mPaint.setStrokeWidth(this.getResources().getDisplayMetrics().density * 4);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawLine(mPadding, mCenterY, mWidth - mPadding, mCenterY, mPaint);

        //绘制横线中间的白色间隔
        for (int i = 0; i < titles.size(); i++) {
            if (i == 0 || i == titles.size() ) {

            }else{
                //绘制横线中间的间隔线
                mPaint.setStrokeWidth(this.getResources().getDisplayMetrics().density * 2);
                mPaint.setColor(Color.WHITE);
                canvas.drawLine(mPadding + i * this.mAverage,
                        mCenterY - this.getResources().getDisplayMetrics().density * 2,
                        this.mPadding + i * this.mAverage, mCenterY + this.getResources().getDisplayMetrics().density * 2, this.mPaint);
            }
            //绘制间隔线上方的字体
            String title = this.titles.get(i);
            mPaint.getTextBounds(title, 0, title.length(), bounds);
            mPaint.setColor(this.getResources().getColor(R.color.color_norm_636363));
            canvas.drawText(title, mPadding + i * this.mAverage - bounds.width() / 2, mCenterY - this.mIntervalLineHeight - bounds.height() / 2 - bounds.height(), mPaint);
        }
        //绘制滑动的白色圆
        mPaint.setStrokeWidth(this.getResources().getDisplayMetrics().density * 1);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(this.getResources().getColor(R.color.ac_title_bg_color));
        canvas.drawCircle(this.mPadding + mCircleTranslateX, mCenterY, (float) (2 * mIntervalLineHeight), mPaint);

        //绘制滑动的白色圆的灰色边
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor(main_color));
        mPaint.setColor(this.getResources().getColor(R.color.ac_title_bg_color));
        canvas.drawCircle(this.mPadding + mCircleTranslateX, mCenterY, this.mCircleRadius, mPaint);
    }

    /**
     * Touch事件中使用到的状态机
     */
    private class TouchState {
        public int None = 0, Down = 1, Move = 2, Up = 0, Single = 3;
        public int state;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        log(event.getX() + "---" + event.getY());
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mTouchState.state == mTouchState.None) {
                float x = event.getX();
                float y = event.getY();

                //判断是否点击到小球所在区域内
                for (int i = 0; i < titles.size(); i++) {
                    if (x > mPadding + i * this.mAverage - mCircleRadius * mClickRote &&
                            x < mPadding + i * this.mAverage + mCircleRadius * mClickRote &&
                            y > mCenterY - mCircleRadius * mClickRote &&
                            y < mCenterY + mCircleRadius * mClickRote) {
                        if (i == this.mCurrentPosition) {
                            mTouchState.state = mTouchState.Down;
                        } else {
                            mTouchState.state = mTouchState.Single;
                        }
                        mTouchState.state = mTouchState.Down;
                        break;
                    }
                }
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (mTouchState.state == mTouchState.Down) {
                mTouchState.state = mTouchState.Move;
            } else if (mTouchState.state == mTouchState.Single) {
                mTouchState.state = mTouchState.Move;
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mTouchState.state == mTouchState.Single || mTouchState.state == mTouchState.Down || mTouchState.state == mTouchState.Move) {
                mTouchState.state = mTouchState.Up;
                float x = event.getX();
                //手指放开后，计算小球应该滑动到的位置
                for (int i = 0; i < titles.size(); i++) {
                    if (x >= mPadding + i * this.mAverage - this.mAverage / 2 && x <= mPadding + i * this.mAverage) {
                        this.mCircleTargetTranslateX = mPadding + i * this.mAverage;
                        this.mCurrentPosition = i;
                        mDirectState.state = mDirectState.Right;
                        new AniThread().start();
                        break;
                    } else if (x <= mPadding + i * this.mAverage + this.mAverage / 2 && x >= mPadding + i * this.mAverage) {
                        this.mCircleTargetTranslateX = mPadding + i * this.mAverage;
                        this.mCurrentPosition = i;
                        mDirectState.state = mDirectState.Left;
                        new AniThread().start();
                        break;
                    }
                }
            }
        }

        if (mTouchState.state == mTouchState.Down || mTouchState.state == mTouchState.Move) {
            this.mCircleTranslateX = event.getX() - this.mPadding;
            if (this.mCircleTranslateX < 0) {
                this.mCircleTranslateX = 0;
            } else if (this.mCircleTranslateX > this.mWidth - 2 * this.mPadding) {
                this.mCircleTranslateX = this.mWidth - 2 * this.mPadding;
            }
            invalidate();
        }

        super.onTouchEvent(event);
        if (mTouchState.state != mTouchState.None) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 小球滑动方向的状态机
     */
    private class DirectState {
        public int None = 0, Left = 1, Right = 2;
        public int state = None;
    }

    /**
     * 小球滑动使用到的子线程
     */
    private class AniThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    if (mDirectState.state == mDirectState.Right) {
                        mCircleTranslateX = mCircleTranslateX + 3;
                        if (mCircleTranslateX > mCircleTargetTranslateX - mPadding) {
                            mCircleTranslateX = mCircleTargetTranslateX - mPadding;
                            mDirectState.state = mDirectState.None;
                        }
                        handler.sendEmptyMessage(1);
                        sleep(5);
                    } else if (mDirectState.state == mDirectState.Left) {
                        mCircleTranslateX = mCircleTranslateX - 3;
                        if (mCircleTranslateX < mCircleTargetTranslateX - mPadding) {
                            mCircleTranslateX = mCircleTargetTranslateX - mPadding;
                            mDirectState.state = mDirectState.None;
                        }
                        handler.sendEmptyMessage(1);
                        sleep(5);
                    } else {
                        handler.sendEmptyMessage(2);
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 设置滑动监听器
     *
     * @param onSelectChangeListener
     */
    public void setOnSelectChangeListener(OnSelectChangeListener onSelectChangeListener) {
        this.mOnselectChangeListener = onSelectChangeListener;
    }

    /**
     * 设置Titles列表
     *
     * @param _titles
     */
    public void setTitles(List<String> _titles) {
        this.titles.clear();
        this.titles.addAll(_titles);
        this.initData();
//        invalidate();
        postInvalidate();
    }

    /**
     * 定义的监听接口
     */
    public interface OnSelectChangeListener {
        void change(int position);
    }

    /**
     * 输出log的方法
     *
     * @param message
     */
    private void log(String message) {
        if (isShowLog) {
            Log.e("Coolspan", message);
        }
    }
}
