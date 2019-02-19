package com.videolibrary.puhser.activity;

import android.os.Bundle;
import android.os.Message;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.lidroid.xutils.util.LogUtils;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshGridView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.videolibrary.controller.LiveController;
import com.videolibrary.metadata.LiveTypeConstants;
import com.videolibrary.utils.IntentUtil;
import com.yhy.common.beans.net.model.msg.LiveRecordAPIPageResult;
import com.yhy.common.beans.net.model.msg.LiveRecordResult;
import com.yhy.common.constants.ValueConstants;
import com.yhy.service.IUserService;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <Li>我的直播记录列表</Li>
 *
 * @author zhaoxiaopo 2016年08月03日18:16:57
 */
public class LiveRecordActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2<GridView>, AdapterView.OnItemClickListener {

    private PullToRefreshGridView mPullToRefreshGridView;
    private GridView mGridView;
    private QuickAdapter<LiveRecordResult> mQuickAdapter;
    private LinearLayout.LayoutParams layoutParams;
    private LinearLayout mDeleteLayout;

    private long userId = -1;

    private SparseArray<Long> mSparseLongArray = new SparseArray();
    private boolean choosedStatus = false;

    private boolean haxNext = true;
    private boolean isRefresh = true;
    private int mPageIndex = 1;

    private List<String> mFetchTypes = new ArrayList<>();
    private List<String> mLiveRecordStatus = new ArrayList<>();

    private SimpleDateFormat mSimpleDateFormat;

    @Autowired
    IUserService userService;

    @Override
    protected void initView(Bundle savedInstanceState) {
        Bundle bundle = null;
        if (getIntent() != null) {
            bundle = getIntent().getExtras();
            userId = bundle.getLong(IntentUtil.BUNDLE_USERID, -1);
        }

        mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mFetchTypes.add(LiveTypeConstants.LIVE_REPLAY);
        mLiveRecordStatus.add(LiveTypeConstants.OFF_SHELVE_LIVE);
        mLiveRecordStatus.add(LiveTypeConstants.NORMAL_LIVE);

        mPullToRefreshGridView = (PullToRefreshGridView) findViewById(R.id.ac_live_record_pullrefresh_gridview);
        mDeleteLayout = (LinearLayout) findViewById(R.id.ac_live_record_delete_layout);

        mQuickAdapter = new QuickAdapter<LiveRecordResult>(this, R.layout.item_live_record_grid, new ArrayList<LiveRecordResult>()) {
            @Override
            protected void convert(final BaseAdapterHelper helper, final LiveRecordResult item) {
                RelativeLayout relativeLayout = helper.getView(R.id.item_live_record_img_layout);
                relativeLayout.setLayoutParams(layoutParams);
                helper.setImageUrl(R.id.item_live_record_img, item.liveCover, 360, 280, R.mipmap.icon_default_750_420);
                helper.setVisible(R.id.item_live_record_choose_layout, choosedStatus);
                helper.setSelected(R.id.item_live_record_choose_img, item.isChoosed);
                helper.setText(R.id.item_live_record_title, item.liveTitle);
                helper.setText(R.id.item_live_record_time, mSimpleDateFormat.format(new Date(item.startDate)));
                String num;
                if (item.viewCount >= 999000) {
                    helper.setText(R.id.cell_live_live_people_num, "999万+");//直播在线人数
                } else if (item.viewCount >= 10000) {
                    num = (new DecimalFormat("#.##").format(item.viewCount / 10000.0f));
                    helper.setText(R.id.item_live_record_view_count, num + "万看过");
                } else {
                    helper.setText(R.id.item_live_record_view_count, item.viewCount + "看过");
                }
                if(LiveTypeConstants.OFF_SHELVE_LIVE.equals(item.status)){
                    helper.setVisible(R.id.item_live_record_shelves_over, true);
                }else{
                    helper.setVisible(R.id.item_live_record_shelves_over, false);
                }
//                if(true){
//                    //是否显示已下架
//                    helper.setVisible(R.id.item_live_record_shelves_over, true);
//                }else{
//                    helper.setVisible(R.id.item_live_record_shelves_over, false);
//                }
                helper.setOnClickListener(R.id.item_live_record_choose_layout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeChooseStatus(item, helper.getPosition());
                        helper.setSelected(R.id.item_live_record_choose_img, item.isChoosed);
                    }
                });
            }
        };

        int width = (ScreenSize.getScreenWidth(getApplicationContext()) - 60) / 2;
        int height = (width * 194) / 344;
        layoutParams = new LinearLayout.LayoutParams(width, height);
        mGridView = mPullToRefreshGridView.getRefreshableView();
        mGridView.setNumColumns(2);
        mGridView.setColumnWidth((ScreenSize.getScreenWidth(getApplicationContext()) - 60) / 2);
        mGridView.setHorizontalSpacing(20);
        mGridView.setPadding(20, 20, 20, 20);

        mPullToRefreshGridView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshGridView.setOnRefreshListener(this);
        mGridView.setAdapter(mQuickAdapter);
        mGridView.setOnItemClickListener(this);

        mDeleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 9/7/16 删除直播记录
                TCEventHelper.onEvent(LiveRecordActivity.this, AnalyDataValue.MINE_LIVE_DELETE_CLICK);
                if (mSparseLongArray.size() > 0) {
                    long[] ids = new long[mSparseLongArray.size()];
                    for (int i = 0; i < mSparseLongArray.size(); i++) {
                        int key = mSparseLongArray.keyAt(i);
                        ids[i] = mSparseLongArray.get(key);
                    }

                    deleteReplay(ids);

                    //reset the right click text status
                    choosedStatus = false;
                    for(int i = 0; i < mSparseLongArray.size(); i ++){
                        int key = mSparseLongArray.keyAt(i);
                        mQuickAdapter.getItem(key).isChoosed = false;
                    }

                    mSparseLongArray.clear();
                    mBaseNavView.setRightText(R.string.live_record_choose);
                    mDeleteLayout.setVisibility(View.GONE);
                    mQuickAdapter.notifyDataSetChanged();
                }
            }
        });

        onPullDownToRefresh(mPullToRefreshGridView);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_live_record, null);
    }

    private BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText("我的直播");
        mBaseNavView.setRightText(R.string.live_record_choose);
        mBaseNavView.setRightTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQuickAdapter.getCount() > 0) {
                    if (getString(R.string.live_record_choose).equals(mBaseNavView.getRightText())) {
                        choosedStatus = true;
                        mBaseNavView.setRightText(R.string.cancel);
                        mDeleteLayout.setVisibility(View.VISIBLE);
                        isEmptyDelete();
                    } else {
                        choosedStatus = false;
                        for(int i = 0; i < mSparseLongArray.size(); i ++){
                            int key = mSparseLongArray.keyAt(i);
                            mQuickAdapter.getItem(key).isChoosed = false;
                        }
                        mSparseLongArray.clear();
                        mBaseNavView.setRightText(R.string.live_record_choose);
                        mDeleteLayout.setVisibility(View.GONE);
                    }
                    mQuickAdapter.notifyDataSetChanged();
                }
            }
        });
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        mPullToRefreshGridView.onRefreshComplete();
        hideErrorView(null);
        switch (msg.what) {
            case LiveController.MSG_LIVE_LIST_OK:
                LiveRecordAPIPageResult liveRecordAPIPageResult = (LiveRecordAPIPageResult) msg.obj;
                if (liveRecordAPIPageResult != null) {
                    haxNext = liveRecordAPIPageResult.hasNext;
                }
                handleListData(liveRecordAPIPageResult);
                break;
            case LiveController.MSG_LIVE_LIST_ERROR:
                if (isRefresh) {
                    showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                            IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
                        @Override
                        public void onClick(View view) {
                            onPullDownToRefresh(mPullToRefreshGridView);
                        }
                    });
                } else {
                    ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
                }
                break;
            case ValueConstants.MSG_HAS_NO_DATA:
                ToastUtil.showToast(this, getString(R.string.no_more_data));
                break;

            case LiveController.MSG_DELTE_REPLAY_OK:
                ToastUtil.showToast(this, "删除成功");
                onPullDownToRefresh(mPullToRefreshGridView);
                break;
            case LiveController.MSG_DELTE_REPLAY_ERROR:
                ToastUtil.showToast(this, "删除失败");
                break;
        }
    }

    private void handleListData(LiveRecordAPIPageResult result) {
        if (isRefresh) {
            if (result != null && result.list != null && result.list.size() > 0) {
                mQuickAdapter.replaceAll(result.list);
            } else {
                mQuickAdapter.clear();
                showErrorView(null, IActionTitleBar.ErrorType.EMPTYVIEW, "暂无数据", " ", "", null);
            }
        } else {
            if (result != null && result.list != null) {
                mQuickAdapter.addAll(result.list);
            }
        }
    }

    private void fetchData(int pageIndex) {
        LiveController.getInstance().getLivelistByUserId(this,
                mHandler,
                userId,
                mFetchTypes,
                mLiveRecordStatus,
                pageIndex, ValueConstants.PAGESIZE);
    }

    private void deleteReplay(long[] deleteIds) {
        LiveController.getInstance().deleteReplay(this, mHandler, deleteIds);
    }

    /**
     * onPullDownToRefresh will be called only when the user has Pulled from
     * the start, and released.
     *
     * @param refreshView
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
        mPageIndex = 1;
        isRefresh = true;
        mSparseLongArray.clear();
        isEmptyDelete();
        fetchData(mPageIndex);
    }

    /**
     * onPullUpToRefresh will be called only when the user has Pulled from
     * the end, and released.
     *
     * @param refreshView
     */
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
        isRefresh = false;
        if (haxNext) {
            mPageIndex++;
            fetchData(mPageIndex);
        } else {
            mHandler.sendEmptyMessage(ValueConstants.MSG_HAS_NO_DATA);
        }
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (choosedStatus) {
            changeChooseStatus(mQuickAdapter.getItem(position), position);
            mQuickAdapter.notifyDataSetChanged();
        } else {
            LiveRecordResult liveRecordResult = mQuickAdapter.getItem(position);
            if (liveRecordResult != null) {
                IntentUtil.startVideoClientActivity(liveRecordResult.liveId,
                        userService.getLoginUserId(), false,liveRecordResult.liveScreenType);
            }
        }
    }

    /**
     * 更新item选中状态，<key, value>, key: the item's position, value: the status if be choosed or not;
     * @param item
     * @param position
     */
    private void changeChooseStatus(LiveRecordResult item, int position) {
        item.isChoosed = item.isChoosed ? false : true;
        if (item.isChoosed) {
            mSparseLongArray.put(position, item.liveId);
        } else {
            mSparseLongArray.remove(position);
        }

        isEmptyDelete();

        LogUtils.d("delete ids --> " + mSparseLongArray.toString());
    }

    private void isEmptyDelete(){
        if(mSparseLongArray.size() == 0){
            mDeleteLayout.setBackgroundColor(getResources().getColor(R.color.neu_cccccc));
            mDeleteLayout.setEnabled(false);
            mDeleteLayout.invalidate();
        }else{
            mDeleteLayout.setBackgroundResource(R.drawable.btn_delete_replay_bg);
            mDeleteLayout.setEnabled(true);
            mDeleteLayout.invalidate();
        }
    }
}
