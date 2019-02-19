package com.quanyan.yhy.ui.discovery.fragment;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.quanyan.base.BaseFragment;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.eventbus.EvBusLiveComPraiNum;
import com.quanyan.yhy.eventbus.EvBusSubjectInfo;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.discovery.controller.DiscoverController;
import com.yhy.common.beans.net.model.comment.SupportUserInfo;
import com.yhy.common.beans.net.model.comment.SupportUserInfoList;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * 直播详情点赞列表
 * <p/>
 * Created by zhaoxp on 2015-11-2.
 */
public class LiveDetailApprasieFragment extends BaseFragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

	private View mFooterView;
	private ListView mListView;
	private QuickAdapter<SupportUserInfo> mAdapter;
	protected DiscoverController mController;
	private long mOutId = -1;
	private String mOutType;

	private boolean isTabClick = false;

	/**
	 * @param subjectId
	 * @return
	 */
	public static LiveDetailApprasieFragment getInstance(long subjectId, String outType) {
		LiveDetailApprasieFragment fragment = new LiveDetailApprasieFragment();
		Bundle bundle = new Bundle();
		bundle.putLong(SPUtils.EXTRA_ID, subjectId);
		bundle.putString(SPUtils.EXTRA_TYPE, outType);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		EventBus.getDefault().unregister(this);
	}

	@Override
	protected void initView(View view, Bundle savedInstanceState) {
		EventBus.getDefault().register(this);
		mListView = (ListView) view.findViewById(R.id.id_stickynavlayout_innerscrollview);
//		mAdapter = new QuickAdapter<SupportUserInfo>(getActivity(), R.layout.cell_live_detail_praise, new ArrayList<SupportUserInfo>()) {
		mAdapter = new QuickAdapter<SupportUserInfo>(getActivity(), R.layout.cell_club_detail_member, new ArrayList<SupportUserInfo>()) {
			@Override
			protected void convert(BaseAdapterHelper helper, SupportUserInfo item) {
				handleAppraiseItem(helper, item);
			}
		};
		mFooterView = getActivity().getLayoutInflater().inflate(R.layout.footer_loading_more, null);
		mListView.addFooterView(mFooterView);
		mListView.setDividerHeight(1);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mListView.setOnScrollListener(this);
	}

	private void handleAppraiseItem(BaseAdapterHelper helper, SupportUserInfo item) {
		helper.setImageUrlRound(R.id.cell_club_detail_member_user_head, item.avatar,128,128,R.mipmap.icon_default_avatar)
				.setText(R.id.cell_club_detail_member_user_name, TextUtils.isEmpty(item.nick) ? "" : item.nick)
//				.setText(R.id.cell_live_detail_appraise_user_sex, item.age );
				.setVisible(R.id.cell_club_detail_member_user_info, false);
		if(ValueConstants.TYPE_SEX_MALE.equals(item.gender)){
			helper.setBackgroundRes(R.id.cell_club_detail_member_user_sex, R.mipmap.male);
		}else if(ValueConstants.TYPE_SEX_FEMAIL.equals(item.gender)){
			helper.setBackgroundRes(R.id.cell_club_detail_member_user_sex, R.mipmap.female);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		int headerCount = mListView.getHeaderViewsCount();
		if(position >= headerCount) {
//			NavUtils.gotoCatePersonageFragment(getActivity(), mAdapter.getItem(position - headerCount).userId + "");
//			NavUtils.gotoCatePersonageFragment(getActivity(), mAdapter.getItem(position - headerCount));
			SupportUserInfo userInfo = mAdapter.getItem(position - headerCount);
//			NavUtils.gotoPersonalPage(getActivity(), userInfo.userId,userInfo.nick,userInfo.options);
			NavUtils.gotoMasterHomepage(getActivity(), userInfo.userId);

		}
	}

	/**
	 * 加载数据
	 * @param pageIndex
	 */
	private void startLoadData(int pageIndex) {
		showLoadingView(getString(R.string.loading_text));
		if(1 == pageIndex){
			isRefresh = true;
		}else{
			isRefresh = false;
		}
		if (-1 != mOutId) {
			mController.doGetLiveDetailAppraisePeople(getActivity(),mOutId, mOutType, pageIndex, ValueConstants.PAGESIZE);
		} else {
			ToastUtil.showToast(getActivity(), getString(R.string.error_params));
		}
	}

	private boolean isRefresh = true;//刷新的状态（true：刷新，false，加载更多）
	private boolean isShow = false;//控制刷新显示次数
	private boolean isHasNext = false;//是否还有下一页

	/**
	 * 更新数据
	 */
	public void updateData(boolean isTabClick) {
		if(mController == null){
			return;
		}
		this.isTabClick = isTabClick;
		isRefresh = true;
		if (!isShow) {
			isShow = true;
			if (mListView != null && mListView.getFirstVisiblePosition() != 0) {
				mListView.setSelection(0);
			}
//			mListView.addHeaderView(mHeaderView);
			startLoadData(1);
		}
	}


	/**
	 * 更新数据列表
	 *
	 * @param evBusSubjectInfo
	 */
	public void onEvent(EvBusSubjectInfo evBusSubjectInfo) {
		if (evBusSubjectInfo != null) {
			boolean delete = evBusSubjectInfo.isDelete();
			if (!delete) {
				updateData(true);
			}
		}
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		if(!isDetached()){
			hideLoadingView();
			hideErrorView(null);
//			if (isRefresh) {
//				mListView.removeHeaderView(mHeaderView);
//			} else {
//				mListView.removeFooterView(mFooterView);
//			}
			isShow = false;
			switch (msg.what) {
				case DiscoverController.MSG_LIVE_DETAIL_COMMENT_OK: {
					SupportUserInfoList supportUserInfoList = (SupportUserInfoList) msg.obj;
					if (supportUserInfoList == null || supportUserInfoList.supportUserInfoList == null || supportUserInfoList.supportUserInfoList.size() < ValueConstants.PAGESIZE) {
						isHasNext = false;
					} else {
						isHasNext = true;
					}
					if (isRefresh) {
						if (supportUserInfoList != null && supportUserInfoList.supportUserInfoList != null) {
							if(supportUserInfoList.supportUserInfoList.size() <= 0) {
								showNoDataPage();
							}
							mAdapter.replaceAll(supportUserInfoList.supportUserInfoList);
						} else {
							mAdapter.clear();
							showNoDataPage();
						}
					} else {
						if (supportUserInfoList != null && supportUserInfoList.supportUserInfoList != null) {
							mAdapter.addAll(supportUserInfoList.supportUserInfoList);
						}
					}

					if(supportUserInfoList == null){
						if(mAdapter.getCount() > 0){
							EventBus.getDefault().post(new EvBusLiveComPraiNum(mAdapter.getCount(), 2));
						}else {
							EventBus.getDefault().post(new EvBusLiveComPraiNum(0, 2));
						}
					}else{
						if(isTabClick){
							EventBus.getDefault().post(new EvBusLiveComPraiNum(supportUserInfoList.count, 2));
						}
					}
					break;
				}
				case DiscoverController.MSG_LIVE_DETAIL_COMMENT_ERROR: {
					if (isRefresh) {
						showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
								IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
							@Override
							public void onClick(View view) {
								updateData(false);
							}
						});
					}
					break;
				}
			}
			isOnBottomLoading = false;
			if (!isHasNext) {
				mListView.removeFooterView(mFooterView);
			}
		}
	}

	private void showNoDataPage(){
		showErrorView(null, IActionTitleBar.ErrorType.EMPTYVIEW, getString(R.string.label_nodata_priase), " ", "", null);
	}
	private int mScrollState;

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.mScrollState = scrollState;
	}

	private boolean isOnBottomLoading = false;

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//		int lastVisibleItem = firstVisibleItem + visibleItemCount;
//		if (totalItemCount >= ValueConstants.PAGESIZE && lastVisibleItem == totalItemCount) {
//			if (!isShow && isHasNext) {
////				mListView.addFooterView(mFooterView);
//				startLoadData((mAdapter.getCount() / ValueConstants.PAGESIZE) + 1);
//			}
//		}

		if (isHasNext) {
			if (firstVisibleItem > 0 && totalItemCount > 0 && (firstVisibleItem + visibleItemCount == totalItemCount)) {
				if (!isOnBottomLoading) {
					isOnBottomLoading = true;
					// TODO: 16/3/3 获取更多数据
					startLoadData((mAdapter.getCount() / ValueConstants.PAGESIZE) + 1);
				}
			}
		}
	}

	@Override
	public View onLoadContentView() {
		mController = new DiscoverController(getActivity(), mHandler);
		View view = View.inflate(getActivity(), R.layout.base_sticky_inner_listview, null);
		Bundle bundle = getArguments();
		if (bundle != null) {
			mOutId = bundle.getLong(SPUtils.EXTRA_ID, -1);
			mOutType = bundle.getString(SPUtils.EXTRA_TYPE, "");
		}
		startLoadData(1);
		return view;
	}
}
