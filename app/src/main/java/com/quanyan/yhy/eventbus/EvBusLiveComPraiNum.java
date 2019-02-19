package com.quanyan.yhy.eventbus;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:直播详情中通知改变评论，点赞数量的变化（type：1  评论列表， 2  点赞列表）
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:1/29/16
 * Time:09:29
 * Version 1.0
 */
public class EvBusLiveComPraiNum {

    private int num;
    /**
     *  1  评论列表， 2  点赞列表
     */
    private int type;

    public EvBusLiveComPraiNum(int num, int type){
        this.num = num;
        this.type = type;
    }

    public int getNum(){
        return this.num;
    }

    public int getType(){
        return this.type;
    }
}
