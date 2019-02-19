package com.yhy.cityselect.entity;

import com.yhy.network.resp.outplace.GetOutPlaceCityListResp;

import java.util.ArrayList;

/**
 * Created by yangboxue on 2018/7/12.
 */

public class CityListBean {
    public String index;
    public ArrayList<GetOutPlaceCityListResp.OutPlaceCity> values;
}
