package com.videolibrary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quanyan.yhy.R;
import com.videolibrary.chat.entity.LiveChatNotifyMessage;
import com.videolibrary.chat.entity.LiveChatTextMessage;

import java.util.ArrayList;

/**
 * 竖屏直播中观看端的聊天消息小列表
 * Created by Jiervs on 2018/4/11.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Object> mList;

    public ChatListAdapter(Context mContext, ArrayList mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View view = View.inflate(mContext, R.layout.item_vertical_live_client_chat,null);
        View view = LayoutInflater.from(mContext).inflate( R.layout.item_vertical_live_client_chat,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_message.setText("");
        if (mList.get(position) instanceof LiveChatTextMessage) {
            LiveChatTextMessage message = (LiveChatTextMessage)mList.get(position);
            if (message.getFromName() !=null && message.getMessageContent() != null ) {
                //name
                SpannableString name = new SpannableString(message.getFromName()+ ":");
                ForegroundColorSpan nameColor = new ForegroundColorSpan(mContext.getResources().getColor(R.color.other_user));
                name.setSpan(nameColor, 0, name.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                holder.tv_message.append(name);

                //content
                SpannableString content = new SpannableString(message.getMessageContent());
                ForegroundColorSpan contentColor = new ForegroundColorSpan(mContext.getResources().getColor(R.color.dark_bg_live_chat));
                content.setSpan(contentColor, 0, content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                holder.tv_message.append(content);
            }
        }

        if (mList.get(position) instanceof LiveChatNotifyMessage) {
            LiveChatNotifyMessage message = (LiveChatNotifyMessage)mList.get(position);
            if (message.getMessageContent() != null ) {
                if (null != message.getFromName()) {
                    //name
                    SpannableString name = new SpannableString(message.getFromName());
                    ForegroundColorSpan nameColor = new ForegroundColorSpan(mContext.getResources().getColor(R.color.other_user));
                    name.setSpan(nameColor, 0, name.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    holder.tv_message.append(name);

                    //notify
                    SpannableString notify = new SpannableString(message.getMessageContent().substring(name.length()));
                    ForegroundColorSpan notifyColor = new ForegroundColorSpan(mContext.getResources().getColor(R.color.dark_bg_live_notify));
                    notify.setSpan(notifyColor, 0, message.getMessageContent().substring(name.length()).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    holder.tv_message.append(notify);
                } else {
                    //notify
                    SpannableString notify = new SpannableString(message.getMessageContent());
                    ForegroundColorSpan notifyColor = new ForegroundColorSpan(mContext.getResources().getColor(R.color.dark_bg_live_notify));
                    notify.setSpan(notifyColor, 0, notify.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    holder.tv_message.append(notify);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_message;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_message = (TextView) itemView.findViewById(R.id.tv_message);
        }
    }

    //添加消息
    public void addMessage(Object message) {
        mList.add(message);
        notifyDataSetChanged();
    }
    //添加公告
    public void addAffiche(String configContent) {
        if (configContent == null || configContent.length() == 0) {
            return;
        }

        LiveChatNotifyMessage message = LiveChatNotifyMessage.createMessageByLocal("鹰和鹰直播: " + configContent);
        addMessage(message);

    }
}
