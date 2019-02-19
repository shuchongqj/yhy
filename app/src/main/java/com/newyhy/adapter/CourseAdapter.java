package com.newyhy.adapter;

import android.app.Activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_RcmdCourseDto;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 推荐课程适配器
 * <p>
 * Created by yangboxue on 2018/6/12.
 */

public class CourseAdapter extends BaseQuickAdapter<Api_RESOURCECENTER_RcmdCourseDto, BaseViewHolder> {

    private Activity mActivity;

    public CourseAdapter(Activity activity, List<Api_RESOURCECENTER_RcmdCourseDto> data) {
        super(R.layout.course_list_item, data);
        mActivity = activity;
    }

    @Override
    protected void convert(BaseViewHolder holder, Api_RESOURCECENTER_RcmdCourseDto courseDto) {
        // 分割线
        holder.setGone(R.id.view_line, holder.getAdapterPosition() == 0 ? false : true);
        // 封面
        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(courseDto.cover), holder.getView(R.id.iv_course_cover), 2, true);
        // 课程名字
        holder.setText(R.id.tv_course_name, courseDto.name);
        // 课程地点
        double distance = courseDto.distance;
        if (distance * 10 < 1 && distance > 0) {      // 小于100  显示0.1
            holder.setText(R.id.tv_course_place, courseDto.placeName + " | " + 0.1 + "km");
        } else {
            holder.setText(R.id.tv_course_place, courseDto.placeName + " | " + new DecimalFormat("#########0.0")
                    .format(distance) + "km");
        }
        // 课程类型
        holder.setText(R.id.tv_course_desc, courseDto.classTimes + "节 · " + courseDto.labelName);
        // 课程价格
        holder.setText(R.id.tv_course_price, "¥" + StringUtil.convertPriceNoSymbolExceptLastZero(courseDto.price));

    }
}
