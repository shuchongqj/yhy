package com.quanyan.yhy.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.quanyan.yhy.R;
import com.yhy.common.beans.hotel.CutInfo;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("ClickableViewAccessibility")
public class CutProgressBar extends View {

    private String TAG = CutProgressBar.class.getSimpleName();
    /**
     * 每节直接的间隙宽度
     */
    private float GAP_SIZE = 2;

    private float PROGRESS_HEIGHT = 12;

    /**
     * 圆填充颜色
     */
    private String CIRCLE_COLOR = "#FFFFFF";

    /**
     * 圆环颜色
     */
    private String ANNULUS_COLOR = "#88000000";

    private float mPadding = 0;

    /**
     * 圆所在位置
     */
    private int clickPosition = 0;

    /**
     * 圆所在位置
     */
    private float clickX = 0;

    /**
     * 是否正在移动
     */
    private boolean isMoving = false;

    /**
     * 每一截进度的款
     */
    private float progress_width;
    private Paint mPaint;
    private List<CutInfo> mCutInfos = new ArrayList<CutInfo>();
    private float mCircleSize;
    private float viewWidth;
    private float viewHeight;
    private OnSelectChangeLinener mOnSelectChangeLinener;

    public CutProgressBar(Context context) {
        this(context, null);
    }

    public CutProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public CutProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
    }

    public boolean isProhibitVew = true;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isProhibitVew) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isMoving = false;

                    float r = (float) (Math.pow(
                            (event.getX() - getCirclePosition(clickPosition).x), 2) + Math
                            .pow((event.getY() - getCirclePosition(clickPosition).y), 2));
                    if (r < Math.pow(mCircleSize, 2)) {
                        return true;
                    }
                    break;


                case MotionEvent.ACTION_MOVE:
                    isMoving = true;
                    clickX = event.getX();
                    if (clickX < calculateTextX0(0)) {
                        clickX = calculateTextX0(0);
                    }
                    if (clickX > calculateTextX0(mCutInfos.size() - 1)) {

                        clickX = calculateTextX0(mCutInfos.size() - 1);
                    }
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    isMoving = false;
                    clickPosition = getRecentlyPosition();
                    invalidate();
                    mOnSelectChangeLinener.OnSelectChange(clickPosition);
                    break;
                default:
                    break;
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        viewWidth = getWidth();
        viewHeight = getHeight();
        mPaint.setAntiAlias(true); // 是否抗锯齿
        if (mCutInfos.size() < 2) {
            try {
                throw new Exception("节点数不能小于2");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        initData();

        for (int i = 0; i < mCutInfos.size(); i++) {
            drawText(canvas, i);
        }

        if (isMoving) {
            for (int i = 0; i < mCutInfos.size() - 1; i++) {
                float x = calculateTextX0(i);
                if(x > clickX){
                    drawLine(canvas, i);
                }else{
                    drawLine2(canvas, i);
                }
//                if (clickPosition - 1 < i) {
//                    drawLine(canvas, i);
//                } else {
//                    drawLine2(canvas, i);
//                }

            }
            drawCircle(canvas, clickX);
        } else {
            for (int i = 0; i < mCutInfos.size() - 1; i++) {
                if (clickPosition - 1 < i) {
                    drawLine(canvas, i);
                } else {
                    drawLine2(canvas, i);
                }

            }
            drawCircle(canvas, clickPosition);
        }
    }

    /**
     * 画圆
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas, int position) {
//		drawCircle(canvas, calculateTextX0(position));
        mPaint.setColor(getResources().getColor(R.color.order_666666));
        canvas.drawCircle(calculateTextX0(position), viewHeight - mCircleSize, mCircleSize, mPaint);
        mPaint.setColor(getResources().getColor(R.color.white));
        canvas.drawCircle(calculateTextX0(position), viewHeight - mCircleSize, mCircleSize - 1, mPaint);
    }

    /**
     * 画圆
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas, float x) {
        mPaint.setColor(getResources().getColor(R.color.order_666666));
        canvas.drawCircle(x, viewHeight - mCircleSize, mCircleSize, mPaint);
        mPaint.setColor(getResources().getColor(R.color.white));
        canvas.drawCircle(x, viewHeight - mCircleSize, mCircleSize - 1, mPaint);
    }

    int LineColor = getResources().getColor(R.color.translucent_77);

    private void drawLine(Canvas canvas, int i) {
        float y = viewHeight - mCircleSize;
        mPaint.setStrokeWidth(PROGRESS_HEIGHT);
        mPaint.setColor(LineColor);
        canvas.drawLine(calculateTextX0(i) + GAP_SIZE, y, calculateTextX0(i)
                + progress_width, y, mPaint);
    }

    int LineColorGray = getResources().getColor(R.color.ac_title_bg_color);

    private void drawLine2(Canvas canvas, int i) {
        float y = viewHeight - mCircleSize;
        mPaint.setStrokeWidth(PROGRESS_HEIGHT);
        mPaint.setColor(LineColorGray);
        canvas.drawLine(calculateTextX0(i) + GAP_SIZE, y, calculateTextX0(i)
                + progress_width, y, mPaint);
    }

    int TextColor = getResources().getColor(R.color.Black);

    /**
     * 画文字
     */
    private void drawText(Canvas canvas, int position) {
        mPaint.setTextSize(mCutInfos.get(position).getMsgSize());
        mPaint.setColor(TextColor);
        canvas.drawText(mCutInfos.get(position).getCutMsg(),
                calculateTextX0(position) - measure(position) / 2, mCutInfos
                        .get(position).getMsgSize(), mPaint);
    }

    /**
     * 获取当前最近的position
     *
     * @return
     */
    private int getRecentlyPosition() {
        float r = viewWidth;
        int position = 0;
        for (int i = 0; i < mCutInfos.size(); i++) {
            Log.i(TAG, "r:" + r + "-----Math.abs(clickX-calculateTextX0(i)):" + Math.abs(clickX - calculateTextX0(i)));
            if (r > Math.abs(clickX - calculateTextX0(i))) {
                r = Math.abs(clickX - calculateTextX0(i));
                position = i;
            }
        }
        return position;
    }

    /**
     * 获取圆心位置
     *
     * @param position
     * @return
     */
    private PointF getCirclePosition(int position) {
        return new PointF(calculateTextX0(position), viewHeight - mCircleSize);
    }

    /**
     * 每段起点的x坐标
     *
     * @param position
     * @return
     */
    private float calculateTextX0(int position) {
        return progress_width * position + mPadding + GAP_SIZE * position;
    }

    /**
     * 测量字体宽度
     *
     * @param position
     * @return
     */
    private float measure(int position) {
        mPaint.setTextSize(mCutInfos.get(position).getMsgSize());
        return mPaint.measureText(mCutInfos.get(position).getCutMsg());
    }

    /**
     * 初始化大小数据
     */
    private void initData() {
        if (mCutInfos.size() < 1) {
            return;
        }

        // 比较 第一段字宽度/2 最后一段字宽度/2 和圆半径的大小 那最大值作为mPadding
        if (mCircleSize > measure(0) / 2) {
            mPadding = mCircleSize;
        } else {
            mPadding = measure(0) / 2;
        }
        if (mPadding < measure(mCutInfos.size() - 1) / 2)
            mPadding = measure(mCutInfos.size() - 1) / 2;
        // 控件宽度 -圆的直径（左右一边一半）-所有间隙 /段数
        progress_width = (viewWidth - 2 * mPadding - GAP_SIZE
                * mCutInfos.size())
                / (mCutInfos.size() - 1);
        if (progress_width < 0) {
            progress_width = 0;
        }
    }

    /**
     * 设置节点信息
     *
     * @param catInfos
     */
    public void setCutInfo(List<CutInfo> catInfos) {
        this.mCutInfos = catInfos;
    }

    /**
     * 设置位置圆的大小（单位dp）
     */
    public void setCircleSize(float circleSize) {
        this.mCircleSize = circleSize;
    }

    /**
     * 设置选中位置
     *
     * @param clickPosition
     */
    public void setClickPosition(int clickPosition) {
        this.clickPosition = clickPosition;
        invalidate();
    }

    /**
     * 获取选中位置
     *
     * @return
     */
    public int getClickPosition() {
        return clickPosition;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            int desired = (int) (getPaddingLeft() + getWidth() + getPaddingRight());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int desired = (int) (getPaddingTop() + getWidth() + getPaddingBottom());
            height = desired;
        }

        setMeasuredDimension(width, height);
    }


    public void setOnSelectChangeLinener(OnSelectChangeLinener onSelectChangeLinener) {
        this.mOnSelectChangeLinener = onSelectChangeLinener;
    }

    public interface OnSelectChangeLinener {
        public void OnSelectChange(int position);
    }


}
