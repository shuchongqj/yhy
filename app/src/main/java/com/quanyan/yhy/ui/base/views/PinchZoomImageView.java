package com.quanyan.yhy.ui.base.views;

/**
 * Created with Android Studio.
 * Title:PinchZoomImageView
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:和平必胜、正义必胜、人民必胜
 * Author:炎黄子孙
 * Date:15/10/20
 * Time:上午11:27
 * Version 1.0
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;

import com.quanyan.yhy.ui.base.views.im.ImageView_;

public class PinchZoomImageView extends ImageView_ {
    private PinchZoomImageView.Mode mode;
    private double beforeLenght;
    private double afterLenght;
    private float scale;
    private int start_x;
    private int start_y;
    private int stop_x;
    private int stop_y;
    private TranslateAnimation trans;

    public PinchZoomImageView(Context context, AttributeSet attributes) {
        super(context, attributes);
        this.mode = PinchZoomImageView.Mode.NONE;
        this.scale = 0.03F;
    }

    public PinchZoomImageView(Context context) {
        super(context);
        this.mode = PinchZoomImageView.Mode.NONE;
        this.scale = 0.03F;
    }

    private double spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return Math.sqrt(x * x + y * y);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if(this.mode == Mode.NONE){
            return super.onTouchEvent(event);
        }
        switch(event.getAction() & 255) {
            case 0:
                this.mode = PinchZoomImageView.Mode.DRAGING;
                this.stop_x = (int)event.getRawX();
                this.stop_y = (int)event.getRawY();
                this.start_x = (int)event.getX();
                this.start_y = this.stop_y - this.getTop();

                try {
                    if(this.getLeft() >= 0 && (float)this.stop_x - event.getRawX() < 0.0F) {
                        this.getParent().getParent().requestDisallowInterceptTouchEvent(false);
                    } else if(this.getRight() <= ((View)this.getParent()).getWidth() && (float)this.stop_x - event.getRawX() > 0.0F) {
                        this.getParent().getParent().requestDisallowInterceptTouchEvent(false);
                    } else {
                        this.getParent().getParent().requestDisallowInterceptTouchEvent(true);
                    }
                } catch (Exception var9) {
                    var9.printStackTrace();
                }

                if(event.getPointerCount() == 2) {
                    this.beforeLenght = this.spacing(event);
                }
                break;
            case 1:
                int deltaXfrom = 0;
                byte deltaXto = 0;
                int deltaYfrom = 0;
                byte deltaYto = 0;
                View parentView = (View)this.getParent();
                int rightHide;
                int gapLenght1;
                if(this.getTop() < 0) {
                    gapLenght1 = -this.getTop();
                    rightHide = parentView.getHeight() - (this.getHeight() + this.getTop());
                    if(rightHide > 0) {
                        if(rightHide > gapLenght1) {
                            deltaYfrom = this.getTop();
                            deltaYto = 0;
                            this.layout(this.getLeft(), 0, this.getRight(), this.getBottom() + gapLenght1);
                        } else {
                            deltaYfrom = -rightHide;
                            deltaYto = 0;
                            this.layout(this.getLeft(), this.getTop() + rightHide, this.getRight(), this.getBottom() + rightHide);
                        }
                    }
                } else if(this.getBottom() > parentView.getHeight()) {
                    gapLenght1 = this.getTop();
                    rightHide = this.getBottom() - parentView.getHeight();
                    if(gapLenght1 > 0) {
                        if(gapLenght1 > rightHide) {
                            deltaYfrom = rightHide;
                            deltaYto = 0;
                            this.layout(this.getLeft(), this.getTop() - rightHide, this.getRight(), this.getBottom() - rightHide);
                        } else {
                            deltaYfrom = gapLenght1;
                            deltaYto = 0;
                            this.layout(this.getLeft(), this.getTop() - gapLenght1, this.getRight(), this.getBottom() - gapLenght1);
                        }
                    }
                }

                if(this.getLeft() < 0) {
                    gapLenght1 = -this.getLeft();
                    rightHide = parentView.getWidth() - (this.getWidth() + this.getLeft());
                    if(rightHide > 0) {
                        if(rightHide > gapLenght1) {
                            deltaXfrom = this.getLeft();
                            deltaXto = 0;
                            this.layout(0, this.getTop(), this.getRight() + gapLenght1, this.getBottom());
                        } else {
                            deltaXfrom = -rightHide;
                            deltaXto = 0;
                            this.layout(this.getLeft() + rightHide, this.getTop(), this.getRight() + rightHide, this.getBottom());
                        }
                    }
                } else if(this.getRight() > parentView.getWidth()) {
                    gapLenght1 = this.getLeft();
                    rightHide = this.getRight() - parentView.getWidth();
                    if(gapLenght1 > 0) {
                        if(gapLenght1 > rightHide) {
                            deltaXfrom = rightHide;
                            deltaXto = 0;
                            this.layout(this.getLeft() - rightHide, this.getTop(), this.getRight() - rightHide, this.getBottom());
                        } else {
                            deltaXfrom = gapLenght1;
                            deltaXto = 0;
                            this.layout(0, this.getTop(), this.getRight() - gapLenght1, this.getBottom());
                        }
                    }
                }

                this.trans = new TranslateAnimation((float)deltaXfrom, (float)deltaXto, (float)deltaYfrom, (float)deltaYto);
                this.trans.setDuration(500L);
                this.startAnimation(this.trans);
                this.mode = PinchZoomImageView.Mode.NONE;
                break;
            case 2:
                if(this.mode == PinchZoomImageView.Mode.DRAGING) {
                    try {
                        if(this.getLeft() >= 0 && (float)this.stop_x - event.getRawX() < 0.0F) {
                            this.getParent().getParent().requestDisallowInterceptTouchEvent(false);
                        } else if(this.getRight() <= ((View)this.getParent()).getWidth() && (float)this.stop_x - event.getRawX() > 0.0F) {
                            this.getParent().getParent().requestDisallowInterceptTouchEvent(false);
                        } else {
                            this.getParent().getParent().requestDisallowInterceptTouchEvent(true);
                        }
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }

                    if(Math.abs(this.stop_x - this.start_x - this.getLeft()) < 88 && Math.abs(this.stop_y - this.start_y - this.getTop()) < 85) {
                        this.setPosition(this.stop_x - this.start_x, this.stop_y - this.start_y, this.stop_x + this.getWidth() - this.start_x, this.stop_y - this.start_y + this.getHeight());
                        this.stop_x = (int)event.getRawX();
                        this.stop_y = (int)event.getRawY();
                    }
                } else if(this.mode == PinchZoomImageView.Mode.ZOOMING && this.spacing(event) > 10.0F) {
                    this.afterLenght = this.spacing(event);
                    double gapLenght = this.afterLenght - this.beforeLenght;
                    if(gapLenght != 0.0F && Math.abs(gapLenght) > 5.0F) {
                        if(gapLenght > 0.0F) {
                            this.setScale(this.scale, PinchZoomImageView.ZoomFlag.BIGGER);
                        } else {
                            this.setScale(this.scale, PinchZoomImageView.ZoomFlag.SMALLER);
                        }

                        this.beforeLenght = this.afterLenght;
                    }
                }
            case 3:
            case 4:
            default:
                break;
            case 5:
                if(this.spacing(event) > 10.0F) {
                    this.mode = PinchZoomImageView.Mode.ZOOMING;
                    this.beforeLenght = this.spacing(event);
                }
                break;
            case 6:
                this.mode = PinchZoomImageView.Mode.NONE;
        }

        return true;
    }

    private void setScale(float temp, PinchZoomImageView.ZoomFlag zoomFlag) {
        if(zoomFlag == PinchZoomImageView.ZoomFlag.BIGGER) {
            this.setFrame(this.getLeft() - (int)(temp * (float)this.getWidth()), this.getTop() - (int)(temp * (float)this.getHeight()), this.getRight() + (int)(temp * (float)this.getWidth()), this.getBottom() + (int)(temp * (float)this.getHeight()));
        } else if(zoomFlag == PinchZoomImageView.ZoomFlag.SMALLER) {
            this.setFrame(this.getLeft() + (int)(temp * (float)this.getWidth()), this.getTop() + (int)(temp * (float)this.getHeight()), this.getRight() - (int)(temp * (float)this.getWidth()), this.getBottom() - (int)(temp * (float)this.getHeight()));
        }

    }

    private void setPosition(int left, int top, int right, int bottom) {
        this.layout(left, top, right, bottom);
    }

    static enum ZoomFlag {
        BIGGER,
        SMALLER;

        private ZoomFlag() {
        }
    }

    static enum Mode {
        NONE,
        DRAGING,
        ZOOMING;

        private Mode() {
        }
    }
}
