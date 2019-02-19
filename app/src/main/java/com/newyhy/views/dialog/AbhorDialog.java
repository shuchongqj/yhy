package com.newyhy.views.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.quanyan.yhy.R;
import com.smart.sdk.api.resp.Api_SNSCENTER_TagInfo;
import com.yhy.common.eventbus.event.EvBusDisLike;
import com.yhy.common.utils.AndroidUtils;
import com.yhy.network.YhyCallback;
import com.yhy.network.YhyCode;
import com.yhy.network.api.snscenter.SnsCenterApi;
import com.yhy.network.req.snscenter.DislikeReq;
import com.yhy.network.req.snscenter.DislikeVideoReq;
import com.yhy.network.resp.Response;
import com.yhy.network.resp.snscenter.DislikeResp;
import com.yhy.network.resp.snscenter.DislikeVideoResp;
import com.yhy.network.resp.snscenter.GetRecommendPageListResp;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 圈子推荐不感兴趣的 dialog
 * Created by Jiervs on 2018/6/23.
 */

public class AbhorDialog extends Dialog {

    private TextView tv_abhor;
    private TextView tv_count;
    private RecyclerView rv_reason;
    private List<GetRecommendPageListResp.RecommendResult.TagInfo> list = new ArrayList<>();
    private GridLayoutManager manager;
    private ReasonAdapter adapter;
    private ImageView up;
    private ImageView down;
    public int direction = 1;
    public int anchorY;
    public int anchorX;
    /**************************************************************        data      ********************************************************************************/
    public boolean isArticle = true;//isArticle: true:不喜欢文章 false:不喜欢视频
    public long videoId;//视频 id
    public String traceId;
    public String	id;	// 文章id
    public long	ugcId;	// 动态id
    public long	authorId;	// 作者id
    public List<Api_SNSCENTER_TagInfo>	tagInfoList = new ArrayList<>();	// TagInfo

    public AbhorDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    public AbhorDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    protected AbhorDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    protected void init(Context context){
        manager = new GridLayoutManager(context,2, LinearLayoutManager.VERTICAL,false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.circle_abhor_dialog);
        tv_abhor = findViewById(R.id.tv_abhor);
        tv_count = findViewById(R.id.tv_count);
        rv_reason = findViewById(R.id.rv_reason);
        up = findViewById(R.id.up_arrow);
        down = findViewById(R.id.down_arrow);
        adapter = new ReasonAdapter(list);
        rv_reason.setLayoutManager(manager);
        rv_reason.setAdapter(adapter);
        tv_abhor.setOnClickListener(v -> {
            abhor();
            AbhorDialog.this.dismiss();
        });

        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(null);
        }

        // 设置对话框大小
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        //适配屏幕宽度
        int d = AndroidUtils.getScreenWidth(getContext());
        params.width = (int) (d * 0.94f);
        int halfDeleteWidth = (int) getContext().getResources().getDimension(R.dimen.yhy_size_20px);
        //确定dialog位置 , DOWN 显示在屏幕下半方，UP显示在屏幕上半方,X位置来确定小箭头的margin
        if (direction == DialogLocationUtils.DOWN) {
            window.setGravity(Gravity.TOP);
            params.y = anchorY;
            down.setVisibility(View.GONE);
            RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) up.getLayoutParams();
            p.setMargins(0,0,params.width - anchorX - halfDeleteWidth,0);
            up.setLayoutParams(p);
        } else {
            window.setGravity(Gravity.BOTTOM);
            params.y = AndroidUtils.getScreenHeight(getContext()) - anchorY;
            up.setVisibility(View.GONE);
            RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) down.getLayoutParams();
            p.setMargins(0,0,params.width - anchorX - halfDeleteWidth,0);
            down.setLayoutParams(p);
        }
        window.setAttributes(params);
    }

    public void setAnchor(int anchorX,int anchorY){
        this.anchorX = anchorX;
        this.anchorY = anchorY;
    }

    private void abhor() {
        if (isArticle) {
            EventBus.getDefault().post(new EvBusDisLike(ugcId,isArticle));
            List<DislikeReq.Companion.TagInfo> tagInfoList = new ArrayList<>();
            for (GetRecommendPageListResp.RecommendResult.TagInfo tagInfo : adapter.mList) {
                if (tagInfo.isSelected) {
                    DislikeReq.Companion.TagInfo info = new DislikeReq.Companion.TagInfo(tagInfo.id,tagInfo.name,tagInfo.type);
                    tagInfoList.add(info);
                }
            }
            DislikeReq.Companion.P p = new DislikeReq.Companion.P(traceId,id,ugcId,String.valueOf(authorId),tagInfoList);
            dislike(p);
        } else {
            EventBus.getDefault().post(new EvBusDisLike(videoId,isArticle));
            dislikeVideo(String.valueOf(videoId));
        }
    }

    public void setReasonAndInfo(List<GetRecommendPageListResp.RecommendResult.TagInfo> list,String uuid,String id,long ugcId,long authorId) {
        this.list.clear();
        this.list.addAll(list);
        if (adapter != null) adapter.notifyDataSetChanged();

        tagInfoList.clear();
        for (GetRecommendPageListResp.RecommendResult.TagInfo tag: list) {
            Api_SNSCENTER_TagInfo tagInfo = new Api_SNSCENTER_TagInfo();
            tagInfo.id = tag.id;
            tagInfo.name = tag.name;
            tagInfo.type = tag.type;
            tagInfoList.add(tagInfo);
        }
        traceId = uuid;
        this.id = id;
        this.ugcId = ugcId;
        this.authorId = authorId;

    }

    /**************************************************************        Api      ********************************************************************************/

    private void dislike(DislikeReq.Companion.P p) {
        YhyCallback<Response<DislikeResp>> callback = new YhyCallback<Response<DislikeResp>>() {
            @Override
            public void onSuccess(Response<DislikeResp> data) {

            }

            @Override
            public void onFail(YhyCode code, Exception exception) {

            }
        };
        new SnsCenterApi().dislike(new DislikeReq(p), callback).execAsync();
    }

    private void dislikeVideo(String videoId){
        YhyCallback<Response<DislikeVideoResp>> callback = new YhyCallback<Response<DislikeVideoResp>>() {
            @Override
            public void onSuccess(Response<DislikeVideoResp> data) {

            }

            @Override
            public void onFail(YhyCode code, Exception exception) {

            }
        };
        new SnsCenterApi().dislikeVideo(new DislikeVideoReq(videoId), callback).execAsync();
    }

    /**************************************************************        Adapter       ****************************************************************************/

    public class ReasonAdapter extends BaseQuickAdapter<GetRecommendPageListResp.RecommendResult.TagInfo,BaseViewHolder> {

        public List<GetRecommendPageListResp.RecommendResult.TagInfo> mList;
        private TextView textView;
        private ConstraintLayout bg;

        public ReasonAdapter(@Nullable List<GetRecommendPageListResp.RecommendResult.TagInfo> data) {
            super(R.layout.circle_abhor_reason_item,data);
            mList = data;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void convert(BaseViewHolder holder, GetRecommendPageListResp.RecommendResult.TagInfo item) {
            textView = holder.getView(R.id.tv_reason);
            bg = holder.getView(R.id.bg);
            if (item.type == 1) {
                textView.setText("不想看" + item.name);
            } else {
                textView.setText(item.name);
            }
            textView.setSelected(item.isSelected);
            bg.setSelected(item.isSelected);
            bg.setOnClickListener(v -> {
                item.isSelected = !item.isSelected;
                notifyItemChanged(holder.getAdapterPosition());
                int count = 0;
                for (GetRecommendPageListResp.RecommendResult.TagInfo tag : mList) {
                    if (tag.isSelected) count++;
                }
                if (count >0) {
                    tv_count.setText("已选" + count +"条理由");
                    tv_abhor.setText("确定");
                } else {
                    tv_count.setText("可选理由，精准屏蔽");
                    tv_abhor.setText("不感兴趣");
                }
            });
        }
    }
}
