package com.quanyan.base.view.customview.imgpager;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.newyhy.activity.HomeMainTabActivity;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.view.customview.autoscrollview.AutoScrollViewPager;
import com.quanyan.base.view.customview.viewpagerindicator.CirclePageIndicator;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.line.LineActivity;
import com.quanyan.yhy.ui.master.activity.MasterConsultHomeActivity;
import com.videolibrary.client.activity.LiveListActivity;
import com.yhy.common.beans.net.model.RCShowcase;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.types.BannerType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:图片轮播的布局，有{@link android.support.v4.view.ViewPager}和{@link CirclePageIndicator}组成，只能用于图片的展示和查看大图
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:2/1/16
 * Time:10:25
 * Version 1.0
 */
public class ImgPagerView extends RelativeLayout implements BaseImgPagerAdapter.BaseImgPagerClick {
    /**
     * 图片宽高比
     */
//    public static final float SCALE_VP = 750 / 300f;
    private float mScale = 0;

    private boolean isInfiniteLoop = true;
    private AutoScrollViewPager mViewPager;
    private CirclePageIndicator mCirclePageIndicator;
    public BaseImgPagerAdapter mBaseImgPagerAdapter;

    private OnImageClickListner listner;

    public ImgPagerView(Context context) {
        super(context);
        initView(context, null);
    }

    public ImgPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ImgPagerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ImgPagerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    public void setScale(Context context, float scale) {
        mScale = scale;
        mViewPager.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (ScreenSize.getScreenWidth(context.getApplicationContext()) / (mScale != 0 ? mScale : ValueConstants.SCALE_VALUE_BANNER))));
        if(mBaseImgPagerAdapter !=null){
            mBaseImgPagerAdapter.setScale(mScale);
        }
    }

    private void initView(Context context, AttributeSet attributeSet) {
        View.inflate(context, R.layout.view_img_pager, this);
//        setBackgroundResource(R.mipmap.icon_default_310_180);
        setBackgroundResource(R.color.white);
        mViewPager = (AutoScrollViewPager) findViewById(R.id.img_pager);
        mCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.pager_indicator);

        mViewPager.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (ScreenSize.getScreenWidth(context.getApplicationContext()) / (mScale != 0 ? mScale : ValueConstants.SCALE_VALUE_BANNER))));

        mBaseImgPagerAdapter = new BaseImgPagerAdapter(context, R.color.white/*R.mipmap.icon_default_310_180*/,(mScale != 0 ? mScale : ValueConstants.SCALE_VALUE_BANNER));
        mBaseImgPagerAdapter.setBaseImgPagerClick(this);
        mViewPager.setAdapter(mBaseImgPagerAdapter);
        mCirclePageIndicator.setViewPager(mViewPager);
        mCirclePageIndicator.setSnap(true);
    }

    /**
     * 开始自动滚动
     */
    public void startAutoScroll() {
        mViewPager.startAutoScroll(4000);
    }

    /**
     * 停止自动滚动
     */
    public void stopAutoScroll() {
        mViewPager.stopAutoScroll();
    }

    /**
     * 设置图片数组
     *
     * @param imgs 图片数组源
     */
    public void setImgs(List<String> imgs) {
        if (imgs != null && imgs.size() > 0) {
            if (mCirclePageIndicator != null) {
                mCirclePageIndicator.setInfiniteLoop(isInfiniteLoop);
                mCirclePageIndicator.setCount(imgs == null ? 0 : imgs.size());
            }
            if (mBaseImgPagerAdapter != null) {
                mBaseImgPagerAdapter.clearItems();
                mBaseImgPagerAdapter.setInfiniteLoop(isInfiniteLoop);
                mBaseImgPagerAdapter.setImgs(imgs);
            }
            if (mViewPager != null) {
                mViewPager.setStopScrollWhenTouch(true);
                mViewPager.setAdapter(mBaseImgPagerAdapter);
                mViewPager.setCurrentItem(mBaseImgPagerAdapter.getData().size() * 300);
            }
        }
    }

    private List<RCShowcase> mRCShowcaseList = new ArrayList<>();

    /**
     * 设置图片数组（对于Banner使用）
     *
     * @param list 图片数组源
     */
    public void setBannerList(final List<RCShowcase> list) {
        List<String> strings = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (RCShowcase rcShowcase : list) {
                strings.add(rcShowcase.imgUrl);
            }
            mRCShowcaseList.clear();
            mRCShowcaseList.addAll(list);
            if (mCirclePageIndicator != null) {
                mCirclePageIndicator.setInfiniteLoop(isInfiniteLoop);
                mCirclePageIndicator.setCount(mRCShowcaseList == null ? 0 : mRCShowcaseList.size());
            }
            if (mBaseImgPagerAdapter != null) {
                mBaseImgPagerAdapter.clearItems();
                mBaseImgPagerAdapter.setInfiniteLoop(isInfiniteLoop);
                mBaseImgPagerAdapter.setImgs(strings);
            }
            if (mViewPager != null) {
                mViewPager.setStopScrollWhenTouch(true);
                mViewPager.setAdapter(mBaseImgPagerAdapter);
                mViewPager.setCurrentItem(mBaseImgPagerAdapter.getData().size() * 300);
            }
        }
    }

    /**
     * 设置是否显示图片数量指示器
     *
     * @param visible
     */
    public void setPageIndicatorVisible(int visible) {
        mCirclePageIndicator.setVisibility(visible);
    }

    public void addOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        mViewPager.addOnPageChangeListener(onPageChangeListener);
    }

    /**
     * 响应图片的点击事件
     *
     * @param position 点击的位置
     */
    @Override
    public void imgClick(int position) {

        if (listner != null){
            listner.onImageClick(position);
        }

        if (position < mRCShowcaseList.size()) {
            //打点
            if(BannerType.STR_VIEW_TALENT_STORY.equals(mRCShowcaseList.get(position).operation)){
                TCEventHelper.onEvent(getContext(), AnalyDataValue.VIEW_TALENT_STORY, mRCShowcaseList.get(position).title);
            }else {
                Map<String, String> map = new HashMap<>();
                map.put(AnalyDataValue.KEY_INDEX, position + 1 + "");
                String pageType = "";
                if (getContext() instanceof HomeMainTabActivity && mIImgPagerType != null) {
                    pageType = mIImgPagerType.getPageType();
                    if (pageType.equals("HOME_BANNER")) {
                        map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.BANNER_CLICK_HOME);
                    } else {
                        map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.BANNER_CLICK_MASTER);
                    }

                } else if (getContext() instanceof LineActivity) {
                    if (ItemType.FREE_LINE.equals(((LineActivity) getContext()).getType())) {
                        map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.BANNER_CLICK_FREE_WALK);
                    } else if (ItemType.TOUR_LINE.equals(((LineActivity) getContext()).getType())) {
                        map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.BANNER_CLICK_PACKAGE_TOUR);
                    } else if (ItemType.FREE_LINE_ABOARD.equals(((LineActivity) getContext()).getType())) {
                        map.put(AnalyDataValue.KEY_TYPE, ItemType.FREE_LINE_ABOARD);
                    } else if (ItemType.TOUR_LINE_ABOARD.equals(((LineActivity) getContext()).getType())) {
                        map.put(AnalyDataValue.KEY_TYPE, ItemType.TOUR_LINE_ABOARD);
                    } else if (ItemType.CITY_ACTIVITY.equals(((LineActivity) getContext()).getType())) {
                        map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.BANNER_CLICK_CITY_ACTIVITY);
                    } else if (ItemType.ARROUND_FUN.equals(((LineActivity) getContext()).getType())) {
                        map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.BANNER_CLICK_ARROUND_FUN);
                    }
                }else if (getContext() instanceof MasterConsultHomeActivity){
                    map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.BANNER_CLICK_MASTER_CONSULT);
                }else if (getContext() instanceof LiveListActivity){
                    map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.BANNER_CLICK_LIVE_HOME);
                }
                map.put(AnalyDataValue.KEY_VALUE, mRCShowcaseList.get(position).title);
                TCEventHelper.onEvent(getContext(), AnalyDataValue.TC_ID_BANNER_CLICK, map);
            }

            NavUtils.depatchAllJump(getContext(), mRCShowcaseList.get(position), position);
        }
    }


    public void setOnImageClickListner(OnImageClickListner listner){
        this.listner = listner;
    }

    private IImgPagerType mIImgPagerType;

    public void setIImgPagerType(IImgPagerType IImgPagerType) {
        mIImgPagerType = IImgPagerType;
    }

    public interface IImgPagerType {
        String getPageType();
    }

    public interface OnImageClickListner{
        void onImageClick(int currentImagePosition);
    }

}
