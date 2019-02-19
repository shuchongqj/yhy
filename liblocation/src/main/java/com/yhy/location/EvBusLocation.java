package com.yhy.location;

/**
 * Created by yangboxue on 2018/7/17.
 */

public class EvBusLocation {
    //type==1001代表定位成功    type==4001 代表定位失败（包含KEY错误或网络问题导致定位的失败）
    private int type;

    public EvBusLocation(int type) {
        this.type = type;
    }

    public int getLocationType() {
        return type;
    }
}
