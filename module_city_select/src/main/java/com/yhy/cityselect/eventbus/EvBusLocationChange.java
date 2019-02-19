package com.yhy.cityselect.eventbus;

import com.yhy.network.resp.outplace.GetOutPlaceCityListResp;

/**
 * Created by yangboxue on 2018/3/2.
 */

public class EvBusLocationChange {

    public GetOutPlaceCityListResp.OutPlaceCity outPlaceCity;
    public boolean isFromWeb;

    public EvBusLocationChange(GetOutPlaceCityListResp.OutPlaceCity outPlaceCity, boolean isFromWeb) {
        this.outPlaceCity = outPlaceCity;
        this.isFromWeb = isFromWeb;
    }
}
