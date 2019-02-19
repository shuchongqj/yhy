package com.quanyan.base.view.customview.scrolldeletelistview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Scroller;

import com.quanyan.base.util.HarwkinLogUtil;
import com.quanyan.yhy.R;

/**
 * Created with Android Studio.
 * Title:ScrollDeleteListView
 * Description:滑动删除的ListView,可定制菜单
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxp
 * Date:2015-12-4
 * Time:9:53
 * Version 1.0
 *
 * 当手指滑动item,取消item的点击事件，不然我们滑动Item也伴随着item点击事件的发生
    MotionEvent cancelEvent = MotionEvent.obtain(ev);
    cancelEvent.setAction(MotionEvent.ACTION_CANCEL | ev.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT);
    onTouchEvent(cancelEvent);
 */
public class ScrollDeleteListView extends ListView {

	private static final String TAG = "ScrollDeleteListView";
	/**
	 * 当前滑动的Item位置
	 */
	private int mCurrentPointPosition = -1;
	/**
	 * 当前滑动的View
	 */
	private View mCurrentPointView;
	/**
	 * 菜单的宽度
	 */
	private int mMenuWith;
	/**
	 * 滑动控制器
	 */
	private Scroller mScroller;
	/**
	 * 第一次触屏的位置
	 */
	private int mDownX;
	private int mDownY;

	/**
	 * 最小滑动速率
	 */
	private int mMinmumVelocity;
	/**
	 * 滑动速率监听器
	 */
	private VelocityTracker mVelocityTracker;
	/**
	 * 最小滑动距离
	 */
	private int mTouchSlop;
	/**
	 * 菜单是否处于显示状态
	 */
	private boolean isOpened = false;
	/**
	 * 正在滑动
	 */
	private boolean isSliding = false;

	public ScrollDeleteListView(Context context) {
		super(context);
		init(context);
	}

	public ScrollDeleteListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ScrollDeleteListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public ScrollDeleteListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context);
	}

	/**
	 * 初始化必要的参数
	 * @param context {@link Context}
	 */
	private void init(Context context) {
		mScroller = new Scroller(context);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		mMinmumVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
	}

	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				addVelocityTracker(ev);

				if(!mScroller.isFinished()){
					return false;
				}

				/**建议使用getX,getY，若使用getRawX，getRawY则获取的position会将未显示的headerview页算在内**/
				mDownX = (int) ev.getX();
				mDownY = (int) ev.getY();

				int pointPosition = pointToPosition(mDownX, mDownY);
				int headerCount = getHeaderViewsCount();

				HarwkinLogUtil.info("scroll delete", "pointPosition : " + pointPosition);
				/**本类使用PullRefreshListView，有一个未显示的headerView（刷新提示），所以需要判断pointPosition == headerCount - 1**/
				if(pointPosition == AdapterView.INVALID_POSITION || pointPosition == headerCount - 1){
					if(isOpened){
						resetTheMenu();
						return false;
					}
					return super.dispatchTouchEvent(ev);
				}

				if(mCurrentPointPosition != -1 && mCurrentPointPosition != pointPosition){
					//若当前item view处于菜单开启状态，则重置，不分发事件
					if(isOpened){
						resetTheMenu();
						return false;
					}
				}

				//获取当前点击的item view
				mCurrentPointPosition = pointPosition;
				HarwkinLogUtil.info("scroll delete", "mCurrentPointPosition : " + mCurrentPointPosition +
						"\ngetFirstVisiblePosition : " + getFirstVisiblePosition() + "\nheaderCount : " + headerCount);
				mCurrentPointView = getChildAt(mCurrentPointPosition - getFirstVisiblePosition());
				//获取菜单的宽度，否则无法滑动
				View view = mCurrentPointView.findViewById(R.id.scroll_delete_menu_layout);
				if(view != null) {
					view.measure(0, 0);
					mMenuWith = view.getMeasuredWidth();
				}
				break;
			case MotionEvent.ACTION_MOVE:
				int deltaX = (int) (ev.getX() - mDownX);
				int deltaY = (int) (ev.getY() - mDownY);
				// X方向滑动的距离大于mTouchSlop并且Y方向滑动的距离小于mTouchSlop，表示可以滑动

				if((Math.abs(deltaX) > mTouchSlop && Math.abs(deltaY) < mTouchSlop)){
					isSliding = true;
				}
				break;
			case MotionEvent.ACTION_UP:
				recycleVelocityTracker();
				if(!isSliding){
					resetTheMenu();
				}
				break;
		}
		return super.dispatchTouchEvent(ev);
	}

	//用来控制滑动角度，仅当角度a满足如下条件才进行滑动，tan a = deltaX / deltaY > 2
	private static final int TAN = 2;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(isSliding && mCurrentPointView != null) {
			requestDisallowInterceptTouchEvent(true);
			addVelocityTracker(event);
			int x = (int) event.getX();
			int y = (int) event.getY();
			int scrollX = mCurrentPointView.getScrollX();
//			Log.d(TAG, "x=" + x + "  y=" + y);
			int action = event.getAction();
			switch (action) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_MOVE: {
					//当手指滑动item,取消item的点击事件，不然我们滑动Item也伴随着item点击事件的发生
					MotionEvent cancelEvent = MotionEvent.obtain(event);
					cancelEvent.setAction(MotionEvent.ACTION_CANCEL | event.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT);
					onTouchEvent(cancelEvent);

					int deltaX = x - mDownX;
					int deltaY = y - mDownY;
					mDownX = x;
					//计算滑动终点是否合法，防止滑动越界
					int newScrollX = scrollX - deltaX;
//					if (deltaX != 0) {
						if (newScrollX < 0) {
							newScrollX = 0;
							isOpened = false;
						}else if (newScrollX > mMenuWith) {
							isOpened = true;
						}
						mCurrentPointView.scrollTo(newScrollX, 0);
//					}
					//在up之前拦截touch事件
					return true;
				}
				case MotionEvent.ACTION_UP:
					int newScrollX = 0;
					//这里做判断，当松开手的时候，会自动向两边滑动，具体向哪边滑，要看当前所处的位置
					if (scrollX - mMenuWith * 0.5 > 0) {
						newScrollX = mMenuWith;
						isOpened = true;
					}else{
						isOpened = false;
					}
					smoothScrollTo(newScrollX, 0);
					recycleVelocityTracker();
					isSliding = false;
					break;
				default:
					break;
			}
		}
		return super.onTouchEvent(event);
	}

	private void smoothScrollTo(int destX, int destY) {
		//缓慢滚动到指定位置
		int scrollX = mCurrentPointView.getScrollX();
		int delta = destX - scrollX;

		mScroller.startScroll(scrollX, 0, delta, 0, Math.abs(delta) * 3);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			//item view滚动
			mCurrentPointView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}else{
			if(mCurrentPointView != null && mCurrentPointView.getScrollX() == 0){
				mCurrentPointView = null;
			}
		}
		super.computeScroll();
	}

	/**
	 * 重置菜单
	 */
	private void resetTheMenu() {
		isOpened = false;
		if(mCurrentPointView != null) {
			smoothScrollTo(0, 0);
			mCurrentPointView.scrollTo(0, 0);
		}
	}

	/**
	 * 速率监听器初始化
	 *
	 * @param motionEvent
	 */
	private void addVelocityTracker(MotionEvent motionEvent) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(motionEvent);
	}

	/**
	 * 回收速率监听器
	 */
	private void recycleVelocityTracker() {
		if (mVelocityTracker != null)
			mVelocityTracker.recycle();
		mVelocityTracker = null;
	}

	/**
	 * 获取X方向的滑动速率，大于0向右，反之向左
	 *
	 * @return
	 */
	private int getScrollVelocity() {
		mVelocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) mVelocityTracker.getXVelocity();
		return velocity;
	}
}
