package com.videolibrary.itemhandle;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.quanyan.base.util.ScreenSize;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.util.HealthCircleTextUtil;
import com.videolibrary.metadata.LiveTypeConstants;
import com.videolibrary.utils.IntentUtil;
import com.yhy.common.beans.net.model.msg.LiveRecordResult;
import com.yhy.common.utils.SPUtils;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import java.text.DecimalFormat;

/**
 * Created with Android Studio.
 * Title:LiveListItemHelper
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/8/19
 * Time:18:53
 * Version 1.1.0
 */
public class LiveListItemHelper {

    private static LiveListItemHelper instance = new LiveListItemHelper();
    @Autowired
    IUserService userService;

    private LiveListItemHelper() {
        YhyRouter.getInstance().inject(this);
    }

    public static LiveListItemHelper getInstance(){
        return instance;
    }
    /**
     * 直播列表的item处理
     *
     * @param helper
     * @param liveRecordResult
     */
    public static void handlLiveListItem(Context context, final BaseAdapterHelper helper, final LiveRecordResult liveRecordResult, final AddressClick addressClick) {
        ImageView imageView = helper.getView(R.id.item_live_image);
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (ScreenSize.getScreenWidth(context.getApplicationContext()) * 9) / 16));
        helper.setImageUrl(R.id.item_live_image, liveRecordResult.liveCover, 360, 280, R.mipmap.icon_default_215_150);
        if (liveRecordResult.userInfo != null) {
            helper.setText(R.id.item_live_user_name, TextUtils.isEmpty(liveRecordResult.userInfo.nickname) ? "" : liveRecordResult.userInfo.nickname);
            helper.setImageUrlRound(R.id.item_live_user_head, liveRecordResult.userInfo.avatar, 180, 180, R.mipmap.icon_default_avatar);
        } else {
            helper.setText(R.id.item_live_user_name, "匿名");
            helper.setImageResource(R.id.item_live_user_head, R.mipmap.icon_default_avatar);
        }
        helper.setText(R.id.item_live_address, TextUtils.isEmpty(liveRecordResult.locationCityName) ? "" : liveRecordResult.locationCityName)
                .setText(R.id.item_live_category, TextUtils.isEmpty(liveRecordResult.liveCategoryName) ? "" : "· " + liveRecordResult.liveCategoryName)
                .setText(R.id.item_live_people_num, LiveTypeConstants.LIVE_ING.equals(liveRecordResult.liveStatus) ? liveRecordResult.onlineCount + "" : (liveRecordResult.viewCount + ""))
                .setText(R.id.item_live_content, TextUtils.isEmpty(liveRecordResult.liveTitle) ? "" : liveRecordResult.liveTitle);
        String num;
        if (LiveTypeConstants.LIVE_ING.equals(liveRecordResult.liveStatus)) {
            if (liveRecordResult.onlineCount >= 9990000) {
                helper.setText(R.id.item_live_people_num, "999")
                        .setText(R.id.item_live_people_num_label, "万+在线");
            } else if (liveRecordResult.onlineCount >= 10000) {
                num = (new DecimalFormat("#.##").format(liveRecordResult.onlineCount / 10000.0f));
                helper.setText(R.id.item_live_people_num, num)
                        .setText(R.id.item_live_people_num_label, "万在线");
            } else {
                helper.setText(R.id.item_live_people_num, "" + liveRecordResult.onlineCount)
                        .setText(R.id.item_live_people_num_label, "在线");
            }
        } else {
            if (liveRecordResult.viewCount >= 9990000) {
                helper.setText(R.id.item_live_people_num, "999")
                        .setText(R.id.item_live_people_num_label, "万+看过");
            } else if (liveRecordResult.viewCount >= 10000) {
                num = (new DecimalFormat("#.##").format(liveRecordResult.viewCount / 10000.0f));
                helper.setText(R.id.item_live_people_num, num)
                        .setText(R.id.item_live_people_num_label, "万看过");
            } else {
                helper.setText(R.id.item_live_people_num, "" + liveRecordResult.viewCount)
                        .setText(R.id.item_live_people_num_label, "看过");
            }
        }
        if (LiveTypeConstants.LIVE_ING.equals(liveRecordResult.liveStatus)) {
            helper.setText(R.id.item_live_status_label, "直播");
        } else {
            helper.setText(R.id.item_live_status_label, "回放");
        }
//        helper.setVisible(R.id.item_live_status_layout, LiveTypeConstants.LIVE_ING.equals(liveRecordResult.liveStatus) ? true : false);

        helper.setOnClickListener(R.id.item_live_user_head, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (liveRecordResult.userInfo != null) {
                    NavUtils.gotoMasterHomepage(v.getContext(), liveRecordResult.userInfo.userId);
                }
            }
        });
        helper.setOnClickListener(R.id.item_live_address_layout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long cityCode = -1;
                try {
                    cityCode = Long.parseLong(liveRecordResult.locationCityCode);
                } catch (NumberFormatException e) {
                }
                if (addressClick != null) {
                    addressClick.addressClick(helper.getPosition(), liveRecordResult.locationCityName, cityCode);
                }
            }
        });

        helper.setOnClickListener(R.id.item_live_content, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (liveRecordResult.userInfo != null) {
                    if (LiveTypeConstants.LIVE_ING.equals(liveRecordResult.liveStatus)) {
                        if (instance.userService.getLoginUserId() == liveRecordResult.userInfo.userId) {
                            ToastUtil.showToast(v.getContext(), "您不能进入自己的直播间");
                            return;
                        }
                        IntentUtil.startVideoClientActivity(liveRecordResult.liveId,
                                liveRecordResult.userInfo.userId, true,liveRecordResult.liveScreenType);
                    } else if (LiveTypeConstants.LIVE_REPLAY.equals(liveRecordResult.liveStatus)) {
                        IntentUtil.startVideoClientActivity(
                                liveRecordResult.liveId, 0, false,liveRecordResult.liveScreenType);
                    }
                }
            }
        });

        HealthCircleTextUtil.SetLinkClickIntercept(context, (TextView) helper.getView(R.id.item_live_content));
    }

    public interface AddressClick {
        /**
         * while click the address layout, you will refresh the list
         *
         * @param position
         * @param cityCode
         */
        void addressClick(int position, String cityName, long cityCode);
    }
}
