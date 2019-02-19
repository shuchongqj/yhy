package com.quanyan.yhy.ui.line.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanyan.base.util.ScreenSize;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.quanyan.yhy.ui.line.lineinterface.DropMenuInterface;
import com.yhy.common.beans.net.model.master.QueryTerm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/3/4
 * Time:15:12
 * Version 1.0
 */
public class TabPopView extends LinearLayout {

    private ListView mListViewFist;
    private ListView mListViewSecond;
    private View mView1;
    private View mView2;

    private QuickAdapter<QueryTerm> mAdapterFist;
    private QuickAdapter<QueryTerm> mAdapterSecond;
    private QuickAdapter<QueryTerm> mAdapterSecondCheckBox;

    private int mPaddLR;
    private int mPaddTB;

    private int mCurrentSelectedIndexFirst = -1;
    private int mCurrentSelectedIndexSecond = -1;

    public TabPopView(Context context) {
        super(context);
        init(context, null);
    }

    public TabPopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TabPopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TabPopView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * 高亮选中的文案
     */
    private String mFocusTag;

    public void focusChooseText(String tag) {
        mFocusTag = tag;
        mAdapterFist.notifyDataSetChanged();
    }

    MultiselectAdapter mMultiselectAdapter;

    private void init(Context context, AttributeSet attributeSet) {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        setOrientation(HORIZONTAL);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ScreenSize.getScreenHeightExcludeStatusBar(getContext().getApplicationContext()) / 3));

        LinearLayout.LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LayoutParams(ScreenSize.convertDIP2PX(context.getApplicationContext(), 1),
                ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setBackgroundColor(getResources().getColor(R.color.neu_f4f4f4));

        mView1 = View.inflate(context, R.layout.base_listview, null);
        mView2 = View.inflate(context, R.layout.base_listview, null);

        mListViewFist = (ListView) mView1.findViewById(R.id.base_listview);
        mListViewSecond = (ListView) mView2.findViewById(R.id.base_listview);

        mView1.setLayoutParams(layoutParams);
        mView2.setLayoutParams(layoutParams);

        addView(mView1);
//        addView(linearLayout);
        addView(mView2);

//        View.inflate(context, R.layout.base_listview, this);
        mPaddLR = ScreenSize.convertDIP2PX(context.getApplicationContext(), 15);
        mPaddTB = ScreenSize.convertDIP2PX(context.getApplicationContext(), 10);


        mView2.setVisibility(View.GONE);

        mAdapterFist = new QuickAdapter<QueryTerm>(context, R.layout.item_subject_list, new ArrayList<QueryTerm>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, QueryTerm item) {
                TextView textView = (TextView)helper.getView(R.id.tv_subject);
                RelativeLayout rlText = (RelativeLayout)helper.getView(R.id.rl_text);
                textView.setText(item.text);
                if (StringUtil.isEmpty(mCurrentSelectedIndexFirstValue)) {
                    if ((-1 == mCurrentSelectedIndexFirst && 0 == helper.getPosition()) ||
                            (mCurrentSelectedIndexFirst == helper.getPosition())) {
                        textView.setTextColor(getResources().getColor(R.color.main));
                        rlText.setBackgroundColor(getResources().getColor(R.color.neu_f4f4f4));
                    } else {
                        rlText.setBackgroundColor(Color.WHITE);
                        textView.setTextColor(getResources().getColor(R.color.neu_666666));
                    }
                }else{
                    if (!StringUtil.isEmpty(item.text)&&mCurrentSelectedIndexFirstValue.equals(item.text)) {
                        textView.setTextColor(getResources().getColor(R.color.main));
                        rlText.setBackgroundColor(getResources().getColor(R.color.neu_f4f4f4));
                    } else {
                        rlText.setBackgroundColor(Color.WHITE);
                        textView.setTextColor(getResources().getColor(R.color.neu_666666));
                    }
                }
            }
        };

        mAdapterSecond = new QuickAdapter<QueryTerm>(context, R.layout.item_subject_list, new ArrayList<QueryTerm>()) {
            @Override
            protected void convert(BaseAdapterHelper helper, QueryTerm item) {
                TextView textView = helper.getView(R.id.tv_subject);
                textView.setText(item.text);
//                textView.setBackgroundColor(getResources().getColor(R.color.neu_f4f4f4));
                if ((mCurrentSelectedIndexSecond == helper.getPosition())) {
                    //第一次默认选中第一个
                    textView.setTextColor(getResources().getColor(R.color.main));
                } else {
                    textView.setTextColor(getResources().getColor(R.color.neu_666666));
                }
            }
        };


        mMultiselectAdapter = new MultiselectAdapter(context);

        mListViewSecond.setDividerHeight(ScreenSize.convertDIP2PX(getContext().getApplicationContext(),
                1));
        mListViewFist.setAdapter(mAdapterFist);

        mListViewFist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentSelectedIndexFirst = position;
                mCurrentSelectedIndexFirstValue=null;
                List<QueryTerm> queryTerms;
                if (isMultiselect) {
                    mAdapterFist.notifyDataSetChanged();
                    queryTerms = mAdapterFist.getItem(position).queryTermList;
                    mCurrentSelectedIndexSecond = position;
                } else {
                    mAdapterFist.notifyDataSetChanged();
                    queryTerms = mAdapterFist.getItem(position).queryTermList;
                    mCurrentSelectedIndexSecond = -1;
                }

                if (queryTerms != null && queryTerms.size() > 0) {
                    if (mDropMenuInterface != null) {
                        if (isMultiselect) {
                            mDropMenuInterface.onFirstItemSelect(mAdapterFist.getItem(position));
                            binSecondViewDataMultiselect(queryTerms, -1, mCurrentSelectedIndexSecond);
                        } else {
                            mDropMenuInterface.onFirstItemSelect(mAdapterFist.getItem(position));
                            binSecondViewData(queryTerms, -1);
                        }
                    }
                } else {
                    if (View.VISIBLE == mView2.getVisibility()) {
                        mView2.setVisibility(View.GONE);
                        if (isMultiselect) {

                            mMultiselectAdapter.clear();


                        } else {
                            mAdapterSecond.clear();
                        }

                    }
                    if (mDropMenuInterface != null) {
                        if (isMultiselect) {
                            mDropMenuInterface.onFirstItemSelect(mMultiselectAdapter.getItem(position));
                        } else {
                            mDropMenuInterface.onFirstItemSelect(mAdapterFist.getItem(position));
                        }

                    }
                }
            }
        });

        mListViewSecond.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentSelectedIndexSecond = position;
                mAdapterSecond.notifyDataSetChanged();

                if (mDropMenuInterface != null) {
                    if (isMultiselect) {
                        mDropMenuInterface.onSecondItemSelect(mMultiselectAdapter.getItem(position));
                    } else {
                        mDropMenuInterface.onSecondItemSelect(mAdapterSecond.getItem(position));
                    }
                }
            }
        });
    }

    public void setCurrentSelectedIndexFirst(int currentSelectedIndexFirst,String v) {
        mCurrentSelectedIndexFirst = currentSelectedIndexFirst;
        mCurrentSelectedIndexFirstValue=v;
        mAdapterFist.notifyDataSetChanged();
    }

    public void setCurrentSelectedIndexSecond(int currentSelectedIndexSecond) {
        mCurrentSelectedIndexSecond = currentSelectedIndexSecond;
    }

    /**
     * 手动触发二级菜单的点击事件
     * @param position
     */
    public void performSencondItemClick(int position){
        mCurrentSelectedIndexSecond = position;
        mAdapterSecond.notifyDataSetChanged();

        if (mDropMenuInterface != null) {
            if (isMultiselect) {
                mDropMenuInterface.onSecondItemSelect(mMultiselectAdapter.getItem(position));
            } else {
                mDropMenuInterface.onSecondItemSelect(mAdapterSecond.getItem(position));
            }
        }
    }

    /**
     * 绑定数据元，二级列表
     *
     * @param datas
     * @param tagId
     */
    private void binSecondViewData(List<QueryTerm> datas, long tagId) {
        if (isMultiselect) {
            mListViewSecond.setAdapter(mMultiselectAdapter);
        } else {
            mListViewSecond.setAdapter(mAdapterSecond);
        }

        if (View.GONE == mView2.getVisibility()) {
            mView2.setVisibility(View.VISIBLE);
        }
        if (datas != null) {
            if (tagId > 0) {
                for (int index = 0; index < datas.size(); index++) {
                    long themeID;
                    try {
                        themeID = Long.parseLong(datas.get(index).value);
                    } catch (Exception e) {
                        continue;
                    }
                    if (tagId == themeID) {
                        mCurrentSelectedIndexSecond = index;
                        break;
                    } else {
                        continue;
                    }
                }
            }

            mAdapterSecond.replaceAll(datas);
        }
    }
    /**
     * 绑定数据元，二级列表
     *
     * @param datas
     * @param tagId
     */
    private void binSecondViewData(List<QueryTerm> datas, String tagId) {
        if (isMultiselect) {
            mListViewSecond.setAdapter(mMultiselectAdapter);
        } else {
            mListViewSecond.setAdapter(mAdapterSecond);
        }
        if (View.GONE == mView2.getVisibility()) {
            mView2.setVisibility(View.VISIBLE);
        }
        if (datas != null) {
            if (!StringUtil.isEmpty(tagId)) {
                for (int index = 0; index < datas.size(); index++) {
                    if (tagId.equals(datas.get(index).value)) {
                        mCurrentSelectedIndexSecond = index;
                        break;
                    } else {
                        continue;
                    }
                }
            }

            mAdapterSecond.replaceAll(datas);
        }
    }

    /**
     * 绑定水源一级列表
     *
     * @param datas
     * @param tagId
     */
    public void bindViewData(List<QueryTerm> datas, long tagId) {
        if (datas != null) {
            if (tagId > 0) {
                for (int index = 0; index < datas.size(); index++) {
                    long themeID;
                    try {
                        themeID = Long.parseLong(datas.get(index).value);
                    } catch (Exception e) {
                        continue;
                    }
                    if (tagId == themeID) {
                        mCurrentSelectedIndexFirst = index;
                        break;
                    } else {
                        continue;
                    }
                }
            }
            mAdapterFist.replaceAll(datas);
            //TODO滑动到选中态
            mListViewFist.setSelection(mCurrentSelectedIndexFirst);
            if (datas.size() > 0) {
                List<QueryTerm> queryTerms = datas.get(0).queryTermList;
                if (queryTerms != null && queryTerms.size() > 0) {
                    mCurrentSelectedIndexSecond = 0;
                    mListViewFist.setDivider(new ColorDrawable(Color.WHITE));
                    mListViewFist.setDividerHeight(ScreenSize.convertDIP2PX(getContext().getApplicationContext(), 1));
                    binSecondViewData(queryTerms, tagId);
                }
            }
        }
    }

    /**
     * 绑定水源一级列表
     *
     * @param datas
     * @param tagId
     */
    private String mCurrentSelectedIndexFirstValue;

    public void bindViewData(List<QueryTerm> datas, long tagId, String tagValue) {
        if (datas != null) {
            if (tagId > 0) {
                for (int index = 0; index < datas.size(); index++) {
                    long themeID;
                    try {
                        themeID = Long.parseLong(datas.get(index).value);
                    } catch (Exception e) {
                        continue;
                    }
                    if (tagId == themeID) {
                        mCurrentSelectedIndexFirst = index;
                        break;
                    } else {
                        continue;
                    }
                }
            }

            mCurrentSelectedIndexFirstValue = tagValue;
            mAdapterFist.replaceAll(datas);
            //TODO滑动到选中态
            mListViewFist.setSelection(mCurrentSelectedIndexFirst);
            if (datas.size() > 0) {
                List<QueryTerm> queryTerms = datas.get(0).queryTermList;
                if (queryTerms != null && queryTerms.size() > 0) {
                    mCurrentSelectedIndexSecond = 0;
                    mListViewFist.setDivider(new ColorDrawable(Color.WHITE));
                    mListViewFist.setDividerHeight(ScreenSize.convertDIP2PX(getContext().getApplicationContext(), 1));
                    binSecondViewData(queryTerms, tagId);
                }
            }
        }
    }

    /**
     * 初始化主题显示
     * @param datas
     * @param tagId
     * @param tagValue
     */
    public void bindViewData(List<QueryTerm> datas, String tagId, String tagValue) {
        if (datas != null) {
            if (!StringUtil.isEmpty(tagId)) {
                for (int index = 0; index < datas.size(); index++) {
                    if (tagId.equals(datas.get(index).value)) {
                        mCurrentSelectedIndexFirst = index;
                        break;
                    } else {
                        continue;
                    }
                }
            }

            mCurrentSelectedIndexFirstValue = tagValue;
            mAdapterFist.replaceAll(datas);
            //TODO滑动到选中态
            mListViewFist.setSelection(mCurrentSelectedIndexFirst);
            if (datas.size() > 0) {
                List<QueryTerm> queryTerms = datas.get(0).queryTermList;
                if (queryTerms != null && queryTerms.size() > 0) {
                    mCurrentSelectedIndexSecond = 0;
                    mListViewFist.setDivider(new ColorDrawable(Color.WHITE));
                    mListViewFist.setDividerHeight(ScreenSize.convertDIP2PX(getContext().getApplicationContext(), 1));
                    binSecondViewData(queryTerms, tagId);
                }
            }
        }
    }

    /**
     * 绑定水源一级列表二级含有CheckBox多选
     *
     * @param datas
     * @param tagId
     */
    boolean isMultiselect = false;

    public void bindViewDataMultiselect(List<QueryTerm> datas, long tagId) {
        isMultiselect = true;
        if (isMultiselect) {
            mListViewSecond.setAdapter(mMultiselectAdapter);
        } else {
            mListViewSecond.setAdapter(mAdapterSecond);
        }
        if (datas != null) {
            if (tagId > 0) {
                for (int index = 0; index < datas.size(); index++) {
                    long themeID;
                    try {
                        themeID = Long.parseLong(datas.get(index).value);
                    } catch (Exception e) {
                        continue;
                    }
                    if (tagId == themeID) {
                        mCurrentSelectedIndexFirst = index;
                        break;
                    } else {
                        continue;
                    }
                }
            }


            mAdapterFist.replaceAll(datas);
            if (datas.size() > 0) {
                List<QueryTerm> queryTerms = datas.get(0).queryTermList;
                if (queryTerms != null && queryTerms.size() > 0) {
                    if (View.GONE == mView2.getVisibility()) {
                        mView2.setVisibility(View.VISIBLE);
                    }
                    mListViewFist.setDivider(new ColorDrawable(Color.WHITE));
                    mListViewFist.setDividerHeight(ScreenSize.convertDIP2PX(getContext().getApplicationContext(), 1));
                    mMultiselectAdapter.setInitialization(queryTerms, datas.size());
                }
            }
        }
    }


    /**
     * 绑定数据元，二级列表含有CheckBox多选
     *
     * @param datas
     * @param tagId
     */
    private void binSecondViewDataMultiselect(List<QueryTerm> datas, int tagId, int group) {
        if (View.GONE == mView2.getVisibility()) {
            mView2.setVisibility(View.VISIBLE);
        }

        if (datas != null) {
            if (tagId > 0) {
                for (int index = 0; index < datas.size(); index++) {
                    long themeID;
                    try {
                        themeID = Long.parseLong(datas.get(index).value);
                    } catch (Exception e) {
                        continue;
                    }
                    if (tagId == themeID) {
                        mCurrentSelectedIndexSecond = index;
                        break;
                    } else {
                        continue;
                    }
                }
            }
            mMultiselectAdapter.setBindingdata(datas, group);
        }
    }

//    public void setOnItemClickListener(AbsListView.OnItemClickListener onItemClickListener) {
//        mListViewFist.setOnItemClickListener(onItemClickListener);
//    }

    private DropMenuInterface mDropMenuInterface;

    /**
     * 列表项点击选择接口，为外部提供数据
     *
     * @param onItemClickInterface
     */
    public void setOnItemClickInterface(DropMenuInterface onItemClickInterface) {
        mDropMenuInterface = onItemClickInterface;
    }

    HashMap<Integer, ArrayList<Boolean>> mCheckedList = new HashMap<Integer, ArrayList<Boolean>>();

    /**
     * 自定义ListView适配器
     */
    class MultiselectAdapter extends BaseAdapter {
        /**
         * 标记CheckBox是否被选中
         **/

        /**
         * 存放要显示的Item数据
         **/
        List<QueryTerm> listPerson;
        /**
         * 一个HashMap对象
         **/
        HashMap<Integer, View> map = new HashMap<Integer, View>();
        Context context;
        ArrayList<Boolean> mChecked;

        public MultiselectAdapter(Context context) {
            this.context = context;
            listPerson = new ArrayList<QueryTerm>();
            mChecked = new ArrayList<Boolean>();
        }

        public void setBindingdata(List<QueryTerm> list, int tag) {

            listPerson = list;
            mChecked = mCheckedList.get(tag);
            notifyDataSetChanged();
        }

        public void clear() {

            listPerson.clear();
            mChecked.clear();
            notifyDataSetChanged();
        }

        /***
         * 初始化
         *
         * @param list
         * @param group
         */
        public void setInitialization(List<QueryTerm> list, int group) {
            listPerson = list;
            for (int i = 0; i < group; i++) {// 遍历且设置CheckBox默认状态为未选中
                ArrayList mCheckeds = new ArrayList<Boolean>();
                for (int is = 0; is < list.size(); is++) {// 遍历且设置CheckBox默认状态为未选中
                    mCheckeds.add(false);
                }
                mCheckedList.put(i, mCheckeds);
            }
            mChecked = mCheckedList.get(0);
            notifyDataSetChanged();
        }

        /**
         * 获取总项数
         */
        @Override
        public int getCount() {
            return listPerson.size();
        }

        /**
         * 获取指定子项
         */
        @Override
        public Object getItem(int position) {
            return listPerson.get(position);
        }

        /**
         * 获取指定子项的ID
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * 返回一个视图对象
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder = null;

            if (map.get(position) == null) {// 根据position判断View是否为空
                LayoutInflater mInflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = mInflater.inflate(R.layout.hotel_top_listview_checkbox, null);
                // 初始化ViewHolder对象
                holder = new ViewHolder();
                holder.selected = (CheckBox) view
                        .findViewById(R.id.tabview_cb);
                holder.name = (TextView) view.findViewById(R.id.tabview_name);

                final int p = position;
                map.put(position, view);// 存储视图信息
                holder.selected.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        mChecked.set(p, cb.isChecked());// 设置CheckBox为选中状态
                    }
                });
                view.setTag(holder);
            } else {
                view = map.get(position);
                holder = (ViewHolder) view.getTag();
            }

            holder.selected.setChecked(mChecked.get(position));
            holder.name.setText(listPerson.get(position).text);

            return view;
        }

    }


    /**
     * 常量类
     */
    static class ViewHolder {
        CheckBox selected;
        TextView name;
    }
}
