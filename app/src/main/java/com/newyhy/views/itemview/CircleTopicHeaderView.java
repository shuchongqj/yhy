package com.newyhy.views.itemview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.yhy.common.beans.net.model.discover.TopicInfoResult;
import com.yhy.common.beans.topic.Topic;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.router.YhyRouter;

import java.util.ArrayList;
import java.util.List;

/**
 * CircleDynamic 中话题的头布局
 * Created by Jiervs on 2018/6/27.
 */

public class CircleTopicHeaderView extends LinearLayout implements View.OnClickListener{

    private RelativeLayout rl_more_topic;

    private RelativeLayout rl_topic1;
    private RelativeLayout rl_topic2;
    private RelativeLayout rl_topic3;
    private RelativeLayout rl_topic4;

    private ImageView iv_1;
    private ImageView iv_2;
    private ImageView iv_3;
    private ImageView iv_4;

    private TextView tv_topic1_title;
    private TextView tv_topic2_title;
    private TextView tv_topic3_title;
    private TextView tv_topic4_title;

    private TextView tv_topic1_detail;
    private TextView tv_topic2_detail;
    private TextView tv_topic3_detail;
    private TextView tv_topic4_detail;

    //data
    private List<Topic> list = new ArrayList<>();

    public CircleTopicHeaderView(Context context) {
        super(context);
        initViews(context);
    }

    public CircleTopicHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public CircleTopicHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    public void initViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.circle_four_topic_view,this);
        rl_more_topic = findViewById(R.id.rl_more_topic);
        rl_more_topic.setOnClickListener(this);

        rl_topic1 = findViewById(R.id.rl_topic1);
        rl_topic2 = findViewById(R.id.rl_topic2);
        rl_topic3 = findViewById(R.id.rl_topic3);
        rl_topic4 = findViewById(R.id.rl_topic4);

        iv_1 = findViewById(R.id.iv_1);
        iv_2 = findViewById(R.id.iv_2);
        iv_3 = findViewById(R.id.iv_3);
        iv_4 = findViewById(R.id.iv_4);

        tv_topic1_title = findViewById(R.id.tv_topic1_title);
        tv_topic2_title = findViewById(R.id.tv_topic2_title);
        tv_topic3_title = findViewById(R.id.tv_topic3_title);
        tv_topic4_title = findViewById(R.id.tv_topic4_title);

        tv_topic1_detail = findViewById(R.id.tv_topic1_detail);
        tv_topic2_detail = findViewById(R.id.tv_topic2_detail);
        tv_topic3_detail = findViewById(R.id.tv_topic3_detail);
        tv_topic4_detail = findViewById(R.id.tv_topic4_detail);

        rl_topic1.setOnClickListener(this);
        rl_topic2.setOnClickListener(this);
        rl_topic3.setOnClickListener(this);
        rl_topic4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_more_topic:
                YhyRouter.getInstance().startHotTopicListActivity(getContext());
                break;

            case R.id.rl_topic1:
                if (list.size()>0) NavUtils.gotoNewTopicDetailsActivity(getContext(),list.get(0).getName(),list.get(0).getId());
                break;

            case R.id.rl_topic2:
                if (list.size()>1) NavUtils.gotoNewTopicDetailsActivity(getContext(),list.get(1).getName(),list.get(1).getId());
                break;

            case R.id.rl_topic3:
                if (list.size()>2) NavUtils.gotoNewTopicDetailsActivity(getContext(),list.get(2).getName(),list.get(2).getId());
                break;

            case R.id.rl_topic4:
                if (list.size()>3) NavUtils.gotoNewTopicDetailsActivity(getContext(),list.get(3).getName(),list.get(3).getId());
                break;
        }
    }

    public void setData(List<Topic> list){
        this.list = list;
        switch (list.size()) {
            case 0:
                break;
            case 1:
                ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(list.get(0).getPic()),R.mipmap.icon_default_215_215, iv_1,2,true);

                tv_topic1_title.setText(list.get(0).getName());

                tv_topic1_detail.setText(list.get(0).getDescription());
                break;
            case 2:
                ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(list.get(0).getPic()), R.mipmap.icon_default_215_215,iv_1,2,true);
                ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(list.get(1).getPic()), R.mipmap.icon_default_215_215,iv_2,2,true);

                tv_topic1_title.setText(list.get(0).getName());
                tv_topic2_title.setText(list.get(1).getName());

                tv_topic1_detail.setText(list.get(0).getDescription());
                tv_topic2_detail.setText(list.get(1).getDescription());
                break;
            case 3:
                ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(list.get(0).getPic()),R.mipmap.icon_default_215_215, iv_1,2,true);
                ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(list.get(1).getPic()), R.mipmap.icon_default_215_215,iv_2,2,true);
                ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(list.get(2).getPic()), R.mipmap.icon_default_215_215,iv_3,2,true);

                tv_topic1_title.setText(list.get(0).getName());
                tv_topic2_title.setText(list.get(1).getName());
                tv_topic3_title.setText(list.get(2).getName());

                tv_topic1_detail.setText(list.get(0).getDescription());
                tv_topic2_detail.setText(list.get(1).getDescription());
                tv_topic3_detail.setText(list.get(2).getDescription());
                break;
            case 4:
            default:
                ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(list.get(0).getPic()), R.mipmap.icon_default_215_215,iv_1,2,true);
                ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(list.get(1).getPic()), R.mipmap.icon_default_215_215,iv_2,2,true);
                ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(list.get(2).getPic()), R.mipmap.icon_default_215_215,iv_3,2,true);
                ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(list.get(3).getPic()), R.mipmap.icon_default_215_215,iv_4,2,true);

                tv_topic1_title.setText(list.get(0).getName());
                tv_topic2_title.setText(list.get(1).getName());
                tv_topic3_title.setText(list.get(2).getName());
                tv_topic4_title.setText(list.get(3).getName());

                tv_topic1_detail.setText(list.get(0).getDescription());
                tv_topic2_detail.setText(list.get(1).getDescription());
                tv_topic3_detail.setText(list.get(2).getDescription());
                tv_topic4_detail.setText(list.get(3).getDescription());
                break;
        }
    }
}
