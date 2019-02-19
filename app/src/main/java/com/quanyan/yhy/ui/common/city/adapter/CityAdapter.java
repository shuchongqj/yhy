package com.quanyan.yhy.ui.common.city.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.yhy.common.beans.city.bean.AddressBean;

import java.util.List;
public class CityAdapter extends BaseAdapter {
    private List<AddressBean> datas;
    public CityAdapter(List<AddressBean> addressBeans){
        this.datas=addressBeans;
    }

    public void updateListView(List<AddressBean> addressBeans){
        datas.clear();
        datas.addAll(addressBeans);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if(convertView==null){
            view= View.inflate(parent.getContext(), R.layout.item_list_address, null);
        }else{
            view=convertView;
        }

        AddressHolder holder= AddressHolder.getHolder(view);
        AddressBean addressBean = datas.get(position);
        String currentIndexStr = "";
        if(!StringUtil.isEmpty(addressBean.getPinyin())) {
            currentIndexStr = String.valueOf(addressBean.getPinyin().charAt(0));
        }
        String indexStr = null;
        if(position == 0){
            //第一条目显示
            indexStr=currentIndexStr;
        }else{
            // 判断当前首字母和上一个首字母是否一致
            String lastIndexStr = "";
            if(datas.get(position - 1).getPinyin() != null){
                lastIndexStr = String.valueOf(datas.get(position - 1).getPinyin().charAt(0));
            }
            if(!TextUtils.equals(lastIndexStr,currentIndexStr)){
                indexStr=currentIndexStr;
            }
        }

        holder.tv_index.setVisibility(indexStr==null ? View.GONE : View.VISIBLE);
        holder.tv_index.setText(indexStr);
        holder.tv_name.setText(addressBean.getName());
        return view;
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    static class AddressHolder{
        TextView tv_index;
        TextView tv_name;
        public AddressHolder(View convertView){
            tv_index= (TextView) convertView.findViewById(R.id.tv_index);
            tv_name= (TextView) convertView.findViewById(R.id.tv_name);

        }
        public static AddressHolder getHolder(View convertView){
            AddressHolder holder= (AddressHolder) convertView.getTag();
            if(holder==null){
                holder=new AddressHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }
}
