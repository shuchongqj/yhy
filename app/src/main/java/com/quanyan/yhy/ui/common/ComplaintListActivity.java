package com.quanyan.yhy.ui.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ComplainType;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.club.clubcontroller.ClubController;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.discovery.viewhelper.LiveItemHelper;
import com.yhy.common.beans.net.model.discover.UgcInfoResult;
import com.yhy.common.beans.net.model.rc.ComplaintInfo;
import com.yhy.common.beans.net.model.rc.ComplaintOptionInfo;
import com.yhy.common.beans.net.model.rc.ComplaintOptionInfoList;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:ComplaintListActivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/5/7
 * Time:下午4:23
 * Version 1.1.0
 */
public class ComplaintListActivity extends BaseActivity {
    private ClubController mController;

    private LinearLayout mLLSubmitOK;
    private ListView mLvComList;

    private QuickAdapter<ComplaintOptionInfo> mCompListAdapter;
    private List<ComplaintOptionInfo> mDataList = new ArrayList<ComplaintOptionInfo>();

    private String textContent;
    private List<String> picList;
    private long id;
//    public UgcInfoResult mSubjectId;

    /**
     * 举报
     *
     * @param context
     */
    public static void gotoComplaintList(Context context, String textContent, ArrayList<String> picList, long id) {
        Intent intent = new Intent(context, ComplaintListActivity.class);
        intent.putExtra("textContent", textContent);
        intent.putStringArrayListExtra("picList", picList);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    public void handleMessage(Message msg) {
        hideLoadingView();
        switch (msg.what) {
            case ValueConstants.MSG_GET_COMPLAINT_LIST_OK:
                handleComplaintList((ComplaintOptionInfoList) msg.obj);
                break;
            case ValueConstants.MSG_GET_COMPLAINT_LIST_KO:
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;
            case ValueConstants.MSG_SUBMIT_COMPLAINT_OK:
                showComplaintSuccessfulPage();
                break;
            case ValueConstants.MSG_SUBMIT_COMPLAINT_KO:
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;
        }
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_complaint_list, null);
    }

    private BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(getString(R.string.label_title_complaint));
        mBaseNavView.setRightText(getString(R.string.commit));
        mBaseNavView.setRightTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSubmitComplaint();
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
        mController = new ClubController(this, mHandler);
//        mSubjectId = (UgcInfoResult)getIntent().getSerializableExtra(SPUtils.EXTRA_DATA);

        textContent = getIntent().getStringExtra("textContent");
        picList = getIntent().getStringArrayListExtra("picList");
        id = getIntent().getLongExtra("id", -1);

        mLvComList = (ListView) findViewById(R.id.lv_complaint_list);
        mLLSubmitOK = (LinearLayout) findViewById(R.id.ll_comlaint_success);

        mCompListAdapter = new QuickAdapter<ComplaintOptionInfo>(this, R.layout.item_complaint_list, mDataList) {
            @Override
            protected void convert(BaseAdapterHelper helper, ComplaintOptionInfo item) {
                LiveItemHelper.handleComplaintItem(helper, item);
            }
        };
        View headerView = LayoutInflater.from(this).inflate(R.layout.header_complaint_list, null);
        mLvComList.addHeaderView(headerView);
        mLvComList.setAdapter(mCompListAdapter);

        mLvComList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int headCount = mLvComList.getHeaderViewsCount();
                if (position < headCount) {
                    return;
                }
                ComplaintOptionInfo clickInfo = mDataList.get(position - headCount);
                for (ComplaintOptionInfo info : mDataList) {
                    if (info.id == clickInfo.id) {
                        info.isChecked = true;
                    } else {
                        info.isChecked = false;
                    }
                }
                mCompListAdapter.replaceAll(mDataList);
            }
        });

        startLoadData();
    }

    /**
     * 请求数据
     */
    private void startLoadData() {
        mController.doGetComplaintOptions(ComplaintListActivity.this);
    }

    /**
     * 处理投诉列表内容
     *
     * @param value
     */
    private void handleComplaintList(ComplaintOptionInfoList value) {
        if (value == null) {
            return;
        }

        mDataList = value.complaintOptionInfos;
        if (mDataList != null && mDataList.size() > 0) {
            mDataList.get(0).isChecked = true;
        }
        mCompListAdapter.replaceAll(mDataList);
        mCompListAdapter.notifyDataSetChanged();
    }

    /**
     * 提交投诉
     */
    private void doSubmitComplaint() {
        //事件统计
        Analysis.pushEvent(this, AnEvent.PLAY_PAGE_REPORT);

        if (mDataList == null || mDataList.size() == 0) {
            return;
        }
        ComplaintOptionInfo info = null;
        for (ComplaintOptionInfo item : mDataList) {
            if (item.isChecked) {
                info = item;
                break;
            }
        }
        ComplaintInfo complaintInfo = new ComplaintInfo();
        complaintInfo.optionId = info.id;
        complaintInfo.reasonContent = info.title;
        complaintInfo.outType = ComplainType.SUBJECT;
        if (!StringUtil.isEmpty(textContent)) {
            complaintInfo.outContent = textContent;
        }
        if (picList != null) {
            complaintInfo.outPicUrls = picList;
        }
        complaintInfo.outId = id;
        mController.doSubmitComplaint(ComplaintListActivity.this, complaintInfo);

        showLoadingView("");
    }

    /**
     * 展示投诉成功的页面
     */
    private void showComplaintSuccessfulPage() {
        mLLSubmitOK.setVisibility(View.VISIBLE);
        mLvComList.setVisibility(View.GONE);

        mBaseNavView.setTitleText(getString(R.string.label_title_complaint_ok));
        mBaseNavView.setRightText(R.string.label_btn_finish);
        mBaseNavView.setRightTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
