package com.quanyan.yhy.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mogujie.tt.imservice.service.IMService;
import com.quanyan.yhy.R;
import com.quanyan.yhy.database.DBManager;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.views.InteractiveMessageView;
import com.quanyan.yhy.ui.views.NotificationMessageItemView;
import com.quanyan.yhy.ui.views.OrderMessageItemView;
import com.yhy.common.beans.im.NotificationMessageEntity;
import com.yhy.common.beans.net.model.notification.NotificationInteractiveMessage;
import com.yhy.common.beans.net.model.notification.NotificationOrderMessage;
import com.yhy.common.constants.NotificationConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:NotificationMessageAdapter
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2015/12/16
 * Time:16:20
 * Version 1.0
 */
public class NotificationMessageAdapter extends BaseAdapter {

    private List<NotificationMessageEntity> lists;
    private Context context;
    private LayoutInflater inflater;
    private IMService imService;

    public NotificationMessageAdapter(Context context, List<NotificationMessageEntity> lists) {
        this.context = context;
        this.lists = lists;
        inflater = LayoutInflater.from(context);
    }

    public long getMixId() {
        if (lists == null)
            return 0;
        return lists.get(lists.size() - 1).getId();
    }

    @Override
    public int getCount() {
        return lists == null ? 0 : lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        NotificationMessageEntity entity = lists.get(position);
        if (entity.isNewVersion()) {
            return 2;
        } else if (entity.getBizType() == NotificationConstants.BIZ_TYPE_TRANSACTION) {
            return 0;
        } else if (entity.getBizType() == NotificationConstants.BIZ_TYPE_INTERACTION) {
            return 1;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NotificationMessageEntity entity = (NotificationMessageEntity) getItem(position);
        int viewType = getItemViewType(position);
        Log.d("adapter", "getViewType = " + viewType + "position = " + position);
        if (convertView == null) {
            convertView = createConvertView(viewType);
        }
        bindView(convertView, position, entity, viewType);
        return convertView;
    }

    private void bindView(View convertView, int position, NotificationMessageEntity entity, int viewType) {
        convertView.setTag(entity);
        try {
            if (viewType == 2) {
                ((NotificationMessageItemView) convertView).loadData(entity, getCount() - 1 == position);
            } else if (viewType == 0) {
                ((OrderMessageItemView) convertView).loadData((NotificationOrderMessage) entity, getCount() - 1 == position);
            } else if (viewType == 1) {
                ((InteractiveMessageView) convertView).loadData((NotificationInteractiveMessage) entity);
            }
        } catch (Exception e) {
            Log.d("adapter", "notificationMessageAdatper obj = " + entity.toString());
        }

    }

    private View createConvertView(int type) {
        View v = null;
        if (type == 0) {
            v = new OrderMessageItemView(context);
            Log.d("adapter", "createView getViewType = " + type + "orderMessageItemView");
        } else if (type == 1) {
            v = new InteractiveMessageView(context);
            Log.d("adapter", "createView getViewType = " + type + "InteractiveMessageView");
        } else if (type == 2) {
            v = new NotificationMessageItemView(context);
            Log.d("adapter", "createView getViewType = " + type + "NotificationMessageItemView");
        }
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                NotificationMessageEntity entity = (NotificationMessageEntity) v.getTag();
                showDeleteMessageDialog(entity);
                return false;
            }
        });
        return v;
    }

    public void addAll(List<NotificationMessageEntity> replaylist) {
        if (lists == null) {
            lists = replaylist;
        } else {
            lists.addAll(replaylist);
        }
        notifyDataSetChanged();
    }

    public void add(NotificationMessageEntity entity) {
        if (lists == null) {
            lists = new ArrayList<>();
        }
        lists.add(0, entity);
        notifyDataSetChanged();
    }

    public void replaysAll(List<NotificationMessageEntity> replaylist) {
        lists = replaylist;
        notifyDataSetChanged();
    }

    private Dialog deleteDialog;

    public void showDeleteMessageDialog(final NotificationMessageEntity entity) {

        deleteDialog = DialogUtil.showMessageDialog(context, context.getString(R.string.dialog_order_cancel_title), context.getString(R.string.dialog_delete_message), context.getString(R.string.label_btn_ok), context.getString(R.string.cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBManager.getInstance(context).deleteMsg(entity);
                int position = lists.indexOf(entity);
                lists.remove(entity);
                notifyDataSetChanged();
                if (position == 0) {
                    if (lists.size() == 0) {
                        entity.setMessage("");
                        imService.getSessionManager().updateSession(entity);
                    } else {
                        imService.getSessionManager().updateSession(lists.get(0));
                    }
                }
                deleteDialog.dismiss();

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });
        deleteDialog.show();
    }

    public void setImService(IMService imService) {
        this.imService = imService;
    }

}
