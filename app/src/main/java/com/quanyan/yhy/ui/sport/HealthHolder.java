package com.quanyan.yhy.ui.sport;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanyan.yhy.R;

/**
 * Created by shenwenjie on 2018/1/29.
 */

public class HealthHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView btnChat = null;
    TextView tvName = null;
    TextView tvPositional = null;
    ImageView sdHead = null;
    HealthListener listener = null;

    public HealthHolder(View itemView) {
        super(itemView);
        sdHead = (ImageView) itemView.findViewById(R.id.health_list_item_sdHead);
        tvPositional = (TextView) itemView.findViewById(R.id.health_list_item_tvPositional);
        tvName = (TextView) itemView.findViewById(R.id.health_list_item_tvName);
        btnChat = (TextView) itemView.findViewById(R.id.health_list_item_btnChat);
        btnChat.setOnClickListener(this);
        sdHead.setOnClickListener(this);
    }

    public void setListener(HealthListener listener) {
        this.listener = listener;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.health_list_item_btnChat:
            case R.id.health_list_item_sdHead:
                if (listener != null) {
                    int position = (int) v.getTag();
                    listener.onChatItemClick(position);
                }
                break;
            default:
                break;
        }
    }
}
