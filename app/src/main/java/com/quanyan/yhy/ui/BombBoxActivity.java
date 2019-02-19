package com.quanyan.yhy.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.view.customview.autoscrollview.AutoScrollViewPager;
import com.quanyan.base.view.customview.imgpager.BombboxImgPagerAdapter;
import com.quanyan.base.view.customview.viewpagerindicator.CirclePageIndicator;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ResourceType;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.yhy.common.beans.net.model.RCShowcase;
import com.yhy.common.beans.net.model.common.Booth;
import com.yhy.common.utils.SPUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:BombBoxActivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/8/17
 * Time:下午6:13
 * Version 1.1.0
 */
public class BombBoxActivity extends BaseActivity implements BombboxImgPagerAdapter.BaseImgPagerClick{
    /**
     * 5:2
     */
    public static final double SCALE_VP = 810 / 1080;
    private float mScale = 0;

    private boolean isInfiniteLoop = true;
    private AutoScrollViewPager mViewPager;
    private CirclePageIndicator mCirclePageIndicator;
    private BombboxImgPagerAdapter mBaseImgPagerAdapter;
    private ImageView mIvClose;
    private RelativeLayout mRlParent;
    /**
     * 首页弹框
     * @param context
     */
    public static void gotoBomboxActivity(Context context){
        Intent intent = new Intent(context,BombBoxActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitleBarBackground(Color.TRANSPARENT);
        mViewPager = (AutoScrollViewPager) findViewById(R.id.img_pager);
        mCirclePageIndicator = (CirclePageIndicator) findViewById(R.id.pager_indicator);
        mIvClose = (ImageView)findViewById(R.id.iv_bombbox_close) ;
        mRlParent = (RelativeLayout)findViewById(R.id.rl_parent);

        int width = ScreenSize.getScreenWidth(this.getApplicationContext()) -
                (2 * getResources().getDimensionPixelSize(R.dimen.dd_dimen_50px));
        int height = (int)(width * 1080/ 810);

        mViewPager.setLayoutParams(new RelativeLayout.LayoutParams(width, height));

        mBaseImgPagerAdapter = new BombboxImgPagerAdapter(this, R.mipmap.ic_transparent);
        mBaseImgPagerAdapter.setBaseImgPagerClick(this);
        mViewPager.setAdapter(mBaseImgPagerAdapter);
        mCirclePageIndicator.setViewPager(mViewPager);
        mCirclePageIndicator.setSnap(true);

        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        try {
            Booth booth = Booth.deserialize(SPUtils.getString(SPUtils.TYPE_DEFAULT,this, ResourceType.QUANYAN_BOMB_BOX_LIST));
            if(booth == null || booth.showcases == null || booth.showcases.size() == 0 ){
                ToastUtil.showToast(this,R.string.label_server_config_param_error);
                finish();
            }else{
                setBannerList(booth.showcases);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRlParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_bomb_box,null);
    }

    @Override
    public View onLoadNavView() {
        return null;
    }

    @Override
    public boolean isTopCover() {
        return false;
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

    private List<RCShowcase> mRCShowcaseList = new ArrayList<>();

    /**
     * 设置图片数组（对于Banner使用）
     *
     * @param list 图片数组源
     */
   private void setBannerList(final List<RCShowcase> list) {
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
        if (position < mRCShowcaseList.size()) {
            NavUtils.depatchAllJump(this, mRCShowcaseList.get(position), position);
            finish();
        }
    }

    private IImgPagerType mIImgPagerType;

    public void setIImgPagerType(IImgPagerType IImgPagerType) {
        mIImgPagerType = IImgPagerType;
    }

    public interface IImgPagerType {
        String getPageType();
    }
}
