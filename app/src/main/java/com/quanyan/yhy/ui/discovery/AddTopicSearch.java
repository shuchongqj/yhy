package com.quanyan.yhy.ui.discovery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.quanyan.base.BaseActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.manager.HistoryManager;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.circles.CirclesController;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.discovery.controller.DiscoverController;
import com.quanyan.yhy.view.SearchEditText;
import com.yhy.common.beans.net.model.discover.TopicInfoResult;
import com.yhy.common.beans.net.model.discover.TopicInfoResultList;
import com.yhy.common.beans.net.model.search.BaseHistorySearch;
import com.yhy.common.beans.net.model.search.MasterSearchHistoryList;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的直播，添加直播，添加话题标签，搜索
 * <p/>
 * Created by zhaoxp on 2015-10-30.
 */
public class AddTopicSearch extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, TextView.OnEditorActionListener {

    private LinearLayout mHotTopicLayout;
    private LinearLayout mUsedTopicLayout;
    private ListView mListView;
    private QuickAdapter<TopicInfoResult> mSearchAdapter;

    private TextView mNewTitle;
    private TextView mNewContent;

    private SearchEditText mSearchEditText;

    private LinearLayout mSearchLayout;
    private ScrollView mDefaultLayout;

    private DiscoverController mController;
    private CirclesController mCirclesController;

    private HistoryManager mHistoryManager;
    private List<BaseHistorySearch> mBaseHistorySearches;

    /**
     * 添加话题标签
     *
     * @param activity
     * @param reqCode
     */
    public static void gotoAddTopic(Activity activity, int reqCode) {
        Intent intent = new Intent(activity, AddTopicSearch.class);
        activity.startActivityForResult(intent, reqCode);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.add_live_topic_search, null);
    }

    private BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.showSeachView(true, "# 添加话题");
        mBaseNavView.setRightText(R.string.cancel);
        mBaseNavView.setRightTextColor(R.color.black);
        mBaseNavView.setRightTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mHistoryManager = new HistoryManager(this, HistoryManager.TOPIC, mHandler);

        mHotTopicLayout = (LinearLayout) findViewById(R.id.add_topic_hot_layout);
        mUsedTopicLayout = (LinearLayout) findViewById(R.id.add_topic_used_layout);
        mListView = (ListView) findViewById(R.id.base_listview);
        mSearchEditText = (SearchEditText) findViewById(R.id.base_nav_view_edit);
        mSearchLayout = (LinearLayout) findViewById(R.id.add_topic_search_layout);
        mDefaultLayout = (ScrollView) findViewById(R.id.add_topic_default_layout);

        mController = new DiscoverController(this, mHandler);
        mCirclesController = new CirclesController(this, mHandler);

		/*--------------------------实时搜索数据----------------------------------*/
        mSearchAdapter = new QuickAdapter<TopicInfoResult>(this, R.layout.fg_circles_item_search, new ArrayList<TopicInfoResult>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, TopicInfoResult item) {
                handleDefaultItem(helper, item, true);
            }
        };

        mFooterView = getLayoutInflater().inflate(R.layout.fg_circles_item_search, null);
        mNewTitle = (TextView) mFooterView.findViewById(R.id.fg_topic_item_title);
        mNewContent = (TextView) mFooterView.findViewById(R.id.fg_topic_item_content);
        mNewContent.setText("新话题");
        mFooterView.setOnClickListener(this);

        mListView.setDividerHeight(1);
        mListView.setHeaderDividersEnabled(false);
        mListView.setAdapter(mSearchAdapter);
        mListView.setOnItemClickListener(this);
        /*--------------------------实时搜索数据------------end----------------------*/
        initEvent();

        mHistoryManager.loadMasterSearchHistory();
//        mController.doGetLiveAddTopicLabels(AddTopicSearch.this, ValueConstants.TYPE_TAG_LIVESUPTAG, 1, 5);
        mCirclesController.getCirclesList(AddTopicSearch.this, 1, 5, 1, 0);
    }

    /**
     * 处理视图数据
     *
     * @param helper 视图控制类
     * @param item   数据
     */
    private void handleDefaultItem(BaseAdapterHelper helper, TopicInfoResult item, boolean selected) {
        helper.setText(R.id.fg_topic_item_title, item.title);
        helper.setText(R.id.fg_topic_item_content, item.content);
    }

    private String mSearchText = "";

    private void initEvent() {
        mSearchEditText.setOnEditorActionListener(this);
        mSearchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mSearchText = s.toString();
                if (!TextUtils.isEmpty(s.toString())) {
                    if (View.GONE == mSearchLayout.getVisibility()) {
                        mSearchLayout.setVisibility(View.VISIBLE);
                        mDefaultLayout.setVisibility(View.GONE);
                    }
                    mNewTitle.setText(mSearchText);
                    startSearch(mSearchText, 1);
                } else {
                    mSearchLayout.setVisibility(View.GONE);
                    mDefaultLayout.setVisibility(View.VISIBLE);
                    mSearchAdapter.clear();
                    mNewTitle.setText("");
                }
            }
        });
    }

    private View mFooterView;
    private boolean isAddedHeaderView = false;
    /**
     * 添加底部视图
     */
    private void addHeaderView(String searchText) {
        if(!isAddedHeaderView) {
            isAddedHeaderView = true;
            mListView.addHeaderView(mFooterView);
        }

//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//            mListView.setAdapter(mSearchAdapter);
//        }

        if (!TextUtils.isEmpty(searchText)) {
            mNewTitle.setText(searchText);
        } else {
            mNewTitle.setText("");
        }
    }

    /**
     * 添加过的标签(最近使用)
     *
     * @param addedTopicLabels
     */
    private void addAddedLabels(List<BaseHistorySearch> addedTopicLabels) {
        if (addedTopicLabels != null) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = 2;
            for (int i = addedTopicLabels.size() - 1; i >= 0; i--) {
                final String topic = addedTopicLabels.get(i).text;
                TextView textView = new TextView(this);
                textView.setBackgroundColor(Color.WHITE);
                int top = ScreenSize.convertDIP2PX(getApplicationContext(), 10);
                int left = ScreenSize.convertDIP2PX(getApplicationContext(), 15);
                textView.setPadding(left,
                        top,
                        left,
                        top);
                textView.setLayoutParams(layoutParams);
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(15);
                textView.setMaxLines(1);
                textView.setEllipsize(TextUtils.TruncateAt.END);
                textView.setText(addedTopicLabels.get(i).text);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chooseTopic(topic);
                    }
                });
                mUsedTopicLayout.addView(textView);
            }
            if (addedTopicLabels.size() > 0) {
                View view = View.inflate(AddTopicSearch.this, R.layout.add_topic_clear_history, null);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MasterSearchHistoryList masterSearchHistoryList = new MasterSearchHistoryList();
                        if (mBaseHistorySearches != null) {
                            mBaseHistorySearches.clear();
                        } else {
                            mBaseHistorySearches = new ArrayList<>();
                        }
                        masterSearchHistoryList.history = mBaseHistorySearches;
                        mHistoryManager.saveMasterSearchHistory(masterSearchHistoryList);
                        mUsedTopicLayout.removeAllViews();
                    }
                });
                mUsedTopicLayout.addView(view);
            }
        }
    }

    /**
     * 热门标签（热门话题）
     *
     * @param popularTopicLabels
     */
    private void addPopularLabels(List<TopicInfoResult> popularTopicLabels) {
        if (popularTopicLabels != null) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = 2;
            for (int i = 0; i < popularTopicLabels.size(); i++) {
                final String title = popularTopicLabels.get(i).title;
                View view = View.inflate(this, R.layout.fg_circles_item_search, null);
                view.setLayoutParams(layoutParams);
//                BaseImgView.loadimg((ImageView) view.findViewById(R.id.fg_topic_item_img), popularTopicLabels.get(i).pics, R.mipmap.icon_default_150_150, R.mipmap.icon_default_150_150, R.mipmap.icon_default_150_150,
//                        ImageScaleType.EXACTLY, 215, 215, -1);
                ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(popularTopicLabels.get(i).pics), R.mipmap.icon_default_150_150, 215, 215, (ImageView) view.findViewById(R.id.fg_topic_item_img));

                ((TextView) view.findViewById(R.id.fg_topic_item_title)).setText(popularTopicLabels.get(i).title);
                ((TextView) view.findViewById(R.id.fg_topic_item_content)).setText(popularTopicLabels.get(i).content);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chooseTopic(title);
                    }
                });
                mHotTopicLayout.addView(view);
            }
        }
    }

    private void chooseTopic(String topic) {
        MasterSearchHistoryList masterSearchHistoryList = new MasterSearchHistoryList();
        if (mBaseHistorySearches == null) {
            mBaseHistorySearches = new ArrayList<>();
        }
        BaseHistorySearch baseHistorySearch = new BaseHistorySearch(topic);
//        boolean flag = false;
        for (BaseHistorySearch baseHistorySearch1 : mBaseHistorySearches) {
            if (topic.equals(baseHistorySearch1.text)) {
//                flag = true;
                mBaseHistorySearches.remove(baseHistorySearch1);
                break;
            }
        }
        if (mBaseHistorySearches.size() >= 10) {
            mBaseHistorySearches.remove(0);
        }
        mBaseHistorySearches.add(baseHistorySearch);
        masterSearchHistoryList.history = mBaseHistorySearches;
        mHistoryManager.saveMasterSearchHistory(masterSearchHistoryList);
        Intent intent = getIntent();
        intent.putExtra(SPUtils.EXTRA_ADD_LIVE_LABEL, topic);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == mFooterView) {
            String edittext = mNewTitle.getText().toString();
            if (edittext.startsWith("#") && edittext.endsWith("#")) {
                chooseTopic(edittext);
            } else {
                String searchText = "#" + edittext + "#";
                chooseTopic(searchText);
            }
        }
    }

    /**
     * 执行搜索
     *
     * @param seachValues
     * @param pageIndex
     */
    private void startSearch(String seachValues, int pageIndex) {
        mController.doGetLiveAddTopicLabelsSearch(AddTopicSearch.this, seachValues, pageIndex, ValueConstants.PAGESIZE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int headerCount = mListView.getHeaderViewsCount();
        if (position >= headerCount) {
            TCEventHelper.onEvent(AddTopicSearch.this, AnalyDataValue.PUB_LIVE_ADD_TAG);
            String edittext = mSearchAdapter.getItem(position - headerCount).title;
            if (edittext.startsWith("#") && edittext.endsWith("#")) {
                chooseTopic(edittext);
            } else {
                String searchText = "#" + edittext + "#";
                chooseTopic(searchText);
            }
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        hideErrorView(null);
        switch (msg.what) {
            case ValueConstants.MSG_MASTER_SEARCH_HISTORY_OK:
                MasterSearchHistoryList masterSearchHistoryList = (MasterSearchHistoryList) msg.obj;
                mBaseHistorySearches = masterSearchHistoryList.history;
                addAddedLabels(mBaseHistorySearches);
                break;
            //默认的数据
            case DiscoverController.MSG_OK:
                TopicInfoResultList topicInfoResultList = (TopicInfoResultList) msg.obj;
                if (topicInfoResultList != null && topicInfoResultList.topicInfoResultList != null) {
                    addPopularLabels(topicInfoResultList.topicInfoResultList);
                } else {
//                    showErrorView(null,
//                            IActionTitleBar.ErrorType.EMPTYVIEW, "", "", "", null);
                }
                break;
            case DiscoverController.MSG_ERROR:
                showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                        IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
                    @Override
                    public void onClick(View view) {
                        if (!TextUtils.isEmpty(mSearchEditText.getText().toString())) {
                            startSearch(mSearchText, 1);
//                            mController.doGetLiveAddTopicLabels(AddTopicSearch.this, ValueConstants.TYPE_TAG_LIVESUPTAG, 1, ValueConstants.PAGESIZE);
                        }
                    }
                });
                ToastUtil.showToast(AddTopicSearch.this, getString(R.string.error_return_data));
                break;
            //搜索的结果
            case ValueConstants.MSG_TAG_SEARCH_OK:
                TopicInfoResultList topicTitlePageListResult = (TopicInfoResultList) msg.obj;
                if (topicTitlePageListResult != null && topicTitlePageListResult.topicInfoResultList != null
                        && topicTitlePageListResult.topicInfoResultList.size() > 0) {
                    if(isAddedHeaderView){
                        isAddedHeaderView = false;
                        mListView.removeHeaderView(mFooterView);
                    }
                    List<TopicInfoResult> addTopicSearchBean = topicTitlePageListResult.topicInfoResultList;
                    mSearchAdapter.replaceAll(addTopicSearchBean);
                } else {
                    if(!isAddedHeaderView) {
                        addHeaderView(mSearchText);
                    }
                    mSearchAdapter.clear();
                }
                break;
            case ValueConstants.MSG_TAG_SEARCH_ERROR:
                if(!isAddedHeaderView) {
                    addHeaderView(mSearchText);
                }
                ToastUtil.showToast(AddTopicSearch.this, getString(R.string.error_return_data));
                break;

            case CirclesController.MSG_TOPIC_LIST_OK:
                TopicInfoResultList topicInfoResultList2 = (TopicInfoResultList) msg.obj;
                if (topicInfoResultList2 != null && topicInfoResultList2.topicInfoResultList != null) {
                    addPopularLabels(topicInfoResultList2.topicInfoResultList);
                }
                break;
            case CirclesController.MSG_TOPIC_LIST_ERROR:
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//        switch (actionId) {
//            case EditorInfo.IME_ACTION_SEARCH:
//                String searchValue = mSearchEditText.getText().toString();
//                if (!TextUtils.isEmpty(searchValue)) {
//                    if (View.GONE == mSearchLayout.getVisibility()) {
//                        mSearchLayout.setVisibility(View.VISIBLE);
//                        mDefaultLayout.setVisibility(View.GONE);
//                    }
//                    startSearch(searchValue, 1);
//                } else {
//                    mSearchLayout.setVisibility(View.GONE);
//                    mDefaultLayout.setVisibility(View.VISIBLE);
//                }
//                break;
//        }
        return true;
    }

//    /**
//     * 标签点击事件
//     */
//    private class TagClick implements View.OnClickListener {
//        private Activity mContext;
//        private ComTagInfo mComTagInfo;
//
//        public TagClick(Activity context, ComTagInfo comTagInfo) {
//            this.mContext = context;
//            this.mComTagInfo = comTagInfo;
//        }
//
//        @Override
//        public void onClick(View v) {
//            Intent intent = getIntent();
//            intent.putExtra(SPUtils.EXTRA_ADD_LIVE_LABEL, mComTagInfo);
//            setResult(RESULT_OK, intent);
//            mContext.finish();
//        }
//    }
}
