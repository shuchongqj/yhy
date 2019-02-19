package com.quanyan.yhy.ui.discovery.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.yhy.common.beans.net.model.discover.TopicInfoResult;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

/**
 * Created with Android Studio.
 * Title:TopicDetailsHeadView
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-6-30
 * Time:17:21
 * Version 1.0
 * Description:
 */
public class TopicDetailsHeadView extends LinearLayout {

    private TextView mTopicTitle;
    private TextView mTopicSubTitle;
    private TextView mTopicContent;
    private ImageView mTopicDetailIv;

    private RelativeLayout mEmptyView;

    public TopicDetailsHeadView(Context context) {
        super(context);
        init(context);
    }

    public TopicDetailsHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TopicDetailsHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TopicDetailsHeadView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        setOrientation(LinearLayout.HORIZONTAL);
        View.inflate(context, R.layout.topicdetails_headview, this);

        mTopicTitle = (TextView) this.findViewById(R.id.tv_topic_title);
        mTopicSubTitle = (TextView) this.findViewById(R.id.tv_topic_subtitle);
        mTopicContent = (TextView) this.findViewById(R.id.tv_topic_content);
        mTopicDetailIv = (ImageView) this.findViewById(R.id.iv_topic_detail);
        mEmptyView = (RelativeLayout)this.findViewById(R.id.emptyView);
    }

    /**
     * 设置话题内容
     *
     * @param topicInfoResult
     */
    public void setTopicContent(TopicInfoResult topicInfoResult) {
        if (topicInfoResult == null) {
            return;
        }
        if (!TextUtils.isEmpty(topicInfoResult.pics)) {
//            BaseImgView.loadimg(mTopicDetailIv, topicInfoResult.pics, R.mipmap.ic_topic_detail_bg,
//                    R.mipmap.ic_topic_detail_bg, R.mipmap.ic_topic_detail_bg, ImageScaleType.EXACTLY, 750, 360, -1);

            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(topicInfoResult.pics), R.mipmap.ic_topic_detail_bg, 750, 360, mTopicDetailIv);

        } else {
            mTopicDetailIv.setImageResource(R.mipmap.ic_topic_detail_bg);
        }

        if (!TextUtils.isEmpty(topicInfoResult.title)) {
            mTopicTitle.setText(topicInfoResult.title);
        } else {
            mTopicTitle.setText("");
        }

        if (!TextUtils.isEmpty(topicInfoResult.content)) {
            if(mTopicContent.getVisibility() == View.GONE){
                mTopicContent.setVisibility(View.VISIBLE);
            }
            mTopicContent.setText(topicInfoResult.content);
        } else {
            mTopicContent.setVisibility(View.GONE);
            mTopicContent.setText("");
        }
        mTopicSubTitle.setText(getContext().getString(R.string.label_count_participate) + StringUtil.formatTopicReadNum(topicInfoResult.talkNum) + "        " +
                getContext().getString(R.string.label_count_read) + StringUtil.formatTopicReadNum(topicInfoResult.redNum));
    }


//    public void setEmptyView(){
//        if(mEmptyView != null && mEmptyView.getVisibility() == View.GONE){
//            mEmptyView.setVisibility(View.VISIBLE);
//        }
//    }

}
