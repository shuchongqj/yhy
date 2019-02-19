package com.quanyan.yhy.ui.discovery.viewhelper;

import android.app.Activity;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.yhy.common.beans.net.model.club.SnsUserInfo;
import com.yhy.common.beans.net.model.comment.SupportUserInfo;

/**
 * Created with Android Studio.
 * Title:CommentUserNameClick
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxp
 * Date:2015-12-14
 * Time:11:28
 * Version 1.0
 */
public class CommentUserNameClick extends ClickableSpan{

	private Activity mActivity;
	private long mUserId;
	private String mType;

	private SnsUserInfo mSnsUserInfo;
	private SupportUserInfo mSupportUserInfo;
	public CommentUserNameClick(Activity activity, long userId, String type){
		mActivity = activity;
		mUserId = userId;
		mType = type;
	}

	public CommentUserNameClick(Activity activity, SnsUserInfo userInfo, String type){
		mActivity = activity;
		mUserId = userInfo.userId;
		mSnsUserInfo = userInfo;
		mType = type;
	}

	public CommentUserNameClick(Activity activity, SupportUserInfo userInfo, String type){
		mActivity = activity;
		mUserId = userInfo.userId;
		mSupportUserInfo = userInfo;
		mType = type;
	}

	@Override
	public void updateDrawState(TextPaint ds) {
//		android:textColorLink = "@color/main_bg_color"
//		android:textColorHighlight="@color/link_text_material_dark"
		ds.setColor(ds.linkColor);
		ds.setUnderlineText(false);//去掉下划线
	}


	@Override
	public void onClick(View widget) {
		if (mSnsUserInfo != null) {
//			NavUtils.gotoPersonalPage(mActivity,mSnsUserInfo.userId,mSnsUserInfo.nickname,mSnsUserInfo.options);
			NavUtils.gotoMasterHomepage(mActivity, mSnsUserInfo.userId);
		} else if (mSupportUserInfo != null) {
//			NavUtils.gotoPersonalPage(mActivity,mSupportUserInfo.userId,mSupportUserInfo.nick,mSupportUserInfo.options);
			NavUtils.gotoMasterHomepage(mActivity, mSupportUserInfo.userId);
		} else {
//			NavUtils.gotoPersonalPage(mActivity,mUserId,null,0);
			NavUtils.gotoMasterHomepage(mActivity, mUserId);
		}

	}
}
