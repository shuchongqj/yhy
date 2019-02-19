package com.yhy.cityselect.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.TextView;

import com.example.module_city_select.R;
import com.yhy.cityselect.entity.CityListBean;
import com.yhy.network.resp.outplace.GetOutPlaceCityListResp;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 城市列表适配器
 *
 * Created by yangboxue on 2018/7/10.
 */

public class CityListAdapter extends SectionedBaseAdapter {

    private Activity mActivity;
    private ArrayList<CityListBean> datas;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;

    public CityListAdapter(Activity activity, ArrayList<CityListBean> datas) {
        mActivity = activity;
        this.datas = datas;
        inflater = LayoutInflater.from(activity);
    }

    @Override
    public Object getItem(int section, int position) {
        return null;
    }

    @Override
    public long getItemId(int section, int position) {
        long result = 0;
        result += section;
        for (int i = 0; i < section - 1; i++) {    //   因为添加了headview，所以section - 1
            result += getCountForSection(i);
        }
        result += position;
        return result;
    }

    @Override
    public int getSectionCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public int getCountForSection(int section) {
        return datas.get(section).values.size();
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        final GetOutPlaceCityListResp.OutPlaceCity item = datas.get(section).values.get(position);

        LVHolder holder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.city_list_item, parent, false);
            holder = new LVHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (LVHolder) convertView.getTag();
        }

        holder.setText(R.id.tv_city_name, item.name);
        holder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(item);
            }
        });
        holder.setVisibile(R.id.view_line, position == datas.get(section).values.size() -1 ? false : true);

        return convertView;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.city_list_title_item, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.tv_title)).setText(datas.get(section).index);

        return convertView;
    }

    public static class LVHolder {
        public HashMap<Integer, View> mViewMaps;
        private View mItemView;

        public LVHolder(View itemView) {
            mViewMaps = new HashMap<>();
            this.mItemView = itemView;
        }

        /**
         * 代替findViewById
         *
         * @param id
         * @param <T>
         * @return
         */
        public <T extends View> T getView(int id) {
            View view = mViewMaps.get(id);
            if (view == null) {
                view = mItemView.findViewById(id);
                mViewMaps.put(id, view);
            }
            return (T) view;
        }

        public View setCheckStatus(int id, boolean b) {
            View view = getView(id);
            ((Checkable) view).setChecked(b);
            return view;
        }

        public void setText(int id, String txt) {
            ((TextView) getView(id)).setText(txt);
        }

        public void setVisibile(int id, boolean visible) {
            (getView(id)).setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }

        public <T extends View> T getItemView() {
            return (T) mItemView;
        }

//        public int getPosition() {
//            return (int) mItemView.getTag(R.id.tag_for_position);
//        }
    }

    public interface OnItemClickListener{
        void onItemClick(GetOutPlaceCityListResp.OutPlaceCity outPlaceCity);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
