package com.quanyan.yhy.ui.common.tourist.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.yhy.R;
import com.quanyan.yhy.common.IdentityType;
import com.yhy.common.beans.net.model.user.Certificate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:DeleteCodeAdapter
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-7
 * Time:11:06
 * Version 1.0
 * Description:
 */
public class DeleteCodeAdapter extends BaseAdapter {

    private Context mContext;
    private List<Certificate> mPersonCodes;
    private DeleteCodeClickListener mDeleteCodeClickListener;

    public DeleteCodeAdapter(Context context, int type) {
        mContext = context;
        mPersonCodes = new ArrayList<Certificate>();
    }

    /**
     * 更新数据
     *
     * @param list
     */
    public void setData(List<Certificate> list) {
        if (list != null) {
            mPersonCodes.clear();
            mPersonCodes.addAll(list);
        }
        notifyDataSetChanged();
    }

    //清空数据
    public void clearData() {
        mPersonCodes.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mPersonCodes.size();
    }

    @Override
    public Object getItem(int position) {
        return mPersonCodes.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.person_code_item, null);
            vh.tv_code = (TextView) convertView.findViewById(R.id.tv_code);
            vh.tv_codetype = (TextView) convertView.findViewById(R.id.tv_codetype);
            vh.tv_edit = (TextView) convertView.findViewById(R.id.tv_edit);
            vh.scroll_delete_menu_layout = (LinearLayout) convertView.findViewById(R.id.scroll_delete_menu_layout);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        if (!TextUtils.isEmpty(mPersonCodes.get(position).type)) {
            vh.tv_codetype.setText(IdentityType.showIdType(mContext, mPersonCodes.get(position).type));
        }

        if (!TextUtils.isEmpty(mPersonCodes.get(position).cardNO)) {
            vh.tv_code.setText(mPersonCodes.get(position).cardNO);
        }

        vh.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(mDeleteCodeClickListener != null){
                     mDeleteCodeClickListener.edit(mPersonCodes.get(position));
                 }
            }
        });

        vh.scroll_delete_menu_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDeleteCodeClickListener != null){
                    mDeleteCodeClickListener.delete(mPersonCodes.get(position));
                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        public TextView tv_codetype;
        public TextView tv_code;
        public TextView tv_edit;
        public LinearLayout scroll_delete_menu_layout;
    }

    public void setCodeAdapterlListener(DeleteCodeClickListener mDeleteCodeClickListener) {
        this.mDeleteCodeClickListener = mDeleteCodeClickListener;
    }

    public interface DeleteCodeClickListener {
        void delete(Certificate personCode);

        void edit(Certificate personCode);
    }
}
