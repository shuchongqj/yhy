
/**
 * @Description: TODO
 * @date 2014-9-5 上午11:30:57
 */


package com.quanyan.yhy.ui.base.views.banner;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.newyhy.activity.HomeMainTabActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.customview.viewpagerindicator.CirclePageIndicator;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ItemType;

import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.base.views.WrapContentHeightViewPager;
import com.quanyan.yhy.ui.common.calendar.ScreenUtil;
import com.quanyan.yhy.ui.line.LineActivity;
import com.yhy.common.beans.net.model.RCShowcase;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BannerView extends RelativeLayout {

    private static final int TIME = 3000;

    private WrapContentHeightViewPager mViewPager;
    private CirclePageIndicator mBannerDot;
    private ViewpagerAdapter mViewpagerAdapter;
    private List<RCShowcase> mBannerList;
    private int mCurrentid = 0;
    private boolean slideFlage = true;
    private boolean isInfiniteLoop = true;
    private boolean mLoadToListener = false;
    private Context mContext;
    private IOnBannerItemClickLisetener mOnBannerItemClickLisetener;
    public BannerView(Context context) {
        super(context);
        init(context);
        showBanner();
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        showBanner();
    }

    public BannerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
        showBanner();
    }

    public void init(Context context) {
        mContext = context;
        LayoutInflater lf = LayoutInflater.from(getContext());
        lf.inflate(R.layout.view_banner, this, true);

        mViewPager = (WrapContentHeightViewPager) findViewById(R.id.banner_view_pager);
        mBannerDot = (CirclePageIndicator) findViewById(R.id.banner_dot);

        // 显示标题右边的 圆点

        mViewpagerAdapter = new ViewpagerAdapter(getContext());
        mViewpagerAdapter.setInfiniteLoop(isInfiniteLoop);
        mViewPager.setAdapter(mViewpagerAdapter);

        mBannerDot.setViewPager(mViewPager);
        mBannerDot.setInfiniteLoop(isInfiniteLoop);

        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                mCurrentid = position;
                mBannerDot.setCurrentItem(mCurrentid);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                if (arg0 == 1) {
                    slideFlage = false;
                } else {
                    slideFlage = true;
                }
            }
        });

        setListener();
    }

    private class ViewpagerAdapter extends AKCirclePageAdapter<RCShowcase> {
        public ViewpagerAdapter(Context c) {
            super(c);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView riv_image = (ImageView) mInflater.inflate(R.layout.item_image_hall, null);
            riv_image.setScaleType(ScaleType.CENTER_CROP);

//            ImageLoaderUtil.loadImage(mContext,
//                    riv_image,
//                    ImageUtils.getImageFullUrl(mData.get(position % mData.size()).imgUrl),
//                    R.drawable.icon_default_310_180,
//                    R.drawable.icon_default_310_180);
            if(StringUtil.isEmpty(mData.get(position % mData.size()).imgUrl)){
                riv_image.setImageResource(R.mipmap.icon_default_750_230);
            }else {
                int width = ScreenUtil.getScreenWidth(mContext.getApplicationContext());
                int height = (int) (ScreenUtil.getScreenWidth(mContext.getApplicationContext()) / WrapContentHeightViewPager.SCALE_VP);
//                BaseImgView.loadimg(riv_image,
//                        mData.get(position % mData.size()).imgUrl,
//                        R.mipmap.icon_default_750_230,
//                        R.mipmap.icon_default_750_230,
//                        R.mipmap.icon_default_750_230,
//                        ImageScaleType.EXACTLY,
//                        width,
//                        height,
//                        0);

                ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(mData.get(position % mData.size()).imgUrl), R.mipmap.icon_default_750_230, width, height, riv_image);

            }

            container.addView(riv_image);
            return riv_image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (object instanceof View) {
                container.removeView((View) object);
            }
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }


    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            if (mViewpagerAdapter.getCount() > 0) {
                if (slideFlage) {
                    mCurrentid = (mCurrentid + 1 == mViewpagerAdapter.getCount() ? 0 : mCurrentid + 1);
                    mViewPager.setCurrentItem(mCurrentid, true);
                }
            }
            handler.postDelayed(this, TIME);
        }
    };

    Handler handler = new Handler();
    private void showBanner() {
        mViewpagerAdapter.clearItems();
        int size = null == mBannerList ? 0 : mBannerList.size();
        if (size > 0) {
            mBannerDot.setCount(size);
            if(size == 1) {
                mBannerDot.setVisibility(View.INVISIBLE);
            } else {
                mBannerDot.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < size; i++) {
                mViewpagerAdapter.addItem(mBannerList.get(i));
            }
        } else {
            mBannerDot.setVisibility(View.INVISIBLE);
        }

        mViewpagerAdapter.notifyDataSetChanged();

        if(mViewpagerAdapter.getData().size() > 0) {
            mViewPager.setAdapter(mViewpagerAdapter);
            mViewPager.setCurrentItem(mViewpagerAdapter.getData().size() * 300);
        }
    };

    private int xpos = 0 , ypos = 0;
    private long time;

    public void setListener(){
        if(mLoadToListener) return;

        if(mViewPager != null) {
            mViewPager.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            xpos = (int) event.getX();
                            ypos = (int) event.getY();
                            time = System.currentTimeMillis();
                            break;
                        case MotionEvent.ACTION_UP:
                            int tmpx = (int) event.getX() - xpos;
                            int tmpy = (int) event.getY() - ypos;
                            if (Math.abs(tmpx) < 10 && Math.abs(tmpy) < 10
                                    && Math.abs(time - System.currentTimeMillis()) < 1500) {
                                if (null != mOnBannerItemClickLisetener) {
                                    int position = isInfiniteLoop ? mViewPager.getCurrentItem() % mViewpagerAdapter.getData().size() : mViewPager
                                            .getCurrentItem();
                                    int size = null == mBannerList ? 0 : mBannerList.size();
                                    if (position >= 0 && size > position) {
                                        RCShowcase showcase = null == mBannerList ? null : mBannerList.get(position);
                                        mOnBannerItemClickLisetener.onClick(showcase, position);
                                    }
                                }
                            }

                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
        }
        mLoadToListener = true;
    }

    public void setBannerList(List<RCShowcase> list) {
        mBannerList = list;
        mLoadToListener = false;
        showBanner();

        setOnBannerItemClickLisetener(new IOnBannerItemClickLisetener() {
            @Override
            public void onClick(RCShowcase showcase, int position) {
                //打点
                Map<String, String> map = new HashMap<>();
                map.put(AnalyDataValue.KEY_INDEX, position + 1 + "");
                if (mContext instanceof HomeMainTabActivity) {
                    map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.BANNER_CLICK_HOME);
                } else if (mContext instanceof LineActivity) {
                    if (ItemType.FREE_LINE.equals(((LineActivity) mContext).getType())) {
                        map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.BANNER_CLICK_FREE_WALK);
                    } else if (ItemType.TOUR_LINE.equals(((LineActivity) mContext).getType())) {
                        map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.BANNER_CLICK_PACKAGE_TOUR);
                    } else if (ItemType.FREE_LINE_ABOARD.equals(((LineActivity) mContext).getType())) {
                        map.put(AnalyDataValue.KEY_TYPE, ItemType.FREE_LINE_ABOARD);
                    } else if (ItemType.TOUR_LINE_ABOARD.equals(((LineActivity) mContext).getType())) {
                        map.put(AnalyDataValue.KEY_TYPE, ItemType.TOUR_LINE_ABOARD);
                    }else if (ItemType.CITY_ACTIVITY.equals(((LineActivity) mContext).getType())) {
                        map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.BANNER_CLICK_CITY_ACTIVITY);
                    } else if (ItemType.ARROUND_FUN.equals(((LineActivity) mContext).getType())) {
                        map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.BANNER_CLICK_ARROUND_FUN);
                    }
                }
                map.put(AnalyDataValue.KEY_VALUE, showcase.title);
                TCEventHelper.onEvent(mContext, AnalyDataValue.TC_ID_BANNER_CLICK, map);

                NavUtils.depatchAllJump(mContext,showcase, position);
            }
        });
    }

    public void stopHandle(){
        handler.removeCallbacks(runnable);
    }

    public void startHandle(){
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, TIME);
    }

    public void setOnBannerItemClickLisetener(IOnBannerItemClickLisetener onBannerItemClickLisetener) {
        this.mOnBannerItemClickLisetener = onBannerItemClickLisetener;
    }

    public interface IOnBannerItemClickLisetener {
        void onClick(RCShowcase showcase, int position);
    }
}
