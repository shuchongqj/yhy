package com.newyhy.views;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.yhy.R;
import com.quanyan.yhy.libanalysis.AnArgs;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.yhy.common.beans.net.model.RCShowcase;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;

/**
 * 运动页面快速咨询，积分商城，俱乐部等后台可配置入口
 * Created by yangboxue on 2018/6/1.
 */

public class CenterBoothView extends LinearLayout {

    private Activity mActivity;
    private ImageView ivIcon;
    private TextView tvTitle;

    public CenterBoothView(Activity context, RCShowcase showcase) {
        super(context);
        mActivity = context;
        init(context, showcase);
    }

    /**
     * 初始化
     *
     * @param context mContext
     */
    private void init(final Context context, final RCShowcase showcase) {
        View view = LayoutInflater.from(context).inflate(R.layout.center_booth_view, this);
        ivIcon = view.findViewById(R.id.iv_booth);
        tvTitle = view.findViewById(R.id.tv_title);

        if (showcase == null) return;

        // 文本
        tvTitle.setText(showcase.title);
        // 图标
        if (!TextUtils.isEmpty(showcase.imgUrl))
            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(showcase.imgUrl), ivIcon);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showcase.isMore) {
                    NavUtils.startWebview(mActivity, "", SPUtils.getURL_SPORT_YYW_MORE(mActivity), -1);
                } else {
                    Analysis.pushEvent(context,AnEvent.SYMBOL_ACTIVITY_RELEASED,String.valueOf(showcase.id),
                            AnArgs.Instance().build(AnArgs.TITLE,showcase.title).getMap());
                    NavUtils.depatchAllJump(mActivity, showcase, -1);
                }

            }
        });
    }
}
