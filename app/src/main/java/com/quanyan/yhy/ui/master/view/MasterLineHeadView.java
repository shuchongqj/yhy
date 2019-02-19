package com.quanyan.yhy.ui.master.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.yhy.common.beans.net.model.RCShowcase;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

/**
 * Created with Android Studio.
 * Title:MasterLineHeadView
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-7-28
 * Time:15:01
 * Version 1.0
 * Description:
 */
public class MasterLineHeadView extends LinearLayout {

    private ImageView mHeadImage;
    private TextView mHeadTitle;
    private TextView mHeadSubTitle;
    private TextView mHeadSubSubTitle;
    private TextView mHeadContent;
    private TextView mHeadListTitle;

    public MasterLineHeadView(Context context) {
        super(context);
        init(context);
    }

    public MasterLineHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MasterLineHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MasterLineHeadView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        setOrientation(LinearLayout.HORIZONTAL);
        View.inflate(context, R.layout.view_masterline_head, this);
        mHeadImage = (ImageView) this.findViewById(R.id.iv_topic_detail);
        mHeadTitle = (TextView) this.findViewById(R.id.tv_topic_title);
        mHeadSubTitle = (TextView) this.findViewById(R.id.tv_topic_subtitle);
        mHeadSubSubTitle = (TextView) this.findViewById(R.id.tv_topic_subsubtitle);
        mHeadContent = (TextView) this.findViewById(R.id.tv_topic_content);
        mHeadListTitle = (TextView) this.findViewById(R.id.tv_list);
    }

    /**
     * 填充数据
     *
     * @param value
     */
    public void handleData(RCShowcase value) {
        mHeadImage.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (ScreenSize.getScreenWidth(getContext()) / ValueConstants.SCALE_VALUE_BANNER)));

        if (StringUtil.isEmpty(value.imgUrl)) {
            mHeadImage.setImageResource(R.mipmap.ic_my_default_bg);
        } else {
            ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(value.imgUrl), R.mipmap.ic_my_default_bg, mHeadImage);

        }
        if (!StringUtil.isEmpty(value.title)) {
            mHeadContent.setText(value.title);
        }
    }

}
