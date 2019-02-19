package com.newyhy.beans;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * 圈子直播，秀场，标准视频列表 Bean
 * Created by Jiervs on 2018/6/26.
 */

public class CircleLiveList implements Serializable{

    private static final long serialVersionUID = 1L;

    public String traceId;
    public boolean hasNext;
    public ArrayList<CircleLiveInfoResult> list;

}
