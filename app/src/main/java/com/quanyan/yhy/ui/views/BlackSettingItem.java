package com.quanyan.yhy.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;

public class BlackSettingItem extends RelativeLayout {
    private ImageView iconView;
    private TextView titleView;
    private TextView summaryView;
    private CheckBox mSwitchView;
    private RelativeLayout mRlParentView;

    public BlackSettingItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context);
    }

    public BlackSettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public BlackSettingItem(Context context) {
        super(context);

        init(context);
    }

    private void init(Context context) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        View view = mLayoutInflater.inflate(R.layout.black_settings_item, null);

        LayoutParams rlp = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);

        iconView = (ImageView) view.findViewById(R.id.icon);
        titleView = (TextView) view.findViewById(R.id.title);
        summaryView = (TextView) view.findViewById(R.id.tv_summary);
        mSwitchView = (CheckBox) view.findViewById(R.id.cb_switch);
        mRlParentView = (RelativeLayout) view.findViewById(R.id.rl_app_setting);
        addView(view, rlp);

        setBackgroundResource(R.drawable.find_item_bg_selector);
    }

    public void setSummary(String summary) {
        if (summaryView == null || summary == null) {
            return;
        }
        summaryView.setText(summary);
    }

    public void initItem(int iconResId, int titleResId) {
        if (iconResId != -1) {
            iconView.setImageResource(iconResId);
        }

        if (titleResId != -1) {
            titleView.setText(titleResId);
        }
    }


    public void initItem(int iconResId, String title) {
        if (iconResId != -1) {
            iconView.setImageResource(iconResId);
        }

        if (!StringUtil.isEmpty(title)) {
            titleView.setText(title);
        }
    }

    public void setIcon(int iconResId) {
        if (iconResId != -1) {
            iconView.setImageResource(iconResId);
        }
    }

    public void setTitle(int titleResId) {
        if (titleResId != -1) {
            titleView.setText(titleResId);
        }
    }

    /**
     * 设置开关值
     *
     * @param value
     */
    public void setSwitch(boolean value) {
        if (mSwitchView == null) {
            return;
        }
        mRlParentView.setPadding(0, 12, 0, 12);
        mSwitchView.setVisibility(View.VISIBLE);
        mSwitchView.setChecked(value);
    }

    /**
     * 设置开关的监听事件
     *
     * @param lsn
     */
    public void setSwitchListener(CompoundButton.OnCheckedChangeListener lsn) {
        if (mSwitchView == null) {
            return;
        }
        mSwitchView.setOnCheckedChangeListener(lsn);
    }

    public void setSwitchable(boolean switchable) {
        if (mSwitchView == null) {
            return;
        }
        mSwitchView.setEnabled(switchable);
    }

    public boolean getCheckedValue(){
        if (mSwitchView == null) {
            return false;
        }
        return mSwitchView.isChecked();
    }
}
