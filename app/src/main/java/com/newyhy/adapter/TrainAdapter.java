package com.newyhy.adapter;

import android.app.Activity;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.quanyan.yhy.R;
import com.smart.sdk.api.resp.Api_RESOURCECENTER_RcmdTrainDto;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;

import java.util.List;

/**
 * 驻场培训机构适配器
 *
 * Created by yangboxue on 2018/6/13.
 */

public class TrainAdapter extends BaseQuickAdapter<Api_RESOURCECENTER_RcmdTrainDto, BaseViewHolder> {

    private Activity mActivity;

    public TrainAdapter(Activity activity, List<Api_RESOURCECENTER_RcmdTrainDto> data) {
        super(R.layout.train_list_item, data);
        mActivity = activity;
    }

    @Override
    protected void convert(BaseViewHolder holder, Api_RESOURCECENTER_RcmdTrainDto trainDto) {
        //  封面
        ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(trainDto.frontCover), holder.getView(R.id.iv_train_cover));
        // 名字
        holder.setText(R.id.tv_trai_name, trainDto.name);
    }
}
