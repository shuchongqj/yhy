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
public class ContactInfoAdapter extends BaseAdapter{
    private Context mContext;
    private List<UserContact> userContacts = new ArrayList<UserContact>();
    public ContactInfoAdapter(Context context, List<UserContact> list){
        mContext = context;
        userContacts = list;
    }

    /**
     * 更新数据
     * @param list
     */
    public void setData(List<UserContact> list){
        userContacts = list;
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

        final VisitorHolder holder = VisitorHolder.getHolder(convertView);

        final UserContact userContact = userContacts.get(position);

        if (!StringUtil.isEmpty(userContact.certificatesType)) {
            String str = typeToString(userContact.certificatesType);
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

        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnContactItemClickLsn != null) {
                    mOnContactItemClickLsn.onEditClick(userContact);
                }
            }
        });

        holder.rl_visitor.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnContactItemClickLsn != null) {
                    mOnContactItemClickLsn.onDeleteClick(userContact);
                }
                return false;
            }
        });

        holder.rl_visitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnContactItemClickLsn != null) {
                    mOnContactItemClickLsn.onSelectClick(userContact);
                }
            }
        });

        return convertView;
    }

    static class VisitorHolder {

        TextView tv_id_type_label, tv_name, tv_phone, tv_id;
        RelativeLayout rl_visitor;
        ImageView iv_check,iv_edit;

        public VisitorHolder(View convertView) {
            tv_id_type_label = (TextView) convertView.findViewById(R.id.tv_id_type_label);
            tv_id = (TextView) convertView.findViewById(R.id.tv_id);
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            rl_visitor = (RelativeLayout) convertView.findViewById(R.id.rl_visitor);
            iv_check = (ImageView) convertView.findViewById(R.id.iv_check);
            iv_edit = (ImageView) convertView.findViewById(R.id.iv_edit);
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

    private String typeToString(String certificatesType) {
        String str;
        if(certificatesType.equals("1")){
            str = mContext.getResources().getStringArray(R.array.id_type)[0];
        }else if(certificatesType.equals("2")){
            str = mContext.getResources().getStringArray(R.array.id_type)[1];
        }else if(certificatesType.equals("3")){
            str = mContext.getResources().getStringArray(R.array.id_type)[2];
        }else {
            str = mContext.getResources().getStringArray(R.array.id_type)[3];
        }
        return str;
    }


    ContactInfoAdapter.OnContactItemClickLsn mOnContactItemClickLsn;
    public void setOnContactItemClickLsn(ContactInfoAdapter.OnContactItemClickLsn lsn){
        mOnContactItemClickLsn = lsn;
    }

    public interface OnContactItemClickLsn{
        void onEditClick(UserContact info);

        void onDeleteClick(UserContact info);

        void onSelectClick(UserContact info);
    }

}

/*View view = LayoutInflater.from(mContext).inflate(R.layout.item_contact_list,null);
        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.rl_visitor);

        ImageView iv_edit = (ImageView)view.findViewById(R.id.iv_edit);
        *//*ImageView iv_delete = (ImageView)view.findViewById(R.id.iv_delete);*//*

        TextView tv_name = (TextView)view.findViewById(R.id.tv_name);
        TextView tv_phone = (TextView)view.findViewById(R.id.tv_phone);
        TextView tv_email = (TextView)view.findViewById(R.id.tv_email);

        final UserContact userContact = userContacts.get(position);
        if(!StringUtil.isEmpty(userContact.contactName)) {
            tv_name.setText(userContact.contactName);
        }

        if(!StringUtil.isEmpty(userContact.contactPhone)) {
            tv_phone.setText(userContact.contactPhone);
        }

        if(!StringUtil.isEmpty(userContact.contactEmail)) {
            tv_email.setText(userContact.contactEmail);
        }

        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnContactItemClickLsn != null){
                    mOnContactItemClickLsn.onEditClick(userContact);
                }
            }
        });
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnContactItemClickLsn != null){
                    mOnContactItemClickLsn.onSelectClick(userContact);
                }
            }
        });
        rl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnContactItemClickLsn != null) {
                    mOnContactItemClickLsn.onDeleteClick(userContact);
                }
                return false;
            }
        });
        *//*iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnContactItemClickLsn != null){
                    mOnContactItemClickLsn.onDeleteClick(contactInfo);
                }
            }
        });*//*

        return view;*/
