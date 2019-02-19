package com.newyhy.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quanyan.yhy.R;
import com.smart.sdk.api.resp.Api_SNSCENTER_GuideTagResult;
import com.smart.sdk.api.resp.Api_SNSCENTER_TagResult;

import java.util.ArrayList;
import java.util.List;

public class InterestGridAdapter extends RecyclerView.Adapter<InterestGridAdapter.MyViewHolder> {
    private List<Api_SNSCENTER_GuideTagResult> list;

    private String[] defaultColorArray = {"#F1FBFF", "#FFF7EE", "#FFFDEC", "#F7FFED","#FFFDEC", "#F1FBFF","#FFF7EE", "#F7FFED","#FFF7EE","#FFFDEC", "#F1FBFF", "#F7FFED", "#F7FFED","#FFF7EE", "#FFFDEC", "#F1FBFF"};

    private List<Api_SNSCENTER_GuideTagResult> selectedList = new ArrayList();

    public InterestGridAdapter(List<Api_SNSCENTER_GuideTagResult> list) {
        this.list = list;
    }

    public List<Api_SNSCENTER_GuideTagResult> getSelectedList() {
        return selectedList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.include_dialog_interest_item, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setPosition(position);
        holder.selected = false;
        holder.tvText.setText(list.get(position).description);
        if (selectedList.contains(list.get(position))) {
            //选中
            holder.tvText.setBackgroundResource(R.mipmap.ic_background_interest_item_selected);
            holder.tvText.setTextColor(Color.parseColor("#000000"));
        } else {
            //未选中
            holder.tvText.setTextColor(Color.parseColor("#4A4A4A"));
            holder.tvText.setBackgroundColor(Color.parseColor(defaultColorArray[position % 16]));//防止数组越界
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View backgroundView;
        TextView tvText;
        private int position;
        private boolean selected;

        public void setPosition(int position) {
            this.position = position;
        }

        public MyViewHolder(View itemView) {
            super(itemView);
            backgroundView = itemView.findViewById(R.id.dialog_interest_item_background);
            tvText = itemView.findViewById(R.id.dialog_interest_item_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (!selected) {
                selected = true;
                tvText.setBackgroundResource(R.mipmap.ic_background_interest_item_selected);
                tvText.setTextColor(Color.parseColor("#000000"));
                selectedList.add(list.get(position));
            } else {
                selected = false;
                tvText.setTextColor(Color.parseColor("#4A4A4A"));
                tvText.setBackgroundColor(Color.parseColor(defaultColorArray[position % 16]));
                selectedList.remove(list.get(position));
            }

        }
    }
}
