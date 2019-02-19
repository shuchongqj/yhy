package com.mogujie.tt.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.harwkin.nb.camera.ImageUtils;
import com.mogujie.tt.DB.sp.ConfigurationSp;
import com.mogujie.tt.imservice.service.IMService;
import com.mogujie.tt.ui.widget.IMGroupAvatar;
import com.mogujie.tt.utils.DateUtil;
import com.mogujie.tt.utils.Logger;
import com.newyhy.views.RoundImageView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.database.DBManager;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.yhy.common.beans.im.entity.RecentInfo;
import com.yhy.common.constants.DBConstant;
import com.yhy.common.constants.MessageConstant;
import com.yhy.common.constants.NotificationConstants;
import com.yhy.common.constants.SysConstant;
import com.yhy.common.utils.AndroidUtils;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 联系人列表适配器
 */
@SuppressLint("ResourceAsColor")
public class ChatAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    private List<RecentInfo> recentSessionList = new ArrayList<>();
    private Logger logger = Logger.getLogger(ChatAdapter.class);

    private static final int CONTACT_TYPE_INVALID = 0;
    private static final int CONTACT_TYPE_USER = 1;
    private static final int CONTACT_TYPE_GROUP = 2;
    private static final int CONTACT_TYPE_NOTIFICATION = 3;
    private static final int CONTACT_TYPE_CONSULT = 4;
    private IMService imService;
    private ConfigurationSp configurationSp;
    private Context mContext;

    @Autowired
    IUserService userService;
    public ChatAdapter(Context context) {
        mContext = context;
        this.mInflater = LayoutInflater.from(context);
        YhyRouter.getInstance().inject(this);
        configurationSp = ConfigurationSp.instance(context, userService.getLoginUserId());
    }

    @Override
    public int getCount() {
        return recentSessionList.size();
    }

    @Override
    public RecentInfo getItem(int position) {
        logger.d("recent#getItem position:%d", position);
//        if (position >= recentSessionList.size() || position < 0) {
//            return null;
//        }
        return recentSessionList.get(position);
    }

    /**
     * 置顶状态的更新  not use now
     */
    public void updateRecentInfoByTop(String sessionKey, boolean isTop) {
        for (RecentInfo recentInfo : recentSessionList) {
            if (recentInfo.getSessionKey().equals(sessionKey)) {
                recentInfo.setTop(isTop);
                notifyDataSetChanged();
                break;
            }
        }
    }


    public int getUnreadPositionOnView(int currentPostion) {
        int nextIndex = currentPostion + 1;
        int sum = getCount();
        if (nextIndex > sum) {
            currentPostion = 0;
        }
        /**从当前点到末尾*/
        for (int index = nextIndex; index < sum; index++) {
            int unCnt = recentSessionList.get(index).getUnReadCnt();
            if (unCnt > 0) {
                return index;
            }
        }
        /**从末尾到当前点*/
        for (int index = 0; index < currentPostion; index++) {
            int unCnt = recentSessionList.get(index).getUnReadCnt();
            if (unCnt > 0) {
                return index;
            }
        }
        //最后返回到最上面
        return 0;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 用户HOLDER
     */
    private final class ContactViewHolder extends ContactHolderBase {
        public RoundImageView avatar;
    }

    /**
     * 基本HOLDER
     */
    public static class ContactHolderBase {
        public TextView uname;
        public TextView lastContent;
        public TextView lastTime;
        public TextView msgCount;
        public View delete;
        public View topView;
        public TextView top;
        public ImageView status;
    }

    /**
     * 群组HOLDER
     */
    private final static class GroupViewHolder extends ContactHolderBase {
        public IMGroupAvatar avatarLayout;
    }

    private View renderUser(int position, View convertView, ViewGroup parent) {
        RecentInfo recentInfo = recentSessionList.get(position);
        ContactViewHolder holder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.tt_item_chat, parent, false);
            holder = new ContactViewHolder();
            holder.avatar = (RoundImageView) convertView.findViewById(R.id.contact_portrait);
            holder.uname = (TextView) convertView.findViewById(R.id.shop_name);
            holder.lastContent = (TextView) convertView.findViewById(R.id.message_body);
            holder.lastTime = (TextView) convertView.findViewById(R.id.message_time);
            holder.msgCount = (TextView) convertView.findViewById(R.id.message_count_notify);
            holder.avatar.setImageResource(R.mipmap.icon_default_avatar);
            holder.delete = convertView.findViewById(R.id.rl_delete);
            holder.topView = convertView.findViewById(R.id.rl_top);
            holder.top = (TextView) convertView.findViewById(R.id.tv_top);
            holder.status = (ImageView) convertView.findViewById(R.id.iv_status);
            convertView.setTag(holder);
        } else {
            holder = (ContactViewHolder) convertView.getTag();
        }

        if (recentInfo.isTop()) {
            // todo   R.color.top_session_background
            convertView.setBackgroundColor(Color.parseColor("#e1e1e1"));
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }

        handleCommonContact(holder, recentInfo);
        return convertView;
    }

    private View renderConsult(int position, View convertView, ViewGroup parent) {
        RecentInfo recentInfo = recentSessionList.get(position);
        ContactViewHolder holder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.tt_item_chat_consult, parent, false);
            holder = new ContactViewHolder();
            holder.avatar = (RoundImageView) convertView.findViewById(R.id.contact_portrait);
            holder.uname = (TextView) convertView.findViewById(R.id.shop_name);
            holder.lastContent = (TextView) convertView.findViewById(R.id.message_body);
            holder.lastTime = (TextView) convertView.findViewById(R.id.message_time);
            holder.msgCount = (TextView) convertView.findViewById(R.id.message_count_notify);
            holder.avatar.setImageResource(R.mipmap.icon_default_avatar);
            holder.delete = convertView.findViewById(R.id.rl_delete);
            holder.topView = convertView.findViewById(R.id.rl_top);
            holder.top = (TextView) convertView.findViewById(R.id.tv_top);
            holder.status = (ImageView) convertView.findViewById(R.id.iv_status);
            convertView.setTag(holder);
        } else {
            holder = (ContactViewHolder) convertView.getTag();
        }

        if (recentInfo.isTop()) {
            // todo   R.color.top_session_background
            convertView.setBackgroundColor(Color.parseColor("#e1e1e1"));
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }

        handleCommonConsult(holder, recentInfo);
        return convertView;
    }

    private View renderNotification(int position, View convertView, ViewGroup parent) {
        RecentInfo recentInfo = recentSessionList.get(position);
        ContactViewHolder holder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.tt_item_chat, parent, false);
            holder = new ContactViewHolder();
            holder.avatar = (RoundImageView) convertView.findViewById(R.id.contact_portrait);
            holder.uname = (TextView) convertView.findViewById(R.id.shop_name);
            holder.lastContent = (TextView) convertView.findViewById(R.id.message_body);
            holder.lastTime = (TextView) convertView.findViewById(R.id.message_time);
            holder.msgCount = (TextView) convertView.findViewById(R.id.message_count_notify);
            holder.delete = convertView.findViewById(R.id.rl_delete);
            holder.top = (TextView) convertView.findViewById(R.id.tv_top);
            holder.topView = convertView.findViewById(R.id.rl_top);
            holder.avatar.setImageResource(R.mipmap.icon_default_avatar);
            holder.status = (ImageView) convertView.findViewById(R.id.iv_status);
            convertView.setTag(holder);
        } else {
            holder = (ContactViewHolder) convertView.getTag();
        }

        if (recentInfo.isTop()) {
            // todo   R.color.top_session_background
            convertView.setBackgroundColor(Color.parseColor("#e1e1e1"));
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }

        handleCommonNotification(holder, recentInfo);
        return convertView;
    }

    private void handleCommonNotification(ContactViewHolder holder, final RecentInfo recentInfo) {
        holder.status.setVisibility(View.GONE);
        int userName = 0;
        String lastContent = "";
        String lastTime = "";
        int unReadCount = 0;
        int msgType = recentInfo.getLatestMsgType();
//        holder.avatar.setCorner(90);
        if (msgType == DBConstant.MSG_TYPE_NOTIFICATION) {
            userName = R.string.label_notification;
//            holder.avatar.setDefaultImageRes(R.mipmap.ic_notificaiton);
            holder.avatar.setImageResource(R.mipmap.ic_notificaiton);
        } else if (msgType == DBConstant.MSG_TYPE_INTERACTION) {
            userName = R.string.label_interaction;
//            holder.avatar.setDefaultImageRes(R.mipmap.ic_interaction);
            holder.avatar.setImageResource(R.mipmap.ic_interaction);
        }
        lastContent = recentInfo.getLatestMsgData();
        lastTime = DateUtil.getSessionTime(recentInfo.getUpdateTime());

        if (configurationSp.isTopSession(recentInfo.getSessionKey())) {
            holder.top.setText("取消置顶");
        } else {
            holder.top.setText("置顶");
        }
        holder.topView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isTop = configurationSp.isTopSession(recentInfo.getSessionKey());
                recentInfo.setTop(!isTop);
                configurationSp.setSessionTop(recentInfo.getSessionKey(), !isTop);
                notifyDataSetChanged();
            }
        });


        unReadCount = recentInfo.getUnReadCnt();
        // 设置未读消息计数
        if (unReadCount > 0) {
            String strCountString = String.valueOf(unReadCount);
            if (unReadCount > 99) {
                strCountString = "99+";
            }
            holder.msgCount.setVisibility(View.VISIBLE);
            holder.msgCount.setText(strCountString);
        } else {
            holder.msgCount.setVisibility(View.GONE);
        }

        // 设置其它信息
        holder.uname.setText(userName);
//        holder.avatar.setImageUrl("");
        if (NotificationConstants.KEY_CREATE_TIME_CONTENT.equals(lastContent)) {
            SpannableStringBuilder spannableString = new SpannableStringBuilder("  " + lastContent);
            spannableString.setSpan(new ImageSpan(imService, R.mipmap.heart_red), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.lastContent.setText(spannableString);
        } else {
            holder.lastContent.setText(lastContent);
        }
        holder.lastTime.setText(lastTime);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int bizType = NotificationConstants.BIZ_TYPE_INTERACTION;
                if (recentInfo.getLatestMsgType() == DBConstant.MSG_TYPE_NOTIFICATION) {
                    bizType = NotificationConstants.BIZ_TYPE_TRANSACTION;
                }
                DBManager.getInstance(imService).deleteMsgByBizType(bizType);
                if (imService == null) return;
                imService.getSessionManager().reqRemoveSession(recentInfo, false);
            }
        });
    }

//    private View renderGroup(int position,View convertView, ViewGroup parent){
//        RecentInfo recentInfo = recentSessionList.get(position);
//        GroupViewHolder holder;
//        if (null == convertView) {
//            convertView = mInflater.inflate(R.layout.tt_item_chat_group, parent,false);
//            holder = new GroupViewHolder();
//            holder.avatarLayout = (IMGroupAvatar) convertView.findViewById(R.id.contact_portrait);
//            holder.uname = (TextView) convertView.findViewById(R.id.shop_name);
//            holder.lastContent = (TextView) convertView.findViewById(R.id.message_body);
//            holder.lastTime = (TextView) convertView.findViewById(R.id.message_time);
//            holder.msgCount = (TextView) convertView.findViewById(R.id.message_count_notify);
//            holder.noDisturb = (ImageView)convertView.findViewById(R.id.message_time_no_disturb_view);
//            convertView.setTag(holder);
//        }else{
//            holder = (GroupViewHolder)convertView.getTag();
//        }
//
//        if(recentInfo.isTop()){
//            // todo   R.color.top_session_background
//            convertView.setBackgroundColor(Color.parseColor("#f4f4f4f4"));
//        }else{
//            convertView.setBackgroundColor(Color.WHITE);
//        }
//
//        /**群屏蔽的设定*/
//        if(recentInfo.isForbidden())
//        {
//            holder.noDisturb.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            holder.noDisturb.setVisibility(View.GONE);
//        }
//
//        handleGroupContact( holder,recentInfo);
//        return convertView;
//    }


    //yingmu base-adapter-helper 了解一下
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //		logger.d("recent#getview position:%d", position);
        try {
            final int type = getItemViewType(position);
            ContactHolderBase holder = null;

            switch (type) {
                case CONTACT_TYPE_USER:
                    convertView = renderUser(position, convertView, parent);
                    break;
                case CONTACT_TYPE_CONSULT:
                    convertView = renderConsult(position, convertView, parent);
                    break;
                case CONTACT_TYPE_NOTIFICATION:
                    convertView = renderNotification(position, convertView, parent);
                    break;
//                case CONTACT_TYPE_GROUP:
//                    convertView =renderGroup(position,convertView,parent);
//                    break;
            }
            return convertView;
        } catch (Exception e) {
            logger.e(e.toString());
            return null;
        }
    }


    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        try {
            if (position >= recentSessionList.size()) {
                return CONTACT_TYPE_INVALID;
            }
            RecentInfo recentInfo = recentSessionList.get(position);
            if (recentInfo.getSessionType() == DBConstant.SESSION_TYPE_SINGLE) {
                return CONTACT_TYPE_USER;
            } else if (recentInfo.getSessionType() == DBConstant.SESSION_TYPE_GROUP) {
                return CONTACT_TYPE_GROUP;
            } else if (recentInfo.getSessionType() == DBConstant.SESSION_TYPE_NOTIFICATION) {
                return CONTACT_TYPE_NOTIFICATION;
            } else if (recentInfo.getSessionType() == DBConstant.SESSION_TYPE_CONSULT) {
                return CONTACT_TYPE_CONSULT;
            } else {
                return CONTACT_TYPE_INVALID;
            }
        } catch (Exception e) {
            logger.e(e.toString());
            return CONTACT_TYPE_INVALID;
        }
    }

    public void setData(List<RecentInfo> recentSessionList, IMService imService) {
        logger.d("recent#set New recent session list");
        logger.d("recent#notifyDataSetChanged");
        this.recentSessionList = recentSessionList;
        this.imService = imService;
        notifyDataSetChanged();
    }

    private void handleCommonContact(final ContactViewHolder contactViewHolder, final RecentInfo recentInfo) {
        String avatarUrl = null;
        String userName = "";
        String lastContent = "";
        String lastTime = "";
        int unReadCount = 0;

        userName = recentInfo.getName();
        lastContent = recentInfo.getLatestMsgData();
        // todo 是不是每次都需要计算
        lastTime = DateUtil.getSessionTime(recentInfo.getUpdateTime());
        if (configurationSp.isTopSession(recentInfo.getSessionKey())) {
            contactViewHolder.top.setText("取消置顶");
        } else {
            contactViewHolder.top.setText("置顶");
        }
        contactViewHolder.topView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isTop = configurationSp.isTopSession(recentInfo.getSessionKey());
                recentInfo.setTop(!isTop);
                configurationSp.setSessionTop(recentInfo.getSessionKey(), !isTop);
                notifyDataSetChanged();
            }
        });
        unReadCount = recentInfo.getUnReadCnt();
        if (null != recentInfo.getAvatar() && recentInfo.getAvatar().size() > 0) {
            avatarUrl = recentInfo.getAvatar().get(0);

        }
        // 设置未读消息计数
        if (unReadCount > 0) {
            String strCountString = String.valueOf(unReadCount);
            if (unReadCount > 99) {
                strCountString = "99+";
            }
            contactViewHolder.msgCount.setVisibility(View.VISIBLE);
            contactViewHolder.msgCount.setText(strCountString);
        } else {
            contactViewHolder.msgCount.setVisibility(View.GONE);
        }
        //头像设置
//        contactViewHolder.avatar.setDefaultImageRes(R.mipmap.icon_default_avatar);
//        contactViewHolder.avatar.setCorner(90);
//        contactViewHolder.avatar.setImageUrl(avatarUrl);

        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(avatarUrl), R.mipmap.icon_default_avatar, contactViewHolder.avatar);
        // 设置其它信息
        contactViewHolder.uname.setText(userName);
        contactViewHolder.lastContent.setText(lastContent);
        contactViewHolder.lastTime.setText(lastTime);
        contactViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imService == null) return;
                imService.getSessionManager().reqRemoveSession(recentInfo, true);
            }
        });
        if (recentInfo.getStatus() == MessageConstant.MSG_SUCCESS) {
            contactViewHolder.status.setVisibility(View.GONE);
        } else {
            contactViewHolder.status.setVisibility(View.VISIBLE);
            if (recentInfo.getStatus() == MessageConstant.MSG_SENDING) {
                contactViewHolder.status.setImageResource(R.mipmap.msg_status_sending);
            } else if (recentInfo.getStatus() == MessageConstant.MSG_FAILURE) {
                contactViewHolder.status.setImageResource(R.mipmap.scenic_tips_image);
            } else {
                contactViewHolder.status.setVisibility(View.GONE);
            }
        }

    }


    private void handleCommonConsult(final ContactViewHolder contactViewHolder, final RecentInfo recentInfo) {
        String avatarUrl = null;
        String userName = "";
        String lastContent = "";
        String lastTime = "";
        int unReadCount = 0;

        userName = recentInfo.getName();
        lastContent = recentInfo.getLatestMsgData();
        // todo 是不是每次都需要计算
        lastTime = DateUtil.getSessionTime(recentInfo.getUpdateTime());
        if (configurationSp.isTopSession(recentInfo.getSessionKey())) {
            contactViewHolder.top.setText("取消置顶");
        } else {
            contactViewHolder.top.setText("置顶");
        }
//        if (recentInfo.getLatestMsgType() == DBConstant.MSG_TYPE_CONSULT_NOTIFY) {
//            contactViewHolder.uname.setVisibility(View.GONE);
//        } else {
//            contactViewHolder.uname.setVisibility(View.VISIBLE);
//        }
        contactViewHolder.topView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isTop = configurationSp.isTopSession(recentInfo.getSessionKey());
                recentInfo.setTop(!isTop);
                configurationSp.setSessionTop(recentInfo.getSessionKey(), !isTop);
                notifyDataSetChanged();
            }
        });
        unReadCount = recentInfo.getUnReadCnt();
        if (null != recentInfo.getAvatar() && recentInfo.getAvatar().size() > 0) {
            avatarUrl = ImageUtils.getImageFullUrl(recentInfo.getAvatar().get(0));

        }
        // 设置未读消息计数
        if (unReadCount > 0) {
            String strCountString = String.valueOf(unReadCount);
            if (unReadCount > 99) {
                strCountString = "99+";
            }
            contactViewHolder.msgCount.setVisibility(View.VISIBLE);
            contactViewHolder.msgCount.setText(strCountString);
        } else {
            contactViewHolder.msgCount.setVisibility(View.GONE);
        }
        //头像设置
//        contactViewHolder.avatar.setDefaultImageRes(R.mipmap.icon_default_avatar);
//        contactViewHolder.avatar.setCorner(90);
//        contactViewHolder.avatar.setImageUrl(avatarUrl);

        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(avatarUrl), R.mipmap.icon_default_avatar, contactViewHolder.avatar);

        // 设置其它信息
        contactViewHolder.uname.setText(userName);
        contactViewHolder.lastContent.setText(lastContent);
        contactViewHolder.lastTime.setText(lastTime);
        contactViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imService == null) return;
                boolean firstDelete = SPUtils.getBoolean(mContext,SPUtils.KEY_FIRST_DELETE_CONSULT_SESSION,true);
                if (firstDelete){
                    ToastUtil.showToast(mContext,"被删除的咨询消息可在订单中查看");
                    SPUtils.save(mContext,SPUtils.KEY_FIRST_DELETE_CONSULT_SESSION,false);
                }
                imService.getSessionManager().reqRemoveSession(recentInfo, true);
            }
        });
        if (recentInfo.getStatus() == MessageConstant.MSG_SUCCESS) {
            contactViewHolder.status.setVisibility(View.GONE);
        } else {
            contactViewHolder.status.setVisibility(View.VISIBLE);
            if (recentInfo.getStatus() == MessageConstant.MSG_SENDING) {
                contactViewHolder.status.setImageResource(R.mipmap.msg_status_sending);
            } else if (recentInfo.getStatus() == MessageConstant.MSG_FAILURE) {
                contactViewHolder.status.setImageResource(R.mipmap.scenic_tips_image);
            } else {
                contactViewHolder.status.setVisibility(View.GONE);
            }
        }

    }

    private void handleGroupContact(GroupViewHolder groupViewHolder, RecentInfo recentInfo) {
        String avatarUrl = null;
        String userName = "";
        String lastContent = "";
        String lastTime = "";
        int unReadCount = 0;
        int sessionType = DBConstant.SESSION_TYPE_SINGLE;

        userName = recentInfo.getName();
        lastContent = recentInfo.getLatestMsgData();
        // todo 是不是每次都需要计算
        lastTime = DateUtil.getSessionTime(recentInfo.getUpdateTime());
        unReadCount = recentInfo.getUnReadCnt();
//        sessionType = recentInfo.getSessionType();
        // 设置未读消息计数 只有群组有的

        if (unReadCount > 0) {
            if (recentInfo.isForbidden()) {
                groupViewHolder.msgCount.setBackgroundResource(R.mipmap.tt_message_botify_no_disturb);
                groupViewHolder.msgCount.setVisibility(View.VISIBLE);
                groupViewHolder.msgCount.setText("");
                ((RelativeLayout.LayoutParams) groupViewHolder.msgCount.getLayoutParams()).leftMargin = AndroidUtils.dp2px(this.mInflater.getContext(),-7);
                ((RelativeLayout.LayoutParams) groupViewHolder.msgCount.getLayoutParams()).topMargin = AndroidUtils.dp2px(this.mInflater.getContext(),6);
                groupViewHolder.msgCount.getLayoutParams().width = AndroidUtils.dp2px(this.mInflater.getContext(),10);
                groupViewHolder.msgCount.getLayoutParams().height = AndroidUtils.dp2px(this.mInflater.getContext(),10);

            } else {
                groupViewHolder.msgCount.setBackgroundResource(R.drawable.tt_message_notify);
                groupViewHolder.msgCount.setVisibility(View.VISIBLE);
                ((RelativeLayout.LayoutParams) groupViewHolder.msgCount.getLayoutParams()).leftMargin = AndroidUtils.dp2px(this.mInflater.getContext(),-10);
                ((RelativeLayout.LayoutParams) groupViewHolder.msgCount.getLayoutParams()).topMargin = AndroidUtils.dp2px(this.mInflater.getContext(),3);
                groupViewHolder.msgCount.getLayoutParams().width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                groupViewHolder.msgCount.getLayoutParams().height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                groupViewHolder.msgCount.setPadding(AndroidUtils.dp2px(this.mInflater.getContext(),3), 0, AndroidUtils.dp2px(this.mInflater.getContext(),3), 0);

                String strCountString = String.valueOf(unReadCount);
                if (unReadCount > 99) {
                    strCountString = "99+";
                }
                groupViewHolder.msgCount.setVisibility(View.VISIBLE);
                groupViewHolder.msgCount.setText(strCountString);
            }

        } else {
            groupViewHolder.msgCount.setVisibility(View.GONE);
        }

        //头像设置
        setGroupAvatar(groupViewHolder, recentInfo.getAvatar());
        // 设置其它信息
        groupViewHolder.uname.setText(userName);
        groupViewHolder.lastContent.setText(lastContent);
        groupViewHolder.lastTime.setText(lastTime);
    }

    /**
     * 设置群头像
     *
     * @param holder
     * @param avatarUrlList
     */
    private void setGroupAvatar(GroupViewHolder holder, List<String> avatarUrlList) {
        try {
            if (null == avatarUrlList) {
                return;
            }
            holder.avatarLayout.setAvatarUrlAppend(SysConstant.AVATAR_APPEND_32);
            holder.avatarLayout.setChildCorner(3);
            if (null != avatarUrlList) {
                holder.avatarLayout.setAvatarUrls(new ArrayList<String>(avatarUrlList));
            }
        } catch (Exception e) {
            logger.e(e.toString());
        }

    }

}
