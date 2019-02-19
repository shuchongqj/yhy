package com.quanyan.yhy.ui.common.person.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.common.person.PersonController;
import com.quanyan.yhy.ui.common.person.adapter.VisitorInfoAdapter;
import com.yhy.common.beans.net.model.common.person.UserContact;
import com.yhy.common.beans.net.model.common.person.UserContact_ArrayResp;
import com.yhy.common.beans.net.model.common.person.VisitorInfoList;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.AndroidUtils;
import com.yhy.common.utils.SPUtils;
import com.yhy.service.IUserService;

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
public class VisitorListActivity extends BaseActivity {
	private VisitorInfoAdapter mAdapter;

	public static int TYPE_FROM_SETTING = 1;//设置进入
	public static int TYPE_FROM_VISITOR = 3;//选择游客进入
	private static int REQUESTCODE_ADD = 101;//添加后返回
	private PersonController mController;
	private long uid;
	private int type;
	private int maxNum;
	private VisitorInfoList visitorInfoList;
	private LinearLayout ll_visitor;

	private PullToRefreshScrollDeleteListView mPullToRefreshScrollDeleteListView;
	private ListView mListView;

	@Autowired
	IUserService userService;
	/**
	 * 跳转到游客列表
	 */
	/*public static void gotoVisitorListActivity(Activity context,int reqCode){
        Intent intent = new Intent(context,VisitorListActivity.class);
        context.startActivityForResult(intent, reqCode);
    }*/


	@Override
	public void handleMessage(Message msg) {
		hideLoadingView();
		hideErrorView(null);
		mPullToRefreshScrollDeleteListView.onRefreshComplete();
		switch (msg.what) {
			case ValueConstants.MSG_GET_VISITIOR_LIST_OK://获取游客列表成功
				handlerVisitorList((UserContact_ArrayResp) msg.obj);
				break;
			case ValueConstants.MSG_GET_VISITIOR_LIST_KO:
				if(mAdapter.getCount() == 0){
					mAdapter.clearData();
					showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
							IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
						@Override
						public void onClick(View view) {
							showLoadingView(getString(R.string.loading_text));
							mController.doGetVisitorList(VisitorListActivity.this,uid);
						}
					});
				}else {
					AndroidUtils.showToastCenter(this, StringUtil.handlerErrorCode(this, msg.arg1));
				}
				break;
			case ValueConstants.MSG_DELETE_VISITOR_OK://删除游客列表成功
				mController.doGetVisitorList(VisitorListActivity.this,uid);
				break;
			case ValueConstants.MSG_DELETE_VISITOR_KO:
				ToastUtil.showToast(VisitorListActivity.this, getString(R.string.toast_delete_visitor_failed));
				break;
		}
	}


	private void handlerVisitorList(UserContact_ArrayResp arrayResp) {

		List<UserContact> value = arrayResp.value;
		if (value != null && value.size() > 0) {
			hideAddNewVisitorInfo();
			mAdapter.setData(value);
			//选择过的联系人状态
			if (visitorInfoList != null) {
				List<UserContact> userContacts = visitorInfoList.value;
				mAdapter.checkStatusChange(userContacts);
			}

		} else {
			mAdapter.clearData();
			showAddNewVisitorInfo();//head头改变
			//showNullPage(ll_visitor, ValueConstants.ERROR_TYPE_NODATA);
		}

	}

	private void showAddNewVisitorInfo() {
		mHeadHintView.setText(R.string.label_hint_no_visitor_pls_add);
	}

	private void hideAddNewVisitorInfo() {
		mHeadHintView.setText(R.string.label_add_visitor);
	}

	@Override
	public View onLoadContentView() {
		return View.inflate(this, R.layout.ac_visitor_list, null);
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
		uid = userService.getLoginUserId();
		type = getIntent().getIntExtra(SPUtils.EXTRA_TYPE, -1);
		maxNum = getIntent().getIntExtra(SPUtils.EXTRA_TAG_ID, -1);
		visitorInfoList = (VisitorInfoList) getIntent().getSerializableExtra(SPUtils.EXTRA_DATA);
		initView();

		mController = new PersonController(this, mHandler);
		mPullToRefreshScrollDeleteListView = (PullToRefreshScrollDeleteListView) findViewById(R.id.lv_content);
		mPullToRefreshScrollDeleteListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		mPullToRefreshScrollDeleteListView.setScrollingWhileRefreshingEnabled(!mPullToRefreshScrollDeleteListView.isScrollingWhileRefreshingEnabled());
		mListView = mPullToRefreshScrollDeleteListView.getRefreshableView();
		ll_visitor = (LinearLayout) findViewById(R.id.ll_visitor);
		createHeaderView();
		initData();
		//网络访问
		showLoadingView(getString(R.string.loading_text));
		mController.doGetVisitorList(VisitorListActivity.this,uid);
	}

	private void initView() {
		if (TYPE_FROM_SETTING == type) {
			mBaseNavView.setTitleText(getString(R.string.title_visitors_list));

		} else if (TYPE_FROM_VISITOR == type) {
			mBaseNavView.setTitleText(getString(R.string.title_visitors_select));
			mBaseNavView.setRightText(getString(R.string.label_btn_ok));
			mBaseNavView.setRightTextClick(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					saveVisitorsSelect();
				}
			});
		}

	}

	/**
	 * 保存选择
	 */
	private void saveVisitorsSelect() {
		Intent intent = new Intent();
		VisitorInfoList visitorInfoList = new VisitorInfoList();
		visitorInfoList.value = mAdapter.getSelectedVisitors();
		intent.putExtra(SPUtils.EXTRA_DATA, visitorInfoList);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	/**
	 * 创建添加头
	 */
	private TextView mHeadHintView;

	private void createHeaderView() {
		View view = getLayoutInflater().inflate(R.layout.item_visitor_list_header, null);
		mHeadHintView = (TextView) view.findViewById(R.id.tv_visitor_add_hint);
		mHeadHintView.setText(getString(R.string.label_add_visitor));

		mListView.addHeaderView(view);
        /*if(TYPE_FROM_SETTING == type){
            mHeadHintView.setText(getString(R.string.label_add_visitor));
        }else if(TYPE_FROM_VISITOR == type){
            mHeadHintView.setText(getString(R.string.label_add_visitor));
        }*/
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				NavUtils.gotoAddOrUpdateVisitorActivity(VisitorListActivity.this, REQUESTCODE_ADD, null);
			}
		});
	}

	private void initData() {
		mAdapter = new VisitorInfoAdapter(this, type);

		mListView.setDividerHeight(0);
		mListView.setAdapter(mAdapter);

		//监听回调
		mAdapter.setOnVisitorItemClickLsn(new VisitorInfoAdapter.OnVisitorItemClickLsn() {
			@Override
			public void onEditClick(UserContact userContact) {
				doAddOrUpdateVisitorInfo(userContact);//修改操作
			}

			@Override
			public void onDeleteClick(UserContact userContact) {
				doDeleteVisitorInfo(userContact);//删除
			}
		});

		mPullToRefreshScrollDeleteListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollDeleteListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollDeleteListView> refreshView) {
				mController.doGetVisitorList(VisitorListActivity.this,uid);//下拉
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollDeleteListView> refreshView) {

			}
		});


		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int headerCount = mListView.getHeaderViewsCount();
				int newPosition = position - headerCount;
				if (newPosition >= 0) {
					UserContact userContact = (UserContact) mAdapter.getItem(newPosition);
					userContact.isChoosed = userContact.isChoosed ? false : true;

					//至多选择多少
					if(TYPE_FROM_VISITOR == type && maxNum > 0){
						List<UserContact> selectedVisitors = mAdapter.getSelectedVisitors();
						if(selectedVisitors.size() > maxNum){
							ToastUtil.showToast(VisitorListActivity.this, "最多选择" + maxNum + "名游客");
							userContact.isChoosed = false;
						}

					}

					((ImageView) view.findViewById(R.id.iv_check)).setImageResource(userContact.isChoosed ? R.mipmap.ic_checked : R.mipmap.ic_uncheck);
				}
			}
		});

	}

	//新增或者更新旅客信息
	private void doAddOrUpdateVisitorInfo(UserContact userContact) {
		NavUtils.gotoAddOrUpdateVisitorActivity(this, REQUESTCODE_ADD, userContact);
	}

	//删除旅客信息
	private void doDeleteVisitorInfo(final UserContact userContact) {

		mController.doDeleteVisitor(VisitorListActivity.this,userContact);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
			case RESULT_OK:
				showLoadingView(getString(R.string.loading_text));
				mController.doGetVisitorList(VisitorListActivity.this,uid);
				break;
		}
	}

	/**
	 * 加载数据
	 */
    /*private void startLoadDatas(boolean isClean) {
        if (isClean) {
            //mPageIndex = 1;
            //userContacts.clear();
        }
        //mController.doGetVisitorList(uid);
    }*/
}
