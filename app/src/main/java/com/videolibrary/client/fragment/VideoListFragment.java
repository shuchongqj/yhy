package com.videolibrary.client.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.quanyan.base.BaseListViewFragment;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.customview.imgpager.ImgPagerView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.common.ResourceType;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.videolibrary.controller.LiveController;
import com.videolibrary.itemhandle.LiveListItemHelper;
import com.videolibrary.metadata.LiveTypeConstants;
import com.videolibrary.utils.IntentUtil;
import com.yhy.common.beans.net.model.common.Booth;
import com.yhy.common.beans.net.model.msg.LiveRecordAPIPageResult;
import com.yhy.common.beans.net.model.msg.LiveRecordResult;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;
import com.yhy.service.IUserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:VideoListFragment
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/8/8
 * Time:16:49
 * Version 1.1.0
 */
public class VideoListFragment extends BaseListViewFragment<LiveRecordResult> {

    private ImgPagerView mImgPagerView;

    private String mTitle;
    private long mCityCode = -1;
    private long mUserId = -1;
    private boolean isNeedBanner = true;

    @Autowired
    IUserService userService;

    private List<String> mFetchTypes = new ArrayList<>();

    public static VideoListFragment newInstance(Bundle extras) {
        VideoListFragment videoListFragment = new VideoListFragment();
        Bundle bundle = new Bundle();
        if(extras != null) {
            bundle.putAll(extras);
        }
        videoListFragment.setArguments(bundle);
        return videoListFragment;
    }

    @Override
    public void fetchData(int pageIndex) {
        LiveController.getInstance().getLiveList(getActivity(),mHandler,
                mFetchTypes,
                mCityCode + "",
                pageIndex,
                getPageSize());
    }

    @Override
    public void onResume() {
        super.onResume();
        manualRefresh();
        if(mImgPagerView != null){
            mImgPagerView.startAutoScroll();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mImgPagerView != null){
            mImgPagerView.stopAutoScroll();
        }
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle != null){
            mTitle = bundle.getString(IntentUtil.BUNDLE_TITLE);
            mCityCode = bundle.getLong(IntentUtil.BUNDLE_CITYCODE, -1);
            mUserId = bundle.getLong(IntentUtil.BUNDLE_USERID, -1);
            isNeedBanner = bundle.getBoolean(IntentUtil.BUNDLE_IS_NEED_BANNER, true);
        }

        mFetchTypes.add(LiveTypeConstants.LIVE_ING);
        mFetchTypes.add(LiveTypeConstants.LIVE_REPLAY);
        if(isNeedBanner) {
            addHeaderView(getBaseDropListView().getListView());
            LiveController.getInstance().doGetLiveListBanner(getActivity(), mHandler, ResourceType.QUANYAN_LIVE_LIST_BANNER);
        }
//        manualRefresh();
    }

    private void addHeaderView(ListView listView) {
        View view = View.inflate(getActivity(), R.layout.view_video_list_top, null);
        mImgPagerView = (ImgPagerView) view.findViewById(R.id.view_video_list_top_img_pager);
        listView.addHeaderView(view);
    }

    @Override
    public void convertItem(BaseAdapterHelper helper, LiveRecordResult item) {
        LiveListItemHelper.handlLiveListItem(getActivity(), helper, item, new LiveListItemHelper.AddressClick() {
            @Override
            public void addressClick(int position, String cityName, long cityCode) {
                if(cityCode == mCityCode){
                    tcEvent(cityName, cityCode);
                    getBaseDropListView().getListView().setSelection(0);
                    getBaseDropListView().getPullRefreshView().setRefreshing(true);
//                    manualRefresh();
                }else {
                    tcEvent(cityName, cityCode);
                    IntentUtil.startLiveListActivity(getActivity(), cityName, cityCode, false);
                }
            }
        });
    }

    private void tcEvent(String cityName, long cityCode) {
        Map<String, String> map = new HashMap<>();
        map.put(AnalyDataValue.KEY_CITY_CODE, cityCode + "");
        map.put(AnalyDataValue.KEY_CITY_NAME, cityName);
        TCEventHelper.onEvent(getActivity(), AnalyDataValue.LIVE_LOCATION_CLICK, map);
    }

    @Override
    public int setItemLayout() {
        return R.layout.item_live_list;
    }

    @Nullable
    @Override
    public List<String> setTabStrings() {
        return null;
    }

    @Nullable
    @Override
    public List<View> setPopViews() {
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int headerCount = getBaseDropListView().getListView().getHeaderViewsCount();
        if (position >= headerCount) {
            LiveRecordResult item = getAdapter().getItem(position - headerCount);
            if(item.userInfo != null) {
                if (LiveTypeConstants.LIVE_ING.equals(item.liveStatus)) {
                    if(userService.getLoginUserId() == item.userInfo.userId){
                        ToastUtil.showToast(getActivity(), "您不能进入自己的直播间");
                        return;
                    }
                    IntentUtil.startVideoClientActivity(item.liveId,
                            item.userInfo.userId, true,item.liveScreenType);
                } else if (LiveTypeConstants.LIVE_REPLAY.equals(item.liveStatus)) {
                    IntentUtil.startVideoClientActivity(
                            item.liveId, item.userInfo.userId, false,item.liveScreenType);
                }
            }
        }
    }

    @Override
    public void dispatchMessage(Message msg) {
        getPullListView().onRefreshComplete();
        hideErrorView(null);
        switch (msg.what){
            case LiveController.MSG_LIVE_BANNER_OK:
                Booth booth = (Booth) msg.obj;
                if(booth != null){
                    mImgPagerView.setBannerList(booth.showcases);
                    mImgPagerView.startAutoScroll();
                }
                break;
            case LiveController.MSG_LIVE_BANNER_ERROR:
                break;
            case LiveController.MSG_LIVE_LIST_OK:
                LiveRecordAPIPageResult liveRecordAPIPageResult = (LiveRecordAPIPageResult) msg.obj;
                if(liveRecordAPIPageResult != null) {
                    setHaxNext(liveRecordAPIPageResult.hasNext);
                }
                handleListData(liveRecordAPIPageResult);
                break;
            case LiveController.MSG_LIVE_LIST_ERROR:
                if(isRefresh()) {
                    showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                            IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
                        @Override
                        public void onClick(View view) {
                            manualRefresh();
                        }
                    });
                }else{
                    ToastUtil.showToast(getActivity(), StringUtil.handlerErrorCode(getActivity(), msg.arg1));
                }
                break;
            case ValueConstants.MSG_HAS_NO_DATA:
                ToastUtil.showToast(getActivity(), getString(R.string.no_more_data));
                break;
        }
    }

    private void handleListData(LiveRecordAPIPageResult result) {
        if(isRefresh()){
            if(result != null && result.list != null){
                getAdapter().replaceAll(result.list);
            }else{
                getAdapter().clear();
                showErrorView(null, IActionTitleBar.ErrorType.EMPTYVIEW, "", "", "", null);
            }
        }else{
            if(result != null && result.list != null){
                getAdapter().addAll(result.list);
            }
        }
    }
}
