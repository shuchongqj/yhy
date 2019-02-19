package com.newyhy.fragment.circle;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.newyhy.activity.NewCircleDetailActivity;
import com.newyhy.adapter.circle.CircleDetailCommentAdapter;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.discovery.controller.DiscoverController;
import com.quanyan.yhy.view.NetWorkErrorView;
import com.yhy.common.base.BaseNewFragment;
import com.yhy.common.beans.net.model.comment.CommentInfo;
import com.yhy.common.beans.net.model.comment.CommentInfoList;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import java.util.ArrayList;

/**
 * Created by yangboxue on 2018/6/27.
 */

public class CircleDetailCommentFragment extends BaseNewFragment {

    private NestedScrollView error_view_contain;
//    private NetWorkErrorView error_view;
    private RecyclerView mListView;
    private CircleDetailCommentAdapter mAdapter;

    private Dialog mDialog;
    private int mDeletePosition;

    protected DiscoverController mController;
    private long mSubjectId = -1;
    private int pageIndex;

    @Autowired
    IUserService userService;

    public static CircleDetailCommentFragment getInstance(long subjectId) {
        CircleDetailCommentFragment fragment = new CircleDetailCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(SPUtils.EXTRA_ID, subjectId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_circle_detail_comment;
    }

    @Override
    protected void initVars() {
        super.initVars();
        YhyRouter.getInstance().inject(this);
        mController = new DiscoverController(getActivity(), mHandler);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mSubjectId = bundle.getLong(SPUtils.EXTRA_ID, -1);
        }
    }

    @Override
    protected void initView() {
        super.initView();
        error_view_contain = mRootView.findViewById(R.id.error_view_contain);//errorView
//        error_view = mRootView.findViewById(R.id.error_view);//errorView

        mListView = mRootView.findViewById(R.id.rv_comment);
        mAdapter = new CircleDetailCommentAdapter(mActivity, new ArrayList<>());
        mListView.setLayoutManager(new LinearLayoutManager(mActivity));
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        super.setListener();

        mAdapter.setmCommentItemClick(mCommentItemClick);
        mAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            CommentInfo data = mAdapter.getItem(i);
            mCommentItemClick.commentItemClick(data);
        });

        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                final CommentInfo commentInfo = mAdapter.getItem(i);
                long userID = 0;
                if (commentInfo.createUserInfo != null) {
                    userID = commentInfo.createUserInfo.userId;
                }
                if(userService.isLogin()) {
                    mDeletePosition = i;
                    if (userService.getLoginUserId() == mSubjectId) {
                        showDeleteDialog(commentInfo, true);
                    } else if (userID == userService.getLoginUserId()) {
                        showDeleteDialog(commentInfo, false);
                    }
                }
                return true;
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageIndex++;
                getData(pageIndex);
            }
        }, mListView);
    }


    @Override
    protected void initData() {
        super.initData();
        pageIndex = 1;
        getData(pageIndex);
    }

    private void getData(int pageIndex) {
        if (-1 != mSubjectId) {
            mController.doGetLiveDetailComment(getActivity(), mSubjectId, ValueConstants.TYPE_COMMENT_LIVESUP, pageIndex, ValueConstants.PAGESIZE);
        } else {
            ToastUtil.showToast(getActivity(), getString(R.string.error_params));
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case DiscoverController.MSG_LIVE_DETAIL_COMMENT_OK: {
                CommentInfoList commentInfoList = (CommentInfoList) msg.obj;
                handleResult(commentInfoList);
//                if (commentInfoList != null) {
//                    isHasNext = commentInfoList.hasNext;
//                    if (isRefresh) {
//                        if (commentInfoList.commentInfoList != null) {
//                            mAdapter.replaceAll(commentInfoList.commentInfoList);
//                        } else {
//                            mAdapter.clear();
//                        }
//                        if (mAdapter.getCount() == 0) {
//                            showNodataPage();
//                        }
//                    } else {
//                        if (commentInfoList.commentInfoList != null) {
//                            mAdapter.addAll(commentInfoList.commentInfoList);
//                        }
//                    }
//                }
//
//                if (commentInfoList == null) {
//                    if (mAdapter.getCount() > 0) {
//                        EventBus.getDefault().post(new EvBusLiveComPraiNum(mAdapter.getCount(), 1));
//                    } else {
//                        EventBus.getDefault().post(new EvBusLiveComPraiNum(0, 1));
//                    }
//                } else {
//                    if (isTabClick) {
//                        EventBus.getDefault().post(new EvBusLiveComPraiNum(commentInfoList.commentNum, 1));
//                    }
//                }
                break;
            }
            case DiscoverController.MSG_LIVE_DETAIL_COMMENT_ERROR: {
//                if (isRefresh) {
//                    showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == msg.arg1 ?
//                            IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
//                        @Override
//                        public void onClick(View view) {
//                            updateData(false, mCreatorId);
//                        }
//                    });
//                }
                break;
            }
            //删除
            case ValueConstants.MSG_DELETE_COMMENT:
                boolean isSuccess = (boolean) msg.obj;
                if (isSuccess) {
                    mAdapter.remove(mDeletePosition);
                    if (mAdapter.getItemCount() <= 0) {
                        showNoDataPage();
                    }
                } else {
                    ToastUtil.showToast(getActivity(), "删除失败");
                }
                ((NewCircleDetailActivity) getActivity()).getDetailData();
                break;
            case ValueConstants.MSG_DELETE_COMMENT_ERROR:
                break;

        }

    }

    private CircleDetailCommentAdapter.CommentItemClick mCommentItemClick;

    public void setCommentItemClickListener(CircleDetailCommentAdapter.CommentItemClick commentItemClickListener) {
        this.mCommentItemClick = commentItemClickListener;
    }


    private void handleResult(CommentInfoList result) {
        if (result.commentInfoList != null && result.commentInfoList.size() > 0) {
            if (pageIndex == 1) {
                error_view_contain.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                mAdapter.setNewData(result.commentInfoList);
            } else {
                mAdapter.addData(result.commentInfoList);
            }
            mAdapter.notifyDataSetChanged();
            mAdapter.loadMoreComplete();
            if (result.hasNext){
                mAdapter.loadMoreComplete();
            }else {
                mAdapter.loadMoreEnd(true);
            }
        } else {
            if (pageIndex == 1){
                showNoDataPage();
            }else {
                ToastUtil.showToast(getActivity(), getString(R.string.scenic_hasnodata));
                mAdapter.loadMoreEnd(true);

            }
        }
    }
    /**
     * 刷新数据
     */
    public void updateData() {
        pageIndex = 1;
        getData(pageIndex);
    }

    private void showDeleteDialog(final CommentInfo commentInfo, final boolean isSelf) {
        mDialog = DialogUtil.showMessageDialog(getActivity(), getString(R.string.toast_delete_comment), getString(R.string.delete_notice),
                getString(R.string.label_btn_ok), getString(R.string.cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //删除
                        if (isSelf) {
                            //TODO 删除我直播下的评论
                            mController.doDelComment(getActivity(), commentInfo.id, mSubjectId);
                        } else {
                            //TODO 删除发布者自己的评论
                            mController.doDelComment(getActivity(), commentInfo.id, ValueConstants.TYPE_COMMENT_LIVESUP);
                        }
                        mDialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //取消
                        mDialog.dismiss();
                    }
                });
        mDialog.show();
    }

    private void showNoDataPage() {
        if (getActivity() == null) {
            return;
        }

        error_view_contain.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);

//        error_view.show(getString(R.string.error_comment_nodata), "  ", "", null);
    }

    public void smoothScrollToTop() {
        mListView.smoothScrollToPosition(0);
    }

}
