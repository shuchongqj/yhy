package com.quanyan.yhy.ui.circles;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.quanyan.base.BaseListViewFragment;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.net.CacheManager;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.yhy.common.beans.net.model.discover.TopicInfoResult;
import com.yhy.common.beans.net.model.discover.TopicInfoResultList;
import com.yhy.common.constants.ValueConstants;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:TabCirclesActivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/6/21
 * Time:09:25
 * Version 1.1.0
 */
public class  TabCirclesFragment extends BaseListViewFragment<TopicInfoResult> {

    private CirclesController mCirclesController;

    private int mServerType = 1;
    private long mServerStartNum;

    @Override
    public void fetchData(int pageIndex) {
        if(isRefresh()){
            mServerStartNum = 0;
            mServerType = 1;
        }
        mCirclesController.getCirclesList(getActivity(), pageIndex, ValueConstants.PAGESIZE, mServerType, mServerStartNum);
    }

    @Override
    public void dispatchMessage(Message msg) {
        mBaseDropListView.getPullRefreshView().onRefreshComplete();
        switch (msg.what) {
            case CirclesController.MSG_TOPIC_LIST_OK:
                isDataInitial = true;
                hideErrorView(null);
                TopicInfoResultList topicInfoResultList = (TopicInfoResultList) msg.obj;
                if(topicInfoResultList != null){
                    setHaxNext(topicInfoResultList.hasNext);

                    mServerStartNum = topicInfoResultList.startNum;
                    mServerType = topicInfoResultList.type;
                    handleTopicList(topicInfoResultList);
                }
                break;
            case CirclesController.MSG_TOPIC_LIST_ERROR:
                if(getAdapter().getCount() <= 0){
                    showErrorView(null, IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
                        @Override
                        public void onClick(View view) {
                            manualRefresh();
                        }
                    });
                }
                ToastUtil.showToast(getActivity().getApplicationContext(), StringUtil.handlerErrorCode(getActivity().getApplicationContext(), msg.arg1));
                break;
            case ValueConstants.MSG_HAS_NO_DATA:
                ToastUtil.showToast(getActivity().getApplicationContext(),
                        getResources().getString(R.string.scenic_hasnodata));
                break;
        }
    }

    private void handleTopicList(TopicInfoResultList topicInfoResultList) {
        if (isRefresh()) {
            if (topicInfoResultList.topicInfoResultList != null && topicInfoResultList.topicInfoResultList.size() > 0) {
                getAdapter().replaceAll(topicInfoResultList.topicInfoResultList);
            } else {
                getAdapter().clear();
                showErrorView(null, IActionTitleBar.ErrorType.EMPTYVIEW, getString(R.string.lable_empty_topic_list),
                        getString(R.string.label_nodata_travelnotes_list_message), "点击刷新", new ErrorViewClick() {
                            @Override
                            public void onClick(View view) {
                                mCirclesController.getCirclesList(getActivity(), 1, ValueConstants.PAGESIZE, mServerType, mServerStartNum);
                            }
                        });
            }
        } else {
            if (topicInfoResultList.topicInfoResultList != null && topicInfoResultList.topicInfoResultList.size() > 0) {
                getAdapter().addAll(topicInfoResultList.topicInfoResultList);
            } else {
                ToastUtil.showToast(getActivity().getApplicationContext(),
                        getResources().getString(R.string.scenic_hasnodata));
            }
        }
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mCirclesController = new CirclesController(getActivity(), mHandler);
        mBaseDropListView.getListView().setDividerHeight(0);
        manualRefresh();

        new CacheManager(getActivity(), mHandler).loadTopicListCache();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        manualRefresh();
    }

    private DecimalFormat mDecimalFormat = new DecimalFormat("#.#");
    @Override
    public void convertItem(BaseAdapterHelper helper, TopicInfoResult item) {
        TopicItemHelper.handleTopicItem(getActivity(), helper, item, mDecimalFormat);
    }

    @Override
    public int setItemLayout() {
        return R.layout.fg_circles_item;
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
        int headerviewCount = mBaseDropListView.getListView().getHeaderViewsCount();
        if(position >= headerviewCount) {
            Map<String, String> map = new HashMap<>();
            map.put(AnalyDataValue.KEY_ID, getAdapter().getItem(position - headerviewCount).topicId + "");
            map.put(AnalyDataValue.KEY_NAME, getAdapter().getItem(position - headerviewCount).title);
            TCEventHelper.onEvent(getActivity(), AnalyDataValue.TOPIC_LIST_CLICK, map);
            //事件统计
            Analysis.pushEvent(getActivity(), AnEvent.QUANZI_TOPIC, String.valueOf(getAdapter().getItem(position - headerviewCount).topicId));
            NavUtils.gotoNewTopicDetailsActivity(getActivity(), getAdapter().getItem(position - headerviewCount).title,getAdapter().getItem(position - headerviewCount).topicId);
        }
    }

    public static Fragment getInstance() {
        TabCirclesFragment tabCirclesFragment = new TabCirclesFragment();
        Bundle bundle = new Bundle();
        tabCirclesFragment.setArguments(bundle);
        return tabCirclesFragment;
    }

    public void startLoadData() {
        if (!isDataInitial) {
            if (mCirclesController != null) {
                manualRefresh();
            }
        }
    }

    public PullToRefreshBase<ObservableListView> getPullToRefreshListView() {
        return getPullToRefreshListView();
    }
}
