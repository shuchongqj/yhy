package com.newyhy.fragment.live;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.newyhy.activity.HorizontalReplayActivity;
import com.newyhy.adapter.live.UserAllVideosAdapter;
import com.newyhy.views.RoundImageView;
import com.quanyan.yhy.R;
import com.smart.sdk.api.resp.Api_SNSCENTER_LiveRecordDTO;
import com.smart.sdk.api.resp.Api_SNSCENTER_SnsReplayListResult;
import com.videolibrary.controller.LiveController;
import com.videolibrary.utils.IntentUtil;
import com.yhy.common.base.BaseNewFragment;
import com.yhy.common.utils.CommonUtil;
import com.yhy.common.utils.SPUtils;
import com.yhy.imageloader.ImageLoadManager;
import com.yhy.network.YhyCallback;
import com.yhy.network.YhyCode;
import com.yhy.network.api.snscenter.SnsCenterApi;
import com.yhy.network.req.snscenter.GetUserReplayReq;
import com.yhy.network.req.snscenter.GetUserSuperbListReq;
import com.yhy.network.resp.Response;
import com.yhy.network.resp.snscenter.GetReplayByUserIdResp;
import com.yhy.network.resp.snscenter.GetUserSuperbListResp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.videolibrary.controller.LiveController.MSG_LIVE_LIST_OK;

public class AnchorInfoFragment extends BaseNewFragment {

    private View view;
    private RecyclerView rv_video;
    private UserAllVideosAdapter adapter;
    private int pageIndex = 1;
    private List<GetUserSuperbListResp.LiveRecordResult> list = new ArrayList<>();
    private long mAnchorUserId;
    private List<String> notInLivesId = new ArrayList<>();

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_anchor_info;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        rv_video = view.findViewById(R.id.rv_video);
        adapter = new UserAllVideosAdapter(R.layout.recommend_video_item,list);
        adapter.setOnLoadMoreListener(() -> {
            pageIndex++;
            fetchReplayLive(new GetUserReplayReq.Companion.P(UUID.randomUUID().toString(),mAnchorUserId
                    ,pageIndex,8,notInLivesId));
        }, rv_video);
        rv_video.setLayoutManager(new GridLayoutManager(getContext(), 2));
        int marginTop = getResources().getDimensionPixelSize(R.dimen.yhy_size_19px);
        int marginLeft = getResources().getDimensionPixelSize(R.dimen.yhy_size_16px);
        int marginRight = getResources().getDimensionPixelSize(R.dimen.yhy_size_5px);
        rv_video.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (position % 2 == 0){
                    outRect.right = marginRight;
                    outRect.left = marginLeft;
                }else {
                    outRect.left = marginRight;
                    outRect.right = marginLeft;
                }
                outRect.top = marginTop;
            }
        });
        rv_video.setAdapter(adapter);
        if (mAnchorUserId > 0) {
            fetchReplayLive(new GetUserReplayReq.Companion.P(UUID.randomUUID().toString(), mAnchorUserId
                    , 1, 4,notInLivesId));
        }
    }

    public void setReplayData(long mAnchorUserId,long mLiveId) {
        this.mAnchorUserId = mAnchorUserId;
        fetchReplayLive(new GetUserReplayReq.Companion.P(UUID.randomUUID().toString(),mAnchorUserId
                ,1,4,notInLivesId));
    }

    /**
     * 查询主播回放
     */
    private void fetchReplayLive(GetUserReplayReq.Companion.P p) {
        //LiveController.getInstance().getLiveListByUserId(getContext(),mHandler,mAnchorUserId,pageIndex,4);
        YhyCallback<Response<GetReplayByUserIdResp>> yhyCallback = new YhyCallback<Response<GetReplayByUserIdResp>>() {
            @Override
            public void onSuccess(Response<GetReplayByUserIdResp> data) {
                if (data == null) return;
                if (data.getContent().list == null) return;
                if (adapter == null) return;
                list.addAll(data.getContent().list);
                adapter.notifyDataSetChanged();
                if (data.getContent().hasNext) {
                    adapter.loadMoreComplete();
                } else {
                    adapter.loadMoreEnd(true);
                }
            }

            @Override
            public void onFail(YhyCode code, Exception exception) {

            }
        };
        new SnsCenterApi().getUserReplayList(new GetUserReplayReq(p),yhyCallback).execAsync();
    }

    @Override
    public void handleMessage(Message msg) {

    }
}
