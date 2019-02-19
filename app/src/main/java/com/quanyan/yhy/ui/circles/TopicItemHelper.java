package com.quanyan.yhy.ui.circles;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.yhy.common.beans.net.model.comment.ComTagInfo;
import com.yhy.common.beans.net.model.discover.TopicInfoResult;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

import java.text.DecimalFormat;

/**
 * Created with Android Studio.
 * Title:TopicItemHelper
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/6/29
 * Time:11:51
 * Version 1.1.0
 */
public class TopicItemHelper {

    public static void handleTopicItem(Context context, BaseAdapterHelper helper, TopicInfoResult topicInfoResult, DecimalFormat decimalFormat) {
        TextView participate  = helper.getView(R.id.fg_topic_item_participate_count);
        TextView readCount  = helper.getView(R.id.fg_topic_item_read_count);

        if(topicInfoResult.talkNum > 10000){
            participate.setText(String.format(context.getString(R.string.label_topic_item_participate_count),
                    decimalFormat.format(topicInfoResult.talkNum / 10000f)));
        }else{
            participate.setText(String.format(context.getString(R.string.label_topic_item_participate_count1),
                    topicInfoResult.talkNum));
        }

        if(topicInfoResult.redNum > 10000){
            readCount.setText(String.format(context.getString(R.string.label_topic_item_read_count),
                    decimalFormat.format(topicInfoResult.redNum / 10000f)));
        }else{
            readCount.setText(String.format(context.getString(R.string.label_topic_item_read_count1),
                    topicInfoResult.redNum));
        }

        ImageLoadManager.loadImage(topicInfoResult.pics,R.mipmap.ic_default_topic_bg,helper.getView(R.id.fg_topic_item_img),3,true);
        helper.setText(R.id.fg_topic_item_title, topicInfoResult.title);
        helper.setText(R.id.fg_topic_item_content, topicInfoResult.content);
    }

    public static void handleTopicItem(Context context, View view, ComTagInfo comTagInfo) {
        view.findViewById(R.id.fg_topic_item_participate_count).setVisibility(View.GONE);
        view.findViewById(R.id.fg_topic_item_read_count).setVisibility(View.GONE);
        ((TextView) view.findViewById(R.id.fg_topic_item_title)).setText("");
        ((TextView) view.findViewById(R.id.fg_topic_item_content)).setText("");
        ((TextView) view.findViewById(R.id.fg_topic_item_participate_count)).setText(
                String.format(context.getString(R.string.label_topic_item_participate_count), 20.3f));
        ((TextView) view.findViewById(R.id.fg_topic_item_read_count)).setText(
                String.format(context.getString(R.string.label_topic_item_read_count), 80f));
//        BaseImgView.loadimg((ImageView) view.findViewById(R.id.fg_topic_item_img),
//                "", R.mipmap.icon_default_215_215, R.mipmap.icon_default_215_215, R.mipmap.icon_default_215_215
//                , ImageScaleType.EXACTLY, 250, 250, -1);
        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(""), R.mipmap.icon_default_215_215, 250, 250, (ImageView) view.findViewById(R.id.fg_topic_item_img));
    }

    public static void handleTopicItem(Context context, View view, TopicInfoResult topicInfoResult) {
        view.findViewById(R.id.fg_topic_item_participate_count).setVisibility(View.GONE);
        view.findViewById(R.id.fg_topic_item_read_count).setVisibility(View.GONE);
        ((TextView) view.findViewById(R.id.fg_topic_item_title)).setText(topicInfoResult.title);
        ((TextView) view.findViewById(R.id.fg_topic_item_content)).setText(topicInfoResult.content);

        if(topicInfoResult.talkNum > 10000){
            ((TextView) view.findViewById(R.id.fg_topic_item_participate_count)).setText(
                    String.format(context.getString(R.string.label_topic_item_participate_count), topicInfoResult.talkNum / 10000f));
        }else{
            ((TextView) view.findViewById(R.id.fg_topic_item_participate_count)).setText(
                    String.format(context.getString(R.string.label_topic_item_participate_count1), topicInfoResult.talkNum));
        }

        if(topicInfoResult.redNum > 10000){
            ((TextView) view.findViewById(R.id.fg_topic_item_read_count)).setText(
                    String.format(context.getString(R.string.label_topic_item_participate_count), topicInfoResult.redNum / 10000f));
        }else{
            ((TextView) view.findViewById(R.id.fg_topic_item_read_count)).setText(
                    String.format(context.getString(R.string.label_topic_item_participate_count1), topicInfoResult.redNum));
        }

//        BaseImgView.loadimg((ImageView) view.findViewById(R.id.fg_topic_item_img),
//                topicInfoResult.pics, R.mipmap.icon_default_215_215, R.mipmap.icon_default_215_215, R.mipmap.icon_default_215_215
//                , ImageScaleType.EXACTLY, 250, 250, -1);

        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(topicInfoResult.pics), R.mipmap.icon_default_215_215, 250, 250, (ImageView) view.findViewById(R.id.fg_topic_item_img));

    }
}
