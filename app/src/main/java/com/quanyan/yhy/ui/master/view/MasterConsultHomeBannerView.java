package com.quanyan.yhy.ui.master.view;

import android.view.ViewGroup;

import com.quanyan.base.view.customview.imgpager.ImgPagerView;
import com.quanyan.yhy.common.ResourceType;
import com.quanyan.yhy.ui.tab.view.hometab.BaseHomeView;
import com.quanyan.yhy.ui.tab.view.hometab.HomeViewInterface;
import com.yhy.common.beans.net.model.common.Booth;
import com.yhy.common.beans.net.model.common.BoothList;
import com.yhy.common.constants.ValueConstants;

import java.util.List;

/**
 * Created with Android Studio.
 * Title:MasterConsultHomeBannerView
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-20
 * Time:15:49
 * Version 1.0
 * Description:
 */
public class MasterConsultHomeBannerView extends BaseHomeView implements HomeViewInterface {

    private ImgPagerView mImgPagerView;

    @Override
    public void onResume() {
        mImgPagerView.startAutoScroll();
    }

    @Override
    public void onPause() {
        mImgPagerView.stopAutoScroll();
    }

    @Override
    public void deliverViewScrollY(int scrollY) {
        if (scrollY <= 360) {
            mImgPagerView.startAutoScroll();
        } else {
            mImgPagerView.stopAutoScroll();
        }
    }

    @Override
    public void setView(ViewGroup parentView, int index) {
        mImgPagerView = new ImgPagerView(parentView.getContext());
        mImgPagerView.setScale(parentView.getContext(), (float) ValueConstants.SCALE_VALUE_BANNER);
        if (index <= parentView.getChildCount()) {
            parentView.addView(mImgPagerView, index);
        } else {
            parentView.addView(mImgPagerView);
        }
    }

    @Override
    public void handleData(Object data) {
        if (data != null && data instanceof BoothList) {
            //广告位
            List<Booth> value = ((BoothList) data).value;
            if (value != null && value.size() != 0) {
                for (int i = 0; i < ((BoothList) data).value.size(); i++) {
                    if ((ResourceType.QUANYAN_CONSULTING_HOME_AD).equals(((BoothList) data).value.get(i).code)) {
                        Booth banner = ((BoothList) data).value.get(i);
                        if (banner != null && banner.showcases != null && banner.showcases.size() > 0) {
                            mImgPagerView.setBannerList(banner.showcases);
                        }
                    }
                }
            }
        }
    }
}
