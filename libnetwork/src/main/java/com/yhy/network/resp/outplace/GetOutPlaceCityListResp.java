package com.yhy.network.resp.outplace;

import java.util.List;

/**
 * Created by yangboxue on 2018/7/10.
 */

public class GetOutPlaceCityListResp {

    List<OutPlaceCity> cityList;

    public List<OutPlaceCity> getCityList() {
        return cityList;
    }

    public void setCityList(List<OutPlaceCity> cityList) {
        this.cityList = cityList;
    }

    public static class OutPlaceCity {
        public long id;                 // 城市id
        public String cityCode;         // 城市标准码
        public String name;             // 名称
        public String cityDesc;         // 城市描述
        public double lat;              // 纬度
        public double lng;              // 经度
        public int isActive;           // 是否开通业务
        public int isPublic;           // 是否公开
        public int isEnable;           // 是否可用
        public int priority;            // 优先级
        public String simpleCode;       // 城市二字码
        public boolean isTitle;         // 是标题
//        public String provinceCode;     // 省份标准码
        //        List<Api_OUTPLACE_District>	districtList	// 区域列表
    }
}
