package com.newyhy.adapter.live;

import android.app.Activity;
import android.nfc.FormatException;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.quanyan.base.util.DateUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.discovery.viewhelper.CommentUserNameClick;
import com.yhy.common.beans.net.model.comment.CommentInfo;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

import java.util.List;

public class CommentAdapter extends BaseQuickAdapter<CommentInfo, BaseViewHolder> {

    private Activity mActivity;
    private CommentItemClick mCommentItemClick;

    public void setmCommentItemClick(CommentItemClick mCommentItemClick) {
        this.mCommentItemClick = mCommentItemClick;
    }

    public CommentAdapter(Activity activity, List<CommentInfo> data) {
        super(R.layout.rc_comment_item, data);
        mActivity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentInfo item) {

        if (item.createUserInfo != null) {
//            helper.setImageUrlRound(R.id.cell_live_detail_comment_user_head,
//                    ImageUtils.getImageFullUrl(item.createUserInfo.avatar), 128, 128,
//                    R.mipmap.icon_default_avatar);
            ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(item.createUserInfo.avatar), R.mipmap.icon_default_avatar, helper.getView(R.id.iv_head));
            helper.setText(R.id.tv_name, item.createUserInfo.nickname);
        }
        if (item.gmtCreated > 0) {
            try {
                helper.setText(R.id.tv_comment_time, DateUtil.getCreateAt(item.gmtCreated));
            } catch (FormatException e) {
                helper.setText(R.id.tv_comment_time, "很久以前");
            }
        } else {
            helper.setText(R.id.tv_comment_time, "");
        }

        helper.setOnClickListener(R.id.iv_head, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                NavUtils.gotoPersonalPage(getActivity(), item.createUserInfo.userId,item.createUserInfo.nickname,item.createUserInfo.options);
                NavUtils.gotoMasterHomepage(mActivity, item.createUserInfo.userId);

            }
        });
        TextView textView = helper.getView(R.id.tv_comment_content);
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
                builder.setSpan(new CommentUserNameClick(mActivity, item.replyToUserInfo, "comment"), createUser.toString().length(),
                        createUser.toString().length() + replyToUser.toString().length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.ac_title_bg_color)),
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
                    mCommentItemClick.commentItemClick(item);
                }
            }
        });

    }

    public interface CommentItemClick {
        void commentItemClick(CommentInfo item);
    }
}
