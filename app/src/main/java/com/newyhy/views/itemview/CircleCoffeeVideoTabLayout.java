package com.newyhy.views.itemview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newyhy.config.CircleBizType;
import com.quanyan.yhy.R;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.videolibrary.utils.IntentUtil;
import com.yhy.common.beans.net.model.discover.UgcInfoResult;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.location.LocationManager;
import com.yhy.network.YhyCallback;
import com.yhy.network.YhyCode;
import com.yhy.network.api.activitycenter.ActivityCenterApi;
import com.yhy.network.req.activitycenter.PlayReq;
import com.yhy.network.resp.Response;
import com.yhy.network.resp.activitycenter.PlayResp;

import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * CircleShowTimeLayout 圈子中小视频 Item 的布局
 * Created by Jiervs on 2018/6/20.
 */

public class CircleCoffeeVideoTabLayout extends LinearLayout{

    private TextView tv_title;
    private TextView tv_origin;
    private ImageView iv_cover;
    private TextView live_state;
    private TextView tv_saw_number;

    //data
    private long id;
    private String title;
    private String origin;
    private String videoPicUrl;
    private int liveStatus;
    private int saw_number;
    private long liveId;
    private long roomId;
    private long anchorId;
    private int videoScreenType;
    private long ugcId;
    private String videoUrl;

    //recycler的位置
    public int position;
    public HashMap<String, String> extraMap = new HashMap<>();

    public CircleCoffeeVideoTabLayout(Context context) {
        super(context);
        initViews(context);
    }

    public CircleCoffeeVideoTabLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public CircleCoffeeVideoTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    public void initViews(Context context){
        LayoutInflater.from(context).inflate(R.layout.circle_coffee_tab_layout,this);
        tv_title = findViewById(R.id.tv_title);
        tv_origin = findViewById(R.id.tv_origin);
        iv_cover = findViewById(R.id.iv_cover);
        live_state = findViewById(R.id.live_state);
        tv_saw_number = findViewById(R.id.tv_saw_number);
    }

    //转换数据t
    public <T> void covertData(T t){

        if (t instanceof UgcInfoResult) {//UgcInfoResult类型
            UgcInfoResult data = (UgcInfoResult) t;
            if (null != data.userInfo) {
                origin = data.userInfo.nickname;
                anchorId = data.userInfo.userId;
            }
            id = data.id;
            title = data.textContent;
            saw_number = (int) data.viewNum;
            liveStatus = data.liveStatus;
            videoPicUrl = data.videoPicUrl;
            videoUrl = data.videoUrl;
            ugcId = data.id;
            liveId = data.liveId;
            if ("HORIZONTAL".equals(data.liveScreenType)) {
                videoScreenType = 0;
            } else {
                videoScreenType = 1;
            }
        }
    }

    public <T> void setData(Context context,T data) {
        covertData(data);

        //标题
        tv_title.setText(title);
        //发布来源
        tv_origin.setText(origin);

        //live state
        if (liveStatus == 1) {
            live_state.setText(R.string.live_state_1);
            live_state.setBackgroundResource(R.drawable.circle_showtime_bg_living);
        }else {
            live_state.setText(R.string.live_state_2);
            live_state.setBackgroundResource(R.drawable.circle_showtime_bg_record);
        }

        //观看或在线人数
        String num = null;
        if (saw_number >= 9990000) {
            (tv_saw_number).setText("999+万");
        } else if (saw_number >= 10000){
            num = (new DecimalFormat("#.##").format(saw_number / 10000.0f));
            (tv_saw_number).setText(num + "万");
        } else {
            (tv_saw_number).setText(saw_number + "");
        }

        //Cover
        if (videoPicUrl != null && !videoPicUrl.equals(iv_cover.getTag(R.id.iv_cover))) {
            iv_cover.setTag(R.id.iv_cover,videoPicUrl);
            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(videoPicUrl),R.mipmap.icon_default_215_260,iv_cover);
        }

        /***********************************************************    Logic Method   *****************************************************************/
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 埋点
                if (extraMap != null) {
                    Analysis.pushEvent(context, AnEvent.ListSvideoClick,
                            new Analysis.Builder().
                                    setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                    setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                    setId(String.valueOf(ugcId)).
                                    setTab(extraMap.get(Analysis.TAB)).
                                    setPosition(position));

                    Analysis.pushEvent(context, AnEvent.PageSvideoOpen,
                            new Analysis.Builder().
                                    setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                    setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                    setId(String.valueOf(ugcId)).
                                    setLastPage(extraMap.get(Analysis.TAG)));
                }
                doPlay();
                IntentUtil.startVideoClientActivity(liveId, anchorId, liveStatus == 1, videoScreenType, videoUrl, ugcId);
            }
        });
    }

    /**
     * 点击事件上传服务器（获取积分）
     */
    public void doPlay() {
        YhyCallback<Response<PlayResp>> callback = new YhyCallback<Response<PlayResp>>() {
            @Override
            public void onSuccess(Response<PlayResp> data) {
            }

            @Override
            public void onFail(YhyCode code, Exception exception) {

            }
        };
        new ActivityCenterApi().play(new PlayReq(new PlayReq.Companion.P(ugcId, CircleBizType.SNS_VEDIO_SHORT)), callback).execAsync();
    }
}
