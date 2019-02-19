package com.videolibrary.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;
import com.quanyan.yhy.R;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created with Android Studio.
 * Title:BarrageViewParent
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/8/11
 * Time:21:07
 * Version 1.1.0
 */
public class BarrageViewParent extends RelativeLayout{

    private Random random = new Random(System.currentTimeMillis());
    private static final long BARRAGE_GAP_MIN_DURATION = 1000;//两个弹幕的最小间隔时间
    private static final long BARRAGE_GAP_MAX_DURATION = 2000;//两个弹幕的最大间隔时间
    private int maxSpeed = 10000;//速度，ms
    private int minSpeed = 4000;//速度，ms
    private int maxSize = 18;//文字大小，sp
    private int minSize = 10;//文字大小，sp

    public static final int RANDOM_SHOW = 1;

    private boolean isVisisble = true;
    private boolean isNeedView = true;

    private LinkedList<WeakReference<TextView>> mWeakReferences;

    public BarrageViewParent(Context context) {
        super(context);
        init(context);
    }

    public BarrageViewParent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BarrageViewParent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BarrageViewParent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        mWeakReferences = new LinkedList<>();
    }

    /**
     * 此方法处理，切换后台再回到前台后的操作（容易导致动画错乱）
     * @param visibility
     */
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        LogUtils.d("" + visibility);
        if(View.GONE == visibility || View.INVISIBLE == visibility){
            mHandler.removeMessages(RANDOM_SHOW);
            isVisisble = false;
        }else{
            isVisisble = true;
            mHandler.sendEmptyMessage(RANDOM_SHOW);
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case RANDOM_SHOW:
                    if(msg.obj == null){
                        return;
                    }
                    if(mWeakReferences.isEmpty()){
                        TextView textView = new TextView(getContext());
                        RelativeLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                        textView.setLayoutParams(layoutParams);
                        textView.setText((String) msg.obj);
                        initTextView(textView, msg.arg1);
                        showBarrageItem(textView, (String) msg.obj, false);
                    }else{
                        WeakReference<TextView> textViewWeakReference =  mWeakReferences.getFirst();
                        if(textViewWeakReference != null &&  textViewWeakReference.get() != null){
                            mWeakReferences.remove(textViewWeakReference);
                            TextView barrageTextItem = textViewWeakReference.get();
                            barrageTextItem.setText((String) msg.obj);
                            initTextView(barrageTextItem, msg.arg1);
                            showBarrageItem(barrageTextItem, (String) msg.obj, true);
                        }
                    }
                    LogUtils.d("after removing the weak references's size : " + mWeakReferences.size());
                    break;
            }
        }
    };

    /**
     * 客户端调用显示弹幕信息
     */
    public void show(String content, boolean isSelf){
        if(isVisisble && isNeedView) {
            Message message = Message.obtain();
            message.obj = content;
            if(isSelf){
                message.arg1 = 1;
            }else{
                message.arg1 = 0;
            }
            message.what = RANDOM_SHOW;

            int duration = (int) ((BARRAGE_GAP_MAX_DURATION - BARRAGE_GAP_MIN_DURATION) * Math.random());
            mHandler.sendMessageDelayed(message, duration);
        }
    }

    /**
     * 主动撤销弹幕的显示，已发送的弹幕消息不作处理
     */
    public void interruptMessage() {
        isNeedView = false;
        mHandler.removeMessages(RANDOM_SHOW);
    }

    /**
     * 重置显示弹幕信息
     */
    public void resetShowMessage(){
        isNeedView = true;
    }

    /**
     * 显示弹幕信息
     * NOTE：为避免重复创建对象，使用WeakReference引用对象
     * @param item
     * @param content
     * @param isAdded
     */
    private void showBarrageItem(final TextView item, final String content, boolean isAdded) {
        int leftMargin = getRight() - getLeft() - getPaddingLeft();
        LayoutParams params = (LayoutParams) item.getLayoutParams();
        params.topMargin = getTopMargin(item, content);
        if(!isAdded) {
            addView(item, params);
        }else{
            item.setLayoutParams(params);
            item.setVisibility(View.VISIBLE);
        }
        Animation anim = generateTranslateAnim(item, content, leftMargin);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                item.setVisibility(View.GONE);
                item.clearAnimation();
                mWeakReferences.addLast(new WeakReference<TextView>(item));
                LogUtils.d("after adding the weak references's size : " + mWeakReferences.size());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        item.setAnimation(anim);
        anim.start();
    }

    /**
     * 设置textview动画
     * @param item
     * @param content
     * @param leftMargin
     * @return
     */
    private TranslateAnimation generateTranslateAnim(TextView item, String content, int leftMargin) {
        int width  = getTextWidth(item, content);
        LogUtils.i("text view leftMargin : " + leftMargin + "    text view width : " + width);
        TranslateAnimation anim = new TranslateAnimation(leftMargin, -width, 0, 0);
        anim.setDuration(getMoveSpeed(item, content));
        anim.setInterpolator(new LinearInterpolator());
        anim.setFillAfter(true);
        return anim;
    }

    /**
     * 初始化弹幕的字体（颜色，阴影）
     * @param textView
     * @param isSelf
     */
    private void initTextView(TextView textView, int isSelf){
//        int textSize = (int) (minSize + (maxSize - minSize) * Math.random());
//        int textColor = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));

        textView.setTextSize(maxSize);
//        textView.setTextColor(textColor);
        if(isSelf == 1){
            //自己发送的
            textView.setTextColor(getResources().getColor(R.color.neu_ffaa00));
        }else{
            textView.setTextColor(Color.WHITE);
        }
        //设置阴影
//        textView.setShadowLayer(10, 5, 3, getResources().getColor(R.color.transparent_black_30));
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB){
            textView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
//        float radius = textView.getTextSize() / 10;
//        BlurMaskFilter blurMaskFilter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.SOLID);
//        textView.getPaint().setMaskFilter(blurMaskFilter);
        textView.setShadowLayer(15, 0, 0, Color.BLACK);
    }

    /**
     * 获取字体的大小宽度
     * @param textView
     * @param content
     * @return
     */
    private int getTextWidth(TextView textView, String content){
        Rect bounds = new Rect();
        textView.getPaint().getTextBounds(content, 0, content.length(), bounds);
        return bounds.width();
    }

    /**
     * 获取view动画的移动速度（即：duration）
     * @param item
     * @param content
     * @return
     */
    private int getMoveSpeed(TextView item, String content){
//        int width  = getTextWidth(item, content);
//        int speed = width * minSpeed;
//        if(speed < 3000){
//            speed = 3000;
//        }else if(speed > 8000){
//            speed = 8000;
//        }
        return (int) (minSpeed + (maxSpeed - minSpeed) * Math.random());
//        return speed;
    }

    /**
     * 获取字体距离屏幕顶部的高度，分行显示
     * NOTE：根据字体大小和屏幕大小设置显示的位置
     * @param textView
     * @param content
     * @return
     */
    private int getTopMargin(TextView textView, String content){
        Rect bounds = new Rect();
        textView.getPaint().getTextBounds(content, 0, content.length(), bounds);

        int totalLine = 0;
        try {
            totalLine = (getMeasuredHeight() / 3) / bounds.height();
        }catch (ArithmeticException e){
            totalLine = 1;
        }
        if(totalLine <= 0){
            totalLine = 1;
        }
        int randomLine = random.nextInt(totalLine);
        return randomLine * bounds.height() + randomLine * 10;
    }
}
