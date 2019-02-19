package com.newyhy.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.newyhy.utils.DisplayUtils;
import com.newyhy.utils.ShareUtils;
import com.newyhy.views.OverlayListLayout;
import com.newyhy.views.SDCircleImageView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.NativeUtils;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.smart.sdk.api.resp.Api_COMPETITION_ArrangeCampaignCrew;
import com.smart.sdk.api.resp.Api_COMPETITION_Campaign;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 约战广场列表适配器
 * Created by yangboxue on 2018/5/28.
 */

public class InviteFightAdapter extends BaseQuickAdapter<Api_COMPETITION_Campaign, BaseViewHolder> {

    private Activity mActivity;

    private static final int AA_INVITE = 1;
    private static final int HALF_INVITE = 2;
    private static final int BALL_INVITE = 3;

    @Autowired
    IUserService userService;

    public InviteFightAdapter(Activity activity, ArrayList<Api_COMPETITION_Campaign> data) {
        super(data);
        YhyRouter.getInstance().inject(this);
        mActivity = activity;

        setMultiTypeDelegate(new MultiTypeDelegate<Api_COMPETITION_Campaign>() {
            @Override
            protected int getItemType(Api_COMPETITION_Campaign data) {
                int viewType = 1;
                switch (data.arrange_campaign_type) {    //1 半场 2 AA半场 3AA约球  4半场约战和半场AA均可匹配的约战),可以组合查询，如 arrange_campaign_type=1,4 则查询半场约战和半场AA两类
                    case 1:       // 半场约战
                    case 4:       // 半场约战
                        if (data.arrangeCampaignCrew != null && data.arrangeCampaignCrew.size() > 0) {
                            viewType = AA_INVITE;
                        } else {
                            viewType = HALF_INVITE;
                        }
                        break;
                    case 2:       // aa约战
                        viewType = AA_INVITE;
                        break;
                    case 3:       // aa约球
                        viewType = BALL_INVITE;
                        break;
                }
                return viewType;
            }
        });

        getMultiTypeDelegate()
                //aa约战
                .registerItemType(AA_INVITE, R.layout.aa_invite_fight_item)
                //半场约战
                .registerItemType(HALF_INVITE, R.layout.half_invite_fight_item)
                //aa约球
                .registerItemType(BALL_INVITE, R.layout.ball_invite_fight_item);
    }

    @Override
    protected void convert(BaseViewHolder holder, Api_COMPETITION_Campaign fightBean) {
        initCommonView(holder, fightBean, holder.getItemViewType());
        switch (holder.getItemViewType()) {
            case AA_INVITE:
                setAaFightView(holder, fightBean);
                break;
            case HALF_INVITE:
                setHalfFightView(holder, fightBean);
                break;
            case BALL_INVITE:
                setAaBallView(holder, fightBean);
                break;
        }
    }

    /**
     * 通用顶部和底部信息
     *
     * @param fightBean
     */
    private void initCommonView(BaseViewHolder holder, final Api_COMPETITION_Campaign fightBean, int viewType) {
        holder.setGone(R.id.view_line, holder.getAdapterPosition() == 0 ? false : true);

        holder.setText(R.id.tv_place, fightBean.tb_place_name);
        holder.setText(R.id.tv_time, transformDate(fightBean.gmt_project_start));

        holder.setText(R.id.tv_location, fightBean.address);

        holder.getView(R.id.tv_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userService.isLogin()) {
                    String title = "";
                    String content = "";
                    String url = SPUtils.getURL_WAR_ID(mActivity);
                    if (!TextUtils.isEmpty(url))
                        url = url.replace(":arrange_campaign_id", String.valueOf(fightBean.arrange_campaign_id));
                    String icon = "";
                    switch (viewType) {
                        case AA_INVITE:
//                        title：[半场AA约战] 足球 7人制 XX月XX日（周X）XX:XX-XX:XX，等你来参与
//                        subtutle：场馆详细地址
//                                  XXXXXXX场馆名称
//
//                        pic：球队logo
//                        https://shadow.yingheying.com/default/aa_share_icon.png
//                        https://shadow.yingheying.com/default/half_share_icon.png

                            if (1 == fightBean.status) {
                                title = "[半场约战]" + transformProjectType(fightBean.project_type) + " " + transform(fightBean.several_people_system) + "人制 " + " " + transformDate(fightBean.gmt_project_start) + "-" + transformEndDate(fightBean.gmt_project_end);
                                content = "活动地址：" + fightBean.address + "\n" + fightBean.tb_place_name;
                                icon = "https://shadow.yingheying.com/default/half_share_icon.png";
                                url = url + "?userId=" + userService.getLoginUserId();
                            } else {

                            }
                            break;
                        case HALF_INVITE:
                            if (1 == fightBean.status) {
                                title = "[半场约战]" + transformProjectType(fightBean.project_type) + " " + transform(fightBean.several_people_system) + "人制 " + " " + transformDate(fightBean.gmt_project_start) + "-" + transformEndDate(fightBean.gmt_project_end);
                                content = "活动地址：" + fightBean.address + "\n" + fightBean.tb_place_name;
                                icon = "https://shadow.yingheying.com/default/half_share_icon.png";
                                url = url + "?userId=" + userService.getLoginUserId();
                            } else {

                            }
                            break;
                        case BALL_INVITE:
                            if (1 == fightBean.status) {
                                title = "[AA约球]" + transformProjectType(fightBean.project_type) + " " + transform(fightBean.several_people_system) + "人制 " + " " + transformDate(fightBean.gmt_project_start) + "-" + transformEndDate(fightBean.gmt_project_end);
                                content = "活动地址：" + fightBean.address + "\n" + fightBean.tb_place_name;
                                icon = "https://shadow.yingheying.com/default/aa_share_icon.png";
                                url = url + "?userId=" + userService.getLoginUserId();
                            } else {

                            }
                            break;
                    }
                    ShareUtils.showShareBoard(mActivity, title, content, url, icon);
                } else {
                    NavUtils.gotoLoginActivity(mActivity);
                }

            }
        });
    }

    /**
     * AA约战
     *
     * @param fightBean
     */
    private void setAaFightView(BaseViewHolder holder, Api_COMPETITION_Campaign fightBean) {
        if (1 == fightBean.status) {    // 约战中         1 约战中 2 约战成功
            holder.setGone(R.id.llyt_bottom, true);
            holder.setGone(R.id.tv_aa_invite_success, false);
            holder.setGone(R.id.llyt_aa_inviting, true);
            holder.setGone(R.id.llyt_aa_right_inviting, true);
            holder.setGone(R.id.llyt_aa_right_invite_success, false);

            holder.setImageResource(R.id.iv_aa_vs, R.mipmap.ic_vs);

            if (fightBean.join_number > fightBean.peoples_floor) {
                holder.setText(R.id.tv_aa_join_num, String.valueOf(fightBean.join_number));
                holder.setText(R.id.tv_aa_total_num, String.valueOf(fightBean.join_number));
            } else {
                holder.setText(R.id.tv_aa_join_num, String.valueOf(fightBean.join_number));
                holder.setText(R.id.tv_aa_total_num, String.valueOf(fightBean.peoples_floor));
            }

            holder.addOnClickListener(R.id.llyt_aa_right_inviting);

        } else {                    // 约战成功
            holder.setGone(R.id.llyt_bottom, false);
            holder.setGone(R.id.tv_aa_invite_success, true);
            holder.setGone(R.id.llyt_aa_inviting, false);
            holder.setGone(R.id.llyt_aa_right_inviting, false);
            holder.setGone(R.id.llyt_aa_right_invite_success, true);

            holder.setImageResource(R.id.iv_aa_vs, R.mipmap.ic_vs_gray);

            final List<Api_COMPETITION_ArrangeCampaignCrew> imageDatas = fightBean.arrangeCampaignCrew;
            if (imageDatas != null && imageDatas.size() > 0) {
                String names = imageDatas.get(0).userName;
                for (int i = 1; i < imageDatas.size(); i++) {
                    names = names + "/" + imageDatas.get(i).userName;
                }
                holder.setText(R.id.tv_aa_accept_names, names);
            }

            OverlayListLayout overlayListLayout = holder.getView(R.id.overlay_aa_accepts);
            overlayListLayout.setAvatarListListener(new OverlayListLayout.ShowAvatarListener() {
                @Override
                public void showImageView(List<SDCircleImageView> imageViewList) {
                    //创建的ImageView的数量
                    int imageSize = imageViewList.size();
                    //实际需要显示的图片的数量
                    int realDataSize = imageDatas.size();
                    int mul = imageSize - realDataSize;
                    for (int i = 0; i < imageSize; i++) {
                        if (i >= mul) {
                            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(imageDatas.get(realDataSize - (i - mul) - 1).avatar), R.mipmap.icon_default_avatar, imageViewList.get(i));
                            imageViewList.get(i).setVisibility(View.VISIBLE);
                        } else {
                            imageViewList.get(i).setVisibility(View.GONE);
                        }
                    }
                }
            });

        }

        holder.setText(R.id.tv_aa_left, fightBean.tb_club_a_clubName);
        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(fightBean.tb_club_a_logoUrl), holder.getView(R.id.iv_aa_left), 2, true);

        holder.setText(R.id.tv_aa_play_num, transform(fightBean.several_people_system) + "人制");
        holder.setText(R.id.tv_aa_play_type, transformProjectType(fightBean.project_type));
        ((ImageView) holder.getView(R.id.iv_aa_play_type)).setImageResource(getProjectTypeIcon(fightBean.project_type));

    }

    /**
     * 半场约战
     *
     * @param fightBean
     */
    private void setHalfFightView(BaseViewHolder holder, Api_COMPETITION_Campaign fightBean) {
        if (1 == fightBean.status) {    // 约战中
            holder.setGone(R.id.llyt_bottom, true);
            holder.setGone(R.id.tv_half_invite_success, false);
            holder.setGone(R.id.llyt_half_right_inviting, true);
            holder.setGone(R.id.llyt_half_right_invite_success, false);

            holder.setImageResource(R.id.iv_half_vs, R.mipmap.ic_vs);

            holder.addOnClickListener(R.id.llyt_half_right_inviting);

        } else {                    // 约战成功
            holder.setGone(R.id.llyt_bottom, false);
            holder.setGone(R.id.tv_half_invite_success, true);
            holder.setGone(R.id.llyt_half_right_inviting, false);
            holder.setGone(R.id.llyt_half_right_invite_success, true);

            holder.setImageResource(R.id.iv_half_vs, R.mipmap.ic_vs_gray);

            holder.setText(R.id.tv_half_right, fightBean.tb_club_b_clubName);
            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(fightBean.tb_club_b_logoUrl), holder.getView(R.id.iv_half_right), 2, true);

        }

        holder.setText(R.id.tv_half_left, fightBean.tb_club_a_clubName);
        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(fightBean.tb_club_a_logoUrl), holder.getView(R.id.iv_half_left), 2, true);

        holder.setText(R.id.tv_half_play_num, transform(fightBean.several_people_system) + "人制");
        holder.setText(R.id.tv_half_play_type, transformProjectType(fightBean.project_type));
        ((ImageView) holder.getView(R.id.iv_half_play_type)).setImageResource(getProjectTypeIcon(fightBean.project_type));

    }

    /**
     * AA约球
     *
     * @param fightBean
     */
    private void setAaBallView(BaseViewHolder holder, Api_COMPETITION_Campaign fightBean) {
        if (1 == fightBean.status) {    // 约球中
            holder.setGone(R.id.llyt_bottom, true);
            holder.setGone(R.id.llyt_ball_inviting, true);
            holder.setGone(R.id.iv_ball_invite_add, true);
            holder.setGone(R.id.tv_ball_invite_success, false);
            holder.setGone(R.id.llyt_ball_right_inviting, true);
            holder.setGone(R.id.overlay_ball_invite_success, false);
            holder.setGone(R.id.overlay_ball_inviting, true);

            final List<Api_COMPETITION_ArrangeCampaignCrew> imageDatas = fightBean.arrangeCampaignCrew;
            OverlayListLayout overlayListLayout = holder.getView(R.id.overlay_ball_inviting);
            overlayListLayout.setAvatarListListener(new OverlayListLayout.ShowAvatarListener() {
                @Override
                public void showImageView(List<SDCircleImageView> imageViewList) {
                    //创建的ImageView的数量
                    int imageSize = imageViewList.size();
                    //实际需要显示的图片的数量
                    int realDataSize = imageDatas.size();
                    int mul = imageSize - realDataSize;
                    for (int i = 0; i < imageSize; i++) {
                        if (i >= mul) {
                            imageViewList.get(i).setBorderWidth(DisplayUtils.dp2px(mActivity, 2));
                            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(imageDatas.get(realDataSize - (i - mul) - 1).avatar), R.mipmap.icon_default_avatar, imageViewList.get(i));
                            imageViewList.get(i).setVisibility(View.VISIBLE);

                            ViewGroup.LayoutParams layoutParams = imageViewList.get(i).getLayoutParams();
                            layoutParams.width = Math.round(DisplayUtils.dp2px(mActivity, 54));
                            layoutParams.height = Math.round(DisplayUtils.dp2px(mActivity, 54));
                            imageViewList.get(i).setLayoutParams(layoutParams);

                        } else {
//                            imageViewList.get(i).setVisibility(View.GONE);
                            imageViewList.get(i).setVisibility(View.VISIBLE);
                            imageViewList.get(i).setBorderWidth(0);
                            imageViewList.get(i).setImageResource(R.mipmap.icon_who);

                            ViewGroup.LayoutParams layoutParams = imageViewList.get(i).getLayoutParams();
                            layoutParams.width = Math.round(DisplayUtils.dp2px(mActivity, 51));
                            layoutParams.height = Math.round(DisplayUtils.dp2px(mActivity, 51));
                            imageViewList.get(i).setLayoutParams(layoutParams);
                        }
                    }
                }
            });

            holder.setText(R.id.tv_ball_play_num, transform(fightBean.several_people_system) + "人制");
            holder.setText(R.id.tv_ball_play_type, transformProjectType(fightBean.project_type));
            ((ImageView) holder.getView(R.id.iv_ball_play_type)).setImageResource(getProjectTypeIcon(fightBean.project_type));


            if (fightBean.join_number > fightBean.peoples_floor) {
                holder.setText(R.id.tv_ball_join_num, String.valueOf(fightBean.join_number));
                holder.setText(R.id.tv_ball_total_num, String.valueOf(fightBean.join_number));
            } else {
                holder.setText(R.id.tv_ball_join_num, String.valueOf(fightBean.join_number));
                holder.setText(R.id.tv_ball_total_num, String.valueOf(fightBean.peoples_floor));
            }

            holder.addOnClickListener(R.id.llyt_ball_right_inviting);

        } else {                    // 约球成功
            holder.setGone(R.id.llyt_bottom, false);
            holder.setGone(R.id.llyt_ball_inviting, false);
            holder.setGone(R.id.iv_ball_invite_add, false);
            holder.setGone(R.id.tv_ball_invite_success, true);
            holder.setGone(R.id.llyt_ball_right_inviting, false);
            holder.setGone(R.id.overlay_ball_invite_success, true);
            holder.setGone(R.id.overlay_ball_inviting, false);

            final List<Api_COMPETITION_ArrangeCampaignCrew> imageDatas = fightBean.arrangeCampaignCrew;
            OverlayListLayout overlayListLayout = holder.getView(R.id.overlay_ball_invite_success);
            overlayListLayout.setAvatarListListener(new OverlayListLayout.ShowAvatarListener() {
                @Override
                public void showImageView(List<SDCircleImageView> imageViewList) {
                    //创建的ImageView的数量
                    int imageSize = imageViewList.size();
                    //实际需要显示的图片的数量
                    int realDataSize = imageDatas.size();
                    int mul = imageSize - realDataSize;
                    for (int i = 0; i < imageSize; i++) {
                        if (i >= mul) {
                            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(imageDatas.get(realDataSize - (i - mul) - 1).avatar), R.mipmap.icon_default_avatar, imageViewList.get(i));
                            imageViewList.get(i).setVisibility(View.VISIBLE);
                        } else {
                            imageViewList.get(i).setVisibility(View.GONE);
                        }
                    }
                }
            });

            holder.setText(R.id.tv_ball_play_num, transform(fightBean.several_people_system) + "人制");
            holder.setText(R.id.tv_ball_play_type, transformProjectType(fightBean.project_type));
            ((ImageView) holder.getView(R.id.iv_ball_play_type)).setImageResource(getProjectTypeIcon(fightBean.project_type));

        }
    }

    private int transform(int several_people_system) {
        int peopleCount = 0;
        switch (several_people_system) {
            case 1801:
                peopleCount = 3;
                break;
            case 1802:
                peopleCount = 5;
                break;
            case 1803:
                peopleCount = 7;
                break;
            case 1804:
                peopleCount = 9;
                break;
            case 1805:
                peopleCount = 11;
                break;
            case 1806:
                peopleCount = 6;
                break;
            case 1807:
                peopleCount = 8;
                break;
            case 3101:
                peopleCount = 5;
                break;
            case 3102:
                peopleCount = 5;
                break;
        }
        return peopleCount;
    }

    // 项目类型(1 篮球 2羽毛球 3足球 4网球 5乒乓球 8桌球 9壁球 10气排球 11保龄球)
    private String transformProjectType(int project_type) {
        String type = "";
        switch (project_type) {
            case 1:
                type = "篮球";
                break;
            case 2:
                type = "羽毛球";
                break;
            case 3:
                type = "足球";
                break;
            case 4:
                type = "网球";
                break;
            case 5:
                type = "乒乓球";
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                type = "桌球";
                break;
            case 9:
                type = "壁球";
                break;
            case 10:
                type = "气排球";
                break;
            case 11:
                type = "保龄球";
                break;
        }
        return type;
    }

    // 项目类型(1 篮球 2羽毛球 3足球 4网球 5乒乓球 8桌球 9壁球 10气排球 11保龄球)
    private int getProjectTypeIcon(int project_type) {
        int icon = R.mipmap.icon_fb;
        switch (project_type) {
            case 1:
                icon = R.mipmap.icon_bb;
                break;
            case 2:
                break;
            case 3:
                icon = R.mipmap.icon_fb;
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
            case 11:
                break;
        }
        return icon;
    }

    /**
     * 格式化日期
     *
     * @param strDate
     * @return
     */
    private String transformDate(String strDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ParsePosition pos = new ParsePosition(0);
            Date date = formatter.parse(strDate, pos);

            SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm");
            String f1 = formatter1.format(date);

            Calendar c = Calendar.getInstance();
            c.setTime(date);
            String month = c.get(Calendar.MONTH) + 1 > 9 ? c.get(Calendar.MONTH) + 1 + "" : "0" + (c.get(Calendar.MONTH) + 1);
            String day = c.get(Calendar.DAY_OF_MONTH) > 9 ? c.get(Calendar.DAY_OF_MONTH) + "" : "0" + c.get(Calendar.DAY_OF_MONTH);

            String result = month + "月" + day + "日" + " " + getWeekOfDate(date) + " " + f1;
            return result;
        } catch (Exception e) {
            return strDate;
        }
    }

    private String transformEndDate(String strDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ParsePosition pos = new ParsePosition(0);
            Date date = formatter.parse(strDate, pos);

            SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm");
            String f1 = formatter1.format(date);

//            Calendar c = Calendar.getInstance();
//            c.setTime(date);
//            String month = c.get(Calendar.MONTH) + 1 > 9 ? c.get(Calendar.MONTH) + 1 + "" : "0" + (c.get(Calendar.MONTH) + 1);
//            String day = c.get(Calendar.DAY_OF_MONTH) > 9 ? c.get(Calendar.DAY_OF_MONTH) + "" : "0" + c.get(Calendar.DAY_OF_MONTH);
//
//            String result = month + "月" + day + "日" + " " + getWeekOfDate(date) + " " + f1;
            return f1;
        } catch (Exception e) {
            return strDate;
        }
    }

    public static String getWeekOfDate(Date date) {
        String[] dayofweek = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return dayofweek[c.get(Calendar.DAY_OF_WEEK) - 1];
    }

}
