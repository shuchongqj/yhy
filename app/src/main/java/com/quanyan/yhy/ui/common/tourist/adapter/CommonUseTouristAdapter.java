package com.quanyan.yhy.ui.common.tourist.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.harwkin.nb.camera.TimeUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.common.tourist.TouristType;
import com.yhy.common.beans.net.model.common.person.UserContact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:CommonUseTouristAdapter
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-6
 * Time:15:24
 * Version 1.0
 * Description:
 */
public class CommonUseTouristAdapter extends BaseAdapter {

    private Context mContext;
    private int type;
    private String touristType;
    private List<UserContact> userContacts;


    public CommonUseTouristAdapter(Context context, int type, String touristType) {
        mContext = context;
        this.type = type;
        this.touristType = touristType;
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
        VisitorHolder vh = null;
        if (convertView == null) {
            vh = new VisitorHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.commonusetourist_view, null);
            vh.iv_check = (ImageView) convertView.findViewById(R.id.iv_check);
            vh.rl_check = (RelativeLayout) convertView.findViewById(R.id.rl_check);
            vh.iv_edit = (TextView) convertView.findViewById(R.id.iv_edit);
            vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            vh.tv_idcard = (TextView) convertView.findViewById(R.id.tv_idcard);
            vh.tv_passcard = (TextView) convertView.findViewById(R.id.tv_passcard);
            vh.tv_mtcard = (TextView) convertView.findViewById(R.id.tv_mtcard);
            vh.tv_hkcard = (TextView) convertView.findViewById(R.id.tv_hkcard);
            vh.tv_tel = (TextView) convertView.findViewById(R.id.tv_tel);
            vh.ll_idcard = (LinearLayout) convertView.findViewById(R.id.ll_idcard);
            vh.ll_passcard = (LinearLayout) convertView.findViewById(R.id.ll_passcard);
            vh.ll_mtcard = (LinearLayout) convertView.findViewById(R.id.ll_mtcard);
            vh.ll_hkcard = (LinearLayout) convertView.findViewById(R.id.ll_hkcard);
            vh.ll_tel = (LinearLayout) convertView.findViewById(R.id.ll_tel);
            vh.ll_tips = (LinearLayout) convertView.findViewById(R.id.ll_tips);
            vh.ll_delete = (RelativeLayout) convertView.findViewById(R.id.cell_visitor_list_delete);
            vh.rl_edit = (RelativeLayout) convertView.findViewById(R.id.rl_edit);
            vh.ll_content = (LinearLayout) convertView.findViewById(R.id.ll_content);
            convertView.setTag(vh);
        } else {
            vh = (VisitorHolder) convertView.getTag();
        }
        vh.ll_tips.setVisibility(View.GONE);


        final UserContact userContact = (UserContact) getItem(position);

        if (type == TouristType.ORDERTOURIST) {
            vh.rl_check.setVisibility(View.VISIBLE);
            vh.rl_check.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 5.0f));
            vh.rl_edit.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 5.0f));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 2.0f);
            layoutParams.setMargins(10, 0, 0, 0);
            vh.ll_content.setLayoutParams(layoutParams);
        } else {
            vh.rl_check.setVisibility(View.GONE);
            vh.rl_edit.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 4.0f));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
            layoutParams.setMargins(50, 0, 0, 0);
            vh.ll_content.setLayoutParams(layoutParams);
        }

        if (userContact.isChoosed) {
            vh.iv_check.setImageResource(R.mipmap.ic_checked);
        } else {
            if(userContact.isCanChoose) {
                vh.iv_check.setImageResource(R.mipmap.ic_uncheck);
            }else{
                vh.iv_check.setImageResource(R.mipmap.ic_cannot_check);
            }
        }

        if (!TextUtils.isEmpty(userContact.contactName)) {
            vh.tv_name.setText(userContact.contactName);
        } else {
            vh.tv_name.setText(null);
        }

        if (!TextUtils.isEmpty(userContact.contactPhone)) {
            vh.tv_tel.setText(userContact.contactPhone);
        } else {
            vh.tv_tel.setText("");
        }

        if (type == TouristType.MIMETOURIST) {
            setTipsVis(userContact, vh, type);
        } else if (type == TouristType.ORDERCONTACTS) {
            setTipsVis(userContact, vh, type);
        } else if (type == TouristType.ORDERTOURIST) {
            if (touristType.equals(TouristType.TRAVELIN)) {
                setTipsVis(userContact, vh, type);
            } else {
                if (TextUtils.isEmpty(userContact.lastName) || TextUtils.isEmpty(userContact.firstName) || userContact.passportCert == null) {
                    vh.ll_tips.setVisibility(View.VISIBLE);
                }
                setTipsVis(userContact, vh, type);
            }
        }

        vh.ll_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TimeUtil.isFastDoubleClick()) {
                    return;
                }
                if (mOnVisitorItemClickLsn != null) {
                    mOnVisitorItemClickLsn.onDeleteClick(userContact);
                }
            }
        });

        vh.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TimeUtil.isFastDoubleClick()) {
                    return;
                }
                if (mOnVisitorItemClickLsn != null) {
                    mOnVisitorItemClickLsn.onEditClick(userContact);
                }
            }
        });

        return convertView;
    }


    static class VisitorHolder {
        public ImageView iv_check;
        public TextView iv_edit, tv_name, tv_idcard, tv_passcard, tv_mtcard, tv_hkcard, tv_tel;
        public LinearLayout ll_idcard, ll_passcard, ll_mtcard, ll_hkcard, ll_tel, ll_tips;
        public RelativeLayout rl_check, rl_edit;
        public RelativeLayout ll_delete;
        public LinearLayout ll_content;
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

    public void setCanChoose() {
        if (type == TouristType.ORDERTOURIST) {
            if (touristType.equals(TouristType.TRAVELIN)) {//境内游游客
                setUserCanChoose();
            } else if (touristType.equals(TouristType.TRAVELOUT)) {//境外游游客
                for (UserContact userContact : userContacts) {
                    if (TextUtils.isEmpty(userContact.lastName) || TextUtils.isEmpty(userContact.firstName)) {
                        userContact.isCanChoose = false;
                    }
                }
                notifyDataSetChanged();
            }
        } else if (type == TouristType.ORDERCONTACTS) {
            setUserCanChoose();
        } else {

        }
    }

    OnVisitorItemClickLsn mOnVisitorItemClickLsn;

    public void setOnVisitorItemClickLsn(OnVisitorItemClickLsn lsn) {
        mOnVisitorItemClickLsn = lsn;
    }

    public interface OnVisitorItemClickLsn {
        void onEditClick(UserContact info);

        void onDeleteClick(UserContact info);

    }

    private void setUserCanChoose() {
        for (UserContact userContact : userContacts) {
            if (userContact.idCert == null && userContact.passportCert == null && userContact.militaryCert == null && userContact.hkMacaoCert == null) {
                userContact.isCanChoose = false;
            }
        }
        notifyDataSetChanged();
    }

    private void setTipsVis(UserContact userContact, VisitorHolder vh, int type) {
        if (userContact == null) {
            return;
        }
        if (vh == null) {
            return;
        }

        if (type == TouristType.MIMETOURIST) {
            setCards(userContact, vh);
        } else {
            if (userContact.idCert == null && userContact.passportCert == null && userContact.militaryCert == null && userContact.hkMacaoCert == null) {
                vh.ll_tips.setVisibility(View.VISIBLE);
                vh.ll_idcard.setVisibility(View.GONE);
                vh.ll_passcard.setVisibility(View.GONE);
                vh.ll_mtcard.setVisibility(View.GONE);
                vh.ll_hkcard.setVisibility(View.GONE);
            } else {
                setCards(userContact, vh);
            }
        }
    }

    private void setCards(UserContact userContact, VisitorHolder vh) {

        if (userContact == null) {
            return;
        }

        if (vh == null) {
            return;
        }

        if (userContact.idCert == null) {
            vh.ll_idcard.setVisibility(View.GONE);
        } else {
            if (userContact.idCert.cert == null) {
                vh.ll_idcard.setVisibility(View.GONE);
            } else {
                if (TextUtils.isEmpty(userContact.idCert.cert.cardNO)) {
                    vh.ll_idcard.setVisibility(View.GONE);
                } else {
                    vh.ll_idcard.setVisibility(View.VISIBLE);
                    vh.tv_idcard.setText(userContact.idCert.cert.cardNO);
                }
            }
        }

        if (userContact.passportCert == null) {
            vh.ll_passcard.setVisibility(View.GONE);
        } else {
            if (userContact.passportCert.cert == null) {
                vh.ll_passcard.setVisibility(View.GONE);
            } else {
                if (TextUtils.isEmpty(userContact.passportCert.cert.cardNO)) {
                    vh.ll_passcard.setVisibility(View.GONE);
                } else {
                    vh.ll_passcard.setVisibility(View.VISIBLE);
                    vh.tv_passcard.setText(userContact.passportCert.cert.cardNO);
                }
            }
        }

        if (userContact.militaryCert == null) {
            vh.ll_mtcard.setVisibility(View.GONE);
        } else {
            if (userContact.militaryCert.cert == null) {
                vh.ll_mtcard.setVisibility(View.GONE);
            } else {
                if (TextUtils.isEmpty(userContact.militaryCert.cert.cardNO)) {
                    vh.ll_mtcard.setVisibility(View.GONE);
                } else {
                    vh.ll_mtcard.setVisibility(View.VISIBLE);
                    vh.tv_mtcard.setText(userContact.militaryCert.cert.cardNO);
                }
            }
        }

        if (userContact.hkMacaoCert == null) {
            vh.ll_hkcard.setVisibility(View.GONE);
        } else {
            if (userContact.hkMacaoCert.cert == null) {
                vh.ll_hkcard.setVisibility(View.GONE);
            } else {
                if (TextUtils.isEmpty(userContact.hkMacaoCert.cert.cardNO)) {
                    vh.ll_hkcard.setVisibility(View.GONE);
                } else {
                    vh.ll_hkcard.setVisibility(View.VISIBLE);
                    vh.tv_hkcard.setText(userContact.hkMacaoCert.cert.cardNO);
                }
            }
        }
    }

}
