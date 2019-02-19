package com.quanyan.yhy.ui.lineabroad.bean;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.customview.NoScrollGridView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.eventbus.EvBusDestCityChoose;
import com.quanyan.yhy.ui.adapter.base.BaseAdapterHelper;
import com.quanyan.yhy.ui.adapter.base.QuickAdapter;
import com.yhy.common.beans.net.model.user.Destination;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created with Android Studio.
 * Title:DestAbroadHelper
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-6-2
 * Time:16:06
 * Version 1.1.0
 */

public class DestAbroadHelper {

    public static void handler(Activity activity, BaseAdapterHelper helper, AbroadResultBean item) {
        helper.setText(R.id.tv_word_title, item.getIndex());
        List<AbroadAreaBean> lists = item.getLists();
        LinearLayout llCityAll = helper.getView(R.id.ll_city_all);

        llCityAll.removeAllViews();
        if (lists != null && lists.size() > 0) {
            for (int i = 0; i < lists.size(); i++) {
                final AbroadAreaBean aab = lists.get(i);
                View view = View.inflate(activity, R.layout.view_city_abroad_containr, null);
                TextView cityName = (TextView) view.findViewById(R.id.tv_city_name);
                NoScrollGridView gridScenicName = (NoScrollGridView) view.findViewById(R.id.grid_scenic_name);
                if (lists.get(i).getDestination() != null && !StringUtil.isEmpty(lists.get(i).getDestination().name)) {
                    cityName.setText(lists.get(i).getDestination().name);
                }
                cityName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(new EvBusDestCityChoose(String.valueOf(aab.getDestination().code),aab.getDestination().name));
                    }
                });
                List<Destination> childDestinations = lists.get(i).getChildDestinations();
                //目的地景区名
                if (childDestinations != null && childDestinations.size() > 0) {
                    gridScenicName.setVisibility(View.VISIBLE);
                    final QuickAdapter<Destination> adapter = new QuickAdapter<Destination>(activity, R.layout.item_abroad_dest, childDestinations) {
                        @Override
                        protected void convert(BaseAdapterHelper helper, Destination item) {
                            helper.setText(R.id.tv_abroad_name, item.name);
                        }
                    };
                    gridScenicName.setAdapter(adapter);

                    gridScenicName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            EventBus.getDefault().post(new EvBusDestCityChoose(String.valueOf(adapter.getItem(position).code),adapter.getItem(position).name));
                        }
                    });
                }else {
                    gridScenicName.setVisibility(View.GONE);
                }
                llCityAll.addView(view);
            }
        }

    }

    public static void handlerReleaseDest(final Activity activity, BaseAdapterHelper helper, AbroadResultBean item, final List<Destination> mSelectList){
        helper.setText(R.id.tv_word_title, item.getIndex());
        List<AbroadAreaBean> lists = item.getLists();
        LinearLayout llCityAll = helper.getView(R.id.ll_city_all);

        llCityAll.removeAllViews();
        if (lists != null && lists.size() > 0) {
            for (int i = 0; i < lists.size(); i++) {
                final AbroadAreaBean aab = lists.get(i);
                View view = View.inflate(activity, R.layout.view_city_abroad_containr, null);
                TextView cityName = (TextView) view.findViewById(R.id.tv_city_name);
                NoScrollGridView gridScenicName = (NoScrollGridView) view.findViewById(R.id.grid_scenic_name);
                if (lists.get(i).getDestination() != null && !StringUtil.isEmpty(lists.get(i).getDestination().name)) {
                    cityName.setText(lists.get(i).getDestination().name);
                }
                cityName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //EventBus.getDefault().post(new EvBusDestCityChoose(String.valueOf(aab.getDestination().code),aab.getDestination().name));
                    }
                });
                List<Destination> childDestinations = lists.get(i).getChildDestinations();
                //目的地景区名
                if (childDestinations != null && childDestinations.size() > 0) {
                    gridScenicName.setVisibility(View.VISIBLE);
                    final QuickAdapter<Destination> adapter = new QuickAdapter<Destination>(activity, R.layout.item_abroad_dest, childDestinations) {
                        @Override
                        protected void convert(BaseAdapterHelper helper, Destination item) {
                            helper.setText(R.id.tv_abroad_name, item.name);
                            helper.setTextColor(R.id.tv_abroad_name, activity.getResources().getColor(R.color.neu_666666));
                            helper.setBackgroundRes(R.id.tv_abroad_name, R.drawable.dest_abroad_bg);
                            if(mSelectList != null && mSelectList.size() > 0){
                                for (int i = 0; i < mSelectList.size(); i++) {
                                    if(item.name.equals(mSelectList.get(i).name)){
                                        helper.setTextColor(R.id.tv_abroad_name, activity.getResources().getColor(R.color.main));
                                        helper.setBackgroundRes(R.id.tv_abroad_name, R.drawable.shape_dest_select_city_bg);
                                    }
                                }
                            }
                        }
                    };
                    gridScenicName.setAdapter(adapter);

                    gridScenicName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //EventBus.getDefault().post(new EvBusDestCityChoose(String.valueOf(adapter.getItem(position).code),adapter.getItem(position).name));
                            mOnCityClickListener.onCityClick(view, adapter.getItem(position));
                        }
                    });
                }else {
                    gridScenicName.setVisibility(View.GONE);
                }
                llCityAll.addView(view);
            }
        }
    }

    static OnCityClickListener mOnCityClickListener;

    public static void setOnCityClickListener(OnCityClickListener lsn) {
        mOnCityClickListener = lsn;
    }

    public interface OnCityClickListener {
        void onCityClick(View view, Destination info);
    }
}
