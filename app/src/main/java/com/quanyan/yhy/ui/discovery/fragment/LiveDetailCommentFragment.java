package com.quanyan.yhy.ui.discovery.fragment;

import android.app.Dialog;
import android.nfc.FormatException;
import android.os.Bundle;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.harwkin.nb.camera.ImageUtils;
import com.quanyan.base.BaseFragment;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.DateUtil;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.eventbus.EvBusLiveComPraiNum;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.discovery.LiveDetailActivity;
import com.quanyan.yhy.ui.discovery.controller.DiscoverController;
import com.quanyan.yhy.ui.discovery.viewhelper.CommentUserNameClick;
import com.yhy.common.beans.net.model.comment.CommentInfo;
import com.yhy.common.beans.net.model.comment.CommentInfoList;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;
import com.yhy.service.IUserService;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * 直播详情评论列表
 * <p/>
 * Created by zhaoxp on 2015-11-2.
 */
public class LiveDetailCommentFragment extends BaseFragment implements AdapterView.OnItemClickListener
        , AdapterView.OnItemLongClickListener, AbsListView.OnScrollListener {

    private View mFooterView;
    private ListView mListView;
    private QuickAdapter<CommentInfo> mAdapter;
    protected DiscoverController mController;
    private long mSubjectId = -1;
    private String mOutType;
    private long mCreatorId;

    private boolean isTabClick = false;//标志LiveDetailActivity中评论tab被点击

    @Autowired
    IUserService userService;
    /**
     * @param subjectId
     * @param outType
     * @return
     */
    public static LiveDetailCommentFragment getInstance(long subjectId, String outType) {
        LiveDetailCommentFragment fragment = new LiveDetailCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(SPUtils.EXTRA_ID, subjectId);
        bundle.putString(SPUtils.EXTRA_TYPE, outType);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 初始化视图
     *
     * @param view               根视图
     * @param savedInstanceState
     */
    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mController = new DiscoverController(getActivity(), mHandler);
        mListView = (ListView) view.findViewById(R.id.id_stickynavlayout_innerscrollview);

        mAdapter = new QuickAdapter<CommentInfo>(getActivity(), R.layout.cell_live_detail_comment, new ArrayList<CommentInfo>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, CommentInfo item) {
                handleCommentItem(helper, item);
            }
        };
        mFooterView = getActivity().getLayoutInflater().inflate(R.layout.footer_loading_more, null);
        mListView.addFooterView(mFooterView);
        mListView.setDividerHeight(1);
        mListView.setOnItemClickListener(this);
//		mListView.setPadding(0, 0, 0, ScreenSize.convertDIP2PX(getActivity().getApplicationContext(), 48));
        mListView.setOnItemLongClickListener(this);
        mListView.setOnScrollListener(this);
        mListView.setAdapter(mAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mSubjectId = bundle.getLong(SPUtils.EXTRA_ID, -1);
            mOutType = bundle.getString(SPUtils.EXTRA_TYPE, "");
        }
        startLoadData(1);
    }

    private void handleCommentItem(final BaseAdapterHelper helper, final CommentInfo item) {
        if (item.createUserInfo != null) {
            helper.setImageUrlRound(R.id.cell_live_detail_comment_user_head,
                    ImageUtils.getImageFullUrl(item.createUserInfo.avatar), 128, 128,
                    R.mipmap.icon_default_avatar);
            helper.setText(R.id.cell_live_detail_comment_username, item.createUserInfo.nickname);
        }
        if (item.gmtCreated > 0) {
            try {
                helper.setText(R.id.cell_live_detail_comment_time, DateUtil.getCreateAt(item.gmtCreated));
            } catch (FormatException e) {
                helper.setText(R.id.cell_live_detail_comment_time, "很久以前");
            }
        } else {
            helper.setText(R.id.cell_live_detail_comment_time, "");
        }

        helper.setOnClickListener(R.id.cell_live_detail_comment_user_head, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                NavUtils.gotoPersonalPage(getActivity(), item.createUserInfo.userId,item.createUserInfo.nickname,item.createUserInfo.options);
                NavUtils.gotoMasterHomepage(getActivity(), item.createUserInfo.userId);

            }
        });
        TextView textView = helper.getView(R.id.cell_live_detail_comment_apply);
        StringBuilder stringBuilder = new StringBuilder();
        if (item.replyToUserInfo != null) {
            StringBuilder createUser = new StringBuilder();
//			createUser.append((item.createUserInfo == null ? "" :(item.createUserInfo.nickname == null ? "" : item.createUserInfo.nickname)));
            createUser.append("回复");

            StringBuilder replyToUser = new StringBuilder();
            replyToUser.append(item.replyToUserInfo.nickname == null ? "" : item.replyToUserInfo.nickname);

            stringBuilder.append(createUser.toString());
            stringBuilder.append(replyToUser.toString())
                    .append(" : ")
                    .append(item.textContent == null ? "" : item.textContent);
            SpannableStringBuilder builder = new SpannableStringBuilder(stringBuilder.toString());
            if (item.createUserInfo != null) {
                //设置评论人的名字点击事件
                builder.setSpan(new CommentUserNameClick(getActivity(), item.replyToUserInfo, "comment"), createUser.toString().length(),
                        createUser.toString().length() + replyToUser.toString().length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.ac_title_bg_color)),
                        createUser.toString().length(),
                        createUser.toString().length() + replyToUser.toString().length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            textView.setText(builder);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            textView.setText(item.textContent);
        }
        textView.setFocusable(true);
        textView.setClickable(true);
        textView.setLongClickable(true);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCommentItemClick != null) {
                    int po = helper.getPosition();
                    mCommentItemClick.commentItemClick(mAdapter.getItem(helper.getPosition()));
                }
            }
        });
    }

    public void deleteFromDetail() {
        mAdapter.remove(mClickDetePos);
        if (mAdapter.getCount() <= 0) {
            showNodataPage();
        }
    }

    private void startLoadData(int pageIndex) {
        showLoadingView(getString(R.string.loading_text));
        if (1 == pageIndex) {
            isRefresh = true;
        } else {
            isRefresh = false;
        }
        if (-1 != mSubjectId) {
            mController.doGetLiveDetailComment(getActivity(), mSubjectId, mOutType, pageIndex, ValueConstants.PAGESIZE);
        } else {
            ToastUtil.showToast(getActivity(), getString(R.string.error_params));
        }
    }

    /**
     * 对某人的评论，接口
     */
    private CommentItemClick mCommentItemClick;

    /**
     * 针对某个评论的回复
     *
     * @param commentItemClickListener
     */
    public void setCommentItemClickListener(CommentItemClick commentItemClickListener) {
        this.mCommentItemClick = commentItemClickListener;
    }

    private int mClickDetePos = -1;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int headerCount = mListView.getHeaderViewsCount();
        if (position >= headerCount) {
            mClickDetePos = position - headerCount;
            if (mCommentItemClick != null) {
                mCommentItemClick.commentItemClick(mAdapter.getItem(position - headerCount));
            }
        }
    }

    private Dialog mDialog;
    private int mDeletePosition;

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final int headerCount = mListView.getHeaderViewsCount();
        if (position >= headerCount) {
            final CommentInfo commentInfo = mAdapter.getItem(position - headerCount);
            long userID = 0;
            if (commentInfo.createUserInfo != null) {
                userID = commentInfo.createUserInfo.userId;
            }
            if(userService.isLogin()) {
                if (userService.getLoginUserId() == mCreatorId) {
                    showDeleteDialog(position, headerCount, commentInfo, true);
                } else if (userID == userService.getLoginUserId()) {
                    showDeleteDialog(position, headerCount, commentInfo, false);
                }
            }
        }
        return true;
    }

    private void showDeleteDialog(final int position, final int headerCount, final CommentInfo commentInfo, final boolean isSelf) {
        mDialog = DialogUtil.showMessageDialog(getActivity(), getString(R.string.toast_delete_comment), getString(R.string.delete_notice),
                getString(R.string.label_btn_ok), getString(R.string.cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //删除
                        mDeletePosition = position - headerCount;
                        if (isSelf) {
                            //TODO 删除我直播下的评论
                            mController.doDelComment(getActivity(), commentInfo.id, mSubjectId);
                        } else {
                            //TODO 删除发布者自己的评论
                            mController.doDelComment(getActivity(), commentInfo.id, mOutType);
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
//				isShow = true;
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

    private boolean isRefresh = true;//刷新的状态（true：刷新，false，加载更多）
    private boolean isShow = false;//控制刷新显示次数
    private boolean isHasNext = false;//是否还有下一页

    /**
     * 更新数据
     */
    public void updateData(boolean isTabClick, long userId) {
        if (mController == null) {
            return;
        }
        mCreatorId = userId;
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

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        //重置到底部加载的状态
        if (!isDetached()) {
            hideErrorView(null);
            hideLoadingView();
            isShow = false;
            switch (msg.what) {
                case DiscoverController.MSG_LIVE_DETAIL_COMMENT_OK: {
                    CommentInfoList commentInfoList = (CommentInfoList) msg.obj;

                    if (commentInfoList != null) {
                        isHasNext = commentInfoList.hasNext;
                        if (isRefresh) {
                            if (commentInfoList.commentInfoList != null) {
                                mAdapter.replaceAll(commentInfoList.commentInfoList);
                            } else {
                                mAdapter.clear();
                            }
                            if (mAdapter.getCount() == 0) {
                                showNodataPage();
                            }
                        } else {
                            if (commentInfoList.commentInfoList != null) {
                                mAdapter.addAll(commentInfoList.commentInfoList);
                            }
                        }
                    }

                    if (commentInfoList == null) {
                        if (mAdapter.getCount() > 0) {
                            EventBus.getDefault().post(new EvBusLiveComPraiNum(mAdapter.getCount(), 1));
                        } else {
                            EventBus.getDefault().post(new EvBusLiveComPraiNum(0, 1));
                        }
                    } else {
                        if (isTabClick) {
                            EventBus.getDefault().post(new EvBusLiveComPraiNum(commentInfoList.commentNum, 1));
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
                                updateData(false, mCreatorId);
                            }
                        });
                    }
                    break;
                }
                //删除
                case ValueConstants.MSG_DELETE_COMMENT:
                    boolean isSuccess = (boolean) msg.obj;
                    if (isSuccess) {
                        mAdapter.remove(mDeletePosition);
                        if (mAdapter.getCount() <= 0) {
                            showNodataPage();
                        }
                    } else {
                        ToastUtil.showToast(getActivity(), "删除失败");
                    }
                    ((LiveDetailActivity) getActivity()).getDetailData();
                    break;
                case ValueConstants.MSG_DELETE_COMMENT_ERROR:
                    break;

            }
            isOnBottomLoading = false;
            if (!isHasNext) {
                mListView.removeFooterView(mFooterView);
            }
        }
    }

    private void showNodataPage() {
        showErrorView(null, IActionTitleBar.ErrorType.EMPTYVIEW, getString(R.string.error_comment_nodata), "  ", "", null);
    }

    @Override
    public View onLoadContentView() {
        View view = View.inflate(getActivity(), R.layout.base_sticky_inner_listview, null);
        return view;
    }

    /**
     * 设置发步这条动态的userid
     * @param creatorId
     */
    public void setCreatorId(long creatorId) {
        mCreatorId = creatorId;
    }

    public interface CommentItemClick {
        void commentItemClick(CommentInfo item);
    }
}
