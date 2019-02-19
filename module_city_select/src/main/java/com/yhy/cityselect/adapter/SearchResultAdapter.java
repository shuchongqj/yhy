package com.yhy.cityselect.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Filter;
import android.widget.Filterable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.module_city_select.R;
import com.yhy.cityselect.CitySelectActivity;
import com.yhy.cityselect.util.TextPinyinUtil;
import com.yhy.network.resp.outplace.GetOutPlaceCityListResp;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索结果列表适配器
 * <p>
 * Created by yangboxue on 2018/7/13.
 */

public class SearchResultAdapter extends BaseQuickAdapter<GetOutPlaceCityListResp.OutPlaceCity, BaseViewHolder> implements Filterable {

    private Activity activity;
    private ArrayList<GetOutPlaceCityListResp.OutPlaceCity> originData;

    public SearchResultAdapter(Activity activity, ArrayList<GetOutPlaceCityListResp.OutPlaceCity> originData, List<GetOutPlaceCityListResp.OutPlaceCity> data) {
        super(R.layout.city_list_item, data);
        this.activity = activity;
        this.originData = originData;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    @Override
    protected void convert(BaseViewHolder holder, GetOutPlaceCityListResp.OutPlaceCity outPlaceCity) {
        holder.setText(R.id.tv_city_name, outPlaceCity.name);
    }


    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults filterResults = new FilterResults();

            if (originData == null) {
                return filterResults;
            }

            if (prefix == null || prefix.length() == 0) {
                filterResults.values = new ArrayList<>();
                filterResults.count = 0;
            } else {
                String prefixString = prefix.toString().toLowerCase();
                ArrayList<GetOutPlaceCityListResp.OutPlaceCity> values = new ArrayList<>();
                int count = originData.size();
                for (int i = 0; i < count; i++) {
                    GetOutPlaceCityListResp.OutPlaceCity outPlaceCity = originData.get(i);

                    if (TextPinyinUtil.isChinaString(prefixString)) {   // 是汉字
                        if (outPlaceCity.name.startsWith(prefixString)) {
                            values.add(outPlaceCity);
                        }
                    } else {
                        String namePinyin = TextPinyinUtil.getInstance().getPinyin(outPlaceCity.name);
                        String prefixPinyin = TextPinyinUtil.getInstance().getPinyin(prefixString);
                        if (namePinyin.startsWith(prefixPinyin)) {
                            values.add(outPlaceCity);
                        } else if (!TextUtils.isEmpty(outPlaceCity.simpleCode)) {
                            String simpleCode = outPlaceCity.simpleCode.toLowerCase();
                            if (simpleCode.startsWith(prefixPinyin)) {
                                values.add(outPlaceCity);
                            }
                        }
                    }

                }

                filterResults.values = values;
                filterResults.count = values.size();
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            setNewData((ArrayList<GetOutPlaceCityListResp.OutPlaceCity>) results.values);
            ((CitySelectActivity)activity).setSearchResultView((ArrayList<GetOutPlaceCityListResp.OutPlaceCity>) results.values);
        }
    };
}
