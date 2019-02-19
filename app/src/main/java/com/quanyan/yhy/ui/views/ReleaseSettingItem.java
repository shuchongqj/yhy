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

import com.quanyan.yhy.R;

public class ReleaseSettingItem extends RelativeLayout {
    private TextView titleView;
    private TextView summaryView;
    private ImageView mGoView;
    private CheckBox mSwitchView;
    private RelativeLayout mRlParentView;

    public ReleaseSettingItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context);
    }

    public ReleaseSettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public ReleaseSettingItem(Context context) {
        super(context);

        init(context);
    }

    private void init(Context context) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        View view = mLayoutInflater.inflate(R.layout.release_settings_item, null);

        LayoutParams rlp = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);

        titleView = (TextView) view.findViewById(R.id.title);
        summaryView = (TextView) view.findViewById(R.id.tv_summary);
        mSwitchView = (CheckBox) view.findViewById(R.id.cb_switch);
        mGoView = (ImageView) view.findViewById(R.id.iv_go);
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

    public String getSummary(){
        return summaryView.getText().toString().trim();
    }

    public void initItem(int titleResId) {

        if (titleResId != -1) {
            titleView.setText(titleResId);
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
        mGoView.setVisibility(View.GONE);
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
}
