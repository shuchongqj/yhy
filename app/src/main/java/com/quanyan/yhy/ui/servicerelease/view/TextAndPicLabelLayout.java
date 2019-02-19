package com.quanyan.yhy.ui.servicerelease.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.quanyan.yhy.R;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

/**
 * Created with Android Studio.
 * Title:TextAndPicLabelLayout
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-7-9
 * Time:14:05
 * Version 1.1.0
 */

public class TextAndPicLabelLayout extends LinearLayout {

    private EditText mEditDesc;
    private ImageView mImageDesc;

    public TextAndPicLabelLayout(Context context) {
        super(context);
        init(context);
    }

    public TextAndPicLabelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextAndPicLabelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = View.inflate(context, R.layout.label_text_picture, null);
        mEditDesc = (EditText) view.findViewById(R.id.et_desc);
        mImageDesc = (ImageView) view.findViewById(R.id.iv_img_desc);
        addView(view);
    }

    public void setImgDesc(String path){
        if(mImageDesc != null){
//            ImageLoaderUtil.loadLocalImage(mImageDesc, path);
            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(path), mImageDesc);
        }

    }

    public String getEditDesc(){
        if(mEditDesc == null){
            return null;
        }
        return mEditDesc.getText().toString().trim();
    }

    public EditText getViewEdit(){
        return mEditDesc;
    }

    public ImageView getViewImg(){
        return mImageDesc;
    }

    public void removeEditChild(){
        removeView(mEditDesc);
    }

    public void removeImgChild(){
        removeView(mImageDesc);
    }

    public int obtainChildCount(){
        return getChildCount();
    }


}
