package com.newyhy.fragment.live;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.newyhy.activity.HorizontalReplayActivity;
import com.newyhy.activity.NewCircleDetailActivity;
import com.newyhy.activity.VerticalReplayActivity;
import com.newyhy.adapter.circle.CircleDetailCommentAdapter;
import com.newyhy.adapter.live.CommentAdapter;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.discovery.controller.DiscoverController;
import com.quanyan.yhy.view.NetWorkErrorView;
import com.yhy.common.base.BaseNewFragment;
import com.yhy.common.beans.net.model.comment.CommentInfo;
import com.yhy.common.beans.net.model.comment.CommentInfoList;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.eventbus.event.EvBusVideoCommentChange;
import com.yhy.common.utils.SPUtils;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class CommentFragment extends BaseNewFragment {

//    private RecyclerView rc_comment;

    private NestedScrollView error_view_contain;
    private NetWorkErrorView error_view;
    private RecyclerView mListView;
    private CommentAdapter mAdapter;

    private Dialog mDialog;
    private int mDeletePosition;

    protected DiscoverController mController;
    private long mOutId;
    private int pageIndex;
    private String commenOutType;

    //    private RecyclerView rc_zan;
    public static CommentFragment getInstance(long subjectId,String commenOutType) {
        CommentFragment fragment = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(SPUtils.EXTRA_ID, subjectId);
        bundle.putString(SPUtils.KEY_ROLE_TYPE,commenOutType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.comment_gragment;
    }

    @Override
    protected void initVars() {
        super.initVars();
        mController = new DiscoverController(getActivity(), mHandler);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mOutId = bundle.getLong(SPUtils.EXTRA_ID, -1);
            commenOutType = bundle.getString(SPUtils.KEY_ROLE_TYPE,"");
        }
    }

    @Override
    protected void initView() {
        super.initView();
        error_view_contain = mRootView.findViewById(R.id.error_view_contain);//errorView
        error_view = mRootView.findViewById(R.id.error_view);//errorView

        mListView = mRootView.findViewById(R.id.rc_comment);
        mAdapter = new CommentAdapter(mActivity, new ArrayList<>());
        mListView.setLayoutManager(new LinearLayoutManager(mActivity));
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pageIndex = 1;
        getData(pageIndex);
    }

    @Override
    protected void setListener() {
        super.setListener();

//        mAdapter.setmCommentItemClick(mCommentItemClick);
//        mAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
//            CommentInfo data = mAdapter.getItem(i);
//            mCommentItemClick.commentItemClick(data);
//        });

//        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
//                final CommentInfo commentInfo = mAdapter.getItem(i);
//                long userID = 0;
//                if (commentInfo.createUserInfo != null) {
//                    userID = commentInfo.createUserInfo.userId;
//                }
//                if (SPUtils.isLogin(getActivity().getApplicationContext())) {
//                    mDeletePosition = i;
//                    if (SPUtils.getUid(getActivity().getApplicationContext()) == mOutId) {
//                        showDeleteDialog(commentInfo, true);
//                    } else if (userID == SPUtils.getUid(getActivity().getApplicationContext())) {
//                        showDeleteDialog(commentInfo, false);
//                    }
//                }
//                return true;
//            }
//        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageIndex++;
                getData(pageIndex);
            }
        }, mListView);
    }


    private void getData(int pageIndex) {
        if (mController == null) return;
        if (-1 != mOutId) {
            mController.doGetLiveDetailComment(getActivity(), mOutId, commenOutType, pageIndex, ValueConstants.PAGESIZE);
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
                if (getActivity() instanceof HorizontalReplayActivity){
                    ((HorizontalReplayActivity) getActivity()).setCommentNum(commentInfoList.commentNum);
                }

                if (getActivity() instanceof VerticalReplayActivity){
                    ((VerticalReplayActivity) getActivity()).setCommentNum(commentInfoList.commentNum);
                }

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
        if (mAdapter == null || error_view_contain == null) return;
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
            if (result.hasNext) {
                mAdapter.loadMoreComplete();
            } else {
                mAdapter.loadMoreEnd(true);
            }
        } else {
            if (pageIndex == 1) {
                showNoDataPage();
            } else {
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
                            mController.doDelComment(getActivity(), commentInfo.id, mOutId);
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void smoothScrollToTop() {
        mListView.smoothScrollToPosition(0);
    }
}
