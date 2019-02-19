package com.newyhy.contract.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.newyhy.beans.CircleCrawlInfoList;
import com.newyhy.contract.CircleSearchContract;
import com.quanyan.yhy.R;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.yhy.common.utils.SPUtils;
import com.yhy.network.YhyCallback;
import com.yhy.network.YhyCaller;
import com.yhy.network.YhyCode;
import com.yhy.network.api.snscenter.SnsCenterApi;
import com.yhy.network.req.snscenter.GetRecommendPageListReq;
import com.yhy.network.req.snscenter.SearchPageListReq;
import com.yhy.network.resp.Response;
import com.yhy.network.resp.snscenter.GetRecommendPageListResp;
import com.yhy.network.resp.snscenter.SearchPageListResp;

/**
 * CircleSearchPresenter
 * Created by Jiervs on 2018/6/21.
 */

public class CircleSearchPresenter implements CircleSearchContract.Presenter{

    private Context context;
    private CircleSearchContract.View view;
    private YhyCaller searchCaller;

    public CircleSearchPresenter(Context context, @NonNull CircleSearchContract.View searchView) {
        this.context = context;
        this.view = searchView;
        searchView.setPresenter(this);
    }

    @Override
    public void process() {

    }

    @Override
    public void release() {
        if (searchCaller != null){
            searchCaller.cancel();
            searchCaller = null;
        }
        context = null;
        view = null;
    }

    @Override
    public void getNetData(String traceId,String title,int pageNo,int pageSize) {

        if (searchCaller != null){
            searchCaller.cancel();
        }
        YhyCallback<Response<SearchPageListResp>> callback = new YhyCallback<Response<SearchPageListResp>>() {
            @Override
            public void onSuccess(Response<SearchPageListResp> data) {
                if (view != null) {
                    view.showData(data.getContent());
                }
            }

            @Override
            public void onFail(YhyCode code, Exception exception) {
                if (view != null) view.showErrorView(R.mipmap.default_page_search,context.getString(R.string.circle_no_data),"");
            }
        };

        SearchPageListReq.Companion.S s = new SearchPageListReq.Companion.S(traceId,title,pageNo,pageSize);
        searchCaller = new SnsCenterApi().searchPageList(new SearchPageListReq(s), callback).execAsync();
    }

    @Override
    public void getCacheData() {

    }
}
