package com.newyhy.adapter.circle;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.quanyan.yhy.R;
import com.yhy.common.beans.net.model.comment.SupportUserInfo;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

import java.util.List;

/**
 * Created by yangboxue on 2018/6/27.
 */

public class CircleDetailPraiseAdapter extends BaseQuickAdapter<SupportUserInfo, BaseViewHolder> {

    private Activity mActivity;

    public CircleDetailPraiseAdapter(Activity activity, List<SupportUserInfo> data) {
        super(R.layout.cell_club_detail_member, data);
        mActivity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, SupportUserInfo item) {
//        helper.setImageUrlRound(R.id.cell_club_detail_member_user_head, item.avatar, 128, 128, R.mipmap.icon_default_avatar);
        ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(item.avatar), R.mipmap.icon_default_avatar, helper.getView(R.id.cell_club_detail_member_user_head));
        helper.setText(R.id.cell_club_detail_member_user_name, TextUtils.isEmpty(item.nick) ? "" : item.nick);
//				.setText(R.id.cell_live_detail_appraise_user_sex, item.age );
        helper.setGone(R.id.cell_club_detail_member_user_info, false);
        if (ValueConstants.TYPE_SEX_MALE.equals(item.gender)) {
            helper.setBackgroundRes(R.id.cell_club_detail_member_user_sex, R.mipmap.male);
        } else if (ValueConstants.TYPE_SEX_FEMAIL.equals(item.gender)) {
            helper.setBackgroundRes(R.id.cell_club_detail_member_user_sex, R.mipmap.female);
        }
    }
}
