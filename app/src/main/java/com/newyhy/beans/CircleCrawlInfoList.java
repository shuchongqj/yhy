package com.newyhy.beans;

import java.io.Serializable;
import java.util.List;

/**
 * 圈子 推荐，搜索，动态标签页 列表
 * Created by Jiervs on 2018/6/29.
 */

public class CircleCrawlInfoList implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    public int pageNo;

    /**
     * 是否有下一页
     */
    public boolean hasNext;

    /**
     * UGC信息列表
     */
    public List<CircleCrawlInfo> list;

}

