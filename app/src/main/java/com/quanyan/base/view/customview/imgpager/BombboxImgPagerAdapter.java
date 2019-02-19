package com.quanyan.base.view.customview.imgpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.quanyan.yhy.R;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:2/1/16
 * Time:09:35
 * Version 1.0
 */
public class BombboxImgPagerAdapter extends PagerAdapter {

    protected ArrayList<String> mData = new ArrayList();

    private boolean isInfiniteLoop;
    private int mDefaultImg; //默认图片

    public BombboxImgPagerAdapter(Context context, int resImg) {
        this.mDefaultImg = resImg;
        isInfiniteLoop = false;
        if (context instanceof BaseImgPagerClick) {
            mBaseImgPagerClick = (BaseImgPagerClick) context;
        }
    }

    /**
     * 设置图片数组
     *
     * @param imgs 图片数组源
     */
    public void setImgs(List<String> imgs) {
        if (imgs == null) {
            return;
        }
        mData.clear();
        mData.addAll(imgs);
        notifyDataSetChanged();
    }

    /**
     * 设置默认图片
     *
     * @param resImg 默认图片的资源ID
     */
    public void setDefaultImg(int resImg) {
        this.mDefaultImg = resImg;
    }

    public void addItem(final String item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addItem(int idx, final String item) {
        mData.add(idx, item);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mData.clear();
        notifyDataSetChanged();
    }

    public String getItem(int position) {
        return mData.get(getPosition(position));
    }

    public ArrayList<String> getData() {
        return mData;
    }

    @Override
    public int getCount() {
        // Infinite loop
        if (mData == null || mData.size() < 1) {
            return 0;
        } else if (mData.size() == 1) {
            return 1;
        }
        return isInfiniteLoop ? Integer.MAX_VALUE : mData.size();
    }

    /**
     * get really position
     *
     * @param position
     * @return
     */
    public int getPosition(int position) {
        if (mData == null || mData.size() < 1) {
            return 0;
        } else if (mData.size() == 1) {
            return 0;
        }
        return isInfiniteLoop ? position % mData.size() : position;
    }

    public void setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = View.inflate(container.getContext(), R.layout.item_bomb_box_pager_img, null);
        container.addView(view);
        ImageView imageView = (ImageView) view.findViewById(R.id.pager_img);
        final int index = mData.size() == 0 ? position : (position % mData.size());
//        String  bgUrl = ImageUtils.getImageFullUrl(mData.get(index));

//        BaseImgView.loadimg(imageView,
//                mData.get(index),
//                mDefaultImg,
//                mDefaultImg,
//                mDefaultImg,
//                ImageScaleType.EXACTLY,
//                810,
//                1080,
//                0);
        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(mData.get(index)), mDefaultImg, 810, 1080, imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBaseImgPagerClick != null) {
                    mBaseImgPagerClick.imgClick(index);
                }
            }
        });
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private BaseImgPagerClick mBaseImgPagerClick;

    /**
     * 设置图片点击事件的回调接口，必须在初始化的时候设置
     *
     * @param baseImgPagerClick
     */
    public void setBaseImgPagerClick(BaseImgPagerClick baseImgPagerClick) {
        mBaseImgPagerClick = baseImgPagerClick;
    }

    public interface BaseImgPagerClick {
        void imgClick(int position);
    }
}
