package com.newyhy.adapter.live;

import android.app.Activity;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.quanyan.yhy.R;
import com.yhy.common.beans.net.model.comment.SupportUserInfo;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

import java.util.List;

public class ZanListAdapter extends BaseQuickAdapter<SupportUserInfo, BaseViewHolder> {

    private Activity mActivity;

    public ZanListAdapter(Activity activity, List<SupportUserInfo> data) {
        super(R.layout.rc_zan_item, data);
        mActivity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, SupportUserInfo item) {
//        helper.setImageUrlRound(R.id.cell_club_detail_member_user_head, item.avatar, 128, 128, R.mipmap.icon_default_avatar);
        ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(item.avatar), R.mipmap.icon_default_avatar, helper.getView(R.id.iv_zan_head));
        helper.setText(R.id.tv_name, TextUtils.isEmpty(item.nick) ? "" : item.nick);
//				.setText(R.id.cell_live_detail_appraise_user_sex, item.age );
//        helper.setGone(R.id.cell_club_detail_member_user_info, false);
//        if (ValueConstants.TYPE_SEX_MALE.equals(item.gender)) {
//            helper.setBackgroundRes(R.id.cell_club_detail_member_user_sex, R.mipmap.male);
//        } else if (ValueConstants.TYPE_SEX_FEMAIL.equals(item.gender)) {
//            helper.setBackgroundRes(R.id.cell_club_detail_member_user_sex, R.mipmap.female);
//        }
    }

}
