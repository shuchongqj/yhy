package com.yhy.cityselect;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.yhy.cityselect.cache.CityCache;
import com.yhy.cityselect.entity.CityIndexBean;
import com.yhy.cityselect.entity.CityListBean;
import com.yhy.cityselect.eventbus.EvBusCityGet;
import com.yhy.common.base.ModuleApplication;
import com.yhy.common.base.YHYBaseApplication;
import com.yhy.common.utils.JSONUtils;
import com.yhy.network.YhyCallback;
import com.yhy.network.YhyCode;
import com.yhy.network.api.outplace.OutPlaceApi;
import com.yhy.network.req.outplace.GetOutPlaceCityListReq;
import com.yhy.network.resp.Response;
import com.yhy.network.resp.outplace.GetOutPlaceCityListResp;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by yangboxue on 2018/7/27.
 */

public class CitySelectModuleApplication implements ModuleApplication {
    private YHYBaseApplication context;

    @Override
    public void onCreate(YHYBaseApplication application) {
        context = application;
        /**
         * 获取第三方场馆所有城市列表
         */
        getOutplaceCityList(application);
    }

    /**
     * 获取第三方场馆所有城市列表
     *
     * @param application
     */
    public void getOutplaceCityList(final YHYBaseApplication application) {
        YhyCallback<Response<GetOutPlaceCityListResp>> callback = new YhyCallback<Response<GetOutPlaceCityListResp>>() {
            @Override
            public void onSuccess(Response<GetOutPlaceCityListResp> data) {
                initCityList(application, data.getContent().getCityList());
                EventBus.getDefault().post(new EvBusCityGet());
            }

            @Override
            public void onFail(YhyCode code, Exception exception) {
                String jsonCity = CityCache.getOutPlaceCityList(application);
                if (TextUtils.isEmpty(jsonCity)) {
                    handler.postDelayed(mRunnable, 3000);

                }
            }
        };
        new OutPlaceApi().
                getOutPlaceCityList(new GetOutPlaceCityListReq(), callback).
                execAsync();
    }

    /**
     * 处理第三方场馆城市列表数据
     *
     * @param dataList
     */
    private void initCityList(YHYBaseApplication application, List<GetOutPlaceCityListResp.OutPlaceCity> dataList) {
        CityCache.saveOutPlaceCityListOrigin(application, JSONUtils.toJson(dataList));

        String[] indexs = new String[]{"热门", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",};
        // 总的城市列表
        ArrayList<CityListBean> cityList = new ArrayList<>();
        // 热门城市列表
        ArrayList<GetOutPlaceCityListResp.OutPlaceCity> hotCityList = new ArrayList<>();
        // 索引
        ArrayList<CityIndexBean> indexList = new ArrayList<>();
        for (String index : indexs) {
            // add index
            CityIndexBean cityIndexBean = new CityIndexBean();
            cityIndexBean.name = index;
            indexList.add(cityIndexBean);
            // add city
            if ("热门".equals(index)) {
                for (GetOutPlaceCityListResp.OutPlaceCity outPlaceCity : dataList) {
                    if (outPlaceCity.isPublic == 1) {
                        hotCityList.add(outPlaceCity);
                    }
                }
            } else {
                CityListBean cityListBean = new CityListBean();
                cityListBean.index = index;

                ArrayList<GetOutPlaceCityListResp.OutPlaceCity> childCityList = new ArrayList<>();
                for (GetOutPlaceCityListResp.OutPlaceCity outPlaceCity : dataList) {
                    if (TextUtils.isEmpty(outPlaceCity.simpleCode)) {

                    } else {
                        String simpleCode = outPlaceCity.simpleCode.toUpperCase();
                        if (simpleCode.startsWith(index)) {
                            childCityList.add(outPlaceCity);
                        }
                    }
                }

                cityListBean.values = childCityList;

                cityList.add(cityListBean);
            }
        }

        CityCache.saveOutPlaceCityList(application, JSONUtils.toJson(cityList));
        CityCache.saveCityListIndex(application, JSONUtils.toJson(indexList));
        CityCache.saveHotCityList(application, JSONUtils.toJson(hotCityList));

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            getOutplaceCityList(context);

        }
    };
}
