package com.quanyan.yhy.ui.comment;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.harwkin.nb.camera.ImageUtils;
import com.quanyan.base.util.DateUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.customview.NoScrollGridView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.discovery.viewhelper.LiveItemHelper;
import com.quanyan.yhy.view.JustifyTextView;
import com.quanyan.yhy.view.RatingBarView;
import com.yhy.common.beans.net.model.comment.RateInfo;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created with Android Studio.
 * Title:CommentItemHelper
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-3-7
 * Time:11:39
 * Version 1.0
 */

public class CommentItemHelper {

    //处理评价列表数据
    public static void handleItem(final Activity context, BaseAdapterHelper helper, final RateInfo item, final String mOrderType, final long mSearchId) {
        //评价用户信息
        if (item.userInfo != null) {
            helper.setImageUrlRound(R.id.iv_comment_user_head, item.userInfo.avatar, 128, 128, R.mipmap.icon_default_avatar);
            helper.setText(R.id.comment_user_name, item.userInfo.nickname);
        }
        //星星显示
        RatingBarView ratingBarView = helper.getView(R.id.comment_count);
        ratingBarView.setClickable(false);
        ratingBarView.setHalfStar(Float.parseFloat(String.valueOf(item.score / 100f)));
        //时间处理
        helper.setText(R.id.tv_comment_time, DateUtil.getyyyymmddhhmm(item.gmtCreated));
        //内容显示
        if(StringUtil.isEmpty(item.content)){
            helper.setVisible(R.id.comment_brief, false);
        }else {
            helper.setVisible(R.id.comment_brief, true);
            helper.setText(R.id.comment_brief, item.content);
        }
        //店家回复
        if(StringUtil.isEmpty(item.backContent)){
            helper.setVisible(R.id.tv_response, false);
        }else {
            helper.setVisible(R.id.tv_response, true);
            helper.setText(R.id.tv_response, item.backContent);
        }

        //图片处理
        if(item.picList != null && item.picList.size() > 0){
            helper.setVisible(R.id.comment_pic_grid, true);
            NoScrollGridView gridPicView = helper.getView(R.id.comment_pic_grid);
            if(gridPicView.getAdapter() == null){
                gridPicView.setAdapter(LiveItemHelper.setPicPreviewAdapter(context));
            }
            ((QuickAdapter) gridPicView.getAdapter()).replaceAll(item.picList);
            gridPicView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(AnalyDataValue.KEY_COMMENTID, item.id + "");
                    map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.getType(mOrderType));
                    map.put(AnalyDataValue.KEY_ID, mSearchId + "");
                    TCEventHelper.onEvent(context, AnalyDataValue.COMMENTS_VIEW_PICTURES, map);

                    ArrayList<String> tmpList = new ArrayList<>();
                    for (String str : item.picList) {
                        tmpList.add(ImageUtils.getImageFullUrl(str));
                    }
                    NavUtils.gotoLookBigImage(context, tmpList, position);
                }
            });
            //gridPicView.setOnItemClickListener(new LiveItemHelper.PicGridItemClick(context, (ArrayList<String>) item.picList));
        }else {
            helper.setVisible(R.id.comment_pic_grid, false);
        }

    }

    //处理评价列表数据
    public static void setCommentAdapter(final Activity context, View view, final RateInfo rateInfo, final String mOrderType, final long mScenicId) {
        ImageView avatarImg = (ImageView) view.findViewById(R.id.iv_comment_user_head);
        TextView nickNameText = (TextView) view.findViewById(R.id.comment_user_name);
        RatingBarView ratingBarView = (RatingBarView) view.findViewById(R.id.comment_count);
        TextView timeText = (TextView) view.findViewById(R.id.tv_comment_time);
        JustifyTextView contentText = (JustifyTextView) view.findViewById(R.id.comment_brief);
        TextView responseText = (TextView) view.findViewById(R.id.tv_response);
        NoScrollGridView gridPicView = (NoScrollGridView) view.findViewById(R.id.comment_pic_grid);
        //评价用户信息
        if(rateInfo.userInfo != null){
            if (!TextUtils.isEmpty(rateInfo.userInfo.avatar)) {
//                BaseImgView.loadimg(avatarImg,
//                        rateInfo.userInfo.avatar,
//                        R.mipmap.icon_default_avatar,
//                        R.mipmap.icon_default_avatar,
//                        R.mipmap.icon_default_avatar,
//                        ImageScaleType.EXACTLY,
//                        128,
//                        128,
//                        180);

                ImageLoadManager.loadCircleImage(CommonUtil.getImageFullUrl(rateInfo.userInfo.avatar), R.mipmap.icon_default_avatar, 128, 128, avatarImg);

            } else {
                avatarImg.setImageResource(R.mipmap.icon_default_avatar);
            }

            nickNameText.setText(rateInfo.userInfo.nickname);
        }
        //星星处理
        ratingBarView.setClickable(false);
        ratingBarView.setHalfStar(Float.parseFloat(String.valueOf(rateInfo.score / 100f)));
        //时间处理
        timeText.setText(DateUtil.getyyyymmddhhmm(rateInfo.gmtCreated));

        //内容显示
        if(StringUtil.isEmpty(rateInfo.content)){
            contentText.setVisibility(View.GONE);
        }else {
            contentText.setVisibility(View.VISIBLE);
            contentText.setText(rateInfo.content);
        }

        //店家回复
        if(StringUtil.isEmpty(rateInfo.backContent)){
            responseText.setVisibility(View.GONE);
        }else {
            responseText.setVisibility(View.VISIBLE);
            responseText.setText(rateInfo.backContent);
        }

        //图片处理
        if(rateInfo.picList != null && rateInfo.picList.size() > 0){
            gridPicView.setVisibility(View.VISIBLE);
            if(gridPicView.getAdapter() == null){
                gridPicView.setAdapter(LiveItemHelper.setPicPreviewAdapter(context));
            }
            ((QuickAdapter) gridPicView.getAdapter()).replaceAll(rateInfo.picList);

            gridPicView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(AnalyDataValue.KEY_COMMENTID, rateInfo.id + "");
                    map.put(AnalyDataValue.KEY_TYPE, AnalyDataValue.getType(mOrderType));
                    map.put(AnalyDataValue.KEY_ID, mScenicId + "");
                    TCEventHelper.onEvent(context, AnalyDataValue.COMMENTS_VIEW_PICTURES, map);

                    ArrayList<String> tmpList = new ArrayList<>();
                    for (String str : rateInfo.picList) {
                        tmpList.add(ImageUtils.getImageFullUrl(str));
                    }
                    NavUtils.gotoLookBigImage(context, tmpList, position);
                }
            });
            //gridPicView.setOnItemClickListener(new LiveItemHelper.PicGridItemClick(context, (ArrayList<String>) rateInfo.picList));
        }else {
            gridPicView.setVisibility(View.GONE);
        }
    }

    /*public static QuickAdapter<String> setPicPreviewAdapter(Context context) {
        int width = (ScreenSize.getScreenWidth(context.getApplicationContext()) - 30 * 2 - 30 * 2) / 3;
        final AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(width, width);
        QuickAdapter<String> adapter = new QuickAdapter<String>(context, R.layout.imageview, new ArrayList<String>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, String picPath) {
                ImageView imageView = helper.getView(R.id.imageview);
//					int width = ScreenSize.dp2px(context.getApplicationContext(), 100);
//					AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(width, width);
                imageView.setLayoutParams(layoutParams);
                helper.setImageUrl(R.id.imageview, picPath, 250, 250, R.mipmap.icon_default_150_150);
            }
        };
        return adapter;
    }*/
}
