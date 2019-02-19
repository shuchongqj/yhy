package com.quanyan.yhy.eventbus;

/**
 * Created by Administrator on 2018/2/7.
 */

public class EvBusMessageCount {
    private int count;
    public EvBusMessageCount(int count)
    {
        this.count=count;
    }
    public int getCount(){
        return count;
    }
}
