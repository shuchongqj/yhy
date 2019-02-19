package com.newyhy.contract.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import com.newyhy.contract.CircleRecommendContract;
import com.quanyan.yhy.R;
import com.yhy.network.YhyCallback;
import com.yhy.network.YhyCode;
import com.yhy.network.api.snscenter.SnsCenterApi;
import com.yhy.network.req.snscenter.GetRecommendPageListReq;
import com.yhy.network.resp.Response;
import com.yhy.network.resp.snscenter.GetRecommendPageListResp;



/**
 * CircleRecommendPresenter
 * Created by Jiervs on 2018/6/21.
 */

public class CircleRecommendPresenter implements CircleRecommendContract.Presenter{

    private Context context;
    private CircleRecommendContract.View view;

    public CircleRecommendPresenter(Context context,@NonNull CircleRecommendContract.View recommendView) {
        this.context = context;
        this.view = recommendView;
        recommendView.setPresenter(this);
    }

    @Override
    public void process() {

    }

    @Override
    public void release() {
        context = null;
        view = null;
    }

    @Override
    public void getNetData(GetRecommendPageListReq.Companion.P p) {

        YhyCallback<Response<GetRecommendPageListResp>> callback = new YhyCallback<Response<GetRecommendPageListResp>>() {
            @Override
            public void onSuccess(Response<GetRecommendPageListResp> data) {
                if (view != null) {
                    view.showData(data.getContent());
                }
            }

            @Override
            public void onFail(YhyCode code, Exception exception) {
                if (view != null) view.showErrorView(R.mipmap.default_page_lists,context.getString(R.string.circle_no_data),"");
            }
        };

        new SnsCenterApi().getRecommendPageList(new GetRecommendPageListReq(p), callback).execAsync();
    }

    @Override
    public void getCacheData() {

    }


}
