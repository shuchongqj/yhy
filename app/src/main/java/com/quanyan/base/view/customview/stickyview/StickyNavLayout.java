package com.quanyan.base.view.customview.stickyview;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.OverScroller;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshListView;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshObservableListView;
import com.quanyan.yhy.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

public class StickyNavLayout extends LinearLayout {

    private View mSlidingTabLayout;
    private ViewPager mViewPager;

    private int mTopViewHeight;
    private ViewGroup mInnerScrollView;
    public boolean isTopHidden = false;

    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;
    private int mMaximumVelocity, mMinimumVelocity;

    private float mLastY;
    private float mLastX;

    private boolean mDragging;

    private boolean isInControl = true;
    private boolean isFixedHeight = true;

    public StickyNavLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);

        mScroller = new OverScroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaximumVelocity = ViewConfiguration.get(context)
                .getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context)
                .getScaledMinimumFlingVelocity();

    }

    /**
     * 设置是否固定高度
     *
     * @param isFixedHeight
     */
    public void setFixedHeight(boolean isFixedHeight) {
        this.isFixedHeight = isFixedHeight;
        invalidate();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mSlidingTabLayout = findViewById(R.id.id_stickynavlayout_indicator);
        View view = findViewById(R.id.id_stickynavlayout_viewpager);
        if (!(view instanceof ViewPager)) {
            throw new RuntimeException(
                    "id_stickynavlayout_viewpager show used by ViewPager !");
        }
        mViewPager = (ViewPager) view;
    }

    public void onFinishInflate(View parent) {
        mSlidingTabLayout = parent.findViewById(R.id.id_stickynavlayout_indicator);
        View view = parent.findViewById(R.id.id_stickynavlayout_viewpager);
        if (!(view instanceof ViewPager)) {
            throw new RuntimeException(
                    "id_stickynavlayout_viewpager show used by ViewPager !");
        }
        mViewPager = (ViewPager) view;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //将默认的measure方式改为不限制，这样就可以根据自布局的高度来设置
        super.onMeasure(widthMeasureSpec, MeasureSpec.UNSPECIFIED);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        mTopViewHeight = mSlidingTabLayout.getTop();
        if (heigh > 0) {
            mTopViewHeight = mTopViewHeight - heigh;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        float y = ev.getY();
        float x = ev.getX();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastY = y;
                mLastX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastY;
                float dx = x - mLastX;
                getCurrentScrollView();

                if (mInnerScrollView instanceof ListView) {

                    ListView lv = (ListView) mInnerScrollView;
                    //只显示屏幕可见部分的View，，若超出屏幕怎会返回空，需使用（ n - getFirstVisiblePosition()）来回去第一个View
                    View childAt = null;
//				HarwkinLogUtil.info("scroll", "getLastVisiblePosition : " + lv.getLastVisiblePosition() + "    getFirstVisiblePosition : " + lv.getFirstVisiblePosition());
                    int divide = lv.getLastVisiblePosition() - lv.getFirstVisiblePosition();
                    if (0 != divide) {
                        childAt = lv.getChildAt(lv.getFirstVisiblePosition() % divide);
                    }

//				HarwkinLogUtil.info("scroll", "dispatchTouchEvent : !isInControl : " + (!isInControl));
////				HarwkinLogUtil.info("scroll", "dispatchTouchEvent : lv.getTop() : " + childAt.getTop());
//				HarwkinLogUtil.info("scroll", "dispatchTouchEvent : isTopHidden : " + (isTopHidden));

                    //当topView不显示时：isInControl = false;
                    //Sticky Layout 不是可滑动状态，listview的第一个Item显示，topView隐藏，向下滑动
                    //是否将事件分发个子View
//				if (!isInControl && childAt != null && childAt.getTop() == 0 && isTopHidden && dy > 0) {
                    if (!isInControl && (lv.getLastVisiblePosition() < 0 || (lv.getFirstVisiblePosition() == 0
                            && childAt != null && childAt.getTop() == 0)) && isTopHidden && dy > 0) {
                        //reset the MotionEvent
                        isInControl = true;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        MotionEvent ev2 = MotionEvent.obtain(ev);
                        dispatchTouchEvent(ev);
                        ev2.setAction(MotionEvent.ACTION_DOWN);
                        return dispatchTouchEvent(ev2);
                    }
                } else if (mInnerScrollView instanceof WebView || mInnerScrollView instanceof ScrollView) {
//				WebView webView = (WebView) mInnerScrollView;
                    if (mInnerScrollView.getScrollY() == 0 && isTopHidden && dy > 0 && !isInControl) {
                        isInControl = true;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        MotionEvent ev2 = MotionEvent.obtain(ev);
                        dispatchTouchEvent(ev);
                        ev2.setAction(MotionEvent.ACTION_DOWN);
                        return dispatchTouchEvent(ev2);
                    }
                } else if (mInnerScrollView instanceof PullToRefreshListView) {
                    ListView lv = ((PullToRefreshListView) mInnerScrollView).getRefreshableView();
                    View childAt = null;
                    int divide = lv.getLastVisiblePosition() - lv.getFirstVisiblePosition();
                    if (0 != divide) {
                        childAt = lv.getChildAt(lv.getFirstVisiblePosition() % divide);
                    }

                    if (!isInControl && (lv.getLastVisiblePosition() < 0 || (lv.getFirstVisiblePosition() == 0
                            && childAt != null && childAt.getTop() == 0)) && isTopHidden && dy > 0) {
                        //reset the MotionEvent
                        isInControl = true;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        MotionEvent ev2 = MotionEvent.obtain(ev);
                        dispatchTouchEvent(ev);
                        ev2.setAction(MotionEvent.ACTION_DOWN);
                        return dispatchTouchEvent(ev2);
                    }
                } else if (mInnerScrollView instanceof SmartRefreshLayout) {
                    if (mInnerScrollView.getScrollY() == 0 && isTopHidden && dy > 0 && !isInControl) {
                        isInControl = true;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        MotionEvent ev2 = MotionEvent.obtain(ev);
                        dispatchTouchEvent(ev);
                        ev2.setAction(MotionEvent.ACTION_DOWN);
                        return dispatchTouchEvent(ev2);
                    }
                }
//                double tan = Math.tan(Math.abs(dy) / Math.abs(dx));
//                if (isScrollBottom && dy < 0 && tan > 0.5) {
//                    int currentItem = mViewPager.getCurrentItem();
//                    int pagerIndex = currentItem + 1;
//                    mViewPager.setCurrentItem(pagerIndex >= mViewPager.getChildCount() ? currentItem : pagerIndex);
//                }
                break;
            case MotionEvent.ACTION_UP:
                if (mInnerScrollView instanceof ListView) {
                } else if (mInnerScrollView instanceof WebView) {
                    WebView webView = (WebView) mInnerScrollView;
                    if ((int) (webView.getContentHeight() * webView.getScale()) <= (webView.getHeight() + webView.getScrollY() + 10)) {
                        isScrollBottom = true;
                    } else {
                        isScrollBottom = false;
                    }
                } else if (mInnerScrollView instanceof ScrollView) {
                    ScrollView scrollView = (ScrollView) mInnerScrollView;
                    if (scrollView.getChildAt(0).getMeasuredHeight() <= scrollView.getScrollY() + scrollView.getHeight()) {
                        isScrollBottom = true;
                    } else {
                        isScrollBottom = false;
                    }
                } else if (mInnerScrollView instanceof PullToRefreshListView) {
                    ListView listView = ((PullToRefreshListView) mInnerScrollView).getRefreshableView();
                } else if (mInnerScrollView instanceof PullToRefreshObservableListView) {

                } else if (mInnerScrollView instanceof SmartRefreshLayout) {
                    SmartRefreshLayout smartRefreshLayout = (SmartRefreshLayout) mInnerScrollView;
                    if (smartRefreshLayout.getChildAt(0).getMeasuredHeight() <= smartRefreshLayout.getScrollY() + smartRefreshLayout.getHeight()) {
                        isScrollBottom = true;
                    } else {
                        isScrollBottom = false;
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     *
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        float y = ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastY;
                getCurrentScrollView();
//			HarwkinLogUtil.info("scroll", "onInterceptTouchEvent  :  Math.abs(dy) > mTouchSlop : " + (Math.abs(dy) > mTouchSlop));
                if (Math.abs(dy) > mTouchSlop) {
                    mDragging = true;
                    if (mInnerScrollView instanceof ListView) {

                        ListView lv = (ListView) mInnerScrollView;
                        //只显示屏幕可见部分的View，，若超出屏幕怎会返回空，需使用（ n - getFirstVisiblePosition()）来回去第一个View
                        View childAt = null;
                        int divide = lv.getLastVisiblePosition() - lv.getFirstVisiblePosition();
                        if (0 < divide) {
                            childAt = lv.getChildAt(lv.getFirstVisiblePosition() % divide);
                        } else if (0 == divide) {
                            childAt = lv.getChildAt(0);
                        }
                        // 如果topView没有隐藏
                        // 或sc的listView在顶部 && topView隐藏 && 下拉，则拦截

//					HarwkinLogUtil.info("scroll", "onInterceptTouchEvent  :  getFirstVisiblePosition() : " + lv.getFirstVisiblePosition());
//					HarwkinLogUtil.info("scroll", "onInterceptTouchEvent  :  ------------- : " + ((lv.getLastVisiblePosition() - lv.getFirstVisiblePosition())));
//					HarwkinLogUtil.info("scroll", "dispatchTouchEvent : c.getTop() : " + (childAt == null ? "c == null" : childAt.getTop()));
//					HarwkinLogUtil.info("scroll", "onInterceptTouchEvent  :  !isTopHidden : " + (!isTopHidden));
//					HarwkinLogUtil.info("scroll", "onInterceptTouchEvent  :  isTopHidden : " + (isTopHidden));

//					if (!isTopHidden || (lv.getFirstVisiblePosition() == 0 && isTopHidden && dy > 0)) {
//					if (!isTopHidden || childAt == null || (childAt.getTop() == 0 && isTopHidden && dy > 0)) {
//					HarwkinLogUtil.info("stickylayout", "isTopHidden && dy > 0 && lv.getFirstVisiblePosition() == 0 && childAt != null && childAt.getTop() == 0  : "
//							+ (isTopHidden && dy > 0 && lv.getFirstVisiblePosition() == 0 && childAt != null && childAt.getTop() == 0));
//					HarwkinLogUtil.info("stickylayout", "isTopHidden  : " + (isTopHidden));
//					HarwkinLogUtil.info("stickylayout", "lv.getLastVisiblePosition() < 0" + (lv.getLastVisiblePosition() < 0));
                        if (!isTopHidden || lv.getLastVisiblePosition() < 0 || (isTopHidden && dy > 0
                                && lv.getFirstVisiblePosition() == 0 && childAt != null && childAt.getTop() == 0)) {
                            initVelocityTrackerIfNotExists();
                            mVelocityTracker.addMovement(ev);
                            mLastY = y;
                            return true;
                        }
                    } else if (mInnerScrollView instanceof WebView || mInnerScrollView instanceof ScrollView) {
                        // 如果topView没有隐藏
                        // 或sc的scrollY = 0 && topView隐藏 && 下拉，则拦截
                        if (!isTopHidden || (mInnerScrollView.getScrollY() == 0
                                && isTopHidden && dy > 0)) {
                            initVelocityTrackerIfNotExists();
                            mVelocityTracker.addMovement(ev);
                            mLastY = y;
                            return true;
                        }
                    } else if (mInnerScrollView instanceof PullToRefreshListView) {
                        ListView lv = ((PullToRefreshListView) mInnerScrollView).getRefreshableView();
                        //只显示屏幕可见部分的View，，若超出屏幕怎会返回空，需使用（ n - getFirstVisiblePosition()）来回去第一个View
                        View childAt = null;
                        int divide = lv.getLastVisiblePosition() - lv.getFirstVisiblePosition();
                        if (0 < divide) {
                            childAt = lv.getChildAt(lv.getFirstVisiblePosition() % divide);
                        } else if (0 == divide) {
                            childAt = lv.getChildAt(0);
                        }
                        // 如果topView没有隐藏
                        // 或sc的listView在顶部 && topView隐藏 && 下拉，则拦截

                        if (!isTopHidden || lv.getLastVisiblePosition() < 0 || (isTopHidden && dy > 0
                                && lv.getFirstVisiblePosition() == 0 && childAt != null && childAt.getTop() == 0)) {
                            initVelocityTrackerIfNotExists();
                            mVelocityTracker.addMovement(ev);
                            mLastY = y;
                            return true;
                        }
                    } else if (mInnerScrollView instanceof PullToRefreshObservableListView) {
                        ListView lv = ((PullToRefreshObservableListView) mInnerScrollView).getRefreshableView();
                        //只显示屏幕可见部分的View，，若超出屏幕怎会返回空，需使用（ n - getFirstVisiblePosition()）来回去第一个View
                        View childAt = null;
                        int divide = lv.getLastVisiblePosition() - lv.getFirstVisiblePosition();
                        if (0 < divide) {
                            childAt = lv.getChildAt(lv.getFirstVisiblePosition() % divide);
                        } else if (0 == divide) {
                            childAt = lv.getChildAt(0);
                        }
                        // 如果topView没有隐藏
                        // 或sc的listView在顶部 && topView隐藏 && 下拉，则拦截

                        if (!isTopHidden || lv.getLastVisiblePosition() < 0 || (isTopHidden && dy > 0
                                && lv.getFirstVisiblePosition() == 0 && childAt != null && childAt.getTop() == 0)) {
                            initVelocityTrackerIfNotExists();
                            mVelocityTracker.addMovement(ev);
                            mLastY = y;
                            return true;
                        }
                    } else if (mInnerScrollView instanceof SmartRefreshLayout) {
                        SmartRefreshLayout smartRefreshLayout = (SmartRefreshLayout) mInnerScrollView;
                        if (smartRefreshLayout.getChildAt(0) instanceof RelativeLayout) {
                            RelativeLayout relativeLayout = (RelativeLayout) smartRefreshLayout.getChildAt(0);
                            if (relativeLayout.getChildAt(0) instanceof RecyclerView) {
                                RecyclerView rv_ugc = (RecyclerView) relativeLayout.getChildAt(0);
                                if (!isTopHidden || (((LinearLayoutManager)rv_ugc.getLayoutManager()).findFirstVisibleItemPosition() == 0
                                        && isTopHidden && dy > 0)) {
                                    initVelocityTrackerIfNotExists();
                                    mVelocityTracker.addMovement(ev);
                                    mLastY = y;
                                    return true;
                                }
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mDragging = false;
                recycleVelocityTracker();
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void resetScroll(ViewGroup innerScrollView){
        if (innerScrollView instanceof ListView) {
            ((ListView)innerScrollView).setSelection(0);
        } else if (innerScrollView instanceof WebView) {
            WebView webView = (WebView) innerScrollView;
            webView.scrollTo(0,0);
        } else if (innerScrollView instanceof ScrollView) {
            ScrollView scrollView = (ScrollView) innerScrollView;
            scrollView.scrollTo(0,0);
        } else if (innerScrollView instanceof PullToRefreshListView) {
            ListView listView = ((PullToRefreshListView) innerScrollView).getRefreshableView();
            listView.setSelection(0);
        } else if (innerScrollView instanceof PullToRefreshObservableListView) {
            ListView listView = ((PullToRefreshObservableListView) innerScrollView).getRefreshableView();
            listView.setSelection(0);
        }
    }

    private int mLastItemIndex = 0;

    private void getCurrentScrollView() {
        int currentItem = mViewPager.getCurrentItem();
        if (mLastItemIndex != currentItem) {
            isScrollBottom = false;
            mLastItemIndex = currentItem;
        }
        PagerAdapter a = mViewPager.getAdapter();
        if (a instanceof FragmentPagerAdapter) {
            FragmentPagerAdapter fadapter = (FragmentPagerAdapter) a;
            Fragment item = (Fragment) fadapter.instantiateItem(mViewPager,
                    currentItem);
            mInnerScrollView = (ViewGroup) (item.getView().findViewById(R.id.id_stickynavlayout_innerscrollview));
            if (mInnerScrollView == null) mInnerScrollView = (item.getView().findViewById(R.id.refreshLayout));
        } else if (a instanceof FragmentStatePagerAdapter) {
            FragmentStatePagerAdapter fsAdapter = (FragmentStatePagerAdapter) a;
            Fragment item = (Fragment) fsAdapter.instantiateItem(mViewPager,
                    currentItem);
            mInnerScrollView = (ViewGroup) (item.getView().findViewById(R.id.id_stickynavlayout_innerscrollview));
            if (mInnerScrollView == null) mInnerScrollView = (item.getView().findViewById(R.id.refreshLayout));
        }
    }

    /**
     * 滑动到底部自动切换tab的标志
     */
    private boolean isScrollBottom = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTrackerIfNotExists();
        mVelocityTracker.addMovement(event);
        int action = event.getAction();
        float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastY = y;
                return true;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastY;

//			HarwkinLogUtil.info("scroll", "onTouchEvent  :  !mDragging && Math.abs(dy) > mTouchSlop : " + (!mDragging && Math.abs(dy) > mTouchSlop));
                if (!mDragging && Math.abs(dy) > mTouchSlop) {
                    mDragging = true;
                }
                if (mDragging) {
                    scrollBy(0, (int) -dy);

                    // 如果topView隐藏，且上滑动时，则改变当前事件为ACTION_DOWN
                    if (getScrollY() == mTopViewHeight && dy < 0) {
                        event.setAction(MotionEvent.ACTION_DOWN);
                        dispatchTouchEvent(event);
                        isInControl = false;//控制滑动的View的改变
                    }
                }

                mLastY = y;
                break;
            case MotionEvent.ACTION_CANCEL:
                mDragging = false;
                recycleVelocityTracker();
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_UP:
                mDragging = false;
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int velocityY = (int) mVelocityTracker.getYVelocity();
                if (Math.abs(velocityY) > mMinimumVelocity) {
                    fling(-velocityY);
                }
                recycleVelocityTracker();
                break;
        }

        return super.onTouchEvent(event);
    }

    public void fling(int velocityY) {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, mTopViewHeight);
        invalidate();
    }

    @Override
    public void scrollTo(int x, int y) {
//		HarwkinLogUtil.info("height", mTopViewHeight + "-----------" + heigh);
//		mTopViewHeight = heigh == 0 ? mTopViewHeight : mTopViewHeight - heigh;
        if (y < 0) {
            y = 0;
        }
        if (y > mTopViewHeight) {
            y = mTopViewHeight;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }

        isTopHidden = getScrollY() == mTopViewHeight;
        if (mOnScrollY != null) {
            mOnScrollY.scrollY(isTopHidden, getScrollY());
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    public void scrollToTop() {
        if (!isTopHidden) {
            scrollTo(0, mTopViewHeight);
        }
    }

    private int heigh;

    public void setTopHeight(int height) {
        this.heigh = height;
    }

    private StickyLayoutScrollY mOnScrollY;

    public void setScrollListener(StickyLayoutScrollY scrollListener) {
        this.mOnScrollY = scrollListener;
    }

    public interface StickyLayoutScrollY {
        void scrollY(boolean isHidden, int scrollY);
    }

}
