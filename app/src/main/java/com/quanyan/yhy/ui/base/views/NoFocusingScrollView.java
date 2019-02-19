/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.quanyan.yhy.ui.base.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.lidroid.xutils.util.LogUtils;

public class NoFocusingScrollView extends ScrollView {

	private int mTop = 100;
	private boolean mDisallowInterceptTouchEvent = false;
	private float mDownY;

	public NoFocusingScrollView(Context context) {
		super(context);
		requestDisallowInterceptTouchEvent(false);
	}

	public NoFocusingScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		requestDisallowInterceptTouchEvent(false);
	}
  
    public NoFocusingScrollView(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        requestDisallowInterceptTouchEvent(false);
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	int scrolly = getScrollY();
        final int action = ev.getAction();
		LogUtils.i("ACTION=:" + action + "getScrollY = " + getScrollY() + "mTop=" + mTop) ;
		boolean b = super.onInterceptTouchEvent(ev);
		LogUtils.i("Super intercept return=" + super.onInterceptTouchEvent(ev));
		if(scrolly < mTop) {
    		requestDisallowInterceptTouchEvent(false);
		} else {
    		requestDisallowInterceptTouchEvent(true);
		}
		return b;
    }
    
    public void setFixTop(int top) {
    	mTop = top;
    }
    
    @Override
    public void requestDisallowInterceptTouchEvent(boolean paramBoolean) {
      super.requestDisallowInterceptTouchEvent(paramBoolean);
      this.mDisallowInterceptTouchEvent = paramBoolean;
      LogUtils.i("requestDisallowInterceptTouchEvent :" + mDisallowInterceptTouchEvent);
    }
}
