package com.quanyan.yhy.ui.comment.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.quanyan.yhy.ui.comment.CommentBean;

import java.util.List;

/**
 * Created with Android Studio.
 * Title:CommentListAdapter
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-3-5
 * Time:15:49
 * Version 1.0
 */

public class CommentListAdapter extends BaseAdapter {

    private Context mContext;
    private List<CommentBean> mCommentBeans;

    public CommentListAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
