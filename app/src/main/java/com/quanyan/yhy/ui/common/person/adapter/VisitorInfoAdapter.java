package com.quanyan.yhy.ui.common.person.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.IdentityType;
import com.quanyan.yhy.ui.common.person.activity.VisitorListActivity;
import com.yhy.common.beans.net.model.common.person.UserContact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:Adapter
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:和平必胜、正义必胜、人民必胜
 * Author:炎黄子孙
 * Date:15/10/30
 * Time:下午8:27
 * Version 1.0
 */
public class VisitorInfoAdapter extends BaseAdapter {
    private Context mContext;
    private int type;
    private List<UserContact> userContacts;


    public VisitorInfoAdapter(Context context, int type) {
        mContext = context;
        this.type = type;
        userContacts = new ArrayList<UserContact>();
    }

    /**
     * 更新数据
     *
     * @param list
     */
    public void setData(List<UserContact> list) {
        if (list != null) {
            userContacts.clear();
            userContacts.addAll(list);
        }
        notifyDataSetChanged();
    }

    //清空数据
    public void clearData() {
        userContacts.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return userContacts.size();
    }

    @Override
    public Object getItem(int position) {
        return userContacts.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_visitor_list, null);
        }

        VisitorHolder holder = VisitorHolder.getHolder(convertView);

        final UserContact userContact = (UserContact) getItem(position);

        if (VisitorListActivity.TYPE_FROM_VISITOR == type) {
            holder.iv_check.setVisibility(View.VISIBLE);

            if (userContact.isChoosed) {
                holder.iv_check.setImageResource(R.mipmap.ic_checked);
            } else {
                holder.iv_check.setImageResource(R.mipmap.ic_uncheck);
            }

        }


        if (!StringUtil.isEmpty(userContact.certificatesType)) {
            //String str = typeToString(userContact.certificatesType);
            String str = IdentityType.showIdType(mContext, userContact.certificatesType);
            holder.tv_id_type_label.setText(str);
        }
        if (!StringUtil.isEmpty(userContact.contactName)) {
            holder.tv_name.setText(userContact.contactName);
        }

        if (!StringUtil.isEmpty(userContact.contactPhone)) {
            holder.tv_phone.setText(userContact.contactPhone);
        }

        if (!StringUtil.isEmpty(userContact.certificatesNum)) {
            holder.tv_id.setText(userContact.certificatesNum);
        }

        holder.cell_visitor_list_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnVisitorItemClickLsn != null) {
                    mOnVisitorItemClickLsn.onDeleteClick(userContact);
                }
            }
        });

        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnVisitorItemClickLsn != null) {
                    mOnVisitorItemClickLsn.onEditClick(userContact);
                }
            }
        });

        return convertView;
    }


    static class VisitorHolder {

        TextView tv_id_type_label, tv_name, tv_phone, tv_id, cell_visitor_list_delete, iv_edit;
        RelativeLayout rl_visitor;
        ImageView iv_check;

        public VisitorHolder(View convertView) {
            tv_id_type_label = (TextView) convertView.findViewById(R.id.tv_id_type_label);
            tv_id = (TextView) convertView.findViewById(R.id.tv_id);
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            cell_visitor_list_delete = (TextView) convertView.findViewById(R.id.cell_visitor_list_delete);
            rl_visitor = (RelativeLayout) convertView.findViewById(R.id.rl_visitor);
            iv_check = (ImageView) convertView.findViewById(R.id.iv_check);
            iv_edit = (TextView) convertView.findViewById(R.id.iv_edit);
        }

        public static VisitorHolder getHolder(View convertView) {
            VisitorHolder holder = (VisitorHolder) convertView.getTag();
            if (holder == null) {
                holder = new VisitorHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }



    /**
     * 选择状态传入改变
     */
    public void checkStatusChange(List<UserContact> userContacts) {

        for (UserContact userContact : userContacts) {
            for (UserContact userContact1 : this.userContacts) {
                if (userContact.id == userContact1.id) {
                    userContact1.isChoosed = userContact.isChoosed;
                    continue;
                }
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 返回选中的常用旅客
     *
     * @return
     */
    public List<UserContact> getSelectedVisitors() {
        List<UserContact> values = new ArrayList<>();

        for (UserContact userContact : userContacts) {
            if (userContact.isChoosed) {
                values.add(userContact);
            }
        }
        return values;
    }

    OnVisitorItemClickLsn mOnVisitorItemClickLsn;

    public void setOnVisitorItemClickLsn(OnVisitorItemClickLsn lsn) {
        mOnVisitorItemClickLsn = lsn;
    }

    public interface OnVisitorItemClickLsn {
        void onEditClick(UserContact info);

        void onDeleteClick(UserContact info);

    }
}
