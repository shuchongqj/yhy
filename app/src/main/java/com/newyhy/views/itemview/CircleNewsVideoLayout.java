package com.newyhy.views.itemview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newyhy.config.CircleBizType;
import com.newyhy.views.KeyTextView;
import com.newyhy.views.dialog.AbhorDialog;
import com.newyhy.views.dialog.DialogLocationUtils;
import com.quanyan.yhy.R;
import com.quanyan.yhy.libanalysis.AnEvent;
import com.quanyan.yhy.libanalysis.Analysis;
import com.videolibrary.utils.IntentUtil;
import com.yhy.common.utils.CommonUtil;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.location.LocationManager;
import com.yhy.network.YhyCallback;
import com.yhy.network.YhyCode;
import com.yhy.network.api.activitycenter.ActivityCenterApi;
import com.yhy.network.api.outplace.OutPlaceApi;
import com.yhy.network.req.activitycenter.PlayReq;
import com.yhy.network.req.outplace.GetOutPlaceCityListReq;
import com.yhy.network.resp.Response;
import com.yhy.network.resp.activitycenter.PlayResp;
import com.yhy.network.resp.outplace.GetOutPlaceCityListResp;
import com.yhy.network.resp.snscenter.GetRecommendPageListResp;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * CircleNewsVideoLayout
 * Created by yangboxue on 2018/7/3.
 */

public class CircleNewsVideoLayout extends LinearLayout {

    private KeyTextView tv_title;
    private TextView tv_origin;
    private TextView tv_commentNum;
    private ImageView iv_delete;
    private ImageView iv_cover;

    //data
    public long id;    // 文章id
    public String title;    // 标题
    public int top;    // 1:置顶，2:热门
    public List<String> picList;    // 图片集合
    public List<GetRecommendPageListResp.RecommendResult.TagInfo> tagInfoList;    // 标签信息列表
    public String source;    // 来源
    public int canComment;    // 是否可以评论，1:是，2:否
    public int commentNum;    // 评论数
    public int recommendType;    // 类型：1文章，2视频
    public long publishDate;    // 发布时间
    public long ugcId;    // 动态id
    public long authorId;    // 作者
    public long videoId;    // 视频id，推荐视频使用
    public String videoUrl;    // 视频URL
    public String videoPicUrl;    // 视频缩略图URL
    public String liveScreenType;    // 直播横竖屏类型,横屏:HORIZONTAL, 竖屏:VERTICAL
    public int type;    // 关注类型 0:未关注 1:单向关注 2:双向关注
    public int viewCount;    // 观看次数
    public String authorName;    // 作者名字

    //searchKey
    public String searchKey;

    //recycler的位置
    public int position;
    public HashMap<String, String> extraMap = new HashMap<>();

    public CircleNewsVideoLayout(Context context) {
        super(context);
        initViews(context);
    }

    public CircleNewsVideoLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public CircleNewsVideoLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    public void initViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.circle_news_video_layout, this);
        tv_title = findViewById(R.id.tv_title);
        tv_origin = findViewById(R.id.tv_origin);
        tv_commentNum = findViewById(R.id.tv_comment_num);
        iv_cover = findViewById(R.id.iv_video_cover);
        iv_delete = findViewById(R.id.iv_delete);
    }

    //转换数据t
    public <T> void covertData(T t) {
        if (t instanceof GetRecommendPageListResp.RecommendResult) {
            GetRecommendPageListResp.RecommendResult data = (GetRecommendPageListResp.RecommendResult) t;
            id = data.id; // 文章id
            title = data.title;    // 推荐标题
            top = data.top;    // 1:置顶，2:热门
            source = data.source;    // 来源
            canComment = data.canComment;    // 是否可以评论，1:是，2:否
            commentNum = data.commentNum;    // 评论数
            videoUrl = data.videoUrl;    // 视频URL
            videoPicUrl = data.videoPicUrl;    // 视频缩略图URL
            recommendType = data.recommendType;    // 类型：1文章，2视频
            publishDate = data.publishDate;    // 发布时间
            liveScreenType = data.liveScreenType;    // 默认横屏0 竖屏1
            ugcId = data.ugcId;    // 动态id
            authorId = data.authorId;    // 作者id
            picList = data.picList;    // 图片集合
            tagInfoList = data.tagInfoList;    // 标签信息列表
            videoId = data.videoId;
        }
    }

    public <T> void setData(Context context, T data) {
        covertData(data);
        if (searchKey != null && searchKey.length() > 0) {
            tv_title.setKeyTextsColor(title, searchKey, R.color.red_ying);
            iv_delete.setVisibility(INVISIBLE);
        } else {
            tv_title.setText(title);
        }
        tv_origin.setText(authorName);
        //评论人数
        String num = null;
        if (commentNum >= 9990000) {
            (tv_commentNum).setText("999+万评论");
        } else if (commentNum >= 10000) {
            num = (new DecimalFormat("#.##").format(commentNum / 10000.0f));
            (tv_commentNum).setText(num + "万评论");
        } else {
            (tv_commentNum).setText(commentNum + "评论");
        }
        //image
        if (!TextUtils.isEmpty(videoPicUrl) && !videoPicUrl.equals(iv_cover.getTag())) {
            iv_cover.setTag(R.id.iv_cover, videoPicUrl);
            ImageLoadManager.loadImage(CommonUtil.getImageFullUrl(videoPicUrl), R.mipmap.icon_default_310_180, iv_cover, 3);
        }

        /****************************************************************      Logic Method      ******************************************************************/
        iv_delete.setOnClickListener(v -> {
            int[] location = new int[2];
            iv_delete.getLocationOnScreen(location);
            int x = location[0];//获取当前位置的横坐标
            int y = location[1];//获取当前位置的纵坐标
            AbhorDialog dialog = new AbhorDialog(context);
            dialog.isArticle = false;
            dialog.videoId = videoId;
            dialog.setReasonAndInfo(tagInfoList, UUID.randomUUID().toString(), String.valueOf(id), ugcId, authorId);
            dialog.setAnchor(x, y);
            dialog.direction = DialogLocationUtils.showLocation(context, y);
            if (tagInfoList != null && tagInfoList.size() > 0)
                for (GetRecommendPageListResp.RecommendResult.TagInfo tag : tagInfoList) {
                    tag.isSelected = false;
                }
            dialog.show();
        });

        this.setOnClickListener(v -> {
            if (TextUtils.isEmpty(searchKey)) {
                // 埋点
                if (extraMap != null) {
                    Analysis.pushEvent(context, AnEvent.ListArticleclick,
                            new Analysis.Builder().
                                    setLat(LocationManager.getInstance().getStorage().getLast_lat()).
                                    setLng(LocationManager.getInstance().getStorage().getLast_lng()).
                                    setId(String.valueOf(id)).
                                    setTab(extraMap.get(Analysis.TAB)).
                                    setPosition(position));
                }
                doPlay();
            }
            if ("HORIZONTAL".equals(liveScreenType)) {
                IntentUtil.startVideoClientActivity(videoId, authorId, false, 0);
            } else {
                IntentUtil.startVideoClientActivity(videoId, authorId, false, 1);
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
        new ActivityCenterApi().play(new PlayReq(new PlayReq.Companion.P(videoId, CircleBizType.SNS_ARTICLE)), callback).execAsync();
    }
}
