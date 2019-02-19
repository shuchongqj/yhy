package com.quanyan.yhy.ui.common.person.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshBase;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshScrollDeleteListView;
import com.quanyan.base.view.customview.scrolldeletelistview.ScrollDeleteListView;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.common.IdentityType;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.common.person.PersonController;
import com.yhy.common.beans.net.model.common.person.UserContact;
import com.yhy.common.beans.net.model.common.person.UserContact_ArrayResp;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.AndroidUtils;
import com.yhy.common.utils.SPUtils;
import com.yhy.service.IUserService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:PersonListActivity
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:和平必胜、正义必胜、人民必胜
 * Author:炎黄子孙
 * Date:15/10/30
 * Time:下午5:31
 * Version 1.0
 */
public class PersonListActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2<ScrollDeleteListView>, AdapterView.OnItemClickListener {
	private static int REQUESTCODE_ADD = 101;//添加后返回
	private PersonController mController;

	private PullToRefreshScrollDeleteListView mPullToRefreshScrollDeleteListView;
	private ListView mListview;
	private QuickAdapter<UserContact> mAdapter;
	private long uid;
	private UserContact mDeleteItem;

	@Autowired
    IUserService userService;

    @Override
    public void handleMessage(Message msg) {
        hideLoadingView();
        hideErrorView(null);
        mPullToRefreshScrollDeleteListView.onRefreshComplete();
        switch (msg.what) {
            case ValueConstants.MSG_GET_VISITIOR_LIST_OK://获取列表成功
                handlerContactList((UserContact_ArrayResp) msg.obj);
                break;
            case ValueConstants.MSG_GET_VISITIOR_LIST_KO://获取列表失败
                if (mAdapter.getCount() == 0) {
                    mAdapter.clear();
                    showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
                            IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
                        @Override
                        public void onClick(View view) {
                            //showLoadingView(getString(R.string.loading_text));
                            mController.doGetVisitorList(PersonListActivity.this, uid);
                        }
                    });
                } else {
                    AndroidUtils.showToastCenter(this, StringUtil.handlerErrorCode(this, msg.arg1));
                }
                break;
            case ValueConstants.MSG_DELETE_VISITOR_OK://删除列表成功
                mController.doGetVisitorList(PersonListActivity.this, uid);
                mAdapter.remove(mDeleteItem);
                break;
            case ValueConstants.MSG_DELETE_VISITOR_KO://删除列表失败
                ToastUtil.showToast(PersonListActivity.this, getString(R.string.toast_delete_visitor_failed));
                break;
        }
    }

    private void handlerContactList(UserContact_ArrayResp arrayResp) {

        List<UserContact> value = arrayResp.value;
        if (value != null && value.size() > 0) {
            hideAddNewVisitorInfo();
            mAdapter.replaceAll(value);
        } else {
            mAdapter.clear();
            showAddNewVisitorInfo();//head提示改变
//				showNullPage(mListview, ValueConstants.ERROR_TYPE_NODATA);
        }

    }

    private void showAddNewVisitorInfo() {
        mHeadHintView.setText(R.string.label_hint_no_person_pls_add);
    }

    private void hideAddNewVisitorInfo() {
        mHeadHintView.setText(R.string.title_add_person);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_person_list, null);
    }

    private BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(getString(R.string.title_people_list));
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        uid = userService.getLoginUserId();

        mController = new PersonController(this, mHandler);
        mPullToRefreshScrollDeleteListView = (PullToRefreshScrollDeleteListView) findViewById(R.id.lv_content);
        mPullToRefreshScrollDeleteListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mPullToRefreshScrollDeleteListView.setScrollingWhileRefreshingEnabled(!mPullToRefreshScrollDeleteListView.isScrollingWhileRefreshingEnabled());
        mPullToRefreshScrollDeleteListView.setOnRefreshListener(this);
        mListview = mPullToRefreshScrollDeleteListView.getRefreshableView();

        createHeaderView();
        initData();
        showLoadingView(getString(R.string.loading_text));
        mController.doGetVisitorList(PersonListActivity.this, uid);
        //startLoadDatas(true);
    }

    /**
     * 创建添加头
     */
    private TextView mHeadHintView;

    private void createHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.item_visitor_list_header, null);
        mHeadHintView = (TextView) view.findViewById(R.id.tv_visitor_add_hint);
        mHeadHintView.setText(getString(R.string.title_add_person));
        mListview.addHeaderView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.gotoAddOrUpdatePersonActivity(PersonListActivity.this, REQUESTCODE_ADD, null);
            }
        });
    }

    /**
     * 保存选择
     */
    private void saveContactSelect(UserContact userContact) {
        Intent intent = new Intent();
        //System.out.println("wjm==b=" + userContact.contactName);
        intent.putExtra(SPUtils.EXTRA_DATA, userContact);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void initData() {
        mAdapter = new QuickAdapter<UserContact>(this, R.layout.item_visitor_list, new ArrayList<UserContact>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, final UserContact item) {

                //String str = typeToString(item.certificatesType);
                String str = IdentityType.showIdType(PersonListActivity.this, item.certificatesType);

                helper.setText(R.id.tv_id_type_label, str)
                        .setText(R.id.tv_name, item.contactName)
                        .setText(R.id.tv_phone, item.contactPhone)
                        .setText(R.id.tv_id, "" + item.certificatesNum);
                helper.setOnClickListener(R.id.iv_edit, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        doAddOrUpdateContactInfo(item);
                    }
                });
                helper.setOnClickListener(R.id.cell_visitor_list_delete, new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						mDeleteItem = item;
						doDeleteContactInfo(item);
					}
				});
			}
		};
		mListview.setDividerHeight(0);
		mListview.setAdapter(mAdapter);
		mListview.setOnItemClickListener(this);
	}

    //新增或者更新联系人信息
    private void doAddOrUpdateContactInfo(UserContact userContact) {
        NavUtils.gotoAddOrUpdatePersonActivity(this, REQUESTCODE_ADD, userContact);
    }

    //删除联系人信息
    private void doDeleteContactInfo(final UserContact userContact) {

        mController.doDeleteVisitor(PersonListActivity.this, userContact);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK://添加或修改成功后返回处理
                showLoadingView(getString(R.string.loading_text));
                mController.doGetVisitorList(PersonListActivity.this, uid);
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollDeleteListView> refreshView) {
        mController.doGetVisitorList(PersonListActivity.this, uid);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollDeleteListView> refreshView) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int headerCount = mListview.getHeaderViewsCount();
        if (position >= headerCount) {
            //选择保存
            saveContactSelect(mAdapter.getItem(position - headerCount));
        }
    }

}
