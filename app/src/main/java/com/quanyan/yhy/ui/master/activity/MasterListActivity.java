package com.quanyan.yhy.ui.master.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshListView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.common.ItemType;
import com.quanyan.yhy.common.QueryType;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.master.controller.MasterController;
import com.quanyan.yhy.ui.master.helper.MasterViewHelper;
import com.yhy.common.beans.net.model.club.PageInfo;
import com.yhy.common.beans.net.model.master.QueryTerm;
import com.yhy.common.beans.net.model.master.TalentInfoList;
import com.yhy.common.beans.net.model.master.TalentQuery;
import com.yhy.common.beans.net.model.master.TalentUserInfo;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:ShopHomePageActivity
 * Description:达人列表
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/3/8
 * Time:下午1:18
 * Version 1.0
 */
public class MasterListActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    @ViewInject(R.id.base_pullrefresh_listview_parent_layout)
    private LinearLayout mListViewParent;
    @ViewInject(R.id.pull_to_refresh_listview)
    private PullToRefreshListView plistview;
    private ListView mListView;

    @ViewInject(R.id.ll_filter)
    private LinearLayout mFilterView;
    @ViewInject(R.id.ll_sort)
    private LinearLayout mSortLinerLayout;
    @ViewInject(R.id.ll_subject)
    private LinearLayout mSubjectLinerLayout;
    @ViewInject(R.id.pop_image)
    private ImageView pop_image;

    private List<QueryTerm> mSortList;
    private List<QueryTerm> mSubjectList;

    private MyDownPopwindow mSortPopWindow;
    private MyDownPopwindow mSubjectPopWindow;
    private MyDownPopwindowAdapter themePopAdapter;
    private MyDownPopwindowAdapter addressPopAdapter;

    @ViewInject(R.id.iv_sort_indicate)
    private ImageView mSortIndicateView;
    @ViewInject(R.id.iv_subject_indicate)
    private ImageView mSubjectIndicateView;
    @ViewInject(R.id.tv_sort)
    private TextView mSortTitle;
    @ViewInject(R.id.tv_subject)
    private TextView mSubjectTitle;

    private QuickAdapter quickAdapter;
    private String mSortId, mSubjectId;
    private boolean hasNext = false;
    private int mPageIndex = 1;
    private int state = 0;
    private String mTitle;
    private MasterController mMasterController;

    /**
     * 达人列表
     *
     * @param context
     */
    public static void gotoMasterListActivity(Context context, String title) {
        Intent intent = new Intent(context, MasterListActivity.class);
        if (!StringUtil.isEmpty(title)) {
            intent.putExtra(SPUtils.EXTRA_TITLE, title);
        }
        context.startActivity(intent);
    }
    /**
     * 达人列表
     * @param context
     */
    public static void gotoMasterListActivity(Context context, String tagId,String title) {
        Intent intent = new Intent(context, MasterListActivity.class);
        if (!StringUtil.isEmpty(title)) {
            intent.putExtra(SPUtils.EXTRA_TITLE, title);
        }
        if (!StringUtil.isEmpty(tagId)) {
            intent.putExtra(SPUtils.EXTRA_TAG_ID, tagId);
        }
        context.startActivity(intent);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_master_list, null);
    }

    private BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        mSubjectId = getIntent().getStringExtra(SPUtils.EXTRA_TAG_ID);
        mTitle = getIntent().getStringExtra(SPUtils.EXTRA_TITLE);
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        mMasterController = new MasterController(this, mHandler);
        if (StringUtil.isEmpty(mTitle)) {
            mBaseNavView.setTitleText(R.string.label_master_list_title);
        } else {
            mBaseNavView.setTitleText(mTitle);
        }
        mBaseNavView.setRightImg(R.mipmap.master_top_right_search_btn);
        mBaseNavView.setRightImgClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.gotoSearchActivity(MasterListActivity.this,
                        ItemType.MASTER,
                        null,
                        getString(R.string.label_title_master_search),
                        -1);
                TCEventHelper.onEvent(MasterListActivity.this, AnalyDataValue.TC_ID_SEARCH, AnalyDataValue.SEARCH_CLICK_MASTER_PRODUCT);
            }
        });
        mListView = plistview.getRefreshableView();
        mListView.setDividerHeight(0);
        quickAdapter = new QuickAdapter<TalentUserInfo>(this, R.layout.master_list_item_view, new ArrayList<TalentUserInfo>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, TalentUserInfo item) {
                MasterViewHelper.handleMasterListItem(MasterListActivity.this, helper, item);
            }
        };

        plistview.setMode(PullToRefreshBase.Mode.BOTH);
        plistview.setOnRefreshListener(new MyOnRefreshListener());

        mListView.setAdapter(quickAdapter);
        mListView.setOnItemClickListener(this);
        mMasterController.getFilterList(MasterListActivity.this);
        state = 0;
        showLoadingView(getResources().getString(R.string.scenic_loading_notice));
    }


    /***
     * 设置监听器
     */
    private boolean isRefresh = false;

    class MyOnRefreshListener implements PullToRefreshBase.OnRefreshListener2<ListView> {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            isRefresh = true;
            mPageIndex = 1;
            state = 1;
            hasNext = false;
            doGetMasterList();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            if (hasNext) {
                isRefresh = false;
                hasNext = false;
                state = 2;
                mPageIndex = mPageIndex + 1;
                doGetMasterList();
            } else {
                mHandler.sendEmptyMessageDelayed(ValueConstants.MSG_HAS_NO_DATA, 1000);
            }
        }
    }

    /**
     * 请求列表数据
     */
    private void doGetMasterList() {
        PageInfo pageInfo = new PageInfo();
        pageInfo.pageNo = mPageIndex;
        pageInfo.pageSize = ValueConstants.PAGESIZE;
        TalentQuery query = new TalentQuery();
        if(!StringUtil.isEmpty(mSubjectId)) {
            query.tagId = mSubjectId;
        }
        query.pageInfo = pageInfo;
        if(mSortId != null) {
            query.sort = mSortId;
        }
//        plistview.setRefreshing();
        mMasterController.doSearchMasterList(MasterListActivity.this,query);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_sort:
                if (mSortPopWindow.isShowing()) {
                    mSortPopWindow.dismiss();
                    mSortTitle.setTextColor(getResources().getColor(R.color.neu_333333));
                } else {
                    mSortPopWindow.showAsDropDown(pop_image);
                    mSortIndicateView.setImageResource(R.mipmap.arrow_up_icon);
                    mSortTitle.setTextColor(getResources().getColor(R.color.neu_fa4619));
                }
                break;
            case R.id.ll_subject:
                if (mSubjectPopWindow.isShowing()) {
                    mSubjectPopWindow.dismiss();
                    mSubjectTitle.setTextColor(getResources().getColor(R.color.neu_333333));
                } else {
                    mSubjectPopWindow.showAsDropDown(pop_image);
                    mSubjectIndicateView.setImageResource(R.mipmap.arrow_up_icon);
                    mSubjectTitle.setTextColor(getResources().getColor(R.color.neu_fa4619));
                }
                break;
            case R.id.sm_title_bar_searchbox:
                startActivity(new Intent(MasterListActivity.this, SearchActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int headerCount = mListView.getHeaderViewsCount();
        if(position < headerCount){
            return ;
        }
        TalentUserInfo item =  ((TalentUserInfo) quickAdapter.getItem(position - headerCount));
        NavUtils.gotoMasterHomepage(MasterListActivity.this,item.userId);
    }


    @Override
    public void handleMessage(Message msg) {
        hideLoadingView();
        hideErrorView(null);
        plistview.onRefreshComplete();
        switch (msg.what) {
            case ValueConstants.MSG_GET_MASTER_LIST_OK://成功
                handleNetDataMaster((TalentInfoList) msg.obj);
                break;
            case ValueConstants.MSG_GET_MASTER_LIST_KO://失败
            case ValueConstants.MSG_GET_LINE_FILTER_ERROR:
                if(quickAdapter.getCount() == 0) {
                    showNetErrorView(msg.arg1);
                }else{
                    ToastUtil.showToast(this,StringUtil.handlerErrorCode(this,msg.arg1));
                }
                break;
            case ValueConstants.MSG_GET_LINE_FILTER_OK:
                bindMasterFilterView((QueryTerm) msg.obj);
                break;
            case ValueConstants.MSG_HAS_NO_DATA:
                if (plistview.isRefreshing()) {
                    plistview.onRefreshComplete();
                }
                ToastUtil.showToast(MasterListActivity.this, getResources().getString(R.string.scenic_hasnodata));
                break;
        }
    }

    /**
     * 设置主题和区域下拉框
     *
     * @param list
     */
    private void bindMasterFilterView(QueryTerm list) {
        if(list == null || list.queryTermList == null || list.queryTermList.size() <= 0){
            return ;
        }

        mFilterView.setVisibility(View.VISIBLE);
        if(list.queryTermList.size() > 0) {
            mSortList = list.queryTermList.get(0).queryTermList;
            mSortTitle.setText(mSortList.get(0).text);
            if(mSortList.size() > 0){
                mSortList.get(0).isSelected = true;
            }
        }
        if(list.queryTermList.size() > 1) {
            mSubjectList = list.queryTermList.get(1).queryTermList;
            if(!StringUtil.isEmpty(mSubjectId)){
                for(int k = 0 ; k < mSubjectList.size(); k ++ ){
                    if(QueryType.MASTER_SORT_TYPE.equals(mSubjectList.get(k).type)  &&
                            mSubjectId.equals(mSubjectList.get(k).value)){
                        mSubjectList.get(k).isSelected = true;
                        mSubjectTitle.setText(mSubjectList.get(k).text);
                    }
                }
            }else{
                mSubjectTitle.setText(mSubjectList.get(0).text);
                if(mSubjectList.size() > 0){
                    mSubjectList.get(0).isSelected = true;
                }
            }
        }

        mSortPopWindow = new MyDownPopwindow(this, mSortList, mSubjectList, 0);
        mSubjectPopWindow = new MyDownPopwindow(this, mSortList, mSubjectList, 1);

        mSortLinerLayout.setOnClickListener(this);
        mSubjectLinerLayout.setOnClickListener(this);

        mSortPopWindow.setOnDismissListener(new SortPopDismiss());
        mSubjectPopWindow.setOnDismissListener(new SubjectPopDismiss());

        doGetMasterList();
    }

    /**
     * 绑定达人列表数据
     *
     * @param result
     */
    private void handleNetDataMaster(TalentInfoList result) {
        hideErrorView(null);
        if (result == null) {
            if (state == 0) {
                plistview.setMode(PullToRefreshBase.Mode.DISABLED);
                showNoDataPageView();
            } else if (state == 3) {
                quickAdapter.clear();
                plistview.setMode(PullToRefreshBase.Mode.DISABLED);
                showNoDataPageView();
            }
            return;
        }
        if (result.talentList == null || result.talentList.size() == 0) {
            if (state == 0) {
                plistview.setMode(PullToRefreshBase.Mode.DISABLED);
                showNoDataPageView();
            } else if (state == 3) {
                quickAdapter.clear();
                plistview.setMode(PullToRefreshBase.Mode.DISABLED);
                showNoDataPageView();
            }
            return;
        }
        hasNext = result.hasNext;
        mPageIndex = result.pageNo;
        if (isRefresh) {
            quickAdapter.replaceAll(result.talentList);
            mListView.setSelection(0);
        } else {
            quickAdapter.addAll(result.talentList);
        }
    }

    /**
     * 主题和区域下拉框
     */
    class MyDownPopwindow extends PopupWindow {
        private View contentView;
        private LinearLayout bottom_layout;

        public MyDownPopwindow(final Context context, final List<QueryTerm> sortList, final List<QueryTerm> subjectList, final int num) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            contentView = inflater.inflate(R.layout.scenic_poplayout, null);
            bottom_layout = (LinearLayout) contentView
                    .findViewById(R.id.bottom_layout);
            ListView listView = (ListView) contentView.findViewById(R.id.listview);
            if (num == 0) {
                themePopAdapter = new MyDownPopwindowAdapter(sortList, subjectList, num);
                listView.setAdapter(themePopAdapter);
            } else {
                addressPopAdapter = new MyDownPopwindowAdapter(sortList, subjectList, num);
                listView.setAdapter(addressPopAdapter);
            }
            this.setContentView(contentView); // 设置SelectPicPopupWindow弹出窗体的宽
            this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT); // 设置SelectPicPopupWindow弹出窗体的高
            this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT); // 设置SelectPicPopupWindow弹出窗体可点击
            ColorDrawable dw = new ColorDrawable(0000000000);
            this.setBackgroundDrawable(dw);
            this.setFocusable(true);
            bottom_layout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    class MyDownPopwindowAdapter extends BaseAdapter {
        private List<QueryTerm> sortList;
        private List<QueryTerm> subjectList;
        private int type;

        public MyDownPopwindowAdapter(List<QueryTerm> sortList, List<QueryTerm> subjectList, int type) {
            this.sortList = sortList;
            this.subjectList = subjectList;
            this.type = type;
        }

        @Override
        public int getCount() {
            if (type == 0) {
                return sortList.size();
            } else {
                return subjectList.size();
            }
        }

        @Override
        public Object getItem(int position) {
            if (type == 0) {
                return sortList.get(position);
            } else {
                return subjectList.get(position);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(MasterListActivity.this)
                    .inflate(R.layout.scenic_pop_item_layout, null);
            TextView text = (TextView) convertView.findViewById(R.id.item_text);
            RelativeLayout item_layout = (RelativeLayout) convertView.findViewById(R.id.item_layout);
            if (type == 0) {
                text.setText(sortList.get(position).text.toString());
                if (sortList.get(position).isSelected) {
                    text.setTextColor(getResources().getColor(R.color.main));
                    item_layout.setBackgroundColor(getResources().getColor(R.color.transparent_two));
                } else {
                    text.setTextColor(getResources().getColor(R.color.scenic_list_666));
                    item_layout.setBackgroundResource(R.drawable.scenic_top_translate_selector);
                }
            } else {
                text.setText(subjectList.get(position).text.toString());
                if (subjectList.get(position).isSelected) {
                    text.setTextColor(getResources().getColor(R.color.main));
                    item_layout.setBackgroundColor(getResources().getColor(R.color.transparent_two));
                } else {
                    text.setTextColor(getResources().getColor(R.color.scenic_list_666));
                    item_layout.setBackgroundResource(R.drawable.scenic_top_translate_selector);
                }
            }

            item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type == 0) {
                        if (position == 0) {
                            mSortTitle.setTextColor(getResources().getColor(R.color.neu_333333));
                        } else {
                            mSortTitle.setTextColor(getResources().getColor(R.color.neu_333333));
                        }
                        mSortTitle.setText(sortList.get(position).text.toString());
                        resetThemeDate(position, type);
                        mSortPopWindow.dismiss();
                        mSortId = sortList.get(position).value;
                    } else if (type == 1) {
                        if (position == 0) {
                            mSubjectTitle.setTextColor(getResources().getColor(R.color.neu_333333));
                        } else {
                            mSubjectTitle.setTextColor(getResources().getColor(R.color.neu_333333));
                        }
                        mSubjectTitle.setText(subjectList.get(position).text);
                        resetThemeDate(position, type);
                        mSubjectPopWindow.dismiss();
                        mSubjectId = subjectList.get(position).value;
                    }
                    //筛选列表数据
                    mPageIndex = 1;
                    state = 3;
                    isRefresh = true;
                    hasNext = false;
                    doGetMasterList();
                    showLoadingView(getResources().getString(R.string.scenic_loading_notice));
                }
            });
            return convertView;
        }

    }

    /**
     * 重新设置数据更新界面
     *
     * @param postion
     * @param type
     */
    private void resetThemeDate(int postion, int type) {
        if (type == 0) {
            for (int i = 0; i < mSortList.size(); i++) {
                if (postion == i) {
                    mSortList.get(i).isSelected = true;
                } else {
                    mSortList.get(i).isSelected = false;
                }
            }
            themePopAdapter.notifyDataSetChanged();
        } else if (type == 1) {
            for (int i = 0; i < mSubjectList.size(); i++) {
                if (postion == i) {
                    mSubjectList.get(i).isSelected = true;
                } else {
                    mSubjectList.get(i).isSelected = false;
                }
            }
            addressPopAdapter.notifyDataSetChanged();
        }
    }

    class SubjectPopDismiss implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            mSubjectIndicateView.setImageResource(R.mipmap.arrow_down_icon_normal);
            mSubjectTitle.setTextColor(getResources().getColor(R.color.neu_333333));
        }
    }

    class SortPopDismiss implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            mSortIndicateView.setImageResource(R.mipmap.arrow_down_icon_normal);
            mSortTitle.setTextColor(getResources().getColor(R.color.neu_333333));
        }
    }

    private void showNoDataPageView() {
        showErrorView(mListViewParent, IActionTitleBar.ErrorType.EMPTYVIEW, getString(R.string.no_search_result), "", "", null);
    }

    private void showNetErrorView(int arg1) {
        showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == arg1 ?
                IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
            @Override
            public void onClick(View view) {
                isRefresh = true;
                mPageIndex = 1;
                state = 0;
                hasNext = false;
                mMasterController.getFilterList(MasterListActivity.this);
                showLoadingView(getResources().getString(R.string.scenic_loading_notice));
            }
        });
    }
}
